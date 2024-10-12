<template>
	<div class="content_box">
		<div class="select_pro">
			<el-select v-model="value" placeholder="请选择" class='project' @change='changeproid'>
				<el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
				</el-option>
			</el-select>
		</div>
		<div class="police_num">
			<div class="model_title">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
				<div>报警数量</div>
			</div>
			<div class="num_con">
				<div class="pol_box police_bg1">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/police_img1.png" />
					<div class="dayOrNum" @click="alarmOpen('1')">
						<div class="pol_day">今日</div>
						<div class="pol_num">{{alarmsStatistics.dayNum}}</div>
					</div>
				</div>
				<div class="pol_box police_bg2">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/police_img2.png" />
					<div class="dayOrNum" @click="alarmOpen('2')">
						<div class="pol_day">本周</div>
						<div class="pol_num">{{alarmsStatistics.weekNum}}</div>
					</div>
				</div>
				<div class="pol_box police_bg3">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/police_img3.png" />
					<div class="dayOrNum" @click="alarmOpen('3')">
						<div class="pol_day">本月</div>
						<div class="pol_num">{{alarmsStatistics.monthNum}}</div>
					</div>
				</div>
				<div class="pol_box police_bg4">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/police_img4.png" />
					<div class="dayOrNum" @click="alarmOpen('4')">
						<div class="pol_day">本年</div>
						<div class="pol_num">{{alarmsStatistics.yearNum}}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="police_trend_box">
			<div class="model_title">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
				<div>报警趋势</div>
			</div>
			<div class="tab_bar">
				<div class="tab_con" @click="trendClick(1)" :class="trendActive==1?'activeTrend':''">7日</div>
				<div class="tab_con" @click="trendClick(2)" :class="trendActive==2?'activeTrend':''">30日</div>
			</div>
			<div id="trend">

			</div>
		</div>
			<div class="police_noise_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>{{noiseTitle}}</div>
				</div>
				<div class="tab_bar2">
					<div class="tab_con2" @click="noiseClick(4)" :class="noiseActive==4?'activeTrend':''">24小时</div>
					<div class="tab_con2" @click="noiseClick(2)" :class="noiseActive==2?'activeTrend':''">30天</div>
				</div>
				<div id="noise">

				</div>
			</div>
			<div class="time_price_area">

				<div class="timer">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/time2.png" />
					<div class="timer_con">
						<div class="con_top">工程面积</div>
						<div class="con_bottom">{{timeArea.totalArea==''?' -- -- -- ':timeArea.totalArea}}平</div>
					</div>
				</div>
				<div class="timer_line"></div>
				<div class="timer">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/timer3.png" />
					<div class="timer_con">
						<div class="con_top">工程造价</div>
						<div class="con_bottom">{{timeArea.totalCost==''?' -- -- -- ':timeArea.totalCost}}万元</div>
					</div>
				</div>
				<div class="timer_line"></div>
				<div class="timer">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/timer1.png" />
					<div class="timer_con">
						<div class="con_top">计划竣工时间</div>
						<div class="con_bottom">{{timeArea.plannedCompletionDate==''?' -- -- -- ':timeArea.plannedCompletionDate}}</div>
					</div>
				</div>
			</div>
			<div class="pm_box">
				<div class="level1">
					<div class="cirebox">
						<div class="crie crie1"></div>
						<div class="crieCon">一级</div>
					</div>
					<div class="cirebox">
						<div class="crie crie2"></div>
						<div class="crieCon">二级</div>
					</div>
					<div class="cirebox">
						<div class="crie crie3"></div>
						<div class="crieCon">三级</div>
					</div>
				</div>
				<div class="level2">
					<div class="cirebox">
						<div class="crie crie1"></div>
						<div class="crieCon">一级</div>
					</div>
					<div class="cirebox">
						<div class="crie crie2"></div>
						<div class="crieCon">二级</div>
					</div>
					<div class="cirebox">
						<div class="crie crie3"></div>
						<div class="crieCon">三级</div>
					</div>
				</div>
				<div id="pmtwo"></div>
				<div id="pm_t"></div>
				<div id="pmten"></div>
				<div class="pmimg_box">
					<div>0</div>
					<div>35</div>
					<div>75</div>
					<div>115</div>
					<div>150</div>
					<div>250</div>
					<div>300</div>
				</div>
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="pmimg" src="../../../../assets/img/projectAnalyse/pmimg.png" />
				<div class="pmimg_quality">
					<div>优</div>
					<div>良</div>
					<div>轻度污染</div>
					<div>中度污染</div>
					<div>重度污染</div>
					<div>严重污染</div>
				</div>
				<div class="weekair">近7日空气质量</div>
				<div class="week_air_box">
					<div class="week_list" v-for='(item,index) in weeklist' :key="index">
						<div class="week_time">{{item.monitorDate}}</div>
						<div class="week_val">
							<span v-if="item.levelName=='优'" class="qy1"></span>
							<span v-else-if="item.levelName=='良'" class="qy2"></span>
							<span v-else-if="item.levelName=='轻度污染'" class="qy3"></span>
							<span v-else-if="item.levelName=='中度污染'" class="qy4"></span>
							<span v-else-if="item.levelName=='重度污染'" class="qy5"></span>
							<span v-else class="qy6"></span>
							{{item.levelName}}
						</div>
					</div>
				</div>
			</div>
			<div class="air_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>{{airTitle}}</div>
				</div>
				<div class="tab_bar2">
					<div class="tab_con2" @click="airClick(4)" :class="airActive==4?'activeTrend':''">24小时</div>
					<div class="tab_con2" @click="airClick(2)" :class="airActive==2?'activeTrend':''">30天</div>
				</div>
				<div id="air">

				</div>
			</div>
			<div class="temperature_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>{{temperatureTitle}}</div>
				</div>
				<div class="tab_bar2">
					<div class="tab_con2" @click="temperatureClick(4)" :class="temperatureActive==4?'activeTrend':''">24小时</div>
					<div class="tab_con2" @click="temperatureClick(2)" :class="temperatureActive==2?'activeTrend':''">30天</div>
				</div>
				<div id="temperature">

				</div>
			</div>
			<div class="police_type">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>历史空气质量分析</div>
				</div>
				<div id="type">

				</div>
			</div>
				<div class="spray_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>喷淋启停记录</div>
					</div>
					<div class="spray">
						<div class="ifspray" v-if="spray.length==0">暂无数据</div>
						<div class="spray_list" v-for="(item,index) in spray" :key="index">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.operationType==1" src="../../../../assets/img/projectAnalyse/openicon.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else src="../../../../assets/img/projectAnalyse/closeicon.png" />
							<div class="spray_name" @click="sprayOpen">{{item.assetName}}</div>
							<div class="spray_time">{{item.operationTime}}</div>
							<div class="spray_value">{{item.value}}</div>
						</div>
					</div>
				</div>
	</div>
