<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-15 17:41:02
 * @文件相对于项目的路径: \park_pc\src\views\statistic\components\chargeList.vue
-->
<template>
  <div class="orderList">
    <div class="orderBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="160px">
        <el-form-item label="订单编号：" prop="orderNo">
          <el-input v-model="queryForm.orderNo" placeholder="请输入订单编号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="支付方式：" prop="chargeType">
          <el-select v-model="queryForm.chargeType" placeholder="请选择支付方式" clearable style="width:220px;">
            <el-option v-for="dict in dict.type.pay_method" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="收费记录"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData" @row-click="getPhoto">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="订单编号" min-width="260" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="应付总额(元)" min-width="120" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="实付总额(元)" min-width="120" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="支付方式" min-width="120" align="center" prop="profitRatio">
          </el-table-column>
          <el-table-column label="备注" min-width="120" align="center" prop="channelName">
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import titleTab from "./title.vue"
import { getToken } from "@/utils/auth"
import {getParkFeeList} from "@/views/statistic/api/dealList.js"
export default {
  name: 'orderList',
  components: {
    titleTab
  },
  dicts: ['pay_method'],
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        orderNo: '',
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
    }
  },
  computed: {},
  watch: {},
  methods: {
    async getList () {
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        ...this.queryForm
      }
      let res = await getParkFeeList(param)
      if (res.code == 200) {
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
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
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
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

.orderList ::v-deep .el-form-item {
  display: flex;
}

.orderList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>