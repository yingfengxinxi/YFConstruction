<template>
	<div>
		<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<el-form-item label="巡检日期" prop="startCheckDate">
				<el-date-picker v-model="queryParams.startCheckDate" type="date" style="width: 140px" size="small" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期">
				</el-date-picker>&nbsp;~
				<el-date-picker v-model="queryParams.endCheckDate" type="date" style="width: 140px" size="small" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期">
				</el-date-picker>
			</el-form-item>
			<el-form-item label="巡检人员" prop="qualCheckPersonName">
				<el-input v-model="queryParams.qualCheckPersonName" placeholder="请输入巡检人员" clearable size="mini" @keyup.enter.native="getList" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
			<el-form-item style="float: right">
				<div class="right-container">
					<el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary" @click="createClick">新建
					</el-button>
<!--					<el-button class="xr-btn&#45;&#45;orange" icon="el-icon-plus" type="primary" @click="exportFile(1)">导出-->
<!--					</el-button>-->
				</div>
			</el-form-item>
		</el-form>
		<!-- 头部：搜索、操作 end -->
		<div class="table-head-container">
			<flexbox v-if="selectionList.length > 0" class="selection-bar">
				<div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
				<flexbox class="selection-items-box">
					<el-button v-for="(item, index) in selectionHandleItems" v-if="canHandle(item.type)" :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
					</el-button>
				</flexbox>
			</flexbox>
		</div>
		<div v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限">
			<el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange" @selection-change="handleSelectionChange">
				<el-table-column show-overflow-tooltip type="selection" align="center" width="55" />
				<el-table-column fixed label="序号" align='center' type="index" width="60" />
				<el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" :sortable="false" show-overflow-tooltip>

				</el-table-column>
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.checkId)">修改
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-delete" @click="deleteClick(scope.row.checkId)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
		</div>
<!--		<create v-if="createShow" :businessType="businessType" :dictData="dictData" :action="action" @close="createShow = false" @save-success="getList" />-->
	</div>
</template>

<script>
import { mapGetters } from 'vuex' //权限

