<!--
 * @Description:
 * @Author: Adela
 * @Date: 2023-03-07 10:55:21
 * @LastEditors: Adela
 * @LastEditTime: 2023-03-07 10:55:21
 * @文件相对于项目的路径: /park_pc/src/views/setting/chargeSetting/index copy.vue
-->
<template>
  <div class="chargeSetting app-container">
    <el-tabs
      v-model="editableTabsValue"
      type="border-card"
      @tab-remove="removeTab"
      @tab-click="handleClick"
    >
      <el-tab-pane
        :label="item.label"
        v-for="item in editableTabs"
        :key="item.name"
        :closable="item.closable"
        :name="item.name"
      >
      </el-tab-pane>
    </el-tabs>
    <component
      ref="tab"
      :is="configManageCom"
      :parkNo="parkNo"
      @addTab="addTab"
      @addSuccess="addSuccess"
      @closeTab="closeTab"
    ></component>
  </div>
</template>

<script>
import One from "./components/one.vue";
import Two from "./components/two.vue";
import Three from "./components/three.vue";
import Four from "./components/four.vue";
import AddRule from "./components/addRule.vue";
import CreateContact from "./components/createContact.vue";
import CreateHolContact from "./components/createHolContact.vue";
import NoRelationList from "./components/noRelationList.vue";
import RelationList from "./components/relationList.vue";
export default {
  name: "chargeSetting",
  data() {
    return {
      configManageCom: "one",
      parkNo: "P20230222150046",
      editableTabsValue: "0",
      editableTabs: [
        {
          label: "停车场收费方案设置",
          name: "0",
          closable: false,
          component: () => import("./components/one.vue"),
        },
        {
          label: "收费规则流程管理",
          name: "1",
          closable: false,
          // component: () => import("./components/two.vue"),
        },
        {
          label: "区域-车类型-车型-整体收费方案关联设置",
          name: "2",
          closable: false,
          // component: () => import("./components/three.vue"),
        },
        {
          label: "节假日设置与收费方案关联",
          name: "3",
          closable: false,
          // component: () => import("./components/four.vue"),
        },
      ],
      tabIndex: 3,
    };
  },
  components: {
    One,
    Two,
    Three,
    Four,
    AddRule,
    CreateContact,
    CreateHolContact,
    NoRelationList,
    RelationList,
  },
  methods: {
    handleClick(tab) {
      console.log(tab);
      let label = tab.label;
      switch (label) {
        case "停车场收费方案设置":
          this.configManageCom = "one";
          break;
        case "收费规则流程管理":
          this.configManageCom = "two";
          break;
        case "区域-车类型-车型-整体收费方案关联设置":
          this.configManageCom = "three";
          break;
        case "节假日设置与收费方案关联":
          this.configManageCom = "four";
          break;
        case "新增收费方案":
          this.configManageCom = "add-rule";
          break;
        case "整体收费方案-区域-车类型-车型关联设置":
          this.configManageCom = "create-contact";
          break;
        case "节假日-区域-车类型-车型-收费方案关联设置":
          this.configManageCom = "create-hol-contact";
          break;
        case "未关联整体收费方案-停车场-车类型-车型":
          this.configManageCom = "no-relation-list";
          break;
        case "查看已关联收费方案-停车场-车类型-车型":
          this.configManageCom = "relation-list";
          break;
      }
    },
    addTab(label) {
      switch (label) {
        case "新增收费方案":
          this.configManageCom = "add-rule";
          break;
        case "整体收费方案-区域-车类型-车型关联设置":
          this.configManageCom = "create-contact";
          break;
        case "节假日-区域-车类型-车型-收费方案关联设置":
          this.configManageCom = "create-hol-contact";
          break;
        case "未关联整体收费方案-停车场-车类型-车型":
          this.configManageCom = "no-relation-list";
          break;
        case "查看已关联收费方案-停车场-车类型-车型":
          this.configManageCom = "relation-list";
          break;
      }
      let addIndex = this.editableTabs.findIndex((item) => {
        return item.label === label;
      });
      if (addIndex > -1) {
        this.editableTabsValue = addIndex + "";
        return false;
      }
      let newTabName = ++this.tabIndex + "";
      this.editableTabs.push({
        label,
        name: newTabName,
        closable: true,
        // component:
        //   label === "新增收费方案"
        //     ? () => import("./components/addRule.vue")
        //     : label === "整体收费方案-区域-车类型-车型关联设置"
        //     ? () => import("./components/createContact.vue")
        //     : () => import("./components/createHolContact.vue"),
      });
      this.editableTabsValue = newTabName;
    },
    removeTab(targetName) {
      console.log(targetName);
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      if (localStorage.getItem("ruleId")) {
        localStorage.removeItem("ruleId");
      }
      console.log(activeName);
      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter((tab) => tab.name !== targetName);
      const label = this.editableTabs[activeName].label;
      switch (label) {
        case "节假日设置与收费方案关联":
          this.configManageCom = "four";
          break;
        case "新增收费方案":
          this.configManageCom = "add-rule";
          break;
        case "整体收费方案-区域-车类型-车型关联设置":
          this.configManageCom = "create-contact";
          break;
        case "节假日-区域-车类型-车型-收费方案关联设置":
          this.configManageCom = "create-hol-contact";
          break;
        case "未关联整体收费方案-停车场-车类型-车型":
          this.configManageCom = "no-relation-list";
          break;
        case "查看已关联收费方案-停车场-车类型-车型":
          this.configManageCom = "relation-list";
          break;
      }
      this.tabIndex--;
    },
    addSuccess(label) {
      let index = this.editableTabs.findIndex((item) => {
        return item.label === label;
      });
      let reomveIndex = index + "";
      this.removeTab(reomveIndex);
      let value = "",
        compont = "";
      switch (label) {
        case "新增收费方案":
          compont = "two";
          value = "1";
          break;
        case "整体收费方案-区域-车类型-车型关联设置":
          compont = "three";
          value = "2";
          break;
        case "节假日-区域-车类型-车型-收费方案关联设置":
          compont = "four";
          value = "3";
          break;
      }
      this.editableTabsValue = value;
      this.configManageCom = compont;
    },
    closeTab(label, editableTabsValue) {
      let index = this.editableTabs.findIndex((item) => {
        return item.label === label;
      });
      let reomveIndex = index + "";
      this.removeTab(reomveIndex);
      // switch (label) {
      //   case "新增收费方案":
      //     this.configManageCom = "two";
      //     value = "1";
      //     break;
      //   case "整体收费方案-区域-车类型-车型关联设置":
      //     this.configManageCom = "three";
      //     value = "2";
      //     break;
      //   case "节假日-区域-车类型-车型-收费方案关联设置":
      //     this.configManageCom = "four";
      //     value = "3";
      //     break;
      // }
      // this.editableTabsValue = editableTabsValue;
    },
  },
};
</script>

<style></style>
