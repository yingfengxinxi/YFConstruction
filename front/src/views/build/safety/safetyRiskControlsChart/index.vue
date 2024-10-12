<template>

  <div>
    <el-row >
      <el-col :span="11">
        <el-card class="box-card" style="margin: 10px">
          <div id="firstMain" style="width: 100%;height:340px;"></div>
        </el-card>
      </el-col>

      <el-col :span="13">
        <el-card class="box-card" style="margin: 10px;">
          <div id="secondMain" style="height:340px;"></div>
        </el-card>
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="11">
        <el-card class="box-card" style="margin: 5px 10px">
          <div id="warningMain" style="width: 100%;height:340px;"></div>
        </el-card>
      </el-col>
      <el-col :span="13">
        <el-card class="box-card" style="margin: 5px 10px;">
          <div id="thirdlyMain" style="height:340px;"></div>
        </el-card>
      </el-col>

    </el-row>
  </div>

</template>

<script type="text/javascript">

  import * as echarts from 'echarts';
  import request from '@/utils/request'
  import {selectChartData} from '../api/safetyRiskControlsChart'

  export default{
    name: 'safetyRiskControlsChart',
    components: {

    },
    data() {
      return {
        loading: false, // 加载动画
        pieOption: null,
        titleName:'',
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
      this.initCurveChart()//曲线图图案
      this.initWarningChart()//整改超时预警

    },
    methods: {
      //初始化数据
      initData(to) {
        this.initWarningList()//整改超时预警数据

      },

      //饼状图初始化图案
      initPieChart(){

        var chartDom = document.getElementById('firstMain');
        var myChart = echarts.init(chartDom,'light');
        var option;

        option = {
          title: {
            text: "分部分项",
          },
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




      //柱状图初始化图案
      initBarChart(){

        var chartDom = document.getElementById('secondMain');
        var myChart = echarts.init(chartDom,'light');
        var option;

        option = {
          title: {
            text: '风险类型',
          },
          grid: {
            bottom: '3%',
            containLabel: true
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            name: '风险类型名称',
            axisLine: {
			  lineStyle: {
			    color: "#5B8FF9",
			  },
			},
			axisLabel:{
				color: '#000',
			},
            data: []
          },
          yAxis: {
            type: 'value',
            axisLine:{
            	show:false
            }
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


      //曲线图初始化图案
      initCurveChart(){

        var chartDom = document.getElementById('thirdlyMain');
        var myChart = echarts.init(chartDom,'light');
        var option;

        option = {
          title: {
            text: "风险分级/标识",
          },
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
        this.curveOption = option
        this.curveChart = myChart
      },


      //整改超时预警饼状图初始化图案
      initWarningChart(){

        var chartDom = document.getElementById('warningMain');
        var myChart = echarts.init(chartDom,'light');
        var option;

        option = {
          title: {
            text: '管控等级',
          },
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
        selectChartData().then(response => {
          this.pieOption.series[0].data = response.data.subItemCensus
          this.pieChart.setOption(this.pieOption, true);
          this.barOption.series[0].data = response.data.accidentTypeCensus.accidentTypeCensusData
          this.barOption.xAxis.data = response.data.accidentTypeCensus.accidentTypeCensusX
          this.barChart.setOption(this.barOption, true);
          this.curveOption.series[0].data = response.data.riskLevelIdentCensus
          this.curveChart.setOption(this.curveOption, true);
          this.warningOption.series[0].data = response.data.riskLevelCensus
          this.wargingChart.setOption(this.warningOption, true);
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },

    },



  }
</script>>
