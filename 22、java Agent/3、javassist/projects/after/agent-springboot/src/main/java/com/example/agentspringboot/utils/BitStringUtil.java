package com.example.agentspringboot.utils;

import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/5/23 3:49 PM
 * @Description: 类描述信息
 */
@Component
public class BitStringUtil {

    public String addString(int length){
        String result = "";
        for (int i =0; i < length; i++) {
            result += (char) (i % 26 + 'a');
        }

        return result;
    }

    public String buildString(int length){
        StringBuilder inst = new StringBuilder();
        for (int i = 0; i < length; i++){
            inst.append((char) (i % 26 + 'a'));
        }
        return inst.toString();
    }

}
