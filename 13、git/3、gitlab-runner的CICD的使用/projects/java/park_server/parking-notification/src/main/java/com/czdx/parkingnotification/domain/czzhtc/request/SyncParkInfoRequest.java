package com.czdx.parkingnotification.domain.czzhtc.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 车场信息同步上传参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 14:34
 */
@Data
public class SyncParkInfoRequest {
    //region 必填参数
    //车场名称
    private String parkName;
    //收费标准
    private String rateInfo;
    //城市代码。参照中华人民共和国国家统计局网站发布的行政区划代码
    //http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html
    //常州市：320400000000
    private String cityCode;
    //endregion
    //地址
    private String address;
    //经度
    private double coordinateX;
    //纬度
    private double coordinateY;
}
