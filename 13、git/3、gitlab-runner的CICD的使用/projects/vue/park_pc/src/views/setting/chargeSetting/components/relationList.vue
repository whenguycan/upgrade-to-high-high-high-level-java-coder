<template>
  <div class="relation-list" ref="list">
    <div class="relation-list-left">
      <common-title :text="'已关联收费方案-停车场-车类型-车型'" />
      <div class="relation-list-left-wrapper">
        <el-table
          style="margin-top: 14px; width: 100%"
          v-loading="loading"
          :data="tableData"
          max-height="580px"
          highlight-current-row
          @row-click="rowClick"
        >
          <el-table-column
            label="序号"
            align="center"
            type="index"
            width="60"
          ></el-table-column>
          <el-table-column label="停车场名称" align="center" prop="parkLotName">
          </el-table-column>
          <el-table-column
            label="车类型名称"
            align="center"
            prop="vehicleCategoryName"
            width="200"
          >
          </el-table-column>
          <el-table-column
            label="车型名称"
            align="center"
            prop="vehicleTypeName"
            width="240"
          >
          </el-table-column>
        </el-table>
        <el-pagination
          style="text-align: right; margin-top: 20px"
          background
          v-show="pageInfo.total > 0"
          :total="pageInfo.total"
          :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <div class="relation-list-right">
      <common-title :text="'收费规则图例'" />
      <div class="charge-legend-wrapper">
        <div class="legend-bg">
          <img class="icon-park" src="@/assets/images/setting/icon-park.png" />
          <span class="icon-text">收费停车场</span>
          <div class="details" v-if="detailShow">
            <div
              class="detail"
              v-for="(duration, durationIndex) in detail.durationList"
              :key="durationIndex"
            >
              <div class="line">
                <span>免费时长</span>
                <el-input
                  :disabled="disabled"
                  type="text"
                  v-model="duration.freeMinute"
                />
                <span>分钟，</span><span>限额</span>
                <el-input
                  :disabled="disabled"
                  type="text"
                  v-model="duration.maximumCharge"
                />
                <span>元</span>
              </div>
              <div
                class="lines"
                v-for="(item, index) in duration.periodList"
                :key="index"
              >
                <div class="line">
                  <span class="lighhight">{{ index | formatTitle }}</span>
                  时段长度
                  <el-input
                    :disabled="disabled"
                    type="text"
                    v-model="item.lengthOfTime"
                  />
                  分钟
                </div>
                <div class="line">
                  费率
                  <el-input
                    :disabled="disabled"
                    type="text"
                    v-model="item.rate"
                  />
                  元/
                  <el-input
                    :disabled="disabled"
                    type="text"
                    v-model="item.minLenghtOfTime"
                  />
                  分钟
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import CommonTitle from "./commonTitle.vue";
import { relationVehicleList, ruleDetail } from "@/api/setting/index";
export default {
  name: "relationList",
  data() {
    return {
      maxHeight: 500,
      pageInfo: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      detailShow: false,
      detail: {
        freeMinuteNumber: "1",
        timeDivisionWay: "1",
        firstDurationChargeWay: "1",
      },
      loading: false,
      tableData: [],
    };
  },
  components: {
    CommonTitle,
  },
  filters: {
    formatTitle(index) {
      if (index === 0) {
        return "首时段：";
      }
      let number = (index + 1).toString();
      if (number.match(/\D/) || number.length >= 14) return;
      let zhArray = [
        "零",
        "一",
        "二",
        "三",
        "四",
        "五",
        "六",
        "七",
        "八",
        "九",
        "十",
      ]; // 数字对应中文
      let baseArray = [
        "",
        "十",
        "百",
        "千",
        "万",
        "十",
        "百",
        "千",
        "亿",
        "十",
        "百",
        "千",
        "万",
      ]; //进位填充字符，第一位是 个位，可省略
      let string = String(number)
        .split("")
        .reverse()
        .map((item, index) => {
          // 把数字切割成数组并倒序排列，然后进行遍历转成中文
          // 如果当前位为0，直接输出数字， 否则输出 数字 + 进位填充字符
          item =
            Number(item) == 0
              ? zhArray[Number(item)]
              : zhArray[Number(item)] + baseArray[index];
          return item;
        })
        .reverse()
        .join(""); // 倒叙回来数组，拼接成字符串
      string = string.replace(/^一十/, "十"); // 如果以 一十 开头，可省略一
      string = string.replace(/零+/, "零"); // 如果有多位相邻的零，只写一个即可
      return "第" + string + "时段：";
    },
  },
  methods: {
    getList() {
      this.loading = true;
      let obj = {
        pageNum: this.pageInfo.pageNum,
        pageSize: this.pageInfo.pageSize,
      };
      relationVehicleList(obj).then((res) => {
        if (res.code === 200) {
          this.tableData = res.rows;
          this.pageInfo.total = res.total;
          this.loading = false;
        }
      });
    },
    rowClick(row) {
      ruleDetail(row.ruleId).then((res) => {
        this.detail = res.data;
        if (this.detail.ceilingPriceMinute && this.detail.ceilingPrice) {
          this.$set(this.detail, "ceilingPriceShow", true);
        }
        this.detailShow = true;
        this.tableData.forEach((item) => {
          this.$set(item, "isEdit", false);
        });
        this.$set(row, "isEdit", true);
        this.disabled = column.label === "操作" ? false : true;
      });
    },
    handleCurrentChange(val) {
      this.pageInfo.pageNum = val
      this.getList()
    }
  },
  mounted() {
    this.getList();
    this.$nextTick(() => {
      this.maxHeight = this.$refs.list.clientHeight - 150
    })
  },
};
</script>

