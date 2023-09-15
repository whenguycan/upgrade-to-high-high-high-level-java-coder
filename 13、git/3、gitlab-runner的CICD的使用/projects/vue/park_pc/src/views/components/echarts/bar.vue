<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-20 15:29:54
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-20 16:15:04
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/bar.vue
-->
<template>
  <div class="barLine">
    <div id="barLine" ref="barLine"></div>
  </div>
</template>

<script>
export default {
  name: '',
  components: {},
  props: {
    isShowLegend: {
      type: Boolean,
      default: false
    },
    unit: {
      type: String,
      default: ''
    },
    echartsD: {
      type: Object,
      default: () => {
        return {}
      }
    },
    barColor: {
      type: Array,
      default: () => {
        return [
          ['#81E4FF', '#1A61EE'],
          ['#7CF1BB', '#20A672']
        ]
      }
    },
    lineColor: {
      type: String,
      default: () => {
        return '#FF980B'
      }
    }
  },
  data() {
    return {
      myEChart: {},
      chartData: {}
    }
  },
  computed: {},
  watch: {
    echartsD: {
      handler(newValue, oldValue) {
        this.chartData = newValue
        this.initMyEChart()
      },
      deep: true
    }
  },
  created() {},
  mounted() {
    this.chartData = this.echartsD
    if (this.echartsD.echartsName) {
      this.initMyEChart()
    }
  },
  methods: {
    initMyEChart() {
      const { echartsName, echartsLegend, echartsLegendName, echartsD } =
        this.chartData
      const barColor = this.barColor
      const series = echartsLegendName.map((item, index) => {
        return {
          type: 'bar',
          yAxisIndex: 0,
          name: item,
          itemStyle: {
            normal: {
              borderRadius: 8,
              color: new this.$echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1,
                [
                  {
                    offset: 0,
                    color: this.$hexToRgb(barColor[index][0], 1)
                  },
                  {
                    offset: 1,
                    color: this.$hexToRgb(barColor[index][1], 1)
                  }
                ],
                false
              )
            }
          },
          barWidth: 8,
          data: echartsD[index]
        }
      })
      this.$refs.barLine.style.height = '100%'
      const unit = this.unit
      this.myEChart = this.$echarts.init(this.$refs.barLine)
      this.myEChart.setOption({
        backgroundColor: '#fff',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          backgroundColor: 'rgba(9, 24, 48, 0.5)',
          borderColor: 'rgba(75, 253, 238, 0.1)',
          textStyle: {
            color: '#CFE3FC'
          },
          borderWidth: 1,
          formatter: function (params) {
            let str = ''
            for (let i = 0; i < params.length; i++) {
              if (i == 0) {
                str += `${params[i].name}<br/>${params[i].seriesName}：<span>${params[0].data} ${unit}</span><br/>`
                continue
              }
              if (i == 1) {
                str += `${params[i].seriesName}：<span>${params[1].data} ${unit}</span><br/>`
                continue
              }
              str += `${params[i].seriesName}：<span>${params[i].data}</span>（%）<br/>`
            }
            return str
          }
        },
        legend: {
          show: this.isShowLegend,
          data: echartsLegendName,
          top: 0,
          right: 10,
          // icon: 'rect',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 20,
          textStyle: {
            color: '#BFC6CE',
            fontWeight: 'normal',
            fontSize: 16
          }
        },
        grid: {
          left: '20px',
          right: '20px',
          top: '40px',
          bottom: '10px',
          containLabel: true
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          x: 'right',
          y: 'center'
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: true,
            axisTick: {
              show: false
            },
            // data: ['虚假博彩类', '兼职刷单类', '网络贷款类', '游戏交易类', '冒充领导(熟人)类', '虚假购物类', '其他'],
            data: echartsName || [],
            axisLine: {
              lineStyle: {
                color: '#dcdee2'
              }
            },
            axisLabel: {
              // formatter: function (value) {
              //   var ret = '' //拼接加\n返回的类目项
              //   var maxLength = 1 //每项显示文字个数
              //   var valLength = value.length //X轴类目项的文字个数
              //   var rowN = Math.ceil(valLength / maxLength) //类目项需要换行的行数
              //   if (rowN > 1) {
              //     //如果类目项的文字大于3,
              //     for (var i = 0; i < rowN; i++) {
              //       var temp = '' //每次截取的字符串
              //       var start = i * maxLength //开始截取的位置
              //       var end = start + maxLength //结束截取的位置
              //       //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
              //       temp = value.substring(start, end) + '\n'
              //       ret += temp //凭借最终的字符串
              //     }
              //     return ret
              //   } else {
              //     return value
              //   }
              // },
              // interval: 0,
              color: '#808695',
              fontSize: 12
            }
          }
        ],
        yAxis: [
          {
            name: this.unit,
            nameTextStyle: {
              color: '#808695',
              fontSize: 12,
              padding: [0, 0, 0, -26]
            },
            type: 'value',
            axisTick: {
              show: false
            },
            axisLine: {
              show: false
            },
            // max: warnD1MAX,
            // interval: warnD1MAX / 5,
            axisLabel: {
              interval: 0,
              color: '#808695'
            },
            nameLocation: 'start',
            splitLine: {
              show: true,
              lineStyle: {
                color: '#f8f8f9',
                width: 1,
                type: 'solid'
              }
            }
          }
        ],
        series
        // series: [
        //   {
        //     type: 'bar',
        //     yAxisIndex: 0,
        //     name: echartsLegendName[0],
        //     itemStyle: {
        //       normal: {
        //         borderRadius: 8,
        //         color: new this.$echarts.graphic.LinearGradient(
        //           0,
        //           0,
        //           0,
        //           1,
        //           [
        //             {
        //               offset: 0,
        //               color: this.$hexToRgb(barColor[0][0], 1)
        //             },
        //             {
        //               offset: 1,
        //               color: this.$hexToRgb(barColor[0][1], 0.2)
        //             }
        //           ],
        //           false
        //         )
        //       }
        //     },
        //     barWidth: 8,
        //     data: echartsD[0] || [114, 12, 15, 10, 12, 13, 1]
        //   },
        //   {
        //     type: 'bar',
        //     yAxisIndex: 0,
        //     name: echartsLegendName[1],
        //     itemStyle: {
        //       normal: {
        //         color: new this.$echarts.graphic.LinearGradient(
        //           0,
        //           0,
        //           0,
        //           1,
        //           [
        //             {
        //               offset: 0,
        //               color: this.$hexToRgb(barColor[1][0], 1)
        //             },
        //             {
        //               offset: 1,
        //               color: this.$hexToRgb(barColor[1][1], 0.2)
        //             }
        //           ],
        //           false
        //         )
        //       }
        //     },
        //     barWidth: 8,
        //     data: echartsD[1] || []
        //   }
        // ]
      })
      window.addEventListener('resize', () => {
        this.myEChart.resize()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.barLine {
  height: 100%;
}
</style>
