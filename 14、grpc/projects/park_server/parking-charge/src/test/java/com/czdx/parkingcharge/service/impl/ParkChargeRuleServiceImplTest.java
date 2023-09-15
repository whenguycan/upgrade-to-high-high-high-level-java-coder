package com.czdx.parkingcharge.service.impl;

import com.czdx.parkingcharge.service.ParkChargeRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParkChargeRuleServiceImplTest {

    @Autowired
    private ParkChargeRuleService parkChargeRuleService;

    @Test
    public void test() {
        parkChargeRuleService.loadChargeRule();
    }

}
