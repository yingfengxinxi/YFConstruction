(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-1e24"],{"9GgJ":function(t,e,s){"use strict";var n={name:"XrHeader",components:{},props:{iconClass:[String,Array],iconColor:String,label:String,showSearch:{type:Boolean,default:!1},searchIconType:{type:String,default:"text"},placeholder:{type:String,default:"请输入内容"},ftTop:{type:String,default:"15px"},content:[String,Number],inputAttr:{type:Object,default:function(){}}},data:function(){return{search:""}},computed:{},watch:{content:{handler:function(){this.search!=this.content&&(this.search=this.content)},immediate:!0}},mounted:function(){},beforeDestroy:function(){},methods:{inputChange:function(){this.$emit("update:content",this.search)},searchClick:function(){this.$emit("search",this.search)}}},i=(s("zIzm"),s("KHd+")),a=Object(i.a)(n,function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("flexbox",{staticClass:"xr-header"},[t.iconClass?s("div",{staticClass:"xr-header__icon",style:{backgroundColor:t.iconColor}},[s("i",{class:t.iconClass})]):t._e(),t._v(" "),s("div",{staticClass:"xr-header__label"},[t.$slots.label?t._t("label"):[t._v(t._s(t.label))]],2),t._v(" "),t.showSearch?s("el-input",t._b({staticClass:"xr-header__search",class:{"is-text":"text"===t.searchIconType},style:{"margin-top":t.ftTop},attrs:{placeholder:t.placeholder},on:{input:t.inputChange},nativeOn:{keyup:function(e){return"button"in e||!t._k(e.keyCode,"enter",13,e.key,"Enter")?t.searchClick(e):null}},model:{value:t.search,callback:function(e){t.search=e},expression:"search"}},"el-input",t.inputAttr,!1),["text"===t.searchIconType?s("el-button",{attrs:{slot:"append",type:"primary"},nativeOn:{click:function(e){return t.searchClick(e)}},slot:"append"},[t._v("搜索")]):s("el-button",{attrs:{slot:"append",icon:"el-icon-search"},nativeOn:{click:function(e){return t.searchClick(e)}},slot:"append"})],1):t._e(),t._v(" "),s("div",{staticClass:"xr-header__ft",style:{top:t.ftTop}},[t._t("ft")],2)],1)},[],!1,null,"7bba770c",null);a.options.__file="index.vue";e.a=a.exports},CDef:function(t,e,s){},GmXa:function(t,e,s){"use strict";var n=s("OOL8");s.n(n).a},IJOt:function(t,e,s){"use strict";s.r(e);var n=s("FyfS"),i=s.n(n),a=s("hSIE"),o=s("Flj7"),c=s("pa88"),l=s("pKm3"),r=s("9GgJ"),d=s("MQ60"),u=s.n(d),h=s("IKCp"),m=s("kK4T"),p=s("7Qib"),f={components:{draggable:u.a,ListTaskAdd:c.a,TaskDetail:l.a,XrHeader:r.a},directives:{scrollx:h.a},mixins:[m.a],data:function(){return{taskHandleShow:!1,taskList:[],loading:!0,taskID:"",detailIndex:-1,detailSection:-1,taskDetailShow:!1}},created:function(){this.getList()},mounted:function(){document.body.ondrop=function(t){t.preventDefault(),t.stopPropagation()}},methods:{getList:function(){var t=this;this.loading=!0,Object(a.d)().then(function(e){var s=!0,n=!1,a=void 0;try{for(var o,c=i()(e.data);!(s=(o=c.next()).done);s=!0){var l=o.value;l.checkedNum=0,l.showTaskAdd=!1;var r=!0,d=!1,u=void 0;try{for(var h,m=i()(l.list);!(r=(h=m.next()).done);r=!0){var p=h.value;5==p.status?(p.checked=!0,l.checkedNum+=1):p.checked=!1}}catch(t){d=!0,u=t}finally{try{!r&&m.return&&m.return()}finally{if(d)throw u}}}}catch(t){n=!0,a=t}finally{try{!s&&c.return&&c.return()}finally{if(n)throw a}}t.taskList=e.data,t.loading=!1}).catch(function(){t.loading=!1})},moveEndTask:function(t){if(document.dispatchEvent(new MouseEvent("mouseup")),t){var e=t.from.id,s=t.to.id;if(e==s&&t.oldIndex==t.newIndex)return;var n=this.taskList[e],i=n.list;this.updateTaskListCheckNum(n);var o=this.taskList[s],c=o.list;this.updateTaskListCheckNum(o);var l={};l=e==s?{toList:c.map(function(t){return t.taskId}),toTopId:s}:{fromList:i.map(function(t){return t.taskId}),fromTopId:e,toList:c.map(function(t){return t.taskId}),toTopId:s},Object(a.f)(l).then(function(t){}).catch(function(){})}},updateTaskListCheckNum:function(t){t.checkedNum=t.list.filter(function(t){return t.checked}).length},checkboxChange:function(t,e){t.checked?e.checkedNum++:e.checkedNum--,Object(o.o)({taskId:t.taskId,status:t.checked?5:1}).then(function(t){}).catch(function(){t.checked?e.checkedNum--:e.checkedNum++,t.checked=!t.checked})},showDetailView:function(t,e,s){this.taskID=t.taskId,this.detailIndex=s,this.detailSection=e,this.taskDetailShow=!0},detailHandle:function(t){if(0==t.index||t.index)if("title-check"==t.type){var e=this.taskList[t.section];this.$set(e.list[t.index],"checked",t.value),t.value?e.checkedNum++:e.checkedNum--,this.$set(e,"checkedNum",e.checkedNum)}else if("delete"==t.type)this.taskList[t.section].list.splice(t.index,1);else if("change-stop-time"==t.type){new Date(t.value).getTime()/1e3+86399>(new Date).getTime()/1e3?this.taskList[t.section].list[t.index].isEnd=!1:this.taskList[t.section].list[t.index].isEnd=!0,this.taskList[t.section].list[t.index].stopTime=t.value}else if("change-priority"==t.type)this.taskList[t.section].list[t.index].priority=t.value.id;else if("change-name"==t.type)this.taskList[t.section].list[t.index].name=t.value;else if("change-comments"==t.type){var s=this.taskList[t.section].list[t.index].commentCount;"add"==t.value?this.taskList[t.section].list[t.index].commentCount=s+1:this.taskList[t.section].list[t.index].commentCount=s-1}else"change-sub-task"==t.type?(this.taskList[t.section].list[t.index].childWCCount=t.value.subdonecount,this.taskList[t.section].list[t.index].childAllCount=t.value.allcount):"change-main-user"==t.type?this.taskList[t.section].list[t.index].mainUser=t.value:"change-label"==t.type&&(this.taskList[t.section].list[t.index].labelList=t.value)},closeBtn:function(){this.taskDetailShow=!1},exportClick:function(){var t=this;this.taskHandleShow=!1,this.loading=!0,Object(a.a)().then(function(e){Object(p.c)(e),t.loading=!1}).catch(function(){t.loading=!1})}}},k=(s("GmXa"),s("KHd+")),v=Object(k.a)(f,function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"my-task"},[s("xr-header",{attrs:{"icon-class":"wk wk-task","icon-color":"#D376FF"}},[s("span",{attrs:{slot:"label"},slot:"label"},[t._v("我的任务")]),t._v(" "),s("el-popover",{attrs:{slot:"label",placement:"bottom-start",width:"182"},slot:"label",model:{value:t.taskHandleShow,callback:function(e){t.taskHandleShow=e},expression:"taskHandleShow"}},[s("div",{staticClass:"project-list-popover-btn-list"},[s("p",{on:{click:t.exportClick}},[t._v("导出任务")])]),t._v(" "),s("i",{staticClass:"wk wk-manage set-img",attrs:{slot:"reference"},slot:"reference"})])],1),t._v(" "),s("div",{staticClass:"my-task-body"},[s("div",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"},{name:"scrollx",rawName:"v-scrollx",value:{ignoreClass:["ignoreClass"]},expression:"{ ignoreClass :['ignoreClass']}"}],staticClass:"content-box"},t._l(t.taskList,function(e,n){return s("div",{key:n,staticClass:"board-column"},[s("flexbox",{staticClass:"board-column-wrapper ignoreClass",attrs:{orient:"vertical",align:"stretch"}},[s("div",{staticClass:"board-column-header"},[s("div",[s("span",{staticClass:"text"},[t._v(" "+t._s(e.title)+" ")]),t._v(" "),s("span",{staticClass:"text-num"},[t._v(t._s(e.checkedNum)+" / "+t._s(e.list.length))])]),t._v(" "),0==e.checkedNum?s("el-progress",{attrs:{percentage:0}}):s("el-progress",{attrs:{percentage:e.checkedNum/e.list.length*100}})],1),t._v(" "),s("draggable",{staticClass:"board-column-content",attrs:{list:e.list,options:{group:"mission",forceFallback:!1,dragClass:"sortable-drag"},id:n},on:{end:t.moveEndTask}},t._l(e.list,function(i,a){return s("div",{key:a,ref:"taskRow",refInFor:!0,class:i.checked?"board-item board-item-active":"board-item",style:{"border-color":t.getPriorityColor(i.priority).color},on:{click:function(e){t.showDetailView(i,n,a)}}},[s("flexbox",{attrs:{align:"stretch"}},[s("div",{on:{click:function(t){t.stopPropagation()}}},[s("el-checkbox",{on:{change:function(s){t.checkboxChange(i,e)}},model:{value:i.checked,callback:function(e){t.$set(i,"checked",e)},expression:"element.checked"}})],1),t._v(" "),s("div",{staticClass:"element-label"},[t._v(t._s(i.name)),i.workName?s("span",[t._v("（"+t._s(i.workName)+"）")]):t._e()]),t._v(" "),i.mainUser?s("xr-avatar",{staticClass:"head-png",attrs:{name:i.mainUser.realname,id:i.mainUser.userId,size:24,src:i.mainUser.img,disabled:!1,trigger:"hover"}}):t._e()],1),t._v(" "),s("div",{staticClass:"img-group"},[i.stopTime?s("div",{staticClass:"img-box"},[s("i",{staticClass:"wukong wukong-time-task",style:{color:1!=i.isEnd||i.checked?"#999":"red"}}),t._v(" "),s("span",{style:{color:1!=i.isEnd||i.checked?"#999":"red"}},[t._v(t._s(t._f("moment")(i.stopTime,"YYYY-MM-DD"))+" 截止")])]):t._e(),t._v(" "),i.childAllCount>0?s("div",{staticClass:"img-box"},[s("i",{staticClass:"wukong wukong-sub-task"}),t._v(" "),s("span",[t._v(t._s(i.childWCCount)+"/"+t._s(i.childAllCount))])]):t._e(),t._v(" "),i.fileCount?s("div",{staticClass:"img-box"},[s("i",{staticClass:"wukong wukong-file"}),t._v(" "),s("span",[t._v(t._s(i.fileCount))])]):t._e(),t._v(" "),i.commentCount?s("div",{staticClass:"img-box"},[s("i",{staticClass:"wukong wukong-comment-task"}),t._v(" "),s("span",[t._v(t._s(i.commentCount))])]):t._e(),t._v(" "),i.labelList.length<=2?t._l(i.labelList,function(e,n){return s("div",{key:n,staticClass:"item-label",style:{background:e.color}},[t._v("\n                    "+t._s(e.labelName)+"\n                  ")])}):[s("div",{staticClass:"item-label",style:{background:i.labelList[0].color}},[t._v(t._s(i.labelList[0].labelName))]),t._v(" "),s("div",{staticClass:"item-label",style:{background:i.labelList[1].color}},[t._v(t._s(i.labelList[1].labelName))]),t._v(" "),s("el-tooltip",{attrs:{placement:"top",effect:"light","popper-class":"tooltip-change-border task-tooltip"}},[s("div",{staticStyle:{margin:"10px 10px 10px 0"},attrs:{slot:"content"},slot:"content"},t._l(i.labelList,function(e,n){return s("div",{key:n,staticStyle:{display:"inline-block","margin-right":"10px"}},[n>=2?s("span",{staticClass:"k-name",staticStyle:{"border-radius":"4px",color:"#FFF",padding:"3px 10px"},style:{background:e.color?e.color:"#ccc"}},[t._v(t._s(e.labelName))]):t._e()])})),t._v(" "),s("div",{staticClass:"color-label-more"},[s("i",[t._v("...")])])])]],2)],1)})),t._v(" "),e.showTaskAdd?s("list-task-add",{attrs:{"is-top":e.isTop},on:{send:t.getList,close:function(t){e.showTaskAdd=!1}}}):s("div",{staticClass:"new-task",on:{click:function(t){e.showTaskAdd=!0}}},[s("span",{staticClass:"el-icon-plus"}),t._v(" "),s("span",[t._v("新建任务")])])],1)],1)}))]),t._v(" "),t.taskDetailShow?s("task-detail",{ref:"particulars",attrs:{id:t.taskID,"detail-index":t.detailIndex,"detail-section":t.detailSection,"no-listener-class":["board-item"]},on:{"on-handle":t.detailHandle,close:t.closeBtn}}):t._e()],1)},[],!1,null,"56fb1fa7",null);v.options.__file="index.vue";e.default=v.exports},IKCp:function(t,e,s){"use strict";var n=s("JqEL"),i=s("Kw5r"),a={isDown:!1,coord:{x:0,y:0}},o=null,c=[],l=function(t){this&&this.contains(t.target)&&(o.style.cursor="pointer",a.isDown=!0,a.coord.x=t.pageX,a.coord.y=t.pageY)},r=function(t){o.style.cursor="default",a.isDown=!1,a.coord.x=0,a.coord.y=0},d=function(t){if(a.isDown){var e=a.coord.x-t.pageX;a.coord.x=t.pageX,o.scrollLeft=o.scrollLeft+e}},u=function(t){h(t)?(o.style.cursor="default",a.isDown=!1):this&&this.contains(t.target)&&(o.scrollLeft+=t.deltaY)},h=function(t){var e=!1;return c.forEach(function(s){var n=document.getElementsByClassName(s);if(n&&!e)for(var i=0;i<n.length;i++){var a=n[i].getBoundingClientRect();if(t.clientY>a.top&&t.clientY<a.top+a.height&&t.clientX>a.left&&t.clientX<a.left+a.width){e=!0;break}}}),e};e.a=i.default.directive("scrollx",{bind:function(t,e,s){var n=e.value;c=n.ignoreClass},inserted:function(t){o=t,Object(n.b)(document,"mousedown",l.bind(t)),Object(n.b)(document,"wheel",u.bind(t)),Object(n.b)(document,"mouseup",r.bind(t)),Object(n.b)(document,"mousemove",d.bind(t))},unbind:function(t){Object(n.a)(document,"mousedown",l.bind(t)),Object(n.a)(document,"mouseup",r.bind(t)),Object(n.a)(document,"wheel",u.bind(t)),Object(n.a)(document,"mousemove",d.bind(t)),a={isDown:!1,coord:{x:0,y:0}}}})},J3XZ:function(t,e,s){"use strict";var n=s("CDef");s.n(n).a},OOL8:function(t,e,s){},ihDC:function(t,e,s){},pa88:function(t,e,s){"use strict";var n=s("LWkM"),i=s("8GhS"),a=s("Flj7"),o={name:"ListTaskAdd",components:{XhUserCell:i.p},props:{workId:[String,Number],userId:[String,Number],classId:[String,Number],isTop:[String,Number],permission:{type:Object,default:function(){return{}}}},data:function(){return{sendLoading:!1,sendContent:"",sendStopTime:"",mainUser:[]}},computed:{createMainUser:function(){return this.mainUser.length?this.mainUser[0]:null},ownerListRequest:function(){return this.workId?n.t:null},ownerListParams:function(){return this.workId?{workId:this.workId}:null},canSetMainUser:function(){return!this.workId||this.permission&&this.permission.setTaskMainUser&&!this.userId},canSetTime:function(){return!this.workId||this.permission&&this.permission.setTaskTime}},watch:{},mounted:function(){},beforeDestroy:function(){},methods:{selectMainUser:function(t){this.mainUser=t.value},send:function(){var t=this;if(this.sendContent.length){this.sendLoading=!0;var e={name:this.sendContent,stopTime:this.sendStopTime};this.userId?e.mainUserId=this.userId:e.mainUserId=this.createMainUser?this.createMainUser.userId:"",this.workId&&(e.workId=this.workId),this.classId&&(e.classId=this.classId),this.isTop&&(e.isTop=this.isTop),Object(a.n)(e).then(function(e){t.sendLoading=!1,t.$message.success("新建成功"),t.resetSendData(),t.$emit("send")}).catch(function(){t.sendLoading=!1})}else this.$message.error("请填写任务标题")},resetSendData:function(){this.sendContent="",this.sendStopTime="",this.mainUser=[]},close:function(){this.$emit("close")}}},c=(s("J3XZ"),s("KHd+")),l=Object(c.a)(o,function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{directives:[{name:"loading",rawName:"v-loading",value:t.sendLoading,expression:"sendLoading"}],staticClass:"list-task-add"},[s("el-input",{attrs:{rows:2,maxlength:100,type:"textarea",placeholder:"请输入内容"},model:{value:t.sendContent,callback:function(e){t.sendContent=e},expression:"sendContent"}}),t._v(" "),t.canSetTime||t.canSetMainUser?s("flexbox",{staticClass:"add-info",attrs:{justify:"flex-end"}},[t.canSetTime?s("el-date-picker",{ref:"endTime",class:{"no-time-top":!t.sendStopTime},attrs:{type:"date","value-format":"yyyy-MM-dd",placeholder:""},model:{value:t.sendStopTime,callback:function(e){t.sendStopTime=e},expression:"sendStopTime"}}):t._e(),t._v(" "),t.canSetMainUser?s("xh-user-cell",{ref:"userCell",attrs:{value:t.mainUser,"info-request":t.ownerListRequest,"info-params":t.ownerListParams,disabled:!t.canSetMainUser,placement:"top"},on:{"value-change":t.selectMainUser}},[s("div",{staticClass:"select-user",attrs:{slot:"reference"},slot:"reference"},[t.createMainUser?s("xr-avatar",{staticClass:"user-img",attrs:{name:t.createMainUser.realname,size:24,src:t.createMainUser.img}}):s("i",{staticClass:"wk wk-persons add-info--person"})],1)]):t._e()],1):t._e(),t._v(" "),s("div",{staticClass:"btn-box"},[s("el-button",{directives:[{name:"debounce",rawName:"v-debounce",value:t.send,expression:"send"}],attrs:{size:"mini",type:"primary"}},[t._v("确定")]),t._v(" "),s("el-button",{attrs:{size:"mini"},on:{click:t.close}},[t._v("取消")])],1)],1)},[],!1,null,"4eb09ed3",null);l.options.__file="ListTaskAdd.vue";e.a=l.exports},zIzm:function(t,e,s){"use strict";var n=s("ihDC");s.n(n).a}}]);