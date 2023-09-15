import request from '@/utils/request'

//获取异常订单
export function getAbnormalList(query) {
  return request({
    url: '/parking/abnormalorder/list',
    method: 'get',
    params: query
  })
}