<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-21 18:01:54
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-07 10:19:26
 * @文件相对于项目的路径: /park_pc/src/views/basicSettings/components/liftgatereason.vue
-->
<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      label-width="88px"
    >
      <el-form-item label="抬杠原因" prop="reason">
        <el-input
          v-model="queryParams.reason"
          placeholder="请输入抬杠原因"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="场库编号" prop="parkNo">
        <el-input
          v-model="queryParams.parkNo"
          placeholder="请输入场库编号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="typeList">
      <el-table-column label="序号" width="180" align="center">
        <template slot-scope="scope">
          <span>{{
            scope.$index + (queryParams.pageNum - 1) * queryParams.pageSize + 1
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="抬杠原因" align="center" prop="reason" />
      <el-table-column
        label="场库编号"
        align="center"
        prop="parkNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建人"
        align="center"
        prop="createBy"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="备注"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            class="delete"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="抬杠原因" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入抬杠原因" />
        </el-form-item>
        <!-- <el-form-item label="场库编号" prop="parkNo">
          <el-input v-model="form.parkNo" placeholder="请输入场库编号" />
        </el-form-item> -->
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSettingliftgatereasonList,
  AddSettingliftgatereason,
  EditSettingliftgatereason,
  removeSettingliftgatereason,
} from "../api";

export default {
  name: "liftgatereason",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reason: [
          { required: true, message: "抬杠原因不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询类型列表 */
    getList() {
      this.loading = true;
      getSettingliftgatereasonList(this.queryParams).then((response) => {
        this.typeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        dictId: undefined,
        parkNo: undefined,
        code: undefined,
        status: "0",
        remark: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增车辆类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.form = {
        reason: row.reason,
        code: row.code,
        parkNo: row.parkNo,
        remark: row.remark,
        id: row.id,
      };
      this.open = true;
      this.title = "编辑车辆类型";
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            EditSettingliftgatereason(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            AddSettingliftgatereason(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认删除")
        .then(function () {
          return removeSettingliftgatereason(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.delete {
  color: red;
}
</style>
