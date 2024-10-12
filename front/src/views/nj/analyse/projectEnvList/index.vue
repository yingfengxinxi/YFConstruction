<template>
	<div class="con_box">
		<div class="environment_box" v-for="(item, index) in dataList">
			<div class="main">
				<div class="left">
					<div :id="'echarts'+index" class="echarts"></div>
					<div class="grade">{{item.levelName?item.levelName:'--'}}</div>
					<div class="date">{{item.monitorTime?item.monitorTime:'--'}}</div>
				</div>
				<div class="right">
					<div class="small_box pm10">
						<div class="value">{{item.pm10Text?item.pm10Text:'--'}}{{item.pm10Unit?item.pm10Unit:''}}</div>
						<div class="title">PM10</div>
					</div>
					<div class="small_box tsp">
						<div class="value">{{item.tsp?item.tsp:'--'}}{{item.tspUnit?item.tspUnit:''}}</div>
						<div class="title">TSP</div>
					</div>
					<div class="small_box noise">
						<div class="value">{{item.noise?item.noise:'--'}}{{item.noiseUnit?item.noiseUnit:''}}</div>
						<div class="title">噪音</div>
					</div>
					<div class="small_box wind_speed">
						<div class="value">{{item.wind?item.wind:'--'}}{{item.windUnit?item.windUnit:''}}</div>
						<div class="title">气压</div>
					</div>
					<div class="small_box temp">
						<div class="value">{{item.temperature?item.temperature:'--'}}{{item.tempUnit?item.tempUnit:''}}</div>
						<div class="title">温度</div>
					</div>
					<div class="small_box humidness">
						<div class="value">{{item.humidness?item.humidness:'--'}}{{item.humidnessUnit?item.humidnessUnit:''}}</div>
						<div class="title">湿度</div>
					</div>
				</div>
			</div>
			<el-tooltip :content="item.assetName" placement="top" effect="light">
			<div class="minor">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectEnvList/img1.png"/>
				<div class="proName">
					{{item.assetName}} ：<span v-if="item.online == 0" style="color:red;">离线</span>
				  <span v-else-if="item.online == 1" >在线</span>
				  <span v-else>--</span>
				</div>
			</div>
			</el-tooltip>
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import { mapGetters } from 'vuex'

	import {selectOverViewList} from '../api/projectEnvList'

	export default{
		data(){
			return{
				dataList:[],
				timer:null, //定时器名称
			}
		},
		computed: {
		  ...mapGetters(['supply','pageDataReflushTime']),
		},
		created(){
			// console.log(this.pageDataReflushTime)
			this.initData()
		},
		mounted(){
			this.remsize()
			this.gauge()
		},
		watch:{
			dataList: function (val) {

				this.$nextTick(() => {
					this.gauge()
				})
			}
		},
		beforeDestroy(){
			clearInterval(this.timer);　　// 清除定时器
			this.timer = null;
		},
		methods:{
			initData() {
				this.getlist()
				let _this=this
				this.timer = setInterval(
				        function()  {
				            _this.getlist()
				          }, this.pageDataReflushTime
				      )
			},
			getlist() {
				selectOverViewList().then(res => {
					this.dataList = res.data
				})
			},
			gauge(){//报警类型占比

				for (let i=0; i<this.dataList.length; i++) {
					let id = 'echarts' + i
					let data = this.dataList[i].pm25
					let myChart = echarts.init(document.getElementById(id));
					let option = {
						tooltip: {
							formatter: "{a} <br/>{b} : {c}%"
						},
						series: [{
							name: '业务指标',
							type: 'gauge',
							detail: {
								formatter: '{value}',
								 textStyle: {
									fontSize: 12,
									color: '#000'
								}
							},
							data: [{
								value: data,
								name: 'PM2.5'
							}],
							title: { //标题
								show: true,
								offsetCenter: [0, -15], // x, y，单位px
								textStyle: {
									color: "#000",
									fontSize: 8, //表盘上的标题文字大小
									fontWeight: 400,
								}
							},
							pointer: {
								show: true,
								length: '80%',
								radius: '20%',
								width: 1,
							},
							axisLabel: {
								show: true,
								color: '#fff',
								distance: 5,
								fontSize: 10,
							}, //刻度标签
							axisTick: {
								show: true,
								splitNumber: 7,
								lineStyle: {
									color: "#fff", //用颜色渐变函数不起作用
									width: 1,
								},
								length: 4
							}, //刻度样式
							splitLine: {
								show: true,
								length: 5,
								lineStyle: {
									color: "#fff", //用颜色渐变函数不起作用
									width: 1,
								},

							}, //分隔线样式
							axisLine: {
								show: true,
								lineStyle: {
									color: [
										[1, new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
												offset: 0.1,
												color: "#FFC600"
											},
											{
												offset: 0.6,
												color: "#30D27C"
											},
											{
												offset: 1,
												color: "#0B95FF"
											}
										])]
									],
									width:10
								}
							}

						}]
					}
					myChart.setOption(option)
				}
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
		}
	}
