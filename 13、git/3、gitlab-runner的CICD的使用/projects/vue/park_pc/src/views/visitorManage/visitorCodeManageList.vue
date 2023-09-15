<!--
 * @Description:  访客码管理 页面
 * @Author: Adela
 * @Date: 2023-03-02 11:23:34
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-14 15:32:56
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/visitorCodeManageList.vue
-->

<template>
  <div class="visitorCodeManageList">
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
          <el-form-item label="访客码名称：" prop="codeName">
            <el-input
              v-model="queryForm.codeName"
              placeholder="请输入访客码名称"
              clearable
              style="width: 220px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="创建时间：" prop="applyTime">
            <el-date-picker
              style="width: 220px"
              v-model="queryForm.applyTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryForm.status"
              placeholder="状态"
              clearable
              style="width: 220px"
            >
              <el-option :key="'1'" label="启用" :value="'1'" />
              <el-option :key="'2'" label="停用" :value="'2'" />
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
        icon="el-icon-plus"
        size="mini"
        class="addBtn"
        @click="addDialog"
        >新增</el-button
      >
      <el-button
        type="primary"
        class="importButton"
        icon="el-icon-upload2"
        size="mini"
        @click="handleImport"
        >导入</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
        >导出</el-button
      >
    </div>
    <div class="tableBox">
      <el-table v-loading="loading" :data="tableData">
        <el-table-column
          label="序号"
          align="center"
          type="index"
          width="80"
          fixed="left"
        >
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
          label="状态"
          min-width="100"
          prop="status"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            <div class="statusI">
              <span
                :class="`${
                  +scope.row.status === 1 ? 'statusIcon' : 'statusGreyIcon'
                }`"
              ></span>
              <span class="statusName">{{
                +scope.row.status === 1 ? "已启用" : "已停用"
              }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="绑定次数"
          min-width="100"
          prop="codeUseNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="免费天数"
          min-width="100"
          prop="codeFreeDay"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="有效时间"
          min-width="160"
          prop="effectiveTime"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="{ row }">
            <span v-if="row.startTime && row.endTime"
              >{{ row.startTime }} ~ {{ row.endTime }}
            </span>
          </template>
        </el-table-column>

        <el-table-column
          label="创建时间"
          min-width="160"
          prop="createTime"
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
              @click="editRow(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="text"
              class="enbale"
              @click="checkRow(scope.row)"
              v-if="+scope.row.status === 1"
              >查看</el-button
            >
            <el-button
              size="mini"
              type="text"
              class="colorHollowBtnRed"
              @click="deleteRow(scope.row)"
              v-if="+scope.row.status === 2"
              >删除</el-button
            >
            <el-button
              v-if="+scope.row.status === 1"
              size="mini"
              type="text"
              class="disabled"
              @click="deactivate(scope.row)"
              >停用</el-button
            >
            <el-button
              v-else
              size="mini"
              type="text"
              class="enbale"
              @click="activate(scope.row)"
              >启用</el-button
            >
            <!-- <el-button size="mini" type="text" @click="agreeRow(scope.row)"
              >审核</el-button
            > -->
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="pageInfo.total > 0"
        :total="pageInfo.total"
        :page.sync="pageInfo.pageNum"
        :limit.sync="pageInfo.pageSize"
        @pagination="getVisitorCodeList"
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
      <visitorCodeDialog
        :open="dialogOpenV"
        :title="dialogTitle"
        :detail="dialogDetail"
        :dialogIsCheck="dialogIsCheck"
        @cancel="cancelDialog"
        @success="successDialog"
      ></visitorCodeDialog>
    </div>
  </div>
</template>

<script>
import { VISITOR_CODE_MANAGE } from "./constant.js";
import titleTab from "@/components/Title/index.vue";
import { getToken } from "@/utils/auth";
import {
  getVisitorCodeList,
  activateVisitorCodeItem,
  deactivateVisitorCodeItem,
  deleteVisitorCodeItem,
  agreeVisitorCodeItem,
  disagreeVisitorCodeItem,
} from "./api/index";

import visitorCodeDialog from "@/views/visitorManage/components/visitorCodeM/visitorCodeDialog";
export default {
  name: "VisitorCodeManageList",
  components: {
    titleTab,
    visitorCodeDialog,
  },
  props: {},
  data() {
    return {
      constName: VISITOR_CODE_MANAGE,
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
          `${VISITOR_CODE_MANAGE.IMPORT_DATA_URL}`,
      },
      loading: false,
      queryForm: {
        codeName: "",
        applyTime: "",
        status: "1",
      },
      tableData: [
        // {
        //   id: 1,
        //   codeName: "测试访客码001",
        //   codeUseNumber: "5",
        //   codeFreeDay: "10",
        //   startTime: "2023-02-28",
        //   endTime: "2023-03-17",
        //   timeLimit: 0,
        //   remark: "测试测试",
        //   applyTime: "2023-02-27",
        //   status: 1,
        //   code: "131231312",
        // },
        // {
        //   id: 1,
        //   codeName: "测试访客码002",
        //   codeUseNumber: "5",
        //   codeFreeDay: "10",
        //   startTime: "2023-02-28",
        //   endTime: "2123-03-17",
        //   timeLimit: 1,
        //   remark: "测试测试",
        //   applyTime: "2023-02-27",
        //   status: 2,
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
      dialogIsCheck: false,
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getVisitorCodeList();
  },
  methods: {
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
      const params = this.handleParams(true); // 为了不加页码参数
      // console.log("adela editor", "12312312", params);
      this.download(
        this.constName.EXPORT_DATA_URL,
        {
          ...params,
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
      this.getVisitorCodeList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleParams(isAllPage) {
      const format = "YYYY-MM-DD";
      const { codeName, applyTime, status } = this.queryForm;
      const params = {
        codeName,
        status,
      };
      if (applyTime) {
        const startTime = applyTime[0];
        const endTime = applyTime[1];
        const _startTime = startTime
          ? this.$moment(startTime).format(format)
          : "";
        const _endTime = endTime ? this.$moment(endTime).format(format) : "";
        params.applyStartTime = _startTime;
        params.applyEndTime = _endTime;
      }
      if (!isAllPage) {
        params.pageSize = this.pageInfo.pageSize;
        params.pageNum = this.pageInfo.pageNum;
      }
      return params;
    },
    async getVisitorCodeList() {
      const parmas = this.handleParams();
      this.loading = true;
      const response = await getVisitorCodeList(parmas);
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
      this.getVisitorCodeList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.queryForm.status = "";
      this.handleQuery();
    },
    // agreeRow(row) {
    //   const id = row.id;
    //   this.$modal
    //     .confirm(`是否确认通过${row.id}的申请`)
    //     .then(function () {
    //       return agreeVisitorCodeItem(id);
    //     })
    //     .then(() => {
    //       this.getVisitorCodeList();
    //       this.$modal.msgSuccess("通过申请");
    //     })
    //     .catch(() => {});
    // },
    // disagreeRow(row) {
    //   const id = row.id;
    //   this.$modal
    //     .confirm(`是否确认驳回${row.id}的申请`)
    //     .then(function () {
    //       return disagreeVisitorCodeItem(id);
    //     })
    //     .then(() => {
    //       this.getVisitorCodeList();
    //       this.$modal.msgSuccess("通过成功");
    //     })
    //     .catch(() => {});
    // },
    addDialog() {
      this.dialogTitle = this.constName.DIALOG_ADD_TITLE;
      this.dialogDetail.id = undefined;
      this.dialogOpenV = true;
      this.dialogIsCheck = false;
    },
    editRow(row) {
      this.dialogTitle = this.constName.DIALOG_EDIT_TITLE;
      this.dialogDetail = row;
      this.dialogOpenV = true;
      this.dialogIsCheck = false;
    },
    checkRow(row) {
      this.dialogTitle = this.constName.DIALOG_CHECK_TITLE;
      this.dialogDetail = row;
      this.dialogOpenV = true;
      this.dialogIsCheck = true; // 查看disable 不可编辑
    },
    cancelDialog() {
      this.dialogDetail = {};
      this.dialogOpenV = false;
    },
    successDialog() {
      this.dialogDetail = {}; // 成功之后直接调接口，不关闭弹窗
      this.handleQuery();
    },

    deactivate(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认停用")
        .then(function () {
          return deactivateVisitorCodeItem(id);
        })
        .then(() => {
          this.getVisitorCodeList();
          this.$modal.msgSuccess("停用成功");
        })
        .catch(() => {});
    },
    activate(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认启用")
        .then(function () {
          return activateVisitorCodeItem(id);
        })
        .then(() => {
          this.getVisitorCodeList();
          this.$modal.msgSuccess("启用成功");
        })
        .catch(() => {});
    },
    deleteRow(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认删除")
        .then(function () {
          return deleteVisitorCodeItem(id);
        })
        .then(() => {
          this.getVisitorCodeList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "./index.scss";

.visitorCodeManageList {
  padding: 20px 0;
  margin: 20px;
  background: #fff;
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
