package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.BlackList;
import com.ruoyi.project.parking.domain.bo.BlackListBO;
import com.ruoyi.project.parking.domain.param.BlackListParam;
import com.ruoyi.project.parking.domain.vo.BlackListVO;
import com.ruoyi.project.parking.service.IBlackListService;
import com.ruoyi.project.parking.mapper.BlackListMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_black_list(黑名单表)】的数据库操作Service实现
 * @since 2023-02-27 17:21:11
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList>
        implements IBlackListService {

    /**
     * @apiNote 获取黑名单列表
     */
    @Override
    public List<BlackList> listByParkNoAndCarNumber(BlackList blackList) {
        LambdaQueryWrapper<BlackList> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), BlackList::getParkNo, SecurityUtils.getParkNo())
                .like(StringUtils.isNotEmpty(blackList.getCarNumber()), BlackList::getCarNumber, blackList.getCarNumber());
        return list(qw);
    }

    /**
     * @apiNote 获取黑名单列表
     */
    @Override
    public List<BlackList> listByParkNoAndCarNumberUnsafe(BlackList blackList) {
        LambdaQueryWrapper<BlackList> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(blackList.getParkNo()), BlackList::getParkNo, blackList.getParkNo())
                .eq(StringUtils.isNotEmpty(blackList.getCarNumber()), BlackList::getCarNumber, blackList.getCarNumber());
        return list(qw);
    }

    /**
     * @apiNote 新增单个黑名单
     */
    @Override
    public boolean add(BlackListVO blackListVO) {
        BlackList blackList = new BlackList();
        BeanUtils.copyBeanProp(blackList, blackListVO);
        blackList.setParkNo(SecurityUtils.getParkNo());
        blackList.setCreateBy(SecurityUtils.getUsername());
        blackList.setCreateTime(LocalDateTime.now());
        return save(blackList);
    }

    /**
     * @apiNote 编辑单个黑名单
     */
    @Override
    public boolean editById(BlackListParam blackListParam) {
        BlackList blackList = new BlackList();
        BeanUtils.copyBeanProp(blackList, blackListParam);
        blackList.setUpdateBy(SecurityUtils.getUsername());
        blackList.setUpdateTime(LocalDateTime.now());
        return updateById(blackList);
    }

    /**
     * @apiNote 黑名单列表导入
     */
    @Override
    public String importBlackList(List<BlackListBO> blackListBOList, boolean updateSupport) {
        if (StringUtils.isNull(blackListBOList) || blackListBOList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BlackListBO blackListBO : blackListBOList) {
            if (StringUtils.isEmpty(blackListBO.getCarNumber())) {
                failureMsg.append("<br/>").append(failureNum).append("、黑名单车牌号为空");
                continue;
            }
            LambdaQueryWrapper<BlackList> qw = new LambdaQueryWrapper<>();
            // 验证是否存在这个固定车
            BlackList blackList = getOne(qw.eq(BlackList::getCarNumber, blackListBO.getCarNumber())
                    .eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), BlackList::getParkNo, SecurityUtils.getParkNo()));
            try {
                if (StringUtils.isNull(blackList)) {
                    // 新增黑名单
                    BlackList addBlackList = new BlackList();
                    BeanUtils.copyBeanProp(addBlackList, blackListBO);
                    addBlackList.setParkNo(SecurityUtils.getParkNo());
                    addBlackList.setCreateBy(SecurityUtils.getUsername());
                    addBlackList.setCreateTime(LocalDateTime.now());
                    save(addBlackList);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、黑名单 ").append(blackListBO.getCarNumber()).append(" 导入成功");
                } else if (updateSupport) {
                    // 编辑黑名单
                    BlackList updateBlackList = new BlackList();
                    BeanUtils.copyBeanProp(updateBlackList, blackListBO);
                    updateBlackList.setId(blackList.getId());
                    updateBlackList.setUpdateBy(SecurityUtils.getUsername());
                    updateBlackList.setUpdateTime(LocalDateTime.now());
                    updateById(updateBlackList);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、黑名单 ").append(blackListBO.getCarNumber()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、黑名单 ").append(blackListBO.getCarNumber()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、黑名单 " + blackListBO.getCarNumber() + " 导入失败：";
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




