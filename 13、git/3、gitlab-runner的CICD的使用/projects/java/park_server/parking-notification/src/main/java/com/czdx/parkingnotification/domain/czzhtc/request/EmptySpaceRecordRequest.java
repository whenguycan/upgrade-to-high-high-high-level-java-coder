package com.czdx.parkingnotification.domain.czzhtc.request;

import lombok.Data;

/**
 * <p>
 * 空车位数上传参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 14:34
 */
@Data
public class EmptySpaceRecordRequest {
    //region 必填参数
    //空车位数
    private int spaceCount;
    //总车位数
    private int totalCount;
    //endregion
}
