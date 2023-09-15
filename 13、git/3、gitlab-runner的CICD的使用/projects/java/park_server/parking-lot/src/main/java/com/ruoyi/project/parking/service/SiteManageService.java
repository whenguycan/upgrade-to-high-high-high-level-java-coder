package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.dahuavo.CarCaptureData;
import com.ruoyi.project.parking.domain.dahuavo.EntrySiteInfo;
import com.ruoyi.project.parking.domain.dahuavo.LeaveSiteInfo;
import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import com.ruoyi.project.parking.grpc.model.vo.CaptureDeviceInfoModelVo;

import javax.websocket.EncodeException;
import java.io.IOException;

public interface SiteManageService {

     void initValidCarData(ValidChannelVo validChannelVo, CaptureDeviceInfoModelVo info) ;

        /**
         * 更新车厂余量
         *
         * @param parkNo
         * @param siteFlag
         */
    void updateCacheParkSpaceAmount(String parkNo, int siteFlag);

    /**
     * 添加出场信息
     *
     * @param validChannelVo
     */
    void addExitRecord(ValidChannelVo validChannelVo);

    /**
     * 添加入场信息
     *
     * @param validChannelVo
     */
    void addEntryRecord(ValidChannelVo validChannelVo);

    void manageEnterGate(ValidChannelVo entrySiteInfo);

//    public void addParkLiveOrder(Long entryId, String parkNo, String carNumber, String carType, String entryFlag);

    /**
     *  管理进入场地
     *
     * @param entrySiteInfo 进场信息
     * @return
     */
//    void manageArriveSiteGate(EntrySiteInfo entrySiteInfo) throws EncodeException, IOException;

    /**
     * 预进场
     *
     * @param entrySiteInfo
     * @throws EncodeException
     * @throws IOException
     */
    void managePreArriveSiteGate(ValidChannelVo entrySiteInfo);

    /**
     * 已经进场
     *
     * @param entrySiteInfo
     * @throws EncodeException
     * @throws IOException
     */
    void manageAfterEnterSiteGate(ValidChannelVo entrySiteInfo) throws EncodeException, IOException;


    /**
     * 已经进场
     *
     * @param entrySiteInfo
     * @throws EncodeException
     * @throws IOException
     */
    void manageLeaveSiteGate(ValidChannelVo entrySiteInfo);

    void updateLeaveRecord(Long exitId, String passageNo);

    /**
     * 预离场
     *
     * @param carCaptureData 预离场
     * @return
     */
    void
    managePreLeaveSiteGate(ValidChannelVo carCaptureData);


}
