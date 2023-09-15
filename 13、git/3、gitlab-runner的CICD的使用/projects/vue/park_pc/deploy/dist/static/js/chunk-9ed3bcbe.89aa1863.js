(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-9ed3bcbe","chunk-7491e9c6","chunk-2d0d3e62"],{"423e":function(e,t,r){"use strict";r("5718")},5718:function(e,t,r){},"5f72":function(e,t,r){"use strict";r.r(t),r.d(t,"getOrderList",(function(){return s})),r.d(t,"deleteOrder",(function(){return i})),r.d(t,"settleOrder",(function(){return n}));var a=r("b775");function s(e){return Object(a["a"])({url:"/parking/parkliverecords/list",method:"get",params:e})}function i(e){return Object(a["a"])({url:"/parking/parkliverecords/remove/"+e,method:"get"})}function n(e){return Object(a["a"])({url:"/parking/parkliverecords/manual",method:"post",data:e})}},"7a62":function(e,t,r){},"7e9f":function(e,t,r){},"87ca":function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"orderList"},[r("div",{staticClass:"orderBox",staticStyle:{width:"70%"}},[r("titleTab",{attrs:{title:"查询条件"}}),r("el-form",{ref:"queryForm",staticClass:"searchBox",attrs:{model:e.queryForm,size:"small","label-width":"160px"}},[r("el-form-item",{attrs:{label:"车辆类型：",prop:"carType"}},[r("el-select",{staticStyle:{width:"220px"},attrs:{placeholder:"请选择车辆类型",clearable:""},model:{value:e.queryForm.carType,callback:function(t){e.$set(e.queryForm,"carType",t)},expression:"queryForm.carType"}},e._l(e.carList,(function(e){return r("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),r("el-form-item",{attrs:{label:"车牌号：",prop:"carNumber"}},[r("el-input",{staticStyle:{width:"220px"},attrs:{placeholder:"请输入车牌号",clearable:""},model:{value:e.queryForm.carNumber,callback:function(t){e.$set(e.queryForm,"carNumber",t)},expression:"queryForm.carNumber"}})],1),r("el-form-item",{attrs:{label:"入场时间：",prop:"entryTime"}},[r("el-date-picker",{attrs:{type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:"选择入场时间"},model:{value:e.queryForm.entryTime,callback:function(t){e.$set(e.queryForm,"entryTime",t)},expression:"queryForm.entryTime"}})],1),r("el-form-item",{staticStyle:{"margin-left":"30px"}},[r("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("查询")]),r("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),r("titleTab",{attrs:{title:"订单记录"}}),r("div",{staticClass:"importDiv"},[r("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.handleExport}},[e._v("导出所有")]),r("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.settleSomeOrder}},[e._v("批量结算")])],1),r("div",{staticClass:"tableBox"},[r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"table",attrs:{data:e.tableData,"row-class-name":e.tableRowClassName},on:{"row-click":e.getPhoto,"selection-change":e.handleSelectionChange}},[r("el-table-column",{attrs:{type:"selection",width:"55"}}),r("el-table-column",{attrs:{label:"序号",align:"center",type:"index",width:"50"}}),r("el-table-column",{attrs:{label:"车牌","min-width":"180",align:"center",prop:"carNumber"}}),r("el-table-column",{attrs:{label:"车类型","min-width":"160",align:"center",prop:"carTypeName"}}),r("el-table-column",{attrs:{label:"入场时间","min-width":"260",align:"center",prop:"entryTime"}}),r("el-table-column",{attrs:{label:"备注","min-width":"160",align:"center",prop:"remark"}}),r("el-table-column",{attrs:{width:"120",fixed:"right",label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(r){return e.settleOrder(t.row)}}},[e._v("结算")]),r("el-button",{staticClass:"colorHollowBtnRed",attrs:{size:"mini",type:"text"},on:{click:function(r){return e.deleteOrder(t.row)}}},[e._v("删除")])]}}])})],1),r("pagination",{directives:[{name:"show",rawName:"v-show",value:e.pageInfo.total>0,expression:"pageInfo.total > 0"}],attrs:{total:e.pageInfo.total,page:e.pageInfo.pageNum,limit:e.pageInfo.pageSize},on:{"update:page":function(t){return e.$set(e.pageInfo,"pageNum",t)},"update:limit":function(t){return e.$set(e.pageInfo,"pageSize",t)},pagination:e.getList}})],1)],1),r("div",{staticClass:"orderBox",staticStyle:{width:"30%","margin-left":"20px"}},[r("titleTab",{attrs:{title:"图片显示"}}),r("div",{staticClass:"photoBox"},[r("div",{staticClass:"carLicence"},[e._m(0),r("div",{staticClass:"photoImg"},[e.carImgFrom?r("img",{attrs:{src:e.carImgFrom,alt:""}}):e._e()])]),r("div",{staticClass:"carImg"},[e._m(1),r("div",{staticClass:"photoImg"},[e.carImgTo?r("img",{attrs:{src:e.carImgTo,alt:""}}):e._e()])])])],1),r("el-dialog",{staticClass:"settleDialog",attrs:{title:"结算订单",visible:e.settleShow,width:"683px","append-to-body":""},on:{"update:visible":function(t){e.settleShow=t},close:e.closeSettle}},[r("div",{staticClass:"settleCon"},[r("div",{staticClass:"oerderNumberList"},[r("span",{staticClass:"orderNumberTit"},[e._v("结算车牌号：")]),r("div",{staticClass:"orderNumberCon"},e._l(e.settleOrderList,(function(t,a){return r("span",{key:a,staticClass:"orderNumberIn"},[e._v(e._s(a==e.settleOrderList.length-1?t.carNumber:t.carNumber+","))])})),0)]),r("el-form",{ref:"settleForm",staticClass:"settleBox",attrs:{rules:e.settleRules,model:e.settleForm,size:"small","label-width":"120px"}},[r("el-form-item",{attrs:{label:"结算金额(元)：",prop:"carPrice"}},[r("el-input",{staticStyle:{width:"220px"},attrs:{placeholder:"请输入结算金额",clearable:""},model:{value:e.settleForm.carPrice,callback:function(t){e.$set(e.settleForm,"carPrice",e._n(t))},expression:"settleForm.carPrice"}})],1),r("el-form-item",{attrs:{label:"结算原因：",prop:"reason"}},[r("el-select",{staticStyle:{width:"220px"},attrs:{placeholder:"请选择结算原因",clearable:""},model:{value:e.settleForm.reason,callback:function(t){e.$set(e.settleForm,"reason",t)},expression:"settleForm.reason"}},e._l(e.reasonList,(function(e){return r("el-option",{key:e.id,attrs:{label:e.reason,value:e.reason}})})),1)],1),r("el-form-item",[r("el-button",{staticClass:"confirmButton",attrs:{type:"primary",size:"mini"},on:{click:e.settleConfirm}},[e._v("确定")]),r("el-button",{staticClass:"cancleButton",attrs:{size:"mini"},on:{click:e.closeSettle}},[e._v("取消")])],1)],1)],1)])],1)},s=[function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"photoTit"},[r("span",[e._v("车辆进场照")])])},function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"photoTit"},[r("span",[e._v("车辆出场照")])])}],i=r("46fc"),n=r("3600"),o=r("1c61"),l=(r("2573"),r("f721"),r("4939"),r("9dc0")),c=(r("5f87"),r("5f72")),u=r("5c4e"),m={name:"orderList",components:{titleTab:l["default"]},dicts:["pay_method"],props:{},data:function(){return{reasonList:[],labelPosition:"right",queryForm:{entryTime:"",carType:"",carNumber:""},tableData:[],pageInfo:{total:5,pageSize:10,pageNum:1},carList:this.$store.getters.carList,aisleList:this.$store.getters.carAisleList,areaList:this.$store.getters.carAreaList,loading:!1,carImgFrom:"",carImgTo:"",nowClickId:"",settleShow:!1,settleForm:{orderList:[],carPrice:"",reason:""},settleOrderList:[],settleRules:{carPrice:[{required:!0,message:"请输入结算金额",trigger:"blur"}],reason:[{required:!0,message:"请选择结算原因",trigger:"change"}]}}},computed:{},watch:{},methods:{getReasonList:function(){var e=this;return Object(o["a"])(Object(n["a"])().mark((function t(){var r;return Object(n["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,Object(u["getSettingliftgatereasonList"])();case 2:r=t.sent,200==r.code&&(e.reasonList=r.rows);case 4:case"end":return t.stop()}}),t)})))()},getList:function(){var e=this;return Object(o["a"])(Object(n["a"])().mark((function t(){var r,a;return Object(n["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e.loading=!0,r=Object(i["a"])({pageSize:e.pageInfo.pageSize,pageNum:e.pageInfo.pageNum},e.queryForm),t.next=4,Object(c["getOrderList"])(r);case 4:a=t.sent,200==a.code&&(e.loading=!1,e.tableData=a.rows,e.pageInfo.total=a.total);case 6:case"end":return t.stop()}}),t)})))()},getPhoto:function(e,t,r){this.nowClickId=e.id,this.carImgFrom=e.carImgUrlFrom,this.carImgTo=e.carImgUrlTo},tableRowClassName:function(e){var t=e.row;e.rowIndex;return t.id===this.nowClickId?"highlight-row":""},handleQuery:function(){this.pageInfo.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleExport:function(){this.download("parking/parksettlementrecords/export",Object(i["a"])({},this.queryForm),"order_".concat((new Date).getTime(),".xlsx"))},deleteOrder:function(e){var t=this;this.$confirm("确定删除这条订单记录？","删除提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){Object(c["deleteOrder"])(e.id).then((function(e){200===e.code&&(t.$message.success("删除订单成功！"),t.getList())})).catch((function(e){t.$message.error(e)}))})).catch((function(){}))},settleOrder:function(e){this.resetSettleForm(),this.settleOrderList.push(e),this.settleForm.orderList.push(e.id),this.settleShow=!0},settleConfirm:function(){var e=this;return Object(o["a"])(Object(n["a"])().mark((function t(){var r,a;return Object(n["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return r={liveIdList:e.settleForm.orderList,payAmount:e.settleForm.carPrice,liftGateReason:e.settleForm.reason},t.next=3,Object(c["settleOrder"])(r);case 3:a=t.sent,200==a.code?(e.$message.success("结算订单成功！"),e.settleShow=!1,e.getList()):e.$message.error("结算订单失败！");case 5:case"end":return t.stop()}}),t)})))()},closeSettle:function(){this.settleShow=!1,this.resetSettleForm()},resetSettleForm:function(){this.$refs.table.clearSelection(),this.settleOrderList=[],this.settleForm={orderList:[],carPrice:"",reason:""}},handleSelectionChange:function(e){var t=this;this.settleOrderList=e,e.forEach((function(e){t.settleForm.orderList.push(e.id)}))},settleSomeOrder:function(){this.settleOrderList.length>0?this.settleShow=!0:this.$message.success("请先选择需要结算订单！")}},created:function(){},mounted:function(){this.getReasonList(),this.getList()}},d=m,p=(r("423e"),r("ba70"),r("e607")),f=Object(p["a"])(d,a,s,!1,null,"649b88a8",null);t["default"]=f.exports},"9dc0":function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"title"},[r("span",{staticClass:"leftSpan"}),r("span",{staticClass:"rightSpan"},[e._v(e._s(e.title))])])},s=[],i={components:{},props:{title:{type:String,default:""}},data:function(){return{}},computed:{},watch:{},methods:{},created:function(){},mounted:function(){}},n=i,o=(r("f2af"),r("e607")),l=Object(o["a"])(n,a,s,!1,null,"4b61fe51",null);t["default"]=l.exports},ba70:function(e,t,r){"use strict";r("7e9f")},f2af:function(e,t,r){"use strict";r("7a62")}}]);