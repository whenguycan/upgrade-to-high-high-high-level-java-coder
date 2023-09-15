<!--
 * @Description: 特殊仪表盘
 * @Author: Adela
 * @Date: 2023-03-13 10:00:54
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 16:56:01
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/specialGauge.vue
-->
<template>
  <div style="width: 100%; height: 100%; position: relative" v-cloak>
    <div
      style="width: 100%; height: 100%"
      ref="myChart"
      :class="{ empty: useData <= 0 }"
    ></div>
  </div>
</template>

<script>
export default {
  name: 'SpecialGauge',
  components: {},
  props: {
    echartData: {
      type: Number | String,
      default: 0
    },
    color: {
      type: Array,
      default() {
        return ['#49C8EA', '#FFC658', '#2C68F5', '#8E8EF0', '#FFFFFF']
      }
    },
    noText: {
      type: Boolean,
      default: true
    },
    icon: {
      type: String,
      default: ''
    },
    normalLength: {
      type: Number,
      default: 10
    },
    isBig: {
      type: Boolean,
      default: false
    },
    isOneLine: {
      type: Boolean,
      default: false
    },
    legendTop: {
      type: String,
      default: '0%'
    }
  },
  data() {
    return {
      myChart: null,
      loading: false,
      useData: this.echartData,
      timer: null,
      i: 0
    }
  },
  computed: {},
  watch: {
    echartData: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.useData = newVal
          this.initMyChart()
        }
      },
      deep: true
    }
  },
  methods: {
    initMyChart() {
      this.$refs.myChart.style.height = '100%'
      this.myChart = this.$echarts.init(this.$refs.myChart)

      let option = (option = {
        backgroundColor: 'transparent',
        series: [
          {
            name: '刻度',
            type: 'gauge',
            radius: '82%',
            min: 0,
            max: 100,
            startAngle: 220,
            endAngle: -40,
            splitNumber: 8,
            axisLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: [[1, 'rgba(0,0,0,0)']]
              }
            }, //仪表盘轴线
            axisLabel: {
              show: false,
              color: '#D7E5E9',
              distance: 30
            }, //刻度标签。
            axisTick: {
              show: true,
              splitNumber: 15,
              lineStyle: {
                color: '#D7E5E9',
                width: 1
              },
              length: -8
            }, //刻度样式
            splitLine: {
              show: true,
              length: -8,
              lineStyle: {
                color: '#fff'
              }
            }, //分隔线样式
            detail: {
              show: false
            },
            pointer: {
              show: false
            }
          },
          // {
          //     name: '内圈小',
          //     type: 'gauge',
          //     radius: '65%',
          //     startAngle: 220,
          //     endAngle: -40,
          //     axisLine: { // 坐标轴线
          //         lineStyle: { // 属性lineStyle控制线条样式
          //             color: [
          //                 [
          //                     1, new echarts.graphic.LinearGradient(
          //                         0, 1, 0, 0, [{
          //                                 offset: 0,
          //                                 color: 'rgba(2,37,51,0)',
          //                             }, {
          //                                 offset: 0.3,
          //                                 color: 'rgba(2,37,51,0.5)',
          //                             },
          //                             {
          //                                 offset: 1,
          //                                 color: 'rgba(2,37,51,0.8)',
          //                             }
          //                         ]
          //                     )
          //                 ],
          //             ],
          //             width: 110
          //         }

          //     },
          //     splitLine: { //分隔线样式
          //         show: false,
          //     },
          //     axisLabel: { //刻度标签
          //         show: false,
          //     },
          //     pointer: {
          //         show: false,
          //     },
          //     axisTick: { //刻度样式
          //         show: false,
          //     },
          //     detail: {
          //         show: false
          //     }
          // },
          {
            type: 'gauge',
            radius: '68%',
            center: ['50%', '50%'],
            startAngle: 220,
            endAngle: -40,
            axisLine: {
              lineStyle: {
                color: [
                  [
                    +this.echartData,
                    new this.$echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(9, 72, 197, 1)'
                      },
                      {
                        offset: 1,
                        color: 'rgba(73, 200, 234, 1)'
                      }
                    ])
                  ],
                  [
                    +this.echartData + 0.005,
                    new this.$echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(73, 200, 234, 0)'
                      },
                      {
                        offset: 1,
                        color: 'rgba(9, 72, 197, 0)'
                      }
                    ])
                  ],
                  [
                    1,
                    new this.$echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(255, 174, 16, 1)'
                      },
                      {
                        offset: 1,
                        color: 'rgba(243, 88, 23, 1)'
                      }
                    ])
                  ]
                ],
                // color: [
                //     [0.3, '#13E267'],
                //     [1, "red"]
                // ],
                width: 12
              }
            },
            //分隔线样式。
            splitLine: {
              show: false
            },
            axisLabel: {
              show: false
            },
            axisTick: {
              show: false
            },
            pointer: {
              show: false
            },
            //仪表盘详情，用于显示数据。
            detail: {
              show: false,
              splitNumber: 15,
              offsetCenter: [0, 0],
              formatter: function (params) {
                return params
              },
              textStyle: {
                color: 'yellow',
                fontSize: 40
              }
            },
            data: []
          }
        ]
      })
      this.myChart.setOption(option)
      window.addEventListener('resize', () => {
        this.myChart.resize()
      })
    }
  },
  created() {
    this.useData = this.echartData
  },
  mounted() {
    this.$nextTick(() => {
      if (this.useData > 0) {
        this.initMyChart()
      }
    })
  },
  beforeDestroy() {
    clearInterval(this.timer)
  }
}
</script>
<style scoped lang="scss">
.legendBg {
  position: absolute;
  width: 246px;
  height: 43px;
  background: #edf7fa;
  border-radius: 4px 4px 4px 4px;
  opacity: 1;
}

.legendBg1 {
  right: 40px;
}
</style>
