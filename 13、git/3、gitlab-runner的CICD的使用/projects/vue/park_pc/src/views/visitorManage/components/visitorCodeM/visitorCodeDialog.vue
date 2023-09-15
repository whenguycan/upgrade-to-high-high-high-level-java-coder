<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-03 11:12:29
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-14 15:13:27
 * @文件相对于项目的路径: /park_pc/src/views/visitorManage/components/visitorCodeM/visitorCodeDialog.vue
-->

<template>
  <div class="visitorCodeDialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogOpen"
      :close-on-click-modal="false"
      width="980px"
      append-to-body
      class="visitorCodeDialog"
      @close="cancel"
    >
      <div class="visitorCodeLc">
        <titleTab title="访客码设置"></titleTab>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="130px"
          style="padding-top: 20px"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item label="访客码名称：" prop="codeName">
                <el-input
                  style="width: 350px"
                  v-model="form.codeName"
                  :disabled="dialogIsCheck"
                  placeholder="请输入访客码名称"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="绑定次数：" prop="codeUseNumber">
                <el-input-number
                  controls-position="right"
                  :min="1"
                  style="width: 350px"
                  v-model="form.codeUseNumber"
                  :disabled="dialogIsCheck"
                  placeholder="请输入绑定次数"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="免费天数：" prop="codeFreeDay">
                <el-input-number
                  controls-position="right"
                  :min="1"
                  style="width: 350px"
                  v-model="form.codeFreeDay"
                  :disabled="dialogIsCheck"
                  placeholder="请输入免费天数"
                  maxlength="50"
                />
                <span> 天</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="二维码有效期：" prop="timeRequired">
                <el-col :span="24">
                  <el-date-picker
                    v-model="form.time"
                    type="daterange"
                    :disabled="form.timeLimit || dialogIsCheck"
                    range-separator="至"
                    start-placeholder="开始"
                    end-placeholder="结束"
                  >
                  </el-date-picker>
                </el-col>
                <el-col :span="24">
                  <el-checkbox
                    v-model="form.timeLimit"
                    :disabled="dialogIsCheck"
                    @change="changeTimeLimit"
                    class="timeLimit"
                  >
                    永久有效期</el-checkbox
                  >
                </el-col>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注：" prop="remark">
                <el-input
                  style="width: 350px"
                  v-model="form.remark"
                  :disabled="dialogIsCheck"
                  type="textarea"
                  :autosize="{ minRows: 2 }"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="btnc" v-if="!dialogIsCheck && isShowAddBtn">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
      <div class="visitorCodeRc">
        <titleTab title="二维码生成"></titleTab>
        <!-- <div class="createCode" style="margin-top: 20px">
          <el-button class="codeBtn"> 二维码一键生成</el-button>
        </div> -->
        <div class="codeContent">
          <div class="codeContentT">二维码</div>
          <div class="codeContentC">
            <canvas id="qrCodeUrl"></canvas>
          </div>
        </div>
        <div class="createCode codeBottom" v-if="detail.id || allowDownload">
          <el-button class="codeGBtn" @click="downloadCodeIg"
            >下载二维码</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import titleTab from "@/components/Title/index.vue";
