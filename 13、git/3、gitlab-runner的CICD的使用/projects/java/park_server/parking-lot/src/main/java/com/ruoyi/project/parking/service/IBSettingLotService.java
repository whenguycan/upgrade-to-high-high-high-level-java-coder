package com.ruoyi.project.parking.service;

import java.util.List;

import com.ruoyi.project.parking.domain.BSettingLot;

/**
 * 场库基础配置Service接口
 *
 * @author ruoyi
 * @date 2023-03-14
 */
public interface IBSettingLotService {

    /**
     * 新增场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    public int insertBSettingLot(BSettingLot bSettingLot);

    /**
     * 修改场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    public int updateBSettingLot(BSettingLot bSettingLot);

    public BSettingLot getBSettingLotByParkNo(BSettingLot bSettingLot);

}
