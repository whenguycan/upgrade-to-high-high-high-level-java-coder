<template>
  <div class="passageway">
    <div class="main-list">
      <page-title title="通道列表">
        <div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" size="mini" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" size="mini" @click="opneQr">打印二维码</el-button>
            </el-col>
          </el-row>
        </div>
      </page-title>
      <div style="padding: 0 20px">
        <el-table v-loading="loading" :data="postList" highlight-current-row @current-change="handleCurrentChange"
          ref="singleTable">
          <el-table-column label="序号" align="center" type="index" width="80">
            <template slot-scope="scope">
              <span>{{
                scope.$index + (queryParams.pageNum - 1) * queryParams.pageSize + 1
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="通道名称" align="center" prop="passageName" />
          <el-table-column label="通行方向" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.fromFieldName }}</span>
              -->
              <span>{{ scope.row.toFieldName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开闸方式" align="center" prop="status">
            <template slot-scope="scope">
              <span>{{ showDictLabel(dict.type.open_type, scope.row.openType) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="passageStatus">
            <template slot-scope="scope">
              <div class="statusI">
                <span :class="`${+scope.row.passageStatus === 1 ? 'statusIcon' : 'statusGreyIcon'
                  }`"></span>
                <span class="statusName">{{
                  +scope.row.passageStatus === 1 ? "已启用" : "已停用"
                }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button size="mini" style="color: red" type="text" @click="handleDelete(scope.row)">删除</el-button>
              <el-button size="mini" style="color: green" type="text" @click="handelOpenQR(scope.row)">二维码</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </div>
      <!-- 添加或修改通道对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
          <el-form-item label="通道名称" prop="passageName">
            <el-input v-model="form.passageName" placeholder="请输入通道名称" />
          </el-form-item>
          <el-form-item label="通道编号" prop="passageNo">
            <el-input v-model="form.passageNo" placeholder="请输入通道编号" />
          </el-form-item>
          <el-form-item label="开闸方式" prop="openType">
            <el-select v-model="form.openType" placeholder="开闸方式" clearable>
              <el-option v-for="dict in dict.type.open_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="通行方向" prop="fromFieldId">
            <el-select v-model="form.fromFieldId" placeholder="开闸方式" clearable>
              <el-option v-for="item in comboboxList" :key="item.id" :label="item.text" :value="item.id" />
            </el-select>
            <span> 至 </span>
            <el-select v-model="form.toFieldId" placeholder="开闸方式" clearable>
              <el-option v-for="item in comboboxList" :key="item.id" :label="item.text" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="passageStatus">
            <el-radio-group v-model="form.passageStatus">
              <el-radio v-for="dict in dict.type.field_status" :key="dict.value" :label="dict.value">{{ dict.label
              }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="通道标识" prop="passageFlag">
            <el-radio-group v-model="form.passageFlag">
              <el-radio key="1" label="1">
                入口
              </el-radio>
              <el-radio key="2" label="2">
                出口
              </el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="绑定固定车类型：" prop="bandRegularCodes">
            <el-select v-model="bandRegularCodes" multiple placeholder="请选择" clearable style="width: 530px">
              <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="fix-list">
      <page-title title="通道列表">
      </page-title>
      <div class="tabBox">
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane label="通道设备绑定" name="1">
            <div slot="label" class="tabSkew">
              <div class="tabFirstImg"></div>
              <div class="tabName">通道设备绑定</div>
              <div class="tabRImg" v-if="+activeName != 2"></div>
            </div>
            <bindDevice :passageId="passageId"></bindDevice>
          </el-tab-pane>
          <el-tab-pane label="设备明细" name="2">
            <div slot="label" class="tabSkew">
              <div class="tabLeftImg"></div>
              <div class="tabName">设备明细</div>
              <div class="tabLastImg"></div>
            </div>
            <deviceList></deviceList>
          </el-tab-pane>
          <!-- <el-tab-pane label="出口岗亭计算机管理的通道明细">角色管理</el-tab-pane> -->
        </el-tabs>
      </div>
    </div>

    <!-- 二维码弹窗 -->
    <el-dialog title="二维码" :visible.sync="openQr" append-to-body>

      <div class="codeContent">
        <div class="codeContentC">
          <canvas id="qrCodeUrl"></canvas>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="downloadCodeIg">保 存</el-button>
        <el-button @click="openQr = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { fieldComboboxList } from "@/api/fieldLibraryManage/region";
import { listPassage, getPassage, delPassage, addPassage, updatePassage, getRegularCarCategorys } from "@/api/fieldLibraryManage/passageway";
import pageTitle from "@/views/components/pageTitle"
import deviceList from "./components/deviceList.vue"
import bindDevice from "./components/bindDevice.vue"
import qrcode from "qrcode";
export default {
  name: "Post",
  dicts: ['field_status', 'open_type'],
  components: { pageTitle, deviceList, bindDevice },
  data() {
    return {
      qrData: {
        name: ''
      },
      openQr: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 通道表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        passageName: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        openType: [
          { required: true, message: "请选择开闸方式", trigger: "blur" },
        ],
        passageNo: [
          { required: true, message: "通道编号不能为空", trigger: "blur" }
        ]
      },
      comboboxList: [],
      passageId: '',
      activeName: '1',
      //绑定固定车类型
      bandRegularCodes: [],
      options: []
    };
  },
  computed: {
    ...mapGetters(["parkInfo"]),
  },
  created() {
    this.getList();
    this.getComboboxList();
    this.getRegularCarCategorys();
  },
  methods: {
    handleCurrentChange(val) {
      // this.currentRow = val;
      console.log(val)
      this.passageId = val.id
    },
    showDictLabel(dict, value) {
      return dict.find(item => item.value == value)?.label || '--'
    },
    /** 查询通道列表 */
    getList() {
      this.loading = true;
      this.queryParams.parkNo = this.parkInfo.deptId
      listPassage(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getComboboxList() {
      this.loading = true;
      fieldComboboxList().then(response => {
        this.comboboxList = response
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        passageName: undefined,
        spaceCount: undefined,
        passageStatus: "1",
        passageFlag: '1',
        bandRegularCodes: ''
      };
      this.bandRegularCodes = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加通道";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getPassage(row.id).then(response => {
        response.fromFieldId = response.fromFieldId + ''
        response.toFieldId = response.toFieldId + ''
        this.form = response;
        if (response.bandRegularCodes != null) {
          this.bandRegularCodes = response.bandRegularCodes.split(",");
          let newArray = [];
          this.bandRegularCodes.forEach((v, i) => {
            newArray.push(Number(v));
          });
          this.bandRegularCodes = [];
          this.bandRegularCodes = newArray;
        }
        this.open = true;
        this.title = "修改通道";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          //开闸方式为固定车自动开闸，绑定固定车类型必填
          if (this.form.openType == 'Vip') {
            if (this.bandRegularCodes.length == 0) {
              this.$modal.alertWarning("开闸方式为固定车自动开闸时，请绑定固定车类型！");
              return;
            }
          }
          this.form.bandRegularCodes = this.bandRegularCodes.join(",");
          if (this.form.id != undefined) {
            updatePassage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.parkNo = this.parkInfo.deptId
            addPassage(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      // const postIds = row.postId || this.ids;
      this.$modal.confirm('是否确认删除通道名称为"' + row.passageName + '"的数据项？').then(function () {
        return delPassage({ id: row.id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    getRegularCarCategorys() {
      getRegularCarCategorys().then(response => {
        this.options = response.rows;
      });
    },
    downloadCodeIg() {
      // console.log("adela editor", this.detail);
      var canvas = document.getElementById("qrCodeUrl"); //获取canvas 对象
      var img = canvas.toDataURL("image/png"); //转换城图片的流
      var url = img; // 获取图片地址
      var a = document.createElement("a"); // 创建一个a节点插入的document
      var event = new MouseEvent("click"); // 模拟鼠标click点击事件
      a.download = this.qrData.name; // 设置a节点的download属性值
      a.href = url;
      // console.log(img); //控制台输出
      a.dispatchEvent(event); // 触发鼠标点击事件
    },
    async createQcodeIMG(
      url,
      size = 280,
      qrText = "二维码",
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
    async initRQcode(code, name) {
      const url = `http://192.168.2.172:81/appointment/index/0?code=${code}`;
      await this.createQcodeIMG(url, 280, name, "#000");
    },
    handelOpenQR(row) {
      this.openQr = true
      this.qrData.name = row.passageName
      this.$nextTick(() => {
        this.initRQcode(row.passageNo, this.qrData.name);
      });
    },
    opneQr() {
      this.openQr = true
      this.qrData.name = '场库二维码'
      this.$nextTick(() => {
        this.initRQcode('', this.qrData.name);
      });
    }
  }
};
</script>

