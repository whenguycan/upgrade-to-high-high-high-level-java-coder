<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 15:42:34
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-13 15:29:10
 * @文件相对于项目的路径: \park_pc\src\views\notice\components\noticeList.vue
-->
<template>
  <div class="noticeList">
    <div class="noticeBox" style="width:100%;">
      <titleTab title="查询条件"></titleTab>
      <el-form class="searchBox" :model="queryForm" ref="queryForm" size="small" label-width="100px">
        <el-form-item label="消息类型：" prop="sendTime">
          <el-select v-model="queryForm.sendTime" placeholder="请选择消息类型" clearable style="width:220px;">
            <el-option v-for="dict in dict.type.notice_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="推送时间：" prop="time">
          <el-date-picker v-model="queryForm.time" type="datetimerange" range-separator="至" start-placeholder="开始时间"
            end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="接收人：" prop="userPhone">
          <el-input v-model="queryForm.userPhone" placeholder="请输入接收人手机号" clearable style="width:220px;" />
        </el-form-item>
        <el-form-item style="margin-left:30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <titleTab title="消息记录"></titleTab>
      <div class="importDiv">
        <el-button type="primary" size="mini" @click="handleExport">导出所有</el-button>
      </div>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="50">
          </el-table-column>
          <el-table-column label="消息类型" min-width="160" align="center" prop="sendTime">
            <template slot-scope="scope">
                {{ showDictLabel(dict.type.notice_type,scope.row.sendTime)}}
              </template>
          </el-table-column>
          <el-table-column label="推送时间" min-width="220" align="center" prop="notifyTime">
          </el-table-column>
          <el-table-column label="接收人" min-width="160" align="center" prop="userPhone">
          </el-table-column>
          <el-table-column label="消息内容" min-width="320" align="center" prop="comment">
          </el-table-column>
          <el-table-column label="推送状态" min-width="120" align="center" prop="status">
            <template slot-scope="scope">
              <div class="statusDiv">
                <span :class="scope.row.status == 1 ?'statusDotGreen': scope.row.status == 0 ? 'statusDotYellow' : 'statusDotRed'"></span>
                <span class="statusCon">{{ scope.row.status == 1 ? '成功' : scope.row.status == 0 ? '未响应' : '失败' }}</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="pageInfo.total > 0" :total="pageInfo.total" :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue"
import {getNoticeRecordList} from "../api/notice.js"
export default {
  name: 'noticeList',
  components: {
    titleTab
  },
  dicts: ['notice_type'],
  props: {},
  data () {
    return {
      labelPosition: 'right',
      queryForm: {
        sendTime:'',
        time: [],
        userPhone:''
      },
      tableData: [],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      loading: false,
    }
  },
  computed: {},
  watch: {},
  methods: {
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    async getList () {
      this.loading = true
      let param = {
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
        sendTime:this.queryForm.sendTime,
        startTime: this.queryForm.time[0],
        endTime: this.queryForm.time[1],
        userPhone:this.queryForm.userPhone,
      }
      let res = await getNoticeRecordList(param)
      if (res.code == 200) {
        this.loading = false
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
      this.download('parking/notificationRecord/export', {
        sendTime:this.queryForm.sendTime,
        startTime: this.queryForm.time[0],
        endTime: this.queryForm.time[1],
        userPhone:this.queryForm.userPhone,
      }, `notice_${new Date().getTime()}.xlsx`)
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
.noticeList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  .noticeBox {
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
    .statusDiv{
      display:flex;
      align-items: center;
      justify-content: center;
    }

    .statusDotGreen {
      width: 6px;
      height: 6px;
      background: #52C41A;
      border-radius: 50%;
    }
    .statusDotYellow{
      width: 6px;
      height: 6px;
      background: #f1c24d;
      border-radius: 50%;
    }

    .statusDotRed {
      width: 6px;
      height: 6px;
      background: #FF1838;
      border-radius: 50%;
    }

    .statusCon {
      margin-left:5px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.88);
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

.noticeList ::v-deep .el-form-item {
  display: flex;
}

.noticeList ::v-deep .el-form-item__content {
  margin-left: 5px !important;
}
</style>