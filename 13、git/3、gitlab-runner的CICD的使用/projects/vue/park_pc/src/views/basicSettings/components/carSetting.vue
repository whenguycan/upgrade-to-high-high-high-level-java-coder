<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-21 17:15:15
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-07 10:16:15
 * @文件相对于项目的路径: /park_pc/src/views/basicSettings/components/carSetting.vue
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
      <el-form-item label="车辆类型" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入车辆类型"
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
      <el-form-item label="车辆类型码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入车辆类型码"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-table-column label="车辆类型" align="center" prop="name" />
      <el-table-column
        label="场库编号"
        align="center"
        prop="parkNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="车辆类型码"
        align="center"
        prop="code"
        :show-overflow-tooltip="true"
      >
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <div class="statusI">
            <span
              :class="`${
                +scope.row.status === 1 ? 'statusIcon' : 'statusGreyIcon'
              }`"
            ></span>
            <span class="statusName">{{
              +scope.row.status === 1 ? "已启用" : "已停用"
            }}</span>
          </div>
        </template>
      </el-table-column>
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
          <el-button
            v-if="+scope.row.status === 0"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >编辑</el-button
          >
          <el-button
            v-if="+scope.row.status === 1"
            size="mini"
            type="text"
            @click="checkRow(scope.row)"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            class="delete"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            v-if="+scope.row.status === 1"
            size="mini"
            type="text"
            class="disabled"
            @click="disableSettingcartype(scope.row)"
            >停用</el-button
          >
          <el-button
            v-else
            size="mini"
            type="text"
            class="enbale"
            @click="enableSettingcartype(scope.row)"
            >启用</el-button
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
        <el-form-item label="车辆类型" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入车辆类型"
            :disabled="isCheck"
          />
        </el-form-item>
        <!-- <el-form-item label="场库编号" prop="parkNo">
          <el-input v-model="form.parkNo" placeholder="请输入场库编号" />
        </el-form-item> -->
        <el-form-item label="车辆类型码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入车辆类型码"
            :disabled="isCheck"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            :disabled="isCheck"
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
  getSettingcartypeList,
  AddSettingcartype,
  EditSettingcartype,
  removeSettingcartype,
  enableSettingcartype,
  disableSettingcartype,
} from "../api";

export default {
  name: "carSetting",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      // 遮罩层
      loading: true,
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
        name: "",
        code: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "车辆类型码不能为空", trigger: "blur" },
        ],
        name: [
          { required: true, message: "车辆类型不能为空", trigger: "blur" },
        ],
      },
      isCheck: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询类型列表 */
    getList() {
      this.loading = true;
      getSettingcartypeList(this.queryParams).then((response) => {
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
      this.isCheck = false;
      this.open = true;
      this.title = "新增车辆类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.form = {
        name: row.name,
        code: row.code,
        parkNo: row.parkNo,
        remark: row.remark,
        id: row.id,
      };
      this.isCheck = false;
      this.open = true;
      this.title = "编辑车辆类型";
    },
    checkRow(row) {
      this.reset();
      this.form = {
        name: row.name,
        code: row.code,
        parkNo: row.parkNo,
        remark: row.remark,
        id: row.id,
      };
      this.isCheck = true;
      this.open = true;
      this.title = "查看车辆类型";
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            EditSettingcartype(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            AddSettingcartype(this.form).then((response) => {
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
          return removeSettingcartype(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    disableSettingcartype(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认停用")
        .then(function () {
          return disableSettingcartype(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("停用成功");
        })
        .catch(() => {});
    },
    enableSettingcartype(row) {
      const id = row.id;
      this.$modal
        .confirm("是否确认启用")
        .then(function () {
          return enableSettingcartype(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("启用成功");
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
.enbale {
  color: #00c237;
}
.disabled {
  color: #da7800;
}

.statusI {
  display: flex;
  align-items: center;
  justify-content: center;
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
