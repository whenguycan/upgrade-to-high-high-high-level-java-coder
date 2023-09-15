<template>
  <div class="one">
    <el-form :model="form" ref="form" label-width="260px" style="width: 820px;padding-top: 20px;">
      <el-form-item label="余位为0是否允许车辆入场：" prop="isPermitEntry">
        <el-radio-group v-model="form.isPermitEntry">
          <el-radio :label="'1'">是</el-radio>
          <el-radio :label="'0'">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="月卡车是否占用车位数：" prop="isUseLot">
        <el-radio-group v-model="form.isUseLot">
          <el-radio :label="'1'">是</el-radio>
          <el-radio :label="'0'">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="绑定车辆类型：" prop="bandCarTypes">
        <el-select v-model="bandCarTypes" multiple placeholder="请选择"
                   clearable
                   style="width: 300px">
          <el-option
            v-for="item in options"
            :key="item.code"
            :label="item.name"
            :value="item.code">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit"
        >确定</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getSettingcartypeList,getInfo,add,edit } from "../api";
export default {
  name: "one",
  data() {
    return {
      // 遮罩层
      loading: true,
      submitLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        name: "",
        code: undefined,
        status: undefined,
      },
      form: {
        id: null,
        isPermitEntry: "0",
        isUseLot: "0",
        bandCarTypes: ""
      },
      options: [],
      bandCarTypes: [],
    };
  },
  props: {
    parkNo: {
      type: [String, Number],
    },
  },
  methods: {
    /** 查询车辆类型列表 */
    getList() {
      this.loading = true;
      getSettingcartypeList(this.queryParams).then((response) => {
        this.options = response.rows;
        this.loading = false;
      });
    },
    getInfo(){
      this.loading = true;
      getInfo().then((response) => {
        console.info("response.data:"+response.data)
        this.loading = false;
        if(response.data){
          this.form = response.data;
          this.bandCarTypes = this.form.bandCarTypes.split(",");
        }
      });
    },
    handleSubmit(){
      this.submitLoading = true;
      this.form.bandCarTypes = this.bandCarTypes.join(",");
      console.info(this.form)
      if(this.form.id == null){
        add(this.form).then((res) => {
          if (res.code === 200) {
            this.$message.success("基础配置新建成功！");
          }
        });
      }else{
        edit(this.form).then((res) => {
          if (res.code === 200) {
            this.$message.success("基础配置修改成功！");
          }
        });
      }
      this.submitLoading = false;
    }
  },
  mounted() {
    this.getList();
    this.getInfo();
  },
  watch: {
    "form.maximumChargeShow": {
      handler(val) {
        if (!val) {
          this.form.maximumCharge = null;
        }
      },
      deep: true,
      immediate: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.one {
}
.one ::v-deep .el-form-item__label {
  font-size: 14px !important;
  font-weight: 400 !important;
  color: rgba(0, 29, 60, 0.85) !important;
  line-height: 32px !important;
}
.one ::v-deep .el-form-item--medium .el-form-item__content {
  line-height: 32px !important;
}
.one ::v-deep .el-input-number--medium {
  line-height: 32px !important;
}
.one ::v-deep .el-form-item {
  margin-bottom: 20px !important;
}
.one ::v-deep .el-input--medium .el-input__inner {
  height: 32px !important;
  line-height: 32px !important;
  font-size: 14px !important;
  color: #001d3c !important;
}
.one ::v-deep .el-input--medium .el-input__icon {
  line-height: 32px !important;
}
</style>
