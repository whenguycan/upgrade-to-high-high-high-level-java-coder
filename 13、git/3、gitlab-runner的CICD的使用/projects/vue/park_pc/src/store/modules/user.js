import { login, logout, getInfo, reLogin } from "@/api/login";
import { getSettingcartypeList } from "@/views/basicSettings/api/index";
import { listDept } from "@/api/fieldLibraryManage/filedLibrary/index";
import { listField } from "@/api/fieldLibraryManage/region/index";
import { listPassage } from "@/api/fieldLibraryManage/passageway/index";
import {
  getToken,
  setToken,
  setRefreshToken,
  removeToken,
  getReLoginToken,
  setReLoginToken
} from "@/utils/auth";
import { getRefreshToken } from "../../utils/auth";

const user = {
  state: {
    token: getToken(),
    refreshToken: getRefreshToken(),
    reLogin: getReLoginToken(),
    name: "",
    nickName: "",
    parkInfo: {},
    avatar: "",
    roles: [],
    permissions: [],
    carList: [],
    carParkList: [],
    carAreaList: [],
    carAisleList: [],
    isAdminorManger: false,
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_REFRESH_TOKEN: (state, refreshToken) => {
      state.refreshToken = refreshToken;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName;
    },
    // 部门 -- 车场信息
    SET_PARKINFO: (state, parkInfo) => {
      state.parkInfo = parkInfo;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
      state.isAdminorManger =
        roles.includes("manager") || roles.includes("admin") ? true : false;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
    SET_CARLIST: (state, carList) => {
      state.carList = carList;
    },
    SET_CARPARKLIST: (state, carParkList) => {
      state.carParkList = carParkList;
    },
    SET_CARAREALIST: (state, carAreaList) => {
      state.carAreaList = carAreaList;
    },
    SET_CARAISLELIST: (state, carAisleList) => {
      state.carAisleList = carAisleList;
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      const verification = userInfo.verification;
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid, verification)
          .then((res) => {
            setToken(res.token);
            setRefreshToken(res.refresh_token);
            commit("SET_TOKEN", res.token);
            commit("SET_REFRESH_TOKEN", res.token);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 切换车场
    reLogin({ commit }, info) {
      return new Promise((resolve, reject) => {
        reLogin(info.deptId, info.isAdmin)
          .then((res) => {
            setReLoginToken(res.token)
            setToken(res.token);
            setRefreshToken(res.refresh_token);
            commit("SET_TOKEN", res.token);
            commit("SET_REFRESH_TOKEN", res.token);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取用户信息
    GetInfo({ dispatch, commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.user;
            const avatar =
              user.avatar == "" || user.avatar == null
                ? require("@/assets/images/profile.jpg")
                : process.env.VUE_APP_BASE_API + user.avatar;
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit("SET_ROLES", res.roles);
              commit("SET_PERMISSIONS", res.permissions);
            } else {
              commit("SET_ROLES", ["ROLE_DEFAULT"]);
            }
            commit("SET_NAME", user.userName);
            commit("SET_NICKNAME", user.nickName);
            commit("SET_PARKINFO", {
              deptId: user.dept.deptId,
              deptName: user.dept.deptName,
            });
            commit("SET_AVATAR", avatar);
            dispatch("GetCarList");
            dispatch("GetCarParkList");
            dispatch("GetCarAreaList");
            dispatch("GetCarAisleList");
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取汽车类型列表
    GetCarList({ commit, state }) {
      return new Promise((resolve, reject) => {
        let param = { status: "1" };
        getSettingcartypeList(param)
          .then((res) => {
            commit("SET_CARLIST", res.rows);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取车场列表
    GetCarParkList({ commit, state }) {
      return new Promise((resolve, reject) => {
        listDept()
          .then((res) => {
            commit("SET_CARPARKLIST", res.data);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取区域列表
    GetCarAreaList({ commit, state }) {
      return new Promise((resolve, reject) => {
        listField()
          .then((res) => {
            commit("SET_CARAREALIST", res.rows);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取通道列表
    GetCarAisleList({ commit, state }) {
      return new Promise((resolve, reject) => {
        listPassage()
          .then((res) => {
            commit("SET_CARAISLELIST", res.rows);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            commit("SET_TOKEN", "");
            commit("SET_REFRESH_TOKEN", "");
            commit("SET_ROLES", []);
            commit("SET_PERMISSIONS", []);
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", "");
        commit("SET_REFRESH_TOKEN", "");
        removeToken();
        resolve();
      });
    },
  },
};

export default user;
