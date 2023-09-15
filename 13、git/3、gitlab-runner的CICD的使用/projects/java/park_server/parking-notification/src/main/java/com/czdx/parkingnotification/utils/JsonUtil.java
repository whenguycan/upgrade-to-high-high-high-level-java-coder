package com.czdx.parkingnotification.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class JsonUtil {

    private JsonUtil() {
    }

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 对象字段全部列入
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 取消默认转换timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        // 忽略空bean转json的错误
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 统一日期格式yyyy-MM-dd HH:mm:ss
        mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT));

        // 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // LocalDateTime日期序列化支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));

        mapper.registerModule(javaTimeModule);
    }


    /**
     * 对象转换为 json 字符串.
     * @param obj 需要转换的对象
     * @return 转换后的字符串
     */
    public static String bean2Json(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Jackson 解析字符串出错：{}", e.getMessage());
            return null;
        }
    }


    /**
     * Json 字符串转换为对象.
     * @param jsonStr 对应的 json 字符串
     * @param objClass 对象的class
     * @param <T> 转换后的对象
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try {
            return mapper.readValue(jsonStr, objClass);
        } catch (IOException e) {
            log.error("Jackson 转换对象出错：{}", e.getMessage());
            return null;
        }
    }


    /**
     * 读取节点数据并返回 Int 类型
     * @param source 资源
     * @param node 节点名称
     * @return
     */
    public static Integer getNodeIntValue(String source, String node) {
        return Integer.parseInt(getNodeStringValue(source, node));
    }


    /**
     * 读取节点数据，并返回 String 类型，最外边不携带双引号
     * @param source 资源
     * @param node 节点名称
     * @return
     */
    public static String getNodeStringValue(String source, String node) {
        try {
            JsonNode jsonNode = mapper.readTree(source);
            return jsonNode.findValue(node).asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * 读取节点数据，并返回 json 格式的字符串 - 外边带有双引号
     * @param source 资源
     * @param node 节点名称
     * @return
     */
    public static String getNodeToJsonStr(String source, String node) {
        try {
            JsonNode jsonNode = mapper.readTree(source);
            return jsonNode.findValue(node).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 读取节点数据，并返回指定类型
     * @param source 资源
     * @param node 节点名称
     * @param clazz 指定类型
     * @return
     */
    public static <T> T getNodeStringToObj(String source, String node, Class<T> clazz) {
        try {
            JsonNode jsonNode = mapper.readTree(source);
            String value = jsonNode.findValue(node).toString();
            return json2Bean(value, clazz);
        } catch (IOException e) {
            log.error("Jackson 转换集合对象出错：{}", e.getMessage());
            return null;
        }
    }


    /**
     * 读取节点数据，并返回指定集合类型
     * @param source 资源
     * @param node 节点名称
     * @param collectionClass 指定集合类型
     * @param elementClasses 集合中类型
     * @return
     */
    public static <T> T getNodeStringToObj(String source, String node, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            JsonNode jsonNode = mapper.readTree(source);
            String value = jsonNode.findValue(node).toString();
            return json2CollectObj(value, collectionClass, elementClasses);
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Json 字符串转 object 用于转为集合对象
     * @param str json字符串
     * @param collectionClass 被转集合class
     * @param elementClasses 被转集合中对象类型class
     * @param <T>
     * @return
     */
    public static <T> T json2CollectObj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return mapper.readValue(str, javaType);
        } catch (IOException e) {
            log.error("Jackson 转换集合对象出错：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 将xml转换为json对象
     */
    public static JSONObject xmlToJson(String xml) throws DocumentException {
        JSONObject jsonObject = new JSONObject();
        Document document = DocumentHelper.parseText(xml);
        //获取根节点元素对象
        Element root = document.getRootElement();
        iterateNodes(root, jsonObject);
        return jsonObject;
    }

    /**
     * 遍历元素
     * @param node
     * @param json
     */
    private static void iterateNodes(Element node, JSONObject json) {
        //获取当前元素名称
        String nodeName = node.getName();
        //判断已遍历的JSON中是否已经有了该元素的名称
        if(json.containsKey(nodeName)){
            //该元素在同级下有多个
            Object Object = json.get(nodeName);
            JSONArray array = null;
            if(Object instanceof JSONArray){
                array = (JSONArray) Object;
            }else {
                array = new JSONArray();
                array.add(Object);
            }
            //获取该元素下所有子元素
            List<Element> listElement = node.elements();
            if(listElement.isEmpty()){
                //该元素无子元素，获取元素的值
                String nodeValue = node.getTextTrim();
                array.add(nodeValue);
                json.put(nodeName, array);
                return ;
            }
            //有子元素
            JSONObject newJson = new JSONObject();
            //遍历所有子元素
            for(Element e:listElement){
                //递归
                iterateNodes(e,newJson);
            }
            array.add(newJson);
            json.put(nodeName, array);
            return ;
        }
        //该元素同级下第一次遍历
        //获取该元素下所有子元素
        List<Element> listElement = node.elements();
        if(listElement.isEmpty()){
            //该元素无子元素，获取元素的值
            String nodeValue = node.getTextTrim();
            json.put(nodeName, nodeValue);
            return ;
        }
        //有子节点，新建一个JSONObject来存储该节点下子节点的值
        JSONObject object = new JSONObject();
        for(Element e:listElement){
            //递归
            iterateNodes(e,object);
        }
        json.put(nodeName, object);
        return ;
    }
}
