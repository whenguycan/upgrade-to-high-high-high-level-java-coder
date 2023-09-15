import Cookies from "js-cookie";

const TokenKey = "Admin-Token";
const RefreshTokenKey = "Refresh-Admin-Token";
const reLogin = "reLogin";

export function getToken() {
  return Cookies.get(TokenKey);
}

export function getRefreshToken() {
  return Cookies.get(RefreshTokenKey);
}

export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function setRefreshToken(refreshToken) {
  return Cookies.set(RefreshTokenKey, refreshToken);
}

export function setReLoginToken(token) {
  return Cookies.set(reLogin, token);
}

export function getReLoginToken() {
  return Cookies.get(reLogin);
}

export function removeToken() {
  Cookies.remove(RefreshTokenKey);
  Cookies.remove(reLogin);
  return Cookies.remove(TokenKey);
}
