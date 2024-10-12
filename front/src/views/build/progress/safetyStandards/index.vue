<template>
	<div class="main">

		<div class="main-body">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px"
				style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="检查评定项目" prop="securityEvaluationId">
					<el-select v-model="queryParams.securityEvaluationId" clearable placeholder="请选择"
						style="width: 160px">
						<el-option v-for="dict in securityEvaluationOptions" :key="dict.dictValue"
							:label="dict.dictLabel" :value="dict.dictValue" />
					</el-select>
				</el-form-item>


				<el-form-item label="检查项目" prop="securityEvaluationName">
					<el-input v-model="queryParams.securityEvaluationName" placeholder="请输入检查项目" clearable size="small"
						@keyup.enter.native="handleQuery" />
				</el-form-item>

				<el-form-item style="float: right">
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
					<el-button type="primary" @click="bulkImportClick">数据导入</el-button>
					<!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

					<el-button class="xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">
						新建</el-button>
				</el-form-item>
			</el-form>
			<el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
				<el-table-column label="评定表" align="center" prop="securityEvaluationId"
					:formatter="securityEvaluationFormat" sortable="custom" />
				<el-table-column label="检查项目" align="center" prop="securityEvaluationName"  sortable="custom"/>
				<el-table-column label="评定标准" align="center" prop="securityEvaluationStandard" show-overflow-tooltip sortable="custom" />
				<el-table-column label="评定方法" align="center" prop="securityEvaluationFunction" show-overflow-tooltip sortable="custom"/>
				<el-table-column label="排序" align="center" prop="sort" sortable="custom" />
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize"
					:total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper"
					@size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
			<!-- 新增或修改参数配置对话框 -->
			<el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
				<el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
					<el-row>
						<el-col :span="12">
							<el-form-item label="评分表" class="add-form-input label_1" prop="scoringTable">
								<el-select v-model="form.scoringTable" placeholder="请选择" clearable size="small"
									style="width: 160px">
									<el-option v-for="dict in securityEvaluationOptions" :key="dict.dictValue"
										:label="dict.dictLabel" :value="dict.dictValue" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="检查项目"  prop="securityEvaluationName"  class="add-form-input">
								<el-input v-model="form.securityEvaluationName" placeholder="请输入检查项目" />
							</el-form-item>
						</el-col>
						
					</el-row>
					
					<el-row>
						<el-col :span="24">
							<el-form-item label="扣分标准" prop="securityEvaluationStandard" class="add-form-input">
								<el-input v-model="form.securityEvaluationStandard" placeholder="请输入扣分标准" />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="评定方法" prop="securityEvaluationFunction" class="add-form-input">
								<el-input v-model="form.securityEvaluationFunction" placeholder="请输入评定方法" />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="排序" prop="sort" class="add-form-input">
								<el-input v-model="form.sort" placeholder="请输入排序" />
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="应得分" prop="shouldScore" class="add-form-input">
								<el-input v-model="form.shouldScore" placeholder="请输入备注" />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="备注" prop="remarks" class="add-form-input">
								<el-input v-model="form.remarks" placeholder="请输入备注" />
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
		<!-- 批量导入 -->
		<bulk-import-user :show="bulkImportShow" :param="param" @close="bulkImportShow=false" />
	</div>
</template>

