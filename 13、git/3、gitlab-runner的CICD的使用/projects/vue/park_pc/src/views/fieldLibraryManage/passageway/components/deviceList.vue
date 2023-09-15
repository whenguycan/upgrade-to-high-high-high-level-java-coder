<template>
  <div class="deviceList">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="postList">
      <el-table-column label="序号" align="center" type="index" width="80">
        <template slot-scope="scope">
          <span>{{
            scope.$index + (queryParams.pageNum - 1) * queryParams.pageSize + 1
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备编号" align="center" prop="deviceId" />
      <el-table-column label="设备类型" align="center" prop="deviceType">
        <template slot-scope="scope">
          <span>{{ showDictLabel(dict.type.device_type, scope.row.deviceType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:post:edit']">修改</el-button>
          <el-button size="mini" type="text" style="color: red" @click="handleDelete(scope.row)"
            v-hasPermi="['system:post:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加或修改设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编号" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceType">
          <el-select v-model="form.deviceType" placeholder="请选择设备类型" clearable>
            <el-option v-for="dict in dict.type.device_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="IP地址" prop="serverIp">
          <el-input v-model="form.serverIp" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
import { listdevice, getdevice, deldevice, adddevice, updatedevice } from "@/api/fieldLibraryManage/device";
import pageTitle from "@/views/components/pageTitle"
export default {
  name: "Post",
  dicts: ['field_status', 'device_type'],
  components: { pageTitle },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 设备表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 99999,
        parkNo: 200
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceId: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
        deviceType: [
          { required: true, message: "请选择设备类型", trigger: "blur" },
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listdevice(this.queryParams).then(response => {
        this.postList = response.rows;
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
        deviceType: undefined,
        spaceCount: undefined,
        remark: undefined,
        // fieldStatus: "1",
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getdevice(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {

          if (this.form.id != undefined) {
            updatedevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.parkNo = this.queryParams.parkNo
            adddevice(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备名称为"' + row.fieldName + '"的数据项？').then(function () {
        return deldevice({ id: row.id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
  }
};
</script>
<style scoped lang='scss'>
.deviceList {
  background-color: #fff;
  height: calc(100vh - 40px);
  // margin: 15px;

  padding: 0 10px;
  padding-top: 10px;
}
</style>