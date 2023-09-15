package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.SelfPay;
import com.ruoyi.project.parking.domain.bo.SelfPayBO;
import com.ruoyi.project.parking.domain.vo.SelfPayVO;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.parking.service.ISelfPayService;
import com.ruoyi.project.parking.mapper.SelfPayMapper;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 琴声何来
 * @description 针对表【t_self_pay(自主缴费表)】的数据库操作Service实现
 * @since 2023-03-01 14:26:32
 */
@Service
public class SelfPayServiceImpl extends ServiceImpl<SelfPayMapper, SelfPay>
        implements ISelfPayService {

    @Autowired
    private IBSelfPaySchemeService selfPaySchemeService;

    @Autowired
    private IRegularCarService regularCarService;

    /**
     * @apiNote 获取自主缴费审核列表 根据场库编号、车牌号、审核状态、创建时间
     */
    @Override
    public List<SelfPay> listByParkNoAndCarNumberAndStatusAndCreateTime(SelfPay selfPay) {
        LambdaQueryWrapper<SelfPay> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), SelfPay::getParkNo, SecurityUtils.getParkNo())
                .like(StringUtils.isNotEmpty(selfPay.getCarNumber()), SelfPay::getCarNumber, selfPay.getCarNumber())
                .eq(StringUtils.isNotEmpty(selfPay.getStatus()), SelfPay::getStatus, selfPay.getStatus())
                .ge(null != selfPay.getCreateTime(), SelfPay::getCreateTime, selfPay.getCreateTime());
        return list(qw);
    }

    /**
     * @apiNote 申请自主缴费
     */
    @Override
    public boolean add(SelfPayVO selfPayVO) {
        SelfPay selfPay = new SelfPay();
        BeanUtils.copyBeanProp(selfPay, selfPayVO);
        selfPay.setParkNo(selfPay.getParkNo());
        selfPay.setApplyBy(SecurityUtils.getUsername());
        selfPay.setApplyTime(LocalDateTime.now());
        selfPay.setCreateBy(SecurityUtils.getUsername());
        selfPay.setCreateTime(LocalDateTime.now());
        // 判断当前场库的自主缴费方案
        BSelfPayScheme bSelfPaySchemeByParkNo = selfPaySchemeService.getBSelfPaySchemeByParkNo();
        if (selfPay.getRenewDays() > bSelfPaySchemeByParkNo.getMaxRenewDays()) {
            throw new ServiceException("续费天数不能超过最大续费天数");
        }
        // 获取固定车有效期结束日期
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.eq(RegularCar::getCarNumber, selfPay.getCarNumber())
                .eq(RegularCar::getCarCategoryId, selfPay.getRegularCarCategoryId())
                .le(RegularCar::getEndTime, LocalDateTime.now())
                .orderByDesc(RegularCar::getEndTime)
                .last("limit 1");
        RegularCar regularCar = regularCarService.getOne(qw);
        // 如果有固定车记录，且截止日期减去续费临期天数之后，仍在当前时间之后，则不允许续费
        if (regularCar != null && selfPay.getCreateTime().isBefore(regularCar.getEndTime().minusDays(bSelfPaySchemeByParkNo.getRenewDeadlineDays()).atStartOfDay())) {
            throw new ServiceException("未到续费临期时间不能续费");
        }
        return save(selfPay);
    }

    /**
     * @apiNote 审核通过、不通过自主缴费
     */
    @Override
    public boolean switchStatusById(Integer id, String status) {
        SelfPay selfPay = getById(id);
        if (selfPay == null) {
            throw new ServiceException("获取自主缴费异常");
        }
        // 状态一致时不进行更新
        if (status.equals(selfPay.getStatus())) {
            return true;
        } else {
            LambdaUpdateWrapper<SelfPay> uw = new LambdaUpdateWrapper<>();
            uw.set(SelfPay::getStatus, status)
                    .set(SelfPay::getUpdateBy, SecurityUtils.getUsername())
                    .set(SelfPay::getUpdateTime, LocalDateTime.now())
                    .eq(SelfPay::getId, id);
            return update(uw);
        }
    }

    /**
     * @param selfPayBOList               导入数据列表
     * @param updateSupport               是否更新数据
     * @param settingRegularCarCategoryId 固定车类型id
     * @apiNote 自主缴费列表导入
     */
    @Override
    public String importSelfPay(List<SelfPayBO> selfPayBOList, boolean updateSupport, String settingRegularCarCategoryId) {
        if (StringUtils.isNull(selfPayBOList) || selfPayBOList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SelfPayBO selfPayBO : selfPayBOList) {
            if (StringUtils.isEmpty(selfPayBO.getCarNumber())) {
                failureMsg.append("<br/>").append(failureNum).append("、自主缴费车牌号为空");
                continue;
            }
            LambdaQueryWrapper<SelfPay> qw = new LambdaQueryWrapper<>();
            // 验证是否存在未审核的自主缴费申请
            SelfPay selfPay = getOne(qw.eq(SelfPay::getCarNumber, selfPayBO.getCarNumber())
                    .eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), SelfPay::getParkNo, SecurityUtils.getParkNo())
                    .eq(SelfPay::getRegularCarCategoryId, settingRegularCarCategoryId)
                    .eq(SelfPay::getStatus, "0"));
            try {
                if (StringUtils.isNull(selfPay)) {
                    //新增自主缴费
                    SelfPay addSelfPay = new SelfPay();
                    BeanUtils.copyBeanProp(addSelfPay, selfPayBO);
                    addSelfPay.setParkNo(SecurityUtils.getParkNo());
                    addSelfPay.setApplyTime(DateLocalDateUtils.dateToLocalDateTime(selfPayBO.getApplyTime()));
                    addSelfPay.setCreateBy(SecurityUtils.getUsername());
                    addSelfPay.setCreateTime(LocalDateTime.now());
                    save(addSelfPay);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、自主缴费申请 ").append(selfPayBO.getCarNumber()).append(" 导入成功");
                } else if (updateSupport) {
                    //更新自主缴费
                    SelfPay updateSelfPay = new SelfPay();
                    BeanUtils.copyBeanProp(updateSelfPay, selfPayBO);
                    updateSelfPay.setId(selfPay.getId());
                    updateSelfPay.setApplyTime(DateLocalDateUtils.dateToLocalDateTime(selfPayBO.getApplyTime()));
                    updateSelfPay.setUpdateBy(SecurityUtils.getUsername());
                    updateSelfPay.setUpdateTime(LocalDateTime.now());
                    updateById(updateSelfPay);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、自主缴费申请 ").append(selfPayBO.getCarNumber()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、自主缴费申请 ").append(selfPayBO.getCarNumber()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、固定车 " + selfPayBO.getCarNumber() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}




