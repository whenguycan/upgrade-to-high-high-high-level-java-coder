import request from '@/utils/request'

//获取消息模板列表
export function getNoticeList(query) {
  return request({
    url: 'parking/notifyTemplate/list',
    method: 'get',
    params: query
  })
}

//编辑消息模板
export function editNotice(data) {
  return request({
    url: 'parking/notifyTemplate/edit',
    method: 'post',
    data: data
  })
}

// 获取消息列表
export function getNoticeRecordList(query) {
  return request({
    url: 'parking/notificationRecord/list',
    method: 'get',
    params: query
  })
}