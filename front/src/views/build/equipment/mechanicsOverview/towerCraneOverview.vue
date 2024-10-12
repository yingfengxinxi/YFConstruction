<template>
  <div class="dom">
    <div class="dom_1">
      <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="planImg" width="100%"/>
      <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'
        v-for="item in towerCrane"
        :key="item.assetId"
        class="dtimg"
        :style="{top:item.planeCoordinatePcX+'px',left:item.planeCoordinatePcY+'px'}"
        @click="goStateList(item.assetId)"
        src="../../../../../static/img/towerCraneMonitor/tjdjimg.png"/>
    </div>
    <div class="dom_2">
      <div class="dom_2_t">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">今日塔机工作情况</div>
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
              <p>{{towerCrane.length}}</p>
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
              <p :class="alertData?((alertData.sst[1].val/30).toFixed(2))>alertData.jt[1].val?'text_green':'text_red':''">{{alertData?(alertData.sst[1].val/30).toFixed(2):0}}</p>
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
          <div class="gzxq_doms " style="border-bottom: 0">
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
            <tr v-for="item in towerCrane.slice(0,8)">
              <td width="30%">{{item.assetName}}</td>
              <td>{{item.monitorTime}}</td>
              <td width="35px"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.line==1" height="20px" src='../../../../../static/img/towerCraneMonitor/yxico.png'>
              	<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else height="20px" src='../../../../../static/img/towerCraneMonitor/lxico.png'/></td>
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
  import { getList ,getAlertList} from './api/towerCrane'
  import request from '@/utils/request'
    export default {
      name: "towerCraneOverview",
      data() {
        return {
          timer:null, //定时器名称
          towerCrane:[],
          line:0,
          alertData:null,
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
          this.loading = true
          getList().then(res => {
            this.towerCrane = res.data;
            this.towerCrane.forEach(function (item) {
              if(item.line == 1){
                that.line++;
              }
            })
            this.loading = false
          }).catch(() => {
              this.loading = false
          })
          getAlertList().then(res => {
            this.alertData = res.data;
            console.log(this.alertData)
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        },
        goStateList(assetId){
          this.goToUrl('tower', {assetId:assetId})
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
