<template>
  <div class="create-contact">
    <div class="create-contact-wrapper">
      <el-row style="width: 100%">
        <el-col :span="8">
          <div class="create-contact-one">
            <common-title :text="'停车场'" />
            <div class="one-wrapper">
              <div class="form-line">
                <el-checkbox v-model="form0.parkLotNameShow"
                  >区分停车场区域</el-checkbox
                >
              </div>
              <div class="table-wrapper" v-if="form0.parkLotNameShow">
                <el-table
                  style="margin-top: 14px"
                  v-loading="loadingOne"
                  :data="tableDataOne"
                  @selection-change="handleSelectionChangeOne"
                >
                  <el-table-column
                    label="序号"
                    align="center"
                    type="index"
                    width="80"
                  >
                  </el-table-column>
                  <el-table-column
                    type="selection"
                    width="80"
                    label="选择"
                    align="center"
                  ></el-table-column>
                  <el-table-column
                    label="名称"
                    align="center"
                    prop="fieldName"
                  />
                </el-table>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="create-contact-two">
            <common-title :text="'车类型'" />
            <div class="two-wrapper">
              <div class="form-line" :class="{ 'form-line-big': form0.gdc }">
                <el-row style="width: 100%">
                  <el-col :span="8">
                    <el-checkbox v-model="form0.lsc">临时车</el-checkbox>
                  </el-col>
                  <el-col :span="8">
                    <el-checkbox v-model="form0.gdc">固定车</el-checkbox>
                  </el-col>
                </el-row>
                <el-row style="width: 100%; margin-top: 5px" v-if="form0.gdc">
                  <el-col :span="12">
                    <el-checkbox v-model="form0.isGroup"
                      >固定车是否区分车主(组)分组</el-checkbox
                    >
                  </el-col>
                </el-row>
              </div>
              <div class="table-wrapper" v-if="form0.isGroup">
                <el-table
                  style="margin-top: 14px"
                  v-loading="loadingTwo"
                  :data="tableDataTwo"
                  @selection-change="handleSelectionChangeTwo"
                >
                  <el-table-column
                    label="序号"
                    align="center"
                    type="index"
                    width="80"
                  >
                  </el-table-column>
                  <el-table-column
                    type="selection"
                    width="80"
                    label="选择"
                    align="center"
                  ></el-table-column>
                  <el-table-column label="名称" align="center" prop="name" />
                </el-table>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="create-contact-three">
            <common-title :text="'车型'" />
            <div class="three-wrapper">
              <div class="form-line">
                <el-checkbox v-model="form0.vehicleTypeNameShow"
                  >区分车型</el-checkbox
                >
              </div>
              <div class="table-wrapper" v-if="form0.vehicleTypeNameShow">
                <el-table
                  style="margin-top: 14px"
                  v-loading="loadingThree"
                  :data="tableDataThree"
                  @selection-change="handleSelectionChangeThree"
                >
                  <el-table-column
                    label="序号"
                    align="center"
                    type="index"
                    width="80"
                  >
                  </el-table-column>
                  <el-table-column
                    type="selection"
                    width="80"
                    label="选择"
                    align="center"
                  ></el-table-column>
                  <el-table-column label="名称" align="center" prop="name" />
                </el-table>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="create-contact-bottom">
      <common-title :text="'操作'" />
      <div class="create-contact-bottom-btns">
        <el-button type="primary" @click="handleSubmit">确定</el-button>
        <el-button style="margin-left: 32px" type="info" @click="closeTab"
          >取消</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import CommonTitle from "./commonTitle.vue";