</template>

<script>
import {
	projectList,
	alarmsStatistics,
	getStartStop,
	latelyAirQualityAnalysis,
	realTimeData,
	hisAirQualityAnalysis,
	todayAirQualityAnalysis,
	alarmsTrend,
	getTimeArea,
	selectAlertTrend
} from '../api/projectAnalyse.js'
import * as echarts from 'echarts';
import { queryMenuListByPath } from '@/api/admin/menu'
import { getDate, getMonday, getMonth, getYear } from './dateFormatUtil.js'
export default {
	data () {
		return {
			trendActive: 1,
			weeklist: [],
			spray: [],
			options: [],
			value: '',
			projectId: '',
			alarmsStatistics: {},
			timeArea: {},
			noiseActive: 4,
			noiseTitle: '',
			airActive: 4,
			airTitle: '',
			temperatureActive: 4,
			temperatureTitle: '',
		}
	},
	mounted () {
		let params = {
			projectState: '2'
		}
		projectList(params).then(data => {
			this.options = data.data
			this.projectId = this.options[0].id
			this.value = this.options[0].id

			this.changeproid(this.value)
		}).catch(() => { })





		this.remsize()
	},
	methods: {
		sprayOpen () {
			this.goToUrl('lifterHistory', {projectId: this.projectId})
		},
		alarmOpen (dateType) {
			let startDate = '';
			let endDate = '';
			switch (dateType) {
				case '1'://今天
					// var now = new Date();
					// var year = now.getFullYear(); //得到年份
					// var month = now.getMonth() + 1;//得到月份
					// var date = now.getDate();//得到日期
					// if (month < 10) month = "0" + month;
					// if (date < 10) date = "0" + date;
					// startDate = year + "-" + month + "-" + date + ' 00:00:00'
					// endDate = year + "-" + month + "-" + date + ' 23:59:59'
					startDate = getDate() + ' 00:00:00'
					endDate = getDate() + ' 23:59:59'
					break;
				case '2'://本周
					startDate = getMonday('s', 0) + ' 00:00:00'
					endDate = getMonday('e', 0) + ' 23:59:59'
					break;
				case '3': //本月
					startDate = getMonth('s', 0) + ' 00:00:00'
					endDate = getMonth('e', 0) + ' 23:59:59'
					break;
				case '4': //本年
					startDate = getYear('s', 0) + ' 00:00:00'
					endDate = getYear('e', 0) + ' 23:59:59'
					break;
				default: //昨天
					break;
			}

			console.log("开始日期=" + startDate + "  结束日期=" + endDate);


			this.goToUrl('envAlert', { projectId: this.projectId, alertType: '1', startDate: startDate, endDate: endDate })
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
					comname: item.path + '/' + item.path,
					name: item.menuId
				}
				if (JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
					this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
				}
				this.$store.state.app.activeclass = activeclass.comname
				this.$router.push({ name: activeclass.name, query: queryParams })
			})
		},

		changeproid (val) {
			this.projectId = val;
			alarmsStatistics({//报警数量
				projectId: this.projectId
			}).then(data => {
				this.alarmsStatistics = data.data
			}).catch(() => { })

			getStartStop({//喷淋启停记录
				projectId: this.projectId
			}).then(data => {
				this.spray = data.data
			}).catch(() => { })

			latelyAirQualityAnalysis({//近7日空气质量
				projectId: this.projectId
			}).then(data => {
				this.weeklist = data.data
			}).catch(() => { })

			this.noiseClick(4)//24小时噪音
			
			this.airClick(4)//24小时空气
			
			this.temperatureClick(4)//24小时温度湿度

			hisAirQualityAnalysis({//历史空气质量分析
				projectId: this.projectId,
			}).then(data => {
				this.type(data.data)
			}).catch(() => { })

			todayAirQualityAnalysis({//PM2.5/10
				projectId: this.projectId,
			}).then(data => {
				this.pmten(data.data)
				this.pm_t(data.data)
				this.pmtwo(data.data)
			}).catch(() => { })

			selectAlertTrend({//报警趋势
				projectId: this.projectId,
				type: this.trendActive
			}).then(data => {
				this.trend(data.data)
			}).catch(() => { })

			getTimeArea(//时间面积造价
				this.projectId).then(data => {
					this.timeArea = data.data
				}).catch(() => { })
		},
		noiseClick(index) {
			this.noiseActive = index
			if (this.noiseActive == 2) {
				this.noiseTitle = '30天噪音数据'
			} else if (this.noiseActive == 4) {
				this.noiseTitle = '24小时噪音数据'
			}
			realTimeData({//24小时噪音
				projectId: this.projectId,
				type: 1,
				timeType:this.noiseActive
			}).then(data => {
				this.noise(data.data, this.noiseActive)
			}).catch(() => { })
		},
		airClick(index) {
			this.airActive = index
			if (this.airActive == 2) {
				this.airTitle = '30天空气质量数据'
			} else if (this.airActive == 4) {
				this.airTitle = '24小时空气质量数据'
			}
			realTimeData({//24小时噪音
				projectId: this.projectId,
				type: 2,
				timeType:this.airActive
			}).then(data => {
				this.air(data.data, this.airActive)
			}).catch(() => { })
		},
		temperatureClick(index) {
			this.temperatureActive = index
			if (this.temperatureActive == 2) {
				this.temperatureTitle = '30温湿度数据'
			} else if (this.temperatureActive == 4) {
				this.temperatureTitle = '24小时温湿度数据'
			}
			realTimeData({//24小时噪音
				projectId: this.projectId,
				type: 3,
				timeType:this.temperatureActive
			}).then(data => {
				this.temperature(data.data, this.temperatureActive)
			}).catch(() => { })
		},
		trendClick (index) {
			this.trendActive = index
			selectAlertTrend({//报警趋势
				projectId: this.projectId,
				type: this.trendActive
			}).then(data => {
				this.trend(data.data)
			}).catch(() => { })
		},
		pmten (data) {//tps
			let myChart = echarts.init(document.getElementById('pmten'));
			let option = {
				series: [{
					// 进度条
					type: 'gauge',
					splitNumber: 5,
					radius: '90%',
					min: 0,
					max: 500,
					pointer: {
						show: true,
						width: 7,
						length: '80%',
						itemStyle: {
							color: 'auto',
						},
					},
					axisLine: {
						show: true,
						lineStyle: {
							width: 15,
							color: [
								[0.24, '#32eef7'],
								[0.6, '#007bff'],
								[1, '#FF6061'],
							],

						},
					},
					axisLabel: {
						show: true,
						color: '#404040',
						// 刻度显示位置
						distance: 0,
						fontSize: 10,
					}, //刻度标签
					axisTick: {
						show: false,
					}, //刻度样式
					splitLine: {
						show: false,
						length: '20%',
						lineStyle: {
							color: '#404040',
							width: 2,
						},
					}, //分隔线样式
					detail: {
						show: true,
						offsetCenter: ['0%', '30%'],
						fontSize: '18px',
						color: '#404040'
					},
					title: {
						show: true,
						offsetCenter: [0, '100%'],
						color: '#404040'
					},
					data: [{
						value: data.TSPAVG,
						name: 'TSP',
					},],
				},
				{
					// 彩色部分
					type: 'gauge',
					splitNumber: 10,
					radius: '85%',
					min: 0,
					max: 45,
					pointer: {
						show: false,
						width: 7,
						length: '70%',
						color: 'auto',
					},
					axisLine: {
						show: true,
						lineStyle: {
							width: 10,
							color: [
								[0.3, 'transparent'],
								[0.7, 'transparent'],
								[1, 'transparent'],
							],
							borderColor: 'transparent',
							borderWidth: '1',
							shadowColor: 'rgba(0,138,255,0.8)',
							shadowBlur: 20,
							shadowOffsetX: 4,
							shadowOffsetY: 4
						},
					},
					axisLabel: {
						show: false,
					}, //刻度标签
					axisTick: {
						show: true,
						splitNumber: 5,
						lineStyle: {
							color: '#EBF3FE',
							width: 1,
						},
					},
					//刻度样式
					splitLine: {
						show: true,
						length: '-6%',
						lineStyle: {
							color: '#4b95f3',
							width: 2,
							shadowColor: 'rgba(0,138,255,0.8)',
							shadowBlur: 20,
							shadowOffsetX: 4,
							shadowOffsetY: 4
						},
					},
					detail: {
						show: false,

					},
					title: {
						show: true,
						offsetCenter: ['0%', '60%'],
						fontSize: 10,
						color: '#404040'
					},
					data: [{
						value: 60,
						name: 'μg/m³',
					},],
				},

				],
			};
			myChart.setOption(option)
		},
		pm_t (data) {//PM2.5
			let myChart = echarts.init(document.getElementById('pm_t'));
			let option = {
				series: [{
					name: '刻度',
					type: 'gauge',
					radius: '78%',
					min: 0,
					max: 300,
					startAngle: 220,
					endAngle: -40,
					splitNumber: 6,
					axisLine: {
						show: true,
						lineStyle: {
							width: 1,
							color: [
								[1, 'rgba(0,0,0,0)']
							]
						}
					}, //仪表盘轴线
					axisLabel: {
						show: true,
						color: '#000',
						distance: 0
					}, //刻度标签。
					axisTick: {
						show: true,
						splitNumber: 15,
						lineStyle: {
							color: '#3699FF',
							width: 1
						},
						length: 8
					}, //刻度样式
					splitLine: {
						show: true,
						length: 20,
						lineStyle: {
							color: '#3699FF'
						}
					}, //分隔线样式
					detail: {
						show: false
					},
					pointer: {
						show: false
					},
					title: {
						show: true,
						offsetCenter: [0, '10%'],
						color: '#404040'
					},
					data: [{
						value: data.PM25AVG,
						name: 'μg/m³'
					}]
				},
				{
					name: '内圈小',
					type: 'gauge',
					radius: '95%',
					startAngle: 220,
					endAngle: -40,
					axisLine: { // 坐标轴线
						lineStyle: { // 属性lineStyle控制线条样式
							color: [
								[
									1, new echarts.graphic.LinearGradient(
										0, 1, 0, 0, [{
											offset: 0,
											color: 'rgba(215,219,244,0.3)',
										}, {
											offset: 0.3,
											color: 'rgba(215,219,244,0.3)',
										},
										{
											offset: 1,
											color: 'rgba(215,219,244,0.3)',
										}
										]
									)
								],
							],
							width: 60
						}

					},
					splitLine: { //分隔线样式
						show: false,
					},
					axisLabel: { //刻度标签
						show: false,
					},
					pointer: {
						show: false,
					},
					axisTick: { //刻度样式
						show: false,
					},
					detail: {
						show: false
					},
					title: {
						show: true,
						offsetCenter: [0, '78%'],
						color: '#404040'
					},
					data: [{
						value: data.PM25AVG,
						name: 'PM2.5'
					}]
				},
				{
					type: 'gauge',
					radius: '90%',
					center: ['50%', '50%'],
					startAngle: 220,
					endAngle: -40,
					axisLine: {
						lineStyle: {
							color: [
								[0.116, '#00d1a0'],
								[0.25, '#39d2ff'],
								[0.383, '#4e74e6'],
								[0.5, '#f3bc40'],
								[0.83, '#f5863e'],
								[1, "#fb466b"]
							],
							width: 15
						}
					},
					//分隔线样式。
					splitLine: {
						show: false,
					},
					axisLabel: {
						show: false
					},
					axisTick: {
						show: false
					},
					pointer: {
						show: false
					},
					//仪表盘详情，用于显示数据。
					detail: {
						show: true,
						splitNumber: 15,
						offsetCenter: [0, '-10%'],
						color: '#ddd',
						formatter: function (params) {
							return params
						},
						textStyle: {
							color: '#404040',
							fontSize: 30
						}
					},
					title: {
						show: true,
						offsetCenter: [0, '30%'],
						color: '#08b5d6'
					},
					data: [{
						value: data.PM25AVG,
						name: data.PM25Level
					}]
				}
				]
			};
			myChart.setOption(option)
		},
		pmtwo (data) { //pm10
			let myChart = echarts.init(document.getElementById('pmtwo'));
			let option = {
				series: [{
					// 进度条
					type: 'gauge',
					splitNumber: 5,
					radius: '90%',
					min: 0,
					max: 250,
					pointer: {
						show: true,
						width: 7,
						length: '80%',
						itemStyle: {
							color: 'auto',
						},
					},
					axisLine: {
						show: true,
						lineStyle: {
							width: 15,
							color: [
								[0.2, '#32eef7'],
								[0.6, '#007bff'],
								[1, '#FF6061'],
							],
						},
					},
					axisLabel: {
						show: true,
						color: '#404040',
						// 刻度显示位置
						distance: 0,
						fontSize: 10,
					}, //刻度标签
					axisTick: {
						show: false,
					}, //刻度样式
					splitLine: {
						show: false,
						length: '20%',
						lineStyle: {
							color: '#3699FF',
							width: 2,
						},
					}, //分隔线样式
					detail: {
						show: true,
						offsetCenter: ['0%', '30%'],
						fontSize: '18px',
						color: '#404040'
					},
					title: {
						show: true,
						offsetCenter: [0, '100%'],
						color: '#404040'
					},
					data: [{
						value: data.PM10AVG,
						name: 'PM10',
					},],
				},
				{
					// 彩色部分
					type: 'gauge',
					splitNumber: 10,
					radius: '85%',
					min: 0,
					max: 45,
					pointer: {
						show: false,
						width: 7,
						length: '70%',
						color: 'auto',
					},
					axisLine: {
						show: true,
						lineStyle: {
							width: 10,
							color: [
								[0.3, 'transparent'],
								[0.7, 'transparent'],
								[1, 'transparent'],
							],
							borderColor: 'transparent',
							borderWidth: '1',
							shadowColor: 'rgba(0,138,255,0.8)',
							shadowBlur: 20,
							shadowOffsetX: 4,
							shadowOffsetY: 4
						},
					},
					axisLabel: {
						show: false,
					}, //刻度标签
					axisTick: {
						show: true,
						splitNumber: 5,
						lineStyle: {
							color: '#EBF3FE',
							width: 1,
						},
					},
					//刻度样式
					splitLine: {
						show: true,
						length: '-6%',
						lineStyle: {
							color: '#4b95f3',
							width: 2,
							shadowColor: 'rgba(0,138,255,0.8)',
							shadowBlur: 20,
							shadowOffsetX: 4,
							shadowOffsetY: 4
						},
					},
					detail: {
						show: false,
					},
					title: {
						show: true,
						show: true,
						offsetCenter: ['0%', '60%'],
						fontSize: 10,
						color: '#404040'
					},
					data: [{
						value: 60,
						name: 'μg/m³',
					},],
				},
				],
			};
			myChart.setOption(option)
		},
		type (data) { //历史空气数据分析
			let seriesData = []
			for (let i = 0; i < data.length; i++) {
				seriesData.push({
					name: data[i].levelName,
					value: data[i].num
				})
			}
			let myChart = echarts.init(document.getElementById('type'));
			let option = {
				tooltip: {
					trigger: 'item',
					transitionDuration: 0, //防止页面抖动
				},
				color: ['#00D1A0 ', '#39D2FF ', '#4E74E6 ', '#F3BC40 ', '#F5863E ', '#FB466B', '#6cf371', '#f5835f', '#6c2be7', '#f3456d'],
				legend: {
					icon: "circle",
					right: '10',
					orient: 'vertical',
					bottom: 'center',
					itemWidth: 10,
					itemHeight: 10,
					itemGap: 5,
					textStyle: {
						color: "#000",
						fontSize: 12
					},
				},
				series: [{
					type: "pie",
					radius: ["35%", "55%"],
					center: ["40%", "50%"],
					hoverAnimation: true,
					z: 10,
					itemStyle: {
						normal: {
							borderWidth: 5,
							borderColor: "#D7EBFF"
						}
					},
					label: {
						show: false,
						color: '#000'
					},
					data: seriesData
				},]
			}
			myChart.setOption(option)
		},
		temperature (data, timeType) { //24小时温湿度数据
			let xAxisData = []
			let temperatureCount = []
			let humidityCount = []
			let unit = ''
			if (timeType == 2) {
				unit = ''
			} else if (timeType == 4) {
				unit = '时'
			}
			for (let i = 0; i < data.length; i++) {
				xAxisData.push(data[i].hourNumber + unit)
				temperatureCount.push(data[i].temperatureCount)
				humidityCount.push(data[i].humidityCount)
			}
			let myChart = echarts.init(document.getElementById('temperature'));
			let option = {
				tooltip: {
					trigger: "axis",
					transitionDuration: 0, //防止页面抖动
				},

				grid: {
					right: "18%",
					left: "15%",
					top: '19%',
					bottom: '20%'
				},
				legend: {
					show:false,
					icon: "circle",
					top: 0,
					data: ['温度', '湿度'],
					right: 30,
					itemWidth: 10,
					itemHeight: 10,
					textStyle: {
						color: "#000",
						fontSize: 10
					},
				},
				xAxis: {
					data: xAxisData,
					type: 'category',
					axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

					axisLabel: {
						color: '#000',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 5 // 每行能显示的字的个数
							var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
							/**
							 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
							 */
							// 条件等同于rowNumber>1
							if (paramsNameNumber > provideNumber) {
								/** 循环每一行,p表示行 */
								for (var p = 0; p < rowNumber; p++) {
									var tempStr = '' // 表示每一次截取的字符串
									var start = p * provideNumber // 开始截取的位置
									var end = start + provideNumber // 结束截取的位置
									// 此处特殊处理最后一行的索引值
									if (p === rowNumber - 1) {
										// 最后一次不换行
										tempStr = params.substring(start, paramsNameNumber)
									} else {
										// 每一次拼接字符串并换行
										tempStr = params.substring(start, end) + '\n'
									}
									newParamsName += tempStr // 最终拼成的字符串
								}
							} else {
								// 将旧标签的值赋给新标签
								newParamsName = params
							}
							return newParamsName
						}

					},
					splitLine: {
						show: false,
					},

				},
				yAxis: [{
					name: '（温度）',
					nameTextStyle: {
						color: "#000",
					},
					type: 'value',
					splitLine: {
						show: true,
						lineStyle: {
							color: '#000',
							opacity: 0.2
						}
					},
					boundaryGap: true,
					axisLine: {
						show: false,
						color: 'A3E9FC'
					},
					axisLabel: {
						show: true,
						margin: 10,
						formatter: '{value} ℃',						
						textStyle: {
							color: '#000'
						}
					},
					axisTick: {
						show: false
					}
				},
				{
					name: '（湿度）',
					nameTextStyle: {
						color: "#000",
					},
					type: 'value',
					splitLine: {
						show: false,
						lineStyle: {
							color: '#000',
							opacity: 0.2
						}
					},
					boundaryGap: true,
					axisLine: {
						show: false,
						color: 'A3E9FC'
					},
					axisLabel: {
						show: true,
						formatter: '{value} %',
						margin: 10,
						textStyle: {
							color: '#000'
						}
					},
					axisTick: {
						show: false
					}
				}],
				series: [{
					name: '温度',
					type: 'line',
					smooth: true,
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: '#A89FFF'
						},
						{
							offset: 0.8,
							color: 'rgba(255, 255, 255, 0)'
						}
						])
					},
					data: temperatureCount,
					lineStyle: {
						normal: {
							color: "#A89FFF",
						},
					},
					itemStyle: {
						color: "#A89FFF",
						borderColor: "#A89FFF",
						borderWidth: 2,
					},
				}, {
					name: '湿度',
					type: 'line',
					smooth: true,
					yAxisIndex: 1,
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: 'rgba(250, 173, 20, 1)'
						},
						{
							offset: 0.8,
							color: 'rgba(255, 255, 255, 0)'
						}
						])
					},
					data: humidityCount,
					lineStyle: {
						normal: {
							color: "rgba(250, 173, 20, 1)",
						},
					},
					itemStyle: {
						color: "rgba(250, 173, 20, 1)",
						borderColor: "rgba(250, 173, 20, 1) ",
						borderWidth: 2,
					},
				}]
			};
			myChart.setOption(option)
		},
		air (data, timeType) { //24小时空气质量数据
			let xAxisData = []
			let PM2Count = []
			let PM10Count = []
			let unit = ''
			if (timeType == 2) {
				unit = ''
			} else if (timeType == 4) {
				unit = '时'
			}
			for (let i = 0; i < data.length; i++) {
				xAxisData.push(data[i].hourNumber + unit)
				PM2Count.push(data[i].PM2Count)
				PM10Count.push(data[i].PM10Count)
			}
			let myChart = echarts.init(document.getElementById('air'));
			let option = {
				tooltip: {
					trigger: "axis",
					transitionDuration: 0, //防止页面抖动
				},

				grid: {
					right: "10%",
					left: "10%",
					top: '19%',
					bottom: '20%'
				},
				legend: {
					show:false,
					icon: "circle",
					right: 30,
					data: ['PM2.5', 'PM10'],
					top: 10,
					itemWidth: 10,
					itemHeight: 10,
					textStyle: {
						color: "#000",
						fontSize: 10
					},
				},
				xAxis: {
					data: xAxisData,
					type: 'category',
					axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

					axisLabel: {
						color: '#000',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 5 // 每行能显示的字的个数
							var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
							/**
							 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
							 */
							// 条件等同于rowNumber>1
							if (paramsNameNumber > provideNumber) {
								/** 循环每一行,p表示行 */
								for (var p = 0; p < rowNumber; p++) {
									var tempStr = '' // 表示每一次截取的字符串
									var start = p * provideNumber // 开始截取的位置
									var end = start + provideNumber // 结束截取的位置
									// 此处特殊处理最后一行的索引值
									if (p === rowNumber - 1) {
										// 最后一次不换行
										tempStr = params.substring(start, paramsNameNumber)
									} else {
										// 每一次拼接字符串并换行
										tempStr = params.substring(start, end) + '\n'
									}
									newParamsName += tempStr // 最终拼成的字符串
								}
							} else {
								// 将旧标签的值赋给新标签
								newParamsName = params
							}
							return newParamsName
						}

					},
					splitLine: {
						show: false,
					},

				},
				yAxis: {
					name: '(μg/m³)',
					nameTextStyle: {
						color: "#000",
					},
					type: 'value',
					splitLine: {
						show: true,
						lineStyle: {
							color: '#000',
							opacity: 0.2
						}
					},
					boundaryGap: true,
					axisLine: {
						show: false,
						color: 'A3E9FC'
					},
					axisLabel: {
						show: true,
						margin: 10,
						textStyle: {
							color: '#000'
						}
					},
					axisTick: {
						show: false
					}
				},
				series: [{
					name: 'PM2.5',
					type: 'line',
					smooth: true,
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: '#30BF78'
						},
						{
							offset: 0.8,
							color: 'rgba(255, 255, 255, 0)'
						}
						])
					},
					data: PM2Count,
					lineStyle: {
						normal: {
							color: "#30BF78",
						},
					},
					itemStyle: {
						color: "#30BF78",
						borderColor: "#30BF78",
						borderWidth: 2,
					},
				}, {
					name: 'PM10',
					type: 'bar',					
					data: PM10Count,					
					itemStyle: {
						color: function(params){
							if(params.value<=50){
								return '#32eef7'
							}else if(50<params.value&&params.value<150){								
								return '#007bff'
							}else if(params.value>=150){
								return '#FF6061'
							}
						},
						
					},
				}]
			};
			myChart.setOption(option)
		},
		//24小时噪音数量
		noise (data, timeType) {
			let xAxisData = []
			let seriesData = []
			let unit = ''
			if (timeType == 2) {
				unit = ''
			} else if (timeType == 4) {
				unit = '时'
			}
			for (let i = 0; i < data.length; i++) {
				xAxisData.push(data[i].hourNumber + unit)
				seriesData.push(data[i].dateCount)
			}
			let myChart = echarts.init(document.getElementById('noise'));
			let option = {
				tooltip: {
					trigger: "axis",
					transitionDuration: 0, //防止页面抖动
				},
				legend: {
					top: 8,
					right: '30',
					textStyle: {
						color: "#000",
						fontSize: 10
					},
					itemWidth: 10,
					itemHeight: 10,

				},
				grid: {
					right: "10%",
					left: "15%",
					top: '18%',
					bottom: '20%'
				},
				xAxis: {

					data: xAxisData,
					type: 'category',
					axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

					axisLabel: {
						color: '#000',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 5 // 每行能显示的字的个数
							var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
							/**
							 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
							 */
							// 条件等同于rowNumber>1
							if (paramsNameNumber > provideNumber) {
								/** 循环每一行,p表示行 */
								for (var p = 0; p < rowNumber; p++) {
									var tempStr = '' // 表示每一次截取的字符串
									var start = p * provideNumber // 开始截取的位置
									var end = start + provideNumber // 结束截取的位置
									// 此处特殊处理最后一行的索引值
									if (p === rowNumber - 1) {
										// 最后一次不换行
										tempStr = params.substring(start, paramsNameNumber)
									} else {
										// 每一次拼接字符串并换行
										tempStr = params.substring(start, end) + '\n'
									}
									newParamsName += tempStr // 最终拼成的字符串
								}
							} else {
								// 将旧标签的值赋给新标签
								newParamsName = params
							}
							return newParamsName
						}

					},
					splitLine: {
						show: false,
					},

				},
				yAxis: {
					name: '(dB)',
					nameTextStyle: {
						color: "#000",
					},
					type: 'value',
					scale: true,
					splitLine: {
						show: true,
						lineStyle: {
							color: '#000',
							opacity: 0.2
						}
					},
					boundaryGap: true,
					axisLine: {
						show: false,
						color: 'A3E9FC'
					},
					axisLabel: {
						show: true,

						textStyle: {
							color: '#000'
						}
					},
					axisTick: {
						show: false
					}
				},
				series: [{
					name: '噪音',
					type: 'bar',
					//					barWidth: '10',
					itemStyle: {
						normal: {
							color: '#5B97F9',
						}
					},
					data: seriesData
				},]
			};
			myChart.setOption(option)
		},
		//报警趋势
		trend (data) {

			let xData = []
			let yData1 = []
			let yData2 = []

			data.forEach(item => {
				xData.push(item.monitorDate)
				yData1.push(item.earlyNumber)
				yData2.push(item.alertNumber)
			})

			let myChart = echarts.init(document.getElementById('trend'));
			let option = {
				tooltip: {
					trigger: "axis",
					transitionDuration: 0, //防止页面抖动
				},

				grid: {
					right: "15%",
					left: "10%",
					top: '10%',
					bottom: '20%'
				},
				xAxis: {
					data: xData,
					type: 'category',
					axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

					axisLabel: {
						color: '#000',
						rotate: '45',
							color: '#000',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 5 // 每行能显示的字的个数
							var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
							/**
							 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
							 */
							// 条件等同于rowNumber>1
							if (paramsNameNumber > provideNumber) {
								/** 循环每一行,p表示行 */
								for (var p = 0; p < rowNumber; p++) {
									var tempStr = '' // 表示每一次截取的字符串
									var start = p * provideNumber // 开始截取的位置
									var end = start + provideNumber // 结束截取的位置
									// 此处特殊处理最后一行的索引值
									if (p === rowNumber - 1) {
										// 最后一次不换行
										tempStr = params.substring(start, paramsNameNumber)
									} else {
										// 每一次拼接字符串并换行
										tempStr = params.substring(start, end) + '\n'
									}
									newParamsName += tempStr // 最终拼成的字符串
								}
							} else {
								// 将旧标签的值赋给新标签
								newParamsName = params
							}
							return newParamsName
						}

					},
					splitLine: {
						show: false,
					},

				},
				yAxis: {
					type: 'value',
					splitLine: {
						show: true,
						lineStyle: {
							color: '#000',
							opacity: 0.2
						}
					},
					boundaryGap: true,
					axisLine: {
						show: false,
						color: 'A3E9FC'
					},
					axisLabel: {
						show: true,
						margin: 10,
						textStyle: {
							color: '#000'
						}
					},
					axisTick: {
						show: false
					}
				},
				series: [{
					name: '预警数量',
					type: 'line',
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: 'rgba(100, 155, 248, 0.5)'
						},
						{
							offset: 0.8,
							color: 'rgba(100, 155, 248, 0.5)'
						}
						])
					},
					data: yData1,
					lineStyle: {
						normal: {
							color: "rgba(100, 155, 248, 0.5)",
						},
					},
					itemStyle: {
						color: "rgba(100, 155, 248, 0.5)",
						borderColor: "#649BF8",
						borderWidth: 2,
					},
				},
				{
					name: '报警数量',
					type: 'line',
					areaStyle: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: 'rgba(245, 125, 101, 0.5)'
						},
						{
							offset: 0.8,
							color: 'rgba(245, 125, 101, 0.5)'
						}
						])
					},
					data: yData2,
					lineStyle: {
						normal: {
							color: "rgba(245, 125, 101, 0.5)",
						},
					},
					itemStyle: {
						color: "rgba(245, 125, 101, 0.5)",
						borderColor: "#F57D65",
						borderWidth: 2,
					},
				}]
			};
			myChart.setOption(option)
		},
		remsize () {
			function setRemFontSize () {
				var remSize = window.innerWidth / 19.2;
				document.querySelector("html").style.fontSize = remSize + "px";
			}
			setRemFontSize();
			window.addEventListener("resize", function () {
				setTimeout(function () {
					setRemFontSize();
				}, 200)
			});
		},
	}
}
</script>

