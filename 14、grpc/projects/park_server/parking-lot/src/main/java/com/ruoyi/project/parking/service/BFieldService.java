package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BField;

import java.util.List;

/**
 * 区域管理表;(b_field)表服务接口
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
public interface BFieldService{

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BField queryById(Integer id);

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @param
     * @param
     * @return
     */
    List<BField> findList(BField bField);
    /**
     * 新增数据
     *
     * @param bField 实例对象
     * @return 实例对象
     */
    int insert(BField bField);
    /**
     * 更新数据
     *
     * @param bField 实例对象
     * @return 实例对象
     */
    int update(BField bField);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
}
