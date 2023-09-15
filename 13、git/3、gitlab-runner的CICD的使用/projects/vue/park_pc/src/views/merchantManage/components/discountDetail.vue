<!-- 商户优惠券明细 -->
<template>
  <div>
    <page-title title="查询条件" />
    <div class="py-14px">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
        <el-form-item label="选择商户：" prop="userId">
          <merchant-selector @change="selectMerchant" />
        </el-form-item>
        <el-form-item label="优惠券状态：" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择" @change="handleQuery">
            <el-option
              v-for="item in couponStatusOption"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100px;" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <page-title title="商户优惠券列表" />
    <div class="py-14px">
      <el-table v-loading="loading" :data="tableList">
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column label="优惠券编号" align="center" prop="couponCode" />
        <el-table-column label="购买日期" align="center" prop="allocatedTime" />
        <el-table-column label="使用状态" align="center" prop="couponStatus">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;justify-content: center;">
              <span
                :class="`${
                  [0, 1].includes(+scope.row.couponStatus) ? 'statusIcon' : 'statusGreyIcon'
                }`"
              ></span>
              <span class="statusName">{{
                couponStatusOption.find(item => item.value == scope.row.couponStatus).label
              }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="使用日期" align="center" prop="usedTime" />
        <el-table-column label="备注" align="center" prop="remark" />
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    </div>
  </div>
</template>

<script>
import PageTitle from '@/views/components/pageTitle'
import { listMerchantDiscount } from '../api'
import MerchantSelector from './merchantSelector.vue'
 
export default {
  components: { PageTitle, MerchantSelector },
  data() {
    return {
      loading: false,
      merchantList: [],
      tableList: [],
      couponStatusOption: [
        { value: undefined, label: '全部' },
        { value: 0, label: '未分配' },
        { value: 1, label: '已分配' },
        { value: 2, label: '已使用' },
        { value: 3, label: '失效' },
      ],
      showInfo: false,
      selectedInfo: undefined,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        couponStatus: undefined,
        delflag: '0'
      }
    }
  },
  methods: {
    getList() {
      listMerchantDiscount(this.queryParams).then(res => {
        this.total = res.total
        this.tableList = res.rows
      })
    },
    selectMerchant(value) {
      this.queryParams.userId = value
      this.handleQuery()
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
  }
}
</script>

<style scoped lang="scss">
.py-14px {
  padding: 0 14px;
}
.statusIcon {
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #52c41a;
  border-radius: 3px;
  border: 0px solid rgba(0, 0, 0, 0.88);
  margin-right: 6px;
}
.statusGreyIcon {
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #9f9f9f;
  border-radius: 3px;
  border: 0px solid rgba(0, 0, 0, 0.88);
  margin-right: 6px;
}
</style>