<style scoped lang='scss'>
@import '@/assets/styles/tab.scss';

.passageway {
  margin: 20px;
  display: flex;
  height: calc(100vh-40px);

  .fix-list {
    width: 600px;
    background: #fff;
    // padding: 20px;
    border-radius: 5px;

  }

  .main-list {
    flex: 1;
    background: #fff;
    // padding: 20px;
    padding-bottom: 20px;
    margin-right: 10px;
    border-radius: 5px;
  }

  .statusI {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .statusIcon {
    display: inline-block;
    width: 6px;
    height: 6px;
    background: #52c41a;
    border-radius: 3px;
    border: 0px solid rgba(0, 0, 0, 0.88);
    margin-right: 6px;
  }

  .statusGreyIcon {
    display: inline-block;
    width: 6px;
    height: 6px;
    background: #9f9f9f;
    border-radius: 3px;
    border: 0px solid rgba(0, 0, 0, 0.88);
    margin-right: 6px;
  }
}

.codeContent {
  // margin: 20px auto;
  // width: 309px;
  // height: 346px;
  background: #ffffff;
  border: 1px solid #d7e5e9;
  display: flex;
  align-items: center;
  justify-content: center;

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

// ::v-deep .el-tabs--border-card>.el-tabs__content {
//   padding: 0 15px;
// }
</style>

