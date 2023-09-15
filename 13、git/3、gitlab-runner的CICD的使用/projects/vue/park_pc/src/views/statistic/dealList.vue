<!--
 * @Description: 交易记录
 * @Author: cuijing
 * @Date: 2023-02-24 09:30:53
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 09:29:15
 * @文件相对于项目的路径: \park_pc\src\views\statistic\dealList.vue
-->
<template>
  <div class="delList tabBox">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="车场收费记录" name="1">
        <div slot="label" class="tabSkew">
          <div class="tabFirstImg"></div>
          <div class="tabName">车场收费记录</div>
          <div class="tabRImg" v-if="+activeName != 2"></div>
        </div>
        <chargeList></chargeList>
      </el-tab-pane>
      <!-- <el-tab-pane label="会员充值记录" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">会员充值记录</div>
          <div class="tabRImg" v-if="activeName != 3"></div>
        </div>
        <memberChargeList></memberChargeList>
      </el-tab-pane> -->
      <el-tab-pane label="车位租用记录" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">车位租用记录</div>
          <div class="tabLastImg"></div>
        </div>
        <parkRentList></parkRentList>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import chargeList from './components/chargeList.vue';
import memberChargeList from './components/memberChargeList.vue';
import parkRentList from './components/parkRentList.vue';
export default {
  components: {
    chargeList,
    memberChargeList,
    parkRentList
  },
  props: {},
  data () {
    return {
      activeName:'1'
    }
  },
  computed: {},
  watch: {},
  methods: {
    handleClick(){
      
    }
  },
  created () {

  },
  mounted () {

  },
}
</script>
<style scoped lang='scss'>
@import '@/assets/styles/tab.scss'
</style>