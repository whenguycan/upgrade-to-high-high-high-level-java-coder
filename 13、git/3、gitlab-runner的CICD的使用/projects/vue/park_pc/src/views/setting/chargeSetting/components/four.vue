<template>
  <div class="four" ref="four">
    <el-row type="flex" style="height: 100%">
      <el-col :span="12">
        <div class="four-left">
          <common-title :text="'节假日设置'" />
          <div class="four-left-wrapper">
            <!-- <div class="button-wrapper"> -->
              <el-button size="small" icon="el-icon-date" @click="getHoliday"
                >获取国家法定节假日</el-button
              >
              <el-button
                size="small"
                type="primary"
                icon="el-icon-plus"
                @click="AddHoliday"
                >添加自定义节假日</el-button
              >
            <!-- </div> -->
            <!-- <div class="table-wrapper"> -->
              <el-table
                key="left"
                style="margin-top: 14px"
                v-loading="loadingOne"
                :data="tableDataOne"
                :max-height="maxHeight"
              >
                <el-table-column
                  label="序号"
                  align="center"
                  type="index"
                  width="60"
                >
                </el-table-column>
                <el-table-column
                  label="年份"
                  align="center"
                  prop="year"
                  width="80"
                >
                </el-table-column>
                <el-table-column
                  label="节日"
                  align="center"
                  prop="name"
                  width="100"
                >
                </el-table-column>
                <el-table-column
                  label="类别"
                  align="center"
                  prop="type"
                  width="100"
                >
                  <template slot-scope="scope">
                    {{ scope.row.type == 1 ? "假期" : "调休" }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="开始时间"
                  align="center"
                  prop="startDate"
                  min-width="160"
                >
                </el-table-column>
                <el-table-column
                  label="结束时间"
                  align="center"
                  prop="endDate"
                  min-width="160"
                >
                </el-table-column>
                <el-table-column
                  width="120"
                  fixed="right"
                  label="操作"
                  align="center"
                  class-name="small-padding fixed-width"
                >
                  <template slot-scope="scope">
                    <el-button type="text" @click="showDetail(scope.row.id)"
                      >编辑</el-button
                    >
                    <el-button
                      type="text"
                      style="color: #ff1838"
                      @click="handleDel(scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                style="text-align: right; margin-top: 10px"
                background
                v-show="pageInfoOne.total > 0"
                :total="pageInfoOne.total"
                :page.sync="pageInfoOne.pageNum"
                :limit.sync="pageInfoOne.pageSize"
                @current-change="handleCurrentChange"
              />
            <!-- </div> -->
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="four-right">
          <common-title :text="'收费方案关联'" />
          <div class="four-right-wrapper">
            <el-button
              size="small"
              type="primary"
              icon="el-icon-plus"
              @click="addTab"
              >设置关联关系</el-button
            >
            <el-table
              key="left"
              style="margin-top: 14px"
              v-loading="loadingTwo"
              :data="tableDataTwo"
              :max-height="maxHeight"
            >
              <el-table-column
                label="序号"
                align="center"
                type="index"
                width="60"
              >
              </el-table-column>
              <el-table-column
                label="区域"
                align="center"
                prop="parkLotName"
                min-width="160"
              >
              </el-table-column>
              <el-table-column
                label="车组类型"
                align="center"
                prop="vehicleCategoryName"
                width="160"
              >
              </el-table-column>
              <el-table-column
                label="车类型"
                align="center"
                prop="vehicleTypeName"
                min-width="160"
              >
              </el-table-column>
              <el-table-column
                label="节假日类别"
                align="center"
                prop="holidayTypeName"
                width="100"
              >
              </el-table-column>
              <el-table-column
                label="收费方案"
                align="center"
                prop="ruleName"
                min-width="200"
              >
              </el-table-column>
              <el-table-column
                width="80"
                fixed="right"
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    style="color: #ff1838"
                    @click="handleDelete(scope.row)"
                    >删除</el-button
                  >
                </template></el-table-column
              >
            </el-table>
            <el-pagination
              style="text-align: right; margin-top: 10px"
              background
              v-show="pageInfoTwo.total > 0"
              :total="pageInfoTwo.total"
              :page.sync="pageInfoTwo.pageNum"
              :limit.sync="pageInfoTwo.pageSize"
              @current-change="handleCurrentChangeTwo"
            />
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :title="title"
      :visible.sync="dialogOneVisible"
      width="500px"
      append-to-body
    >
      <el-form ref="form" :model="editForm" :rules="rules" label-width="60px">
        <el-form-item label="节日" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入节日名称" />
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-select v-model="editForm.type" style="width: 100%">
            <el-option label="假期" value="1"></el-option>
            <el-option label="调休" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始" prop="startDate">
          <el-date-picker
            style="width: 100%"
            v-model="editForm.startDate"
            type="datetime"
            placeholder="选择开始时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束" prop="endDate">
          <el-date-picker
            style="width: 100%"
            v-model="editForm.endDate"
            type="datetime"
            placeholder="选择结束时间"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelDialog">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import CommonTitle from "./commonTitle.vue";
import {
  holidayList,
  holidayDetail,
  holidayDel,
  holidayAdd,
  holidayEdit,
  getThisYearJjr,
  relationHolidayList,
  relationHolidayDel,
} from "@/api/setting/index";
export default {
  name: "four",
  data() {
    return {
      maxHeight: 100,
      loadingOne: false,
      loadingTwo: false,
      pageInfoOne: {
        pageNum: 1,
        pageSize: 10,
        pageTotal: 0,
      },
      pageInfoTwo: {
        pageNum: 1,
        pageSize: 10,
        pageTotal: 0,
      },
      tableDataOne: [],
      tableDataTwo: [],
      dialogOneVisible: false,
      editForm: {},
      rules: {
        name: [
          { required: true, message: "节假日名称不能为空", trigger: "blur" },
        ],
        type: [
          { required: true, message: "节假日类型不能为空", trigger: "blur" },
        ],
        startDate: [
          { required: true, message: "开始时间不能为空", trigger: "blur" },
        ],
        endDate: [
          { required: true, message: "结束时间不能为空", trigger: "blur" },
        ],
      },
      title: "",
    };
  },
  components: {
    CommonTitle,
  },
  methods: {
    getList() {
      this.loadingOne = true;
      let obj = {
        pageNum: this.pageInfoOne.pageNum,
        pageSize: this.pageInfoOne.pageSize,
      };
      holidayList(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataOne = res.rows;
          this.pageInfoOne.total = res.total;
          this.loadingOne = false;
        }
      });
    },
    getRelationHolidayList() {
      this.loadingTwo = true;
      let obj = {
        pageSize: this.pageInfoTwo.pageSize,
        pageNum: this.pageInfoTwo.pageNum,
      };
      relationHolidayList(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataTwo = res.rows;
          this.pageInfoTwo.total = res.total;
          this.loadingTwo = false;
        }
      });
    },
    reset() {
      this.resetForm("form");
    },
    getHoliday() {
      getThisYearJjr().then((res) => {
        if (res.code === 200) {
          let data = res.data;
          data.forEach((item) => {
            let index = this.tableDataOne.findIndex((tableItem) => {
              return tableItem.id == item.id;
            });
            if (index === -1) {
              this.tableDataOne.push(item);
            }
          });
          this.tableDataOne = this.tableDataOne.sort(this.sortBy("id"));
          this.$message.success("获取成功！");
        }
      });
    },
    AddHoliday() {
      this.reset();
      for (var i in this.editForm) {
        delete this.editForm[i];
      }
      this.title = "新增节假日";
      this.dialogOneVisible = true;
    },
    sortBy(field) {
      //根据传过来的字段进行排序
      return (x, y) => {
        return x[field] - y[field];
      };
    },
    showDetail(id) {
      this.reset();
      holidayDetail(id).then((res) => {
        this.editForm = res.data;
        this.title = "节假日编辑";
        this.dialogOneVisible = true;
      });
    },
    handleDel(row) {
      this.$confirm("", {
        title: "删除提示",
        message: "确定删除这条内容？",
        iconClass: "el-icon-warning colorYellow",
      })
        .then(() => {
          holidayDel(row).then((res) => {
            if (res.code === 200) {
              this.$message.success("删除成功！");
              this.pageInfoOne.pageNum = 1;
              this.getList();
            }
          });
        })
        .catch(() => {
          // console.log('no')
        });
    },
    handleDelete(row) {
      this.$confirm("", {
        title: "删除提示",
        message: "确定删除这条内容？",
        iconClass: "el-icon-warning colorYellow",
      })
        .then(() => {
          relationHolidayDel(row).then((res) => {
            if (res.code === 200) {
              this.$message.success("删除成功！");
              this.getRelationHolidayList();
            }
          });
        })
        .catch(() => {
          // console.log('no')
        });
    },
    cancelDialog() {
      this.dialogOneVisible = false;
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.title.indexOf("编辑") > -1) {
            holidayEdit(this.editForm).then((res) => {
              if (res.code === 200) {
                this.$message.success("编辑成功！");
                this.dialogOneVisible = false;
                this.pageInfoOne.pageNum = 1;
                this.getList();
              }
            });
          } else {
            holidayAdd(this.editForm).then((res) => {
              if (res.code === 200) {
                this.$message.success("编辑成功！");
                this.dialogOneVisible = false;
                this.pageInfoOne.pageNum = 1;
                this.getList();
              }
            });
          }
        }
      });
    },
    addTab() {
      this.$emit("addTab", "节假日-区域-车类型-车型-收费方案关联设置");
    },
    handleCurrentChange(val) {
      this.pageInfoOne.pageNum = val;
      this.getList();
    },
    handleCurrentChangeTwo(val) {
      this.pageInfoTwo.pageNum = val;
      this.getRelationHolidayList();
    },
  },
  mounted() {
    this.getList();
    this.getRelationHolidayList();
    this.$nextTick(() => {
      console.log(this.$refs.four.clientHeight)
      this.maxHeight = this.$refs.four.clientHeight - 160
    })
  },
};
</script>

