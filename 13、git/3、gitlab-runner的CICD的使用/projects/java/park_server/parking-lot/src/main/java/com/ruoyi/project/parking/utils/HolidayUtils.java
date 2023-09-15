package com.ruoyi.project.parking.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HolidayUtils {

    /**
     * 获取节假日不含周末
     * @param year /
     * @return /
     */
    public static String getJjr(Integer year) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> holidays = new ArrayList<>();
        Set<String> strs = new LinkedHashSet<>();
        buildOriData(holidays, year - 1, strs, true);
        buildOriData(holidays, year, strs, false);
        if (CollectionUtils.isNotEmpty(holidays)) {
            for (String str : strs) {
                List<Map<String, Object>> resultItems = holidays.stream().filter(x -> x.get("name").equals(str)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(resultItems)) {
                    if (str.contains("补班")) {
                        result.addAll(resultItems);
                    } else {
                        resultItems.get(0).put("startDate", resultItems.get(0).get("startDate"));
                        resultItems.get(0).put("endDate", resultItems.get(resultItems.size() - 1).get("endDate"));
                        result.add(resultItems.get(0));
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(result)) {
            for (Map<String, Object> item : result) {
                item.put("name", item.get("name").toString().replace("前补班", "").replace("后补班", ""));
            }
        }
        return JSON.toJSONString(result);
    }

    private static void buildOriData(List<Map<String, Object>> holidays, Integer year, Set<String> strs, Boolean isLastYear) throws Exception {
        String url = "http://timor.tech/api/holiday/year/" + year;
        String response = HttpClientUtil.doGet(url, null);
        JSONObject object = JSONObject.parseObject(response);
        if (!object.get("code").equals(0)) {
            throw new Exception("获取节假日失败");
        }
        Map<String, Map<String, Object>> holiday = JSONObject.parseObject(object.get("holiday").toString(), LinkedHashMap.class);
        if (!holiday.isEmpty()) {
            Set<String> strings = holiday.keySet();
            for (String str : strings) {
                Map<String, Object> stringObjectMap = holiday.get(str);
                String date = stringObjectMap.get("date").toString();
                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int dateMonthValue = localDate.getMonthValue();
                String name = stringObjectMap.get("name").toString();
                if (name.equals("除夕") || name.equals("初一") || name.equals("初二")
                        || name.equals("初三") || name.equals("初四") || name.equals("初五")
                        || name.equals("初六")) {
                    stringObjectMap.put("name", "春节");
                }
                if (isLastYear ? (stringObjectMap.get("name").toString().contains("元旦") && dateMonthValue == 12) :
                        (!stringObjectMap.get("name").toString().contains("元旦") || dateMonthValue != 12)) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("year", isLastYear ? year + 1 : year);
                    item.put("name", stringObjectMap.get("name"));
                    item.put("type", stringObjectMap.get("wage").equals(1) ? 2 : 1);
                    item.put("startDate", stringObjectMap.get("date") + " 00:00:00");
                    item.put("endDate", stringObjectMap.get("date") + " 23:59:59");
                    holidays.add(item);
                }
                strs.add(stringObjectMap.get("name").toString());
            }
        }
    }
}
