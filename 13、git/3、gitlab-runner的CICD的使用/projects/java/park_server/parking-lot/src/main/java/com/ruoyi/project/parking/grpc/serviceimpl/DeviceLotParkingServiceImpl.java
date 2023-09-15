package com.ruoyi.project.parking.grpc.serviceimpl;

import com.czdx.grpc.lib.lot.CaptureDeviceInfoResponse;
import com.czdx.grpc.lib.lot.DeviceLotParkingServiceGrpc;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import com.ruoyi.project.parking.grpc.model.vo.CaptureDeviceInfoModelVo;
import com.ruoyi.project.parking.service.SiteManageService;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@GrpcService
public class DeviceLotParkingServiceImpl extends DeviceLotParkingServiceGrpc.DeviceLotParkingServiceImplBase {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    SiteManageService siteManageService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @apiNote 验证当前车牌号在当前场库是否在黑名单中
     */
    @Override
    public void capture(com.czdx.grpc.lib.lot.CaptureDeviceInfoRequest request,
                        io.grpc.stub.StreamObserver<com.czdx.grpc.lib.lot.CaptureDeviceInfoResponse> responseObserver) {
        CaptureDeviceInfoResponse.Builder captureDeviceInfoResponse = CaptureDeviceInfoResponse.newBuilder();
        CaptureDeviceInfoModelVo info = new CaptureDeviceInfoModelVo();
        BeanUtils.copyProperties(request, info);
//        String parkNo = request.getParkNo();
//        String carNumber = request.getCarNumPic();
        //业务通道数据，离场无汽车类型
        ValidChannelVo validChannelVo = new ValidChannelVo();
        siteManageService.initValidCarData(validChannelVo, info);
        if (validChannelVo.getPassageId() == null) {
            logger.info("推送的通道Id：" + validChannelVo.getPassageId() + "不存在");
            captureDeviceInfoResponse.setStatus("500");
            captureDeviceInfoResponse.setMess(info.getDeviceLocalIp() + "不存在");
            responseObserver.onNext(captureDeviceInfoResponse.build());
            responseObserver.onCompleted();
            return;
        }
        //设置应用业务索要的数据
        info.setValidChannelVo(validChannelVo);
        //从场外进入场内
        if (CommonConstants.Entry_STATUS.equals(validChannelVo.getPassageFlag())) {
            logger.info("捕获预进场车牌号：" + validChannelVo.getCarNum());
            //预进场，判断车辆类型
            siteManageService.manageEnterGate(validChannelVo);

        } else if (CommonConstants.LEAVE_STATUS.equals(validChannelVo.getPassageFlag()) && CommonConstants.PARK_OUTER_LABEL.equals(validChannelVo.getToFieldName())) {
            //预离场- 里到场库外 -拉订单  -开门
            logger.info("捕获预出场车牌号：" + validChannelVo.getCarNum());
            siteManageService.managePreLeaveSiteGate(validChannelVo);

        }
        //推送websocket
        String passageIdStr = String.valueOf(validChannelVo.getPassageId());
        AsyncManager.me().execute(AsyncFactory.sendPassageMessage(passageIdStr, validChannelVo));
        Long size = redisTemplate.opsForList().size(passageIdStr);
        if (size != null && size > 0) {
            //拿出最新的一条记录
            List<ValidChannelVo> validChannelVoList = redisTemplate.opsForList().range(passageIdStr, -1, -1);
            if (CollectionUtils.isNotEmpty(validChannelVoList)) {
                String actTime = DateUtils.parseLocalDateToStr(validChannelVoList.get(0).getActTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                String day = DateUtils.parseLocalDateToStr(LocalDateTime.now(), DateUtils.YYYY_MM_DD);
                //不是同一天就删除
                if (actTime.indexOf(day) == -1) {
                    redisTemplate.delete(passageIdStr);
                }
            }
        }
        logger.info("推送的通道Id：" + validChannelVo.getPassageId());
        //将预进场的信息存入到当前的通道口中
        redisTemplate.opsForList().rightPush(passageIdStr, info.getValidChannelVo());
        captureDeviceInfoResponse.setStatus("200");
        captureDeviceInfoResponse.setStatus("success");
        responseObserver.onNext(captureDeviceInfoResponse.build());
        responseObserver.onCompleted();
    }


    /**
     *
     */
    @Override
    public void updateCarRecord(com.czdx.grpc.lib.lot.CaptureDeviceInfoRequest request,
                                io.grpc.stub.StreamObserver<com.czdx.grpc.lib.lot.CaptureDeviceInfoResponse> responseObserver) {
        ValidChannelVo validChannelVo = new ValidChannelVo();
        CaptureDeviceInfoResponse.Builder captureDeviceInfoResponse = CaptureDeviceInfoResponse.newBuilder();
        CaptureDeviceInfoModelVo info = new CaptureDeviceInfoModelVo();
        BeanUtils.copyProperties(request, info);
        //初始化业务通道数据
        siteManageService.initValidCarData(validChannelVo, info);
        if (validChannelVo.getPassageId() == null) {
            captureDeviceInfoResponse.setStatus("500");
            captureDeviceInfoResponse.setMess(validChannelVo.getDeviceIp() + "不存在");
            responseObserver.onNext(captureDeviceInfoResponse.build());
            responseObserver.onCompleted();
            return;
        }
        logger.info("闸门已经打开");
        if (CommonConstants.Entry_STATUS.equals(validChannelVo.getPassageFlag())) {
            siteManageService.addEntryRecord(validChannelVo);
            //从厂库外到场内，余量减1
            if (CommonConstants.PARK_OUTER_LABEL.equals(validChannelVo.getFromFieldName())) {
                siteManageService.updateCacheParkSpaceAmount(validChannelVo.getParkNo(), 1);
            }
            logger.info("车辆已经进场");
        } else if (CommonConstants.LEAVE_STATUS.equals(validChannelVo.getPassageFlag())) {
            if (CommonConstants.PARK_OUTER_LABEL.equals(validChannelVo.getToFieldName())) {
                siteManageService.addExitRecord(validChannelVo);
            }
            logger.info("车辆已经出场");
        } else {
            logger.info("闸门的作用无法确定");
        }
        responseObserver.onNext(captureDeviceInfoResponse.build());
        responseObserver.onCompleted();
    }
}
