package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.merchant.CountBVisitorApplyRequest;
import com.czdx.grpc.lib.merchant.CountBVisitorApplyResponse;
import com.czdx.grpc.lib.merchant.VisitorApplyServiceGrpc;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.domain.vo.GISStatisticsVO;
import com.ruoyi.project.parking.domain.vo.ParkVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.service.BFieldService;
import com.ruoyi.project.parking.service.IBigScreenService;
import com.ruoyi.project.parking.service.IEntryExitAnalysisDayFactService;
import com.ruoyi.project.parking.service.IRevenueStatisticsDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BigScreenServiceImpl implements IBigScreenService {

    @Autowired
    private IRevenueStatisticsDayFactService revenueStatisticsDayFactService;

    @Autowired
    private IEntryExitAnalysisDayFactService entryExitAnalysisDayFactService;

    @Autowired
    private HomePageMapper homePageMapper;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    BFieldService fieldService;

    @Autowired
    RedisCache redisCache;

    @GrpcClient("parking-member-merchant-server")
    private VisitorApplyServiceGrpc.VisitorApplyServiceBlockingStub visitorApplyServiceBlockingStub;

    /**
     * GIS数据统计
     */
    @Override
    public GISStatisticsVO getGISStatistics(String parkNo) {
        GISStatisticsVO gisStatisticsVO = new GISStatisticsVO();
        // 查询所有归属子车场编号
        List<String> parkNos = homePageMapper.getChildParkNosByNo(parkNo);
        //当前日期
        LocalDate now = LocalDate.now();
        //查询本年收入
        LocalDate startOfYear = now.withDayOfYear(1);
        LocalDate endOfYear = now.plusYears(1).withDayOfYear(1);
        BigDecimal sumAmountOfYear = revenueStatisticsDayFactService.sumAmountOfYear(startOfYear, endOfYear, parkNos);
        gisStatisticsVO.setYearAmount(sumAmountOfYear);
        //查询当日收入
        BigDecimal sumAmountOfDay = revenueStatisticsDayFactService.sumAmountOfDay(now, parkNos);
        gisStatisticsVO.setDayAmount(sumAmountOfDay);
        //查询今日访客，只统计已审核通过的访客
        CountBVisitorApplyRequest request = CountBVisitorApplyRequest.newBuilder()
                .addAllParkNos(parkNos)
                .setDay(now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .setStatus("1")
                .build();
        CountBVisitorApplyResponse response = visitorApplyServiceBlockingStub.countBVisitorApply(request);
        gisStatisticsVO.setDayVisitor(response.getCount());
        //查询进出车辆
        int entryOfDay = entryExitAnalysisDayFactService.sumEntryOfDay(now, parkNos);
        int exitOfDay = entryExitAnalysisDayFactService.sumExitOfDay(now, parkNos);
        gisStatisticsVO.setTurnoverNum(entryOfDay + exitOfDay);
        gisStatisticsVO.setEntryNum(entryOfDay);
        gisStatisticsVO.setExitNum(exitOfDay);
        return gisStatisticsVO;
    }

    /**
     * 车场状态数据统计
     */
    @Override
    public List<ParkVO> getParkInfo(String parkNo) {
        List<ParkVO> result = new ArrayList<>();
        // 查询所有归属子车场编号
        List<String> parkNos = homePageMapper.getChildParkNosByNo(parkNo);
        parkNos.forEach(no -> {
            SysDept sysDept = sysDeptService.selectDeptByParkNo(no);
            ParkVO parkVO = new ParkVO();
            parkVO.setParkId(sysDept.getDeptId());
            parkVO.setParkNo(sysDept.getParkNo());
            parkVO.setParkName(sysDept.getDeptName());
            parkVO.setLatitude(sysDept.getLatitude());
            parkVO.setLongitude(sysDept.getLongitude());
            //车场状态根据车位数确定
            //总车位数
            BigDecimal total = BigDecimal.ZERO;
            BField field = new BField();
            field.setFieldStatus("1");
            field.setParkNo(sysDept.getParkNo());
            List<BField> fieldList = fieldService.findList(field);
            for (BField bField : fieldList) {
                total = total.add(BigDecimal.valueOf(bField.getSpaceCount()));
            }
            if (total.equals(BigDecimal.ZERO)) {
                parkVO.setStatus(DictUtils.getDictValue("parking_status", "已满"));
            } else {
                //空闲车位数
                BigDecimal idle = BigDecimal.valueOf(redisCache.getCacheObject(CacheConstants.PARKNO_ACCOUNT_KEY + sysDept.getParkNo()));
                double idlePercent = idle.divide(total, 2, RoundingMode.CEILING).doubleValue();
                if (idle.equals(total)) {
                    parkVO.setStatus(DictUtils.getDictValue("parking_status", "已满"));
                } else if (idlePercent >= 0.3) {
                    parkVO.setStatus(DictUtils.getDictValue("parking_status", "空闲"));
                } else {
                    parkVO.setStatus(DictUtils.getDictValue("parking_status", "较少"));
                }
            }
            result.add(parkVO);
        });
        return result;
    }
}
