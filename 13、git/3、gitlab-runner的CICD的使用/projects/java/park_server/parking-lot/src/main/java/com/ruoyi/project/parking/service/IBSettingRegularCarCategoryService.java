package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryVO;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【b_setting_regular_car_category(固定车类型表)】的数据库操作Service
* @since 2023-02-24 09:34:55
*/
public interface IBSettingRegularCarCategoryService extends IService<BSettingRegularCarCategory> {

    /**
     * @apiNote 查询固定车类型列表 通过场地编号、固定车类型识别码、分组类型编号、是否允许线上购买
     */
    List<BSettingRegularCarCategoryBO> listByParkNoAndCodeAndGroupIdAndOnlineFlag(BSettingRegularCarCategory settingRegularCarCategory);

    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    List<BSettingRegularCarCategoryBO> listOnline(BSettingRegularCarCategory settingRegularCarCategory);

    /**
     * @apiNote 获取单个固定车类型
     */
    BSettingRegularCarCategoryBO getBSettingRegularCarCategoryById(Integer id);

    /**
     * @apiNote 新增 固定车类型
     */
    boolean add(BSettingRegularCarCategoryVO settingRegularCarCategoryVO);

    /**
     * @apiNote 编辑 固定车类型
     */
    boolean editById(BSettingRegularCarCategoryParam settingRegularCarCategoryParam);

    /**
     * @apiNote 逻辑删除 固定车类型
     */
    boolean removeById(Integer id);

    /**
     * @apiNote 启用、停用 固定车类型
     */
    boolean switchStatusById(Integer id,String status);
}
