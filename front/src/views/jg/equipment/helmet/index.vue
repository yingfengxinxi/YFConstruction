<template>
	<div>
		<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
			style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<el-form-item label="项目名称" prop="projectId">
				<el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
					<el-option v-for="item in dictData.projectOptions" :key="item.id" :label="item.name"
						:value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="设备编号" prop="imei">
				<el-input v-model="queryParams.imei" placeholder="请输入设备编号" clearable size="small"
					@keyup.enter.native="getList" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
			<el-form-item style="margin-left: 50px">
				<el-radio-group v-model="tabPosition" @change="openList">
					<el-radio-button label="list">列表</el-radio-button>
					<el-radio-button label="map">地图</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<!-- <el-form-item style="float: right">
				<div class="right-container">
					<el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary"
						@click="createClick">新建
					</el-button>
				</div>
			</el-form-item> -->
		</el-form>
		<div v-if="isList" v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限">
			<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight"
				:cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border"
				use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick"
				@sort-change="sortChange" @selection-change="handleSelectionChange">
				<el-table-column fixed label="序号" align='center' type="index" width="60" />
				<el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop"
					:label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" align="center"
					show-overflow-tooltip>

				</el-table-column>
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" icon="el-icon-position" @click="trackClick(scope.row)">轨迹
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-edit" @click="detailsClick(scope.row.id)">查看
						</el-button>
						<!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >修改
            </el-button> -->
						<!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteClick(scope.row.id)"
            >删除
            </el-button> -->
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize"
					:total="total" :pager-count="5" class="p-bar" background
					layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" />
			</div>
		</div>
		<!-- 地图标记及呼叫 -->
		<BdMap v-if="isMap"></BdMap>
		<!-- 选择劳务人员信息弹出层 -->
		<el-dialog :title="title2" :visible.sync="open2" top="5vh" width="1400px" append-to-body>
			<el-row>
			  <el-col :span="8">
				  <el-form v-show="true" ref="queryForm2" :model="queryParams2" :inline="true" label-width="80px"
				  	style="padding-top: 20px;padding-left:10px;">
				  	<el-form-item label="监测时间">
				  		<el-date-picker v-model="executeDates2" type="datetimerange" range-separator="至"
				  			start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss"
				  			:default-time="['00:00:00', '23:59:59']">
				  		</el-date-picker>
				  	</el-form-item>
				  	<el-form-item>
				  		<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery2">搜索</el-button>
				  		<el-button icon="el-icon-refresh" size="mini" @click="resetQuery2">重置</el-button>
				  	</el-form-item>
				  </el-form>
				  <el-table v-loading="loading" :data="dataList2" height="500px">
				    <el-table-column label="定位时间" align="center" prop="uploadTime"/>
				    <el-table-column label="X坐标" align="center" prop="xaxis"/>
				    <el-table-column label="Y坐标" align="center" prop="yaxis"/>
				  </el-table>
				  <div class="p-contianer">
				    <el-pagination
				      :current-page="currentPage2"
				      :page-sizes="pageSizes2"
				      :page-size.sync="pageSize2"
				      :total="total2"
				      class="p-bar"
				      background
					  :small="true"
				      layout="prev, pager, next, sizes, total"
				      @size-change="handleSizeChange2"
				      @current-change="handleCurrentChange2"/>
				  </div>
				  
			  </el-col>
			  <el-col :span="16">
			  	<div id="svgDom"  style="width: 930px;height:700px"></div>			  
			  </el-col>
			</el-row>
			
		</el-dialog>

		<!-- 新建 -->
		<create v-if="createShow" :businessType="businessType" :action="action" :dictData="dictData"
			@close="createShow = false" @save-success="getList" />
	</div>
</template>

