/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-02 14:54:51
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-06 17:54:20
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/api/_visitorCodeManageList.js
 */
import request from '@/utils/request'

const _baseUrl = '/parking/BVisitorCodeManage' // 访客码管理base
const _Url = {
  getVisitorCodeList: `${_baseUrl}/list`, // 列表
  addVisitorCodeItem: `${_baseUrl}/add`, // 新增
  editVisitorCodeItem: `${_baseUrl}/edit`, // 修改
  deleteVisitorCodeItem: `${_baseUrl}/delete/`, // 删除
  activateVisitorCodeItem: `${_baseUrl}/activate/`, //  启用
  deactivateVisitorCodeItem: `${_baseUrl}/deactivate/`, //  停用
  getDetail: `${_baseUrl}/`, // 详情
}

// list
export function getVisitorCodeList(query) {
  return request({
    url: _Url.getVisitorCodeList,
    method: 'get',
    params: query
  })
}

// add
export function addVisitorCodeItem(data) {
  return request({
    url: _Url.addVisitorCodeItem,
    method: 'post',
    data: data
  })
}

// edit
export function editVisitorCodeItem(data) {
  return request({
    url: _Url.editVisitorCodeItem,
    method: 'post',
    data: data
  })
}

//删除
export function deleteVisitorCodeItem(id) {
  return request({
    url: _Url.deleteVisitorCodeItem + id,
    method: 'get',
  })
}
// activate 启用
export function activateVisitorCodeItem(id) {
  return request({
    url: _Url.activateVisitorCodeItem + id,
    method: 'get',
  })
}

// deactivate 停用
export function deactivateVisitorCodeItem(id) {
  return request({
    url: _Url.deactivateVisitorCodeItem + id,
    method: 'get',
  })
}


// 详情
export function getDetail(id) {
  return request({
    url: _Url.getDetail + id,
    method: 'get'
  })
}
