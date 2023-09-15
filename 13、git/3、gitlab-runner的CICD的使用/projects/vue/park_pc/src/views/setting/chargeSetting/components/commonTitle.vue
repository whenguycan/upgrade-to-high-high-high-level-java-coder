<template>
  <div class="common-title">
    <p class="common-title-line"></p>
    <p class="common-title-text">{{ text }}</p>
  </div>
</template>

<script>
export default {
  name: "commonTitle",
  data() {
    return {};
  },
  props: {
    text: {
      type: String,
      default: "",
    },
  },
};
</script>

<style lang="scss" scoped>
.common-title {
  display: flex;
  align-items: center;
  height: 40px;
  background: #eff8fb;
  .common-title-line {
    width: 3px;
    height: 16px;
    background: #49c8ea;
  }
  .common-title-text {
    margin-left: 16px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #001d3c;
  }
}
</style>