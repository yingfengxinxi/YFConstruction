<template>
	<div id="cloudCenter">
		<div class="content_left_box">
			<div class="equipment_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">设备类型占比</div>
				<div id="equipment">

				</div>
			</div>
			<div class="accessType_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">接入设备数状态</div>
				<div class="access_box">
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access1.png"/>
						<div class="access_tit">塔吊</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[1]?lineData[1].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[1]?lineData[1].zs:0}}</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access2.png"/>
						<div class="access_tit">升降机</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[2]?lineData[2].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[2]?lineData[2].zs:0}}</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access3.png"/>
						<div class="access_tit">高支模</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[3]?lineData[3].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[3]?lineData[3].zs:0}}</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access4.png"/>
						<div class="access_tit">深基坑</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[0]?lineData[0].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[0]?lineData[0].zs:0}}</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access5.png"/>
						<div class="access_tit">卸料平台</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[4]?lineData[4].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[4]?lineData[4].zs:0}}</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access6.png"/>
						<div class="access_tit">视频监控</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="equassetsGRP()">{{lineData[5]?lineData[5].zx:0}}</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="equassetsGRP()">{{lineData[5]?lineData[5].zs:0}}</div>
					</div>

					<!-- 跳转到设备状态页面，演示完毕需解开 -->
					<!-- <div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access1.png"/>
						<div class="access_tit">塔吊</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('2', '1')">6</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('2', '')">10</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access2.png"/>
						<div class="access_tit">升降机</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('3', '1')">6</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('3', '')">6</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access3.png"/>
						<div class="access_tit">高支模</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('11', '1')">8</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('11', '')">12</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access4.png"/>
						<div class="access_tit">深基坑</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('1', '1')">12</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('1', '')">14</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access5.png"/>
						<div class="access_tit">卸料平台</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('12', '1')">2</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('12', '')">6</div>
					</div>
					<div class="access_list">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/access6.png"/>
						<div class="access_tit">视频监控</div>
						<div class="access_line"></div>
						<div class="access_yx">运行</div>
						<div class="access_yx_num" @click="goStateList('99', '1')">15</div>
						<div class="access_zs">总数</div>
						<div class="access_zs_num" @click="goStateList('99', '')">18</div>
					</div> -->
				</div>
			</div>
		</div>
		<div class="content_center_box">
			<div class="police_list_box">
				<div class="more" @click="goenvMonitorPointAlert">
					更多>>
				</div>
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">报警列表</div>
				<div class="police_list">
					<div class="police_list_title">
						<div style="width: 20%;">项目名称</div>
						<div style="width: 40%; ">报警内容</div>
						<div style="width: 20%;">超标值</div>
						<div style="width: 20%;">报警时间</div>
					</div>
					<div class="prolice_list_conbox">
						<div class="police_list_con" v-for="item in datalist">
							<div style="width: 20%;">{{item.a}}</div>
							<div style="width: 40%;">{{item.b}}</div>
							<div style="width: 20%;">{{item.c}}</div>
							<div style="width: 20%;">{{item.d}}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="contrast_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">近30天项目报警数对比</div>
				<div id="contrast">

				</div>
			</div>
			<div class="contrast_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">报警多发报警时段</div>
				<div id="multiple">

				</div>
			</div>
		</div>
		<div class="content_right_box">
			<div class="trend_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">报警趋势</div>
				<div id="trend"></div>
			</div>
			<div class="proportion_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">报警占比分析</div>
				<div id="proportion"></div>
			</div>
			<div class="proportion_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">项目报警数量分析</div>
				<div class="num_fenxi">
					<div class="fenxi_tit">
						<div class="fenxi_list">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="policeicon" src="../../../../../static/img/policeicon.png"/>
							<div class="fx_tit">
								当日预警
							</div>
							<div class="fx_num">
								5
							</div>
							<div class="fx_until">次</div>
						</div>
						<div class="fenxi_list">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="policeicon" src="../../../../../static/img/policeicon.png"/>
							<div class="fx_tit">
								当日报警
							</div>
							<div class="fx_num">
								7
							</div>
							<div class="fx_until">次</div>
						</div>
					</div>
					<div class="fx_con">
						<div class="fx_hezi">
							<div class="hz_tit">
								累计预警
							</div>
							<div class="hz_con">
								<div class="hz_num">42</div><div class="hz_until">次</div>
							</div>

						</div>
						<div class="fx_hezi">
							<div class="hz_tit">
								累计报警
							</div>
							<div class="hz_con">
								<div class="hz_num">67</div><div class="hz_until">次</div>
							</div>

						</div>
						<div class="fx_hezi">
							<div class="hz_tit">
								累计处理
							</div>
							<div class="hz_con">
								<div class="hz_num">64</div><div class="hz_until">个</div>
							</div>

						</div>
						<div class="fx_hezi">
							<div class="hz_tit">
								未处理
							</div>
							<div class="hz_con">
								<div class="hz_num">3</div><div class="hz_until">个</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import { queryMenuListByPath } from '@/api/admin/menu'
	import {getTrand,getLine} from "../api/dangerBigProject";

	export default {
		data() {
			return {
				datalist:[{
					a:'幸福小区项目',
					b:'塔机起重力矩超过设定极限630kn',
					c:'635kn·m',
					d:'2021-11-29'
				},
				{
					a:'丰润花园项目',
					b:'塔机起升高度超过报警上限160m',
					c:'162m',
					d:'2021-11-29'
				},{
					a:'富力城项目二期',
					b:'噪音值超过报警上限55dB',
					c:'60dB',
					d:'2021-11-29'
				},{
					a:'地鑫新河花园',
					b:'监测值超过报警上限20μg/m³',
					c:'35μg/m³',
					d:'2021-11-29'
				},{
					a:'丰润花园项目',
					b:'升降机倾角超过报警上限12°',
					c:'13.5°',
					d:'2021-11-29'
				}],
        lineData:[],
			}
		},
		mounted() {
			getTrand().then(data => {
				this.gettrend(data)
			}).catch(() => { })
			this.getequipment()
			this.getcontrast()
			this.multiple()
			this.proportion()
			this.remsize()

      getLine().then(data => {
        this.lineData = data.data
        console.log('设备数据',data)

      }).catch(() => { })
		},
		methods: {
			proportion(){
				let myChart = echarts.init(document.getElementById('proportion'));
				let option = {
					tooltip: {
						trigger: 'item',
					},
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
					color: ['#5B96FA', '#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353', '#77B8F8'],
					series: [{
						name: '设备类型',
						type: 'pie',
						radius: ['38%', '60%'],
						center: ['40%', '50%'],
						avoidLabelOverlap: false,
						itemStyle: {
							normal: {
								borderWidth: 5,
								borderColor: "#D7EBFF"
							}
						},
						label: {

						},
						labelLine: {
							show: true,
						},
						data: [{name:"深基坑",value:25},{name:"升降机",value:13},{name:"高支模",value:22},{name:"塔机",value:10},
						{name:"卸料平台",value:15}]
					}]
				};
				myChart.setOption(option)
			},
			gettrend(data){
				let myChart = echarts.init(document.getElementById('trend'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					color:['#57CB91'],
					grid: {
						right: "15%",
						left: "8%",
						top: '25%',
						bottom: '20%'
					},
					color: ["#818CFB", "#19F1FF", "#FBE56B", "#70AFEA"],
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
						 data:data.data.monitorDate,
						type: 'category',
						axisLine: {
							show: false,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000000',
						},
						splitLine: {
							show: false,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis:[{
						type: 'value',
						name: '(次)',
						nameTextStyle: {
							padding: [0, 0, 0, -10]
						},
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
					},{
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
							margin: 10,
							formatter: '{value} %',
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					}],
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
                        //[12, 18, 20, 16, 12, 15, 32]
                        data:[3,2,4,6,1,2,1]
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
                        // [23, 15, 14, 25, 26, 14, 18]
                        data:[2,2,5,2,0,1,0]
                    },
                        {
                            name: '日间超标数占比',
                            type: 'line',
                            symbol: 'none',
                            yAxisIndex: 1,
                            smooth: false,
                            //[12, 16, 12, 8, 8, 15]
                            data:[60,50,55,67,100,67,100],
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
                            //[12, 22, 16, 18, 18, 15]
                            data: [40,50,45,33,0,33,0],
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
			multiple(){
				let myChart = echarts.init(document.getElementById('multiple'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0, //防止页面抖动
					},
					color:['#57CB91'],
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
						 data: ['00:00', '02:00', '04:00', '06:00', '08:00', '10:00',
						 '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
						type: 'category',
						axisLine: {
							show: false,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000000',
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
						type: 'value',
						name: '(次)',
						nameTextStyle: {
							padding: [0, 0, 0, -10]
						},
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
                        name: '报警数量',
                        type: 'line',
                        symbolSize: 8,
                        smooth: true,
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    //color: '#94C9EC'
                                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                                        offset: 0,
                                        color: 'rgba(87, 203, 145, 0.1)'
                                    }, {
                                        offset: 1,
                                        color: 'rgba(87, 203, 145, 0.5)'
                                    }]),
                                }
                            }
                        },
                        data: [0, 0, 0, 1, 2, 6, 8, 7, 8, 6, 5, 4]
                    }
                    ]
				};
				myChart.setOption(option)
			},
			getcontrast(){
				let myChart = echarts.init(document.getElementById('contrast'));
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
						data: ['凤凰星辰', '丰润花园','富力城二期','地鑫新河花园',  '德百玫瑰公馆A区',  '宏业大厦',
						 '幸福小区', '鹤翔园', '康复大学', '阳光艺境'],
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
						type: 'value',
						name: '(次)',
						nameTextStyle: {
							padding: [0, 0, 0, -20]
						},
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
						name: '预警数',
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
						data: [33, 15, 8, 6, 5, 10, 30,35,28,32]
					}, {
						name: '报警数',
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
						data: [23, 15, 14, 25, 26, 14, 18,12,13,15]
					},]
				};
				myChart.setOption(option)
			},
			getequipment() {
				let myChart = echarts.init(document.getElementById('equipment'));
				let that = this;
				myChart.on('click', function (event) {
				    that.equassetsGRP()
				})
				let option = {
					tooltip: {
						trigger: 'item',
					},
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
					color: ['#5B96FA', '#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353', '#77B8F8'],
					series: [{
						name: '设备类型',
						type: 'pie',
						radius: ['38%', '60%'],
						center: ['40%', '50%'],
						avoidLabelOverlap: false,
						itemStyle: {
							normal: {
								borderWidth: 5,
								borderColor: "#D7EBFF"
							}
						},
						label: {
							formatter: '{d}%'
						},
						labelLine: {
							show: true,
						},
						data: [{
								value: 10,
								name: '塔机'
							},
							{
								value: 6,
								name: '升降机'
							},
							{
								value: 12,
								name: '高支模'
							},
							{
								value: 14,
								name: '深基坑'
							},
							{
								value: 6,
								name: '卸料平台'
							}
						]
					}]
				};
				myChart.setOption(option)
			},

			// 跳转实时监测报警
			goenvMonitorPointAlert(){
			  this.goToUrl('monitorPointAlert')
			},
			// 跳转设备资产台账
			equassetsGRP(){
			  this.goToUrl('equassetsGRP')
			},
			goStateList (types, online) {
				let monitorTypeList = types.split(',')
				if (online != '') {
					this.goToUrl('equOnlineStatus', { monitorTypeList: monitorTypeList, online:online})
				} else {
					this.goToUrl('equOnlineStatus', { monitorTypeList: monitorTypeList })
				}
			},

			// 跳转页面
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
						comname:item.path,
						name: item.menuId
					}
					if (JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
						this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
					}
					this.$store.state.app.activeclass = activeclass.comname
					this.$router.push({ name: activeclass.name, params: queryParams })
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
		},
	}