<script>
	import {
		mapGetters
	} from 'vuex' //权限
	import Lockr from 'lockr' //分页
	import * as echarts from 'echarts'
	import Create from './Create'
	import BdMap from './BaiduMap'
	import axios from 'axios'
	import { sortColumn } from '@/utils'
	import {
		getList,
		deleteDetails,
		getPeopleList,
		getGpsList,
		getProjectsList,
		getSVG,
		selectActionTrack
	} from "../api/helmet"

	export default {
		/** DEMO列表 */
		name: 'inspectionPlan',
		components: {
			Create,
			BdMap,
		},
		data() {
			return {
				businessType: 10000,
				loading: false, // 加载动画
				/** 页面头部 */
				inputContent: '', //查询值
				moreTypes: [{
						type: 'import',
						name: '导入',
						icon: 'import'
					},
					{
						type: 'export',
						name: '导出',
						icon: 'export'
					},
				],
				/** 列表 */
				tableHeight: document.documentElement.clientHeight - 250, // 表的高度
				list: [],
				fieldList: [ //表头
					{
						prop: "projectName",
						label: "项目名称"
					},
					{
						prop: "imei",
						label: "设备编号"
					},
					{
						prop: "peopleName",
						label: "项目人员"
					},
					{
						prop: "sos",
						label: "呼叫状态"
					},
					{
						prop: "vol",
						label: "电压"
					},
					{
						prop: "gsm",
						label: "信号强度"
					},
					{
						prop: "createTime",
						label: "创建时间"
					},
				],
				sortData: [], // 字段排序
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 40, 80, 100],
				total: 0,
				queryParams: {},
				/** 新增*/
				createShow: false,
				/***/
				action: {
					type: 'save',
					data: null
				},
				// 字典数据
				dictData: {
					// 呼叫状态
					sosStateDict: [],
					// 项目人员
					peopleLict: [],
					// 项目下拉框列表
					projectOptions: [],
				},
				/** 勾选行 */
				selectionList: [], // 勾选数据 用于全局导出
				/** 控制详情展示 */
				rowID: '', // 行信息
				showDview: false,

				// 安全帽Id
				helmetId: '',
				rom:null,
				// 弹出框分页
				currentPage2: 1,
				pageSize2: 5,
				pageSizes2: [5, 10, 20, 30, 40],
				total2: 0,
				// 弹出层表格数据
				dataList2: [],
				// 弹出层标题
				title: '',
				// 是否显示弹出层
				open: false,
				// 弹出层标题
				title2: '',
				// 是否显示个人信息弹出层
				open2: false,
				// 弹出层查询参数
				queryParams2: {
					page: '',
					limit: '',
				},
				executeDates2: [this.formatterDate(new Date().getTime() - 24 * 60 * 60 * 1000), this.formatterDate(this
					.formatterDate(new Date().getTime()))],
				// 列表是否显示
				isList: true,
				// 地图是否显示
				isMap: false,
				tabPosition: 'list',
				myChart:null,
			}
		},
		computed: {
			//权限 start
			...mapGetters(['allAuth']),
			//权限 end
			titleIcon() {
				return require(`@/assets/img/crm/customer.png`)
			},
		},
		mounted() {
			/** 控制table的高度 */
			window.onresize = () => {
				this.updateTableHeight()
			}
			// 查询项目数据
			getProjectsList().then(res => {
				this.dictData.projectOptions = res.data;
			})
			// 获取字典数据
			this.initDicts()
			// 获取执行部门数据
			this.selectPeopleList()
			//获取列表数据
			this.getList();
		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			openList() {

				if (this.tabPosition == 'list') {
					this.isMap = false
					this.isList = true
				}
				if (this.tabPosition == 'map') {
					this.isMap = true
					this.isList = false
				}
			},
			/** 获取列表数据 */
			getList() {
				this.loading = true
				//分页
				this.queryParams.page = this.currentPage;
				this.queryParams.limit = this.pageSize;
				console.log(this.queryParams)
				getList(this.queryParams)
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
			// 当某一行被点击时会触发该事件
			handleRowClick(row, column, event) {
				if (column.property == 'code') {
					this.rowID = row.id;
					this.showDview = true;
				}
			},
			//列格式化  如一些字典等
			fieldFormatter(row, column, cellValue) {
				if (column.property === 'sos') {
					return this.selectDictLabel(this.dictData.sosStateDict, row.sos)
				}
				return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
			},
			//按钮操作权限
			canHandle(type) {
				return true;
				//return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
			},
			/**
			 * 字段排序
			 */
//			sortChange(column, prop, order) {
//				this.currentPage = 1
//				this.sortData = column
//				this.getList()
//			},
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
			// 更改每页展示数量
			handleSizeChange(val) {
				Lockr.set('crmPageSizes', val)
				this.pageSize = val
				this.getList()
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.currentPage = 1;
				this.queryParams = {};
				this.getList()
			},
			/**
			 * 轨迹点击
			 */
			trackClick(data) {
				this.rom = data;
				//弹出层表单重置
				this.helmetId = data.id
				this.resetForm('queryForm2')
				this.title2 = '查询定位信息'
				this.open2 = true
				this.currentPage2 = 1
				this.pageSize2 = 5
				this.myChart = null;
				this.executeDates2 = [this.formatterDate(new Date().getTime() - 24 * 60 * 60 * 1000), this.formatterDate(this
					.formatterDate(new Date().getTime()))]
				this.$nextTick(function(){
					
					if(!this.myChart){
						document.getElementById("svgDom").removeAttribute("_echarts_instance_");
						this.myChart = echarts.init(document.getElementById('svgDom'));
					}else{
						this.myChart.setOption({});
					}
					 axios({
					        method:"GET",
					        url:process.env.BASE_API+"build/equipmentHelmet/getSvg/"+data.projectId,
					       }).then(response=>{
					          
							   echarts.registerMap('MacOdrum-LV5-floorplan-web', {
							   	svg: response.data
							   });
							   this.getGpsList()
					       })
					
				})
					
			},
			/**
			 * 新建点击
			 */
			createClick() {
				this.action.type = 'save';
				this.createShow = true
			},
			/**
			 * 修改点击
			 */
			updateClick(id) {
				this.action.type = 'update';
				this.action.id = id;
				this.createShow = true
			},
			/**
			 * 查看点击
			 */
			detailsClick(id) {
				this.action.type = 'details';
				this.action.id = id;
				this.createShow = true
			},
			deleteClick(id) {
				this.$confirm("确定删除选中的数据吗？", '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					})
					.then(() => {
						this.loading = true;
						deleteDetails(id).then(res => {
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
			},
			/**
			 * 更新表高
			 */
			updateTableHeight() {
				var offsetHei = document.documentElement.clientHeight
				var removeHeight = this.selectionList.length > 0 ? 300 : 250
				this.tableHeight = offsetHei - removeHeight
			},
			// 初始化字典数据
			initDicts() {
				this.getDicts('b_equipment_sos_state').then(response => {
					this.dictData.sosStateDict = response.data
				})
			},
			// 查询项目人员
			selectPeopleList() {
				getPeopleList().then(res => {
					this.dictData.peopleLict = res.data
				})
			},


			/** 弹出层搜索按钮操作 */
			handleQuery2() {
				this.currentPage2 = 1
				this.queryParams2.limit = this.pageSize2
				this.getGpsList()
			},

			/** 弹出层重置按钮操作 */
			resetQuery2() {
				this.resetForm('queryForm2')
				this.executeDates2 = [this.formatterDate(new Date().getTime() - 24 * 60 * 60 * 1000), this.formatterDate(
					this.formatterDate(new Date().getTime()))]
				this.handleQuery2()
			},
			// 弹出层更改每页展示数量
			handleSizeChange2(val) {
				this.pageSize2 = val
				this.getGpsList()
			},
			// 弹出层更改当前页数
			handleCurrentChange2(val) {
				this.currentPage2 = val
				this.getGpsList()
			},

			// 格式化时间字符串
			formatterDate(date) {
				let time = new Date(date)
				if (date == null || date == "") return "";
				let Y = time.getFullYear() + "-";
				let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
				let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
				let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
				let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
				let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
				let all = (Y + M + D + h + m + s)
				return all;
			},


			/** 获取轨迹列表数据 */
			getGpsList() {
				this.loading = true
				//分页
				this.queryParams2.helmetId = this.helmetId;
				this.queryParams2.page = this.currentPage2;
				this.queryParams2.limit = this.pageSize2;

				// 执行时间范围
				if (this.executeDates2 != '' && this.executeDates2 != null) {
					this.queryParams2.startTime = this.executeDates2[0]
					this.queryParams2.endTime = this.executeDates2[1]
				} else {
					this.queryParams2.startTime = ''
					this.queryParams2.endTime = ''
				}

				getGpsList(this.queryParams2)
					.then(res => {
						this.dataList2 = res.data.list;
						if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize2) < this.currentPage2 &&
							this.currentPage2 > 1) {
							this.currentPage2 = this.currentPage2 - 1
							this.getGpsList()
						} else {
							this.total2 = res.data.totalRow
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
				selectActionTrack(this.queryParams2)
					.then(res => {
						if(res.data.length == 0){
							res.data = [[0,0]]
						}
						
						var option = {
							title: {
								text: this.rom.projectName,
								left: 'center',
								bottom: 10
							},
							tooltip: {},
							geo: {
								map: 'MacOdrum-LV5-floorplan-web',
								roam: true,
								emphasis: {
									itemStyle: {
										color: undefined
									},
									label: {
										show: false
									}
								}
							},
							series: [{
								name: this.rom.peopleName,
								type: 'lines',
								coordinateSystem: 'geo',
								geoIndex: 0,
								emphasis: {
									label: {
										show: false
									}
								},
								polyline: true,
								lineStyle: {
									color: '#c46e54',
									width: 5,
									opacity: 1,
									type: 'dotted'
								},
								effect: {
									show: true,
									period: 8,
									color: '#a10000',
									constantSpeed: 80,
									trailLength: 0,
									symbolSize: [20, 12],
									symbol: 'path://M35.5 40.5c0-22.16 17.84-40 40-40s40 17.84 40 40c0 1.6939-.1042 3.3626-.3067 5H35.8067c-.2025-1.6374-.3067-3.3061-.3067-5zm90.9621-2.6663c-.62-1.4856-.9621-3.1182-.9621-4.8337 0-6.925 5.575-12.5 12.5-12.5s12.5 5.575 12.5 12.5a12.685 12.685 0 0 1-.1529 1.9691l.9537.5506-15.6454 27.0986-.1554-.0897V65.5h-28.7285c-7.318 9.1548-18.587 15-31.2715 15s-23.9535-5.8452-31.2715-15H15.5v-2.8059l-.0937.0437-8.8727-19.0274C2.912 41.5258.5 37.5549.5 33c0-6.925 5.575-12.5 12.5-12.5S25.5 26.075 25.5 33c0 .9035-.0949 1.784-.2753 2.6321L29.8262 45.5h92.2098z'
								},
								data: [{
									coords: res.data
								}]
							}]
						};
						this.myChart.setOption(option);
						this.loading = false
					})
					.catch(() => {
						this.loading = false
					})
			},
			/*活动轨迹处置*/


		}
	}
</script>

<style lang="scss" scoped>
	//表头
	.table-head-container {
		//border-bottom: 1px solid #e6e6e6;
		border-top: 1px solid #e6e6e6;
		background-color: white;
	}

	.th-container {
		font-size: 13px;
		height: 50px;
		padding: 0 20px;

		.scene-select {
			width: 180px;

			/deep/ .el-input__inner {
				cursor: pointer;
			}
		}

		.filter-button {
			margin-left: 20px;

			/deep/ i {
				font-size: 14px;
				margin-right: 5px;
			}
		}
	}

	/** 场景和筛选 */
	.condition_title {
		cursor: pointer;
	}

	.condition_title:hover {
		color: $xr-color-primary;
	}

	.m-arrow {
		margin: 0 8px;
	}

	.c-filtrate {
		margin: 0 10px 0 30px;
		width: 12px;
	}

	/** 勾选操作 */
	.selection-bar {
		font-size: 12px;
		height: 50px;
		padding: 0 20px;
		color: #777;

		.selected—title {
			flex-shrink: 0;
			padding-right: 20px;
			color: #333;

			.selected—count {
				color: $xr-color-primary;
			}
		}
	}

	.selection-items-box {
		overflow-x: auto;
		overflow-y: hidden;
		padding: 0 15px;

		.el-button {
			color: #666;
			background-color: #f9f9f9;
			border-color: #e5e5e5;
			font-size: 12px;
			height: 28px;
			border-radius: 4px;

			/deep/ i {
				font-size: 12px;
				margin-right: 5px;
			}
		}

		.el-button--primary:hover {
			background: $xr-color-primary;
			border-color: $xr-color-primary;
			color: #ffffff;
		}

		.el-button+.el-button {
			margin-left: 15px;
		}
	}

	//页面头部
	.c-container {
		height: 60px;
		position: relative;

		//z-index: 100;
		.title-icon {
			width: 30px;
			height: 30px;
			margin-right: 10px;
		}

		.title {
			float: left;
			width: auto;
			padding-left: 28px;
			font-size: 16px;
			font-weight: 600;
			margin-top: 15px;
		}

		.sc-container {
			width: 300px;
			margin: -16px 0 0 -150px;
			position: absolute;
			left: 50%;
			top: 50%;
		}

		.el-input {
			/deep/ .el-input-group__append {
				background-color: $xr-color-primary;
				border-color: $xr-color-primary;
				color: white;
			}
		}

		.right-container {
			margin-right: -10px;
			float: right;
			margin: 15px 20px 0 0;
			position: relative;

			.dup-check-btn,
			.xr-btn--orange {
				margin-left: 0;
				margin-right: 10px;
			}

			.right-item {
				float: left;
				margin-right: 10px;
				padding: 8px 10px;
				font-size: 13px;
				border-radius: 2px;
			}
		}
	}

	@import '@/views/styles/table.scss';
</style>
