<!--
 * @Description: 今日收入分析
 * @Author: Adela
 * @Date: 2023-03-09 14:54:28
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 16:40:37
 * @文件相对于项目的路径: /park_pc/src/views/home/components/todayIncome.vue
-->
<template>
  <div class="todayIncome">
    <normal-pie :echartData="payMethodDayFact" :legendTop="'15%'"></normal-pie>
  </div>
</template>

<script>
import { getPayMethodDayFact } from '@/views/home/api/index.js'
import normalPie from '@/views/components/echarts/normalPie'
export default {
  name: 'TodayIncome',
  components: {
    normalPie
  },
  props: {},
  data() {
    return {
      payMethodDayFact: [],
      pieData: [
        { name: '临停缴费', value: 4.2 },
        { name: '月租会员', value: 2.4 },
        { name: '商户储值', value: 0.8 }
      ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getPayMethodDayFact()
  },
  methods: {
    async getPayMethodDayFact() {
      const res = await getPayMethodDayFact()
      if (res && +res.code === 200 && res.data) {
        this.payMethodDayFact = res.data
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.todayIncome {
  height: 100%;
  // background: #f6f7f9;
}
</style>
