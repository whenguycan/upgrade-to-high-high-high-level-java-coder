package com.czdx.parkingcharge.common.constants;

/**
 *
 * description: 业务字符串
 * @author mingchenxu
 * @date 2023/3/21 10:31
 */
public class BUStr {

    private BUStr() {}


    /**
     * 车场计费约束前缀
     * Redis
     */
    public static final String R_PARKE_LOT_CHARGE_SCHEMA_PREFIX = "PARK_LOT:CHARGE_SCHEMA:";

    /**
     * 规则引擎停车费规则前缀
     */
    public static final String D_PARKING_CHARGE_RULE_PREFIX = "PARKING_CHARGE_";

    /**
     * 规则引擎车辆获取车场停车费规则前缀
     */
    public static final String D_CAR_GET_PARKING_CHARGE_RULE_PREFIX = "CAR_GET_PARKING_CHARGE_";

    /**
     * 计费日期时间格式化字符串
     */
    public static final String CHARGE_DATE_TIME_FORMATTER_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * 计费时刻格式化字符串
     */
    public static final String CHARGE_TIME_FORMATTER_STR = "HH:mm";

    /**
     * 规则引擎：计费规则 模板名称
     */
    public static final String D_CHARGE_RULE_TEMPLATE_NAME = "park_charge_rule_template";

    /**
     * 规则引擎：计费规则-车型 模板名称
     */
    public static final String D_CHARGE_RULE_VEHICLE_RELATION_TEMPLATE_NAME = "car_get_park_charge_rule_template";

}
