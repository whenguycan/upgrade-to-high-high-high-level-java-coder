package com.ruoyi.project.parking.controller;

import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;
import com.ruoyi.project.parking.domain.vo.TExitRecordsVo;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.mapper.BPassageDeviceMapper;
import com.ruoyi.project.parking.mapper.BPassageMapper;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.ITEntryRecordsService;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import com.ruoyi.project.parking.service.grpcclient.DeviceGrpcClientService;
import com.ruoyi.project.parking.service.grpcclient.model.OpenCloseChannelRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Resource
    private BPassageDeviceMapper bPassageDeviceMapper;
    @Resource
    private DeviceGrpcClientService deviceGrpcClientService;

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
     * 查询车辆进场记录列表
     */
    @ApiOperation("查询车辆进场记录列表")
    @GetMapping("/exitList")
    public TableDataInfo exitList(TExitRecordsVo tExitRecordsVo) {
        startPage();
        List<TExitRecords> list = tExitRecordsService.selectTExitRecordsList(tExitRecordsVo);
        return getDataTable(list);
    }

    /**
     * 修改入场车牌信息
     */
    @Log(title = "修改入场车牌信息", businessType = BusinessType.UPDATE)
    @PutMapping("/editEntryCarNumber")
    @ApiOperation("修改入场车牌信息")
    public AjaxResult editEntryCarNumber(@RequestBody TEntryRecords tEntryRecords) {
        if (isCarNo(tEntryRecords.getCarNumberEdit())){
            int count = tEntryRecordsService.updateTEntryRecords(tEntryRecords);
            if (count > 0) {
                // 修改车牌号
                iParkLiveRecordsService.updateEntryCarNumberByParkNoCarNumber(tEntryRecords.getCarNumberEdit(), tEntryRecords.getParkNo(), tEntryRecords.getCarNumber());
            }
            return toAjax(count);
        }else {
            return AjaxResult.error("车牌号不正确");
        }

    }

    /**
     * 修改出场车牌信息
     */
    @Log(title = "修改出场车牌信息", businessType = BusinessType.UPDATE)
    @PutMapping("/editExitCarNumber")
    @ApiOperation("修改出场车牌信息")
    public AjaxResult editExitCarNumber(@RequestBody TExitRecords tExitRecords) {
        if (isCarNo(tExitRecords.getCarNumberEdit())){
            boolean flag = iParkLiveRecordsService.updateExitCarNumberByParkNoCarNumber(tExitRecords.getCarNumberEdit(), tExitRecords.getParkNo(), tExitRecords.getCarNumber());
            if (flag) {
                String passageNo = tExitRecords.getParkNo();
                redisCache.setCacheObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
                        + "_" + passageNo, tExitRecords.getCarNumber());
            }
            return toAjax(flag);
        }else {
            return AjaxResult.error("车牌号不正确");
        }

    }

    /**
     * 批量修改车辆进场时间记录
     */
    @Log(title = "批量修改车辆进场时间记录", businessType = BusinessType.UPDATE)
    @PutMapping("/editEntryTime")
    @ApiOperation("批量修改车辆进场时间记录")
    public AjaxResult editList(@RequestBody List<TEntryRecords> tEntryRecords) {
        int count = 0;
        String userName = getUsername();
        for (TEntryRecords entryRecords : tEntryRecords) {
            entryRecords.setUpdateBy(userName);
            count += tEntryRecordsService.updateTEntryRecords(entryRecords);

            String entryTime = DateUtils.parseLocalDateToStr(entryRecords.getEntryTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
            iParkLiveRecordsService.updateEntryTimeByParkNoCarNumber(
                    DateUtils.toLocalDateTime(entryTime, DateUtils.YYYY_MM_DD_HH_MM_SS),
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
    public AjaxResult add(@RequestBody TEntryRecords tEntryRecords) {
        if (isCarNo(tEntryRecords.getCarNumber())){
            String entryTime = DateUtils.parseLocalDateToStr( tEntryRecords.getEntryTime(),DateUtils.YYYY_MM_DD_HH_MM_SS);
            ParkLiveRecords parkLiveRecords = iParkLiveRecordsService.queryByParkNoCarNumber(tEntryRecords.getParkNo(), tEntryRecords.getCarNumber());
            if (parkLiveRecords != null) {
                return error("车牌[" + parkLiveRecords.getCarNumber() + "] 已在场库中");
            }
            //插入在场记录表
            Integer parkLiveId = iParkLiveRecordsService.insertParkLiveRecords(tEntryRecords.getParkNo(),
                    tEntryRecords.getCarNumber(),
                    tEntryRecords.getCarType(),
                    DateUtils.toLocalDateTime(entryTime, DateUtils.YYYY_MM_DD_HH_MM_SS));
            //进场记录表关联在场记录表id
            tEntryRecords.setParkLiveId(parkLiveId);
            tEntryRecords.setPassageId(tEntryRecords.getId());
            BPassageDeviceVo bPassageDeviceVo = new BPassageDeviceVo();
            bPassageDeviceVo.setPassageId(tEntryRecords.getPassageId());
            bPassageDeviceVo.setDeviceType(CommonConstants.P_Channel_Device);
            List<BPassageDeviceVo> bPassageDeviceVos = bPassageDeviceMapper.selectBPassageDeviceList(bPassageDeviceVo);
            if (CollectionUtils.isNotEmpty(bPassageDeviceVos)) {
                tEntryRecords.setDeviceId(bPassageDeviceVos.get(0).getDeviceId());
            }
            tEntryRecords.setCarNumberEdit(tEntryRecords.getCarNumber());
            //插入进场记录表
            return toAjax(tEntryRecordsService.insertTEntryRecords(tEntryRecords));
        }else {
            return AjaxResult.error("车牌号不正确");
        }

    }

    /**
     * 修改停车场空位
     */
    @Log(title = "修改停车场空位", businessType = BusinessType.INSERT)
    @GetMapping("/editParkingSpace")
    @ApiOperation("修改停车场空位")
    public AjaxResult editParkingSpace(String numberStr, String parkNo) {
        if(StringUtils.isNotBlank(numberStr)) {
            int number=Integer.parseInt(numberStr);
            redisCache.setCacheObject(CacheConstants.PARKNO_ACCOUNT_KEY + parkNo, number);
        }
        return AjaxResult.success();
    }

    /**
     * 开关闸
     */
    @Log(title = "开关闸", businessType = BusinessType.INSERT)
    @PostMapping("/openCloseChannel")
    @ApiOperation("开关闸")
    public AjaxResult openCloseChannel(String passageNo,String parkNo,String carNumber,int operateType) throws ClientException {
        BPassageDeviceVo bPassageDeviceVo = bPassageDeviceMapper.selectBPassageDeviceByPassageNo(passageNo);
        OpenCloseChannelRequest openCloseChannelRequest = new OpenCloseChannelRequest();
        openCloseChannelRequest.setStatus("1");
        openCloseChannelRequest.setDeviceIp(bPassageDeviceVo.getServerIp());
        openCloseChannelRequest.setParkNo(parkNo);
//            openCloseChannelRequest.setEntryOrExitId(entrySiteInfo.getEntryOrExitId());
        openCloseChannelRequest.setCarNum(carNumber);
        deviceGrpcClientService.openCloseChannel(openCloseChannelRequest);
        return AjaxResult.success();
    }

    public static boolean isCarNo(String carNo){
        if (carNo.length() >= 7 && carNo.length() <= 8){
            Pattern p = Pattern.compile("^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$");
            Matcher m = p.matcher(carNo);
            if (!m.matches()){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

}
