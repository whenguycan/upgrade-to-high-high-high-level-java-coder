package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.WhiteList;
import com.ruoyi.project.parking.domain.bo.WhiteListBO;
import com.ruoyi.project.parking.domain.param.WhiteListParam;
import com.ruoyi.project.parking.domain.vo.WhiteListVO;
import com.ruoyi.project.parking.service.IWhiteListService;
import com.ruoyi.project.parking.mapper.WhiteListMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_white_list(白名单表)】的数据库操作Service实现
 * @since 2023-02-27 17:21:16
 */
@Service
public class WhiteListServiceImpl extends ServiceImpl<WhiteListMapper, WhiteList>
        implements IWhiteListService {

    /**
     * @apiNote 获取白名单列表 通过场地编号、车牌号
     */
    @Override
    public List<WhiteList> listByParkNoAndCarNumber(WhiteList whiteList) {
        LambdaQueryWrapper<WhiteList> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), WhiteList::getParkNo, SecurityUtils.getParkNo())
                .like(StringUtils.isNotEmpty(whiteList.getCarNumber()), WhiteList::getCarNumber, whiteList.getCarNumber());
        return list(qw);
    }

    /**
     * @apiNote 新增单个白名单
     */
    @Override
    public boolean add(WhiteListVO whiteListVO) {
        WhiteList whiteList = new WhiteList();
        BeanUtils.copyBeanProp(whiteList, whiteListVO);
        whiteList.setParkNo(SecurityUtils.getParkNo());
        whiteList.setCreateBy(SecurityUtils.getUsername());
        whiteList.setCreateTime(LocalDateTime.now());
        return save(whiteList);
    }

    @Override
    public boolean editById(WhiteListParam whiteListParam) {
        WhiteList whiteList = new WhiteList();
        BeanUtils.copyBeanProp(whiteList, whiteListParam);
        whiteList.setUpdateBy(SecurityUtils.getUsername());
        whiteList.setUpdateTime(LocalDateTime.now());
        return updateById(whiteList);
    }

    @Override
    public String importWhiteList(List<WhiteListBO> whiteListBOList, boolean updateSupport) {
        if (StringUtils.isNull(whiteListBOList) || whiteListBOList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (WhiteListBO whiteListBO : whiteListBOList) {
            if (StringUtils.isEmpty(whiteListBO.getCarNumber())) {
                failureMsg.append("<br/>").append(failureNum).append("、白名单车牌号为空");
                continue;
            }
            LambdaQueryWrapper<WhiteList> qw = new LambdaQueryWrapper<>();
            // 验证是否存在这个固定车
            WhiteList whiteList = getOne(qw.eq(WhiteList::getCarNumber, whiteListBO.getCarNumber())
                    .eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), WhiteList::getParkNo, SecurityUtils.getParkNo()));
            try {
                if (StringUtils.isNull(whiteList)) {
                    // 默认接口新增为线下类型
                    WhiteList addWhiteList = new WhiteList();
                    BeanUtils.copyBeanProp(addWhiteList, whiteListBO);
                    addWhiteList.setParkNo(SecurityUtils.getParkNo());
                    addWhiteList.setCreateBy(SecurityUtils.getUsername());
                    addWhiteList.setCreateTime(LocalDateTime.now());
                    save(addWhiteList);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、白名单 ").append(whiteListBO.getCarNumber()).append(" 导入成功");
                } else if (updateSupport) {
                    WhiteList updateWhiteList = new WhiteList();
                    BeanUtils.copyBeanProp(updateWhiteList, whiteListBO);
                    updateWhiteList.setId(whiteList.getId());
                    updateWhiteList.setUpdateBy(SecurityUtils.getUsername());
                    updateWhiteList.setUpdateTime(LocalDateTime.now());
                    updateById(updateWhiteList);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、白名单 ").append(whiteListBO.getCarNumber()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、白名单 ").append(whiteListBO.getCarNumber()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、白名单 " + whiteListBO.getCarNumber() + " 导入失败：";
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

    /**
     * @apiNote 新增单个白名单
     */
    @Override
    public boolean addWhiteList(WhiteListVO whiteListVO) {
        WhiteList whiteList = new WhiteList();
        BeanUtils.copyBeanProp(whiteList, whiteListVO);
        whiteList.setCreateTime(LocalDateTime.now());
        return save(whiteList);
    }

    /**
     * @apiNote 获取黑名单列表
     */
    @Override
    public List<WhiteList> listByParkNoAndCarNumberUnsafe(WhiteList whiteList) {
        LambdaQueryWrapper<WhiteList> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(whiteList.getParkNo()), WhiteList::getParkNo, whiteList.getParkNo())
                .eq(StringUtils.isNotEmpty(whiteList.getCarNumber()), WhiteList::getCarNumber, whiteList.getCarNumber());
        return list(qw);
    }
}




