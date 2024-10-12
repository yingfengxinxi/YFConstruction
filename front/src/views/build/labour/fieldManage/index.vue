<template>

    <div>
		<el-row>
		    <el-col :span="11">
				<el-card class="box-card" style="margin: 10px">
					<div id="firstMain" style="height:330px;"></div>
					<div class="servicedetails">
						<div class="servicetitle">
								在场状态
						</div>
						<div class="attendance">
							<span class="charttitle">出勤人数</span>
							<span class="chartnum">{{attendanceNum}}人</span>
						</div>
						<div class="allpeople">
							<span class="charttitle">总人数</span>
							<span class="chartnum">{{headcount}}人</span>
						</div>
						<div class="assessment">
							<span class="charttitle">出勤率</span>
							<span class="chartnum">{{percent}}</span>
						</div>
					</div>
				</el-card>
		    </el-col>

		    <el-col :span="13">
				<el-card class="box-card" style="margin: 10px;">
					<div id="secondMain" style="height:330px;"></div>
				</el-card>
		    </el-col>
		</el-row>
		<el-row>
			<el-col :span="11">
				<el-card class="box-card" style="margin: 10px;">
					<div id="barMain" style="height:330px;"></div>
				</el-card>
			</el-col>

		    <el-col :span="13">
		    	<el-card class="box-card" style="margin: 10px">
		    		<div id="nationMain" style="height:330px;"></div>
		    	</el-card>
		    </el-col>

		</el-row>
	</div>


</template>

<script type="text/javascript">

import * as echarts from 'echarts';
import request from '@/utils/request'
import {selectAttendanceData, selectWorkTypeList, selectBarData, selectNationAndGenderList} from '../api/fieldManage'

