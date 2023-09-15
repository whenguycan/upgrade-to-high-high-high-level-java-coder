<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 10:02:52
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 16:47:01
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/regularList/carList.vue
-->

<template>
  <div class="carList" v-if="nowType && nowType.id">
    <!-- {{ nowType }} -->
    <el-form
      class="searchBox"
      :model="queryForm"
      ref="queryForm"
      size="small"
      label-width="120px"
    >
      <el-row :gutter="50">
        <el-col
          :span="12"
          style="display: flex; flex-direction: column; align-items: end"
        >
          <el-form-item label="车主姓名" prop="ownerName">
            <el-input
              v-model="queryForm.ownerName"
              placeholder="请输入车主姓名"
              clearable
              style="width: 220px"
            />
          </el-form-item>
          <el-form-item label="联系方式" prop="ownerPhone">
            <el-input
              v-model="queryForm.ownerPhone"
              placeholder="请输入联系方式"
              clearable
              style="width: 220px"
            />
          </el-form-item>
        </el-col>
        <el-col
          :span="12"
          style="display: flex; flex-direction: column; align-items: start"
        >
          <el-form-item label="车牌号：" prop="carNumber">
            <el-input
              v-model="queryForm.carNumber"
              placeholder="请输入车牌号"
              clearable
              style="width: 220px"
            />
          </el-form-item>
          <el-form-item label="类型：" prop="carType">
            <el-select
              v-model="queryForm.carType"
              placeholder="类型："
              clearable
              style="width: 220px"
            >
              <el-option :key="''" label="全部" :value="''" />
              <el-option :key="'0'" label="线下" :value="'0'" />
              <el-option :key="'1'" label="线上" :value="'1'" />
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
    <!-- </div> -->
    <titleTab title="车辆记录"></titleTab>
    <div class="importDiv">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        class="addBtn"
        @click="addCarInfo"
        >新增</el-button
      >
      <el-button
        type="primary"
        size="mini"
        icon="el-icon-download"
        @click="handleExportTemplate"
        >模版</el-button
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
    </div>
    <div class="tableBox">
      <el-table v-loading="loading" :data="tableData">
        <el-table-column label="序号" align="center" type="index" width="80" fixed="left">
          <template slot-scope="scope">
            <span>{{
              scope.$index + (pageInfo.pageNum - 1) * pageInfo.pageSize + 1
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="车牌号"
          min-width="100"
          prop="carNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车辆颜色"
          min-width="100"
          prop="carColor"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车牌备注"
          min-width="200"
          prop="carRemark"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车主姓名"
          min-width="100"
          prop="ownerName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="身份证号"
          min-width="220"
          prop="ownerCardId"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="联系地址"
          min-width="260"
          prop="ownerAddress"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="联系电话"
          min-width="160"
          prop="ownerPhone"
          :show-overflow-tooltip="true"
        />
        <!-- <el-table-column
          label="固定车类型"
          min-width="260"
          prop=""
        /> -->
        <el-table-column
          label="流动车位数"
          min-width="100"
          prop="flowPlaceNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="有效期开始时间"
          min-width="160"
          prop="startTime"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="有效期结束时间"
          min-width="160"
          prop="endTime"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="备注"
          prop="remark"
          min-width="200"
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
            <el-button size="mini" type="text" @click="editRow(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="text"
              class="colorHollowBtnRed"
              @click="deleteRow(scope.row)"
              >删除</el-button
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
        :action="
          upload.url +
          '?updateSupport=' +
          upload.updateSupport +
          `&carCategoryId=` +
          nowType.id
        "
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
          <!-- <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          > -->
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新增修改车辆记录 -->
    <div v-if="openV">
      <carDialog
        :open="openV"
        :title="title"
        :detail="detail"
        @cancel="cancelCarInfo"
        @success="successCar"
        :nowType="nowType"
      ></carDialog>
    </div>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import carDialog from "@/views/carManage/components/regularList/carDialog";
import { getToken } from "@/utils/auth";
import { regularCarList, removeRegularCar } from "../../api/index";
export default {
  name: "carList",
  components: {
    titleTab,
    carDialog,
  },
  props: {
    searchType: {
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
  data() {
    return {
      detail: {},
      openV: false,
      rules: {},
      queryForm: {
        ownerName: "",
        ownerPhone: "",
        carNumber: "",
        carType: "",
      },
      tableData: [
        // {
        //   id: 1,
        //   carNumber: "苏D99999",
        //   carColor: "白",
        //   carRemark: "测试",
        //   ownerName: "mm",
        //   ownerPhone: "12345",
        //   ownerCardId: "32068219930216111",
        //   ownerAddress: "dasdasda",
        //   flowPlaceNumber: 1,
        //   startTime: "2023-02-02 0:02:00",
        //   endTime: "2023-02-02 10:02:00",
        //   timeLimit: 0,
        //   remark: "21312312",
        // },
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
        url: process.env.VUE_APP_BASE_API + "/parking/regularCar/importData",
      },
    };
  },
  computed: {},
  watch: {
    nowType: {
      deep: true,
      handler(val) {
        if (val && val.id) {
          // 有数据才去请求
          this.handleQuery();
        }
      },
    },
  },
  created() {
    console.log(11);
  },
  mounted() {
    this.getList();
  },
  methods: {
    // 新增车辆信息 弹框
    addCarInfo() {
      this.title = "新增车牌信息";
      this.detail.id = undefined;
      this.openV = true;
    },
    editRow(row) {
      this.title = "修改车牌信息";
      this.detail = row;
      this.openV = true;
    },
    deleteRow(row) {
      const id = row.id;
      this.$modal
        .confirm(`是否确认删除${row.ownerName}的数据`)
        .then(function () {
          return removeRegularCar(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    cancelCarInfo() {
      this.detail = {};
      this.openV = false;
    },
    successCar() {
      this.detail = {};
      this.openV = false;
      this.getList();
    },
    handleParams() {
      if (this.searchType) {
        this.queryForm.ownerName = this.nowType.ownerName;
        this.queryForm.carNumber = this.nowType.carNumber;
        this.queryForm.ownerPhone = this.nowType.ownerPhone;
        this.queryForm.carType = this.nowType.carType;
      }
      const params = {
        ...this.queryForm,
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        carCategoryId: this.nowType.id,
      };
      return params;
    },
    // carList
    async getList() {
      const parmas = this.handleParams();
      this.loading = true;
      const response = await regularCarList(parmas);
      if (response.code === 200) {
        this.tableData = response.rows;
        this.pageInfo.total = response.total;
        this.loading = false;
        this.$emit("closeGlobalSearch", false);
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
        "/parking/regularCar/importTemplate",
        {},
        `车辆信息模版.xlsx`
      );
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/parking/regularCar/export",
        {
          carCategoryId: this.nowType.id,
          ...this.queryForm,
        },
        `车辆信息_${new Date().getTime()}.xlsx`
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

.carList {
  .searchBox {
    padding: 10px 30px;

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

.plateManagementIndex ::v-deep .el-form-item {
  display: flex;
}

.plateManagementIndex ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>
