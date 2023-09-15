package com.ruoyi.project.parking.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.parking.domain.BPassage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "通道信息表", description = "")
@TableName("b_passage")
@Data
public class BPassageVo extends BPassage {
    private String fromFieldName;
    private String toFieldName;
}
