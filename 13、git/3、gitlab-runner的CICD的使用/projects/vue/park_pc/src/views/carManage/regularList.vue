<!--
 * @Description: 固定车管理
 * @Author: Adela
 * @Date: 2023-02-27 09:35:29
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-28 16:31:40
 * @文件相对于项目的路径: /park_pc/src/views/carManage/regularList.vue
-->
<template>
  <div class="regularList">
    <div class="regularListCon regularListConLeft">
      <titleTab title="固定车类型信息"></titleTab>
      <div class="regularListConL">
        <carTypeList
          @chooserRow="chooserRow"
          @gotoCarGlobalSearchRow="gotoCarGlobalSearchRow"
        ></carTypeList>
      </div>
    </div>
    <div class="regularListCon regularListConRight" style="margin-left: 16px">
      <titleTab title="车辆信息"></titleTab>
      <div v-if="nowType">
        <carList :nowType="nowType" :searchType="searchType" @closeGlobalSearch="closeGlobalSearch"></carList>
      </div>
    </div>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import carTypeList from "./components/regularList/carTypeList.vue";
import carList from "./components/regularList/carList.vue";

export default {
  name: "regularList",
  components: {
    titleTab,
    carTypeList,
    carList,
  },
  props: {},
  data() {
    return {
      nowType: null,
      searchType: false,
    };
  },
  computed: {},
  watch: {},
  methods: {
    chooserRow(row) {
      this.nowType = row;
    },
    gotoCarGlobalSearchRow(row) {
      this.nowType = {
        id: row.carCategoryId,
        carNumber: row.carNumber,
        ownerName: row.ownerName,
        ownerPhone: row.ownerPhone,
        carType: row.carType
      };
      this.searchType = true;
    },
    closeGlobalSearch() {
      this.searchType = false
    }
  },
  created() {},
  mounted() {},
};
</script>
<style scoped lang="scss">
@import "./index.scss";
.regularList {
  // width: 100%;
  height: 100%;
  margin: 16px;
  display: flex;

  ::v-deep .el-tooltip__popper {
    max-width: 40% !important; //宽度可根据自己需要进行设置
  }

  .regularListCon {
    background-color: #fff;
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    border-radius: 4px;
    padding: 20px 0;
    min-height: calc(100vh - 110px);
  }

  .regularListConLeft {
    min-width: 340px;
    width: 24%;
  }
  .regularListConRight {
    // flex: 1
    width: 76%;
  }

  .regularListConL {
    padding: 10px 0 10px 8px;
  }
}
</style>
