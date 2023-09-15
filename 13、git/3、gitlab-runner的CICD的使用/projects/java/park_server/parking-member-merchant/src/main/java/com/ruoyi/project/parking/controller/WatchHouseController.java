package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * H5我的车辆管理Controller
 *
 * @author fangch
 * @date 2023-03-02
 */
@RestController
@RequestMapping("/parking/watchHouse")
public class WatchHouseController extends BaseController {



    @Resource
    private RedisCache redisCache;
    @GetMapping("/getCarNoByPassage")
    public AjaxResult getCarNoByPassage(String passageNo,String parkNo){
        //查询redis有没有预出场车牌号
        String carNumberKey = CacheConstants.PARKNO_PASSAGE_KEY +
                parkNo +
                "_" +
                passageNo;
        return success(redisCache.getCacheObject(carNumberKey));
    }




}
