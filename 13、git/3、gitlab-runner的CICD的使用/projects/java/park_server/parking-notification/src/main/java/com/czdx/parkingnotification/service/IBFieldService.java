package com.czdx.parkingnotification.service;

import com.czdx.parkingnotification.domain.BField;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【b_field(区域管理表)】的数据库操作Service
* @since 2023-04-04 09:41:14
*/
public interface IBFieldService extends IService<BField> {

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @param
     * @param
     * @return
     */
    List<BField> findList(BField bField, List<String> parkNos);

}
