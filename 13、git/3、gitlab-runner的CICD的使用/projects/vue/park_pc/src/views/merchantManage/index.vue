<!-- 商户管理 -->
<template>
  <div class="fix-list">
    <div class="tabBox">
      <el-tabs v-model="activeName" type="card">
        <el-tab-pane label="商户充值优惠管理" name="1">
          <div slot="label" class="tabSkew">
            <div class="tabFirstImg"></div>
            <div class="tabName">商户充值优惠管理</div>
            <div class="tabRImg" v-if="+activeName != 2"></div>
          </div>
          <discount />
        </el-tab-pane>
        <el-tab-pane label="小时优惠券购买管理" name="2">
          <div slot="label" class="tabSkew">
            <div class="tabLeftImg"></div>
            <div class="tabName">小时优惠券购买管理</div>
            <div class="tabRImg" v-if="+activeName != 3"></div>
          </div>
          <hourDiscount />
        </el-tab-pane>
        <el-tab-pane label="商户信息管理" name="3">
          <div slot="label" class="tabSkew">
            <div class="tabLeftImg"></div>
            <div class="tabName">商户信息管理</div>
            <div class="tabRImg" v-if="+activeName != 4"></div>
          </div>
          <merchant-info />
        </el-tab-pane>
        <el-tab-pane label="商户流水管理" name="4">
          <div slot="label" class="tabSkew">
            <div class="tabLeftImg"></div>
            <div class="tabName">商户流水管理</div>
            <div class="tabRImg" v-if="+activeName != 5"></div>
          </div>
          <statements />
        </el-tab-pane>
        <el-tab-pane label="商户优惠券管理" name="5">
          <div slot="label" class="tabSkew">
            <div class="tabLeftImg"></div>
            <div class="tabName">商户优惠券管理</div>
            <div class="tabLastImg"></div>
          </div>
          <discount-detail />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import discount from './components/discount.vue';
import hourDiscount from './components/hourDiscount.vue';
import merchantInfo from './components/merchantInfo.vue';
import statements from './components/statements.vue';
import discountDetail from './components/discountDetail.vue';

export default {
  components: {discount, hourDiscount, merchantInfo, statements, discountDetail},
  data() {
    return {
      activeName: '1',
    }
  }
}
</script>

<style scoped lang='scss'>
@import '@/assets/styles/tab.scss';
</style>