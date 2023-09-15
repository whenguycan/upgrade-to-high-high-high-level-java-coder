package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.dahuavo.DaHuaResData;
import com.ruoyi.project.parking.domain.dahuavo.EntrySiteInfo;
import com.ruoyi.project.parking.domain.dahuavo.LeaveSiteInfo;
import com.ruoyi.project.parking.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;

/**
 *  对接大华设备信息的接口
 *  mzl
 */
@RestController
@RequestMapping("/SiteInfo")
public class SiteInfoController {

    @Resource
    SiteManageService siteManageService;

    /**
     * @param entrySiteInfo
     * @return
     */
    //，过杆前和过杆后是不一样的；
    @PostMapping("/getEntrySiteInfo")
    public AjaxResult getEntrySiteInfo(@RequestBody EntrySiteInfo entrySiteInfo) throws EncodeException, IOException {
        siteManageService.manageArriveSiteGate(entrySiteInfo);
        DaHuaResData daHuaResData = new DaHuaResData("isOpen");
        return new AjaxResult("000000", "success", true, daHuaResData);
    }

    /**
     * 离开停车场
     *
     * @param leaveSiteInfo
     * @return 不开闸门
     */

    @PostMapping("/getLeaveSiteInfo")
    //大华的设备 调用几次这个接口，过杆前和过杆后是不一样的；
    public AjaxResult getLeaveSiteInfo(@RequestBody LeaveSiteInfo leaveSiteInfo) throws EncodeException, IOException {
        siteManageService.manageLeaveSiteGate(leaveSiteInfo);
        return new AjaxResult();

    }

}