<style scoped>
.content_box {
	width: 100%;
  height: 100%;
  overflow: hidden;
 position: relative;
 
}

.left_box {
  height: 100%;
  width: 3.93rem;
}

.right_box {
  height: 100%;
  width: 12.16rem;
}

.select_pro {
  width: 3.93rem;
  height:6.79%;
  top: 2%;
  left: 1.19%;
  position: absolute;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 0.05rem;
  display: flex;
  align-items: center;
}

.project {
  width: 80%;
  margin: 0 auto;
}

.police_num {
  width: 3.93rem;
  height: 26.6%;
  position: absolute;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  left: 1.19%;
  top: 10.9%;
}

.model_title {
  height: 0.4rem;
  display: flex;
  align-items: center;
}

.model_title img {
  width: 0.16rem;
  height: 0.16rem;
  margin-left: 0.2rem;
  margin-right: 0.1rem;
}

.model_title div {
  font-size: 0.16rem;
  color: #000000;
}

.num_con {
  height: calc(100% - 0.5rem);
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  margin-top: 0.1rem;
}

.pol_box {
  height: 40%;
  width: 1.6rem;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
}

.pol_box img {
  height: 66%;
  width: 0.52rem;
}

.dayOrNum {
  height: 66%;
  display: flex;
  justify-content: space-around;
  flex-direction: column;
  text-align: center;
  width: 0.6rem;
}

