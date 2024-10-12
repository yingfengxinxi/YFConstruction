<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
             style="background-color: white;border-top: 0px solid #e6e6e6;">
				<el-form-item label="派工人员" prop="dispatchPeopleId">
					<el-select
                          v-model="queryParams.dispatchPeopleId"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="width: 100%">
						<el-option
								v-for="dict in oddList"
								:key="dict.id"
								:label="dict.name"
								:value="dict.id" />
					</el-select>

				</el-form-item>
				<el-form-item label="派工班组" prop="dispatchTeamId">
					<el-select
							v-model="queryParams.dispatchTeamId"
							placeholder="请选择"
							clearable
							size="small"
							style="width: 100%">
					<el-option
							v-for="dict in teamList"
							:key="dict.id"
							:label="dict.teamName"
							:value="dict.id" />
					</el-select>
				</el-form-item>
				<el-form-item label="派工时间" prop="dispatchTime">
					<el-date-picker
						v-model="queryParams.dispatchTime"
						type="datetimerange"
						range-separator="-"
						value-format="yyyy-MM-dd HH:mm:ss"
						start-placeholder="开始日期"
						end-placeholder="结束日期">
					</el-date-picker>
				</el-form-item>

				<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          		<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

				<el-button class="export-btn" style="float:right;margin-top: 4px;margin-right: 20px;" @click="exportExcel">导出</el-button>
				<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addPaperRules">新建</el-button>


				</el-form>

			</div>
			<el-table ref="menuTable" :data="paperRuleDataList" style="width: 100%" :height="tableHeight" border v-loading="loading" @sort-change="sortChange">
				<el-table-column type="index" align="center" label="序号" width="70"></el-table-column>
				<el-table-column prop="recordPeopleName" align="center" label="记录人" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchTime" align="center" label="派工时间" width="140" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchReason" align="center" label="派工原因" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchUnitName" align="center" label="派工单位" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchTeamName" align="center" label="派工班组" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchPeopleName" align="center" label="派工人员" sortable="custom"></el-table-column>
				<el-table-column prop="dispatchText" align="center" label="派工内容" sortable="custom"></el-table-column>
				<el-table-column prop="visaDepartmentName" align="center" label="签证部门" sortable="custom"></el-table-column>
				<el-table-column prop="responsibleName" align="center" label="责任人" sortable="custom"></el-table-column>
				<el-table-column prop="workTimeStart" align="center" label="作业开始时间" width="140" sortable="custom"></el-table-column>
				<el-table-column prop="workTimeEnd" align="center" label="作业结束时间" width="140" sortable="custom"></el-table-column>
				<el-table-column fixed="right" align="center" label="操作" width="180">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">编辑</el-button>
						<el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button>
						<el-button type="text" icon="el-icon-document" size="small" @click="handleClick('show', scope)">查看</el-button>

					</template>
				</el-table-column>
			</el-table>
            <div class="p-contianer" >
                  <el-pagination
                  :current-page="currentPage"
                  :page-sizes="pageSizes"
                  :page-size.sync="pageSize"
                  :total="total"
                  class="p-bar"
                  background
                  layout="prev, pager, next, sizes, total, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"/>
              </div>
		</div>


		<!-- 零工签证 新建-->
		<CreateMenu v-if="showCreate"
					:menuForm="currentMenu"
					:actionType="actionType"
					@save="getList"
					@hiden-view="showCreate=false" />

	</div>

</template>

