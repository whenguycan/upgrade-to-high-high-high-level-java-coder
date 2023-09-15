<template>
  <div class="app-container monitor-container">
    <el-card class="top-group">
      <el-button type="primary top-btn" icon="el-icon-setting" @click="showEdit = true">修改空位</el-button>
      <el-button class="top-btn refresh" icon="el-icon-refresh-right" @click="init()">刷新</el-button>
      <el-button class="top-btn search" icon="el-icon-search" @click="onSearch">查询</el-button>
    </el-card>
    <div v-loading="loading" class="monitor-content">
      <Entrance v-for="entrance in entrances" :key="entrance.id" :info="entrance" />
      <Exit v-for="exit in exits" :key="exit.id" :info="exit" />
    </div>
    <SearchDialog ref="searchDialog" />
    <template>
    <el-dialog title="修改空位" :visible.sync="showEdit">
      <el-form style="padding: 0 30%;">
        <el-form-item label="剩余车位">
          <el-input-number v-model="idleNum" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button type="primary" @click="handleCommit" :disabled="!idleNum" :loading="loadingCommit">确 定</el-button>
      </span>
    </el-dialog>
  </template>
  </div>
</template>

<script>
import Entrance from './components/entrance.vue';
import Exit from './components/exit.vue';
import SearchDialog from './components/searchDialog.vue';
import { editParkingSpace } from './api'
  
export default {
  components: { SearchDialog, Entrance, Exit },
  data() {
    return {
      loading: true,
      entrances: [],
      exits: [],
      showEdit: false,
      idleNum: undefined,
      loadingCommit: false
    }
  },
  created() {
    this.init()
  },
  destroyed() {
    sessionStorage.removeItem('entrances')
    sessionStorage.removeItem('exits')
  },
  methods: {
    init() {
      this.loading = true
      this.entrances = JSON.parse(sessionStorage.getItem('entrances'))
      this.exits = JSON.parse(sessionStorage.getItem('exits'))
      this.loading = false
    },
    onSearch() {
      this.$refs.searchDialog.showDialog()
    },
    handleCancel() {
      this.showEdit = false
      this.idleNum = undefined
    },
    handleCommit() {
      const parkNo = this.entrances[0].parkNo
      this.loadingCommit = true
      editParkingSpace({ parkNo, number: this.idleNum }).then(res => {
        this.$modal.msgSuccess("修改成功");
        this.showEdit = false
        this.idleNum = undefined
        this.loadingCommit = false
      })
    }
  }
};
</script>
  
<style lang="scss" scoped>
.monitor-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  min-height: 180px;
}
.top-group {
  width: 100%;
  height: 60px;
  margin-bottom: 20px;
}

.top-btn {
  height: 32px;
  box-shadow: 0px 2px 0px 0px rgba(5,145,255,0.1);
  border-radius: 6px;
  border: 1px solid rgba(0,0,0,0);
  color: #fff;
  padding: 9px 5px;
}

.refresh {
  background: #57AAC2;
}

.search {
  background: #4F9294;
}

.monitor-content {
  height: 100%;
  display: inline-flex;
  justify-content: space-around;
  flex-wrap: wrap;

/* 使用伪元素辅助左对齐 */
  &::after {
    content: '';
    // flex: auto;    /* 或者flex: 1 */
    width: 575px;
  }
}

::v-deep .el-loading-spinner {
  margin-top: -42px;
}
</style>
