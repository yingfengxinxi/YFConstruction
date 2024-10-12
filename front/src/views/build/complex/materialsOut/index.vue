<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
             style="background-color: white;border-top: 0px solid #e6e6e6;">

				<el-form-item label="物料编号" prop="inId">
					<el-input v-model="queryParams.code" />
				</el-form-item>


				<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          		<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

				<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addPaperRules">新建</el-button>


				</el-form>

			</div>
			<el-table ref="menuTable" :data="paperRuleDataList" style="width: 100%" :height="tableHeight" border v-loading="loading" @sort-change="sortChange">
				<el-table-column type="index" align="center" label="序号" width="70"></el-table-column>
				<el-table-column prop="code" align="center" label="物料编号" width="110" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="name" align="center" label="物料名称" width="120" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="specification" align="center" label="规格" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="model" align="center" label="型号" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="outNumber" align="center" label="退场数量" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="unit" align="center" label="计量单位" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="outTime" align="center" label="退场时间" width="180" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="materialClerk" align="center" label="退料人员" width="120" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="witness" align="center" label="见证人员" width="120" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="manufacturer" align="center" label="生产厂家" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="supplyUnit" align="center" label="供应单位" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="purchaseUnit" align="center" label="购买单位" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="outReason" align="center" label="退场原因" width="140" show-overflow-tooltip sortable="custom"></el-table-column>



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


		<!-- 施工综合-物料退场 新建-->
		<CreateMenu v-if="showCreate"
					:menuForm="currentMenu"
					:actionType="actionType"
					@save="getList"
					@hiden-view="showCreate=false" />

	</div>

</template>

<script>

	import {getList, selectById, delById } from '@/views/build/complex/api/materialsOut'

	import CreateMenu from './CreateMenu'
  import { sortColumn } from '@/utils/index';

	export default {
		//施工综合-物料退场
		name: 'materialsOut',
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

				currentMenu: {}, //当前施工综合-物料退场
				paperRuleDataList: [], //施工综合-物料退场列表

				actionType:'',
				//新增
				showCreate: false,

			}
		},
		computed: {

		},
		created(){

			this.getList()

		},

		mounted() {

		},
		methods: {

			/** 重置按钮操作 */
			resetQuery() {
			this.queryParams = {}
			// this.resetForm('queryForm')
			this.getList()
			},

            //施工综合-物料退场列表
            getList(){

                var queryParams = {};
                queryParams.page = this.currentPage;
              queryParams.limit = this.pageSize;
              queryParams.code = this.queryParams.code
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



			async handleClick(event, scope) {
				//此处需同步调用
				// await selectById(scope.row.id)
				// 	.then(res => {
				// 		this.currentMenu = res.data;
				// 	})

				var queryParams = {page:1,limit:10,id:scope.row.id};
				await getList(queryParams).then(res=>{
					this.currentMenu = res.data.list[0];
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
