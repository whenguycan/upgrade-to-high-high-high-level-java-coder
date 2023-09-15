package com.ruoyi.project.common;

public class OrderNoGenerator {

    /**
     * 生成订单编号
     *
     * @return
     */
    public static String generateNo(String remark) {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 2);
        return remark+ String.valueOf(snowflakeIdWorker.nextId());
    }

}
