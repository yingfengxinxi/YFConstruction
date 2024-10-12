<template>

	<div class="body" ref="body">
		<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="dataParams" :inline="true" label-width="80px"
			style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<!--<el-form-item label="监测类型" prop="monitorType">
				<el-select v-model="monitorType" @change="monitorTypeChange" placeholder="请选择">
					<el-option v-for="option in monitorTypeDict" :key="option.dictValue" :label="option.dictLabel" :value="option.dictValue" />
				</el-select>
			</el-form-item>-->
			<el-form-item label="监测点" prop="pointIds">
				<el-select collapse-tags v-model="pointValueList" placeholder="请选择" clearable size="small" multiple
					@change="pointIdChange" style="width: 200px">
					<el-option v-for="dict in pointList" :key="dict.pointId" :label="dict.pointCode"
						:value="dict.pointId" />
				</el-select>
			</el-form-item>
			<el-form-item label="测点编号" prop="pointCode">
				<el-input v-model="dataParams.pointCode" placeholder="请输入测点编号" clearable size="mini" />
			</el-form-item>
			<el-form-item label="采集时间" prop="dateValue1">
				<el-date-picker v-model="dateValue1" type="datetimerange" range-separator="至" start-placeholder="开始日期"
					end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']">
				</el-date-picker>
			</el-form-item>
			<el-form-item label="报警状态" prop="alarmStatus">
				<el-select v-model="dataParams.alarmStatus" placeholder="请选择" clearable>
					<el-option v-for="option in statusOptions" :key="option.dictValue" :label="option.dictLabel"
						:value="option.dictValue" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<div class="right-container">
					<!-- <el-button class="xr-btn--orange" icon="el-icon-plus" type="primary" @click='create()'>新建
					</el-button>
					<el-button class="xr-btn--blue" type="primary" @click="bulkImportClick">导入
					</el-button> -->
					<el-button class="xr-btn--blue" type="primary" @click="exportFile(1)">导出
					</el-button>
					<!-- <el-button type="primary" @click='perDayData()'>日均数据
					</el-button> -->
				</div>
			</el-form-item>
			<el-form-item style="float: right">
				<el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
		<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" :cell-class-name="cellClassName"
			:header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border
			highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange"
			@selection-change="handleSelectionChange">
			<!-- <el-table-column show-overflow-tooltip type="selection" align="center" width="55" /> -->
			<el-table-column fixed label="序号" align='center' type="index" width="60" />
			<el-table-column :formatter="fieldFormatter" v-for="(item, index) in (monitorType == '1'?fieldList2:fieldList)" :key="index"
				:fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width" sortable="custom"
				show-overflow-tooltip align="center">

			</el-table-column>
		<!-- 	<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.id)">修改
					</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="deleteClick(scope.row.id)">删除
					</el-button>
					<el-button size="mini" type="text" icon="el-icon-printer" @click="informClick(scope.row.id)">标记无效
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		<div class="p-contianer">
			<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" :pager-count="5"
				class="p-bar" background layout="prev, pager, next, sizes, total, jumper"
				@size-change="handleSizeChange" @current-change="handleCurrentChange" />
		</div>
		<div>
			<el-form ref="queryLineForm" :model="queryParams" :inline="true" label-width="80px"
				style="padding-left:10px;">
				<el-form-item class='dateinput' label="监测时间">
					<el-date-picker v-model="dateValue2" type="datetimerange" range-separator="至"
						start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss"
						:default-time="['00:00:00', '23:59:59']">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="监测点" prop="pointIds">
					<el-select collapse-tags v-model="pointValue" placeholder="请选择" clearable size="small" multiple
						@change="pointIdChange" style="width: 200px">
						<el-option v-for="dict in pointList" :key="dict.pointId" :label="dict.pointCode"
							:value="dict.pointId" />
					</el-select>
				</el-form-item>
				<el-form-item label="Y轴最小值" prop="minValue">
					<el-input type="number" v-model.number="queryParams.minValue" placeholder="Y轴最小值" clearable
						size="mini" />
				</el-form-item>
				<el-form-item label="Y轴最大值" prop="maxValue">
					<el-input type="number" v-model.number="queryParams.maxValue" placeholder="Y轴最大值" clearable
						size="mini" />
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery1">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery1">重置</el-button>
				</el-form-item>
			</el-form>
			<div id="lineChart" style="width: 100%;" :style="{height:treeHeight-100+'px',width:bodywidth}"></div>

		</div>
		<create v-if="createShow" :businessType="businessType" :dictData="dictData" :action="action"
			@close="createShow = false" @save-success="getList" />

		<!-- 批量导入 -->
		<bulk-import-user :show="bulkImportShow" :param="param" @close="importClose" />
	</div>

</template>

