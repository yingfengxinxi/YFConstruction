<template>

    <el-row style="margin-top: 50px">
        <el-col :span="10">
        	<el-card class="box-card" style="margin: 10px">
        		<div class="title">{{this.titleName}}项目统计</div>
            	<div id="firstMain" style="width: 100%;height:400px;margin-top:20px;" v-show="xmlist.length>0"></div>
            	<div class="zwsj" v-if="xmlist.length==0">
						暂无数据
					</div>
            </el-card>
        </el-col>

        <el-col :span="14">
        	<el-card class="box-card" style="margin: 10px">
        		<div class="title">{{this.titleName}}数量统计</div>
            	<div id="secondMain" style="width:100%;height:400px;margin-top:20px;" v-show="sllist.length>0"></div>
            	<div class="zwsj" v-if="sllist.length==0">
						暂无数据
					</div>
             </el-card>
        </el-col>
        
    </el-row>

</template>

<script type="text/javascript">

import * as echarts from 'echarts';
import request from '@/utils/request'
import {initPieList, initBarList} from '../api/statisticsEcharts'

export default{
    name: 'actualStatistics',
    components: {
     
    },
    data() {
      return {
        loading: false, // 加载动画
        pieOption: null,
        codeAndIdLists:[],//柱状图下拉框
        
        monitType:'',//类型  '1'：实测实量；    '3'：检测检验；
        titleName:'',
        xmlist:[1],
        sllist:[2]
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

        this.initPieChart()//饼状图图案
        this.initBarChart()//柱状图图案
        
    },
    methods: { 
        //初始化数据
        initData(to) {

            if(to.path.indexOf('/standardroomStatistics')>0){
            this.monitType = '2'
            }else if(to.path.indexOf('/actualStatistics')>0){
            this.monitType = '1'//实测实量
            this.titleName = '实测实量超标' 
            }else if(to.path.indexOf('/inspectStatistics')>0){
            this.monitType = '3'//检测检验
            this.titleName = '检测检验超标'
            }

            
            this.initPieList()//饼状图数据
            this.initBarList()//柱状图数据

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
                legend: {
                    orient: 'vertical',
                    right : '10',
                    top:'center'
                },
                series: [
                    {
                        name: '项目',
                        type: 'pie',
                        radius: '50%',
                        center:['40%','50%'],
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
            initPieList(this.monitType).then(response => {
                this.xmlist=response.data
                // this.pieOption.title.text = this.titleName 
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

            var emphasisStyle = {
                itemStyle: {
                    barBorderWidth: 1,
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowOffsetY: 10,
                    shadowColor: 'rgba(0,0,0,0.5)'
                }
            };

            option = {
                legend: {
                    data: ['超出范围', '未超出范围'],
                    
                    orient: 'vertical',
                    right : '10',
                },
                // brush: {
                //     toolbox: ['rect', 'polygon', 'lineX', 'lineY', 'keep', 'clear'],
                //     xAxisIndex: 0
                // },
                // toolbox: {
                //     feature: {
                //         magicType: {
                //             type: ['stack', 'tiled']
                //         },
                //         dataView: {}
                //     }
                // },
                tooltip: {},
                xAxis: {
                    data: [],
                    name: '填报编号',
                    axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},
                    axisLabel: {
                    	color: '#000',
                        interval: 0,
                        rotate: 30
                    },
                },
                yAxis: {
                   splitLine: {
						show: false,
						
					},
                },
                 grid: {
                    left: 100
                },

                series: [ {
                    name: '超出范围',
                    type: 'bar',
                    stack: 'one',
                    emphasis: emphasisStyle,
                    // itemStyle:{
                    //     color:'red'
                    // },
                    data: []
                },
                {
                    name: '未超出范围',
                    type: 'bar',
                    stack: 'one',
                    emphasis: emphasisStyle,
                    // itemStyle:{
                    //     color:'green'
                    // },
                    data: []
                }]      
            }
            
            option && myChart.setOption(option,true);
            this.barOption = option
            this.barChart = myChart
        },

        //初始化柱状图数据
        initBarList(){

            //获取 柱数值 信息及 X轴
           initBarList(this.monitType).then(response => {
             
                let data  = response.data
                let dataY1 = []
                let dataY2 = []
                let dataX = []
				this.sllist=data
                for(let index = 0 ;index < data.length; index++ ){
                    const element = data[index]

                    if(element.flag == '1'){//超出范围
                        dataY1.push(element.value)
                    }else if(element.flag == '0'){
                        dataY2.push(element.value)
                    }

                    //判断当前element的id和下一个element的id是否相同，不同的话，比较dataY1和dataY2的长度
                    if(index < data.length-1 && element.id != data[index+1].id){
                        if(dataY1.length > dataY2.length){
                            dataY2.push(0)
                        }else if(dataY1.length < dataY2.length){
                            dataY1.push(0)
                        }
                    }
                    
                    dataX.push(element.code)

                }

                let resultarr = [...new Set(dataX)]; //去重

                let listX = resultarr.slice(0, 10); 
                let listY1 = dataY1.slice(0, 10); 
                let listY2 = dataY2.slice(0, 10); 

                this.barOption.series[0].data = listY1
                this.barOption.series[1].data = listY2
               
                this.barOption.xAxis.data = listX
                this.barChart.setOption(this.barOption, true)    
                this.loading = false
            }).catch(() => {
                this.loading = false
            })

        },

    },

    

}






</script>
<style scoped>
	.title{
		font-size: 20px;
		font-weight: bold;
	}
	.zwsj{
		height: 380px;
		margin-top: 20px;
		text-align: center;
		font-size: 12px;
		color: #999;
	}
</style>