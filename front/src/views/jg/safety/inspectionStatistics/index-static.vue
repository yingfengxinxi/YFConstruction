<template>
	<div class="content_box">
		<div class="content_left">
			<div class="statistics_box">
				<div class="statistics_num_box">
					<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">项目隐患数量统计</div>
					<div class="hezi">
						<div class="yh_tj">
							<div class="wcl_yh">
								<div  class="wcl_tit">未处理隐患</div>
								<div class="wcl_num">8</div>
							</div>
							<div id="untrea"></div>
						</div>
						<div id="yhLX"></div>
					</div>
				</div>
				<div class="cl_time">
					<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">告警处理效率</div>
					<div class="cl_time_box">
						<div class="cl_box">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/cl2.png" />
							<div class="cl_con">
								<div class="cl_top_box">
									<div class="cl_num">6.5</div>
									<div class="cl_utils">h</div>
								</div>
								<div class="cl_tit">平均告警解除时间</div>
							</div>
						</div>
						<div class="cl_box">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/cl1.png" />
							<div class="cl_con">
								<div class="cl_top_box">
									<div class="cl_num">92</div>
									<div class="cl_utils">%</div>
								</div>
								<div class="cl_tit">日均告警解除率</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="danger_than_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">安全隐患数量占比分析</div>
				<div id="danger">

				</div>
			</div>
		</div>
		<div class="content_center">
			<div class="trend_analysis">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">安全隐患趋势分析</div>
				<div class="active_box">
					<div :class="active==1?'hid_active':''" @click="hidActive(1)">按周</div>
					<div :class="active==2?'hid_active':''" @click="hidActive(2)">按月</div>
					<div :class="active==3?'hid_active':''" @click="hidActive(3)">按季度</div>
					<div :class="active==4?'hid_active':''" @click="hidActive(4)">按年</div>
				</div>
				<div id='hidden'>

				</div>
			</div>
			<div class="hidden_type">
				<div class="hidden_box">
					<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">安全隐患级别占比</div>
					<div id="level"></div>
				</div>
				<div class="ht_line"></div>
				<div class="hidden_box">
					<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">安全隐患类型占比</div>
					<div id="typeEchart"></div>
				</div>
			</div>
		</div>
		<div class="content_right">
			<div class="ranking_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">安全隐患排名</div>
				<div class="rank_box">
					<div class="rank_tit_box">
						<div class="rank_tit" style="width: 40%;">项目名称</div>
						<div class="rank_tit" style="width: 20%;text-align: center">未处理</div>
						<div class="rank_tit" style="width: 20%;text-align: center">今日隐患</div>
						<div class="rank_tit" style="width: 20%;text-align: center">总隐患</div>
					</div>
					<div class="rank_con_box">
						<div class="rank_con_con" v-for="item in data">
							<div class="rank_con rank_img" style="width: 40%;">
								<div class="kuang"></div> {{item.name}}
							</div>
							<div class="rank_con" style="width: 20%;justify-content: center;">{{item.a}}</div>
							<div class="rank_con" style="width: 20%;justify-content: center;">{{item.b}}</div>
							<div class="rank_con" style="width: 20%;justify-content: center;">{{item.c}}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ranking_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">整改时效分析</div>
				<div class="rank_box">
					<div class="rank_tit_box rank_tit_box2">
						<div class="rank_tit rank_tit2" style="width: 40%;">项目名称</div>
						<div class="rank_tit rank_tit2" style="width: 30%;text-align: center">整改完成率</div>
						<div class="rank_tit rank_tit2" style="width: 30%;text-align: center">平均用时(天)</div>
					</div>
					<div class="rank_con_box">
						<div class="rank_con_con prescription_con_con" v-for="item in data">
							<div class="rank_con rank_img" style="width: 40%;">
								<div class="kuang kuang2"></div> {{item.name}}
							</div>
							<div class="rank_con" style="width: 30%;justify-content: center;">{{item.d}}</div>
							<div class="rank_con" style="width: 30%;justify-content: center;">{{item.e}}</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	export default {
		data() {
			return {
				active: 1,
				data: [{
					name: '幸福小区项目',
					a: '2',
					b: '5',
					c: '22',
					d: '90.0%',
					e: '1.2'
				}, {
					name: '丰润花园项目',
					a: '0',
					b: '3',
					c: '18',
					d: '100%',
					e: '0.8'
				}, {
					name: '宏业大厦项目',
					a: '0',
					b: '0',
					c: '10',
					d: '100%',
					e: '1.1'
				}, {
					name: '地鑫新河花园项目',
					a: '0',
					b: '0',
					c: '5',
					d: '100%',
					e: '1.8'
				}, {
					name: '德百玫瑰公馆A项目',
					a: '0',
					b: '0',
					c: '2',
					d: '100%',
					e: '2.2'
				}, {
					name: '富力城二期',
					a: '1',
					b: '1',
					c: '8',
					d: '87.5%',
					e: '1.5'
				}, {
					name: '鹤翔园项目',
					a: '2',
					b: '2',
					c: '12',
					d: '83.3%',
					e: '2.2'
				}, {
					name: '阳光艺境项目',
					a: '1',
					b: '0',
					c: '24',
					d: '95.8%',
					e: '3.4'
				}, {
					name: '凤凰星辰项目',
					a: '2',
					b: '0',
					c: '20',
					d: '90%',
					e: '4.6'
					// }, {
					// 	name: '锦绣景园',
					// 	a: '11',
					// 	b: '22',
					// 	c: '65',
					// 	d: '55%',
					// 	e: '156'
				}
				]
			}
		},
		mounted() {
			this.getDanger()
			this.getHidden()
			this.getlevel()
			this.getType()
			this.getuntrea()
			this.getyhLX()
			this.remsize()
		},
		methods: {
			getyhLX() {
				let myChart = echarts.init(document.getElementById('yhLX'));
				let data = [{
						name: '一般隐患',
						value: 12,
					},
					{
						name: '重大隐患',
						value: 8,
					},
				];
				let getArrByKey = (data, k) => {
					let key = k || "value";
					let res = [];
					if(data) {
						data.forEach(function(t) {
							res.push(t[key]);
						});
					}
					return res;
				};
				let getSymbolData = (data) => {
					let arr = [];
					for(var i = 0; i < data.length; i++) {
						arr.push({
							value: data[i].value,
							symbolPosition: 'end'
						})
					}
					return arr;
				}
				// console.log(getSymbolData(data));
				let option = {
					grid: {
						top: '20%',
						bottom: '2%',
						right: '30%',
						left: '30%',
					},
					xAxis: {
						show: false
					},
					yAxis: [{
						triggerEvent: true,
						show: true,
						inverse: true,
						data: getArrByKey(data, 'name'),
						axisLine: {
							show: false
						},
						splitLine: {
							show: false
						},
						axisTick: {
							show: false
						},

						axisLabel: {
							show: true,
							interval: 0,
							color: '#666666',
							align: 'left',
							margin: 50,
							formatter: function(value, index) {
								return '{title|' + value + '}'
							},
							rich: {
								title: {

									align: 'right',
									fontSize: 12
								}
							}
						},
					}, {
						triggerEvent: true,
						show: true,
						inverse: true,
						data: getArrByKey(data, 'name'),
						axisLine: {
							show: false
						},
						splitLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						axisLabel: {
							interval: 0,
							shadowOffsetX: '20px',
							color: ['#666666'],
							align: 'left',
							verticalAlign: 'center',
							lineHeight: 40,
							fontSize: 12,
							formatter: function(value, index) {
								return data[index].value
							},

						}
					}],
					series: [ {
						name: '条',
						type: 'bar',
						showBackground: true,
						// barBorderRadius: 30,
						yAxisIndex: 0,
						data: data,
						barWidth: 10,
						// align: left,
						itemStyle: {
							normal: {
								color: "rgba(41, 162, 245, 1)",
                barBorderRadius: 10,
							},

						},

					}]
				};
				myChart.setOption(option)
			},
			getuntrea() {
				let myChart = echarts.init(document.getElementById('untrea'));
				let option = {
					title: {
						text: '75%',
						x: '50%',
						top: 'middle',
						textAlign: 'center',
						textStyle: {
							fontSize: '18',
							color: '#5B97F9',
							fontWeight: '100',
							textAlign: 'center',
						},
					},
					polar: {
						radius: ['55%', '45%'],
						center: ['50%', '50%'],
					},
					angleAxis: {
						max: 100,
						show: false,
						startAngle: 0,
					},
					radiusAxis: {
						type: 'category',
						show: true,
						axisLabel: {
							show: false,
						},
						axisLine: {
							show: false,

						},
						axisTick: {
							show: false
						},
					},
					series: [{
						name: '',
						type: 'bar',
						roundCap: true,
						barWidth: 60,
						showBackground: true,
						backgroundStyle: {
							color: '#fff',
						},
						data: [75],
						coordinateSystem: 'polar',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
									offset: 0,
									color: '#0ff'
								}, {
									offset: 1,
									color: '#02aeff'
								}]),
							}
						}
					}, ]
				};
				myChart.setOption(option)
			},
			getType() {
				let myChart = echarts.init(document.getElementById('typeEchart'));
				let option = {
					tooltip: {
						trigger: 'item'
					},
					color: ['#48D558', '#EFBE42', '#f6ca59', '#e250b1', '#3b5cf9', '#1bdff1', '#6cf371', '#f5835f', '#6c2be7', '#f3456d'],
					legend: {
						icon: "circle",
						left: 'center',
						bottom: '60',
						itemGap: 20,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							fontSize: 12
						},
					},
					series: [{
						type: "pie",
						radius: ["30%", "45%"],
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
							show: true,
							formatter: '{d}%',
						},
						data: [{
							name: '基础管理类隐患',
							value: '82'
						}, {
							name: '生产现场类隐患',
							value: '18'
						}]
					}]
				}
				myChart.setOption(option)
			},
			getlevel() {
				let myChart = echarts.init(document.getElementById('level'));
				let option = {
					tooltip: {
						trigger: 'item'
					},
					color: ['#60CFFF', '#5C87FC', '#f6ca59', '#e250b1', '#3b5cf9', '#1bdff1', '#6cf371', '#f5835f', '#6c2be7', '#f3456d'],
					legend: {
						icon: "circle",
						left: 'center',
						bottom: '60',
						itemGap: 20,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							fontSize: 12
						},
					},
					series: [{
						type: "pie",
						radius: ["30%", "45%"],
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
							show: true,
							formatter: '{d}%',
						},
						data: [{
							name: '一般事故隐患',
							value: '65'
						}, {
							name: '重大事故隐患',
							value: '35'
						}]
					}]
				}
				myChart.setOption(option)
			},
			hidActive(num) {
				this.active = num
			},
			getHidden() {

				let myChart = echarts.init(document.getElementById('hidden'));
				let option = {
					tooltip: {
						trigger: "axis",
					},
					grid: {
						right: "10%",
						left: "10%",
						top: '30%',
						bottom: '20%'
					},
					color: ['#77A7F8', '#77A7F8'],
					legend: {
						icon: "circle",
						top: 60,
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						//['26日', '27日', '28日', '29日', '30日', '31日', ]
						data: ['11.15', '11.16', '11.17', '11.18', '11.19', '11.20', '11.21', '11.22', '11.23', '11.24', '11.25'],
						type: 'category',
						axisLine: {
							show: false,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000',
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
						name: '安全隐患',
						type: 'bar',
						barWidth: '15%',
						itemStyle: {
							normal: {
								color: '#77A7F8',
								//barBorderRadius: 11,
							}
						},
						//[12, 18, 20, 16, 12, 15, 32]
						data: [12, 18, 20, 16, 12, 15, 32, 24, 8, 24, 11]
					}, {
						name: '安全隐患',
						type: 'line',
						stack: '工资数',
            itemStyle: {
              color: "#ffffff"
            },
            //symbol: 'none',
						lineStyle: {
							normal: {
								color: "#77A7F8",
							},
						},
						itemStyle: {
							normal: {
								color: '#77A7F8',
                label : {
                  show: false
                },
                borderColor:'#77A7F8',  // 拐点边框颜色
                lineStyle:{
                  width:2,  // 设置线宽
                  type:'solid'  //'dotted'虚线 'solid'实线
                }
							}
						},
						data: [12, 18, 20, 16, 12, 15, 32, 24, 8, 24, 11]
					}, ]
				};
				myChart.setOption(option)
			},
			getDanger() {
				let myChart = echarts.init(document.getElementById('danger'));
				let option = {
					tooltip: {
						trigger: "axis",
					},

					grid: {
						right: "10%",
						left: "15%",
						top: '20%',
						bottom: '20%'
					},
					legend: {
						icon: "circle",
						top: 5,
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						//['26日', '27日', '28日', '29日', '30日', '31日', ]
						data: ['25日', '26日', '27日', '28日', '29日', '30日', '31日'],
						type: 'category',
						axisLine: {
							show: true,
							color: '#A3E9FC'
						},

						axisLabel: {
							color: '#000',
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
							name: '待整改',
							type: 'bar',
							stack: 'A',
							barWidth: '15%',
							barGap: '-100%',
							z: 10,
							itemStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
										offset: 0,
										color: '#A89FFF',
									}, {
										offset: 1,
										color: '#A89FFF',
									}]),
									barBorderRadius: 12,
								},
							},
						data: [0, 1, 0, 2, 4, 8, 3]
						},
						{
							name: '待复检',
							type: 'bar',
							stack: 'A',
							barWidth: '15%',
							z: 5,
							barGap: '-100%',
							itemStyle: {
								normal: {
									color: '#649BF8',
									barBorderRadius: 11,
									// shadowBlur: [0, 0, 0, 10],
									// shadowColor: '#649BF8',
									// shadowOffsetY: 20,
								}

							},
							data: [0, 2, 5, 0, 3, 0, 1]
						},
						{
							name: '已完成',
							type: 'bar',
							stack: 'A',
							barWidth: '15%',
							barGap: '-100%',
							z: 1,
							itemStyle: {
								normal: {
									color: '#30BF78',
									barBorderRadius: 11,
									// shadowBlur: [0, 0, 0, 10],
									// shadowColor: '#30BF78',
									// shadowOffsetY: 20,
								}
							},
							data: [6, 9, 5, 8, 4, 3, 1]
						}
					]
				};
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
		}
	}