.pol_day {
  font-size: 0.14rem;
  color: #666666;
}

.pol_num {
  font-size: 0.24rem;
  color: #619cff;
}

.police_bg1 {
  background: url(../../../../assets/img/projectAnalyse/police_bg1.png)
    no-repeat;
  background-size: 100% 100%;
}

.police_bg2 {
  background: url(../../../../assets/img/projectAnalyse/police_bg2.png)
    no-repeat;
  background-size: 100% 100%;
}

.police_bg3 {
  background: url(../../../../assets/img/projectAnalyse/police_bg3.png)
    no-repeat;
  background-size: 100% 100%;
}

.police_bg4 {
  background: url(../../../../assets/img/projectAnalyse/police_bg4.png)
    no-repeat;
  background-size: 100% 100%;
}

.police_trend_box {
  width: 3.93rem;
  height: 27.6%;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  margin-top: 0.1rem;
  position: absolute;
  top: 39%;
  left: 1.19%;
}

.tab_bar {
  position: absolute;
  right: 0.2rem;
  top: 0.1rem;
  width: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tab_con {
  width: 0.7rem;
  height: 24px;
  background: #e6f5ff;
  border-radius: 2px;
  border: 1px solid #c8e0f8;
  text-align: center;
  line-height: 24px;
  color: #649bf8;
  font-size: 0.12rem;
  cursor: pointer;
}

.tab_bar2 {
  position: absolute;
  right: 0.2rem;
  top: 0.1rem;
  width: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tab_con2 {
  width: 0.7rem;
  height: 24px;
  background: #e6f5ff;
  border-radius: 2px;
  border: 1px solid #c8e0f8;
  text-align: center;
  line-height: 24px;
  color: #649bf8;
  font-size: 0.12rem;
  cursor: pointer;
}

.activeTrend {
  background: #649bf8;
  color: #ffffff;
  border: 1px solid #649bf8;
}

#trend {
  height: calc(100% - 0.4rem);
  width: 100%;
}

.police_noise_box {
  width: 3.93rem;
  height: 27.6%;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  top: 70.2%;
  left: 1.19%;
  position: absolute;
}

#noise {
  height: calc(100% - 0.4rem);
  width: 100%;
}