<style lang="scss" scoped>
.relation-list {
  display: flex;
  .relation-list-left {
    width: calc(100% - 395px);
    height: 640px;
  }
  .relation-list-right {
    width: 379px;
    margin-left: 16px;
    height: 640px;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    .charge-legend-wrapper {
      border: 1px solid #d7e5e9;
      border-top: none;
      flex: 1;
      display: flex;
      justify-content: center;
      .legend-bg {
        margin-top: 23px;
        width: 342px;
        height: 574px;
        background: linear-gradient(180deg, #008cd1 0%, #0948c5 100%);
        border-radius: 200px 200px 0px 0px;
        border: 6px solid #ffe497;
        display: flex;
        flex-direction: column;
        align-items: center;
        .icon-park {
          margin-top: 78px;
          width: 80px;
          height: 80px;
        }
        .icon-text {
          margin-top: 23px;
          font-size: 26px;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ffffff;
        }
        .details {
          height: 300px;
          padding: 0 10px;
          overflow: auto;
          .detail {
            width: 100%;
            .line {
              margin-top: 20px;
              width: 100%;
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #ffffff;
              display: flex;
              align-items: center;
              justify-content: center;
              .lighhight {
                font-size: 18px;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #ffe000;
              }
            }
          }
        }
      }
    }
  }
}

.relation-list-left ::v-deep .el-table__body tr,
.el-table__body td,
.el-table td.el-table__cell {
  padding: 0 !important;
  height: 36px !important;
  line-height: 36px !important;
}
.relation-list-left ::v-deep .el-table--medium .el-table__cell {
  padding: 0 !important;
}

::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 2px;
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  background: rgba(112, 173, 236, 0.2);
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  border-radius: 0;
  background: rgba(112, 173, 236, 0.1);
}
.details::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
.details::-webkit-scrollbar-thumb {
  border-radius: 2px;
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  background: rgba(112, 173, 236, 0.2);
}
.details::-webkit-scrollbar-track {
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  border-radius: 0;
  background: rgba(112, 173, 236, 0.1);
}

.legend-bg ::v-deep .el-input {
  width: 48px !important;
  height: 32px !important;
  line-height: 32px !important;
  margin: 0 6px !important;
}
.legend-bg ::v-deep .el-input--medium .el-input__inner {
  width: 48px !important;
  height: 32px !important;
  line-height: 32px !important;
  background: #ffffff;
  border-radius: 6px !important;
  padding: 0 6px !important;
}
</style>