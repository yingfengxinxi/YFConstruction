<script src="api/monitorHistoryData.js"></script>
<template>
	<div class="content_box">
		<div class="con_left_box">
			<div class="early_warning_box">
				<div class="subject"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/Symbol.png">当日环境报警分析</div>
				<div id="earlyWarning">

				</div>
			</div>
			<div class="alarm_trend_box">
				<div class="subject"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/Symbol.png">报警趋势</div>
				<div id="alarmTrend"></div>
			</div>
			<div class="environment_box">
				<div class="subject"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/Symbol.png">环境监测</div>
				<div id="environment"></div>
			</div>
		</div>
		<div class="con_right_box">
			<div class="header_top_box">
				<div class="type_box header_tp3">
					<div class="equipment_tit">
						环境监测点
					</div>
					<div class="equipment_con">
						<div class="function_box" @click="goStateList('155','1')">
							<!-- <div class="function_box" @click="goStateList('9','1')"> -->
							<div class="function_tit">在线</div>
							<div class="function_num">{{envMonitPont.onLineNum}}</div>
						</div>
						<div class="num" @click="goStateList('155','')">
							<!-- <div class="num" @click="goStateList('9','')"> -->
							<div class="function_tit">总数</div>
							<div class="function_num">{{envMonitPont.totalnum}}</div>
						</div>
					</div>
				</div>
				<div class="type_box header_tp1">
					<div class="equipment_tit">
						机械设备
					</div>
					<div class="equipment_con">
						<div class="function_box" @click="goStateList('148','1')">
							<!-- <div class="function_box" @click="goStateList('2,3','1')"> -->
							<div class="function_tit">运行</div>
							<div class="function_num">{{conv(50,'onLineNum')}}</div>
						</div>
						<div class="num" @click="goStateList('148','')">
							<!-- <div class="num" @click="goStateList('2,3','')"> -->
							<div class="function_tit">总数</div>
							<div class="function_num">{{conv(50,'totalnum')}}</div>
						</div>
					</div>
				</div>
				<div class="type_box header_tp2">
					<div class="equipment_tit">
						视频监控
					</div>
					<div class="equipment_con">
						<div class="function_box" @click="goStateList('157','1')">
							<!-- <div class="function_box" @click="goStateList('99','1')"> -->
							<div class="function_tit">运行</div>
							<div class="function_num">{{conv(30,'onLineNum')}}</div>
						</div>
						<div class="num" @click="goStateList('157','')">
							<!-- <div class="num" @click="goStateList('99','')"> -->
							<div class="function_tit">总数</div>
							<div class="function_num">{{conv(30,'totalnum')}}</div>
						</div>
					</div>
				</div>

			</div>
			<div id="allmap" class="baidumap">

			</div>
			<div class="map_but_tem">

				<el-radio-group v-model="mapMonitorRadio" @change="initMapData()">
					<img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' style="float: left;margin-top: 9px;margin-left: 15px" src="./img/xman.png">
					<el-select v-model="mapPorjectRadio" @change="initMapData()" @visible-change="addPorjectMark" placeholder="请选择">

						<el-option v-for="(item, index) in engineeringStatusOptions" :key="index" :label="item.name" :value="item.value"></el-option>
					</el-select>

					<el-radio-button label="5"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' class="an_img" src="./img/yjan.png">预警</el-radio-button>
					<div style="display: inline-block;border-left: 1px solid #EBF2FF;height: 15px;float: left;margin-top: 8px"> </div>
					<el-radio-button label="6"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' class="an_img" src="./img/yjan.png">报警</el-radio-button>
					<div style="display: inline-block;border-left: 1px solid #EBF2FF;height: 15px;float: left;margin-top: 8px"> </div>
					<el-radio-button label="7"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' class="an_img" src="./img/pm10an.png">PM10</el-radio-button>
					<div style="display: inline-block;border-left: 1px solid #EBF2FF;height: 15px;float: left;margin-top: 8px"> </div>
					<el-radio-button label="8"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' class="an_img" src="./img/tjan.png">塔机</el-radio-button>
				</el-radio-group>
			</div>
			<div class="project_details">
				<div class="subject"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/Symbol.png">项目工程进度</div>
				<div id="speed" style="height: calc(100% - 0.28rem);width: 100%">

				</div>
			</div>
			<div class="project_num">
				<div class="subject"><img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/Symbol.png">项目报警数量统计</div>
				<div class="statistics2">
					<div class="statistics2_dom" style="background-color: #EAF1FF;">
						<div class="statistics2_dom1">
							<img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/bj_icon1.png">
						</div>
						<div class="statistics2_dom2" @click="openGoTo('0')">
							<div style="font-size: 20px;text-align: center;color: #5B96FA ">5</div>
							<div>今日预警</div>
						</div>
					</div>
					<div class="statistics2_dom" style="background-color: rgba(115, 220, 160, 0.2);">
						<div class="statistics2_dom1">
							<img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' src="../../../../static/img/bj_icon2.png">
						</div>
						<div class="statistics2_dom2" @click="openGoTo('1')">
							<div style="font-size: 20px;text-align: center;color: #5BD590">7</div>
							<div>今日报警</div>
						</div>
					</div>
				</div>
				<div class="statistics">
					<div class="statistics_tit">
						<div style="width: 35%;"></div>
						<div style="width: 30.25%;">今日数量</div>
						<div style="width: 30.25%;">总计数量</div>
					</div>
					<div class="statistics_con_box">
						<div class="statistics_con">
							<div class="block_title" style="width: 39%;font-size: 12px;text-align: center">项目名称</div>
							<div class="con_num" style="width: 20%;font-size: 12px">设备报警</div>
							<div class="con_num" style="width: 20%;font-size: 12px">安全隐患</div>
							<div class="con_znum" style="width: 20%;font-size: 12px">设备报警</div>
							<div class="con_znum" style="width: 20%;font-size: 12px">安全隐患</div>
						</div>
						<div class="statistics_con" v-for="(item,index) in projectAlarmNum" :key="index">
							<div class="con_title" style="width: 39%;">
								<div class="block"></div>
								<div class="block_title" style="font-size: 12px">{{item.projectAbbreviation}}</div>
							</div>
							<div class="con_num" style="width: 20%;font-size: 12px" @click="alarmOpen(item.projectId,'1')">{{item.todayAlarmNum}}</div>
							<div class="con_num" style="width: 20%;font-size: 12px" @click="alarmOpen2(item.projectId,'1')">{{item.todayAlarmNum2}}</div>
							<div class="con_znum" style="width: 20%;font-size: 12px" @click="alarmOpen(item.projectId,'')">{{item.totalalarmnum}}</div>
							<div class="con_znum" style="width: 20%;font-size: 12px" @click="alarmOpen2(item.projectId,'')">{{item.totalalarmnum2}}</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import iconw from "./img/bj1.png"
	import iconj from "./img/bj2.png"
	import iconc from "./img/bj3.png"
	import bmap from './img/mapic.png'
	import iconzj from "./img/zj.png"
	import icontg from "./img/tg.png"
	import iconwkg from "./img/wkg.png"
	import iconjg from './img/jg.png'
	import iconyjtb from './img/yjtb.png'
	import iconbjtb from './img/bjtb.png'
	import iconpm10dy0 from './img/pm10dy0.png'
	import iconpm10dy35 from './img/pm10dy35.png'
	import iconpm10dy75 from './img/pm10dy75.png'
	import iconpm10dy115 from './img/pm10dy115.png'
	import iconpm10dy150 from './img/pm10dy150.png'
	import iconpm10dy250 from './img/pm10dy250.png'
	import icontjzx from './img/tjzx.png'
	import icontjlx from './img/tjlx.png'
	import icontjyx from './img/tjyx.png'
	import * as echarts from 'echarts';
	import { queryMenuListByPath } from '@/api/admin/menu'
	import { envMonitPont, equipmentNum, alarmsNum1, alarmsNum2, projectNum, earlyWarning, projectAlarmNum, selectProjectStateCount, monitSituationNoise, monitSituationPM, getMAP } from './indexApi.js'
	export default {
		data() {
			return {
				map: null,
				marker: null,
				colorlists: [],

				//接口返回数据
				envMonitPont: {
					'onLineNum': '0',
					'totalnum': '0'
				},
				equipmentNum: [{
					'onLineNum': '0',
					'totalnum': '0'
				}, {
					'onLineNum': '0',
					'totalnum': '0'
				}, {
					'onLineNum': '0',
					'totalnum': '0'
				}],
				projectAlarms: {
					'alarmsProjectNum': '0',
					'alarmstNum': '0',
					'projectNum': '0'
				},
				//{ value: 11, name: 'PM10' }, { value: 21, name: 'PM2.5' }, { value: 13, name: '噪声' }, { value: 14, name: '湿度' }, { value: 32, name: '温度' }, { value: 32, name: 'TPS' },
				EarlyWarning: [],
				projectAlarmNum: [],
				monitSituationNoise: [],
				monitSituationPM: [],
				pointMonitor: [],
				speedlist: [],
				mapPorjectRadio: '2',
				mapMonitorRadio: null,
				zjMapMarkers: {
					info: [],
					early: [],
					alert: [],
					pm10: [],
					tj: []
				},
				tgMapMarkers: {
					info: [],
					early: [],
					alert: [],
					pm10: [],
					tj: []
				},
				jgMapMarkers: {
					info: [],
					early: [],
					alert: [],
					pm10: [],
					tj: []
				},
				wkgMapMarkers: {
					info: [],
					early: [],
					alert: [],
					pm10: [],
					tj: []
				},
				engineeringStatusOptions: []
			}
		},
		created() {
			this.randomRgb()
		},
		mounted() {
			this.getLargeScreen()
			this.remsize();
			this.baiduMap()
			this.initMapData()
			this.getDicts('b_engineering_status').then(response => {
				var than = this;
				this.engineeringStatusOptions[0] = {
					name: '全部',
					value: '0'
				}
				var bo = true;
				response.data.forEach(function(element) {
					if(element.dictLabel.indexOf("在建") != -1) {
						than.engineeringStatusOptions.push({
							name: '已接入',
							value: element.dictValue
						})
					}
					if(element.dictLabel.indexOf("竣工") != -1) {
						if(bo) {
							than.engineeringStatusOptions.push({
								name: '已完成',
								value: element.dictValue
							})
							bo = false
						}
					}
					if(element.dictLabel.indexOf("未开工") != -1) {
						than.engineeringStatusOptions.push({
							name: '未接入',
							value: element.dictValue
						})
					}
					if(element.dictLabel.indexOf("停工") != -1) {
						than.engineeringStatusOptions.push({
							name: '暂停',
							value: element.dictValue
						})
					}
				});
			})
		},
		methods: {
			goStateList(typeId, online) {
				if(online != '') {
					this.goToUrl('equassetsGRP', {
						typeId: typeId
					})
				} else {
					this.goToUrl('equassetsGRP', {
						typeId: typeId
					})
				}
			},
			/* goStateList (types, online) {
				let monitorTypeList = types.split(',')
				if (online != '') {
					this.goToUrlParams('equOnlineStatus', { monitorTypeList: monitorTypeList, online:online})
				} else {
					this.goToUrlParams('equOnlineStatus', { monitorTypeList: monitorTypeList })
				}
			}, */
			//报警
			alarmOpen(projectId, type) {
				if(type == '1') {
					this.goToUrl('monitorPointAlert', {
						projectId: projectId,
						timeSlot: '1'
					})
				} else {
					this.goToUrl('monitorPointAlert', {
						projectId: projectId
					})
				}
			},
			//隐患
			alarmOpen2(projectId, type) {
				if(type == '1') {
					this.goToUrl('inspectionInfo', {
						projectId: projectId,
						timeSlot: '1'
					})
				} else {
					this.goToUrl('inspectionInfo', {
						projectId: projectId
					})
				}
			},
			openGoTo(alarmType) {
				var day2 = new Date();
				day2.setTime(day2.getTime());
				var s2 = day2.getFullYear() + "-" + (day2.getMonth() + 1) + "-" + day2.getDate() + ' 00:00:00';

				var day3 = new Date();
				//day3.setTime(day3.getTime()+24*60*60*1000);
				day3.setTime(day3.getTime());
				var s3 = day3.getFullYear() + "-" + (day3.getMonth() + 1) + "-" + day3.getDate() + ' 23:59:59';
				let executeDates = [s2, s3]
				if(alarmType == '0') {
					this.goToUrl('monitorPointAlert', {
						alertType: alarmType
					}, {
						executeDates: executeDates
					})
				} else if(alarmType == '1') {
					this.goToUrl('monitorPointAlert', {
						alertType: alarmType
					}, {
						executeDates: executeDates
					})
				}
			},

			goenvAlert(num) {
				if(num == 3) {
					this.goToUrl('engineering', {
						alertType: num
					})
				} else {
					this.goToUrl('envAlert', {
						alertType: num
					})

				}
			},

			goToUrl(path, queryParams, queryParams2) {
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
						name: activeclass.name,
						query: queryParams,
						params: queryParams2
					})
				})
			},
			// 通过params方式传参
			goToUrlParams(path, queryParams) {
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
						name: activeclass.name,
						params: queryParams
					})
				})
			},
			//同步查询所有在线监控设备
			async monitorOnlineNum() {
				let allDeviceList = [];
				let onlineNum = 0;
				//获取所有设备列表
				await this.$axios({
					method: 'get',
					url: process.env.VIDEO_URL + "/api/device/query/devices",
					params: {
						"page": "1",
						"count": "9999"
					}
				}).then(function(res) {
					allDeviceList = res.data.list;
				}).catch(function(error) {
					console.log(error)
				});
				//轮训设备列表，查询是否在线
				allDeviceList.forEach(element => {
					this.$axios({
						method: 'get',
						url: process.env.VIDEO_URL + "/api/device/query/devices/" + element.deviceId + "/status",
						params: {
							"deviceId": element.deviceId
						}
					}).then(function(res) {
						if(res.data.indexOf("Timeout") != -1) {
							onlineNum++;
						}
					}).catch(function(error) {
						console.log(error)
					});
				});
				//this.equipmentNum[0].onLineNum = onlineNum
			},
			//大屏数据获取
			getLargeScreen() {
				//环境监测点数据
				envMonitPont().then(res => {
					// this.envMonitPont = res.data
					this.envMonitPont = {
						'onLineNum': 12,
						'totalnum': 15
					}
				}).catch((err) => {
					console.log(err);
				})

				//设备分类数据
				equipmentNum().then(res => {
					// this.equipmentNum = res.data;
					this.equipmentNum = [{
						'totalnum': '48',
						'onLineNum': '35',
						'typeId': 50
					}, {
						'totalnum': '18',
						'onLineNum': '15',
						'typeId': 30
					}]
					this.monitorOnlineNum();
				}).catch((err) => {
					console.log(err);
				})
				//查询监控设备在线数

				//总报警数（环境预警项目）
				alarmsNum1().then(res => {
					this.projectAlarms.alarmsProjectNum = res.data.alarmsProjectNum
				}).catch((err) => {
					console.log(err);
				})

				//报警项目统计（严重超标项目）
				alarmsNum2().then(res => {
					this.projectAlarms.alarmstNum = res.data.alarmstNum
				}).catch((err) => {
					console.log(err);
				})

				//报警项目统计（接入项目）
				projectNum().then(res => {
					this.projectAlarms.projectNum = res.data.projectNum
				}).catch((err) => {
					console.log(err);
				})

				//预警分类
				earlyWarning().then(res => {
					//this.EarlyWarning = res.data;
					this.EarlyWarning = [{
							name: "扬尘",
							value: 3
						}, {
							name: "塔机",
							value: 1
						}, {
							name: "深基坑",
							value: 0
						}, {
							name: "升降机",
							value: 1
						}, {
							name: "噪音",
							value: 0
						},
						{
							name: "高支模",
							value: 2
						}, {
							name: "视频分析",
							value: 0
						}
					]
					this.earlyWarning()
				}).catch((err) => {
					console.log(err);
				})

				//按项目分类统计报警数
				// this.postAxios('/build/homePage/projectAlarmNum', {},'json').then(res => {
				// this.projectAlarmNum=res.data.data;
				// }).catch(err => {})
				projectAlarmNum().then(res => {

					// this.projectAlarmNum = res.data;
					this.projectAlarmNum = [{
							"projectId": "51",
							"projectName": "幸福小区项目",
							"projectAbbreviation": "幸福小区项目",
							"todayAlarmNum": "3",
							"totalalarmnum": "26",
							"todayAlarmNum2": "5",
							"totalalarmnum2": "22"
						},
						{
							"projectId": "52",
							"projectName": "丰润花园项目",
							"projectAbbreviation": "丰润花园项目",
							"todayAlarmNum": "2",
							"totalalarmnum": "15",
							"todayAlarmNum2": "3",
							"totalalarmnum2": "18"
						},
						{
							"projectId": "60",
							"projectName": "富力城项目二期 ",
							"projectAbbreviation": "富力城项目二期 ",
							"todayAlarmNum": "1",
							"totalalarmnum": "8",
							"todayAlarmNum2": "1",
							"totalalarmnum2": "8"
						},
						{
							"projectId": "54",
							"projectName": "地鑫新河花园",
							"projectAbbreviation": "地鑫新河花园",
							"todayAlarmNum": "1",
							"totalalarmnum": "6",
							"todayAlarmNum2": "0",
							"totalalarmnum2": "5"
						},
						{
							"projectId": "58",
							"projectName": "德百玫瑰公馆A区",
							"projectAbbreviation": "德百玫瑰公馆A区",
							"todayAlarmNum": "0",
							"totalalarmnum": "5",
							"todayAlarmNum2": "0",
							"totalalarmnum2": "2"
						},
						{
							"projectId": "53",
							"projectName": "宏业大厦项目",
							"projectAbbreviation": "宏业大厦项目",
							"todayAlarmNum": "0",
							"totalalarmnum": "0",
							"todayAlarmNum2": "0",
							"totalalarmnum2": "0"
						}
					]
				}).catch((err) => {
					console.log(err);
				})

				//噪音监控情况(报警趋势)
				monitSituationNoise().then(res => {
					//this.monitSituationNoise = res.data;
					this.monitSituationNoise = {
						dayNumProp: [60, 50, 55, 67, 100, 67, 100],
						nightNumProp: [40, 50, 45, 33, 0, 33, 0],
						dayNum: [3, 2, 4, 6, 1, 2, 1],
						nightNum: [2, 2, 5, 2, 0, 1, 0]
					}
					this.monitSituationNoise.monitorDate = res.data.monitorDate
					this.alarmTrend()
				}).catch((err) => {
					console.log(err);
				})

				//PM2.5、PM10监控情况（环境检测）
				monitSituationPM().then(res => {
					//this.monitSituationPM = res.data;
					this.monitSituationPM = {
						PM25Num: [35, 52, 66, 50, 37, 40, 44],
						PM10Num: [25, 34, 32, 44, 32, 31, 22]
					}
					this.monitSituationPM.monitorDate = res.data.monitorDate;
					this.environment()
				}).catch((err) => {
					console.log(err);
				})
				selectProjectStateCount().then(res => {
					this.speedlist = res.data
					this.speedFn()
				}).catch((err) => {
					console.log("11", err);
				})

			},
			conv(typeId, type) {
				// if (typeId == 30 && type == 'onLineNum') {
				// 	return 0;
				// }
				let temp = 0;
				this.equipmentNum.forEach(item => {
					if(item.typeId == typeId) {
						temp = item[type]
					}
				})
				return temp
			},
			randomRgb() { //获取随机颜色
				for(let i = 0; i < 1000; i++) {
					let R = Math.floor(Math.random() * 255);
					let G = Math.floor(Math.random() * 255);
					let B = Math.floor(Math.random() * 255);

					this.colorlists.push('rgb(' + R + ',' + G + ',' + B + ')')

				}

			},
			baiduMap() {
				this.map = new BMap.Map("allmap"); // 创建地图实例
				let point = new BMap.Point(117.323065, 34.806747); // 创建点坐标
				this.map.centerAndZoom(point, 11); // 初始化地图，设置中心点坐标和地图级别
				this.map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
				var icon1 = new BMap.Icon(iconw, new BMap.Size(60, 55));
				var icon2 = new BMap.Icon(iconj, new BMap.Size(60, 55));
				var icon3 = new BMap.Icon(iconc, new BMap.Size(60, 55));
				icon1.setImageSize(new BMap.Size(60, 55)); //设置图标大小
				icon2.setImageSize(new BMap.Size(60, 55)); //设置图标大小
				icon3.setImageSize(new BMap.Size(60, 55)); //设置图标大小
				var b = new BMap.Bounds(new BMap.Point(116, 37.1), new BMap.Point(117.5, 37.6));
				var strictBounds = this.map.getBounds();
				// this.map.addEventListener("dragend", function () {
				// 	if (strictBounds.containsPoint(this.getCenter())) return;
				// 	var c = this.getCenter();
				// 	let x = c.lng,
				// 			y = c.lat,
				// 			maxX = strictBounds.getNorthEast().lng,
				// 			maxY = strictBounds.getNorthEast().lat,
				// 			minX = strictBounds.getSouthWest().lng,
				// 			minY = strictBounds.getSouthWest().lat;
				// 	if (x < minX) x = minX;
				// 	if (x > maxX) x = maxX;
				// 	if (y < minY) y = minY;
				// 	if (y > maxY) y = maxY;
				// 	this.centerAndZoom(new BMap.Point(117.323065,34.806747), 12);
				// })
			},
			loadMarker() {
				for(let p of this.pointMonitor) {
					var tagging = new BMap.Point(p.lng, p.lat);
					var mark = null;
					var yjMark = null;
					var bjMark = null;
					var pmMark = null;
					var tjMark = null;
					if(p.projectState == '1') {
						var icon = new BMap.Icon(iconwkg, new BMap.Size(60, 55));
						mark = new BMap.Marker(tagging, {
							icon: icon
						});
						this.wkgMapMarkers.info.push(mark)
						if(p.earlyNum > 0) {
							yjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconyjtb, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.early.push(yjMark)
						}
						if(p.alertNum > 0) {
							bjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconbjtb, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.alert.push(bjMark)
						}
						if(p.pm10Val > 0 && p.pm10Val <= 35) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy0, new BMap.Size(60, 55))
							});
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 35 && p.pm10Val <= 75) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy35, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 75 && p.pm10Val <= 115) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy75, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 115 && p.pm10Val <= 150) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy115, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 150 && p.pm10Val <= 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy150, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy250, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.pm10.push(pmMark)
						}
						if(p.tdCode != null && p.tdCode != undefined) {
							tjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(icontjzx, new BMap.Size(60, 55))
							})
							this.wkgMapMarkers.tj.push(tjMark)
						}
					} else if(p.projectState == '2') {
						var icon = new BMap.Icon(iconzj, new BMap.Size(60, 55));
						mark = new BMap.Marker(tagging, {
							icon: icon
						});
						this.zjMapMarkers.info.push(mark)
						if(p.earlyNum > 0) {
							yjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconyjtb, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.early.push(yjMark)
						}
						if(p.alertNum > 0) {
							bjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconbjtb, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.alert.push(bjMark)
						}
						if(p.pm10Val > 0 && p.pm10Val <= 35) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy0, new BMap.Size(60, 55))
							});
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 35 && p.pm10Val <= 75) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy35, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 75 && p.pm10Val <= 115) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy75, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 115 && p.pm10Val <= 150) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy115, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 150 && p.pm10Val <= 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy150, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy250, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.pm10.push(pmMark)
						}
						if(p.tdCode != null && p.tdCode != undefined) {
							tjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(icontjzx, new BMap.Size(60, 55))
							})
							this.zjMapMarkers.tj.push(tjMark)
						}

					} else if(p.projectState == '3') {
						var icon = new BMap.Icon(icontg, new BMap.Size(60, 55));
						mark = new BMap.Marker(tagging, {
							icon: icon
						});
						this.tgMapMarkers.info.push(mark)
						if(p.earlyNum > 0) {
							yjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconyjtb, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.early.push(yjMark)
						}
						if(p.alertNum > 0) {
							bjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconbjtb, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.alert.push(bjMark)
						}
						if(p.pm10Val > 0 && p.pm10Val <= 35) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy0, new BMap.Size(60, 55))
							});
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 35 && p.pm10Val <= 75) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy35, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 75 && p.pm10Val <= 115) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy75, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 115 && p.pm10Val <= 150) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy115, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 150 && p.pm10Val <= 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy150, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy250, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.pm10.push(pmMark)
						}
						if(p.tdCode != null && p.tdCode != undefined) {
							tjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(icontjzx, new BMap.Size(60, 55))
							})
							this.tgMapMarkers.tj.push(tjMark)
						}
					} else {
						var icon = new BMap.Icon(iconjg, new BMap.Size(60, 55));
						mark = new BMap.Marker(tagging, {
							icon: icon
						});
						this.jgMapMarkers.info.push(mark)
						if(p.earlyNum > 0) {
							yjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconyjtb, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.early.push(yjMark)
						}
						if(p.alertNum > 0) {
							bjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconbjtb, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.alert.push(bjMark)
						}
						if(p.pm10Val > 0 && p.pm10Val <= 35) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy0, new BMap.Size(60, 55))
							});
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 35 && p.pm10Val <= 75) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy35, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 75 && p.pm10Val <= 115) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy75, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 115 && p.pm10Val <= 150) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy115, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 150 && p.pm10Val <= 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy150, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.pm10Val > 250) {
							pmMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(iconpm10dy250, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.pm10.push(pmMark)
						}
						if(p.tdCode != null && p.tdCode != undefined) {
							tjMark = new BMap.Marker(tagging, {
								icon: new BMap.Icon(icontjzx, new BMap.Size(60, 55))
							})
							this.jgMapMarkers.tj.push(tjMark)
						}
					}
					this.information(p, mark)
					this.informationYJ(p, yjMark)
					this.informationBJ(p, bjMark)
					this.informationPM(p, pmMark)
					this.informationTJ(p, tjMark)
				}
				this.addMark();
				console.log(this.zjMapMarkers)
			},
			information(data, marker) {
				if(marker == null || marker == undefined) {
					return
				}
				marker.addEventListener("click", function(e) {
					var opts = {
						enableMessage: true,
					};
					var p = e.target;
					var point = new BMap.Point(data.lng, data.lat);
					var content;
					content = `<div class='bmap_con_box' style='font-size:12px'>
					<div class='bmap_tit'><img     src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
					<div class='bmap'><div class='bcom_tit'>项目名称 </div><div class='bmap_con'>${data.projectName}</div></div>
					<div class='bmap'><div class='bcom_tit'>施工单位名称</div><div class='bmap_con'>${data.constructionUnitName}</div></div>
					<div class='bmap'><div class='bcom_tit'>工程面积</div><div class='bmap_con'>${data.totalArea}平</div></div>
					<div class='bmap'><div class='bcom_tit'>工程造价</div><div class='bmap_con'>${data.totalCost}万元</div></div>
					<div class='bmap'><div class='bcom_tit'>实际开工日期</div><div class='bmap_con'>${data.actualCommencementDate}</div></div>
					<div class='bmap'><div class='bcom_tit'>计划竣工时间</div><div class='bmap_con'>${data.contractCompletionDate}</div></div>
					<div class='bmap'><div class='bcom_tit'>项目地址</div><div class='bmap_con'>${data.projectAddress}</div></div>
				</div>`
					var infoWindow = new BMap.InfoWindow(
						content, opts); // 创建信息窗口对象
					this.map.openInfoWindow(infoWindow, point); //开启信息窗口

				});
			},
			informationYJ(data, marker) {
				if(marker == null || marker == undefined) {
					return
				}
				marker.addEventListener("click", function(e) {
					var opts = {
						enableMessage: true,
					};
					var p = e.target;
					var point = new BMap.Point(data.lng, data.lat);
					var content;
					content = `<div class='bmap_con_box_bj' style='font-size:12px'>
					<div class='bmap_tit'><img    src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测设备 </div><div class='bmap_con'>${data.earlySbName}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测点名称</div><div class='bmap_con'>${data.earlyJcdName}</div></div>
					<div class='bmap'><div class='bcom_tit'>预警内容</div><div class='bmap_con'>${data.earlyContent}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测数值</div><div class='bmap_con'>${data.earlyData}</div></div>
					<div class='bmap'><div class='bcom_tit'>预警时间</div><div class='bmap_con'>${data.earlyTime}</div></div>
				</div>`
					var infoWindow = new BMap.InfoWindow(
						content, opts); // 创建信息窗口对象
					this.map.openInfoWindow(infoWindow, point); //开启信息窗口

				});
			},
			informationBJ(data, marker) {
				if(marker == null || marker == undefined) {
					return
				}
				marker.addEventListener("click", function(e) {
					var opts = {
						enableMessage: true,
					};
					var p = e.target;
					var point = new BMap.Point(data.lng, data.lat);
					var content;
					content = `<div class='bmap_con_box_bj' style='font-size:12px'>
					<div class='bmap_tit'><img     src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测设备 </div><div class='bmap_con'>${data.alertSbName}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测点名称</div><div class='bmap_con'>${data.alertJcdName}</div></div>
					<div class='bmap'><div class='bcom_tit'>报警内容</div><div class='bmap_con'>${data.alertContent}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测数值</div><div class='bmap_con'>${data.alertData}</div></div>
					<div class='bmap'><div class='bcom_tit'>报警时间</div><div class='bmap_con'>${data.alertTime}</div></div>
				</div>`
					var infoWindow = new BMap.InfoWindow(
						content, opts); // 创建信息窗口对象
					this.map.openInfoWindow(infoWindow, point); //开启信息窗口

				});
			},
			informationPM(data, marker) {
				if(marker == null || marker == undefined) {
					return
				}
				marker.addEventListener("click", function(e) {
					var opts = {
						enableMessage: true,
					};
					var p = e.target;
					var point = new BMap.Point(data.lng, data.lat);
					var content;
					content = `<div class='bmap_con_box_TJ' style='font-size:12px'>
					<div class='bmap_tit'><img     src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测点名称 </div><div class='bmap_con'>${data.pm10Jcd}</div></div>
					<div class='bmap'><div class='bcom_tit'>监测数值</div><div class='bmap_con'>${data.pm10Val}</div></div>
					<div class='bmap'><div class='bcom_tit'>获取时间</div><div class='bmap_con'>${data.pm10Time}</div></div>
				</div>`
					var infoWindow = new BMap.InfoWindow(
						content, opts); // 创建信息窗口对象
					this.map.openInfoWindow(infoWindow, point); //开启信息窗口

				});
			},
			informationTJ(data, marker) {
				if(marker == null || marker == undefined) {
					return
				}
				marker.addEventListener("click", function(e) {
					var opts = {
						enableMessage: true,
					};
					var p = e.target;
					var point = new BMap.Point(data.lng, data.lat);
					var content;
					content = `<div class='bmap_con_box_TJ' style='font-size:12px'>
					<div class='bmap_tit'><img   src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
					<div class='bmap'><div class='bcom_tit'>塔机编号 </div><div class='bmap_con'>${data.tdCode}</div></div>
					<div class='bmap'><div class='bcom_tit'>塔机名称</div><div class='bmap_con'>${data.tdName}</div></div>
					<div class='bmap'><div class='bcom_tit'>塔机状态</div><div class='bmap_con'>在线</div></div>
				</div>`
					var infoWindow = new BMap.InfoWindow(
						content, opts); // 创建信息窗口对象
					this.map.openInfoWindow(infoWindow, point); //开启信息窗口

				});
			},
			addPorjectMark(e) {
				if(e) {
					this.mapMonitorRadio = null
					this.addMark();

				}
			},
			addMark() {
				var thsn = this;
				this.map.clearOverlays();
				if(this.mapPorjectRadio == "0") {
					if(this.mapMonitorRadio == null) {
						this.wkgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.zjMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.tgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.jgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '5') {
						this.wkgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.zjMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.tgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.jgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '6') {
						this.wkgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.zjMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.tgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.jgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '7') {
						this.wkgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.zjMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.tgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.jgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '8') {
						this.wkgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.zjMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.tgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
						this.jgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					}
				} else if(this.mapPorjectRadio == "1") {
					if(this.mapMonitorRadio == null) {
						this.wkgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '5') {
						this.wkgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '6') {
						this.wkgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '7') {
						this.wkgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '8') {
						this.wkgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					}
				} else if(this.mapPorjectRadio == "2") {
					if(this.mapMonitorRadio == null) {
						this.zjMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '5') {
						this.zjMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '6') {
						this.zjMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '7') {
						this.zjMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '8') {
						this.zjMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					}
				} else if(this.mapPorjectRadio == "3") {
					if(this.mapMonitorRadio == null) {
						this.tgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '5') {
						this.tgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '6') {
						this.tgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '7') {
						this.tgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '8') {
						this.tgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					}
				} else if(this.mapPorjectRadio == "4") {
					if(this.mapMonitorRadio == null) {
						this.jgMapMarkers.info.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '5') {
						this.jgMapMarkers.early.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '6') {
						this.jgMapMarkers.alert.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '7') {
						this.jgMapMarkers.pm10.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					} else if(this.mapMonitorRadio == '8') {
						this.jgMapMarkers.tj.forEach(function(element) {
							thsn.map.addOverlay(element);
						});
					}
				}
			},
			initMapData() {
				var data = {};
				if(this.mapPorjectRadio != "" && this.mapPorjectRadio != "0") {
					data.state = this.mapPorjectRadio
				}
				if(this.mapMonitorRadio == "5") {
					data.alertType = '0'
				}
				if(this.mapMonitorRadio == "6") {
					data.alertType = '1'
				}
				if(this.mapMonitorRadio == "7") {
					data.pm10 = '1'
				}
				if(this.mapMonitorRadio == "8") {
					data.tower = '1'
				}
				getMAP(data).then(res => {
					console.log(res)
					this.pointMonitor = res.data
					this.loadMarker()
				}).catch((err) => {
					console.log(err);
				})
			},
			//环境检测
			environment() {
				let myChart = echarts.init(document.getElementById('environment'));
				let option = {
					tooltip: {
						trigger: "axis",
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "8%",
						left: "15%",
						top: '15%',
						bottom: '25%'
					},
					legend: {
						icon: "circle",
						top: 5,
						data: ['PM2.5', 'PM10'],
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						data: this.monitSituationPM.monitorDate,
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},
						boundaryGap: false,
						axisLabel: {
							color: '#fffff',
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
							show: true,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
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
							margin: 20,
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
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(129,140,251,0.6)'
								},
								{
									offset: 1,
									color: 'rgba(23, 60, 138, 0)'
								}
							])
						},
						data: this.monitSituationPM.PM25Num,

						itemStyle: {
							normal: {

								borderColor: '#818CFB',
								lineStyle: {
									color: '#818CFB'
								}
							}
						}
					}, {
						name: 'PM10',
						type: 'line',
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: '#57CB91'
								},
								{
									offset: 1,
									color: 'rgba(255, 255, 255, 0)'
								}
							])
						},
						data: this.monitSituationPM.PM10Num,

						itemStyle: {
							normal: {

								borderColor: '#57CB91',
								lineStyle: {
									color: '#57CB91'
								}
							}
						}
					}, ]
				};
				myChart.setOption(option)
			},
			//报警趋势
			alarmTrend() {
				let myChart = echarts.init(document.getElementById('alarmTrend'));
				let option = {
					tooltip: {
						trigger: "axis",
					},
					color: ["#818CFB", "#19F1FF", "#FBE56B", "#70AFEA"],
					grid: {
						right: "15%",
						left: "10%",
						top: '10%',
						bottom: '30%'
					},
					legend: {
						bottom: 0,
						left: 'center',
						textStyle: {
							color: "#000000",
							fontSize: 10
						},
						itemWidth: 20,
						itemHeight: 10,
						color: ["#818CFB", "#19F1FF", "#FBE56B", "#70AFEA"],
					},
					xAxis: {
						data: this.monitSituationNoise.monitorDate,
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},

						axisLabel: {
							color: '#000',
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
						},

					},
					yAxis: [{
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
						{
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
						},
					],
					series: [{
							name: '日间超标数',
							type: 'bar',
							barWidth: '15%',
							itemStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
										offset: 0,
										color: '#19F1FF'
									}, {
										offset: 1,
										color: 'rgba(19,166,183,0.4)'
									}]),
									barBorderRadius: 11,
								}
							},
							data: this.monitSituationNoise.dayNum
						}, {
							name: '夜间超标数',
							type: 'bar',
							barWidth: '15%',
							itemStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
										offset: 0,
										color: '#638CFE'
									}, {
										offset: 1,
										color: 'rgba(54,97,216,0.4)'
									}]),
									barBorderRadius: 11,
								}

							},
							data: this.monitSituationNoise.nightNum
						},
						{
							name: '日间超标数占比',
							type: 'line',
							symbol: 'none',
							yAxisIndex: 1,
							smooth: false,
							data: this.monitSituationNoise.dayNumProp,
							itemStyle: {
								normal: {
									color: '#FBE56B',
									lineStyle: {
										color: '#FBE56B'
									}
								}
							}
						},
						{
							name: '夜间超标数占比',
							type: 'line',
							symbol: 'none',
							yAxisIndex: 1,
							smooth: false,
							data: this.monitSituationNoise.nightNumProp,
							itemStyle: {
								normal: {
									color: '#56B0F7',
									lineStyle: {
										color: '#56B0F7'
									}
								}
							}
						},
					]
				};
				myChart.setOption(option)
			},
			//当日预警分类情况
			earlyWarning() {
				let myChart = echarts.init(document.getElementById('earlyWarning'));
				let that = this;
				myChart.on('click', function(event) {
					switch(event.data.name) {
						case 'PM2.5':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case 'PM10':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});;
							break;
						case '噪音':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '温度':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '湿度':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '风力':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '风向':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '风速':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case 'TSP':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});
							break;
						case '气压':
							that.goToUrl('monitorPointAlert', {
								monitorType: '9',
								pointName: event.data.name
							});;
							break;
					};
				})
				let option = {
					tooltip: {
						trigger: 'item'
					},
					color: ['#5B96FA', '#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353', '#77B8F8'],
					legend: {
						icon: "circle",
						orient: 'vertical',
						right: 0,
						top: 'center',
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000000",
							fontSize: 10
						},
					},
					series: [{
						type: "pie",
						radius: ["35%", "55%"],
						center: ["38%", "50%"],
						hoverAnimation: true,
						z: 10,
						itemStyle: {
							normal: {
								borderWidth: 5,
								borderColor: "#D7EBFF"
							}
						},
						label: {
							show: true,
							color: '#000'
						},
						data: this.EarlyWarning,
					}]
				}
				myChart.setOption(option)
			},
			speedFn() { //项目进度图
				let myChart = echarts.init(document.getElementById('speed'));
				let that = this;
				myChart.on('click', function(event) {
					let projectState = '';
					if(event.data.dictValue == undefined) {
						projectState = "4"
					} else {
						projectState = event.data.dictValue
					}
					that.goToUrl('projectInfo', {
						'projectState': projectState
					});
				})
				let option = {
					tooltip: {
						trigger: 'item',
					},
					color: ['#5c87fc', '#1edfc0', '#f6ca59', '#e250b1', '#3b5cf9', '#1bdff1', '#6cf371', '#f5835f', '#6c2be7', '#f3456d'],
					legend: {
						icon: "circle",
						orient: 'vertical',
						right: 30,
						top: 'center',
						itemWidth: 10,
						itemHeight: 10,
						itemGap: 20,
						textStyle: {
							color: "#000",
							fontSize: 12
						},
					},
					series: [{
						type: 'pie',
						radius: '60%',
						center: ['30%', '50%'],
						roseType: 'area',
						label: {
							show: true,
							color: '#000',
							formatter: '{c}',
						},
						labelLine: {
							normal: {
								length: 10,
								length2: 5,
								lineStyle: {
									width: 2
								}
							}
						},
						data: this.speedlist
					}]
				}
				myChart.setOption(option)
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
			mapPorject(val) {

			},
		}
	}
