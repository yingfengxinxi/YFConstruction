<template>
	<div class="content_box">
		<div class="con_left_box">
			<div class="early_warning_box">
				<div class="subject"><img src="../../../../static/img/Symbol.png">当日环境报警分析</div>
				<div id="earlyWarning">

				</div>
			</div>
			<div class="alarm_trend_box">
				<div class="subject"><img src="../../../../static/img/Symbol.png">报警趋势</div>
				<div id="alarmTrend"></div>
			</div>
			<div class="environment_box">
				<div class="subject"><img  src="../../../../static/img/Symbol.png">环境监测</div>
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
						<div class="function_box" @click="goStateList('40','1')">
							<div class="function_tit">在线</div>
							<div class="function_num">{{envMonitPont.onLineNum}}</div>
						</div>
						<div class="num" @click="goStateList('40','')">
							<div class="function_tit">总数</div>
							<div class="function_num">{{envMonitPont.totalnum}}</div>
						</div>
					</div>
				</div>
				<div class="type_box header_tp1">
					<div class="equipment_tit">
						喷淋设备
					</div>
					<div class="equipment_con">
						<div class="function_box" @click="goStateList('50','1')">
							<div class="function_tit">运行</div>
							<div class="function_num">{{conv(50,'onLineNum')}}</div>
						</div>
						<div class="num" @click="goStateList('50','')">
							<div class="function_tit">总数</div>
							<div class="function_num">{{conv(50,'totalnum')}}</div>
						</div>
					</div>
				</div>
				<div class="type_box header_tp2">
					<div class="equipment_tit">
						监控设备
					</div>
					<div class="equipment_con">
						<div class="function_box" @click="goStateList('30','1')">
							<div class="function_tit">运行</div>
							<div class="function_num">{{conv(30,'onLineNum')}}</div>
						</div>
						<div class="num" @click="goStateList('30','')">
							<div class="function_tit">总数</div>
							<div class="function_num">{{conv(30,'totalnum')}}</div>
						</div>
					</div>
				</div>

			</div>
			<div id="allmap" class="baidumap"></div>
			<div class="project_details">
				<!-- <div class="project_type pro_type1" @click="goenvAlert(0)">
					<div class="pro_tit">环境预警项目</div>
					<div class="pro_num">{{projectAlarms.alarmsProjectNum}}</div>
					<div class="pro_company">个</div>
				</div> -->
				<div class="project_type pro_type1" @click="goenvAlert(1)">
					<div class="pro_tit">报警项目数量</div>
					<div class="pro_num">{{projectAlarms.alarmstNum}}</div>
					<div class="pro_company">个</div>
				</div>
				<div class="project_type pro_type2" @click="goenvAlert(3)">
					<div class="pro_tit">接入项目数量</div>
					<div class="pro_num pro_blue">{{projectAlarms.projectNum}}</div>
					<div class="pro_company">个</div>
				</div>
				<div class="project_type pro_type3" @click="goenvAlert(3)">
					<div class="pro_tit">已建项目数量</div>
					<div class="pro_num pro_green">{{projectAlarms.totalNumber}}</div>
					<div class="pro_company">个</div>
				</div>
			</div>
			<div class="project_num">
				<div class="subject"><img   src="../../../../static/img/Symbol.png">项目报警数量统计</div>
				<div class="statistics">
					<div class="statistics_tit">
						<div style="width: 45%;"></div>
						<div style="width: 25.25%;">今日报警数</div>
						<div style="width: 25.25%;">总计报警数</div>
					</div>
					<div class="statistics_con_box">
						<div class="statistics_con" v-for="(item,index) in projectAlarmNum" :key="index">
							<div class="con_title" style="width: 45%;">
								<div class="block"></div>
								<div class="block_title">{{item.projectAbbreviation}}</div>
							</div>
							<div class="con_num" style="width: 25.25%;">{{item.todayAlarmNum}}</div>
							<div class="con_znum" style="width: 25.25%;">{{item.totalalarmnum}}</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import icon1 from "./img/bj1.png"
