<template>
  <el-select
    v-model="merchantId"
    placeholder="请选择商户（支持搜索）"
    filterable
    remote
    :remote-method="remoteMethod"
    @change="handleSelect"
  >
    <div v-loading="loading">
      <el-option
        v-for="item in merchantList"
        :key="item.userId"
        :label="item.nickName"
        :value="item.userId"
      />
    </div>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
      layout="prev, pager, next, total"
      style="bottom: 8px;"
    />
  </el-select>
</template>

<script>
import { listMerchant } from '../api';

export default {
  data() {
    return {
      loading: false,
      merchantList: [],
      total: 0,
      merchantId: undefined,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        delflag: '0',
        nickName: undefined,
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      listMerchant(this.queryParams).then(res => {
        this.total = res.total
        this.merchantList = res.rows
      })
    },
    remoteMethod(query) {
      if (query !== '') {
        this.queryParams.nickName = query
        this.getList()
      } else {
        this.merchantList = [];
      }
    },
    handleSelect(value) {
      this.$emit('change', value)
    }
  }
}
</script>
