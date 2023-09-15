<!--
 * @Description:
 * @Author: cuijing
 * @Date: 2023-02-24 10:00:00
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-24 10:00:00
 * @文件相对于项目的路径: /park_pc/src/components/Title/index.vue
-->
<template>
  <div class="title">
    <span class="leftSpan"></span>
    <span class="rightSpan">{{ title }}</span>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    title: {
      type: String,
      default: "标题",
    },
  },
  data() {
    return {};
  },
  computed: {},
  watch: {},
  methods: {},
  created() {},
  mounted() {},
};
</script>
<style scoped lang="scss">
.title {
  display: flex;
  align-items: center;

  .leftSpan {
    width: 3px;
    height: 16px;
    background: #49c8ea;
  }

  .rightSpan {
    margin-left: 15px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #001d3c;
    line-height: 22px;
  }
}
</style>