import icon2 from "./img/bj2.png"
import icon3 from "./img/bj3.png"
import icon4 from "./img/bj4.png"
import bmap from './img/mapic.png'
import * as echarts from 'echarts';
import { queryMenuListByPath } from '@/api/admin/menu'
import { envMonitPont, equipmentNum, alarmsNum1, alarmsNum2, projectNum, earlyWarning, projectAlarmNum, monitSituationNoise, monitSituationPM, getMAP } from './indexApi.js'
export default {
	data () {
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
				'projectNum': '0',
				'totalNumber': '0'
			},
			//{ value: 11, name: 'PM10' }, { value: 21, name: 'PM2.5' }, { value: 13, name: '噪声' }, { value: 14, name: '湿度' }, { value: 32, name: '温度' }, { value: 32, name: 'TPS' },
			EarlyWarning: [],
			projectAlarmNum: [],
			monitSituationNoise: [],
			monitSituationPM: [],
			pointMonitor: [],
		}
	},
	created () {
		this.randomRgb()
	},
	mounted () {
		this.getLargeScreen()
		this.remsize()
		getMAP().then(res => {
			this.pointMonitor = res.data
			var iconImg1 = new BMap.Icon(icon1, new BMap.Size(40, 37));
			var iconImg2 = new BMap.Icon(icon2, new BMap.Size(40, 37));
			var iconImg3 = new BMap.Icon(icon3, new BMap.Size(40, 37));
			var iconImg4 = new BMap.Icon(icon4, new BMap.Size(40, 37));
			iconImg1.setImageSize(new BMap.Size(40, 37));//设置图标大小
			iconImg2.setImageSize(new BMap.Size(40, 37));//设置图标大小
			iconImg3.setImageSize(new BMap.Size(40, 37));//设置图标大小
			iconImg4.setImageSize(new BMap.Size(40, 37));//设置图标大小
			for (let p of this.pointMonitor) {
				var tagging = new BMap.Point(p.lng, p.lat);
				// console.log("===p.projectState="+p.projectState)
				if(p.projectState==1){
					this.marker= new BMap.Marker(tagging, {icon: iconImg1}); // 未接入
				}else if(p.projectState==2){
					this.marker= new BMap.Marker(tagging, {icon: iconImg2}); // 已接入
				}else if(p.projectState==4){
					this.marker= new BMap.Marker(tagging, {icon: iconImg4}); // 已完成
				}else{
					this.marker= new BMap.Marker(tagging, {icon: iconImg3}); // 暂停
				}
//				this.marker = new BMap.Marker(tagging, { icon: icon1 });
				this.map.addOverlay(this.marker)
				this.information(p)
			}
		}).catch((err) => {
			console.log(err);
		})
		this.baiduMap()

	},
	methods: {
		goStateList (typeId, online) {
			if (online != '') {
				this.goToUrl('equiState', { typeId: typeId, online: online })
			} else {
				this.goToUrl('equiState', { typeId: typeId })
			}
		},

		goenvAlert (num) {
			if (num == 3) {
				this.goToUrl('engineering', { alertType: num })
			} else {
				this.goToUrl('envAlert', { alertType: num })

			}
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
		//同步查询所有在线监控设备
		async monitorOnlineNum () {
			let allDeviceList = [];
			let onlineNum = 0;
			//获取所有设备列表
			await this.$axios({
				method: 'get',
				url: process.env.VIDEO_URL + "/api/device/query/devices",
				params: { "page": "1", "count": "9999" }
			}).then(function (res) {
				console.log(res);
				allDeviceList = res.data.list;
			}).catch(function (error) {
				console.log(error)
			});
			//轮训设备列表，查询是否在线
			allDeviceList.forEach(element => {
				this.$axios({
					method: 'get',
					url: process.env.VIDEO_URL + "/api/device/query/devices/" + element.deviceId + "/status",
					params: { "deviceId": element.deviceId }
				}).then(function (res) {
					if (res.data.indexOf("Timeout") != -1) {
						onlineNum++;
					}
				}).catch(function (error) {
					console.log(error)
				});
			});
			this.equipmentNum[0].onLineNum = onlineNum
		},
		//大屏数据获取
		getLargeScreen () {
			//环境监测点数据
			envMonitPont().then(res => {
				this.envMonitPont = res.data
			}).catch((err) => {
				console.log(err);
			})

			//设备分类数据
			equipmentNum().then(res => {
				this.equipmentNum = res.data;
				// this.monitorOnlineNum();
				console.log(this.equipmentNum)
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
				this.projectAlarms.totalNumber = res.data.totalNumber
			}).catch((err) => {
				console.log(err);
			})

			//预警分类
			earlyWarning().then(res => {
				this.EarlyWarning = res.data;
				this.earlyWarning()
			}).catch((err) => {
				console.log(err);
			})

			//按项目分类统计报警数
			// this.postAxios('/build/homePage/projectAlarmNum', {},'json').then(res => {
			// this.projectAlarmNum=res.data.data;
			// }).catch(err => {})
			projectAlarmNum().then(res => {
				this.projectAlarmNum = res.data;
			}).catch((err) => {
				console.log(err);
			})

			//噪音监控情况(报警趋势)
			monitSituationNoise().then(res => {
				this.monitSituationNoise = res.data;
				this.alarmTrend()
			}).catch((err) => {
				console.log(err);
			})

			//PM2.5、PM10监控情况（环境检测）
			monitSituationPM().then(res => {
				this.monitSituationPM = res.data;
				this.environment()
			}).catch((err) => {
				console.log(err);
			})

		},
		conv (typeId, type) {
			// if (typeId == 30 && type == 'onLineNum') {
			// 	return 0;
			// }
			let temp = 0;
			this.equipmentNum.forEach(item => {
				if (item.typeId == typeId) {
					temp = item[type]
				}
			})
			return temp
		},
		randomRgb () { //获取随机颜色
			for (let i = 0; i < 1000; i++) {
				let R = Math.floor(Math.random() * 255);
				let G = Math.floor(Math.random() * 255);
				let B = Math.floor(Math.random() * 255);

				this.colorlists.push('rgb(' + R + ',' + G + ',' + B + ')')

			}

		},
		baiduMap () {
			this.map = new BMap.Map("allmap");  // 创建地图实例
			let point = new BMap.Point(116.79702, 37.62901);  // 创建点坐标
			this.map.centerAndZoom(point, 14);  // 初始化地图，设置中心点坐标和地图级别
			this.map.enableScrollWheelZoom(true);  //开启鼠标滚轮缩放
			

			var b = new BMap.Bounds(new BMap.Point(116, 37.1), new BMap.Point(117.5, 37.6));
			var strictBounds = this.map.getBounds();
			this.map.addEventListener("dragend", function () {
				if (strictBounds.containsPoint(this.getCenter())) return;
				var c = this.getCenter();
				let x = c.lng,
					y = c.lat,
					maxX = strictBounds.getNorthEast().lng,
					maxY = strictBounds.getNorthEast().lat,
					minX = strictBounds.getSouthWest().lng,
					minY = strictBounds.getSouthWest().lat;
				if (x < minX) x = minX;
				if (x > maxX) x = maxX;
				if (y < minY) y = minY;
				if (y > maxY) y = maxY;
				this.centerAndZoom(new BMap.Point(116.79702, 37.65301), 14);
			})
		},
		information (data) {
			this.marker.addEventListener("click", function (e) {
				var opts = {
					enableMessage: true,
				};
				var p = e.target;
				var point = new BMap.Point(data.lng, data.lat);
				var content;
				content = `<div class='bmap_con_box' style='font-size:12px'>
					<div class='bmap_tit'><img      src=${bmap} style='display:block!important;width:12px;height:12px'><div class='bcom_tit'>项目简称 : </div><div class='bmap_con'> ${data.projectAbbreviation}</div></div>
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
		//环境检测
		environment () {
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
						show: true,
						color: '#A3E9FC'
					},

					axisLabel: {
						color: '#fffff',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 4 // 每行能显示的字的个数
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
				},]
			};
			myChart.setOption(option)
		},
		//报警趋势
		alarmTrend () {
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
						show: false,
						color: '#A3E9FC'
					},

					axisLabel: {
						color: '#000',
						formatter: function (params) {
							var newParamsName = '' // 最终拼接成的字符串
							var paramsNameNumber = params.length // 实际标签的个数
							var provideNumber = 4 // 每行能显示的字的个数
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
		earlyWarning () {
			let myChart = echarts.init(document.getElementById('earlyWarning'));
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

<style scoped lang="scss">
.content_box {
  padding: 0.1rem 0.1rem 0px 0.1rem;
  overflow: hidden;
  height: 100%;
  display: flex;
  justify-content: space-evenly;
}

.con_left_box {
  width: 4.16rem;
  height: 100%;
}

.con_right_box {
  width: 12.15rem;
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
  width: 4.04rem;
  height: 100%;
  float: left;
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
}

.baidumap {
  float: left;
  height: 82.6%;
  width: 7.98rem;
  margin-top: 0.13rem;
  margin-left: 0.15rem;
}

.project_details {
  float: left;
  width: 3.85rem;
  height: 35.3%;
  margin-left: 0.13rem;
  margin-top: 0.14rem;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 0.05rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
}

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
  width: 3.85rem;
  margin-top: 0.2rem;
  margin-left: 0.13rem;
  height: 44.52%;
  background: #ffffff;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 0.05rem;
}

.statistics {
  width: 3.42rem;
  margin: 0 auto;
  height: calc(100% - 0.28rem);
}

.statistics_tit {
  display: flex;
  align-items: center;
  margin-top: 3.54%;
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
  height: 12.5%;
  border-top: 1px solid rgba(0, 0, 0, 0.15);
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
}

.con_znum {
  text-align: center;
  color: #404040;
}

.block {
  height: 0.08rem;
  width: 0.08rem;
  margin-right: 0.13rem;
  justify-content: center;
  background: rgba(0, 166, 240, 1);
}
/* 隐藏边角 */
/deep/ .BMap_pop > :nth-child(1) {
  display: none;
}
/deep/ .BMap_pop > :nth-child(3) {
  display: none;
}
/deep/ .BMap_pop > :nth-child(5) {
  display: none;
}
/deep/ .BMap_pop > :nth-child(7) {
  display: none;
}
/deep/ .BMap_pop > :nth-child(8) {
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
/deep/ .BMap_pop > :nth-child(9) {
  left: -8px !important;
  top: 35px !important;
}
/deep/.bmap_con_box {
  background: url(../index/img/pcmapbg.png) no-repeat;
  background-size: 100% 100%;
  width: 406px !important;
  height: 228px !important;
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
  width: 240px;
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
</style>