<style lang="scss" scoped>
.four {
  height: 100%;
  .four-left {
    margin-right: 16px;
    display: flex;
    flex-direction: column;
    height: 100%;
    .four-left-wrapper {
      height: 100%;
      flex: 1;
      background: #ffffff;
      border: 1px solid #d7e5e9;
      border-top: none;
      padding: 12px 19px;
      // display: flex;
      // flex-direction: column;
      // .button-wrapper {
      //   flex-shrink: 0;
      // }
      // .table-wrapper {
      //   flex: 1;
      // }
    }
  }
  .four-right {
    width: 100%;
    display: flex;
    flex-direction: column;
    height: 100%;
    .four-right-wrapper {
      flex: 1;
      background: #ffffff;
      border: 1px solid #d7e5e9;
      border-top: none;
      padding: 12px 19px;
    }
  }
}
.four ::v-deep .el-table__body tr,
.el-table__body td,
.el-table td.el-table__cell {
  padding: 0 !important;
  height: 36px !important;
  line-height: 36px !important;
}
.four ::v-deep .el-table--medium .el-table__cell {
  padding: 0 !important;
}
// .four ::v-deep .el-table__fixed::before,
// .el-table__fixed-right::before {
//   height: 0 !important;
// }
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 2px;
  // box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  background: rgba(112, 173, 236, 0.2);
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
  // box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  border-radius: 0;
  background: rgba(112, 173, 236, 0.1);
}
</style>