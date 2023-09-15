<!--
 * @Description: 进出场车辆记录
 * @Author: Adela
 * @Date: 2023-03-09 08:53:14
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-21 15:28:36
 * @文件相对于项目的路径: /park_pc/src/views/home/components/cardRecord.vue
-->
<template>
  <div class="cardRecord" id="carRecordList">
    <div class="recordTab">
      <span
        class="recordTabI"
        :class="{ recordTabIA: activeIndex === 1 }"
        @click="chooseTab(1)"
        >进场记录</span
      >
      <span
        class="recordTabI"
        :class="{ recordTabIA: activeIndex === 2 }"
        @click="chooseTab(2)"
        >出场记录</span
      >
    </div>
    <el-table v-loading="loading" :data="cardList" :height="recordListHeight">
      <el-table-column label="序号" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车牌号" align="center" prop="cardNumber" />
      <el-table-column
        label="进出场时间"
        align="center"
        prop="time"
        :show-overflow-tooltip="true"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'CardRecord',
  components: {},
  props: {},
  data() {
    return {
      activeIndex: 1,
      recordListHeight: 300,
      loading: false,
      cardList: [
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' },
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' },
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' },
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' },
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' },
        { cardNumber: '苏D88888', time: '2023-03-08' },
        { cardNumber: '苏D99999', time: '2023-03-08' }
      ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.$nextTick(() => {
      const _id = document.getElementById('carRecordList')
      this.recordListHeight = _id.offsetHeight
    })
  },
  methods: {
    chooseTab(v) {
      this.activeIndex = v
    }
  }
}
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
.cardRecord {
  position: relative;
  height: 100%;

  ::v-deep .el-table::before,
  .el-table--group::after,
  .el-table--border::after {
    background: transparent !important;
  }

  .recordTab {
    position: absolute;
    top: -44px;
    right: 0px;
    width: 170px;
    height: 28px;
    background: #e5f0f2;
    border-radius: 4px 4px 4px 4px;
    opacity: 1;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .recordTabI {
      width: 73px;
      height: 18px;
      font-size: 13px;
      font-weight: 400;
      color: rgba(0, 29, 60, 0.75);
      line-height: 15px;
      padding: 2px 10px;
      cursor: pointer;
    }

    .recordTabIA {
      background: #ffffff;
      box-shadow: 0px 2px 4px 0px rgba(0, 29, 60, 0.2);
      border-radius: 4px 4px 4px 4px;
    }
  }
}
</style>
