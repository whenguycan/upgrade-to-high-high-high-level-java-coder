package com.ll.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ll.goods.dao.GoodsDao;
import com.ll.goods.entity.GoodsEntity;
import com.ll.goods.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:59 AM
 * @Description: 类描述信息
 */
@Service("GoodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, GoodsEntity> implements GoodsService {
}
