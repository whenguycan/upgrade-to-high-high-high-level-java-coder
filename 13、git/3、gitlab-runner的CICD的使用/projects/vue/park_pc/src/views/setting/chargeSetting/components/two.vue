<template>
  <div class="two" ref="wrapper">
    <el-row type="flex" style="height: 100%">
      <el-col :lg="8" :xl="8">
        <div class="charge-legend">
          <common-title :text="'收费规则图例'" />
          <div class="charge-legend-wrapper" ref="left">
            <div class="legend-bg">
              <img
                class="icon-park"
                src="@/assets/images/setting/icon-park.png"
              />
              <span class="icon-text" style="margin-bottom: 10px"
                >收费停车场</span
              >
              <div class="details" v-if="detailShow">
                <div
                  class="detail"
                  v-for="(duration, durationIndex) in detail.durationList"
                  :key="durationIndex"
                >
                  <p class="qj">期间{{ parseInt(durationIndex) + 1 }}</p>
                  <div
                    class="line"
                    v-if="
                      !duration.startTime &&
                      (duration.freeMinute != 0 || duration.maximumCharge != 0)
                    "
                  >
                    <span v-if="duration.freeMinute != 0">免费时长</span>
                    <el-input
                      v-if="duration.freeMinute != 0"
                      :disabled="disabled"
                      type="text"
                      v-model="duration.freeMinute"
                    />
                    <span v-if="duration.maximumCharge != 0">分钟，</span
                    ><span v-if="duration.maximumCharge != 0">限额</span>
                    <el-input
                      v-if="duration.maximumCharge != 0"
                      :disabled="disabled"
                      type="text"
                      v-model="duration.maximumCharge"
                    />
                    <span v-if="duration.maximumCharge != 0">元</span>
                  </div>
                  <div
                    class="line time-line"
                    v-if="duration.startTime && detail.durationList.length > 1"
                  >
                    <el-time-picker
                      :disabled="disabled"
                      :clearable="false"
                      v-model="duration.startTime"
                      value-format="HH:mm"
                      format="HH:mm"
                    >
                    </el-time-picker>
                    -
                    <el-time-picker
                      v-if="durationIndex === detail.durationList.length - 1"
                      :disabled="disabled"
                      :clearable="false"
                      v-model="detail.durationList[0].startTime"
                      value-format="HH:mm"
                      format="HH:mm"
                    >
                    </el-time-picker>
                    <el-time-picker
                      v-else
                      :disabled="disabled"
                      :clearable="false"
                      v-model="detail.durationList[durationIndex + 1].startTime"
                      value-format="HH:mm"
                      format="HH:mm"
                    ></el-time-picker>
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
      </el-col>
      <el-col :lg="16" :xl="13">
        <div class="charge-wrapper">
          <div class="charge-wrapper-top">
            <common-title :text="'收费规则管理'" />
            <div class="charge-wrapper-margin">
              <div class="title-line">
                <span class="text">收费规则列表</span>
                <el-button icon="el-icon-plus" type="primary" @click="addRule"
                  >新增</el-button
                >
              </div>
              <el-table
                class="scrollBar"
                v-loading="loading"
                :data="tableData"
                max-height="332px"
                highlight-current-row
                @row-click="rowClick"
                :row-style="rowStyle"
              >
                <el-table-column
                  label="序号"
                  align="center"
                  type="index"
                  width="60"
                >
                </el-table-column>
                <el-table-column
                  label="收费方案规则名称"
                  align="center"
                  prop="ruleName"
                />
                <el-table-column
                  width="180"
                  fixed="right"
                  label="操作"
                  align="center"
                  class-name="small-padding fixed-width"
                >
                  <template slot-scope="scope">
                    <el-button type="text" @click="handleEdit(scope.row)"
                      >编辑</el-button
                    >
                    <el-button
                      type="text"
                      @click.native.stop="handleReset(scope.row)"
                      >重置</el-button
                    >
                    <el-button
                      type="text"
                      style="color: #ff1838"
                      @click.native.stop="handleDelete(scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                style="text-align: right; margin-top: 10px"
                background
                v-show="pageInfo.total > 10"
                :total="pageInfo.total"
                :page.sync="pageInfo.pageNum"
                :limit.sync="pageInfo.pageSize"
                @current-change="handleCurrentChange"
              />
              <el-form :model="detail" ref="form" label-width="124px">
                <div class="form-border form-border-two">
                  <el-row style="width: 100%">
                    <el-col :span="12">
                      <el-form-item>
                        <el-checkbox
                          :disabled="disabled"
                          v-model="detail.ceilingPriceShow"
                          >最高限价</el-checkbox
                        >
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item v-if="detail.ceilingPriceShow">
                        <el-input
                          style="margin-right: 6px"
                          :disabled="disabled"
                          v-model="detail.ceilingPriceMinute"
                        />分钟内限价<el-input
                          style="margin: 0 6px"
                          :disabled="disabled"
                          v-model="detail.ceilingPrice"
                        />元
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="form-border">
                  <el-row style="width: 100%">
                    <el-col :span="12">
                      <el-form-item>
                        <el-checkbox
                          :disabled="disabled"
                          @change="freeTimeChange"
                          :value="
                            detail.chargeContainFreeTime === 'Y' ? true : false
                          "
                          >算费是否包含免费时间</el-checkbox
                        >
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="免费时长次数：">
                        <el-select
                          :disabled="disabled"
                          v-model="detail.freeMinuteNumber"
                          style="width: 214px"
                        >
                          <el-option
                            v-for="item in dict.type
                              .park_rule_free_minute_number"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="form-border">
                  <el-row style="width: 100%">
                    <el-col :span="12">
                      <el-form-item label="计时分割方式：">
                        <el-select
                          :disabled="disabled"
                          v-model="detail.timeDivisionWay"
                          style="width: 214px"
                        >
                          <el-option
                            v-for="item in dict.type
                              .park_rule_time_division_way"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="首时段计费方式：">
                        <el-select
                          :disabled="disabled"
                          v-model="detail.firstDurationChargeWay"
                          style="width: 214px"
                        >
                          <el-option
                            v-for="item in dict.type
                              .park_rule_first_duration_charge_way"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="form-border">
                  <el-form-item label="计时舍入方式：">
                    <el-radio-group
                      :disabled="disabled"
                      v-model="detail.timeRoundWay"
                    >
                      <el-radio :label="'1'">全入</el-radio>
                      <el-radio :label="'2'">全舍</el-radio>
                      <el-radio :label="'3'">四舍五入</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </div>
                <el-button
                  class="queding"
                  type="primary"
                  :loading="submitLoading"
                  :disabled="disabled"
                  @click="handleConfirm"
                  >确定</el-button
                >
              </el-form>
            </div>
          </div>
          <div class="charge-wrapper-bottom">
            <div class="bottom-title">测试收费规则</div>
            <div class="bottom-time">
              <el-form
                ref="timeForm"
                :model="timeForm"
                label-width="84px"
                style="width: 100%"
              >
                <el-row style="width: 100%">
                  <el-col :span="10">
                    <el-form-item label="入场时间：">
                      <el-date-picker
                        v-model="timeForm.entryTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择入场时间"
                      >
                      </el-date-picker></el-form-item
                  ></el-col>
                  <el-col :span="10">
                    <el-form-item label="出场时间：">
                      <el-date-picker
                        v-model="timeForm.exitTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择出场时间"
                      >
                      </el-date-picker></el-form-item
                  ></el-col>
                  <el-col :span="4">
                    <el-button
                      class="jisuan"
                      type="primary"
                      @click="handleCalculate"
                      >计算</el-button
                    >
                  </el-col>
                </el-row>
              </el-form>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CommonTitle from "./commonTitle.vue";
