/*
 * @Description: 车辆管理-固定车
 * @Author: Adela
 * @Date: 2023-02-27 09:37:14
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 13:35:10
 * @文件相对于项目的路径: /park_pc/src/views/carManage/api/_regularList.js
 */
import request from '@/utils/request'

const regularCarCategoryBaseUrl = '/parking/settingRegularCarCategory' // 固定车类型 base
const _regularCarCategoryUrl = {
  regularCarCategoryList: `${regularCarCategoryBaseUrl}/list`, // 固定车list 类型
  addRegularCarCategory: `${regularCarCategoryBaseUrl}/add`, // 新增固定车 类型
  editRegularCarCategory: `${regularCarCategoryBaseUrl}/edit`, // 编辑固定车 类型
  removeRegularCarCategory: `${regularCarCategoryBaseUrl}/remove/`, // 删除固定车 类型
  getRegularCarCategoryDetail: `${regularCarCategoryBaseUrl}/detail/`, // 获取固定车 类型 详情
}

const regularCarBaseUrl = '/parking/regularCar' // 固定车
const _regularCarUrl = {
  regularCarList: `${regularCarBaseUrl}/list`, // 固定车list 类型
  addRegularCar: `${regularCarBaseUrl}/add`, // 新增固定车 类型
  editRegularCar: `${regularCarBaseUrl}/edit`, // 编辑固定车 类型
  removeRegularCar: `${regularCarBaseUrl}/remove/`, // 删除固定车 类型
  getRegularCarDetail: `${regularCarBaseUrl}/detail/`, // 获取固定车 类型 详情

  importTemplateRegularCar: `${regularCarBaseUrl}/importTemplate`, //列表导入模板
  importDataRegularCar: `${regularCarBaseUrl}/importData`, // 列表导入
  exportListRegularCar: `${regularCarBaseUrl}/export`, // 列表导出
}

// 固定车list 类型
export function regularCarCategoryList(query) {
  return request({
    url: _regularCarCategoryUrl.regularCarCategoryList,
    method: 'get',
    params: query
  })
}

// 新增固定车 类型
export function addRegularCarCategory(data) {
  return request({
    url: _regularCarCategoryUrl.addRegularCarCategory,
    method: 'post',
    data: data
  })
}

// 编辑固定车 类型
export function editRegularCarCategory(data) {
  return request({
    url: _regularCarCategoryUrl.editRegularCarCategory,
    method: 'post',
    data: data
  })
}

// 删除固定车 类型
export function removeRegularCarCategory(id) {
  return request({
    url: _regularCarCategoryUrl.removeRegularCarCategory + id,
    method: 'get'
  })
}

// 获取固定车 类型 详情
export function getRegularCarCategoryDetail(id) {
  return request({
    url: _regularCarCategoryUrl.getRegularCarCategoryDetail + id,
    method: 'get'
  })
}


// 固定车 list
export function regularCarList(query) {
  return request({
    url: _regularCarUrl.regularCarList,
    method: 'get',
    params: query
  })
}

// 新增固定车
export function addRegularCar(data) {
  return request({
    url: _regularCarUrl.addRegularCar,
    method: 'post',
    data: data
  })
}

// 编辑固定车
export function editRegularCar(data) {
  return request({
    url: _regularCarUrl.editRegularCar,
    method: 'post',
    data: data
  })
}

// 删除固定车
export function removeRegularCar(id) {
  return request({
    url: _regularCarUrl.removeRegularCar + id,
    method: 'get'
  })
}

// 获取固定车 类型 详情
export function getRegularCarDetail(id) {
  return request({
    url: _regularCarCategoryUrl.getRegularCarDetail + id,
    method: 'get'
  })
}
