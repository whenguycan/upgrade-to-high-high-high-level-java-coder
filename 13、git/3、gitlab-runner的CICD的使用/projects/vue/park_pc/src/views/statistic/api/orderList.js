/*
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 16:12:38
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-28 14:41:33
 * @文件相对于项目的路径: \park_pc\src\views\statistic\api\orderList.js
 */
import request from '@/utils/request'

//获取离场订单列表
export function getOrderList(query) {
  return request({
    url: 'parking/parksettlementrecords/list',
    method: 'get',
    params: query
  })
}

//新增离场订单
export function addOrder(data) {
  return request({
    url: 'parking/parksettlementrecords/add',
    method: 'post',
    data: data
  })
}

//删除离场订单
export function deleteOrder(id) {
  return request({
    url: 'parking/parksettlementrecords/remove/'+id,
    method: 'get',
  })
}

//获取离场订单详情
export function getDetailOrder(id) {
  return request({
    url: 'parking/parksettlementrecords/info?id='+id,
    method: 'get',
  })
}