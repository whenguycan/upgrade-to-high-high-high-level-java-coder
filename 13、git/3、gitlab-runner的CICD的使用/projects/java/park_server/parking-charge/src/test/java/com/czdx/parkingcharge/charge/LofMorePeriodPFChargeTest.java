package com.czdx.parkingcharge.charge;

import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.service.DroolsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 计费规则测试：时长->期间多时段区间计费
 * 命名规则：test{0}{1}{2}{3}{4}{5}
 * 0：期间时段（A=单时段，M=多时段）
 * 1：最高限价（N=否，Y=是）
 * 2：计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
 * 3：免费时长次数（1=一次收费一次；2=每个分割一次）
 * 4：首时段计费方式（1=一次收费只有一次；2=每次分割一次）
 *
 */
@SpringBootTest
class LofMorePeriodPFChargeTest {

    private DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private DroolsService droolsService;

    /**
     * 期间-3时段-无最高限价-不分割-免费[1次收费1次]-首时段[1次收费一次]
     */
    @Test
    public void testMN111() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(26)), "异常，计费费用应为26，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-19 13:59:00");
        pr.setRulePrefix("MN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(250)), "异常，计费费用应为250，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-24小时强制分割-免费[1次收费1次]-首时段[1次收费一次]
     */
    @Test
    public void testMN211() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 24小时分割，处于边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:00:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(117)), "异常，计费费用应为117，实际为" + fee);

        // 24小时分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:01:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(121)), "异常，计费费用应为121，实际为" + fee);

        // 24小时分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 10:30:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(124)), "异常，计费费用应为124，实际为" + fee);

        // 24小时分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 10:31:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(128)), "异常，计费费用应为128，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(366)), "异常，计费费用应为366，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-24小时强制分割-免费[1次收费1次]-首时段[每次分割一次]
     */
    @Test
    public void testMN212() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 24小时分割，处于边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:00:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(117)), "异常，计费费用应为117，实际为" + fee);

        // 24小时分割，处于第一个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:01:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(122)), "异常，计费费用应为122，实际为" + fee);

        // 24小时分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:31:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(126)), "异常，计费费用应为126，实际为" + fee);

        // 24小时分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 10:01:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(129)), "异常，计费费用应为129，实际为" + fee);

        // 24小时分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 11:01:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(133)), "异常，计费费用应为133，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN212-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(377)), "异常，计费费用应为377，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-24小时强制分割-免费[每次分割一次]-首时段[1次收费1次]
     */
    @Test
    public void testMN221() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 24小时分割，处于边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:18:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(117)), "异常，计费费用应为117，实际为" + fee);

        // 24小时分割，处于第一个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:19:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(121)), "异常，计费费用应为121，实际为" + fee);

        // 24小时分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:31:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(124)), "异常，计费费用应为124，实际为" + fee);

        // 24小时分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 10:30:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(124)), "异常，计费费用应为124，实际为" + fee);

        // 24小时分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 10:31:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(128)), "异常，计费费用应为128，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(366)), "异常，计费费用应为366，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-24小时强制分割-免费[每次分割一次]-首时段[每次分割一次]
     */
    @Test
    public void testMN222() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 24小时分割，处于边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:18:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(117)), "异常，计费费用应为117，实际为" + fee);

        // 24小时分割，处于第一个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:19:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(122)), "异常，计费费用应为122，实际为" + fee);

        // 24小时分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:31:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(126)), "异常，计费费用应为126，实际为" + fee);

        // 24小时分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 11:00:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(129)), "异常，计费费用应为129，实际为" + fee);

        // 24小时分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 11:01:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(133)), "异常，计费费用应为133，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN222-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(377)), "异常，计费费用应为377，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-0点强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMN311() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 0点强制分割，处于边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 00:00:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(75)), "异常，计费费用应为75，实际为" + fee);

        // 0点强制分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 00:01:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(79)), "异常，计费费用应为79，实际为" + fee);

        // 0点强制分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 00:31:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(82)), "异常，计费费用应为82，实际为" + fee);

        // 0点强制分割，隐藏分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 01:31:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(86)), "异常，计费费用应为86，实际为" + fee);

        // 0点强制分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 03:01:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(93)), "异常，计费费用应为93，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(366)), "异常，计费费用应为366，实际为" + fee);
    }

    /**
     * 期间-3时段-无最高限价-0点强制分割-免费[1次收费1次]-首时段[每次分割一次]
     */
    @Test
    public void testMN312() {

    }

    /**
     * 期间-3时段-无最高限价-0点强制分割-免费[每次分割一次]-首时段[1次收费1次]
     */
    @Test
    public void testMN321() {

    }

    /**
     * 期间-3时段-无最高限价-0点强制分割-免费[每次分割一次]-首时段[每次分割一次]
     */
    @Test
    public void testMN322() {

    }

    /**
     * 期间-3时段-无最高限价-期间强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMN411() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 期间强制分割，第一次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:01:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 期间强制分割，第一次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 期间强制分割，第一次分割，处于隐藏的分割边界
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:30:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 期间强制分割，第一次分割，隐藏的分割边界，第二时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 期间强制分割，第二次分割，处于第二时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:01:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(27)), "异常，计费费用应为27，实际为" + fee);

        // 期间强制分割，第二次分割，隐藏的分割后，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 14:31:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(34)), "异常，计费费用应为34，实际为" + fee);

        // 0点强制分割，隐藏第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 03:01:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(104)), "异常，计费费用应为104，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(426)), "异常，计费费用应为426，实际为" + fee);
    }

    public void testMN412() {

    }

    public void testMN421() {

    }

    /**
     * 期间-3时段-无最高限价-期间强制分割-免费[每次分割一次]-首时段[每次分割一次]
     */
    public void testMN422() {

    }

    /**
     * 期间-3时段-有最高限价-不分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMY111() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(26)), "异常，计费费用应为26，实际为" + fee);

        // 隐藏第二次分割，处于第二个时段，超过最高限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 03:01:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(175)), "异常，计费费用应为175，实际为" + fee);
    }

    public void testMY112() {

    }

    public void testMY121() {

    }

    /**
     * 期间-3时段-有最高限价-24小时强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMY211() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(26)), "异常，计费费用应为26，实际为" + fee);

        // 24点强制分割，隐藏第二次分割，处于第二个时段，超过最高限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 11:01:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);
    }

    public void testMY212() {

    }

    public void testMY221() {

    }

    public void testMY222() {

    }

    /**
     * 期间-3时段-有最高限价-0点强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMY311() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(26)), "异常，计费费用应为26，实际为" + fee);

        // 0点强制分割，隐藏第二次分割，处于第二个时段，超过最高限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-19 11:01:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(114)), "异常，计费费用应为114，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(175)), "异常，计费费用应为175，实际为" + fee);
    }

    public void testMY312() {

    }

    public void testMY321() {

    }

    public void testMY322() {

    }

    /**
     * 期间-3时段-有最高限价-期间强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMY411() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 10:18:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(12)), "异常，计费费用应为12，实际为" + fee);

        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:18:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:31:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(19)), "异常，计费费用应为19，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(30)), "异常，计费费用应为30，实际为" + fee);

        // 隐藏第二次分割，处于第二个时段，超过最高限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-19 11:01:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(115)), "异常，计费费用应为115，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(179)), "异常，计费费用应为179，实际为" + fee);
    }

    /**
     * 期间-3时段-有最高限价-期间强制分割-免费[1次收费1次]-首时段[1次收费1次]
     */
    @Test
    public void testMY411NCFT() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:18:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，第一个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:48:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(5)), "异常，计费费用应为5，实际为" + fee);


        // 隐藏期间第二次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 09:49:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(9)), "异常，计费费用应为9，实际为" + fee);

        // 隐藏期间第二次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 11:01:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(16)), "异常，计费费用应为16，实际为" + fee);

        // 隐藏期间第三次分割，处于第二个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 12:31:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(23)), "异常，计费费用应为23，实际为" + fee);

        // 隐藏期间第三次分割，处于第三个时段
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-17 13:59:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(30)), "异常，计费费用应为30，实际为" + fee);

        // 隐藏第二次分割，处于第二个时段，超过最高限价
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-18 09:18:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(137)), "异常，计费费用应为137，实际为" + fee);

        // 任意时间
        pr = getParkingRecord("2023-03-17 09:00:00", "2023-03-20 13:59:00");
        pr.setRulePrefix("MY411NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(361)), "异常，计费费用应为361，实际为" + fee);
    }

    public void testMY412() {

    }

    public void testMY421() {

    }

    public void testMY422() {

    }

    private ParkingRecord getParkingRecord(String entryTime, String exitTime) {
        LocalDateTime startTime = LocalDateTime.parse(entryTime, dft);
        LocalDateTime endTime = LocalDateTime.parse(exitTime, dft);
        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setEntryTime(startTime);
        parkingRecord.setExitTime(endTime);
        return parkingRecord;
    }

}
