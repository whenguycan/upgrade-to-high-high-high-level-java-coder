<!--
 * @Description: 
 * @Author: Adela
 * @Date: 2023-02-22 10:06:51
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-14 14:37:14
 * @文件相对于项目的路径: \park_pc\src\views\login.vue
-->
<template>
  <div class="login">
    <div class="loginBox">
      <div class="leftLoginBox">
        <div class="loginTop">
          <img src="@/assets/logo/logo.png" alt="" srcset="">
          <span>智慧停车管理平台</span>
        </div>
        <div class="imgDiv">
          <img class="leftImg" src="@/assets/images/login_left.png" alt="">
        </div>
      </div>
      <div class="rightLoginBox">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <h3 class="title">登录</h3>
          <h6 class="littleTitle">欢迎登录智慧停车管理平台</h6>
          <el-form-item prop="username">
            <div class="formTit">
              <img src="@/assets/images/ren.png" alt="">
              <span>账号</span>
            </div>
            <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
              <!-- <img src="@/assets/images/user.png" slot="prefix" class="el-input__icon input-icon user" /> -->
              <!-- <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" /> -->
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <div class="formTit">
              <img src="@/assets/images/mi.png" alt="">
              <span>密码</span>
            </div>
            <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
              @keyup.enter.native="handleLogin">
              <!-- <img src="@/assets/images/password.png" slot="prefix" class="el-input__icon input-icon password" /> -->
              <!-- <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" /> -->
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="formTit">
              <img src="@/assets/images/yan.png" alt="">
              <span>验证码</span>
            </div>
            <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
              @keyup.enter.native="handleLogin">
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCode" class="login-code-img" />
            </div>
          </el-form-item>
          <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
          <el-form-item style="width:100%;">
            <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
              @click.native.prevent="useVerify">
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
            <div style="float: right;" v-if="register">
              <router-link class="link-type" :to="'/register'">立即注册</router-link>
            </div>
        </el-form-item>
        <!--  滑动/点选认证  -->
        <Verify @success="success" mode="pop" captchaType="blockPuzzle" :imgSize="{ width: '370px', height: '200px' }"
            ref="verify"></Verify>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
