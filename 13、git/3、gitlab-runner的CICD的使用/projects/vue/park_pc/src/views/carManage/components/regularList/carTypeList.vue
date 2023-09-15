<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-27 09:53:39
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 15:45:40
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/regularList/carTypeList.vue
-->

<template>
  <div class="addType">
    <div class="addTypeBtn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        class="addBtn"
        @click="addType"
        >新增</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-search"
        class="searchBtn"
        size="mini"
        @click="globalSearch"
        >全局搜索</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-download"
        class="exportBtn"
        @click="exportAllData"
        size="mini"
        >导出全部车牌</el-button
      >
    </div>
    <div class="addTypeTable">
      <el-table
        v-loading="loading"
        :data="tableData"
        :row-class-name="tableRowClassName"
        @cell-click="chooseTableCell"
      >
        <el-table-column label="序号" align="center" width="60">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="名称"
          :show-overflow-tooltip="true"
          prop="name"
        />
        <el-table-column
          label="分组类型"
          prop="groupName"
          min-width="80"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="操作" class-name="small-padding">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="editRow(scope.row)"
              >编辑</el-button
            >

            <el-button
              size="mini"
              type="text"
              class="colorHollowBtnRed"
              @click="deleteRow(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增修改车辆类型dialog -->
    <div v-if="openD">
      <carTypeDialog
        :open="openD"
        :title="title"
        :detail="detail"
        @cancel="cancelTypeD"
        @success="successTypeD"
      ></carTypeDialog>
    </div>

    <div v-if="openGlobalD">
      <carGlobalSearch
        title="全局搜索"
        :open="openGlobalD"
        @cancel="cancelGlobalSearch"
        @gotoCarGlobalSearch="gotoCarGlobalSearch"
      ></carGlobalSearch>
    </div>
  </div>
</template>

<script>
import carTypeDialog from "./carTypeDialog.vue";
import carGlobalSearch from "./carGlobalSearch.vue";
import {
  regularCarCategoryList,
  removeRegularCarCategory,
} from "../../api/index";

export default {
  name: "AddType",
  components: {
    carTypeDialog,
    carGlobalSearch,
  },
  props: {},
  data() {
    return {
      nowClickId: null,
      loading: false,
      openD: false,
      detail: {},
      tableData: [
        // {
        //   id: 1,
        //   endTime: "2023-05-31 00:00:00",
        //   groupId: "1",
        //   name: "长期固定送货车",
        //   startTime: "2023-02-01 00:00:00",
        //   timeLimit: 1,
        //   payData: [
        //     {
        //       month: "1",
        //       price: "10",
        //       iseditor: false,
        //     },
        //     {
        //       month: "2",
        //       price: "20",
        //       iseditor: false,
        //     },
        //   ],
        // },
        // {
        //   id: 2,
        //   endTime: "2023-05-31 00:00:00",
        //   groupId: "1",
        //   name: "长期固定送货车",
        //   startTime: "2023-02-01 00:00:00",
        //   timeLimit: 1,
        //   payData: [
        //     {
        //       month: "1",
        //       price: "10",
        //       iseditor: false,
        //     },
        //     {
        //       month: "2",
        //       price: "20",
        //       iseditor: false,
        //     },
        //   ],
        // },
      ],
      openGlobalD: false,
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getList();
  },
  mounted() {},
  methods: {
    tableRowClassName({ row, rowIndex }) {
      if (row.id === this.nowClickId) {
        return "highlight-row";
      }
      return "";
    },
    addType() {
      this.title = "新增固定车类型";
      this.detail.id = undefined;
      this.openD = true;
    },
    editRow(row) {
      this.title = "修改固定车类型";
      this.detail = row;
      this.openD = true;
    },
    deleteRow(row) {
      const id = row.id;
      this.$modal
        .confirm(`是否确认删除${row.name}数据`)
        .then(function () {
          return removeRegularCarCategory(id);
        })
        .then(() => {
          this.getList();
          this.$emit("chooserRow", {});
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    // 获取当前第几行
    chooseTableCell(row) {
      this.nowClickId = row.id;
      this.$emit("chooserRow", row);
      console.log("chooserRow", row.id, row);
    },
    cancelTypeD() {
      this.detail = {};
      this.openD = false;
    },
    successTypeD() {
      this.detail = {};
      this.openD = false;
      this.getList();
      // 调list接口
    },
    async getList() {
      this.loading = true;
      const response = await regularCarCategoryList();
      if (response.code === 200) {
        this.tableData = response.rows;
        // this.total = response.total;
        this.loading = false;
      } else {
        console.log(1111);
      }
    },

    // 全局搜索
    globalSearch() {
      this.openGlobalD = true;
    },
    cancelGlobalSearch() {
      this.openGlobalD = false;
    },
    gotoCarGlobalSearch(row) {
      this.nowClickId = row.carCategoryId;
      this.$emit("gotoCarGlobalSearchRow", row);
      this.openGlobalD = false;
    },

    /** 导出全部数据按钮操作 */
    exportAllData() {
      this.download(
        "/parking/regularCar/export",
        {},
        `全部车辆信息_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "../../index.scss";

.el-table {
  ::v-deep.highlight-row {
    background: #f5a928 !important;
  }
}
::v-deep.el-table--enable-row-hover
  .el-table__body
  tr:hover
  > td.el-table__cell {
  background-color: transparent;
}
</style>
