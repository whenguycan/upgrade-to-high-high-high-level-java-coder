package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.common.constants.BUStr;
import com.czdx.parkingcharge.domain.ParkChargeScheme;
import com.czdx.parkingcharge.service.ParkChargeSchemeService;
import com.czdx.parkingcharge.mapper.ParkChargeSchemeMapper;
import com.czdx.parkingcharge.system.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_scheme(停车场收费方案表)】的数据库操作Service实现
* @createDate 2023-03-14 09:42:04
*/
@RequiredArgsConstructor
@Service
public class ParkChargeSchemeServiceImpl extends ServiceImpl<ParkChargeSchemeMapper, ParkChargeScheme>
    implements ParkChargeSchemeService{

    private final RedisCache redisCache;

    /**
     *
     * description: 初始化时，重新加载计费约束
     * @author mingchenxu
     * @date 2023/3/21 10:48
     */
    @PostConstruct
    public void initChargeScheme() {
        // 删除已缓存的约束
        redisCache.deleteObject(BUStr.R_PARKE_LOT_CHARGE_SCHEMA_PREFIX + "*");
        // 重新缓存约束
        cacheParkLotChargeScheme(null);
    }

    /**
     *
     * description: 缓存车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 10:45
     */
    @Override
    public void cacheParkLotChargeScheme(String parkNo) {
        LambdaQueryWrapper<ParkChargeScheme> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(parkNo)) {
            wrapper.eq(ParkChargeScheme::getParkNo, parkNo);
        }
        List<ParkChargeScheme> schemes = baseMapper.selectList(wrapper);
        // 缓存到redis
        if (CollectionUtils.isNotEmpty(schemes)) {
            schemes.forEach(this::cacheScheme2Redis);
        }
    }

    /**
     *
     * description: 刷新车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 11:06
     * @param parkNo 车场编号
     * @return int
     */
    @Override
    public int refreshParkLotChargeScheme(String parkNo) {
        ParkChargeScheme chargeScheme = null;
        if (StringUtils.isNotEmpty(parkNo)) {
            chargeScheme = refreshChargeScheme(parkNo);
        }
        return chargeScheme != null ? 1 : 0;
    }

    /**
     *
     * description: 获取计费约束
     * @author mingchenxu
     * @date 2023/3/21 10:51
     * @param parkNo 车场编号
     * @return com.czdx.parkingcharge.domain.ParkChargeScheme
     */
    @Override
    public Optional<ParkChargeScheme> getChargeScheme(String parkNo) {
        ParkChargeScheme chargeScheme = null;
        if (StringUtils.isNotEmpty(parkNo)) {
            chargeScheme = getSchemeFromRedis(parkNo);
            if (chargeScheme == null) {
                chargeScheme = refreshChargeScheme(parkNo);
            }
        }
        return Optional.ofNullable(chargeScheme);
    }

    /**
     *
     * description: 刷新计费约束
     * @author mingchenxu
     * @date 2023/3/21 11:07
     * @param parkNo 车场编号
     * @return com.czdx.parkingcharge.domain.ParkChargeScheme
     */
    private ParkChargeScheme refreshChargeScheme(String parkNo) {
        ParkChargeScheme chargeScheme;
        // 数据库查询，缓存redis
        LambdaQueryWrapper<ParkChargeScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkChargeScheme::getParkNo, parkNo);
        chargeScheme = baseMapper.selectOne(wrapper);
        if (chargeScheme != null) {
            cacheScheme2Redis(chargeScheme);
        }
        return chargeScheme;
    }

    /**
     *
     * description: 缓存计费约束到redis
     * @author mingchenxu
     * @date 2023/3/21 10:45
     * @param psc 计费约束
     */
    private void cacheScheme2Redis(ParkChargeScheme psc) {
        redisCache.setCacheObject(BUStr.R_PARKE_LOT_CHARGE_SCHEMA_PREFIX + psc.getParkNo(), psc);
    }

    /**
     *
     * description: 获取计费约束从redis
     * @author mingchenxu
     * @date 2023/3/21 10:45
     * @param parkNo 车场编号
     */
    private ParkChargeScheme getSchemeFromRedis(String parkNo) {
        return redisCache.getCacheObject(BUStr.R_PARKE_LOT_CHARGE_SCHEMA_PREFIX + parkNo);
    }

}




