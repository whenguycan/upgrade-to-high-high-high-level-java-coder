<template>
  <div class="no-relation-list">
    <common-title :text="'未关联收费方案'" />
    <div class="no-relation-list-wrapper">
      <el-table
        style="margin-top: 14px; width: 100%"
        v-loading="loading"
        :data="tableData"
        max-height="480px"
      >
        <el-table-column
          label="序号"
          align="center"
          type="index"
          width="80"
        ></el-table-column>
        <el-table-column label="停车场名称" align="center" prop="parkLotName">
        </el-table-column>
        <el-table-column
          label="车类型名称"
          align="center"
          prop="vehicleCategoryName"
          width="200"
        >
        </el-table-column>
        <el-table-column
          label="车型名称"
          align="center"
          prop="vehicleTypeName"
          width="200"
        >
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import CommonTitle from "./commonTitle.vue";
import { noRelationVehicleList } from "@/api/setting/index";
export default {
  name: "noRelationList",
  data() {
    return {
      loading: false,
      tableData: [],
    };
  },
  components: {
    CommonTitle,
  },
  methods: {
    getList() {
      this.loading = true;
      let obj = {
        pageNum: 1,
        pageSize: 50,
      };
      noRelationVehicleList(obj)
        .then((res) => {
          if (res.code === 200) {
            this.tableData = res.rows;
            this.loading = false;
          }
        })
        .catch((errMsg) => {
          this.$message.error(errMsg);
          this.loading = false;
        });
    },
  },
  mounted() {
    this.getList();
  },
};
</script>

<style lang="scss" scoped>
.no-relation-list {
  height: 540px;
  width: 100%;
  display: flex;
  flex-direction: column;
  .no-relation-list-wrapper {
    padding: 16px;
    flex: 1;
    background: #ffffff;
    border: 1px solid #d7e5e9;
    border-top: none;
  }
}

.no-relation-list-wrapper ::v-deep .el-table__body tr,
.el-table__body td,
.el-table td.el-table__cell {
  padding: 0 !important;
  height: 36px !important;
  line-height: 36px !important;
}
.no-relation-list-wrapper ::v-deep .el-table--medium .el-table__cell {
  padding: 0 !important;
}

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