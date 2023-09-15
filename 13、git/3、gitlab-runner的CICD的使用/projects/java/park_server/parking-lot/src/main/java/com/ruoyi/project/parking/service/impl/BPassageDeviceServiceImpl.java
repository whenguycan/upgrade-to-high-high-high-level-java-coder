package com.ruoyi.project.parking.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.domain.vo.DeviceStatisticsVO;
import com.ruoyi.project.parking.mapper.BPassageMapper;
import com.ruoyi.project.parking.mapper.HomePageMapper;
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
public class BPassageDeviceServiceImpl extends ServiceImpl<BPassageDeviceMapper, BPassageDevice> implements IBPassageDeviceService {
    @Resource
    private BPassageDeviceMapper bPassageDeviceMapper;

    @Autowired
    private BPassageMapper passageMapper;

    @Autowired
    private HomePageMapper homePageMapper;

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
    public List<BPassageDeviceVo> selectBPassageDeviceList(BPassageDeviceVo bPassageDevice) {
        return bPassageDeviceMapper.selectBPassageDeviceList(bPassageDevice);
    }

    @Override
    public List<BPassageDevice> selectDeviceList() {
        return bPassageDeviceMapper.selectList(null);
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

    /**
     * 查询设备数据统计
     *
     * @param parkNo 车场编号
     * @return 设备数据统计
     */
    @Override
    public List<DeviceStatisticsVO> getDeviceStatistics(String parkNo) {
        List<DeviceStatisticsVO> result = new ArrayList<>();
        // 查询所有归属子车场编号
        List<String> parkNos = homePageMapper.getChildParkNosByNo(parkNo);
        //根据车场编号查询通道号
        List<BPassage> bPassages = passageMapper.selectList(new LambdaQueryWrapper<BPassage>().in(BPassage::getParkNo, parkNos));
        if (bPassages.isEmpty()) {
            result.add(new DeviceStatisticsVO("在线设备", 0));
            result.add(new DeviceStatisticsVO("离线设备", 0));
            result.add(new DeviceStatisticsVO("故障维修", 0));
        } else {
            //todo 离线状态需要查询大华设备系统，先都用在线状态
//            deviceStatisticsVO.setOnlineNum(Math.toIntExact(bPassages.stream().filter(item -> "1".equals(item.getPassageStatus())).count()));
            result.add(new DeviceStatisticsVO("在线设备", bPassages.size()));
            result.add(new DeviceStatisticsVO("离线设备", 0));
            result.add(new DeviceStatisticsVO("故障维修", 0));
        }
        return result;
    }

    @Override
    public Long countDeviceByDeviceCode(String deviceCode) {
        LambdaQueryWrapper<BPassageDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BPassageDevice::getDeviceId, deviceCode);
        return count(queryWrapper);
    }
}
