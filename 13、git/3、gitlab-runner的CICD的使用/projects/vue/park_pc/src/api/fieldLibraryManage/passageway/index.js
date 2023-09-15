import request from "@/utils/request";

// 列表
export function listPassage(query) {
  return request({
    url: "/bPassage/list",
    method: "get",
    params: query,
  });
}

// 详细
export function getPassage(deptId) {
  return request({
    url: "/bPassage/" + deptId,
    method: "get",
  });
}

// 新增
export function addPassage(data) {
  return request({
    url: "/bPassage/add",
    method: "post",
    data: data,
  });
}

// 修改部门
export function updatePassage(data) {
  return request({
    url: "/bPassage/edit",
    method: "put",
    data: data,
  });
}

// 删除部门
export function delPassage(query) {
  return request({
    url: "/bPassage/delete",
    method: "delete",
    params: query,
  });
}

// 删除部门
export function getRegularCarCategorys(query) {
  return request({
    url: "/parking/settingRegularCarCategory/list",
    method: "get",
    params: query,
  });
}