</script>

<style scoped lang="scss">
	.content_box {
		overflow: hidden;
		height: 100%;
		display: flex;
		justify-content: space-evenly;
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

	.content_left {
		height: 95%;
		width: 4.1rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		margin-top: 0.2rem;
	}

	.content_center {
		height: 95%;
		width: 7.87rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		margin-top: 0.2rem;
	}

	.content_right {
		height: 95%;
		width: 4.06rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		margin-top: 0.2rem;
	}

	.statistics_box {
		height: 49.07%;
		width: 100%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
	}

	.danger_than_box {
		height: 49.07%;
		width: 100%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
	}

	#danger {
		height: calc(100% - 0.28rem);
		width: 100%;
	}

	.trend_analysis {
		height: 49.07%;
		width: 100%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		position: relative;
	}

	.active_box {
		width: 3rem;
		display: flex;
		justify-content: space-between;
		position: absolute;
		right: 0.33rem;
		top: 0.49rem;
		z-index: 10;
	}

	.active_box div {
		color: #649BF8;
		font-size: 0.12rem;
		border: 1px solid #C8E0F8;
		border-radius: 2px;
		width: 0.66rem;
		height: 0.24rem;
		text-align: center;
		line-height: 0.24rem;
		cursor: pointer;
		background: #E6F5FF;
	}

	.active_box .hid_active {
		color: #FFFFFF;
		background: #649BF8;
	}

	.hidden_type {
		height: 49.07%;
		width: 100%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
		display: flex;
	}

	#hidden {
		height: calc(100% - 0.28rem);
		width: 100%;
	}

	.hidden_box {
		height: 100%;
		width: 3.92rem;
	}

	#level {
		height: calc(100% - 0.28rem);
		width: 100%;
	}

	.ht_line {
		width: 1px;
		height: 55.94%;
		background: #CADEFF;
		margin-top: 1rem;
	}

	#typeEchart {
		height: calc(100% - 0.28rem);
		width: 100%;
	}

	.ranking_box {
		height: 49.07%;
		width: 100%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 5px;
	}

	.rank_box {
		height: calc(100% - 0.28rem);
		width: 3.71rem;
		margin: 0 auto;
	}

	.rank_tit_box {
		width: 100%;
		display: flex;
		height: 7.1%;
		border-bottom: 1px solid #286DFF;
		margin-top: 0.11rem;
		line-height: 100%;
	}

	.rank_tit_box2 {
		border-bottom: 1px solid #3FD189;
	}

	.rank_tit {
		color: #286DFF;
		font-size: 0.12rem;
		text-indent: 0.1rem;
	}

	.rank_tit2 {
		color: #3FD189;
	}

	.rank_con_box {
		height: 85%;
		width: 100%;
		overflow: auto;
	}

	.rank_con_box::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}

	.rank_con_box {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}

	.rank_con_con {
		display: flex;
		width: 100%;
		height: 8.57%;
		margin-bottom: 0.08rem;
		background: linear-gradient(90deg, #F9FCFF 0%, #E6EFFF 100%);
	}

	.prescription_con_con {
		background: linear-gradient(90deg, #FBFFFE 0%, rgba(222, 255, 236, 0.65) 100%)
	}

	.rank_con {
		font-size: 0.12rem;
		color: #404040;
		text-indent: 0.1rem;
		display: flex;
		align-items: center;
	}

	.kuang {
		width: 0.13rem;
		height: 0.13rem;
		background: #73CDF6;
		border-radius: 2px;
		margin-right: 0.05rem;
	}

	.kuang2 {
		background: #30BF78;
	}

	.statistics_num_box {
		height: 57.6%;
		width: 100%;
	}

	.hezi {
		height: calc(100% - 0.28rem);
		display: flex;
	}

	.yh_tj {
		height: 100%;
		width: 1.84rem;
		position: relative;
	}

	#untrea {
		width: 100%;
		height: 100%;
	}

	#yhLX {
		height: 100%;
		width: 2.26rem;
	}

	.cl_time {
		height: 41.4%;
		width: 100%;
	}

	.cl_time_box {
		height: calc(100% - 0.28rem);
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
		.cl_box{
		width: 1.8rem;
		height: 0.73rem;
		display: flex;
		align-content: center;

	}
	.cl_con{
		display: flex;
		height: 0.73rem;
		flex-direction: column;
    	justify-content: space-evenly;
    	color: #404040;
    	margin-left: 0.10rem;
    	font-size: 0.12rem;
	}
	.cl_top_box{
		display: flex;
		align-items: baseline;
	}
	.cl_num{
		color: #00CCF8;
		font-size: 0.24rem;
		margin-right: 5px;
	}
	.wcl_yh{
		position: absolute;
		bottom: 0;
		display: flex;
		align-items: center;
		    width: 100%;
    	justify-content: center;
	}
	.wcl_tit{
		color: #404040;
		font-size: 0.12rem;
		margin-right: 0.1rem;
	}
	.wcl_num{
		color: #146DFF;
		font-size: 0.2rem;
	}
</style>
