<template>
	<div class="main">
	
		<div class="main-table-header">

			<el-form ref="queryForm" v-show="true" :inline="true" label-width="80px"
			style="background-color: white;border-top: 0px solid #e6e6e6;">

			<el-form-item  label="选择年月" class='special' prop="planMonth" >
					<el-date-picker
						class="timecom"
						v-model="queryParamForm.planMonth"
						type="month"
						placeholder="请选择"
						align="right"
						value-format="yyyy-MM"
						>
					</el-date-picker>
					
					<el-button class="xr-btn--blue" type="primary" icon="el-icon-search" style="margin-top: 4px;" @click="getList">搜索</el-button>
          			<el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>

			</el-form-item>
			
			
			<el-button class="export-btn" style="float:right;margin-top: 4px;margin-right: 20px;" @click="exportExcel">导出</el-button>
			<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addPaperRules">新建</el-button>
			

			</el-form>

		</div>
		<el-table highlight-current-row ref="menuTable" :data="tableDataList" style="width: 100%" :height="tableHeight" border v-loading="loading" >
			<el-table-column type="index" align="center" label="序号" width="70"></el-table-column>
			<el-table-column prop="planMonth" align="center" label="年月"></el-table-column>
			<el-table-column prop="leaserName" align="center" label="租赁商名称" width="140"></el-table-column>
			<el-table-column prop="turnoverName" align="center" label="周转材名称" width="120"></el-table-column>
			<el-table-column prop="billingMethod" align="center" label="计费方式"></el-table-column>
			<el-table-column prop="feeType" align="center" label="费用类型"></el-table-column>
			<el-table-column prop="totalRentalPrice" align="center" label="租赁总价"></el-table-column>
			<el-table-column prop="totalRentalPriceNotTax" align="center" label="租赁总价不含税" width="140"></el-table-column>
			<el-table-column prop="totalRentalPriceTax" align="center" label="租赁总价税金" width="120"></el-table-column>
			<el-table-column prop="collectionPrice" align="center" label="归集金额含税" width="120"></el-table-column>
			<el-table-column prop="collectionPriceNotTax" align="center" label="归集金额不含税" width="140"></el-table-column>
			<el-table-column prop="ifCollection" align="center" label="是否归集" :formatter="ifCollectionFormat"></el-table-column>
			<el-table-column prop="planMonthDetailId" align="center" label="月计划节点" width="140" :formatter="planMonthIdAndNameFormmater"></el-table-column>
			
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

		<!-- 月计划周转材租赁明细 新建-->
		<CreateMenu v-if="showCreate" 
					:form="currentMenu" 
					:actionType="actionType" 
					:queryForm="queryForm"
					@save="getList" 
					@hiden-view="showCreate=false" />

	</div>

</template>

<script>

	import {getList, selectById, delById , exportExcel} from '@/views/build/progress/api/monthTurnmatLea'
    import {selectIdAndName , planMonthIdAndNameFormmater} from '@/views/build/progress/api/planMonth'
	import CreateMenu from './CreateMenu'

	import { downloadExcelWithResData } from '@/utils/index'

	export default {
		//周转材租赁明细
		name: 'monthTurnmatLea',
		components: {
			CreateMenu,
		},
		props:{
			queryForm:{
				planMonth:'',
				mid:''
			}
		},
		data() {
			return {
		
				loading: false, // 加载动画
				tableHeight:document.documentElement.clientHeight - 280,
				
				queryParamForm:{},//搜索

                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,

				currentMenu: {}, //当前月计划周转材租赁明细
				tableDataList: [], //月计划周转材租赁明细列表
				
				actionType:'',
				//新增
				showCreate: false,

				ifCollectionDict:[],//是否归集

			}
		},
		computed: {
			
		},
		created(){

			//  是否归集
			this.getDicts('sys_yes_no').then(response => {
				this.ifCollectionDict = response.data
			})

			this.selectIdAndName()//月计划id和name的list
	  
			this.getList()
			
		},

		mounted() {
			
		},
		methods: {		
            
			/** 重置按钮操作 */
			resetQuery() {
			this.queryParamForm = {}
			// this.resetForm('queryForm')
			this.getList()
			},

            //月计划周转材租赁明细列表
            getList(){
				
                var queryParams = {};
                queryParams.page = this.currentPage;
				queryParams.limit = this.pageSize;
				queryParams.planMonth = this.queryParamForm.planMonth;
				

                getList(queryParams).then(res => {
					if(res.data != null){
						this.tableDataList = res.data.list

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

			//是否归集
			ifCollectionFormat(row, column){
				return this.selectDictLabel(this.ifCollectionDict, row.ifCollection)
			},

			//月计划id和name的list
			selectIdAndName(){
				selectIdAndName(this.queryForm.mid).then(res=>{
					this.planMonthIdAndNameList = res.data
				})
			},

			planMonthIdAndNameFormmater(row, column){
				
				return planMonthIdAndNameFormmater(row.planMonthDetailId,this.planMonthIdAndNameList)
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

	@import 'src/views/styles/table.scss';
</style>
