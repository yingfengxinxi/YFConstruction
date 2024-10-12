<template>
	<div class="main">
		<div class="main-body">
			<el-tabs class="tabbox" v-model="activeName" @tab-click="handleClick">
				<el-tab-pane label="工单类型管理" name="first">
					<div class="workorder_title">
						<div class="gddl">工单大类<i class="el-icon-circle-plus-outline" @click="add"></i></div>
						<div>
							<div class="gd_list" @click.stop="switchtable(index)" :class="currentIndex==index?'activgd':''" v-for="(item,index) in gdlist">
								<div class="gd_tit">
									{{item.title}}
								</div>
								<div>
									<i class="el-icon-edit-outline" @click="handleUpdate(item)"></i>
									<i class="el-icon-delete" @click="handleDelete(index)"></i>
								</div>
							</div>
						</div>
					</div>
					<div class="tabright">
						<el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
							<el-form-item label="" prop="itemName">
								<el-input v-model="queryParams.itemName" placeholder="搜索" clearable size="small" @keyup.enter.native="handleQuery" />
							</el-form-item>
							<el-form-item>
								<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
								<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
							</el-form-item>
							<el-form-item style="float: right">
								<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
							</el-form-item>
						</el-form>
						<div class="table-head-container">
							<flexbox v-if="selectionList.length > 0" class="selection-bar">
								<div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
								<flexbox class="selection-items-box">
									<el-button class='bbt' v-for="(item, index) in selectionHandleItems" :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
									</el-button>
								</flexbox>
							</flexbox>
						</div>
						<el-table :data="dataList" :border="true" :height="tableHeight" @selection-change="handleSelectionChange">
							<el-table-column type="selection" align="center" width="55" />
							<el-table-column label="编号" align="center" prop="code" />
							<el-table-column label="名称" align="center" prop="name"  />
							<el-table-column label="默认项" align="center" prop="parcelP" />
							<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
								<template slot-scope="scope">
									<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate2(scope.row)">编辑</el-button>
									<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete2(scope.row)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="p-contianer">
							<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="工单时限管理" name="second"><gdsxgl></gdsxgl></el-tab-pane>
				<el-tab-pane label="工单处理时限设置" name="third"><gdsjsz></gdsjsz></el-tab-pane>
				<el-tab-pane label="功能设置" name="fourth">
					<div class="gnsz">
						 <el-checkbox v-model="checked">户内报修</el-checkbox>
					</div>
				</el-tab-pane>
			</el-tabs>
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
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>		
		<!--表格修改按钮-->
		<el-dialog :title="title2" :visible.sync="open2" width="650px" append-to-body :close-on-click-modal="false">
			<el-form ref="form2" :model="form2" :rules="rules" label-width="100px">
				<el-row>
					<el-col :span="12">
						<el-form-item label="名称" prop="name">
							<el-input v-model="form2.name" placeholder="请输入名称" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="编号">
							<el-input v-model="form2.code" placeholder="请输入编号" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="默认项">
							<el-input v-model="form2.parcelP" placeholder="请输入名称" />
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm2">确 定</el-button>
				<el-button @click="cancel2">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import gdsxgl from './components/gdsxgl.vue'
	import gdsjsz from './components/gdsjsz.vue'
	export default {
		data() {
			return {
				checked: true,
				form2:{
					name:'',
					code:'',
					parcelP:'',
				},
				open2:false,
				/** 选中时操作 */
				selectionHandleItems: [{
						name: "删除",
						type: "delete",
						icon: "delete"
					},
					// {
					//     name: "导出选中",
					//     type: "export",
					//     icon: "export"
					// }
				],
				// 分页
				currentPage: 1,
				pageSize: 10,
				total: 0,
				sortData: {}, // 字段排序
				tableHeight: document.documentElement.clientHeight - 330, // 表的高度
				queryParams: {
					itemName: ''
				},
				selectionList: [], // 勾选数据 用于全局导出
				title: '新建工单大类',
				title2: '编辑工单子类',
				rules: {
					name: [{
						required: true,
						message: '请输入名称名称',
						trigger: 'blur'
					}]
				},
				open: false,
				form: {
					name: '',
					code: ''
				},
				activeName: 'first',
				currentIndex: 0,
				gdlist: [{
						title: '报修业务'
					},
					{
						title: '充值业务'
					},
					{
						title: '住户维修'
					}
				],
				dataList: [{
						code: '001',
						name: '摄像头维修',
						parcelP: '',
					},
					{
						code: '011',
						name: '护栏维修',
						parcelP: '',
					},
					{
						code: '003',
						name: '加急单',
						parcelP: '',
					},
				]
			};
		},
		components:{
			gdsxgl,
			gdsjsz
		},
		methods: {
			handleDelete2(row){
				console.log(row)
			},
			submitForm2(){
				this.open2=false
				this.form2.name=''
				this.form2.code=''
				this.form2.parcelP=''
			},
			cancel2(){
				this.open2=false
				this.form2.name=''
				this.form2.code=''
				this.form2.parcelP=''
			},
			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				this.pageSize = val
			},
			// 多选框选中数据
			handleSelectionChange(val) {
				// this.ids = selection.map(item => item.id)
				// this.single = selection.length != 1
				// this.multiple = !selection.length
				this.selectionList = val; // 勾选的行
				this.updateTableHeight();
			},
			/**
			 * 更新表高
			 */
			updateTableHeight() {
				var offsetHei = document.documentElement.clientHeight;
				var removeHeight = this.selectionList.length > 0 ? 360 : 330;
				this.tableHeight = offsetHei - removeHeight;
			},
			//新建
			handleAdd() {
				this.title2='新建工单子类'
				this.open2=true
			},
			/** 搜索按钮操作 */
			handleQuery() {

			},
			//重置
			resetQuery() {

			},
			handleUpdate2(row){
//				console.log(row)
//				this.form2.name=row.name
//				this.form2.code=row.code
//				this.form2.parcelP=row.parcelP
//				console.log(this.form2)
console.log(row)
				this.form2=row
// 				this.$set(this.form2,'name',row.name)
// 				this.$set(this.form2,'code',row.code)
// 				this.$set(this.form2,'parcelP',row.parcelP)

				this.title2='编辑工单子类'
				this.open2=true
					
			},
			//修改大类
			handleUpdate(item) {
				this.open = true
				this.title = '编辑工单大类'
				this.form.name = item.title
			},
			//删除
			handleDelete(index) {
				this.$confirm('是否确认删除选择的数据项?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return
				}).then(() => {
					this.gdlist.splice(index, 1);
					this.msgSuccess('删除成功')
				}).catch(function() {})
			},
			/** 提交按钮 */
			submitForm: function() {
				this.$refs['form'].validate(valid => {
					if(valid) {
						console.log()
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
				this.form.name=''
				this.form.code=''
			},
			add() {
				this.open = true
				this.form.name=''
				this.form.code=''
			},
			switchtable(index) {
				this.currentIndex = index
			},
			handleClick(tab, event) {
				console.log(tab, event);
			}
		}
	};
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;
	}
	
	.main-body {
		height: 100%;
		padding: 10px;
	}
	
	.workorder_title {
		width: 280px;
		border: 1px solid #e6e6e6;
		margin-left: 10px;
		height: 100%;
	}
	
	.tabright {
		border: 1px solid #e6e6e6;
		border-radius: 4px;
		position: absolute;
		top: 0;
		left: 295px;
		bottom: 0;
		right: 0;
	}
	
	.tabbox {
		height: 100%;
	}
	
	.gddl {
		font-size: 16px;
		font-weight: 700;
		height: 50px;
		line-height: 50px;
		display: flex;
		align-items: center;
		width: 100%;
		border-bottom: 1px solid #e6e6e6;
		justify-content: space-around;
	}
	
	.gd_list {
		display: flex;
		padding: 12px 15px;
		align-items: center;
		border-bottom: 1px solid #e6e6e6;
		justify-content: space-between;
		font-size: 14px;
	}
	
	.el-icon-circle-plus-outline {
		color: #2362fb;
		margin-left: 20px;
		font-size: 24px;
		cursor: pointer;
	}
	
	.el-icon-edit-outline {
		color: #2362fb;
		cursor: pointer;
	}
	
	.el-icon-delete {
		color: #2362fb;
		cursor: pointer;
	}
	
	.activgd {
		background-color: #F6F8FA;
	}
	.gnsz{
		margin-left: 40px;
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
	.bbt{
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
	/deep/ .el-tabs__content {
		height: calc(100% - 50px);
	}
	
	/deep/ .el-tab-pane {
		height: 100%;
	}
</style>