<template>
	
    <div class="body" ref="body">
        <div>
            <el-form ref="queryLineForm" :model="queryParams" :inline="true" label-width="80px" style="padding-left:10px;">
                <el-form-item class='dateinput' label="监测时间">
                    <!--<el-date-picker class='datestart' 
					v-model="queryParams.startTime" 
					type="date" 
					value-format="yyyy-MM-dd"
					style="width:160px" placeholder="选择日期">
                    </el-date-picker>
                    <div class="separate">~</div>
                    <el-date-picker class='dateend' 
					v-model="queryParams.endTime" 
					type="date" 
					value-format="yyyy-MM-dd"
					style="width:160px" placeholder="选择日期">
                    </el-date-picker>-->
                    <el-date-picker
				        v-model="executeDates"
				        type="daterange"
				        range-separator="至"
				        start-placeholder="开始日期"
				        end-placeholder="结束日期"
				        value-format="yyyy-MM-dd">
        </el-date-picker>
                </el-form-item>
                <el-form-item label="监测点" prop="pointName">
                    <el-select collapse-tags v-model="queryParams.pointId" 
						placeholder="请选择" 
						clearable size="small"
						@change="pointIdChange"
                        style="width: 200px">
                        <el-option v-for="dict in pointList" :key="dict.pointId" :label="dict.pointName" :value="dict.pointId" />
                    </el-select>
                </el-form-item>

                <!-- <el-form-item label="统计类型" prop="statisticsType">
                    <el-select v-model="queryParams.statisticsType" placeholder="请选择" clearable size="small" style="width: 120px">
                        <el-option key="2" value="2" label="小时" />
                        <el-option key="1" value="1" label="分钟" />
                    </el-select>
                </el-form-item> -->
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery1">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery1">重置</el-button>
                </el-form-item>
            </el-form>
            <div id="lineChart" style="width: 100%;" :style="{height:treeHeight-100+'px',width:bodywidth}"></div>

        </div>
    </div>
	
</template>

