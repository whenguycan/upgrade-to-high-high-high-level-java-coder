package com.ruoyi.project.parking.mapper;

import com.google.protobuf.ProtocolStringList;
import com.ruoyi.project.parking.domain.BVisitorApply;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 停车预约申请Mapper接口
 * 
 * @author fangch
 * @date 2023-03-07
 */
@Mapper
public interface BVisitorApplyMapper 
{
    /**
     * 查询停车预约申请
     * 
     * @param id 停车预约申请主键
     * @return 停车预约申请
     */
    BVisitorApplyVO selectBVisitorApplyById(Integer id);

    /**
     * 查询停车预约申请列表
     * 
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请集合
     */
    List<BVisitorApplyVO> selectBVisitorApplyList(BVisitorApply bVisitorApply);

    /**
     * 查询停车预约申请列表
     *
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请
     */
    List<BVisitorApplyVO> getBVisitorApplyList(BVisitorApplyParam bVisitorApply);

    /**
     * 查询访客待审批数
     *
     * @param parkNos
     * @param status
     * @return
     */
    Integer getNotDisposedCount(@Param("parkNos") List<String> parkNos, @Param("status") String status);

    /**
     * 新增停车预约申请
     * 
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    int insertBVisitorApply(BVisitorApply bVisitorApply);

    /**
     * 修改停车预约申请
     * 
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    int updateBVisitorApply(BVisitorApply bVisitorApply);

    /**
     * 检查状态
     *
     * @param id
     * @param status
     * @return
     */
    int checkStatus(@Param("id") Integer id, @Param("status") String status);

    /**
     * 批量审核和驳回
     *
     * @param id
     * @param status
     * @param rejectReason
     * @param updateBy
     * @param updateTime
     * @return
     */
    int updateStatus(@Param("id") Integer id, @Param("status") String status,
                     @Param("rejectReason") String rejectReason, @Param("updateBy") String updateBy,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 删除停车预约申请
     * 
     * @param id 停车预约申请主键
     * @return 结果
     */
    int deleteBVisitorApplyById(Integer id);

    /**
     * 批量删除停车预约申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBVisitorApplyByIds(Integer[] ids);

    /**
     * 查询指定日期访客数量
     */
    int countBVisitorApply(String day, String status, ProtocolStringList parkNos);

    List<VisitorApplySheetBO> statisticsSheet(String parkNo, String startDate, String endDate);
}