<script>
	import * as echarts from 'echarts';
	import Create from './Create'
	import BulkImportUser from '@/components/importModel'
	import {
		exportFile
	} from "@/api/build/exportFile";
	import {
		downloadExcelWithResData
	} from '@/utils/index';
	import {
		selectPointsInfoForEcharts
	} from './../api/monitorPoint'
	import {
		getList,
		getDataList,
		selectMonitorDataListEcharts,
		deleteById
	} from './../api/fonPitMon'
  import { sortColumn } from '@/utils/index';

	export default {
		name: 'historyEcharts',

		components: {
			Create,
			BulkImportUser
		},
		data() {
			return {
				currentPage: 1,
				pageSize: 10,
				total: 0,
				loading: false, // 加载动画
				createShow: false,
				businessType: 1000,
				dictData: {
					pointList: []
				},
				action: {
					type: 'save',
					isReadonly: false,
					data: null
				},
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
				dataParams: {

				},
				// 绑定时间组件
				dateValue1: '',
				dateValue2: '',
				statusOptions: [{
					dictLabel: '正常',
					dictValue: '1'
				}, {
					dictLabel: '预警',
					dictValue: '2'
				}, {
					dictLabel: '报警',
					dictValue: '3'
				}],
				effectiveType: [{
					label: '有效',
					value: '1'
				}, {
					label: '无效',
					value: '2'
				}],
				list: [],
				fieldList: [ //表头
					{
						prop: "assetName",
						label: "设备名称",
					},
					{
						prop: "pointName",
						label: "监测点名称",
					},
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
						prop: "alarmStatus",
						label: "报警状态",
					},

				],
				fieldList2: [ //表头
					{
						prop: "assetName",
						label: "设备名称",
					},
					{
						prop: "pointName",
						label: "监测点名称",
					},
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
						prop: "aggregateValue",
						label: "累计变化值",
					},
					{
						prop: "rateChange",
						label: "变化速率",
					},
					{
						prop: "alarmStatus",
						label: "报警状态",
					},

				],
				// 列表监测点绑定数据
				pointValueList: [],
				// 多选监测点
				pointValue: [],
				// 监测项类型数据
				monitorTypeDict: [],
				monitorType: '', //页面类型   1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
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
						name: '监测时间',
						type: 'category',
						boundaryGap: false,
						axisLine: {
							onZero: true
						},
					}],
					yAxis: [{
						type: 'value',
					}],
					series: [{
						name: '',
						type: 'line',
						center: ['40%', '50%'],
						data: [],
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				},
				// 导入组件相关
				param: '',
				bulkImportShow: false,

			}
		},
		computed: {

		},
		created() {
		},
		mounted() {
			this.initDate()
			this.initData(this.$route)
		},
		watch: {
			//监听页面切换
			'$route'(to, from) {
				//TODO 重新加载数据
				this.initData(to)
				console.log(this.$route)
			}
		},
		methods: {

			// 初始化数据
			initData(to) {
				if (to.path.indexOf('/fonPitMon') > 0) {
					this.monitorType = '1'
					this.$options.name = 'fonPitMon'
					this.chartOption.title.text= '基坑监测位移曲线'
				} else if (to.path.indexOf('/highFormwork') > 0) {
					this.monitorType = '11'
					this.$options.name = 'highFormwork'
					this.chartOption.title.text= '高支模数据曲线'
				}
				console.log(this.monitorType)
				this.getList()
				this.selectPointsInfoForEcharts()
			},
			initDate() {
				var day1 = new Date();
				day1.setTime(day1.getTime() - 24 * 60 * 60 * 1000 - 1000);
				var s1 = day1.getFullYear() + "-" + (day1.getMonth() + 1) + "-" + day1.getDate() +
					" " + day1.getHours() + ":" + day1.getMinutes() + ":" + day1.getSeconds();
				//今天的时间
				var day2 = new Date();
				day2.setTime(day2.getTime());
				var s2 = day2.getFullYear() + "-" + (day2.getMonth() + 1) + "-" + day2.getDate() +
					" " + day2.getHours() + ":" + day2.getMinutes() + ":" + day2.getSeconds();
				// 设置监测时间
				this.dateValue2 = [s1, s2]
			},
			monitorTypeChange() {
				this.getList()
				this.selectPointsInfoForEcharts()
			},
			create() {
				this.action.type = 'save';
				this.createShow = true
			},
			getList() {
				this.dataParams.page = this.currentPage;
				this.dataParams.limit = this.pageSize;
				this.dataParams.monitorType = this.monitorType

				this.dataParams.pointIds = this.pointValueList
        this.dataParams.fieldName = this.queryParams.fieldName
        this.dataParams.sortingType = this.queryParams.sortingType

				if (this.dateValue1 != '' && this.dateValue1 != null) {
					this.dataParams.startTime = this.dateValue1[0]
					this.dataParams.endTime = this.dateValue1[1]
				} else {
					this.dataParams.startTime = null
					this.dataParams.endTime = null
				}
				getDataList(this.dataParams)
					.then(res => {
						this.list = res.data.list;
						if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage &&
							this.currentPage > 1) {
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
			//列格式化  如一些字典等
			fieldFormatter(row, column, cellValue) {
				if (column.property === 'alarmStatus') {
					return this.selectDictLabel(this.statusOptions, row.alarmStatus)
				} else if (column.property === 'statisticalType') {
					return this.selectDictLabel(this.dictData.statisticalTypeDict, row.statisticalType)
				}
				return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
			},
			perDayData() {
				this.$router.push({
					path: this.$route.path + '/monitoringData',
					query: {
						dictId: 1,
					}
				})
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				this.pageSize = val
				this.getList()
			},
			/*sortChange(column, prop, order) {
				this.currentPage = 1
				this.sortData = column

			}*/
    sortChange(column) {
      if(column.prop == 'value') {
        column.prop = 'value+0'
      }
      if(column.prop == 'aggregateValue') {
        column.prop = 'aggregateValue+0'
      }
      if(column.prop == 'rateChange') {
        column.prop = 'rateChange+0'
      }
      sortColumn(this,column);
    },
			/** 勾选操作 */
			// 当选择项发生变化时会触发该事件
			handleSelectionChange(val) {
				this.selectionList = val // 勾选的行
				this.updateTableHeight();
			},

			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val
				this.getList()
			},
			handleTypeDrop(command) {
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
									this.getList();
								} else {
									this.$message.error(res.msg);
								}
								this.loading = false;
							}).catch(err => {
								this.loading = false;
							});
						})
						.catch(() => {})
				} else if (command == 'export') { //导出
					this.exportFile(2);
				} else if (command == 'import') { //导入

				}
			},
			// 当某一行被点击时会触发该事件
			handleRowClick(row, column, event) {
				if (column.property == 'code') {
					this.rowID = row.id;
					this.showDview = true;
				}
			},
			updateClick(id) {
				this.action.type = 'update';
				this.action.id = id;
				this.createShow = true
			},
			deleteClick(id) {
				this.$confirm('确定删除选中的数据吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					deleteById(id)
						.then(data => {
							if (data.code == 0) {
								this.$message({
									message: '删除成功',
									type: 'success'
								});
								this.getTableList();
							} else {
				 			this.$message.error(data.msg);
							}
						})
						.catch(() => {})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			informClick() {

			},
			//监测点下拉框改变后刷新
			pointIdChange() {
				this.$forceUpdate()
			},

			//查询监测点
			selectPointsInfoForEcharts() {
				let map = {
					monitorType: this.monitorType
				}

				selectPointsInfoForEcharts(map).then(res => {
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
			cellClassName({
				row,
				column,
				rowIndex,
				columnIndex
			}) {
				if (column.property === 'code') {
					return 'can-visit--underline can-visit--bold'
				}
			},

			headerCellClassName({
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
							"<span style='display: inline-block; width: 4px; height:10px; border-radius: 5px;background-color: " +
							param[i]
							.color + ";'></span>" +
							"<span style=''> " + "监测值: </span>" +
							"<span style='color:" + param[i].color + "'>" + param[i].value + "(" + YName + ")" +
							"</span>"


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
				this.resetForm('queryLineForm')
				this.queryParams = {}

				// 重置监测点
				this.pointValue = []
				this.pointList.forEach(item => {
					this.pointValue.push(item.pointId)
				})
				// 重置监测时间
				this.initDate()

				this.handleQuery1()
			},


			initChart() {
				this.chartObj = echarts.init(document.getElementById('lineChart'), "light")
			},
			// 查询ecahrts数据
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

				selectMonitorDataListEcharts(params).then(response => {
					let data = response.data

					this.chartOption.series = data

					this.chartObj.setOption(this.chartOption, true)


				}).catch(() => {


				})

			},
			// 导入方法
			bulkImportClick() {
				let lists = [
					"监测点编号(*)",
					"记录值(*)",
					"采集时间 时间格式如下yyyy-MM-dd HH:mm:ss(*)"
				]
				let colNameCodes = [
					"pointCode",
					"value",
					"monitorTime"
				]
				//示例数据
				let dataLists = [
					"a34002",
					"50",
					"2021-10-05 00:00:00"
				]

				this.param = {
					title: "监测数据导入模板(*)为必填项 监测编号需与对应监测点相同",
					excelTitle: "监测数据导入模板",
					colNameCodes: colNameCodes,
					colNames: lists,
					sampleData: dataLists,
					url: 'build/equipmentMonitorData/excelImport'
				}
			 this.bulkImportShow = true
			},
			// 导入成功后
			importClose() {
				this.bulkImportShow = false
				this.initData()
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
			async exportFile(type) {
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
					await getDataList(reqParam)
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
				let fieldList = [ //表头
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
						prop: "alarmStatus",
						label: "报警状态",
						dictList: [{
								"dictLabel": "正常",
								"dictValue": "1"
							},
							{
								"dictLabel": "预警",
								"dictValue": "2"
							},
							{
								"dictLabel": "报警",
								"dictValue": "3"
							},
							{
								"dictLabel": "控制",
								"dictValue": "4"
							},
						],
					},
				]

				let param = {};
				param.result = data;
				param.fieldList = fieldList;
			 param.title = "监测数据";
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

	.dateinput .dateend,
	.separate,
	.datestart {
		float: left;
	}

	.separate {
		margin-left: 5px;
		margin-right: 5px;
	}

	/deep/ .el-input__inner {
		height: 34px !important;
		line-height: 34px !important;
	}

	/*/deep/ .vxe-table--footer-wrapper{
	margin-top:0!important
}*/
</style>
