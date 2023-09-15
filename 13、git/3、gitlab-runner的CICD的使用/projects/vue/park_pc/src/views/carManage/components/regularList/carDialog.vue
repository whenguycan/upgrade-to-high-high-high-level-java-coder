<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 09:58:40
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 09:54:32
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/regularList/carDialog.vue
-->

<template>
  <div class="addCarInfoDialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogOpen"
      :close-on-click-modal="false"
      width="800px"
      append-to-body
      class="addCarInfoDialog"
      @close="cancel"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <titleTab
          title="车牌信息"
          :style="{ margin: '0 0 20px 0 ' }"
        ></titleTab>
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌" prop="carNumber">
              <el-input
                v-model="form.carNumber"
                placeholder="请输入车牌"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" prop="carColor">
            <el-form-item label="车辆颜色">
              <el-input v-model="form.carColor" placeholder="请输入车辆颜色" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="carRemark">
              <el-input
                v-model="form.carRemark"
                type="textarea"
                :autosize="{ minRows: 2 }"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <titleTab
          title="车主信息"
          :style="{ margin: '0 0 20px 0 ' }"
        ></titleTab>
        <el-row>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="ownerName">
              <el-input v-model="form.ownerName" placeholder="请输入车主姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="ownerPhone">
              <el-input
                v-model="form.ownerPhone"
                placeholder="请输入联系方式"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="ownerCardId">
              <el-input
                v-model="form.ownerCardId"
                placeholder="请输入身份证号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主地址" prop="ownerAddress">
              <el-input
                v-model="form.ownerAddress"
                placeholder="请输入车主地址"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" class="flowPlaceNumber">
            <el-form-item label="流动车位" prop="flowPlaceNumber">
              <el-input-number
                v-model="form.flowPlaceNumber"
                controls-position="right"
                :min="0"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="有效期" prop="time">
              <el-date-picker
                v-model="form.time"
                type="daterange"
                :disabled="form.timeLimit"
                range-separator="至"
                start-placeholder="开始"
                end-placeholder="结束"
              >
              </el-date-picker>
              <el-checkbox
                v-model="form.timeLimit"
                @change="changeTimeLimit"
                class="timeLimit"
              >
                永久有效期</el-checkbox
              >
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :autosize="{ minRows: 2 }"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import {
  addRegularCar,
  editRegularCar,
  getRegularCarDetail,
} from "../../api/index";
export default {
  name: "AddCarInfoDialog",
  components: {
    titleTab,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    detail: {
      type: Object,
    },
    open: {
      type: Boolean,
      default: false,
    },
    nowType: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  watch() {},
  data() {
    return {
      format: "YYYY-MM-DD",
      dialogOpen: this.open,
      rules: {
        carNumber: [
          { required: true, message: "车牌不能为空", trigger: "blur" },
        ],
        ownerName: [
          { required: true, message: "车主姓名不能为空", trigger: "blur" },
        ],
        // ownerPhone: [
        //   { required: true, message: "手机号码不能为空", trigger: "blur" },
        //   {
        //     pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        //     message: "请输入正确的手机号码",
        //     trigger: "blur",
        //   },
        // ],
      },
      form: {
        carNumber: "",
        carColor: "",
        carRemark: "",
        ownerName: "",
        ownerPhone: "",
        ownerCardId: "",
        ownerAddress: "",
        flowPlaceNumber: 1,
        time: null,
        timeLimit: null,
        remark: "",
      },
    };
  },
  computed: {},
  watch: {},
  created() {
    if (this.detail.id) {
      this.getformData();
    }
  },
  mounted() {},
  methods: {
    // 新增、编辑 请求接口数据处理
    handlefromDate() {
      const {
        carNumber,
        carColor,
        carRemark,
        ownerName,
        ownerPhone,
        ownerCardId,
        ownerAddress,
        flowPlaceNumber,
        time,
        timeLimit,
        remark,
        id,
      } = this.form;
      const params = {
        carNumber,
        carColor,
        carRemark,
        ownerName,
        ownerPhone,
        ownerCardId,
        ownerAddress,
        flowPlaceNumber,
        timeLimit: timeLimit ? 1 : 0, // 永久有效时间 '0''-临时 ''1''-永久'
        remark,
      };
      const format = this.format;
      if (time) {
        const startTime = time[0];
        const endTime = time[1];
        const _startTime = startTime
          ? this.$moment(startTime).format(format)
          : "";
        const _endTime = endTime ? this.$moment(endTime).format(format) : "";
        params.startTime = _startTime;
        params.endTime = _endTime;
      }

      if (id != undefined) {
        params.id = id;
      }
      if (this.nowType.id) {
        params.carCategoryId = this.nowType.id;
      }
      return params;
    },
    // 详情 回填 数据处理
    getformData() {
      if (this.detail.id) {
        const {
          carNumber,
          carColor,
          carRemark,
          ownerName,
          ownerPhone,
          ownerCardId,
          ownerAddress,
          flowPlaceNumber,
          endTime,
          startTime,
          timeLimit,
          remark,
          id,
        } = this.detail;
        const _timeLimit = +timeLimit === 1 ? true : false;
        this.form = {
          carNumber,
          carColor,
          carRemark,
          ownerName,
          ownerPhone,
          ownerCardId,
          ownerAddress,
          flowPlaceNumber,
          timeLimit: _timeLimit,
          remark,
        };
        if (id != undefined) {
          this.form.id = id;
        }
        if (endTime && startTime) {
          const _time = [startTime, endTime];
          this.form.time = _time;
        }
      }
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.dialogOpen = false;
      this.$emit("cancel", false);
    },
    // 表单重置
    reset() {
      this.form = {
        carNumber: "",
        carColor: "",
        carRemark: "",
        ownerName: "",
        ownerPhone: "",
        ownerCardId: "",
        ownerAddress: "",
        flowPlaceNumber: 1,
        time: null,
        timeLimit: null,
        remark: "",
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          const parmas = this.handlefromDate();
          if (parmas.id != undefined) {
            editRegularCar(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.$emit("success", false);
              } else {
                this.$modal.msgError("修改失败");
                this.$emit("cancel", false);
              }
            });
          } else {
            addRegularCar(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.$emit("success", false);
              } else {
                this.$modal.msgError("新增失败");
                this.$emit("success", false);
              }
            });
          }
        }
      });
    },

    changeTimeLimit(v) {
      const _v = v;
      const { time } = this.form;
      if (v) {
        if (time) {
          const start = time[0];
          const end = time[1];
          const _end = this.$moment(end).add(100, "years");
          this.form.time = [start, _end];
          console.log("adela editor", start, end);
        } else {
          const start = this.$moment(new Date());
          const _end = this.$moment(start).add(100, "years");
          this.form.time = [start, _end];
        }
      } else {
        if (time) {
          const start = time[0];
          const end = time[1];
          const _end = this.$moment(end).subtract(100, "years");
          this.form.time = [start, _end];
          console.log("adela editor", start, end);
        }
      }
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.addCarInfoDialog {
  ::v-deep .el-dialog__body {
    padding: 20px 40px 20px 20px;
  }
  .flowPlaceNumber .el-input-number {
    width: 110px;
  }
  .timeLimit {
    margin-left: 20px;
  }
}
</style>
