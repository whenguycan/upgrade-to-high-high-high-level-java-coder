package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.vo.BPassageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通道信息表;(b_passage)表数据库访问层
 *
 * @author : http://www.chiner.pro
 * @date : 2023-2-21
 */
@Mapper
public interface BPassageMapper extends BaseMapper<BPassage> {

   List qryList(BPassageVo vo);
}
