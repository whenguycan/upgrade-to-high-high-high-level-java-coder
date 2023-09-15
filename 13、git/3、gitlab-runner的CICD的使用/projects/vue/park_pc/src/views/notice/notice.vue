<!--
 * @Description: 消息配置
 * @Author: cuijing
 * @Date: 2023-02-24 09:30:53
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 08:53:16
 * @文件相对于项目的路径: \park_pc\src\views\notice\notice.vue
-->
<template>
  <div class="delList tabBox">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="消息推送配置" name="1">
        <div slot="label" class="tabSkew">
          <div class="tabFirstImg"></div>
          <div class="tabName">消息推送配置</div>
          <div class="tabRImg" v-if="+activeName != 2"></div>
        </div>
        <noticePush></noticePush>
      </el-tab-pane>
      <!-- <el-tab-pane label="消息推送模板" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">消息推送模板</div>
          <div class="tabRImg" v-if="activeName != 3"></div>
        </div>
        <noticeTemplate></noticeTemplate>
      </el-tab-pane> -->
      <el-tab-pane label="消息记录" name="2">
        <div slot="label" class="tabSkew">
          <div class="tabLeftImg"></div>
          <div class="tabName">消息记录</div>
          <div class="tabLastImg"></div>
        </div>
        <noticeList></noticeList>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import noticeList from './components/noticeList.vue'
import noticePush from './components/noticePush.vue'
import noticeTemplate from './components/noticeTemplate.vue'
export default {
  components: {
    noticeList,
    noticePush,
    noticeTemplate
  },
  props: {},
  data () {
    return {
      activeName: '1'
    }
  },
  computed: {},
  watch: {},
  methods: {
    handleClick () {

    }
  },
  created () {

  },
  mounted () {

  },
}
</script>
<style scoped lang='scss'>
@import '@/assets/styles/tab.scss'
</style>
