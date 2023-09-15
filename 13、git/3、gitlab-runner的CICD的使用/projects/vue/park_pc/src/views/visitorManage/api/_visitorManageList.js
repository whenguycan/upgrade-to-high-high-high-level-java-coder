/*
 * @Description: 访客审核管理接口
 * @Author: Adela
 * @Date: 2023-03-10 15:04:50
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-13 08:34:43
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/api/_visitorManageList.js
 */
import request from '@/utils/request'

const _baseUrl = '/parking/BVisitorApplyManage' // 访客审核管理base
const _Url = {
  getVisitorMList: `${_baseUrl}/list`, // 列表
  approveVisitorMItems: `${_baseUrl}/approve/`, // 审核
  disApproveVisitorMItems: `${_baseUrl}/reject`, //  驳回
}

// list
export function getVisitorMList(query) {
  return request({
    url: _Url.getVisitorMList,
    method: 'get',
    params: query
  })
}

//审核
export function approveVisitorMItems(id) {
  return request({
    url: _Url.approveVisitorMItems + id,
    method: 'get',
  })
}
// activate 启用
export function disApproveVisitorMItems(query) {
  return request({
    url: _Url.disApproveVisitorMItems,
    method: 'get',
    params: query
  })
}
