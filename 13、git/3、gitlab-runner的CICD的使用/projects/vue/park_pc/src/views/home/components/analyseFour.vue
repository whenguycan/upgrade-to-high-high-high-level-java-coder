<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-20 15:16:32
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 15:53:33
 * @文件相对于项目的路径: /park_pc/src/views/home/components/analyseFour.vue
-->
<template>
  <div class="analyseFour">
    <LineD :echartsD="lined"></LineD>
  </div>
</template>

<script>
import { getOrderSituationDayFact } from '@/views/home/api/index.js'

import LineD from '@/views/components/echarts/line.vue'
export default {
  name: 'AnalyseFour',
  components: {
    LineD
  },
  props: {},
  data() {
    return {
      lined: {}
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getOrderSituationDayFact()
  },
  methods: {
    async getOrderSituationDayFact() {
      const res = await getOrderSituationDayFact()
      if (res && +res.code === 200 && res.data) {
        const v = res.data
        this.getList(v)
      }
    },
    getList(v) {
      // const v = [
      //   { name: '03-02', value: 50 },
      //   { name: '03-03', value: 30 },
      //   { name: '03-04', value: 40 },
      //   { name: '03-05', value: 60 },
      //   { name: '03-06', value: 20 },
      //   { name: '03-07', value: 40 }
      // ]
      const res = this.$handleEchartsData(v)
      if (res && v) {
        this.lined = {
          ...res,
          echartsLegendName: ['订单量']
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类

.analyseFour {
  height: 100%;
}
</style>