<script>
	
	// import Reminder from '@/components/Reminder'
	import XrHeader from '@/components/XrHeader'
	import request from '@/utils/request'
	import SelectCol from '@/views/admin/components/selectCol'
	import WkUploadFile from '@/components/CreateCom/WkUploadFile'
	import {
		guid,sortColumn
	} from '@/utils'
	import FilesList from '@/components/common/FilesList' // 附件列表
	import BulkImportUser from '@/components/importModel'
	//	import vPinyin from './vuepy.js';

	export default {
		name: 'ProgressSecurity',
		components: {
			// Reminder,
			XrHeader,
			WkUploadFile,
			SelectCol,
			BulkImportUser,
			FilesList
		},
		data() {
			return {
				// 遮罩层
				loading: true,
				tableHeight: document.documentElement.clientHeight - 280, // 表的高度
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 显示搜索条件
				showSearch: true,
				// 分页
				param:{},
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 30, 40],
				// 批量导入
				bulkImportShow: false,
				total: 0,
				// 表格数据
				dataList: [],
				securityEvaluationOptions: [],
				// 附件id
				batchId: '',
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
					limit: this.pageSize
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {
					scoringTable: [{
						required: true,
						message: '必填项不能为空',
						trigger: 'blur'
					}],
					securityEvaluationName: [{
						required: true,
						message: '必填项不能为空',
						trigger: 'blur'
					}],
					securityEvaluationSubitem: [{
						required: true,
						message: '必填项不能为空',
						trigger: 'blur'
					}],
					securityEvaluationStandard: [{
						required: true,
						message: '必填项不能为空',
						trigger: 'blur'
					}],
					securityEvaluationFunction: [{
						required: true,
						message: '必填项不能为空',
						trigger: 'blur'
					}],
					sort: [{
						required: true,
						message: '排序不能为空',
						trigger: 'blur'
					}],
					shouldScore: [{
						required: true,
						message: '排序不能为空',
						trigger: 'blur'
					}]

				}
			}
		},

		created() {
			this.getList()
			this.getDicts('b_safety_standards').then(response => {
				this.securityEvaluationOptions = response.data
			})
		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			/** 查询数据列表 */
			getList() {
				this.loading = true
				this.queryParams.page = this.currentPage
				this.queryParams.limit = this.pageSize

				request({
					url: 'build/progressSafetyProduction/list',
					method: 'post',
					data: this.queryParams,
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(response => {
					this.dataList = response.data.list
					this.total = response.data.totalRow
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			formatterTime(row, column) {
				let data = row[column.property]
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
				this.currentPage = 1
				this.queryParams.limit = this.pageSize
				this.getList()
			},

			/** 重置按钮操作 */
			resetQuery() {
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
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset()
				const pipeId = row.id || this.ids
				request({
					url: 'build/progressSafetyProduction/selectById/' + pipeId,
					method: 'get'
				}).then(response => {
					this.form = response.data
					this.batchId = response.data.batchId
					this.open = true
					this.title = '修改'
				})
			},
			/** 提交按钮 */
			submitForm: function() {
				this.$refs['form'].validate(valid => {
					if (valid) {
						if (this.form.id != undefined) {
							request({
								url: 'build/progressSafetyProduction/edit',
								method: 'post',
								data: this.form,
								headers: {
									'Content-Type': 'application/json;charset=UTF-8'
								}
							}).then(response => {
								if (response.code === 0) {
									this.msgSuccess('修改成功')
									this.open = false
									this.getList()
								}
							})
						} else {
							request({
								url: 'build/progressSafetyProduction/add',
								method: 'post',
								data: this.form,
								headers: {
									'Content-Type': 'application/json;charset=UTF-8'
								}
							}).then(response => {
								if (response.code === 0) {
									this.msgSuccess('新增成功')
									this.open = false
									this.getList()
								}
							})
						}
					}
				})
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const id = row.id || this.ids

				this.$confirm('是否确认删除该数据项?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return request({
						url: 'build/progressSafetyProduction/deleteDetails/' + id,
						method: 'get'
					})
				}).then(() => {
					this.getList()
					this.msgSuccess('删除成功')
				}).catch(function() {})
			},

			// /** 导出按钮操作 */
			// handleExport() {
			//   this.download('system/dict/type/export', {
			//     ...this.queryParams
			//   }, `type_${new Date().getTime()}.xlsx`)
			// },
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
			securityEvaluationFormat(row, column) {
				return this.selectDictLabel(this.securityEvaluationOptions, row.scoringTable)
			},
			/**
			 * 批量导入
			 */
			bulkImportClick() {
				let lists = [
					"检查评定项目(*)",
					"检查项目(*)",
					"扣分标准(*)",
					"排序(*)"

				]
				//示例数据
				let dataLists = [
					"1",
					"1",
					"1",
					"1"
				]

				this.param = {
					title: "施工企业安全生产评价标准(*)为必填项",
					excelTitle: "施工企业安全生产评价标准模板",
					colNames: lists,
					sampleData: dataLists,
					url: 'build/progressSafetyProduction/excelImport'
				}
				this.bulkImportShow = true
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
		cursor: pointer
	}

	.link-type:focus:hover,
	.link-type:hover {
		color: #20a0ff
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
</style>