//引入组件
import Verify from "./../components/verifition/Verify"
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  components: {
    Verify
  },
  data () {
    return {
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
        captchaVerification: "",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created () {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode () {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie () {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { })
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    },
    success (params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      this.loginForm.verification = params.captchaVerification
      // 调用登录
      this.handleLogin()
    },
    useVerify () {
      this.$refs.verify.show()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  flex-direction: column;
  // justify-content: center;
  align-items: center;
  height: 100%;
  background: linear-gradient(180deg, #509EE0 0%, #2D5CC6 100%);
  // background-image: url("../assets/images/login-background.png");
  // background-size: cover;
}

.loginTop {
  width: 100%;
  height: 85px;
  margin: 0 30px;
  // border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  padding: 0 0 0 60px;

  img {
    width: 42px;
    height: 47px;
  }

  span {
    margin-left: 20px;
    font-size: 22px;
    font-family: PingFang SC-Semibold, PingFang SC;
    font-weight: 600;
    color: #E3E9FF;
    line-height: 21px;
    letter-spacing: 2px;
  }
}

.loginBox {
  flex: 1;
  width: 100%;
  display: flex;

  .leftLoginBox {
    flex: 1;
    display: flex;
    flex-direction: column;

    .imgDiv {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;

      .leftImg {
        width: 54.8%;
        // height:463px;
      }
    }

  }

  .rightLoginBox {
    width: 530px;
    background: linear-gradient(180deg, #4F9CE0 0%, #3871E5 100%);
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;

  }
}

.title {
  // margin: 0px auto 30px auto;
  // text-align: center;
  color: #ffffff;
  font-size: 30px;
  line-height: 40px;
  font-family: Inter-Medium, Inter;
  margin: 0;
  padding: 0;
}

.littleTitle {
  font-size: 14px;
  font-family: Inter-Medium, Inter;
  font-weight: 500;
  color: #FFFFFF;
  margin: 10px 0 50px;
  padding: 0;
}

.formTit {
  display: flex;
  align-items: center;

  img {
    width: 23px;
    height: 23px;
  }

  span {
    margin-left: 20px;
    font-size: 16px;
    font-family: Inter-Medium, Inter;
    font-weight: 500;
    color: rgba(255, 255, 255, 0.5);
    line-height: 19px;
  }
}

input:-webkit-autofill {
  // background: rgba(0, 65, 151, 0.39) !important;
  // box-shadow: 0px 0px 6px 0px rgba(8, 30, 152, 0.27) !important;
  // border-radius: 41px !important;
  // border: none !important;
  // font-size: 18px !important;
  // font-family: PingFangSC-Regular, PingFang SC !important;
  // font-weight: 400 !important;
  // color: rgba(255, 255, 255, 0.6) !important;
  // line-height: 59px !important;
  -webkit-box-shadow: 0 0 0px 1000px transparent inset !important;
  background-color: transparent !important; //设置input框记住密码背景颜色
  background-image: none;
  transition: background-color 50000s ease-in-out 0s;
  -webkit-text-fill-color: rgba(255, 255, 255, 0.6) !important; //设置浏览器默认密码字体颜色
}

.login-form {
  border-radius: 6px;
  // background: #ffffff;
  width: 400px;

  // padding: 25px 25px 5px 25px;
  .el-input {
    height: 49px;

    // input {
    //   height: 59px;
    //   background: rgba(0, 65, 151, 0.39);
    //   box-shadow: 0px 0px 6px 0px rgba(8, 30, 152, 0.27);
    //   border-radius: 41px;
    //   border: none;
    //   font-size: 18px;
    //   font-family: PingFangSC-Regular, PingFang SC;
    //   font-weight: 400;
    //   color: rgba(255, 255, 255, 0.6);
    //   line-height: 59px;
    //   padding: 0 0 0 80px;
    //   caret-color: #fff;
    // }
    input {
      height: 49px;
      background: transparent;
      border-top: none;
      border-left: none;
      border-right: none;
      border-top: none;
      border-radius: 0px;
      border-bottom: 1px solid #A3C6F0;
      font-size: 18px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.5);
      line-height: 39px;
      caret-color: #fff;
    }


  }

  .el-form-item.is-error .el-input__inner {
    border-color: #A3C6F0;
  }

  .el-input__inner::placeholder {
    color: rgba(255, 255, 255, 0.3);
  }

  .user {
    width: 27px;
    height: 31px;
    margin: 14px 0 0 22px;
  }

  .password {
    width: 35px;
    height: 31px;
    margin: 14px 0 0 20px;
  }

  .verifyCode {
    width: 35px;
    height: 31px;
    margin: 14px 0 0 16px;
  }

  .el-form-item {
    margin-bottom: 20px;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner {
    background-color: #80C0FF;
    border-color: #80C0FF;
    width: 16px;
    height: 16px;
  }

  .el-checkbox__inner {
    width: 16px;
    height: 16px;
  }

  .el-checkbox__inner::after {
    height: 8px;
    left: 5px;
  }

  .el-checkbox__label {
    color: #ffffff;
    font-size: 15px;
  }

  .el-form-item__error {
    font-size: 15px;
  }

  .el-checkbox__input.is-checked+.el-checkbox__label {
    color: #ffffff;
    font-size: 15px;
  }

  // .el-button--primary {
  //   background-color: transparent;
  //   height: 82px;
  //   box-shadow: 0px 0px 6px 0px #081E98;
  //   border-radius: 41px;
  //   border: 3px solid #FFFFFF;
  //   font-size: 28px;
  //   font-family: PingFangSC-Semibold, PingFang SC;
  //   font-weight: 500;
  //   color: #FFFFFF;
  //   line-height: 42px;
  // }
  .el-button--primary {
    background-color: #80C0FF;
    height: 52px;
    // box-shadow: 0px 0px 6px 0px #081E98;
    border-radius: 5px;
    border: 3px solid #80C0FF;
    font-size: 16px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 500;
    color: #FFFFFF;
    line-height: 22px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 49px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 49px;
}
</style>
