package com.ruoyi.project.parking.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BVisitorCodeManage;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.param.BVisitorCodeManageQueryParam;
import com.ruoyi.project.parking.domain.param.BVisitorCodeUpdateStatusParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import com.ruoyi.project.parking.enums.BVisitorCodeManageEnums;
import com.ruoyi.project.parking.mapper.BVisitorCodeManageMapper;
import com.ruoyi.project.parking.service.IBVisitorApplyManageService;
import com.ruoyi.project.parking.service.IBVisitorCodeManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 访客码管理Service业务层处理
 * 
 * @author fangch
 * @date 2023-03-06
 */
@Slf4j
@Service
public class BVisitorCodeManageServiceImpl implements IBVisitorCodeManageService {

    @Autowired
    private BVisitorCodeManageMapper bVisitorCodeManageMapper;

    @Autowired
    protected Validator validator;

    @Autowired
    private IBVisitorApplyManageService ibVisitorApplyManageService;

    /**
     * 查询访客码管理
     * 
     * @param id 访客码管理主键
     * @return 访客码管理
     */
    @Override
    public BVisitorCodeManage selectBVisitorCodeManageById(Integer id)
    {
        return bVisitorCodeManageMapper.selectBVisitorCodeManageById(id);
    }

    /**
     * 查询访客码管理列表
     * 
     * @param param 访客码管理
     * @return 访客码管理
     */
    @Override
    public List<BVisitorCodeManage> selectBVisitorCodeManageList(BVisitorCodeManageQueryParam param)
    {
        return bVisitorCodeManageMapper.selectBVisitorCodeManageList(param);
    }

    /**
     * 新增访客码管理
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    @Override
    public AjaxResult insertBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage) {
        try {
            bVisitorCodeManage.setCode(UUID.get12UUID());
            bVisitorCodeManage.setCreateTime(LocalDateTime.now());
            bVisitorCodeManage.setStatus(BVisitorCodeManageEnums.STATUS.ACTIVATED.getValue());
            bVisitorCodeManageMapper.insertBVisitorCodeManage(bVisitorCodeManage);
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(bVisitorCodeManage.getCode());
    }

    /**
     * 修改访客码管理
     * 
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    @Override
    public AjaxResult updateBVisitorCodeManage(BVisitorCodeManage bVisitorCodeManage) {
        if (bVisitorCodeManage.getId() == null) {
            return AjaxResult.warn("逻辑ID不可为空");
        }
        BVisitorApplyParam param = new BVisitorApplyParam();
        param.setCode(bVisitorCodeManageMapper.selectBVisitorCodeManageById(bVisitorCodeManage.getId()).getCode());
        List<BVisitorApplyVO> applyVOS = ibVisitorApplyManageService.selectBVisitorApplyManageList(param);
        if (CollectionUtils.isNotEmpty(applyVOS)) {
            return AjaxResult.warn("已关联访客预约申请，不可修改");
        }
        bVisitorCodeManage.setUpdateTime(LocalDateTime.now());
        int result = bVisitorCodeManageMapper.updateBVisitorCodeManage(bVisitorCodeManage);
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改访客码状态
     *
     * @param param 访客码管理
     * @return 结果
     */
    @Override
    public AjaxResult updateStatus(BVisitorCodeUpdateStatusParam param) {
        BVisitorApplyParam applyParam = new BVisitorApplyParam();
        applyParam.setCode(bVisitorCodeManageMapper.selectBVisitorCodeManageById(param.getId()).getCode());
        List<BVisitorApplyVO> applyVOS = ibVisitorApplyManageService.selectBVisitorApplyManageList(applyParam);
        if (CollectionUtils.isNotEmpty(applyVOS)) {
            return AjaxResult.warn("已关联访客预约申请，不可修改");
        }
        BVisitorCodeManage oldData = bVisitorCodeManageMapper.selectBVisitorCodeManageById(param.getId());
        if (oldData.getStatus().equals(param.getStatus())) {
            return AjaxResult.warn("该访客码已" + Objects.requireNonNull(BVisitorCodeManageEnums.STATUS.getByValue(oldData.getStatus())).getDesc() + "，请检查后操作");
        }
        param.setUpdateTime(LocalDateTime.now());
        BVisitorCodeManage bVisitorCodeManage = new BVisitorCodeManage();
        BeanUtils.copyProperties(param, bVisitorCodeManage);
        return AjaxResult.success(bVisitorCodeManageMapper.updateBVisitorCodeManage(bVisitorCodeManage));
    }

