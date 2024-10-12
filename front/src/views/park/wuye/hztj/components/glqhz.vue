<template>
	<div>
		<el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
			<el-form-item label="" prop="time">
				<el-date-picker v-model="queryParams.time" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
				</el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-button type="primary"  size="mini" @click="handleQuery">导出EXCEL</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" :border="true" :height="tableHeight">
			<el-table-column label="管理区" align="center" prop="glq" />
			<el-table-column label="报修总数" align="center" prop="bxzs" />
			<el-table-column label="待分配" align="center" prop="dfp" />
			<el-table-column label="未处理" align="center" prop="wcl" />
			<el-table-column label="处理中" align="center" prop="clz" />
			<el-table-column label="已完工" align="center" prop="ywg" />
			<el-table-column label="已撤销" align="center" prop="ycx" />
			<el-table-column label="完工率" align="center" prop="wgl" />
			<el-table-column label="回访数" align="center" prop="hfs" />
			<el-table-column label="满意数" align="center" prop="mys" />
			<el-table-column label="满意率" align="center" prop="myl" />
		</el-table>
		<div class="p-contianer">
			<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper"  />
		</div>
		<div class="sm">
			<div>说明:</div>
			<div>报修总数不统计已删除已作废的</div>
			<div>处理中=处理中+已处理+已确认未完成</div>
			<div>已完工=已确认已完成+已关闭</div>
			<div>完工率=已完工/报修总数</div>
			<div>满意率=满意数/报修总数</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				queryParams: {},
				tableHeight: document.documentElement.clientHeight - 500, // 表的高度
				// 分页
				currentPage: 1,
				pageSize: 10,
				total: 0,
				dataList: [{
					glq: '001',
					bxzs: '1',
					dfp: '1',
					dcl: '0',
					wcl: '1',
					clz: '0',
					ywg: '0',
					ycx: '0',
					wgl: '0.00%',
					hfs: '0',
					mys: '0',
					myl: '0.00%'
				},{
					glq: '002',
					bxzs: '1',
					dfp: '0',
					dcl: '0',
					wcl: '0',
					clz: '0',
					ywg: '1',
					ycx: '0',
					wgl: '100%',
					hfs: '0',
					mys: '1',
					myl: '100%'
				},  ]
			}
		},
		methods: {
			handleQuery() {

			}
		}
	}
</script>

<style scoped>
	.sm div{
		margin-bottom: 10px;
	}
</style>