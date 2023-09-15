package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.merchant.*;
import com.ruoyi.project.parking.domain.PayTypeDayFact;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.service.IBVisitorApplyManageService;
import com.ruoyi.project.parking.service.IEntryExitAnalysisDayFactService;
import com.ruoyi.project.parking.service.IPayTypeDayFactService;
import com.ruoyi.project.parking.service.ISheetService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SheetServiceImpl implements ISheetService {

    @Autowired
    IPayTypeDayFactService payTypeDayFactService;

    @Autowired
    IEntryExitAnalysisDayFactService entryExitAnalysisDayFactService;

    @GrpcClient("parking-member-merchant-server")
    VisitorApplyServiceGrpc.VisitorApplyServiceBlockingStub visitorApplyServiceBlockingStub;

    /**
     * @apiNote 支付类型报表
     */
    @Override
    public List<PayTypeDayFact> payTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO) {
        return payTypeDayFactService.getPayTypeStatisticsSheet(statisticsSheetVO);
    }

    /**
     * @apiNote 车流量报表
     */
    @Override
    public List<CarVolumeSheetBO> carVolumeSheet(CarVolumeSheetVO carVolumeSheetVO) {
        if ("1".equals(carVolumeSheetVO.getType())) {
            //按日统计
            return entryExitAnalysisDayFactService.getCarVolumeSheetDay(carVolumeSheetVO);
        } else {
            //按小时统计
            return entryExitAnalysisDayFactService.getCarVolumeSheetHour(carVolumeSheetVO);
        }
    }

    @Override
    public List<VisitorApplySheetBO> visitorApplyStatisticsSheet(StatisticsSheetVO statisticsSheetVO) {
        List<VisitorApplySheetBO> result = new ArrayList<>();
        // 组装查询参数，可以采用protojson工具类转换
        VisitorApplyStatisticsSheetRequestProto.Builder request = VisitorApplyStatisticsSheetRequestProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(request, statisticsSheetVO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VisitorApplyStatisticsSheetResponseProto responseProto = visitorApplyServiceBlockingStub.visitorApplyStatisticsSheet(request.build());
        List<VisitorApplyStatisticsSheet> visitApplyList = responseProto.getVisitApplyList();
        visitApplyList.forEach(item -> {
            try {
                result.add(ProtoJsonUtil.toPojoBean(VisitorApplySheetBO.class, item));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
