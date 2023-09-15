## TCC下如何解决空回滚和业务悬挂



#### 1、建立一张表

> 为了实现空回滚、防止业务悬挂，以及幂等性要求。我们必须在[数据库](https://cloud.tencent.com/solution/database?from=10680)记录当前事务id和执行状态

```sql
CREATE TABLE `account_freeze_tbl` (
  `xid` varchar(128) NOT NULL COMMENT '事务id',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `state` int(1) DEFAULT NULL COMMENT '事务状态，0:try，1:confirm，2:cancel',
  PRIMARY KEY (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
```



#### 2、TCC的代码中注意

```java
//try节点
public void deduct(String userId, int money) {
  // 0. 获取事务id
  String xid = RootContext.getXID();
  // 业务悬挂处理，防止已经发起回滚操作后，阻塞的try恢复，进行扣减
  // 导致无法confirm也无法cancel
  // 1. 根据xid去上表中查询一条记录，如果有，一定是CANCEL执行过，需要拒绝业务,防止业务悬挂
  AccountFreeze oldFreeze = freezeMapper.selectById(xid);
  if (oldFreeze != null) {
    // 拒绝
    return;
  }
  // 1. 扣减可用余额
  accountMapper.deduct(userId, money);
  // 2. 记录冻结金额，事务状态
  AccountFreeze freeze = new AccountFreeze();
  freeze.setUserId(userId);
  freeze.setFreezeMoney(money);
  freeze.setState(AccountFreeze.State.TRY);
  freeze.setXid(xid);
  freezeMapper.insert(freeze);
}





//二阶段回滚阶段
public boolean cancel(BusinessActionContext context) {
  String xid = context.getXid();
  // 0. 查询冻结记录，可以走数据库，也可以走上下文
  AccountFreeze freeze = freezeMapper.selectById(xid);
  String userId = context.getActionContext("userId").toString();
  // 1. 空回滚判断，就是去上面的表中查询xid的记录，判断freeze是否为null，为null证明try没执行，需要空回滚
  if (freeze == null) {
    // 证明try没执行，需要空回滚，记录一下这个回滚的信息
    freeze = new AccountFreeze();
    freeze.setUserId(userId);
    freeze.setFreezeMoney(0);
    freeze.setState(AccountFreeze.State.CANCEL);
    freeze.setXid(xid);
    freezeMapper.insert(freeze);
    return true;
  }
  // 2. 幂等判断，只要cancel执行了，这个状态一定是CANCEL
  // 所以判断这个值就可以知道是否幂等，防止上一轮cancel超时后重复执行cancel
  if (freeze.getState() == AccountFreeze.State.CANCEL) {
    // 已经处理过一次CANCEL了，无需重复处理
    return true;
  }
  // 1. 恢复可用余额
  accountMapper.refund(freeze.getUserId(), freeze.getFreezeMoney());
  // 2. 将冻结金额清零，状态改为CANCEL
  freeze.setFreezeMoney(0);
  freeze.setState(AccountFreeze.State.CANCEL);
  int count = freezeMapper.updateById(freeze);
  return count == 1;
}
```

