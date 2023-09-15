import request from "@/utils/merchantRequest";

// 商户信息列表
export function listMerchant(query) {
  return request({
    url: '/merchant/merchantQuery/list',
    method: 'get',
    params: query
  })
}

// 商户详情
export function merchantDetail(id) {
  return request({
    url: "/merchant/merchantQuery/" + id,
    method: "get",
  });
}

// 新增商户
export function addMerchant(data) {
  return request({
    url: "/system/user",
    method: "post",
    data
  });
}

// 充值优惠列表
export function listDiscount(query) {
  return request({
    url: '/merchant/set/list',
    method: 'get',
    params: query
  })
}

// 更新充值优惠列表
export function addDiscount(data) {
  return request({
    url: "/merchant/set/add",
    method: "post",
    data: data,
  });
}

// 小时优惠列表
export function listHourDiscount(query) {
  return request({
    url: '/merchant/amount/list',
    method: 'get',
    params: query
  })
}

// 更新小时优惠列表
export function addHourDiscount(data) {
  return request({
    url: "/merchant/amount/add",
    method: "post",
    data: data,
  });
}

// 优惠券管理列表
export function listMerchantDiscount(query) {
  return request({
    url: '/merchant/detail/list',
    method: 'get',
    params: query
  })
}

// 流水列表
export function listStatements(query) {
  return request({
    url: "/merchant/records/list",
    method: "get",
    params: query,
  });
}
