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
 * 计费规则测试：时长->时刻多期间多时段区间计费
 * 命名规则：test{0}{1}{2}{3}{4}{5}
 * 0：期间时段（A=单时段，M=多时段）
 * 1：最高限价（N=否，Y=是）
 * 2：计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
 * 3：免费时长次数（1=一次收费一次；2=每个分割一次）
 * 4：首时段计费方式（1=一次收费只有一次；2=每次分割一次）
 *
 */
@SpringBootTest
public class STMorePeriodPFChargeTest {

    private DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private DroolsService droolsService;

    @Test
    public void testSTMY111() {
        ParkingRecord pr = null;
        BigDecimal fee = null;
        // 免费
        pr = getParkingRecord("2023-03-27 09:00:00", "2023-03-28 18:18:00");
        pr.setRulePrefix("STMY111");
        fee = droolsService.chargeParkingFee(pr);
        Assert.isTrue(fee.equals(BigDecimal.ZERO), "异常，计费费用应为0，实际为" + fee);
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
