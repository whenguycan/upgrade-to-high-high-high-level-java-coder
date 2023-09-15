/*
 * @Description:
 * @Author: cuijing
 * @Date: 2022-06-20 14:24:09
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-28 15:31:57
 * @文件相对于项目的路径: \park_pc\src\filter\filter.js
 */

import moment from "moment";
const formatNumber = (n) => {
  var b = parseInt(n).toString();
  var len = b.length;
  if (len <= 3) {
    return b;
  }
  var r = len % 3;
  return r > 0 ? b.slice(0, r) + "," + b.slice(r, len).match(/\d{3}/g).join(",") : b.slice(r, len).match(
    /\d{3}/g).join(",");
}

const dateFormat = (value, type = 'YYYY-MM-DD') => {
  if (value.constructor === String || value.constructor === Date) {
    return moment(value).format(type)
  } else {
    return value
  }
}

const chatTime = (value) => {
  if (value) {
    return moment(value).format('YYYY-MM-DD HH:mm:ss')
  } else {
    return ''
  }
}

const dateFormatYY = (value) => {
  return value.substring(0,4)+'-'+value.substring(4,6)+'-'+value.substring(6,8)+' '+value.substring(8,10)+':'+value.substring(10,12)+':'+value.substring(12,14)
}

export {
  formatNumber,
  dateFormatYY,
  dateFormat,
  chatTime
}
