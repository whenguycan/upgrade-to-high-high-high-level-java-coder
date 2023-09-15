package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.constant.WarnMessageModel;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.service.IBPassageDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * 通道设备绑定Controller
 *
 * @author mzl
 * @date 2023-02-23
 */
@Api(tags = "通道设备绑定")
@RestController
@RequestMapping("/parking/device")
public class BPassageDeviceController extends BaseController {
    @Autowired
    private IBPassageDeviceService bPassageDeviceService;

    public static final String DEVICE_CODE_LABEL = "设备编号";

    /**
     * 查询通道设备绑定列表
     */
    @ApiOperation("查询通道设备绑定列表")
    @GetMapping("/list")
    public TableDataInfo list(BPassageDeviceVo bPassageDevice) {
        startPage();
        List list=null;
        if(StringUtils.isEmpty(bPassageDevice.getDeviceStatus())){
            list=bPassageDeviceService.selectDeviceList();
        }else{
            if("0".equals(bPassageDevice.getDeviceStatus())){
                bPassageDevice.setParkNo(null);
            }
            list = bPassageDeviceService.selectBPassageDeviceList(bPassageDevice);
        }
        return getDataTable(list);
    }

    /**
     * 导出通道设备绑定列表
     */

    /**
     * 获取通道设备绑定详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取通道设备绑定详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bPassageDeviceService.selectBPassageDeviceById(id));
    }

    /**
     * 新增通道设备绑定
     */
    @Log(title = "通道设备绑定", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation("新增通道设备绑定")
    public AjaxResult add(@RequestBody BPassageDevice bPassageDevice) {
        if (bPassageDeviceService.countDeviceByDeviceCode(bPassageDevice.getDeviceId()) > 0) {
            return error(MessageFormat.format(WarnMessageModel.DATABASE_EXIST_TIP, DEVICE_CODE_LABEL, bPassageDevice.getDeviceId()));
        }
        bPassageDevice.setDeviceStatus("0");
        return toAjax(bPassageDeviceService.insertBPassageDevice(bPassageDevice));
    }

    /**
     * 修改通道设备绑定
     */
    @Log(title = "通道设备绑定", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    @ApiOperation("修改通道设备绑定")
    public AjaxResult edit(@RequestBody BPassageDevice bPassageDevice) {
        return toAjax(bPassageDeviceService.updateBPassageDevice(bPassageDevice));
    }

    /**
     * 删除通道设备绑定
     */
    @Log(title = "通道设备绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除通道设备绑定")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bPassageDeviceService.deleteBPassageDeviceByIds(ids));
    }
}
