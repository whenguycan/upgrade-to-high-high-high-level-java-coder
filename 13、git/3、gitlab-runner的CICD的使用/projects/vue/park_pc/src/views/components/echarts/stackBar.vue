<!--
 * @Description: 堆叠图
 * @Author: Adela
 * @Date: 2023-03-09 10:43:55
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-09 17:48:43
 * @文件相对于项目的路径: /park_pc/src/views/components/echarts/stackBar.vue
-->
<template>
  <div
    style="width: 100%; height: 100%; position: relative overflow: auto"
    v-cloak
  >
    <div
      style="width: 100%; height: 100%"
      ref="myChart"
      :class="{ empty: dataOpt.data && dataOpt.data.length <= 0 }"
    ></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  components: {},
  props: {
    isBig: {
      type: Boolean,
      default: false,
    },
    axisMargin: {
      type: Number,
      default: 14,
    },
    showLegend: {
      type: Boolean,
      default: true,
    },
    splitNumber: {
      type: Number,
      default: 5,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    echartOpt: {
      type: Object,
      default() {
        return {
          top: -4,
          right: 10,
        };
      },
    },
    echartData: {
      type: Array,
      default() {
        return [];
      },
    },
    dataOpt: {
      type: Object,
      default() {
        return {
          title: [],
          xAxis: [],
          data: [],
        };
      },
    },
    tit: {
      type: String,
      default: "",
    },
    animation: {
      type: Boolean,
      default: false,
    },
    animationTimeOut: {
      type: Number,
      default: 2000,
    },
  },
  data() {
    return {
      myChart: null,
      useData: this.dataOpt,
      timer: null,
      i: 0,
    };
  },
  computed: {},
  watch: {
    dataOpt: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.useData = newVal;
          this.initMyChart();
        }
      },
      deep: true,
    },
    animation: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.i = 0;
        }
      },
      deep: true,
    },
  },
  created() {
    this.useData = this.dataOpt;
  },
  mounted() {
    this.$nextTick(() => {
      if (this.useData) {
        this.initMyChart();
      }
    });
  },
  methods: {
    getArrByKey(data, k) {
      let key = k || "value";
      let res = [];
      if (data) {
        data.forEach(function (t) {
          res.push(t[key]);
        });
      }
      return res;
    },
    getSeries() {
      const series = [];
      if (this.useData && this.useData.data) {
        this.useData.data.map((item, index) => {
          series.push({
            name: this.useData.title[index],
            type: "bar",
            showBackground: false,
            xAxisIndex: 0,
            data: item,
            stack: "value",
            barWidth: this.isBig ? 28 : 19,
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: this.useData.color[index].c2,
                    },
                    {
                      offset: 1,
                      color: this.useData.color[index].c1,
                    },
                  ],
                  false
                ),
              },
              barBorderRadius: 4,
            },
            label: {
              normal: {
                color: "#fff",
                show: false,
                position: [-5, -23],
                textStyle: {
                  fontSize: 16,
                },
                formatter: function (a, b) {
                  return a.value;
                },
              },
            },
          });
        });
      }
      return series;
    },
    initMyChart() {
      console.log("adela editor", this.dataOpt);
      this.$refs.myChart.style.height = "100%";
      this.myChart = echarts.init(this.$refs.myChart, null, {
        renderer: "svg",
      });
      let option = {
        backgroundColor: "transparent",
        grid: {
          top: 45,
          bottom: 0,
          right: 0,
          left: 20,
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
          textStyle: {
            fontSize: 16,
          },
          formatter: function (param) {
            var resultTooltip =
              '<div style="background:#EFF8FB;border:1px solid rgba(255,255,255,.2);padding:5px 12px;border-radius:4px;">';
            resultTooltip += `<div style="">${param[0]?.name}</div><div style="padding-top:5px;">`;
            param.forEach((item) => {
              resultTooltip += `
                <div style="display:flex;align-items: center;">
                  <span style="display:inline-block;width:16px;height:16px;background-color:${item.color.colorStops[1].color};margin-right: 10px;border: 2px solid rgba(255, 255, 255, 0.4);"></span>
                  <div style="display:flex; justify-content: space-between">
                    <span style="margin-right: 5px">${item?.seriesName}:</span><span>${item?.value} </span>
                  </div>
                </div>`;
            });
            resultTooltip += `</div></div>`;
            return resultTooltip;
          },
        },
        legend: {
          show: true,
          data: this.useData?.title,
          top: 0,
          right: this.echartOpt.right,
          itemWidth: 14,
          itemHeight: 14,
          itemGap: 20,
          textStyle: {
            color: "#BFC6CE",
            fontWeight: "normal",
            fontSize: 16,
          },
        },
        xAxis: {
          show: true,
          type: "category",
          axisLabel: {
            margin: this.axisMargin,
            textStyle: {
              fontSize: 16,
              color: "#BFC6CE",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            lineStyle: {
              color: "#e6ebf5",
            },
            show: true,
          },
          data: this.useData?.xAxis,
        },
        yAxis: {
          show: true,
          type: "value",
          splitNumber: this.splitNumber,
          // name: '(%)',
          // nameLocation: 'start',
          // nameTextStyle: {
          //   color: 'rgba(222, 230, 235, 0.6)',
          //   padding: [0, 20, 0, 0]
          // },
          axisLabel: {
            formatter: "{value}",
            textStyle: {
              fontSize: 16,
              color: "#BFC6CE",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: "#e6ebf5",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        series: this.getSeries(),
      };
      this.myChart.setOption(option);
      window.addEventListener("resize", () => {
        this.myChart.resize();
      });
      // if (this.animation) {
      //   this.barAnimation();
      //   this.myChart.on("mouseover", (params) => {
      //     if (this.timer) {
      //       clearInterval(this.timer);
      //       this.timer = null;
      //     }
      //     this.myChart.dispatchAction({
      //       type: "downplay",
      //       seriesIndex: 0,
      //       dataIndex: this.i - 1,
      //     });
      //   });
      //   this.myChart.on("mouseout", (params) => {
      //     this.barAnimation();
      //   });
      // }
    },
    barAnimation() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
      this.timer = setInterval(() => {
        this.myChart.dispatchAction({
          type: "downplay",
          seriesIndex: 0,
          dataIndex: this.i - 1,
        });
        this.myChart.dispatchAction({
          type: "highlight",
          seriesIndex: 0,
          dataIndex: this.i,
        });
        this.myChart.dispatchAction({
          type: "showTip",
          seriesIndex: 0,
          dataIndex: this.i,
        });
        this.i++;
        if (this.i > this.useData.data[0].length - 1) {
          this.i = 0;
        }
      }, this.animationTimeOut);
    },
  },

  beforeDestroy() {
    clearInterval(this.timer);
  },
};
</script>
<style scoped lang="scss"></style>
