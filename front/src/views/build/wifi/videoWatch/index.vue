<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="125px"
             style="background-color: white;border-top: 0px solid #e6e6e6;">
				<el-form-item label="教育模板名称" prop="vid">
					<el-select
                          v-model="queryParamForm.vid"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="width: 100%">
                    <el-option
                            v-for="dict in videoNameList"
                            :key="dict.id+dict.name"
                            :label="dict.name"
                            :value="dict.id" />
                  </el-select>

				</el-form-item>

				<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          		<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

				<!-- <el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="handleAdd">新建</el-button> -->

				</el-form>

			</div>
			<el-table ref="menuTable" :data="videoDataList" style="width: 100%" :height="tableHeight" border
				@row-click="handleRowClick"
				:cell-class-name="cellClassName"
				@sort-change="sortChange"
				v-loading="loading" >
				<el-table-column type="index" align="center" label="序号" width="90"></el-table-column>
				<el-table-column prop="vId" align="center" label="教育模板名称" :formatter="vIdFormat" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="courseVideoName" align="center" label="课程视频名称" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="fileName" align="center" label="视频文件名" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="watchTime" align="center" label="观看时间" sortable="custom"></el-table-column>
				<el-table-column prop="watchPeopleName" align="center" label="观看人" sortable="custom"></el-table-column>

				<!-- <el-table-column fixed="right" align="center" label="操作" width="210">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">编辑</el-button>
						<el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button>
						<el-button type="text" icon="el-icon-document" size="small" @click="handleClick('show', scope)">查看</el-button>

					</template>
				</el-table-column> -->
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


		<!-- 教育视频观看记录配置 -->
		<CreateMenu v-if="showCreate"
					:menuForm="currentMenu"
					:actionType="actionType"
					@save="getList"
					@hiden-view="showCreate=false" />

		<!-- 点击 路劲 弹出 视频播放 -->
		<showvideo v-if="showvideoFlag"
					:fileUrl="fileUrl"
					@close="showvideoFlag=false"/>


	</div>

</template>

<script>

    import {getList, selectById, delById , queryById } from '@/views/build/wifi/api/videoWatch'
	import {selectNameList ,selectByBithIdToFileId } from '@/views/build/wifi/api/videoSet'

	import CreateMenu from './components/CreateMenu'
	import showvideo from './components/video'
	import {sortColumn } from '@/utils'
	export default {
		/** 教育视频观看记录配置 */
		name: 'videoWatch',
		components: {
			CreateMenu,
			showvideo
		},
		data() {
			return {

				loading: false, // 加载动画
				tableHeight:document.documentElement.clientHeight - 230,

				queryParamForm:{},//搜索

                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,

				currentMenu: {}, //当前教育视频观看记录
				videoDataList: [], //教育视频观看记录列表

				actionType:'',
				//新增
				showCreate: false,

				videoNameList:[],//视频模板下拉框使用

				fileUrl:'',//点击路劲的时候转换的URL
				showvideoFlag:false,//点击路劲的时候使用

			}
		},
		computed: {

		},
		created(){
			this.selectNameList()

			this.getList()

		},

		mounted() {

		},
		methods: {
			sortChange(column) {
		      sortColumn(this,column);
		    },
			//教育模板名称格式化
			vIdFormat(row, column) {
			var datas = this.videoNameList
			var value = row.vId
			var actions = []

			Object.keys(datas).some((key) => {
				if (datas[key].id == ('' + value)) {
					actions.push(datas[key].name)
					return true
				}
			})
			return actions.join('')
		},
			// 当某一行被点击时会触发该事件
			handleRowClick(row, column, event) {
				if (column.property == 'fileName' && row.fileName != '视频不存在') {

          selectByBithIdToFileId(row.vdId).then(res=>{

						queryById(res.data.id).then(resp=>{

							this.fileUrl = resp.data.url
							// console.log(this.fileUrl)
							this.showvideoFlag = true
						})
					})

				}
			},

			/**
			 * 通过回调控制class
			 */
			cellClassName({row, column, rowIndex, columnIndex}) {
				if (column.property === 'fileName' && row.fileName != '视频不存在') {
				return 'can-visit--underline can-visit--bold'
				}
			},


			/** 重置按钮操作 */
			resetQuery() {
			this.queryParamForm = {}
			// this.resetForm('queryForm')
			this.getList()
			},

            //教育视频观看记录列表
            getList(){
                var queryParams = {};
                queryParams.page = this.currentPage;
				queryParams.limit = this.pageSize;
				queryParams.vid = this.queryParamForm.vid

                getList(queryParams).then(res => {
					if(res.data != null){
						this.videoDataList = res.data.list

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
			handleAdd() {
				this.currentMenu = {}
				this.actionType = 'create'
				this.showCreate = true;
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

			//查询全部视频模板名称
			selectNameList(){
				selectNameList().then(response => {
				this.videoNameList = response.data
				this.loading = false
				}).catch(() => {
				this.loading = false
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
