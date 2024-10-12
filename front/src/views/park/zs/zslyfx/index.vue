<template>

    <div>
		<div>
			<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
					 style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			  <el-form-item label="访问时间">
				<el-date-picker
				  v-model="accessDates"
				  type="daterange"
				  range-separator="至"
				  start-placeholder="开始日期"
				  end-placeholder="结束日期"
				  value-format="yyyy-MM-dd">
				</el-date-picker>
			  </el-form-item>
			</el-form>
		</div>
		<div>
			<el-row>
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px; overflow: hidden;">
						<div class="l-container" id="firstMain" :style="{width: width1, height:tableHeight, float:float}"></div>
						<div class="l-container" :style="{width: width1, height:tableHeight, float:float}">
							<div style="padding-top: 35%;">
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #FFA500; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">上门</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">11%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">1</span>
								</div>
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #FFF68F; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">电话</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">22%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">2</span>
								</div>
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #FFDEAD; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">网络</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">22%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">2</span>
								</div>
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #C71585; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">老客户介绍</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">11%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">1</span>
								</div>
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #4169FF; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">同行介绍</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">11%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">1</span>
								</div>
								<div class="lenged" style="margin-top: 20px;">
									<span style="background: #3CB371; width: 10px; height: 10px; border-radius: 50%; display: inline-block; overflow: hidden;"></span>
									<span style="margin-left: 10px; width: 120px; display: inline-block; overflow: hidden;">其他</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">22%</span>
									<span style="width: 120px; display: inline-block; overflow: hidden;">2</span>
								</div>
							</div>
						</div>
					</el-card>
				</el-col>
			
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px;">
						<div id="secondMain" :style="{width: width2, height:tableHeight}"></div>
					</el-card>
				</el-col>    
			</el-row>
		</div>
	</div>
</template>

<script type="text/javascript">

import * as echarts from 'echarts';
import request from '@/utils/request'
//import {selectPieData, selectBarData, selectWarningData} from '../api/questionOverview'

