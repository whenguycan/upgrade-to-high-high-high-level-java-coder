package com.czdx.common;

/**
 *
 * description: 消息队列常量
 * @author mingchenxu
 * @date 2023/2/23 14:37
 */
public class RabbitmqConstant {

    private RabbitmqConstant() {}

    // 队列 start
    // 停车车辆入场通知
    public static final String QUEUE_PARKING_CAR_ENTRY_NOTIFICATION = "queue-parking-car-entry-notification";

    // 队列 end，交换机 start
    // 停车车辆入场通知
    public static final String EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION = "exchange-parking-car-entry-notification";


    // 交换机 end，路由规则 start
    // 停车车辆入场通知
    public static final String ROUTE_PARKING_CAR_ENTRY_NOTIFICATION = "route-parking-car-entry-notification";

    // 队列 start
    // 停车车辆离场通知
    public static final String QUEUE_PARKING_CAR_EXIT_NOTIFICATION = "queue-parking-car-exit-notification";

    // 队列 end，交换机 start
    // 停车车辆离场通知
    public static final String EXCHANGE_PARKING_CAR_EXIT_NOTIFICATION = "exchange-parking-car-exit-notification";


    // 交换机 end，路由规则 start
    // 停车车辆离场通知
    public static final String ROUTE_PARKING_CAR_EXIT_NOTIFICATION = "route-parking-car-exit-notification";

    // 队列 start
    // 支付成功消息通知
    public static final String QUEUE_PARKING_PAYMENT = "queue_parking_payment";

    // 队列 end，交换机 start
    // 支付成功消息通知
    public static final String EXCHANGE_PARKING_PAYMENT = "exchange_parking_payment";


    // 交换机 end，路由规则 start
    // 支付成功消息通知
    public static final String ROUTE_PARKING_PAYMENT = "route_parking_payment";

    //订单延迟消息交换机
    public static final String ORDER_DELAYED_MESSAGE_EXCHANGE = "order.deplyed.exchange";

    //订单延迟消息队列
    public static final String ORDER_DELAYED_MESSAGE_QUEUE = "order.delayed.queue";

    //订单延迟消息路由key
    public static final String ORDER_DELAYED_ROUTING_KEY = "order.delayed.routekey";

    //支付结果推送消息交换机
    public static final String PAYMENT_RESULT_EXCHANGE = "payment.notify.exchange";

    //支付结果推送消息队列
    public static final String PAYMENT_RESULT_QUEUE = "payment.notify.queue";

    //支付结果推送消息路由key
    public static final String PAYMENT_RESULT_ROUTING_KEY = "payment.notify.routingKey";

    //所有订单完成之后的交换机
    public static final String ORDER_PAYSUCCESS_EXCHANGE = "order_prysuccess_exchange";

    //月租订单的路由键
    public static final String MONTHLY_ORDER_ROUTING_KEY = "monthly.monthly_order_routing_key";

    //月租订单完成之后的的队列
    public static final String MONTHLY_ORDER_QUEUE = "monthly_order_queue";

    //商户订单的路由键
    public static final String MERCHANT_ORDER_ROUTING_KEY = "merchant.merchant_order_routing_key";

    //商户订单完成之后的队列
    public static final String MERCHANT_ORDER_QUEUE = "merchant_order_queue";




}
