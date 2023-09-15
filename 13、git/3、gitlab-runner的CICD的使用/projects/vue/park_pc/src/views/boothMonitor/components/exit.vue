<template>
  <el-card class="exit-card">
    <div slot="header" class="box-title">
      <span>{{ exitName }}</span>
      <span style="float: right; padding: 3px 0;">
        停留 <span style="color: #34A400;">{{ waitTime }}秒</span>
      </span>
    </div>
    <div style="display: flex;">
      <div style="display: flex;flex-direction: column;">
        <img class="car-photo"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng97ba621144b4bd71e625be52c485487b4a517215628bb532444afff95679ba77" />
      </div>
      <el-form size="mini" style="margin-left: 8px; flex: 1;">
        <el-form-item label="车牌号">
          <span class="fw-600">{{ displayInfo.carNo }}</span>
          <span class="fw-600" style="margin-left: 20px;">临时车</span>
        </el-form-item>
        <el-form-item label="入场通道">
          <span class="fw-600 w-130">{{ displayInfo.entranceName }}</span>
        </el-form-item>
        <el-form-item label="入场时间">
          <span class="fw-600 w-130">{{ displayInfo.enterTime }}</span>
        </el-form-item>
        <el-form-item label="出场时间">
          <span class="fw-600 w-130">{{ displayInfo.exitTime }}</span>
        </el-form-item>
        <el-form-item label="停留时长">
          <span class="fw-600 w-130">{{ displayInfo.exitTime }}</span>
        </el-form-item>
        <el-form-item label="车型">
          <span class="fw-600 w-130 ml-28">{{ displayInfo.carType }}</span>
        </el-form-item>
        <el-form-item label="费用">
          <span class="fw-600 w-130 fees ml-28">{{ displayInfo.fees }}</span>
          <span class="fw-600" style="margin-left: 10px;">元</span>
        </el-form-item>
      </el-form>
    </div>
    <div style="display: flex;margin-top: 16px;">
      <img class="no-photo"
        src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng169f80afb3d9051e985f4248535fab682f25758e6f412908dd642ebc683aa684" />
      <div class="button-group">
        <el-button size="mini" type="primary" style="margin-left: 8px;" @click="editCarNo">补录</el-button>
        <el-button size="mini" type="primary">重拍</el-button>
        <el-button size="mini" type="info">返场</el-button>
      </div>
      <el-form size="mini" class="footer-form">
        <el-form-item label="车牌号">
          <span>{{ displayInfo.carNo }}</span>
        </el-form-item>
        <el-form-item label="停车费">
          <span>{{ displayInfo.fees }} 元</span>
        </el-form-item>
        <el-form-item label="时间">
          <span>{{ displayInfo.stayTime }}</span>
        </el-form-item>
      </el-form>
      <div class="form-footer">
        <el-button size="medium" type="primary" class="normal-btn">
          <span class="block-mb-1">收费</span><br /><span class="block-mb-1">播报</span>
        </el-button>
        <el-button size="medium" type="primary" class="normal-btn">
          <span class="block-mb-1">免费</span>
        </el-button>
        <el-button size="medium" type="warning" class="normal-btn" @click="showDialog = true">
          <span class="block-mb-1">开闸</span><br /><span class="block-mb-1">收费</span>
        </el-button>
      </div>
    </div>
    <el-dialog :title="isEdit ? '修改车牌' : '开闸收费'" :visible.sync="showDialog" @closed="cancel" destroy-on-close
      width="500px">
      <div style="padding: 0 15%">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item v-if="isEdit" label="车牌号" prop="carNumber">
            <el-input v-model="form.carNumber" style="width: 220px;"></el-input>
          </el-form-item>
          <el-form-item v-else label="线下收费" prop="offlineCharges">
            <el-input v-model="form.offlineCharges" style="width: 220px;"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="loadingSubmit">确 定</el-button>
        <el-button @click="showDialog = false">取 消</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { editExitCarNo } from '../api'
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
      exitName: this.$props.info.passageName,
      waitTime: 0,
      displayInfo: {
        carNo: '苏D072G8',
        entranceName: '停车场主入口',
        enterTime: '2023-02-22 11:18:23',
        exitTime: '2023-02-22 11:18:23',
        stayTime: '1小时28分09秒',
        carType: '小型车',
        fees: '5'
      },
      itemData: {},
      showDialog: false,
      isEdit: false,
      form: { ...this.$props.info },
      loadingSubmit: false
    }
  },
  computed: {
    rules() {
      if (this.isEdit) {
        return {
          carNumber: [{ required: true, message: "车牌不能为空", trigger: "blur" }],
        }
      }
      return {
        offlineCharges: [{ required: true, message: "请输入实收金额", trigger: "blur" }],
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
      this.form = { ...this.$props.info }
    },
    editCarNo() {
      this.showDialog = true
      this.isEdit = true
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loadingSubmit = true
          editExitCarNo(this.form).then(res => {
            this.loadingSubmit = false
            this.$modal.msgSuccess(isEdit ? "修改成功" : "已收费");
            this.showDialog = false;
            this.isEdit = false
            this.initForm()
          });
        }
      })
    },
    cancel() {
      this.showDialog = false;
      this.isEdit = false
      this.initForm()
    },
    getMessage(e) {
      console.log('getMessage', JSON.parse(e.data))
      this.itemData = JSON.parse(e.data)
      this.itemData.carImgUrl = 'https://121.229.5.12:9443/evo-apigw/ipms/imageConvert/originalImage?fileUrl=' + this.itemData.carImgUrl
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

  .el-input__inner {
    padding: 0 8px;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 4px;

    .el-form-item__label {
      font-weight: 500;
      line-height: 20px;
    }

    .el-form-item__content {
      line-height: 20px;
    }
  }

  .el-button--mini {
    padding: 3px 12px;
  }
}

.exit-card {
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
  margin-left: 5px;
  display: flex;
}

.no-photo {
  width: 116px;
  height: 64px;
  border: 2px solid rgba(245, 172, 40, 1);
}

.button-group {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  // height: 91px;

  .el-button+.el-button {
    margin-left: 8px;
  }
}

.footer-form {
  margin-left: 10px;
  flex: 1;

  span {
    font-size: 12px;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 2px;
  }

  ::v-deep .el-form-item {
    .el-form-item__label {
      padding: 1px 4px;
      font-weight: 400;
      width: 46px;
      text-align-last: justify;
      background: #D8D8D8;
      font-size: 12px;
      line-height: 29px;
    }

    .el-form-item__content {
      background: #D8D8D8;
      margin-left: 48px;
      padding-left: 4px;
    }
  }
}

.fw-600 {
  font-weight: 600;
}

.w-130 {
  width: 130px;
}

.ml-28 {
  margin-left: 28px;
}

.fees {
  font-size: 16px;
  color: #c80000;
}

.block-mb-1 {
  display: block;
  margin: 0 0 1px 0;
}

.normal-btn {
  width: 59px;
  height: 68px;
  border-radius: 8px;
  padding: 10px 14px;
  border-radius: 8px;
  font-weight: 600;
}
</style>