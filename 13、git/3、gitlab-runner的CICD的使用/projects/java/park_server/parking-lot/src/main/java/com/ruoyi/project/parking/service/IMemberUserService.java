package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.domain.MemberUser;
import com.ruoyi.project.parking.domain.param.BMyCarParam;
import com.ruoyi.project.parking.domain.param.MemberUserParam;

import java.util.List;

/**
 * 会员 业务层
 * 
 * @author fangch
 */
public interface IMemberUserService {
    /**
     * 查询会员详情
     *
     * @param id 会员主键
     * @return 会员详情
     */
    MemberUser selectUserById(Integer id);

    /**
     * 根据条件分页查询会员列表
     * 
     * @param memberUser 会员信息
     * @return 会员信息集合信息
     */
    List<MemberUser> selectUserList(MemberUserParam memberUser);

    /**
     * 通过会员ID查询我的车辆
     *
     * @param userId 会员ID
     * @return 我的车辆对象信息
     */
    List<BMyCar> selectCarList(Long userId);

    /**
     * 绑定车辆
     *
     * @param bMyCarParam 我的车辆
     * @return 结果
     */
    AjaxResult insertBMyCar(BMyCarParam bMyCarParam);

    /**
     * 解绑车辆
     *
     * @param ids 需要删除的我的车辆主键集合
     * @return 结果
     */
    int deleteBMyCarByIds(Integer[] ids);

    /**
     * 设为默认
     *
     * @param bMyCarParam 我的车辆
     * @return 结果
     */
    int setDefault(BMyCarParam bMyCarParam);
}
