<template>
	<div>
		<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
			style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<el-form-item label="项目名称">
				<el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable size="small"
					@keyup.enter.native="getList" />
			</el-form-item>
			<el-form-item label="设备名称">
				<el-input v-model="queryParams.equipmentName" placeholder="请输入设备名称" clearable size="small"
					@keyup.enter.native="getList" />
			</el-form-item>
			<el-form-item label="监测时间">
				<el-date-picker v-model="executeDates" type="datetimerange" range-separator="至" start-placeholder="开始日期"
					end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']">
				</el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="searchList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>
		<!-- 头部：搜索、操作 end -->
		<div class="table-head-container">

			<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight"
				:cell-class-name="cellClassName" class="n-table--border" use-virtual stripe border highlight-current-row
				style="width: 100%" @sort-change="sortChange">
				<el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
				<el-table-column v-for="(item, index) in fieldList" :key="index" :prop="item.prop" :label="item.label"
					:width="item.width" :formatter="fieldFormatter" sortable="custom" show-overflow-tooltip align="center">

				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize"
					:total="total" :pager-count="5" class="p-bar" background
					layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" />
			</div>
		</div>

		<!-- 批量导入 -->
		<bulk-import-user :show="bulkImportShow" :param="param" @close="bulkImportClose" />
	</div>
</template>

<script>
	import {
		mapGetters
	} from 'vuex' //权限
	import Lockr from 'lockr' //分页
	import {
		selectTowerEffectPage
	} from "../api/monitorHistoryData";
	import BulkImportUser from '@/components/importModel'
	import {sortColumn } from '@/utils'
	export default {
		/** DEMO列表 */
		name: '',
		components: {
			BulkImportUser,
		},
		data() {
			return {
				conName: '',
				loading: false, // 加载动画
				/** 列表 */
				tableHeight: document.documentElement.clientHeight - 280, // 表的高度
				list: [],
				fieldList: [ //表头
					{
						prop: "projectName",
						label: "项目名称",
						width: "180"
					},
					{
						prop: "assetName",
						label: "塔机名称",
						width: "120"
					},
					{
						prop: "assetCode",
						label: "塔机编号",
						width: "120"
					},
					{
						prop: "startTime",
						label: "开始时间",
						width: "200"
					},
					{
						prop: "endTime",
						label: "结束时间",
						width: "200"
					},
					{
						prop: "weight",
						label: "本次循环最大钓重（T）",
						width: "200"
					},
					{
						prop: "maxPercent",
						label: "本次循环最大力矩（T·M）",
						width: "200"
					},
					{
						prop: "maxHeight",
						label: "最大高度（米）",
						width: "120"
					},
					{
						prop: "minHeight",
						label: "最小高度（米）",
						width: "120"
					},
					{
						prop: "maxRadius",
						label: "最大幅度（米）",
						width: "120"
					},
					{
						prop: "minRadius",
						label: "最小幅度（米）",
						width: "120"
					},
					{
						prop: "startAngle",
						label: "起吊点角度（米）",
						width: "120"
					},
					{
						prop: "endAngle",
						label: "卸掉点角度（米）",
						width: "120"
					},
					{
						prop: "liftingPointHeight",
						label: "起掉点高度（米）",
						width: "120"
					},
					{
						prop: "liftingPointHeightUnload",
						label: "卸掉点高度（米）",
						width: "120"
					},
					{
						prop: "liftingPointRadius",
						label: "起掉点幅度（米）",
						width: "120"
					},
					{
						prop: "liftingPointRadiusUnload",
						label: "卸掉点幅度（米）",
						width: "120"
					},
				],
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 40, 80, 100],
				total: 0,
				// 执行时间范围
				executeDates: '',
				queryParams: {},
				// 字典数据或关联下拉框数据
				dictData: {
					// 监测项类型字典项
					monitorTypeDict: [],
					// 监测项子类型字典项
					monitorSonTypeDict: [],
				},
				// 菜单类型
				monitorType: '',
				// 监测项子类型
				envMonitorSonType: '',
				// 批量导入
				bulkImportShow: false,
				param: {}
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
		created() {},
		mounted() {
			//获取列表数据
			this.initDicts()
			this.initData(this.$route)
			// 初始化时间，默认为今天
		},
		watch: {
			//监听页面切换
			'$route'(to, from) {
				//TODO 重新加载数据
				this.initData(to)
			}
		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			// 初始化数据
			initData(to) {
				this.currentPage = 1;
				this.pageSize = 10;
				this.executeDates = ''
				this.showDownload = false;
				this.getList()
			},
			searchList() {
				this.currentPage = 1;
				this.total = 0;
				this.getList()
			},
			/** 获取列表数据 */
			getList() {
				this.loading = true
				//分页
				this.queryParams.page = this.currentPage;
				this.queryParams.limit = this.pageSize;

				// 执行时间范围
				if (this.executeDates != null && this.executeDates != '') {
					this.queryParams.startTime = this.executeDates[0]
					this.queryParams.endTime = this.executeDates[1]
				} else {
					this.queryParams.startTime = ''
					this.queryParams.endTime = ''
				}

				selectTowerEffectPage(this.queryParams)
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
			/**
			 * 批量导入
			 */
			bulkImportClick() {
				let lists = [
					"监测点编号(*)",
					"监测项名称(*)",
					"监测值(*)",
					"监测时间(*) 时间格式如下yyyy-MM-dd HH:mm:ss"
				]
				let colNameCodes = [
					"pointCode",
					"pointName",
					"value",
					"monitorTime"
				]

				//示例数据
				let dataLists = [
					"XLPT-001",
					"混凝土支撑轴力",
					"33",
					"2020-05-11 10:22:22"
				]


				this.param = {
					title: "监测项历史数据导入模板(*)为必填项 监测项名称需与对应监测点相同",
					excelTitle: "监测项历史数据导入模板",
					colNameCodes: colNameCodes,
					colNames: lists,
					sampleData: dataLists,
					url: 'build/equipmentMonitorData/excelImport'
				}

				this.bulkImportShow = true
			},
			//列格式化  如一些字典等
			fieldFormatter(row, column, cellValue) {
				if (column.property === 'liftingDirection') {
					return this.selectDictLabel(this.dictData.monitorTypeDict, row.liftingDirection)
				}
				//console.log(row, column)
				return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
			},

			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val
				this.getList()
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				Lockr.set('crmPageSizes', val)
				this.pageSize = val
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
				if (column.property === 'value') {
					if ((row.alarmUpper != null && parseInt(row.value) >= row.alarmUpper) ||
						(row.alarmLower != null && parseInt(row.value) <= row.alarmLower)) {
						return 'column-font-color-red'
					}
				}
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.currentPage = 1;
				this.queryParams = {
					dataType: '1'
				}
				this.executeDates = ''
				this.getList()
			},
			// 初始化字典数据
			initDicts() {
				this.getDicts('b_lift__run_status').then(res => {
					this.dictData.monitorTypeDict = res.data
				})
			},
			// 关闭导入组件
			bulkImportClose() {
				this.bulkImportShow = false
				this.getList()
			},
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

	/deep/ .column-font-color-red .el-tooltip {
		color: red !important;
	}

	@import '@/views/styles/table.scss';
</style>
