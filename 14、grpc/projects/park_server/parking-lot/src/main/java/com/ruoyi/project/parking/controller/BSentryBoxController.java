package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通道信息表;(b_passage)表控制层
 * @date : 2023-2-21
 */
@Api(tags = "岗亭管理")
@RestController
@RequestMapping("/bSentryBox")
public class BSentryBoxController extends BaseController {

    @Autowired
    private RedisCache redisCache;
    /**
     * 新增数据
     *
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody List<String> passageIds){
        String userName=getUsername();
        for (String id:passageIds){
            redisCache.setCacheObject(CacheConstants.SENTRYBOX_PASSAGE_KEY+userName+"_"+id,userName);
        }
        return toAjax(passageIds.size());
    }

}
