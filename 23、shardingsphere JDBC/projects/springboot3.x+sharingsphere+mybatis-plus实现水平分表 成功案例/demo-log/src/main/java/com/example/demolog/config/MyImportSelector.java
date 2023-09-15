package com.example.demolog.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/13 1:22 PM
 * @Description: 类描述信息
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] beans = new String[]{
            "com.example.demolog.config.Duck"
        };

        return beans;
    }
}
