<template>
  <div class="main select" id="cloudCenter">
    <div class="main-body">
      <div class="body">
        <div class="top">
          <div class="left">
            <div class="left-img1">
              <div class="span-img1">
                <div class="img1"></div>
              </div>
              <div class="title1">塔吊基本信息</div>
            </div>
            <div class="left_bottom">
              <div class="img2">
              	<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img2" src="../../../../../static/img/towerCraneMonitor/top-left-2.png"/>
              </div>
              <div class="straight_line"></div>
              <div class="left_right_div">
									<div style="margin-left: 0.2rem;">
										<span class="left_right_div_top_span1">塔机名称</span>
										<span class="left_right_div_top_span2">
													  <el-select v-model="assetId" @change="towerCraneChange" placeholder="请选择"
													  >
													    <el-option
													      v-for="item in towerCraneOptions"
													      :key="item.assetId"
													      :label="item.assetName"
													      :value="item.assetId">
													    </el-option>
													  </el-select>
												  </span>
									</div>
                <div class="left_right_div_top">
                  <span class="left_right_div_top_span1">塔机编号</span>
                  <span class="left_right_div_top_span2">{{assetCode}}</span>
                </div>
                <div class="left_right_div_bottom">
                  <div class="left_right_div_bottom_div div_color1">
                    <div class="left_right_div_bottom_div_img icon_img1"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/top-right-1.png"></div>
                    <div class="left_right_div_bottom_div_center">55.0m</div>
                    <div class="left_right_div_bottom_div_right">前臂长</div>
                  </div>
                  <div class="left_right_div_bottom_div div_color2">
                    <div class="left_right_div_bottom_div_img icon_img2"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/top-right-2.png"></div>
                    <div class="left_right_div_bottom_div_center">10.0m</div>
                    <div class="left_right_div_bottom_div_right">后臂长</div>
                  </div>
                  <div class="left_right_div_bottom_div div_color3">
                    <div class="left_right_div_bottom_div_img icon_img3"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/top-right-3.png"></div>
                    <div class="left_right_div_bottom_div_center">80.0m</div>
                    <div class="left_right_div_bottom_div_right">塔臂高</div>
                  </div>
                  <div class="left_right_div_bottom_div div_color4">
                    <div class="left_right_div_bottom_div_img icon_img4"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/top-right-4.png"></div>
                    <div class="left_right_div_bottom_div_center">2.0m</div>
                    <div class="left_right_div_bottom_div_right">倍率</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="right">
            <div class="left-img1">
              <div class="span-img1">
                <div class="img1"></div>
              </div>
              <div class="title1">塔吊监测数据</div>
            </div>

            <div class="tables">
              <el-tabs
                v-model="tabCurrentName"
                type="border-card"
                class="d-container-bd--left"
                style="border-top: 1px solid #e6e6e6;"
              >
                <el-tab-pane
                  label="实时数据"
                  name="realTimeData"
                >
                  <Real-Time-Data  :lzData="lzData"></Real-Time-Data>
                </el-tab-pane>
                <el-tab-pane
                  label="历史数据"
                  name="historyDataTable"
                >
                  <History-Data-Table :lzData="lzData"></History-Data-Table>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
        <div class="bottom">
          <div class="left-img1">
            <div class="span-img1">
              <div class="img1"></div>
            </div>
            <div class="title1">塔机报警信息</div>
          </div>
          <div class="bottom_bottom">
            <div class="bottom_div ">
              <div class="bottom_img1"> <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-1.png"></div>
              <div class="bottom_div_div">高度高位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img2">
              	<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-2.png">
                <div class="bottom_top_img">
                  <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-top-1.png">
                </div>
              </div>
              <div class="bottom_div_div">高度低位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img3"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-3.png"></div>
              <div class="bottom_div_div">幅度高位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img4"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-4.png"></div>
              <div class="bottom_div_div">幅度低位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img5"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-5.png"></div>
              <div class="bottom_div_div">X角度度高位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img6"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-6.png"></div>
              <div class="bottom_div_div">Y角度度高位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img7"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-7.png"></div>
              <div class="bottom_div_div">载重高位</div>
            </div>
            <div class="bottom_div ">
              <div class="bottom_img8"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/bottom-8.png"></div>
              <div class="bottom_div_div">载重低位</div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HistoryDataTable from "./historyData";
