/*
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-06 16:00:20
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-06 16:02:38
 * @文件相对于项目的路径: \park_pc\src\views\statistic\api\onOrderList.js
 */
import request from '@/utils/request'

//获取在场订单列表
export function getOrderList(query) {
  return request({
    url: '/parking/parkliverecords/list',
    method: 'get',
    params: query
  })
}

//删除在场订单列表
export function deleteOrder(id) {
  return request({
    url: '/parking/parkliverecords/remove/'+id,
    method: 'get',
  })
}


// 批量手动结算
export function settleOrder(data){
  return request({
    url:'/parking/parkliverecords/manual',
    method:'post',
    data: data
  })
}
