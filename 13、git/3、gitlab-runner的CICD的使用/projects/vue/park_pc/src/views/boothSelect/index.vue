<template>
  <el-container class="container">
    <el-header class="header" style="height: 85px;">
      <div style="display: flex;align-items: center">
        <img width="30px" src="@/assets/logo/logo.png" alt="" srcset="">
        <span class="title" style="width: 500px;">智慧停车管理平台--岗亭管理</span>
      </div>
      <span>您好！
        <el-dropdown trigger="hover">
          <el-button type="text" class="text-button">{{ nickName }}</el-button>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        | <el-button type="text" @click.native="logout" class="text-button">退出</el-button></span>
    </el-header>
    <div style="padding: 0 14px;"><el-divider class="divider" /></div>
    <el-main>
      <div v-if="isRow" class="content-row">
        <div style="display: flex;flex-direction: column;flex: 1;height: 100%;">
          <div class="row-content">
            <div class="row-title">入 口</div>
            <el-checkbox-group v-model="selectedEntrance" class="auto-scroll">
              <el-checkbox v-for="item in entranceOptions" :key="item.id" :label="item" border>{{ item.passageName
              }}</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="row-content">
            <div class="row-title">出 口</div>
            <el-checkbox-group v-model="selectedExit" class="auto-scroll">
              <el-checkbox v-for="item in exitOptions" :key="item.id" :label="item" border>{{ item.passageName
              }}</el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <el-divider direction="vertical" />
        <el-button size="large" type="primary" :disabled="disabled" round class="row-content-button" @click="enter">
          <span>进入</span><span>岗亭</span>
        </el-button>
      </div>
      <div v-else class="content-col">
        <div class="column-content">
          <span class="top-text">入 口</span>
          <el-checkbox-group v-model="selectedEntrance" class="auto-scroll">
            <el-checkbox v-for="item in entranceOptions" :key="item.id" :label="item" border>{{ item.passageName
            }}</el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="column-content">
          <span class="top-text">出 口</span>
          <el-checkbox-group v-model="selectedExit" class="auto-scroll">
            <el-checkbox v-for="item in exitOptions" :key="item.id" :label="item" border>{{ item.passageName
            }}</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </el-main>
    <el-footer v-if="!isRow" class="footer" style="height: 196px;">
      <el-button size="large" type="primary" round class="enter-button" :disabled="disabled"
        @click="enter">进入岗亭</el-button>
    </el-footer>
  </el-container>
</template>

<script>
import { listBooth, sentryBoxAdd } from './api'
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      entranceOptions: [],
      exitOptions: [],
      selectedEntrance: [],
      selectedExit: [],
      loading: false
    }
  },
  computed: {
    ...mapGetters([
      'nickName',
    ]),
    /** 是否是行模式 */
    isRow() {
      return this.entranceOptions.length > 4 || this.exitOptions.length > 4
    },
    /** 不选择岗亭禁用 “进入岗亭” button */
    disabled() {
      const list = [...this.selectedEntrance, ...this.selectedExit]
      return list.length == 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true;
      listBooth().then(response => {
        this.exitOptions = response.rows.filter(item => item.passageFlag == 2);
        this.entranceOptions = response.rows.filter(item => item.passageFlag == 1);
        this.loading = false;
      });
    },
    enter() {
      // console.log(this.selectedEntrance)
      // console.log(this.selectedExit)

      let arr = [...this.selectedEntrance, ...this.selectedExit]
      let ids = []
      arr.forEach(e => {
        ids.push(e.passageNo)
      })
      console.log(ids.join(','))
      // return;
      sentryBoxAdd(ids).then(res => {
        sessionStorage.setItem('entrances', JSON.stringify([...this.selectedEntrance]))
        sessionStorage.setItem('exits', JSON.stringify([...this.selectedExit]))
        this.$router.push('/boothMonitor')
      })
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => { });
    }
  }
}
</script>

<style scoped lang="scss">
@import "./assets/style.scss";

.container {
  background: url(./assets/bg.png) center no-repeat;
  background-size: cover;
}

.text-button {
  font-size: 16px;
  color: #fff;
}

.auto-scroll {
  height: 100%;
  overflow-y: auto;
}
</style>