/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-02 16:28:50
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-07 09:36:17
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/constant.js
 */

// 访客码管理列表 数据
const VISITOR_CODE_MANAGE = {
  SEARCH_TITLE: '查询条件',
  LIST_TITLE: '访客码管理列表',
  EXPORT_TEMPLATE_TITLE: '访客码模版',
  EXPORT_TEMPLATE_URL: '/parking/BVisitorCodeManage/importTemplate',
  IMPORT_DATA_TITLE: '访客码导入',
  IMPORT_DATA_URL: '/parking/BVisitorCodeManage/importData',
  EXPORT_DATA_TITLE: '访客码管理列表',
  EXPORT_DATA_URL: '/parking/BVisitorCodeManage/export',
  DIALOG_ADD_TITLE: '新增访客码',
  DIALOG_EDIT_TITLE: '编辑访客码',
  DIALOG_CHECK_TITLE: '查看访客码',
}

// 访客人员审核审核管理列表 数据
const VISITOR_APPLY_CHECK__MANAGE = {
  SEARCH_TITLE: '查询条件',
  LIST_TITLE: '访客审核列表',
  EXPORT_TEMPLATE_TITLE: '访客模版',
  EXPORT_TEMPLATE_URL: '/parking/selfPay/importTemplate',
  IMPORT_DATA_TITLE: '访客导入',
  IMPORT_DATA_URL: '/parking/selfPay/importData',
  EXPORT_DATA_TITLE: '访客管理列表',
  EXPORT_DATA_URL: '/parking/selfPay/export',
  DIALOG_ADD_TITLE: '新增访客',
  DIALOG_EDIT_TITLE: '编辑访客',
}


export {
  VISITOR_CODE_MANAGE,
  VISITOR_APPLY_CHECK__MANAGE
}
