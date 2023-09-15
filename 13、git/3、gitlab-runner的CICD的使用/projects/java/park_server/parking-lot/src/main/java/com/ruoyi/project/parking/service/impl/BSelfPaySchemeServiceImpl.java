package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.domain.param.BSelfPaySchemeParam;
import com.ruoyi.project.parking.mapper.BSelfPaySchemeMapper;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import org.springframework.stereotype.Service;

/**
 * @author 琴声何来
 * @description 针对表【b_self_pay_scheme(自主缴费方案表)】的数据库操作Service实现
 * @since 2023-03-06 14:01:45
 */
@Service
public class BSelfPaySchemeServiceImpl extends ServiceImpl<BSelfPaySchemeMapper, BSelfPayScheme>
        implements IBSelfPaySchemeService {

    /**
     * @return com.ruoyi.project.parking.domain.BSelfPayScheme
     * @apiNote 获取单个自主缴费方案
     * @author 琴声何来
     * @since 2023/3/1 10:01
     */
    @Override
    public BSelfPayScheme getBSelfPaySchemeByParkNo() {
        LambdaQueryWrapper<BSelfPayScheme> qw = new LambdaQueryWrapper<>();
        qw.eq(BSelfPayScheme::getParkNo, SecurityUtils.getParkNo());
        return getOne(qw);
    }

    /**
     * @apiNote 获取单个自主缴费方案 无需认证
     */
    @Override
    public BSelfPayScheme getBSelfPaySchemeByParkNo(String parkNo) {
        LambdaQueryWrapper<BSelfPayScheme> qw = new LambdaQueryWrapper<>();
        qw.eq(BSelfPayScheme::getParkNo, parkNo);
        return getOne(qw);
    }

    /**
     * @param selfPaySchemeParam 编辑自主缴费方案入参
     * @return boolean 编辑结果
     * @apiNote 编辑自主缴费方案
     * @author 琴声何来
     * @since 2023/3/1 10:11
     */
    @Override
    public boolean editById(BSelfPaySchemeParam selfPaySchemeParam) {
        BSelfPayScheme selfPayScheme = new BSelfPayScheme();
        BeanUtils.copyBeanProp(selfPayScheme, selfPaySchemeParam);
        selfPayScheme.setUpdateBy(selfPayScheme.getUpdateBy());
        selfPayScheme.setUpdateTime(selfPayScheme.getUpdateTime());
        return updateById(selfPayScheme);
    }
}




