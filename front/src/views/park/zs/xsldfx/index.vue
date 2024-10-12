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
					<el-card class="box-card" style="margin: 10px;">
						<div id="firstMain" :style="{width: width, height:tableHeight}"></div>
					</el-card>
				</el-col>
			
				<el-col :span="12">
					<el-card class="box-card" style="margin: 10px;">
						<div id="secondMain" :style="{width: width, height:tableHeight}"></div>
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
		width: '100%',
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

            option = {
                title: {
                    text: '客户阶段分析',
                },
                tooltip: {
                    trigger: 'item',
                    //formatter: "{a} <br/>{b} : {c}%"
                },
                toolbox: {
                    feature: {
                        dataView: {readOnly: false},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                legend: {
                    data: ['初期沟通','房源选择','价格谈判','合同签约'],
                    bottom:'10'
                },
            
                series: [
                    {
                        name:'漏斗图',
                        type:'funnel',
                        left: '10%',
                        top: 60,
                        //x2: 80,
                        bottom: 60,
                        width: '80%',
                        // height: {totalHeight} - y - y2,
                        //min: 0,
                        //max: 100,
                        minSize: '0%',
                        maxSize: '100%',
                        sort: 'none',
                        gap: 2,
                        label: {
                            show: true,
                        },
                        labelLine: {
                            length: 20,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        },
                        itemStyle: {
                            borderColor: '#fff',
                            borderWidth: 1
                        },
                        emphasis: {
                            label: {
                                fontSize: 20
                            }
                        },
                        data: [
                            {value: 2, name: '初期沟通'},
                            {value: 0, name: '房源选择'},
                            {value: 0, name: '价格谈判'},
                            {value: 1, name: '合同签约'},
                        ]
                    }
                ]
            };
            
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
			
			var data = [
				{value: 1, name: '潜在客户'},
                {value: 1, name: '签约客户'},
                {value: 1, name: '流失客户'},
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
            			text: '客户状态分析',
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
						return params.name + '：' + percent	+ '%'
					}
				},
                color:['#27BEFF','#BDEB80'],
                legend: {
                    bottom: '10',
                    left:'center',
                    icon: "circle",
                },
                series: [
                    {
                        type: 'pie',
                        radius: ['30%','45%'],
                        center: ['50%', '50%'],
                        // roseType: 'radius',
                        itemStyle: {
                            borderRadius: 8,
							normal: {
								borderWidth:5,
								borderColor:'#ffffff'
							}
                        },
                        data: data,
                        label: {
            	            normal: {
								formatter: function(params) {
									var percent = 0;
									percent = ((params.value / total) * 100).toFixed(0);
									return params.name + '：' + percent	+ '%'			
								}
            	                /* formatter: params => {
            	                    return (
            							params.value
            	                    );
            	                }, */
            	            }
            	        },
                    }
                ]
            }
            
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