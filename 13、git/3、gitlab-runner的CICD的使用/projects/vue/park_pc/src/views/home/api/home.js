/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-21 14:37:22
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 14:42:02
 * @文件相对于项目的路径: /park_pc/src/views/home/api/home.js
 */
import request from '@/utils/request'

const baseUrl = '/parking/HomePage'

// 首页顶部：/parking/HomePage/homePageHeader
export function getHomePageHeader(query) {
  return request({
    url: `${baseUrl}/homePageHeader`,
    method: 'get',
    params: query
  })
}

// 付费方式：/parking/HomePage/getPayMethodDayFact
export function getPayMethodDayFact(query) {
  return request({
    url: `${baseUrl}/getPayMethodDayFact`,
    method: 'get',
    params: query
  })
}

// 付费类型: /parking/HomePage/getPayTypeDayFact
export function getPayTypeDayFact(query) {
  return request({
    url: `${baseUrl}/getPayTypeDayFact`,
    method: 'get',
    params: query
  })
}

// 昨日时长统计 /parking/HomePage/getDurationStatisticDayFact
export function getDurationStatisticDayFact(query) {
  return request({
    url: `${baseUrl}/getDurationStatisticDayFact`,
    method: 'get',
    params: query
  })
}

// 近七日订单情况 /parking/HomePage/getOrderSituationDayFact
export function getOrderSituationDayFact(query) {
  return request({
    url: `${baseUrl}/getOrderSituationDayFact`,
    method: 'get',
    params: query
  })
}

// 昨日出入场分析 /parking/HomePage/getEntryExitAnalysisDayFact
export function getEntryExitAnalysisDayFact(query) {
  return request({
    url: `${baseUrl}/getEntryExitAnalysisDayFact`,
    method: 'get',
    params: query
  })
}

// 昨日分时利用：/parking/HomePage/getTimeShareUsageDayFact
export function getTimeShareUsageDayFact(query) {
  return request({
    url: `${baseUrl}/getTimeShareUsageDayFact`,
    method: 'get',
    params: query
  })
}
