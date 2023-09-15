<template>
  <div class="pageTitle">
    <div class="left-box">
      <div class="line"></div>
      <div class="title"> <span>{{ title }}</span></div>
    </div>
    <div class="right-box">
      <slot></slot>
    </div>

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
  data() {
    return {

    }
  },
  computed: {},
  watch: {},
  methods: {

  },
  created() {

  },
  mounted() {

  },
}
</script>
<style scoped lang='scss'>
.pageTitle {
  display: flex;
  align-items: center;
  height: 50px;

  .left-box {
    display: flex;
    align-items: center;
    flex: 1;
    .line {
      width: 4px;
      height: 20px;
      background: #3ecaf0;
    }
    .title {
      margin-left: 10px;
      font-size: 16px;
      font-weight: 600;
      color: #000;
    }
  }
  .right-box {
    padding-right: 10px;
    flex: 1;
    display: flex;
    flex-direction: row-reverse;
    align-items: center;
  }
}
</style>