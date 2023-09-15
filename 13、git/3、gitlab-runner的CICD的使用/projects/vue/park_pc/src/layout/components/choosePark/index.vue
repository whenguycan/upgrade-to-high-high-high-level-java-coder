<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-02-23 15:03:12
 * @LastEditors: Adela
 * @LastEditTime: 2023-02-23 16:51:01
 * @文件相对于项目的路径: /park_pc/src/layout/components/choosePark/index.vue
-->
<template>
  <div class="choosePark">

    <!-- v-if="isAdminorManger" -->
    <div v-if="isAdminorManger || reLogin" @click="changePark">
      {{ parkInfo.deptName }}
      <!-- {{ parkInfo.deptId }} -->
      <i class="el-icon-caret-bottom" />
    </div>
    <div v-else>
      {{ parkInfo.deptName }}
    </div>
    <el-dialog title="切换车场" :visible.sync="open" width="400px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-row>
        <el-col :span="24">
          <el-form-item label="所属车场" prop="deptId">
            <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择所属车场"
              @select="onChange" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-row>
          <el-col :span="24">
            <el-form-item label="所属车场" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择">
                <el-option
                  v-for="item in carParkList"
                  :key="item.deptId"
                                                          :label="item.deptName"
                                                          :value="item.deptId"
                                                        >
                                                        </el-option>
                                                      </el-select>
                                                    </el-form-item>
                                                  </el-col>
                                                </el-row> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters, mapMutations } from "vuex";
import { deptChangeTree } from "@/api/system/user";
import { reLogin } from "@/api/login";
import { notDeepEqual } from "assert";
export default {
  name: "Index",
  components: {
    Treeselect,
  },
  props: {},
  data() {
    return {
      open: false,
      form: {
        deptId: null,
      },
      currentDept: {
        deptId: '',
        deptName: '',
        isAdmin: '',
      },
      deptOptions: null,
    };
  },
  computed: {
    ...mapGetters(["parkInfo", "roles", "carParkList", 'isAdminorManger', 'reLogin']),
  },
  watch: {},
  created() {
    this.form.deptId = this.parkInfo.deptId

    this.currentDept.deptId = this.parkInfo.deptId
    this.currentDept.deptName = this.parkInfo.deptName

    this.getDeptTree();
    console.log('isAdminorManger', this.reLogin)
  },
  mounted() { },
  methods: {
    ...mapMutations(['SET_PARKINFO']),
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptChangeTree().then((response) => {
        console.log('1111')
        this.deptOptions = response.data;
      });
    },
    changePark() {
      this.open = true;
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.resetForm("form");
    },
    onChange(node) {
      console.log(node)
      this.currentDept.deptId = node.id
      this.currentDept.deptName = node.label
      this.currentDept.isAdmin = node.isAdmin
    },
    submitForm() {
      console.log('2', this.form);
      this.open = false;
      // this.$store.commit('SET_PARKINFO', {
      //   deptId: this.currentDept.deptId,
      //   deptName: this.currentDept.deptName
      // })


      //重新登陆 id 换token
      //保存token
      // 浏览器跳转
      this.$store.dispatch("reLogin", {
        isAdmin: this.currentDept.isAdmin,
        deptId: this.currentDept.deptId
      }).then(() => {
        const local = window.location.origin;
        window.location.href = local
      }).catch(() => {

      })

      // reLogin(this.currentDept.deptId).then(res=>{

      // })


    },
  },
};
</script>

<style scoped lang="scss">
//@import 'xxx.scss';引入公共css类
</style>
