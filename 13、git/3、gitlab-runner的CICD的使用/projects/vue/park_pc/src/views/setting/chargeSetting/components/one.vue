<template>
  <div class="one">
    <el-form :model="form" ref="form" label-width="260px" style="width: 520px">
      <el-form-item label="计费舍入：" prop="roundWay">
        <el-radio-group v-model="form.roundWay">
          <el-radio :label="'1'">全入</el-radio>
          <el-radio :label="'2'">全舍</el-radio>
          <el-radio :label="'3'">四舍五入</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="最高收费：" prop="maximumCharge">
        <el-checkbox v-model="form.maximumChargeShow"></el-checkbox>
      </el-form-item>
      <el-form-item
        v-if="form.maximumChargeShow"
        label="最高限价："
        prop="maximumCharge"
      >
        <div class="maximumCharge" style="display: flex; align-items: center">
          <el-input
            :precision="2"
            type="number"
            v-model="form.maximumCharge"
            label="描述文字"
            :controls="false"
          >
            <template slot="append">元</template>
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="最小计费精度：" prop="minimumChargeAccurary">
        <el-input
          step="0.01"
          type="number"
          v-model="form.minimumChargeAccurary"
          label="描述文字"
          :controls="false"
        >
          <template slot="append">元</template>
        </el-input>
      </el-form-item>
      <el-form-item label="时间优惠券是否包含免费时段：" prop="tcFreeTimeFlag">
        <el-select
          style="width: 100%"
          v-model="form.tcFreeTimeFlag"
          placeholder="请选择"
        >
          <el-option
            v-for="item in list1"
            :key="item.value"
            :label="item.label"
            :value="item.value + ''"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="使用时间优惠券的方式：" prop="tcUseWay">
        <el-select
          style="width: 100%"
          v-model="form.tcUseWay"
          placeholder="请选择使用时间优惠券的方式"
        >
          <el-option
            v-for="item in list2"
            :key="item.value"
            :label="item.label"
            :value="item.value + ''"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="秒设置：" prop="secondCarry">
        <el-select
          style="width: 100%"
          v-model="form.secondCarry"
          placeholder="请选择秒设置"
        >
          <el-option
            v-for="item in list3"
            :key="item.value"
            :label="item.label"
            :value="item.value + ''"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitLoading" @click="handleQuery"
          >确定</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { parkAdd, parkDetail, parkEdit } from "@/api/setting/index";
export default {
  name: "one",
  data() {
    return {
      isEdit: false,
      form: {
        roundWay: "2",
        maximumChargeShow: false,
        maximumCharge: 0.0,
        minimumChargeAccurary: 0.1,
        tcFreeTimeFlag: "1",
        tcUseWay: "1",
        secondCarry: "1",
      },
      list1: [
        {
          label: "不包含",
          value: 0,
        },
        {
          label: "包含",
          value: 1,
        },
      ],
      list2: [
        {
          label: "入场时间前移",
          value: 1,
        },
        {
          label: "出场时间后移",
          value: 2,
        },
        {
          label: "减掉优惠时间",
          value: 3,
        },
      ],
      list3: [
        {
          label: "截秒",
          value: 1,
        },
        {
          label: "秒进位",
          value: 2,
        },
      ],
      submitLoading: false,
    };
  },
  props: {
    parkNo: {
      type: [String, Number],
    },
  },
  methods: {
    getDetail() {
      parkDetail().then((res) => {
        if (res.code === 200 && res.data) {
          console.log('1111111')
          this.isEdit = true;
          this.form = res.data;
          if (JSON.stringify(this.form.maximumCharge) !== 'null') {
            this.$set(this.form, "maximumChargeShow", true);
          }
        }
      });
    },
    handleQuery() {
      if (!this.form.maximumChargeShow) {
        this.form.maximumCharge = null;
      }
      if(this.form.minimumChargeAccurary.toString().split(".")[1].length > 2) {
        this.$message.error('最小计费精度只能精度到分！')
        return false;
      }
      let obj = { ...this.form };
      if (this.isEdit) {
        parkEdit(obj)
          .then((res) => {
            if (res.code === 200) {
              this.$message.success("收费方案编辑成功！");
              this.submitLoading = false;
              this.getDetail();
            }
          })
          .catch((errMsg) => {
            this.$message.error(errMsg);
            this.submitLoading = false;
          });
      } else {
        parkAdd(obj)
          .then((res) => {
            if (res.code === 200) {
              this.$message.success("收费方案新增成功！");
              this.submitLoading = false;
              this.getDetail();
            }
          })
          .catch((errMsg) => {
            this.$message.error(errMsg);
            this.submitLoading = false;
          });
      }
    },
  },
  mounted() {
    this.getDetail();
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