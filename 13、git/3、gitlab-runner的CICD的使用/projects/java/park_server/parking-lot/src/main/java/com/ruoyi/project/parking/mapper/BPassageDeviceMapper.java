package com.ruoyi.project.parking.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;

/**
 * 通道设备绑定Mapper接口
 *
 * @author ruoyi
 * @date 2023-02-23
 */
public interface BPassageDeviceMapper extends BaseMapper<BPassageDevice>
{
    Integer countBindDevice(Integer passageId);
    /**
     * 查询通道设备绑定
     *
     * @param id 通道设备绑定主键
     * @return 通道设备绑定
     */
    public BPassageDeviceVo selectBPassageDeviceById(Long id);

    /**
     * 查询通道设备绑定
     *
     * @param id 通道设备绑定设备编号
     * @return 通道设备绑定
     */
    public BPassageDeviceVo selectBPassageDeviceByPassageNo(String passageNo);

    /**
     * 查询通道设备绑定列表
     *
     * @param bPassageDevice 通道设备绑定
     * @return 通道设备绑定集合
     */
    public List<BPassageDeviceVo> selectBPassageDeviceList(BPassageDeviceVo bPassageDevice);

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
     * 删除通道设备绑定
     *
     * @param id 通道设备绑定主键
     * @return 结果
     */
    public int deleteBPassageDeviceById(Long id);

    /**
     * 批量删除通道设备绑定
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBPassageDeviceByIds(Long[] ids);
}
