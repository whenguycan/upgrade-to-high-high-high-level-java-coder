package com.ll.goods.controller;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: tangwei
 * @Date: 2023/4/21 3:07 PM
 * @Description: 类描述信息
 */
@RestController
public class IndexController {

    @Autowired
    private Environment env;

    @GetMapping("/index")
    public String idnex(){

        return "goods-project-index-function";
    }


    @Autowired
    private NacosConfigManager nacosConfigManager;

    @GetMapping("/index2")
    public void index2() throws NacosException {
        //发布配置文件到nacos管理端
        nacosConfigManager.getConfigService().publishConfig("oo.yaml", "DEFAULT_GROUP", "height: 187", "yaml");
    }
}


