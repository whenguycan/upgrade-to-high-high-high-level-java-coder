/*
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 16:12:38
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-13 17:41:12
 * @文件相对于项目的路径: \park_pc\src\views\statistic\api\derateList.js
 */
import request from '@/utils/request'

//获取减免记录列表
export function getDerateList(query) {
  return request({
    url: 'parking/exemptorder/list',
    method: 'get',
    params: query
  })
}