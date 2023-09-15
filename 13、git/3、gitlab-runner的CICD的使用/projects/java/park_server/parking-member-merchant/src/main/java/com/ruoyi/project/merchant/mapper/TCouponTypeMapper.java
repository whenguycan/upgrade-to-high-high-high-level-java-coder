package com.ruoyi.project.merchant.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.vo.CouponTypeVo;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券种类Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface TCouponTypeMapper extends BaseMapper<TCouponType>
{
    /**
     * 查询优惠券种类
     *
     * @param id 优惠券种类主键
     * @return 优惠券种类
     */
    public TCouponType selectTCouponTypeById(Long id);

    /**
     * 统计
     * @param tCouponType
     * @return
     */
    public List<CouponTypeVo> countCouponForApp(CouponTypeVo tCouponType);

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类集合
     */
    public List<TCouponType> selectTCouponTypeList(TCouponType tCouponType);

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类集合
     */
    public List<TCouponType> selectTCouponTypeListPC(TCouponType tCouponType);

    public int selectTCouponTypeListPCCount(TCouponType tCouponType);

    /**
     * 新增优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    public int insertTCouponType(TCouponType tCouponType);

    /**
     * 修改优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    public int updateTCouponType(TCouponType tCouponType);

    /**
     * 删除优惠券种类
     *
     * @param id 优惠券种类主键
     * @return 结果
     */
    public int deleteTCouponTypeById(Long id);

    /**
     * 批量删除优惠券种类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCouponTypeByIds(Long[] ids);

    public List<TCouponType> selectCouponsByParkNo(@Param("parkNo") String parkNo, @Param("couponMold") String couponMold);
}