<script>

	import {getList, selectById, delById , exportExcel} from '@/views/build/labour/api/oddVisa'
	import { getListByBZ ,selectOddIdAndName} from '@/api/build/infocollection/infocoll'

	import CreateMenu from './CreateMenu'

	import { downloadExcelWithResData,sortColumn } from '@/utils/index'

	export default {
		//零工签证
		name: 'oddVisa',
		components: {
			CreateMenu,
		},
		data() {
			return {

				loading: false, // 加载动画
				tableHeight:document.documentElement.clientHeight - 230,

        queryParams:{},//搜索

                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,

				currentMenu: {}, //当前零工签证
				paperRuleDataList: [], //零工签证列表

				actionType:'',
				//新增
				showCreate: false,

				teamList:[],//班组
				oddList:[],//零工

			}
		},
		computed: {

		},
		created(){
			this.getListByBZ()//班组
	  		this.selectOddIdAndName()//零工 派工人员

			this.getList()

		},

		mounted() {

		},
		methods: {
			//班组
			getListByBZ(){
				getListByBZ().then(res=>{
					this.teamList = res.data
				})
      		},
      		//零工  派工人员
			selectOddIdAndName(){
				selectOddIdAndName().then(res=>{
					this.oddList = res.data
				})
      		},

			/** 重置按钮操作 */
			resetQuery() {
			this.queryParams = {}
			// this.resetForm('queryForm')
			this.getList()
			},

            //零工签证列表
            getList(){

                var queryParams = {};
                queryParams.page = this.currentPage;
				queryParams.limit = this.pageSize;
				queryParams.dispatchPeopleId = this.queryParams.dispatchPeopleId
				queryParams.dispatchTeamId = this.queryParams.dispatchTeamId
				if(this.queryParams.dispatchTime != null && this.queryParams.dispatchTime != undefined && this.queryParams.dispatchTime != ''){
					queryParams.dispatchTimeStart = this.queryParams.dispatchTime[0]
					queryParams.dispatchTimeEnd = this.queryParams.dispatchTime[1]
				}
              queryParams.fieldName = this.queryParams.fieldName
              queryParams.sortingType = this.queryParams.sortingType
                getList(queryParams).then(res => {
					if(res.data != null){
						this.paperRuleDataList = res.data.list

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

					}

                    this.loading = false

                })
            },

            //新增
			addPaperRules() {
				this.currentMenu = {}
				this.actionType = 'create'
				this.showCreate = true;
			},

			//导出
			exportExcel(){
				var queryParams = {};

				queryParams.dispatchPeopleId = this.queryParams.dispatchPeopleId
				queryParams.dispatchTeamId = this.queryParams.dispatchTeamId
				if(this.queryParams.dispatchTime != null && this.queryParams.dispatchTime != undefined && this.queryParams.dispatchTime != ''){
					queryParams.dispatchTimeStart = this.queryParams.dispatchTime[0]
					queryParams.dispatchTimeEnd = this.queryParams.dispatchTime[1]
				}

				exportExcel(queryParams).then(res=>{
					downloadExcelWithResData(res)
          			this.loading = false
				})
			},

			async handleClick(event, scope) {
				//此处需同步调用
				await selectById(scope.row.id)
					.then(res => {
						this.currentMenu = res.data;
					})

				if (event == 'edit') {//修改
					this.actionType = 'update'
					this.showCreate = true;
				} else if (event == 'delete') { //删除
					this.$confirm('您确定要删除吗?', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						})
						.then(() => {
							delById(this.currentMenu.id)
								.then(res => {
									this.loading = false
									if (res.code == 0) {
										this.msgSuccess('删除成功')
                                        this.getList()

									} else {
										this.$message({
											type: 'warn',
											message: res.msg
										})
									}
								})
								.catch(() => {
									this.loading = false
								})
						})
						.catch(() => {
							this.$message({
								type: 'info',
								message: '已取消删除'
							})
						})
				} else if(event == 'show'){//查看
					this.actionType = 'show'
					this.showCreate = true;
				}
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
      sortChange(column) {
        sortColumn(this,column);
      },

		}
	}
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;

		/*margin:0 15px;*/
		/deep/ .xr-header {
			padding: 15px 30px;
		}
	}

	.main-body {
		background-color: white;
		/*border-top: 1px solid $xr-border-line-color;*/
		border-bottom: 1px solid $xr-border-line-color;
		padding: 10px 10px 10px 0px;
		height: 100%;
		overflow-y: auto;
	}

	.main-table-header {
		height: 50px;
		background-color: white;
		position: relative;

		.main-table-header-button {
			float: right;
			margin-right: 10px;
			margin-top: 4px;
		}
	}

	.p-contianer {
		position: relative;
		background-color: white;
		height: 44px;

		.p-bar {
			float: right;
			margin: 5px 100px 0 0;
			font-size: 14px !important;
		}
	}

	/** 树形结构 */
	.el-tree /deep/ .el-tree-node__expand-icon {
		display: none;
	}

	.el-tree /deep/ .el-tree-node__content {
		height: 40px;

		.node-data {
			// .node-img {
			//   width: 15px;
			//   height: 15px;
			//   display: block;
			//   margin-right: 8px;
			//   margin-left: 24px;
			// }
			height: 40px;
			padding: 0 15px;
			position: relative;
			border-radius: $xr-border-radius-base;

			.wk {
				font-size: 14px;
				color: #8a94a6;
				flex-shrink: 0;
			}

			.wk-department {
				margin-right: 8px;
			}

			&__mark {
				display: inline-block;
				width: 6px;
				height: 6px;
				border-radius: 50%;
				background-color: #e6e6e6;
				margin-right: 8px;
				flex-shrink: 0;
			}

			&__label {
				flex: 1;
				color: #333;
				font-size: 14px;
				margin-right: 8px;
			}

			.wk-up-unfold {
				margin-left: 8px;
				transition: transform 0.3s;
			}

			.wk-up-unfold.is-close {
				transform: rotateZ(180deg);
			}

			// .node-label-set {
			//   display: none;
			// }
		}

		.node-data.is-current,
		.node-data:hover {
			background-color: $xr--background-color-base;
		}

		// .node-data:hover .node-label-set {
		//   display: block;
		// }
	}

	@import 'src/views/styles/table.scss';
</style>
