<template>
	<div id="cloudCenter" class="crm-workbench">
		<!--  	<div class="titleHeader">-->
		<!--  		麦岛居住区改造E区项目-->
		<!--  	</div>-->
		<div class="content">
			<div class="layoutleft">
				<div class="alarm_statistics">
					<div class="subject subtitle gcjdu"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">报警统计</div>
					<div class="police_list_box">

						<div class="police_list police_list1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon1.png"/>
							<div class="police_name">塔机</div>
							<div class="police_num1">
								<div class="pol_tit">报警</div>
								<div class="pol_val " @click="gopolicelist('2')">{{shebinfo.towerCrane.alertNumber}}</div>
							</div>
							<div class="police_num2">
								<div class="pol_tit">总数</div>
								<div class="pol_val " @click="gopolicelist('2')">{{shebinfo.towerCrane.total}}</div>
							</div>
						</div>
						<div class="police_list police_list1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon2.png"/>
							<div class="police_name">升降机</div>
							<div class="police_num1">
								<div class="pol_tit">报警</div>
								<div class="pol_val " @click="gopolicelist('3')">{{shebinfo.lift.alertNumber}}</div>
							</div>
							<div class="police_num2">
								<div class="pol_tit">总数</div>
								<div class="pol_val " @click="gopolicelist('3')">{{shebinfo.lift.total}}</div>
							</div>
						</div>
						<!-- <div class="police_list police_list1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon3.png"/>
							<div class="police_name">烟感</div>
							<div class="police_num1">
								<div class="pol_tit">报警</div>
								<div class="pol_val " @click="gopolicelist('13')">{{shebinfo.somkeDetector.alertNumber}}</div>
							</div>
							<div class="police_num2">
								<div class="pol_tit">总数</div>
								<div class="pol_val " @click="gopolicelist('13')">{{shebinfo.somkeDetector.total}}</div>
							</div>
						</div> -->
						<div class="police_list police_list1">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon3.png"/>
							<div class="police_name">AI识别</div>
							<div class="police_num1">
								<div class="pol_tit">报警</div>
								<div class="pol_val " @click="gopolicelist('97')">{{shebinfo.ai.alertNumber}}</div>
							</div>
							<div class="police_num2">
								<div class="pol_tit">总数</div>
								<div class="pol_val " @click="gopolicelist('97')">{{shebinfo.ai.total}}</div>
							</div>
						</div>
						<div class="police_list police_list2">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon4.png"/>
							<div class="police_name">PM2.5</div>
							<div class="police_num1 fontcolor3">{{selectEnvDataAt.pm25.value?selectEnvDataAt.pm25.value:'-- / --'}}{{selectEnvDataAt.pm25.unit}}</div>
							<div @click="gopolicelist('9')"  class="mouse_style mouse_style1" style="background: #30BF78;">{{PMdata.pm25==1?'超标':'正常'}}</div>

						</div>
						<div class="police_list police_list2">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon5.png"/>
							<div class="police_name">PM10</div>
							<div class="police_num1 fontcolor4">{{selectEnvDataAt.pm10.value?selectEnvDataAt.pm10.value:'-- / --'}}{{selectEnvDataAt.pm10.unit}}</div>
							<div @click="gopolicelist('9')"  class="mouse_style mouse_style1" style="background: #FAAD14;">{{PMdata.pm10==1?'超标':'正常'}}</div>
						</div>
						<div class="police_list police_list2">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indexicon" src="../../../../static/img/indexicon6.png"/>
							<div class="police_name">噪音</div>
							<div class="police_num1 fontcolor5">{{selectEnvDataAt.noise.value?selectEnvDataAt.noise.value:'-- / --'}}{{selectEnvDataAt.noise.unit}}</div>
							<div @click="gopolicelist('9')"  class="mouse_style mouse_style1" style="background: #FA8414;">{{PMdata.noise==1?'超标':'正常'}}</div>
						</div>
					</div>
				</div>
				<div class="police_ss">
					<div class="police_more fontcolor1" @click="policeMore()">更多>></div>
					<div class="subject subtitle gcjdu"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">实时报警列表</div>
					<div class="jcc_box">
						<div class="jc_data">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/bjicon1.png"/>
							报警
						</div>
						<div class="jc_data">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/bjicon2.png"/>
							预警
						</div>
					</div>
					<div class="PMpolice">
						<div class="PMpolice_list" v-for="(item,index) in untreatedDataList">
							<div class="PM_content mouse_style" @click="seeData(item)">
								<div class="bjtype" :class="item.alertType==1?'pmlitile':'pmlitile2'">
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.alertType==1" src="../../../../static/img/bjicon1.png"/>
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else src="../../../../static/img/bjicon2.png"/>
									<el-tooltip :content="item.content" placement="top" effect="light">
										<div class="xswt">{{item.content}}</div>
									</el-tooltip>
								</div>
							</div>
							<div class="pmpval">{{item.pointName?item.pointName:item.assetName}}</div>
							<div class="pmplo_date">{{item.alertTimeFormat}}</div>
						</div>
						<div class="zwsj" v-if="untreatedDataList.length==0">
							暂无数据
						</div>
					</div>
				</div>
			</div>
			<!--右边板块-->
			<div class="layoutright">
				<div class="projectdetails">
					<div class="everydata" @click="detailsDataClick">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="projecticon" src="../../../../static/img/project1.png">
						<div>
							<p class="protitle">计划竣工时间</p>
							<p class="procontent" id="endTime" style="color: #649BF8;">2021年10月30日</p>
						</div>
					</div>

					<div class="everydata" @click="detailsDataClick">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="projecticon" src="../../../../static/img/project2.png">
						<div>
							<p class="protitle">工程面积</p>
							<p class="procontent" id="allAre" style="color: #FAAD14;">250,000平</p>
						</div>
					</div>

					<div class="everydata" style="margin-right: 0;" @click="detailsDataClick">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="projecticon" src="../../../../static/img/project3.png">
						<div>
							<p class="protitle">工程造价</p>
							<p class="procontent" id="allTost" style="color: #30BF78;">2,700,00万元</p>
						</div>
					</div>
				</div>
				<div class="videbox" style="float: left;">
					<!--视频-->
					<div class="video">
						<table-video />
					</div>
					<!--工程进度-->
					<div class="progress">
						<div class="subject subtitle gcjdu"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">工程进度</div>
						<div class="legend">
							<div class="projectday"><span class="protime">项目时长</span><span class="proday" id="allDate">309天</span>
							</div>
							<div>
								<ul>
									<li>
										<div class="circular color1" /><span>未开始</span>
									</li>
									<li>
										<div class="circular color3" /><span>进行中</span>
									</li>
									<li>
										<div class="circular color2 " /><span>已完成</span>
									</li>
									<li>
										<div class="circular color4" /><span>超时</span>
									</li>
								</ul>
							</div>
						</div>
						<div class="speed">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="wcline" v-if='speed.length>0' src="../../../../static/img/wcline.png">
							<!--<ul>
								<li v-for="item in speed">
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.status==1" class="wcicon" src="../../../../static/img/wc1.png">
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==2" class="wcicon" src="../../../../static/img/wc3.png">
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==3" class="wcicon" src="../../../../static/img/wc2.png">
									<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==4" class="wcicon" src="../../../../static/img/wc4.png">
								</li>
							</ul>-->
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="syleft" src="../../../../static/img/syleft.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="syright" src="../../../../static/img/syright.png" />
							<swiper :options="swiperOption" ref="mySwiper" v-if='speed.length>0'>
								<swiper-slide v-for="(data,index) in speed" :key="index+321">
									<ul>
										<li v-for="item in data" :key="item.actualEndDate">

											<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.status==1" class="wcicon" src="../../../../static/img/wc1.png">
											<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==2" class="wcicon" src="../../../../static/img/wc2.png">
											<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==3" class="wcicon" src="../../../../static/img/wc3.png">
											<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-else-if="item.status==4" class="wcicon" src="../../../../static/img/wc4.png">
											<el-tooltip :content="item.name" placement="top" effect="light">
												<div class="speedcon">
													<div class="speedcondata">{{ item.name }}</div>
													<div>{{ item.planStartDate }}</div>
												</div>
											</el-tooltip>

										</li>
									</ul>
								</swiper-slide>
							</swiper>
							<div class="zwsj" v-if="speed.length==0">
								暂无数据
							</div>
						</div>
					</div>

				</div>
				<div class="dangerboxa">
					<div class="servicebox">
						<div class="subject subtitle">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">劳务信息
						</div>
						<div id="main"></div>
						<div class="servicedetails">
							<div class="servicetitle">
								在场状态
							</div>
							<div class="attendance" @click="goToLabourInfo">
								<!--演示需放开下方的数据，注释绑定数据 -->
								<span class="charttitle">出勤人数</span>
								<!-- <span class="chartnum">39人</span> -->
								<span class="chartnum">{{attendanceNum}}</span>
							</div>
							<div class="allpeople" @click="goToLabourInfo">
								<span class="charttitle">总人数</span>
								<!-- <span class="chartnum">56人</span> -->
								<span class="chartnum">{{headcount}}</span>
							</div>
							<div class="assessment" @click="goToLabourInfo">
								<span class="charttitle">出勤率</span>
								<!-- <span class="chartnum">70%</span> -->
								<span class="chartnum">{{percent}}</span>
							</div>
						</div>
					</div>
					<div class="danger">
						<div class="subject subtitle"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../static/img/Symbol.png">隐患</div>
						<danger :projectId='userInfo.projectId' class="danChart" />
					</div>
				</div>
			</div>
		</div>
		<!-- 项目信息详情 对话框 -->
		<project-details-data v-if="open2" :userInfo='userInfo' @close="open2 = false"></project-details-data>

		<!--公告 查看详情 -->
		<component :is="componentName" v-if="showComDetail" :action="{type:'detail'}" :id="rowID" :businessType="businessType" :class="componentClass" @close="showComDetail=false" />

	</div>
