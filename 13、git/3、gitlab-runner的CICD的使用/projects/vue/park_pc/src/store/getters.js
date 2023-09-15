/*
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-23 14:03:37
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-23 16:51:42
 * @文件相对于项目的路径: /park_pc/src/store/getters.js
 */
const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  dict: state => state.dict.dict,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  nickName: state => state.user.nickName,
  parkInfo: state => state.user.parkInfo,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  isAdminorManger: state => state.user.isAdminorManger,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes,
  topbarRouters:state => state.permission.topbarRouters,
  defaultRoutes:state => state.permission.defaultRoutes,
  sidebarRouters:state => state.permission.sidebarRouters,
  carList: state => state.user.carList,
  carParkList: state => state.user.carParkList,
  carAreaList: state => state.user.carAreaList,
  carAisleList: state => state.user.carAisleList,
  reLogin: state => state.user.reLogin,
}
export default getters
