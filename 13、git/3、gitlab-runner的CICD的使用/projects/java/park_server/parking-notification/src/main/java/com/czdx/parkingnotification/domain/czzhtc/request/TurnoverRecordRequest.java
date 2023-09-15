package com.czdx.parkingnotification.domain.czzhtc.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 进出记录上传参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 14:34
 */
@Data
public class TurnoverRecordRequest {
    //region 必填参数
    //进出记录标识。主键。入场和出场必须是不同的recordid
    private int recordID;
    //通行时间。格式：yyyy-MM-dd HH:mm:ss
    private LocalDateTime recordTime;
    //车牌号
    private String plate;
    //车牌颜色
    private int color;
    //记录类型。0入场，1出场
    private int recordType;
    //通道编号。场库出入口通道的编号（数字不能为 0或者1），可从2 开始
    private String DevNum;
    //endregion
    //是否人工确认过车牌。0-否；1-是
    private int confirmFlag;
    //对应收费记录标识。多次收费记录的，上传最后一次收费记录的标识
    private int billID;
}
