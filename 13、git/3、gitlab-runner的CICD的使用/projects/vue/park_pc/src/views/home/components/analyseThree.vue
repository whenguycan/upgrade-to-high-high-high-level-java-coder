<!--
 * @Description: 昨日时长统计
 * @Author: Adela
 * @Date: 2023-03-21 15:40:36
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 16:49:42
 * @文件相对于项目的路径: /park_pc/src/views/home/components/analyseThree.vue
-->
<template>
  <div class="analyseThree">
    <Npie :echartData="pieData1" :opt="pieOpt"></Npie>
  </div>
</template>

<script>
import { getDurationStatisticDayFact } from '@/views/home/api/index.js'
import Npie from '@/views/components/echarts/pie.vue'
export default {
  name: 'AnalyseThree',
  components: {
    Npie
  },
  props: {},
  data() {
    return {
      pieData1: [
        { name: '新北区', value: 23 },
        { name: '天宁区', value: 20 },
        { name: '钟楼区', value: 10 },
        { name: '武进区', value: 44 }
      ],
      pieOpt: {
        color: [
          '#2C68F5',
          '#7C8DFF',
          '#06E38A',
          '#FFDE5C',
          '#FFAA5B',
          '#49C8EA',
          '#e3da41',
          '#eebe4e',
          '#fcc35f',
          '#ff9c44',
          '#fe628d',
          '#fe628d',
          '#fe628d'
        ],
        total: 127, // 数据总数
        text1: '',
        text2: '',
        shapeR: 40, // pie图中间白色阴影半径
        shapeLeft: '30%', // pie图中间白色阴影距左距离
        lengendShow: false
      }
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getDurationStatisticDayFact()
  },
  methods: {
    async getDurationStatisticDayFact() {
      const res = await getDurationStatisticDayFact()
      if (res && +res.code === 200 && res.data) {
        this.pieData1 = res.data
        this.pieOpt.text1 = res.data[0].value
        this.pieOpt.text2 = res.data[0].name
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.analyseThree {
  height: 100%;
}
</style>
