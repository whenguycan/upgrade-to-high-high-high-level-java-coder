/*
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-15 17:36:59
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-15 17:37:30
 * @文件相对于项目的路径: \park_pc\src\views\statistic\api\dealList.js
 */
import request from '@/utils/request'

//获取车场收费记录
export function getParkFeeList(query) {
  return request({
    url: '/parking/parkorderrecords/list',
    method: 'get',
    params: query
  })
}