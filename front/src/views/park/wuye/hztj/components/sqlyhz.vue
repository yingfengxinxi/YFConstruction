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
			<el-table-column label="申请来源" align="center" prop="sqly" />
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
					sqly: '上门',
					bxzs: '756',
					dfp: '2',
					wcl: '2',
					clz: '665',
					ywg: '87',
					ycx: '0',
					wgl: '11.51%',
					hfs: '83',
					mys: '77',
					myl: '10.19%'
				},{
					sqly: '呼叫中心',
					bxzs: '418',
					dfp: '0',
					wcl: '1',
					clz: '306',
					ywg: '111',
					ycx: '0',
					wgl: '26.56%',
					hfs: '108',
					mys: '103',
					myl: '24.64%'
				}, {
					sqly: '网上申报',
					bxzs: '3',
					dfp: '0',
					wcl: '0',
					clz: '1',
					ywg: '2',
					ycx: '0',
					wgl: '66.67%',
					hfs: '1',
					mys: '1',
					myl: '33.33%'
				}, {
					sqly: '微信端',
					bxzs: '89',
					dfp: '3',
					wcl: '1',
					clz: '16',
					ywg: '69',
					ycx: '0',
					wgl: '77.53%',
					hfs: '2',
					mys: '10',
					myl: '11.24%'
				}, {
					sqly: 'APP',
					bxzs: '18',
					dfp: '1',
					wcl: '2',
					clz: '8',
					ywg: '7',
					ycx: '0',
					wgl: '38.89%',
					hfs: '1',
					mys: '0',
					myl: '0.00%'
				},
				{
					sqly: '现场',
					bxzs: '62',
					dfp: '0',
					wcl: '1',
					clz: '55',
					ywg: '6',
					ycx: '0',
					wgl: '9.86%',
					hfs: '4',
					mys: '3',
					myl: '4.84%'
				},
				{
					sqly: '其他',
					bxzs: '5',
					dfp: '0',
					wcl: '0',
					clz: '3',
					ywg: '2',
					ycx: '0',
					wgl: '40.00%',
					hfs: '2',
					mys: '3',
					myl: '40.00%'
				},]
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