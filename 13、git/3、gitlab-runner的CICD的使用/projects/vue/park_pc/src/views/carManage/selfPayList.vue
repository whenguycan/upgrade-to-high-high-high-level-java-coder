<!--
 * @Description: 自主缴费设置 加 审核
 * @Author: Adela
 * @Date: 2023-03-01 15:07:01
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-01 15:20:06
 * @文件相对于项目的路径: /park_pc/src/views/carManage/selfPayList.vue
-->
<template>
  <div class="selfPayList tabBox">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="自主续费方案设置" name="1">
        <div slot="label" class="tabSkew">
          <div class="tabFirstImg"></div>
          <div class="tabName">自主续费方案设置</div>
          <div class="tabRImg" v-if="+activeName != 2"></div>
        </div>
        <selfPayScheme v-if="+activeName === 1"></selfPayScheme>
      </el-tab-pane>
      <el-tab-pane label="自主缴费审核管理" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">自主缴费审核管理</div>
          <div class="tabLastImg"></div>
        </div>
        <selfPayExamine v-if="+activeName === 2"></selfPayExamine>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import selfPayScheme from "./components/selfPayList/selfPayScheme.vue";
import selfPayExamine from "./components/selfPayList/selfPayExamine.vue";
export default {
  name: "SelfPayList",
  components: {
    selfPayScheme,
    selfPayExamine,
  },
  props: {},
  data() {
    return {
      activeName: "1",
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "@/assets/styles/tab.scss";
.selfPayList {
}
</style>
