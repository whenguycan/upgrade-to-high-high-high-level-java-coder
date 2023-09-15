<!--
 * @Description: 异常订单
 * @Author: cuijing
 * @Date: 2023-03-10 08:52:09
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-15 17:43:13
 * @文件相对于项目的路径: \park_pc\src\views\statistic\abnormalList.vue
-->
<template>
  <div class="abnormalList">
    <div class="abnormalBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="160px">
        <el-form-item label="订单编号：" prop="orderNo">
          <el-input v-model="queryForm.orderNo" placeholder="请输入订单编号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item label="异常类型：" prop="abnormalType">
          <el-select v-model="queryForm.abnormalType" placeholder="请选择异常类型" clearable style="width:220px;">
            <el-option v-for="dict in carList" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="异常订单"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="订单编号" min-width="220" align="center" prop="orderNo">
          </el-table-column>
          <el-table-column label="车牌" min-width="180" align="center" prop="carNumber">
          </el-table-column>
          <el-table-column label="车类型" min-width="160" align="center" prop="carTypeName">
          </el-table-column>
          <el-table-column label="入场时间" min-width="220" align="center" prop="entryTime">
            <template slot-scope="scope">
              {{ scope.row.entryTime |  chatTime}}
            </template>
          </el-table-column>
          <el-table-column label="异常类型" min-width="220" align="center" prop="unusualType">
          </el-table-column>
          <el-table-column label="异常原因" min-width="220" align="center" prop="unusualReason">
          </el-table-column>
          <el-table-column label="备注" min-width="160" align="center" prop="remark">
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import titleTab from "./components/title.vue"
import { getToken } from "@/utils/auth"
import { getAbnormalList, deleteAbnormal } from "@/views/statistic/api/abnormalList.js"
export default {
  name: 'abnormalList',
  components: {
    titleTab
  },
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        orderNo:'',
        abnormalType: '',
      },
      tableData: [],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      carList: this.$store.getters.carList,
      aisleList: this.$store.getters.carAisleList,
      areaList: this.$store.getters.carAreaList,
      loading: false,
    }
  },
  computed: {},
  watch: {},
  methods: {
    // 获取订单列表
    async getList () {
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        ...this.queryForm
      }
      let res = await getAbnormalList(param)
      if (res.code == 200) {
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
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
      this.download('parking/abnormalorder/export', {
        ...this.queryForm
      }, `abnormal_${new Date().getTime()}.xlsx`)
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

.abnormalList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  .abnormalBox {
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

    .addAbnormal {
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

.abnormalList ::v-deep .el-form-item {
  display: flex;
}

.abnormalList ::v-deep .el-form-item__content {
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
