<template>
	
    <div class="body" ref="body">
    	<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="dataParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<el-form-item label="监测类型" prop="monitorType">
				<el-select v-model="monitorType" @change="monitorTypeChange" placeholder="请选择">
					<el-option v-for="option in monitorTypeDict" :key="option.dictValue" :label="option.dictLabel" :value="option.dictValue" />
				</el-select>
			</el-form-item>
			<el-form-item label="监测点" prop="pointIds">
				<el-select collapse-tags v-model="pointValueList" 
					placeholder="请选择" 
					clearable size="small"
					multiple
					@change="pointIdChange"
					style="width: 200px">
					<el-option v-for="dict in pointList" :key="dict.pointId" :label="dict.pointCode" :value="dict.pointId" />
				</el-select>
			</el-form-item>
			<el-form-item label="测点编号" prop="pointCode">
				<el-input v-model="dataParams.pointCode" placeholder="请输入测点编号" clearable size="mini"/>
			</el-form-item>
			<el-form-item label="采集时间" prop="dateValue1">
				<el-date-picker
				  v-model="dateValue1"
				  type="datetimerange"
				  range-separator="至"
				  start-placeholder="开始日期"
				  end-placeholder="结束日期"
				  value-format="yyyy-MM-dd HH:mm:ss"
				  :default-time="['00:00:00', '23:59:59']">
				</el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>	
			 <el-form-item style="float: right">
	          <el-button
	            class="main-table-header-button"
	            type="normal"
	            icon="el-icon-back"
	            @click="back" >返回</el-button>
	        </el-form-item>
			<el-form-item style="float: right">
				<div class="right-container">
					</el-button>
					<el-button class="xr-btn--orange" icon="el-icon-plus" type="primary" @click="exportFile(1)">导出
					</el-button>
				</div>
			</el-form-item>
		</el-form>
		<!-- <div class="table-head-container">
			<flexbox v-if="selectionList.length > 0" class="selection-bar">
				<div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
				<flexbox class="selection-items-box">
					<el-button v-for="(item, index) in selectionHandleItems"  :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
					</el-button>
				</flexbox>
			</flexbox>
		</div> -->
		<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange" @selection-change="handleSelectionChange">
			<!-- <el-table-column show-overflow-tooltip type="selection" align="center" width="55" /> -->
			<el-table-column fixed label="序号" align='center' type="index" width="60" />
			<el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width"  :sortable="false" show-overflow-tooltip>

			</el-table-column>
			<!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.checkId)">修改
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		<div class="p-contianer">
				<el-pagination :current-page="currentPage"  :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
        <div>
            <el-form ref="queryLineForm" :model="queryParams" :inline="true" label-width="80px" style="padding-left:10px;">
                <el-form-item class='dateinput' label="监测时间">
                    <el-date-picker
                      v-model="dateValue2"
                      type="datetimerange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                	  value-format="yyyy-MM-dd HH:mm:ss"
                      :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="监测点" prop="pointName">
                    <el-select collapse-tags v-model="pointValue" 
                		placeholder="请选择" 
                		clearable size="small"
                		multiple
                		@change="pointIdChange"
                        style="width: 200px">
                        <el-option v-for="dict in pointList" :key="dict.pointId" :label="dict.pointCode" :value="dict.pointId" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Y轴最小值" prop="minValue">
                	<el-input type="number" v-model.number="queryParams.minValue" placeholder="Y轴最小值" clearable size="mini"/>
                </el-form-item>
                <el-form-item label="Y轴最大值" prop="maxValue">
                	<el-input type="number" v-model.number="queryParams.maxValue" placeholder="Y轴最大值" clearable size="mini"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery1">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery1">重置</el-button>
                </el-form-item>
               
            </el-form>
            <div id="lineChartMonth" style="width: 100%;" :style="{height:treeHeight-100+'px',width:bodywidth}"></div>

        </div>
    </div>
	
</template>