import {
  ruleList,
  ruleDetail,
  ruleEdit,
  ruleDelete,
  ruleTest,
} from "@/api/setting/index";
import moment from "moment";
export default {
  name: "two",
  dicts: [
    "park_rule_free_minute_number",
    "park_rule_time_division_way",
    "park_rule_first_duration_charge_way",
  ],
  data() {
    return {
      clientHeight: 500,
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
      form: {},
      timeForm: {
        entryTime: "",
        exitTime: "",
      },
      disabled: true,
      submitLoading: false,
    };
  },
  props: {
    parkNo: {
      type: [Number, String],
    },
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
      ruleList(obj)
        .then((res) => {
          if (res.code === 200) {
            this.tableData = res.rows;
            this.pageInfo.total = res.total;
            // this.pageInfo.total = res.data.total;
            this.loading = false;
          }
        })
        .catch((errMsg) => {
          this.$message.error(errMsg);
          this.loading = false;
        });
    },
    handleCurrentChange(val) {
      this.pageInfo.pageNum = val;
      this.getList();
    },
    timeChange(index, val) {
      console.log(index, val);
    },
    addRule() {
      localStorage.removeItem("ruleName");
      this.$emit("addTab", "新增收费方案");
    },
    handleEdit(row) {
      this.disabled = false;
    },
    handleReset(row) {
      let obj = {
        name: row.ruleName,
        id: row.id,
      };
      localStorage.setItem("ruleName", JSON.stringify(obj));
      this.$emit("addTab", "新增收费方案");
    },
    handleDelete(row) {
      this.$confirm("", {
        title: "删除提示",
        message: "确定删除这条规则？",
        iconClass: "el-icon-warning colorYellow",
      })
        .then(() => {
          ruleDelete(row.id).then((res) => {
            if (res.code === 200) {
              this.$message.success(`规则${row.ruleName}删除成功！`);
              this.disabled = true;
              this.getList();
              this.detail = {
                freeMinuteNumber: "1",
                timeDivisionWay: "1",
                firstDurationChargeWay: "1",
              };
            }
          });
        })
        .catch(() => {
          // console.log('no')
        });
    },
    rowClick(row, column, event) {
      console.log(column);
      ruleDetail(row.id).then((res) => {
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
    rowStyle() {
      return {
        height: "36px",
      };
    },
    freeTimeChange(val) {
      if (val) {
        this.$set(this.detail, "chargeContainFreeTime", "Y");
      } else {
        this.$set(this.detail, "chargeContainFreeTime", "N");
      }
    },
    handleConfirm() {
      this.submitLoading = true;
      if (!this.detail.ceilingPriceShow) {
        this.detail.ceilingPriceMinute = "";
        this.detail.ceilingPrice = "";
      }
      console.log(this.detail);
      let durationCreateWay = parseInt(this.detail.durationCreateWay);
      let durationList = this.detail.durationList;
      if (durationCreateWay === 2) {
        for (var i = 0; i < durationList.length; i++) {
          let item = durationList[i];
          let lengthOfTime = parseInt(item.lengthOfTime);
          let periodList = item.periodList;
          let periodListLen = periodList.length;
          if (periodListLen > 1) {
            let total = 0;
            for (var j = 0; j < periodList.length; j++) {
              let newItem = periodList[j];
              if (j < periodList.length - 1 && periodList.length > 1) {
                if (parseInt(newItem.lengthOfTime) === 0) {
                  this.$message.error(
                    `期间${i + 1} - 第${newItem.sort}时段时长不能为0！`
                  );
                  this.submitLoading = false;
                  return false;
                }
              }
              total += parseInt(newItem.lengthOfTime);
              if (total > lengthOfTime) {
                let shouldTime =
                  parseInt(newItem["lengthOfTime"]) - (total - lengthOfTime);
                this.$message.error(
                  `期间${i + 1} - 第${
                    newItem.sort
                  }时段时长不能超过${shouldTime}分钟！`
                );
                this.submitLoading = false;
                return false;
              }
            }
          }
        }
      }
      ruleEdit(this.detail)
        .then((res) => {
          if (res.code === 200) {
            this.$message.success(`规则${this.detail.ruleName}修改成功！`);
            this.disabled = true;
            this.submitLoading = false;
          }
        })
        .catch((errMsg) => {
          this.submitLoading = false;
        });
    },
    handleCalculate() {
      if (!this.detail.id) {
        this.$message.error("请先选择规则！");
        return false;
      }
      let obj = { ...this.timeForm, id: this.detail.id };
      ruleTest(obj).then((res) => {
        let message = "金额：" + res.data;
        this.$confirm(message, "提示", {
          confirmButtonText: "确定",
          showCancelButton: false,
          type: "success",
        });
      });
    },
  },
  mounted() {
    this.timeForm.entryTime = moment(
      new Date(new Date().setHours(0, 0, 0, 0) + 9 * 60 * 60 * 1000)
    ).format("YYYY-MM-DD hh:ss:mm");
    this.timeForm.exitTime = moment(
      new Date(new Date().setHours(0, 0, 0, 0) + 12 * 60 * 60 * 1000)
    ).format("YYYY-MM-DD hh:ss:mm");
    this.getList();
  },
};
</script>

<style lang="scss" scoped>
.two {
  // display: flex;
  width: 100%;
  //   height: 100%;
  .charge-legend {
    width: 100%;
    height: 100%;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    .charge-legend-wrapper {
      padding: 20px;
      height: 100%;
      border: 1px solid #d7e5e9;
      border-top: none;
      padding-bottom: 20px;
      flex: 1;
      display: flex;
      justify-content: center;
      .legend-bg {
        // margin-top: 23px;
        max-width: 450px;
        width: 100%;
        height: 100%;
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
          height: calc(100% - 300px);
          width: calc(100% - 20px);
          overflow: auto;
          .detail {
            width: 100%;
            .qj {
              color: #ff445e;
              text-align: center;
              font-size: 24px;
              font-weight: bold;
              margin: 0 0 10px 0;
            }
            .line {
              margin-bottom: 20px;
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
  .charge-wrapper {
    margin-left: 16px;
    width: calc(100% - 16px);
    background: #ffffff;
    // display: flex;
    // flex-direction: column;
    // justify-content: space-between;
    .charge-wrapper-margin {
      padding: 0 16px 20px;
      border-left: 1px solid #d7e5e9;
      border-right: 1px solid #d7e5e9;
      // width: 100%;
      .title-line {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin: 5px 0;
        .text {
          font-size: 14px;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #001d3c;
        }
      }
      .form-border {
        margin-top: 5px;
        width: 100%;
        height: 42px;
        background: rgba(239, 248, 251, 0.5);
        border: 1px dashed #d7e5e9;
        display: flex;
        align-items: center;
        // padding-left: 20px;
      }
      //   .form-border-two {
      //     display: flex;
      //     flex-direction: column;
      //   }
      .queding {
        margin-top: 16px;
        position: relative;
        left: 50%;
        transform: translateX(-50%);
      }
    }
    .charge-wrapper-bottom {
      flex-shrink: 0;
      width: 100%;
      height: 87px;
      background: #fff6f6;
      border: 1px dashed #ffc8c8;
      display: flex;
      flex-direction: column;
      .bottom-title {
        padding-left: 16px;
        height: 36px;
        line-height: 36px;
        background: #fbefef;
        font-size: 14px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ff1838;
        border-bottom: 1px dashed #ffc8c8;
      }
      .bottom-time {
        width: 100%;
        flex: 1;
        display: flex;
        align-items: center;
        .jisuan {
          margin-right: 16px;
          width: 88px;
          height: 32px;
          background: #ff445e;
          border-radius: 6px;
          border: 1px solid rgba(0, 0, 0, 0);
        }
      }
    }
  }
}
.legend-bg ::v-deep .el-input {
  width: 48px !important;
  height: 28px !important;
  line-height: 28px !important;
  margin: 0 6px !important;
}
.legend-bg ::v-deep .el-input--medium .el-input__inner {
  width: 48px !important;
  height: 28px !important;
  line-height: 28px !important;
  background: #ffffff;
  border-radius: 6px !important;
  padding: 0 6px !important;
}
.form-border ::v-deep .el-form-item {
  margin-bottom: 0 !important;
}
.form-border ::v-deep .el-form-item--medium .el-form-item__label {
  color: rgba(0, 29, 60, 0.85) !important;
  font-size: 14px !important;
  font-weight: normal !important;
  line-height: 28px !important;
}
.form-border ::v-deep .el-checkbox__label {
  color: rgba(0, 29, 60, 0.85) !important;
  font-size: 14px !important;
  font-weight: normal !important;
}
.form-border ::v-deep .el-input--medium .el-input__inner {
  height: 28px !important;
  line-height: 28px !important;
}
.form-border ::v-deep .el-form-item--medium .el-form-item__content {
  height: 28px !important;
  line-height: 28px !important;
}
.form-border ::v-deep .el-input--medium .el-input__icon {
  height: 28px !important;
  line-height: 28px !important;
}
.bottom-time ::v-deep .el-form-item__label {
  padding: 0 !important;
}
.bottom-time ::v-deep .el-form-item {
  margin-bottom: 0 !important;
}
.bottom-time ::v-deep .el-form-item--medium .el-form-item__label {
  font-size: 14px !important;
  font-weight: 400 !important;
  color: rgba(0, 29, 60, 0.85) !important;
}
.bottom-time ::v-deep .el-col {
  display: flex;
  justify-content: flex-end;
}
.two ::v-deep .el-table__body tr,
.el-table__body td,
.el-table td.el-table__cell {
  padding: 0 !important;
  height: 36px !important;
  line-height: 36px !important;
}
.two ::v-deep .el-table--medium .el-table__cell {
  padding: 0 !important;
}
.form-border.form-border-two ::v-deep .el-input {
  width: 60px !important;
  height: 28px !important;
  line-height: 28px !important;
  // margin: 0 !important;
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
.title-line ::v-deep .el-button--medium {
  padding: 8px 20px !important;
}
.legend-bg .time-line ::v-deep .el-input {
  width: 100px !important;
}
.legend-bg .time-line ::v-deep .el-input--medium .el-input__inner {
  width: 100px !important;
  padding: 0 20px 0 30px !important;
}
.legend-bg .time-line ::v-deep .el-input--medium .el-input__icon {
  line-height: 28px !important;
}
</style>