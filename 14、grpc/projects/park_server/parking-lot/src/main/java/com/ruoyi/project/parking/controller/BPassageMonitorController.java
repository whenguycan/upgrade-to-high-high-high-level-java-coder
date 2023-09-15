package com.ruoyi.project.parking.controller;

import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.mapper.BPassageMapper;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.ITEntryRecordsService;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 监控页面
 *
 * @author ruoyi
 * @date 2023-02-22
 */
@Api(tags = "监控页面")
@RestController
@RequestMapping("/passageMonitor")
public class BPassageMonitorController extends BaseController {
    @Autowired
    private ITEntryRecordsService tEntryRecordsService;
    @Autowired
    private IParkLiveRecordsService iParkLiveRecordsService;
    @Autowired
    private ITExitRecordsService tExitRecordsService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IpmsDevice ipmsDevice;
    @Autowired
    private BPassageMapper bPassageMapper;
    /**
     * 查询车辆进场记录列表
     */
    @ApiOperation("查询车辆进场记录列表")
    @GetMapping("/list")
    public TableDataInfo list(TEntryRecordsVo tEntryRecordsVo) {
        startPage();
        List<TEntryRecords> list = tEntryRecordsService.selectTEntryRecordsList(tEntryRecordsVo);
        return getDataTable(list);
    }

    /**
     * 修改入场车牌信息
     */
    @Log(title = "修改入场车牌信息", businessType = BusinessType.UPDATE)
    @PutMapping("/editEntryCarNumber")
    @ApiOperation("修改入场车牌信息")
    public AjaxResult editEntryCarNumber(@RequestBody TEntryRecords tEntryRecords) {
        int count=tEntryRecordsService.updateTEntryRecords(tEntryRecords);
        if (count>0){
            // 修改车牌号
            iParkLiveRecordsService.updateCarNumberByParkNoCarNumber
                    (
                            tEntryRecords.getCarNumberEdit(),
                    tEntryRecords.getParkNo(),
                            tEntryRecords.getCarNumber());
        }
        return toAjax(count);
    }

    /**
     * 修改出场车牌信息
     */
    @Log(title = "修改出场车牌信息", businessType = BusinessType.UPDATE)
    @PutMapping("/editExitCarNumber")
    @ApiOperation("修改出场车牌信息")
    public AjaxResult editExitCarNumber(@RequestBody TExitRecords tExitRecords) {
        boolean flag = iParkLiveRecordsService.updateCarNumberByParkNoCarNumber
                (
                        tExitRecords.getCarNumberEdit(),
                        tExitRecords.getParkNo(),
                        tExitRecords.getCarNumber());
        if (flag){
            String passageNo=bPassageMapper.selectById(tExitRecords.getPassageId()).getPassageNo();
            redisCache.setCacheObject(CacheConstants.PARKNO_PASSAGE_KEY+tExitRecords.getParkNo()
                    +"_"+passageNo,tExitRecords.getCarNumber());
        }
        return toAjax(flag);
    }

    /**
     * 批量修改车辆进场时间记录
     */
    @Log(title = "批量修改车辆进场时间记录", businessType = BusinessType.UPDATE)
    @PutMapping("/editEntryTime")
    @ApiOperation("批量修改车辆进场时间记录")
    public AjaxResult editList(@RequestBody List<TEntryRecords> tEntryRecords) {
        int count=0;
        String userName=getUsername();
        for (TEntryRecords entryRecords:tEntryRecords){
            entryRecords.setUpdateBy(userName);
            count+=tEntryRecordsService.updateTEntryRecords(entryRecords);

            String entryTime=DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,entryRecords.getEntryTime());
            iParkLiveRecordsService.updateEntryTimeByParkNoCarNumber(
                    DateUtils.toLocalDateTime(entryTime,DateUtils.YYYY_MM_DD_HH_MM_SS),
                    entryRecords.getParkNo(),
                    entryRecords.getCarNumberEdit()
            );
        }
        return toAjax(count);
    }

    /**
     * 补录入场信息
     */
    @Log(title = "补录入场信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("补录入场信息")
    public AjaxResult add(@RequestBody TEntryRecords tEntryRecords)
    {
        String entryTime=DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,tEntryRecords.getEntryTime());
        //插入在场记录表
        Integer parkLiveId=iParkLiveRecordsService.insertParkLiveRecords(tEntryRecords.getParkNo(),
                tEntryRecords.getCarNumber(),
                tEntryRecords.getCarType(),
                DateUtils.toLocalDateTime(entryTime,DateUtils.YYYY_MM_DD_HH_MM_SS));
        //进场记录表关联在场记录表id
        tEntryRecords.setParkLiveId(parkLiveId);
        //插入进场记录表
        return toAjax(tEntryRecordsService.insertTEntryRecords(tEntryRecords));
    }

    /**
     * 修改停车场空位
     */
    @Log(title = "修改停车场空位", businessType = BusinessType.INSERT)
    @PostMapping("/editParkingSpace")
    @ApiOperation("修改停车场空位")
    public AjaxResult editParkingSpace(String number,String parkNo){
        redisCache.setCacheObject(CacheConstants.PARKNO_ACCOUNT_KEY+parkNo,number);
        return AjaxResult.success();
    }

    /**
     * 开关闸
     */
    @Log(title = "开关闸", businessType = BusinessType.INSERT)
    @PostMapping("/openCloseChannel")
    @ApiOperation("开关闸")
    public AjaxResult openCloseChannel(String channelId,int operateType) throws ClientException {
        return AjaxResult.success(ipmsDevice.OpenCloseChannel(channelId,operateType,null));
    }


    /**
     * 收费开闸
     */
    @Log(title = "收费开闸", businessType = BusinessType.INSERT)
    @PostMapping("/payOpemChannel")
    @ApiOperation("收费开闸")
    public AjaxResult payOpemChannel(TEntryRecordsVo tEntryRecordsVo) throws ClientException {
        //todo 订单信息处理
        ipmsDevice.OpenCloseChannel(tEntryRecordsVo.getPassageId().toString(),1,null);
        return AjaxResult.success(iParkLiveRecordsService.manualComputation(tEntryRecordsVo.getParkNo(),tEntryRecordsVo.getCarNumber(),tEntryRecordsVo.getPayAmount(),tEntryRecordsVo.getLiftGateReason()));
    }



}
