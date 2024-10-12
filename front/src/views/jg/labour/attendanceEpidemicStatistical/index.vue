<template>
	<div>
		<div style="width: 70%;float:left;height: 720px;">
			<el-row>
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px">
						<div id="jcctj" style="width: 100%;height:330px;"></div>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px;">
						<div id="rsqs" style="height:330px;"></div>
					</el-card>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px">
						<div id="rszb" style="width: 100%;height:330px;"></div>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px;">
						<div id="hszb" style="height:330px;"></div>
					</el-card>
				</el-col>
			</el-row>
		</div>
		<div style="width: 30%;float:left;">
			<el-table :data="dataList" style="height: 760px;top: 10px" @sort-change="sortChange">
				<el-table-column label="序号" type="index" align='center' width="70"></el-table-column>
				<el-table-column label="项目名称" align="center" prop="projectName" width="120" sortable="custom" />
				<el-table-column label="红码数量" align="center" prop="hhmrs" width="100" sortable="custom" />
				<el-table-column label="黄码数量" align="center" prop="hmrs" width="100" sortable="custom" />
				<el-table-column label="核酸超时人员" align="center" prop="hs3" width="120" sortable="custom" />
			</el-table>
		</div>
	</div>

</template>

<script>
	import * as echarts from 'echarts';
	import { sortColumn } from '@/utils'
	import request from '@/utils/request'
	export default {
		data() {
			return {
				dataList: [],
				queryParams:{}
			}
		},
		mounted() {
			this.getList()
		},
		methods: {
			sortChange(column) {
				sortColumn(this, column);
			},
			getList() {
				request({
					url: 'build/bLabourAttendanceEpidemic/attendanceEpidemicStatistical',
					method: 'post',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(response => {
					//console.log(response.data)
					this.rszb(response.data)
					this.hszb(response.data)
					this.jcctj(response.data.attendanceEpidemicStatistical)
					this.rsqs(response.data.attendanceEpidemicStatistical)
					this.dataList = response.data.attendanceEpidemicStatisticalList
				}).catch(() => {

				})
			},
			jcctj(data) {
				let xAxisData = []
				let seriesData = []
				let seriesData2 = []
				data.forEach(item => {
					xAxisData.push(item.date)
					seriesData.push(item.jczs)
					seriesData2.push(item.cczs)
				})
				let myChart = echarts.init(document.getElementById('jcctj'));
				let option = {
					title: {
						text: '近七日进出场人数统计',
					},
					grid: {
						bottom: '3%',
						containLabel: true
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
						data: xAxisData
					},
					yAxis: {
						type: 'value',
						axisLine: {
							show: false
						}
					},
					series: [{
						data: seriesData,
						type: 'line',
						name: '进场数量'
					}, {
						data: seriesData2,
						type: 'line',
						name: '出场数量'
					}]
				}
				myChart.setOption(option)
			},
			rsqs(data) {
				let xAxisData = []
				let seriesData = []
				data.forEach(item => {
					xAxisData.push(item.date)
					seriesData.push(item.wdzs)
				})
				let myChart = echarts.init(document.getElementById('rsqs'));
				let option = {
					title: {
						text: '近七日超37°人数趋势分析',
					},
					grid: {
						bottom: '3%',
						containLabel: true
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
						data: xAxisData
					},
					yAxis: {
						type: 'value',
						axisLine: {
							show: false
						}
					},
					series: [{
						data: seriesData,
						type: 'line',
						name: '超37°人数'
					}]
				}
				myChart.setOption(option)
			},
			rszb(data) {
				var data = [{
					name: '绿码',
					value: data.ls
				}, {
					name: '黄码',
					value: data.hs
				}, {
					name: '红码',
					value: data.hhs
				}]
				let myChart = echarts.init(document.getElementById('rszb'));
				let option = {
					title: {
						text: "近七日健康码人数占比",
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a}:{d}<br/>{b}:{c}'
					},
					legend: {

						left: 'center',
						bottom: '10'
					},
					series: [{
						name: '占比',
						type: 'pie',
						radius: '50%',
						// center: ['50%', '60%'],
						data: data,
					}]
				}
				myChart.setOption(option)

			},
			hszb(data) {
				var data = [{
					name: '48小时内',
					value: data.hs1
				}, {
					name: '72小时内',
					value: data.hs2
				}, {
					name: '超72小时',
					value: data.hs3
				}]
				let myChart = echarts.init(document.getElementById('hszb'));
				let option = {
					title: {
						text: "近七日核酸人数占比",
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a}:{d}<br/>{b}:{c}'
					},
					legend: {

						left: 'center',
						bottom: '10'
					},
					series: [{
						name: '占比',
						type: 'pie',
						radius: '50%',
						// center: ['50%', '60%'],
						data: data,
					}]
				}
				myChart.setOption(option)

			}
		},

	}
</script>

<style scoped>

</style>