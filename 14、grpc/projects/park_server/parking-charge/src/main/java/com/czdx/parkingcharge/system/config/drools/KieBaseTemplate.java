package com.czdx.parkingcharge.system.config.drools;

import org.kie.api.KieBase;

/**
 *
 * description: Kie操作模板
 * @author mingchenxu
 * @date 2023/3/16 13:38
 */
public class KieBaseTemplate {

    private KieBase kieBase;

    public KieBaseTemplate(KieBase kieBase) {
        this.kieBase = kieBase;
    }

    public KieBase getKieBase() {
        return kieBase;
    }

    public void update(KieBase kieBase) {
        this.kieBase = kieBase;
    }

}