<script>

	import * as echarts from 'echarts';
	import { exportFile } from "@/api/build/exportFile";
	import { downloadExcelWithResData } from '@/utils/index';
    import {selectInfoForEcharts} from './../api/monitorHistoryData'
    import {selectPointsInfoForEcharts} from './../api/monitorPoint'
    import {getListByMonth, selectMonitorDataMonthEcharts} from './../api/fonPitMon'

	export default {
		name: 'historyEcharts',

		components: {
			
		},
		data() {
			return {
				currentPage: 1,
				pageSize: 10,
				total: 0,
				loading: false, // 加载动画
				/** 选中时操作 */
				selectionHandleItems: [{
					name: '删除',
					type: 'delete',
					icon: 'delete'
				},
				{
					name: '导出选中',
					type: 'export',
					icon: 'export'
				}
				],
				/** 勾选行 */
				selectionList: [], // 勾选数据 用于全局导出
				dataParams:{
					
				},
				// 监测项类型数据
				monitorTypeDict: [],
				monitorType: '', 
				// 绑定时间组件
				dateValue1: '',
				dateValue2: '',
				// 列表监测点绑定数据
				pointValueList:[],
				// 多选监测点
				pointValue: [],
				// 字典相关数据
				dictData:{
					pointList: []
				},
				list: [
					{
						qualCheckType:'CJ1',
						qualCheckDate:'2021-10-18',
						qualRepairTimeLimit:'-414.091',
						qualCheckPersonName:'-0.097',
						qualCheckCom:'-0.68',
						qualDocumentNumber:'0.519',

					},{
						qualCheckType:'CJ2',
						qualCheckDate:'2021-10-18 10:37:32',
						qualRepairTimeLimit:'-1213.8',
						qualCheckPersonName:'0.097',
						qualCheckCom:'-9.038',
						qualDocumentNumber:'0.531',

					}
				],
			fieldList: [ //表头
				{
					prop: "pointCode",
					label: "监测点编号",
				},
				{
					prop: "monitorTime",
					label: "采集时间",
				},
				{
					prop: "value",
					label: "记录值",
				},
				{
					prop: "createTime",
					label: "创建时间",
				}
			],
				queryParams: {},
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
					}],
					yAxis: [{
						type: 'value',
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
			}
		},
		computed: {
		
		},
		created() {
			// 获取列表数据
			this.initDate()
			this.initData()
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
			// 初始化数据
			initData(){
			  this.getDicts('b_equipment_monitor_type').then(res => {
				this.monitorTypeDict = res.data
				// 默认选择第一个监测类型
				if (this.monitorTypeDict.length > 0) {
					this.monitorType = this.monitorTypeDict[0].dictValue
				}
				this.getList()
				this.selectPointsInfoForEcharts()
			  })
			},
			initDate() {	
				var day1 = new Date();
				day1.setTime(day1.getTime()-24*60*60*1000 - 1000);
				var s1 = day1.getFullYear()+"-" + (day1.getMonth()+1) + "-" + day1.getDate() 
						+ " " + day1.getHours() +":" + day1.getMinutes() + ":" + day1.getSeconds();
				//今天的时间
				var day2 = new Date();
				day2.setTime(day2.getTime());
				var s2 = day2.getFullYear()+"-" + (day2.getMonth()+1) + "-" + day2.getDate()
						+ " " + day2.getHours() +":" + day2.getMinutes() + ":" + day2.getSeconds();
				// 设置监测时间
				this.dateValue2 = [s1, s2]
			},
			// 获取列表数据
			getList(){
				this.dataParams.page = this.currentPage;
				this.dataParams.limit = this.pageSize;
				this.dataParams.monitorType = this.monitorType
				
				this.dataParams.pointIds = this.pointValueList
				
				if (this.dateValue1 != '' && this.dateValue1 != null) {
					this.dataParams.startTime = this.dateValue1[0]
					this.dataParams.endTime = this.dateValue1[1]
				} else {
					this.dataParams.startTime = null
					this.dataParams.endTime = null
				}
				getListByMonth(this.dataParams)
				.then(res => {
					this.list = res.data.list;
					if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
					  this.currentPage = this.currentPage - 1
					  this.getList()
					} else {
					  this.total = res.data.totalRow
					  this.loading = false
					}
					this.$nextTick(() => {
					  document.querySelector('.el-table__body-wrapper').scrollTop = 1
					})
					this.loading = false
				})
				.catch(() => {
				  this.loading = false
				})
			},
			// 监测类型change方法
			monitorTypeChange() {
				this.getList()
				this.selectPointsInfoForEcharts()
			},
			back() {
		      this.$router.go(-1)
		    },
			perDayData(){
				this.$router.push({
			        path: this.$route.path + '/monitoringData',
			        query: {
			          dictId: 1,
			        }
			      })
			},
			// 更改每页展示数量
			handleSizeChange (val) {
				this.pageSize = val
				this.getList()
			},
			sortChange (column, prop, order) {
				this.currentPage = 1
				this.sortData = column
			
			},
			/** 勾选操作 */
			// 当选择项发生变化时会触发该事件
			handleSelectionChange (val) {
				this.selectionList = val // 勾选的行
				
			},
	
			// 更改当前页数
			handleCurrentChange (val) {
				this.currentPage = val
				this.getList()
			},
			handleTypeDrop (command) {
			if (command == 'delete') { //删除
				this.$confirm("确定删除选中的数据吗？", '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
						const ids = this.selectionList.map(item => item['checkId'])
						this.loading = true;
						delTrueByIds(ids).then(res => {
							if (res.code == 0) {
								this.$message.success("删除成功");
								
							} else {
								this.$message.error(res.msg);
							}
							this.loading = false;
						}).catch(err => {
							this.loading = false;
						});
					})
					.catch(() => { })
			} else if (command == 'export') { //导出
				this.exportFile(2);
			} else if (command == 'import') { //导入

			}
		},
		// 当某一行被点击时会触发该事件
		handleRowClick (row, column, event) {
			if (column.property == 'code') {
				this.rowID = row.id;
				this.showDview = true;
			}
		},
			updateClick(){
				
			},
			//监测点下拉框改变后刷新
			pointIdChange(){
				this.$forceUpdate()
			},
			
            //查询监测点
            selectPointsInfoForEcharts(){
				let map = {monitorType:this.monitorType}

                selectPointsInfoForEcharts(map).then(res=>{
                    this.pointList = res.data
					this.dictData.pointList = this.pointList

					// 监测点设置默认值
					this.pointValue = []
					this.pointList.forEach(item => {
						this.pointValue.push(item.pointId)
					})

                    //初始化图
                    this.initChart()
                     //加载数据
					this.getListForLine()

                })
            },
			/**
		 * 通过回调控制class
		 */
		cellClassName ({
			row,
			column,
			rowIndex,
			columnIndex
		}) {
			if (column.property === 'code') {
				return 'can-visit--underline can-visit--bold'
			}
		},

		headerCellClassName ({
			row,
			column,
			rowIndex,
			columnIndex
		}) {
			/*if (column.property === 'code') {
			  return 'header-can-visit-backgroud'
			}*/
			return ''
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
				
			// 列表重置方法
			resetQuery() {
				this.dataParams = {};
				this.dateValue1 = '';
				this.pointValueList = [];
				this.getList()	
			},
            
			/** 历史曲线搜索按钮操作 */
			handleQuery1() {
				this.getListForLine()
            },
            
			/** 历史曲线重置按钮操作 */
			resetQuery1() {

				this.handleQuery1()
			},
				
			initChart() {
				this.chartObj = echarts.init(document.getElementById('lineChartMonth'),"light")
			},
			
			getListForLine() {
				let params = this.queryParams
				params.monitorType = this.monitorType
				params.pointIds = this.pointValue
				
				
				if (this.dateValue2 != '' && this.dateValue2 != null) {
					params.startTime = this.dateValue2[0]
					params.endTime = this.dateValue2[1]
				} else {
					params.startTime = null
					params.endTime = null
				}
				
				
				selectMonitorDataMonthEcharts(params).then(response => {
					let data = response.data
					
					this.chartOption.series = data
					console.log("走了吗")
					
					this.chartObj.setOption(this.chartOption, true)				
				
				}).catch(() => {		
				
				})
            },
			/**
			 *
			 * 使用导出需引入js文件如下：
			 * import { exportFile } from "@/api/build/exportFile";
			 * import { downloadExcelWithResData } from '@/utils/index';
			 * 方法入参说明： type=1 全部导出  type=2 部分导出
			 * 请求列表数据说明：需替换为每页列表的数据请求url
			 * 
			 */
			async  exportFile (type) {
			  this.loading = true;
			  let data = '';
			  if (type == 1) {
			    //请求不分页的列表数据
				
				var objString = JSON.stringify(this.dataParams);
				var obj2 = JSON.parse(objString);
			    let reqParam = obj2;
				reqParam.monitorType = this.monitorType
				reqParam.pointIds = this.pointValueList
				
				if (this.dateValue1 != '' && this.dateValue1 != null) {
					reqParam.startTime = this.dateValue1[0]
					reqParam.endTime = this.dateValue1[1]
				} else {
					reqParam.startTime = null
					reqParam.endTime = null
				}
				reqParam.pageType = 0
			    await getListByMonth(reqParam)
			      .then(res => {
			        data = res.data.list;
			      })
			      .catch(err => {
			        // console.log(err);
			        this.$message.error("导出数据加载失败");
			        this.loading = false;
			      })
			    // console.log(data);
			  } else if (type == 2) {
			    //获取当前选中行的数据
			    data = this.selectionList;
			  }
				/**
				 * 整理数据
				 * param中result是需要导出的数据(必填)
				 * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)，
				 * 如果有字典需转换请添加dictType类型，如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)
				 * param中title是导出Excel的一级表头(必填)
				 * 注意：fieldList中字段值必须和列表中字段值相对应！！！
				 */
			  let fieldList = [//表头
			    { prop: "pointCode", label: "监测点编号", },
			    { prop: "monitorTime", label: "采集时间", },
			    { prop: "value", label: "记录值", },
			  ]
			  
			  let param = {};
			  param.result = data;
			  param.fieldList = fieldList;
			  param.title = "监测日均数据";
			  await exportFile(param)
			    .then(res => {
			      // console.log(res);
			      downloadExcelWithResData(res);
			      this.loading = false;
			    })
			    .catch(err => {
			      // console.log(err)
			      this.$message.error("导出文件生成异常");
			      this.loading = false;
			    })
			},
				
		}
	}
</script>

<style lang="scss" scoped>



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
