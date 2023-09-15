package com.czdx.common;

/**
 *
 * description: 消息队列常量
 * @author mingchenxu
 * @date 2023/2/23 14:37
 */
public class RabbitmqConstant {

    private RabbitmqConstant() {}

    //region  停车车辆入场通知
    // 停车车辆入场通知
    public static final String QUEUE_PARKING_CAR_ENTRY_NOTIFICATION = "queue-parking-car-entry-notification";

    // 停车车辆入场通知
    public static final String EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION = "exchange-parking-car-entry-notification";


    // 停车车辆入场通知
    public static final String ROUTE_PARKING_CAR_ENTRY_NOTIFICATION = "route-parking-car-entry-notification";
    //endregion

    // region 停车车辆离场通知
    // 停车车辆离场通知
    public static final String QUEUE_PARKING_CAR_EXIT_NOTIFICATION = "queue-parking-car-exit-notification";

    // 队列 end，交换机 start
    // 停车车辆离场通知
    public static final String EXCHANGE_PARKING_CAR_EXIT_NOTIFICATION = "exchange-parking-car-exit-notification";


    // 交换机 end，路由规则 start
    // 停车车辆离场通知
    public static final String ROUTE_PARKING_CAR_EXIT_NOTIFICATION = "route-parking-car-exit-notification";
    //endregion

    //region 场库系统推送支付成功后消息处理
    // 支付成功消息通知
    public static final String EXCHANGE_LOT_PARKING_PAYSUCCESS = "exchange_parking_payment";

    // 硬件系统道闸支付成功消息通知
    public static final String ROUTE_DEVICE_PARKING_PAYSUCCESS = "route_device_parking_paysuccess";

    // 硬件系统道闸支付成功消息通知
    public static final String QUEUE_DEVICE_PARKING_PAYSUCCESS = "queue_device_parking_paysuccess";

    // 通知系统微信公众号推送支付成功消息通知
    public static final String ROUTE_NOTIFICATION_PARKING_PAYSUCCESS = "route_notification_parking_paysuccess";

    // 通知系统微信公众号推送支付成功消息通知
    public static final String QUEUE_NOTIFICATION_PARKING_PAYSUCCESS = "queue_notification_parking_paysuccess";

    // 场库系统支付成功后检查道闸是否抬杆，是否正常离场 延时消息通知
    public static final String EXCHANGE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY = "exchange_lot_parking_paysuccess_exit_delay";

    // 场库系统支付成功后检查道闸是否抬杆，是否正常离场 延时消息通知
    public static final String ROUTE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY = "route_lot_parking_paysuccess_exit_delay";

    // 场库系统支付成功后检查道闸是否抬杆，是否正常离场 延时消息通知
    public static final String QUEUE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY = "queue_lot_parking_paysuccess_exit_delay";
    //endregion

    //region 订单系统自用延迟消息队列 处理超时未支付订单
    //订单延迟消息交换机
    public static final String ORDER_DELAYED_MESSAGE_EXCHANGE = "order.deplyed.exchange";

    //订单延迟消息队列
    public static final String ORDER_DELAYED_MESSAGE_QUEUE = "order.delayed.queue";

    //订单延迟消息路由key
    public static final String ORDER_DELAYED_ROUTING_KEY = "order.delayed.routekey";
    //endregion

    //region 支付系统推送订单系统支付结果消息
    
    //支付结果推送消息交换机
    public static final String EXCHANGE_PAYMENT_ORDER_RESULT = "payment.notify.exchange";

    //支付结果推送消息路由key
    public static final String ROUTE_ORDER_PAYMENT_RESULT = "payment.notify.routingKey";

    //支付结果推送消息队列
    public static final String QUEUE_ORDER_PAYMENT_RESULT = "payment.notify.queue";

    //endregion

    //region 订单系统推送场库系统、会员系统 所有订单完成后的业务处理
    //所有订单完成之后的交换机
    public static final String EXCHANGE_ORDER_PAYSUCCESS = "order_prysuccess_exchange";

    //月租订单的路由键
    public static final String ROUTE_MEMBER_MONTHLY_ORDER_PAYSUCCESS = "monthly.monthly_order_routing_key";

    //月租订单完成之后的的队列
    public static final String QUEUE_MEMBER_MONTHLY_ORDER_PAYSUCCESS = "monthly_order_queue";

    //商户订单的路由键
    public static final String ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS = "merchant.merchant_order_routing_key";

    //商户订单完成之后的队列
    public static final String QUEUE_MEMBER_MERCHANT_ORDER_PAYSUCCESS = "merchant_order_queue";
    
    //车场订单支付成功之后，需要推消息到微信，所用的消息队列routing_key
    public static final String ROUTE_LOT_PARKING_ORDER_PAYSUCCESS = "parking.parking_order_payed_routing_key";

    //车场订单支付成功之后，需要推消息到微信，所用的消息队列
    public static final String QUEUE_LOT_PARKING_ORDER_PAYSUCCESS = "parking_order_payed_queue";
    //endregion




}
