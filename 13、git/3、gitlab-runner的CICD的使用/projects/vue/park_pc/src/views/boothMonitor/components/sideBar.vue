<template>
  <div :class="{'has-logo':showLogo}" :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
      <logo v-if="showLogo" :collapse="isCollapse" />
      <el-scrollbar v-if="!isCollapse" :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
        <div class="sidebar-content">
          <div class="cash-text">
            <span class="text-name"><img src="../assets/cash.png" />合计</span>{{ total }}<span>元</span>
          </div>
          <div class="cash-text"><span class="text-name">线上</span>{{ online }}<span>元</span></div>
          <div class="cash-text"><span class="text-name">线下</span>{{ offline }}<span>元</span></div>
          <div class="cash-text">
            <span class="text-name"><img src="../assets/parking-spaces.png" />空余</span>{{ idle }}<span>车位</span>
          </div>
          <el-divider />
          <div class="cash-text">
            <span class="text-name"><img src="../assets/device-status.png" />设备状态</span>
          </div>
        </div>
      </el-scrollbar>
      <div class="CitydriverRafiki" v-if="!isCollapse">
        <img src="../../../assets/images/CitydriverRafiki.png" alt="">
      </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "@/layout/components/Sidebar/Logo";
import variables from "@/assets/styles/variables.scss";

export default {
  components: { Logo },
  data() {
    return {
      total: 36122,
      online: 36000,
      offline: 122,
      idle: 1163
    }
  },
  computed: {
      ...mapState(["settings"]),
      ...mapGetters(["sidebarRouters", "sidebar"]),
      activeMenu() {
          const route = this.$route;
          const { meta, path } = route;
          // if set path, the sidebar will highlight the path you set
          if (meta.activeMenu) {
              return meta.activeMenu;
          }
          return path;
      },
      showLogo() {
          return this.$store.state.settings.sidebarLogo;
      },
      variables() {
          return variables;
      },
      isCollapse() {
          return !this.sidebar.opened;
      }
  }
};
</script>
<style lang="scss" scoped>
.CitydriverRafiki {
height: 177px;
width: 200px;
&>img {
  height: 177px;
  width: 200px;
}

// position: fixed;
// bottom: 0;
}

.sidebar-content {
  padding: 21px 7px 20px 12px;
  color: rgba(255, 255, 255, 1);
  font-size: 16px;
  .cash-text {
    text-align: left;
    white-space: nowrap;
    line-height: 22px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 9px;
    &:nth-child(3) {
      margin-bottom: 21px;
    }

    .text-name {
      width: 54px;
      display: flex;
      justify-content: flex-end;
      align-items: center;
      img {
        margin-right: 4px;
      }
      &:nth-last-child(1) {
        width: 86px;
      }
    }
  }
}
</style>

