<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
					style="background-color: white;border-top: 0px solid #e6e6e6;">

					<el-form-item v-if="canHandle('project')" label="项目名称" prop="projectId">
						<el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small"
							style="width: 200px">
							<el-option v-for="item in projectInfoList" :key="item.id" :label="item.name"
								:value="item.id" />
						</el-select>
					</el-form-item>
					<el-form-item label="设备编号" prop="equCode">
						<el-input v-model="queryParams.equCode" placeholder="请输入设备编号" clearable size="small" />
					</el-form-item>
					<el-form-item label="身份证号" prop="idCard">
						<el-input v-model="queryParams.idCard" placeholder="请输入身份证号" clearable size="small" />
					</el-form-item>

					<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;"
						@click="getList">搜索</el-button>
					<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

					<el-button type="primary" style="float:right;margin-top: 4px;margin-right: 20px;"
						@click="excelImport">导入</el-button>

				</el-form>

			</div>
			<el-table ref="menuTable" :data="paperRuleDataList" style="width: 100%" :height="tableHeight" border
				v-loading="loading" @sort-change="sortChange">
				<el-table-column type="index" align="center" label="序号" width="60"></el-table-column>
				<el-table-column v-if="canHandle('project')" prop="projectName" align="center" label="项目名称" width="140" sortable="custom">
				</el-table-column>
				<el-table-column prop="assetName" align="center" label="设备名称" width="140" sortable="custom"></el-table-column>
				<el-table-column prop="equCode" align="center" label="设备编号" width="140" sortable="custom"></el-table-column>
				<el-table-column prop="typeName" align="center" label="设备类型" width="110" sortable="custom"></el-table-column>
				<el-table-column prop="name" align="center" label="司机姓名" width="140" sortable="custom"></el-table-column>
				<el-table-column prop="idCard" align="center" label="身份证号" width="170" sortable="custom"></el-table-column>
				<el-table-column prop="phone" align="center" label="联系方式" width="120" sortable="custom"></el-table-column>
				<el-table-column prop="cerName" align="center" label="证书名称" width="140" show-overflow-tooltip sortable="custom"></el-table-column>
				<el-table-column prop="cerNumber" align="center" label="证书编号" width="140" show-overflow-tooltip sortable="custom">
				</el-table-column>
				<el-table-column prop="cerUseendDate" align="center" label="有效期止" width="120" sortable="custom"></el-table-column>

				<el-table-column prop="startingDate" align="center" label="上岗时间" width="160" sortable="custom"></el-table-column>
				<el-table-column align="center" label="识别照片" width="120">
					<template slot-scope="scope">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="scope.row.imgCardUrl" style="height: 53px" />
					</template>
				</el-table-column>


			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize"
					:total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper"
					@size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
		</div>


		<!-- 批量导入 -->
		<bulk-import-user :show="bulkImportShow" :param="param" @close="bulkImportClose" />

	</div>

</template>

<script>
	import {
		mapGetters
	} from "vuex"; //权限
	import {
		getList,
		getProjectList
	} from '@/views/build/equipment/api/driverIdent'

	import {
		downloadExcelWithResData
	} from '@/utils/index'
	import BulkImportUser from '@/components/importModel'
  import { sortColumnNoToLowerCase } from '@/utils/index';

	export default {
		//机械设备司机识别
		name: 'driverIdent',
		components: {

			BulkImportUser
		},
		data() {
			return {

				loading: false, // 加载动画
				tableHeight: document.documentElement.clientHeight - 230,

        queryParams: {}, //搜索

				// 分页
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 30, 40],
				total: 0,

				paperRuleDataList: [], //机械设备司机识别列表


				// 批量导入
				bulkImportShow: false,
				param: {}, //导入使用
				// 项目信息列表
				projectInfoList: [],


			}
		},
		computed: {
			//权限 start
			...mapGetters(['allAuth']),
			//权限 end

		},
		created() {

			//  证书类型 字典项
			this.getDicts('b_cerCategoryType').then(response => {
				this.cerCategoryTypeOptions = response.data
			})

			// 项目列表
			this.getProjectInfoList()

			this.getList()

		},

		mounted() {

		},
		methods: {

			// 字典状态字典翻译
			cerCategoryTypeFormat(row, column) {
				return this.selectDictLabel(this.cerCategoryTypeOptions, row.cerCategoryType)
			},

			/** 重置按钮操作 */
			resetQuery() {
				this.queryParams = {}
				// this.resetForm('queryForm')
				this.getList()
			},

			// 获取项目信息列表
			getProjectInfoList() {
				getProjectList().then(res => {
					this.projectInfoList = res.data || []
				})
			},

			//周界防护报警列表
			getList() {

				var queryParams = {};
				queryParams.page = this.currentPage;
				queryParams.limit = this.pageSize;

				queryParams.equCode = this.queryParams.equCode
				queryParams.idCard = this.queryParams.idCard
				queryParams.projectId = this.queryParams.projectId
        queryParams.fieldName = this.queryParams.fieldName
        queryParams.sortingType = this.queryParams.sortingType
				getList(queryParams).then(res => {
					if (res.data != null) {
						this.paperRuleDataList = res.data.list

						if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage &&
							this.currentPage > 1) {
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

			//按钮操作权限
			canHandle(type) {
				return this.allAuth.driverIdent ? this.allAuth.driverIdent[type] : false;
				//return this.allAuth.roster ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
			},


			//导入
			excelImport() {
				let lists = [
					"设备编号(*)",
					"身份证号(*)",
					"上岗时间(*)",
					"下岗时间"
				]
				//示例数据
				let dataLists = [
					"00201",
					"632323190605262063",
					"2021-07-26 15:47:14",
					"2021-08-02 15:47:14"
				]

				this.param = {
					title: "机械设备司机识别导入模板(*)为必填项",
					excelTitle: "机械设备司机识别导入模板",
					colNames: lists,
					sampleData: dataLists,
					url: 'build/equipmentDriverIdent/excelImport'
				}
				this.bulkImportShow = true
			},
			// 关闭导入组件
			bulkImportClose() {
				this.bulkImportShow = false
				this.getList()
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
			  var list = []
        list.push("typeName");
        list.push("workLength");
        sortColumnNoToLowerCase(this,column,list);
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
