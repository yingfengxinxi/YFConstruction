<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
             style="background-color: white;border-top: 0px solid #e6e6e6;">
				
				<el-form-item label="报警时间" prop="dispatchTime">
					<el-date-picker
						v-model="queryParamForm.dispatchTime"
						type="datetimerange"
						range-separator="-"
						value-format="yyyy-MM-dd HH:mm:ss"
						start-placeholder="开始日期"
						end-placeholder="结束日期">
					</el-date-picker>
				</el-form-item>

				<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          		<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

				<el-button class="export-btn" style="float:right;margin-top: 4px;margin-right: 20px;" @click="excelImport">导入</el-button>
				<!-- <el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addPaperRules">新建</el-button> -->
				
				</el-form>

			</div>
			<el-table ref="menuTable" :data="paperRuleDataList" style="width: 100%" :height="tableHeight" border v-loading="loading" >
				<el-table-column type="index" align="center" label="序号" width="90"></el-table-column>
				<el-table-column prop="boundaryBlockCode" align="center" label="防护网编号" width="110"></el-table-column>
<!--				<el-table-column prop="type" align="center" label="类型" width="120" :formatter="typeFormat"></el-table-column>-->
				<el-table-column prop="name" align="center" label="边界名称" ></el-table-column>
				<el-table-column prop="monolithicEngineering" align="center" label="报警位置" ></el-table-column>
				<el-table-column prop="alarmTime" align="center" label="报警时间" width="180"></el-table-column>
				<el-table-column prop="alarmText" align="center" label="报警内容"  ></el-table-column>
<!--				<el-table-column prop="alarmType" align="center" label="报警类型" width="120" :formatter="alarmTypeFormat"></el-table-column>-->
				<el-table-column prop="handlePeopleName" align="center" label="处理人" width="120"></el-table-column>
				<el-table-column prop="handleTime" align="center" label="处理时间" width="180"></el-table-column>
				<el-table-column prop="handleMeasures" align="center" label="处理措施" ></el-table-column>
				<el-table-column prop="handleConclusion" align="center" label="处理后结论" width="120"></el-table-column>
			
				<el-table-column fixed="right" align="center" label="操作" width="180">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">处理</el-button>
						<!-- <el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button> -->
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
		

		<!-- 周界防护报警 新建-->
		<CreateMenu v-if="showCreate" 
					:menuForm="currentMenu" 
					:actionType="actionType" 
					@save="getList" 
					@hiden-view="showCreate=false" />

		<!-- 批量导入 -->
		<bulk-import-user
			:show="bulkImportShow"
			:param="param"
			@close="bulkImportClose"
			/>

	</div>

</template>

