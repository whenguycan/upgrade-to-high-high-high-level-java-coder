package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.vo.BPassageVo;
import com.ruoyi.project.parking.mapper.BPassageMapper;
import com.ruoyi.project.parking.service.BPassageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道信息表;(b_passage)表服务实现类
 *
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@Service
public class BPassageServiceImpl implements BPassageService {
    @Autowired
    private BPassageMapper bPassageMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public BPassage queryById(Integer id) {
        return bPassageMapper.selectById(id);
    }

    @Override
    public List<BPassageVo> qryList(BPassageVo bPassage) {
        return bPassageMapper.qryList(bPassage);
    }

//    /**
//     * 分页查询
//     *
//     * @param bPassage 筛选条件
//     * @return
//     */
//    public List<BPassage> findList(BPassage bPassage){
//        //1. 构建动态查询条件
//        LambdaQueryWrapper<BPassage> queryWrapper = new LambdaQueryWrapper<>();
//        if(StringUtils.isNotBlank(bPassage.getPassageName())){
//            queryWrapper.eq(BPassage::getPassageName, bPassage.getPassageName());
//        }
//        if(StringUtils.isNotBlank(bPassage.getOpenType())){
//            queryWrapper.eq(BPassage::getOpenType, bPassage.getOpenType());
//        }
//        if(StringUtils.isNotBlank(bPassage.getPassageStatus())){
//            queryWrapper.eq(BPassage::getPassageStatus, bPassage.getPassageStatus());
//        }
//        if(StringUtils.isNotBlank(bPassage.getRemark())){
//            queryWrapper.eq(BPassage::getRemark, bPassage.getRemark());
//        }
//        if(StringUtils.isNotBlank(bPassage.getCreateBy())){
//            queryWrapper.eq(BPassage::getCreateBy, bPassage.getCreateBy());
//        }
//        if(StringUtils.isNotBlank(bPassage.getUpdateBy())){
//            queryWrapper.eq(BPassage::getUpdateBy, bPassage.getUpdateBy());
//        }
//        //2. 执行分页查询
//        return  bPassageMapper.selectList(queryWrapper);
//    }

    /**
     * 新增数据
     *
     * @param bPassage 实例对象
     * @return 实例对象
     */
    public int insert(BPassage bPassage) {
        return bPassageMapper.insert(bPassage);
    }

    /**
     * 更新数据
     *
     * @param bPassage 实例对象
     * @return 实例对象
     */
    public int update(BPassage bPassage) {

        return bPassageMapper.updateById(bPassage);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    public boolean deleteById(Integer id) {
        int total = bPassageMapper.deleteById(id);
        return total > 0;
    }

    @Override
    public Long countPassageByPassageName(String passageName, String parkNo) {
        BPassage bPassage = new BPassage();
        bPassage.setParkNo(parkNo);
        bPassage.setPassageName(passageName);
        return countPassage(bPassage);
    }

    @Override
    public Long countPassageByPassageNo(String passageNo, String parkNo) {
        BPassage bPassage = new BPassage();
        bPassage.setParkNo(parkNo);
        bPassage.setPassageNo(passageNo);
        return countPassage(bPassage);
    }


    @Override
    public String selectPassageNameByPassageNo(String passageNo) {
        LambdaQueryWrapper<BPassage> qw = new LambdaQueryWrapper<>();
        qw.eq(BPassage::getPassageNo, passageNo);
        BPassage bPassage = bPassageMapper.selectOne(qw);
        if (bPassage == null) {
            return StringUtils.EMPTY;
        } else {
            return bPassage.getPassageName();
        }
    }

    private Long countPassage(BPassage bPassage) {
        LambdaQueryWrapper<BPassage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BPassage::getParkNo, bPassage.getParkNo());
        if (StringUtils.isNotEmpty(bPassage.getPassageName())) {
            queryWrapper.eq(BPassage::getPassageName, bPassage.getPassageName());
            return bPassageMapper.selectCount(queryWrapper);
        }
        if (StringUtils.isNotEmpty(bPassage.getPassageNo())) {
            queryWrapper.eq(BPassage::getPassageNo, bPassage.getPassageNo());
            return bPassageMapper.selectCount(queryWrapper);
        }
        return 0L;
    }
}
