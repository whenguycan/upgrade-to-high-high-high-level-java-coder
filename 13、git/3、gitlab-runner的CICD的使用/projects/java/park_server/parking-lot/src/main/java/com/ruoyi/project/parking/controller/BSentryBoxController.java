package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.service.IRevenueStatisticsDayFactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 通道信息表;(b_passage)表控制层
 *
 * @date : 2023-2-21
 */
@Api(tags = "岗亭管理")
@RestController
@RequestMapping("/bSentryBox")
public class BSentryBoxController extends BaseController {

    @Autowired
    private RedisCache redisCache;
    @Resource
    private IRevenueStatisticsDayFactService revenueStatisticsDayFactService;

    /**
     * 新增数据
     *
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody List<String> passageIds) {
        String userName = getUsername();
        for (String id : passageIds) {
//            QueryWrapper<BPassageDevice> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("device_type",3).eq("passage_id",id);
//            BPassageDevice bPassageDevice=bPassageDeviceMapper.selectOne(queryWrapper);
            redisCache.setCacheObject(CacheConstants.SENTRYBOX_PASSAGE_KEY + userName + "_" + id, userName);
        }
        return toAjax(passageIds.size());
    }

    @ApiOperation("岗亭数据")
    @GetMapping("/getConsumptionAmountOfDay")
    public AjaxResult getAmountOfDay(String parkNo) {
        return AjaxResult.success(revenueStatisticsDayFactService.sumDisConsumptionAmountOfDay(LocalDate.now(), parkNo));
    }

    @ApiOperation("岗亭的空余车辆")
    @GetMapping("/getSpaceAmount")
    public AjaxResult getSpaceAmount(String parkNo) {
        Object spaceAmount = redisCache.getCacheObject(CacheConstants.PARKNO_ACCOUNT_KEY + parkNo);
        if (spaceAmount != null) {
            return success(spaceAmount);
        }
        return success(0);
    }


}
