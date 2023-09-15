/*
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-10 09:32:46
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 09:32:54
 * @文件相对于项目的路径: \park_pc\src\views\statistic\api\deductionList.js
 */

import request from '@/utils/request'


//获取抵扣记录列表
export function getDeductionList(query) {
  return request({
    url: 'parking/deductionorder/list',
    method: 'get',
    params: query
  })
}