<!--
 * @Description: 今日停车流量分析
 * @Author: Adela
 * @Date: 2023-03-09 10:48:51
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 15:28:46
 * @文件相对于项目的路径: /park_pc/src/views/home/components/parkingFlow.vue
-->
<template>
  <div class="parkingFlow">
    <stackBar
      :dataOpt="parkingFlowD"
      :showLegend="false"
      :splitNumber="4"
      :axisMargin="9"
      :animation="true"
    ></stackBar>
  </div>
</template>

<script>
import stackBar from '@/views/components/echarts/stackBar'
export default {
  name: 'ParkingFlow',
  components: {
    stackBar
  },
  props: {},
  data() {
    return {
      resList: [
        { name: '新北', a: 11, value2: 41, value3: 53, value4: 14 },
        { name: '天宁', a: 10, value2: 23, value3: 33, value4: 24 },
        { name: '武进', a: 31, value2: 32, value3: 83, value4: 34 },
        { name: '溧阳', a: 21, value2: 42, value3: 43, value4: 44 }
      ],
      parkingFlowD: {}
    }
  },
  computed: {},
  watch: {},
  created() {
    const v = this.$handleEchartsData(this.resList)
    this.parkingFlowD = this.$handleEchartsData(this.resList)
    this.getThreeD()
  },
  mounted() {},
  methods: {
    getThreeD(data) {
      // const data = [{name: '应急车辆',value1: 1212},]
      const color = [
        { c1: 'rgba(73, 200, 234, 1)', c2: 'rgba(44, 178, 215, 0.12)' },
        { c1: 'rgba(51, 212, 116, 1)', c2: 'rgba(78, 209, 45, 0)' },
        { c1: 'rgba(255, 168, 0, 1)', c2: 'rgba(238, 127, 25, 0)' },
        { c1: 'rgba(58, 116, 255, 1)', c2: 'rgba(23, 87, 239, 0)' }
      ]
      let xAxis = [
        '08:00-09:00',
        '09:00-10:00',
        '10:00-11:00',
        '11:00-12:00',
        '12:00-13:00',
        '13:00-14:00'
      ]
      const data1 = [10, 21, 30, 40, 10, 20]
      const data2 = [20, 1, 10, 10, 10, 10]
      const data3 = [3, 10, 10, 10, 10, 10]
      const data4 = [4, 10, 10, 10, 10, 10]
      const title = ['微型车', '小型车', '中型车', '大型车']

      this.parkingFlowD = {
        xAxis,
        title,
        color,
        data: [data1, data2, data3, data4]
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.parkingFlow {
  height: 100%;
}
</style>
