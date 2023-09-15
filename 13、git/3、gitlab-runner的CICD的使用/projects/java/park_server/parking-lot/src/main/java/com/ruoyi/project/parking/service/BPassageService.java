package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.vo.BPassageVo;

import java.util.List;

/**
 * 通道信息表;(b_passage)表服务接口
 *
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
public interface BPassageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BPassage queryById(Integer id);

    /**
     * 分页查询
     *
     * @param bPassage 筛选条件
     * @return
     */
    List<BPassageVo> qryList(BPassageVo bPassage);

    /**
     * 新增数据
     *
     * @param bPassage 实例对象
     * @return 实例对象
     */
    int insert(BPassage bPassage);

    /**
     * 更新数据
     *
     * @param bPassage 实例对象
     * @return 实例对象
     */
    int update(BPassage bPassage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Long countPassageByPassageName(String passageName,String parkNO);

    Long countPassageByPassageNo(String passageNo,String parkNo);

    /**
     * 通过通道编号 查询通道名称
     * @param passageNo 通道编号
     */
    String selectPassageNameByPassageNo(String passageNo);

}
