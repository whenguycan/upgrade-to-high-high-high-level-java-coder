<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 09:54:09
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 17:41:22
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/regularList/carTypeDialog.vue
-->

<template>
  <div class="addTypeDialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogOpen"
      :close-on-click-modal="false"
      width="700px"
      append-to-body
      class="payEdit"
      @close="cancel"
    >
      <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
        <el-form-item label="固定车名称：" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入固定车名称"
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="分组类型：" prop="groupId">
          <el-select
            v-model="formData.groupId"
            disabled
            placeholder="请选择分组类型"
            style="width: 220px"
          >
            <el-option
              v-for="dict in dict.type.mod_car_owner_group_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="购买时限：" prop="timeLimit">
          <el-checkbox v-model="formData.timeLimit"></el-checkbox>
        </el-form-item>
        <el-form-item
          label="续费生效时间："
          prop="startTime"
          v-if="formData.timeLimit"
        >
          <el-date-picker
            v-model="formData.startTime"
            type="datetime"
            placeholder="选择续费生效时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="续费失效时间："
          prop="endTime"
          v-if="formData.timeLimit"
        >
          <el-date-picker
            v-model="formData.endTime"
            type="datetime"
            placeholder="选择续费失效时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="续费标准：" prop="renewalStandard">
          <div class="renewalStandard">
            <span class="tip">请按由小到大的顺序设置</span>
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              class="addBtn"
              @click="addpriceList"
              >新增</el-button
            >
          </div>
        </el-form-item>
        <el-table v-loading="loading" :data="priceList">
          <el-table-column label="月(个)" align="center" prop="month">
            <template slot-scope="scope">
              <el-input
                type="number"
                v-model="scope.row.month"
                placeholder="请输入月数"
                v-if="scope.row.iseditor"
              />
              <span v-if="!scope.row.iseditor">{{ scope.row.month }}</span>
            </template>
          </el-table-column>

          <el-table-column label="金额(元)" align="center" prop="price">
            <template slot-scope="scope">
              <el-input
                type="number"
                v-model="scope.row.price"
                placeholder="请输入金额"
                v-if="scope.row.iseditor"
              />
              <span v-if="!scope.row.iseditor">{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-position"
                @click="editpriceList(scope.row)"
                v-if="!scope.row.iseditor"
                >编辑</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-position"
                @click="savepriceList(scope.row, scope.$index)"
                v-if="scope.row.iseditor"
                >保存</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-position"
                @click="deletepriceList(scope.$index)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addRegularCarCategory,
  editRegularCarCategory,
  getRegularCarCategoryDetail,
} from "../../api/index";
export default {
  name: "AddTypeDialog",
  dicts: ["mod_car_owner_group_type"],
  components: {},
  props: {
    title: {
      type: String,
      default: "",
    },
    open: {
      type: Boolean,
      default: false,
    },
    detail: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    var validateRenewalStandard = (rule, value, callback) => {
      const priceList = this.priceList;
      if (priceList.length == 0) {
        callback(new Error("请新增一条续费标准"));
      } else {
        const _v = [];
        priceList.forEach((item) => {
          if (item.iseditor === true) {
            _v.push(item);
          }
        });
        if (_v.length > 0) {
          callback(new Error("请将续费标准填写完整"));
        }
        callback();
      }
    };
    var validateStartTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请选择开始时间"));
      } else {
        if (this.formData.endTime) {
          this.$refs.form.validateField("endTime");
        }
        callback();
      }
    };
    var validateEndTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请选择结束时间"));
      } else {
        if (!this.formData.startTime) {
          callback(new Error("请选择开始时间"));
        } else if (Date.parse(this.formData.startTime) >= Date.parse(value)) {
          callback(new Error("结束时间必须大于开始时间"));
        } else {
          callback();
        }
      }
    };
    return {
      format: "YYYY-MM-DD HH:mm:ss",
      dialogOpen: this.open,
      parkNo: "P20230222150100",
      formData: {
        name: undefined,
        groupId: "1",
        timeLimit: false, // '0'-不限制 '1'-限制
        startTime: undefined,
        endTime: undefined,
      },
      loading: false,
      priceList: [],
      rules: {
        name: [
          { required: true, message: "固定车名称不能为空", trigger: "blur" },
        ],
        groupId: [
          { required: true, message: "分组类型不能为空", trigger: "blur" },
        ],
        startTime: [
          { required: true, validator: validateStartTime, trigger: "blur" },
        ],
        endTime: [
          { required: true, validator: validateEndTime, trigger: "blur" },
        ],
        renewalStandard: [
          {
            required: true,
            validator: validateRenewalStandard,
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {},
  watch: {},
  created() {
    console.log(11);
    if (this.detail.id) {
      this.getformData(this.detail.id);
    }
  },
  mounted() {},
  methods: {
    // 新增、编辑 请求接口数据处理
    handlefromDate() {
      const { name, groupId, timeLimit, startTime, endTime, id } =
        this.formData;
      const params = {
        parkNo: this.parkNo,
        name,
        groupId,
        timeLimit: timeLimit ? "1" : "0",
      };
      if (this.priceList && this.priceList.length > 0) {
        const priceList = this.priceList.map((item) => {
          return {
            month: +item.month,
            price: +item.price,
          };
        });
        params.priceList = priceList;
      }
      if (id != undefined) {
        params.id = id;
      }
      if (timeLimit) {
        const format = this.format;
        const _startTime = startTime
          ? this.$moment(startTime).format(format)
          : "";
        const _endTime = endTime ? this.$moment(endTime).format(format) : "";
        params.startTime = _startTime;
        params.endTime = _endTime;
      }
      return params;
    },
    // 详情 回填 数据处理
    async getformData(rowId) {
      const res = await getRegularCarCategoryDetail(rowId);
      if (+res.code === 200) {
        const d = res.data;
        const { name, groupId, timeLimit, startTime, endTime, priceList, id } =
          d;
        const _timeLimit = +timeLimit === 1 ? true : false;
        this.formData = {
          name,
          groupId,
          timeLimit: _timeLimit,
          startTime,
          endTime,
        };
        if (id != undefined) {
          this.formData.id = id;
        }
        if (priceList && priceList.length > 0) {
          this.priceList = priceList.map((item) => {
            return {
              iseditor: false,
              month: item.month,
              price: item.price,
            };
          });
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
      this.formData = {
        name: undefined,
        groupId: "1",
        timeLimit: false,
        startTime: undefined,
        endTime: undefined,
      };
      this.priceList = [];
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          const parmas = this.handlefromDate();
          console.log("新增、修改", parmas);
          if (parmas.id != undefined) {
            editRegularCarCategory(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.$emit("success", true);
              } else {
                this.$modal.msgError("修改失败");
                this.$emit("cancel", false);
              }
            });
          } else {
            addRegularCarCategory(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.$emit("success", true);
              } else {
                this.$modal.msgError("新增失败");
                this.$emit("cancel", false);
              }
            });
          }
        }
      });
    },
    // -------续费标准--------
    // 续费新增
    addpriceList() {
      this.priceList.push({
        month: null,
        price: null,
        iseditor: true, // 可编辑 true
      });
    },
    editpriceList(row) {
      row.iseditor = true;
    },
    savepriceList(row, index) {
      if (!row.month) {
        this.$message.error(
          `请将续费标准中 第${index + 1}行, 第1列 的月数填写完整`
        );
      } else if (!row.price) {
        this.$message.error(
          `请将续费标准中 第${index + 1}行, 第2列 的金额填写完整`
        );
      } else {
        row.iseditor = false;
      }
    },
    deletepriceList(index) {
      this.priceList.splice(index, 1);
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../index.scss";
//@import 'xxx.scss';引入公共css类

.renewalStandard {
  .tip {
    // margin: 0 0 8px 0px;
    color: rgba(0, 29, 60, 0.6);
  }
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.payEdit {
  ::v-deep .el-dialog__body {
    padding: 20px 110px;
  }
}
</style>
