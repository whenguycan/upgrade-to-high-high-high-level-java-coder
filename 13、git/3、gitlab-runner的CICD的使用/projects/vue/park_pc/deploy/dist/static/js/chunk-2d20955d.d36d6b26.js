(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d20955d"],{a92a:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",e._g(e._b({attrs:{width:"500px","close-on-click-modal":!1,"modal-append-to-body":!1},on:{open:e.onOpen,close:e.onClose}},"el-dialog",e.$attrs,!1),e.$listeners),[a("el-row",{attrs:{gutter:15}},[a("el-form",{ref:"elForm",attrs:{model:e.formData,rules:e.rules,size:"medium","label-width":"100px"}},[a("el-col",{attrs:{span:24}},[a("el-form-item",{attrs:{label:"生成类型",prop:"type"}},[a("el-radio-group",{model:{value:e.formData.type,callback:function(t){e.$set(e.formData,"type",t)},expression:"formData.type"}},e._l(e.typeOptions,(function(t,l){return a("el-radio-button",{key:l,attrs:{label:t.value,disabled:t.disabled}},[e._v(" "+e._s(t.label)+" ")])})),1)],1),e.showFileName?a("el-form-item",{attrs:{label:"文件名",prop:"fileName"}},[a("el-input",{attrs:{placeholder:"请输入文件名",clearable:""},model:{value:e.formData.fileName,callback:function(t){e.$set(e.formData,"fileName",t)},expression:"formData.fileName"}})],1):e._e()],1)],1)],1),a("div",{attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.close}},[e._v(" 取消 ")]),a("el-button",{attrs:{type:"primary"},on:{click:e.handleConfirm}},[e._v(" 确定 ")])],1)],1)],1)},o=[],r=a("46fc"),i={inheritAttrs:!1,props:["showFileName"],data:function(){return{formData:{fileName:void 0,type:"file"},rules:{fileName:[{required:!0,message:"请输入文件名",trigger:"blur"}],type:[{required:!0,message:"生成类型不能为空",trigger:"change"}]},typeOptions:[{label:"页面",value:"file"},{label:"弹窗",value:"dialog"}]}},computed:{},watch:{},mounted:function(){},methods:{onOpen:function(){this.showFileName&&(this.formData.fileName="".concat(+new Date,".vue"))},onClose:function(){},close:function(e){this.$emit("update:visible",!1)},handleConfirm:function(){var e=this;this.$refs.elForm.validate((function(t){t&&(e.$emit("confirm",Object(r["a"])({},e.formData)),e.close())}))}}},n=i,s=a("e607"),m=Object(s["a"])(n,l,o,!1,null,null,null);t["default"]=m.exports}}]);