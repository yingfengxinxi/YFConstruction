<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
             style="background-color: white;border-top: 0px solid #e6e6e6;">
				<el-form-item label="工种" prop="workTypeId">
					<el-select
					v-model="selectForm.workTypeId"
					clearable
					placeholder="请选择">
					<el-option
						v-for="item in workTypeOptions"
						:key="item.dictValue"
						:label="item.dictLabel"
						:value="item.dictValue"/>
					</el-select>

				</el-form-item>

				<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          		<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

				<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addPaperRules">新建</el-button>

				</el-form>

			</div>
			<el-table ref="menuTable" :data="paperRuleDataList" style="width: 100%" :height="tableHeight" border v-loading="loading" @sort-change="sortChange" >
				<el-table-column type="index" align="center" label="序号" width="90"></el-table-column>
				<el-table-column prop="paperRulesName" align="center" label="试卷名称" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="workTypeId" align="center" label="适用工种" :formatter="WTFormat" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="paperScore" align="center" label="试卷总分值" sortable="custom" ></el-table-column>
				<el-table-column prop="paperCount" align="center" label="试题数量" sortable="custom" ></el-table-column>
				<el-table-column prop="pastScope" align="center" label="通过分值" sortable="custom" ></el-table-column>


				<el-table-column fixed="right" align="center" label="操作" width="210">
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


		<!-- 试卷规则配置 -->
		<CreateMenu v-if="showCreate"
					:menuForm="currentMenu"
					:actionType="actionType"
					@save="getList"
					@hiden-view="showCreate=false" />

	</div>

</template>

<script>

    import {getPaperRulesList, selectPaperRulesById, delPaperRulesById } from '@/views/build/wifi/api/paperRules'

	import CreateMenu from './components/CreateMenu'
	import {sortColumn } from '@/utils'
	import {getWTList } from '@/api/build/infocollection/infocoll'
	export default {
		/** 问题类别管理 */
		name: 'wifiPaperRules',
		components: {
			CreateMenu,
		},
		data() {
			return {
				//树型开始
				loading: false, // 加载动画
				tableHeight:document.documentElement.clientHeight - 230,

				queryParams:{},
				selectForm:{},
                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,

				currentMenu: {}, //当前试卷规则
				paperRuleDataList: [], //试卷规则列表

				actionType:'',
				//新增
				showCreate: false,

				worktypeList:[],//工种
				workTypeOptions: [],//工种字典

			}
		},
		computed: {

		},
		created(){
			this.getList()
			//this.getWorktypeList()//工种
			// 工种字典
			this.getDicts('b_labour_work_type').then(response => {
			  this.workTypeOptions = response.data
			})
		},

		mounted() {

		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			/** 重置按钮操作 */
			resetQuery() {
			this.selectForm = {}
			// this.resetForm('queryForm')
			this.getList()
			},

            //试卷规则列表
            getList(){
                this.queryParams.page = this.currentPage;
				 this.queryParams.limit = this.pageSize;
				 this.queryParams.workTypeId = this.selectForm.workTypeId

                getPaperRulesList(this.queryParams).then(res => {
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

			async handleClick(event, scope) {
				//此处需同步调用
				await selectPaperRulesById(scope.row.id)
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
							delPaperRulesById(this.currentMenu.id)
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

			/** 查询所工种数据列表 */
			getWorktypeList() {
				getWTList().then(response => {
				this.worktypeList = response.data
				this.loading = false
				}).catch(() => {
				this.loading = false
				})
			},

			//劳务工种
			WTFormat(row, column){
				//var datas  = this.worktypeList
				var datas  = this.workTypeOptions
				var value = row.workTypeId
				var actions = []

				Object.keys(datas).some((key) => {
				/* if (datas[key].id == ('' + value)) {
					actions.push(datas[key].workType)
					return true
				} */
				if (datas[key].dictValue == ('' + value)) {
					actions.push(datas[key].dictLabel)
					return true
				}
				})
				return actions.join('')
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
			margin-right: 20px;
			margin-top: 10px;
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