export default{
    name: 'fieldManage',
    components: {

    },
    data() {
      return {
        loading: false, // 加载动画
        pieOption: null,
        titleName:'',

		attendanceNum: '', // 出勤人数
		absenteeism: '', // 缺勤人数
		headcount: '', // 总人数
		percent: '', // 出勤率
      }
    },
    watch:{
        //监听页面切换
      '$route'(to, from) {
        //TODO 重新加载数据
        this.initData(to)
      }
    },
    created(){


    },
    mounted(){
        this.initData(this.$route)

        this.initPieChart()//劳务信息
		this.initSecondChart()//现场工种
        this.initBarChart()//班组人数
		this.initNationAndGenderChart()//全场性别和民族

    },
    methods: {
        //初始化数据
        initData(to) {

            this.initPieList()//劳务信息数据
			this.initSecondList()//现场工种
            this.initBarList()//班组人数
			this.initNationAndGenderList()//全场性别和民族

        },

        //劳务信息初始化图案
        initPieChart(){

            var chartDom = document.getElementById('firstMain');
            var myChart = echarts.init(chartDom,'light');
            var option;

            option = {
			    title: [
					{
						text: '劳务信息',
					},
					{
					    text: '',
						x: 'center',
						y: 'center',
						textStyle: {
						    fontSize: '30',
						    fontWeight: '500',
						}
					}
				],
			    color:['#27BEFF','#BDEB80'],
			    legend: {
			        top: '40',
			        left:'14',
			        icon: "circle",
					orient: 'vertical',
			    },
			    series: [
			        {
			            type: 'pie',
			            radius: ['40%','50%'],
			            center: ['50%', '50%'],
			            // roseType: 'radius',
			            itemStyle: {
			                borderRadius: 8
			            },
			            data: [
			                {value: 39, name: '出勤人数'},
			                {value: 17, name: '缺勤人数'},
			            ],
			            label: {
				            normal: {
				                formatter: params => {
				                    return (
										params.value
				                    );
				                },
				            }
				        },
			        }
			    ]
			}

            option && myChart.setOption(option,true);
            this.pieOption = option
            this.pieChart = myChart
        },

        //初始化劳务信息数据
        initPieList(){
			selectAttendanceData().then(res => {
				var attendanceData = res.data
				this.pieOption.title[1].text = attendanceData.headcount

				this.attendanceNum = attendanceData.attendanceNum
				this.absenteeism = attendanceData.absenteeism
				this.headcount = attendanceData.headcount
				this.percent = attendanceData.percent

				var data = []
				var objA = {
					value: this.attendanceNum,
					name: '出勤人数'
				}
				var objB = {
					value : this.absenteeism,
					name: '缺勤人数'
				}
				data.push(objA)
				data.push(objB)
				this.pieOption.series[0].data = data

				this.pieChart.setOption(this.pieOption, true)
				this.loading = false
			}).catch(() => {
				this.loading = false
            })

        },


		//现场工种初始化图案
		initSecondChart(){

		    var chartDom = document.getElementById('secondMain');
		    var myChart = echarts.init(chartDom,'light');
		    var option;

			option = {
				title: {
					text: '全场工种'
				},
		        tooltip: {
		            trigger: 'item'
		        },
				legend: {
					top: 'bottom'
				},
				// toolbox: {
				// 	show: true,
				// 	feature: {
				// 		mark: {show: true},
				// 		dataView: {show: true, readOnly: false},
				// 		restore: {show: true},
				// 		saveAsImage: {show: true}
				// 	}
				// },
				series: [
					{
						type: 'pie',
						radius: ['35%', '60%'],
						center: ['50%', '50%'],
						data: [],
					},
				]
			}

		    option && myChart.setOption(option,true);
		    this.secondOption = option
		    this.secondChart = myChart
		},

		//初始化现场工种数据
		initSecondList(){

		    //获取柱数值信息及X轴
		   selectWorkTypeList().then(re => {
		        let data  = re.data
				this.secondOption.series[0].data = data
		        this.secondChart.setOption(this.secondOption, true)
		        this.loading = false
		    }).catch(() => {
		        this.loading = false
		    })

		},


        // 班组人数
        initBarChart(){

            var chartDom = document.getElementById('barMain');
            var myChart = echarts.init(chartDom,'light');
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

			option = {
				title: {
					text: '全场班组人数',
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
						formatter: function(value){
						    var str = "";
						    var num = 6; //每行显示字数
						    var valLength = value.length; //该项x轴字数
						    var rowNum = Math.ceil(valLength / num); // 行数

						    if(rowNum > 1)
						    {
						        for(var i = 0; i < rowNum; i++)
						        {
						            var temp = "";
						            var start = i * num;
						            var end = start + num;

						            temp = value.substring(start, end) + "\n";
						            str += temp;
						        }

						        return str;
						    }
						    else
						    {
						        return value;
						    }
						}
					},
			        data: []
			    },
			    yAxis: {
			        type: 'value',
			        axisLine: {
						show: false,
						color: 'A3E9FC'
					},
			    },
			    series: [{
			        data: [],
			        type: 'bar',
					barWidth: 30,//宽度
			    }]
			}

            option && myChart.setOption(option,true);
            this.barOption = option
            this.barChart = myChart
        },

        //初始化班组人数
        initBarList(){

            //获取柱数值信息及X轴
           selectBarData().then(re => {
                let data  = re.data
                let dataX = []
				let dataY = []

				data.forEach(function(element) {
					dataX.push(element.name)
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

		//全场性别和民族
		initNationAndGenderChart(){

		    var chartDom = document.getElementById('nationMain');
		    var myChart = echarts.init(chartDom,'light');
		    var option;

		    option = {
		        title: {
		            text: '全场性别和民族',
		        },
		        tooltip: {
		            trigger: 'item'
		        },
		        legend: {
		            orient: 'vertical',
		            right : '10',
		        },
				"color": ['#77B8F8', '#73DCA0', '#FFCB87', '#F57D65',
					'#FF9FE3', '#A89FFF', '#5B96FA'],
		        series: [
		            {
		                type: 'pie',
		                radius: '50%',
						center: ['25%', '50%'],
		                data: [],
		                emphasis: {
		                    itemStyle: {
		                        shadowBlur: 10,
		                        shadowOffsetX: 0,
		                        shadowColor: 'rgba(0, 0, 0, 0.5)'
		                    }
		                }
		            },
					{
					    type: 'pie',
					    radius: '50%',
						center: ['75%', '50%'],
					    data: [],
					    emphasis: {
					        itemStyle: {
					            shadowBlur: 10,
					            shadowOffsetX: 0,
					            shadowColor: 'rgba(0, 0, 0, 0.5)'
					        }
					    }
					}
		        ]
		    }

		    option && myChart.setOption(option,true);
		    this.nationOption = option
		    this.nationChart = myChart
		},

		//初始化整改超时预警饼状图数据
		initNationAndGenderList(){
		    selectNationAndGenderList().then(res => {

				var data = res.data
				var nationList = data.nationList
				var genderList = data.genderList

		        this.nationOption.series[0].data = genderList
		        this.nationOption.series[1].data = nationList
		        this.nationChart.setOption(this.nationOption, true)
		        this.loading = false
		    }).catch(() => {
		    this.loading = false
		    })
		},

    },

}

</script>>

<style scoped lang="scss">
  #firstMain{
  	width: 330px;
  	height: 360px;
  	float: left;
  }
  .servicedetails{
  	float: left;
  	height: 241px;
  	width: 190px;
  	/*padding-left: 40px;*/
	  padding: 60px;
  }
  .servicetitle{
  	font-size: 16px;
  	font-weight: 600;
		color: #404040;
		margin-top: 4px;
  }
  .attendance{
  	width: 159px;
		height: 45px;
		background: rgba(39, 190, 255, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 18px;
		line-height: 45px;
		.chartnum{
			color: #27BEFF;
		}
  }
  .allpeople{
  	width: 159px;
		height: 45px;
		background: rgba(52, 119, 255, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 18px;
		line-height: 45px;
				.chartnum{
			color: #3477FF;
		}

  }
  .assessment{
 		width: 159px;
		height: 45px;
		background: rgba(189, 235, 128, 0.08);
		border-radius: 5px;
		border: 1px solid #E6E7E7;
		margin-top: 18px;
		line-height: 45px;

  }
  .charttitle{
  	float: left;
  	font-weight: 400;
		color: #404040;
		font-size: 14px;
		margin-left: 16px;
  }
  .chartnum{
  	float: right;
	  font-size: 0.18rem;
	  margin-right: 0.16rem;
  }
  /deep/ .el-card__body{
	  overflow: hidden;
  }
</style>