<script>

	import {getList, selectById, delById } from '@/views/build/safety/api/blockAlarm'

	import CreateMenu from './CreateMenu'

	import { downloadExcelWithResData } from '@/utils/index'
	import BulkImportUser from '@/components/importModel'

	export default {
		//周界防护报警
		name: 'blockAlarm',
		components: {
			CreateMenu,
			BulkImportUser
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

				currentMenu: {}, //当前周界防护报警
				paperRuleDataList: [], //周界防护报警列表
				
				actionType:'',
				//新增
				showCreate: false,
				// 批量导入
				bulkImportShow: false,
				param:{},

				typeOptions:[],//类型（基坑边界、施工边界）
				alarmTypeOptions:[],//报警类型

			}
		},
		computed: {
			
		},
		created(){
			//
			// //类型（基坑边界、施工边界）
			// this.getDicts('b_safety_boundary_block_type').then(response=>{
			// 	this.typeOptions = response.data
			// })
			//
			// //报警类型
			// this.getDicts('b_safety_blockAlarm_alarmType').then(response=>{
			// 	this.alarmTypeOptions = response.data
			// })
			//
			this.getList()
			
		},

		mounted() {
			
		},
		methods: {	
				
            
			/** 重置按钮操作 */
			resetQuery() {
			// this.queryParamForm = {}
			// // this.resetForm('queryForm')
			// this.getList()
			},

            //周界防护报警列表
            getList(){
				
                // var queryParams = {};
                // queryParams.page = this.currentPage;
				// queryParams.limit = this.pageSize;
				//
				// let dispatchTime = this.queryParamForm.dispatchTime
				// if(dispatchTime != null && dispatchTime != undefined && dispatchTime != ''){
				// 	queryParams.alarmTimeStart = dispatchTime[0]
				// 	queryParams.alarmTimeEnd = dispatchTime[1]
				// }

				this.paperRuleDataList =  [
					{
						"boundaryBlockCode": "ZZ002",
						"createUserId": 14847,
						"handleTime": "2021-06-17 16:53:13",
						"updateUserId": null,
						"alarmTime": "2021-06-17 17:43:40",
						"alarmText": "人员入侵",
						"handleConclusion": "报警消除",
						"updateTime": "2021-06-17 16:53:16",
						"batchId": "bb701e8c-c2d1-40ff-8910-d14f6b5eac1c",
						"type": "1",
						"handlePeopleId": null,
						"protectiveNetCode": "ZZ002",
						"alarmType": "1",
						"createTime": "2021-06-17 16:52:40",
						"tenantId": 1,
						"name": "园区A座南侧围栏#3",
						"handlePeopleName": "李青",
						"handleMeasures": "已处理",
						"id": 230,
						"boundaryBlockId": null,
						"projectId": 4,
						"monolithicEngineering": "园区A座南侧"
					},
					{
						"boundaryBlockCode": "ZZ002",
						"createUserId": 14773,
						"handleTime": "2021-06-23 00:00:00",
						"updateUserId": null,
						"alarmTime": "2021-06-17 17:43:40",
						"alarmText": "人员入侵",
						"handleConclusion": "已解决",
						"updateTime": "2021-06-17 16:47:40",
						"batchId": "be49b404-2cbc-4dcb-abe5-ab7d8d8f7022",
						"type": "1",
						"handlePeopleId": null,
						"protectiveNetCode": "ZZ002",
						"alarmType": "1",
						"createTime": "2021-06-17 16:23:05",
						"tenantId": 1,
						"name": "园区A座南侧围栏#3",
						"handlePeopleName": "王健",
						"handleMeasures": "已驱赶",
						"id": 228,
						"boundaryBlockId": null,
						"projectId": 4,
						"monolithicEngineering": "园区A座南侧"
					},
					{
						"boundaryBlockCode": "ZZ002",
						"createUserId": 14773,
						"handleTime": null,
						"updateUserId": null,
						"alarmTime": "2020-11-12 15:47:14",
						"alarmText": "发现违规人员进入",
						"handleConclusion": "已解决",
						"updateTime": "2021-06-17 15:46:49",
						"batchId": "8e49c377-818b-480e-9022-a28bb964f6a5",
						"type": "1",
						"handlePeopleId": null,
						"protectiveNetCode": "ZZ002",
						"alarmType": "1",
						"createTime": "2021-06-16 16:55:03",
						"tenantId": 1,
						"name": "园区A座南侧围栏#3",
						"handlePeopleName": "张三",
						"handleMeasures": "人员驱逐出危险区域",
						"id": 216,
						"boundaryBlockId": 2,
						"projectId": 4,
						"monolithicEngineering": "园区A座南侧"
					},
				]
				this.loading = false
                // getList(queryParams).then(res => {
				// 	if(res.data != null){
				// 		this.paperRuleDataList = res.data.list
				//
				// 		if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
				// 			this.currentPage = this.currentPage - 1
				// 			this.getList()
				// 		} else {
				// 			this.total = res.data.totalRow
				// 			this.loading = false
				// 		}
				// 		this.$nextTick(() => {
				// 			document.querySelector('.el-table__body-wrapper').scrollTop = 1
				// 		})
				//
				// 	}
                //
                //     this.loading = false
				//
                // })
            },
            
            //新增
			addPaperRules() { 
				this.currentMenu = {}
				this.actionType = 'create'
				this.showCreate = true;
			},
			
			//导入
			excelImport(){
				let lists = [
					"边界区块防护网编号(*)",
					"报警时间(*)",
					"报警内容(*)",
					"报警类型(*)"
				]
				//示例数据
				let dataLists = [
					"1-1",
					"2021-01-10 15:47:14",
					"报警内容示例数据",
					"1"//违法入侵
				]

				this.param = {
					title:"周界防护报警导入模板(*)为必填项",
					excelTitle:"周界防护报警导入模板",
					colNames:lists,
					sampleData:dataLists,
					url:'build/safetyBlockAlarm/excelImport'
				}
				this.bulkImportShow = true
			},
			// 关闭导入组件
			bulkImportClose() {
				this.bulkImportShow=false
				this.getList()
			},
            
			async handleClick(event, scope) {
				//此处需同步调用
				var queryParams = {page:1,limit:10,id:scope.row.id};

				// await selectById(scope.row.id)
				// 	.then(res => {
				// 		this.currentMenu = res.data;
				// 	})

				// await getList(queryParams).then(res=>{
				// 	console.log(res.data)
				// 	this.currentMenu = res.data.list[0];
				// })
				this.currentMenu = {
					"boundaryBlockCode": "ZZ002",
					"createUserId": 14847,
					"handleTime": "2021-06-17 16:53:13",
					"updateUserId": null,
					"alarmTime": "2021-06-17 17:43:40",
					"alarmText": "人员入侵",
					"handleConclusion": "报警消除",
					"updateTime": "2021-06-17 16:53:16",
					"batchId": "bb701e8c-c2d1-40ff-8910-d14f6b5eac1c",
					"type": "1",
					"handlePeopleId": null,
					"protectiveNetCode": "ZZ002",
					"alarmType": "1",
					"createTime": "2021-06-17 16:52:40",
					"tenantId": 1,
					"name": "园区A座南侧围栏#3",
					"handlePeopleName": "李青",
					"handleMeasures": "已处理",
					"id": 230,
					"boundaryBlockId": null,
					"projectId": 4,
					"monolithicEngineering": "园区A座南侧"
				}

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

			//类型
			typeFormat(row, column){
				return this.selectDictLabel(this.typeOptions, row.type)
			},
			//报警类型
			alarmTypeFormat(row, column){
				return this.selectDictLabel(this.alarmTypeOptions, row.alarmType)
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
