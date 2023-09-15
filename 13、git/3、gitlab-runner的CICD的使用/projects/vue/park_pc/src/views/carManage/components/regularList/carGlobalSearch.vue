<!--
 * @Description: 全局搜索
 * @Author: Adela
 * @Date: 2023-02-28 10:23:24
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 16:46:34
 * @文件相对于项目的路径: /park_pc/src/views/carManage/components/regularList/carGlobalSearch.vue
-->

<template>
  <div class="carGlobalSearch">
    <el-dialog
      :title="title"
      :visible.sync="dialogOpen"
      :close-on-click-modal="false"
      width="1000px"
      append-to-body
      class="payEdit"
      @close="cancel"
    >
      <el-form
        class="searchBox"
        :model="queryForm"
        ref="queryForm"
        size="small"
        label-width="120px"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item label="车主姓名" prop="ownerName">
              <el-input
                v-model="queryForm.ownerName"
                placeholder="请输入车主姓名"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车牌号：" prop="carNumber">
              <el-input
                v-model="queryForm.carNumber"
                placeholder="请输入车牌号"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系方式" prop="ownerPhone">
              <el-input
                v-model="queryForm.ownerPhone"
                placeholder="请输入联系方式"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="固定车类型：" prop="carCategoryId">
              <el-select
                v-model="queryForm.carCategoryId"
                placeholder="固定车类型："
                clearable
              >
                <el-option
                  v-for="dict in typeList"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="类型：" prop="carType">
              <el-select
                v-model="queryForm.carType"
                placeholder="类型："
                clearable
              >
                <el-option :key="''" label="全部" :value="''" />
                <el-option :key="'0'" label="线下" :value="'0'" />
                <el-option :key="'1'" label="线上" :value="'1'" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item class="subBox">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >查询</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
      <!-- </div> -->
      <titleTab title="车辆记录"></titleTab>
      <div class="tableBox">
        <el-table v-loading="loading" :data="tableData">
          <el-table-column label="序号" align="center" type="index" width="80" fixed="left">
            <template slot-scope="scope">
              <span>{{
                scope.$index + (pageInfo.pageNum - 1) * pageInfo.pageSize + 1
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="车牌号"
            min-width="100"
            prop="carNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="车辆颜色"
            min-width="100"
            prop="carColor"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="车牌备注"
            min-width="200"
            prop="carRemark"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="车主姓名"
            min-width="100"
            prop="ownerName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="身份证号"
            min-width="220"
            prop="ownerCardId"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="联系地址"
            min-width="260"
            prop="ownerAddress"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="联系电话"
            min-width="160"
            prop="ownerPhone"
            :show-overflow-tooltip="true"
          />
          <!-- <el-table-column
          label="固定车类型"
          min-width="260"
          prop=""
        /> -->
          <el-table-column
            label="流动车位数"
            min-width="100"
            prop="flowPlaceNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="有效期开始时间"
            min-width="160"
            prop="startTime"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="有效期结束时间"
            min-width="160"
            prop="endTime"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="备注"
            prop="remark"
            min-width="200"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            width="120"
            fixed="right"
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="gotoCarGlobalSearch(scope.row)"
                >明细</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="pageInfo.total > 0"
          :total="pageInfo.total"
          :page.sync="pageInfo.pageNum"
          :limit.sync="pageInfo.pageSize"
          @pagination="getList"
        />
      </div>
      <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div> -->
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import { regularCarList, regularCarCategoryList } from "../../api/index";

export default {
  name: "CarGlobalSearch",
  dicts: ["mod_car_owner_group_type"],
  components: {
    titleTab,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    open: {
      type: Boolean,
      default: false,
    },
    detail: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      format: "YYYY-MM-DD HH:mm:ss",
      dialogOpen: this.open,
      rules: {},
      queryForm: {
        ownerName: "",
        ownerPhone: "",
        carNumber: "",
        carCategoryId: "",
        carType: "",
      },
      tableData: [],
      pageInfo: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      loading: false,
      typeList: [],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getList();
    this.getTypeList();
  },
  methods: {
    async getTypeList() {
      const response = await regularCarCategoryList();
      if (response.code === 200) {
        this.typeList = response.rows;
      } else {
        console.log(1111);
      }
    },
    // carList
    async getList() {
      const parmas = {
        ...this.queryForm,
        pageSize: this.pageInfo.pageSize,
        pageNum: this.pageInfo.pageNum,
      };
      this.loading = true;
      const response = await regularCarList(parmas);
      if (response.code === 200) {
        this.tableData = response.rows;
        this.pageInfo.total = response.total;
        this.loading = false;
      } else {
        console.log(1111);
      }
    },
    handleQuery() {
      this.pageInfo.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 取消按钮
    cancel() {
      this.dialogOpen = false;
      this.$emit("cancel", false);
    },
    gotoCarGlobalSearch(row) {
      this.$emit("gotoCarGlobalSearch", row);
      console.log("gotoCarGlobalSearch", row);
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../index.scss";
//@import 'xxx.scss';引入公共css类

.subBox {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
