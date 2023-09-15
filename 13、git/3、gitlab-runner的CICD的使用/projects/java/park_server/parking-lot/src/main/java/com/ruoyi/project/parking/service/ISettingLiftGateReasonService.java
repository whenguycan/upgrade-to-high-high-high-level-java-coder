package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.param.SettingLiftGateReasonParam;
import com.ruoyi.project.parking.domain.vo.SettingLiftGateReasonVO;
import com.ruoyi.project.parking.entity.SettingLiftGateReason;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 闸道抬杆原因表 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
public interface ISettingLiftGateReasonService extends IService<SettingLiftGateReason> {
    /**
     * 查询 闸道抬杆原因列表 通过 场地编号
     * @param settingLiftGateReason
     * @return
     */
    List<SettingLiftGateReason> listCondition(SettingLiftGateReason settingLiftGateReason);

    /**
     * 新增 闸道抬杆原因
     * @param settingLiftGateReasonVO
     * @return
     */
    boolean add(SettingLiftGateReasonVO settingLiftGateReasonVO);

    /**
     * 保存 闸道抬杆原因
     * @param settingLiftGateReasonParam
     * @return
     */
    boolean editById(SettingLiftGateReasonParam settingLiftGateReasonParam);

    /**
     * 伪删除 闸道抬杆原因
     * @param id
     * @return
     */
    boolean removeById(Integer id);
}
