package com.czdx.parkingpayment.utils;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

/**
 * <ul> 注意：
 *  <li>该实现无法处理含有Any类型字段的Message</li>
 *  <li>enum类型数据会转化为enum的字符串名</li>
 *  <li>bytes会转化为utf8编码的字符串</li>
 * </ul> 以上这段暂未进行测试
 * @Author mingchenxu
 * @Description proto 与 Json 转换工具类
 */
public class ProtoJsonUtil {
    /**
     * proto 对象转 JSON
     * 使用方法： //反序列化之后
     *             UserProto.User user1 = UserProto.User.parseFrom(user);
     *             //转 json
     *             String jsonObject = ProtoJsonUtil.toJson(user1);
     * @param sourceMessage proto 对象
     * @return 返回 JSON 数据
     * @throws InvalidProtocolBufferException
     */
    public static String toJson(Message sourceMessage) throws InvalidProtocolBufferException {
        if (sourceMessage != null) {
            //保持下划线格式参数请求
            String json = JsonFormat.printer().preservingProtoFieldNames().includingDefaultValueFields().print(sourceMessage);
            return json;
        }
        return null;
    }

    /**
     * JSON 转 proto 对象
     * 使用方法：Message message = ProtoJsonUtil.toObject(UserProto.User.newBuilder(), jsonObject);
     * @param targetBuilder proto 对象 bulider
     * @param json          json 数据
     * @return 返回转换后的 proto 对象
     * @throws InvalidProtocolBufferException
     */
    public static Message toObject(Message.Builder targetBuilder, String json) throws InvalidProtocolBufferException {
        if (json != null) {
            //ignoringUnknownFields 如果 json 串中存在的属性 proto 对象中不存在，则进行忽略，否则会抛出异常
            JsonFormat.parser().ignoringUnknownFields().merge(json, targetBuilder);
            return targetBuilder.build();
        }
        return null;
    }

    /**
     * 将ProtoBean对象转化为POJO对象
     *
     * @param destPojoClass 目标POJO对象的类类型
     * @param sourceMessage 含有数据的ProtoBean对象实例
     * @param <PojoType> 目标POJO对象的类类型范型
     * @return
     * @throws IOException
     */
    public static <PojoType> PojoType toPojoBean(Class<PojoType> destPojoClass, Message sourceMessage)
            throws IOException {
        String json = JsonFormat.printer().print(sourceMessage);
        return new Gson().fromJson(json, destPojoClass);
    }

    /**
     * 将POJO对象转化为ProtoBean对象
     *
     * @param destBuilder 目标Message对象的Builder类
     * @param sourcePojoBean 含有数据的POJO对象
     * @return
     * @throws IOException
     */
    public static void toProtoBean(Message.Builder destBuilder, Object sourcePojoBean) throws IOException {
        String json = new Gson().toJson(sourcePojoBean);
        JsonFormat.parser().merge(json, destBuilder);
    }

}


