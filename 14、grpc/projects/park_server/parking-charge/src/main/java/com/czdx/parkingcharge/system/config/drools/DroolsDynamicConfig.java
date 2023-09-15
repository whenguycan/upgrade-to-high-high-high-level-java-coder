package com.czdx.parkingcharge.system.config.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * description: Drools 配置文件-动态加载文件
 *
 * @author mingchenxu
 * @date 2023/3/2 15:28
 */
@Slf4j
@Configuration
public class DroolsDynamicConfig {

    //指定规则文件存放的目录
    private static final String RULES_PATH = "rules/";

    @Bean
    @PostConstruct
    public void initKieUtil() throws IOException {
        System.setProperty("drools.dateformat","yyyy-MM-dd");
        ResourcePatternResolver resourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        // 加载目录下的所有文件
        Resource[] files =
                resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*/*/*/*.drl");
        for (Resource file : files) {
            // 采用kieHelper加载，便于后期动态变更规则
            KieUtil.addRule(ResourceFactory.newInputStreamResource(file.getInputStream()));
        }
        KieUtil.updateKieBase();
    }

    /**
     * KModule
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }

}
