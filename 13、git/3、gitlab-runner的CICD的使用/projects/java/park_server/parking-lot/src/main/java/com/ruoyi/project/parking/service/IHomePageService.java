package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.vo.HomePageHeaderVo;

/**
 * 首页Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IHomePageService
{
    /**
     * 首页顶部
     * 
     * @param deptId 场库id
     * @return 首页顶部
     */
    HomePageHeaderVo homePageHeader(Long deptId);

}
