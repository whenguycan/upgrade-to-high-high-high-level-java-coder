package com.czdx.parkingcharge.charge;

import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.service.DroolsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 计费规则测试：时长->期间单时段区间计费
 * 命名规则：test{0}{1}{2}{3}{4}{5}
 * 0：期间时段（A=单时段，M=多时段）
 * 1：最高限价（N=否，Y=是）
 * 2：计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
 * 3：免费时长次数（1=一次收费一次；2=每个分割一次）
 * 4：首时段计费方式（1=一次收费只有一次；2=每次分割一次）
 *
 */
@SpringBootTest
class LofAlonePeriodPFChargeTest {

    private DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private DroolsService droolsService;

    /**
     * 期间-单时段-无最高限价-不分割-免费[1次收费1次]-首时段[1次收费一次]
     */
    @Test
    public void testAN111() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间外，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);
    }


    /**
     * 期间-单时段-有最高限价-不分割-免费[1次收费一次]-首时段[1次收费一次]
     * 算费不包含免费时间
     */
    @Test
    public void testAN111NCFT() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 10:18:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(4)), "异常，计费费用应为4，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 10:19:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(8)), "异常，计费费用应为8，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(40)), "异常，计费费用应为40，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 09:18:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(96)), "异常，计费费用应为96，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(192)), "异常，计费费用应为192，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(196)), "异常，计费费用应为196，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:19:00");
        pr.setRulePrefix("AN111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(292)), "异常，计费费用应为292，实际为" + fee);

    }

    /**
     * 期间-单时段-有最高限价-不分割-免费[1次收费一次]-首时段[1次收费一次]
     * 算费不包含免费时间
     */
    @Test
    public void testAN111NCFT2() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 08:30:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 08:31:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(4)), "异常，计费费用应为4，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 08:30:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(96)), "异常，计费费用应为96，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 08:31:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:30:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:31:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(104)), "异常，计费费用应为104，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:30:00");
        pr.setRulePrefix("AN111NCFT2-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(196)), "异常，计费费用应为196，实际为" + fee);

    }

    /**
     * 期间-单时段-无最高限价-24小时强制分割-免费[1次收费1次]-首时段[1次收费一次]
     */
    @Test
    public void testAN211() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间外，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 期间外，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 19:18:00");
        pr.setRulePrefix("AN211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为105，实际为" + fee);
    }

    /**
     * 期间-单时段-无最高限价-24小时强制分割-免费[每次分割一次]-首时段[1次收费一次]
     */
    @Test
    public void testAN221() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间外，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 期间外，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 19:18:00");
        pr.setRulePrefix("AN221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);
    }

    /**
     * 期间-单时段-无最高限价-0点强制分割-免费[1次收费1次]-首时段[1次收费一次]
     */
    @Test
    public void testAN311() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 分割后，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 19:18:00");
        pr.setRulePrefix("AN311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);
    }

    /**
     * 期间-单时段-无最高限价-0点强制分割-免费[每次分割一次]-首时段[1次收费一次]
     */
    @Test
    public void testAN321() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，有免费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 00:18:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过免费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 00:19:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 分割后，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 19:18:00");
        pr.setRulePrefix("AN321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);
    }

    /**
     * 期间-单时段-无最高限价-期间强制分割-免费[1次收费一次]-首时段[1次收费一次]
     */
    @Test
    public void testAN411() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 01:01:00");
        pr.setRulePrefix("AN411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);
    }

    /**
     * 期间-单时段-无最高限价-期间强制分割-免费[每次分割一次]-首时段[1次收费一次]
     */
    @Test
    public void testAN421() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，有免费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 00:18:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过免费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 00:19:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 01:01:00");
        pr.setRulePrefix("AN421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-不分割-免费[1次收费一次]-首时段[1次收费一次]
     */
    @Test
    public void testAY111() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(25)), "异常，计费费用应为25，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 01:01:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(75)), "异常，计费费用应为75，实际为" + fee);

    }

    /**
     * 期间-单时段-有最高限价-不分割-免费[1次收费一次]-首时段[1次收费一次]
     * 算费不包含免费时间
     */
    @Test
    public void testAY111NCFT() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，第二个收费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 10:18:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(5)), "异常，计费费用应为5，实际为" + fee);

        // 期间内，第二个收费时间
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 10:19:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(10)), "异常，计费费用应为10，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(10)), "异常，计费费用应为10，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(25)), "异常，计费费用应为25，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 08:18:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(205)), "异常，计费费用应为205，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:01:00");
        pr.setRulePrefix("AY111NCFT-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(230)), "异常，计费费用应为230，实际为" + fee);

    }

    /**
     * 期间-单时段-有最高限价-不分割-免费[每次分割一次]-首时段[1次收费一次]
     */
    @Test
    public void testAY121() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY121-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY121-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AY111-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(25)), "异常，计费费用应为25，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AY121-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AY121-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 01:01:00");
        pr.setRulePrefix("AY121-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(75)), "异常，计费费用应为75，实际为" + fee);

    }

    /**
     * 期间-单时段-有最高限价-24小时强制分割-免费[1次收费一次]-首时段[1次收费一次]
     */
    @Test
    public void testAY211() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(25)), "异常，计费费用应为25，实际为" + fee);

        // 分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 01:01:00");
        pr.setRulePrefix("AY211-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(75)), "异常，计费费用应为75，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-24小时强制分割-免费[每次分割一次]-首时段[1次收费一次]
     * 最高限价 2880 分钟 收费 100
     * 期间限价200
     */
    @Test
    public void testAY221() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:19:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 19:18:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为50，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 最高限价分割后，又达到24小时分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:18:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);

        // 最高限价分割后，又达到24小时分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:19:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);

        // 两次最高限价分割后，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-14 09:19:00");
        pr.setRulePrefix("AY221-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(250)), "异常，计费费用应为250，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-0点强制分割-免费[1次收费一次]-首时段[1次收费一次]
     * 最高限价 2880 分钟 收费 100
     * 期间最高50元
     */
    @Test
    public void testAY311() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 00:00:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间内，0点强制分割
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 00:01:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 00:00:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:01:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-14 01:01:00");
        pr.setRulePrefix("AY311-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-0点强制分割-免费[每次分割一次]-首时段[1次收费一次]
     * 最高限价 2880 分钟 收费 100
     * 期间最高50元
     */
    @Test
    public void testAY321() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 00:18:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间内，0点强制分割
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 00:19:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 00:18:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 00:19:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(155)), "异常，计费费用应为155，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-14 01:01:00");
        pr.setRulePrefix("AY321-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-期间强制分割-免费[1次收费一次]-首时段[1次收费一次]
     * 最高限价 2880 分钟 收费 100
     * 期间最高50元
     */
    @Test
    public void testAY411() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 05:00:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间强制分割，临界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 09:00:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间强制分割，过界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 09:01:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 最高限价分割后，临算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:00:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:01:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:00:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 最高限价分割后，又达到期间强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:01:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(155)), "异常，计费费用应为155，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-14 01:01:00");
        pr.setRulePrefix("AY411-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);
    }

    /**
     * 期间-单时段-有最高限价-期间强制分割-免费[每次分割一次]-首时段[1次收费一次]
     * 最高限价 2880 分钟 收费 100
     * 期间最高50元
     */
    @Test
    public void testAY421() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 09:18:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);

        // 期间内，不超过期间限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-10 11:18:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(15)), "异常，计费费用应为15，实际为" + fee);

        // 期间内，超过期间限价、最高限价
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 05:00:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间强制分割，临界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 09:18:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(50)), "异常，计费费用应为50，实际为" + fee);

        // 期间强制分割，过界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-11 09:19:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(55)), "异常，计费费用应为55，实际为" + fee);

        // 最高限价分割后，临算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:18:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(100)), "异常，计费费用应为100，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-12 09:19:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(105)), "异常，计费费用应为105，实际为" + fee);

        // 最高限价分割后，下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:18:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(150)), "异常，计费费用应为150，实际为" + fee);

        // 最高限价分割后，又达到期间强制分割，超过下次算费免费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-13 09:19:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(155)), "异常，计费费用应为155，实际为" + fee);

        // 最高限价分割后，又达到0点强制分割，超过下次算费边界
        pr = getParkingRecord("2023-03-10 09:00:00", "2023-03-14 01:01:00");
        pr.setRulePrefix("AY421-");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.valueOf(200)), "异常，计费费用应为200，实际为" + fee);
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
