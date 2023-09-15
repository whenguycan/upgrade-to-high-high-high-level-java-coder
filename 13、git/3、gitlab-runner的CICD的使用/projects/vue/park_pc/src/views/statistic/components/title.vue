<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-22 16:12:56
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-22 16:27:54
 * @文件相对于项目的路径: \park_pc\src\views\statistic\components\title.vue
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
      default: ''
    }
  },
  data () {
    return {

    }
  },
  computed: {},
  watch: {},
  methods: {

  },
  created () {

  },
  mounted () {

  },
}
</script>
<style scoped lang='scss'>
.title {
  display: flex;
  align-items: center;

  .leftSpan {
    width: 3px;
    height: 16px;
    background: #49C8EA;
  }

  .rightSpan {
    margin-left:15px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #001D3C;
    line-height: 22px;
  }
}
</style>