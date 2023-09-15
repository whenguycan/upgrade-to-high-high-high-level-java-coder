/*
 * @Description:
 * @Author: cuijing
 * @Date: 2023-02-27 09:37:35
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-06 14:53:42
 * @文件相对于项目的路径: \park_pc\src\views\memberManage\api\memberList.js
 */
import request from '@/utils/request'

// 会员列表 list
export function getMemberList(query) {
  return request({
    url: '/member/user/list',
    method: 'get',
    params: query
  })
}
// 查询会员详情
export function getMember(id) {
  return request({
    url: '/member/user/getInfo/'+id,
    method: 'get',
  })
}
//查询会员车辆详情
export function getMemberDetail(id) {
  return request({
    url: '/member/user/'+id,
    method: 'get',
  })
}

// 会员新增车辆
export function memberBindCar(data) {
  return request({
    url: '/member/user/bindVehicle',
    method: 'post',
    data: data
  })
}

// 会员解绑车辆
export function memberUnBindCar(id) {
  return request({
    url: '/member/user/unbindVehicle/'+id,
    method: 'post',
  })
}

// 会员车辆设为默认
export function setDefault(data) {
  return request({
    url: '/member/user/setDefault',
    method: 'post',
    data: data
  })
}