    /**
     * 批量删除访客码管理
     * 
     * @param ids 需要删除的访客码管理主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteBVisitorCodeManageByIds(Integer[] ids) {
        int result = bVisitorCodeManageMapper.deleteBVisitorCodeManageByIds(ids);
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除访客码管理信息
     * 
     * @param id 访客码管理主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteBVisitorCodeManageById(Integer id) {
        BVisitorApplyParam param = new BVisitorApplyParam();
        param.setCode(bVisitorCodeManageMapper.selectBVisitorCodeManageById(id).getCode());
        List<BVisitorApplyVO> applyVOS = ibVisitorApplyManageService.selectBVisitorApplyManageList(param);
        if (CollectionUtils.isNotEmpty(applyVOS)) {
            return AjaxResult.warn("已关联访客预约申请，不可删除");
        }
        int result = bVisitorCodeManageMapper.deleteBVisitorCodeManageById(id);
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 导入访客码数据
     *
     * @param codeManageList 访客码数据列表
     * @param userId 操作用户
     * @param parkNo 车场编号
     * @return 结果
     */
    @Override
    @Transactional
    public String importData(List<BVisitorCodeManage> codeManageList, String userId, String parkNo)
    {
        if (CollectionUtils.isEmpty(codeManageList)) {
            throw new ServiceException("导入访客码数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < codeManageList.size(); i++) {
            try {
                if (StringUtils.isEmpty(codeManageList.get(i).getCodeName())) {
                    failureNum++;
                    failureMsg.append("<br/>").append("第").append(i+1).append("条数据：访客码名称不可为空");
                } else {
                    // 验证是否存在这个用户
                    int cnt = bVisitorCodeManageMapper.checkUnique(codeManageList.get(i).getCodeName());
                    if (cnt == 0) {
                        BeanValidators.validateWithException(validator, codeManageList.get(i));
                        codeManageList.get(i).setParkNo(parkNo);
                        codeManageList.get(i).setCode(UUID.get12UUID());
                        codeManageList.get(i).setCreateBy(userId);
                        codeManageList.get(i).setCreateTime(LocalDateTime.now());
                        codeManageList.get(i).setStatus(BVisitorCodeManageEnums.STATUS.ACTIVATED.getValue());
                        codeManageList.get(i).setCodeUsedNumber(0);
                        bVisitorCodeManageMapper.insertBVisitorCodeManage(codeManageList.get(i));
                        successNum++;
//                        successMsg.append("<br/>").append(successNum).append("、访客码 ").append(codeManageList.get(i).getCodeName()).append(" 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>").append("第").append(i+1).append("条数据：访客码 ").append(codeManageList.get(i).getCodeName()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "第" + (i+1) + "条数据：访客码 " + codeManageList.get(i).getCodeName() + " 导入失败：";
                failureMsg.append("<br/>").append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条。");
        }
        return successMsg.toString();
    }

    /**
     * 获取访客码信息
     *
     * @param code 访客码
     * @return 访客码管理
     */
    @Override
    public BVisitorCodeManageVO getCodeInfo(String code) {
        return bVisitorCodeManageMapper.getCodeInfo(code);
    }

    /**
     * 刷新绑定次数
     *
     * @param bVisitorCodeManage 访客码管理
     * @return 结果
     */
    @Override
    public int updateNumber(BVisitorCodeManage bVisitorCodeManage) {
        return bVisitorCodeManageMapper.updateNumber(bVisitorCodeManage);
    }
}