.time_price_area {
  height: 11.43%;
  width:12.16rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  display: flex;
  align-items: center;
  position: absolute;
  top:2% ;
  left: 25.8%;
}

.timer {
  height: 71.1%;
  width: 4.16rem;
  display: flex;
  align-items: center;
}

.timer img {
  height: 100%;
  width: auto;
  margin-left: 0.42rem;
  margin-right: 0.3rem;
}

.timer_con {
  height: 100%;
  width: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.timer_line {
  width: 1px;
  height: 71.1%;
  background: #e7e7e7;
}

.con_top {
  font-size: 0.14rem;
  color: #666666;
}

.con_bottom {
  font-size: 0.24rem;
  color: #666666;
}

.con_le {
  width: 8rem;
  height: 87.4%;
  margin-top: 0.06rem;
  float: left;
}

.pm_box {
  height: 52.5%;
  width: 8rem;
  background: url(../../../../assets/img/projectAnalyse/pmbg.png) no-repeat;
  background-size: 100% 100%;
  position: absolute;
  top: 15%;
  left:26%;
}
.level1 {
  position: absolute;
  display: flex;
  justify-content: space-between;
  width: 2rem;
  color: #000000;
  font-size: 0.12rem;
  top: 0.5rem;
  left: 0rem;
}
.level2 {
  position: absolute;
  display: flex;
  justify-content: space-between;
  width: 2rem;
  color: #000000;
  font-size: 0.12rem;
  top: 0.5rem;
  right: 0rem;
}
.cirebox {
  display: flex;
}
.crie {
  width: 0.12rem;
  height: 0.12rem;
  border-radius: 50%;
  margin-right: 10px;
}
.crie1 {
  background: #32eef7;
}
.crie2 {
  background: #007bff;
}
.crie3 {
  background: #ff6061;
}
.air_box {
  height: 27.6%;
  width: 3.93rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  position: absolute;
  left: 25.8%;
  top:70.2% ;
}

#air {
  height: calc(100% - 0.4rem);
  width: 100%;
}

