package com.ruoyi.project.parking.utils;

import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/2/23 9:22 AM
 * @Description: 类描述信息
 */
@Component
public class SnowflakeIdWorker16Size {

    public SnowflakeIdWorker16Size() {
        workerId = 1;
        datacenterId = 2;
    }

    // ==============================Fields===========================================
    /** 开始时间截 (2015-01-01) */
    private final long twepoch = 1420041600000L;

    /** 机器id所占的位数 */
    private final long workerIdBits = 3L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 3L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 9L;

    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;

    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long workerId;

    /** 数据中心ID(0~31) */
    private long datacenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param workerId 工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker16Size(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的(即在同一个毫秒内的并发请求)，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) // (当前时间戳 - 系统预设的时间戳) 通过 << 向左移动 "timestampLeftShift" 位，
                // timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits，
                // sequenceBits为序号位 = 12， workerIdBits为服务ID位 = 5， datacenterIdBits为数据ID位 = 5
                | (datacenterId << datacenterIdShift) // datacenterId的具体数据是用户定义的，通过 << 向左移动 " datacenterIdShift " 位，
                // datacenterIdShift = sequenceBits + workerIdBits
                // sequenceBits为序号位 = 12， workerIdBits为服务ID位 = 5
                | (workerId << workerIdShift) // workerId的具体数据是用户定义的，通过 << 向左移动 " workerIdShift " 位，
                // workerIdBits = sequenceBits
                // sequenceBits为序号位 = 12
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================
//    /** 测试 */
//    public static void main(String[] args) {
//        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 2);
//        for (int i = 0; i < 1000; i++) {
//            long id = idWorker.nextId();
//            System.out.println(Long.toBinaryString(id));
//            System.out.println(id);
//        }
//    }
    public static void main(String[] args) {
        SnowflakeIdWorker16Size snowflakeIdWorker = new SnowflakeIdWorker16Size(1, 2);
        String orderNum = String.valueOf(snowflakeIdWorker.nextId());
        System.out.println(orderNum);
    }
}
