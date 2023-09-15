<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-07 09:59:05
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 16:30:03
 * @文件相对于项目的路径: \park_pc\src\views\notice\components\noticePush.vue
-->
<template>
  <div class="noticePush">
    <div class="pushBox">
      <entry-notice :noticeItem="entryNotice" @isEdit="getNoticeList"></entry-notice>
      <exit-notice :noticeItem="exitNotice" @isEdit="getNoticeList"></exit-notice>
      <pay-notice :noticeItem="payNotice" @isEdit="getNoticeList"></pay-notice>
    </div>
  </div>
</template>

<script>
import entryNotice from './entryNotice.vue'
import exitNotice from './exitNotice.vue'
import payNotice from './payNotice.vue'
import {getNoticeList} from '@/views/notice/api/notice.js'
export default {
  components: {
    entryNotice,
    exitNotice,
    payNotice
  },
  props: {},
  data () {
    return {
      noticeList:[],
      entryNotice:null,
      exitNotice:null,
      payNotice:null,
    }
  },
  computed: {},
  watch: {},
  methods: {
    async getNoticeList(){
      let res = await getNoticeList()
      if(res.code == 200){
        this.noticeList = res.rows
        this.noticeList.forEach(item=>{
          if(item.sendTime == '0'){
            this.entryNotice = item
          }else if(item.sendTime == '1'){
            this.exitNotice = item
          }else if(item.sendTime == '2'){
            this.payNotice = item
          }
        })

      }
    }
  },
  created () {

  },
  mounted () {
    this.getNoticeList()
  },
}
</script>
<style scoped lang='scss'>
.noticePush {
  margin: 16px;

  .pushBox {
    display: flex;
    flex-wrap: wrap;
  }
}

.noticePush ::v-deep .el-switch__core {
  border: 1px solid #49BAD9 !important;
}

.noticePush ::v-deep .el-switch__core:after {
  background-color: #49BAD9 !important;
  width: 40px !important;
  height: 16px !important;
  border-radius: 10px !important;
}

.noticePush ::v-deep .el-switch.is-checked .el-switch__core::after {
  background-color: #49BAD9 !important;
  margin-left: -41px !important;
}

.noticePush ::v-deep .el-switch__label--right {
  position: absolute;
  right: 15px;
}

.noticePush ::v-deep .el-switch__label--left {
  position: absolute;
  left: 15px;
  z-index: 1;
}

.noticePush ::v-deep .el-switch__label.is-active {
  color: #ffffff;
}

.noticePush ::v-deep .el-switch__label {
  font-size: 12px;
}

.noticePush ::v-deep .el-switch__label * {
  font-size: 12px;
}
.noticePush ::v-deep .el-textarea__inner{
  font-family: PingFang SC-Regular, PingFang SC;
}
</style>
