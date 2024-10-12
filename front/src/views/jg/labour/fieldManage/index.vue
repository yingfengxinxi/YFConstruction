<template>
	<div id="cloudCenter">
		<div class="left_box">
			<div class="left-top">
				<div class="left-top-title">
					{{ datetime }}
				</div>

				<div class="left-top-center">
					<div class="workers_box" @click="gotoRoster()">
						<span class="left-title left-title-wrapper">现场实时人数</span>
						<div class="flex-center workers_num" v-for="(item,index) in xcssrsList">
							<span class="num-back">{{item}}</span>
						</div>
						<span class="workers_untils">人</span>
					</div>

					<div class="workers_box" @click="gotoRoster()">
						<span class="left-title left-title-wrapper">劳务总人数</span>
						<div class="flex-center workers_num" v-for="(item,index) in lwzrsList">
							<span class="num-back">{{item}}</span>
						</div>
						<span class="workers_untils">人</span>
					</div>
				</div>

				<div class="left-top-bottom">
					<div class="left-top-bottom-item" @click="gotoAttendanceDetail()()">
						<div class="left-item-title"><span>管理人员</span> <p class="left-item-value">{{glryrs}}</p></div>
					</div>
					<div class="unback" @click="gotoAttendanceDetail()">
						<div class="people_gl">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/cql.png" style="width: .2rem;height: .2rem;">
							<span>出勤率</span>
							<p class="left-item-value">{{glrycql}}%</p>
						</div>
					</div>
					<div class="left-top-bottom-item" @click="gotoAttendanceDetail()">
						<div class="left-item-title"><span>普通工人</span> <p class="left-item-value">{{jzgrrs}}</p></div>
					</div>
					<div class="unback" @click="gotoAttendanceDetail()">
						<div class="people_gl">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/cql.png" style="width: .2rem;height: .2rem;">
							<span>出勤率</span>
							<p class="left-item-value">{{jzgrcql}}%</p>
						</div>
					</div>
				</div>
			</div>

			<div class="left-bottom">
				<div class="left-bottom-title">
					<div class="flex-center">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-zone.png" style="margin-left:0.1rem" alt="" class="avatar-16">
						<span class="text-22" style="margin-left: .08rem">实时动态</span>
					</div>

					<div class="lenged_jc" style="width: 40%">
						<div class="flex-center">
							<div class="spot bg-ff6100"></div>
							<span class="text-16">进场</span>
						</div>
						<div class="flex-center">
							<div class="spot bg-3477FF"></div>
							<span class="text-16">离场</span>
						</div>

					</div>
				</div>

				<div class="dynamic-list">
          <div class="dynamic-item" v-for="(item, index) in dynamicList" :key="`dynamic` + index">
            <div class="dynamic-info">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="item.bloodPressure" alt="" class="dynamic-avatar">
              <div class="dynamic-user-info">
                <span class="text-12 text-404040">姓名 {{ item.alcoholicity }}</span>
                <span class="text-12 text-404040">{{ item.bloodOxygen }}</span>
                <span class="text-12 text-070707 text-600">{{ item.dataGeneration }}</span>
              </div>
            </div>
            <div class="flex-center dynamic_time">
              <div :class="item.clockDirection==0?'spot bg-ff6100':'spot bg-3477FF'" style="margin-right: .1rem"></div>
              <span class="text-14 text-404040 text-400">{{ item.clockTime }}</span>
            </div>
          </div>
				</div>
			</div>
		</div>
		<div class="center_box">
			<div class="center-top">
				<div class="flex-center" style="margin-bottom: .08rem;margin-left: 0.1rem;margin-top: 0.12rem;">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-zone.png" alt="" class="avatar-16">
					<span class="text-22" style="margin-left: .08rem">建筑工人构成分析</span>
				</div>
				<div class="center-top-bottom">
					<div class="center-top-data">
						<div class="center-top-left" @click="gotoRoster()">
							<div class="flex-center">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-spot.png" alt="" class="icon-spot">
								<span class="text-16 text-400 text-070707 flex-center his_peo_box">历史施工人数 <span class="his_peo_num">{{lssgrs}}</span>人</span>
							</div>
						</div>
						<div class="center-top-left">
							<div class="sex_zb">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-spot.png" alt="" class="icon-spot">
								<div class="flex-center flex-column alitem-start sex_box">
									<div class="sex_than">
										<span>男性占比:</span>
										<span>{{nxzb}}%</span>
									</div>
									<div class="sex_than">
										<span class="data-info">同比 {{nantb}}%</span>
										<span class="data-info">环比 {{nanhb}}%</span>
									</div>
								</div>
							</div>
						</div>
						<div class="center-top-left">
							<div class="sex_zb">
								<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-spot.png" alt="" class="icon-spot">
								<div class="flex-center flex-column alitem-start sex_box">
									<div class="sex_than">
										<span>女性占比:</span>
										<span>{{nvxzb}}%</span>
									</div>
									<div class="sex_than">
										<span class="data-info">同比 {{nvtb}}%</span>
										<span class="data-info">环比 {{nvhb}}%</span>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="center-top-echarts" id="center-top-echarts"></div>
				</div>

			</div>

			<div class="center-bottom">
				<div class="flex-center" style="margin-bottom: .08rem;margin-left: 0.1rem;margin-top: 0.12rem;">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-zone.png" alt="" class="avatar-16">
					<span class="text-22" style="margin-left: .08rem">建筑工人工种与持证分析</span>
				</div>

				<div class="center-bottom-data">
					<div class="center-bottom-item" v-for="(item, index) in centerBottomList" :key="index">
						<div class="center-item1" :class="`bg-img` + (index + 1)">
							<!--              :style="`background-image: url(../../../../assets/people/icon-center)` + ( index + 1) + `.png`"-->
							<span class="text-16" :style="`color: `+ item.color + `;`">{{ item.name }}</span>
						</div>

						<div class="center-item2" :id="`item` + index"></div>

						<div class="center-item3">
							<div>
								<span class="text-24 text-5B97F9">{{item.value}}</span>
								<span class="text-16 text-070707">人</span>
							</div>
							<p class="text-14 text-5B97F9">同比： {{item.value1}}%</p>
							<p class="text-14 text-5B97F9">环比： {{item.value2}}%</p>
						</div>
					</div>

				</div>
			</div>
		</div>
		<div class="right_box">
			<div class="right-top">
				<div class="flex-center" style="margin-bottom: .08rem;margin-left: 0.1rem;margin-top: 0.12rem;">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-zone.png" alt="" class="avatar-16">
					<span class="text-22" style="margin-left: .08rem">工资发放情况分析</span>
				</div>
				<div id="right-top-echarts"></div>
			</div>

			<div class="right-bottom">
				<div class="flex-center" style="margin-bottom: .08rem;margin-left: 0.1rem;margin-top: 0.12rem;">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/people/icon-zone.png" alt="" class="avatar-16">
					<span class="text-22" style="margin-left: .08rem">拖欠工资企业排名</span>
				</div>

				<div id="right-bottom-echarts"></div>
			</div>
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import { queryMenuListByPath } from '@/api/admin/menu'
  import request from '@/utils/request'

	export default {
		data() {
			return {
				datetime: null,
				dynamicList: [],
        lwzrs: '',
        lwzrsList: [],
        xcssrs: '',
        xcssrsList: [],
        glryrs: '',
        glrycql: '',
        jzgrrs: '',
        jzgrcql: '',
        lssgrs: '',
        nxzb: '',
        nvxzb: '',
        nantb: '',
        nanhb: '',
        nvtb: '',
        nvhb: '',
				centerBottomList: [],

			}
		},
		components: {},
		created() {

    },
		mounted() {
			//this.jzgrfx()
			//this.jzgrgz()
			//this.tqgzqy();
			this.showtime();
			this.remsize()
      this.getList();
      this.peopleNumber();
      this.workersConstitute();
      this.workerTypeConstitute();
      this.wageConstitute();
      this.defaultWageConstitute();
		},
		methods: {
			showtime() {
				var date = new Date(); //创建一个日期对象的实例,new代表创建，Date是一个时间对象，连起来就是创建对象，并用变量date接收，实例就是一个实实在在的东西
				// console.log(date); //在控制台输出date的值
				var year = date.getFullYear();
				var month = date.getMonth() + 1; //这里系统定义的月份是从0到11定义的，而加一的目的就是让它变成从1到12，这样就可以实现正常的月份了
				var day = date.getDate();
				var hour = date.getHours();
				if(hour < 10) {
					hour = '0' + hour;
				} //这里用if语句的原因是当我们的时间走到一位数字时就会出现闪动，会使整个代码混乱，为了解决这一缺点在个位数前面用拼接的方法加一个0，这样就形成了两位数，这样就不会出现闪动。分钟和秒也是一样
				var minute = date.getMinutes();
				if(minute < 10) {
					minute = '0' + minute;
				}
				var second = date.getSeconds();
				if(second < 10) {
					second = '0' + second;
				}
				var time = year + '年' + month + '月' + day + '日 ' + hour + ':' + minute + ':' + second;
				this.datetime = time
				// var time1 = document.getElementById('zcsjb');
				//time1.innerHTML = time; //innerHTML是整个标签的前后里面的内容，把time里面的内容赋值到了time1中
				this.$forceUpdate()
				setTimeout(this.showtime, 1000);
			},

			// 建筑工人构成分析
			jzgrfx(ageList,schoolingList,text) {
				let option = {
					color: ['#5075E6', '#F3BC40', '#27D35A', '#54D1FF', '#AFFFFF'],
					title: {
						bottom: '5%',
						left: 'center',
						text: text,
						textStyle: {
							fontSize: 14,
						}
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a} <br/>{b}: {c} ({d}%)'
					},
					legend: {
						data: [
							'20-30岁',
							'30-40岁',
							'40-50岁',
							'50-60岁',
							'60以上'
						],
						right: 'right',
						top: 'center',
						icon: "circle",
						textStyle: {}
					},
					series: [{
							name: '学历分析',
							type: 'pie',
							selectedMode: 'single',
							radius: [0, '35%'],
							center: ['40%', '40%'],
							label: {
								position: 'inner',
								fontSize: 12,
								color: '#fff'
							},
							labelLine: {
								show: false
							},
							data: schoolingList
						},
						{
							name: '年龄分析',
							type: 'pie',
							radius: ['50%', '65%'],
							center: ['40%', '40%'],
							labelLine: {
								length: 10
							},
							label: {
								fontSize: 12,
								color: "#000000",
								formatter: '{d}%'
							},
							data: ageList
						}
					]
				};
				let myChart = echarts.init(document.getElementById('center-top-echarts'));
				myChart.setOption(option)
			},
			// 建筑工人工种与持证分析
			jzgrgzlb(id) {
				let dataArr1 = [];
				for(var i = 0; i < 100; i++) {
					if(i % 10 === 0) {
						dataArr1.push({
							name: (i + 1).toString(),
							value: 30,
							itemStyle: {
								normal: {
									color: "rgba(0,255,255,1)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)",
								}
							}
						})
					} else {
						dataArr1.push({
							name: (i + 1).toString(),
							value: 100,
							itemStyle: {
								normal: {
									color: "rgba(0,0,0,0)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					}
				}
				let dataArr2 = [];
				for(var i = 0; i < 100; i++) {
					if(i % 5 === 0) {
						dataArr2.push({
							name: (i + 1).toString(),
							value: 20,
							itemStyle: {
								normal: {
									color: "rgba(0,255,255,1)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					} else {
						dataArr2.push({
							name: (i + 1).toString(),
							value: 100,
							itemStyle: {
								normal: {
									color: "rgba(0,0,0,0)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					}
				}
				let dataArr3 = [];
				for(var i = 0; i < 100; i++) {
					if(i % 5 === 0) {
						dataArr3.push({
							name: (i + 1).toString(),
							value: 20,
							itemStyle: {
								normal: {
									color: "rgba(0,255,255,.3)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					} else {
						dataArr3.push({
							name: (i + 1).toString(),
							value: 100,
							itemStyle: {
								normal: {
									color: "rgba(0,0,0,0)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					}
				}
				let dataArr4 = [];
				for(var i = 0; i < 100; i++) {
					if(i % 10 === 0) {
						dataArr4.push({
							name: (i + 1).toString(),
							value: 30,
							itemStyle: {
								normal: {
									color: "rgba(0,255,255,.5)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					} else {
						dataArr4.push({
							name: (i + 1).toString(),
							value: 100,
							itemStyle: {
								normal: {
									color: "rgba(0,0,0,0)",
									borderWidth: 0,
									borderColor: "rgba(0,0,0,0)"
								}
							}
						})
					}
				}
				let option = {
					title: [{
						text: this.centerBottomList[id].bfb + '%',
						x: '48%',
						y: '42%',
						textAlign: 'center',
						textStyle: {
							fontSize: '16',
							fontWeight: '100',
							color: '#5B97F9',
							textAlign: 'center',
						},
					}, ],
					polar: {
						radius: ['71%', '62%'],
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
								color: '#177CFC',
							},
							data: [this.centerBottomList[id].bfb],
							coordinateSystem: 'polar',
							itemStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
										offset: 0,
										color: '#fff'
									}, {
										offset: 1,
										color: '#fff'
									}]),
								}
							}
						},
						{
							type: 'pie',
							radius: ['53%', '55%'],
							center: ['50%', '50%'],
							data: [{
								hoverOffset: 1,
								value: 100,
								name: '',
								itemStyle: {
									color: '#D7E5FE',
								},
								label: {
									show: false
								},
								labelLine: {
									normal: {
										smooth: true,
										lineStyle: {
											width: 0
										}
									}
								},
								hoverAnimation: false,
							}, {
								label: {
									show: false
								},
								labelLine: {
									normal: {
										smooth: true,
										lineStyle: {
											width: 0
										}
									}
								},
								value: 0,
								hoverAnimation: false,
								itemStyle: {
									color: '#3c3a48',
								},
							}]
						}, {
							type: 'pie',
							zlevel: 0,
							silent: true,
							radius: ['87%', '85.5%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr1
						},
						{
							type: 'pie',
							zlevel: 0,
							silent: true,
							startAngle: -150,
							radius: ['85%', '83.5%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr4
						},
						{
							type: 'pie',
							zlevel: 0,
							silent: true,
							startAngle: -140,
							radius: ['88%', '86.5%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr1
						},
						{
							type: 'pie',
							zlevel: 0,
							silent: true,
							radius: ['81%', '80%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr2
						},
						{
							type: 'pie',
							zlevel: 0,
							silent: true,
							startAngle: -140,
							radius: ['81%', '80%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr3
						},
						{
							type: 'pie',
							zlevel: 0,
							silent: true,
							startAngle: -147.5,
							radius: ['81%', '80%'],
							z: 1,
							label: {
								normal: {
									show: false
								},
							},
							labelLine: {
								normal: {
									show: false
								}
							},
							data: dataArr3
						},

					]
				};
				var id = 'item' + id;
				let myChart = echarts.init(document.getElementById(id));
				myChart.setOption(option)
			},
			// 工资发放情况分析
			jzgrgz(mouth,yfgz,sfgz) {
				var option = {
					color: ['#649BF8', '#30BF78'],
					textStyle: {
						fontSize: 18
					},
					grid: {
						right: "8%",
						left: "15%",
						top: '15%',
						bottom: '25%'
					},
					legend: {
						icon: "circle",
						top: 5,
						data: ['应发工资', '实发工资'],
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow',
							label: {
								show: true,
								backgroundColor: '#333'
							}
						}
					},
					xAxis: [{
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
						data: mouth
					}],
					yAxis: {
						name: '(万元)',
						nameTextStyle: {
							color: "#000",
							fontSize: '12'
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
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					},
					series: [{
							name: '应发工资',
							type: 'line',
							itemStyle: {
								normal: {
									areaStyle: {
										color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
											offset: 0,
											color: 'rgba(100, 155, 248,1)'
										}, {
											offset: 1,
											color: 'rgba(100, 155, 248, 0.1)'
										}]),
										barBorderRadius: 11,
									}
								}
							},
							data: yfgz
						},
						{
							name: '实发工资',
							type: 'line',
							symbol: 'circle',
							itemStyle: {
								normal: {
									areaStyle: {
										color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
											offset: 0,
											color: 'rgba(48, 191, 120, 1)'
										}, {
											offset: 1,
											color: 'rgba(48, 191, 120, 0)'
										}]),
										barBorderRadius: 11,
									}
								}
							},
							data: sfgz
						}
					]
				};
				let myChart = echarts.init(document.getElementById('right-top-echarts'));
				myChart.setOption(option)
			},

			// 拖欠工资
			tqgzqy(data) {
        let resultData = data
				let yLabel = []
				let yData = []
        resultData.forEach(item => {
          yLabel.push(item.infocollectionName)
          yData.push(item.attendanceNumber)
        })
				let bgData = []
				for(let i in yData) {
					bgData.push(100)
				}
				var option = {
					grid: {
						left: '5%',
						right: '5%',
						bottom: '5%',
						top: '3%',
						containLabel: true
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'none'
						},
						formatter: function(params) {
							return params[0].name + '<br/>' +
								"<span style='display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:rgba(36,207,233,0.9)'></span>" +
								params[0].seriesName + ' : ' + params[0].value + '万元 <br/>'
						}
					},
					xAxis: {

						position: 'top',
						type: 'value',
						axisLine: {
							show: false
						},
						axisLabel: {
							show: false,
							margin: 10,
							textStyle: {
								color: '#000000'
							}
						},
						axisTick: {
							show: false
						},
						splitLine: {
							show: false
						},
						nameTextStyle: {
							color: '#fff'
						}
					},
					yAxis: [{
						type: 'category',
						inverse: true,
						axisLabel: {
							show: true,
							margin: 15,
							textStyle: {
								color: '000',
							},
						},
						splitLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						data: yLabel
					}, {
						type: 'category',
						inverse: true,
						axisTick: 'none',
						axisLine: 'none',
						show: true,
						axisLabel: {
							textStyle: {
								color: '#000000',
								fontSize: '12'
							},
							 formatter: function(value, index) {
					                return value+'(万元)'
					            },
						},
						data: yData
					}],
					series: [{
							name: '金额',
							type: 'bar',
							zlevel: 1,
							itemStyle: {
								normal: {
									barBorderRadius: [0, 30, 30, 0],
									color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
										offset: 0,
										color: '#1C71FF'
									}, {
										offset: 1,
										color: '#3AE8EC'
									}]),
									shadowBlur: 0,
									shadowColor: 'rgba(87,220,222,0.7)'
								},
							},
							barWidth: 10,
							data: yData
						},
						{
							name: '背景',
							type: 'bar',
							barWidth: 10,
							barGap: '-100%',
							data: bgData,
							itemStyle: {
								normal: {
									color: 'rgba(255,255,255,1)',
									barBorderRadius: [0, 30, 30, 0],
								}
							},
						},
					]
				};
				let myChart = echarts.init(document.getElementById('right-bottom-echarts'));
				myChart.setOption(option)
			},

			// 跳转人员列表
			gotoRoster() {
				this.goToUrl('roster')
			},
			// 跳转考勤列表
			gotoAttendanceDetail() {
				this.goToUrl('attendanceDetail', {})
			},

			// 跳转页面
			goToUrl(path, queryParams) {
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
						comname:item.path,
						name: item.menuId
					}
					if(JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
						this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
					}
					this.$store.state.app.activeclass = activeclass.comname
					this.$router.push({
						name: activeclass.name,
						query: queryParams
					})
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
      getList(){
        request({
          url: 'build/bLabourAttendanceRecord/selectDynamic',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          //console.log("打印:"+ JSON.stringify(res.data))
          this.dynamicList =res.data
        })

      },
      peopleNumber(){
        request({
          url: 'build/bLabourAttendanceRecord/peopleNumber',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          var data = res.data
          //console.log("打印:"+ JSON.stringify(res.data))
          var lwzrs = data.lwzrs
          var lwzrsString = lwzrs.toString();
          var lwzrsSz = lwzrsString.split('')
          this.lwzrsList=lwzrsSz;
          var xcssrs = data.xcssrs
          var xcssrsString = xcssrs.toString();
          var xcssrsSz = xcssrsString.split('')
          this.xcssrsList=xcssrsSz;
          this.glryrs = data.glryrs
          this.glrycql = data.glrycql
          this.jzgrrs = data.jzgrrs
          this.jzgrcql = data.jzgrcql

          //this.dynamicList =res.data
        })
      },
      workersConstitute(){
        request({
          url: 'build/bLabourAttendanceRecord/workersConstitute',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          var data = res.data
          console.log("打印:"+ JSON.stringify(res.data))
          this.lssgrs = data.lssgrs
          this.nxzb = data.nxzb
          this.nvxzb = data.nvxzb
          this.nantb = data.nantb
          this.nanhb = data.nanhb
          this.nvtb = data.nvtb
          this.nvhb = data.nvhb
          var k ={};
          k.value= data.nl1
          k.name='20-30岁'
          var ageList =[];
          var schoolingList =[];
          var text='学历大部分为'+data.schoolingInstructions+'， 年龄主要'+data.ageInstructions;
          ageList.push(k);
          k ={};
          k.value= data.nl2
          k.name='30-40岁'
          ageList.push(k);
          k ={};
          k.value= data.nl3
          k.name='40-50岁'
          ageList.push(k);
          k ={};
          k.value= data.nl4
          k.name='50-60岁'
          ageList.push(k);
          k ={};
          k.value= data.nl5
          k.name='60以上'
          ageList.push(k);
          let resultData = res.data.schooling
          resultData.forEach(item => {
            var schooling={}
            schooling.value = item.zs
            schooling.name =item.mc
            schoolingList.push(schooling)

          })

          this.jzgrfx(ageList,schoolingList,text)
          //this.dynamicList =res.data
        })
      },
      workerTypeConstitute(){
        request({
          url: 'build/bLabourAttendanceRecord/workerTypeConstitute',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.centerBottomList = res.data.workerTypeList
          this.$nextTick(function () {
            for(var i = 0; i < this.centerBottomList.length; i++) {
              this.jzgrgzlb(i);
            }
          })

        })
      },
      wageConstitute(){
        request({
          url: 'build/bLabourAttendanceRecord/wageConstitute',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.jzgrgz(res.data.mouth,res.data.yfgz,res.data.sfgz)
        })
      },
      defaultWageConstitute(){
        request({
          url: 'build/bLabourAttendanceRecord/defaultWageConstitute',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.tqgzqy(res.data.defaultWageList)
        })
      },
		},
	}
</script>

<style scoped>
	#cloudCenter {
		height: 100%;
		display: flex;
		justify-content: space-evenly;
		overflow: hidden;
		align-items: center;
	}

	.content_box {
		height: 100%;
		overflow: hidden;
		display: flex;
		justify-content: space-evenly;
	}

	.left_box {
		height: 95%;
		width: 4.10rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		flex-shrink: 0;
	}

	.left-top {
		width: 4.1rem;
		height: 39.7%;
		display: flex;
		align-items: center;
		flex-direction: column;
		background: #ffffff;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
		justify-content: space-between;
	}

	.left-top-title {
		width: 3.84rem;
		height:13.3%;
		flex-shrink: 0;
		background-image: url(../../../../assets/people/left-top-title.png);
		background-repeat: no-repeat;
		background-position: 100% 100%;
		background-size: 100% 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: .18rem;
		color: #5B97F9;
		font-weight: 700;
	}

	.left-top-center {
		width: 100%;
		height: 37%;
		flex-grow: 1;
		display: flex;
		align-items: flex-start;
		justify-content: space-evenly;
		flex-direction: column;
	}

	.left-bottom {
		width: 4.1rem;
		height: 58.8%;
		flex-shrink: 0;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
	}

	.left-bottom-title {
		width: 100%;
		height: .5rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.num-back {
		width: 0.38rem;
		height: 0.42rem;
		background-image: url(../../../../assets/left-top-back.png);
		background-repeat: no-repeat;
		background-position: 100% 100%;
		background-size: 100% 100%;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 0.28rem;
		color: #5B97F9;
	}

	.left-top-bottom {
		width: 3.67rem;
		height: 44.9%;
		margin: 0 auto;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		justify-content: space-between;
	}

	.left-top-bottom-item {
		width: 1.8rem;
		height: 45.4%;
		position: relative;
		background-image: url(../../../../assets/people/glybj.png);
		background-repeat: no-repeat;
		background-position: 100% 100%;
		background-size: 100% 100%;
		display: flex;
		align-items: center;
		cursor: pointer;
	}

	.unback {
		width: 1.8rem;
		height: 45.4%;		
		border: 1px solid #E2EDFF;
		position: relative;
		background: url();
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		cursor: pointer;
	}

	.left-item-title {
		font-size: .14rem;
		font-weight: 600;
		color: #333;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.left-item-title span{
		margin-left: 0.54rem;
		margin-right: 0.2rem;
	}
	p.left-item-value {
		font-size: .18rem;
		color: orange;
		font-weight: 500;
	}

	.center_box {
		height: 95%;
		width: 7.87rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.right_box {
		height: 95%;
		width: 4.06rem;
		display: flex;
		align-items: center;
		flex-direction: column;
		justify-content: space-between;
		flex-shrink: 0;
	}

	.flex {
		display: flex;
	}

	.flex-center {
		display: flex;
		align-items: center;
	}

	.flex-betwen {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.alitem-start {
		align-items: flex-start!important;
	}

	.flex-column {
		flex-direction: column;
	}

	.left-title {
		font-size: 0.16rem;
		color: #404040;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.left-title-wrapper {
		width: 1.01rem;
		margin-right: .17rem;
		margin-left: 0.14rem;
		font-size: 0.14rem;
	}

	.width-100 {
		width: 100%;
	}

	.text-20 {
		font-size: .20rem;
	}
	.text-24{
		font-size: 0.24rem;
	}
	.text-22 {
		font-size: 0.16rem;
	}

	.text-12 {
		font-size: .12rem;
	}

	.text-13 {
		font-size: .13rem;
	}

	.text-14 {
		font-size: .14rem;
	}

	.text-16 {
		font-size: .16rem;
	}

	.text-404040 {
		color: #666666;
	}

	.text-070707 {
		color: #070707;
	}

	.text-400 {
		font-weight: 400;
	}

	.text-500 {
		font-weight: 500;
	}

	.text-600 {
		font-weight: 600;
	}

	.text-700 {
		font-weight: 700;
	}

	.avatar-16 {
		width: .16rem;
		height: .16rem;
	}

	.spot {
		width: .08rem;
		height: .08rem;
		border-radius: 888rem;
		border: none;
		outline: none;
		margin: 0 .08rem;
	}

	.bg-ff6100 {
		background: #FF6100;
	}

	.bg-3477FF {
		background-color: #3477FF;
	}

	.dynamic-list {
		width: 100%;
		height: calc(100% - 0.5rem);
		flex-grow: 1;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		flex-wrap: wrap;
		overflow-y: auto;
	}
	.dynamic-list::-webkit-scrollbar {
	  display: none;
	  /* Chrome Safari */
	}

	.dynamic-list {
	  scrollbar-width: none;
	  /* firefox */
	  -ms-overflow-style: none;
	  /* IE 10+ */
	}
	.dynamic-item {
		width: 48%;
		height: 33.3%;
		display: flex;
		align-items: flex-start;
		justify-content: center;
		flex-direction: column;
	}

	.dynamic-info {
		width: 100%;
		height: 60%;
		display: flex;
		align-items:center;
		justify-content: space-between;
	}

	.dynamic-user-info {
		width: calc(100% - 0.7rem);
		height: 100%;
		display: flex;
		flex-direction: column;
    	justify-content: space-evenly;
	}
	.dynamic-user-info span{
		width: 100%;
	}
	.dynamic-avatar {
		width: .6rem;
		height: 90%;
	}

	.center-top {
		width: 100%;
		height: 39.7%;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
		flex-shrink: 0;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		flex-direction: column;
	}

	.center-top-bottom {
		width: 100%;
		height: calc(100% - 0.4rem);
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
	}

	.center-top-data {
		display: flex;
		align-items: flex-start;
		justify-content: space-evenly;
		flex-direction: column;
		height: 100%;
		padding-left: .1rem;
	}

	.center-bottom {
		width: 100%;
		height: 58.8%;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
		flex-shrink: 0;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		flex-direction: column;
	}

	.center-top-left {
		width: 2.92rem;
		height: 22.3%;
		display: flex;
		align-items: center;
		background-image: url("../../../../assets/people/center-top-back.png");
		background-position: 100% 100%;
		background-size: 100% 100%;
		background-repeat: no-repeat;
		padding-left: .18rem;
	}

	.icon-spot {
		width: .1rem;
		height: .1rem;
		margin-right: .2rem;
	}

	.data-info {
		font-size: .14rem;
		color: #5B97F9;
		font-weight: 500;
		margin-top: .08rem;
	}

	#center-top-echarts {
		width: 5rem;
		height: 100%;
	}

	.center-bottom-data {
		width: 100%;
		height: calc(100% - 0.4rem);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.center-bottom-item {
		flex-grow: 1;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}

	.center-item1 {
		width: 1.41rem;
		height: 22.6%;
		background-repeat: no-repeat;
		background-size: 100% 100%;
		background-position: 100% 100%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.bg-img1 {
		background-image: url("../../../../assets/people/icon-center1.png");
	}

	.bg-img2 {
		background-image: url("../../../../assets/people/icon-center2.png");
	}

	.bg-img3 {
		background-image: url("../../../../assets/people/icon-center3.png");
	}

	.bg-img4 {
		background-image: url("../../../../assets/people/icon-center4.png");
	}

	.bg-img5 {
		background-image: url("../../../../assets/people/icon-center5.png");
	}

	.center-item2 {
		width: 1.32rem;
		height: 26.4%;
	}

	.center-item3 {
		line-height: 0.3rem;
		width: 1.32rem;
		display: flex;
		align-items: center;
		flex-direction: column;
	}
	.center-item3 div{
		display: flex;
		align-items: baseline;
		margin-bottom: 0.16rem;
	}
	.center-item3 p{
		margin-bottom: 0.1rem;
	}
	.text-5B97F9 {
		color: #5B97F9;
	}

	.right-top {
		width: 100%;
		height: 39.7%;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		flex-direction: column;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
	}

	#right-top-echarts {
		width: 100%;
		height: calc(100% - 0.4rem);
	}

	.right-bottom {
		width: 100%;
		height: 58.8%;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		flex-direction: column;
		box-shadow: 0 0 .1rem 0 rgba(145, 180, 241, .29);
		border-radius: .05rem;
	}

	#right-bottom-echarts {
		width: 100%;
		height: calc(100% - 0.4rem);
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

	.workers_box {
		display: flex;
		align-items: center;
		cursor: pointer;
	}

	.workers_num {
		margin-right: 0.1rem;
	}

	.workers_untils {
		font-size: 0.14rem;
	}

	.people_gl {
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		width: 100%;
		text-align: center;
	}

	.people_gl span {
		font-size: 0.14rem;
		color: #333;
		font-weight: 600;
	}

	.dynamic_time {
		margin-top: 0.1rem;
	}

	.lenged_jc {
		display: flex;
		align-items: center;
	}

	.sex_zb {
		display: flex;
		align-items: center;
	}
	.sex_than{
		display: flex;
		width: 100%;
		justify-content: space-between;
		font-size: 0.14rem;
	}
	.sex_box{
		width: 1.68rem;
	}
	.his_peo_num{
		color: #4AF4FE;
		font-size: 0.24rem;
		margin: 0 0.1rem;
	}
	.his_peo_box{
		font-size: 0.16rem;
	}
</style>
