package com.czdx.parkingnotification.domain.czzhtc.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 收费记录上传参数
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 14:33
 */
@Data
public class BillRecordRequest {
    //region 必填参数
    //收费记录。主键。一次停车分多次支付，每次支付的billid不得重复，并且与临停计费查询接口返回的billid一致
    private int billID;
    //收费时间。格式：yyyy-MM-dd HH:mm:ss
    private LocalDateTime billTime;
    //车牌号
    private String plate;
    //车牌颜色。-1-未知；1-蓝色；2-白色；3-黑色；4-黄色；5-绿色
    private int color;
    //开始时间。格式：yyyy-MM-dd HH:mm:ss
    private LocalDateTime startTime;
    //结束时间。格式：yyyy-MM-dd HH:mm:ss
    private LocalDateTime endTime;
    //收费方式。
    // 0-出口收费；
    // 1-中央收费；
    // 2-提前收费（出入口拥堵时，使用手持机、pad等移动设备提前收费）；
    // 3-APP缴费（使用常州智慧停车微信公众号或APP进行线上支付）；
    // 6-自助缴费机
    private int billType;
    //支付方式。
    // 0-现金支付；
    // 1-支付宝支付；
    // 2-微信支付；
    // 3-银联支付
    private int payType;
    //线上支付渠道
    //0:现金支付
    //1:第三方线上支付收费（即车场自有渠道线上支付）
    //2:常州智慧停车线上收费（即云平台线上支付）
    private int payChannel;
    //应收。单位（元）
    private double should;
    //实收。单位（元）
    private double paid;
    //endregion
    //车辆类型
    //1-小型车；
    //2-中型车；
    //3-大型车；
    //5-军警车辆；
    private int carType;
    //优惠金额。单位（元）
    private double freeMoney;
    //使用充值金额。单位（元）
    private double prePaid;
    //提前收费金额。单位（元）。提前收费金额指出场前本次进出记录已经收费的金额
    private double advPaid;
    //会员卡优惠金额
    private double memberFreeMoney;
    //固定用户按临停计算的优惠金额。如果按临停计费，账单的优惠金额
    private double regularFreeMoney;
    //账单编号。如果使用App或者其他非现金支付方式，需要保留第三方支付账单编号
    private String orderCode;
    //电子优惠券编号。同时使用到多张电子优惠券时，将编号以英文半角逗号形式连接
    private String discountCodes;
    //备注信息
    private String remark;
}
