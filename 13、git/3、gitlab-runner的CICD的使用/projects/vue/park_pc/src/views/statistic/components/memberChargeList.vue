<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-24 10:38:03
 * @文件相对于项目的路径: \park_pc\src\views\statistic\components\memberChargeList.vue
-->
<template>
  <div class="orderList">
    <div class="orderBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="180px">
        <el-form-item label="订单编号：" prop="orderNumber">
          <el-input v-model="queryForm.carNumber" placeholder="请输入订单编号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="会员号：" prop="memberNumber">
          <el-input v-model="queryForm.carNumber" placeholder="请输入会员号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="车牌号：" prop="carNumber">
          <el-input v-model="queryForm.carNumber" placeholder="请输入车牌号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="下单时间：" prop="time">
          <el-date-picker v-model="queryForm.endTime" type="datetime" placeholder="选择订单结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="支付方式：" prop="chargeType">
          <el-select v-model="queryForm.chargeType" placeholder="请选择支付方式" clearable style="width:220px;">
            <el-option v-for="dict in chargeList" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div class="subBox">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </div>
      <titleTab title="充值记录"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
        <el-button type="primary" class="importButton" size="mini" @click="handleImport">导入记录</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData" @row-click="getPhoto">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="订单编号" min-width="180" align="center" prop="projectNumber">
          </el-table-column>
          <el-table-column label="会员号" min-width="160" align="center" prop="projectAmount">
          </el-table-column>
          <el-table-column label="下单时间" min-width="260" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="应付总额(元)" min-width="120" align="center" prop="profitRatio">
            <template slot-scope="scope">
              {{ scope.row.projectAmount }}-->{{ formatMoney }}
            </template>
          </el-table-column>
          <el-table-column label="实付总额(元)" min-width="120" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="支付方式" min-width="120" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="备注" min-width="120" align="center" prop="channelName">
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-position" @click="">编辑</el-button>
              <el-button size="mini" type="text" icon="el-icon-position" @click="">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
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
import titleTab from "./title.vue"
import { getToken } from "@/utils/auth"
export default {
  name: 'orderList',
  components: {
    titleTab
  },
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        orderNumber: '',
        memberNumber: '',
        carNumber: '',
        time: '',
        chargeType: '',
      },
      tableData: [],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      carList: this.$store.getters.carList,
      chargeList: this.$store.getters.carList,
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
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      carLicence: '',
      carImg: '',
    }
  },
  computed: {},
  watch: {},
  methods: {
    getList () {

    },
    getPhoto (row, column, event) {

    },
    handleQuery () {
      this.page.pageNum = 1
      this.getList()
    },
    resetQuery () {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    handleImport () {
      this.upload.title = "用户导入"
      this.upload.open = true
    },
    // 文件上传中处理
    handleFileUploadProgress (event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess (response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    },
    // 提交上传文件
    submitFileForm () {
      this.$refs.upload.submit()
    }
  },
  created () {

  },
  mounted () {
    this.getList()
  },
}
</script>
<style scoped lang='scss'>
.orderList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  .orderBox {
    background-color: #fff;
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    border-radius: 4px;
    padding: 20px 0;
  }

  .searchBox {
    padding: 10px 30px;
    display: flex;
    flex-wrap: wrap;
  }

  .subBox {
    display: flex;
    align-items: center;
    justify-content: center;

  }

  .importDiv {
    padding: 10px 30px;

    .importButton {
      background-color: rgba(27, 136, 190, 1);
      border-color: rgba(27, 136, 190, 1)
    }
  }

  .tableBox {
    padding: 0 30px 10px;
  }

  .photoBox {
    display: flex;
    flex-direction: column;
    padding: 12px;
    height: 100%;

    .carLicence {
      flex: 1;
      background: #FFFFFF;
      border: 1px solid #D7E5E9;
    }

    .photoTit {
      height: 40px;
      background: #EFF8FB;
      border-bottom: 1px solid #D7E5E9;

      span {
        font-size: 16px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #001D3C;
        line-height: 40px;
        margin-left: 10px;
      }
    }

    .photoImg {
      padding: 10px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .carImg {
      flex: 1.2;
      background: #FFFFFF;
      border: 1px solid #D7E5E9;
    }
  }
}

.orderList ::v-deep .el-form-item {
  display: flex;
}

.orderList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>
