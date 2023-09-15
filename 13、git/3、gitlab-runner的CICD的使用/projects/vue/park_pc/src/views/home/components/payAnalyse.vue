<!--
 * @Description: 预交费码和直付码使用比例统计
 * @Author: Adela
 * @Date: 2023-03-13 09:58:54
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 17:29:59
 * @文件相对于项目的路径: /park_pc/src/views/home/components/payAnalyse.vue
-->
<template>
  <div class="payAnalyse">
    <div class="payAI" style="position: relative">
      <specialGauge :echartData="lineD"></specialGauge>
      <div class="eicon">
        <img src="../../../assets/images/home/eicon.png" alt="" />
      </div>
    </div>
    <div class="payAI payAIp">
      <div class="itemS botttomB">
        <div class="itemST">
          <img src="../../../assets/images/home/prePay.png" alt="" />
          <p>预交费码</p>
        </div>
        <div class="itemSTP">
          <div class="itemSTPI">
            <span class="name">总额：</span>
            <div class="value">
              {{
                payTypeDayFact[0] && payTypeDayFact[0].value
                  ? payTypeDayFact[0].value
                  : 0
              }}
              <span class="unit">万元</span>
            </div>
          </div>
          <div class="itemSTPI">
            <span class="name">占比：</span>
            <div class="value">
              {{
                payTypeDayFact[0] && payTypeDayFact[0].ratio
                  ? payTypeDayFact[0].ratio
                  : 0
              }}
              <span class="unit">%</span>
            </div>
          </div>
        </div>
      </div>
      <div class="itemS">
        <div class="itemST">
          <img src="../../../assets/images/home/pay.png" alt="" />
          <p>直付码</p>
        </div>
        <div class="itemSTP">
          <div class="itemSTPI">
            <span class="name">总额：</span>
            <div class="value">
              {{
                payTypeDayFact[1] && payTypeDayFact[1].value
                  ? payTypeDayFact[1].value
                  : 0
              }}
              <span class="unit">万元</span>
            </div>
          </div>
          <div class="itemSTPI">
            <span class="name">占比：</span>
            <div class="value">
              {{
                payTypeDayFact[1] && payTypeDayFact[1].ratio
                  ? payTypeDayFact[1].ratio
                  : 0
              }}
              <span class="unit">%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPayTypeDayFact } from '@/views/home/api/index.js'
import specialGauge from '../../components/echarts/specialGauge.vue'
export default {
  name: 'PayAnalyse',
  components: {
    specialGauge
  },
  props: {},
  data() {
    return {
      lineD: 0,
      payTypeDayFact: []
      // dataC: [
      //   { name: '临停缴费', value: 4.2 },
      //   { name: '月租会员', value: 2.4 },
      //   { name: '商户储值', value: 0.8 }
      // ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getPayTypeDayFact()
  },
  methods: {
    async getPayTypeDayFact() {
      const res = await getPayTypeDayFact()
      if (res && +res.code === 200 && res.data) {
        this.payTypeDayFact = res.data
        if (res.data[0] && res.data[0].ratio) {
          this.lineD = res.data[0].ratio / 100
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.payAnalyse {
  height: 100%;
  display: flex;

  .payAI {
    width: 50%;
    .itemS {
      max-width: 220px;
      // height: 100px;
      padding-top: 10px;
      .itemST {
        display: flex;
        align-items: center;
        font-size: 15px;
        font-weight: 500;
        color: #001d3c;
        line-height: 18px;
        & > img {
          width: 20px;
          height: 20px;
          margin-right: 10px;
        }
      }
      .itemSTP {
        display: flex;
        flex-direction: column;
        .itemSTPI {
          display: flex;
          align-items: center;
          justify-content: space-between;
          // width: 100%;
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 29, 60, 0.85);
          .value {
            font-size: 22px;
            font-weight: bold;
            color: #001d3c;
            line-height: 26px;
          }
          .unit {
            margin-left: 4px;
            font-size: 12px;
            font-weight: 400;
            color: rgba(0, 29, 60, 0.7);
            line-height: 14px;
          }
        }
      }
    }
  }
  .payAIp {
    display: flex;
    flex-direction: column;
    justify-content: center;
    // padding: 0 20px 0 0;
  }

  .botttomB {
    border-bottom: 1px dotted #d7e5e9;
    padding-bottom: 10px;
  }

  .eicon {
    position: absolute;
    z-index: 99999;
    top: 50%;
    left: 50%;
    transform: translate(-40px, -40px);
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: #eff8fb;
    display: flex;
    align-items: center;
    justify-content: center;
    & > img {
      width: 66px;
      height: 66px;
    }
  }
}
</style>