export default{
    name: 'questionOverview',
    components: {
     
    },
    data() {
      return {
        loading: false, // 加载动画
        pieOption: null,
        titleName:'',
        queryParams:{},
		float: 'left',
		width1: '50%',
		width2: '100%',
        tableHeight: document.documentElement.clientHeight - 260 + 'px', // 高度
		// 访问时间范围
		accessDates: '',
      }
    },
    watch:{
    },
    created(){

       
    },
    mounted(){

        this.initFirstChart()//饼状图图案
        this.initSecondChart()//柱状图图案
        
    },
    methods: {

        //饼状图初始化图案
        initFirstChart(){
            
            var chartDom = document.getElementById('firstMain');
            var myChart = echarts.init(chartDom,'light');
            var option;

            var data = [
            	{value: 1, name: '上门'},
                {value: 2, name: '电话'},
                {value: 2, name: '网络'},
                {value: 1, name: '老客户介绍'},
                {value: 1, name: '同行介绍'},
                {value: 2, name: '其他'},
            ]
            
            var total = 0;
            for (var i = 0; i < data.length; i++) {
            	total +=data[i].value;
            }
            
            var text2 = [
            	'{name|访问客户}',
            	'{value|' + total + '个}'
            ].join('\n')
            
            option = {
                title: [
            		{
            			text: '招商来源占比分析',
            		},
            		{
            		    text: text2,
            			x: 'center',
            			y: 'center',
            			textStyle: {
            			    rich:{
            					name:{
            						fontSize: 25,
            						lineHeight: 40,
            					},
            					value:{
            						fontSize: 25,
            						lineHeight: 40,
            					},
            				}
            			}
            		}
            	],
				tooltip: {
					formatter : function(params){
						var percent = 0;
						percent = ((params.value / total) * 100).toFixed(0);
						return params.name + '：' + percent	+ '% ' + params.value
					}
				},
                color:['#FFA500','#FFF68F','#FFDEAD','#C71585','#4169FF','#3CB371'],
                series: [
                    {
                        type: 'pie',
                        radius: ['80%','60%'],
                        center: ['50%', '50%'],
                        // roseType: 'radius',
                        itemStyle: {
                            borderRadius: 8,
            				normal: {
            					borderWidth:5,
            					borderColor:'#ffffff'
            				}
                        },
						label: {
							show: false,
						},
						labelLine: {
							show: false
						},
                        data: data,
                    }
                ]
            }
            
            option && myChart.setOption(option,true);
            this.pieOption = option
            this.pieChart = myChart
        },

        //初始化饼状图数据
        initFirstList(){
            selectPieData().then(response => {
                
                this.pieOption.title.text = this.titleName
                this.pieOption.series[0].data = response.data
                this.pieChart.setOption(this.pieOption, true)    
                this.loading = false
            }).catch(() => {
            this.loading = false
            })
        },
        

        //柱状图初始化图案
        initSecondChart(){
            
            var chartDom = document.getElementById('secondMain');
            var myChart = echarts.init(chartDom,'light');
            var option;
			
			var dataArr = {
			    xdata: ['1', '2', '3', '4', '5', '6'],
			    result: [
			        { name: '上门', data: [0, 0, 0, 0, 0, 0] },
			        { name: '电话', data: [0, 0, 0, 0, 0, 0] },
			        { name: '网络', data: [0, 0, 0, 0, 0, 0] },
			        { name: '老客户介绍', data: [0, 0, 0, 0, 0, 0] },
			        { name: '同行介绍', data: [0, 1, 1, 1, 1, 2] },
			        { name: '其他', data: [1, 1, 1, 0, 0, 0] },
			    ]
			};
			
			option = {
				title: {
					text: '招商来源月度对比'
				},
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
			            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data: ['上门', '电话', '网络', '老客户介绍', '同行介绍', '其他'],
			        bottom:'10'
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '10%',
			        containLabel: true
			    },
			    xAxis: [
			        {
			            type: 'category',
			            data: ['1月', '2月', '3月', '4月', '5月', '6月']
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value'
			        }
			    ],
			    series: [
			        {
			            name: '上门',
			            type: 'bar',
			            stack: '来源',
			            emphasis: {
			                focus: 'series'
			            },
						itemStyle:{
							color: '#FFA500'	
						},
			            data: [0, 0, 0, 0, 0, 1]
			        },
			        {
			            name: '电话',
			            type: 'bar',
			            stack: '来源',
			            emphasis: {
			                focus: 'series'
			            },
						itemStyle:{
							color: '#FFF68F'
						},
			            data: [0, 0, 0, 1, 0, 1]
			        },
			        {
			            name: '网络',
			            type: 'bar',
			            stack: '来源',
			            emphasis: {
			                focus: 'series'
			            },
						itemStyle:{
							color: '#FFDEAD'
						},
			            data: [0, 0, 0, 0, 1, 1]
			        },
			        {
			            name: '老客户介绍',
			            type: 'bar',
			            stack: '来源',
			            emphasis: {
			                focus: 'series'
			            },
						itemStyle:{
							color: '#C71585'
						},
			            data: [0, 0, 0, 0, 1, 0]
			        },
			        {
			            name: '同行介绍',
			            type: 'bar',
			            stack: '来源',
						itemStyle:{
							color: '#4169FF'
						},
			            emphasis: {
			                focus: 'series'
			            },
			            data: [0, 0, 0, 0, 1, 0],
			        },
			        {
			            name: '其他',
			            type: 'bar',
			            barWidth: 40,
			            stack: '来源',
						itemStyle:{
							color: '#3CB371'
						},
			            emphasis: {
			                focus: 'series'
			            },
			            data: [0, 0, 0, 0, 2, 0]
			        },
			    ]
			};
            
            option && myChart.setOption(option,true);
            this.barOption = option
            this.barChart = myChart
        },

        //初始化柱状图数据
        initSecondList(){

           //获取柱数值信息及X轴
		   let params = {
			   type: '1'
		   }
           selectBarData(params).then(re => {          
                let data  = re.data
                let dataX = []
				let dataY = []
				
				data.forEach(function(element) {
					dataX.push(element.clickDate)
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

    },

}



</script>>

<style lang="scss" scoped>
	.l-container {
		float: left;
	}
	.lenged{
		display: flex;
		justify-content: space-between;
	}
	/deep/.el-card__body{
		overflow: hidden;
	}
</style>