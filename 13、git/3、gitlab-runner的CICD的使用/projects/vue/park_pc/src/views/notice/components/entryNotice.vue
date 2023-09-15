<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-03-07 09:59:05
 * @LastEditors: cuijing
 * @LastEditTime: 2023-03-10 16:25:04
 * @文件相对于项目的路径: \park_pc\src\views\notice\components\entryNotice.vue
-->
<template>
  <div class="pushCon">
    <div class="pushConTop">
      <span class="tit">{{ notice.name }}</span>
      <el-switch v-model="notice.status" active-color="#ffffff" inactive-color="#ffffff" :width="80" active-text="开"
        inactive-text="关" @change="changeNotice">
      </el-switch>
    </div>
    <div class="pushConBot" v-if="notice.isEdit">
      <div class="temLi">
        <span class="temTit">模板ID:</span>
        <span class="temCon">{{ notice.templateId }}</span>
      </div>
      <div class="temLi">
        <el-input v-model="notice.firstData" type="textarea" :rows="2" placeholder="请输入消息开始内容，例：您好，您已进入XX停车场" maxlength="10" show-word-limit></el-input>
      </div>
      <div class="temLi">
        <span class="temTit">停车场名称:</span>
        <span class="temCon">XX停车场</span>
      </div>
      <div class="temLi">
        <span class="temTit">车牌号:</span>
        <span class="temCon">苏D:XXXXXX</span>
      </div>
      <div class="temLi">
        <span class="temTit">进场时间:</span>
        <span class="temCon">2022-02-06 12:23:00</span>
      </div>
      <div class="temLi">
        <el-input v-model="notice.remarkData" type="textarea" :rows="2" placeholder="请输入消息结束内容，例：感谢您的使用。" maxlength="20" show-word-limit></el-input>
      </div>
      <div class="temLi" style="justify-content: flex-end;">
        <el-button type="primary" size="mini" class="confirmButton" @click="editConfirm(false)">确定</el-button>
        <el-button size="mini" class="cancleButton" @click="notice.isEdit = false">取消</el-button>
      </div>
    </div>
    <div class="pushConBot" v-else>
      <div class="grey" v-if="!notice.status"></div>
      <div class="temLi">
        <span class="temTit">模板ID:</span>
        <span class="temCon">{{ notice.templateId }}</span>
      </div>
      <div class="temBox">
        <div class="edit" @click="edit" v-if="notice.status"><i class="el-icon-edit"></i></div>
        <div class="temLi">
          <span class="temConn">{{ notice.firstData }}</span>
        </div>
        <div class="temLi">
          <span class="temTit">停车场名称:</span>
          <span class="temCon">XX停车场</span>
        </div>
        <div class="temLi">
          <span class="temTit">车牌号:</span>
          <span class="temCon">苏D:XXXXXX</span>
        </div>
        <div class="temLi">
          <span class="temTit">进场时间:</span>
          <span class="temCon">2022-02-06 12:23:00</span>
        </div>
        <div class="temLi">
          <span class="temConn">{{ notice.remarkData }}</span>
        </div>
        <div class="temLi temBtn">
          <span class="temLeft">详情</span>
          <span class="temRight">></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {editNotice} from '@/views/notice/api/notice.js'
export default {
  components: {},
  props: {
    noticeItem:{
      type:Object,
      default(){
        return {}
      }
    }
  },
  watch:{
    noticeItem:{
      handler: function (val, oldVal) {
        if (val) {
          this.notice.status = this.noticeItem.status == 1 ? true : false
          this.notice.firstData = this.noticeItem.firstData
          this.notice.remarkData = this.noticeItem.remarkData
        }
      },
      deep: true,
    }
  },
  data () {
    return {
      notice: {
        name: '入场通知',
        status: false,
        templateId:'qMWOPqkj5bOx2_TDl1_6uNMYWtVwsonF-HL312PeWUA',
        firstData: '',
        remarkData: '',
        isEdit: false,
      }
    }
  },
  computed: {},
  methods: {
    edit () {
      this.notice.isEdit = true
    },
    async editConfirm (isOpen=false) {
      let param = {
        id:this.noticeItem.id,
        parkNo:this.noticeItem.parkNo,
        templateId:this.notice.templateId,
        firstData:this.notice.firstData,
        remarkData:this.notice.remarkData,
        status:this.notice.status ? 1 : 0
      }
      let res = await editNotice(param)
      if(res.code == 200){
        if(isOpen){
          if(this.notice.status){
            this.$message.success("开启入场通知成功！")
          }else{
            this.$message.success("关闭入场通知成功！")
          }
        }else{
          this.$message.success("编辑入场通知成功！")
        }
        this.notice.isEdit = false
        this.$emit('isEdit',true)
      }else{
        if(isOpen){
          if(this.notice.status){
            this.$message.error("关闭入场通知失败！")
          }else{
            this.$message.success("关闭入场通知失败！")
          }
        }else{
          this.$message.error("编辑入场通知失败！")
        }
        this.notice.isEdit = false
        this.$emit('isEdit',true)
      }
    },
    changeNotice(val){
      this.notice.status = val
      this.editConfirm(true)
    }
  },
  created () {

  },
  mounted () {
    
  },
}
</script>
<style scoped lang='scss'>
.pushCon {
  width: 356px;
  min-height: 336px;
  background: #FFFEFE;
  box-shadow: 0px 0px 6px 0px rgba(0, 29, 60, 0.2);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  margin-right: 15px;
  margin-bottom: 15px;

  .pushConTop {
    padding: 0 20px;
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: linear-gradient(90deg, rgba(73, 186, 217, 0.12) 0%, rgba(73, 186, 217, 0.02) 100%);

    .tit {
      font-size: 17px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #001D3C;
    }
  }
  .grey{
    position:absolute;
    background-color: rgba(255,254,254,0.6);
    top:0px;
    left:0px;
    width:100%;
    height:100%;
    z-index:1;
  }
  .pushConBot {
    flex:1;
    display: flex;
    flex-direction: column;
    padding: 15px 20px;
    position:relative;

    .temBox {
      border: 1px solid #ddd;
      padding: 10px;
      position: relative;
    }

    .temBtn {
      border-top: 1px solid #ddd;
      justify-content: space-between;
      margin-top: 5px;
    }

    .edit {
      position: absolute;
      right: 5px;
      top: 5px;
      cursor: pointer;

      i {
        color: #49bad9;
      }

      span {
        font-size: 14px;
        font-weight: 600;
        color: #49bad9;
        margin-left: 5px;
      }
    }

    .temLi {
      width: 100%;
      padding: 5px 0;
      display: flex;
      // align-items: center;

      .temTit {
        width: 80px;
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(0, 29, 60, 0.7);
      }

      .temCon {
        flex: 1;
        margin-left: 5px;
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #001D3C;
        word-break: break-all;
      }

      .temConn {
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #001D3C;
        word-break: break-all;
      }

      .temLeft {
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(0, 29, 60, 0.7);
        word-break: break-all;
      }

      .temRight {
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: rgba(0, 29, 60, 0.7);
        word-break: break-all;
      }
    }
  }
}
</style>

