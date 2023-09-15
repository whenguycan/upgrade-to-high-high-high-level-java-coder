package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.BMyCar;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BMyCarParam extends BMyCar {

    /** 会员ID */
    @NotBlank(message = "会员ID不可为空")
    private String userId;
}
