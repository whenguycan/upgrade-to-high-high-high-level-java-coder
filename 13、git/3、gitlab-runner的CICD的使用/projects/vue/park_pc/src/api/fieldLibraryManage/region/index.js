import request from "@/utils/request";

// 列表
export function listField(query) {
  return request({
    url: "/bField/list",
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


export function fieldComboboxList() {
  return request({
    url: "/bField/fieldComboboxList",
    method: "get"
  });
}