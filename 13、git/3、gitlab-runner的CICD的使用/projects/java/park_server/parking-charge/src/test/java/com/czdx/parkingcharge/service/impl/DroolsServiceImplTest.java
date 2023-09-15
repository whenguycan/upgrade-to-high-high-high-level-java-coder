package com.czdx.parkingcharge.service.impl;

import com.czdx.parkingcharge.service.DroolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 计费规则测试
 * 命名规则：test{0}{1}{2}{3}{4}{5}
 * 0：期间时段（L=单时段，M=多时段）
 * 1：最高限价（N=否，Y=是）
 * 2：计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
 * 3：免费时长次数（1=一次收费一次；2=每个分割一次）
 * 4：首时段计费方式（1=一次收费只有一次；2=每次分割一次）
 *
 */
@SpringBootTest
class DroolsServiceImplTest {

    @Autowired
    private DroolsService droolsService;

    public void testLN111() {

    }

    public void testLN121() {

    }

    public void testLN211() {

    }

    public void testLN221() {

    }

    public void testLN311() {

    }

    public void testLN321() {

    }

    public void testLN411() {

    }

    public void testLN421() {

    }

    public void testLY111() {

    }

    public void testLY121() {

    }

    public void testLY211() {

    }

    public void testLY221() {

    }

    public void testLY311() {

    }

    public void testLY321() {

    }

    public void testLY411() {

    }

    public void testLY421() {

    }

}
