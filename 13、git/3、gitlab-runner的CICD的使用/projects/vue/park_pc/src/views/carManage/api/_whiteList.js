/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 09:37:35
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-28 15:12:53
 * @文件相对于项目的路径: \park_pc\src\views\carManage\api\_whiteList.js
 */
import request from '@/utils/request'

// 白名单列表 list
export function getWhiteList(query) {
  return request({
    url: '/parking/whiteList/list',
    method: 'get',
    params: query
  })
}

//新增白名单
export function addWhite(data) {
  return request({
    url: '/parking/whiteList/add',
    method: 'post',
    data: data
  })
}

//编辑白名单
export function editWhite(data) {
  return request({
    url: '/parking/whiteList/edit',
    method: 'post',
    data: data
  })
}

//删除白名单
export function deleteWhite(id) {
  return request({
    url: '/parking/whiteList/remove/'+id,
    method: 'get',
  })
}