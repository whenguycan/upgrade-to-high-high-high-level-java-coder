import request from "@/utils/request";

// 列表
export function listRecords(query) {
  return request({
    url: "/parking/records/list",
    method: "get",
    params: query,
  });
}

// 详细
export function getRecords(RecordsId) {
  return request({
    url: "/parking/records/" + RecordsId,
    method: "get",
  });
}

// 新增
export function addRecords(data) {
  return request({
    url: "/parking/records",
    method: "post",
    data: data,
  });
}

// 修改
export function updateRecords(data) {
  return request({
    url: "/parking/records",
    method: "put",
    data: data,
  });
}

// 删除
export function delRecords(id) {
  return request({
    url: "/parking/records/"+id,
    method: "delete",
  });
}
