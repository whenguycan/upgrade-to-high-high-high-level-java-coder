package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.param.BSelfPaySchemeParam;

/**
* @author 琴声何来
* @description 针对表【b_self_pay_scheme(自主缴费方案表)】的数据库操作Service
* @since 2023-03-01 09:42:42
*/
public interface IBSelfPaySchemeService extends IService<BSelfPayScheme> {

    /**
     * @apiNote 获取单个自主缴费方案
     * @author 琴声何来
     * @since 2023/3/1 10:01
     * @return com.ruoyi.project.parking.domain.BSelfPayScheme
     */
    BSelfPayScheme getBSelfPaySchemeByParkNo();

    /**
     * @apiNote 获取单个自主缴费方案 无需认证
     */
    BSelfPayScheme getBSelfPaySchemeByParkNo(String parkNo);

    boolean editById(BSelfPaySchemeParam selfPaySchemeParam);
}
