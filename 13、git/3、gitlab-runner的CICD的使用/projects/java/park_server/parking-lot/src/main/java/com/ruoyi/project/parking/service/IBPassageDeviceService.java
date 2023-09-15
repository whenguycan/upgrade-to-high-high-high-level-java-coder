package com.ruoyi.project.parking.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.domain.vo.DeviceStatisticsVO;

/**
 * 通道设备绑定Service接口
 *
 * @author ruoyi
 * @date 2023-02-23
 */
public interface IBPassageDeviceService extends IService<BPassageDevice> {


    public boolean existDeviceBindPassage(Integer passageId);

    /**
     * 查询通道设备绑定
     *
     * @param id 通道设备绑定主键
     * @return 通道设备绑定
     */
    public BPassageDevice selectBPassageDeviceById(Long id);

    /**
     * 查询通道设备绑定列表
     *
     * @param bPassageDevice 通道设备绑定
     * @return 通道设备绑定集合
     */
    public List<BPassageDeviceVo> selectBPassageDeviceList(BPassageDeviceVo bPassageDevice);


    /**
     * 查询通道设备绑定列表
     *
     * @return 通道设备绑定集合
     */
    public List<BPassageDevice> selectDeviceList();


    /**
     * 新增通道设备绑定
     *
     * @param bPassageDevice 通道设备绑定
     * @return 结果
     */
    public int insertBPassageDevice(BPassageDevice bPassageDevice);

    /**
     * 修改通道设备绑定
     *
     * @param bPassageDevice 通道设备绑定
     * @return 结果
     */
    public int updateBPassageDevice(BPassageDevice bPassageDevice);

    /**
     * 批量删除通道设备绑定
     *
     * @param ids 需要删除的通道设备绑定主键集合
     * @return 结果
     */
    public int deleteBPassageDeviceByIds(Long[] ids);

    /**
     * 删除通道设备绑定信息
     *
     * @param id 通道设备绑定主键
     * @return 结果
     */
    public int deleteBPassageDeviceById(Long id);

    /**
     * 查询设备数据统计
     *
     * @param parkNo 车场编号
     * @return 设备数据统计
     */
    List<DeviceStatisticsVO> getDeviceStatistics(String parkNo);

    public Long countDeviceByDeviceCode(String deviceCode);

}