</script>

<style scoped lang="scss">
	.content_box {
		overflow: hidden;
		height: 100%;
		display: flex;
		padding-top: 15px;
	}
	
	.con_left_box {
		width: 4.16rem;
		height: 100%;
		margin-left: 1%;
	}
	
	.con_right_box {
		width: 12.2rem;
		height: 100%;
	}
	
	.subject {
		height: 0.16rem;
		font-size: 0.16rem;
		font-weight: 500;
		color: #404040;
		display: flex;
		align-items: center;
		margin-left: 0.08rem;
		margin-top: 0.12rem;
		img {
			margin-right: 0.1rem;
			height: 0.16rem;
			width: 0.16rem;
		}
	}
	
	.early_warning_box {
		overflow: hidden;
		width: 4.16rem;
		height: 32.9%;
		background: #ffffff;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
	}
	
	#earlyWarning {
		width: 100%;
		height: calc(100% - 0.28rem);
	}
	
	.alarm_trend_box {
		margin-top: 0.2rem;
		overflow: hidden;
		width: 4.16rem;
		height: 30%;
		background: #ffffff;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
	}
	
	#alarmTrend {
		width: 100%;
		height: calc(100% - 0.28rem);
	}
	
	.environment_box {
		margin-top: 0.2rem;
		overflow: hidden;
		width: 4.16rem;
		height: 30%;
		background: #ffffff;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
	}
	
	#environment {
		width: 100%;
		height: calc(100% - 0.28rem);
	}
	
	.header_top_box {
		height: 13.05%;
	}
	
	.type_box {
		/*width: 4.04rem;*/
		width: 3.9rem;
		height: 100%;
		float: left;
		background-color: red;
		margin-left: 1.28%;
	}
	
	.header_tp1 {
		background: url(../index/img/index_tp1.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.header_tp2 {
		background: url(../index/img/index_tp3.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.header_tp3 {
		background: url(../index/img/index_tp2.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.equipment_tit {
		margin-left: 0.39rem;
		margin-top: 0.12rem;
		font-weight: 500;
		font-size: 0.18rem;
		color: #ffffff;
	}
	
	.function_box {
		color: #ffffff;
		margin-left: 0.39rem;
		margin-top: 0.12rem;
		float: left;
		overflow: hidden;
		cursor: pointer;
	}
	
	.function_tit {
		float: left;
		font-size: 0.14rem;
		margin-top: 0.1rem;
		margin-right: 0.07rem;
	}
	
	.function_num {
		font-size: 0.3rem;
		float: left;
	}
	
	.num {
		color: #ffffff;
		margin-left: 0.6rem;
		float: left;
		margin-top: 0.12rem;
		overflow: hidden;
		cursor: pointer;
	}
	
	.baidumap {
		float: left;
		height: 82.6%;
		width: 7.936rem;
		margin-top: 0.1rem;
		margin-left: 1.28%;
	}
	
	.statistics2_dom {
		display: inline-block;
		/*border: 1px solid #0FC9E7;*/
		height: 100%;
		border-radius: 5px;
		width: 42%;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		cursor: pointer;
	}
	
	.statistics2_dom1 {
		display: inline-block;
	}
	
	.statistics2_dom2 {
		display: inline-block;
	}
	/*.project_details {*/
	/*float: left;*/
	/*width: 3.85rem;*/
	/*height: 35.3%;*/
	/*margin-left: 0.13rem;*/
	/*margin-top: 0.14rem;*/
	/*background: #ffffff;*/
	/*box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);*/
	/*border-radius: 0.05rem;*/
	/*display: flex;*/
	/*flex-direction: column;*/
	/*align-items: center;*/
	/*justify-content: space-around;*/
	/*}*/
	
	.project_type {
		width: 3.58rem;
		height: 24.4%;
		display: flex;
		align-items: center;
	}
	
	.pro_type1 {
		background: url(../index/img/pro1.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.pro_type2 {
		background: url(../index/img/pro2.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.pro_type3 {
		background: url(../index/img/pro3.png) no-repeat;
		background-size: 100% 100%;
	}
	
	.pro_tit {
		color: #404040;
		margin-left: 0.47rem;
		font-size: 0.16rem;
	}
	
	.pro_num {
		color: #8282ff;
		width: 1rem;
		text-align: center;
		font-size: 0.3rem;
		margin-left: 0.5rem;
	}
	
	.pro_company {
		font-size: 0.16rem;
		color: #404040;
		margin-left: 0.2rem;
	}
	
	.pro_blue {
		color: #2899ff;
	}
	
	.pro_green {
		color: #33c270;
	}
	
	.project_num {
		float: left;
		width: 3.95rem;
		margin-top: 0.2rem;
		height: 49.52%;
		background: #ffffff;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
		margin-left: 1.28%;
	}
	
	.project_details {
		float: left;
		width: 3.95rem;
		margin-top: 0.2rem;
		height: 30.3%;
		background: #ffffff;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
		margin-left: 1.28%;
	}
	
	.statistics {
		width: 3.6rem;
		margin: 0 auto;
		height: calc(100% - 1.28rem);
	}
	
	.statistics2 {
		width: 3.6rem;
		margin: 0 auto;
		height: 18.13%;
		display: flex;
		justify-content: space-around;
		margin-top: 0.1rem;
	}
	
	.statistics_tit {
		display: flex;
		align-items: center;
		margin-top: 3.54%;
		border-bottom: 1px solid #286DFF;
		line-height: 25px;
	}
	
	.statistics_tit div {
		color: #286dff;
		font-size: 0.14rem;
		text-align: center;
	}
	
	.statistics_con_box {
		height: 87%;
		width: 100%;
		overflow-x: auto;
		margin-top: 0.08rem;
	}
	
	.statistics_con_box::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}
	
	.statistics_con_box {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	
	.statistics_con {
		width: 100%;
		height: 14.4%;
		border-bottom: 1px solid rgba(0, 0, 0, 0.15);
		display: flex;
		align-items: center;
	}
	
	.con_title {
		text-align: center;
		display: flex;
		align-items: center;
		color: #404040;
	}
	
	.con_num {
		text-align: center;
		color: #404040;
		cursor: pointer;
	}
	
	.con_znum {
		text-align: center;
		color: #404040;
		cursor: pointer;
	}
	
	.block {
		height: 0.08rem;
		width: 0.08rem;
		margin-right: 0.05rem;
		justify-content: center;
		background: rgba(0, 166, 240, 1);
	}
	
	.map_but_tem {
		position: absolute;
		bottom: 4%;
		margin-left: 2%;
		box-shadow: 2px 2px 2px #ccc;
		background-color: #FFFFFF;
	}
	/* 隐藏边角 */
	
	/deep/ .BMap_pop> :nth-child(1) {
		display: none;
	}
	
	/deep/ .BMap_pop> :nth-child(3) {
		display: none;
	}
	
	/deep/ .BMap_pop> :nth-child(5) {
		display: none;
	}
	
	/deep/ .BMap_pop> :nth-child(7) {
		display: none;
	}
	
	/deep/ .BMap_pop> :nth-child(8) {
		display: none;
	}
	/* 右上角删除按键 */
	
	/deep/.BMap_pop img {
		top: 43px !important;
		right: 5px !important;
		display: none;
	}
	
	/deep/.BMap_top {
		display: none;
	}
	
	/deep/.BMap_bottom {
		display: none;
	}
	
	/deep/.BMap_center {
		display: none;
	}
	
	/deep/ .BMap_pop> :nth-child(9) {
		left: -8px !important;
		top: 35px !important;
	}
	
	/deep/.bmap_con_box {
		background: url(../index/img/pcmapbg.png) no-repeat;
		background-size: 100% 100%;
		width: 406px !important;
		height: 228px !important;
	}
	
	/deep/.bmap_con_box_bj {
		background: url(../index/img/pcmapbg.png) no-repeat;
		background-size: 100% 100%;
		width: 300px !important;
		height: 200px !important;
	}
	
	/deep/.bmap_con_box_TJ {
		background: url(../index/img/pcmapbg.png) no-repeat;
		background-size: 100% 100%;
		width: 200px !important;
		height: 120px !important;
	}
	
	/deep/ .bmap_tit {
		height: 37px;
		line-height: 37px;
		display: flex;
		align-items: center;
		margin-bottom: 10px;
		background: rgba(40, 109, 255, 0.04);
		color: #286dff;
	}
	
	/deep/ .bmap_tit img {
		margin-left: 26px;
		margin-right: 7px;
	}
	
	/deep/.bmap_tit .bmap_con {
		/*width: 240px;*/
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
	/deep/ .bmap {
		margin-left: 30px;
		line-height: 20px;
		display: flex;
	}
	
	/deep/ .bmap .bcom_tit {
		width: 75px;
		text-align: justify;
		height: 20px;
		overflow: hidden;
		color: #404040;
	}
	
	/deep/ .bmap .bcom_tit:after {
		content: " ";
		display: inline-block;
		width: 100%;
	}
	
	/deep/ .bmap .bmap_con {
		width: 200px;
		margin-left: 36px;
		color: #666666;
	}
	
	/deep/.el-radio-button {
		float: left;
		width: 84px;
		height: 31px;
	}
	
	/deep/.el-select {
		float: left;
	}
	
	/deep/.el-input__inner {
		height: 31px;
	}
	
	/deep/.el-select-dropdown {
		margin-top: 0 !important;
	}
	
	/deep/.popper__arrow {
		display: none;
	}
	
	/deep/.el-input__inner {
		border: 0;
		border-radius: 0;
		padding-right: 10px;
	}
	
	/deep/.el-input--suffix {
		width: 57px;
	}
	
	/deep/.el-input__suffix {
		right: 0;
	}
	
	/deep/.el-radio-button__inner {
		width: 84px;
		height: 31px;
		border: 0;
		border-radius: 0;
		padding-left: 0px;
	}
	
	.an_img {
		margin-right: 5px;
		//border-left: 1px solid #EBF2FF;
		//padding-left: 18px;
	}
</style>