.temperature_box {
  height: 27.6%;
  width: 3.93rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  position: absolute;
  top: 70.2%;
  left: 50.5%;
}

#temperature {
  height: calc(100% - 0.4rem);
  width: 100%;
}

.con_lr {
  float: right;
  height: 87.5%;
}

.police_type {
  height: 38.5%;
  width: 3.93rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  position: absolute;
  left: 75.2%;
  top: 15%;
}

#type {
  width: 100%;
  height: calc(100% - 0.4rem);
}

.spray_box {
   height: 41.6%;
  width: 3.93rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
  position: absolute;
 left: 75.2%;
 top: 56%;
}

.spray {
  height: calc(100% - 0.4rem);
  width: 3.93rem;
  overflow: auto;
}

.spray::-webkit-scrollbar {
  display: none;
  /* Chrome Safari */
}

.spray {
  scrollbar-width: none;
  /* firefox */
  -ms-overflow-style: none;
  /* IE 10+ */
}

.spray_list {
  height: 12.5%;
  display: flex;
  align-items: center;
  color: #404040;
  font-size: 0.14rem;
  width: 3.64rem;
  margin: 0 auto;
}

.spray_list:nth-child(odd) {
  background: rgba(242, 248, 255, 1);
}

.spray_list img {
  height: 0.16rem;
  width: 0.16rem;
  margin-right: 0.1rem;
  margin-left: 0.1rem;
}

