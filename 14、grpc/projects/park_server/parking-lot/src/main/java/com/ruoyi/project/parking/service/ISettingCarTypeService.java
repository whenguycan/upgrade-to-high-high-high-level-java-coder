package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.param.SettingCarTypeParam;
import com.ruoyi.project.parking.domain.vo.SettingCarTypeVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车辆类型表 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
public interface ISettingCarTypeService extends IService<SettingCarType> {

    /**
     * 查询车辆类型列表 通过 查询条件
     * @param settingCarType 查询条件
     * @return
     */
    List<SettingCarType> listCondition(SettingCarType settingCarType);


    /**
     * 查询类型名
     * @param parkNo 车场编号
     * @param code 类型码
     */
    String queryTypeNameByPartNoAndCode(String parkNo,String code);

    /**
     * 新增 车辆类型
     * @param settingCarTypeVO
     * @return
     */
    boolean add(SettingCarTypeVO settingCarTypeVO);

    /**
     * 保存 车辆类型
     * @param settingCarTypeParam
     * @return
     */
    boolean editById(SettingCarTypeParam settingCarTypeParam);

    /**
     * 伪删除 车辆类型
     * @param id
     * @return
     */
    boolean removeById(Integer id);

    /**
     * 启用、停用 车辆类型
     * @param id 车辆类型id
     * @param status ”0“:停用 “1”:启用
     * @return
     */
    boolean switchStatusById(Integer id,String status);
}
