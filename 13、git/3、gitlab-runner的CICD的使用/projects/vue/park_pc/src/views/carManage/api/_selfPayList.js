/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-01 15:50:11
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-01 16:27:29
 * @文件相对于项目的路径: /park_pc/src/views/carManage/api/_selfPayList.js
 */
import request from '@/utils/request'

const selfPaySchemeBaseUrl = '/parking/selfPayScheme' // 自主缴费 base
const _selfPaySchemeUrl = {
  getSelfPaySchemeDetail: `${selfPaySchemeBaseUrl}/detail`, // 根据场库编号获取单个自主缴费方案
  editSelfPayScheme: `${selfPaySchemeBaseUrl}/edit`, // 编辑自主缴费方案
}

const selfPayBaseUrl = '/parking/selfPay' // 自主缴费审核 base
const _selfPayUrl = {
  getSelfPayList: `${selfPayBaseUrl}/list`, // 获取自主缴费审核列表
  addSelfPay: `${selfPayBaseUrl}/add`, // 申请自主缴费
  agreeSelfPay: `${selfPayBaseUrl}/agree/`, // 审核通过自主缴费
  disagreeSelfPay: `${selfPayBaseUrl}/disagree`, // 审核不通过自主缴费 -- 驳回

  importSelfPayData: `${selfPayBaseUrl}/importData`, // 自主缴费列表导入
  importSelfPayTemplate: `${selfPayBaseUrl}/importTemplate`, // 自主缴费列表导入模板
  exportSelfPay: `${selfPayBaseUrl}/export`, // 自主缴费列表导出
}

// 根据场库编号获取单个自主缴费方案
export function getSelfPaySchemeDetail(query) {
  return request({
    url: _selfPaySchemeUrl.getSelfPaySchemeDetail,
    method: 'get',
    params: query
  })
}

// 编辑自主缴费方案
export function editSelfPayScheme(data) {
  return request({
    url: _selfPaySchemeUrl.editSelfPayScheme,
    method: 'post',
    data: data
  })
}


// 获取自主缴费审核列表
export function getSelfPayList(query) {
  return request({
    url: _selfPayUrl.getSelfPayList,
    method: 'get',
    params: query
  })
}

// 申请自主缴费
export function addSelfPay(data) {
  return request({
    url: _selfPayUrl.addSelfPay,
    method: 'post',
    data: data
  })
}

// 审核通过自主缴费
export function agreeSelfPay(id) {
  return request({
    url: _selfPayUrl.agreeSelfPay + id,
    method: 'get'
  })
}

// 审核不通过自主缴费 -- 驳回
export function disagreeSelfPay(id) {
  return request({
    url: _selfPayUrl.disagreeSelfPay + id,
    method: 'get'
  })
}