import { addVisitorCodeItem, editVisitorCodeItem } from "../../api/index";
import qrcode from "qrcode";
export default {
  name: "VisitorCodeDialog",
  components: {
    titleTab,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    detail: {
      type: Object,
    },
    open: {
      type: Boolean,
      default: false,
    },
    dialogIsCheck: {
      type: Boolean,
      default: false,
    },
  },
  watch() {},
  data() {
    var timeRequired = (rule, value, callback) => {
      const { timeLimit, time } = this.form;
      if (!timeLimit) {
        if (time) {
          callback();
        } else {
          callback(new Error("请选择时间"));
        }
      } else {
        callback();
      }
    };
    return {
      allowDownload: false,
      format: "YYYY-MM-DD",
      dialogOpen: this.open,
      rules: {
        codeName: [
          { required: true, message: "访客码不能为空", trigger: "blur" },
        ],
        codeUseNumber: [
          { required: true, message: "绑定次数不能为空", trigger: "blur" },
        ],
        codeFreeDay: [
          { required: true, message: "免费天数不能为空", trigger: "blur" },
        ],
        timeRequired: [
          {
            required: true,
            validator: timeRequired,
            trigger: "blur",
          },
        ],
      },
      form: {
        codeName: "",
        codeUseNumber: "",
        codeFreeDay: "",
        time: null,
        timeLimit: null,
        remark: "",
      },
      isShowAddBtn: true
    };
  },
  computed: {},
  watch: {},
  created() {
    if (this.detail && this.detail.id) {
      this.getformData();
      const _detail = this.detail;
      this.$nextTick(() => {
        this.initRQcode(_detail);
      });
    } else {
      this.$nextTick(() => {
        this.initGQcode();
      });
    }
  },
  mounted() {},
  methods: {
    // 新增、编辑 请求接口数据处理
    handlefromDate() {
      const {
        code,
        codeName,
        codeUseNumber,
        codeFreeDay,
        time,
        timeLimit,
        remark,
        id,
      } = this.form;
      const params = {
        code,
        codeName,
        codeUseNumber,
        codeFreeDay,
        timeLimit: timeLimit ? 1 : 0, // 永久有效时间 '0''-临时 ''1''-永久'
        remark,
      };
      const format = this.format;
      if (time) {
        const startTime = time[0];
        const endTime = time[1];
        const _startTime = startTime
          ? this.$moment(startTime).format(format)
          : "";
        const _endTime = endTime ? this.$moment(endTime).format(format) : "";
        params.startTime = _startTime;
        params.endTime = _endTime;
      }

      if (id != undefined) {
        params.id = id;
      }
      return params;
    },
    // 详情 回填 数据处理
    getformData() {
      if (this.detail.id) {
        const {
          codeName,
          codeUseNumber,
          codeFreeDay,
          endTime,
          startTime,
          timeLimit,
          remark,
          id,
        } = this.detail;
        const _timeLimit = +timeLimit === 1 ? true : false;
        this.form = {
          codeName,
          codeUseNumber,
          codeFreeDay,
          timeLimit: _timeLimit,
          remark,
        };
        if (id != undefined) {
          this.form.id = id;
        }
        if (endTime && startTime) {
          const _time = [startTime, endTime];
          this.form.time = _time;
        }
      }
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.dialogOpen = false;
      this.$emit("cancel", false);
    },
    // 表单重置
    reset() {
      this.form = {
        codeName: "",
        codeUseNumber: "",
        codeFreeDay: "",
        time: null,
        timeLimit: null,
        remark: "",
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.isShowAddBtn = true
      const that = this;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          const parmas = this.handlefromDate();
          if (parmas.id != undefined) {
            editVisitorCodeItem(parmas).then((response) => {
              if (+response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.initRQcode(parmas);
                this.$emit("success", false);
              } else if (+response.code === 601) {
                this.$modal.msgError(response.msg);
              } else {
                this.$modal.msgError("修改失败");
                // this.$emit("cancel", false);
              }
            });
          } else {
            // this.initRQcode(parmas); // 注意需要有code 新增的时候问接口要返回值code
            addVisitorCodeItem(parmas).then((response) => {
              if (+response.code === 200) {
                const code = response.msg;
                this.initRQcode({ ...parmas, code });
                this.$modal.msgSuccess("新增成功");
                this.$emit("success", false);
                this.isShowAddBtn = false
              } else if (+response.code === 601) {
                this.$modal.msgError(response.msg);
              } else {
                this.$modal.msgError("新增失败");
                // this.$emit("cancel", false);
              }
            });
          }
        }
      });
    },
    changeTimeLimit(v) {
      this.$refs.form.validateField("timeRequired");
      const _v = v;
      const { time } = this.form;
      if (v) {
        if (time) {
          const start = time[0];
          const end = time[1];
          const _end = this.$moment(end).add(100, "years");
          this.form.time = [start, _end];
          console.log("adela editor", start, end);
        } else {
          const start = this.$moment(new Date());
          const _end = this.$moment(start).add(100, "years");
          this.form.time = [start, _end];
        }
      } else {
        if (time) {
          const start = time[0];
          const end = time[1];
          const _end = this.$moment(end).subtract(100, "years");
          this.form.time = [start, _end];
        }
      }
    },
    downloadCodeIg() {
      // console.log("adela editor", this.detail);
      var canvas = document.getElementById("qrCodeUrl"); //获取canvas 对象
      var img = canvas.toDataURL("image/png"); //转换城图片的流
      var url = img; // 获取图片地址
      var a = document.createElement("a"); // 创建一个a节点插入的document
      var event = new MouseEvent("click"); // 模拟鼠标click点击事件
      a.download = this.form.codeName || "访客码"; // 设置a节点的download属性值
      a.href = url;
      // console.log(img); //控制台输出
      a.dispatchEvent(event); // 触发鼠标点击事件
    },
    async createQcodeIMG(
      url,
      size = 280,
      qrText = "访客二维码",
      color = "#000"
    ) {
      const canvas = await qrcode.toCanvas(
        document.getElementById("qrCodeUrl"),
        url,
        {
          errorCorrectionLevel: "H",
          // margin: 1, // 设置padding 二维码的padding
          height: size,
          width: size,
          color: { dark: color },
          // version: 5,
          errorCorrectLevel: "L",
          rendererOpts: { quality: 0.8 },
        }
      );
      // console.log("adela editor", "12312", canvas);
      const fontWeight = "bold"; // 字体 粗体 格式
      const fontSize = 14; // 字体大小
      const tb = 5; // 底部文字上下间距
      const realHeight = size + fontSize + 2 * tb; //实际高度

      // 画布上下文对象
      const ctx = canvas.getContext("2d");
      // 获取二维码数据
      const data = ctx.getImageData(0, 0, size, size);
      ctx.fillStyle = "#fff";
      canvas.setAttribute("height", realHeight); // 重设画布像素高度
      canvas.style.setProperty("height", realHeight + "px"); // 重设画布实际高度
      ctx.fillRect(0, 0, size, realHeight);
      ctx.putImageData(data, 0, 0); // 填充二维码数据
      ctx.font = `${fontWeight} ${fontSize}px Arial`;
      const textWidth = ctx.measureText(qrText).width; //文字真实宽度
      const ftop = size; //文字距顶部位置
      const fleft = (size - textWidth) / 2; //根据字体大小计算文字left
      const textPadding = fontSize / 2; //字体边距为字体大小的一半可以自己设置
      // 设置底部背景色
      ctx.fillStyle = "#fff";
      ctx.fillRect(0, size, size, realHeight - 2 * tb);
      // 设置字体填充位置
      ctx.fillRect(
        fleft - textPadding / 2,
        ftop - textPadding / 2,
        textWidth + textPadding,
        fontSize + textPadding
      );
      ctx.textBaseline = "top"; //设置绘制文本时的文本基线。
      ctx.fillStyle = "#333"; // 字体颜色
      ctx.fillText(qrText, fleft, ftop);
      // console.log(canvas.toDataURL());
      return canvas;
    },
    initGQcode() {
      this.createQcodeIMG("暂无数据", 280, "请填写访客码信息", "#c2c2c2");
    },
    async initRQcode(item) {
      const code = item.code ? item.code : "";
      // const url = `http://www.baidu.com?code=${code}`;
      const url = `http://192.168.2.172:81/appointment/index/0?code=${code}`;
      console.log("地址暂时还没换fy那边的现在这样，记录一下", url);
      await this.createQcodeIMG(url, 280, item.codeName, "#000");
      this.allowDownload = true;
    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
@import "../../index.scss";
.visitorCodeDialog {
  ::v-deep .el-dialog__body {
    display: flex;
    padding: 12px;
    justify-content: space-between;
  }

  .visitorCodeLc {
    width: 60%;
    height: 600px;
    background: #ffffff;
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    border-radius: 4px;
    padding: 12px 0;
  }

  .btnc {
    display: flex;
    justify-content: center;
  }

  .visitorCodeRc {
    width: 39%;
    height: 600px;
    background: #ffffff;
    box-shadow: 0px 0px 4px 0px rgba(0, 29, 60, 0.15);
    border-radius: 4px;
    padding: 12px 0;

    .createCode {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .codeContent {
      margin: 20px auto;
      width: 309px;
      // height: 346px;
      background: #ffffff;
      border: 1px solid #d7e5e9;
      .codeContentT {
        width: 309px;
        height: 40px;
        line-height: 40px;
        background: #eff8fb;
        border-bottom: 1px solid #d7e5e9;
        font-size: 16px;
        font-weight: 600;
        color: #001d3c;
        padding: 0 0 0 18px;
      }
      .codeContentC {
        display: flex;
        justify-content: center;
        // width: 280px;
        height: 324px;
        // height: 300px;
      }
    }
    .codeBottom {
    }
  }
  .flowPlaceNumber .el-input-number {
    width: 110px;
  }
  .timeLimit {
    // margin-left: 20px;
  }
}
</style>
