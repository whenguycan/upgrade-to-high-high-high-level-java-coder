<template>
  <el-dialog title="人工查找" :visible.sync="visable" width="904px">
    <el-dialog title="车辆照片" :visible.sync="isShowImg" append-to-body width="500px">
      <div style="width: 100%; display: flex;justify-content: center;">
        <ImagePreview :src="carImgUrl" />
      </div>
    </el-dialog>
    <el-dialog
      :title="isEdit ? '修改车牌' : '批量修改入场时间'"
      :visible.sync="showInner"
      @closed="handleClose"
      append-to-body
      destroy-on-close
      width="500px"
    >
      <el-form ref="form" :model="form" :rules="rules" style="padding: 0 10%;">
        <el-form-item v-if="isEdit" label="车牌号">
          <el-input v-model="form.carNumber" />
        </el-form-item>
        <el-form-item v-else label="入场时间">
          <el-date-picker
            v-model="form.entryTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showInner = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="loadingSubmit" :disabled="disabled">确 定</el-button>
      </span>
    </el-dialog>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="区域名称">
        <el-input v-model="queryParams.fieldName" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item>
      <el-form-item label="通道名称">
        <el-input v-model="queryParams.passageName" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item>
      <el-form-item label="车辆类型">
        <el-input v-model="queryParams.carType" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item>
      <el-form-item label="车牌">
        <el-input v-model="queryParams.carNumber" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item>
      <el-form-item label="时间段">
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
      <el-form-item>
        <div style="width: 864px;display: flex;justify-content: center;">
          <el-button type="primary" style="width: 108px;" @click="handleQuery">查询</el-button>
          <el-button style="width: 108px;">重置</el-button>
        </div>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableList" @select="handleSelect" @select-all="handleSelect">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column property="carNumber" label="车牌" width="100"></el-table-column>
      <el-table-column property="carType" label="车辆类型" width="100"></el-table-column>
      <el-table-column property="passageName" label="通道名称"></el-table-column>
      <el-table-column label="查看车辆照片">
        <template slot-scope="scope">
          <el-button type="text" @click="showPhoto(scope.row.carImgUrl)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column property="entryTime" label="入场时间"></el-table-column>
      <el-table-column property="isOpen" label="是否开闸"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editCarNo(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex;">
      <el-button type="text" :disabled="selection.length === 0" @click="showInner = true">批量修改</el-button>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" style="width: 100%;" />
    </div>
  </el-dialog>
</template>

<script>
import { listMonitor, editEnterCarNo, updateEnterTime } from '../api'

export default {
  data() {
    return {
      visable: false,
      loading: false,
      isShowImg: false,
      carImgUrl: undefined,
      // 控制内部弹窗显示
      showInner: false,
      // 是否是修改车牌，如果不是则打开内部弹窗为批量修改
      isEdit: false,
      originCarNo: undefined,
      loadingSubmit: false,
      selection: [],
      form: {},
      tableList: [],
      // 总条数
      total: 0,
      queryParams: {
        pageNum: undefined,
        pageSize: 10,
        fieldName: undefined,
        passageName: undefined,
        carType: undefined,
        carNumber: undefined,
        dateTimeRange: undefined
      }
    }
  },
  computed: {
    rules() {
      if (this.isEdit) {
        return {
          carNumber: [{ required: true, message: "车牌不能为空", trigger: "blur" }]
        }
      }
      return {
        entryTime: [{ required: true, message: "请选择进场时间", trigger: "blur" }]
      }
    },
    disabled() {
      if (this.isEdit) {
        return this.form?.carNumber == '' || this.form?.carNumber == this.originCarNo
      }
      return false
    }
  },
  methods: {
    showDialog() {
      this.visable = true
      this.getList()
    },
    getList() {
      this.loading = true
      const queryParams = {
        ...this.queryParams,
        beginDate: this.queryParams.dateTimeRange?.[0],
        endDate: this.queryParams.dateTimeRange?.[1],
      }
      listMonitor(queryParams).then(res => {
        this.tableList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    showPhoto(imgUrl) {
      this.carImgUrl = imgUrl
      this.isShowImg = true
    },
    closeImg() {
      this.carImgUrl = undefined
      this.isShowImg = false
    },
    editCarNo(row) {
      this.form = {...row}
      this.originCarNo = row.carNumber
      this.showInner = true
      this.isEdit = true
    },
    submitForm() {
      const fc = this.isEdit ? editEnterCarNo : updateEnterTime
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loadingSubmit = true
          const form = this.isEdit ? this.form : 
            this.selection.map(item => {
              return {
                ...item,
                entryTime: this.form.entryTime
              }
            })
          fc(form).then(res => {
            this.loadingSubmit = false
            this.$modal.msgSuccess("修改成功");
            this.showInner = false;
            this.isEdit = false
            this.form = {}
            this.getList()
          });
        }
      })
    },
    handleSelect(selection) {
      this.selection = selection
    },
    handleClose() {
      this.isEdit = false
      this.showInner = false
    }
  }
}
</script>
