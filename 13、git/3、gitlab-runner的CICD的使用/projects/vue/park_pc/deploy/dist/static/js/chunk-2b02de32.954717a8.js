(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2b02de32"],{5911:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("el-row",[a("el-col",{staticClass:"card-box",attrs:{span:24}},[a("el-card",[a("div",{attrs:{slot:"header"},slot:"header"},[a("span",[e._v("基本信息")])]),a("div",{staticClass:"el-table el-table--enable-row-hover el-table--medium"},[a("table",{staticStyle:{width:"100%"},attrs:{cellspacing:"0"}},[a("tbody",[a("tr",[a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("Redis版本")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.redis_version))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("运行模式")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s("standalone"==e.cache.info.redis_mode?"单机":"集群"))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("端口")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.tcp_port))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("客户端数")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.connected_clients))]):e._e()])]),a("tr",[a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("运行时间(天)")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.uptime_in_days))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("使用内存")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.used_memory_human))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("使用CPU")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(parseFloat(e.cache.info.used_cpu_user_children).toFixed(2)))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("内存配置")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.maxmemory_human))]):e._e()])]),a("tr",[a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("AOF是否开启")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s("0"==e.cache.info.aof_enabled?"否":"是"))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("RDB是否成功")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.rdb_last_bgsave_status))]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("Key数量")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.dbSize?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.dbSize)+" ")]):e._e()]),a("td",{staticClass:"el-table__cell is-leaf"},[a("div",{staticClass:"cell"},[e._v("网络入口/出口")])]),a("td",{staticClass:"el-table__cell is-leaf"},[e.cache.info?a("div",{staticClass:"cell"},[e._v(e._s(e.cache.info.instantaneous_input_kbps)+"kps/"+e._s(e.cache.info.instantaneous_output_kbps)+"kps")]):e._e()])])])])])])],1),a("el-col",{staticClass:"card-box",attrs:{span:12}},[a("el-card",[a("div",{attrs:{slot:"header"},slot:"header"},[a("span",[e._v("命令统计")])]),a("div",{staticClass:"el-table el-table--enable-row-hover el-table--medium"},[a("div",{ref:"commandstats",staticStyle:{height:"420px"}})])])],1),a("el-col",{staticClass:"card-box",attrs:{span:12}},[a("el-card",[a("div",{attrs:{slot:"header"},slot:"header"},[a("span",[e._v("内存信息")])]),a("div",{staticClass:"el-table el-table--enable-row-hover el-table--medium"},[a("div",{ref:"usedmemory",staticStyle:{height:"420px"}})])])],1)],1)],1)},s=[],c=a("ceee"),i=a("2228"),n={name:"Cache",data:function(){return{commandstats:null,usedmemory:null,cache:[]}},created:function(){this.getList(),this.openLoading()},methods:{getList:function(){var e=this;Object(c["d"])().then((function(t){e.cache=t.data,e.$modal.closeLoading(),e.commandstats=i["init"](e.$refs.commandstats,"macarons"),e.commandstats.setOption({tooltip:{trigger:"item",formatter:"{a} <br/>{b} : {c} ({d}%)"},series:[{name:"命令",type:"pie",roseType:"radius",radius:[15,95],center:["50%","38%"],data:t.data.commandStats,animationEasing:"cubicInOut",animationDuration:1e3}]}),e.usedmemory=i["init"](e.$refs.usedmemory,"macarons"),e.usedmemory.setOption({tooltip:{formatter:"{b} <br/>{a} : "+e.cache.info.used_memory_human},series:[{name:"峰值",type:"gauge",min:0,max:1e3,detail:{formatter:e.cache.info.used_memory_human},data:[{value:parseFloat(e.cache.info.used_memory_human),name:"内存消耗"}]}]})}))},openLoading:function(){this.$modal.loading("正在加载缓存监控数据，请稍候！")}}},o=n,d=a("e607"),r=Object(d["a"])(o,l,s,!1,null,null,null);t["default"]=r.exports},ceee:function(e,t,a){"use strict";a.d(t,"d",(function(){return s})),a.d(t,"g",(function(){return c})),a.d(t,"f",(function(){return i})),a.d(t,"e",(function(){return n})),a.d(t,"c",(function(){return o})),a.d(t,"b",(function(){return d})),a.d(t,"a",(function(){return r}));var l=a("b775");function s(){return Object(l["a"])({url:"/monitor/cache",method:"get"})}function c(){return Object(l["a"])({url:"/monitor/cache/getNames",method:"get"})}function i(e){return Object(l["a"])({url:"/monitor/cache/getKeys/"+e,method:"get"})}function n(e,t){return Object(l["a"])({url:"/monitor/cache/getValue/"+e+"/"+t,method:"get"})}function o(e){return Object(l["a"])({url:"/monitor/cache/clearCacheName/"+e,method:"delete"})}function d(e){return Object(l["a"])({url:"/monitor/cache/clearCacheKey/"+e,method:"delete"})}function r(){return Object(l["a"])({url:"/monitor/cache/clearCacheAll",method:"delete"})}}}]);