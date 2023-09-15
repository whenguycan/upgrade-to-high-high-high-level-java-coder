package com.ruoyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.RuoYiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 首页
 *
 * @author ruoyi
 */
@RequestMapping
@Controller
@CrossOrigin
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    @Value("wx.token")
    public String Token; //微信公众平台中设置的token

    @Value("wx.checkToken")
    public String checkToken; //是否校验微信公众平台中设置的token

    /**
     * 微信公众平台中设置的token
     */
    @RequestMapping("/")
    public void index(HttpServletResponse response, HttpServletRequest request) throws Exception {
        if (checkToken.equals("true")) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            String[] str = { Token, timestamp, nonce };
            // 字典排序
            Arrays.sort(str);
            String bigStr = str[0] + str[1] + str[2];
            // SHA1加密
            String digest = sha1(bigStr);
            // 确认请求来至微信
            if (digest.equals(signature)) {
                response.getWriter().print(echostr);
            }
        }
    }

    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //使用指定的字节来更新我们的摘要
        md.update(b);
        //获取密文  （完成摘要计算）
        byte[] b2 = md.digest();
        //获取计算的长度
        int len = b2.length;
        //16进制字符串
        String str = "0123456789abcdef";
        //把字符串转为字符串数组
        char[] ch = str.toCharArray();
        //创建一个40位长度的字节数组
        char[] chs = new char[len*2];
        //循环20次
        for(int i=0,k=0;i<len;i++) {
            //获取摘要计算后的字节数组中的每个字节
            byte b3 = b2[i];
            // >>>:无符号右移
            // &:按位与
            //0xf:0-15的数字
            chs[k++] = ch[b3 >>> 4 & 0xf];
            chs[k++] = ch[b3 & 0xf];
        }
        //字符数组转为字符串
        return new String(chs);
    }
}
