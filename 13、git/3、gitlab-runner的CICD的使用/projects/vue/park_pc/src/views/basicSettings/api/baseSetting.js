/*
 * @Description: 基本信息设置 -- 车辆类型、闸道抬杆原因
 * @Author: Adela
 * @Date: 2023-02-24 09:51:31
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-24 09:52:35
 * @文件相对于项目的路径: /park_pc/src/views/basicSettings/api/baseSetting.js
 */
import request from '@/utils/request'

// 车辆类型 list
export function getSettingcartypeList(query) {
  return request({
    url: '/parking/settingcartype/list',
    method: 'get',
    params: query
  })
}

// add 车辆类型 list
export function AddSettingcartype(data) {
  return request({
    url: '/parking/settingcartype/add',
    method: 'post',
    data: data
  })
}

//  edit 车辆类型 list
export function EditSettingcartype(data) {
  return request({
    url: '/parking/settingcartype/edit',
    method: 'post',
    data: data
  })
}

//  delete 车辆类型 list
export function removeSettingcartype(id) {
  return request({
    url: 'parking/settingcartype/remove/' + id,
    method: 'get'
  })
}

// 启用
export function enableSettingcartype(id) {
  return request({
    url: 'parking/settingcartype/enable/' + id,
    method: 'get'
  })
}

// 停用
export function disableSettingcartype(id) {
  return request({
    url: 'parking/settingcartype/disable/' + id,
    method: 'get'
  })
}


// 闸道抬杆原因 list
export function getSettingliftgatereasonList(query) {
  return request({
    url: '/parking/settingliftgatereason/list',
    method: 'get',
    params: query
  })
}

// add 闸道抬杆原因 list
export function AddSettingliftgatereason(data) {
  return request({
    url: '/parking/settingliftgatereason/add',
    method: 'post',
    data: data
  })
}

//  edit 闸道抬杆原因 list
export function EditSettingliftgatereason(data) {
  return request({
    url: '/parking/settingliftgatereason/edit',
    method: 'post',
    data: data
  })
}

//  delete 闸道抬杆原因 list
export function removeSettingliftgatereason(id) {
  return request({
    url: '/parking/settingliftgatereason/remove/' + id,
    method: 'get'
  })
}

//  lot基本信息获取接口
export function getInfo(data) {
  return request({
    url: '/parking/bSettingLot/getInfo',
    method: 'post',
    data: data
  })
}

//  lot基本信息保存接口
export function add(data) {
  return request({
    url: '/parking/bSettingLot/add',
    method: 'post',
    data: data
  })
}

//  lot基本信息修改接口
export function edit(data) {
  return request({
    url: '/parking/bSettingLot/edit',
    method: 'post',
    data: data
  })
}
