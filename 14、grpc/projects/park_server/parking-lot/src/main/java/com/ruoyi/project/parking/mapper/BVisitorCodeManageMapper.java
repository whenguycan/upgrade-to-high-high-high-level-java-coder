package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.BVisitorCodeManage;
import com.ruoyi.project.parking.domain.param.BVisitorCodeManageQueryParam;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 访客码管理Mapper接口
 * 
 * @author fangch
 * @date 2023-03-06
 */
@Mapper
public interface BVisitorCodeManageMapper 
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
    int insertBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage);

    /**
     * 修改访客码管理
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    int updateBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage);

    /**
     * 删除访客码管理
     * 
     * @param id 访客码管理主键
     * @return 结果
     */
    int deleteBVisitorCodeManageById(Integer id);

    /**
     * 批量删除访客码管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBVisitorCodeManageByIds(Integer[] ids);

    /**
     * 检查访客码唯一
     *
     * @param codeName 访客码名称
     * @return 结果
     */
    int checkUnique(String codeName);

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