import request from '@/utils/request'
import RealTimeData from "./realTimeData";
// import {selectTowerCrane} from "../api/assets"
import { mapGetters } from 'vuex'
export default {
  name:"towerCraneMonitor",
  components: {
    HistoryDataTable,
    RealTimeData
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      //默认显示tab
      tabCurrentName: "realTimeData",
	  // 塔机设备列表
	  towerCraneOptions: [],
	  // 设备id
	  assetId:'',
	  // 设备编号
	  assetCode: '',
      lzData: [{}],
      timer:null, //定时器名称
    };
  },
  computed: {
    ...mapGetters(['supply','pageDataReflushTime']),
  },
  created() {},
  mounted(){
	this.selectTowerCraneList()
  	this.remsize()
//    this.resetScreenSize()
  },
  watch:{
    dataList: function (val) {

      this.$nextTick(() => {
      })
    }
  },
  beforeDestroy(){
    clearInterval(this.timer);　　// 清除定时器
    this.timer = null;
  },
  methods: {
  	remsize(){
    	function setRemFontSize() {
        var remSize = window.innerWidth / 19.2;
        document.querySelector("html").style.fontSize = remSize + "px";
      }
      setRemFontSize();
      window.addEventListener("resize", function() {
        setTimeout(function() {
          setRemFontSize();
        }, 200)
      });
    },
	// 获取塔机设备列表
	// selectTowerCraneList() {
  	//   var _this = this;
    //
	// 	selectTowerCrane().then(res => {
	// 		this.towerCraneOptions = res.data
	// 		if (this.towerCraneOptions.length > 0) {
	// 			this.assetId = this.towerCraneOptions[0].assetId
	// 			this.assetCode = this.towerCraneOptions[0].assetCode
     //    this.getTowerCraneData(this.towerCraneOptions[0].assetId);
     //    this.timer = setInterval(
     //      function()  {
     //        _this.getTowerCraneData(_this.assetId);
     //      }, this.pageDataReflushTime
     //    )
	// 		}
	// 	})
    //
	// },
    selectTowerCraneList() {
      this.towerCraneOptions = [{assetId:4,assetName:'1#塔吊',assetCode:'CS5523'},{assetId:5,assetName:'2#塔吊',assetCode:'CS5524'},]
      this.assetId = 4
      this.assetCode = 'CS5523'
      this.getTowerCraneData( this.assetId);
    },
	towerCraneChange(value) {
		let obj = {};
		this.towerCraneOptions.forEach(val => {
			if (val.assetId == value) {
				obj = val;
			}
		})
    this.getTowerCraneData(value);
		this.assetCode = obj.assetCode;
	},
    getTowerCraneData(id){
      // request({
      //   url: 'build/equipmentMonitorData/getTowerCraneData/'+id,
      //   method: 'post',
      //   data: {},
      //   headers: {
      //     'Content-Type': 'application/json;charset=UTF-8'
      //   }
      // }).then(response => {
      //   console.log(response)
      //
      // }).catch(() => {
      //
      // })
      this.lzData = [{monitorTime :'2021-12-07 11:25:18',fd :'12',gd:'16',zj:'23',dz:'1.2',dz:'1.2',lj:'14.4',fs:'2.1',qjx:'0',qjy:'0'},
        {monitorTime :'2021-12-07 11:25:18',fd :'12',gd:'16',zj:'23',dz:'1.2',dz:'1.2',lj:'14.4',fs:'2.1',qjx:'0',qjy:'0'},
        {monitorTime :'2021-12-07 11:25:18',fd :'12',gd:'16',zj:'23',dz:'1.2',dz:'1.2',lj:'14.4',fs:'2.1',qjx:'0',qjy:'0'},
        {monitorTime :'2021-12-07 11:25:18',fd :'12',gd:'16',zj:'23',dz:'1.2',dz:'1.2',lj:'14.4',fs:'2.1',qjx:'0',qjy:'0'},
        {monitorTime :'2021-12-07 11:25:18',fd :'12',gd:'16',zj:'23',dz:'1.2',dz:'1.2',lj:'14.4',fs:'2.1',qjx:'0',qjy:'0'}]
    }
  }
};
</script>
<style lang="scss" scoped>
.main {
  height: 100%;
   transform-origin: left top;
   overflow: hidden;
}
.main-body {
  background-color: white;
  padding: 10px;
  height: 100%;
}

