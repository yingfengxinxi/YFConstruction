<template>

	<div>
		<el-row>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div id="firstMain" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>

			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div id="warningMain" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 5px 10px;">
					<div id="kqsd" style="height:340px;"></div>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div id="secondMain" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script type="text/javascript">
	import * as echarts from 'echarts';
	import request from '@/utils/request'
	import { selectPieData, selectBarData, selectWarningData, selectKqsdfxData } from '../api/sprayStatistical'

	export default {
		name: 'sprayStatistical',
		components: {

		},
		data() {
			return {
				loading: false, // 加载动画
				pieOption: null,
				titleName: '',
			}
		},
		watch: {
			//监听页面切换
			'$route' (to, from) {
				//TODO 重新加载数据
				this.initData(to)
			}
		},
		created() {

		},
		mounted() {
			this.initData(this.$route)

			this.initPieChart() //饼状图图案
			this.initBarChart() //柱状图图案
			this.initWarningChart() //整改超时预警
			this.initKqsdfxBarChart() // 开启时段柱状图分析

		},
		methods: {
			//初始化数据
			initData(to) {
				this.titleName = '喷淋设备在线率分析'

				this.initPieList() //饼状图数据
				//this.initBarList()//柱状图数据
				//this.initWarningList()//整改超时预警数据

			},

			//饼状图初始化图案
			initPieChart() {

				var chartDom = document.getElementById('firstMain');
				var myChart = echarts.init(chartDom, 'light');
				var option;

				option = {
					title: {
						text: this.titleName,
						// subtext: 'top5',
					},
					tooltip: {
						trigger: 'item'
					},
					legend: {
						orient: 'vertical',
						right: '0%',
					},
					series: [{
						name: '项目',
						type: 'pie',
						radius: '50%',
						data: [],
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				}

				option && myChart.setOption(option, true);
				this.pieOption = option
				this.pieChart = myChart
			},

			//初始化饼状图数据
			initPieList() {
				selectPieData().then(response => {

					this.pieOption.title.text = this.titleName
					this.pieOption.series[0].data = response.data
					this.pieChart.setOption(this.pieOption, true)
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},

			//柱状图初始化图案
			initBarChart() {

				var chartDom = document.getElementById('secondMain');
				var myChart = echarts.init(chartDom, 'light');
				var option;

				var emphasisStyle = {
					itemStyle: {
						barBorderWidth: 1,
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowOffsetY: 0,
						shadowColor: 'rgba(0,0,0,0.5)'
					}
				};
				let brs = []
				let zs = []
				let xname = []
				selectBarData().then(re => {
					let data = re.data
					data.forEach(function(item) {
						brs.push(item.brs)
						zs.push(item.zs)
						xname.push(item.projetName)
					})
					option = {
						title: {
							text: '项目扬尘报警数量统计'
						},
						legend: {},
						tooltip: {},
						xAxis: {
							data: xname,
							axisLabel: {
								rotate: '45',
							}
						},
						yAxis: {
							type: 'value',
							min: 0,
							axisLabel: {
								formatter: '{value} 个'
							}
						},
						// Declare several bar series, each will be mapped
						// to a column of dataset.source by default.
						series: [{
							name:'今日报警数',
							type: 'bar',
							data:brs
						}, {
							name:'总报警数',
							type: 'bar',
							data:zs
						}]
					};

					option && myChart.setOption(option, true);
					this.barOption = option
					this.barChart = myChart
					this.loading = false
				}).catch(() => {
					this.loading = false
				})

			},

			//柱状图初始化图案
			initKqsdfxBarChart() {

				var chartDom = document.getElementById('kqsd');
				var myChart = echarts.init(chartDom, 'light');
				var option;

				var emphasisStyle = {
					itemStyle: {
						barBorderWidth: 1,
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowOffsetY: 0,
						shadowColor: 'rgba(0,0,0,0.5)'
					}
				};
				var dataBarChart = [];
				var csh = []
				csh.push('product')
				csh.push('开启数量')
				dataBarChart.push(csh)
				selectKqsdfxData().then(re => {
					let data = re.data
					data.forEach(function(element) {
						var k = [];
						k.push(element.name)
						k.push(element.value)
						dataBarChart.push(k)
					})
					option = {
						title: {
							text: '近24小时开启时段分析'
						},
						legend: {},
						tooltip: {},
						dataset: {
							source: dataBarChart
						},
						xAxis: {
							type: 'category'
						},
						yAxis: {
							type: 'value',
							min: 0,
							axisLabel: {
								formatter: '{value} 个'
							}
						},
						// Declare several bar series, each will be mapped
						// to a column of dataset.source by default.
						series: [{
							type: 'bar'
						}]
					};

					option && myChart.setOption(option, true);
					this.barOption = option
					this.barChart = myChart
					this.loading = false
				}).catch(() => {
					this.loading = false
				})

			},

			//初始化柱状图数据
			initBarList() {

				//获取柱数值信息及X轴
				let params = {
					type: '1'
				}
				selectBarData(params).then(re => {
					let data = re.data
					let dataX = []
					let dataY = []

					data.forEach(function(element) {
						dataX.push(element.clickDate)
						dataY.push(element.value)
					})

					this.barOption.xAxis.data = dataX
					this.barOption.series[0].data = dataY
					this.barChart.setOption(this.barOption, true)
					this.loading = false
				}).catch(() => {
					this.loading = false
				})

			},

			// 整改超时预警饼状图初始化图案
			initWarningChart() {

				var chartDom = document.getElementById('warningMain');
				var myChart = echarts.init(chartDom, 'light');
				var option;
				var dataBZsc = [];
				var dataBPjsc = [];
				var projectName = [];
				selectWarningData().then(response => {

					let data = response.data
					data.forEach(function(element) {
						projectName.push(element.xmmc)
						dataBZsc.push(element.zsc)
						dataBPjsc.push(element.pjz)
					})

					option = {
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						title: {
							text: '设备喷淋时长分析'
						},
						legend: {
							data: ['总时长', '平均时长']
						},
						xAxis: [{
							type: 'category',
							data: projectName,
							axisPointer: {
								type: 'shadow'
							},
							axisLabel: {
								rotate: '45',
							}
						}],
						yAxis: [{
								type: 'value',
								name: '总时长',
								min: 0,
								max: Math.ceil(Math.max.apply(null, dataBZsc) * 1.2 / 5) * 5,
								interval: Math.ceil(Math.max.apply(null, dataBZsc) * 1.2 / 5),
								axisLabel: {
									formatter: '{value} 小时',

								}
							},
							{
								type: 'value',
								name: '平均时长',
								min: 0,
								max: Math.ceil(Math.max.apply(null, dataBPjsc) * 1.2 / 5) * 5,
								interval: Math.ceil(Math.max.apply(null, dataBPjsc) * 1.2 / 5),
								axisLabel: {
									formatter: '{value} 小时'
								}
							}
						],
						series: [{
								name: '总时长',
								type: 'bar',
								data: dataBZsc
							},

							{
								name: '平均时长',
								type: 'line',
								yAxisIndex: 1,
								data: dataBPjsc
							}
						]
					};

					option && myChart.setOption(option, true);
					this.warningOption = option
					this.wargingChart = myChart

					this.loading = false
				}).catch(() => {
					this.loading = false
				})

			},

			//初始化整改超时预警饼状图数据
			initWarningList() {
				selectWarningData().then(response => {

					this.warningOption.series[0].data = response.data
					this.wargingChart.setOption(this.warningOption, true)
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},

		},

	}
</script>>