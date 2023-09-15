<!--
 * @Description: 在场订单
 * @Author: cuijing
 * @Date: 2023-03-03 08:51:19
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-07 15:19:53
 * @文件相对于项目的路径: \park_pc\src\views\statistic\onOrderList.vue
-->
<template>
  <div class="orderList">
    <div class="orderBox" style="width:70%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="160px">
        <el-form-item label="车辆类型：" prop="carType">
          <el-select v-model="queryForm.carType" placeholder="请选择车辆类型" clearable style="width:220px;">
            <el-option v-for="dict in carList" :key="dict.id" :label="dict.name" :value="dict.id" />
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
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="订单记录"></titleTab>
      <div class="importDiv">
        <!-- <el-button type="primary" size="mini" icon="el-icon-plus" class="addOrder" @click="addOrder">新增</el-button> -->
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
        <el-button type="primary" size="mini" @click="settleSomeOrder">批量结算</el-button>
        <!-- <el-button type="primary" class="importButton" size="mini" @click="handleImport">导入记录</el-button> -->
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" ref="table" :data="tableData" :row-class-name="tableRowClassName"
          @row-click="getPhoto" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="车牌" min-width="180" align="center" prop="carNumber">
          </el-table-column>
          <el-table-column label="车类型" min-width="160" align="center" prop="carTypeName">
          </el-table-column>
          <el-table-column label="入场时间" min-width="260" align="center" prop="entryTime">
          </el-table-column>
          <el-table-column label="备注" min-width="160" align="center" prop="remark">
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <!-- <el-button size="mini" class="colorHollowBtnCyan" type="text" @click="getDetail(scope.row)">查看</el-button> -->
              <el-button size="mini" type="text" @click="settleOrder(scope.row)">结算</el-button>
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
    <!-- 结算订单弹框 -->
    <el-dialog class="settleDialog" title="结算订单" :visible.sync="settleShow" width="683px" append-to-body
      @close="closeSettle">
      <div class="settleCon">
        <div class="oerderNumberList">
          <span class="orderNumberTit">结算车牌号：</span>
          <div class="orderNumberCon">
            <span class="orderNumberIn" v-for="(item, index) in settleOrderList" :key="index">{{ index ==
              settleOrderList.length - 1 ? item.carNumber : item.carNumber + ',' }}</span>
          </div>
        </div>
        <el-form class="settleBox" :rules="settleRules" :model="settleForm" ref="settleForm" size="small"
          label-width="120px">
          <el-form-item label="结算金额(元)：" prop="carPrice">
            <el-input v-model.number="settleForm.carPrice" placeholder="请输入结算金额" clearable style="width:220px;" />
          </el-form-item>
          <el-form-item label="结算原因：" prop="reason">
            <el-select v-model="settleForm.reason" placeholder="请选择结算原因" clearable style="width:220px;">
              <el-option v-for="dict in reasonList" :key="dict.id" :label="dict.reason" :value="dict.reason" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" class="confirmButton" @click="settleConfirm">确定</el-button>
            <el-button size="mini" class="cancleButton" @click="closeSettle">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "./components/title.vue"
import { getToken } from "@/utils/auth"
import { getOrderList, deleteOrder, settleOrder } from "@/views/statistic/api/onOrderList.js"
import { getSettingliftgatereasonList } from "@/views/basicSettings//api/baseSetting.js"
export default {
  name: 'orderList',
  components: {
    titleTab
  },
  dicts: ['pay_method'],
  props: {},
  data () {
    return {
      reasonList: [],
      labelPosition: 'right',
      queryForm: {
        entryTime: '',
        // exitTime: '',
        carType: '',
        // passageId: '',
        // fieldId: '',
        carNumber: '',
      },
      tableData: [],
      pageInfo: {
        total: 5,
        pageSize: 10,
        pageNum: 1,
      },
      carList: this.$store.getters.carList,
      aisleList: this.$store.getters.carAisleList,
      areaList: this.$store.getters.carAreaList,
      loading: false,
      carImgFrom: '',
      carImgTo: '',
      nowClickId: '',
      settleShow: false,
      settleForm: {
        orderList: [],
        carPrice: '',
        reason: ''
      },
      settleOrderList: [],
      settleRules: {
        carPrice: [
          { required: true, message: '请输入结算金额', trigger: 'blur' },
        ],
        reason: [
          { required: true, message: '请选择结算原因', trigger: 'change' },
        ]
      }
    }
  },
  computed: {},
  watch: {},
  methods: {
    async getReasonList () {
      let res = await getSettingliftgatereasonList()
      if (res.code == 200) {
        this.reasonList = res.rows
      }
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
    // 结算订单
    settleOrder (row) {
      this.resetSettleForm()
      this.settleOrderList.push(row)
      this.settleForm.orderList.push(row.id)
      this.settleShow = true
    },
    async settleConfirm () {
      let param = {
        liveIdList: this.settleForm.orderList,
        payAmount: this.settleForm.carPrice,
        liftGateReason: this.settleForm.reason
      }
      let res = await settleOrder(param)
      if(res.code ==  200){
        this.$message.success("结算订单成功！")
        this.settleShow = false
        this.getList()
      }else{
        this.$message.error("结算订单失败！")
      }
    },
    closeSettle () {
      this.settleShow = false
      this.resetSettleForm()
    },
    resetSettleForm () {
      this.$refs.table.clearSelection()
      this.settleOrderList = []
      this.settleForm = {
        orderList: [],
        carPrice: '',
        reason: ''
      }
    },
    // 批量选中
    handleSelectionChange (val) {
      this.settleOrderList = val
      val.forEach(item=>{
        this.settleForm.orderList.push(item.id)
      })
    },
    // 批量结算
    settleSomeOrder () {
      if (this.settleOrderList.length > 0) {
        this.settleShow = true
      } else {
        this.$message.success('请先选择需要结算订单！')
      }
    }
  },
  created () {

  },
  mounted () {
    this.getReasonList()
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

.settleDialog {
  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__body {
    padding: 0;
  }

  .settleTit {
    height: 40px;
    background: #EFF8FB;
    border-bottom: 1px solid #D7E5E9;
    display: flex;
    align-items: center;
  }

  .settleCon {
    padding: 15px 20px 30px;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;

    .oerderNumberList {
      padding: 0 0 20px;
      display: flex;
      // align-items: center;
      line-height: 20px;

      .orderNumberTit {
        font-weight: 600;
        width: 90px;
      }

      .orderNumberCon {
        flex: 1;
        -webkit-line-clamp: 2;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

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