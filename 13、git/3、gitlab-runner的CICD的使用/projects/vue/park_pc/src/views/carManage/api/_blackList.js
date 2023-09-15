/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 09:37:35
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-28 15:12:53
 * @文件相对于项目的路径: \park_pc\src\views\carManage\api\_whiteList.js
 */
import request from '@/utils/request'

// 黑名单列表 list
export function getBlackList(query) {
  return request({
    url: '/parking/blackList/list',
    method: 'get',
    params: query
  })
}

//新增黑名单
export function addBlack(data) {
  return request({
    url: '/parking/blackList/add',
    method: 'post',
    data: data
  })
}

//编辑黑名单
export function editBlack(data) {
  return request({
    url: '/parking/blackList/edit',
    method: 'post',
    data: data
  })
}

//删除黑名单
export function deleteBlack(id) {
  return request({
    url: '/parking/blackList/remove/'+id,
    method: 'get',
  })
}
