<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-22 14:31:12
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-22 14:31:29
 * @文件相对于项目的路径: /park_pc/src/views/basicSettings/_baseSetting.vue
-->
<template>
  <div class="baseSetting">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="车辆类型" name="1">
        <div slot="label" class="tabSkew">
          <div class="tabFirstImg"></div>
          <div class="tabName">车辆类型</div>
          <div class="tabRImg" v-if="+activeName != 2"></div>
        </div>
        <carSetting v-if="+activeName === 1"></carSetting>
      </el-tab-pane>
      <el-tab-pane label="闸道抬杆原因" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">闸道抬杆原因</div>
          <div class="tabRImg" v-if="activeName != 3"></div>
        </div>
        <liftgatereason></liftgatereason>
      </el-tab-pane>
      <el-tab-pane label="闸道抬杆原因1" name="3">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">闸道抬杆原因1</div>
          <div class="tabRImg" v-if="activeName != 4"></div>
        </div>
        <liftgatereason></liftgatereason>
      </el-tab-pane>
      <el-tab-pane label="闸道抬杆原因2" name="4">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">闸道抬杆原因2</div>
          <div class="tabLastImg"></div>
        </div>
        <liftgatereason></liftgatereason>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import carSetting from "./components/carSetting.vue";
import liftgatereason from "./components/liftgatereason.vue";
export default {
  name: "BaseSetting",
  components: {
    carSetting,
    liftgatereason,
  },
  props: {},
  data() {
    return {
      activeName: "1",
    };
  },
  computed: {},
  watch: {},
  created() {
    console.log(11);
  },
  mounted() {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
  },
};
</script>

<style scoped lang="scss">
.baseSetting {
  min-height: calc(100vh - 60px);
  margin: 10px 16px 0;
  background: #ffffff;
  // box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
  border-radius: 4px 4px 4px 4px;
  ::v-deep .el-tabs__header {
    height: 40px;
    background: #f6f7f9;
  }
  ::v-deep .el-tabs--card > .el-tabs__header .el-tabs__nav {
    border: 0;
  }

  ::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item {
    // transform: skew(30deg);
    padding: 0;
    border: 0;
  }
  ::v-deep.el-tabs__item {
    color: rgba(0, 29, 60, 0.7);
  }
  ::v-deep.el-tabs__item:hover {
    color: #001d3c !important;
    cursor: pointer;
    font-weight: 700;
  }
  ::v-deep .el-tabs__item.is-active {
    color: #001d3c !important;
    cursor: pointer;
    font-weight: 700;
  }
  .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
    .tabSkew {
      // transform: skew(-30deg);
      padding: 0;
      background: url("~@/assets/images/common/tab/tabBg.png") repeat-x;
      background-size: 1px 100%;
      display: flex;
    }
    .tabFirstImg {
      width: 22px;
      height: 40px;
      background: url("~@/assets/images/common/tab/tabLeftActive.png") no-repeat;
      background-size: 22px 40px;
    }
    .tabLeftImg {
      width: 22px;
      height: 40px;
      background: url("~@/assets/images/common/tab/tabActiveL.png") no-repeat;
      background-size: 22px 40px;
    }
    .tabLastImg {
      width: 22px;
      height: 40px;
      background: url("~@/assets/images/common/tab/tabLastActive.png") no-repeat;
      background-size: 22px 40px;
    }
    .tabRImg {
      width: 22px;
      height: 40px;
      background: url("~@/assets/images/common/tab/tabActive.png") no-repeat;
      background-size: 22px 40px;
    }
  }
  .tabSkew {
    display: flex;
    // transform: skew(-30deg);
    padding: 0;
    // background: #e5f3f8;
    background: url("~@/assets/images/common/tab/tabBBg.png") repeat-x;
    background-size: 1px 100%;
    // border: 1px solid #ccc;
  }
  .tabFirstImg {
    width: 22px;
    height: 40px;
    background: url("~@/assets/images/common/tab/tabLeft.png") no-repeat;
    background-size: 22px 40px;
  }
  .tabLastImg {
    width: 22px;
    height: 40px;
    background: url("~@/assets/images/common/tab/tabLast.png") no-repeat;
    background-size: 22px 40px;
  }
  .tabRImg {
    width: 22px;
    height: 40px;
    background: url("~@/assets/images/common/tab/tabR.png") no-repeat;
    background-size: 22px 40px;
  }
  .tabName {
    // transform:skew(-30deg);
    padding: 0 10px 0;
  }
  ::v-deep.el-tabs__header {
    margin: 0;
  }
  ::v-deep.el-tabs__content {
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    min-height: calc(100vh - 100px);
    border-radius: 0px 0px 4px 4px;
  }
}

//@import 'xxx.scss';引入公共css类
</style>
