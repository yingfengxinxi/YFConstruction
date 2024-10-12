<template>
  <div class="dom">
    <div class="dom_1">
      <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="planImg" width="100%" />
      <el-tooltip class="item" effect="dark" :content="item.assetName" :key="item.assetId" placement="top" v-for="item in elevators">
      <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     
            
            v-if="item.planeCoordinatePcX&&item.monitorType==3"
            class="dtimg"
            :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
            @click="goStateList(item.assetId,item.monitorType)"
            src="../../../../../static/img/towerCraneMonitor/zlsjjico.png" />
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     
            
            v-if="item.planeCoordinatePcX&&item.monitorType==2"
            class="dtimg"
            :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
            @click="goStateList(item.assetId,item.monitorType)"
            src="../../../../../static/img/towerCraneMonitor/tjdjimg.png" />
         <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     
            
            v-if="item.planeCoordinatePcX&&item.monitorType==99"
            class="dtimg"
            :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
            @click="goStateList(item.assetId,item.monitorType)"
            src="../../../../../static/img/towerCraneMonitor/dtdgimg.png" />
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     
            
            v-if="item.planeCoordinatePcX&&item.monitorType==11"
            class="dtimg"
            :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
            @click="goStateList(item.assetId,item.monitorType)"
            src="../../../../../static/img/towerCraneMonitor/gzm_icon.png" /> 
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     
            
            v-if="item.planeCoordinatePcX&&item.monitorType==1"
            class="dtimg"
            :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
            @click="goStateList(item.assetId,item.monitorType)"
            src="../../../../../static/img/towerCraneMonitor/sjk_icon.png" />   
   		</el-tooltip>
    </div>
    <div class="dom_2">
      <div class="dom_2_t">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">今日升降机工作情况</div>
        <div class="gzxq_dom">
          <div class="gzxq_doms" >
            <div class="gzxq_doms_img">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/xzimg.png"/>
            </div>
            <div class="gzxq_doms_txt1">
              <p>{{line}}</p>
              <p>在线设备</p>
            </div>
            <div class="gzxq_doms_txt2" style="top: 10%;">
              <p>设备总数</p>
            </div>
            <div class="gzxq_doms_txt3" style="top: 10%;">
              <p>{{elevators.length}}</p>
            </div>
          </div>
          <div class="gzxq_doms" >
            <div class="gzxq_doms_img">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bjimg.png"/>
            </div>
            <div class="gzxq_doms_txt1">
              <p>{{alertData?alertData.jt[1].val:0}}</p>
              <p>报警总数</p>
            </div>
            <div class="gzxq_doms_txt2">
              <p>昨日数据</p>
              <p>同比过去30日</p>
            </div>
            <div class="gzxq_doms_txt3">
              <p>{{alertData?alertData.zt[1].val:0}}</p>
              <p :class="alertData?((alertData.sst[1].val/30).toFixed(2))>alertData.jt[1].val?'text_green':'text_red':''">{{alertData?(alertData.sst[1].val/30).toFixed(2):''}}</p>
            </div>
          </div>
          <div class="gzxq_doms" >
            <div class="gzxq_doms_img">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/wzimg.png"/>
            </div>
            <div class="gzxq_doms_txt1">
              <p>{{alertData?alertData.jt[2].val:0}}</p>
              <p>违章总数</p>
            </div>
            <div class="gzxq_doms_txt2">
              <p>昨日数据</p>
              <p>同比过去30日</p>
            </div>
            <div class="gzxq_doms_txt3">
              <p>{{alertData?alertData.zt[2].val:0}}</p>
              <p :class="alertData?((alertData.sst[2].val/30).toFixed(2))>alertData.jt[2].val?'text_green':'text_red':''">{{alertData?(alertData.sst[2].val/30).toFixed(2):0}}</p>
            </div>
          </div>
          <div class="gzxq_doms" style="border-bottom: 0">
            <div class="gzxq_doms_img">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/yjimg.png"/>
            </div>
            <div class="gzxq_doms_txt1">
              <p>{{alertData?alertData.jt[0].val:0}}</p>
              <p>预警总数</p>
            </div>
            <div class="gzxq_doms_txt2">
              <p>昨日数据</p>
              <p>同比过去30日</p>
            </div>
            <div class="gzxq_doms_txt3">
              <p>{{alertData?alertData.zt[0].val:0}}</p>
              <p :class="alertData?((alertData.sst[0].val/30).toFixed(2))>alertData.jt[0].val?'text_green':'text_red':''">{{alertData?(alertData.sst[0].val/30).toFixed(2):0}}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="dom_2_b">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">设备实时运行列表</div>
        <div class="table_dom">
          <table cellpadding="0" cellspacing="0" class="my_table">
            <tr v-for="item in elevators.slice(0,7)">
              <td width="30%">{{item.assetName}}</td>
              <td>{{item.monitorTime}}</td>
              <td width="35px"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.line==1" height="20px" src='../../../../../static/img/towerCraneMonitor/yxico.png'>
              	<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else height="20px" src='../../../../../static/img/towerCraneMonitor/lxico.png'/></td>
              </td>
              <td width="60px">{{item.line==1?'运行':'离线'}}</td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { queryMenuListByPath } from '@/api/admin/menu'
  import { getAllList ,getAllAlertList} from './api/elevator'
  import request from '@/utils/request'
  export default {
    name: "elevatorOverview",
    data() {
      return {
        timer:null, //定时器名称
        elevators:[],
        alertData:null,
        line:0,
        planImg:'',
          proDate:{}
      };
    },
    created() {

    },
    mounted() {
      this.init()
       this.getProDate()
    },
    methods: {
    	getProDate(){
				let projectId=JSON.parse(localStorage.getItem('loginUserInfo')).projectId

				request({
				    url: 'build/projectInfo/getProjectInfoById/'+projectId,
				    method: 'post',
				    data: {},
				    headers: {
				      'Content-Type': 'application/json;charset=UTF-8'
				    }
				 }).then((res)=>{
				  	this.proDate=res.data

				  	request({
					    url: 'adminFile/queryFileList/'+this.proDate.planBatchId,
					    method: 'post',
					    data: {},
					    headers: {
					      'Content-Type': 'application/json;charset=UTF-8'
					    }
					 }).then((res)=>{
					  	this.planImg=res.data[0].url
					  })

				  })
			},
      init(){
      	let that=this
        getAllList().then(res => {
          this.elevators = res.data;
          this.elevators.forEach(function (item,ina,all) {
            if(item.line == 1){
              that.line++;
            }
          })
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
        getAllAlertList().then(res => {
          this.alertData = res.data;
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      goStateList(assetId,type){
      	if (data.monitorType == '2') {//塔机
      		this.goToUrl('tower', {assetId:assetId})
        }
        if (data.monitorType == '3') {//升降机
           this.goToUrl('elevator', {assetId:assetId})
        }
        if (data.monitorType == '99') {//吊钩可视化
            this.goToUrl('hookBlindVisual', {assetId:assetId,name:name,isHook:true})
        }
        if (data.monitorType == '1') {//深基坑
           this.goToUrl('foundationView', {assetId:assetId})
        }
        if (data.monitorType == '11') {//高支模
            this.goToUrl('highFormView', {assetId:assetId})
        }
        
      },
      goToUrl (path, queryParams) {
        let param = { path: path }
        // 查询菜单
        queryMenuListByPath(param).then(res => {
          
          let data = res.data
          if (data.length <= 0) {
            return;
          }
          let item = data[0]
          var activeclass = {
            close: true,
            title: item.menuName,
            comname:item.path,
            name: item.menuId
          }
          if (JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
            this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
          }
          this.$store.state.app.activeclass = activeclass.comname
          this.$router.push({ name: activeclass.name, query: queryParams })
        })
      },
    }
  }
</script>
<style scoped lang="scss">
@import "./equover.scss";
</style>