.body {
  overflow: hidden;
  height: 100%;
}
.top{
	height:60%;
}
.left {
  width: 7.34rem;
  height: calc(100% - 32px);
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.44);
  border-radius: 5px;
  float: left;
  margin-top: 0.21rem;
  margin-right: 0.11rem;
  margin-bottom: 0.11rem;
  margin-left: 0.2rem;
  .left-img1 {
    margin-top: 0.15rem;
    margin-left: 0.14rem;
    overflow: hidden;
    .span-img1 {
      float: left;
      display: block;
      margin-right: 0.10rem;
      margin-top: 2px;
      .img1 {
        float: left;
        display: block;
        width: 16px;
       height: 16px;
        background: url(../../../../../static/img/towerCraneMonitor/top-left-1.png);
      }
    }
    .title1 {
      float: left;
      display: block;
      width: 0.96rem;
      height: 22px;
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #404040;
      line-height: 22px;
    }
  }
  .left_bottom {
    margin-top: 0.62rem;
    margin-left: 0.27rem;
    height: 83%;
    overflow: hidden;

    .straight_line {
      float: left;
      width: 1px;
      height: 80%;
      border: 1px solid #e1f0ff;
      margin-left: 0.34rem;
      margin-top: 0.07rem;
      margin-bottom: 0.26rem;
    }
    .left_right_div {
      float: left;
      height: 80%;
      .left_right_div_top {
        margin-left: 0.20rem;
        margin-bottom: 8.4%;
        height: 6.5%;
        .left_right_div_top_span1 {
          width: 0.61rem;
          height: 6.5%;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #404040;
          line-height: 100%;
        }
        .left_right_div_top_span2 {
          margin-left: 10px;
          width: 129px;
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #3477ff;
          line-height: 20px;
        }
      }
      .left_right_div_bottom {
        margin-top: 7%;
        height: 58%;
        margin-left: 0.2rem;
        .left_right_div_bottom_div {
          overflow: hidden;
          margin-bottom: 7%;
          width: 2.28rem;
          display: flex;
          align-items: center;
          justify-content:space-around;
          height: 21%;
          border-radius: 5px;
          .left_right_div_bottom_div_img {
            width: 0.16rem;
            height: 100%;
            display: flex;
        		align-items: center;
            margin-left: 0.16rem;
            img{
            	width: 0.16rem;
            }
          }
          /*.icon_img1 {
            background: url(../../../../../static/img/towerCraneMonitor/top-right-1.png);
          }
          .icon_img2 {
            background: url(../../../../../static/img/towerCraneMonitor/top-right-2.png);
          }
          .icon_img3 {
            background: url(../../../../../static/img/towerCraneMonitor/top-right-3.png);
          }
          .icon_img4 {
            background: url(../../../../../static/img/towerCraneMonitor/top-right-4.png);
          }*/
          .left_right_div_bottom_div_center {

            margin-left: 0.30rem;

            width: 0.77rem;

            font-size: 0.26rem;
            font-weight: 500;
            color: #649bf8;

          }
          .left_right_div_bottom_div_right {


            margin-right: 0.10rem;
            /*width: 0.42rem;*/

            font-size: 0.14rem;
            font-weight: 500;
            color: #404040;

          }
        }
      }
    }
  }
}
.img2 {
      float: left;
     height: 90%;
      /*background: url(../../../../../static/img/towerCraneMonitor/top-left-2.png);*/
   		img{
   			height: 100%;
   		}
    }