import {
  listField,
  categoryList,
  getSettingcartypeList,
  setRelation,
} from "@/api/setting/index";
export default {
  name: "createContact",
  data() {
    return {
      form0: {
        parkLotNameShow: false,
        lsc: false,
        gdc: false,
        isGroup: false,
        vehicleTypeNameShow: false,
      },
      idsOne: [],
      idsTwo: [],
      idsThree: [],
      loadingOne: false,
      loadingTwo: false,
      loadingThree: false,
      tableDataOne: [],
      tableDataTwo: [],
      tableDataThree: [],
      pageInfoOne: {
        pageNum: 1,
        pageSize: 50,
        pageTotal: 0,
      },
      pageInfoTwo: {
        pageNum: 1,
        pageSize: 50,
        pageTotal: 0,
      },
      pageInfoThree: {
        pageNum: 1,
        pageSize: 50,
        pageTotal: 0,
      },
    };
  },
  props: {
    parkNo: {
      type: [String, Number],
    },
  },
  components: {
    CommonTitle,
  },
  methods: {
    getFields() {
      this.loadingOne = true;
      let obj = {
        pageNum: this.pageInfoOne.pageNum,
        pageSize: this.pageInfoOne.pageSize,
      };
      listField(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataOne = res.rows;
          this.pageInfoOne.total = res.total;
          this.loadingOne = false;
        }
      });
    },
    getCarTypes() {
      this.loadingTwo = true;
      let obj = {
        pageNum: this.pageInfoTwo.pageNum,
        pageSize: this.pageInfoTwo.pageSize,
      };
      categoryList(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataTwo = res.rows;
          this.pageInfoTwo.total = res.total;
          this.loadingTwo = false;
        }
      });
    },
    getCarTypeList() {
      this.loadingThree = true;
      let obj = {
        pageNum: this.pageInfoThree.pageNum,
        pageSize: this.pageInfoThree.pageSize,
      };
      getSettingcartypeList(obj).then((res) => {
        if (res.code === 200) {
          this.tableDataThree = res.rows;
          this.pageInfoThree.total = res.total;
          this.loadingThree = false;
        }
      });
    },
    handleSelectionChangeOne(val) {
      let ids = [];
      if (val.length > 0) {
        val.forEach((item) => {
          ids.push(item.id);
        });
      }
      this.idsOne = ids;
      console.log(this.idsOne);
    },
    handleSelectionChangeTwo(val) {
      let ids = [];
      if (val.length > 0) {
        val.forEach((item) => {
          ids.push(item.id);
        });
      }
      this.idsTwo = ids;
      console.log(this.idsTwo);
    },
    handleSelectionChangeThree(val) {
      let ids = [];
      if (val.length > 0) {
        val.forEach((item) => {
          ids.push(item.id);
        });
      }
      this.idsThree = ids;
      console.log(this.idsThree);
    },
    handleSubmit() {
      if (!this.form0.lsc && !this.form0.gdc) {
        this.$message.error("请选择车类型！");
        return false;
      }
      if (this.form0.parkLotNameShow && this.idsOne.length === 0) {
        this.$message.error("请选择区分停车场区域！");
        return false;
      }
      if (this.form0.isGroup && this.idsTwo.length === 0) {
        this.$message.error("请选择固定车是否区分车主(组)分组！");
        return false;
      }
      if (this.form0.vehicleTypeNameShow && this.idsThree.length === 0) {
        this.$message.error("请选择区分车型！");
        return false;
      }
      if (this.idsOne.length === 0) {
        this.idsOne.push("ALL");
      }
      if (this.idsThree.length === 0) {
        this.idsThree.push("ALL");
      }
      if (this.form0.lsc) {
        if (this.idsTwo.indexOf("LS") === -1) {
          this.idsTwo.push("LS");
        }
      }
      if (
        this.form0.gdc &&
        (this.idsTwo.length === 0 ||
          (this.idsTwo.length === 1 && this.idsTwo[0] === "LS"))
      ) {
        this.idsTwo.push("GD");
      }
      let list = [];
      for (var i = 0; i < this.idsOne.length; i++) {
        let obj1 = {
          parkLotSign: this.idsOne[i],
        };
        for (var j = 0; j < this.idsTwo.length; j++) {
          let obj2 = {
            vehicleCategorySign: this.idsTwo[j],
          };
          for (var k = 0; k < this.idsThree.length; k++) {
            let obj3 = {
              vehicleTypeSign: this.idsThree[k],
            };
            let obj = {
              ...obj1,
              ...obj2,
              ...obj3,
              ruleId: localStorage.getItem("ruleId"),
            };
            list.push(obj);
          }
        }
      }
      localStorage.removeItem("ruleId");
      setRelation(list).then((res) => {
        if (res.code === 200) {
          this.$message.success("规则关联成功！");
          this.$emit("addSuccess", "整体收费方案-区域-车类型-车型关联设置");
        }
      });
    },
    closeTab() {
      this.$emit("closeTab", "整体收费方案-区域-车类型-车型关联设置", "2");
    },
  },
  watch: {
    "form0.parkLotNameShow": {
      handler(val) {
        if (val) {
          this.loadingOne = true;
          this.idsOne = [];
          this.getFields();
        } else {
          this.tableDataOne = [];
          this.pageInfoOne.total = 0;
          this.idsOne = [];
        }
      },
      deep: true,
      immediate: true,
    },
    "form0.gdc": {
      handler(val) {
        if (!val) {
          this.form0.isGroup = false;
        }
      },
      deep: true,
      immediate: true,
    },
    "form0.isGroup": {
      handler(val) {
        if (val) {
          this.idsTwo = [];
          this.getCarTypes();
        } else {
          this.idsTwo = [];
          this.tableDataTwo = [];
          this.pageInfoOne.total = 0;
        }
      },
      deep: true,
      immediate: true,
    },
    "form0.vehicleTypeNameShow": {
      handler(val) {
        if (val) {
          this.loadingThree = true;
          this.idsThree = [];
          this.getCarTypeList();
        } else {
          this.tableDataThree = [];
          this.pageInfoThree.total = 0;
          this.idsThree = [];
        }
      },
      deep: true,
      immediate: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.create-contact {
  display: flex;
  flex-direction: column;
  min-height: 550px;
  .create-contact-wrapper {
    display: flex;
    flex: 1;
    .create-contact-one {
      height: 100%;
      margin-right: 10px;
      display: flex;
      flex-direction: column;
      .one-wrapper {
        flex: 1;
        padding: 13px 11px 13px 17px;
        border: 1px solid #d7e5e9;
        border-top: none;
      }
    }
    .create-contact-two {
      height: 100%;
      // width: 100%;
      margin: 0 5px;
      display: flex;
      flex-direction: column;
      .two-wrapper {
        flex: 1;
        padding: 13px 11px 13px 17px;
        border: 1px solid #d7e5e9;
        border-top: none;
      }
    }
    .create-contact-three {
      height: 100%;
      margin-left: 10px;
      display: flex;
      flex-direction: column;
      .three-wrapper {
        flex: 1;
        padding: 13px 11px 13px 17px;
        border: 1px solid #d7e5e9;
        border-top: none;
      }
    }
  }
  .create-contact-bottom {
    flex-shrink: 0;
    margin-top: 20px;
    .create-contact-bottom-btns {
      height: 50px;
      width: 100%;
      border: 1px solid #d7e5e9;
      border-top: none;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
.form-line {
  width: 100%;
  height: 56px;
  background: rgba(239, 248, 251, 0.5);
  border: 1px dashed #d7e5e9;
  padding-left: 19px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.form-line-big {
  height: 76px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.form-line ::v-deep .el-form-item {
  margin-bottom: 0 !important;
  height: 56px !important;
}
.form-line ::v-deep .el-form-item--medium .el-form-item__content {
  line-height: 56px !important;
}
.table-wrapper ::v-deep .el-table-column--selection .cell {
  padding-left: 0 !important;
}
</style>