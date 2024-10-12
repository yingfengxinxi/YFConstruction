<template>
	<div>
		<!--<div class="c-container">
      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
        :src="titleIcon"
        class="title-icon">交换机管理
      </flexbox>
    </div>-->
		<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
			style="padding: 20px 20px 0px 20px;background-color: white;border-top: 1px solid #e6e6e6;">
			<el-form-item label="检查评定项目" prop="securityEvaluationId">
				<el-select v-model="queryParams.securityId" clearable placeholder="请选择"
					style="width: 160px">
					<el-option v-for="dict in securityEvaluationOptions" :key="dict.dictValue"
						:label="dict.dictLabel" :value="dict.dictValue" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
			<el-form-item style="float: right">
				<div class="right-container">
					<el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary"
						@click="createClick">新建
					</el-button>
					<el-dropdown v-if="moreTypes.length > 0" trigger="click" @command="handleTypeDrop">
						<el-button icon="el-icon-more" />
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item v-for="(item, index) in moreTypes" :key="index"
								v-if="canHandle(item.type)" :icon="item.icon | wkIconPre" :command="item.type">
								{{ item.name }}
							</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
			</el-form-item>
		</el-form>
		<!-- 头部：搜索、操作 end -->
		<div class="table-head-container">
			<flexbox v-if="selectionList.length > 0" class="selection-bar">
				<div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
				<flexbox class="selection-items-box">
					<el-button v-for="(item, index) in selectionHandleItems" v-if="canHandle(item.type)"
						:icon="item.icon | wkIconPre" :key="index" type="primary"
						@click.native="handleTypeDrop(item.type)">{{ item.name }}
					</el-button>
				</flexbox>
			</flexbox>
		</div>
		<div v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限" class="crm-container">
			<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight"
				:cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border"
				use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick"
				@sort-change="sortChange" @expand-change="expandChange">
				<el-table-column type="expand">
					<template slot-scope="props">
						<div class="expand_div">
						<el-table :data="tableData" stripe style="width: 100%">
							<el-table-column prop="securityEvaluationName" label="评定项目" align="center">
							</el-table-column>
							<el-table-column prop="securityEvaluationSubitem" label="评定项目" align="center">
							</el-table-column>
							<el-table-column   prop="shouldScore" label="应得分数"  align="center">
							</el-table-column>
							<el-table-column label="扣减分数" align="center">
								<template slot-scope="scope">
									<el-input  @change="(val) => modify(val, scope.row)"   v-model="tableData[scope.$index].mark"  placeholder="扣减分数"></el-input>
								</template>
							</el-table-column>
						</el-table>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="序号" type="index" width="70" />
				<el-table-column v-for="(item, index) in fieldList" :key="index" :prop="item.prop" :label="item.label"
					:width="item.width" :formatter="fieldFormatter" :sortable="false" :align="item.align"
					align="center">

				</el-table-column>
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" icon="el-icon-delete"
							@click="deleteClick(scope.row.securityId)">删除
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-printer" @click="informClick(scope.row.securityId)">
							自评报表
						</el-button>
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
		<!-- 新建 -->
		<create v-if="createShow" :businessType="businessType" :action="action" @close="createShow = false"
			@save-success="getList" />
	</div>
</template>

