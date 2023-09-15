<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-20 15:15:07
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-20 16:17:00
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/line.vue
-->
<template>
  <div class="line">
    <div id="lineS" ref="lineS"></div>
  </div>
</template>

<script>
export default {
  name: 'lineS',
  components: {},
  props: {
    unit: {
      type: String,
      default: ''
    },
    color: {
      type: String,
      default: () => {
        return '#4674F7'
      }
    },
    echartsD: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
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
      this.$refs.lineS.style.height = '100%'
      this.myEChart = this.$echarts.init(this.$refs.lineS)
      const that = this
      const series = echartsLegend.map((item, index) => {
        return {
          type: 'line',
          symbol: 'circle',
          name: echartsLegendName[index],
          symbolSize: 10,
          smooth: false,
          animationDuration: 2000,
          areaStyle: {
            normal: {
              color: new this.$echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1,
                [
                  {
                    offset: 0,
                    color: this.$hexToRgb(this.color, 0.2)
                  },
                  {
                    offset: 1,
                    color: this.$hexToRgb(this.color, 0.01)
                  }
                ],
                false
              ),
              shadowColor: this.$hexToRgb(this.color, 0.01),
              shadowBlur: 10
            }
          },
          itemStyle: {
            normal: {
              color: this.$hexToRgb(this.color, 0.8),
              borderColor: '#fff',
              borderWidth: 2
            }
          },
          data: echartsD[item]
          // markLine: {
          //   silent: true,
          //   label: {
          //     show: true,
          //     position: 'insideEndTop',
          //     fontSize: 10,
          //     fontWeight: 700,
          //     formatter: '压降目标'
          //   },
          //   lineStyle: {
          //     color: 'rgba(23, 35, 61, 0.3)'
          //   },
          //   data: [
          //     {
          //       yAxis: this.chartData.target || 1
          //     }
          //   ]
          // }
        }
      })
      const option = {
        title: {
          text: '',
          left: 'center'
        },
        legend: {
          show: false,
          data: echartsLegend,
          top: 0,
          right: 0,
          icon: 'rect',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 20,
          textStyle: {
            color: '#BFC6CE',
            fontWeight: 'normal',
            fontSize: 16
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
            // shadowStyle: {
            //   color: 'rgba(221, 219, 219, 0.3)'
            // }
          },
          backgroundColor: 'rgba(9, 24, 48, 0.5)',
          borderColor: 'rgba(75, 253, 238, 0.1)',
          textStyle: {
            color: '#CFE3FC'
          },
          formatter: function (params) {
            const unit = that.unit
            let str = ''
            for (let i = 0; i < params.length; i++) {
              if (i == 0) {
                str += `${params[i].name}<br/>${params[i].seriesName}：<span>${params[0].data}${unit}</span><br/>`
                continue
              }
              str += `${params[i].seriesName}：<span>${params[i].data}${unit}</span><br/>`
            }
            return str
          }
        },
        xAxis: {
          type: 'category',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              width: 1,
              color: '#E4EAF7'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            // rotate: 45,
            color: 'rgba(0, 6, 34, 0.5)'
          },
          data: echartsName
        },
        grid: {
          top: '20',
          left: '20',
          right: '10',
          bottom: '30',
          containLabel: true
        },

        yAxis: {
          type: 'value',
          name: this.unit,
          nameTextStyle: {
            color: '#808695',
            fontSize: 12,
            padding: [0, 0, 0, -26]
          },
          nameLocation: 'start',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              width: 1,
              color: '#E4EAF7'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: 'rgba(0, 6, 34, 0.5)'
          }
        },
        series
        // series: [
        //   {
        //     type: 'line',
        //     symbol: 'circle',
        //     name: echartsLegend[0],
        //     symbolSize: 10,
        //     smooth: false,
        //     animationDuration: 2000,
        //     areaStyle: {
        //       normal: {
        //         color: new this.$echarts.graphic.LinearGradient(
        //           0,
        //           0,
        //           0,
        //           1,
        //           [
        //             {
        //               offset: 0,
        //               color: this.$hexToRgb(this.color, 0.2)
        //             },
        //             {
        //               offset: 1,
        //               color: this.$hexToRgb(this.color, 0.01)
        //             }
        //           ],
        //           false
        //         ),
        //         shadowColor: this.$hexToRgb(this.color, 0.01),
        //         shadowBlur: 10
        //       }
        //     },
        //     itemStyle: {
        //       normal: {
        //         color: this.$hexToRgb(this.color, 0.8),
        //         borderColor: '#fff',
        //         borderWidth: 2
        //       }
        //     },
        //     data: echartsD[echartsLegend[0]],
        //     markLine: {
        //       silent: true,
        //       label: {
        //         show: true,
        //         position: 'insideEndTop',
        //         fontSize: 10,
        //         fontWeight: 700,
        //         formatter: '压降目标'
        //       },
        //       lineStyle: {
        //         color: 'rgba(23, 35, 61, 0.3)'
        //       },
        //       data: [
        //         {
        //           yAxis: this.chartData.target || 1
        //         }
        //       ]
        //     }
        //   }
        // ]
      }
      this.myEChart.setOption(option)

      window.addEventListener('resize', () => {
        this.myEChart.resize()
      })
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.line {
  height: 100%;
}
</style>
