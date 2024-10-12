<template>

	<div>
		<el-row>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div ref="workTypeEchart" class="title">工种答题占比统计</div>
					<div id="workTypeEchart" style="height:340px;" v-show="gzlist.length>0"></div>
					<div class="zwsj" v-if="gzlist.length==0">
						暂无数据
					</div>
				</el-card>
			</el-col>

			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div class="title">试题类别正确率占比</div>
					<div id="scopeByTestType" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 5px 10px;">
					<div ref="scopeByTen" class="title">答题平均分前十名</div>
					<div id="scopeByTen" style="height:340px;" v-show="dtlist.length>0"></div>
					<div class="zwsj" v-if="dtlist.length==0">
						暂无数据
					</div>
				</el-card>
			</el-col>

			<el-col :span="12">
				<el-card class="box-card" style="margin: 5px 10px;">
					<div ref="videoWatchCounts" class="title">课题学习占比分析</div>
					<div id="videoWatchCounts" style="height:340px;" v-show="kclist.length>0"></div>
					<div class="zwsj" v-if="kclist.length==0">
						暂无数据
					</div>
				</el-card>
			</el-col>

		</el-row>
	</div>
</template>

<script type="text/javascript">
	import * as echarts from 'echarts';
	import request from '@/utils/request'
	import { getMapByTestTypeToEcharts } from '../api/questTest'

	export default {
		// 教育答题统计
		name: 'wifiEchartsList',
		components: {

		},
		data() {
			return {
				loading: false, // 加载动画
				gzlist: [],
				dtlist: [],
				kclist: [],
				widthOne: '',
				widthScopeByTen: '',
				widthVideoWatchCounts: ''
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
			this.widthOne = this.$refs.workTypeEchart.offsetWidth + 'px'
			document.getElementById("workTypeEchart").style.width = this.widthOne
			this.widthScopeByTen = this.$refs.scopeByTen.offsetWidth + 'px'
			document.getElementById("scopeByTen").style.width = this.widthScopeByTen
			this.widthVideoWatchCounts = this.$refs.videoWatchCounts.offsetWidth + 'px'
			document.getElementById("videoWatchCounts").style.width = this.widthVideoWatchCounts
			this.initData(this.$route)

		},
		methods: {
			//初始化数据
			initData(to) {
				getMapByTestTypeToEcharts().then(res => {
					let data = res.data

					this.initWorkTypeEchart(data.echartsWorkTypeList) //工种答题占比分析--饼状图--初始化图案
					this.initBarChart(data.scopeByTestType) //试题得分分析：按试题类型分类，柱状图代表总分，折线图代表平均分---初始化图案
					this.scopeByTenChart(data.scopeByTen) //答题人员排名，前10--横向柱状图
					this.videoWatchCountsEchart(data.videoWatchCounts) //4、视频观看次数占比--饼状图

				})

			},

			//1、工种答题占比分析--饼状图--初始化图案
			initWorkTypeEchart(data) {
				this.gzlist = data
				var chartDom = document.getElementById('workTypeEchart');
				var myChart = echarts.init(chartDom, 'light');

				let option = {
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
						radius: '80%',
						data: data,
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				}

				myChart.setOption(option, true);

			},

			//2、试题得分分析：按试题类型分类，柱状图代表总分，折线图代表平均分---初始化图案
			initBarChart(data) {

				var chartDom = document.getElementById('scopeByTestType');
				var myChart = echarts.init(chartDom, 'light');

				let option = {
					color: ['#77A7F8', '#FAB262'],
					legend: {
						data: ['试题数量', '正确率']
					},
					tooltip: {
						trigger: 'axis'
					},
					xAxis: {
						type: 'category',
						// name: '试题类型',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},

						axisLabel: {
							color: '#000',
							interval: 0,
							rotate: 30
						},
						data: data.typeNameList
					},
					yAxis: [{
							type: 'value',
							name: '试题数量',
							axisLine: {
								show: false
							},
							min: 0,
							max: 20,
							interval: 4,
						},
						{
							type: 'value',
							name: '正确率',
							axisLine: {
								show: false
							},
							min: 0,
							max: 100,
							interval: 20,
							axisLabel: {
								formatter: '{value} %'
							}
						}
					],
					series: [{
						name: '试题数量',
						data: data.allScope,
						barWidth: 20,
						type: 'bar'
					}, {
						name: '正确率',
						data: data.avgScopList,
						type: 'line',
						yAxisIndex: 1
					}]
				}

				myChart.setOption(option, true);

			},

			//3、答题人员排名，前10--横向柱状图
			scopeByTenChart(data) {
				this.dtlist = data.scopeList
				var chartDom = document.getElementById('scopeByTen');
				var myChart = echarts.init(chartDom, 'light');

				var emphasisStyle = {
					itemStyle: {
						barBorderWidth: 1,
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowOffsetY: 0,
						shadowColor: 'rgba(0,0,0,0.5)'
					}
				};

				let option = {
					title: {
						subtext: '平均得分'
					},
					tooltip: {
						trigger: 'axis'
					},
					xAxis: {
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},
						axisLabel: {
							color: '#000',

						},
						name: '答题人员',
						data: data.peopleNameList
					},
					yAxis: {
						type: 'value',
						axisLine: {
							show: false
						},
						axisLabel: {
							formatter: '{value} '
						}
					},
					series: [{
						data: data.scopeList,
						type: 'bar'
					}]
				}
				myChart.setOption(option, true);

			},

			//4、视频观看次数占比--饼状图
			videoWatchCountsEchart(data) {
				this.kclist = data
				var chartDom = document.getElementById('videoWatchCounts');
				var myChart = echarts.init(chartDom, 'light');

				let option = {
					title: {
						text: '课程学习占比分析',
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
						name: '课程库名称',
						type: 'pie',
						radius: '80%',
						data: data,
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				}

				myChart.setOption(option, true);

			},

		},

	}
</script>

<style scoped>
	.title {
		font-size: 20px;
		font-weight: bold;
	}
	
	.zwsj {
		height: 320px;
		margin-top: 20px;
		text-align: center;
		font-size: 12px;
		color: #999;
	}
</style>