<script>
	import Lockr from 'lockr' //分页

	import Create from './Create'

	import {
		getList,
		delTrueByIds,
		getEvaluateList,
		queryPrintTemplateByType,
		getPrintData,
		update
	} from "../api/selfEvaluate.js";

	import DiyDict from "@/components/common/Dict/diy";

	export default {
		/** 交换机列表 */
		name: 'ExchangesIndex',
		components: {
			Create,
			DiyDict
		},
		data() {
			return {
				businessType: 10000,
				loading: false, // 加载动画
				/** 页面头部 */
				inputContent: '', //查询值
				moreTypes: [], //更多操作
				/** 选中时操作 */
				selectionHandleItems: [{
					name: '删除',
					type: 'delete',
					icon: 'delete'
				}, ],
				securityEvaluationOptions: [],
				/** 列表 */
				tableHeight: document.documentElement.clientHeight - 245, // 表的高度
				list: [],
				tableData: [],
				fieldList: [ //表头
					{
						prop: "securityName",
						label: "自评项目",
					},
					{
						prop: "evaluateMonth",
						label: "自评月份",
					},
					{
						prop: "mark",
						label: "总分",
					},
					{
						prop: "createTime",
						label: "创建时间",
					},
				],
				sortData: [], // 字段排序
				currentPage: 1,
				pageSize: Lockr.get('crmPageSizes') || 15,
				pageSizes: [15, 30, 60, 100],
				total: 0,
				queryParams: {},
				/** 新增*/
				createShow: false,
				/***/
				action: {
					type: 'save',
					data: null
				},
				/** 勾选行 */
				selectionList: [], // 勾选数据 用于全局导出
				/** 字典项 */
				typeDict: {
					"fanout": "扇形（FANOUT）",
					"direct": "直连（DIRECT）",
					"topic": "通配（TOPIC）",
					"headers": "头部（HEADERS）"
				},
				yesOrNoDict: {
					"1": "是",
					"0": "否"
				}
			}
		},
		computed: {
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
			this.getDicts('b_security_evaluation').then(response => {
				this.securityEvaluationOptions = response.data
			})
			//获取列表数据
			this.getList();
		},
		methods: {

			/** 获取列表数据 */
			getList() {
				this.loading = true
				//分页
				this.queryParams.page = this.currentPage;
				this.queryParams.limit = this.pageSize;
				//排序
				if (this.sortData.length > 0) {
					var orders = [];
					this.sortData.forEach(function(item, index) {
						var one = {};
						one.prop = item.prop;
						one.asc = item.order == 'ascending';
						orders.push(orders);
					})
					this.queryParams.orders = orders;
				}
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
			/**
			 *更多操作点击
			 */
			handleTypeDrop(command) {
				console.log(command)
				if (command == 'delete') { //删除
					this.$confirm("请谨慎操作：是否确认删除？", '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						})
						.then(() => {
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

				} else if (command == 'import') { //导入

				}
			},
			// 当某一行被点击时会触发该事件
			handleRowClick(row, column, event) {
				if (column.property == 'planName') {
					this.rowID = row.id;
					this.showDview = true;
				}
			},
			//列格式化  如一些字典等
			fieldFormatter(row, column, cellValue) {
				if (column.property == 'evaluateMonth') {
					return row[column.property].substring(0,4);
				}
				if (column.property == 'durability' ||
					column.property == 'autoDelete' ||
					column.property == 'internal' ||
					column.property == 'isSys') {
					return this.yesOrNoDict[row[column.property]];
				}
				return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
			},
			//按钮操作权限
			canHandle(type) {
				return true;
			},
			/**
			 * 字段排序
			 */
			sortChange(column, prop, order) {
				this.currentPage = 1
				this.sortData = column
				this.getList()
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
				if (column.property === 'planName') {
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
				this.queryParams = {};
				this.getList()
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
			deleteClick(id) {
				this.$confirm("请谨慎操作：是否确认删除？", '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					})
					.then(() => {
						delTrueByIds(id).then(res => {
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
			expandChange(row) {
				this.loading = true
				getEvaluateList(row.securityId)
					.then(res => {
						this.tableData = res.data
						this.loading = false
					})
					.catch(() => {
						this.loading = false
					})
			},
			informClick(id) {
				//组装数据
				getPrintData(id).then(printData => {
					queryPrintTemplateByType(10040).then(res => {
						let templateId = res.data.templateId
						//Copy 进行打印
						var params = {
							templateId: templateId,
							dataId: 2,
							printData: printData.data
						}
						console.log(params)
						this.$router.push({
							path: this.$route.path + '/print',
							query: {
								printParams: params,
							}
						})
					})
				})
			},
			modify(val,row){
				var data = {id:row.id,mark:val}
				update(data)
					.then(res => {
					
					})
			},
			/**
			 * 更新表高
			 */
			updateTableHeight() {
				var offsetHei = document.documentElement.clientHeight
				var removeHeight = this.selectionList.length > 0 ? 370 : 320
				this.tableHeight = offsetHei - removeHeight
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
	/deep/.el-form-item__label{
		width: 120px !important;
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
	.expand_div {
	  border: solid 1px #e6e6e6;
	  /deep/ .el-table__empty-block {
	    width: 100% !important;
	  }
	}
	@import '@/views/styles/table.scss';
</style>
