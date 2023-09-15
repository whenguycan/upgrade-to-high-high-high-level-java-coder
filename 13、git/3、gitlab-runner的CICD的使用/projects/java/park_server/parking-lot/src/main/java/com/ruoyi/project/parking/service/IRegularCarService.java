package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.RegularCar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.bo.RegularCarBO;
import com.ruoyi.project.parking.domain.param.RegularCarParam;
import com.ruoyi.project.parking.domain.vo.RegularCarVO;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【t_regular_car(固定车记录表)】的数据库操作Service
* @since 2023-02-24 10:40:09
*/
public interface IRegularCarService extends IService<RegularCar> {

    /**
     * @apiNote 获取固定车记录列表 通过场地编号、车牌号、车位编号、车主姓名、车主联系方式、固定车类型、固定车添加类型（线上线下）
     */
    List<RegularCar> listByParkNoAndCarNumberAndPlaceNoAndOwnerAndCategoryIdAndCarType(RegularCar regularCar);

    /**
     * @apiNote 获取固定车记录列表 不带认证
     */
    List<RegularCar> listUnsafe(RegularCar regularCar);

    /**
     * @apiNote 新增单个固定车记录（线下）
     */
    boolean add(RegularCarVO regularCarVO);

    /**
     * @apiNote 新增单个固定车记录（线上）
     */
    boolean addOnline(RegularCarVO regularCarVO,String parkNo,String userName);

    /**
     * @apiNote 编辑单个固定车记录
     */
    boolean editById(RegularCarParam regularCarParam);

    /**
     * @apiNote 逻辑删除单个固定车记录
     */
    boolean removeById(Integer id);

    /**
     * @apiNote 启用、禁用单个固定车记录
     */
    boolean switchStatusById(Integer id, String status);

    /**
     * @apiNote 固定车列表导入
     */
    String importRegularCar(List<RegularCarBO> regularCarList, boolean updateSupport,Integer carCategoryId);
}
