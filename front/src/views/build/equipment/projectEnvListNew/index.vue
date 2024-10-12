<template>
	<div class="con_box">
		<div class="environment_box" v-for="(item, index) in dataList">
			<div class="main">
				<div class="minor">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectEnvList/img1.png" />
					<div class="proName">
						<p class="proName-left">项目名称:{{item.projectName}} </p>
						<p class="proName-right">{{item.monitorTime}} </p>
					</div>
				</div>
				<div class="left">
					<div class="sbzx-dom" v-if="item.online == 1">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectEnvList/zximg.png" />
						<p class="sbzx">设备在线</p>
					</div>
					<div class="sbzlx-dom" v-if="item.online == 0">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectEnvList/lximg.png" />
						<p class="sbzx">设备离线</p>
					</div>
				</div>
				<div class="right">
					<div class="sj-ddom sj-ddom-hs">
						<div class="sjtxt" style="margin-left:0.3rem;">PM10</div>
						<div class="sjtxt">{{item.pm10Text}} {{item.pm10Unit}}</div>
						<div class="sjtxt">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.xj =='xj'" src="../../../../assets/img/projectEnvList/xj.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.xj == 'ss'" src="../../../../assets/img/projectEnvList/ss.png" />
							<span v-if="item.xj == '--'">--</span>
						</div>
						<div class="sjtxt-1" :style="'background-color:'+ys(item.pm10)">{{panduan(item.pm10)}}</div>
					</div>
					<div class="sj-ddom sj-ddom-zhong">
						<div class="sjtxt">PM2.5</div>
						<div class="sjtxt">Tsp</div>
						<div class="sjtxt">噪音</div>
						<div class="sjtxt">温度</div>
						<div class="sjtxt">气压</div>
					</div>
					<div class="sj-ddom sj-ddom-xia">
						<div class="sjtxt">{{item.pm25Text}}{{item.pm25Unit}}</div>
						<div class="sjtxt">{{item.tsp}}{{item.tspUnit}}</div>
						<div class="sjtxt">{{item.noise}}{{item.noiseUnit}}</div>
						<div class="sjtxt">{{item.temperature}}{{item.tempUnit}}</div>
						<div class="sjtxt">{{item.wind}}{{item.windUnit}}</div>
					</div>
				</div>
				<div>
					
				</div>
			</div>
			
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import {
		mapGetters
	} from 'vuex'

	import {
		selectOverViewList
	} from '../api/projectEnvList'

	export default {
		data() {
			return {
				dataList: [],
				timer: null, //定时器名称
			}
		},
		computed: {
			...mapGetters(['supply', 'pageDataReflushTime']),
		},
		created() {
			// console.log(this.pageDataReflushTime)
			this.initData()
		},
		mounted() {
			this.remsize()
		},
		watch: {
			dataList: function(val) {

				this.$nextTick(() => {
				})
			}
		},
		beforeDestroy() {
			clearInterval(this.timer); // 清除定时器
			this.timer = null;
		},
		methods: {
			initData() {
				this.getlist()
				let _this = this
				this.timer = setInterval(
					function() {
				 	_this.getlist()
					}, this.pageDataReflushTime
				)
			},
			getlist() {
				selectOverViewList().then(res => {
					this.dataList.forEach(function(e){ 
					  res.data.forEach(function(f,i){
						  if(e.assetId == f.assetId){
							  
							  if(e.pm10<f.pm10){
								  res.data[i].xj='xj'
							  }else if(e.pm10>f.pm10){
								  res.data[i].xj='ss'
							  }else{
								  res.data[i].xj='--'
							  }
							  return
						  }
					  })
					}) 
					this.dataList = res.data
				})
			},
			remsize() {
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
			panduan(pm10){
				var str = "--"
				if(pm10<=35){
					str = "优"
				}else if(35<pm10&&pm10<=75){
					str = "良"
				}else if(75<pm10&&pm10<=115){
					str = "轻度污染"
				}else if(115<pm10&&pm10<=150){
					str = "中度污染"
				}else if(150<pm10&&pm10<=250){
					str = "重度污染"
				}else if(250<pm10){
					str = "严重污染"
				}
				return str;
			},
			ys(pm10){
				var str = "#ffffff"
				if(pm10<=35){
					str = "#00d1a0"
				}else if(35<pm10&&pm10<=75){
					str = "#39d2ff"
				}else if(75<pm10&&pm10<=115){
					str = "#4e74e6"
				}else if(115<pm10&&pm10<=150){
					str = "#f3bc40"
				}else if(150<pm10&&pm10<=250){
					str = "#f5863e"
				}else if(250<pm10){
					str = "#fb466b"
				}
				return str;
			}
		}
	}
</script>

<style scoped>
	.con_box {
		height: 100%;
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
		align-content: flex-start;
	}

	.environment_box {
		width: 5.5rem;
		height: 1.5rem;
		margin-top: 0.2rem;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		margin-right: 0.13rem;
	}

	.con_box>:nth-child(3n+1) {
		margin-left: 0.13rem;
	}

	.con_box>:nth-child(3n) {
		margin-right: 0rem;
	}

	.main {
		height: 82.71%;
		width: 100%;
		display: flex;
		flex-wrap: wrap;
	}

	.left {
		margin-top: 0.20rem;
		height: 89.34%;
		width: 0.75rem;
		font-weight: 600;
	}

	.echarts {
		height: 75.22%;
		width: 100%;
	}

	.grade {
		width: 100%;
		font-size: 0.18rem;
		font-weight: 500;
		text-align: center;
	}

	.date {
		height: 0.2rem;
		width: 100%;
		margin-top: 0.09rem;
		font-size: 0.14rem;
		font-weight: 500;
		line-height: 0.20rem;
		text-align: center;
	}

	.right {
		margin-top: 0.20rem;
		height: 89.34%;
		width: 4.35rem;
	}

	.small_box {
		height: 22.93%;
		width: 0.96rem;
		margin-right: 0.05rem;
		display: flex;
		flex-wrap: wrap;
		align-content: space-around;
	}

	.pm10 {
		background: rgba(86, 148, 254, 0.16);
		border-radius: 2px;
	}

	.pm10 .value {
		color: rgba(86, 148, 254, 1);
	}

	.wind_speed {
		background: rgba(136, 255, 130, 0.12);
		border-radius: 2px;
	}

	.wind_speed .value {
		color: rgba(87, 203, 145, 1);
	}

	.tsp {
		background: rgba(109, 201, 251, 0.16);
	}

	.tsp .value {
		color: rgba(86, 199, 254, 1);
	}

	.noise {
		background: rgba(31, 216, 181, 0.16);
		border-radius: 2px;
	}

	.noise .value {
		color: rgba(87, 203, 145, 1);
	}

	.temp {
		background: rgba(250, 195, 20, 0.16);
		border-radius: 2px;
	}

	.temp .value {
		color: rgba(250, 173, 20, 1);
	}

	.humidness {
		background: rgba(255, 154, 70, 0.16);
		border-radius: 2px;
	}

	.humidness .value {
		color: rgba(255, 135, 83, 1);
	}

	.value {
		font-size: 0.16rem;
		font-weight: 500;
		width: 100%;
		text-align: center;
	}

	.title {
		font-size: 0.12rem;
		font-weight: 500;
		width: 100%;
		text-align: center;
		color: rgba(102, 102, 102, 1);
	}

	.minor {
		height: 16.94%;
		width: 100%;
		border-top: 1px solid rgba(245, 245, 245, 1);
		margin: 0 auto;
		display: flex;
	}

	.minor img {
		width: 0.22rem;
		height: 0.22rem;
		margin-top: 0.14rem;
		margin-left: 0.15rem;
	}

	.proName {
		width: 100%;
		height: 0.22rem;
		margin-top: 0.10rem;
		margin-left: 0.14rem;
		font-size: 0.14rem;
		font-weight: 500;
		color: #404040;
		line-height: 0.22rem;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	.proName-left{
		width: 65%;
		float: left;
		overflow:hidden;
		text-overflow:ellipsis;
	}
	.proName-right{
		float: right;
		width: 35%;
		overflow:hidden;
		text-overflow:ellipsis;
	}
	.sbzx-dom  img {
		width: 0.6rem;
		height: 0.6rem;
		margin-top: 0.14rem;
		margin-left: 0.15rem;
	}
	.sbzlx-dom  img {
		width: 0.6rem;
		height: 0.6rem;
		margin-top: 0.14rem;
		margin-left: 0.15rem;
	}
	.sbzx{
		margin-left: 0.15rem;
	}
	.sbzx-dom{
		color: #404040;
	}
	.sbzlx-dom{
		color: #999999;
	}
	.sj-ddom{
		width: 100%;
		height: 30%;
		display: flex;
		align-items: baseline;
		font-weight: 600;
	}
	.sj-ddom-hs{
		background-color: #F7F7F7
	}
	.sjtxt{
		margin-top: 0.08rem;
		margin-left: 0.08rem;
	}
	.sjtxt-1{
		margin-top: 0.08rem;
		margin-left: 0.08rem;
		background-color: #00d1a0;
		color: #FFFFFF;
		font-size: 0.08rem;
		height: 0.16rem;
		width: 0.55rem;
		padding: 0.02rem;
		border-radius: 10%;
		text-align: center;
		font-weight: 400;
	}
	.sj-ddom-zhong{
		font-size: 0.08rem;
		color: #999999;
		justify-content: space-around;
	}
	.sj-ddom-xia{
		justify-content: space-around;
	}
</style>