<script>

	import * as echarts from 'echarts';
    import {selectInfoForEcharts} from './../api/monitorHistoryData'
    import {selectPointsInfoForEcharts} from './../api/monitorPoint'
    import { getTodayZeroHour } from "../../../../utils/common";

	export default {
		name: 'historyEcharts',

		components: {
			
		},
		data() {
			return {

				queryParams: {
					startTime: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
					endTime: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(), //moment(new Date()).format('yyyy-MM-DD'),

					pointId: null,
					pointName: '',
					reportType: '1',//页面类型   1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
					// statisticsType: '2'//统计类型 1:分钟 2:小时
				},
				// 历史曲线数据
				historyCurveData: {
					xAxisL: [],
					series: []
				},
				//基坑监测点列表--下拉框使用
				pointList: [],
				treeProps: {
					label: "pointName"
				},
				executeDates:[this.formatterDate(new Date().getTime()),this.formatterDate(new Date().getTime()+24*60*60*1000)],
				checkAll: false,
				tableHeight: document.documentElement.clientHeight - 300,
				treeHeight: document.documentElement.clientHeight - 100,
				bodywidth: '',

				//单个图
				chartOption: {
					title: {
						text: '基坑监测位移曲线',
						left: 'center'
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							animation: false
						},
					},
					legend: {
						left: 10,
						top: 30
					},
					toolbox: {
						feature: {
							dataZoom: {
								yAxisIndex: 'none'
							},
							restore: {},
							saveAsImage: {}
						}
					},
					axisPointer: {
						link: {
							xAxisIndex: 'all'
						}
					},
					dataZoom: [{
						show: true,
						realtime: true,
                        start: 50,
                        end: 100
                    }],
					grid: [{
						left: 100,
						right: 100,
						top: '20%',
						height: '60%'
					}],
					xAxis: [{
						name:'监测时间',
						type: 'category',
						boundaryGap: false,
						axisLine: {
							onZero: true
						},
						data: []
					}],
					yAxis: [{
						type: 'value',
						name:''
					}],
					series: [
						{
                        name: '',
                        type: 'line',
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
				},
				yUnit:''//Y轴单位

			}
		},
		computed: {
		
		},
		created() {
            this.selectPointsInfoForEcharts()
		},
		mounted() {
            
		},
		watch: {
			//监听页面切换
			// '$route'(to, from) {
			// 	//TODO 重新加载数据 
			// 	this.initData(to)
			// }
		},
		methods: {
			// 格式化时间字符串
		    formatterDate (date) {
		    	let time=new Date(date)
		      if (date == null || date == "") return "";
		      let Y = time.getFullYear() + "-";
		      let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
		      let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
		      let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
		      let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
		      let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
		      let all = (Y + M + D)
		      return all;
		    },
			//监测点下拉框改变后刷新
			pointIdChange(){
				this.$forceUpdate()
			},
			
            //查询监测点--基坑监测：1
            selectPointsInfoForEcharts(){
				let map = {monitorType:this.queryParams.reportType}

                selectPointsInfoForEcharts(map).then(res=>{
                    this.pointList = res.data

					if(this.pointList.length != 0){
						this.queryParams.pointId = this.pointList[0].pointId

					}

                    //初始化图
                    this.initChart()
                     //加载数据
					this.getListForLine()

                })
            },


			// 格式化鼠标移入显示
			formatterToolTip() {
				var that = this
				return function(param) {
					
					let YName = that.yUnit
					var resultTooltip = "";
					resultTooltip =
						"<div style='background:rgba(13,5,30,.6);border:1px solid rgba(255,255,255,.2);padding:5px;border-radius:3px;'>" +
						"<div style='text-align:center;'>" + param[0].name + "</div>" +
						"<div style='padding-top:5px;'>"
					for (var i = 0; i < param.length; i++) {

						if (i > 0) {
							resultTooltip += "<div style='padding-top:2px;'>"
						}

						resultTooltip +=
							"<span style='display: inline-block; width: 4px; height:10px; border-radius: 5px;background-color: " + param[i]
							.color + ";'></span>" +
							"<span style=''> " +  "监测值: </span>" +
							"<span style='color:" + param[i].color + "'>" + param[i].value +"("+ YName +")"+ "</span>" 


					}
					resultTooltip +=
						"</div>" +
						"</div>";
					return resultTooltip
				}
            },
            
			/** 历史曲线搜索按钮操作 */
			handleQuery1() {
				this.getListForLine()
            },
            
			/** 历史曲线重置按钮操作 */
			resetQuery1() {
				this.resetForm('queryLineForm')
				this.queryParams =  {
					startTime: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
					endTime: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(), //moment(new Date()).format('yyyy-MM-DD'),

					reportType: '1', //页面类型   1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
					// statisticsType: '2'//统计类型 1:分钟 2:小时
				}

				if(this.pointList.length != 0){
					this.queryParams.pointId = this.pointList[0].pointId
				}

				this.chartOption.yAxis[0].name = ''
				this.yUnit = ''

				this.handleQuery1()
			},
			
			
			initChart() {
				this.chartObj = echarts.init(document.getElementById('lineChart'),"light")
			},
			
            
			getListForLine() {
				console.log(this.executeDates)
				let endTime = this.queryParams.endTime + " 23:59:59"
				if (this.executeDates != null && this.executeDates != '') {
			      this.queryParams.startTime = this.executeDates[0]
			      this.queryParams.endTime = this.executeDates[1]
			    } else {
			      this.queryParams.startTime = ''
			      this.queryParams.endTime = ''
			    }
				let params = {
					monitorType:this.queryParams.reportType,
					pointId: this.queryParams.pointId,
					// statisticsType: this.queryParams.statisticsType,

					startTime: this.queryParams.startTime,
					endTime: this.queryParams.endTime
				}
				
//				if (new Date(params.startTime) > new Date(params.endTime)) {
//					alert('开始时间不能大于结束时间')
//					return
//				}

				// 时间校验 statisticsType 1为分钟; 2为小时
				// 如果统计类型为分钟，只能查询一天内的数据
				// let msec = 24 * 60 * 60 * 1000
				// if (params.statisticsType === '1') {
				// 	if (new Date(params.endTime) - new Date(params.startTime) > msec) {
				// 		alert('统计类型为分钟时开始时间和结束时间不能超过一天')
				// 		return
				// 	}
                // }
				
				selectInfoForEcharts(params).then(response => {
					let data = response.data
					if(data != null){

						this.chartOption.xAxis[0].data = data.xData

						this.chartOption.series[0].data = data.serieData
						this.chartOption.series[0].name = data.legendData
						if(data.yUnit != null){
							this.chartOption.yAxis[0].name = '单位：'+data.yUnit
							this.yUnit = data.yUnit
						}
						

						this.chartOption.tooltip.formatter = this.formatterToolTip()
						if(data.xData.length < 5){
							this.chartOption.dataZoom[0].start = 0
						}else{
							this.chartOption.dataZoom[0].start = 50
						}
					
					}

					this.chartObj.setOption(this.chartOption, true)
			

				}).catch(() => {


				})

            },
			
			
			
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		padding: 0 15px;
		height: 100%;
	}

	.body {
		flex: 1;
		overflow-y: hidden;
		padding: 10px 30px 0px 30px;
		background-color: white;
		border: 1px solid #e6e6e6;
		border-radius: $xr-border-radius-base;
		position: relative;
	}


	.section-pageTitle {
		color: #333;
		font-weight: bold;
		font-size: 14px;
		margin-bottom: 30px;
	}

	.section-content {
		margin-bottom: 30px;

		.name {
			color: #333;
			font-size: 14px;
			margin-bottom: 10px;
		}

		.el-input {
			width: 300px;
		}
	}

	/deep/ .vxe-table--empty-placeholder {
		top: 74px !important;
		height: 256px !important;
	}

	/deep/ .el-tree-node {
		white-space: initial;
	}

	/deep/.el-tree-node__content {
		align-items: baseline;
		height: auto;
	}
	.dateinput .dateend,.separate,.datestart{
		float: left;
	}  
	.separate{
		margin-left: 5px;
		margin-right: 5px;
	}
	/deep/ .el-input__inner{
		height: 34px!important;
		line-height: 34px!important;
	}
	/*/deep/ .vxe-table--footer-wrapper{
	margin-top:0!important
}*/
</style>
