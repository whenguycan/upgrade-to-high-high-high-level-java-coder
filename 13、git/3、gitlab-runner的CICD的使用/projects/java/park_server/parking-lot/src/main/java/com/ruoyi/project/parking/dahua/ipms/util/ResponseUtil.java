package com.ruoyi.project.parking.dahua.ipms.util;

import com.dahuatech.hutool.json.JSONArray;
import com.dahuatech.hutool.json.JSONObject;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.oauth.http.IccResponse;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import org.apache.poi.ss.formula.functions.T;

/**
 * @ClassName ResponseUtil
 * @Author 227494
 * @Date 2022/3/21 14:46
 * @Description ResponseUtil 解析响应工具类
 * @Version 1.0
 */
public class ResponseUtil {

    public static String getStringFromResponse(IccResponse response) {
        JSONObject resJson = JSONUtil.parseObj(response.getResult());
        return resJson.getStr("data");
    }

    public static Integer getIntFromResponse(IccResponse response) {
        return Integer.valueOf(getStringFromResponse(response));
    }

    public static Long getLongFromResponse(IccResponse response) {
        return Long.valueOf(getStringFromResponse(response));
    }

    public static JSONObject getJsonFromResponse(IccResponse response) {
        JSONObject resJson = JSONUtil.parseObj(response.getResult());
        return resJson.getJSONObject("data");
    }

    public static Boolean getBooleanFromResponse(IccResponse response) {
        return Boolean.valueOf(getStringFromResponse(response));
    }

    public static JSONArray getJsonArrayFromResponse(IccResponse response){
        JSONObject resJson = JSONUtil.parseObj(response.getResult());
        return resJson.getJSONArray("data");
    }

}
