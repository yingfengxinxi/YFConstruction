<template>
	<div class="dangerbox">
		<div id="mainchart">
			
		</div>
		<div class="line">
		</div>
		<div id="mychart">
			
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	import {queryMenuListByPath} from '@/api/admin/menu'
	import {selectSafetyHiddenDanger, selectQualHiddenDanger} from '../workbench'	
	export default {
		name: 'tavlevideo',
		props:['projectId'],
		
		data() {
			return {
				index: 0,
				zlyhlist:[]
			}
		},
		watch:{
			projectId: {
				handler(newName, oldName) {
					this.projectId = newName
				},
			
			}

		},
		mounted() {
			this.drawChart()
			this.mychart()
			this.remsize()
		},
		methods: {
			remsize(){
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
			drawChart() {
				let myChart = echarts.init(document.getElementById("mainchart"),null,{ devicePixelRatio: 2 });
				
				let option = {
					title: {
				        text: '安全隐患',
				        top:25,
				        left:'10',
				        textStyle:{
				            fontSize:12
				        },
						triggerEvent: true, // 是否触发事件
				    },
					grid: {
						bottom: "40"
					},
					color: ["#77A7F8", "#82DFC1"],
					    tooltip: {
					        trigger: 'axis',
					        axisPointer: {           
					            type: 'shadow'    
					        }
					    },
					legend: {
						top: '30',
						right: '20',
						icon: "circle",
						data: ['总数', '未解决',]
					},
					xAxis: {
						type: 'category',
						axisLabel: {
							textStyle: {
						        color: "#999999", //X轴文字颜色
						      },
							fontSize:10,
							
						},
						 axisLine: {
						      show: true, //隐藏X轴轴线
						      lineStyle: {
						        color: "#5B8FF9",
						      },
						    },
						//data: ['石方施工', '基坑施工', '主体施工', '装饰施工', '施工收尾'],
						data:[],
					},
					yAxis: {
						type: 'value',
						axisLine:{
							show:false,
						},
						max: function(value) {
							return value.max<5?5:value.max;
						}
					},
					series: [{
							name:'总数',
							//data: [50, 60, 80, 20, 70],
							data: [],
							type: 'bar',
							
						},
						{
							name:'未解决',
							//data: [20, 30, 15, 5, 20],
							data: [],
							type: 'bar',
							
						}
					]
				};
				
				let map = {projectId:this.projectId}

				selectSafetyHiddenDanger(map).then(res => {
					var data = res.data
					var xData = []
					var dataA = []
					var dataB = []
					
					data.forEach(item => {
						xData.push(item.name)
						dataA.push(item.count)
						dataB.push(item.unfinished)
					})
					
					option.xAxis.data = xData
					option.series[0].data = dataA
					option.series[1].data = dataB
							
					myChart.setOption(option, true)
					myChart.on('click', params => {
						//console.log(params)
						if (params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('inspectionInfo')
						}
					})
				})
			},
			mychart(){
				let myChart = echarts.init(document.getElementById("mychart"),null,{ devicePixelRatio: 2 });
				let option = {
					title: {
				        text: '质量隐患',
				        top:25,
				        left:'10',
				        textStyle:{
				            fontSize:12
				        },
						triggerEvent: true, // 是否触发事件
				    },
					grid: {
						bottom: "40"
					},
					color: ["#77A7F8", "#FAB262"],
					    tooltip: {
					        trigger: 'axis',
					        axisPointer: {           
					            type: 'shadow'    
					        }
					    },
					legend: {
						top: '30',
						right: '20',
						icon: "circle",
						data: ['总数', '未解决',]
					},
//					xAxis: {
//						type: 'category',
//						axisLine: {
//						      show: true, //隐藏X轴轴线
//						      lineStyle: {
//						        color: "#5B8FF9",
//						      },
//						    },
//						axisLabel: {
//							interval:0,
//							textStyle: {
//						        color: "#999999", //X轴文字颜色
//						      },
//							fontSize:10,
////							rotate: '45',
////							formatter: function(value){
////							    var str = "";
////							    var num = 4; //每行显示字数
////							    var valLength = value.length; //该项x轴字数
////							    var rowNum = Math.ceil(valLength / num); // 行数
////
////							    if(rowNum > 1)
////							    {
////							        for(var i = 0; i < rowNum; i++)
////							        {
////							            var temp = "";
////							            var start = i * num;
////							            var end = start + num;
////
////							            temp = value.substring(start, end) + "\n";
////							            str += temp;
////							        }
////
////							        return str;
////							    }
////							    else
////							    {
////							        return value;
////							    }
////							}
//						},
//						//data: ['石方施工', '基坑施工', '主体施工', '装饰施工', '施工收尾'],
//						data: []
//					},
					xAxis: {
						type: 'category',
						axisLabel: {
							textStyle: {
						        color: "#999999", //X轴文字颜色
						      },
							fontSize:10,
							
						},
						 axisLine: {
						      show: true, //隐藏X轴轴线
						      lineStyle: {
						        color: "#5B8FF9",
						      },
						    },
						//data: ['石方施工', '基坑施工', '主体施工', '装饰施工', '施工收尾'],
						data:[],
					},
					yAxis: {
						type: 'value',
						axisLine:{
							show:false,
						},
						max: function(value) {
							return value.max<5?5:value.max;
						}
					},
					series: [
						{
							name:'总数',
							//data: [80, 20, 50, 60, 70],
							data: [],
							type: 'bar',
							
						},
						{
							name:'未解决',
							//data: [15, 5, 20, 30, 20],
							data: [],
							type: 'bar',
							
						}
					]
				};
				
				let map = {projectId:this.projectId}
				selectQualHiddenDanger(map).then(res => {
					var data = res.data
					var xData = []
					var dataA = []
					var dataB = []
					this.zlyhlist=res.data
					data.forEach(item => {
						xData.push(item.name)
						dataA.push(item.count)
						dataB.push(item.unfinished)
					})
					
					option.xAxis.data = xData
					option.series[0].data = dataA
					option.series[1].data = dataB
							
					myChart.setOption(option, true)
					// 质量隐患绑定点击事件
					myChart.on('click', params => {
						//console.log(params)
						if (params.componentType === 'title' || params.componentType === 'series') {
							this.goToUrl('qualCheck')
						}
					})
				});				
			},		
			// 根据菜单路由跳转页面
			goToUrl(path) {
				let param = {path: path}
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
					    comname: item.path,
					    name:item.menuId
					}
					if(JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(activeclass)) == -1) {
					    this.$store.state.app.pageheader.push(activeclass); // 进行动态的操作
					} 
					this.$store.state.app.activeclass= activeclass.comname
					this.$router.push({name:activeclass.name})
				})
			},
		}
	}
</script>

<style scoped lang="scss">
	.dangerbox{
		height: calc(100% - 16px - 0.12rem);
	}
	#mainchart {
		width: 90%;
		height: 48%;
		margin: 0 auto;
	}
	.line{
		width: 90%;
		height: 1px;
		border: 1px solid #E6E7E7;
		margin: 0 auto;
	
		
	}
	#mychart{
		width: 90%;
		height: 48%;
		margin: 0 auto;	
	}
	.zwsj{
		font-size: 14px;
		color: #999;
		text-align: center;
		margin-top: 20px;
	}
</style>