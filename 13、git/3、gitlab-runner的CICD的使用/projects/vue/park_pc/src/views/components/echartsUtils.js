/*
 * @Description: echarts工具
 * @Author: Adela
 * @Date: 2023-03-09 11:40:53
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-09 11:40:53
 * @文件相对于项目的路径: /park_pc/src/views/components/echartsUtils.js
 */
// 16进制颜色转rgba
export function hexToRgb(bgColor, alpha = 1) {
  let color = bgColor.slice(1); // 去掉'#'号
  let rgba = [
    parseInt("0x" + color.slice(0, 2)),
    parseInt("0x" + color.slice(2, 4)),
    parseInt("0x" + color.slice(4, 6)),
    alpha
  ];
  return "rgba(" + rgba.toString() + ")";
}

// 接口数据进行转化成echarts标准数据
// List: [
//   { name: '新北', value3: 13, value4: 14, },
//   { name: '天宁',  value3: 23, value4: 24, },
//   { name: '武进',  value3: 33, value4: 34, },
// ]
// newList{
//   "echartsName": ["新北", "天宁", "武进", ],
//   "echartsLegend": ["value3", "value4"],
//   "echartsD": {
//     "value3": [13, 23, 33,],
//     "value4": [14, 24, 34,]
//   }
// }
export function handleEchartsData(v, type = 'name') {
  const keyv = Object.keys(v[0])
  const newObj = new Object()
  v.forEach((item) => {
    keyv.forEach(name => {
      if (newObj[name] && newObj[name].length > 0) {
        newObj[name].push(item[name])
      } else {
        newObj[name] = []
        newObj[name].push(item[name])
      }
    })
    return
  })
  const echartsName = newObj[type]
  delete newObj[type]
  const echartsD = newObj
  keyv.splice(keyv.indexOf(type), 1)
  const echartsLegend = keyv
  // console.log('echartsName', echartsName, 'echartsLegend', echartsLegend, 'echartsD', echartsD)
  return {
    echartsName,
    echartsLegend,
    echartsD,
  }
}
