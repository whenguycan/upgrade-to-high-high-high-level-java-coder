(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-452fd15c","chunk-bdb25ce8","chunk-03180c34","chunk-263c3fbd"],{2734:function(e,t,a){"use strict";a("9ee3")},"2b69":function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"deviceList"},[a("el-row",{staticClass:"mb8",attrs:{gutter:10}},[a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1)],1),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.postList}},[a("el-table-column",{attrs:{label:"序号",align:"center",type:"index",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.$index+(e.queryParams.pageNum-1)*e.queryParams.pageSize+1))])]}}])}),a("el-table-column",{attrs:{label:"设备编号",align:"center",prop:"deviceId"}}),a("el-table-column",{attrs:{label:"设备类型",align:"center",prop:"deviceType"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(e.showDictLabel(e.dict.type.device_type,t.row.deviceType)))])]}}])}),a("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:post:edit"],expression:"['system:post:edit']"}],attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleUpdate(t.row)}}},[e._v("修改")]),a("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:post:remove"],expression:"['system:post:remove']"}],staticStyle:{color:"red"},attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),a("el-dialog",{attrs:{title:e.title,visible:e.open,width:"500px","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[a("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"80px"}},[a("el-form-item",{attrs:{label:"设备编号",prop:"deviceId"}},[a("el-input",{attrs:{placeholder:"请输入设备编号"},model:{value:e.form.deviceId,callback:function(t){e.$set(e.form,"deviceId",t)},expression:"form.deviceId"}})],1),a("el-form-item",{attrs:{label:"设备类型",prop:"deviceType"}},[a("el-select",{attrs:{placeholder:"请选择设备类型",clearable:""},model:{value:e.form.deviceType,callback:function(t){e.$set(e.form,"deviceType",t)},expression:"form.deviceType"}},e._l(e.dict.type.device_type,(function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),a("el-form-item",{attrs:{label:"IP地址",prop:"serverIp"}},[a("el-input",{attrs:{placeholder:"请输入IP地址"},model:{value:e.form.serverIp,callback:function(t){e.$set(e.form,"serverIp",t)},expression:"form.serverIp"}})],1),a("el-form-item",{attrs:{label:"备注",prop:"remark"}},[a("el-input",{attrs:{type:"textarea",placeholder:"请输入内容"},model:{value:e.form.remark,callback:function(t){e.$set(e.form,"remark",t)},expression:"form.remark"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:e.submitForm}},[e._v("确 定")]),a("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1)},i=[],s=(a("2fcd"),a("f721"),a("31d4")),o=a("ee56"),l={name:"Post",dicts:["field_status","device_type"],components:{pageTitle:o["default"]},data:function(){return{loading:!0,postList:[],title:"",open:!1,queryParams:{pageNum:1,pageSize:99999,parkNo:200},form:{},rules:{deviceId:[{required:!0,message:"设备编号不能为空",trigger:"blur"}],deviceType:[{required:!0,message:"请选择设备类型",trigger:"blur"}]}}},created:function(){this.getList()},methods:{showDictLabel:function(e,t){var a;return(null===(a=e.find((function(e){return e.value==t})))||void 0===a?void 0:a.label)||"--"},getList:function(){var e=this;this.loading=!0,Object(s["d"])(this.queryParams).then((function(t){e.postList=t.rows,e.loading=!1}))},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={deviceType:void 0,spaceCount:void 0,remark:void 0},this.resetForm("form")},handleAdd:function(){this.reset(),this.open=!0,this.title="添加设备"},handleUpdate:function(e){var t=this;this.reset(),Object(s["c"])(e.id).then((function(e){t.form=e.data,t.open=!0,t.title="修改设备"}))},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){t&&(void 0!=e.form.id?Object(s["e"])(e.form).then((function(t){e.$modal.msgSuccess("修改成功"),e.open=!1,e.getList()})):(e.form.parkNo=e.queryParams.parkNo,Object(s["a"])(e.form).then((function(t){e.$modal.msgSuccess("新增成功"),e.open=!1,e.getList()}))))}))},handleDelete:function(e){var t=this;this.$modal.confirm('是否确认删除设备名称为"'+e.fieldName+'"的数据项？').then((function(){return Object(s["b"])({id:e.id})})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")})).catch((function(){}))}}},r=l,c=(a("a654"),a("e607")),d=Object(c["a"])(r,n,i,!1,null,"17980cce",null);t["default"]=d.exports},"31ab":function(e,t,a){},"31d4":function(e,t,a){"use strict";a.d(t,"d",(function(){return i})),a.d(t,"c",(function(){return s})),a.d(t,"a",(function(){return o})),a.d(t,"e",(function(){return l})),a.d(t,"b",(function(){return r}));var n=a("b775");function i(e){return Object(n["a"])({url:"/parking/device/list",method:"get",params:e})}function s(e){return Object(n["a"])({url:"/parking/device/"+e,method:"get"})}function o(e){return Object(n["a"])({url:"/parking/device/add",method:"post",data:e})}function l(e){return Object(n["a"])({url:"/parking/device/edit",method:"put",data:e})}function r(e){return Object(n["a"])({url:"/parking/device/"+e,method:"delete"})}},3238:function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"passageway"},[a("div",{staticClass:"main-list"},[a("page-title",{attrs:{title:"通道列表"}},[a("div",[a("el-row",{staticClass:"mb8",attrs:{gutter:10}},[a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.opneQr}},[e._v("打印二维码")])],1)],1)],1)]),a("div",{staticStyle:{padding:"0 20px"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"singleTable",attrs:{data:e.postList,"highlight-current-row":""},on:{"current-change":e.handleCurrentChange}},[a("el-table-column",{attrs:{label:"序号",align:"center",type:"index",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.$index+(e.queryParams.pageNum-1)*e.queryParams.pageSize+1))])]}}])}),a("el-table-column",{attrs:{label:"通道名称",align:"center",prop:"passageName"}}),a("el-table-column",{attrs:{label:"通行方向",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.fromFieldName))]),e._v(" --\x3e "),a("span",[e._v(e._s(t.row.toFieldName))])]}}])}),a("el-table-column",{attrs:{label:"开闸方式",align:"center",prop:"status"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(e.showDictLabel(e.dict.type.open_type,t.row.openType)))])]}}])}),a("el-table-column",{attrs:{label:"状态",align:"center",prop:"passageStatus"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("div",{staticClass:"statusI"},[a("span",{class:1===+t.row.passageStatus?"statusIcon":"statusGreyIcon"}),a("span",{staticClass:"statusName"},[e._v(e._s(1===+t.row.passageStatus?"已启用":"已停用"))])])]}}])}),a("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleUpdate(t.row)}}},[e._v("修改")]),a("el-button",{staticStyle:{color:"red"},attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleDelete(t.row)}}},[e._v("删除")]),a("el-button",{staticStyle:{color:"green"},attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handelOpenQR(t.row)}}},[e._v("二维码")])]}}])})],1),a("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total > 0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.getList}})],1),a("el-dialog",{attrs:{title:e.title,visible:e.open,width:"700px","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[a("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"130px"}},[a("el-form-item",{attrs:{label:"通道名称",prop:"passageName"}},[a("el-input",{attrs:{placeholder:"请输入通道名称"},model:{value:e.form.passageName,callback:function(t){e.$set(e.form,"passageName",t)},expression:"form.passageName"}})],1),a("el-form-item",{attrs:{label:"通道编号",prop:"passageNo"}},[a("el-input",{attrs:{placeholder:"请输入通道编号"},model:{value:e.form.passageNo,callback:function(t){e.$set(e.form,"passageNo",t)},expression:"form.passageNo"}})],1),a("el-form-item",{attrs:{label:"开闸方式",prop:"openType"}},[a("el-select",{attrs:{placeholder:"开闸方式",clearable:""},model:{value:e.form.openType,callback:function(t){e.$set(e.form,"openType",t)},expression:"form.openType"}},e._l(e.dict.type.open_type,(function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),a("el-form-item",{attrs:{label:"通行方向",prop:"fromFieldId"}},[a("el-select",{attrs:{placeholder:"开闸方式",clearable:""},model:{value:e.form.fromFieldId,callback:function(t){e.$set(e.form,"fromFieldId",t)},expression:"form.fromFieldId"}},e._l(e.comboboxList,(function(e){return a("el-option",{key:e.id,attrs:{label:e.text,value:e.id}})})),1),a("span",[e._v(" 至 ")]),a("el-select",{attrs:{placeholder:"开闸方式",clearable:""},model:{value:e.form.toFieldId,callback:function(t){e.$set(e.form,"toFieldId",t)},expression:"form.toFieldId"}},e._l(e.comboboxList,(function(e){return a("el-option",{key:e.id,attrs:{label:e.text,value:e.id}})})),1)],1),a("el-form-item",{attrs:{label:"状态",prop:"passageStatus"}},[a("el-radio-group",{model:{value:e.form.passageStatus,callback:function(t){e.$set(e.form,"passageStatus",t)},expression:"form.passageStatus"}},e._l(e.dict.type.field_status,(function(t){return a("el-radio",{key:t.value,attrs:{label:t.value}},[e._v(e._s(t.label))])})),1)],1),a("el-form-item",{attrs:{label:"通道标识",prop:"passageFlag"}},[a("el-radio-group",{model:{value:e.form.passageFlag,callback:function(t){e.$set(e.form,"passageFlag",t)},expression:"form.passageFlag"}},[a("el-radio",{key:"1",attrs:{label:"1"}},[e._v(" 入口 ")]),a("el-radio",{key:"2",attrs:{label:"2"}},[e._v(" 出口 ")])],1)],1),a("el-form-item",{attrs:{label:"绑定固定车类型：",prop:"bandRegularCodes"}},[a("el-select",{staticStyle:{width:"530px"},attrs:{multiple:"",placeholder:"请选择",clearable:""},model:{value:e.bandRegularCodes,callback:function(t){e.bandRegularCodes=t},expression:"bandRegularCodes"}},e._l(e.options,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:e.submitForm}},[e._v("确 定")]),a("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1),a("div",{staticClass:"fix-list"},[a("page-title",{attrs:{title:"通道列表"}}),a("div",{staticClass:"tabBox"},[a("el-tabs",{attrs:{type:"card"},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[a("el-tab-pane",{attrs:{label:"通道设备绑定",name:"1"}},[a("div",{staticClass:"tabSkew",attrs:{slot:"label"},slot:"label"},[a("div",{staticClass:"tabFirstImg"}),a("div",{staticClass:"tabName"},[e._v("通道设备绑定")]),2!=+e.activeName?a("div",{staticClass:"tabRImg"}):e._e()]),a("bindDevice",{attrs:{passageId:e.passageId}})],1),a("el-tab-pane",{attrs:{label:"设备明细",name:"2"}},[a("div",{staticClass:"tabSkew",attrs:{slot:"label"},slot:"label"},[a("div",{staticClass:"tabLeftImg"}),a("div",{staticClass:"tabName"},[e._v("设备明细")]),a("div",{staticClass:"tabLastImg"})]),a("deviceList")],1)],1)],1)],1),a("el-dialog",{attrs:{title:"二维码",visible:e.openQr,"append-to-body":""},on:{"update:visible":function(t){e.openQr=t}}},[a("div",{staticClass:"codeContent"},[a("div",{staticClass:"codeContentC"},[a("canvas",{attrs:{id:"qrCodeUrl"}})])]),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:e.downloadCodeIg}},[e._v("保 存")]),a("el-button",{on:{click:function(t){e.openQr=!1}}},[e._v("取 消")])],1)])],1)},i=[],s=a("3600"),o=a("1c61"),l=(a("2fcd"),a("f721"),a("4939"),a("2573"),a("4b47"),a("7500"),a("0ab6"),a("66d7"),a("0096")),r=a("4233"),c=a("ee56"),d=a("2b69"),u=a("78c5"),p=a("0b59"),f=a.n(p),m={name:"Post",dicts:["field_status","open_type"],components:{pageTitle:c["default"],deviceList:d["default"],bindDevice:u["default"]},data:function(){return{qrData:{name:""},openQr:!1,loading:!0,total:0,postList:[],title:"",open:!1,queryParams:{pageNum:1,pageSize:10,parkNo:200},form:{},rules:{passageName:[{required:!0,message:"标题不能为空",trigger:"blur"}],openType:[{required:!0,message:"请选择开闸方式",trigger:"blur"}],passageNo:[{required:!0,message:"通道编号不能为空",trigger:"blur"}]},comboboxList:[],passageId:"",activeName:"1",bandRegularCodes:[],options:[]}},created:function(){this.getList(),this.getComboboxList(),this.getRegularCarCategorys()},methods:{handleCurrentChange:function(e){console.log(e),this.passageId=e.id},showDictLabel:function(e,t){var a;return(null===(a=e.find((function(e){return e.value==t})))||void 0===a?void 0:a.label)||"--"},getList:function(){var e=this;this.loading=!0,Object(r["e"])(this.queryParams).then((function(t){e.postList=t.rows,e.total=t.total,e.loading=!1}))},getComboboxList:function(){var e=this;this.loading=!0,Object(l["c"])().then((function(t){e.comboboxList=t}))},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={passageName:void 0,spaceCount:void 0,passageStatus:"1",passageFlag:"1",bandRegularCodes:""},this.bandRegularCodes=[],this.resetForm("form")},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleAdd:function(){this.reset(),this.open=!0,this.title="添加通道"},handleUpdate:function(e){var t=this;this.reset(),Object(r["c"])(e.id).then((function(e){if(e.fromFieldId=e.fromFieldId+"",e.toFieldId=e.toFieldId+"",t.form=e,null!=e.bandRegularCodes){t.bandRegularCodes=e.bandRegularCodes.split(",");var a=[];t.bandRegularCodes.forEach((function(e,t){a.push(Number(e))})),t.bandRegularCodes=[],t.bandRegularCodes=a}t.open=!0,t.title="修改通道"}))},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){if(t){if("Vip"==e.form.openType&&0==e.bandRegularCodes.length)return void e.$modal.alertWarning("开闸方式为固定车自动开闸时，请绑定固定车类型！");e.form.bandRegularCodes=e.bandRegularCodes.join(","),void 0!=e.form.id?Object(r["f"])(e.form).then((function(t){e.$modal.msgSuccess("修改成功"),e.open=!1,e.getList()})):(e.form.parkNo=e.queryParams.parkNo,Object(r["a"])(e.form).then((function(t){e.$modal.msgSuccess("新增成功"),e.open=!1,e.getList()})))}}))},handleDelete:function(e){var t=this;this.$modal.confirm('是否确认删除通道名称为"'+e.passageName+'"的数据项？').then((function(){return Object(r["b"])({id:e.id})})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")})).catch((function(){}))},getRegularCarCategorys:function(){var e=this;Object(r["d"])().then((function(t){e.options=t.rows}))},downloadCodeIg:function(){var e=document.getElementById("qrCodeUrl"),t=e.toDataURL("image/png"),a=t,n=document.createElement("a"),i=new MouseEvent("click");n.download=this.qrData.name,n.href=a,n.dispatchEvent(i)},createQcodeIMG:function(e){var t=arguments;return Object(o["a"])(Object(s["a"])().mark((function a(){var n,i,o,l,r,c,d,u,p,m,b,g,v,h;return Object(s["a"])().wrap((function(a){while(1)switch(a.prev=a.next){case 0:return n=t.length>1&&void 0!==t[1]?t[1]:280,i=t.length>2&&void 0!==t[2]?t[2]:"二维码",o=t.length>3&&void 0!==t[3]?t[3]:"#000",a.next=5,f.a.toCanvas(document.getElementById("qrCodeUrl"),e,{errorCorrectionLevel:"H",height:n,width:n,color:{dark:o},errorCorrectLevel:"L",rendererOpts:{quality:.8}});case 5:return l=a.sent,r="bold",c=14,d=5,u=n+c+2*d,p=l.getContext("2d"),m=p.getImageData(0,0,n,n),p.fillStyle="#fff",l.setAttribute("height",u),l.style.setProperty("height",u+"px"),p.fillRect(0,0,n,u),p.putImageData(m,0,0),p.font="".concat(r," ").concat(c,"px Arial"),b=p.measureText(i).width,g=n,v=(n-b)/2,h=c/2,p.fillStyle="#fff",p.fillRect(0,n,n,u-2*d),p.fillRect(v-h/2,g-h/2,b+h,c+h),p.textBaseline="top",p.fillStyle="#333",p.fillText(i,v,g),a.abrupt("return",l);case 29:case"end":return a.stop()}}),a)})))()},initRQcode:function(e,t){var a=this;return Object(o["a"])(Object(s["a"])().mark((function n(){var i;return Object(s["a"])().wrap((function(n){while(1)switch(n.prev=n.next){case 0:return i="http://192.168.2.172:81/appointment/index/0?code=".concat(e),n.next=3,a.createQcodeIMG(i,280,t,"#000");case 3:case"end":return n.stop()}}),n)})))()},handelOpenQR:function(e){var t=this;this.openQr=!0,this.qrData.name=e.passageName,this.$nextTick((function(){t.initRQcode(e.passageNo,t.qrData.name)}))},opneQr:function(){var e=this;this.openQr=!0,this.qrData.name="场库二维码",this.$nextTick((function(){e.initRQcode("",e.qrData.name)}))}}},b=m,g=(a("2734"),a("e607")),v=Object(g["a"])(b,n,i,!1,null,"21293467",null);t["default"]=v.exports},"32add":function(e,t,a){"use strict";a("3523")},3523:function(e,t,a){},"3a085":function(e,t,a){},"48a7":function(e,t,a){"use strict";a("3a085")},"78c5":function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"bindDevice"},[a("div",{staticClass:"tab-title"},[e._v("已选设备")]),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.selectList}},[a("el-table-column",{attrs:{label:"序号",align:"center",type:"index",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.$index+1))])]}}])}),a("el-table-column",{attrs:{label:"设备编号",align:"center",prop:"deviceId"}}),a("el-table-column",{attrs:{label:"设备类型",align:"center",prop:"deviceType"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(e.showDictLabel(e.dict.type.device_type,t.row.deviceType)))])]}}])}),a("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{staticStyle:{color:"red"},attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleUpdate(t.row,"0")}}},[e._v("解绑")])]}}])})],1),a("div",{staticClass:"tab-title"},[e._v("待选设备")]),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading2,expression:"loading2"}],attrs:{data:e.unselectList}},[a("el-table-column",{attrs:{label:"序号",align:"center",type:"index",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.$index+1))])]}}])}),a("el-table-column",{attrs:{label:"设备编号",align:"center",prop:"deviceId"}}),a("el-table-column",{attrs:{label:"设备类型",align:"center",prop:"deviceType"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(e.showDictLabel(e.dict.type.device_type,t.row.deviceType)))])]}}])}),a("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{staticStyle:{color:"green"},attrs:{size:"mini",type:"text"},on:{click:function(a){return e.handleUpdate(t.row,"1")}}},[e._v("绑定")])]}}])})],1)],1)},i=[],s=a("559a"),o=a("46fc"),l=(a("4b47"),a("2fcd"),a("f721"),a("31d4")),r=a("ee56"),c={name:"Post",dicts:["field_status","device_type"],components:{pageTitle:r["default"]},props:{passageId:{type:[String,Number],required:!0}},watch:{passageId:{handler:function(e){if(""==e)return console.log("未选"),!1;this.passage=e,console.log("绑定列表"),this.getList(e)},immediate:!0}},data:function(){return{passage:"",loading:!1,loading2:!1,queryParams:{pageNum:1,pageSize:99999,parkNo:200},selectList:[],unselectList:[]}},created:function(){},methods:{handleUpdate:function(e,t){var a=this,n="1"==t?"绑定":"解绑";this.$modal.confirm("是否确认".concat(n,"改设备？")).then((function(){Object(l["e"])({deviceStatus:t,passageId:a.passageId,deviceId:e.deviceId,id:e.id}).then((function(){a.getList(a.passage),a.$modal.msgSuccess("".concat(n,"成功"))}))})).catch((function(){}))},showDictLabel:function(e,t){var a;return(null===(a=e.find((function(e){return e.value==t})))||void 0===a?void 0:a.label)||"--"},getList:function(e){var t=this;this.loading=!0,this.loading2=!0,Object(l["d"])(Object(o["a"])(Object(o["a"])({},this.queryParams),{},{passageId:e,deviceStatus:"1"})).then((function(e){t.selectList=e.rows,t.loading=!1})),Object(l["d"])(Object(o["a"])(Object(o["a"])({},this.queryParams),{},{deviceStatus:"0"})).then((function(e){t.unselectList=e.rows,t.loading2=!1}))},reset:function(){var e;this.form=(e={fieldName:void 0,spaceCount:void 0},Object(s["a"])(e,"fieldName",void 0),Object(s["a"])(e,"fieldStatus","1"),e),this.resetForm("form")},handleDelete:function(e){var t=this;this.$modal.confirm('是否确认删除设备名称为"'+e.fieldName+'"的数据项？').then((function(){return Object(l["b"])({id:e.id})})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")})).catch((function(){}))}}},d=c,u=(a("32add"),a("e607")),p=Object(u["a"])(d,n,i,!1,null,"24a3340c",null);t["default"]=p.exports},"9ee3":function(e,t,a){},a654:function(e,t,a){"use strict";a("31ab")},ee56:function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"pageTitle"},[a("div",{staticClass:"left-box"},[a("div",{staticClass:"line"}),a("div",{staticClass:"title"},[a("span",[e._v(e._s(e.title))])])]),a("div",{staticClass:"right-box"},[e._t("default")],2)])},i=[],s={components:{},props:{title:{type:String,default:""}},data:function(){return{}},computed:{},watch:{},methods:{},created:function(){},mounted:function(){}},o=s,l=(a("48a7"),a("e607")),r=Object(l["a"])(o,n,i,!1,null,"05add394",null);t["default"]=r.exports}}]);