package com.czdx.parkingcharge.charge;

import com.czdx.parkingcharge.service.DroolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    private DroolsService droolsService;

    public void testMN111() {

    }

    public void testMN112() {

    }

    public void testMN121() {

    }

    public void testMN211() {

    }

    public void testMN212() {

    }

    public void testMN221() {

    }

    public void testMN222() {

    }

    public void testMN311() {

    }

    public void testMN312() {

    }

    public void testMN321() {

    }

    public void testMN322() {

    }

    public void testMN411() {

    }

    public void testMN412() {

    }

    public void testMN421() {

    }

    public void testMN422() {

    }

    public void testMY111() {

    }

    public void testMY112() {

    }

    public void testMY121() {

    }

    public void testMY211() {

    }

    public void testMY212() {

    }

    public void testMY221() {

    }

    public void testMY222() {

    }

    public void testMY311() {

    }

    public void testMY312() {

    }

    public void testMY321() {

    }

    public void testMY322() {

    }

    public void testMY411() {

    }

    public void testMY412() {

    }

    public void testMY421() {

    }

    public void testMY422() {

    }

}
