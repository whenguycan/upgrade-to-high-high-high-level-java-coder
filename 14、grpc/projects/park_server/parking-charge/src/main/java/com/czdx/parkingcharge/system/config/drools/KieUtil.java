package com.czdx.parkingcharge.system.config.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;

/**
 *
 * description: Kie工具类
 * @author mingchenxu
 * @date 2023/3/16 13:48
 */
@Slf4j
public class KieUtil {

    public static KieHelper kieHelper;
    public static KieBase kieBase;

    static {
        kieHelper = new ExKieHelper();
        kieBase = kieHelper.getKieContainer().getKieBase();
    }

    public static void delete(String name, ResourceType type) {
        log.info("KieUtil-删除规则：{}", name);
        String path = ((ExKieHelper) kieHelper).getPath(name, type);
        kieHelper.kfs.delete(path);
    }

    public static void addRule(String content, String name) {
        log.info("KieUtil-新增规则：{}", name);
        kieHelper.addContent(content, name + "." + ResourceType.DRL);
    }

    public static void addRule(Resource resource) {
        kieHelper.addResource(resource, ResourceType.DRL);
    }

    public static void addRuleFlow(String content, String name) {
        kieHelper.addContent(content, name + "." + ResourceType.BPMN2.getDefaultExtension());
    }

    public static KieBase getKieBase() {
        return kieBase;
    }

    public static void setKieBase(KieBase base) {
        kieBase = base;
    }

    public static void updateKieBase() {
        kieBase = kieHelper.build();
    }

    private static class ExKieHelper extends KieHelper {
        public ExKieHelper() {
            super();
        }

        public String getPath(String name, ResourceType type) {
            return "src/main/resources/" + name + "." + type.getDefaultExtension();
        }
    }

}
