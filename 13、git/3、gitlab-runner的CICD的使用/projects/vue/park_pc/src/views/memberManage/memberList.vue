<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-17 17:30:55
 * @文件相对于项目的路径: \park_pc\src\views\memberManage\memberList.vue
-->
<template>
  <div class="memberList">
    <div class="memberBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="100px">
        <el-form-item label="手机号：" prop="phonenumber">
          <el-input v-model="queryForm.phonenumber" placeholder="请输入手机号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="会员列表"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="昵称" min-width="180" align="center" prop="nickName">
          </el-table-column>
          <el-table-column label="手机号" min-width="180" align="center" prop="phonenumber">
          </el-table-column>
          <el-table-column label="绑定车辆数" min-width="160" align="center" prop="boundNum">
          </el-table-column>
          <el-table-column label="注册时间" min-width="260" align="center" prop="createTime">
            <template slot-scope="scope">
              {{ scope.row.createTime | chatTime }}
            </template>
          </el-table-column>
          <el-table-column label="最近登录时间" min-width="260" align="center" prop="loginDate">
            <template slot-scope="scope">
              {{ scope.row.loginDate | chatTime }}
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="120" align="center" prop="remark">
          </el-table-column>
          <el-table-column width="120" fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" class="colorHollowBtnCyan"
                @click="detailMemeber(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
    <!-- 会员详情弹框 -->
    <el-dialog class="detailDialog" title="会员详情" :visible.sync="detailShow" width="962px" append-to-body>
      <div class="detailCon">
        <div class="detailConTop">
          <el-row>
            <el-col :span="8">
              <div class="grid-content">
                <span class="tit">会员昵称：</span>
                <span class="con">{{ detail.nickName }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content">
                <span class="tit">会员手机号：</span>
                <span class="con">{{ detail.phonenumber }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content">
                <span class="tit">绑定车辆数：</span>
                <span class="con">{{ detail.boundNum }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="detailConCenter">
          <div class="conLeft">
            <div class="conTop">
              <titleTab title="会员车辆列表"></titleTab>
            </div>
            <div class="conBox">
              <div class="addDiv">
                <el-button type="primary" size="mini" icon="el-icon-plus" @click="addCar">新增</el-button>
                <div class="addCarForm" v-if="addShow">
                  <el-form class="addCarBox" :model="addCarForm" ref="addCarForm" size="small" label-width="0px">
                    <el-form-item label="" prop="carNumber">
                      <el-input v-model="addCarForm.carNumber" placeholder="请输入车牌号" clearable style="width:160px;" />
                    </el-form-item>
                    <el-form-item style="margin-left:10px;">
                      <el-button type="primary" size="mini" @click="addCarConfirm">确定</el-button>
                      <el-button size="mini" @click="closeCar">取消</el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
              <div class="carList">
                <el-table :data="carTable" :row-class-name="tableRowClassName" @row-click="getOrderRow">
                  <el-table-column label="序号" align="center" type="index" width="50">
                  </el-table-column>
                  <el-table-column label="车牌号" min-width="140" align="center" prop="carNo">
                  </el-table-column>
                  <el-table-column width="100" label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" class="colorHollowBtnCyan"
                        @click="unbindCar(scope.row)">解绑</el-button>
                      <el-button v-if="scope.row.defaultFlag == '0'" size="mini" type="text" class="colorHollowBtnCyan"
                        @click="setDefaultCar(scope.row)">设为默认</el-button>
                      <span v-else class="defalutSpan">默认</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="conRight">
            <div class="conTop">
              <titleTab title="车辆订单记录"></titleTab>
            </div>
            <div class="conBox">
              <div class="searchCon">
                <el-form class="searchBox" :model="orderForm" ref="orderForm" size="small" label-width="90px">
                  <el-form-item label="入场时间：" prop="entryTime">
                    <el-date-picker v-model="orderForm.entryTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="选择入场时间" style="width:185px;">
                    </el-date-picker>
                  </el-form-item>
                  <el-form-item label="出场时间：" prop="exitTime">
                    <el-date-picker v-model="orderForm.exitTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="选择出场时间" style="width:185px;">
                    </el-date-picker>
                  </el-form-item>
                </el-form>
              </div>
              <div class="subBox">
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleOrderQuery">查询</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetOrderQuery">重置</el-button>
              </div>
              <div class="orderTable">
                <el-table :data="orderTable" v-loading="carLoading" height="310">
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
                </el-table>
                <pagination v-show="orderPageInfo.total > 0" :total="orderPageInfo.total"
                  :page.sync="orderPageInfo.pageNum" :limit.sync="orderPageInfo.pageSize" @pagination="getOrderList" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import moment from "moment"
import titleTab from "@/components/Title/index.vue"
import { getToken } from "@/utils/auth"
import { getMemberList, getMember, getMemberDetail, memberBindCar, memberUnBindCar, setDefault } from './api/memberList'
import { getOrderList } from "@/views/statistic/api/orderList.js"
export default {
  name: 'memberList',
  components: {
    titleTab
  },
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        phonenumber: '',
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
      orderTable: [],
      orderPageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      orderForm: {
        entryTime: '',
        exitTime: ''
      },
      addShow: false,
      addCarForm: {
        carNumber: ''
      },
      userId: null,
      carLoading: false
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
      let res = await getMemberList(param)
      if (res.code == 200) {
        this.loading = false
        this.tableData = res.rows
        this.pageInfo.total = res.total
      }
    },
    detailMemeber (row) {
      this.userId = row.userId
      this.getDetail(row.userId)
    },
    // 获取会员详情
    async getMember (id) {
      let res = await getMember(id)
      if (res.code == 200) {
        this.detail = res.data
      }
    },
    // 获取会员车辆
    async getDetail (id) {
      let res = await getMemberDetail(id)
      if (res.code == 200) {
        this.getMember(id)
        this.carTable = res.data
        this.resetOrderTable()
        this.nowClickId = null
        this.detailShow = true
      }
    },
    resetOrderTable () {
      this.orderPageInfo = {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      }
      this.resetForm("orderForm")
      this.orderTable = []
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
      this.download('member/user/export', {
        ...this.queryForm
      }, `member_${new Date().getTime()}.xlsx`)
    },
    addCar () {
      this.addCarForm = {
        carNumber: ''
      }
      this.addShow = true
    },
    // 新增会员车辆
    async addCarConfirm () {
      let param = {
        userId: this.userId,
        carNo: this.addCarForm.carNumber,
        validTime: moment(new Date()).format('YYYY-MM-DD hh:ss:mm'),
        defaultFlag: 0
      }
      let res = await memberBindCar(param)
      if (res.code == 200) {
        this.$message.success("新增车辆成功！")
        this.getDetail(this.userId)
        this.getList()
        this.addShow = false
      } else {
        this.$message.error("新增车辆失败！")
        this.addShow = false
      }
    },
    closeCar () {
      this.addCarForm = {
        carNumber: ''
      }
      this.addShow = false
    },
    unbindCar (row) {
      this.$confirm("确定为会员解绑该车辆？", "解绑提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          memberUnBindCar(row.id)
            .then((res) => {
              if (res.code === 200) {
                this.$message.success("解绑车辆成功！")
                this.getDetail(this.userId)
                this.getList()
              }
            })
            .catch((errMsg) => {
              this.$message.error(errMsg)
            })
        })
        .catch(() => { })
    },
    async setDefaultCar (row) {
      this.$confirm("确定为会员解设定该车辆为默认？", "设置提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          let param = {
            userId: this.userId,
            id: row.id,
          }
          let res = await setDefault(param)
          if (res.code == 200) {
            this.$message.success("设为默认车辆成功！")
            this.getDetail(this.userId)
          } else {
            this.$message.error("设为默认车辆失败！")
          }
        })
        .catch(() => { })
    },
    getOrderRow (row, column, event) {
      this.nowClickId = row.id
      this.nowCar = row.carNo
      this.getOrderList()
    },
    async getOrderList () {
      this.carLoading = true
      let param = {
        pageSize: this.orderPageInfo.pageSize,
        pageNum: this.orderPageInfo.pageNum,
        memberId: this.userId,
        carNumber: this.nowCar,
        ...this.orderForm
      }
      let res = await getOrderList(param)
      if (res.code == 200) {
        this.carLoading = false
        this.orderTable = res.rows
        this.orderPageInfo.total = res.total
      }
    },
    tableRowClassName ({ row, rowIndex }) {
      if (row.id === this.nowClickId) {
        return "highlight-row"
      }
      return ""
    },
    handleOrderQuery () {
      this.orderPageInfo.pageNum = 1
      this.getOrderList()
    },
    resetOrderQuery () {
      this.resetForm("orderForm")
      this.handleOrderQuery()
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

.memberList {
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

.detailDialog {
  .detailCon {
    height: 580px;
    display: flex;
    flex-direction: column;

    .detailConTop {
      background: #FFFFFF;
      box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
      border-radius: 4px;
      padding: 10px 10px;

      .grid-content {
        display: flex;
        align-items: center;
        line-height: 20px;

        .tit {
          font-weight: 600;
        }
      }
    }

    .detailConCenter {
      margin-top: 10px;
      display: flex;
      justify-content: space-between;
      flex: 1;
      height: 500px;
    }

    .conLeft {
      width: 35%;
      background: #FFFFFF;
      box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
      border-radius: 4px;
      padding: 10px 0;
      display: flex;
      flex-direction: column;

      .conBox {
        padding: 0 5px;
        flex: 1;
        display: flex;
        flex-direction: column;
        height: 458px;

        .addDiv {
          margin: 10px 0;

          .addCarForm {
            padding: 10px 0 0;

            .addCarBox {
              display: flex;
              flex-wrap: wrap;

              .el-form-item--small.el-form-item {
                margin-bottom: 0px;
              }
            }

          }
        }

        .defalutSpan {
          font-size: 12px;
          border: 1px solid #fea90c;
          padding: 3px 8px;
          border-radius: 3px;
          color: #fea90c;
          margin-left: 10px;
        }

        .carList {
          flex: 1;
          overflow-y: auto;
        }

        .carList::-webkit-scrollbar {
          width: 6px;
        }

        .carList::-webkit-scrollbar-thumb {
          border-radius: 10px;
          box-shadow: inset 0 0 5px rgba(112, 173, 236, 0.2);
          background: rgba(112, 173, 236, 0.2);
        }

        .carList::-webkit-scrollbar-track {
          box-shadow: inset 0 0 5px rgba(112, 173, 236, 0.2);
          border-radius: 0;
          background: rgba(112, 173, 236, 0.1);
        }
      }

    }

    .conRight {
      width: 64%;
      background: #FFFFFF;
      box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
      border-radius: 4px;
      padding: 10px 0;
      display: flex;
      flex-direction: column;

      .conBox {
        padding: 10px 5px;
        flex: 1;
        display: flex;
        flex-direction: column;

        .searchCon {
          .searchBox {
            padding: 10px 10px;
            display: flex;
            flex-wrap: wrap;

            // align-items: center;
            ::v-deep .el-form-item--small.el-form-item {
              margin-bottom: 10px;
            }
          }
        }

        .subBox {
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .orderTable {
          margin-top: 10px;
          width: 100%;
          height: 370px;
        }
      }
    }
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

.memberList ::v-deep .el-form-item {
  display: flex;
}

.memberList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>
