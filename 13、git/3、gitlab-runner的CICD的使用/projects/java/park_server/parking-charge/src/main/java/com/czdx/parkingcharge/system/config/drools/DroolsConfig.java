package com.czdx.parkingcharge.system.config.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * description: Drools 配置文件
 *
 * @author mingchenxu
 * @date 2023/3/2 15:28
 */
@Slf4j
//@Configuration
public class DroolsConfig {

    //指定规则文件存放的目录
    private static final String RULES_PATH = "rules/";

    private final KieServices kieServices = KieServices.Factory.get();

    /**
     * 文件系统
     * @return
     * @throws IOException
     */
    @Bean
    @ConditionalOnMissingBean
    public KieFileSystem kieFileSystem() throws IOException {
        System.setProperty("drools.dateformat","yyyy-MM-dd");
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        ResourcePatternResolver resourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        // 加载目录下的所有文件
        Resource[] files =
                resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*/*/*/*.drl");
        for (Resource file : files) {
//            kieFileSystem.write(ResourceFactory.newFileResource(file.getFile()));
            kieFileSystem.write("src/main/resources/" + UUID.randomUUID().toString() + file.getFilename() + ".drl", ResourceFactory.newInputStreamResource(file.getInputStream()));
        }
        return kieFileSystem;
    }

    /**
     * KIE容器
     * @return
     * @throws IOException
     */
    @Bean
    @ConditionalOnMissingBean
    public KieContainer kieContainer() throws IOException {
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    /**
     * KIEBASE
     * @return
     * @throws IOException
     */
    @Bean
    @ConditionalOnMissingBean
    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
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
