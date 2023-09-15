<template>
  <div class="add-rule">
    <common-title :text="'收费规则流程'" />
    <div class="add-rule-wrapper">
      <el-steps
        :active="activeIndex"
        finish-status="success"
        process-status="process"
      >
        <el-step
          @click.native="handleClick(index)"
          v-for="(item, index) in steps"
          :key="item.title"
          :title="item.title"
        ></el-step>
      </el-steps>
      <el-form
        style="margin-top: 78px"
        ref="form"
        :model="form"
        label-width="130px"
      >
        <el-row v-if="activeIndex === 0">
          <el-col class="row-border" :span="14" :offset="5">
            <el-form-item label="收费规则名称：">
              <el-input
                :disabled="activeIndex === nowStep ? false : true"
                v-model="form.ruleName"
                placeholder="请输入收费规则名称"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 1">
          <el-col class="row-border" :span="14" :offset="5">
            <el-form-item label="期间生成方式：">
              <el-radio-group
                :disabled="activeIndex === nowStep ? false : true"
                v-model="form.durationCreateWay"
                @change="changeDurationCreateWay"
              >
                <el-radio :label="1">按时刻计时</el-radio>
                <el-radio :label="2">按时长计时</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="期间个数：">
              <el-input
                :disabled="activeIndex === nowStep ? false : true"
                style="width: 100px"
                type="number"
                v-model="form.durationNum"
                @input="changeDurationNum"
                min="1"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 2">
          <el-col :span="18" :offset="3">
            <el-table
              key="one"
              border
              :data="form.durationList"
              max-height="320px"
            >
              <el-table-column
                label="序号"
                align="center"
                type="index"
                width="60"
              >
              </el-table-column>
              <el-table-column
                label="名称"
                width="200"
                align="center"
                prop="name"
              >
              </el-table-column>
              <el-table-column
                :label="
                  form.durationCreateWay == 1 ? '开始时刻(分钟)' : '时长(分钟)'
                "
                align="center"
              >
                <template slot-scope="scope">
                  <el-time-picker
                    style="width: 100%"
                    v-if="form.durationCreateWay == 1"
                    v-model="scope.row.startTime"
                    placeholder="选择开始时刻"
                    value-format="HH:mm"
                    format="HH:mm"
                  >
                  </el-time-picker>
                  <el-input
                    v-else
                    type="number"
                    v-model="scope.row.lengthOfTime"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="免费时长" width="180" align="center">
                <template slot-scope="scope">
                  <el-input
                    type="number"
                    v-model="scope.row.freeMinute"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="最高收费" width="180" align="center">
                <template slot-scope="scope">
                  <el-input
                    type="number"
                    v-model="scope.row.maximumCharge"
                  ></el-input>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 3">
          <el-col :span="18" :offset="3">
            <el-table
              ref="table"
              :key="tableShow"
              border
              :data="form.durationList"
              max-height="320px"
              row-key="id"
              default-expand-all
              :tree-props="{
                children: 'children',
              }"
            >
              <el-table-column
                label="名称"
                align="center"
                prop="name"
                width="110"
              >
              </el-table-column>
              <el-table-column label="时长(分钟)" width="200" align="center">
                <template slot-scope="scope">
                  <el-input
                    v-if="scope.row.isChildren"
                    type="number"
                    v-model="tableData[scope.$index].lengthOfTime"
                  ></el-input>
                  <span v-else>{{ scope.row.lengthOfTime }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="最小单位时长(分钟)"
                width="200"
                align="center"
              >
                <template slot-scope="scope">
                  <el-input
                    type="number"
                    v-if="scope.row.isChildren"
                    v-model="tableData[scope.$index].minLenghtOfTime"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="费率(元)" width="200" align="center">
                <template slot-scope="scope">
                  <el-input
                    type="number"
                    v-if="scope.row.isChildren"
                    v-model="tableData[scope.$index].rate"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column
                width="160"
                fixed="right"
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    v-if="!scope.row.isChildren"
                    type="text"
                    @click="addNewTime(scope.row)"
                    >新增时段</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 4">
          <el-col class="row-border" :span="14" :offset="5">
            <el-row style="width: 100%">
              <el-col :span="6">
                <el-form-item>
                  <el-checkbox
                    :disabled="activeIndex === nowStep ? false : true"
                    v-model="form.ceilingPriceShow"
                    >最高限价</el-checkbox
                  >
                </el-form-item>
              </el-col>
              <el-col class="form-line" :span="18">
                <el-form-item v-if="form.ceilingPriceShow">
                  <el-input
                    :disabled="activeIndex === nowStep ? false : true"
                    v-model="form.ceilingPriceMinute"
                  />分钟内限价<el-input v-model="form.ceilingPrice" />元
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 5">
          <el-col class="row-border" :span="14" :offset="5">
            <el-row style="width: 100%">
              <el-col :span="12">
                <el-form-item>
                  <el-checkbox
                    @change="freeTimeChange"
                    :value="form.chargeContainFreeTime === 'Y' ? true : false"
                    >算费是否包含免费时间</el-checkbox
                  >
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="免费时长次数：">
                  <el-select
                    :disabled="activeIndex === nowStep ? false : true"
                    v-model="form.freeMinuteNumber"
                  >
                    <el-option
                      v-for="item in dict.type.park_rule_free_minute_number"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="margin-top: 10px">
              <el-col :span="12">
                <el-form-item label="计时分割方式：">
                  <el-select
                    :disabled="activeIndex === nowStep ? false : true"
                    v-model="form.timeDivisionWay"
                  >
                    <el-option
                      v-for="item in dict.type.park_rule_time_division_way"
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
                    :disabled="activeIndex === nowStep ? false : true"
                    v-model="form.firstDurationChargeWay"
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
          </el-col>
        </el-row>
        <el-row v-if="activeIndex === 6">
          <el-col class="row-border" :span="14" :offset="5">
            <el-form-item label="计时舍入方式：">
              <el-radio-group
                :disabled="activeIndex === nowStep ? false : true"
                v-model="form.timeRoundWay"
              >
                <el-radio :label="1">全入</el-radio>
                <el-radio :label="2">全舍</el-radio>
                <el-radio :label="3">四舍五入</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row
          :style="{ marginTop: nowStep == 2 || nowStep == 3 ? '25px' : '40px' }"
        >
          <el-col
            :span="12"
            :offset="6"
            style="display: flex; justify-content: center"
          >
            <el-button
              style="width: 108px"
              type="info"
              v-if="activeIndex === 6"
              @click="closeTab"
              >取消</el-button
            >
            <el-button
              style="width: 108px; margin-left: 18px"
              type="primary"
              v-if="activeIndex === 6"
              @click="handleConfirm"
              >确定</el-button
            >
            <el-button
              v-if="nowStep === activeIndex && nowStep < 6"
              type="primary"
              @click="handleNext"
              >下一步</el-button
            >
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
import moment from "moment";
import CommonTitle from "./commonTitle.vue";
import { ruleAdd, ruleReset } from "@/api/setting/index";
export default {
  name: "addRule",
  dicts: [
    "park_rule_free_minute_number",
    "park_rule_time_division_way",
    "park_rule_first_duration_charge_way",
  ],
  data() {
    return {
      activeIndex: 0,
      nowStep: 0,
      steps: [
        {
          title: "收费规则名称",
        },
        {
          title: "期间生成方式",
        },
        {
          title: "收费期间设置",
        },
        {
          title: "收费时段设置",
        },
        {
          title: "最高限价",
        },
        {
          title: "算费",
        },
        {
          title: "舍入方式",
        },
      ],
      form: {
        ruleName: localStorage.getItem("ruleName")
          ? JSON.parse(localStorage.getItem("ruleName")).name
          : "",
        durationCreateWay: 1,
        durationNum: 1,
        freeMinuteNumber: "1",
        timeDivisionWay: "1",
        firstDurationChargeWay: "1",
        timeRoundWay: 1,
      },
      tableData: [],
      tableShow: true,
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
  methods: {
    handleClick(index) {
      console.log(index);
      if (index > this.nowStep) {
        this.$message.warning("请按照顺序填写！");
        return false;
      }
      this.activeIndex = index;
    },
    changeDurationCreateWay(val) {
      this.form.durationNum = 1;
    },
    changeDurationNum(val) {
      if (this.form.durationCreateWay === 2) {
        if (val > 1) {
          this.$message.error("期间个数不能超过1个！");
          this.$set(this.form, "durationNum", 1);
        }
      }
    },
    freeTimeChange(val) {
      if (val) {
        this.$set(this.form, "chargeContainFreeTime", "Y");
      } else {
        this.$set(this.form, "chargeContainFreeTime", "N");
      }
    },
    addNewTime(row) {
      let index = this.form.durationList.findIndex((item) => {
        return item.id === row.id;
      });
      let tableIndex = 0;
      for (var i = 0; i < index + 1; i++) {
        let child = this.form.durationList[i].children;
        let childLen = child.length;
        let total = parseInt(childLen) + 1;
        tableIndex += total;
      }
      let children = row.children;
      let len = children.length;
      let obj = {
        id: 100 * row.id + len + 1,
        name: `第${len + 1}时段`,
        lengthOfTime: "0",
        minLenghtOfTime: "60",
        rate: "1",
        isChildren: true,
      };
      children.push(obj);
      this.tableData.splice(tableIndex, 0, obj);
      this.tableShow = !this.tableShow;
      console.log(this.tableData);
    },
    childrenChange() {
      this.tableShow = !this.tableShow;
    },
    handleConfirm() {
      let durationList = this.form.durationList;
      durationList.forEach((item, index) => {
        this.$set(item, "sort", index + 1);
        item.periodList = item.children;
        item.periodList.forEach((period, periodIndex) => {
          this.$set(period, "sort", periodIndex + 1);
        });
        delete item.children;
      });
      console.log(durationList);
      let obj = { ...this.form };
      if (localStorage.getItem("ruleName")) {
        obj.id = JSON.parse(localStorage.getItem("ruleName")).id;
        ruleReset(obj).then((res) => {
          if (res.code === 200) {
            localStorage.removeItem("ruleName");
            this.$message.success("规则重置成功！");
            this.$emit("addSuccess", "新增收费方案");
          }
        });
      } else {
        ruleAdd(obj).then((res) => {
          if (res.code === 200) {
            this.$message.success("规则新建成功！");
            this.$emit("addSuccess", "新增收费方案");
          }
        });
      }
    },
    closeTab() {
      this.$emit("closeTab", "新增收费方案", "1");
    },
    handleNext() {
      if (this.activeIndex === 0) {
        if (!this.form.ruleName) {
          this.$message.error("请填写规则名称！");
          return false;
        }
      }
      if (this.activeIndex === 1) {
        if (!this.form.durationCreateWay) {
          this.$message.error("请选择计费舍入方式！");
          return false;
        }
        if (!this.form.durationNum) {
          this.$message.error("请输入期间个数！");
          return false;
        }
        for (var i = 0; i < this.form.durationNum; i++) {
          if (!this.form.durationList) {
            this.form.durationList = [];
          }
          if (this.form.durationCreateWay == 1) {
            this.form.durationList.push({
              id: i + 1,
              name: `期间${i + 1}`,
              startTime: "00:00",
              freeMinute: 0,
              maximumCharge: 0,
            });
          } else {
            this.form.durationList.push({
              id: i + 1,
              name: `期间${i + 1}`,
              lengthOfTime: 1,
              freeMinute: 0,
              maximumCharge: 0,
            });
          }
        }
      }
      if (this.activeIndex === 2) {
        for (var i = 0; i < this.form.durationList.length; i++) {
          let item = this.form.durationList[i];
          for (var j in item) {
            if (item[j] === "") {
              this.$message.error("请将表格填写完整！");
              return false;
            }
          }
          if (this.form.durationCreateWay == 1) {
            if (
              i < this.form.durationList.length - 1 &&
              this.form.durationList.length > 1
            ) {
              let result = this.compareStrTime(
                item.startTime,
                this.form.durationList[i + 1].startTime
              );
              if (!result.flag) {
                this.$message.error(result.message);
                return false;
              }
            }
          } else {
            if (parseInt(item.lengthOfTime) === 0) {
              this.$message.error("期间时长不能为0！");
              return false;
            }
          }
        }
        this.form.durationList.forEach((table, tableIndex) => {
          this.tableData.push(table);
          table.children = [];
          let obj = {
            id: 100 * (tableIndex + 1) + tableIndex,
            name: "首时段",
            lengthOfTime: "0",
            minLenghtOfTime: "60",
            rate: "1",
            isChildren: true,
          };
          table.children.push(obj);
          this.tableData.push(obj);
        });
      }
      if (this.activeIndex === 3) {
        let durationList = this.form.durationList;
        for (var i = 0; i < durationList.length; i++) {
          let total = 0;
          let item = durationList[i];
          let lengthOfTime = parseInt(item.lengthOfTime);
          let periodList = item.children;
          for (var j = 0; j < periodList.length; j++) {
            let newItem = periodList[j];
            for (var k in newItem) {
              if (newItem[k] === "") {
                this.$message.error("请将表格填写完整！");
                return false;
              }
            }
            if (
              periodList.length > 1 &&
              j < periodList.length - 1 &&
              this.form.durationCreateWay == 2
            ) {
              if (parseInt(newItem.lengthOfTime) === 0) {
                this.$message.error(
                  `${item.name} - ${newItem.name}时长不能为0！`
                );
                return false;
              }
              if (lengthOfTime) {
                total += parseInt(newItem["lengthOfTime"]);
              }
              if (total > lengthOfTime) {
                let shouldTime =
                  parseInt(newItem["lengthOfTime"]) - (total - lengthOfTime);
                this.$message.error(
                  `${item.name} - ${newItem.name}时长不能超过${shouldTime}分钟！`
                );
                return false;
              }
            }
            if (this.form.durationCreateWay == 1) {
              if (periodList.length > 1 && j < periodList.length - 1) {
                if (parseInt(newItem.lengthOfTime) === 0) {
                  this.$message.error(
                    `${item.name} - ${newItem.name}时长不能为0！`
                  );
                  return false;
                }
              }
            }
          }
        }
      }
      if (this.activeIndex === 4) {
        if (this.form.ceilingPriceShow) {
          if (!this.form.ceilingPriceMinute) {
            this.$message.error("请填写最高限价分钟！");
            return false;
          }
          if (
            Math.round(this.form.ceilingPriceMinute) !==
            parseFloat(this.form.ceilingPriceMinute)
          ) {
            this.$message.error("最高限价分钟只能为整数！");
            return false;
          }
          if (!this.form.ceilingPrice) {
            this.$message.error("请填写最高限价！");
            return false;
          }
        } else {
          this.$set(this.form, "ceilingPriceMinute", "");
          this.$set(this.form, "ceilingPrice", "");
        }
      }
      if (this.activeIndex === 5) {
        if (!this.form.chargeContainFreeTime) {
          this.$set(this.form, "chargeContainFreeTime", "N");
        }
        if (!this.form.freeMinuteNumber) {
          this.$message.error("请选择免费时长次数！");
          return false;
        }
        if (!this.form.timeDivisionWay) {
          this.$message.error("请选择计时分隔方式！");
          return false;
        }
        if (!this.form.firstDurationChargeWay) {
          this.$message.error("请选择首时段计费方式！");
          return false;
        }
      }
      this.nowStep++;
      this.activeIndex++;
    },
    compareStrTime(timeStart, timeEnd) {
      let result = {};
      let time = moment(new Date()).format("yyyy-MM-DD ");
      var dateA = new Date(time + timeStart);
      var dateB = new Date(time + timeEnd);
      console.log(dateA, dateB);
      if (isNaN(dateA) || isNaN(dateB)) return null;
      if (dateA > dateB) {
        result = {
          flag: false,
          message: "期间开始时刻需要按照从小到大的顺序填写",
        };
      } else if (dateA < dateB) {
        result = {
          flag: true,
        };
      } else {
        result = {
          flag: false,
          message: "期间开始时刻不能相等",
        };
      }
      return result;
    },
  },
};
</script>

<style lang="scss" scoped>
.add-rule {
  width: 100%;
  height: calc(100% - 40px);
  .add-rule-wrapper {
    width: 100%;
    height: 100%;
    border: 1px solid #d7e5e9;
    padding: 22px 22px 0;
    .row-border {
      display: flex;
      flex-direction: column;
      //   align-items: center;
      justify-content: center;
      background: rgba(239, 248, 251, 0.5);
      border: 1px dashed #d7e5e9;
      padding: 12px 10px 12px 0;
    }
  }
}
.add-rule ::v-deep .el-steps--horizontal {
  width: 90% !important;
}
.add-rule ::v-deep .el-step__title {
  font-size: 16px !important;
  font-weight: 400 !important;
  color: rgba(0, 29, 60, 0.7) !important;
  position: absolute !important;
  top: -4px !important;
  left: 37px !important;
  background: #ffffff !important;
  padding-right: 5px !important;
  white-space: nowrap !important;
}
.add-rule ::v-deep .el-step.is-horizontal .el-step__line {
  left: 60px;
  //   right: 30px;
  right: 5px;
}
.add-rule ::v-deep .el-step__title.is-finish {
  color: rgba(0, 0, 0, 0.88) !important;
}
.add-rule ::v-deep .el-step__icon.is-text {
  width: 32px !important;
  height: 32px !important;
  background: #dbe8ed !important;
  border-radius: 32px !important;
  border: none !important;
}
.add-rule ::v-deep .is-success .el-step__icon.is-text {
  background: rgba(73, 200, 234, 0.15) !important;
}
.add-rule ::v-deep .is-process .el-step__icon.is-text {
  background: #49c8ea !important;
}
.add-rule ::v-deep .el-step__icon-inner {
  font-size: 14px !important;
  font-weight: 400 !important;
  color: rgba(0, 29, 60, 0.6) !important;
}
.add-rule ::v-deep .is-success .el-step__icon-inner {
  color: #00acda !important;
}
.add-rule ::v-deep .is-process .el-step__icon-inner {
  color: #ffffff !important;
}
.add-rule ::v-deep .el-step__head.is-success {
  border-color: #c0c4cc !important;
}
.add-rule ::v-deep .el-input--medium .el-input__inner {
  height: 32px !important;
  line-height: 32px !important;
}
.add-rule ::v-deep .el-form-item--medium .el-form-item__content {
  height: 32px !important;
  line-height: 32px !important;
}
.add-rule ::v-deep .el-form-item {
  margin-bottom: 0 !important;
}
.add-rule ::v-deep .el-form-item--medium .el-form-item__label {
  font-size: 14px !important;
  font-weight: 400 !important;
  color: rgba(0, 29, 60, 0.85) !important;
}
.form-line ::v-deep .el-input {
  width: 88px !important;
  height: 32px !important;
  line-height: 32px !important;
  margin: 0 6px !important;
}
.add-rule ::v-deep .el-input--medium .el-input__icon {
  line-height: 32px !important;
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
</style>