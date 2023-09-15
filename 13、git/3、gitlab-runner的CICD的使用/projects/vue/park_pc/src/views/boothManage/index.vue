<template>
  <div class="app-container content-box" style="height: 86vh;">
    <div class="image-wrapper">
      <img src="./assets/parking.png" />
    </div>
    <div class="enter-btn" @click="enterSelectPage">
      <span class="text">进入岗亭管理</span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {}
  },
  methods: {
    enterSelectPage() {
      this.$router.push('/boothSelect')
    }
  }
}
</script>

<style scoped lang="scss">
.content-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 48px 0;
}

.image-wrapper {
  width: 72%;
  img {
    width: 100%;
  }
}

.enter-btn {
  background-color: rgba(245, 172, 40, 1);
  border-radius: 41px;
  height: 82px;
  width: 524px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  &:hover {
    opacity: 0.8;
  }

  .text {
    width: 180px;
    height: 42px;
    overflow-wrap: break-word;
    color: rgba(255, 255, 255, 1);
    font-size: 30px;
    font-family: PingFangSC-Semibold;
    font-weight: 600;
    text-align: left;
    white-space: nowrap;
    line-height: 42px;
  }
}
</style>
