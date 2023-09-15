package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.mapper.BFieldMapper;
import com.ruoyi.project.parking.service.BFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 区域管理表;(b_field)表服务实现类
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@Service
public class BFieldServiceImpl implements BFieldService{
    @Autowired
    private BFieldMapper bFieldMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public BField queryById(Integer id){
        return bFieldMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @return
     */
    public List<BField> findList(BField bField){
        //1. 构建动态查询条件
        LambdaQueryWrapper<BField> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bField.getFieldName())){
            queryWrapper.eq(BField::getFieldName, bField.getFieldName());
        }
        if(StringUtils.isNotBlank(bField.getFieldStatus())){
            queryWrapper.eq(BField::getFieldStatus, bField.getFieldStatus());
        }
        if(StringUtils.isNotBlank(bField.getRemark())){
            queryWrapper.eq(BField::getRemark, bField.getRemark());
        }
        if(StringUtils.isNotBlank(bField.getCreateBy())){
            queryWrapper.eq(BField::getCreateBy, bField.getCreateBy());
        }
        if(StringUtils.isNotBlank(bField.getParkNo())){
            queryWrapper.eq(BField::getParkNo, bField.getParkNo());
        }
        if(StringUtils.isNotBlank(bField.getUpdateBy())){
            queryWrapper.eq(BField::getUpdateBy, bField.getUpdateBy());
        }
        return bFieldMapper.selectList(queryWrapper);
    }

    /**
     * 新增数据
     *
     * @param bField 实例对象
     * @return 实例对象
     */
    public int insert(BField bField){
        bField.setCreateTime(new Date());
        bField.setCreateBy(SecurityUtils.getUsername());
       return  bFieldMapper.insert(bField);
    }

    /**
     * 更新数据
     *
     * @param bField 实例对象
     * @return 实例对象
     */
    public int update(BField bField){
        bField.setUpdateTime(new Date());
        bField.setUpdateBy(SecurityUtils.getUsername());
        return bFieldMapper.updateById(bField);


    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    public boolean deleteById(Integer id){
        int total = bFieldMapper.deleteById(id);
        return total > 0;
    }
}
