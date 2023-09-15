<!-- 商户流水明细 -->
<template>
  <div>
    <page-title title="查询条件" />
    <div class="py-14px">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
        <el-form-item label="选择商户：" prop="operId">
          <merchant-selector  @change="selectMerchant" />
        </el-form-item>
        <el-form-item label="充值时间：" prop="dateTimeRange">
          <el-date-picker
            v-model="queryParams.dateTimeRange"
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleQuery"
          />
        </el-form-item>
        <el-form-item label="充值状态：" prop="status">
          <el-select v-model="queryParams.operatorType" placeholder="请选择" @change="handleQuery">
            <el-option 
              v-for="(item, idx) in operateOptions"
              :key="idx"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100px;">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <page-title title="商户流水列表" />
    <div class="py-14px">
      <el-table v-loading="loading" :data="tableList">
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column label="当前状态" align="center" prop="operatorType">
          <template slot-scope="scope">
            {{ operateOptions.find(item => item.value === scope.row.operatorType).label }}
          </template>
        </el-table-column>
        <el-table-column label="交易单号" align="center" prop="orderNo" />
        <el-table-column label="商户名称" align="center" prop="operName" />
        <!-- <el-table-column label="收款机构" align="center" prop="serviceName" /> -->
        <el-table-column label="金额（元）" align="center" prop="amount" />
        <el-table-column label="支付时间" align="center" prop="updateTime" />
        <el-table-column label="备注" align="center" prop="remark" />
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    </div>
  </div>
</template>

<script>
import PageTitle from '@/views/components/pageTitle'
import { listStatements } from '../api'
import merchantSelector from './merchantSelector.vue'
 
export default {
  components: { PageTitle, merchantSelector },
  data() {
    return {
      loading: false,
      merchantList: [],
      tableList: [],
      showInfo: false,
      selectedInfo: undefined,
      total: 0,
      operateOptions: [
        { label: '全部', value: undefined },
        { label: '充值', value: 1 },
        { label: '续费', value: 2 },
        { label: '使用', value: 3 },
        { label: '回收', value: 4 },
        { label: '退款', value: 5 },
        { label: '作废', value: 6 },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        operId: undefined,
        operatorType: undefined,
        dateTimeRange: undefined,
        delflag: '0'
      }
    }
  },
  methods: {
    getList() {
      let query = {...this.queryParams}
      if (!!query.dateTimeRange) {
        query = {
          ...query,
          startTime: query.dateTimeRange[0],
          endTime: query.dateTimeRange[1]
        }
      }
      listStatements(query).then(res => {
        this.total = res.total
        this.tableList = res.rows
      })
    },
    selectMerchant(value) {
      this.queryParams.operId = value
      this.handleQuery()
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    }
  }
}
</script>

<style scoped lang="scss">
.py-14px {
  padding: 0 14px;
}
</style>