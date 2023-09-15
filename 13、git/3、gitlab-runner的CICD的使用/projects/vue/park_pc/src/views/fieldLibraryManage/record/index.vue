<template>
  <div class="record">

    <page-title title="起落杆记录列表">
    </page-title>
    <el-table v-loading="loading" :data="postList">
      <el-table-column label="序号" align="center" type="index" width="80">
        <template slot-scope="scope">
          <span>{{
            scope.$index + (queryParams.pageNum - 1) * queryParams.pageSize + 1
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="场库" align="center" prop="parkName" />
      <el-table-column label="通道" align="center" prop="passageName" />
      <el-table-column label="时间" align="center" prop="createTime" />
      <el-table-column label="操作类型" align="center" prop="operateType">
        <template slot-scope="scope">
          <span>{{ showDictLabel(dict.type.operate_type, scope.row.operateType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- 添加或修改起落杆记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="fieldName">
          <el-input v-model="form.fieldName" placeholder="请输入起落杆记录名称" />
        </el-form-item>
        <el-form-item label="车位数" prop="spaceCount">
          <el-input type="number" v-model="form.spaceCount" placeholder="请输入车位数" />
        </el-form-item>
        <el-form-item label="状态" prop="fieldStatus">
          <el-radio-group v-model="form.fieldStatus">
            <el-radio v-for="dict in dict.type.field_status" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
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
import { mapGetters } from "vuex";
import { listRecords, getRecords, delRecords, addRecords, updateRecords } from "@/api/fieldLibraryManage/record";
import pageTitle from "@/views/components/pageTitle"
export default {
  name: "Post",
  dicts: ['field_status', 'operate_type'],
  components: { pageTitle },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 起落杆记录表格数据
      postList: [],
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
        fieldName: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        spaceCount: [
          { required: true, message: "车位数不能为空", trigger: "blur" },
        ],
      }
    };
  },
  computed: {
    ...mapGetters(["parkInfo"]),
  },
  created() {
    this.getList();
  },
  methods: {
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    /** 查询起落杆记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkNo = this.parkInfo.deptId
      listRecords(this.queryParams).then(response => {
        this.postList = response.rows;
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
        fieldName: undefined,
        spaceCount: undefined,
        fieldName: undefined,
        fieldStatus: "1",
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加起落杆记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getRecords(row.id).then(response => {
        this.form = response;
        this.open = true;
        this.title = "修改起落杆记录";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {

          if (this.form.id != undefined) {
            updateRecords(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.parkNo = this.parkInfo.deptId
            addRecords(this.form).then(response => {
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
      // const postIds = row.postId || this.ids;
      this.$modal.confirm('是否确认删除起落杆记录名称为"' + row.fieldName + '"的数据项？').then(function () {
        return delRecords({ id: row.id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style scoped lang='scss'>
.record {
  background-color: #fff;
  height: calc(100vh - 40px);
  margin: 15px;

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
}
</style>