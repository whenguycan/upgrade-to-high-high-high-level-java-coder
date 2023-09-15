<!-- 商户信息管理 -->
<template>
  <div>
    <page-title title="查询条件" />
    <div class="py-14px">
      <el-form :model="queryParams" ref="queryForm" size="mini">
        <el-form-item label="商户名称：" prop="nickName">
          <div style="display: flex;">
            <el-input v-model="queryParams.nickName" style="width: 200px;" />
            <div style="margin-left: 40px;display: flex;width: 450px;justify-content: space-around;">
              <el-button type="primary" @click="handleQuery"><span style="width: 48px;display: block;">查询</span></el-button>
              <el-button @click="resetQuery"><span style="width: 48px;display: block;">重置</span></el-button>
              <el-button type="primary">导出所有</el-button>
              <el-button type="primary">导入记录</el-button>
            </div>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <page-title title="商户管理列表" />
    <div class="py-14px">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" style="margin-bottom: 14px;">新增</el-button>
      <el-table v-loading="loading" :data="merchantList">
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column label="商户名称" align="center" prop="nickName" />
        <el-table-column label="联系方式" align="center" prop="phonenumber" />
        <el-table-column label="店户地址" align="center" prop="address" />
        <el-table-column label="使用停车场" align="center" prop="dept.deptName" />
        <el-table-column label="商户储值（元）" align="center">
          <template slot-scope="scope">
            {{ scope.row.accountValue + scope.row.giveValue }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleOpenDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handlePayback(scope.row)">退费</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">退店</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    </div>
    <el-dialog title="商户信息" v-if="showInfo" :visible.sync="showInfo" width="600px">
      <el-form ref="form" :model="dialogData" :rules="isAdd ? rules : undefined" label-position="right" label-width="150px">
        <el-form-item label="商户名称：" prop="nickName">
          <el-input v-if="isAdd" v-model="dialogData.nickName" placeholder="请输入" />
          <span v-else>{{ dialogData.nickName }}</span>
        </el-form-item>
        <el-form-item label="商户地址：" prop="address">
          <el-input v-if="isAdd" v-model="dialogData.address" placeholder="请输入" />
          <span v-else>{{ dialogData.address }}</span>
        </el-form-item>
        <el-form-item label="使用停车场：" prop="deptId">
          <el-select v-if="isAdd" v-model="dialogData.deptId" placeholder="请选择停车场">
            <el-option 
              v-for="item in parks"
              :key="item.deptId"
              :value="item.deptId"
              :label="item.deptName"
            />
          </el-select>
          <span v-else>{{ dialogData.dept.deptName }}</span>
        </el-form-item>
        <el-form-item label="联系方式：">
          <el-input v-if="isAdd" v-model="dialogData.phonenumber" placeholder="请输入" />
          <span v-else>{{ dialogData.phonenumber }}</span>
        </el-form-item>
        <el-form-item v-if="!isAdd" label="商户储值（元）：">
          <span>{{ dialogData.accountValue + dialogData.giveValue }}</span>
        </el-form-item>
        <el-form-item v-if="!isAdd"  label="优惠券存量（张）：">
          <span>{{ dialogData.count }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="isAdd" type="primary" @click="submit">提 交</el-button>
        <el-button v-else type="primary" @click="showInfo = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PageTitle from '@/views/components/pageTitle'
import { listMerchant, merchantDetail, addMerchant } from '../api'
import { listDept } from "@/api/system/dept.js";

const initForm = {
  nickName: undefined,
  address: undefined,
  deptId: undefined,
  phonenumber: undefined,
  giveValue: undefined,
  count: undefined
}
 
export default {
  components: { PageTitle },
  data() {
    return {
      loading: false,
      merchantList: [],
      parks: [],
      showInfo: false,
      isAdd: false,
      dialogData: {...initForm},
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        delflag: '0'
      },
      rules: {
        nickName: [{ required: true, message: '商户名称不能为空', trigger: 'blur' }],
        address: [{ required: true, message: '商户地址不能为空', trigger: 'blur' }],
        deptId: [{ required: true, message: '请选择使用停车场', trigger: 'blur' }],
        phonenumber: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
      }
    }
  },
  watch: {
    'showInfo'(val) {
      if(!val) {
        this.initForm()
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询停车场信息 */
    getParks() {
      listDept().then((response) => {
        this.parks = response.data;
      });
    },
    getList() {
      this.loading = true
      listMerchant(this.queryParams).then(res => {
        this.total = res.total
        this.merchantList = res.rows
        this.loading = false
      })
    },
    initForm() {
      this.isAdd = false
      this.dialogData = {...initForm}
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.getParks()
      this.showInfo = true
      this.isAdd = true
    },
    handleOpenDetail(row) {
      merchantDetail(row.userId).then(res => {
        this.dialogData = res.data
        this.showInfo = true
      })
    },
    handlePayback() {
      console.log('退费')
    },
    handleDelete() {
      console.log('退店')
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const data = {
            ...this.dialogData,
            userName: this.dialogData.nickName,
            password: '123456',
          }
          addMerchant(data).then(res => {
            this.$modal.msgSuccess('新增成功');
              this.showInfo = false
              this.initForm()
              this.getList();
          })
        }
      }); 
    }
  }
}
</script>

<style scoped lang="scss">
.py-14px {
  padding: 0 14px;
}
</style>