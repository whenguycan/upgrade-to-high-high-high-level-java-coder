import request from "@/utils/request";

// 查询车辆进场记录列表
export function listMonitor(query) {
  return request({
    url: "/passageMonitor/list",
    method: "get",
    params: query,
  });
}

// 详细
export function getField(FieldId) {
  return request({
    url: "/bField/" + FieldId,
    method: "get",
  });
}

// 补录入场信息
export function addEntryInfo(data) {
  return request({
    url: "/passageMonitor",
    method: "post",
    data: data,
  });
}

// 批量修改车辆进场时间记录
export function updateEnterTime(data) {
  return request({
    url: "/passageMonitor/editEntryTime",
    method: "put",
    data: data,
  });
}

// 修改停车场空位
export function editParkingSpace(data) {
  return request({
    url: '/passageMonitor/editParkingSpace',
    method: 'post',
    data: data
  })
}

// 修改入场车牌
export function editEnterCarNo(data) {
  return request({
    url: '/passageMonitor/editEntryCarNumber',
    method: 'put',
    data: data
  })
}

// 修改出场车牌
export function editExitCarNo(data) {
  return request({
    url: '/passageMonitor/editExitCarNumber',
    method: 'put',
    data: data
  })
}
