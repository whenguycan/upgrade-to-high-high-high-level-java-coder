<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-20 15:31:33
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 15:53:27
 * @文件相对于项目的路径: /park_pc/src/views/home/components/analysesIX.vue
-->
<template>
  <div class="analyseSix">
    <BarChart :echartsD="lined" :barColor="barColor"></BarChart>
  </div>
</template>

<script>
import { getTimeShareUsageDayFact } from '@/views/home/api/index.js'

import BarChart from '@/views/components/echarts/bar.vue'
export default {
  name: 'AnalyseFive',
  components: {
    BarChart
  },
  props: {},
  data() {
    return {
      barColor: [['#FFD25E', '#F35817']],
      lined: {}
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getTimeShareUsageDayFact()
  },
  methods: {
    async getTimeShareUsageDayFact() {
      const res = await getTimeShareUsageDayFact()
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
      if (res) {
        this.lined = {
          echartsD: [res.echartsD.value, res.echartsD.value1],
          echartsLegendName: ['昨日分时利用'],
          echartsLegend: res.echartsLegend,
          echartsName: res.echartsName
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.analyseSix {
  height: 100%;
}
</style>
