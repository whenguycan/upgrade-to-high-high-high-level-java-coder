<!--
 * @Description:  自主缴费审核管理
 * @Author: Adela
 * @Date: 2023-03-01 15:18:08
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-01 17:22:51
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/selfPayList/selfPayExamine.vue
-->

<template>
  <div class="selfPayExamine">
    <titleTab title="查询条件"></titleTab>
    <el-form
      class="searchBox"
      :model="queryForm"
      ref="queryForm"
      size="small"
      label-width="100px"
    >
      <el-row>
        <el-col :span="8">
          <el-form-item label="审批类型：" prop="status">
            <el-select
              v-model="queryForm.status"
              placeholder="审批类型"
              clearable
              style="width: 220px"
            >
              <el-option :key="'0'" label="未审核" :value="'0'" />
              <el-option :key="'1'" label="审核通过" :value="'1'" />
              <el-option :key="'2'" label="审核未通过" :value="'2'" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="车牌号：" prop="carNumber">
            <el-input
              v-model="queryForm.carNumber"
              placeholder="请输入车牌号"
              clearable
              style="width: 220px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="申请时间：" prop="applyTime">
            <el-input
              v-model="queryForm.applyTime"
              placeholder="请输入申请时间"
              clearable
              style="width: 220px"
            />
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
        <el-button
          type="primary"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          >导出</el-button
        >
        <el-button
          type="primary"
          class="importButton"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          >导入</el-button
        >
      </el-form-item>
    </el-form>
    <!-- </div> -->
    <titleTab title="自主缴费审核管理列表"></titleTab>
    <div class="importDiv">
      <el-button type="primary" size="mini" class="addBtn">全部审批</el-button>
      <!-- <el-button
        type="primary"
        size="mini"
        icon="el-icon-download"
        @click="handleExportTemplate"
        >模版</el-button
      > -->
      <!-- <el-button
        type="primary"
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
        >导出</el-button
      >
      <el-button
        type="primary"
        class="importButton"
        icon="el-icon-upload2"
        size="mini"
        @click="handleImport"
        >导入</el-button
      > -->
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
          label="审批类型"
          min-width="100"
          prop="status"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车主姓名"
          min-width="100"
          prop="ownerName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车牌号"
          min-width="100"
          prop="carNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车主类型"
          min-width="100"
          prop="ownerType"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="续费天数"
          min-width="100"
          prop="renewDays"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="停车场"
          min-width="100"
          prop="parkName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="联系方式"
          min-width="100"
          prop="ownerPhone"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="申请时间"
          min-width="160"
          prop="applyTime"
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
            <el-button size="mini" type="text" @click="agreeRow(scope.row)"
              >审核</el-button
            >
            <el-button
              size="mini"
              type="text"
              class="colorHollowBtnRed"
              @click="disagreeRow(scope.row)"
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
        @pagination="getList"
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
          <!-- <div class="el-upload__tip" slot="tip">
             <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
           </div> -->
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
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import { getToken } from "@/utils/auth";
import {
  getSelfPayList,
  removeRegularCar,
  agreeSelfPay,
  disagreeSelfPay,
} from "../../api/index";
export default {
  name: "SelfPayExamine",
  components: {
    titleTab,
  },
  props: {},
  data() {
    return {
      detail: {},
      openV: false,
      rules: {},
      queryForm: {
        applyTime: "",
        carNumber: "",
        status: "",
      },
      tableData: [
        {
          // id: 1,
          // carNumber: "苏D99999",
          // ownerName: "mm",
          // ownerPhone: "12345",
          // status: "1",
          // ownerType: "1",
          // renewDays: 200,
          // parkName: "alalala",
          // applyTime: "20230301",
        },
      ],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      loading: false,
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
        url: process.env.VUE_APP_BASE_API + "/parking/selfPay/importData",
      },
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getList();
  },
  methods: {
    agreeRow(row) {
      const id = row.id;
      this.$modal
        .confirm(`是否确认通过${row.id}的申请`)
        .then(function () {
          return agreeSelfPay(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("通过申请");
        })
        .catch(() => {});
    },
    disagreeRow(row) {
      const id = row.id;
      this.$modal
        .confirm(`是否确认驳回${row.id}的申请`)
        .then(function () {
          return disagreeSelfPay(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("通过成功");
        })
        .catch(() => {});
    },
    handleParams() {
      const params = {
        ...this.queryForm,
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
      };
      return params;
    },
    // carList
    async getList() {
      const parmas = this.handleParams();
      this.loading = true;
      const response = await getSelfPayList(parmas);
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
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出模版按钮操作 */
    handleExportTemplate() {
      this.download(
        "/parking/selfPay/importTemplate",
        {},
        `自主缴费审核模版_${new Date().getTime()}.xlsx`
      );
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/parking/selfPay/export",
        {
          ...this.queryForm,
        },
        `自主缴费审核列表信息_${new Date().getTime()}.xlsx`
      );
    },
    handleImport() {
      this.upload.title = "用户导入";
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
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "../../index.scss";

.selfPayExamine {
  padding: 20px 0 0 0;
  .searchBox {
    padding: 10px 10px;

    // display: flex;
    // align-items: center;
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
