package com.ruoyi.project.parking.dahua.icc;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

public class ICCUtils {
    private static Logger logger = LoggerFactory.getLogger(ICCUtils.class);

    public static String decode(@RequestBody String param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("+++++++++++++++++++接收到新增推送接口数据+++++++++++++++++++++");
        logger.info(param);
        param = URLDecoder.decode(param, "utf-8");
        param = param.replace(" ", "+");
        String userAgent = request.getHeader("User-Agent");
        userAgent = userAgent.substring(userAgent.lastIndexOf("_") + 1, userAgent.length());
        String[] nums = userAgent.split("[\\D]+");
        int ciphertnum = 0;
        String lastNum = nums[nums.length - 1];
        if (lastNum.length() > 1) {
            System.out.println(lastNum.length());
            lastNum = lastNum.substring(lastNum.length() - 1, lastNum.length());
        }
        ciphertnum = Integer.parseInt(lastNum);
        String key = userAgent.substring(ciphertnum, ciphertnum + 16);
        //解密
        String resultStr = AESUtil.aesDecrypt(param, key);
        logger.info(resultStr);
        return resultStr;

//        byte[] raw = key.getBytes("utf-8");
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//        byte[] decode = new Base64().decode(param);
//        byte[] bytes = cipher.doFinal(decode);
//        String s = new String(bytes, "utf-8");

    }
}
