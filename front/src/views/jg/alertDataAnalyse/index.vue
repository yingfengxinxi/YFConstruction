<script src="alertDataAnalyse.js"></script>
<template>
	<div id="cloudCenter">
		<div class="left_box">
			<div class="police_num">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>报警数量</div>
				</div>
				<div class="num_con">
					<div class="pol_box police_bg1">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/police_img1.png"/>
						<div class="dayOrNum">
							<div class="pol_day">今日</div>
							<div class="pol_num">{{alertNumberData.dayNumber}}</div>
						</div>
					</div>
					<div class="pol_box police_bg2">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/police_img2.png"/>
						<div class="dayOrNum">
							<div class="pol_day">本周</div>
							<div class="pol_num">{{alertNumberData.weekNumber}}</div>
						</div>
					</div>
					<div class="pol_box police_bg3">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/police_img3.png"/>
						<div class="dayOrNum">
							<div class="pol_day">本月</div>
							<div class="pol_num">{{alertNumberData.monthNumber}}</div>
						</div>
					</div>
					<div class="pol_box police_bg4">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/police_img4.png"/>
						<div class="dayOrNum">
							<div class="pol_day">本年</div>
							<div class="pol_num">{{alertNumberData.yearNumber}}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="police_trend_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
						<div>报警趋势</div>
					</div>
					<div class="tab_bar">
						<div class="tab_con" :class="trendIndex==1?'activeTrend':''" @click="trendClick(1)">7日报警数量</div>
						<div class="tab_con" :class="trendIndex==2?'activeTrend':''" @click="trendClick(2)">30日报警数量</div>
					</div>
					<div id="trend">

					</div>
				</div>
				<div class="contrast_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
						<div>近30天前10项目报警数对比</div>
					</div>
					<div id="contrast">

					</div>
				</div>
		</div>
		<div class="center_box">
			<div class="police_list_box">
				<div class="more" @click="goenvAlert">
					更多>>
				</div>
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						报警列表
					</div>
				</div>
				<div class="police_list">
					<div class="police_list_title">
						<div style="width: 20%;">项目名称</div>
						<div style="width: 40%; ">报警内容</div>
						<div style="width: 20%;">超标值</div>
						<div style="width: 20%;">报警时间</div>
						<!-- <div style="width: 20%;">处理时间</div> -->
					</div>
					<div class="prolice_list_conbox">
						<div class="police_list_con" v-for="item in datalist">
							<div style="width: 20%;">{{item.projectName}}</div>
							<div style="width: 40%;">{{item.content}}</div>
							<div style="width: 20%;">{{item.monitorData ? item.monitorData: '--'}}{{item.unit && item.monitorData ? item.unit : ''}}</div>
							<div style="width: 20%;">{{item.createTime}}</div>
							<!-- <div style="width: 20%;">{{item.handleTime}}</div> -->
						</div>
					</div>
				</div>
			</div>
			<div class="dust_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						扬尘报警多发报警时段
					</div>
				</div>
				<div class="dust_tab">
					<div class="dust_tab_con" :class="dustIndex==1?'dust_active':''" @click="dustClick(1)">全部</div>
					<div>
						<el-date-picker
						  :class="dustIndex==2?'seledate dust_active':'seledate'"
						  @change="dustClick(2)"
					      v-model="raiseDustDate"
					      type="date"
						  value-format="yyyy-MM-dd"
					      :clearable='false'
					      placeholder="选择日期">
					    </el-date-picker>
					</div>
				</div>
				<div id="dust">

				</div>
			</div>
			<div class="airdata_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						噪音报警多发报警时段
					</div>
				</div>
				<div class="dust_tab">
					<div class="dust_tab_con" :class="airdataIndex==1?'dust_active':''" @click="airClick(1)">总数</div>
					<div>
						<el-date-picker
						  :class="airdataIndex==2?'seledate dust_active':'seledate'"
						  @change="airClick(2)"
					      v-model="noiseDate"
					      type="date"
						  value-format="yyyy-MM-dd"
					      :clearable='false'
					      placeholder="选择日期">
					    </el-date-picker>
					</div>
				</div>
				<div id="airdata">

				</div>
			</div>
		</div>
		<div class="right_box">
			<div class="type_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						报警类型占比
					</div>
				</div>
				<div id="type">

				</div>
			</div>
			<div class="excellent_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						PM10最优秀前5名
					</div>
				</div>
				<div class="excellent">
					<div class="excellent_list" v-for="(item,index) in proListGood">
						<div class="kuang" :style="{background:colorlists[index]}"></div>
						<div class="Serial">{{index+1}}</div>
						<div class="kl_pro" :title="item.name">{{item.name}}</div>
						<div class="pro_val">{{item.value}}μg/m³</div>
					</div>
				</div>
			</div>
			<div class="excellent_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../assets/img/projectAnalyse/model_title.png"/>
					<div>
						PM10最严重前5名
					</div>
				</div>
				<div class="excellent">
					<div class="excellent_list" v-for="(item,index) in proListBad">
						<div class="kuang" :style="{background:colorlists[index]}"></div>
						<div class="Serial">{{index+1}}</div>
						<div class="kl_pro" :title="item.name">{{item.name}}</div>
						<div class="pro_val">{{item.value}}μg/m³</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import {queryMenuListByPath} from '@/api/admin/menu'

	import {selectAlertNumber, selectAlertList, selectTypeProportion, selectAlertTrend,
		selectPMRank, selectRaiseDustAlert, selectProjectAlertNumber, selectNoiseAlert } from './alertDataAnalyse.js'

	export default {
		data() {
			return {
				// 报警数量相关数据
				alertNumberData: {
					dayNumber: 0,
					weekNumber: 0,
					monthNumber: 0,
					yearNumber: 0,
				},
				trendIndex: 1,
				dustIndex:1,
				airdataIndex:1,
				colorlists: ['#F57D65', '#FAAD14', '#73DCA0', '#77B8F8', '#A89FFF'],
				// 颗粒物最优秀前5名
				proListGood:[],
				// 颗粒物最严重前5名
				proListBad:[],
				datalist:[],
				// 扬尘报警查询时间
				raiseDustDate:'',
				// 噪音报警查询时间
				noiseDate:''
			}
		},
		components: {
		},
		created(){
			//this.randomRgb()
		},
		mounted() {
			// 获取报警数量
			this.getAlertNumber()
			// 查询报警列表
			this.getAlertList()
			this.type()
			// 查询报警类型占比
			this.initTypeData()
			// 初始化噪音报警多发时段数据 默认查询实时数据
			this.initAirData(this.airdataIndex)
			this.airdata()
			// 初始化扬尘报警多发时段数据 默认查询实时数据
			this.dustClick(this.dustIndex)
			this.dust()

			// 近一月各项目报警数对比
			this.contrast()
			this.initContrastData()

			this.trend()
			this.initTrendData(1)

			// 颗粒物最优秀前5
			this.selectPMRankGood()
			// 颗粒物最严重前5
			this.selectPMRankBad()
			this.remsize()
		},
		methods: {
			randomRgb() {//获取随机颜色
				for (let i=0;i<1000;i++) {
					let R = Math.floor(Math.random() * 255);
					let G = Math.floor(Math.random() * 255);
					let B = Math.floor(Math.random() * 255);
					this.colorlists.push('rgb(' + R + ',' + G + ',' + B + ')')
				}
			},
			type(){//报警类型占比
				let myChart = echarts.init(document.getElementById('type'));

				let option = {
					color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
					tooltip: {
						trigger: 'item',
						transitionDuration: 0, //防止页面抖动
					},
					legend: {
						icon: "circle",
						orient: 'vertical',
						right: 10,
						top:'center',
						itemWidth: 10,
        				itemHeight: 10,
        				 itemGap:20,
						textStyle: {
							color: "#000",
							fontSize:10
						},
					},
					series: [{
						type: "pie",
						radius: ["50%", "70%"],
						center: ["40%", "45%"],
						hoverAnimation: true,
						z: 10,
						itemStyle: {
							normal: {
								borderWidth: 5,
								borderColor: "rgba(215, 235, 255, 1)"
							}
						},
						label: {
							show: true,
							color: '#77B8F8'
						},
						data: [
							{
								"name": "PM10",
								"value": 8
							},
							{
								"name": "噪音",
								"value": 2
							},
							{
								"name": "TSP",
								"value": 1
							},
							{
								"name": "PM2.5",
								"value": 6
							}
						],
					}]
				}
				myChart.setOption(option)
				this.typeOption = option
				this.typeChart = myChart
			},
			initTypeData() {
				selectTypeProportion().then(res => {
					this.typeOption.series[0].data = res.data
					this.typeChart.setOption(this.typeOption, true)
				}).catch(() => {
				})
			},
			airClick(num){
				this.airdataIndex=num
				this.initAirData(num)
			},
			airdata(){//24小时空气质量数据
				let myChart = echarts.init(document.getElementById('airdata'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					grid: {
						right: "5%",
						left: "8%",
						top: '15%',
						bottom: '20%'
					},
					legend: {
						top: 10,
						right: '20',
						textStyle: {
							color: "#000",
							fontSize:10
						},
						itemWidth: 10,
        				itemHeight: 10,

					},
					xAxis: {
						data: [],
						type: 'category',
						axisLine: {
							show: false,
							color: 'red'
						},

						axisLabel: {
							color: '#000000',
							formatter: function(params) {
								var newParamsName = '' // 最终拼接成的字符串
								var paramsNameNumber = params.length // 实际标签的个数
								var provideNumber = 4 // 每行能显示的字的个数
								var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
								/**
								 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
								 */
								// 条件等同于rowNumber>1
								if(paramsNameNumber > provideNumber) {
									/** 循环每一行,p表示行 */
									for(var p = 0; p < rowNumber; p++) {
										var tempStr = '' // 表示每一次截取的字符串
										var start = p * provideNumber // 开始截取的位置
										var end = start + provideNumber // 结束截取的位置
										// 此处特殊处理最后一行的索引值
										if(p === rowNumber - 1) {
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
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						name:'(次)',
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
						name: '噪音',
						type: 'bar',
						barWidth: '8',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: '#73DCA0'
								}, {
									offset: 1,
									color: 'rgba(115, 220, 160, 1)'
								}]),
								barBorderRadius: 11,
							}
						},
						data: []
					}]
				};
				myChart.setOption(option)
				this.airOption = option
				this.airChart = myChart
			},
			initAirData(type) {
				let params = {
					type: type
				}
				// 24小时时段数据查询 type 1:查询最近24小时实时数据, 2:查询所传日期的当日数据
				if (type == 2) {
					params.date = this.noiseDate
				}
				selectNoiseAlert(params).then(res => {
					let resultData = res.data
					let xData = []
					let yData1 = []

					resultData.forEach(item => {
						xData.push(item.hourNumber + '时')
						yData1.push(item.son3)

						this.airOption.xAxis.data = xData
						this.airOption.series[0].data = yData1

						this.airChart.setOption(this.airOption, true)
					})
				})
			},
			dustClick(num){
				this.dustIndex=num
				this.initDustData(num)
			},

			dust(){//扬尘报警多发报警时段
				let myChart = echarts.init(document.getElementById('dust'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					grid: {
						right: "5%",
						left: "8%",
						top: '15%',
						bottom: '20%'
					},
					legend: {
						top: 10,
						right: '30',
						textStyle: {
							color: "#000",
							fontSize:10
						},
						itemWidth: 10,
        				itemHeight: 10,

					},
					xAxis: {
						data: [],
						type: 'category',
						axisLine: {
							show: false,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000000',
							formatter: function(params) {
								var newParamsName = '' // 最终拼接成的字符串
								var paramsNameNumber = params.length // 实际标签的个数
								var provideNumber = 4 // 每行能显示的字的个数
								var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
								/**
								 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
								 */
								// 条件等同于rowNumber>1
								if(paramsNameNumber > provideNumber) {
									/** 循环每一行,p表示行 */
									for(var p = 0; p < rowNumber; p++) {
										var tempStr = '' // 表示每一次截取的字符串
										var start = p * provideNumber // 开始截取的位置
										var end = start + provideNumber // 结束截取的位置
										// 此处特殊处理最后一行的索引值
										if(p === rowNumber - 1) {
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
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						name:'(次)',
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
						type: 'bar',
						barWidth: '8',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(119, 167, 248, 1)'
								}, {
									offset: 1,
									color: 'rgba(119, 167, 248, 1)'
								}]),
								barBorderRadius: 11,
							}
						},
						data: [12, 18, 20, 16, 12, 15, 32]
					}, {
						name: 'PM10',
						type: 'bar',
						barWidth: '8',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(192, 220, 253, 1)'
								}, {
									offset: 1,
									color: 'rgba(192, 220, 253, 1)'
								}]),
								barBorderRadius: 11,
							}

						},
						data: []
					},]
				};
				myChart.setOption(option)
				this.dustOption = option
				this.dustChart = myChart
			},
			// 查询扬尘报警多发报警时段
			initDustData(type) {
				let params = {
					type: type
				}
				// 24小时时段数据查询 type 1:查询最近24小时实时数据, 2:查询所传日期的当日数据
				if (type == 2) {
					params.date = this.raiseDustDate
				}
				selectRaiseDustAlert(params).then(res => {
					let resultData = res.data
					let xData = []
					let yData1 = []
					let yData2 = []

					resultData.forEach(item => {
						xData.push(item.hourNumber + '时')
						yData1.push(item.son1)
						yData2.push(item.son2)

						this.dustOption.xAxis.data = xData
						this.dustOption.series[0].data = yData1
						this.dustOption.series[1].data = yData2

						this.dustChart.setOption(this.dustOption, true)
					})
				})
			},
			contrast() { //近一月各项目报警数对比
				let colorStart = ['rgba(94, 137, 255, 1)', 'rgba(46, 210, 159, 1)', 'rgba(93, 194, 249, 1)', 'rgba(176, 255, 255, 1)', 'rgba(245, 235, 110, 1)', 'rgba(239, 190, 66, 1)', 'rgba(243, 69, 109, 1)']
				let colorEnd = ['rgba(48, 92, 224, 0.4)', 'rgba(46, 203, 160, 0.22)', 'rgba(93, 194, 249, 0.2)', 'rgba(176, 255, 255, 0.21)', 'rgba(245, 235, 110, 0.2)', 'rgba(238, 190, 66, 0.2)', 'rgba(243, 69, 109, 0.19)']
				let myChart = echarts.init(document.getElementById('contrast'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					grid: {
						right: "10%",
						left: "10%",
						top: '15%',
						bottom: '35%'
					},
					legend: {
						top: 10,
						right: '30',
						textStyle: {
							color: "#000",
							fontSize:10
						},
						itemWidth: 10,
        				itemHeight: 10,

					},
					xAxis: {
						data: [],
						type: 'category',
						axisLine: {
							show: false,
							color: '#fff'
						},

						axisLabel: {
							color: '#000000',
							rotate:'45',
              fontSize:'10',
							formatter: function(params) {
								var newParamsName = '' // 最终拼接成的字符串
								var paramsNameNumber = params.length // 实际标签的个数
								var provideNumber = 4 // 每行能显示的字的个数
								var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
								/**
								 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
								 */
								// 条件等同于rowNumber>1
								/* if(paramsNameNumber > provideNumber) {
									// 循环每一行,p表示行
									for(var p = 0; p < rowNumber; p++) {
										var tempStr = '' // 表示每一次截取的字符串
										var start = p * provideNumber // 开始截取的位置
										var end = start + provideNumber // 结束截取的位置
										// 此处特殊处理最后一行的索引值
										if(p === rowNumber - 1) {
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
								} */
								if (paramsNameNumber > 4) {
									newParamsName = params.substr(0, 4)
								} else {
									newParamsName = params
								}
								return params.replace("项目","");
							}

						},
						splitLine: {
							show: false,
							lineStyle: {
								color: '#000000',
								opacity: 0.2
							}
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

						data: [],
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
						type: 'bar',
						data: [],
						barWidth: 10,
						itemStyle: {
							normal: {
								color: "rgba(245, 125, 101, 1)",
								borderColor: "#F57D65",
								barBorderRadius: 11,
							}
						},
					}]
				};
				myChart.setOption(option)
				this.contrastOption = option
				this.contrastChart = myChart
			},
			// 查询近一月各项目报警数目
			initContrastData() {
				selectProjectAlertNumber().then(res => {
					let resultData = res.data
					let xData = []
					let yData1 = []
					let yData2 = []

					resultData.forEach(item => {
						xData.push(item.projectName)
						yData1.push(item.earlyNumber)
						yData2.push(item.alarmNumber)
					})

					this.contrastOption.xAxis.data = xData
					this.contrastOption.series[0].data = yData1
					this.contrastOption.series[1].data = yData2

					this.contrastChart.setOption(this.contrastOption, true)
				})
			},
			trendClick(num) {
				this.trendIndex = num
				this.initTrendData(num)
			},
			trend() {
				let myChart = echarts.init(document.getElementById('trend'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					grid: {
						right: "10%",
						left: "10%",
						top: '15%',
						bottom: '20%'
					},
					legend: {
						top: 10,
						right: '30',
						textStyle: {
							color: "#000",
							fontSize:10
						},
						itemWidth: 10,
        				itemHeight: 10,

					},
					xAxis: {
						data: [],
						boundaryGap: false,
						type: 'category',
						axisLine: {
							show: false,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000000',
								rotate:'45',
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
						data: [],
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
						data: [],
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
				this.trendOption = option
				this.trendChart = myChart
			},
			// 查询报警趋势
			initTrendData(type) {
				// type 类型 1：查询最近7天 2：查询最近30天
				let params = {
					type: type
				}
				selectAlertTrend(params).then(res => {
					let resData = res.data
					let xData = []
					let yData1 = []
					let yData2 = []

					resData.forEach(item => {
						xData.push(item.monitorDate)
						yData1.push(item.earlyNumber)
						yData2.push(item.alertNumber)
					})

					this.trendOption.xAxis.data = xData
					this.trendOption.series[0].data = yData1
					this.trendOption.series[1].data = yData2

				    this.trendChart.setOption(this.trendOption, true)
				}).catch(() => {
				})
			},
			// 查询报警数量
			getAlertNumber() {
				selectAlertNumber().then(res => {
					this.alertNumberData = res.data
				})
			},
			// 查询报警列表
			getAlertList() {
				selectAlertList().then(res => {
					this.datalist = res.data
				})
			},
			// 近24小时颗粒物最优秀前5名
			selectPMRankGood() {
				let params = {
					type: 2
				}
				selectPMRank(params).then(res => {
					this.proListGood = res.data
				})
			},
			// 近24小时颗粒物最严重前5名
			selectPMRankBad() {
				let params = {
					type: 1
				}
				selectPMRank(params).then(res => {
					this.proListBad = res.data
				})
			},
			goenvAlert(){
				this.goToUrl('monitorPointAlert')
			},
			goToUrl(path) {
				let param = {path: path}
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
						name:item.menuId
					}
					if(JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
						this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
					}
					this.$store.state.app.activeclass= activeclass.comname
					this.$router.push({name:activeclass.name})
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
		}
	}
</script>

<style scoped>
	#cloudCenter{
		height: 100%;
		display: flex;
		justify-content: space-evenly;
	}
	.content_box {
		height: 100%;
		overflow: hidden;
		display: flex;
		justify-content: space-evenly;
	}

	.left_box {
		height: 100%;
		width: 3.93rem;
		display: flex;
    flex-direction: column;
    justify-content: space-evenly;
	}
	.center_box {
		height: 100%;
		width: 8.06rem;
		display: flex;
    flex-direction: column;
    justify-content: space-evenly;
	}
	.right_box {
		height: 100%;
		width: 3.93rem;
		display: flex;
    flex-direction: column;
    justify-content: space-evenly;
	}

	.police_num{
		width: 3.93rem;
		height: 31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
	}

	.model_title{
		height: 0.4rem;
		display: flex;
		align-items: center;
	}
	.model_title img{
		width: 0.16rem;
		height: 0.16rem;
		margin-left: 0.2rem;
		margin-right: 0.1rem;
	}
	.model_title div{
		font-size: 0.16rem;
		color: #000000;
	}
	.num_con{
		height: calc(100% - 0.6rem);
		display: flex;
	    flex-wrap: wrap;
	    justify-content: space-evenly;
	    margin-top: 0.1rem;
	}
	.pol_box{
		height: 40%;
		width: 1.6rem;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	    /*margin-top: 0.1rem;*/
	}
	.pol_box img{
		height: 66%;
		width: 0.52rem;
	}
	.dayOrNum{
		height: 66%;
		display: flex;
		justify-content: space-around;
		flex-direction: column;
		text-align: center;
		width: 0.6rem;

	}
	.pol_day{
		font-size: 0.14rem;
		color: #666666;
	}
	.pol_num{
		font-size: 0.24rem;
		color: #619CFF;
	}
	.police_bg1{
		background: url(../../../assets/img/projectAnalyse/police_bg1.png) no-repeat;
		background-size: 100% 100%;
	}
	.police_bg2{
		background: url(../../../assets/img/projectAnalyse/police_bg2.png) no-repeat;
		background-size: 100% 100%;
	}
	.police_bg3{
		background: url(../../../assets/img/projectAnalyse/police_bg3.png) no-repeat;
		background-size: 100% 100%;
	}
	.police_bg4{
		background: url(../../../assets/img/projectAnalyse/police_bg4.png) no-repeat;
		background-size: 100% 100%;
	}
	.police_trend_box{
		width: 3.93rem;
		height: 31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
	}
	.tab_bar{
		position: absolute;
		right: 0.2rem;
		top: 0.1rem;
		width: 2.2rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.tab_con{
		width: 1rem;
		height:24px;
		background: #E6F5FF;
		border-radius: 2px;
		border: 1px solid #C8E0F8;
		text-align: center;
		line-height: 24px;
		color: #649BF8;
		font-size: 0.12rem;
		cursor: pointer;
	}
	.activeTrend{
		background: rgba(100, 155, 248, 1);
		color: #FFFFFF;
		border: 1px solid rgba(100, 155, 248, 1);
	}
	#trend{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.contrast_box{
		width: 3.93rem;
		height: 31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
	}
	#contrast{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.police_list_box{
		width: 8.06rem;
		height:31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
	}
	.more{
		height: 0.16rem;
		font-size: 0.12rem;
		color: #286DFF;
		cursor: pointer;
		position: absolute;
		right: 0.2rem;
		top: 0.14rem;
	}
	.police_list{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.police_list_title{
		width: 7.6rem;
		margin: 0 auto;
		height: 0.37rem;
		border-bottom: 1px solid #286DFF;
		line-height: 0.37rem;
		color: #286DFF;
		font-size: 0.14rem;
		display: flex;
	}
	.police_list_title div{
		text-align: center;
	}
	.prolice_list_conbox{
		width: 7.6rem;
		margin: 0 auto;

		height: 77.28%;
		overflow-y: auto;
	}

		.prolice_list_conbox::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}
	.prolice_list_conbox {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	.police_list_con{
		display: flex;
	}
	.police_list_con div{
		text-align: center;
		color: #666666;
		font-size: 0.14rem;
		height: 0.4rem;
		line-height: 0.4rem;
	}
	.dust_box{
		width: 8.06rem;
		height: 31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		position: relative;
	}
	.dust_tab{
		position: absolute;
		display: flex;
		z-index: 9;
		top: 13px;
		right: 20px;
	}
	.dust_tab_con{
		width: 0.66rem;
		height: 0.24rem;
		border: 1px solid #C8E0F8;
		background: #E6F5FF;
		border-radius: 2px;
		font-size: 0.12rem;
		font-weight: 500;
		color:#649BF8;
		text-align: center;
		line-height: 0.24rem;
		margin-right: 7px;
	}
	.seledate{
		height: 0.24rem;
		display: flex;
	}
	.seledate /deep/input{
		height: 0.24rem;
		background: none;
		background: #E6F3FF;
		color: #649BF8;
		font-size: 0.12rem;
		border: 1px solid #DBE8FF;
	}
	.dust_active /deep/input{
		background: #649BF8;
		color:#FFFFFF;
		border: 1px solid #649BF8;
	}
	/deep/.el-input__prefix .el-input__icon{
		line-height: 0.24rem;
	}
	.dust_active{
		background: #649BF8;
		color:#FFFFFF;
		border: 1px solid #649BF8;
	}
	#dust{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.airdata_box{
		width: 8.06rem;
		height:31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		position: relative;
	}
	#airdata{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.type_box{
		width: 3.93rem;
		height:31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
	}
	#type{
		height: calc(100% - 0.4rem);
		width: 100%;
	}
	.excellent_box{
		width: 3.93rem;
		height: 31.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		background-size: 100% 100%;
		position: relative;
	}
	.excellent{
		width: 100%;
		height:84.74%;
	}
	.police_list_con:nth-child(even) {
		background: #F2F8FF;
	}
	.excellent_list{
		margin: 0 auto;
		width: 3.63rem;
		height: 0.4rem;
		display: flex;
		align-items: center;
	}
	.excellent_list:nth-child(even) {
		background: #F2F8FF;
	}
	.kuang{
		width: 8px;
		margin-left: 24px;
		height: 8px;
		margin-right: 10px;
	}
	.Serial{
		width: 20px;
		font-size: 14px;
		color: #404040;
		text-align: center;

	}
	.kl_pro{
		width: 150px;
		font-size: 14px;
		color: #404040;
		margin-left: 20px;
		overflow: hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
	}
	.pro_val{
		width: 100px;
		font-size: 14px;
		color: #404040;
		margin-left: 50px;
	}
</style>