.div_color1 {
  background: rgba(52, 119, 255, 0.08);
  border: 1px solid rgba(100, 155, 248, 0.2);
}
.div_color2 {
  background: rgba(48, 191, 120, 0.08);
  border: 1px solid rgba(48, 191, 120, 0.2);
}
.div_color3 {
  background: rgba(250, 173, 20, 0.12);
  border: 1px solid rgba(250, 173, 20, 0.2);
}
.div_color4 {
  background: rgba(255, 52, 52, 0.08);
  border: 1px solid rgba(100, 155, 248, 0.2);
}
.right {
  width: 8.57rem;
  height: calc(100% - 32px);
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  float: left;
  margin-left: 0.1rem;
  margin-top: 0.21rem;
  margin-bottom: 0.11rem;
  .left-img1 {
    margin-top: 15px;
    margin-left: 14px;
    overflow: hidden;
    .span-img1 {
      float: left;
      display: block;
      margin-right: 10px;
      margin-top: 2px;
      .img1 {
        float: left;
        display: block;
        width: 16px;
        height: 16px;
        background: url(../../../../../static/img/towerCraneMonitor/top-left-1.png);
      }
    }
    .title1 {
      float: left;
      display: block;
      width: 96px;
      height: 22px;
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #404040;
      line-height: 22px;
    }
  }
  .tables {
    width: 8.55rem;
    height: 490px;
  }
}
.bottom {
  width: 16.13rem;
  height:calc(40% - 30px);
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  float: left;
  margin-top: 11px;
  margin-right: 0.30rem;
  margin-bottom: 19px;
  margin-left: 0.2rem;
  .left-img1 {
    margin-top: 15px;
    margin-left: 0.14rem;
    overflow: hidden;
    .span-img1 {
      float: left;
      display: block;
      margin-right: 0.10rem;
      margin-top: 2px;
      .img1 {
        float: left;
        display: block;
        width: 16px;
        height: 16px;
        background: url(../../../../../static/img/towerCraneMonitor/top-left-1.png);
      }
    }
    .title1 {
      float: left;
      display: block;
      width: 0.96rem;
      height: 22px;
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #404040;
      line-height: 22px;
    }
  }
  .bottom_bottom {
    margin-top: 0.80rem;
    margin-right: 0.3rem;
    overflow: hidden;
    .bottom_div {
      float: left;
      width: 1.64rem;
      height: 145px;
      margin-left: 0.33rem;
    }
  }
}
.bottom_img1 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-1.png);
}
.bottom_img2 {
  width: 1.64rem;
  position: relative;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-2.png);
  overflow: hidden;
}
.bottom_top_img {
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-top-1.png);
  width: 0.3632rem;
  position:absolute;
  right:0;
  top:0;
  img{
  	 width: 0.3632rem;
  }
}
.bottom_img3 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-3.png);
}
.bottom_img4 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-4.png);
}
.bottom_img5 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-5.png);
}
.bottom_img6 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-6.png);
}
.bottom_img7 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-7.png);
}
.bottom_img8 {
  width: 1.64rem;
  img{
  	width: 1.64rem;
  }
  //background: url(../../../../../static/img/towerCraneMonitor/bottom-8.png);
}
.bottom_div_div {
  text-align: center;
}

.select {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
/deep/ .el-tabs__content {
  padding: 0px !important;
}
/deep/ .el-tabs__nav{
  position: relative;
    left: 50%;
    transform: translateX(-50%)!important;
    margin: 0 auto;
}
/deep/ .el-tabs__header{
  background: none!important;
  border: none!important
}
/deep/ .d-container-bd--left{
      border: none!important;
    box-shadow: none!important;
}
/deep/.el-tabs__item,.is-active,.is-top{
  border: none!important
}
</style>
