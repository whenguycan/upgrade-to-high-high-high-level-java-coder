<template>
  <div class="auditList">
    <div class="memberBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="100px">
        <el-form-item label="退款单号：" prop="orderNo">
          <el-input v-model="queryForm.orderNo" placeholder="请输入退款单号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="会员列表"></titleTab>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="退款单号" min-width="200" align="center" prop="orderNo">
          </el-table-column>
          <el-table-column label="所属车场编号" min-width="80" align="center" prop="parkNo">
          </el-table-column>
          <el-table-column label="商户ID" min-width="100" align="center" prop="applyUserId">
          </el-table-column>
          <el-table-column label="退款金额" min-width="60" align="center" prop="refundAmount">
          </el-table-column>
          <el-table-column label="申请人" min-width="80" align="center" prop="applyUserName">
          </el-table-column>
          <el-table-column label="申请时间" min-width="120" align="center" prop="applyTime">
          </el-table-column>
          <el-table-column label="申请说明" min-width="200" align="center" prop="applyRemark">
          </el-table-column>
          <el-table-column label="退款状态" min-width="80" align="center" >
            <template slot-scope="scope">
              <dict-tag :options="dict.type.bd_refund_status" :value="scope.row.refundStatus"/>
            </template>
          </el-table-column>
          <el-table-column label="审核人" min-width="80" align="center" prop="auditUserName">
          </el-table-column>
          <el-table-column label="审核时间" min-width="120" align="center" prop="auditTime">
          </el-table-column>
          <el-table-column label="审核说明" min-width="200" align="center" prop="auditRemark">
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" class="colorHollowBtnCyan"
                         @click="handleSubmit(scope.row)" v-if="scope.row.refundStatus =='0'">审核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
                    :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
    <!-- 审核弹框 -->
    <el-dialog class="auditDialog" title="退款审核" :visible.sync="detailShow" width="600px" append-to-body>
      <div class="">
        <el-form
          ref="form"
          :model="form"
          label-width="130px"
          style="padding-top: 20px"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item label="审核状态：" prop="codeName">
                <el-select v-model="form.refundStatus" placeholder="审核状态" clearable>
                  <el-option v-for="s in refundStatuss" :label="s.name" :value="s.id" :key="s.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="审核说明：" prop="remark">
                <el-input
                  style="width: 350px"
                  v-model="form.auditRemark"
                  type="textarea"
                  :autosize="{ minRows: 2 }"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="btnc" >
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import moment from "moment"
import titleTab from "@/components/Title/index.vue"
import { getToken } from "@/utils/auth"
import { getAuditList, getInfo, doAudit } from './api/refundAuditList'
export default {
  name: 'refundAuditList',
  components: {
    titleTab
  },
  props: {},
  dicts: ["bd_refund_status"],
  data () {
    return {
      labelPosition: 'right',
      refundStatuss: [{
        id: '1',
        name: '审核通过'
        },
        {
          id: '2',
          name: '审核不通过'
      }],
      queryForm: {
        orderNo: '',
      },
      tableData: [],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      loading: false,
      detail: {},
      detailShow: false,
      carTable: [],
      nowClickId: null,
      nowCar: '',
      addShow: false,
      userId: null,
      carLoading: false,
      form: {
        id:0,
        applyUserId: 0,
        refundStatus: "",
        auditRemark: "",
      },
    }
  },
  computed: {},
  watch: {},
  methods: {
    // 获取会员列表
    async getList () {
      this.loading = true
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        ...this.queryForm
      }
      let res = await getAuditList(param)
      if (res.code == 200) {
        this.loading = false
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
    },
    // 获取审核记录详情接口
    async getInfo (id) {
      let res = await getInfo(id)
      if (res.code == 200) {
        this.form = res.data;
        if(res.data.refundStatus == '0'){
          this.form.refundStatus = '';
        }
      }
    },
    submit () {
      this.loading = true;
      doAudit(this.form).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.detailShow = false;
        this.handleQuery();
      });
    },
    handleSubmit(row){
      this.detailShow = true;
      this.getInfo(row.id);
    },
    handleQuery () {
      this.pageInfo.pageNum = 1
      this.getList()
    },
    resetQuery () {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.dialogOpen = false;
      this.$emit("cancel", false);
    },
    // 表单重置
    reset() {
      this.form = {
        id:0,
        applyUserId: 0,
        refundStatus: "",
        auditRemark: "",
      };
      this.resetForm("form");
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

.auditList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  .memberBox {
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

.auditDialog {
  .btnc {
    display: flex;
    justify-content: center;
  }

}

.detailDialog ::v-deep .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar {
  width: 6px;
}

.detailDialog ::v-deep .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 10px;
  box-shadow: inset 0 0 5px rgba(112, 173, 236, 0.2);
  background: rgba(112, 173, 236, 0.2);
}

.detailDialog ::v-deep .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar-track {
  box-shadow: inset 0 0 5px rgba(112, 173, 236, 0.2);
  border-radius: 0;
  background: rgba(112, 173, 236, 0.1);
}

.auditList ::v-deep .el-form-item {
  display: flex;
}

.auditList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>
