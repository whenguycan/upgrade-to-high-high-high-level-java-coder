package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.dahuavo.EntrySiteInfo;
import com.ruoyi.project.parking.domain.dahuavo.LeaveSiteInfo;

import javax.websocket.EncodeException;
import java.io.IOException;

public interface SiteManageService {



    /**
     *  管理进入场地
     *
     * @param entrySiteInfo 进场信息
     * @return
     */
    void manageArriveSiteGate(EntrySiteInfo entrySiteInfo) throws EncodeException, IOException;

    /**
     *  管理离开场地
     *
     * @param leaveSiteInfo 出场信息
     * @return
     */
    void manageLeaveSiteGate(LeaveSiteInfo leaveSiteInfo) throws EncodeException, IOException;


}
