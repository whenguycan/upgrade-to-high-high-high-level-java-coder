<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-06 15:01:16
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-10 17:18:24
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/components/visitorManageList/checkDialog.vue
-->
<template>
  <div class="checkDialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogOpen"
      :close-on-click-modal="false"
      width="580px"
      append-to-body
      class="checkDialog"
      @close="cancel"
    >
      <div class="icon" v-if="isApprove">
        <i class="el-icon-circle-check"></i>
      </div>
      <div class="icon iconClose" v-else>
        <i class="el-icon-circle-close"></i>
      </div>

      <div class="tipC">
        <div>
          您是否想{{ isApprove ? "通过" : "驳回" }}
          <div v-for="(item, index) in detail" :key="index" class="tipCT">
            <span>"{{ item.name }}"</span>
            <i v-if="index < detail.length - 1">, </i>
          </div>

          的审核！
        </div>
        <el-input
          style="margin-top: 26px; width: 300px"
          v-if="!isApprove"
          type="textarea"
          :rows="2"
          placeholder="请输入驳回理由"
          v-model="rejectReason"
        >
        </el-input>
      </div>

      <div class="btnc">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import { approveVisitorMItems, disApproveVisitorMItems } from "../../api/index";
export default {
  name: "CheckDialog",
  components: {
    titleTab,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    detail: {
      type: Array,
    },
    open: {
      type: Boolean,
      default: false,
    },
    isApprove: {
      type: Boolean,
      default: true,
    },
  },
  watch() {},
  data() {
    return {
      dialogOpen: this.open,
      rejectReason: "",
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    // 取消按钮
    cancel() {
      this.dialogOpen = false;
      this.$emit("cancel", false);
    },
    /** 提交按钮 */
    submitForm: function () {
      const idArr = this.detail.map((item) => {
        return item.id;
      });
      const _id = idArr.toString();
      console.log("adela editor", idArr, _id);
      if (this.isApprove) {
        approveVisitorMItems(_id).then((response) => {
          if (+response.code === 200) {
            this.$modal.msgSuccess("审核成功");
            this.$emit("success", false);
          } else {
            this.$modal.msgError("审核失败");
            this.$emit("cancel", false);
          }
        });
      } else {
        const params = {
          ids: _id,
          rejectReason: this.rejectReason
        }
        console.log('adela editor',params);

        disApproveVisitorMItems(params).then((response) => {
          if (+response.code === 200) {
            this.$modal.msgSuccess("审核成功");
            this.$emit("success", false);
          } else {
            this.$modal.msgError("审核失败");
            this.$emit("cancel", false);
          }
        });
      }
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "../../index.scss";
.checkDialog {
  .btnc {
    width: 100%;
    display: flex;
    justify-content: center;
  }

  .icon {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 80px;
    color: #52c41a;
  }

  .iconClose {
    color: #ccc;
  }
  .tipC {
    text-align: center;
    margin: 20px 0 40px;
    .tipCT {
      display: inline-block;
      & > span {
        font-weight: 700;
        font-size: 16px;
      }
    }
  }
}
</style>
