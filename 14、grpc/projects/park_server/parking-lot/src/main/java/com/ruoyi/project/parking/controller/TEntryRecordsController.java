package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.service.IBPassageDeviceService;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.ITEntryRecordsService;
import com.ruoyi.project.parking.websocket.Entry.WebSocketServerEntryPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 车辆进场记录Controller
 *
 * @author mzl
 * @date 2023-02-22
 */
@Api(tags = "车辆进场记录")
@RestController
@RequestMapping("/entryrecords")
public class TEntryRecordsController extends BaseController {
    @Autowired
    private ITEntryRecordsService tEntryRecordsService;

    @Resource
    RedisCache redisCache;

    /**
     * 获取车辆进场记录详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取车辆进场记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tEntryRecordsService.selectTEntryRecordsById(id));
    }

    /**
     * 新增车辆进场记录
     */
    @Log(title = "车辆进场记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation("新增车辆进场记录")
    public AjaxResult add(@RequestBody TEntryRecords tEntryRecords) throws EncodeException, IOException {

        Collection<String> keys = redisCache.keys
                (CacheConstants.SENTRYBOX_PASSAGE_KEY + "*" + "_" + tEntryRecords.getPassageId());
        for (String key : keys) {
            String sentryBoxName = key.replace("sentrybox_passage:", "").replace("_" + tEntryRecords.getPassageId(), "");
            WebSocketServerEntryPool.sendMessageByPassageId(sentryBoxName, tEntryRecords);

        }
        return toAjax(tEntryRecordsService.insertTEntryRecords(tEntryRecords));
    }

    /**
     * 删除车辆进场记录
     */
    @Log(title = "车辆进场记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除车辆进场记录")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tEntryRecordsService.deleteTEntryRecordsByIds(ids));
    }
}