.spray_name {
  width: 1.2rem;
}

.spray_time {
  width: 1.4rem;
}

#pmtwo {
  width: 25%;
  float: left;
  height: 52.4%;
  margin-top: 0.5rem;
}

#pm_t {
  width: 50%;
  height: 62.8%;
  float: left;
  margin-top: 0.1rem;
}

#pmten {
  width: 25%;
  float: left;
  height: 52.4%;
  margin-top: 0.5rem;
}
.pmimg_box {
  margin: 0 auto;
  width: 4.4rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #000000;
  font-size: 12px;
}
.pmimg {
  width: 4.26rem;
  height: auto;
  display: block;
  margin: 0 auto;
}
.pmimg_quality {
  width: 4.26rem;
  margin: 0 auto;
  display: flex;
  text-align: center;
  color: #000000;
  font-size: 12px;
  justify-content: space-evenly;
  margin-top: 3px;
}
.pmimg_quality div {
  text-align: center;
  width: 0.71rem;
}
.weekair {
	position: absolute;
	bottom: 17%;
  color: #000000;
  font-size: 0.14rem;
}

.week_air_box {
  width: 100%;
  height: 15.5%;
  background: rgba(242, 248, 255, 1);
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 0;
}

.week_list {
  width: 14.28%;
  text-align: center;
}

.week_time {
  color: #286dff;
  font-size: 0.14rem;
}

.week_val {
  color: #000000;
  font-size: 0.14rem;
  margin-top: 0.1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
.week_val span {
  display: block;
  height: 0.12rem;
  width: 0.12rem;
  border-radius: 50%;
  margin-right: 8px;
}
.qy1 {
  background: #00d1a0;
}
.qy2 {
  background: #39d2ff;
}
.qy3 {
  background: #4e74e6;
}
.qy4 {
  background: #f3bc40;
}
.qy5 {
  background: #f5863e;
}
.qy6 {
  background: #fb466b;
}
.ifspray {
  color: #cccccc;
  text-align: center;
  margin: 0 auto;
  margin-top: 10px;
  font-size: 0.12rem;
}
</style>