// import Create from './Create'
import { getList, delTrueByIds } from "./envLevel";
import { exportFile } from "@/api/build/exportFile";
import { downloadExcelWithResData } from '@/utils/index';
export default {
	name: 'envLevel',
	components: {
		// Create
	},
	data () {
		return {
			loading: false, // 加载动画
			/** 页面头部 */
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
			], //更多操作
			/** 选中时操作 */
			selectionHandleItems: [{
				name: '删除',
				type: 'delete',
				icon: 'delete'
			},
			// {
			// 	name: '导出选中',
			// 	type: 'export',
			// 	icon: 'export'
			// }
			],
			/** 列表 */
			tableHeight: document.documentElement.clientHeight - 250, // 表的高度   //搜索有两行时 -310
			list: [],
			fieldList: [ //表头
				{
					prop: "levelName",
					label: "环境等级",
				},
				{
					prop: "min",
					label: "等级开始值",
				},
				{
					prop: "max",
					label: "等级结束值",
				},
				{
					prop: "explain",
					label: "环境等级说明",
				}
			],
			sortData: [], // 字段排序
			currentPage: 1,
			pageSize: 20,
			total: 0,
			queryParams: {},
			/** 新增*/
			createShow: false,
			/***/
			action: {
				type: 'save',
				isReadonly: false,
				data: null
			},
			/** 勾选行 */
			selectionList: [], // 勾选数据 用于全局导出
			/** 控制详情展示 */
			rowID: '', // 行信息
			showDview: false,

		}
	},
	computed: {
		//权限 start
		...mapGetters(['allAuth']),
		//权限 end
		titleIcon () {
			return require(`@/assets/img/crm/customer.png`)
		},
	},
	mounted () {
		this.getList();
	},
	methods: {
		/** 获取列表数据 */
		getList () {
			this.loading = true
			//分页
			this.queryParams.page = this.currentPage;
			this.queryParams.limit = this.pageSize;

			getList(this.queryParams)
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
		/**
		 *更多操作点击
		 */
		handleTypeDrop (command) {
			if (command == 'delete') { //删除
				this.$confirm("确定删除选中的数据吗？", '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
					.then(() => {
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
		//列格式化  如一些字典等
		fieldFormatter (row, column, cellValue) {
			if (column.property === 'qualCheckType') {
				return this.selectDictLabel(this.dictData.checkTypeDict, row.qualCheckType)
			}

			return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
		},
		//按钮操作权限
		canHandle (type) {
			return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
		},
		/**
		 * 字段排序
		 */
		sortChange (column, prop, order) {
			this.currentPage = 1
			this.sortData = column
			this.getList()
		},
		/** 勾选操作 */
		// 当选择项发生变化时会触发该事件
		handleSelectionChange (val) {
			this.selectionList = val // 勾选的行
			this.updateTableHeight();
		},

		// 更改当前页数
		handleCurrentChange (val) {
			this.currentPage = val
			this.getList()
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
		// 更改每页展示数量
		handleSizeChange (val) {
			this.pageSize = val
			this.getList()
		},
		/** 重置按钮操作 */
		resetQuery () {
			this.queryParams = {};
			this.getList()
		},
		/**
		 * 新建点击
		 */
		createClick () {
			this.action.type = 'save';
			this.createShow = true
		},
		/**
		 * 修改点击
		 */
		updateClick (id) {
			this.action.type = 'update';
			this.action.id = id;
			this.createShow = true
		},
		deleteClick (id) {
			this.$confirm("确定删除选中的数据吗？", '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(() => {
					const ids = [id];
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
				.catch(() => { })
		},
		/**
		 * 更新表高
		 */
		updateTableHeight () {
			var offsetHei = document.documentElement.clientHeight
			var removeHeight = this.selectionList.length > 0 ? 300 : 250   //两行 360：310  //一行 300：250
			this.tableHeight = offsetHei - removeHeight
		},
		/**
		 *
		 * 使用导出需引入js文件如下：
		 * import { exportFile } from "@/api/build/exportFile";
		 * import { downloadExcelWithResData } from '@/utils/index';
		 * 方法入参说明： type=1 全部导出  type=2 部分导出
		 * 
		 */
		async  exportFile (type) {
			this.loading=true;
			let data = '';
			if (type == 1) {
				//请求不分页的列表数据
				var objString = JSON.stringify(this.queryParams);
				var obj2 = JSON.parse(objString);
				let reqParam = obj2;
				reqParam.pageType=0
				await getQualCheckList(reqParam)
					.then(res => {
						data = res.data.list;
					})
					.catch(err => {
						// console.log(err);
						this.$message.error("导出数据加载失败");
						this.loading=false;
					})
				// console.log(data);
			} else if (type == 2) {
				//获取当前选中行的数据
				data=this.selectionList;
			}
			/**
			 * 整理数据
			 * param中result是需要导出的数据(必填)
			 * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)，
			 * 如果有字典需转换请添加dictType类型，如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)
			 * param中title是导出Excel的一级表头(必填)
			 * 注意：fieldList中字段值必须和列表中字段值相对应！！！
			 */
			let fieldList= [ //表头
				{ prop: "qualCheckType", label: "巡检类型", dictType:"b_chekc_type" },
				{ prop: "qualCheckDate", label: "巡检日期", },
				{ prop: "qualRepairTimeLimit", label: "整改期限", },
				{ prop: "qualCheckPersonName", label: "巡检人", },
				{ prop: "qualCheckCom", label: "巡检单位", },
				{ prop: "qualDocumentNumber", label: "单据编号", },
				{ prop: "qualDocumentDate", label: "单据日期", },
				{ prop: "qualReceiptStatus", label: "单据状态",dictList:[{"dictLabel":"已复检","dictValue":"1"},{"dictLabel":"未复检","dictValue":"0"}] },
				{ prop: "qualPrincipalName", label: "项目负责人", },
			]
			let param = {};
			param.result = data;
			param.fieldList = fieldList;
			param.title = "导出整合测试";
			await exportFile(param)
				.then(res => {
					// console.log(res);
					downloadExcelWithResData(res);
					this.loading=false;
				})
				.catch(err => {
					// console.log(err)
					this.$message.error("导出文件生成异常");
					this.loading=false;
				})
		}
	}
}
</script>

<style lang="scss" scoped>
/** 勾选操作开始  */
/*需要勾选则加上	*/
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
  .el-button + .el-button {
    margin-left: 15px;
  }
}
/** 勾选操作样式结束 */
</style>