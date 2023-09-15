<template>
  <div class="three" ref="three">
    <el-row type="flex" style="height: 100%">
      <el-col :span="10">
        <div class="three-left">
          <common-title :text="'整体收费方案'" />
          <div class="three-left-wrapper">
            <el-button
            style="margin-right: 10px;"
              icon="el-icon-plus"
              size="small"
              type="primary"
              @click="getListOne"
              >查看未关联整体收费方案-停车场-车类型-车型</el-button
            >
            <el-button
              @click="getListTwo"
              icon="el-icon-plus"
              size="small"
              type="primary"
              style="margin-left: 0; margin-top: 8px"
              >查看已关联收费方案-停车场-车类型-车型</el-button
            >
            <el-table
              style="margin-top: 14px"
              v-loading="loading"
              :data="tableData"
              :max-height="maxHeightOne"
              key="three"
              highlight-current-row
              @row-click="rowClick"
            >
              <el-table-column
                label="收费方案/规则名称"
                width="200"
                align="center"
                prop="ruleName"
              />
              <el-table-column
                fixed="right"
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button type="text" @click="handleSetting(scope.row)"
                    >设置关联关系</el-button
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
          </div>
        </div>
      </el-col>
      <el-col :span="14">
        <div class="three-right">
          <common-title :text="'整体收费方案-停车场-车类型-车型关联关系'" />
          <div class="three-right-wrapper">
            <el-table
              key="left"
              style="margin-top: 14px; width: 100%"
              v-loading="loadingTwo"
              :data="tableDataTwo"
              :max-height="maxHeightTwo"
            >
              <el-table-column
                label="序号"
                align="center"
                type="index"
                width="60"
              ></el-table-column>
              <el-table-column
                label="停车场名称"
                align="center"
                prop="parkLotName"
              >
              </el-table-column>
              <el-table-column
                label="车类型名称"
                align="center"
                prop="vehicleCategoryName"
                width="160"
              >
              </el-table-column>
              <el-table-column
                label="车型名称"
                align="center"
                prop="vehicleTypeName"
                width="160"
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
  </div>
</template>

<script>
import CommonTitle from "./commonTitle.vue";
import {
  ruleList,
  relationVehicleDel,
  relationVehicleList,
} from "@/api/setting/index";
export default {
  name: "three",
  data() {
    return {
      maxHeightOne: 100,
      maxHeightTwo: 100,
      loading: false,
      tableData: [],
      loadingTwo: false,
      tableDataTwo: [],
      pageInfoOne: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      pageInfoTwo: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  components: {
    CommonTitle,
  },
  props: {
    parkNo: {
      type: [Number, String],
    },
  },
  methods: {
    getRuleList() {
      this.loading = true;
      let obj = {
        pageNum: this.pageInfoOne.pageNum,
        pageSize: this.pageInfoOne.pageSize,
      };
      ruleList(obj)
        .then((res) => {
          if (res.code === 200) {
            this.tableData = res.rows;
            this.pageInfoOne.total = res.total;
            this.loading = false;
          }
        })
        .catch((errMsg) => {
          this.$message.error(errMsg);
          this.loading = false;
        });
    },
    getListOne() {
      this.$emit("addTab", "未关联整体收费方案-停车场-车类型-车型");
    },
    getListTwo() {
      this.$emit("addTab", "查看已关联收费方案-停车场-车类型-车型");
    },
    handleSetting(row) {
      localStorage.setItem("ruleId", row.id);
      this.$emit("addTab", "整体收费方案-区域-车类型-车型关联设置");
    },
    rowClick(row) {
      this.chooseRow = row
      this.getRelationVehicleList(row.id)
    },
    getRelationVehicleList(id) {
      this.loadingTwo = true;
      let obj = {
        pageNum: this.pageInfoTwo.pageNum,
        pageSize: this.pageInfoTwo.pageSize,
        ruleId: id,
      };
      relationVehicleList(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataTwo = res.rows;
          this.pageInfoTwo.total = res.total;
          this.loadingTwo = false;
        }
      });
    },
    handleDelete(row) {
      this.$confirm("", {
        title: "删除提示",
        message: "确定删除这条内容？",
        iconClass: "el-icon-warning colorYellow",
      })
        .then(() => {
          relationVehicleDel(row).then((res) => {
            if (res.code === 200) {
              this.$message.success("删除成功！");
              this.loadingTwo = true;
              relationVehicleList({
                pageNum: 1,
                pageSize: 50,
                ruleId: row.ruleId,
              }).then((res) => {
                this.tableDataTwo = res.rows;
                this.loadingTwo = false;
              });
            }
          });
        })
        .catch(() => {
          // console.log('no')
        });
    },
    handleCurrentChange(val) {
      this.pageInfoOne.pageNum = val;
      this.getRuleList();
    },
    handleCurrentChangeTwo(val) {
      this.pageInfoTwo.pageNum = val;
      this.getRelationVehicleList(this.chooseRow.id);
    },
  },
  mounted() {
    this.getRuleList();
    this.$nextTick(() => {
      console.log(this.$refs.three.clientHeight);
      this.maxHeightOne = this.$refs.three.clientHeight - 195
      this.maxHeightTwo = this.$refs.three.clientHeight - 122
    });
  },
};
</script>

<style lang="scss" scoped>
.three {
  height: 100%;
  .three-left {
    // flex-shrink: 0;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    .three-left-wrapper {
      flex: 1;
      background: #ffffff;
      border: 1px solid #d7e5e9;
      border-top: none;
      padding: 12px 19px;
    }
  }
  .three-right {
    margin-left: 21px;
    // flex: 1;
    width: calc(100% - 21px);
    height: 100%;
    display: flex;
    flex-direction: column;
    .three-right-wrapper {
      flex: 1;
      background: #ffffff;
      border: 1px solid #d7e5e9;
      border-top: none;
      padding: 0 16px;
    }
  }
}
.three-left-wrapper ::v-deep .el-table__body tr,
.el-table__body td,
.el-table td.el-table__cell {
  padding: 0 !important;
  height: 36px !important;
  line-height: 36px !important;
}
.three-left-wrapper ::v-deep .el-table--medium .el-table__cell,
.three-right-wrapper ::v-deep .el-table--medium .el-table__cell {
  padding: 0 !important;
}
.three-left-wrapper ::v-deep .el-table__fixed::before,
.el-table__fixed-right::before,
.three-right-wrapper ::v-deep .el-table__fixed::before,
.el-table__fixed-right::before {
  height: 0 !important;
}
// .three-left-wrapper ::v-deep .el-table .el-table__cell {
//   padding: 0 !important;
// }
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 2px;
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  background: rgba(112, 173, 236, 0.2);
}
::v-deep .el-table__body-wrapper::-webkit-scrollbar-track {
  box-shadow: inset 0 0 1px rgba(112, 173, 236, 0.2);
  border-radius: 0;
  background: rgba(112, 173, 236, 0.1);
}
</style>