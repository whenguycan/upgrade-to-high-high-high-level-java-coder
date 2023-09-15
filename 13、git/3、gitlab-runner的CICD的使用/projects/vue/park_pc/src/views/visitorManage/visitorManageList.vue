<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-03 10:38:26
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-14 15:17:34
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/visitorManageList.vue
-->

<template>
  <div class="visitorManageList">
    <titleTab :title="constName.SEARCH_TITLE"></titleTab>
    <el-form
      class="searchBox"
      :model="queryForm"
      ref="queryForm"
      size="small"
      label-width="100px"
    >
      <el-row>
        <el-col :span="8">
          <el-form-item label="姓名：" prop="name">
            <el-input
              v-model="queryForm.name"
              placeholder="请输入姓名"
              clearable
              style="width: 220px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="手机号：" prop="phone">
            <el-input
              v-model="queryForm.phone"
              placeholder="请输入手机号"
              clearable
              style="width: 220px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="审核状态：" prop="status">
            <el-select
              v-model="queryForm.status"
              placeholder="审核状态"
              clearable
              style="width: 220px"
            >
              <el-option :key="'0'" label="审核中" :value="'0'" />
              <el-option :key="'1'" label="已通过" :value="'1'" />
              <el-option :key="'2'" label="已驳回" :value="'2'" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="subBox">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <titleTab :title="constName.LIST_TITLE"></titleTab>
    <div class="importDiv">
      <el-button
        type="primary"
        size="mini"
        class="addBtn"
        @click="examineSomeApplication"
        >批量审核</el-button
      >
      <el-button
        type="primary"
        size="mini"
        class="importButton"
        @click="disapproveSomeApplication"
        >批量驳回</el-button
      >
    </div>
    <div class="tableBox">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="selectable"
          fixed="left"
        >
        </el-table-column>
        <el-table-column label="序号" align="center" type="index" width="80">
          <template slot-scope="scope">
            <span>{{
              scope.$index + (pageInfo.pageNum - 1) * pageInfo.pageSize + 1
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="访客码名称"
          min-width="100"
          prop="codeName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车牌号"
          min-width="100"
          prop="carNo"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="姓名"
          min-width="100"
          prop="name"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="手机号"
          min-width="100"
          prop="phone"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="来访日期"
          min-width="160"
          prop="day"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="来访事由"
          min-width="160"
          prop="visitReason"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="状态"
          min-width="160"
          prop="status"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            <div class="statusI">
              <span
                :class="`${
                  +scope.row.status === 1
                    ? 'statusIcon'
                    : +scope.row.status === 2
                    ? 'statusGreyIcon'
                    : 'statusBlueIcon'
                }`"
              ></span>
              <span class="statusName">{{
                +scope.row.status === 1
                  ? "已通过"
                  : +scope.row.status === 2
                  ? "已驳回"
                  : "审核中"
              }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="驳回原因"
          min-width="160"
          prop="rejectReason"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          width="120"
          fixed="right"
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="agreeRow(scope.row)"
              v-if="+scope.row.status === 0"
              >审核</el-button
            >
            <el-button
              size="mini"
              type="text"
              class="colorHollowBtnRed"
              @click="disagreeRow(scope.row)"
              v-if="+scope.row.status === 0"
              >驳回</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="pageInfo.total > 0"
        :total="pageInfo.total"
        :page.sync="pageInfo.pageNum"
        :limit.sync="pageInfo.pageSize"
        @pagination="getVisitorMList"
      />
    </div>

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="handleExportTemplate"
            >下载模板</el-link
          >
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新增修改车辆记录 -->
    <div v-if="dialogOpenV">
      <checkDialog
        :open="dialogOpenV"
        :title="dialogTitle"
        :detail="dialogDetail"
        :isApprove="isApprove"
        @cancel="cancelDialog"
        @success="successDialog"
      ></checkDialog>
    </div>
  </div>
</template>

<script>
import { VISITOR_APPLY_CHECK__MANAGE } from "./constant.js";
import titleTab from "@/components/Title/index.vue";
import { getToken } from "@/utils/auth";
import { getVisitorMList } from "./api/index";

import checkDialog from "@/views/visitorManage/components/visitorManageList/checkDialog";
export default {
  name: "VisitorCodeManageList",
  components: {
    titleTab,
    checkDialog,
  },
  props: {},
  data() {
    return {
      constName: VISITOR_APPLY_CHECK__MANAGE,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url:
          process.env.VUE_APP_BASE_API +
          `${VISITOR_APPLY_CHECK__MANAGE.IMPORT_DATA_URL}`,
      },
      loading: false,
      queryForm: {
        name: "",
        phone: "",
        status: "0",
      },
      tableData: [
        // {
        //   id: 1,
        //   name: "mm",
        //   phone: "18036481866",
        //   carNo: "苏D99999",
        //   day: "2023-02-28",
        //   visitReason: "事由",
        //   code: "131231312",
        // },
      ],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      dialogDetail: {},
      dialogTitle: "",
      dialogOpenV: false,
      applyList: [],
      isApprove: true,
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getVisitorMList();
  },
  methods: {
    // 审核中的才可以进行勾选
    selectable(row) {
      return +row.status === 0;
    },
    /** 导出模版按钮操作 */
    handleExportTemplate() {
      this.download(
        this.constName.EXPORT_TEMPLATE_URL,
        {},
        `${this.constName.EXPORT_TEMPLATE_TITLE}_${new Date().getTime()}.xlsx`
      );
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        this.constName.EXPORT_DATA_URL,
        {
          ...this.queryForm,
        },
        `${this.constName.EXPORT_DATA_TITLE}_${new Date().getTime()}.xlsx`
      );
    },
    handleImport() {
      this.upload.title = this.constName.IMPORT_DATA_TITLE;
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
          response.msg +
          "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
      );
      this.getVisitorMList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleParams() {
      const { name, phone, status } = this.queryForm;
      const params = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        name,
        phone,
        status,
      };
      return params;
    },
    async getVisitorMList() {
      const parmas = this.handleParams();
      this.loading = true;
      const response = await getVisitorMList(parmas);
      if (response.code === 200) {
        this.tableData = response.rows;
        this.pageInfo.total = response.total;
        this.loading = false;
      } else {
        console.log(1111);
      }
    },
    handleQuery() {
      this.pageInfo.pageNum = 1;
      this.getVisitorMList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.queryForm.status = "";
      this.handleQuery();
    },
    // 批量选中
    handleSelectionChange(val) {
      this.applyList = val;
      console.log("adela editor", this.applyList);
    },
    // 批量审核
    examineSomeApplication() {
      this.isApprove = true;
      if (this.applyList.length > 0) {
        this.dialogTitle = "批量审核";
        this.dialogDetail = this.applyList;
        this.dialogOpenV = true;
      } else {
        this.$message.warning("请先选择需审核的数据！");
      }
    },
    // 批量驳回
    disapproveSomeApplication() {
      this.isApprove = false;
      if (this.applyList.length > 0) {
        this.dialogTitle = "批量驳回";
        this.dialogDetail = this.applyList;
        this.dialogOpenV = true;
      } else {
        this.$message.warning("请先选择需驳回的数据！");
      }
    },
    // 单独审核
    agreeRow(row) {
      this.isApprove = true;
      this.dialogDetail = [{ ...row }];
      this.dialogTitle = "审核";
      this.dialogOpenV = true;
    },
    // 单独驳回
    disagreeRow(row) {
      this.isApprove = false;
      this.dialogDetail = [{ ...row }];
      this.dialogTitle = "驳回";
      this.dialogOpenV = true;
    },
    cancelDialog() {
      this.dialogDetail = {};
      this.dialogOpenV = false;
    },
    successDialog() {
      this.cancelDialog();
      this.getVisitorMList();
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "./index.scss";

.visitorManageList {
  padding: 20px 0;
  margin: 20px;
  background: #fff;

  ::v-deep .el-table th.el-table__cell > .cell {
    padding-left: 14px !important;
  }
  .searchBox {
    padding: 10px 10px;
    .subBox {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .importDiv {
    padding: 10px 30px 0;
    .importButton {
      background-color: rgba(27, 136, 190, 1);
      border-color: rgba(27, 136, 190, 1);
    }
  }

  .tableBox {
    padding: 0 30px 10px;
  }
}
</style>