</script>

<style scoped lang="scss">
	#cloudCenter {
		height: 100%;
		display: flex;
		justify-content: space-evenly;
	}

	.content_left_box {
		height: 95.05%;
		margin-top: 0.2rem;
	}
	.content_center_box{
		height: 95.05%;
		margin-top: 0.2rem;
	}
	.content_right_box{
		height: 95.05%;
		margin-top: 0.2rem;
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

	.equipment_box {
		width: 4.1rem;
		height: 32%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		overflow: hidden;
	}

	#equipment {
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	.accessType_box{
		height: 66.1%;
		width: 4.1rem;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		overflow: hidden;
		margin-top: 0.15rem;
	}
	.access_box{
	  height: calc(100% - 0.28rem);
		width: 100%;
	}
	.access_list{
		margin: 0 auto;
		width: 3.74rem;
		height: 11.16%;
		border: 1px solid #E6EEFF;
		margin-top: 0.25rem;
		display: flex;
		align-items: center;
		font-size: 0.16rem;
	}
	.access_list img{
		height: 85%;
		width: auto;
	}
	.access_tit{
		width: 0.68rem;
		margin-right: 0.15rem;
	}
	.access_line{
		width: 1px;
		background: #5B97F9;
		height: 39%;
	}
	.access_yx{
		width: 0.36rem;
		margin-left: 0.2rem;
		margin-right: 0.18rem;
	}
	.access_yx_num{
		width: 0.2rem;
		cursor: pointer;
	}
	.access_zs_num{
		cursor: pointer;
	}
	.access_zs{
			width: 0.36rem;
		margin-left: 0.3rem;
		margin-right: 0.1rem;
	}
	.police_list_box{
		width: 7.87rem;
		height:32.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
		overflow: hidden;
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
		height: calc(100% - 0.28rem);
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
	.police_list_con:nth-of-type(even){
		background: #F2F8FF;
	}
	.contrast_box{
		width: 7.87rem;
		height:32.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
		overflow: hidden;
		margin-top: 0.15rem;
	}
	#contrast{
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	#multiple{
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	.trend_box{
		width: 4.06rem;
		height:32.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
		overflow: hidden;
	}
	#trend{
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	.proportion_box{
		width: 4.06rem;
		height:32.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
		border-radius: 5px;
		position: relative;
		overflow: hidden;
		margin-top: 0.15rem;
	}
	#proportion{
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	.num_fenxi{
		height: calc(100% - 0.28rem);
		width: 100%;
	}
	.fenxi_tit{
		display: flex;
		justify-content: space-around;
		margin-top: 0.1rem;
	}
	.fenxi_list{
		width: 1.48rem;
		display: flex;
		align-items: center;
		font-size: 0.14rem;
		color: #000000;
		text-align: center;
	}
	.policeicon{
		width: 0.23rem;
		height: 0.23rem;
	}
	.fx_tit{
		width: 0.68rem;
	}
	.fx_num{
		font-size: 0.2rem;
		color: #146DFF;
		width: 0.4rem;
	}
	.fx_con{
		margin: 0 auto;
		height: 71.5%;
		width: 3.46rem;
		background:url(../../../../../static/img/bjbg.png) no-repeat;
		background-size:100% 100% ;
		margin-top: 0.18rem;
		display: flex;
		flex-wrap: wrap;
	}
	.fx_hezi{
		width: 50%;
		height: 50%;
		display: flex;
		flex-direction: column;
		justify-content: space-evenly;
	}
	.hz_tit{
		color: #000000;
		font-size: 0.14rem;
		text-align: center;
	}
	.hz_con{
		display: flex;
		    align-items: baseline;
    justify-content: center;
	}
	.hz_num{
		color: #146DFF;
		font-size: 0.20rem;
	}
	.hz_until{
		color: #000000;
		font-size: 0.14rem;
	}
</style>
