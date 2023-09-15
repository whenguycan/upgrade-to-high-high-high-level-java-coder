<!--
 * @Description: 自主续费方案设置
 * @Author: Adela
 * @Date: 2023-03-01 15:17:47
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-07 09:17:48
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/selfPayList/selfPayScheme.vue
-->
<template>
  <div class="selfPayScheme">
    <el-form ref="form" :model="formData" :rules="rules" label-width="220px">
      <el-form-item label="公众号自主续费：">
        <el-switch
          v-model="formData.renewStatus"
          :active-value="'1'"
          :inactive-value="'0'"
          active-text="开"
          inactive-text="关"
        ></el-switch>
      </el-form-item>
      <el-form-item label="续费审核：">
        <el-switch
          v-model="formData.systemVerify"
          :active-value="'1'"
          :inactive-value="'0'"
          active-text="开"
          inactive-text="关"
        ></el-switch>
      </el-form-item>
      <template>
        <el-form-item label="新车主审核：" v-if="+formData.systemVerify === 1">
          <el-switch
            v-model="formData.newOwnerVerify"
            :active-value="'1'"
            :inactive-value="'0'"
            active-text="开"
            inactive-text="关"
          ></el-switch>
        </el-form-item>
        <el-form-item label="老车主审核：" v-if="+formData.systemVerify === 1">
          <el-switch
            v-model="formData.oldOwnerVerify"
            :active-value="'1'"
            :inactive-value="'0'"
            active-text="开"
            inactive-text="关"
          ></el-switch>
        </el-form-item>
        <el-form-item label="最长续费天数：">
          <el-input
            type="number"
            placeholder="请输入最长续费天数"
            style="width: 220px"
            v-model="formData.maxRenewDays"
            :min="0"
          >
          </el-input>
          天
        </el-form-item>
        <el-form-item label="续费临期天数：">
          <el-input
            type="number"
            placeholder="请输入最长续费天数"
            style="width: 220px"
            v-model="formData.renewDeadlineDays"
            :min="0"
          >
          </el-input>
          天
        </el-form-item>
      </template>

      <el-form-item>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getSelfPaySchemeDetail, editSelfPayScheme } from "../../api/index";
export default {
  name: "SelfPayScheme",
  components: {},
  props: {},
  data() {
    return {
      rules: {},
      formData: {
        systemVerify: null,
        newOwnerVerify: null,
        oldOwnerVerify: null,
        maxRenewDays: 0,
        renewDeadlineDays: 0,
      },
      islock: false,
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getformData();
  },
  mounted() {},
  methods: {
    // 新增、编辑 请求接口数据处理
    handlefromDate() {
      const {
        systemVerify,
        newOwnerVerify,
        oldOwnerVerify,
        maxRenewDays,
        renewDeadlineDays,
        renewStatus,
        id,
      } = this.formData;
      const params = {
        systemVerify,
        renewStatus,
      };
      if (id != undefined) {
        params.id = id;
      }
      if (+systemVerify === 1) {
        params.newOwnerVerify = newOwnerVerify;
        params.oldOwnerVerify = oldOwnerVerify;
      } else {
        params.newOwnerVerify = "0";
        params.oldOwnerVerify = "0";
      }

      params.maxRenewDays = maxRenewDays;
      params.renewDeadlineDays = renewDeadlineDays;
      return params;
    },
    // 详情 回填 数据处理
    async getformData() {
      const res = await getSelfPaySchemeDetail();
      if (+res.code === 200) {
        const d = res.data;
        const {
          systemVerify,
          newOwnerVerify,
          oldOwnerVerify,
          maxRenewDays,
          renewDeadlineDays,
          renewStatus,
          id,
        } = d;
        this.formData = {
          systemVerify,
          newOwnerVerify,
          oldOwnerVerify,
          maxRenewDays,
          renewDeadlineDays,
          renewStatus
        };
        if (id != undefined) {
          this.formData.id = id;
        }
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.islock) return;
          this.islock = true;
          const parmas = this.handlefromDate();
          if (parmas.id != undefined) {
            editSelfPayScheme(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.$emit("success", true);
                this.islock = false;
              } else {
                this.$modal.msgError("修改失败");
                this.$emit("cancel", false);
                this.islock = false;
              }
            });
          }
          this.islock = false;
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.selfPayScheme {
  padding: 30px 0;
}
</style>
