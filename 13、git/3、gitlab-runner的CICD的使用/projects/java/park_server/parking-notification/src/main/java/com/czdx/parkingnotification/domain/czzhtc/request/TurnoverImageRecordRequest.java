package com.czdx.parkingnotification.domain.czzhtc.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 进出记录图片上传参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 14:34
 */
@Data
public class TurnoverImageRecordRequest {
    //region 必填参数
    //进出记录标识。主键。需要获取对应图片的进出记录的ID
    private int recordID;
    //记录图片的类型。0-车牌小图 1-车身图。固定为1
    private int type;
    //endregion
}
