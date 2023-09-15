<!--
 * @Description: 普通pie
 * @Author: Adela
 * @Date: 2023-03-09 14:21:32
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 16:42:32
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/normalPie.vue
-->
<template>
  <div style="width: 100%; height: 100%; position: relative" v-cloak>
    <div
      style="width: 100%; height: 100%"
      ref="myChart"
      :class="{ empty: useData.length <= 0 }"
    ></div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    echartData: {
      type: Array,
      default() {
        return []
      }
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
      const totalCount = this.useData.reduce((pre = 0, currV) => {
        return (pre += currV.value)
      }, 0)

      let option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{b}'
        },
        legend: {
          orient: 'vertical',
          top: 'center',
          left: '50%',
          data: this.useData,
          itemWidth: 16,
          itemHeight: 16,
          itemGap: 30,
          padding: 6,
          textStyle: {
            lineHeight: 40,
            // backgroundColor: "#EDF7FA",
            backgroundColor: {
              image: require('../../../assets/images/common/echarts/pieBg.png')
            },
            borderRadius: 4,
            padding: [0, 10, 0, 10],
            rich: {
              // a（系列名称），b（数据项名称），c（数值）, d（百分比）
              a: {
                color: '#001D3C',
                fontSize: 14,
                width: 40,
                padding: [0, 0, 0, 10]
              },
              b: {
                fontSize: 22,
                width: 80,
                fontWeight: '700',
                align: 'right'
              },
              c: {
                color: '#001D3C',
                fontSize: 14,
                width: 30,
                align: 'right'
              }
            }
          },
          formatter: (name) => {
            let target
            for (let i = 0; i < this.useData.length; i++) {
              if (this.useData[i].name == name && name) {
                if (totalCount > 0) {
                  target =
                    this.useData[i].value > 0
                      ? parseFloat(
                          parseFloat(this.useData[i].value / totalCount) * 100
                        ).toFixed(2) + '%'
                      : this.useData[i].value + '%'
                } else {
                  target = this.useData[i].value + this.unit
                }
                target = this.useData[i].value
              }
            }
            if (this.noText) {
              return `{a|${name}}{b|${target}}{c|万元}`
            } else {
              return `{a|${name}}`
            }
          }
        },
        series: [
          {
            name: '',
            type: 'pie',
            // roseType: true,
            clockwise: false,
            center: ['25%', '50%'],
            radius: ['0', '50%'],
            data: this.useData,
            minAngle: 10,
            color: this.color,
            labelLine: {
              normal: {
                show: false
              }
            },
            label: {
              normal: {
                // position: 'inner',
                // formatter: '{d}%',
                show: false,
                textStyle: {
                  color: '#fff',
                  fontWeight: 'bold',
                  fontSize: 14
                }
              }
            }
          }
        ]
      }
      this.myChart.setOption(option)
      window.addEventListener('resize', () => {
        this.myChart.resize()
      })
    },
    pieAnimation() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
      this.timer = setInterval(() => {
        this.myChart.dispatchAction({
          type: 'downplay',
          seriesIndex: 0,
          dataIndex: this.i == 0 ? this.useData.length - 1 : this.i - 1
        })
        this.myChart.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: this.i
        })
        if (this.i == this.echartData.lengthuseData * 2 - 1) {
          this.i = 0
        } else {
          this.i++
        }

        if (this.i % 2 == 1) {
          // 展示tootip
          this.myChart.dispatchAction({
            type: 'showTip',
            seriesIndex: 0,
            dataIndex: this.i - 1
          })
        }
      }, 2000)
    }
  },
  created() {
    this.useData = this.echartData
  },
  mounted() {
    this.$nextTick(() => {
      if (this.useData.length > 0) {
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
