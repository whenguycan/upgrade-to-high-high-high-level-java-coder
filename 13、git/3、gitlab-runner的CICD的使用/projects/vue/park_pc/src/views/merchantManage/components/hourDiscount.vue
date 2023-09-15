<!-- 小时优惠券管理 -->
<template>
  <div class="discount">
    <div class="content">
      <el-form>
        <el-form-item label="小时券使用停车场：">
          <el-select v-model="parkNo" placeholder="请选择停车场" @change="getTableList">
            <el-option 
              v-for="item in parks"
              :key="item.deptId"
              :value="item.parkNo"
              :label="item.deptName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="小时券：">
          <div style="display: flex;justify-content: space-between;align-items: center;">
          <span  class="notice">请按由小到大的顺序设定</span>
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="addNew" :disabled="!parkNo">新增</el-button>
          </div>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="discountList">
        <el-table-column label="小时券时长" prop="duration">
          <template slot-scope="scope">
            <el-input v-model="scope.row.duration" />
          </template>
        </el-table-column>
        <el-table-column label="售价（元）" prop="amount">
          <template slot-scope="scope">
            <el-input v-model="scope.row.amount" />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" style="color: red;" @click="handleDel(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div  class="btn-group">
        <el-button type="primary" @click="submit" :disabled="disabled">保存</el-button>
        <el-button @click="cancel" :disabled="disabled">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { listDept } from "@/api/system/dept.js";
import { deepClone } from '@/utils/index.js'
import { listHourDiscount, addHourDiscount } from "../api";

export default {
  data() {
    return {
      parks: [],
      parkNo: undefined,
      loading: false,
      discountList: [],
      originList: []
    }
  },
  created() {
    this.getParks()
  },
  computed: {
    disabled() {
      const originValue = JSON.stringify(this.originList)
      const editValue = JSON.stringify(this.discountList)
      return originValue === editValue
    }
  },
  methods: {
    /** 查询停车场信息 */
    getParks() {
      listDept().then((response) => {
        this.parks = response.data;
      });
    },
    getTableList() {
      this.loading = true
      const query = { parkNo: this.parkNo }
      listHourDiscount(query).then(res => {
        this.originList = JSON.parse(JSON.stringify(res.list))
        this.discountList = JSON.parse(JSON.stringify(res.list))
        this.loading = false
      })
    },
    submit() {
      // 判断当前list中有无空数据
      const check = this.discountList.find(item => item.duration == '' || item.amount == '')
      if(!!check) {
        return this.$modal.msgError('列表中存在空数据，请补充或删除！')
      }
      const data = {
        parkNo: this.parkNo,
        list: this.discountList.map(item => {
          return {
            ...item,
            parkNo: this.parkNo,
          }
        })
      }
      addHourDiscount(data).then(res => {
        this.$modal.msgSuccess('保存成功！');
        this.getTableList()
      })
    },
    addNew() {
      this.discountList.push({
        duration: '',
        amount: ''
      })
    },
    handleDel(idx, row) {
      this.discountList.splice(idx, 1)
    },
    cancel() {
      const init = () => {
        this.discountList = deepClone(this.originList)
      }
      this.$confirm('此操作将删除未保存的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        init()
        this.$message({
          type: 'success',
          message: '已取消修改！'
        });
      }).catch(() => {});
    }
  }
}
</script>

<style scoped lang="scss">
.discount {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 27px;

  .content {
    width: 50%;
    min-width: 360px;

    .notice {
      font-size: 12px;
      font-weight: 400;
      color: rgba(0,29,60,0.6);
      height: 36px;
    }
  }

  .btn-group {
    margin-top: 18px;
    display: flex;
    justify-content: center;
  }
}
</style>