</script>

<style scoped>
	.con_box{
		height: 100%;
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
	}
	.environment_box{
		width: 3.93rem;
		height: 31.2%;
		margin-top: 0.2rem;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		margin-right: 0.13rem;
	}
	.con_box>:nth-child(4n+1){
		margin-left: 0.13rem;
	}
	.con_box>:nth-child(4n){
		margin-right: 0rem;
	}
	.main{
		height: 82.71%;
		width: 100%;
		display: flex;
	}
	.left{
		height: 89.34%;
		width: 1.76rem;
	}
	.echarts{
		height: 75.22%;
		width: 100%;
	}
	.grade{
		width: 100%;
		font-size: 0.18rem;
		font-weight: 500;
		text-align: center;
	}
	.date{
		height: 0.2rem;
		width: 100%;
		margin-top: 0.09rem;
		font-size: 0.14rem;
		font-weight: 500;
		line-height: 0.20rem;
		text-align: center;
	}
	.right{
		height: 94%;
		width: 2.02rem;
		display: flex;
		padding-top:0.1rem ;
		flex-wrap: wrap;
		align-content: space-evenly;
	}
	.small_box{
		height: 22.93%;
		width: 0.96rem;
		margin-right: 0.05rem;
		display: flex;
		flex-wrap: wrap;
		align-content: space-around;
	}
	.pm10{
		background: rgba(86, 148, 254, 0.16);
		border-radius: 2px;
	}
	.pm10 .value{
		color: rgba(86, 148, 254, 1);
	}
	.wind_speed{
		background: rgba(136, 255, 130, 0.12);
		border-radius: 2px;
	}
	.wind_speed .value{
		color: rgba(87, 203, 145, 1);
	}
	.tsp{
		background: rgba(109, 201, 251, 0.16);
	}
	.tsp .value{
		color: rgba(86, 199, 254, 1);
	}
	.noise{
		background: rgba(31, 216, 181, 0.16);
		border-radius: 2px;
	}
	.noise .value{
		color: rgba(87, 203, 145, 1);
	}
	.temp{
		background: rgba(250, 195, 20, 0.16);
		border-radius: 2px;
	}
	.temp .value{
		color: rgba(250, 173, 20, 1);
	}
	.humidness{
		background: rgba(255, 154, 70, 0.16);
		border-radius: 2px;
	}
	.humidness .value{
		color: rgba(255, 135, 83, 1);
	}
	.value{
		font-size: 0.16rem;
		font-weight: 500;
		width: 100%;
		text-align: center;
	}
	.title{
		font-size: 0.12rem;
		font-weight: 500;
		width: 100%;
		text-align: center;
		color: rgba(102, 102, 102, 1);
	}
	.minor{
		height: 16.94%;
		width: 3.62rem;
		border-top: 1px solid rgba(245, 245, 245, 1);
		margin: 0 auto;
		display: flex;
	}
	.minor img{
		width: 0.22rem;
		height: 0.22rem;
		margin-top: 0.15rem;
		margin-left: 0.12rem;
	}
	.proName{
		height: 0.22rem;
		margin-top: 0.15rem;
		margin-left: 0.10rem;
		font-size: 0.16rem;
		font-weight: 500;
		color: #5694FE;
		line-height: 0.22rem;
		width: 3rem;
		white-space:nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

</style>
