<template>
	<div class="main">
		<div class="main-body">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="分部分项" prop="subItemWorksParts">
					<el-select v-model="queryParams.subItemWorksParts" placeholder="请选择" clearable size="small" style="width: 160px">
						<el-option v-for="dict in subItemWorksPartsOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
					</el-select>
				</el-form-item>
				<el-form-item label="风险类型" prop="riskType">
					<el-input v-model="queryParams.riskType" placeholder="请输入风险类型" clearable size="small" @keyup.enter.native="getList" />
				</el-form-item>
				<el-form-item label="风险分级/标识" prop="riskLevelIdent" label-width="120px">
					<el-select v-model="queryParams.riskLevelIdent" placeholder="请选择" clearable size="small" style="width: 160px">
						<el-option v-for="dict in riskLevelIdentOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
					</el-select>
				</el-form-item>
				<el-form-item label="发布时间" prop="sjtbDate">
					<el-date-picker class="timecom" v-model="sjtbDate" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="发布时间起" end-placeholder="发布时间至" :picker-options="pickerOptions">
					</el-date-picker>
				</el-form-item>
				<el-form-item style="float: right">
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
					<el-button type="primary" icon="el-icon-printer" size="mini" @click="exportFile(1)">导出</el-button>
					<el-button class="xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
				</el-form-item>
			</el-form>
			<div class="table-head-container">
				<flexbox v-if="selectionList.length > 0" class="selection-bar">
					<div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
					<flexbox class="selection-items-box">
						<el-button v-for="(item, index) in selectionHandleItems" v-if="canHandle(item.type)" :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
						</el-button>
					</flexbox>
				</flexbox>
			</div>
			<el-table v-loading="loading" id="crm-table"  :data="dataList" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange" @selection-change="handleSelectionChange">
				<!-- <el-table v-loading="loading" :data="dataList"> -->
				<!-- <el-table-column show-overflow-tooltip type="selection" align="center" width="55" /> -->
				<el-table-column label="序号" align='center' type="index" width="60" />
				<el-table-column label="分部分项" :formatter="SubItemWorksPartsFc" align="center" prop="subItemWorksParts" show-overflow-tooltip/>
				<el-table-column label="风险类型" align="center" prop="riskType" show-overflow-tooltip/>
				<el-table-column label="风险辨识点" align="center" prop="riskIdentPoint" show-overflow-tooltip/>
				<el-table-column label="事故类型" align="center" prop="accidentType" show-overflow-tooltip/>
				<el-table-column label="风险分级/标识" :formatter="riskLevelIdentFc" align="center" prop="riskLevelIdent" show-overflow-tooltip/>
				<el-table-column label="防范措施" align="center" prop="preventiveMeasures" show-overflow-tooltip/>
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
						<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
			<!-- 新增或修改参数配置对话框 -->
			<el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
				<el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
					<el-row>
						<el-col :span="12">
							<el-form-item label="分部分项" prop="subItemWorksParts" class="add-form-input">
								<el-select v-model="form.subItemWorksParts" placeholder="请选择" clearable size="small" style="width: 300px">
									<el-option v-for="dict in subItemWorksPartsOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="风险类型" prop="riskType" class="add-form-input">
								<el-input v-model="form.riskType" placeholder="请输入风险类型" />
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="风险辨识点" prop="riskIdentPoint">
								<el-input type="textarea" :rows="2" v-model="form.riskIdentPoint" placeholder="风险辨识点"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="事故类型" prop="accidentType" class="add-form-input">
								<el-input v-model="form.accidentType" placeholder="请输入事故类型" />
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="风险分级/标识" label-width="130px" prop="riskLevelIdent" class="add-form-input">
								<el-select v-model="form.riskLevelIdent" placeholder="请选择" clearable size="small" style="width: 300px">
									<el-option v-for="dict in riskLevelIdentOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="防范措施" prop="preventiveMeasures">
								<el-input type="textarea" :rows="2" v-model="form.preventiveMeasures" placeholder="请输入防范措施"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="工作依据" prop="workingBasis">
								<el-input type="textarea" :rows="4" v-model="form.workingBasis" placeholder="请输入工作依据"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="submitForm">确 定</el-button>
					<el-button @click="cancel">取 消</el-button>
				</div>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	// import Reminder from '@/components/Reminder'
	import XrHeader from '@/components/XrHeader'
	import request from '@/utils/request'
	import SelectCol from '@/views/admin/components/selectCol'
	import WkUploadFile from '@/components/CreateCom/WkUploadFile'
	import { guid } from '@/utils'
	import FilesList from '@/components/common/FilesList' // 附件列表
	//	import vPinyin from './vuepy.js';
	import mergeList from '@/views/build/safety/safetyRiskControlsTemplate/mergeList';
	//导出
	import { exportFile } from "@/api/build/exportFile";
	import { downloadExcelWithResData } from '@/utils/index';
	export default {
		name: 'SafetyRiskControls',
		components: {
			// Reminder,
			XrHeader,
			WkUploadFile,
			SelectCol,
			FilesList
		},
		mixins: [mergeList],
		data() {
			return {
				// 遮罩层
				loading: true,
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 显示搜索条件
				showSearch: true,
				// 隐藏设备设施名称
				deviceTypeShow: true,
				// 分页
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 30, 40],
				total: 0,
				// 表格数据
				dataList: [],

				// 附件id
				batchId: '',
				// 工作内容（静态风险：检查内容）（动态风险：工作内容—活动作业内容）
				contentTitle: '工作内容',
				// 标准要求（静态独有：标准要求）（动态独有：风险点描述）
				standardRequireTitle: '标准要求',
				// 弹出层标题
				title: '',
				// 是否显示弹出层
				open: false,
				pickerOptions: {
					shortcuts: [{
						text: '最近一周',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近一个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近三个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
							picker.$emit('pick', [start, end]);
						}
					}]
				},
				sjtbDate: '',
				// 查询参数
				queryParams: {
					page: this.currentPage,
					limit: this.pageSize,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {
					subItemWorksParts: [{
						required: true,
						message: '分部分项不能为空',
						trigger: 'blur'
					}],
					riskType: [{
						required: true,
						message: '风险类型不能为空',
						trigger: 'blur'
					}],
					riskIdentPoint: [{
						required: true,
						message: '风险辨识点不能为空',
						trigger: 'blur'
					}],
					accidentType: [{
						required: true,
						message: '事故类型不能为空',
						trigger: 'blur'
					}],
					riskLevelIdent: [{
						required: true,
						message: '风险分级/标识不能为空',
						trigger: 'change'
					}],
					preventiveMeasures: [{
						required: true,
						message: '防范措施不能为空',
						trigger: 'blur'
					}],
					workingBasis: [{
						required: true,
						message: '工作依据不能为空',
						trigger: 'blur'
					}],
				},

				//新--------------
				subItemWorksPartsOption: [],
				riskLevelIdentOption: [],
				/** 勾选行 */
				selectionList: [], // 勾选数据 用于全局导出
				/** 选中时操作 */
				selectionHandleItems: [{
					name: '删除',
					type: 'delete',
					icon: 'delete'
				}],
				tableHeight: document.documentElement.clientHeight - 250,
				sortData: [], // 字段排序
			}
		},

		created() {
			this.getList()
			//分部分项
			this.getDicts('b_sub_item_works_parts').then(response => {
				this.subItemWorksPartsOption = response.data
			})
			// 风险等级
			this.getDicts('b_risk_level_ident').then(response => {
				this.riskLevelIdentOption = response.data
			})
		},
		mounted() {
			/** 控制table的高度 */
			window.onresize = () => {
				this.updateTableHeight()
			}
		},
		methods: {

			/** 查询数据列表 */
			async getList() {
				this.loading = true
				this.queryParams.page = this.currentPage
				this.queryParams.limit = this.pageSize
				await request({
					url: 'build/safetyRiskControlsTemplate/templateList',
					method: 'post',
					data: this.queryParams,
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(response => {
					this.dataList = response.data.list
					this.total = response.data.totalRow
					this.dataList.forEach((data, index) => {
						data.id = String(data.id)
					})

					this.buildMerge(this.dataList)
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			// 分部分项字典格式化
			SubItemWorksPartsFc(row, column) {
				return this.selectDictLabel(this.subItemWorksPartsOption, row.subItemWorksParts);
			},
			//风险标识字典格式化
			riskLevelIdentFc(row, column) {
				return this.selectDictLabel(this.riskLevelIdentOption, row.riskLevelIdent);
			},
			formatterTime(row, column) {
				let data = row[column.property]
				if(data == null || data == '') return "--";
				return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
			},
			// 取消按钮
			cancel() {
				this.open = false
				this.reset()
			},
			// 表单重置
			reset() {
				this.form = {}
				this.resetForm('form')
			},
			/** 搜索按钮操作 */
			handleQuery() {
				if(this.sjtbDate != null && this.sjtbDate != '') {
					var date = new Date(this.sjtbDate[0])
					var y = date.getFullYear()
					var m = date.getMonth() + 1
					m = m < 10 ? ('0' + m) : m
					var d = date.getDate()
					d = d < 10 ? ('0' + d) : d
					const time = y + '-' + m + '-' + d
					this.queryParams.startTime = time
					var date = new Date(this.sjtbDate[1])
					var y = date.getFullYear()
					var m = date.getMonth() + 1
					m = m < 10 ? ('0' + m) : m
					var d = date.getDate()
					d = d < 10 ? ('0' + d) : d
					const time2 = y + '-' + m + '-' + d
					this.queryParams.endTime = time2
				} else {
					this.queryParams.startTime = ''
					this.queryParams.endTime = ''
				}
				this.queryParams.page = 1
				this.queryParams.limit = this.pageSize
				this.getList()
			},

			/** 重置按钮操作 */
			resetQuery() {
				this.sjtbDate = ''
				//this.resetForm('queryForm')
				this.queryParams = {};
				this.handleQuery()
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.reset()
				this.open = true
				this.title = '新增'
			},
			/** 提交按钮 */
			submitForm: function() {
				this.$refs['form'].validate(valid => {
					if(valid) {
						if(this.form.id != undefined) {
							request({
								url: 'build/safetyRiskControlsTemplate/templateSaveOrUpdate',
								method: 'post',
								data: this.form,
								headers: {
									'Content-Type': 'application/json;charset=UTF-8'
								}
							}).then(response => {
								if(response.code === 0) {
									this.msgSuccess('新增成功')
									this.open = false
									this.getList()
								}
							})
						} else {
							request({
								url: 'build/safetyRiskControlsTemplate/templateSaveOrUpdate',
								method: 'post',
								data: this.form,
								headers: {
									'Content-Type': 'application/json;charset=UTF-8'
								}
							}).then(response => {
								if(response.code === 0) {
									this.msgSuccess('新增成功')
									this.open = false
									this.getList()
								}
							})
						}
					}
				})
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				this.pageSize = val
				this.getList()
			},
			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val
				this.getList()
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				console.log(row.id);
				const id = [row.id] || this.ids

				this.$confirm('是否确认删除该数据项?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return request({
						url: 'build/safetyRiskControlsTemplate/templateDel',
						method: 'post',
						data: id,
						headers: {
							'Content-Type': 'application/json;charset=UTF-8'
						}
					}).then(() => {
						this.getList()
						this.msgSuccess('删除成功')
					}).catch(function() {
						this.msgSuccess('删除失败')
					})
				}).catch(function() {})
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset()
				const pipeId = row.id
				request({
					url: 'build/safetyRiskControlsTemplate/getById/' + pipeId,
					method: 'get'
				}).then(response => {
					this.form = response.data
					this.open = true
					this.title = '修改'
				})
			},
			/**
			 *更多操作点击
			 */
			handleTypeDrop(command) {
				if(command == 'delete') { //删除
					this.$confirm("确定删除选中的数据吗？", '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						})
						.then(() => {
							const ids = this.selectionList.map(item => item['id'])
							this.loading = true;
							request({
								url: 'build/safetyRiskControlsTemplate/templateDel',
								method: 'post',
								data: ids,
								headers: {
									'Content-Type': 'application/json;charset=UTF-8'
								}
							}).then(res => {
								console.log(res)
								if(res.code == 0) {
									this.$message.success("删除成功");
									this.getList();
								} else {
									this.$message.error(res.msg);
								}
								this.loading = false;
							}).catch(() => {
								this.loading = false;
							})

						}).catch(() => {})
				} else if(command == 'export') { //导出

				} else if(command == 'import') { //导入

				}
			},
			/** 勾选操作 */
			// 当选择项发生变化时会触发该事件
			handleSelectionChange(val) {
				this.selectionList = val // 勾选的行
				this.updateTableHeight();
			},
			/**
			 * 更新表高
			 */
			updateTableHeight() {
				var offsetHei = document.documentElement.clientHeight
				var removeHeight = this.selectionList.length > 0 ? 300 : 250
				this.tableHeight = offsetHei - removeHeight
			},
			canHandle(type) {
				return true;
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
				if(column.property === 'code') {
					return 'can-visit--underline can-visit--bold'
				}
			},
			headerCellClassName({
				row,
				column,
				rowIndex,
				columnIndex
			}) {
				return ''
			},
			// 当某一行被点击时会触发该事件
			handleRowClick(row, column, event) {
				if(column.property == 'code') {
					this.rowID = row.id;
					this.showDview = true;
				}
			},
			/**
			 * 字段排序
			 */
			sortChange(column, prop, order) {
				this.currentPage = 1
				this.sortData = column
				this.getList()
			},
			/**
			 *
			 * 使用导出需引入js文件如下：
			 * import { exportFile } from "@/api/build/exportFile";
			 * import { downloadExcelWithResData } from '@/utils/index';
			 * 方法入参说明： type=1 全部导出  type=2 部分导出
			 * 
			 */
			async exportFile(type) {
				this.loading = true;
				let data = '';
				if(type == 1) {
					//请求不分页的列表数据 需替换为当前页面列表数据请求url
					var objString = JSON.stringify(this.queryParams);
					var obj2 = JSON.parse(objString);
					let reqParam = obj2;
					reqParam.pageType = 0
					await request({
						url: 'build/safetyRiskControlsTemplate/templateList',
						method: 'post',
						data: reqParam,
						headers: {
							'Content-Type': 'application/json;charset=UTF-8'
						}
					}).then(res => {
						data = res.data.list
					}).catch(() => {
						this.loading = false
					})

				} else if(type == 2) {
					//获取当前选中行的数据
					data = this.selectionList;
				}
				/**
				 * 整理数据
				 * param中result是需要导出的数据(必填)
				 * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)
				 * 如果有字典需转换请添加dictType类型，
				 * 如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)，
				 * dictList中字段名必须严格设置为dictLabel，dictValue，如果不是，请处理
				 * param中title是导出Excel的一级表头(必填)
				 * 注意：fieldList中字段值必须和列表中字段值相对应！！！
				 */
				let fieldList = [ //表头
					{
						prop: "subItemWorksParts",
						label: "分部分项",
						dictType: 'b_sub_item_works_parts'
					},
					{
						prop: "riskType",
						label: "风险类型"
					},
					{
						prop: "riskIdentPoint",
						label: "风险辨识点"
					},
					{
						prop: "accidentType",
						label: "事故类型"
					},
					{
						prop: "riskLevelIdent",
						label: "风险分级/标识",
						dictType: "b_risk_level_ident"
					},
					{
						prop: "preventiveMeasures",
						label: "防范措施"
					},
				]
				let param = {};
				param.result = data;
				param.fieldList = fieldList;
				param.title = "安全风险管控模板";
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
	.main {
		height: 100%;
	}
	
	/deep/ .xr-header {
		padding: 15px 30px;
	}
	
	.main-body {
		background-color: white;
		/*border-top: 1px solid $xr-border-line-color;*/
		border-bottom: 1px solid $xr-border-line-color;
	}
	
	.main-table-header-button {
		float: right;
		margin-right: 20px;
		margin-top: 10px;
	}
	
	.add-form::after {
		content: "";
		display: block;
		clear: both;
	}
	
	.link-type,
	.link-type:focus {
		color: #337ab7;
		cursor: pointer;
	}
	
	.link-type:focus:hover,
	.link-type:hover {
		color: #20a0ff;
	}
	
	.el-table {
		border-top: 1px solid $xr-border-line-color;
	}
	
	/deep/ .special .el-form-item__label {
		width: 100px !important;
	}
	
	.timecom {
		margin-top: 4px;
		height: 32px;
	}
	
	.timecom span {
		width: 20px;
	}
	
	.table-head-container {
		//border-bottom: 1px solid #e6e6e6;
		border-top: 1px solid #e6e6e6;
		background-color: white;
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
</style>