</template>
<script>
	import { swiper, swiperSlide } from "vue-awesome-swiper";
	import "swiper/dist/css/swiper.css";
	import { queryMenuListByPath } from '@/api/admin/menu'
	import tableVideo from './components/tableVideo'
	import danger from './components/danger'
	import * as echarts from 'echarts';
	import DetailsData from './components/DetailsData'
	import ProjectDetailsData from './components/projInfoDetail'
	import {
		getProjectInfoById,
		selectByProjectId,
		getThree,
		selectProgress,
		selectNumber,
		selectEnvDataAt,
		selectStatePM,
		getSelectUntreatedDataList,
		getSelectLaowu
	} from '@/api/build/projectInfo'
	import {
		selectAreaList
	} from '@/api/common'
	import {
		mapGetters
	} from 'vuex'
	import CreateSections from '@/components/CreateSections'
	import {
		selectAttendanceData,
		selectNotice,
		getEnvironment
	} from './workbench'
	export default {
		name: 'Workbench',
		components: {
			tableVideo,
			danger,
			DetailsData,
			CreateSections,
			ProjectDetailsData,
			swiper,
			swiperSlide
		},
		data() {
			return {
				//swiper 初始化
				swiperOption: {
					navigation: {
						prevEl: '.syleft',
						nextEl: '.syright'
					}
				},
				// 统计类型数据字典
				engineeringStatusOptions: [],
				projectClassifyOptions: [],
				engineeringPurposeOptions: [],
				constructionNatureOptions: [],
				createProjectLevelOptions: [],
				constructionScaleOptions: [],
				bankAccountOptions: [],
				countryOptions: [],
				regionOptions: [],
				qualificationOptions: [],
				contractModeOptions: [],
				tallExquisiteOptions: [],
				managementModelOptions: [],
				subcontractingModeOptions: [],
				projectLevelOptions: [],
				engineeringTypeOptions: [],
				qualityTargetOptions: [],
				safetyTargetOptions: [],
				yesNoOptions: [],
				qualityLevelTargetOptions: [],
				SingleBuildingType: [],
				projectTypeOptions: [],
				activeclass: {},
				lzData: [],
				projectId: null,
				systemImage: '',
				open2: false,
				// 表单参数
				form: {

				},
				attendanceNum: '', // 出勤人数
				absenteeism: '', // 缺勤人数
				headcount: '', // 总人数
				percent: '', // 出勤率
				// 弹出层标题
				title: '',
				// 区划数据-省
				provinceOptions: [],
				// 区划数据-市
				cityOptions: [],
				// 区划数据-区
				districtOptions: [],
				lzform: {},

				speed: [],

				//公告 --开始
				showComDetail: false, // 查看相关客户管理详情
				rowID: '',
				businessType: null,
				componentName: '',
				componentClass: [],
				//公告 --结束
				shebinfo:{
					lift:{
						alertNumber:{},
						total:{}
					},
					somkeDetector:{
						alertNumber:{},
						total:{}
					},
					towerCrane:{
						alertNumber:{},
						total:{}
					},
					ai:{
						alertNumber:{},
						total:{}
					}
				},
				selectEnvDataAt:{
					noise:{
						value:'',
						unit:''
					},
					pm10:{
						value:''
					},
					pm25:{
						value:''
					}
				},
				PMdata:{
					noise:{
						ifOverweight:''
					},
					pm10:{
						ifOverweight:''
					},
					pm25:{
						ifOverweight:''
					}
				},
				untreatedDataList:[],
			}
		},
		computed: {
			...mapGetters([
				'userInfo'
			])
		},
		created() {
			this.getDicts('b_engineering_status').then(response => {
				this.engineeringStatusOptions = response.data
			})
			this.getDicts('b_project_classify').then(response => {
				this.projectClassifyOptions = response.data
			})
			this.getDicts('b_engineering_purpose').then(response => {
				this.engineeringPurposeOptions = response.data
			})
			this.getDicts('b_construction_nature').then(response => {
				this.constructionNatureOptions = response.data
			})
			this.getDicts('b_create_project_level').then(response => {
				this.createProjectLevelOptions = response.data
			})
			this.getDicts('b_construction_scale').then(response => {
				this.constructionScaleOptions = response.data
			})
			this.getDicts('b_bankAccount').then(response => {
				this.bankAccountOptions = response.data
			})
			this.getDicts('b_country').then(response => {
				this.countryOptions = response.data
			})
			this.getDicts('b_region').then(response => {
				this.regionOptions = response.data
			})
			this.getDicts('b_qualification').then(response => {
				this.qualificationOptions = response.data
			})
			this.getDicts('b_contract_mode').then(response => {
				this.contractModeOptions = response.data
			})
			this.getDicts('b_tall_exquisite').then(response => {
				this.tallExquisiteOptions = response.data
			})
			this.getDicts('b_management_model').then(response => {
				this.managementModelOptions = response.data
			})
			this.getDicts('b_subcontracting_mode').then(response => {
				this.subcontractingModeOptions = response.data
			})
			this.getDicts('b_project_level').then(response => {
				this.projectLevelOptions = response.data
			})
			this.getDicts('b_engineering_type').then(response => {
				this.engineeringTypeOptions = response.data
			})
			this.getDicts('b_quality_target').then(response => {
				this.qualityTargetOptions = response.data
			})
			this.getDicts('b_safety_target').then(response => {
				this.safetyTargetOptions = response.data
			})
			this.getDicts('sys_yes_no').then(response => {
				this.yesNoOptions = response.data
			})
			this.getDicts('b_quality_level_target').then(response => {
				this.qualityLevelTargetOptions = response.data
			})
			this.getDicts('b_project_type').then(response => {
				this.projectTypeOptions = response.data
			})
			this.getDicts('b_single_building_type').then(response => {
				this.SingleBuildingType = response.data
			})

			//右上方三个块
			this.getThree()
			//工程进度
			this.selectProgress()
			this.selectNumber()
			this.getAmbient()
			this.selectStatePM()
			this.getSelectUntreatedDataList()
		},
		mounted() {
			this.remsize()
			//this.resetScreenSize()

			this.drawChart()
			this.PMchart()

			window.WIDGET = {
				'CONFIG': {
					'layout': '1',
					'width': '522',
					'height': '178',
					'background': '2',
					'dataColor': '000000',
					'key': '1e5fb8e0cb784f48842eb5bad6cb5a12'
				}
			}
			var script = document.createElement('script')
			script.type = 'text/javascript'
			script.src = 'https://widget.qweather.net/standard/static/js/he-standard-common.js?v=2.0'
			document.getElementsByTagName('head')[0].appendChild(script)
		},
		beforeDestroy() {

		},
		methods: {
			/**
			 * 公告 -- 查看详情
			 */
			checkDetail(item) {

				this.rowID = parseInt(item.announcementId);

				let comName = "oa/notice/NoticeDetail"
				this.componentName = this.loadView(comName);

				this.componentClass.push('detail-view');

				this.showComDetail = true;
			},
			loadView(view) { // 路由懒加载
				return(resolve) => require([`@/views/${view}`], resolve)
			},

			// 劳务信息echarts
			drawChart() {
				const myChart = echarts.init(document.getElementById('main'), null, {
					devicePixelRatio: 2
				})
				const option = {
					title: {
						text: '劳务',
						x: '49',
						y: 'center',
						textStyle: {
							fontSize: '16',
							fontWeight: '500'
						},
						triggerEvent: true // 是否触发事件
					},
					color: ['#5B97F9', '#FAB262'],
					tooltip: {
						trigger: 'item',
						transitionDuration: 0, //防止页面抖动
					},
					legend: {
						bottom: '20',
						right: '20',
						icon: 'circle',
						orient: 'vertical'
					},
					series: [{

						type: 'pie',
						radius: ['40%', '50%'],
						center: ['70', '50%'],
						roseType: 'radius',
						itemStyle: {
							borderRadius: 8
						},
						data: [{
							value: 39,
							name: '出勤人数'
						},
							{
								value: 17,
								name: '缺勤人数'
							}
						],
						label: {
							normal: {
								formatter: params => {
									return(params.value)
								}
							}
						}
					}]
				}
				myChart.setOption(option)
				getSelectLaowu().then(res=>{
					var attendanceData = res.data

					this.attendanceNum = attendanceData[0].value
					this.absenteeism = attendanceData[1].value
					this.headcount = Number(attendanceData[0].value)+Number(attendanceData[1].value)
					this.percent = ((this.attendanceNum/this.headcount)*100).toFixed()
//					option.title.text = attendanceData.headcount

					var data = []
					var objA = {
						value: this.attendanceNum,
						name: '出勤人数'
					}
					var objB = {
						value: this.absenteeism,
						name: '缺勤人数'
					}
					data.push(objA)
					data.push(objB)
					option.series[0].data = data

					myChart.setOption(option)

					this.loading = false
				})



				// TODO 正式发布时解开以下注释，可请求真实数据
				// selectAttendanceData().then(res => {
				// 	var attendanceData = res.data
				// 	option.title.text = attendanceData.headcount
				//
				// 	this.attendanceNum = attendanceData.attendanceNum
				// 	this.absenteeism = attendanceData.absenteeism
				// 	this.headcount = attendanceData.headcount
				// 	this.percent = attendanceData.percent
				//
				// 	var data = []
				// 	var objA = {
				// 		value: this.attendanceNum,
				// 		name: '出勤人数'
				// 	}
				// 	var objB = {
				// 		value: this.absenteeism,
				// 		name: '缺勤人数'
				// 	}
				// 	data.push(objA)
				// 	data.push(objB)
				// 	option.series[0].data = data
				//
				// 	myChart.setOption(option)
				//
				// 	this.loading = false
				// }).catch(() => {
				// 	this.loading = false
				// })
				// 跳转事件
				myChart.on('click', params => {
					// console.log(params)
					if(params.componentType === 'title' || params.componentType === 'series') {
						this.goToLabourInfo()
					}
				})
			},
			// PM浓度echatrs
			PMchart() {
				const myChart = echarts.init(document.getElementById('concentration'), null, {
					devicePixelRatio: 2
				})
				const option = {
					tooltip: {
						trigger: 'axis',
						formatter: '{b}<br/>{a0}：{c0}μg/m³<br/>{a1}：{c1}μg/m³'
					},
					grid: {
						top: '15%',
						left: '10%',
						right: '10%',
						bottom: '20%'
					},
					legend: {
						top: '0'
					},
					color: ['#27BEFF', '#BDEB80'],
					xAxis: [{
						type: 'category',
						axisLine: {
							show: false,
							color: '#5B8FF9'
						},

						axisLabel: {
							color: '#000',
							width: 100
						},
						splitLine: {
							show: false
						},
						boundaryGap: false,
						data: ['2021-04-21', '2021-04-22', '2021-04-23', '2021-04-24', '2021-04-25',
							'2021-04-26', '2021-04-27'
						] // this.$moment(data.times).format("HH-mm") ,

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
							show: false
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					}],
					series: [{
						name: 'PM2.5',
						type: 'line',
						symbolSize: 10,
						lineStyle: {
							normal: {
								color: '#27BEFF'
							}
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
								], false)
							}
						},
						data: [20, 19, 25, 24, 23, 25, 28] // data.values
					},
						{
							name: 'PM10',
							type: 'line',
							symbolSize: 10,
							lineStyle: {
								normal: {
									color: '#BDEB80'
								}
							},
							areaStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
										offset: 0,
										color: 'rgba(189,235,128,0.8)'
									},
										{
											offset: 1,
											color: 'rgba(189,235,128,0)'
										}
									], false)
								}
							},
							data: [53, 55, 54, 52, 61, 57, 56] // data.values
						}
					]
				}
				getEnvironment().then(res => {

					option.xAxis.data = res.data.monitorDate
					option.series[0].data = res.data.PM25Num
					option.series[1].data = res.data.PM10Num
					myChart.setOption(option)
				})
				// 跳转事件
				myChart.on('click', params => {
					// console.log(params)
					if(params.componentType === 'title' || params.componentType === 'series') {
						this.goToEnvironHistory()()
					}
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
			resetScreenSize(dw, dh) {
				if(window.innerWidth > 1000) {
					function init() {
						var _el = document.getElementById('weather')
						var hScale = (window.innerHeight) / (dh || 1080)
						var wScale = (window.innerWidth) / (dw || 1920)
						console.log(window.innerHeight, window.innerWidth)
						_el.style.transform = 'scaleX(' + wScale + ') scaleY(' + hScale + ')'
					}
					var lazyFun
					window.onresize = function() {
						clearTimeout(lazyFun)
						lazyFun = setTimeout(function() {
							init()
						}, 600)
					}
					init()
				}
			},
			//点击弹出项目详情页面
			detailsDataClick() {
				this.open2 = true
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
			// 获取县级区划数据
			getDistrictList(id) {
				const param = {
					parentId: id
				}
				selectAreaList(param).then(res => {
					this.districtOptions = res.data
				})
			},
			getlzData() {
				selectByProjectId(this.projectId).then(response => {
					this.lzData = response.data
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			// 跳转劳务信息
			goToLabourInfo() {
				this.goToUrl('attendanceDetail')
			},
			// 跳转到环境监测历史数据
			goToEnvironHistory() {
				this.goToUrl('environHistory')
			},
			// 取消按钮
			cancel() {
				this.open2 = false
				// this.form = {}
				// 清空市、区区划数据
				// this.cityOptions = []
				// this.districtOptions = []

			},
			engineeringStatusFormat(row, column) {
				return this.selectDictLabel(this.engineeringStatusOptions, row.engineeringStatus)
			},
			projectStateFormat(row, column) {
				return this.selectDictLabel(this.engineeringStatusOptions, row.projectState)
			},
			projectClassifyFormat(row, column) {
				return this.selectDictLabel(this.projectClassifyOptions, row.projectClassify)
			},
			singleBuildingTypeFormat(row, column) {
				return this.selectDictLabel(this.SingleBuildingType, row.type)
			},
			singleBuildingIsFormat(row, column) {
				return this.selectDictLabel(this.yesNoOptions, row.isPublic)
			},
			areaChangeProvince(d) {
				this.form.city = ''
				this.form.district = ''
				this.getCityList(d)
			},
			areaChangeCity(d) {
				this.form.district = ''
				this.getDistrictList(d)
				this.$forceUpdate()
			},
			areaChangeDistrict(d) {
				this.$forceUpdate()
			},
			submiteImage(data) {
				this.systemImage = data.image
			},

			// 竣工时间、总面积、总造价 userInfo.projectId
			getThree() {
				getThree(this.userInfo.projectId).then(res => {

					let rightThree = res.data

					if(rightThree != null) {
						document.getElementById("endTime").innerHTML = rightThree.endTime
						document.getElementById("allAre").innerHTML = rightThree.allAre + "平"
						document.getElementById("allTost").innerHTML = rightThree.allTost + "万元"
					}

				})
			},

			//工程进度
			selectProgress() {
				selectProgress().then(res => {
					// console.log(res)
					this.speed = res.data.ZIB
					this.speed = res.data.ZIB
					var list = []
					//一次显示5个
					for(var i = 0; i < this.speed.length; i += 5) {
						list.push(this.speed.slice(i, i + 5));
					}
					this.speed = []
					for(var j = 0; j < list.length; j++) {
						this.speed.push(list[j])
					}
					// console.log(this.speed)
					document.getElementById("allDate").innerHTML = res.data.ZUB.allDate + "天"

				})
			},
			//左上角报警
			selectNumber(){
				selectNumber({
					selectType:'2'
				}).then(res=>{
					this.shebinfo=res.data
				})
			},
			getAmbient(){
				selectEnvDataAt().then(res=>{
					this.selectEnvDataAt=res.data
				})
			},
			selectStatePM(){
				selectStatePM().then(res=>{
					this.PMdata=res.data
				})
			},
			getSelectUntreatedDataList(){
				getSelectUntreatedDataList({
					handleState:0
				}).then(res=>{
					this.untreatedDataList=res.data
				})
			},
			policeMore(){
				this.goToUrl('monitorPointAlert')
			},
			gopolicelist(num){
				if (num == '97') {
					this.goToUrl('aiVideoAlert',{})
				} else {
					this.goToUrl('monitorPointAlert',{alertType:'1',monitorType:num})
				}
			},
			// 根据菜单路由跳转页面
			goToUrl(path,queryParams) {
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
					console.log(res)
					this.$store.state.app.activeclass = activeclass.comname
					this.$router.push({
						name: activeclass.name,
						query: queryParams
					})
				})
			},

		}
	}
</script>

<style scoped lang="scss">
	.crm-workbench {
		height: 100%;
	}
	/*.titleHeader {
		height: 60px;
		background: #FFFFFF;
		font-size: 20px;
		color: #404040;
		line-height: 60px;
		font-weight: 500;
		padding-left: 48px;
	}*/

	.content {
		padding: 0.19rem 0 0.17rem 0;
		font-weight: 500;
		height: 100%;
		display: flex;
		justify-content: center;
	}

	.layoutleft {
		float: left;
		height: 100%;
	}

	.alarm_statistics {
		width: 4.66rem;
		height: 46%;
		background: #FFFFFF;
		overflow: hidden;
		border-radius: 5px;
		box-shadow: 0px 0px 20px 0px rgba(145,180,241,0.2900);
	}
	.police_list_box{
		height: calc(100% - 28px);
		display: flex;
		flex-direction: column;
		justify-content: space-evenly;

	}
	.police_list{
		width:93%;
		margin: 0 auto;
		height: 14.1%;
		display: flex;
		align-items: center;
	}
	.police_list1{
		background: #EFF5FF;
		border-radius: 10px;
	}
	.police_list2{
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
		border-radius: 5px;
	}
	.indexicon{
		height: 65%;
		width: auto;
		margin-left: 0.1rem;
	}
	.police_name{
		width: 26%;
		margin-left: 0.11rem;
		color: #404040;
		font-size: 0.14rem;
	}
	.police_num1{
		width: 30%;
		display: flex;
		align-items: center;
	}
	.police_num2{

		display: flex;
		align-items: center;
	}
	.pol_tit{
		margin-right: 10px;
		color: #404040;
		font-size: 0.14rem;
	}
	.pol_val{
		font-size: 0.18rem;
		cursor: pointer;
		color: #649BF8;
	}
	.bigevent {
		width: 4.66rem;
		height: 36%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #E1E1E1;
		padding: 8px 11.6px 16px 8px;
	}

	.subject {
		height: 16px;
		font-size: 0.16rem;
		font-weight: 500;
		color: #404040;
		display: flex;
		align-items: center;
	img {
		margin-right: 5px;
	}
	}

	.bigsymbolbox {
		height: calc(100% - 16px);
	}

	.bigsymbolbox ul {
		height: 100%;
	}

	.bigsymbolbox ul li {
		height: 11%;
		display: flex;
		align-items: center;
		width: 100%;
		background: #F7F8FA;
		border-radius: 5px;
		border: 1px solid #E1E1E1;
		margin-top: 3.25%;
		font-size: 0.12rem;
		padding-right: 5px;
	}

	.bigsymbolbox ul li:hover {
		cursor: pointer;
	}

	.fontcolor0 {
		color: #1B65FA;
	}

	.fontcolor1 {
		color: #27BEFF;
	}

	.fontcolor2 {
		color: #18BAA0;
	}

	.fontcolor3 {
		color: #57CB91;
	}

	.fontcolor4 {
		color: #FAAD14;
	}

	.fontcolor5 {
		color: #FA8414;
	}
	.bigcontent {
		vertical-align: top;
		font-size: 0.13rem;
		margin-left: 0.14rem;
		width: 3rem;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: inline-block;
	}

	.bigtime {
		font-size: 0.14rem;
		margin-left: 0.2rem;
	}

	.servicebox {
		width: 4.35rem;
		height: 22%;
		background: #FFFFFF;
		border-radius: 5px;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
		margin-top: 0.17rem;
		overflow: hidden;
	}

	#main {
		width: 2.85rem;
		height: calc(100% - 28px);
		float: left;
	}

	.servicedetails {
		float: left;
		height: calc(100% - 28px);
		width: 1.40rem;
	}

	.servicetitle {
		font-size: 16px;
		font-weight: 600;
		color: #404040;
		margin-top: 4px;
	}

	.attendance {
		width: 1.40rem;
		height: 20%;
		background: rgba(52, 119, 255, 0.0800);
		border-radius: 5px;
		border: 1px solid rgba(100,155,248,0.2000);
		margin-top: 0.11rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
	.chartnum {
		color: #649BF8;
		font-weight: 500;
	}
	}

	.allpeople {
		width: 1.40rem;
		height: 20%;
		background: rgba(250,173,20,0.1200);
		border-radius: 5px;
		border: 1px solid rgba(250,173,20,0.2000);
		margin-top: 0.11rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
	.chartnum {
		color: #FAAD14;
		font-weight: 500;
	}
	}

	.assessment {
		width: 1.40rem;
		height: 20%;
		background: rgba(48,191,120,0.0800);
		border-radius: 5px;
		border: 1px solid rgba(48,191,120,0.2200);
		margin-top: 0.11rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
	.chartnum {
		color: #404040;
		font-weight: 500;
	}
	}

	.charttitle {
		float: left;
		font-weight: 400;
		color: #404040;
		font-size: 0.14rem;
		margin-left: 0.16rem;
	}

	.chartnum {
		float: right;
		font-size: 0.18rem;
		margin-right: 0.16rem;
	}

	.PMbox {
		width: 4.66rem;
		height: 29.6%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #DFDFDF;
		margin-top: 0.17rem;
	}

	#concentration {
		width: 4.66rem;
		height: calc(100% - 28px);
	}

	.subtitle {
		margin-left: 8px;
		margin-top: 0.12rem;
	}

	.layoutright {
		margin-left: 0.2rem;
		width: 11.4rem;
		height: 100%;
	}

	.projectdetails {
		width: 11.4rem;
		height: 12.2%;
		background: #FFFFFF;
		border-radius: 5px;
	}

	.everydata {
		width: 3.66rem;
		float: left;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
		border-radius: 5px;
		display: flex;
		align-items: center;
		height: 100%;
		margin-right: 0.2rem;
	}
	.projecticon {
		float: left;
		width: 0.7rem;
		margin-left: 0.4rem;
	}

	.everydata div {
		float: left;
		margin-left: 0.26rem;
	}

	.protitle {
		margin-top: 0.06rem;
		color: #404040;
		font-size: 0.14rem;
	}

	.procontent {
		font-size: 0.24rem;
		color: #404040;
		margin-top: 0.044rem;
	}

	.videbox {
		height: 85%;
	}

	.video {
		width: 6.8rem;
		height: 62.5%;
		background: #FFFFFF;
		border-radius: 5px;
		margin-top: 0.19rem;
	}

	.progress {
		width: 6.8rem;
		height: 34.7%;
		background: #FFFFFF;
		border-radius: 5px;
		margin-top: 0.17rem;
		overflow: hidden;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
	}

	.progress .gcjd {
		margin-top: 0.04rem;
	}

	.danger {
		width: 4.35rem;
		height: 61%;
		background: #FFFFFF;
		border-radius: 5px;
		overflow: hidden;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
		margin-top: 0.15rem;
	}

	.legend {
		height: 9.1%;
		margin-top: 4px;
	}

	.projectday {
		float: left;
		color: #404040;
		font-size: 16px;
		font-weight: 500;
		margin-left: 1.3rem;
	}

	.proday {
		color: #FA1B4E;
	}

	.legend ul {
		float: left;
		margin-left: 0.5rem;
	li {
		float: left;
		font-size: 0.11rem;
		margin-right: 0.16rem;
		display: flex;
		align-items: center;
	}
	}

	.circular {
		height: 0.11rem;
		width: 0.11rem;
		float: left;
		border-radius: 50%;
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

	.speed {
		width: 90%;
		height: 72.5%;
		background: linear-gradient(180deg, rgba(249, 249, 249, 0.69) 0%, rgba(232, 237, 248, 0.69) 100%);
		margin: 0 auto;
		position: relative;
		overflow: hidden;
	}

	.wcline {
		position: absolute;
		transform: translateX(-50%);
		left: 50%;
		margin-top: 8.5%;
		width: 90%;
	}

	.syleft {
		position: absolute;
		left: 1%;
		margin-top: 7%;
		z-index: 99;
	}

	.syright {
		position: absolute;
		right: 1%;
		margin-top: 7%;
		z-index: 99;
	}

	.speed ul {
		display: flex;
		width: 100%;
		margin-top: 5%;
	}

	.speed ul li {
		float: left;
		width: 0.9rem;
		font-size: 0.13rem;
		margin-left: 0.15rem;
		margin-right: 0.15rem;
	}

	.wcicon {
		width: 60%;
		margin: 0 auto;
		display: block;
		margin-bottom: 15px;
	}

	.speedcon {
		width: 100%;
		height: 0.5rem;
		background: url("../../../../static/img/wcbg.png");
		text-align: center;
		background-size: 100%;
	}

	.speedcondata {
		width: 100%;
		margin: 0 auto;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: inline-block;
		font-size: 0.13rem;
		margin-top: 0.07rem;
	}

	.weather {
		margin-top: 0.19rem;
		width: 4.35rem;
		height: 22%;
		background: #FFFFFF;
		border-radius: 5px;
		border: 1px solid #E1E1E1;
	}

	/deep/ #he-plugin-standard {
		width: 100%!important;
		height: 100%!important;
	}

	.danChart {
		margin-top: 10px;
	}
	/*详情页面样式*/

	.main-table-header-button {
		float: right;
		margin-right: 20px;
		margin-top: 10px;
	}

	.add-form-input {
		float: left;
		margin-bottom: 14px;
	}

	add-form-divider {
		float: left;
		margin-bottom: 10px;
	}

	.add-form::after {
		content: "";
		display: block;
		clear: both;
	}

	add-form-a .el-form-item {
		width: 50% !important;
	}

	.add-form .el-form-item {
		width: 33%;
	}

	.add-form-w100 .el-form-item {
		width: 100%;
	}

	.link-type,
	.link-type:focus {
		color: #337ab7;
		cursor: pointer
	}

	.link-type:focus:hover,
	.link-type:hover {
		color: #20a0ff
	}

	.el-table {
		border-top: 1px solid $xr-border-line-color;
	}

	.bm-view {
		width: 100%;
		height: 700px;
	}

	/deep/ .el-dialog {
		height: 700px;
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		margin-top: auto !important;
	}

	/deep/ .el-dialog .el-dialog__body {
		height: 615px;
		overflow: auto;
	}

	.uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 200px;
		height: 200px;
		line-height: 200px;
		text-align: center;
	}

	.upload /deep/ .el-upload-dragger {
		height: 200px;
	}

	.upload-show {
		position: relative;
		display: block;
		width: 360px;
		height: 200px;
	img {
		width: 100%;
		height: 100%;
	}
	.icon-delete {
		position: absolute;
		top: -10px;
		right: -8px;
		color: red;
		font-size: 20px;
		display: none;
	}
	}

	.dangerboxa {
		height: 100%;
		margin-left: 0.2rem;
		float: left;
	}

	.upload-show:hover {
	.icon-delete {
		display: block;
	}
	}

	/deep/.el-form .el-input {
		width: 220px;
	}

	/deep/.el-form .el-select {
		width: 220px;
	}

	/deep/.el-form .el-textarea {
		width: 220px;
	}

	.add-form-w100 .el-input {
		width: 500px;
	}
	/*详情页面样式*/

	/deep/ .wv-lt-location a {
		display: none;
	}

	.police_ss{
		width: 4.66rem;
		height: 51.5%;
		background: #FFFFFF;
		border-radius: 5px;
		box-shadow: 0px 0px 20px 0px rgba(145,180,241,0.2900);
		margin-top: 0.15rem;
		position: relative;
		overflow: hidden;
	}
	.police_more{
		position: absolute;
		right: 10px;
		top: 10px;
		cursor: pointer;
	}
	.jcc_box{
		display: flex;
		align-items: center;
		justify-content: flex-end;
		margin-top: 10px;
	}
	.jc_data{
		font-size: 12px;
		display: flex;
		align-items: center;
		margin-right: 30px;
	}
	.jc_data img{
		margin-right: 11px;
	}
	.PMpolice{
		height:calc(100% - 60px);
		overflow-y: auto;
	}
	.PMpolice::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}

	.PMpolice {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	.PMpolice_list{
		display: flex;
		margin-top: 13px;
		height: 60px;
		width: 93%;
		margin: 0 auto;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.2900);
		border-radius: 5px;
		align-items: center;
		font-size: 12px;
	}
	.pmlitile{
		color: #FC2E2E;
	}
	.pmlitile2{
		color: #FAAD14;
	}
	.PMinfo_box{
		display: flex;
		justify-content: space-between;
		font-size: 12px;
		margin-top: 10px;
	}
	.mouse_style{
		cursor: pointer;
	}
	.mouse_style1{
		width: 0.46rem;
		height: 20px;
		color: #FFFFFF;
		font-size: 0.1rem;
		text-align: center;
		line-height: 20px;
	}
	.zwsj{
		font-size: 14px;
		color: #999;
		text-align: center;
		margin-top: 20px;
	}
	.bjtype{
		display: flex;
		align-items: center;
		font-size: 12px;
		height: 26px;
		line-height: 26px;

		border-radius: 5px;


	}
	.xswt{
		width: 70%;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.bjtype img{
		margin-right: 4px;
		margin-left: 7px;
		margin-top: -5px;
	}
	.PM_content{
		width: 40%;

	}
	.pmpval{
		width: 30%;
		color: #404040;
		font-size: 12px;
	}
	.pmplo_date{
		width: 30%;
		color: #666666;
		font-size: 12px;
	}

</style>
