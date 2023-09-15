<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-21 15:50:55
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 17:05:36
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/pie.vue
-->
<template>
  <div style="width: 100%; height: 100%; position: relative">
    <div style="width: 100%; height: 100%" ref="myChart" v-if="show"></div>
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
    opt: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      myChart: null,
      useData: this.echartData,
      show: true
    }
  },
  computed: {},
  watch: {
    echartData: {
      handler(newVal, oldVal) {
        this.useData = newVal
        this.initMyChart()
      },
      deep: true
    }
  },
  methods: {
    hexToRgba(hex, opacity) {
      return (
        'rgba(' +
        parseInt('0x' + hex.slice(1, 3)) +
        ',' +
        parseInt('0x' + hex.slice(3, 5)) +
        ',' +
        parseInt('0x' + hex.slice(5, 7)) +
        ',' +
        opacity +
        ')'
      )
    },
    initMyChart() {
      this.myChart = this.$echarts.init(this.$refs.myChart)
      let data1 = []
      let legend = []
      this.useData.forEach((item, index) => {
        data1.push({
          value: item?.value,
          name: item?.name,
          itemStyle: {
            normal: {
              borderWidth: 4,
              color: this.opt.color[index]
            }
          }
        })
        legend.push({
          name: item?.name,
          icon: 'rect',
          itemStyle: {
            color: this.opt.color[index],
            borderColor: this.opt.color[index]
            // borderWidth: 4
            // opacity: 0.8,
            // shadowColor: this.opt.color[index],
            // shadowBlur: 10
          }
        })
      })
      let option = {
        animation: true,
        // backgroundColor: 'transparent',
        // width: '60%',
        // height: '60%',
        title: {
          text: this.opt.text1,
          top: '32%',
          left: '50%',
          subtext: this.opt.text2,
          textAlign: 'center',
          show: this.useData.length > 0,
          triggerEvent: true,
          textStyle: {
            fontSize: 24,
            fontFamily: 'Impact',
            color: '#001D3C',
            padding: [10, 0, 0, 0],
            width: this.opt.shapeR * 2
          },
          subText: {
            fontSize: 14,
            color: '#001D3C',
            padding: [0, 0, 0, 0],
            width: this.opt.shapeR * 2
          }
        },
        grid: {
          top: 0,
          bottom: 0,
          left: 0,
          right: 0,
          containLabel: true
        },
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(9, 24, 48, 0.5)',
          borderColor: 'rgba(75, 253, 238, 0.1)',
          textStyle: {
            fontSize: 12,
            color: '#CFE3FC'
          },
          formatter: (params) => {
            return params.name + ' : ' + params.value
          }
        },
        legend: [
          {
            show: true,
            bottom: '2%',
            data: legend,
            // orient: 'vertical',
            // type: 'scroll',
            // pageIconColor: '#ccc', //图例分页左右箭头图标颜色
            // pageTextStyle: {
            //   color: '#ccc', //图例分页页码的颜色设置
            //   fontSize: 18
            // },
            // pageIconSize: 18, //当然就是按钮的大小
            // pageIconInactiveColor: '#7f7f7f', // 禁用的按钮颜色
            // lineStyle: {
            //   color: '#D0ECFF',
            //   width: 1
            // },
            itemWidth: 16,
            itemHeight: 16,
            // itemGap: this.opt.itemGap,
            padding: 6,
            textStyle: {
              rich: {
                a: {
                  color: '#001D3C',
                  fontSize: 12,
                  width: 100
                }
              }
            },
            formatter: (name) => {
              return `{a|${name}} `
            }
          }
        ],
        series: [
          {
            type: 'pie',
            radius: ['32%', '50%'],
            center: ['50%', '40%'],
            minAngle: 2, // 最小的扇区角度（0 ~ 360），用于防止某个值过小导致扇区太小影响交互
            avoidLabelOverlap: true, // 是否启用防止标签重叠策略
            hoverAnimation: true,
            startAngle: 90,
            selectedMode: 'single',
            selectedOffset: 0,
            itemStyle: {
              normal: {
                borderWidth: 4,
                borderColor: '#fff'
              }
            },
            label: {
              show: false
            },
            data: data1,
            z: 3
          }
        ]
      }
      this.myChart.setOption(option)
      window.addEventListener('resize', () => {
        this.myChart.resize()
      })
      this.myChart.dispatchAction({
        type: 'highlight',
        seriesIndex: 0,
        dataIndex: 0
      })
      this.myChart.on('mouseover', (params) => {
        this.myChart.setOption({
          title: {
            text: params.value,
            top: '32%',
            left: '50%',
            subtext: params.name,
            textAlign: 'center',
            show: this.useData.length > 0,
            triggerEvent: true,
            textStyle: {
              fontSize: 24,
              fontFamily: 'Impact',
              color: '#001D3C',
              padding: [10, 0, 0, 0],
              width: this.opt.shapeR * 2
            }
          }
        })
        this.myChart.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: params.dataIndex
        })
        if (params.dataIndex !== 0) {
          this.myChart.dispatchAction({
            type: 'downplay',
            seriesIndex: 0,
            dataIndex: 0
          })
        }
      })
      this.myChart.on('mouseout', (params) => {
        this.myChart.dispatchAction({
          type: 'downplay',
          seriesIndex: 0,
          dataIndex: params.dataIndex
        })
        this.myChart.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: 0
        })
        this.myChart.setOption({
          title: {
            text: this.opt.text1,
            top: '32%',
            left: '50%',
            subtext: this.opt.text2,
            textAlign: 'center',
            show: this.useData.length > 0,
            triggerEvent: true,
            textStyle: {
              fontSize: 24,
              fontFamily: 'Impact',
              color: '#001D3C',
              padding: [10, 0, 0, 0],
              width: this.opt.shapeR * 2
            }
          }
        })
      })
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
<style scoped lang="scss"></style>
