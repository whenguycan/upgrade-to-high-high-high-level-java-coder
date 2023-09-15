import request from "@/utils/request";

// 列表
export function listdevice(query) {
  return request({
    url: "/parking/device/list",
    method: "get",
    params: query,
  });
}

// 详细
export function getdevice(id) {
  return request({
    url: "/parking/device/" + id,
    method: "get",
  });
}

// 新增
export function adddevice(data) {
  return request({
    url: "/parking/device/add",
    method: "post",
    data: data,
  });
}

// 修改
export function updatedevice(data) {
  return request({
    url: "/parking/device/edit",
    method: "put",
    data: data,
  });
}

// 删除
export function deldevice(id) {
  return request({
    url: "/parking/device/"+id,
    method: "delete"
  });
}
