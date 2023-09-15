package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.merchant.CouponProvider;
import com.czdx.grpc.lib.merchant.CouponProviderServiceGrpc;
import com.ruoyi.project.parking.domain.CouponDetailDayFact;
import com.ruoyi.project.parking.domain.vo.CouponDetailStatisticsVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.service.ICouponDetailDayFactService;
import com.ruoyi.project.parking.mapper.CouponDetailDayFactMapper;
import com.ruoyi.project.system.domain.SysDept;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【coupon_detail_day_fact(首页月发放优惠券统计事实表)】的数据库操作Service实现
 * @since 2023-03-27 10:16:38
 */
@Service
public class CouponDetailDayFactServiceImpl extends ServiceImpl<CouponDetailDayFactMapper, CouponDetailDayFact>
        implements ICouponDetailDayFactService {

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-member-merchant-server")
    private CouponProviderServiceGrpc.CouponProviderServiceBlockingStub couponProviderServiceBlockingStub;

    /**
     * 查询月发放优惠券统计
     *
     * @param parkNo 车场编号
     * @return 月发放优惠券统计
     */
    @Override
    public List<CouponDetailStatisticsVO> getCouponDetailStatistics(String parkNo) {
        List<CouponDetailStatisticsVO> result = new ArrayList<>();
        // 查询所有归属子车场编号
        List<String> parkNos = homePageMapper.getChildParkNosByNo(parkNo);
        // 当年的年份
        int year = LocalDate.now().getYear();
        LambdaQueryWrapper<CouponDetailDayFact> qw = new LambdaQueryWrapper<>();
        qw.eq(CouponDetailDayFact::getYear, year)
                .in(CouponDetailDayFact::getParkNo, parkNos);
        List<CouponDetailDayFact> list = list(qw);
        list.forEach(item -> {
            List<CouponDetailStatisticsVO> monthList = result.stream().filter(couponDetailStatisticsVO -> couponDetailStatisticsVO.getMonth() == item.getMonth()).toList();
            if (monthList.size() == 0) {
                CouponDetailStatisticsVO couponDetailStatisticsVO = new CouponDetailStatisticsVO();
                couponDetailStatisticsVO.setMonth(item.getMonth());
                couponDetailStatisticsVO.setCount(item.getCount());
                result.add(couponDetailStatisticsVO);
            } else {
                CouponDetailStatisticsVO couponDetailStatisticsVO = monthList.get(0);
                couponDetailStatisticsVO.setCount(couponDetailStatisticsVO.getCount() + item.getCount());
            }
        });
        return result;
    }

    @Override
    public void analyseCouponDetailDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 分析月发放优惠券事实
            LocalDate date = LocalDate.now().minusDays(1);
            String day = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // grpc请求会员子系统获取优惠券总数
            CouponProvider.AnalyseCouponDetailRequest request = CouponProvider.AnalyseCouponDetailRequest.newBuilder()
                    .setDay(day)
                    .setParkNo(park.getParkNo())
                    .build();
            CouponProvider.AnalyseCouponDetailResponse response = couponProviderServiceBlockingStub.analyseCouponDetailDayFact(request);
            LambdaQueryWrapper<CouponDetailDayFact> qw = new LambdaQueryWrapper<>();
            qw.eq(CouponDetailDayFact::getParkNo, park.getParkNo())
                    .eq(CouponDetailDayFact::getYear, response.getYear())
                    .eq(CouponDetailDayFact::getMonth, response.getMonth())
                    .last("limit 1");
            CouponDetailDayFact existFact = getOne(qw);
            if (null == existFact) {
                existFact = new CouponDetailDayFact();
                existFact.setParkNo(park.getParkNo());
                existFact.setDay(day);
                existFact.setMonth(date.getMonthValue());
                existFact.setYear(date.getYear());
                existFact.setCount(response.getCount());
                existFact.setCreateBy(userId);
                existFact.setCreateTime(LocalDateTime.now());
                save(existFact);
            } else {
                existFact.setDay(day);
                existFact.setCount(response.getCount());
                existFact.setUpdateBy(userId);
                existFact.setUpdateTime(LocalDateTime.now());
                updateById(existFact);
            }
        }
    }
}



