<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-13 14:13:30
 * @文件相对于项目的路径: \park_pc\src\views\carManage\whiteList.vue
-->
<template>
  <div class="whiteList">
    <div class="whiteBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="100px">
        <el-form-item label="车牌号：" prop="carNumber">
          <el-input v-model="queryForm.carNumber" placeholder="请输入车牌号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="白名单列表"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" icon="el-icon-plus" class="addOrder" @click="addWhite">新增</el-button>
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
        <el-button type="primary" class="importButton" size="mini" @click="handleImport">导入记录</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="车牌号" min-width="180" align="center" prop="carNumber">
          </el-table-column>
          <el-table-column label="开始日期" min-width="180" align="center" prop="startTime">
          </el-table-column>
          <el-table-column label="结束日期" min-width="180" align="center" prop="endTime">
          </el-table-column>
          <el-table-column label="操作员" min-width="160" align="center" prop="createBy">
          </el-table-column>
          <el-table-column label="备注" min-width="120" align="center" prop="remark">
          </el-table-column>
          <el-table-column label="操作时间" min-width="260" align="center" prop="createTime">
            <template slot-scope="scope">
              {{ scope.row.createTime | chatTime }}
            </template>
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" class="colorHollowBtnCyan" @click="editWhite(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" class="colorHollowBtnRed" @click="deleteWhite(scope.row)">删除</el-button>
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
          <!-- <div class="el-upload__tip" slot="tip">
                                        <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
                                      </div> -->
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增白名单弹框 -->
    <el-dialog class="addDialog" :title="whiteTitle" :visible.sync="whiteShow" width="683px" append-to-body>
      <div class="addCon">
        <el-form class="addBox" :rules="addRules" :model="addForm" ref="addForm" size="small" label-width="120px">
          <el-form-item label="车牌号：" prop="carNumber">
            <el-input v-model="addForm.carNumber" placeholder="请输入车牌号" clearable style="width:220px;" />
          </el-form-item>
          <el-form-item label="有效期：" prop="time">
            <el-date-picker v-model="addForm.time" type="daterange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="备注：" prop="remark">
            <el-input v-model="addForm.remark" placeholder="请输入备注" clearable style="width:220px;" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" class="confirmButton" @click="addConfirm">确定</el-button>
            <el-button size="mini" class="cancleButton" @click="whiteShow = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue"
import { getToken } from "@/utils/auth"
import { getWhiteList, addWhite, editWhite, deleteWhite } from './api/_whiteList'
export default {
  name: 'whiteList',
  components: {
    titleTab
  },
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        carNumber: '',
      },
      tableData: [],
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
        url: process.env.VUE_APP_BASE_API + "/parking/whiteList/importData"
      },
      whiteShow: false,
      addForm: {
        carNumber: '',
        // startTime: '',
        // endTime: '',
        time: [],
        remark: '',
      },
      addRules: {
        carNumber: [
          { required: true, message: '请输入车牌号', trigger: 'blur' },
        ],
      },
      whiteTitle: '新增白名单'
    }
  },
  computed: {},
  watch: {},
  methods: {
    async getList () {
      this.loading = true
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        ...this.queryForm
      }
      let res = await getWhiteList(param)
      if (res.code == 200) {
        this.loading = false
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
    },
    addWhite () {
      this.addForm = {
        carNumber: '',
        time: [],
        remark: '',
      }
      this.whiteTitle = '新增白名单'
      this.whiteShow = true
    },
    async addWhiteFun () {
      let param = {
        carNumber: this.addForm.carNumber,
        startTime: this.addForm.time[0],
        endTime: this.addForm.time[1],
        remark: this.addForm.remark
      }
      let res = await addWhite(param)
      if (res.code == 200) {
        this.whiteShow = false
        this.getList()
        this.$message.success("新增白名单成功！")
      } else {
        this.$message.error("新增白名单失败！")
      }
    },
    editWhite (row) {
      let data = JSON.parse(JSON.stringify(row))
      this.addForm = {
        id: data.id,
        carNumber: data.carNumber,
        time: [data.startTime || '', data.endTime || ''],
        remark: data.remark
      }
      this.whiteShow = true
      this.whiteTitle = '修改白名单'
    },
    async editWhiteFun () {
      let param = {
        id: this.addForm.id,
        carNumber: this.addForm.carNumber,
        startTime: this.addForm.time[0],
        endTime: this.addForm.time[1],
        remark: this.addForm.remark
      }
      let res = await editWhite(param)
      if (res.code == 200) {
        this.whiteShow = false
        this.getList()
        this.$message.success("修改白名单成功！")
      } else {
        this.$message.error("修改白名单失败！")
      }
    },
    addConfirm () {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          if (this.whiteTitle == '新增白名单') {
            this.addWhiteFun()
          } else {
            this.editWhiteFun()
          }
        } else {
          this.$message.error("验证失败！")
        }
      })
    },
    // 删除订单
    deleteWhite (row) {
      this.$confirm("确定删除这条白名单记录？", "删除提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteWhite(row.id)
            .then((res) => {
              if (res.code === 200) {
                this.$message.success("删除白名单成功！")
                this.getList()
              }
            })
            .catch((errMsg) => {
              this.$message.error(errMsg)
            })
        })
        .catch(() => { })
    },
    handleQuery () {
      this.pageInfo.pageNum = 1
      this.getList()
    },
    resetQuery () {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('parking/whiteList/export', {
        ...this.queryForm
      }, `white_${new Date().getTime()}.xlsx`)
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
    },
    importTemplate () {
      this.download('parking/whiteList/importTemplate', {
      }, `white_template_${new Date().getTime()}.xlsx`)
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
.whiteList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  .whiteBox {
    background-color: #fff;
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    border-radius: 4px;
    padding: 20px 0;
  }

  .searchBox {
    padding: 10px 30px;
    display: flex;
    flex-wrap: wrap;
    // align-items: center;
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
      margin-top: 10px;
    }
  }
}

.addDialog {
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__body {
    padding: 0;
  }

  .addTit {
    height: 40px;
    background: #EFF8FB;
    border-bottom: 1px solid #D7E5E9;
    display: flex;
    align-items: center;
  }

  .addCon {
    padding: 15px 20px 30px;
    display: flex;
    justify-content: center;

    .confirmButton {
      width: 108px;
      height: 32px;
      background: #49C8EA;
      border-radius: 6px;
      border: 1px solid rgba(0, 0, 0, 0);
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #FFFFFF;
    }

    .cancleButton {
      width: 108px;
      height: 32px;
      background: #EEF6F8;
      border-radius: 6px;
      border: 1px solid #00ACDA;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #00ACDA;
    }
  }
}

.whiteList ::v-deep .el-form-item {
  display: flex;
}

.whiteList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>