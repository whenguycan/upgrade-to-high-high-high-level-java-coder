import request from "@/utils/request";

// 列表
export function listBooth(query) {
  return request({
    url: "/bPassage/list",
    method: "get",
    params: query,
  });
}
// 通道选择
export function sentryBoxAdd(data) {
  return request({
    url: "/bSentryBox/add",
    method: "post",
    data: data,
  });
}
// 详细
export function getField(FieldId) {
  return request({
    url: "/bField/" + FieldId,
    method: "get",
  });
}

// 新增
export function addField(data) {
  return request({
    url: "/bField/add",
    method: "post",
    data: data,
  });
}

// 修改部门
export function updateField(data) {
  return request({
    url: "/bField/edit",
    method: "put",
    data: data,
  });
}

// 删除部门
export function delField(query) {
  return request({
    url: "/bField/remove",
    method: "post",
    params: query,
  });
}
