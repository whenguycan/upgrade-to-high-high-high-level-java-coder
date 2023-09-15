<template>
  <div class="home">
    <el-row :gutter="10">
      <el-col
        :xs="12"
        :sm="8"
        :md="{ span: '4-8' }"
        :lg="{ span: '4-8' }"
        style="padding-bottom: 10px"
      >
        <el-card class="update-log">
          <div class="TopIcon TopIconBg_1">
            <div class="topL">
              <div class="topLTitle">今日收入</div>
              <div class="topLC">
                <span class="topLCValue">{{
                  homepageHeaderD.todayIncome ? homepageHeaderD.todayIncome : 0
                }}</span>
                <span class="toplCUnit">万元</span>
              </div>
            </div>
            <div class="topRIcon topRIcon_1"></div>
          </div>
        </el-card>
      </el-col>
      <el-col
        :xs="12"
        :sm="8"
        :md="{ span: '4-8' }"
        :lg="{ span: '4-8' }"
        style="padding-bottom: 10px"
      >
        <!-- <el-col style="margin-bottom: 10px; width: 20%"> -->
        <el-card class="update-log">
          <div class="TopIcon TopIconBg_2">
            <div class="topL">
              <div class="topLTitle">总收入</div>
              <div class="topLC">
                <span class="topLCValue">{{
                  homepageHeaderD.totalIncome ? homepageHeaderD.totalIncome : 0
                }}</span>
                <span class="toplCUnit">万元</span>
              </div>
            </div>
            <div class="topRIcon topRIcon_2"></div>
          </div>
        </el-card>
      </el-col>
      <el-col
        :xs="12"
        :sm="8"
        :md="{ span: '4-8' }"
        :lg="{ span: '4-8' }"
        style="padding-bottom: 10px"
      >
        <el-card class="update-log">
          <div class="TopIcon TopIconBg_3">
            <div class="topL">
              <div class="topLTitle">剩余车位</div>
              <div class="topLC">
                <span class="topLCValue">{{
                  homepageHeaderD.remainSpaceCount
                    ? homepageHeaderD.remainSpaceCount
                    : 0
                }}</span>
                <span class="toplCUnit">个</span>
              </div>
            </div>
            <div class="topRIcon topRIcon_3"></div>
          </div>
        </el-card>
      </el-col>
      <el-col
        :xs="12"
        :sm="12"
        :md="{ span: '4-8' }"
        :lg="{ span: '4-8' }"
        style="padding-bottom: 10px"
      >
        <el-card class="update-log">
          <div class="TopIcon TopIconBg_4">
            <div class="topL">
              <div class="topLTitle">访客未处置</div>
              <div class="topLC">
                <span class="topLCValue">{{
                  homepageHeaderD.notDisposedCount
                    ? homepageHeaderD.notDisposedCount
                    : 0
                }}</span>
                <span class="toplCUnit">个</span>
              </div>
            </div>
            <div class="topRIcon topRIcon_4"></div>
          </div>
        </el-card>
      </el-col>
      <el-col
        :xs="24"
        :sm="12"
        :md="{ span: '4-8' }"
        :lg="{ span: '4-8' }"
        style="padding-bottom: 10px"
      >
        <el-card class="update-log">
          <div class="TopIcon TopIconBg_5">
            <div class="topL">
              <div class="topLTitle">固定车</div>
              <div class="topLC">
                <span class="topLCValue">{{
                  homepageHeaderD.regularCarCount
                    ? homepageHeaderD.regularCarCount
                    : 0
                }}</span>
                <span class="toplCUnit">辆</span>
              </div>
            </div>
            <div class="topRIcon topRIcon_5"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <titleTab title="付费统计"></titleTab>
            <div class="recordTab">
              <span
                class="recordTabI"
                :class="{ recordTabIA: activeIndex === 1 }"
                @click="chooseTab(1)"
                >付费类型</span
              >
              <span
                class="recordTabI"
                :class="{ recordTabIA: activeIndex === 2 }"
                @click="chooseTab(2)"
                >付费方式</span
              >
            </div>
            <div class="carCItem parkingflowList">
              <todayIncome v-if="activeIndex === 2"></todayIncome>
              <payAnalyse v-else></payAnalyse>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <titleTab title="昨日时长统计"></titleTab>
            <div class="carCItem carRecordList">
              <AnalyseThree></AnalyseThree>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <titleTab title="近七日订单情况"></titleTab>
            <div class="carCItem carRecordList">
              <AnalyseFour></AnalyseFour>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <div class="titleD">
              <titleTab title="昨日进出场分析"></titleTab>
            </div>
            <div class="carCItem carRecordList">
              <AnalyseFive></AnalyseFive>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <div class="titleD">
              <titleTab title="昨日分时利用"></titleTab>
            </div>
            <div class="carCItem carRecordList">
              <AnalyseSix></AnalyseSix>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <titleTab title="今日停车流量分析" class="carT"></titleTab>
            <div class="carCItem parkingflowList">
              <parkingFlow></parkingFlow>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="cardCItem" style="margin-bottom: 10px">
          <div class="carC">
            <titleTab title="进出场车辆记录"></titleTab>
            <div class="carCItem carRecordList">
              <carList></carList>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import { getHomePageHeader } from '@/views/home/api/index.js'
