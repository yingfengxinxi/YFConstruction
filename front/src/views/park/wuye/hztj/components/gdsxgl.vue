<template>
	<div>
		<el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
			<el-form-item label="" prop="itemName">
				<el-input v-model="queryParams.itemName" placeholder="搜索" clearable size="small" @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button type="primary" size="mini" @click="handleQuery">导出excel</el-button>
			</el-form-item>
			<el-form-item style="float: right">
				<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" :border="true" :height="tableHeight">
			<el-table-column label="编号" width='100' align="center" prop="code" />
			<el-table-column label="名称" align="center" prop="name" />
			<el-table-column label="处理时限（小时）" width='120' align="center" prop="time" />
			<el-table-column label="备注" align="center" prop="remarks" />
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div class="p-contianer">
			<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
		</div>
		
		<el-dialog :title="title" :visible.sync="open" width="650px" append-to-body :close-on-click-modal="false">
			<el-form ref="form" :model="form" :rules="rules" label-width="100px">
				<el-row>
					<el-col :span="12">
						<el-form-item label="名称" prop="name">
							<el-input v-model="form.name" placeholder="请输入名称" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="编号">
							<el-input v-model="form.code" placeholder="请输入编号" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="工单处理时限" prop="time">
							<el-input placeholder="请输入工单处理时限" v-model="form.time">
							    <template slot="append">小时</template>
							  </el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="备注">
							<el-input v-model="form.name" placeholder="请输入备注" />
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
</template>

<script>
	export default {
		data() {
			return {
				open:false,
				rules: {
					name: [{
						required: true,
						message: '请输入名称名称',
						trigger: 'blur'
					}],
					time: [{
						required: true,
						message: '请输入时间',
						trigger: 'blur'
					}],
				},
				form:{
					name:'',
					code:'',
					time:'',
					remarks:''
				},
				title:'新建工单限时',
				tableHeight: document.documentElement.clientHeight - 330, // 表的高度
				dataList: [{
						name: '疑难问题维修',
						code: '001',
						time: '142',
						remarks: '疑难问题维修，公维维修'
					},
					{
						name: '24小时内处理（适用于咨询单及其他业务）',
						code: '0011',
						time: '24',
						remarks: '适用于咨询单及其他业务'
					},
					{
						name: '4小时内处理（适用于加急单）',
						code: '0012',
						time: '4',
						remarks: '适用于加急单'
					},
					{
						name: '8小时内处理（适用于一般单）',
						code: '0013',
						time: '8',
						remarks: '适用于一般单'
					}
				],
				queryParams: {
					itemName: ''
				},
				// 分页
				currentPage: 1,
				pageSize: 10,
				total: 0,
				sortData: {}, // 字段排序
			}
		},

		created() {

		},
		
		methods: {
			handleDelete(){
				                this.$confirm('是否确认删除选择的数据项?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function() {
                    return 
                }).then(() => {
                    this.getList()
                    this.msgSuccess('删除成功')
                }).catch(function() {})
			},
			handleUpdate(row){
				this.title='编辑工单限时'
				this.form=row
				this.open=true
			},
						/** 提交按钮 */
			submitForm: function() {
				this.$refs['form'].validate(valid => {
					if(valid) {
						this.gdlist.push({
							title: this.form.name
						})
						this.msgSuccess('新增成功')
						this.open = false
					}
				})
			},
			// 取消按钮
			cancel() {
				this.open = false

			},
			handleCurrentChange() {

			},
			handleSizeChange() {

			},
			handleAdd() {
				this.title='新建工单限时'
				this.form=''
				this.open=true
			},
			handleQuery() {

			}
		}
	}
</script>

<style scpoed>

</style>