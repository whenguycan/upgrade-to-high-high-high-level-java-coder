package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.project.parking.domain.BPassageDevice;
import lombok.Data;

@Data
public class BPassageDeviceVo extends BPassageDevice {
    private String passageNo;
    private String fromFieldName;
    private String toFieldName;
    private String parkNo;
    private String passageName;
    private String passageFlag;

}
