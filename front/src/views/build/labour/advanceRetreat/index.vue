<template>
	<div class="main">

		<div class="main-body">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
				style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="分包商名称" prop="corpName">
					<el-input v-model="queryParams.corpName" placeholder="请输入分包商名称" clearable size="small"
						@keyup.enter.native="handleQuery" />
				</el-form-item>
				<el-form-item label="姓名" prop="name">
					<el-input v-model="queryParams.name" placeholder="请输入分包商名称" clearable size="small"
						@keyup.enter.native="handleQuery" />
				</el-form-item>
				<el-form-item label="证件号码" prop="idCardNumber">
					<el-input v-model="queryParams.idCardNumber" placeholder="请输入分包商名称" clearable size="small"
						@keyup.enter.native="handleQuery" />
				</el-form-item>
				<el-form-item label="类型" prop="type">
					<el-select v-model="queryParams.type" placeholder="请选择" clearable size="small"
						style="width: 160px">
						<el-option label="进场" value="1" />
						<el-option label="出场" value="2" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
				</el-form-item>
				<el-form-item style="float: right">
					<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus"
						size="mini" @click="handleAdd">新建</el-button>
				</el-form-item>
			</el-form>
			<el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
				<el-table-column label="序号" type="index" align='center' width="70"></el-table-column>
				<el-table-column label="姓名" align="center" prop="name" width="80px" sortable="custom" />
				<el-table-column label="证件号码" align="center" prop="idCardNumber" width="150px" show-overflow-tooltip sortable="custom"/>
				<el-table-column label="班组" align="center" prop="teamName" width="150px" show-overflow-tooltip sortable="custom"/>
				<el-table-column label="分包商名称" align="center" prop="realName" width="200px" show-overflow-tooltip sortable="custom"/>
				<el-table-column label="统一社会信用代码" align="center" prop="corpCode" show-overflow-tooltip sortable="custom"/>
				<el-table-column label="类型" align="center" prop="date" :formatter="typeFormat" sortable="custom" />
				<el-table-column label="进出场时间" align="center" prop="date" :formatter="formatterTime" sortable="custom"/>
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
				<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar"
					background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" />
			</div>
			<!-- 添加或修改参数配置对话框 -->
			<el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
				<el-form ref="form" :model="form" :rules="rules" label-width="120px" class="add-form">
					<el-form-item label="班组名称" prop="teamId" class="add-form-input">
						<el-select v-model="form.teamId" placeholder="请选择" clearable size="small" style="width: 160px"
							@change="dataload">
							<el-option v-for="dict in BZList" :key="dict.id" :label="dict.teamName" :value="dict.id" />
						</el-select>
					</el-form-item>
					<el-form-item label="所属单位" prop="subconId" class="add-form-input">
						<el-select v-model="form.subconId" disabled="disabled" placeholder="请选择" clearable size="small"
							style="width: 160px">
							<el-option v-for="dict in FBSList" :key="dict.id" :label="dict.realName" :value="dict.id" />
						</el-select>
					</el-form-item>
					<el-form-item label="身份证类型" prop="idCardType" class="add-form-input">
						<el-select v-model="form.idCardType" placeholder="请选择" clearable size="small"
							style="width: 200px">
							<el-option v-for="dict in idCardTypeDict" :key="dict.dictValue" :label="dict.dictLabel"
								:value="dict.dictValue" />
						</el-select>
					</el-form-item>
					<el-form-item label="证件号码" prop="idCardNumber" class="add-form-input">
						<el-input v-model="form.idCardNumber" placeholder="请输入证件号码" />
					</el-form-item>
					<el-form-item label="进出场日期" prop="date" class="add-form-input">
						<el-date-picker v-model="form.date" type="date" value-format="yyyy-MM-dd HH:mm:ss"
							placeholder="选择日期">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="进出场类型" prop="type" class="add-form-input">
						<el-select v-model="form.type" placeholder="请选择" clearable size="small" style="width: 200px">
							<el-option label="进场" value="1" />
							<el-option label="出场" value="2" />
						</el-select>
					</el-form-item>
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
	import {
		selectAdvanceRetreatList,
		saveAdvanceRetreat,
		getAdvanceRetreatById,
		deleteAdvanceRetreat
	} from '@/api/build/labour'
	import {
		selectAreaList
	} from '@/api/common'
	import {
		validatePhone,
		validateIdNo,
		isNum,
		guid,
		sortColumn
	} from '@/utils'
	import Reminder from '@/components/Reminder'
	import XrHeader from '@/components/XrHeader'
	import request from '@/utils/request'
	import vPinyin from '@/utils/vuepy.js'
	import {
		getWTList,
		getListByFBS,
		getListByBZ
	} from '@/api/build/infocollection/infocoll'
	export default {
		name: 'subcontractor',
		components: {
			Reminder,
			XrHeader
		},
		data() {
			return {
				// 遮罩层
				loading: true,
				tableHeight: document.documentElement.clientHeight - 280, // 表的高度
				// 显示搜索条件
				showSearch: true,
				// 分页
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 30, 40],
				total: 0,
				// 表格数据
				dataList: [],
				// 分包商数据信息
				FBSList: [],
				//证件类型字典
				idCardTypeDict: [],
				// 劳务工种数据信息
				worktypeList: [],
				// 班组数据信息
				BZList: [],
				// 弹出层标题
				title: '',
				// 是否显示弹出层
				open: false,
				// 分包商类型字典数据
				subcontractorTypeDict: [],
				// 参建单位类型字典数据
				corpTypeDict: [],
				// 分包资质等级
				overallMeritDict: [],
				// 是否生效
				isConformityOptions: [{
						value: '1',
						label: '是'
					},
					{
						value: '0',
						label: '否'
					}
				],
				// 区划数据-省
				provinceOptions: [],
				// 区划数据-市
				cityOptions: [],
				// 区划数据-区
				districtOptions: [],
				// 查询参数
				queryParams: {
					page: this.currentPage,
					limit: this.pageSize,
				},
				// 表单参数
				form: {
					city: undefined
				},
				// 表单校验
				rules: {
					teamId: [{
						required: true,
						message: '班组不能为空',
						trigger: 'blur'
					}],
					subconId: [{
						required: true,
						message: '所属单位不能为空',
						trigger: 'blur'
					}],
					corpType: [{
						required: true,
						message: '身份证类型不能为空',
						trigger: 'blur'
					}],
					idCardNumber: [{
						required: true,
						message: '身份证号码不能为空',
						trigger: 'blur'
					}],
					date: [{
						required: true,
						message: '进出场日期不能为空',
						trigger: 'blur'
					}],
					type: [{
						required: true,
						message: '进出场类型不能为空',
						trigger: 'blur'
					}],
				}
			}
		},
		created() {
			// 获取列表数据
			this.getList()
			// 初始化字典数据
			this.initDicts()
			this.getListByBZ()
			this.getListByFBS();
		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			/** 查询分包商数据 **/
			getList() {
				this.loading = true
				this.queryParams.page = this.currentPage
				this.queryParams.limit = this.pageSize
				selectAdvanceRetreatList(this.queryParams).then(response => {
					if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this
						.currentPage && this.currentPage > 1) {
						this.currentPage = this.currentPage - 1
						this.getList()
					} else {
						this.total = response.data.totalRow
						this.loading = false
					}
					this.dataList = response.data.list
					this.total = response.data.totalRow
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			// 初始化字典数据
			initDicts() {
				this.getDicts('b_subcontractor_type').then(response => {
					this.subcontractorTypeDict = response.data
				})
				this.getDicts('b_corp_type').then(response => {
					this.corpTypeDict = response.data
				})

				this.getDicts('b_overall_merit').then(res => {
					this.overallMeritDict = res.data
				})
				this.getDicts('b_id_card_type').then(res => {
					this.idCardTypeDict = res.data
				})
			},
			// 分包商类型字典翻译
			typeFormat(row, column) {
				if (row.type == '1') {
					return '进场'
				} else {
					return '出场'
				}
			},
			// 获取省级区划数据
			getProvinceList() {
				const param = {
					level: 1
				}
				selectAreaList(param).then(res => {
					this.provinceOptions = res.data
				})
			},
			// 获取市级区划数据
			getCityList(id) {
				const param = {
					parentId: id
				}
				selectAreaList(param).then(res => {
					this.cityOptions = res.data
				})
			},
			// 获取县级区划数据
			getDistrictList(id) {
				const param = {
					parentId: id
				}
				selectAreaList(param).then(res => {
					this.districtOptions = res.data
					console.log(this.districtOptions)

				})
			},
			// 强制刷新下拉框
			areaChangeProvince(d) {
				this.form.city = ''
				this.form.district = ''
				this.getCityList(d)
			},
			areaChangeCity(d) {
				this.form.district = ''
				this.getDistrictList(d)
				this.$forceUpdate()
			},
			areaChangeDistrict(d) {
				this.$forceUpdate()
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
				this.resetForm('queryForm')
				this.handleQuery()
			},
			/** 新增按钮操作 */
			handleAdd() {
				// 清空市、区区划数据
				this.cityOptions = []
				this.districtOptions = []
				// 获取省级区划
				this.getProvinceList()
				this.reset()
				this.open = true
				this.title = '添加信息'
			},

			/** 修改按钮操作 */
			handleUpdate(row) {
				// 获取省级区划
				this.getProvinceList()
				this.reset()
				const id = row.id
				getAdvanceRetreatById(id).then(response => {
					this.form = response.data
					this.open = true
					this.title = '修改信息'
					this.getCityList(response.data.province)
					this.getDistrictList(response.data.city)
				})
			},
			/** 提交按钮 */
			submitForm: function() {
				this.$refs['form'].validate(valid => {
					if (valid) {
						saveAdvanceRetreat(this.form).then(response => {
							if (response.code === 0) {
								this.msgSuccess('新增成功')
								this.open = false
								this.getList()
							}
						})
					}
				})
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const id = row.id
				const realName = row.realName
				this.$confirm('是否确认删除"' + name + '"的数据项?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return deleteAdvanceRetreat(id)
				}).then(() => {
					this.getList()
					this.msgSuccess('删除成功')
				}).catch(function() {})
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('system/dict/type/export', {
					...this.queryParams
				}, `type_${new Date().getTime()}.xlsx`)
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
			/** 查询所有分包商id和name */
			getListByFBS() {
				getListByFBS().then(response => {
					this.FBSList = response.data
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			/** 查询所有班组名称 */
			getListByBZ() {
				getListByBZ().then(response => {
					this.BZList = response.data
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			dataload(value) {
				var than = this
				request({
					url: 'build/labourTeam/queryById/' + value,
					method: 'post'
				}).then(response => {
					console.log(response)
					this.FBSList.forEach((item) => {
						if (response.data.subcontractorId == item.id) {
							console.log(item)
						}
					})
					than.form.subconId = response.data.subcontractorId
					this.$forceUpdate()
				})
			},
      formatterTime(row,column){
        let data = row[column.property]
        return  /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
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

	.add-form-input {
		float: left;
	}

	.add-form::after {
		content: "";
		display: block;
		clear: both;
	}

	.add-form .el-form-item {
		width: 50%;
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

	/deep/ .el-dialog {
		height: 700px;
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		margin-top: auto !important;
	}

	/deep/ .el-dialog .el-dialog__body {
		height: 600px;
		overflow: auto;
	}
</style>
