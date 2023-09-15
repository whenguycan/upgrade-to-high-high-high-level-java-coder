<template>
  <div class="bindDevice">
    <div class="tab-title">已选设备</div>
    <el-table v-loading="loading" :data="selectList">
      <el-table-column label="序号" align="center" type="index" width="80">
        <template slot-scope="scope">
          <span>{{
            scope.$index + 1
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
          <el-button size="mini" type="text" style="color: red" @click="handleUpdate(scope.row, '0')">解绑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="tab-title">待选设备</div>
    <el-table v-loading="loading2" :data="unselectList">
      <el-table-column label="序号" align="center" type="index" width="80">
        <template slot-scope="scope">
          <span>{{
            scope.$index + 1
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
          <el-button size="mini" style="color: green" type="text" @click="handleUpdate(scope.row, '1')">绑定</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listdevice, getdevice, deldevice, adddevice, updatedevice } from "@/api/fieldLibraryManage/device";
import pageTitle from "@/views/components/pageTitle"
export default {
  name: "Post",
  dicts: ['field_status', 'device_type'],
  components: { pageTitle },
  props: {
    passageId: {
      type: [String, Number],
      required: true
    },
  },
  watch: {
    passageId: {
      handler(val) {
        if (val != '') {
          this.passage = val
          console.log('绑定列表')
          this.getList(val)
        } else {
          console.log('未选')
          return false;
        }
      },
      immediate: true
    }
  },
  data() {
    return {
      passage: '',
      // 遮罩层
      loading: false,
      loading2: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 99999,
        parkNo: 200
      },
      selectList: [], //已选设备
      unselectList: [], //未选设备
    };
  },
  created() {
    // this.getList();
  },
  methods: {
    /** 修改按钮操作 */
    handleUpdate(row, deviceStatus) {

      let str = deviceStatus == '1' ? '绑定' : '解绑'
      this.$modal.confirm(`是否确认${str}改设备？`).then(() => {
        updatedevice({
          deviceStatus: deviceStatus,
          passageId: this.passageId,
          deviceId: row.deviceId,
          id: row.id
        }).then(() => {
          this.getList(this.passage)
          this.$modal.msgSuccess(`${str}成功`);
        })
      }
      ).catch(() => { });
    },
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    /** 查询设备列表 */
    getList(passageId) {
      this.loading = true;
      this.loading2 = true;
      listdevice({
        ...this.queryParams,
        passageId: passageId,
        deviceStatus: '1',
      }).then(response => {
        this.selectList = response.rows;
        this.loading = false;
      });
      listdevice({
        ...this.queryParams,
        // passageId: passageId,
        deviceStatus: '0',
      }).then(response => {
        this.unselectList = response.rows;
        this.loading2 = false;
      });
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
.bindDevice {
  background-color: #fff;
  height: calc(100vh - 40px);
  padding: 0 10px;
  // margin: 15px;

  .tab-title {
    font-size: 14px;
    padding: 10px 0;
    font-weight: 600;
  }
}
</style>