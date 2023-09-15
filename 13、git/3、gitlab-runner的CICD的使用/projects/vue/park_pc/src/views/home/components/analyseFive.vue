<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-20 15:31:33
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 15:53:07
 * @文件相对于项目的路径: /park_pc/src/views/home/components/analyseFive.vue
-->
<template>
  <div class="analyseFive">
    <BarChart :echartsD="lined" :isShowLegend="true"></BarChart>
  </div>
</template>

<script>
import { getEntryExitAnalysisDayFact } from '@/views/home/api/index.js'

import BarChart from '@/views/components/echarts/bar.vue'
export default {
  name: 'AnalyseFive',
  components: {
    BarChart
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
    this.getEntryExitAnalysisDayFact()
  },
  methods: {
    async getEntryExitAnalysisDayFact() {
      const res = await getEntryExitAnalysisDayFact()
      if (res && +res.code === 200 && res.data) {
        if (res.data && res.data.入场车辆 && res.data.出场车辆) {
          const v = res.data.入场车辆.map((item, index) => {
            return {
              ...item,
              value1: res.data.出场车辆[index].value
            }
          })
          this.getList(v)
        }
      }
    },
    getList(v) {
      // const v = [
      //   { name: '03-02', value: 50, value1: 50 },
      //   { name: '03-03', value: 30, value1: 50 },
      //   { name: '03-04', value: 40, value1: 50 },
      //   { name: '03-05', value: 60, value1: 50 },
      //   { name: '03-06', value: 20, value1: 50 },
      //   { name: '03-07', value: 40, value1: 50 }
      // ]
      const res = this.$handleEchartsData(v)
      if (res) {
        this.lined = {
          echartsD: [res.echartsD.value, res.echartsD.value1],
          echartsLegendName: ['入场车辆', '出场车辆'],
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
.analyseFive {
  height: 100%;
}
</style>
