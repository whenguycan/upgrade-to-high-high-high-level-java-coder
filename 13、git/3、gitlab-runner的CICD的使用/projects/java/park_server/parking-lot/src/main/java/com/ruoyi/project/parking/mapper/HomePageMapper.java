package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.system.domain.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页Mapper接口
 *
 * @author fangch
 * @date 2023-03-20
 */
@Mapper
public interface HomePageMapper
{
    /**
     * 查询所有场库id
     *
     * @return 场库信息
     */
    List<SysDept> getAllParkIds();

    /**
     * 查询子节点车场编号
     *
     * @param deptId 场库id
     * @return 子节点车场编号
     */
    List<String> getChildParkNos(Long deptId);

    /**
     * 查询子节点车场编号
     *
     * @param parkNo 车场编号
     * @return 子节点车场编号
     */
    List<String> getChildParkNosByNo(String parkNo);

    /**
     * 查询剩余车位数
     *
     * @param parkNos 子节点车场编号
     * @return 剩余车位数
     */
    Integer getRemainSpaceCount(List<String> parkNos);

    /**
     * 查询固定车辆数
     *
     * @param parkNos 子节点车场编号
     * @return 固定车辆数
     */
    Integer getRegularCarCount(List<String> parkNos);
}
