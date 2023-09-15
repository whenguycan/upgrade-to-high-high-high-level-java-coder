<template>
  <el-card class="entrance-card">
    <div slot="header" class="box-title">
      <span>{{ entranceName }}</span>
      <span style="float: right; padding: 3px 0;">
        停留 <span style="color: #34A400;">{{ waitTime }}秒</span>
      </span>
    </div>
    <div class="box-content">
      <div style="display: flex;">
        <img class="car-photo" :src="itemData.carImgUrl" />
        <el-form size="mini" style="margin-left: 8px; flex: 1;">
          <el-form-item label="车牌号">
            <span class="fw-600">{{ itemData.carNum }}</span>
            <span class="fw-600" style="margin-left: 20px;">临时车</span>
          </el-form-item>
          <el-form-item label="入场通道">
            <span class="fw-600 w-130">{{ itemData.devName }}</span>
          </el-form-item>
          <el-form-item label="入场时间">
            <span class="fw-600 w-130">{{ itemData.capTime }}</span>
          </el-form-item>
          <el-form-item>
            <el-button type="info" disabled>修改车牌</el-button>
            <el-button type="primary" disabled>重拍</el-button>
            <el-button type="primary" @click="showEdit = true">补录</el-button>
          </el-form-item>
          <el-form-item class="form-footer">
            <img class="no-photo" :src="itemData.carImgUrl" />
            <el-button type="warning" size="medium" class="open-btn">开闸</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="tableList" size="mini" style="margin-top: 4px;">
        <el-table-column prop="carNo" label="车 牌" align="center" />
        <el-table-column prop="entryTime" label="入场时间" align="center" />
        <el-table-column prop="isOpen" label="是否开闸" align="center" />
        <el-table-column prop="remark" label="备注" align="center" />
      </el-table>
    </div>
    <el-dialog title="补录进场信息" :visible.sync="showEdit" width="500px">
      <div style="padding: 0 15%">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="车牌号" prop="carNumber">
            <el-input v-model="form.carNumber" style="width: 220px;"></el-input>
          </el-form-item>
          <el-form-item label="进场时间" prop="entryTime">
            <el-date-picker v-model="form.entryTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="loadingSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { addEntryInfo } from '../api'
import ws from '../utils'

export default {
  props: {
    info: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      itemData: {
        carNum:'',
      },
      entranceName: this.$props.info.passageName,
      waitTime: 0,
      displayInfo: {
        carNo: '苏D072G8',
        entryTime: '2023-02-22 11:18:23'
      },
      tableList: [
        { carNo: '苏D072G8', entryTime: '2023-02-22 12:56:23', isOpen: '已开闸', remark: '临时车_摄像机开闸' },
        { carNo: '苏D072G8', entryTime: '2023-02-22 12:56:23', isOpen: '已开闸', remark: '临时车_摄像机开闸' },
        { carNo: '苏D072G8', entryTime: '2023-02-22 12:56:23', isOpen: '已开闸', remark: '临时车_摄像机开闸' },
      ],
      showEdit: false,
      loadingSubmit: false,
      form: {
        ...this.$props.info,
        carNumber: undefined,
        entryTime: undefined,
      },
      rules: {
        carNumber: [
          { required: true, message: "车牌不能为空", trigger: "blur" },
        ],
        entryTime: [
          { required: true, message: "请选择进场时间", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    ws.initWebSocket(this.info.id, this.getMessage)
  },
  destroyed() {
    ws.closeWebsocket()
  },
  methods: {
    initForm() {
      this.form = {
        ...this.$props.info,
        carNumber: undefined,
        entryTime: undefined,
      }
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          addEntryInfo(this.form).then(res => {
            this.$modal.msgSuccess("新增成功");
            this.showEdit = false;
            this.initForm()
          });
        }
      })
    },
    cancel() {
      this.showEdit = false;
      this.initForm()
    },
    getMessage(e) {
      console.log('getMessage', JSON.parse(e.data))
      this.itemData = JSON.parse(e.data)
      this.itemData.carImgUrl = 'https://121.229.5.12:9443/evo-apigw/ipms/imageConvert/originalImage?fileUrl=' + this.itemData.carImgUrl
      // console.log('getMessage', e)
      // const { data: { carNo, entryTime } } = JSON.parse(e.data)
      // this.displayInfo = {
      //   carNo,
      //   entryTime
      // }
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-card__body {
  padding: 6px;

  .el-form-item--mini.el-form-item {
    margin-bottom: 4px;
    line-height: 20px;

    .el-form-item__label {
      font-weight: 500;
      line-height: 20px;
    }

    .el-form-item__content {
      line-height: 20px;
    }
  }

  .el-button--mini {
    padding: 5px 12px;
  }

  .el-table {

    & tr,
    th {
      pointer-events: none;
      background: #D8D8D8 !important;
      color: #263F59;

      .cell {
        line-height: 20px;
      }
    }

    & th {
      height: 20px !important;
    }
  }

  .el-table--mini .el-table__cell {
    padding: 1px 0;
  }
}

.entrance-card {
  width: 555px;
  height: 320px;
  margin: 10px;

  .box-title {
    font-weight: 600;
    color: #001D3C;
    line-height: 20px;
  }
}

.car-photo {
  width: 332px;
  height: 171px;
}

.form-footer {
  ::v-deep .el-form-item__content {
    display: flex;
    justify-content: space-between;
  }
}

.no-photo {
  width: 120px;
  height: 68px;
  border: 2px solid rgba(245, 172, 40, 1);
  margin-left: -2px;
}

.fw-600 {
  font-weight: 600;
}

.w-130 {
  width: 130px;
}

.open-btn {
  width: 59px;
  height: 68px;
  padding: 10px 14px;
  font-weight: 600;
}
</style>