/*
 * @Description:
 * @Author: cuijing
 * @Date: 2023-02-27 09:37:35
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-06 14:53:42
 * @文件相对于项目的路径: \park_pc\src\views\memberManage\api\memberList.js
 */
import request from "@/utils/merchantRequest";

// 审核列表 list
export function getAuditList(query) {
  return request({
    url: '/merchant/audit/list',
    method: 'get',
    params: query
  })
}
// 查询审核记录详情
export function getInfo(id) {
  return request({
    url: '/merchant/audit/'+id,
    method: 'get',
  })
}

// 审核接口
export function doAudit(data) {
  return request({
    url: '/merchant/audit/audit',
    method: 'post',
    data: data
  })
}
