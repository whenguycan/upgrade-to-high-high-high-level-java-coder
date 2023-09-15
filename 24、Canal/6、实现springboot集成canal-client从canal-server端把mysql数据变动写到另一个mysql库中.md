## 实现springboot集成canal-client从canal-server端把mysql数据变动写到另一个mysql库中



#### 操作步骤如下：

- 本地创建一个普通的springboot项目，canal.client的1.1.4版本完全兼容java17和springboot3.x

- 成功集成mysql + mybatis-plus

- 引入依赖

  ```xml
  <dependency>
    <groupId>com.alibaba.otter</groupId>
    <artifactId>canal.client</artifactId>
    <version>1.1.4</version>
  </dependency>
  ```

- 编写如下类，去canal的服务端获取mysql的数据变动信息

  ```java
  import com.alibaba.otter.canal.client.CanalConnector;
  import com.alibaba.otter.canal.client.CanalConnectors;
  import com.alibaba.otter.canal.protocol.CanalEntry;
  import com.alibaba.otter.canal.protocol.Message;
  import com.google.protobuf.ByteString;
  import com.google.protobuf.InvalidProtocolBufferException;
  import org.springframework.stereotype.Component;
  
  import java.net.InetSocketAddress;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;
  
  /**
   * @Auther: tangwei
   * @Date: 2023/8/23 2:14 PM
   * @Description: 类描述信息
   */
  @Component
  public class MyCanalClient {
  
      public void run() throws InvalidProtocolBufferException {
          // 创建链接
          CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("10.10.210.24",
                  11111), "test", "", "");
          int batchSize = 1000;
          try {
              connector.connect();
              connector.subscribe("saas-fast-parking.t_tenant,saas-fast-parking.sys_dept");//精确订阅saas-fast-parking.t_tenant,saas-fast-parking.sys_dept 这2张表，也就是这2张表有变动了才会被这儿获取，别的表的变动这儿不做处理
              connector.rollback();
              while (true) {
                  Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                  long batchId = message.getId();
                  int size = message.getEntries().size();
                  if (batchId == -1 || size == 0) {
                      try {
                          Thread.sleep(1000L);
                      } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                      }
                  } else {
                      // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                      dataHandle(message.getEntries());
                  }
  
                  connector.ack(batchId); // 提交确认
                  // connector.rollback(batchId); // 处理失败, 回滚数据
              }
          } finally {
              connector.disconnect();
          }
      }
  
      private void dataHandle(List<CanalEntry.Entry> entrys) throws InvalidProtocolBufferException {
          for (CanalEntry.Entry entry : entrys) {
              //获取到操作的表名
              String tableName = entry.getHeader().getTableName();
  
              //获取到Entry 类型
              CanalEntry.EntryType entryType = entry.getEntryType();
  
              //判断entryType为ROWDATA类型
              if(CanalEntry.EntryType.ROWDATA.equals(entryType)){
  
                  //获取到序列化数据
                  ByteString storeValue = entry.getStoreValue();
  
                  //反序列化数据
                  CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
  
                  //获取事件类型是 INSERT还是UPDATE等
                  CanalEntry.EventType eventType = rowChange.getEventType();
                
                	//如果修改了表结构，需要触发修改对应表的表结构
                  if(eventType == CanalEntry.EventType.ALTER){
                      String sql = rowChange.getSql();//获取到执行的语句，直接执行
                      
                      return;
                  }
  
                  //获取具体的数据
                  List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                  //遍历数据
                  for (CanalEntry.RowData rowData: rowDataList){
                      //获取到变化之前的列信息
                      List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                      Map<String, Object> beforeColumnMap = new HashMap<>();
                      for (CanalEntry.Column column: beforeColumnsList){
                          beforeColumnMap.put(column.getName(), column.getValue());
                      }
  
                      //获取变化之后的列信息
                      List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                      Map<String, Object>afterColumnMap = new HashMap<>();
                      for (CanalEntry.Column column: afterColumnsList){
                          afterColumnMap.put(column.getName(), column.getValue());
                      }
  
                      System.out.println("表名：" + tableName + ", 操作类型：" + eventType);
                      System.out.println("改前：" + beforeColumnMap);
                      System.out.println("改后：" + afterColumnMap);
  
                      StringBuffer sql = new StringBuffer("");
                      if(eventType == CanalEntry.EventType.INSERT){
                          sql.append("insert into ").append(tableName);
  
                          StringBuffer columns = new StringBuffer("(");
                          StringBuffer values = new StringBuffer("(");
  
                          for (Map.Entry<String, Object> e: afterColumnMap.entrySet()){
                              if(!e.getValue().equals("")){
                                  columns.append(" ").append(e.getKey()).append(", ");
                                  values.append(" ").append(e.getValue()).append(", ");
                              }
                          }
                          columns.deleteCharAt(columns.length() - 2);
                          columns.append(")");
                          sql.append(columns.toString());
                          sql.append(" values ");
  
                          values.deleteCharAt(values.length() - 2);
                          values.append(")");
                          sql.append(values.toString());
  
                      }
  
                      if(eventType == CanalEntry.EventType.UPDATE){
                          sql.append("update ").append(tableName).append(" set ");
                          boolean isFirst = true;
                          StringBuffer whereCondition = new StringBuffer(" where ");
                          for (Map.Entry<String, Object> e: afterColumnMap.entrySet()){
                              if(isFirst){
                                  whereCondition.append(e.getKey()).append("=").append(e.getValue());
                                  isFirst = false;
                                  continue;
                              }
                              if(!e.getValue().equals("")){
                                  sql.append(e.getKey()).append("=").append("\"").append(e.getValue()).append("\", ");
                              }
                          }
                          sql.deleteCharAt(sql.length() - 2);
  
                          sql.append(whereCondition.toString());
                      }
  
                      if(eventType == CanalEntry.EventType.DELETE){
                          sql.append("delete from ").append(tableName).append(" where ");
                          boolean isFirst = true;
                          for (Map.Entry<String, Object> e: beforeColumnMap.entrySet()){
                              if(isFirst){
                                  sql.append(e.getKey()).append("=").append(e.getValue());
                                  break;
                              }
  
                          }
                      }
  
                      System.out.println("sql语句为：" + sql.toString());
  
                  }
  
              }
          }
      }
  
  
  
  
  }
  ```

- 拼装好了原生sql，那么就只要使用mybatis-plus执行原生sql就可以了！即mybatis-plus的sqlrunner。

- 修改springboot的项目入口文件

  ```java
  @SpringBootApplication
  public class Demo555Application implements CommandLineRunner { //实现CommandLineRunner接口
  
      @Autowired
      MyCanalClient canalClient;
  
      public static void main(String[] args) {
          SpringApplication.run(Demo555Application.class, args);
      }
  
      @Override
      public void run(String... args) throws Exception { //重写CommandLineRunner的run方法
          canalClient.run();
      }
  }
  ```

- 启动项目，进行测试