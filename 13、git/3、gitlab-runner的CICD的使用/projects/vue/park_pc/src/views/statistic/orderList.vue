<!--
 * @Description: 离场订单
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 09:29:58
 * @文件相对于项目的路径: \park_pc\src\views\statistic\orderList.vue
-->
<template>
  <div class="orderList">
    <div class="orderBox" style="width:70%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="160px">
        <el-form-item label="车辆类型：" prop="carType">
          <el-select v-model="queryForm.carType" placeholder="请选择车辆类型" clearable style="width:220px;">
            <el-option v-for="dict in carList" :key="dict.id" :label="dict.name" :value="dict.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="车牌号：" prop="carNumber">
          <el-input v-model="queryForm.carNumber" placeholder="请输入车牌号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="入场时间：" prop="entryTime">
          <el-date-picker v-model="queryForm.entryTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择入场时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出场时间：" prop="exitTime">
          <el-date-picker v-model="queryForm.exitTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择出场时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div class="subBox">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </div>
      <titleTab title="订单记录"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" icon="el-icon-plus" class="addOrder" @click="addOrder">新增</el-button>
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
        <el-button type="primary" class="importButton" size="mini" @click="handleImport">导入记录</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData" :row-class-name="tableRowClassName" @row-click="getPhoto">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="车牌" min-width="180" align="center" prop="carNumber">
          </el-table-column>
          <el-table-column label="车类型" min-width="160" align="center" prop="carTypeName">
          </el-table-column>
          <el-table-column label="入场时间" min-width="260" align="center" prop="entryTime">
          </el-table-column>
          <el-table-column label="出场时间" min-width="260" align="center" prop="exitTime">
          </el-table-column>
          <el-table-column label="停留时长" min-width="260" align="center" prop="durationTime">
          </el-table-column>
          <el-table-column label="收费总额(元)" min-width="260" align="center" prop="parkingFee">
          </el-table-column>
          <el-table-column label="备注" min-width="160" align="center" prop="remark">
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" class="colorHollowBtnCyan" type="text" @click="getDetail(scope.row)">查看</el-button>
              <!-- <el-button size="mini" type="text" @click="editOrder(scope.row)">编辑</el-button> -->
              <el-button size="mini" class="colorHollowBtnRed" type="text" @click="deleteOrder(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
    <div class="orderBox" style="width:30%;margin-left:20px;">
      <titleTab title="图片显示"></titleTab>
      <div class="photoBox">
        <div class="carLicence">
          <div class="photoTit">
            <span>车辆进场照</span>
          </div>
          <div class="photoImg">
            <img v-if="carImgFrom" :src="carImgFrom" alt="">
          </div>
        </div>
        <div class="carImg">
          <div class="photoTit">
            <span>车辆出场照</span>
          </div>
          <div class="photoImg">
            <img v-if="carImgTo" :src="carImgTo" alt="">
          </div>
        </div>
      </div>
    </div>
    <!-- 新增订单弹框 -->
    <el-dialog class="addDialog" title="补录订单" :visible.sync="addShow" width="683px" append-to-body>
      <div class="addCon">
        <el-form class="addBox" :rules="addRules" :model="addForm" ref="addForm" size="small" label-width="120px">
          <el-form-item label="车牌号：" prop="carNumber">
            <el-input v-model="addForm.carNumber" placeholder="请输入车牌号" clearable style="width:220px;" />
          </el-form-item>
          <el-form-item label="入场时间：" prop="entryTime" required>
            <el-date-picker v-model="addForm.entryTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择入场时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="出场时间：" prop="exitTime" required>
            <el-date-picker v-model="addForm.exitTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择出场时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="备注：" prop="remark">
            <el-input v-model="addForm.remark" placeholder="请输入备注" clearable style="width:220px;" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" class="confirmButton" @click="addConfirm">确定</el-button>
            <el-button size="mini" class="cancleButton" @click="addShow = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
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
          <!-- <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                                                                  @click="importTemplate">下载模板</el-link> -->
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 订单详情弹框 -->
    <el-dialog class="detailDialog" title="订单详情" :visible.sync="detailShow" width="862px" append-to-body>
      <div class="detailCon">
        <div class="conTit">
          <titleTab title="车辆信息"></titleTab>
        </div>
        <div class="conTable mt15">
          <el-table :data="detail.fieldInfo">
            <el-table-column label="区域" min-width="180" align="center" prop="parkFieldName">
            </el-table-column>
            <el-table-column label="车牌" min-width="160" align="center" prop="carNumber">
            </el-table-column>
            <el-table-column label="车类型" min-width="160" align="center" prop="carTypeName">
            </el-table-column>
            <el-table-column label="入场通道" min-width="220" align="center" prop="entryPassageName">
            </el-table-column>
            <el-table-column label="入场时间" min-width="220" align="center" prop="entryTime">
            </el-table-column>
            <el-table-column label="出场通道" min-width="220" align="center" prop="exitPassageName">
            </el-table-column>
            <el-table-column label="出场时间" min-width="220" align="center" prop="exitTime">
            </el-table-column>
            <el-table-column label="停留总时长" min-width="220" align="center" prop="stopTime">
            </el-table-column>
          </el-table>
        </div>
        <div class="conTit mt15">
          <titleTab title="付费信息"></titleTab>
        </div>
        <div class="conTable mt15">
          <el-table :data="detail.parkingOrderInfo">
            <el-table-column label="订单编号" min-width="200" align="center" prop="orderNo">
            </el-table-column>
            <el-table-column label="应付金额(元)" min-width="160" align="center" prop="payAmount">
            </el-table-column>
            <el-table-column label="实付金额(元)" min-width="160" align="center" prop="paidAmount">
            </el-table-column>
            <el-table-column label="支付通道" min-width="120" align="center" prop="passageNo">
              <template slot-scope="scope">
                {{ scope.row.passageNo ? scope.row.passageNo : '预支付'}}
              </template>
            </el-table-column>
            <el-table-column label="支付方式" min-width="120" align="center" prop="payMethod">
              <template slot-scope="scope">
                {{ showDictLabel(dict.type.pay_method,scope.row.payMethod)}}
              </template>
            </el-table-column>
            <el-table-column label="付款时间" min-width="180" align="center" prop="payTime">
            </el-table-column>
            <el-table-column label="备注" min-width="120" align="center" prop="remark">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "./components/title.vue"
