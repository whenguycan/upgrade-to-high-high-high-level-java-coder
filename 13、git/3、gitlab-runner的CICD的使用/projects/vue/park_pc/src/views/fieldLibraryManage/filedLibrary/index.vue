<template>
  <div class="filedLibrary">
    <page-title title="车场列表">
      <div>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:dept:add']">新增</el-button>
      </div>
    </page-title>
    <el-table v-if="refreshTable" v-loading="loading" :data="deptList" row-key="deptId" :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column prop="deptName" label="场库名称" width="200"></el-table-column>
      <el-table-column prop="parkNo" label="编号" width="150"></el-table-column>
      <!-- <el-table-column prop="longitude" label="经度"></el-table-column>
      <el-table-column prop="latitude" label="纬度"></el-table-column> -->
      <el-table-column prop="leader" label="联系人"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="bank" label="绑定银行"></el-table-column>
      <el-table-column prop="bankCard" label="银行卡号"></el-table-column>
      <el-table-column prop="authCode" label="授权码"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <div v-if="scope.row.status == 0">
            <el-button size="mini" type="text" @click="handleUpdate(scope.row)"
              v-hasPermi="['system:dept:edit']">编辑</el-button>
            <el-button size="mini" style="color: red" type="text" @click="onUpdateStatus(scope.row, 1)">禁用</el-button>
          </div>
          <div v-else>
            <el-button size="mini" style="color: green" type="text" @click="onUpdateStatus(scope.row, 0)">启用</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加或编辑场库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级场库" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级场库" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="场库名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入场库名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="绑定银行" prop="bank">
              <el-input v-model="form.bank" placeholder="请输入绑定银行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行卡号" prop="bankCard">
              <el-input v-model="form.bankCard" placeholder="请输入银行卡号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授权码" prop="authCode">
              <el-input v-model="form.authCode" placeholder="请输入授权码" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="场库状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild, updateStatus } from "@/api/fieldLibraryManage/filedLibrary";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import pageTitle from "@/views/components/pageTitle"
export default {
  name: "Dept",
  dicts: ['sys_normal_disable'],
  components: { Treeselect, pageTitle },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 场库树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        deptName: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parentId: [
          { required: true, message: "上级场库不能为空", trigger: "blur" }
        ],
        deptName: [
          { required: true, message: "场库名称不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "显示排序不能为空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询场库列表 */
    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
      });
    },
    /** 转换场库数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        longitude: undefined,
        latitude: undefined,
        bank: undefined,
        bankCard: undefined,
        remark: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.deptId;
      }
      this.open = true;
      this.title = "添加场库";
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },
    onUpdateStatus(row, status) {
      console.log(row)
      console.log(status)
      let str = status == 1 ? '禁用' : '启用'
      this.$modal
        .confirm(`是否确认${str}?`)
        .then(
          () => {
            updateStatus({
              deptId: row.deptId,
              status: status
            }).then(response => {
              this.$modal.msgSuccess("操作成功");
              this.getList();
            });
          }
        )
        .catch(() => { });
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      getDept(row.deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑场库";
        listDeptExcludeChild(row.deptId).then(response => {
          this.deptOptions = this.handleTree(response.data, "deptId");
          if (this.deptOptions.length == 0) {
            const noResultsOptions = { deptId: this.form.parentId, deptName: this.form.parentName, children: [] };
            this.deptOptions.push(noResultsOptions);
          }
        });
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名称为"' + row.deptName + '"的数据项？').then(function () {
        return delDept(row.deptId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    }
  }
};
</script>


<style scoped lang='scss'>
.filedLibrary {
  background-color: #fff;
  height: calc(100vh - 40px);
  margin: 16px;
}
</style>