package com.ll.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: tangwei
 * @Date: 2023/4/23 10:03 AM
 * @Description: 接口描述信息
 */
@FeignClient(name = "goods", url = "10.10.210.24:8092")
public interface GoodsFeign {

    @GetMapping("/index")
    public String getGoodsIndex();
}
