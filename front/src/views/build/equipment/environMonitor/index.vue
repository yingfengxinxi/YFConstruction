<template>
	<div class="main-body select" id="cloudCenter">
		<div class="environ_box">
			<div class="environ_left">
				<div class="zy_box">
					<div class="top_top">
						<div class="span-img1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
						</div>
						<div class="title1">24小时空气质量数据</div>
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="history_img" src="../../../../../static/img/towerCraneMonitor/history.png" />
					</div>
					<div id="bottom_Echars2"></div>
				</div>
				<div class="yzwd_box">
					<div class="top_bottom_top">
						<div class="top_top">
							<div class="span-img1">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
							</div>
							<div class="title1">噪声 <span class="unit">单位：分贝</span></div>
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="history_img" src="../../../../../static/img/towerCraneMonitor/history.png" />
						</div>
					</div>
					<div id="bottom_Echars1"></div>
				</div>
				<div class="eskqzl_box">
					<div class="top_top">
						<div class="span-img1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
						</div>
						<div class="title1">近一周温度</div>
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="history_img" src="../../../../../static/img/towerCraneMonitor/history.png" />
					</div>
					<div id="top_bottom_Echars1"></div>
				</div>
			</div>

			<div class="environ_center">
				<div class="qtsssj_box">
					<div class="top_top">
						<div class="span-img1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
						</div>
						<div class="title1">实时数据</div>
					</div>
					<div class="qthj_box">
						<div class="qt_div">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/qthjimg1.png"/>
							<div class="qt_name">温度</div>
							<div class="qt_val">{{wdData}}℃</div>
						</div>
						<div class="qt_div">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/qthjimg2.png"/>
							<div class="qt_name">湿度</div>
							<div class="qt_val">{{sdData}}%rh</div>
						</div>
						<div class="qt_div">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/qthjimg3.png"/>
							<div class="qt_name">风速</div>
							<div class="qt_val">{{fsData}}km/h</div>
						</div>
						<div class="qt_div">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/qthjimg4.png"/>
							<div class="qt_name">噪声</div>
							<div class="qt_val">{{zyData}}dB</div>
						</div>
					</div>
				</div>
				<div class="kqzl_box">
					<div class="top_top">
						<div class="span-img1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
						</div>
						<div class="title1">空气质量</div>
					</div>
					<div class="ybp_box">
						<div id="tps">

						</div>
						<div id="pm10">

						</div>
						<div id="pm2">

						</div>
					</div>
				</div>
				<div class="jqrzl_box">
					<div class="top_top">
						<div class="span-img1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
						</div>
						<div class="title1">近7日空气质量</div>
					</div>
					<div class="seven_box">
						<div class="seven_list" v-for="item in sevenList">
							<div v-if="item.levelName=='优'" class="qy1 seven_name">{{item.levelName}}</div>
							<div v-else-if="item.levelName=='良'" class="qy2 seven_name">{{item.levelName}}</div>
							<div v-else-if="item.levelName=='轻度污染'" class="qy3 seven_name">{{item.levelName}}</div>
							<div v-else-if="item.levelName=='中度污染'" class="qy4 seven_name">{{item.levelName}}</div>
							<div v-else-if="item.levelName=='重度污染'" class="qy5 seven_name">{{item.levelName}}</div>
							<div v-else-if="item.levelName=='严重污染'" class="qy6 seven_name">{{item.levelName}}</div>
							<div v-else class="qy7 seven_name">{{item.levelName}}</div>
							<div class="seven_date">{{item.monitorDate}}</div>
						</div>
					</div>
					<div class="seven_line"></div>
					<div class="kqnum">
						<div>0</div>
						<div>35</div>
						<div>75</div>
						<div>115</div>
						<div>150</div>
						<div>250</div>
						<div>300</div>
					</div>
					<div class="cskdiv">

						<div>
							<div class="bgdiv" style="background: #30BF78;"></div>
							<div class="bgtit">优</div>
						</div>
						<div>
							<div class="bgdiv" style="background: #3397FE;"></div>
							<div class="bgtit">良</div>
						</div>
						<div>
							<div class="bgdiv" style="background: #3477FF;"></div>
							<div class="bgtit">轻度污染</div>
						</div>
						<div>
							<div class="bgdiv" style="background: #FAAD14;"></div>
							<div class="bgtit">中度污染</div>
						</div>
						<div>
							<div class="bgdiv" style="background: #F4664A;"></div>
							<div class="bgtit">重度污染</div>
						</div>
						<div>
							<div class="bgdiv" style="background: #FF5353;"></div>
							<div class="bgtit">严重污染</div>
						</div>
					</div>
				</div>
			</div>
			<div class="environ_right">
				<div class="lskqzl_box">
					<div class="top_bottom_top">
						<div class="top_top">
							<div class="span-img1">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
							</div>
							<div class="title1">历史空气质量分析</div>
						</div>
					</div>
					<div id="hisAir"></div>
				</div>
				<div class="plqtjl_box">
					<div class="top_bottom_top">
						<div class="top_top">
							<div class="span-img1">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../static/img/towerCraneMonitor/top-left-1.png" />
							</div>
							<div class="title1">喷淋启停记录</div>
						</div>
					</div>
					<div class="pl_box">
						<div class="pl_list" v-for="item in plList">
							<div class="pl_data">
								<div>{{item.assetName}}</div>
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.operationType==1"  src="../../../../../static/img/plopen1.png" />
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else  src="../../../../../static/img/plopen2.png" />

							</div>
							<div class="pldate">
								{{item.operationTime}}
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import  * as echarts from "echarts";
	import { mapGetters } from 'vuex'
	import {
		realTimeData,
		realTimeDataTemperature,
		realTimeDataAll,
		selectEquList,
		selectPmTsp,
		selectSeven,
		selecAnalysis,
    selectEnvDataAt
	} from '../api/environMonitor.js'
	export default {
		name: "environMonitor",
		components: {},
		data() {
			return {
				timer: null, //定时器名称
				plList:[],
				zyData:'',
				wdData:'',
				sdData:'',
				fsData:'',
				sevenList:[]
			};
		},
		computed: {
			...mapGetters(['supply', 'pageDataReflushTime']),
		},
		created() {

			//this.initData()
			this.remsize()
		},
		mounted() {
			this.getSeven()
			this.getStrastio()
			this.gettspPM()
			this.getHisAirQualityAnalysis()
			this.changeproid();
		},
		watch: {
			dataList: function(val) {
				this.$nextTick(() => {

				})
			}
		},
		beforeDestroy() {
			clearInterval(this.timer);　　 // 清除定时器
			this.timer = null;
		},
		methods: {
			initData() {
				let _this = this
				this.timer = setInterval(
					function() {
						_this.changeproid();
						_this.gettspPM()
					}, this.pageDataReflushTime
				)
			},
			getHisAirQualityAnalysis(){
				selecAnalysis().then(res => {
					this.hisAir(res.data)
				}).catch(() => {})
			},
			getSeven(){
				selectSeven().then(res => {
					this.sevenList=res.data
				}).catch(() => {})
			},
			getStrastio(){//喷淋启停
				selectEquList({
					pageType: 0
				}).then(res => {
					this.plList=res.data.list
				}).catch(() => {})
			},
			gettspPM(){//pm10.2.5 tsp
				selectPmTsp({}).then(res => {
					this.tspEcharts(res.data)
					this.PMEchars10(res.data)
					this.PMEchars(res.data)
				}).catch(() => {})
			},
			hisAir (data) { //历史空气质量分析
				let seriesData = []
				for (let i = 0; i < data.length; i++) {
					seriesData.push({
						name: data[i].levelName,
						value: data[i].num
					})
				}
				let myChart = echarts.init(document.getElementById('hisAir'));
				let option = {
					color: ['#30BF78 ', '#3397FE ', '#3477FF ', '#FAAD14 ', '#F4664A ', '#FB466B', '#6cf371', '#f5835f', '#6c2be7', '#f3456d'],
					tooltip: {
						trigger: 'item',
						transitionDuration: 0,//防止页面抖动
					},
					legend: {
						icon: "circle",
						left: 'center',

						bottom: '10',
						itemWidth: 10,
						itemHeight: 10,
						itemGap: 15,
						textStyle: {
							color: "#666666",
							fontSize: 12
						},
					},
					series: [{
						type: 'pie',
						radius: ['42%', '60%'],
						center: ["50%", "40%"],
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
							color: '#666666'
						},
						data: seriesData
					},]
				};
				myChart.setOption(option)
			},
			tspEcharts(data){
				let myChart = echarts.init(document.getElementById('tps'));
				// console.log(myChart);
				let option = {
					series: [{
						type: "gauge",
						radius: "80%",
						min: 0,
						max: 500,
						title: {
							show: true,
							color: "#000000",
							offsetCenter: ["0%", "100%"]
						},
						axisLine: {
							lineStyle: {
								width: 8,
									color: [
									[0.2, '#30BF78'],
									[0.6, '#FAAD14'],
									[1, '#FF5353'],
								],
							}
						},
						pointer: {
							width: 3,
							itemStyle: {
								color: "auto"
							}
						},
						axisTick: {
							distance: 0,
							length: 0,
							lineStyle: {
								color: "#fff",
								width: 2
							}
						},
						splitLine: {
							distance: 0,
							length: -10,
							show: false,
							lineStyle: {
								color: "#fff",
								width: 4
							}
						},
						axisLabel: {
							color: "auto",
							distance: 20,
							fontSize: 6
						},
						detail: {
							valueAnimation: true,
							formatter: "{value} μg/m3",
							color: "auto",
							fontSize: 12
						},

						data: [{
							value: data.TSPAVG,
							name: "TSP"
						}]
					}]
				};
				myChart.setOption(option, true);
			},
			//PM2.5
			PMEchars(data) {
				let myChart = echarts.init(document.getElementById('pm2'));
				// console.log(myChart);
				let option = {
					series: [{
						type: "gauge",
						radius: "90%",
						min: 0,
						max: 300,
						title: {
							show: true,
							color: "#000000",
							offsetCenter: ["0%", "90%"]
						},
						axisLine: {
							lineStyle: {
								width: 8,
								color: [
									[0.116, '#00d1a0'],
									[0.25, '#39d2ff'],
									[0.383, '#4e74e6'],
									[0.5, '#f3bc40'],
									[0.83, '#f5863e'],
									[1, "#fb466b"]
								],
							}
						},
						pointer: {
							width: 3,
							itemStyle: {
								color: "auto"
							}
						},
						axisTick: {
							distance: 0,
							length: 0,
							lineStyle: {
								color: "#fff",
								width: 2
							}
						},
						splitLine: {
							distance: 0,
							length: -10,
							show: false,
							lineStyle: {
								color: "#fff",
								width: 4
							}
						},
						axisLabel: {
							color: "auto",
							distance: 20,
							fontSize: 10
						},
						detail: {
							valueAnimation: true,
							formatter: "{value} μg/m3",
							color: "auto",
							fontSize: 12
						},

						data: [{
							value: data.PM25AVG,
							name: "PM2.5"
						}]
					}]
				};
				myChart.setOption(option, true);
			},
			//PM10
			PMEchars10(data) {
				let myChart = echarts.init(document.getElementById('pm10'));
				// console.log(myChart);
				let option = {
					series: [{
						type: "gauge",
						radius: "80%",
						min: 0,
						max: 250,
						title: {
							show: true,
							color: "#000000",
							offsetCenter: ["0%", "100%"]
						},
						axisLine: {
							lineStyle: {
								width: 8,
								color: [
									[0.2, '#30BF78'],
									[0.6, '#FAAD14'],
									[1, '#FF5353'],
								],
							}
						},
						pointer: {
							width: 3,
							itemStyle: {
								color: "auto"
							}
						},
						axisTick: {
							distance: 0,
							length: 0,
							lineStyle: {
								color: "#fff",
								width: 2
							}
						},
						splitLine: {
							distance: 0,
							length: -10,
							show: false,
							lineStyle: {
								color: "#fff",
								width: 4
							}
						},
						axisLabel: {
							color: "auto",
							distance: 20,
							fontSize: 6
						},
						detail: {
							valueAnimation: true,
							formatter: "{value} μg/m3",
							color: "auto",
							fontSize: 12
						},

						data: [{
							value: data.PM10AVG,
							name: "PM10"
						}]
					}]
				};
				myChart.setOption(option, true);
			},
			//温度折线
			temperatureBrokenLineEchars(id, data) {
				let xAxisData = []
				let zgData = []
				let zdData = []
				for(let i = 0; i < data.length; i++) {
					xAxisData.push(data[i].rq)
					zgData.push(data[i].zg)
					zdData.push(data[i].zd)
				}
				let myChart = echarts.init(document.getElementById(id));
				let option = {
					tooltip: {
						trigger: "axis",
						axisPointer: {
							type: "cross",
							label: {
								backgroundColor: "#6a7985"
							}
						}
					},
					legend: {
						data: ["最高气温", "最低气温"]
					},
					grid: {
						left: "3%",
						right: "6%",
						bottom: "10%",
						top: '13%',
						containLabel: true
					},
					xAxis: [{
						type: "category",
						boundaryGap: false,
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},
						axisLabel:{
							color: '#000',
						
							formatter: function (params) {
								var str=params
								var res=str.substring(5,10)
								
								
								return res
							}
						},
						data: xAxisData
					}],
					yAxis: [{
						type: "value",
						axisLine: {
							show: false,
						},
						axisLabel: {
							formatter: "{value} ℃"
						}
					}],
					series: [{
							name: "最低气温",
							type: "line",
						
							itemStyle: {
								normal: {
									color: "rgba(100, 155, 248, 1)", //改变折线点的颜色
									lineStyle: {
										color: "rgba(100, 155, 248, 1)" //改变折线颜色
									}
								}
							},
							areaStyle: {
								color: {
									type: "radial",
									x: 0.5,
									y: 0.5,
									r: 0.5,
									colorStops: [{
											offset: 0,
											color: "rgba(100, 155, 248, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(100, 155, 248, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								},
								
								color: {
									type: "linear",
									x: 0,
									y: 0,
									x2: 0,
									y2: 1,
									colorStops: [{
											offset: 0,
											color: "rgba(100, 155, 248, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(100, 155, 248, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								}
							},
							emphasis: {
								focus: "series"
							},
							data: zdData
						},
						{
							name: "最高气温",
							type: "line",
							
							itemStyle: {
								normal: {
									color: "rgba(250, 173, 20, 1)", //改变折线点的颜色
									lineStyle: {
										color: "rgba(250, 173, 20, 1)" //改变折线颜色
									}
								}
							},
							areaStyle: {
								color: {
									type: "radial",
									x: 0.5,
									y: 0.5,
									r: 0.5,
									colorStops: [{
											offset: 0,
											color: "rgba(250, 173, 20, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(250, 173, 20, 0.1)" // 100% 处的颜色
										}
									],
									
								},
								
								color: {
									type: "linear",
									x: 0,
									y: 0,
									x2: 0,
									y2: 1,
									colorStops: [{
											offset: 0,
											color: "rgba(250, 173, 20, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(250, 173, 20, 0.1)" // 100% 处的颜色
										}
									],
									
								}
							},
							emphasis: {
								focus: "series"
							},
							data: zgData
						}
					]
				};
				myChart.setOption(option, true);
			},
			//噪声柱状图
			NoiseHistogramEchars(id, data) {
				let xAxisData = []
				let seriesData = []
				for(let i = 0; i < data.length; i++) {
					xAxisData.push(data[i].hourNumber + '时')
					seriesData.push(data[i].dateCount)
				}
				let myChart = echarts.init(document.getElementById(id));
				let option = {
					grid: {
						left: "3%",
						right: "4%",
						bottom: "10%",
						top: "10%",
						containLabel: true
					},
					xAxis: {
						type: "category",
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},
						axisLabel:{
							color: '#000',
						},
						data: xAxisData
					},
					yAxis: {
						type: "value",
						axisLine: {
							show: false,
						},
						data: [0, 15, 30, 60, 75, 100, 125],
					},
					series: [{
						data: seriesData,
						type: "bar",
						barWidth: 15,
						itemStyle: {
							normal: {
								color: "rgba(91, 151, 249, 1)"
							}
						}
					}]
				};
				myChart.setOption(option, true);
			},
			//空气折线
			AirEchars(id, data) {
				let xAxisData = []
				let PM2Count = []
				let PM10Count = []
				for(let i = 0; i < data.length; i++) {
					xAxisData.push(data[i].hourNumber + '时')
					PM2Count.push(data[i].PM2Count)
					PM10Count.push(data[i].PM10Count)
				}
				let myChart = echarts.init(document.getElementById(id));
				let option = {
					// title: {
					//     text: '堆叠区域图'
					// },
					tooltip: {
						trigger: "axis",
						axisPointer: {
							type: "cross",
							label: {
								backgroundColor: "#6a7985"
							}
						}
					},
					legend: {
						data: ["PM2.5", "PM10"]
					},
					grid: {
						left: "3%",
						right: "4%",
						bottom: "3%",
						containLabel: true
					},
					xAxis: [{
						type: "category",
						boundaryGap: false,
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},
						axisLabel:{
							color: '#000',
						},
						data: xAxisData
					}],
					yAxis: [{
						type: "value",
						axisLine: {
							show: false,
						},
						axisLabel: {
							formatter: "{value}"
						}
					}],
					series: [{
							name: "PM10",
							type: "line",
							
							itemStyle: {
								normal: {
									color: "rgba(100, 155, 248, 1)", //改变折线点的颜色
									lineStyle: {
										color: "rgba(100, 155, 248, 1)" //改变折线颜色
									}
								}
							},
							areaStyle: {
								color: {
									type: "radial",
									x: 0.5,
									y: 0.5,
									r: 0.5,
									colorStops: [{
											offset: 0,
											color: "rgba(100, 155, 248, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(100, 155, 248, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								},
								origin: "start",
								color: {
									type: "linear",
									x: 0,
									y: 0,
									x2: 0,
									y2: 1,
									colorStops: [{
											offset: 0,
											color: "rgba(100, 155, 248, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(100, 155, 248, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								}
							},
							emphasis: {
								focus: "series"
							},
							data: PM10Count
						},
						{
							name: "PM2.5",
							type: "line",
							
							itemStyle: {
								normal: {
									color: "rgba(48, 191, 120, 1)", //改变折线点的颜色
									lineStyle: {
										color: "rgba(48, 191, 120, 1)" //改变折线颜色
									}
								}
							},
							areaStyle: {
								color: {
									type: "radial",
									x: 0.5,
									y: 0.5,
									r: 0.5,
									colorStops: [{
											offset: 0,
											color: "rgba(48, 191, 120, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(48, 191, 120, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								},
								origin: "auto",
								color: {
									type: "linear",
									x: 0,
									y: 0,
									x2: 0,
									y2: 1,
									colorStops: [{
											offset: 0,
											color: "rgba(48, 191, 120, 1)" // 0% 处的颜色
										},
										{
											offset: 1,
											color: "rgba(48, 191, 120, 0.1)" // 100% 处的颜色
										}
									],
									global: false // 缺省为 false
								}
							},
							emphasis: {
								focus: "series"
							},
							data: PM2Count
						}
					]
				};
				myChart.setOption(option, true);
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
			resetScreenSize(dw, dh) {
				if(window.innerWidth > 1000) {
					function init() {
						var _el = document.getElementById('cloudCenter');
						var hScale = (window.innerHeight) / (dh || 1080);
						var wScale = (window.innerWidth) / (dw || 1920);
						console.log(window.innerHeight, window.innerWidth)
						_el.style.transform = 'scaleX(' + wScale + ') scaleY(' + hScale + ')'
					}
					var lazyFun;
					window.onresize = function() {
						clearTimeout(lazyFun);
						lazyFun = setTimeout(function() {
							init()
						}, 600)
					}
					init()
				}
			},
			changeproid() {
				realTimeData({ //24小时空气
					type: 2
				}).then(data => {
					this.AirEchars("bottom_Echars2", data.data)
				}).catch(() => {})


				realTimeData({ //24小时噪音
					type: 1
				}).then(data => {
					this.NoiseHistogramEchars("bottom_Echars1", data.data)
				}).catch(() => {})

				realTimeDataTemperature({ //温度折线
				}).then(data => {
					this.temperatureBrokenLineEchars("top_bottom_Echars1", data.data)
				}).catch(() => {})

				realTimeDataAll({ //温度折线
				}).then(data => {
				/*	for(let i = 0; i < data.data.length; i++) {
						this.zyData=data.data[i].zy
						this.wdData=data.data[i].wd
						this.sdData=data.data[i].sd
						this.fsData=data.data[i].fs
					}*/
				}).catch(() => {})

        selectEnvDataAt({ // 其他数据
        }).then(data => {
          this.zyData=data.data.noise.value
          this.wdData=data.data.temperature.value
          this.sdData=data.data.humidity.value
          this.fsData=data.data.windSpeed.value
        }).catch(() => {})

			}
		}
	};
</script>
<style lang="scss" scoped>
	.main-body {
		background-color: white;
		/*transform-origin: left top;
  overflow: hidden;*/
		height: 100%;
		width: 100%;
	}

	.environ_box {
		width: 16.63rem;
		height: 100%;
		display: flex;
		justify-content: space-evenly;
	}

	.environ_left {
		height: 100%;
	}

	.zy_box {
		width: 5.28rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 10px;
	}

	#bottom_Echars1 {
		width: 5.28rem;
		height: calc(98% - 26px);
	}

	.yzwd_box {
		width: 5.28rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 21px;
	}

	#top_bottom_Echars1 {
		width: 5.28rem;
		height: calc(98% - 26px);
	}

	.eskqzl_box {
		width: 5.28rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 21px;
	}

	#bottom_Echars2 {
		width: 5.28rem;
		height: calc(98% - 26px);
	}

	.environ_center {
		height: 100%;
	}

	.qtsssj_box {
		width: 6.77rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 10px;
	}
	.qthj_box{
		height: calc(98% - 32px);
		display: flex;
	    flex-wrap: wrap;
    	justify-content: space-evenly;
    	align-items: center;
	}
	.qt_div{
		height: 39.6%;
		width: 3.08rem;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.4400);
		border-radius: 5px;
		display: flex;
		align-items: center;
	}
	.qt_div img{
		margin-left: 0.21rem;
		height: 83%;
		width: auto;
	}
	.qt_name{
		color: #404040;
		font-size: 0.14rem;
		margin-left: 0.4rem;
		margin-right: 0.1rem;
	}
	.qt_val{
		color: #649BF8;
		font-size: 0.24rem;
	}
	.kqzl_box {
		width: 6.77rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 21px;
	}
	.ybp_box{
		height: calc(98% - 26px);
		display: flex;
	}
	#tps{
		height: 100%;
		width: 33.3%;
	}
	#pm2{
		height: 100%;
		width: 33.3%;
	}
	#pm10{
		height: 100%;
		width: 33.3%;
	}
	.jqrzl_box {
		width: 6.77rem;
		height: 30.4%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 21px;
	}
	.seven_box{
		width: 6.23rem;
		display: flex;
		margin: 0 auto;
		margin-top: 5%;
		justify-content: space-between;
	}
	.seven_list{
		width: 0.89rem;
		text-align: center;
	}
	.seven_name{
		font-size: 0.28rem;
	}
	.qy1{
		color: #30BF78;
	}
	.qy2{
		color: #3397FE;
	}
	.qy3{
		color: #3477FF;
	}
	.qy4{
		color: #FAAD14;
	}
	.qy5{
		color: #F4664A;
	}

	.qy6{
		color: #FF5353;
	}
	.seven_date{
		color: #404040;
		font-size: 0.14rem;
	}
	.seven_line{
		width: 6.48rem;
		margin: 0 auto;
		margin-top: 5%;
		margin-bottom: 5%;
		border: 1px solid #E1E1E1;
	}
	.kqnum{
		width: 6.2rem;
		color: #404040;
		font-size: 0.14rem;
		display: flex;
		margin: 0 auto;
		justify-content: space-between;
	}
	.cskdiv{
		width: 6rem;
		display: flex;
		margin: 0 auto;
		justify-content: space-between;
	}
	.cskdiv div{
		width: 0.96rem;
		height: 5px;

	}
	.cskdiv .bgtit{
		text-align: center;
		color: #404040;
		font-size: 0.14rem;
		margin-top: 5px;
	}
	.environ_right {
		height: 100%;
	}

	.lskqzl_box {
		width: 3.85rem;
		height: 41.8%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.2900);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 10px;
	}
	#hisAir{
		height: calc(98% - 26px);
	}
	.plqtjl_box {
		width: 3.85rem;
		height: 51.7%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.2900);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 23px;
	}
	.pl_box{
		height: calc(98% - 26px);
		overflow-y: auto;
		margin-top: 0.12rem;
	}
	.pl_box::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}
	.pl_box {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	.pl_list{
		width: 3.62rem;
		height: 13.4%;
		background: #F3F7FF;
		border-radius: 5px;
		margin: 0 auto;
		margin-bottom: 10px;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.pl_data{
		display: flex;
		align-items: center;
		margin-left: 0.09rem;
		font-size: 0.14rem;
		color: #404040;
	}
	.pl_data div{
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;

	}
	.pl_data img{
		margin-left: 0.1rem;
	}
	.pldate{
		margin-right: 0.09rem;
		color: #666666;
		font-size: 0.14rem;
	}







	.write {
		margin-left: 0.10rem;
		height: 0.20rem;
		font-size: 0.14rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #404040;
		line-height: 0.20rem;
		display: inline-block;
	}

	.write2 {
		margin-left: 0.30rem;
		height: 0.20rem;
		font-size: 0.14rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #649bf8;
		line-height: 0.20rem;
		display: inline-block;
	}

	.top_top {
		margin-left: 0.14rem;
		overflow: hidden;
		margin-top: 2%;
		.span-img1 {
			float: left;
			display: block;
			margin-right: 0.10rem;
			.img1 {
				float: left;
				display: block;
				width: 16px;
				height: auto;
				// background: url(../../../../../static/img/towerCraneMonitor/top-left-1.png);
			}
		}
		.title1 {
			float: left;
			display: block;
			width: 1.50rem;
			height: 0.22rem;
			font-size: 0.16rem;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: #404040;
			line-height: 0.22rem;
		}
		.history_img {
			float: right;
			display: block;
			width: 0.24rem;
			height: 0.24rem;
			margin-right: 0.15rem;
			//background: url(../../../../../static/img/towerCraneMonitor/history.png);
		}
	}

	.unit {
		// width: 60px;
		// height: 17px;
		font-size: 0.12rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #404040;
		line-height: 0.17rem;
	}

	.shadowBorder {
		background: #ffffff;
		box-shadow: 0px 0px 0.1rem 0px rgba(145, 180, 241, 0.44);
		border-radius: 0.05rem;
	}

	.select {
		-webkit-touch-callout: none;
		-webkit-user-select: none;
		-moz-user-select: none;
		-ms-user-select: none;
		user-select: none;
	}

	/deep/ .wv-lt-location a {
		display: none;
	}
</style>