import { getToken } from "@/utils/auth"
import { getOrderList, addOrder, deleteOrder, getDetailOrder } from "@/views/statistic/api/orderList.js"
export default {
  name: 'orderList',
  components: {
    titleTab
  },
  props: {},
  dicts: ['pay_method'],
  data () {
    var validateStartTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择入场时间'))
      } else {
        if (this.addForm.entryTime) {
          this.$refs.addForm.validateField('exitTime')
        }
        callback()
      }

    }
    var validateEndTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择出场时间'))
      } else {
        if (!this.addForm.entryTime) {
          callback(new Error('请选择入场时间！'))
        } else if (new Date(this.addForm.entryTime).getTime() >= new Date(value).getTime()) {
          callback(new Error('出场时间必须大于入场时间！'))
        } else {
          callback()
        }
      }
    }
    return {
      labelPosition: 'right',
      queryForm: {
        entryTime: '',
        exitTime: '',
        carType: '',
        // passageId: '',
        // fieldId: '',
        carNumber: '',
      },
      tableData: [

      ],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      carList: this.$store.getters.carList,
      aisleList: this.$store.getters.carAisleList,
      areaList: this.$store.getters.carAreaList,
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
        updateSupport: true,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/parking/parksettlementrecords/import"
      },
      carImgFrom: '',
      carImgTo: '',
      nowClickId: '',
      detailShow: false,
      detail: {},
      addShow: false,
      addForm: {
        carNumber: '',
        entryTime: '',
        exitTime: '',
        remark: ''
      },
      addRules: {
        carNumber: [
          { required: true, message: '请输入车牌号', trigger: 'blur' },
        ],
        entryTime: [
          { validator: validateStartTime, trigger: 'blur' }
        ],
        exitTime: [
          { validator: validateEndTime, trigger: 'blur' }
        ]
      },
    }
  },
  computed: {},
  watch: {},
  methods: {
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    // 获取订单列表
    async getList () {
      this.loading = true
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        ...this.queryForm
      }
      let res = await getOrderList(param)
      if (res.code == 200) {
        this.loading = false
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
    },
    // 查看订单车辆照片
    getPhoto (row, column, event) {
      this.nowClickId = row.id
      this.carImgFrom = row.carImgUrlFrom
      this.carImgTo = row.carImgUrlTo
    },
    tableRowClassName ({ row, rowIndex }) {
      if (row.id === this.nowClickId) {
        return "highlight-row"
      }
      return ""
    },
    // 搜索
    handleQuery () {
      this.pageInfo.pageNum = 1
      this.getList()
    },
    resetQuery () {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 导出
    handleExport () {
      this.download('parking/parksettlementrecords/export', {
        ...this.queryForm
      }, `order_${new Date().getTime()}.xlsx`)
    },
    // 导入
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
    //查看详情
    getDetail (row) {
      this.detailShow = true
      this.detailOrder(row.id)
    },
    async detailOrder (id) {
      let res = await getDetailOrder(id)
      if (res.code == 200) {
        this.detail = res.data
      }
    },
    // 补录订单
    addOrder () {
      this.addShow = true
    },
    async addConfirm () {
      this.$refs['addForm'].validate(async (valid) => {
        if (valid) {
          let param = {
            ...this.addForm
          }
          let res = await addOrder(param)
          if (res.code == 200) {
            this.addShow = false
            this.getList()
            this.$message.success("新增离场订单成功！")
          } else {
            this.$message.error("新增离场订单失败！")
          }
        } else {
          this.$message.error("验证失败！")
        }
      })
    },
    // 删除订单
    deleteOrder (row) {
      this.$confirm("确定删除这条订单记录？", "删除提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteOrder(row.id)
            .then((res) => {
              if (res.code === 200) {
                this.$message.success("删除订单成功！")
                this.getList()
              }
            })
            .catch((errMsg) => {
              this.$message.error(errMsg)
            })
        })
        .catch(() => { })
    },
  },
  created () {

  },
  mounted () {
    this.getList()
  },
}
</script>
<style scoped lang='scss'>
.el-table {
  ::v-deep.highlight-row {
    background: #DBF4FB !important;
  }
}

