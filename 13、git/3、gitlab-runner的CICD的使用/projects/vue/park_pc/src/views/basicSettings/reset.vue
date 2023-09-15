<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-15 09:30:59
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-15 16:39:37
 * @文件相对于项目的路径: \park_pc\src\views\basicSettings\reset.vue
-->
<template>
  <div class="reset tabBox">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="停车场重置" name="1">
        <div slot="label" class="tabSkew">
          <div class="tabFirstImg"></div>
          <div class="tabName">停车场重置</div>
          <div class="tabRImg" v-if="+activeName != 2"></div>
        </div>
        <div class="con">
          <el-button type="primary" size="mini" @click="showBox('停车场重置')">重置停车场</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="订单重置" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">订单重置</div>
          <div class="tabLastImg"></div>
        </div>
        <div class="con">
          <el-button type="primary" class="resetOrder" size="mini" @click="showBox('订单重置')">重置订单</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-dialog class="showDialog" :title="title" :visible.sync="show" width="483px" append-to-body>
      <div class="showCon">
        请联系系统管理员或技术人员！
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  components: {},
  props: {},
  data () {
    return {
      activeName: '1',
      show: false,
      title: ''
    }
  },
  computed: {},
  watch: {},
  methods: {
    showBox (title) {
      this.title = title
      this.show = true
    }
  },
  created () {

  },
  mounted () {

  },
}
</script>
<style scoped lang='scss'>
@import '@/assets/styles/tab.scss';

.reset {
  .con {
    margin: 16px;
    height: 100%;
  }

  .resetPark {
    background-color: #49C8EA;
    border-color: #49C8EA;
  }

  .resetOrder {
    background-color: #1b88be;
    border-color: #1b88be;
  }

}

.showCon {
  font-size: 16px;
}
</style>