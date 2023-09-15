package com.ruoyi.project.parking.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.BPassageDeviceMapper;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.service.IBPassageDeviceService;

import javax.annotation.Resource;

/**
 * 通道设备绑定Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-23
 */
@Service
public class BPassageDeviceServiceImpl implements IBPassageDeviceService {
    @Resource
    private BPassageDeviceMapper bPassageDeviceMapper;

    @Override
    public boolean existDeviceBindPassage(Integer passageId) {

        return bPassageDeviceMapper.countBindDevice(passageId) > 0 ? true : false;
    }

    /**
     * 查询通道设备绑定
     *
     * @param id 通道设备绑定主键
     * @return 通道设备绑定
     */
    @Override
    public BPassageDevice selectBPassageDeviceById(Long id) {
        return bPassageDeviceMapper.selectBPassageDeviceById(id);
    }

    /**
     * 查询通道设备绑定列表
     *
     * @param bPassageDevice 通道设备绑定
     * @return 通道设备绑定
     */
    @Override
    public List<BPassageDeviceVo> selectBPassageDeviceList(BPassageDevice bPassageDevice) {
        return bPassageDeviceMapper.selectBPassageDeviceList(bPassageDevice);
    }

    /**
     * 新增通道设备绑定
     *
     * @param bPassageDevice 通道设备绑定
     * @return 结果
     */
    @Override
    public int insertBPassageDevice(BPassageDevice bPassageDevice) {
        bPassageDevice.setCreateTime(DateUtils.getNowDate());
        return bPassageDeviceMapper.insertBPassageDevice(bPassageDevice);
    }

    /**
     * 修改通道设备绑定
     *
     * @param bPassageDevice 通道设备绑定
     * @return 结果
     */
    @Override
    public int updateBPassageDevice(BPassageDevice bPassageDevice) {
        bPassageDevice.setUpdateTime(DateUtils.getNowDate());
        return bPassageDeviceMapper.updateBPassageDevice(bPassageDevice);
    }

    /**
     * 批量删除通道设备绑定
     *
     * @param ids 需要删除的通道设备绑定主键
     * @return 结果
     */
    @Override
    public int deleteBPassageDeviceByIds(Long[] ids) {
        return bPassageDeviceMapper.deleteBPassageDeviceByIds(ids);
    }

    /**
     * 删除通道设备绑定信息
     *
     * @param id 通道设备绑定主键
     * @return 结果
     */
    @Override
    public int deleteBPassageDeviceById(Long id) {
        return bPassageDeviceMapper.deleteBPassageDeviceById(id);
    }
}
