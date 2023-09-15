package com.ruoyi.common.utils.sms;

import com.alibaba.fastjson2.JSON;

import java.security.MessageDigest;

/**
 * describe <p>生成签名</p>
 *
 * @author LijieZhu
 * @date 2021/8/18 8:45
 */
public class DigestUtil {
    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        return DigestUtil.getSha1(str);
    }

    public static String enc(SendMessageParam param,String appKey) {
        String s = "";
        if (param.getSendType()!=null){
            s=param.getAppId() + appKey + JSON.toJSONString(param.getContent()) + param.getPhones() + String.valueOf(param.getSendType()) + param.getSignName() + param.getTemplateCode() + String.valueOf(param.getTimestamp());
        }else {
            s=param.getAppId() + appKey + JSON.toJSONString(param.getContent()) + param.getPhones() + param.getSignName() + param.getTemplateCode() + String.valueOf(param.getTimestamp());
        }

        return encode(s);
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

    public static String getSha1(String str) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
