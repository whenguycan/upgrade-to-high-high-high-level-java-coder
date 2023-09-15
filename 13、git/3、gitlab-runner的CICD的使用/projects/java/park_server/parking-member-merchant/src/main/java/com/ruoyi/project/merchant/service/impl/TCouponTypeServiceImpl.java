package com.ruoyi.project.merchant.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.vo.CouponTypeVo;
import com.ruoyi.project.merchant.mapper.TCouponDetailMapper;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.parking.domain.vo.MemberUserVO;
import com.ruoyi.project.parking.utils.SnowflakeIdWorker16Size;
import com.ruoyi.project.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TCouponTypeMapper;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.service.ITCouponTypeService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 优惠券种类Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TCouponTypeServiceImpl extends ServiceImpl<TCouponTypeMapper,TCouponType> implements ITCouponTypeService {
    @Resource
    private TCouponTypeMapper tCouponTypeMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ITCouponDetailService itCouponDetailService;

    /**
     * 查询优惠券种类
     *
     * @param id 优惠券种类主键
     * @return 优惠券种类
     */
    @Override
    public TCouponType selectTCouponTypeById(Long id) {
        return tCouponTypeMapper.selectTCouponTypeById(id);
    }

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类
     */
    @Override
    public List<TCouponType> selectTCouponTypeList(TCouponType tCouponType) {
        return tCouponTypeMapper.selectTCouponTypeList(tCouponType);
    }

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类
     */
    @Override
    public List<TCouponType> selectTCouponTypeListPC(TCouponType tCouponType) {
        return tCouponTypeMapper.selectTCouponTypeListPC(tCouponType);
    }

    @Override
    public int selectTCouponTypeListPCCount(TCouponType tCouponType) {
        return tCouponTypeMapper.selectTCouponTypeListPCCount(tCouponType);
    }

    /**
     * 新增优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertTCouponType(TCouponType tCouponType) {
        tCouponType.setCreateTime(DateUtils.getNowDate());
        tCouponType.setCreateBy(SecurityUtils.getUsername());
        int n = tCouponTypeMapper.insertTCouponType(tCouponType);
        //平台券本车场所有商户增加
        if("1".equals(tCouponType.getCouponMold())){
            List<MemberUserVO> list = sysUserMapper.selectMemberListByDeptId(SecurityUtils.getDeptId());
            if(!list.isEmpty()){
                for (MemberUserVO m : list){
                    tCouponType.setUserId(m.getUserId());
                    tCouponTypeMapper.insertTCouponType(tCouponType);
                }
            }
        }
        return n;
    }

    /**
     * 修改优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    @Override
    public int updateTCouponType(TCouponType tCouponType) {
        tCouponType.setUpdateTime(DateUtils.getNowDate());
        tCouponType.setUpdateBy(SecurityUtils.getUsername());
        TCouponDetail tCouponDetail = new TCouponDetail();
        tCouponDetail.setCouponId(tCouponType.getId());
        if (itCouponDetailService.countCouponDetail(tCouponDetail) > 0) {
            return -1;
        }
        return tCouponTypeMapper.updateTCouponType(tCouponType);
    }

    /**
     * 批量删除优惠券种类
     *
     * @param ids 需要删除的优惠券种类主键
     * @return 结果
     */
    @Override
    public int deleteTCouponTypeByIds(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            TCouponDetail tCouponDetail = new TCouponDetail();
            tCouponDetail.setCouponId(ids[i]);
            if (itCouponDetailService.countCouponDetail(tCouponDetail) > 0) {
                return -1;
            }
        }
        return tCouponTypeMapper.deleteTCouponTypeByIds(ids);
    }

    /**
     * 删除优惠券种类信息
     *
     * @param id 优惠券种类主键
     * @return 结果
     */
    @Override
    public int deleteTCouponTypeById(Long id) {
        return tCouponTypeMapper.deleteTCouponTypeById(id);
    }

    @Override
    public List<CouponTypeVo> countAppCoupon(CouponTypeVo couponType) {
        return tCouponTypeMapper.countCouponForApp(couponType);

    }

    @Override
    public Long countCouponType(Long userId,String parkNo) {
        LambdaQueryWrapper<TCouponType> couponTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponTypeLambdaQueryWrapper.eq(TCouponType::getUserId, userId);
        couponTypeLambdaQueryWrapper.eq(TCouponType::getParkNo, parkNo);
        return tCouponTypeMapper.selectCount(couponTypeLambdaQueryWrapper);
    }

    @Override
    public List<TCouponType> selectCouponsByParkNo(String parkNo, String couponMold) {
        return tCouponTypeMapper.selectCouponsByParkNo(parkNo, couponMold);
    }
}
