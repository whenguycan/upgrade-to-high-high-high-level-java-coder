package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.WhiteList;
import com.ruoyi.project.parking.domain.bo.WhiteListBO;
import com.ruoyi.project.parking.domain.param.WhiteListParam;
import com.ruoyi.project.parking.domain.vo.WhiteListVO;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【t_white_list(白名单表)】的数据库操作Service
* @since 2023-02-27 17:21:16
*/
public interface IWhiteListService extends IService<WhiteList> {

    /**
     * @apiNote 获取白名单列表
     */
    List<WhiteList> listByParkNoAndCarNumber(WhiteList whiteList);

    /**
     * @apiNote 新增单个白名单
     */
    boolean add(WhiteListVO whiteListVO);

    /**
     * @apiNote 编辑单个白名单
     */
    boolean editById(WhiteListParam whiteListParam);

    /**
     * @apiNote 白名单列表导入
     */
    String importWhiteList(List<WhiteListBO> whiteListBOList, boolean updateSupport);

    /**
     * @apiNote 新增单个白名单
     */
    boolean addWhiteList(WhiteListVO whiteListVO);

    /**
     * @apiNote 获取白名单列表
     */
    public List<WhiteList> listByParkNoAndCarNumberUnsafe(WhiteList whiteList);
}