import titleTab from '@/components/Title/index.vue'
import todayIncome from './home/components/todayIncome.vue'
// import parkingFlow from './home/components/parkingFlow.vue'
// import carList from '@/views/home/components/cardRecord.vue'
import payAnalyse from './home/components/payAnalyse.vue'

import AnalyseThree from './home/components/analyseThree.vue'
import AnalyseFour from './home/components/analyseFour.vue'
import AnalyseFive from './home/components/analyseFive.vue'
import AnalyseSix from './home/components/analyseSix.vue'

export default {
  name: 'Index',
  components: {
    titleTab,
    todayIncome,
    // parkingFlow,
    // carList,
    payAnalyse,
    AnalyseThree,
    AnalyseFour,
    AnalyseFive,
    AnalyseSix
  },
  data() {
    return {
      activeIndex: 1,
      showF: true,
      homepageHeaderD: {}
    }
  },
  created() {},
  mounted() {
    this.getHomePageHeader()
  },
  methods: {
    chooseTab(v) {
      this.activeIndex = v
    },
    goTarget(href) {
      window.open(href, '_blank')
    },
    async getHomePageHeader() {
      const res = await getHomePageHeader()
      if (res && +res.code === 200 && res.data) {
        this.homepageHeaderD = res.data
      }
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  margin: 16px;
  // .el-col-md-4-8 {
  //   width: 20%;
  // }
  ::v-deep .el-col {
    // padding: 0 5px !important;
  }
  ::v-deep .el-card__body {
    padding: 0;
  }

  .TopIcon {
    position: relative;
    width: 100%;
    min-height: 10vh;

    // height: 96px;
    // background: url("../assets/images/home/top1_bg.png") no-repeat;
    background-size: 100% 96px;
    display: flex;
    align-items: center;
    padding: 0 10px 0 20px;
  }
  .topL {
    flex: 1;
    .topLTitle {
      width: 100%;
      font-size: 14px;
      font-weight: 400;
      color: rgba(0, 29, 60, 0.85);
      line-height: 26px;
    }
    .topLC {
      display: flex;
      align-items: flex-end;

      .topLCValue {
        font-size: 28px;
        font-weight: bold;
        color: #001d3c;
        line-height: 34px;
      }
      .toplCUnit {
        font-size: 12px;
        font-weight: 400;
        color: rgba(0, 29, 60, 0.7);
        line-height: 24px;
        margin-left: 6px;
      }
    }
  }
  .topRIcon {
    position: absolute;
    right: 10px;
    width: 56px;
    height: 64px;
    background-size: 56px 64px;
    margin-top: 10px;
  }

  @for $i from 1 through 5 {
    .TopIconBg_#{$i} {
      color: #ccc;
      background: url('../assets/images/home/top#{$i}_bg.png') no-repeat;
      background-size: 100% 10vh;
      .topRIcon_#{$i} {
        background: url('../assets/images/home/top#{$i}_icon.png') no-repeat;
        background-size: 56px 64px;
      }
    }
  }

  .carC {
    padding: 20px 20px 20px 0;
    position: relative;
    height: 100%;
    .carT {
      position: absolute;
    }
    .carCItem {
      // height: 1000px;
      padding: 20px 0 20px 20px;
    }
    .carRecordList {
      height: calc(38vh - 50px) !important;
      padding: 0;
      // overflow-y: auto;
    }
    .parkingflowList {
      height: calc(38vh - 50px) !important;
      // padding: 0 0 0 20px;
    }
  }

  .cardCItem {
    // height: calc(50vh - 144px) !important;
    height: 38vh;
  }

  .recordTab {
    position: absolute;
    top: 18px;
    right: 18px;
    width: 160px;
    height: 28px;
    background: #e5f0f2;
    border-radius: 4px 4px 4px 4px;
    opacity: 1;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .recordTabI {
      width: 70px;
      height: 18px;
      font-size: 13px;
      font-weight: 400;
      color: rgba(0, 29, 60, 0.75);
      line-height: 15px;
      padding: 2px 0;
      // padding: 2px 10px;
      text-align: center;
      cursor: pointer;
    }

    .recordTabIA {
      background: #ffffff;
      box-shadow: 0px 2px 4px 0px rgba(0, 29, 60, 0.2);
      border-radius: 4px 4px 4px 4px;
    }
  }

  .titleD {
    position: absolute;
    z-index: 9999;
  }
}
</style>