::v-deep.el-table--enable-row-hover .el-table__body tr:hover>td.el-table__cell {
  background-color: transparent;
}

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
    // align-items: center;
  }

  .subBox {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .importDiv {
    padding: 10px 30px;

    .addOrder {
      background: #49C8EA;
      border-radius: 6px;
      border: 1px solid rgba(0, 0, 0, 0);
    }

    .importButton {
      background-color: rgba(27, 136, 190, 1);
      border-color: rgba(27, 136, 190, 1);
      border-radius: 6px;
    }
  }

  .tableBox {
    padding: 0 30px 10px;

    .fieldCon {
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: 'microsoft yahei', verdana, Tahoma;
    }
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

.orderList ::v-deep .el-form-item {
  display: flex;
}

.orderList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>
<style lang='scss'>
.detailDialog {
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__body {
    padding: 0;
  }

  .detailTit {
    height: 40px;
    background: #EFF8FB;
    border-bottom: 1px solid #D7E5E9;
    display: flex;
    align-items: center;
  }

  .detailCon {
    padding: 15px 20px 30px;

    .conTit {
      width: 100%;
      height: 40px;
      background: #EFF8FB;
      border: 1px solid #D7E5E9;
      display: flex;
      align-items: center;
    }

    .mt15 {
      margin-top: 15px;
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

.editDialog {
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__body {
    padding: 0;
  }

  .editTit {
    height: 40px;
    background: #EFF8FB;
    border-bottom: 1px solid #D7E5E9;
    display: flex;
    align-items: center;
  }

  .editCon {
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
</style>