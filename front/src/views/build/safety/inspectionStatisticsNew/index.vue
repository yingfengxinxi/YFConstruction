<template>

	<div>
		<el-row >
			<el-col :span="11">
				
				<el-card class="box-card" style="margin: 10px">
					<div ref="firstMain" class="title">危险源Top10</div>
					<div id="firstMain" style="height:340px;" v-show="wxylist.length>0"></div>
					<div class="zwsj" v-if="wxylist.length==0">
						暂无数据
					</div>
				</el-card>
			</el-col>

			<el-col :span="13">
				<el-card class="box-card" style="margin: 10px;">
					<div ref="secondMain" class="title">检查问题数量统计</div>
					<div id="secondMain" style="height:340px;" v-show="jcwtlist.length>0"></div>
					<div class="zwsj" v-if="jcwtlist.length==0">
						暂无数据
					</div>
				</el-card>
			</el-col>

		</el-row>
		<el-row>
			<el-col :span="11">
				<el-card class="box-card" style="margin: 5px 10px">
					<div class="title">整改超时预警</div>
					<div id="warningMain" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>
			<el-col :span="13">
				<el-card class="box-card" style="margin: 5px 10px;">
					<div class="title">安全隐患趋势</div>
					<div id="thirdlyMain" style="height:340px;" v-show="aqqslist.length>0"></div>
					<div class="zwsj" v-if="aqqslist.length==0">
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
import {selectPieData, selectBarData, selectCurveData, selectWarningData} from '../api/inspectionStatistricsNew'

export default{
    name: 'inspectionStatistics',
    components: {
     
    },
    data() {
      return {
        loading: false, // 加载动画
        pieOption: null,  
        titleName:'',
        wxylist:[],
        jcwtlist:[],
        aqqslist:[],
		widthOne: '',
		widthTwo: '',
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
		this.widthOne = this.$refs.firstMain.offsetWidth + 'px'
		this.widthTwo = this.$refs.secondMain.offsetWidth + 'px'
		document.getElementById("firstMain").style.width = this.widthOne
		document.getElementById("secondMain").style.width = this.widthTwo
		document.getElementById("thirdlyMain").style.width = this.widthTwo
        this.initData(this.$route)

        this.initPieChart()//饼状图图案
        this.initBarChart()//柱状图图案
		this.initCurveChart()//曲线图图案
		this.initWarningChart()//整改超时预警
        
    },
    methods: { 
        //初始化数据
        initData(to) {

            this.titleName = '危险源Top10'

            
            this.initPieList()//饼状图数据
            this.initBarList()//柱状图数据
			this.initCurveList()//曲线图数据
			this.initWarningList()//整改超时预警数据

        },

        //饼状图初始化图案
        initPieChart(){
            
            var chartDom = document.getElementById('firstMain');
            var myChart = echarts.init(chartDom,'light');
            var option;

            option = {
                tooltip: {
                    trigger: 'item'
                },
                // legend: {
                //     orient: 'vertical',
                //     right : '0%',
                // },
                series: [
                    {
                        name: '项目',
                        type: 'pie',
                        radius: '50%',
						// center: ['50%', '60%'],
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
            this.pieOption = option
            this.pieChart = myChart
        },

        //初始化饼状图数据
        initPieList(){
            selectPieData().then(response => {
				this.wxylist=response.data
                this.pieOption.series[0].data = response.data
                this.pieChart.setOption(this.pieOption, true)    
                this.loading = false
            }).catch(() => {
            this.loading = false
            })
        },
        

        //柱状图初始化图案
        initBarChart(){
            
            var chartDom = document.getElementById('secondMain');
            var myChart = echarts.init(chartDom,'light');
            var option;
			
			option = {
				grid: {
					bottom: '3%',
					containLabel: true
				},
                tooltip: {
                    trigger: 'axis'
                },
			    xAxis: {
			        type: 'category',
					name: '事故类型',
			        data: []
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: [{
			        data: [],
			        type: 'bar'
			    }]
			}
            
            option && myChart.setOption(option,true);
            this.barOption = option
            this.barChart = myChart
        },

        //初始化柱状图数据
        initBarList(){

            //获取柱数值信息及X轴
           selectBarData().then(res => {          
                let data  = res.data
                let dataX = []
				let dataY = []
				this.jcwtlist=res.data
				
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
		
		//曲线图初始化图案
		initCurveChart(){
		    
		    var chartDom = document.getElementById('thirdlyMain');
		    var myChart = echarts.init(chartDom,'light');
		    var option;
			
			option = {
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: []
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				// toolbox: {
				// 	feature: {
				// 		saveAsImage: {}
				// 	}
				// },
				xAxis: {
					type: 'category',
					boundaryGap: false,
					data: []
				},
				yAxis: {
					type: 'value'
				},
				series: []
			}
		    
		    option && myChart.setOption(option,true);
		    this.curveOption = option
		    this.curveChart = myChart
		},
		
		//初始化曲线图数据
		initCurveList(){
		
		    //获取柱数值信息及X轴
		   selectCurveData().then(res => { 
		        let data  = res.data
				this.aqqslist=data.dataX
				this.curveOption.legend.data = data.legend
				this.curveOption.xAxis.data = data.dataX
				this.curveOption.series = data.series
		        this.curveChart.setOption(this.curveOption, true)  
		        this.loading = false
		    }).catch(() => {
		        this.loading = false
		    })
		
		},
		
		//整改超时预警饼状图初始化图案
		initWarningChart(){
		    
		    var chartDom = document.getElementById('warningMain');
		    var myChart = echarts.init(chartDom,'light');
		    var option;
		
		    option = {
		        tooltip: {
		            trigger: 'item'
		        },
		        // legend: {
		        //     orient: 'vertical',
				// 	right : '0%',
		        // },
		        series: [
		            {
		                name: '项目',
		                type: 'pie',
		                radius: '50%',
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
		    this.warningOption = option
		    this.wargingChart = myChart
		},
		
		//初始化整改超时预警饼状图数据
		initWarningList(){
		    selectWarningData().then(response => {
		        
		        this.warningOption.series[0].data = response.data
		        this.wargingChart.setOption(this.warningOption, true)    
		        this.loading = false
		    }).catch(() => {
		    this.loading = false
		    })
		},

    },

    

}






</script>>

<style scoped>
	.title{
		font-size: 20px;
		font-weight: bold;
	}
	.zwsj{
		height: 320px;
		margin-top: 20px;
		text-align: center;
		font-size: 12px;
		color: #999;
	}
</style>