package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BVisitorCodeManage;
import com.ruoyi.project.parking.domain.param.BVisitorCodeManageQueryParam;
import com.ruoyi.project.parking.domain.param.BVisitorCodeUpdateStatusParam;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;

import java.util.List;

/**
 * 访客码管理Service接口
 * 
 * @author fangch
 * @date 2023-03-06
 */
public interface IBVisitorCodeManageService 
{
    /**
     * 查询访客码管理
     * 
     * @param id 访客码管理主键
     * @return 访客码管理
     */
    BVisitorCodeManage selectBVisitorCodeManageById(Integer id);

    /**
     * 查询访客码管理列表
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 访客码管理集合
     */
    List<BVisitorCodeManage> selectBVisitorCodeManageList(BVisitorCodeManageQueryParam param);

    /**
     * 新增访客码管理
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    AjaxResult insertBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage);

    /**
     * 修改访客码管理
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    AjaxResult updateBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage);

    /**
     * 修改访客码状态
     *
     * @param param 访客码管理
     * @return 结果
     */
    AjaxResult updateStatus(BVisitorCodeUpdateStatusParam param);

    /**
     * 批量删除访客码管理
     * 
     * @param ids 需要删除的访客码管理主键集合
     * @return 结果
     */
    AjaxResult deleteBVisitorCodeManageByIds(Integer[] ids);

    /**
     * 删除访客码管理信息
     * 
     * @param id 访客码管理主键
     * @return 结果
     */
    AjaxResult deleteBVisitorCodeManageById(Integer id);

    /**
     * 导入访客码数据
     *
     * @param codeManageList 访客码数据列表
     * @param userId 操作用户
     * @param parkNo 车场编号
     * @return 结果
     */
    String importData(List<BVisitorCodeManage> codeManageList, String userId, String parkNo);

    /**
     * 获取访客码信息
     *
     * @param code 访客码
     * @return 访客码管理
     */
    BVisitorCodeManageVO getCodeInfo(String code);

    /**
     * 刷新绑定次数
     *
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    int updateNumber(BVisitorCodeManage bVisitorCodeManage);
}
