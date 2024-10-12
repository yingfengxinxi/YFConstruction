<template>
	<div id="cloudCenter" class="crm-workbench">
		<!--  	<div class="titleHeader">-->
		<!--  		麦岛居住区改造E区项目-->
		<!--  	</div>-->
		<div class="content">
			<!-- 左边板块 -->
			<div class="layoutleft">

				<!--echarts-->
				<div class="projtype">
					<!--项目类型-->
					<div class="leftprojtypetitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">项目类型</div>
					<div id="main1"></div>

					<div class="projtype_linr"></div>
					<!-- 项目进度 -->
					<div class="leftprojtypetitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">项目进度</div>
					<div id="main2"></div>
				</div>
				<!--今日施工现场在场总人数-->
				<div class="PMbox">
					<div class="subject subtitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">今日施工在场总人数</div>
					<div id="concentration">

					</div>
				</div>
			</div>
			<!--右边板块-->
			<div class="layoutright">
				<!-- top -->
				<div class="projectdetails">
					<div class="projAll projAll_red" @click="goToProjectInfo">
						<div class="projAll_title1">项目总数</div>
						<div class="projAll_title2" id="projectCount" style="cursor:pointer">155个</div>
					</div>
					<div class="projAll projAll_yellow" @click="goToProjectInfo">
						<div class="projAll_title1">工程总造价</div>
						<div class="projAll_title2" id="projectAllCost" style="cursor:pointer">158,000万元</div>
					</div>
					<div class="projAll projAll_green" @click="goToProjectInfo">
						<div class="projAll_title1">劳务总人数</div>
						<div class="projAll_title2" id="labourNum" style="cursor:pointer">112,000人</div>
					</div>
					<div class="projAll projAll_blue" @click="goToProjectInfo">
						<div class="projAll_title1">在建项目</div>
						<div class="projAll_title2" id="projectSta" style="cursor:pointer">125个</div>
					</div>
				</div>
				<!-- left -->
				<div class="mapbox" style="float: left;">
					<!--地图-->
					<div class="leftmap">
						<b-map :projectList="projectList"></b-map>
					</div>
					<!--项目明细-->
					<div class="progress">
						<el-table v-loading="loading" :data="projectList" @row-click="projectRowClick" style="cursor:pointer">
							<el-table-column type="index" align="left" label="序号" width="80px"></el-table-column>
							<el-table-column label="项目名称" align="left" prop="projectName" />
							<!-- <el-table-column label="项目地址" align="left" prop="projectAddress" width="240px" /> -->
							<!--                        <el-table-column label="项目区域" align="left" prop="province"  :formatter="areaFormat" />-->
							<el-table-column label="总造价（万元）" align="left" prop="totalCost" width="140px" />
							<el-table-column label="总面积（平方米）" align="left" prop="totalArea" width="140px" />
							<el-table-column label="项目分类" align="left" prop="projectClassify" :formatter="projectClassifyFormat" width="120px" />
						</el-table>
					</div>
				</div>
				<!-- right -->
				<div class="rightbox" style="float: right;">
					<div class="rightClass">
						<div class="leftprojtypetitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">【中建】36号地铁一期项目</div>

						<div class="projShow">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="projShow_img" src="../../../../static/img/videobg1.jpg">
							<div class="projShow_date">2021-10-11 星期四 12:34:06</div>
						</div>

					</div>
					<div class="danger">
						<div class="leftprojtypetitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">隐患
							<!-- <select class="projectSelect">
                          <option>【中建】36号地铁一期项目</option>
                      </select> -->

						</div>
						<el-dropdown @command="chengePeoject" size="medium" split-button class="projectSelect">
							<span class="el-dropdown-link">
                            {{userProjName }}
                        </span>
							<el-dropdown-menu slot="dropdown">
								<el-dropdown-item v-for="(item, index) in userProListData" :key="index" :command="item">
									{{item.projectName }}
								</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
						<div class="fourColorBox">
							<div class="blueShow">
								<div class="blueShow_num" style="color: #3397FE;text-align: center;" id="jiancha">66</div>
								<div class="blueShow_title">检查数</div>
							</div>

							<div class="pupleShow">
								<div class="blueShow_num" style="color: #286DFF;text-align: center;" id="wenti">325</div>
								<div class="blueShow_title">问题数</div>
							</div>

							<div class="yellowShow">
								<div class="blueShow_num" style="color: #FF8F13;text-align: center;" id="zhenggaiWanLV">11%</div>
								<div class="blueShow_title">整改完成率</div>
							</div>

							<div class="greenShow">
								<div class="blueShow_num" style="color: #08D15E;text-align: center;">100%</div>
								<div class="blueShow_title">及时整改率</div>
							</div>
						</div>

						<div class="echartsTab">
							<div class="tab-bar">
								<span @click="tabbar(1)" class="qualTitle zlrange" :class="{active:1==index}">质量隐患<div :class="{linerange:1==index}"></div></span>
								<span @click="tabbar(2)" class="qualTitle aqrange" :class="{active:2==index}">安全隐患<div :class="{linerange:2==index}"></div></span>
								<div class="line_heng"></div>
							</div>
							<div class="tab-con" ref='tabcon'>
								<div v-show="1==index">
									<div id="qul_hiddenTroubleEcharts"></div>
								</div>
								<div v-show="2==index">
									<div ref='echartstwo' id="safe_hiddenTroubleEcharts"></div>

								</div>

							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import bMap from './components/map'
	import * as echarts from 'echarts';

	import { queryProjTypeMapListAll, queryProjNoPageList, selectAttendanceDataByProject, selectProgress, selectYinHuanFour } from './api/allData'
	import { selectAreaList, userProListAPI } from '@/api/common'
	import { queryMenuListByPath } from '@/api/admin/menu'
	import { selectSafetyHiddenDanger, selectQualHiddenDanger } from '../../crm/workbench/workbench'

	import { mapGetters } from 'vuex'

	export default {
		name: 'allData',
		components: {
			bMap
		},
		data() {
			return {
				loading: false,

				index: 1, //隐患：tab页

				projTypeEchart: [], //项目类型Echart
				projTypeEchartOption: [], //项目类型Echart

				projectList: [], //项目明细list
				projectClassifyOptions: [], //项目类型字典项
				provinceOptions: [], //省
				cityOptions: [], //市

				userProListData: [], //隐患模板右边选择框  项目
				userProjName: '',
				userProjId: 0,

			}
		},

		created() {
			//项目类型字典项
			this.getDicts('b_project_classify').then(response => {
				this.projectClassifyOptions = response.data
			})

			//省
			this.getProvinceList()
			//市
			this.getCityList()

		},

		computed: {
			...mapGetters([
				'userInfo',
			]),
		},

		mounted() {
			this.$refs.echartstwo.style.height = this.$refs.tabcon.offsetHeight + 'px'

			//项目类型echarts
			this.drawChart()
			//今日施工现场在场总人数实时数据echatrs
			// this.PMchart()

			//this.resetScreenSize ()
			this.remsize()

			this.projTypeEchatsData()
			//项目明细
			this.queryProjNoPageList()

		},

		beforeDestroy() {

		},
		methods: {

			//项目类型、项目进度echarts
			drawChart() {
				let myChart = echarts.init(document.getElementById("main1"), null, {
					devicePixelRatio: 2
				});

				let option = {
					tooltip: {
						trigger: 'item'
					},
					// legend: {
					//     top: '20',
					//     right: '20',
					//     icon: "circle",
					//     orient: 'vertical',
					//      textStyle: {
					// 	          fontSize:'12'
					// 	        },
					// },
					series: [{
						name: '项目类型',
						type: 'pie',
						radius: ['40%', '60%'],
						center: ['40%', '50%'],
						color: ['#77B8F8', '#73DCA0', '#A89FFF', '#FF9FE3', '#FFCB87', '#FF5353', '#536CFF'],

						avoidLabelOverlap: false,
						itemStyle: {
							borderRadius: 10,
							borderColor: '#fff',
							borderWidth: 2
						},
						label: {
							show: true,
							position: 'outside'
						},
						emphasis: {
							label: {
								show: true,
								fontSize: '12',
								fontWeight: 'bold'
							}
						},
						labelLine: {
							show: true
						},
						data: [
							// {value: 39, name: '房屋建筑工程'},
							// {value: 17, name: '市政公用工程'},
							// {value: 39, name: '机电安装工程'},
							// {value: 17, name: '铁路工程'},
							// {value: 39, name: '公路工程'},
							// {value: 17, name: '港口与航道工程'},
							// {value: 39, name: '水利水电工程'},
						]
					}]
				};
				myChart.setOption(option);
				myChart.on('click', params => {
					//console.log(params)
					if(params.componentType === 'title' || params.componentType === 'series') {
						this.goToProjectInfo()
					}
				})
				this.projTypeEchartOption = option
				this.projTypeEchart = myChart

				//项目进度echarts
				let myChart2 = echarts.init(document.getElementById("main2"), null, {
					devicePixelRatio: 2
				});
				let option2 = {
					tooltip: {
						trigger: 'item'
					},
					// legend: {
					//     top:'20',
					//     right: '30',
					//     icon: "circle",
					//     orient: 'vertical',
					// },
					series: [{
						name: '项目进度',
						type: 'pie',
						radius: '60%',
						center: ['40%', '50%'],
						color: ['#5B96FA', '#73DCA0', '#A89FFF', '#FF9FE3'],
						data: [],
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				};

				selectProgress().then(res => {
					let data = res.data
					option2.series[0].data = [{
							value: data.notStart,
							name: '未开始'
						},
						{
							value: data.doing,
							name: '进行中'
						},
						{
							value: data.done,
							name: '已完成'
						},
						{
							value: data.pastTime,
							name: '已超期'
						},
					]

					myChart2.setOption(option2, true);
					myChart2.on('click', params => {
						//console.log(params)
						if(params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('planCreate')
						}
					})

					//给右上角四个木块赋值
					let rightFour = data.rightFour
					document.getElementById("projectCount").innerHTML = rightFour.projectCount
					document.getElementById("projectAllCost").innerHTML = rightFour.projectAllCost
					document.getElementById("labourNum").innerHTML = rightFour.labourNum
					document.getElementById("projectSta").innerHTML = rightFour.projectSta

				})

			},

			// 项目类型echarts数据
			projTypeEchatsData() {
				queryProjTypeMapListAll().then(res => {

					this.projTypeEchartOption.series[0].data = res.data
					this.projTypeEchart.setOption(this.projTypeEchartOption, true)
					this.loading = false

				})
			},

			//今日施工现场在场总人数实时数据echatrs
			PMchart() {
				let myChart = echarts.init(document.getElementById("concentration"), null, {
					devicePixelRatio: 2
				});
				let option = {
					tooltip: {
						trigger: 'axis',
						formatter: '{b}<br/>{a0}：{c0}人<br/>'
					},
					grid: {
						top: '15%',
						left: '15%',
						right: '12%',
						bottom: '22%',
					},
					legend: {
						top: '0',
						right: '9'
					},
					color: ["#27BEFF"],
					xAxis: [{
						axisLabel: {
							color: '#000',
							width: 100,

							// interval: 0,//X轴斜向展示字符
							// rotate: 30,

							formatter: function(value) {
								var str = "";
								var num = 4; //每行显示字数
								var valLength = 10; //该项x轴字数
								var rowNum = Math.ceil(valLength / num); // 行数

								if(rowNum > 1) {
									for(var i = 0; i < rowNum; i++) {
										var temp = "";
										var start = i * num;
										var end = start + num;

										temp = value.substring(start, end) + "\n";
										str += temp;
									}

									return str;
								} else {
									return value;
								}
							}
						},
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},

						splitLine: {
							show: false
						},
						boundaryGap: false,
						// data: ["单位名称","单位名称","单位名称","单位名称","单位名称","单位名称","单位名称"]//this.$moment(data.times).format("HH-mm") ,
						data: []
					}],

					yAxis: [{
						type: 'value',
						min: 0,
						// max: 140,
						splitNumber: 4,
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000000',
								opacity: 0.15
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false,
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000',
							}
						},
						axisTick: {
							show: false,
						},
					}],
					series: [{
							name: '人数',
							type: 'line',
							symbolSize: 10,
							lineStyle: {
								normal: {
									color: "#27BEFF",
								},
							},
							areaStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
											offset: 0,
											color: 'rgba(39,190,255,0.8)'
										},
										{
											offset: 1,
											color: 'rgba(39,190,255,0)'
										}
									], false),
								}
							},
							symbol: 'circle', //实心点
							// data: [20,19,25,24,23,25,28]
							data: []
						},

					]
				};

				selectAttendanceDataByProject(this.userProListData).then(res => {

					var data = res.data
					var xData = []
					var dataA = []

					data.forEach(item => {
						xData.push(item.projectName)
						dataA.push(item.attendanceNum)

					})

					option.xAxis[0].data = xData
					option.series[0].data = dataA

					myChart.setOption(option, true)
					/* myChart.getZr().on('click',params=>{
					    console.log(params)
					}) */

					myChart.on('click', params => {
						//console.log(params)
						if(params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('attendanceDetail')
						}
					})

				})

			},

			//隐患信息echarts
			drawHiddenTroubleEchart() {

				//隐患 四个块数据
				this.selectYinHuanFour()

				var chartDom = document.getElementById('qul_hiddenTroubleEcharts');
				var safe_chartDom = document.getElementById('safe_hiddenTroubleEcharts');

				var myChart = echarts.init(chartDom, 'light', {
					devicePixelRatio: 2
				});
				var safe_myChart = echarts.init(safe_chartDom, 'light', {
					devicePixelRatio: 2
				});

				var option;
				var safe_option;

				var emphasisStyle = {
					itemStyle: {
						barBorderWidth: 1,
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowOffsetY: 0,
						shadowColor: 'rgba(0,0,0,0.5)'
					}
				};

				option = {

					color: ['#77A7F8', '#FAB262'],
					legend: {
						data: ['总数', '未解决'],
						top: '10',
						right: '14',
						icon: 'circle',

					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						top: '20%',
						left: '10%',
						right: '10%',
						bottom: '25%',
					},
					xAxis: {
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},
						axisLabel: {
							interval: 0,
							fontSize: 10,
							formatter: function(value) {
								var str = "";
								var num = 4; //每行显示字数
								var valLength = value.length; //该项x轴字数
								var rowNum = Math.ceil(valLength / num); // 行数

								if(rowNum > 1) {
									for(var i = 0; i < rowNum; i++) {
										var temp = "";
										var start = i * num;
										var end = start + num;

										temp = value.substring(start, end) + "\n";
										str += temp;
									}

									return str;
								} else {
									return value;
								}
							}
						},
						//data: ['石方施工', '基坑施工', '主体施工', '装饰施工', '施工收尾'],
						data: [],
					},
					yAxis: [{
						type: 'value',
						name: '质量隐患',
						axisLine: {
							show: false,

						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000',
								opacity: 0.2
							}
						},
						max: function(value) {
							return value.max < 5 ? 5 : value.max;
						}
					}],
					series: [{
							name: '总数',
							type: 'bar',
							// data: [ 7, 25, 76, 35, 62]
							data: []

						},
						{
							name: '未解决',
							type: 'bar',
							// data: [ 4, 20, 70, 30, 60]
							data: []
						}
					]
				};

				safe_option = {

					color: ['#77A7F8', '#FAB262'],
					legend: {
						data: ['总数', '未解决'],
						top: '10',
						right: '14',
						icon: 'circle',

					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						top: '20%',
						left: '10%',
						right: '10%',
						bottom: '25%',
					},
					xAxis: {
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},
						axisLabel: {
							interval: 0,
							fontSize: 10,
							formatter: function(value) {
								var str = "";
								var num = 4; //每行显示字数
								var valLength = value.length; //该项x轴字数
								var rowNum = Math.ceil(valLength / num); // 行数

								if(rowNum > 1) {
									for(var i = 0; i < rowNum; i++) {
										var temp = "";
										var start = i * num;
										var end = start + num;

										temp = value.substring(start, end) + "\n";
										str += temp;
									}

									return str;
								} else {
									return value;
								}
							}
						},
						//data: ['石方施工', '基坑施工', '主体施工', '装饰施工', '施工收尾'],
						data: []
					},
					yAxis: [{
						type: 'value',
						name: '安全隐患',
						axisLine: {
							show: false,
						},
						max: function(value) {
							return value.max < 5 ? 5 : value.max;
						}
					}],
					series: [{
							name: '总数',
							type: 'bar',
							// data: [ 11, 24, 55, 34, 61]
							data: []

						},
						{
							name: '未解决',
							type: 'bar',
							// data: [ 4, 12, 45, 20, 54]
							data: []
						}
					]
				};

				//质量隐患数据
				let map = {
					projectId: this.userProjId
				}
				selectQualHiddenDanger(map).then(res => {
					var data = res.data
					var xData = []
					var dataA = []
					var dataB = []

					data.forEach(item => {
						xData.push(item.name)
						dataA.push(item.count)
						dataB.push(item.unfinished)
					})

					option.xAxis.data = xData
					option.series[0].data = dataA
					option.series[1].data = dataB

					myChart.setOption(option, true)
					// 质量隐患绑定点击事件
					myChart.on('click', params => {
						//console.log(params)
						if(params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('qualCheck')
						}
					})
				});

				//安全隐患数据
				selectSafetyHiddenDanger(map).then(res => {
					var data = res.data
					var xData = []
					var dataA = []
					var dataB = []

					data.forEach(item => {
						xData.push(item.name)
						dataA.push(item.count)
						dataB.push(item.unfinished)
					})

					safe_option.xAxis.data = xData
					safe_option.series[0].data = dataA
					safe_option.series[1].data = dataB

					safe_myChart.setOption(safe_option, true)
					safe_myChart.on('click', params => {
						//console.log(params)
						if(params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('inspectionInfo')
						}
					})
				})

			},

			//隐患--四个块  检查数 问题数 整改率
			selectYinHuanFour() {
				selectYinHuanFour(this.userProjId).then(res => {

					let rightFour = res.data
					document.getElementById("jiancha").innerHTML = rightFour.jilu
					document.getElementById("wenti").innerHTML = rightFour.wenti
					if(rightFour.zhenggailV == null) {
						document.getElementById("zhenggaiWanLV").innerHTML = 0
					} else {
						document.getElementById("zhenggaiWanLV").innerHTML = rightFour.zhenggailV + '%'

					}
				})
			},

			tabbar(index) {
				this.index = index
			},

			//项目明细list，地图下方的table
			queryProjNoPageList() {
				this.loading = true

				queryProjNoPageList().then(response => {

					this.projectList = response.data

					//项目下拉框
					this.userProList()

					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},

			//项目类型
			projectClassifyFormat(row, column) {
				return this.selectDictLabel(this.projectClassifyOptions, row.projectClassify)
			},

			// 获取省级区划数据
			getProvinceList() {
				const param = {
					level: 1
				}
				selectAreaList(param).then(res => {
					this.provinceOptions = res.data
				})
			},
			// 获取市级区划数据
			getCityList(id) {
				const param = {
					parentId: id
				}
				selectAreaList(param).then(res => {
					this.cityOptions = res.data

				})
			},

			//区域formmater
			areaFormat(row, column) {
				var actions = []

				//省
				let datas = this.provinceOptions
				let value = row.province

				Object.keys(datas).some((key) => {
					if(datas[key].id == ('' + value)) {
						actions.push(datas[key].name)
						return true
					}
				})

				//市
				let datas2 = this.cityOptions
				let value2 = row.city

				Object.keys(datas2).some((key) => {
					if(datas2[key].id == ('' + value2)) {
						actions.push(datas2[key].name)
						return true
					}
				})

				return actions.join('')
			},

			//项目列表
			userProList() {
				var that = this;

				let data = this.projectList
				let data2 = []
				data.forEach(item => {

					let map = {
						projectId: item.id,
						projectName: item.projectName
					}
					data2.push(map)

					if(item.id == that.userInfo.projectId) {
						that.userProjId = item.id
						that.userProjName = item.projectName;
					}
				})

				this.userProListData = data2

				//隐患
				this.drawHiddenTroubleEchart()

				//今日施工现场在场总人数实时数据echatrs
				this.PMchart()

				//注意（不要删除先）：与b_project_user关联，目前暂时不用关联，所以project下拉框先使用地图下方列表的数据
				// userProListAPI().then(res => {

				//     this.userProListData = res.data

				//     this.userProjName = this.userProListData[0].projectName
				//     this.userProjId = this.userProListData[0].projectId

				//     //隐患
				//     this.drawHiddenTroubleEchart()

				//     //今日施工现场在场总人数实时数据echatrs
				//     this.PMchart()

				// })
			},
			//切换项目
			chengePeoject(item) {
				// console.log(item)
				this.userProjId = item.projectId
				this.userProjName = item.projectName

				//隐患
				this.drawHiddenTroubleEchart()
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
						var wScale = (window.innerWidth - 220) / (dw || 1920);
						// console.log(window.innerHeight, window.innerWidth)
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
			// 根据菜单路由跳转页面
			goToUrl(path) {
				let param = {
					path: path
				}
				// 查询菜单
				queryMenuListByPath(param).then(res => {
					let data = res.data
					if(data.length <= 0) {
						return;
					}
					let item = data[0]
					var activeclass = {
						close: true,
						title: item.menuName,
						comname: item.path,
						name: item.menuId
					}
					if(JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
						this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
					}
					this.$store.state.app.activeclass = activeclass.comname
					this.$router.push({
						name: activeclass.name
					})
				})
			},
			// 跳转到项目管理页面
			goToProjectInfo() {
				this.goToUrl('projectInfo')
			},
			// 行点击事件
			projectRowClick(row) {
				this.goToProjectInfo()
			}
		}
	}
</script>

<style scoped lang="scss">
	/deep/ .app-main {
		overflow: hidden!important;
	}
	
	.crm-workbench {
		height: 100%;
	}
	
	.content {
		padding: 0.2rem 0.3rem;
		font-weight: 500;
		height: 100%;
		display: flex;
		justify-content: space-around;
	}
	
	.layoutleft {
		float: left;
		height: 100%;
	}
	
	.projtype {
		width: 4.04rem;
		height: 60%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		padding: 12px 15px 16px 8px;
		border: 1px solid #E1E1E1;
	}
	
	.subject {
		height: 16px;
		font-size: 16px;
		font-weight: 500;
		color: #404040;
		display: flex;
		align-items: center;
		img {
			margin-right: 5px;
		}
	}
	
	.bgcolor {
		display: inline-block;
		margin-top: 5px;
		margin-left: 4px;
		width: 6px;
		height: 20px;
		border-radius: 4px;
	}
	
	.bgcolor0 {
		background: #1B65FA;
	}
	
	.bgcolor1 {
		background: #27BEFF;
	}
	
	.bgcolor2 {
		background: #18BAA0;
	}
	
	.bgcolor3 {
		background: #BDEB80;
	}
	
	.bgcolor4 {
		background: #F9C827;
	}
	
	.bgcolor5 {
		background: #FFA62A;
	}
	
	.bigcontent {
		vertical-align: top;
		font-size: 14px;
		margin-left: 14px;
		width: 325px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: inline-block;
	}
	
	.bigtime {
		font-size: 14px;
		float: right;
		margin-right: 7px;
	}
	
	.servicebox {
		width: 522px;
		height: 269px;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 20px;
	}
	
	#main1 {
		width: 100%;
		height: calc(50% - 28px);
	}
	
	#lineEcharts {
		width: 474px;
		height: 2px;
		border: 1px solid #E6E7E7;
	}
	
	#main2 {
		width: 100%;
		height: calc(50% - 16px);
	}
	
	.servicedetails {
		float: left;
		height: 241px;
		width: 190px;
		padding-left: 20px;
	}
	
	.servicetitle {
		font-size: 16px;
		font-weight: 600;
		color: #404040;
		margin-top: 4px;
	}
	
	.attendance {
		width: 159px;
		height: 45px;
		background: rgba(39, 190, 255, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 11px;
		line-height: 45px;
		.chartnum {
			color: #27BEFF;
		}
	}
	
	.allpeople {
		width: 159px;
		height: 45px;
		background: rgba(52, 119, 255, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 11px;
		line-height: 45px;
		.chartnum {
			color: #3477FF;
		}
	}
	
	.assessment {
		width: 159px;
		height: 45px;
		background: rgba(189, 235, 128, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 11px;
		line-height: 45px;
	}
	
	.charttitle {
		float: left;
		font-weight: 400;
		color: #404040;
		font-size: 14px;
		margin-left: 16px;
	}
	
	.chartnum {
		float: right;
		font-size: 26px;
		margin-right: 16px;
	}
	
	.PMbox {
		width: 4.02rem;
		height: 37.5%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 0.21rem;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
	}
	
	#concentration {
		width: 100%;
		height: calc(100% - 28px);
	}
	
	.subtitle {
		margin-left: 8px;
		margin-top: 12px;
	}
	
	.everydata {
		width: 436px;
		float: left;
		height: 79px;
		padding-left: 57px;
	}
	
	.line {
		float: left;
		width: 1px;
		height: 79px;
		border: 1px solid #E7E7E7;
	}
	
	.projecticon {
		float: left;
		width: 79px;
		height: 79px;
	}
	
	.everydata div {
		float: left;
		margin-left: 30px;
	}
	
	.protitle {
		margin-top: 12px;
		color: #404040;
		font-size: 14px;
	}
	
	.procontent {
		font-size: 24px;
		color: #404040;
		margin-top: 5px;
	}
	
	.legend {
		height: 22px;
		line-height: 22px;
		margin-top: 4px;
	}
	
	.projectday {
		float: left;
		color: #404040;
		font-size: 16px;
		font-weight: 500;
		margin-left: 235px;
	}
	
	.proday {
		color: #FA1B4E;
	}
	
	.legend ul {
		float: left;
		margin-left: 85px;
		li {
			float: left;
			margin-right: 18px;
		}
	}
	
	.circular {
		height: 12px;
		width: 12px;
		float: left;
		border-radius: 50%;
		margin-top: 5px;
		margin-right: 5px;
	}
	
	.color1 {
		background: #CFCDCD;
	}
	
	.color2 {
		background: #9EE05D;
	}
	
	.color3 {
		background: #84ABFF;
	}
	
	.color4 {
		background: #FFA62A;
	}
	
	.wcline {
		position: absolute;
		transform: translateX(-50%);
		left: 50%;
		top: 57px;
	}
	
	.wcicon {
		width: 58px;
		height: 58px;
		margin: 0 auto;
		display: block;
		margin-bottom: 15px;
	}
	
	.danChart {
		margin-top: 10px;
	}
	
	.leftprojtypetitle {
		// width: 64px;
		height: 16px;
		font-size: 16px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #404040;
		line-height: 16px;
		display: flex;
		align-items: center;
		img {
			margin-right: 5px;
			vertical-align: middle;
			height: 16px;
			width: 16px;
		}
	}
	
	.projtype_linr {
		width: 95%;
		height: 2px;
		background: #E6E7E7;
		margin: 0 auto;
		margin-bottom: 11px
	}
	
	// 右侧
	.layoutright {
		float: right;
		height: 100%;
		width: 12.08rem;
	}
	
	//   top
	.projectdetails {
		width: 12.08rem;
		height: 13.33%;
		display: flex;
	}
	
	.projAll {
		width: 3.02rem;
		height: 100%;
	}
	
	.projAll_red {
		background: url('../../../../static/img/ALL1.png') no-repeat;
		background-size: 100% 100%;
		;
	}
	
	.projAll_yellow {
		background: url('../../../../static/img/ALL2.png') no-repeat;
		background-size: 100% 100%;
	}
	
	.projAll_green {
		background: url('../../../../static/img/ALL3.png') no-repeat;
		background-size: 100% 100%;
	}
	
	.projAll_blue {
		background: url('../../../../static/img/ALL4.png') no-repeat;
		background-size: 100% 100%;
	}
	
	.projAll_title1 {
		margin-left: 0.44rem;
		margin-top: 0.16rem;
		height: 20px;
		font-size: 0.14rem;
		font-weight: 500;
		color: #FFFFFF;
		line-height: 20px;
	}
	
	.projAll_title2 {
		margin-left: 0.42rem;
		font-size: 0.3rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #FFFFFF;
	}
	
	// left
	.mapbox {
		width: 7.72rem;
		height: 86%;
	}
	
	.leftmap {
		width: calc(100% - 0.1rem);
		height: 60.75%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 0.08rem;
		margin-left: 0.1rem;
	}
	
	.progress {
		width: 7.62rem;
		height: 36%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 0.2rem;
		margin-left: 0.1rem;
		overflow-y: auto;
	}
	
	.deatil_ul {
		height: 17px;
		font-size: 12px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #666666;
		line-height: 17px;
	}
	
	// right
	.rightbox {
		width: 4.16rem;
		height: 86%;
	}
	
	.rightClass {
		width: 100%;
		height: 33.5%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		padding: 12px 15px 16px 8px;
		margin-top: 7px;
	}
	
	.projShow {
		width: 3.73rem;
		height: calc(100% - 28px);
		background: #928888;
		border-radius: 4px;
		margin: 0 auto;
		position: relative;
		margin-top: 17px
	}
	
	.projShow_img {
		width: 100%;
		height: 100%;
	}
	
	.projShow_date {
		position: absolute;
		bottom: 0;
		width: 482px;
		height: 40px;
		line-height: 40px;
		background: rgba(255, 255, 255, 0.88);
		font-size: 12px;
		color: #404040;
		padding-left: 15px;
	}
	
	// 隐患
	.danger {
		width: 100%;
		height: 64.25%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 0.2rem;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		padding: 12px 0 0 8px;
		position: relative;
	}
	
	.projectSelect {
		height: 6.03%;
		background: #E6F5FF;
		border-radius: 5px;
		position: absolute;
		font-size: 14px;
		right: 0.1rem;
		top: 0.08rem;
		font-weight: 400;
		color: #404040;
		line-height: 31px;
		float: right;
	}
	
	/deep/ .projectSelect .el-button {
		background-color: #E6F5FF;
	}
	
	.fourColorBox {
		display: flex;
		clear: both;
		height: 23.34%;
		flex-direction: column;
		flex-wrap: wrap;
		margin: 0 auto;
		margin-top: 0.3rem;
	}
	
	.fourColorLine_shu {
		width: 1px;
		height: 17px;
		border: 1px solid #DCDCDC;
		margin-top: 30px;
	}
	
	.blueShow {
		width: 1.85rem;
		height: calc(50% - 4px);
		background: rgba(39, 185, 255, 0.12);
		box-shadow: 0px 2px 6px 0px rgba(51, 151, 254, 0.36);
		border-radius: 5px;
		border: 2px solid #FFFFFF;
		margin-right: 0.1rem;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
	
	.blueShow_num {
		font-size: 0.26rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
	}
	
	.blueShow_title {
		font-size: 0.12rem;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		color: #404040;
	}
	
	.pupleShow {
		width: 1.85rem;
		height: calc(50% - 4px);
		background: rgba(39, 106, 255, 0.12);
		box-shadow: 0px 2px 6px 0px rgba(51, 163, 254, 0.36);
		border-radius: 5px;
		margin-top: 0.05rem;
		border: 2px solid #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
	
	.yellowShow {
		width: 1.85rem;
		height: calc(50% - 4px);
		background: rgba(255, 166, 39, 0.12);
		box-shadow: 0px 2px 6px 0px rgba(254, 131, 51, 0.36);
		border-radius: 5px;
		border: 2px solid #FFFFFF;
		margin-right: 0.05rem;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
	
	.greenShow {
		width: 1.85rem;
		height: calc(50% - 4px);
		background: rgba(39, 255, 128, 0.12);
		box-shadow: 0px 2px 6px 0px rgba(28, 225, 163, 0.36);
		border-radius: 5px;
		border: 2px solid #FFFFFF;
		display: flex;
		align-items: center;
		margin-top: 0.05rem;
		justify-content: space-evenly;
	}
	
	.line_heng {
		width: 100%;
		height: 1px;
		border: 1px solid rgba(0, 0, 0, 0.09);
		top: 30px;
		position: absolute;
	}
	
	.echartsTab {
		height: calc(70% - 0.23rem);
		margin-top: 0.13rem;
	}
	
	.tab-bar {
		display: flex;
		justify-content: center;
		flex-direction: row;
		flex-wrap: wrap;
		align-content: center;
		position: relative;
	}
	
	.tab-bar span {
		display: block;
		position: relative;
		float: left;
		margin-top: 10px
	}
	
	.zlrange {}
	
	.aqrange {
		margin-left: 0.63rem
	}
	
	.tab-bar span div {
		position: absolute;
		width: 29px;
		height: 2px;
		background: #3397FE;
		border-radius: 2px;
		left: 50%;
		transform: translateX(-50%);
		top: 20px;
		display: none
	}
	
	.tab-bar .linerange {
		display: block
	}
	
	.tab-bar .active {
		color: #3397FE
	}
	
	.qualTitle {
		width: 48px;
		height: 17px;
		font-size: 12px;
		font-family: PingFangSC-Medium, PingFang SC;
	}
	
	.tab-con {
		height: 80%;
		margin-top: 0.13rem;
	}
	
	.tab-con div {
		height: 100%;
		width: 4.16rem;
	}
	
	//echarts
	#qul_hiddenTroubleEcharts {
		height: 100%;
		width: 4.16rem;
	}
	
	#safe_hiddenTroubleEcharts {
		height: 100%;
		width: 4.16rem;
	}
</style>