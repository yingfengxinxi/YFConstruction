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
			<el-table-column label="处理人" align="center" prop="clr" />
			<el-table-column label="受理总数" align="center" prop="slzs" />
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
					clr: '李林',
					slzs: '756',
					wcl: '2',
					clz: '665',
					ywg: '87',
					ycx: '0',
					wgl: '11.51%',
					hfs: '83',
					mys: '77',
					myl: '10.19%'
				},{
					clr: '张强',
					slzs: '418',
					wcl: '1',
					clz: '306',
					ywg: '111',
					ycx: '0',
					wgl: '26.56%',
					hfs: '108',
					mys: '103',
					myl: '24.64%'
				}, {
					clr: '刘桂',
					slzs: '3',
					wcl: '0',
					clz: '1',
					ywg: '2',
					ycx: '0',
					wgl: '66.67%',
					hfs: '1',
					mys: '1',
					myl: '33.33%'
				} ]
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