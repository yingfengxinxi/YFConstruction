<template>
	<div class="main">
		<div class="main-body">
			<el-form ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="状态" prop="status">
					<el-select v-model="queryParams.status" placeholder="请选择">
						<el-option v-for="item in options1" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="维修人" prop="trainingType">
					<el-select v-model="queryParams.trainingType" multiple collapse-tags placeholder="请选择">
						<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="报修单号" prop="bxdh">
					<el-input v-model="queryParams.bxdh" placeholder="请输入报修单号" />
				</el-form-item>
				<el-form-item label="派工单号" prop="pgdh">
					<el-input v-model="queryParams.pgdh" placeholder="请输入派工单号" />
				</el-form-item>
				<el-form-item label="管理区" prop="glq">
					<el-select v-model="queryParams.glq" placeholder="请选择">
						<el-option v-for="item in glq" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="楼栋" prop="ld">
					<el-select v-model="queryParams.ld" placeholder="请选择">
						<el-option v-for="item in ld" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="工单时限" prop="gdsx">
					<el-select v-model="queryParams.gdsx" placeholder="请选择">
						<el-option v-for="item in gdsx" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="报修地点" prop="bxdd">
					<el-input v-model="queryParams.bxdd" placeholder="请输入报修地点" />
				</el-form-item>
				<el-form-item style="float: right">
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
					<!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->
					<el-button type="primary" size="mini">导出Excel</el-button>
				</el-form-item>
			</el-form>
			<el-table :data="dataList" :border="true" :height="tableHeight">
				<el-table-column label="派工单号" align="center" prop="pgdh" width="200px" />
				<el-table-column label="状态" align="center" prop="zt"  width="150px">
					<template slot-scope="scope">
						<el-tag type="success" v-if='scope.row.zt==1'>已确认已完成</el-tag>
						<el-tag type="info" v-else-if='scope.row.zt==2'>进行中</el-tag>
						<el-tag type="warning" v-else='scope.row.zt==3'>待开始</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="报修单号" align="center" prop="bxdh" width="200px" />
				<el-table-column label="派工时间" align="center" prop="pgsj" width="200px" />
				<el-table-column label="工单时限" align="center" prop="gdsx" width="200px" />
				<el-table-column label="报修人" align="center" prop="bxr" />
				<el-table-column label="联系电话" align="center" prop="lxdh" width="200px" />
				<el-table-column label="维修人" align="center" prop="wxr" width="200px" />
				<el-table-column label="管理区" align="center" prop="gxq" width="200px" />
				<el-table-column label="楼栋" align="center" prop="ld" />
				<el-table-column label="要求处理事项" align="center" prop="yqclsx" width="200px" />
				<el-table-column label="派工备注" align="center" prop="pgbz" width="200px" />
				<el-table-column label="开始处理时间" align="center" prop="ksclsj" width="200px" />
				<el-table-column label="回单时间" align="center" prop="hdsj" width="200px" />
				<el-table-column label="回单情况说明" align="center" prop="hdsm" width="200px" />
				<el-table-column label="确认时间" align="center" prop="qrsj" width="200px" />
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px" fixed="right">
					<template slot-scope="scope">
						<el-button v-if='scope.row.status==3' size="mini" type="text" @click="handleUpdate(scope.row)">编辑</el-button>
						<el-button v-if='scope.row.status==3' size="mini" type="text" @click="handleUpdate(scope.row)">回退</el-button>
						<el-button v-if='scope.row.status==3' size="mini" type="text">开始处理</el-button>
						<el-button v-if='scope.row.status==1' size="mini" type="text" @click="handleVoid(scope.row)">作废</el-button>
						<el-button  size="mini" type="text" @click="handleAdd(scope.row)">添加说明</el-button>
						<el-button  size="mini" type="text" >打印派工单</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" />
			</div>
		</div>
		<el-dialog title="添加说明" :visible.sync="open3" width="700px" append-to-body>
					<el-form ref="form3" :model="form3" :rules="rules3" label-width="100px">
							<el-row>
						<el-col :span="24">
							<el-form-item label="维修说明" prop="tjsm">
								<el-input type="textarea" v-model="form3.tjsm"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					</el-form>	
					<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="submitForm3">确 定</el-button>
					<el-button @click="cancel3">取 消</el-button>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				open3:false,
				form3:{},
				dataList: [{
					pgdh: 'PG00120210628003',
					zt: 1,
					bxdh: 'BX00120210624001',
					pgsj: '2021-06-28 14:58:48',
					gdqx: '8小时内处理（适用于一般单）',
					gdqxval: 1,
					bxr: '贾春华',
					lxdh: '',
					wxr: '王绪',
					bxdd: '',
					glq: '诺怀项目1',
					ld: '诺怀项目1',
					yqclsx: '尽快修好',
					pgbz: '尽快处理',
					ksclsj: '2021-06-29 14:58:48',
					hdsj: '2021-06-29 14:58:48',
					hdqksm: '',
					qrsj: '',
					status:'1'
				},{
					pgdh: 'PG00120210628002',
					zt: 3,
					bxdh: 'BX202106250824',
					pgsj: '2021-06-28 14:58:48',
					gdqx: '8小时内处理（适用于一般单）',
					gdqxval: 1,
					bxr: '李伟',
					lxdh: '13405085698',
					wxr: '杨存保',
					bxdd: '大门口',
					glq: '诺怀项目1',
					ld: '诺怀项目1',
					yqclsx: '尽快修好',
					pgbz: '尽快处理',
					ksclsj: '2021-06-29 14:58:48',
					hdsj: '2021-06-29 14:58:48',
					hdqksm: '',
					qrsj: '',
					status:'3'
				}],
				rules3:{
					tjsm:[{
						required: true,
						message: '维修说明不得为空',
						trigger: 'change'
					}]
				},
				form: {},
				queryParams: {},
				gdsx: [{
						value: '1',
						label: '8小时内处理（适用于一般单）'
					}, {
						value: '2',
						label: '4小时内处理（适用于加急单）'
					}, {
						value: '3',
						label: '24小时内处理（适用于咨询单及其他业务）'
					},
					{
						value: '4',
						label: '疑难问题维修'
					},
				],
				ld: [{
					value: '1',
					label: 'A2栋'
				}, {
					value: '2',
					label: 'A3栋'
				}, {
					value: '3',
					label: 'B05栋'
				}, ],
				glq: [{
					value: '1',
					label: '诺杯项目1'
				}, {
					value: '2',
					label: '诺杯项目2'
				}, ],
				options: [{
					value: '选项1',
					label: 'admin'
				}, {
					value: '选项2',
					label: '张霖'
				}, {
					value: '选项3',
					label: '李晓晓'
				}, {
					value: '选项4',
					label: '刘国栋'
				}, {
					value: '选项5',
					label: '王金'
				}],
				options1: [{
					value: '选项1',
					label: '全部'
				}, {
					value: '选项2',
					label: '审批中'
				}, {
					value: '选项3',
					label: '同意'
				}, {
					value: '选项4',
					label: '拒绝'
				}],
				// 分页
				currentPage: 1,
				pageSize: 10,
				total: 0,
				tableHeight: document.documentElement.clientHeight - 400, // 表的高度
				queryParams: {
					itemName: ''
				},
				rules: {
					name: [{
						required: true,
						message: '请输入名称名称',
						trigger: 'blur'
					}]
				},
				open: false,
			};
		},
		components: {},
		methods: {
			handleAdd(){
				this.open3=true;
			},
			handleVoid(){
				this.$confirm('是否确认作废选择的数据项?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.msgSuccess('作废成功')
				}).catch(function() {})
			},
			handleDelete2(row) {
				console.log(row)
			},
			submitForm2() {
				this.open2 = false
				this.form2.name = ''
				this.form2.code = ''
				this.form2.parcelP = ''
			},
			cancel2() {
				this.open2 = false
				this.form2.name = ''
				this.form2.code = ''
				this.form2.parcelP = ''
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
			/** 搜索按钮操作 */
			handleQuery() {

			},
			submitForm3:function() {
				this.$refs['form3'].validate(valid => {
					if(valid) {
						this.msgSuccess('添加成功')
									this.open3 = false

					}
				})
			},
			cancel3(){
				this.open3=false
			},
			//重置
			resetQuery() {

			},
			handleUpdate2(row) {
				//				console.log(row)
				//				this.form2.name=row.name
				//				this.form2.code=row.code
				//				this.form2.parcelP=row.parcelP
				//				console.log(this.form2)
				console.log(row)
				this.form2 = row
				// 				this.$set(this.form2,'name',row.name)
				// 				this.$set(this.form2,'code',row.code)
				// 				this.$set(this.form2,'parcelP',row.parcelP)

				this.title2 = '编辑工单子类'
				this.open2 = true

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
				this.form.name = ''
				this.form.code = ''
			},
			add() {
				this.open = true
				this.form.name = ''
				this.form.code = ''
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
	
	.gnsz {
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
	
	.bbt {
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