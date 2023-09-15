package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BlackList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.bo.BlackListBO;
import com.ruoyi.project.parking.domain.param.BlackListParam;
import com.ruoyi.project.parking.domain.vo.BlackListVO;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【t_black_list(黑名单表)】的数据库操作Service
* @since 2023-02-27 17:21:11
*/
public interface IBlackListService extends IService<BlackList> {

    /**
     * @apiNote 获取黑名单列表
     */
    List<BlackList> listByParkNoAndCarNumber(BlackList blackList);

    /**
     * @apiNote 获取黑名单列表 无需认证
     */
    List<BlackList> listByParkNoAndCarNumberUnsafe(BlackList blackList);

    /**
     * @apiNote 新增单个黑名单
     */
    boolean add(BlackListVO blackListVO);

    /**
     * @apiNote 编辑单个黑名单
     */
    boolean editById(BlackListParam blackListParam);

    /**
     * @apiNote 黑名单列表导入
     */
    String importBlackList(List<BlackListBO> blackListBOList, boolean updateSupport);
}
