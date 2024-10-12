<template>
	<div class="main">

		<div class="main-body">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="状态" prop="subcontractorId">
					<el-select v-model="queryParams.subcontractorId" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="item in subcontractorOptions" :key="item.label" :label="item.label" :value="item.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="工单大类" prop="workTypeId">
					<el-select v-model="queryParams.workTypeId" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in workTypeOption" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="报修人" prop="teamLeaderName">
					<el-input v-model="queryParams.teamLeaderName" placeholder="请输入报修人" clearable size="small" @keyup.enter.native="handleQuery" />
				</el-form-item>
				<el-form-item label="管理区" prop="Administration">
					<el-select v-model="queryParams.Administration" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in Administration" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="楼栋" prop="Building">
					<el-select v-model="queryParams.Building" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in Building" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="报修来源" prop="source">
					<el-select v-model="queryParams.source" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in source" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="分配人" prop="distribution">
					<el-select v-model="queryParams.distribution" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in distribution" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="维修人" prop="repair">
					<el-select v-model="queryParams.repair" placeholder="请选择" clearable size="small" style="width: 200px">
						<el-option v-for="option in repair" :key="option.id" :label="option.label" :value="option.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="报修时间" prop="time">
					<el-date-picker v-model="queryParams.time" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
				</el-form-item>
				<el-form-item style="float: right">
					<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
				</el-form-item>
			</el-form>
			<el-table :data="dataList" :height="tableHeight">
				<el-table-column label="报修单号" align="center" prop="code" width="200px" />
				<el-table-column label="状态" align="center" prop="status" width="120px">
					<template slot-scope="scope">
						<el-tag type="success" v-if='scope.row.status==1'>完成</el-tag>
						<el-tag type="info" v-else-if='scope.row.status==2'>处理中</el-tag>
						<el-tag type="warning" v-else='scope.row.status==3'>已关闭</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="报修人" align="center" prop="name" />
				<el-table-column label="联系电话" align="center" prop="phone" width="200px" />
				<el-table-column label="报修时间" align="center" prop="bxtime"width="120px"  />
				<el-table-column label="分配人	" align="center" prop="fpr" />
				<el-table-column label="维修人" align="center" prop="wxr" />
				<el-table-column label="工单时限" align="center" prop="gdsx" width="200px" />
				<el-table-column label="工单大类" align="center" prop="gddl" />
				<el-table-column label="工单小类" align="center" prop="gdxl" />
				<el-table-column label="报修来源" align="center" prop="bxly" />
				<el-table-column label="要求处理事项" align="center" prop="yqclsx" width="200px" />
				<el-table-column label="回单情况说明" align="center" prop="hdqksm" width="200px" />
				<el-table-column label="管理区" align="center" prop="glq" />
				<el-table-column label="楼栋" align="center" prop="ld" />
				<el-table-column label="回访人员" align="center" prop="hfry" />
				<el-table-column label="回访时间" align="center" prop="hfsj" width="200px" />
				<el-table-column label="评价" align="center" prop="pj" />
				<el-table-column label="评价内容" align="center" prop="pjnr" width="200px" />
				<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="handleUpdate(scope.row)">编辑</el-button>
						<el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
						<el-button size="mini" type="text" >派工</el-button>
						<el-button size="mini" type="text" @click="co(scope.row)">转交</el-button>
						<el-button size="mini" type="text" @click="description(scope.row)">添加说明</el-button>
						<el-button size="mini" type="text" @click="handleDelete(scope.row)">作废</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
			</div>
			<!-- 添加或修改参数配置对话框 -->
			<el-dialog :title="title" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false">
				<el-form ref="form" :model="form" :rules="rules" label-width="100px">
					<el-row>
						<el-col :span="24">
							<el-form-item label="报修区域" prop="region">
								<el-radio-group v-model="form.region">
									<el-radio label="户内"></el-radio>
									<el-radio label="工区"></el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="报修位置" prop="position1">
								<select-tree :options="positiondata" @selected="selectPreNode" v-model="form.position1"  :props="defaultProps" />
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="报修地点" prop="place">
								<el-input type="text" v-model="form.place" placeholder='请输入报修地点' />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24">
							<el-form-item label="报修详情" prop="yqclsx">
								<el-input type="textarea" v-model="form.yqclsx"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="报修人" prop="name">
								<el-input type="text" v-model="form.name" placeholder='请输入报修人' />
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="联系电话" prop="phone">
								<el-input type="text" v-model="form.phone" placeholder='请输入联系电话' />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="预约时间" prop="yysj">
								<el-date-picker
						      v-model="form.yysj"
						      type="datetime"
						      placeholder="选择日期时间">
						    </el-date-picker>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="报修来源" prop="bxly">
								<el-select v-model="form.bxly" placeholder="请选择" clearable size="small" style="width: 200px">
									<el-option v-for="option in source" :key="option.id" :label="option.label" :value="option.value" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="工单时限" prop="gdsx">
								<el-select v-model="form.gdsx" placeholder="请选择" clearable size="small" style="width: 200px">
									<el-option v-for="option in timeWork" :key="option.id" :label="option.label" :value="option.value" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="工单大类" prop="gddl">
								<el-select v-model="form.gddl" placeholder="请选择" clearable size="small" style="width: 200px">
									<el-option v-for="option in workTypeOption" :key="option.id" :label="option.label" :value="option.value" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="工单时限" prop="gdzl">
								<el-select v-model="form.gdzl" placeholder="请选择" clearable size="small" style="width: 200px">
									<el-option v-for="option in workzl" :key="option.id" :label="option.label" :value="option.value" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="录单人" prop="ldr">
									<el-input type="text" v-model="form.name" placeholder='请输入报修人' />
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="录单时间" prop="ldsj">
								<el-date-picker
						      v-model="form.ldsj"
						      type="datetime"
						      placeholder="选择日期时间">
						    </el-date-picker>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>			
				<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="submitForm">确 定</el-button>
					<el-button @click="cancel">取 消</el-button>
				</div>
			</el-dialog>
			<!-- 选择劳务人员信息弹出层 -->
			<el-dialog :title="title2" :visible.sync="open2" width="700px" append-to-body>
					<el-form ref="form2" :model="form2" :rules="rules2" label-width="100px">
							<el-row>
						<el-col :span="12">
							<el-form-item label="转交给" prop="zj">
								<el-select v-model="form2.zj" placeholder="请选择" clearable size="small" style="width: 200px">
									<el-option v-for="option in colist" :key="option.id" :label="option.label" :value="option.value" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					</el-form>	
					<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="submitForm2">确 定</el-button>
					<el-button @click="cancel2">取 消</el-button>
				</div>
			</el-dialog>
			<el-dialog title="添加说明" :visible.sync="open3" width="700px" append-to-body>
					<el-form ref="form3" :model="form3" :rules="rules3" label-width="100px">
							<el-row>
						<el-col :span="12">
							<el-form-item label="维修说明" prop="tjsm">
								<el-input type="textarea" v-model="form3.tjsm"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					</el-form>	
					<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="submitForm3">确 定</el-button>
					<el-button @click="cancel3">取 消</el-button>
				</div>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	import SelectTree from '@/components/SelectTree'
	import { selectTeamList, addTeam, editTeam, getTeamById, deleteTeam, selectTeamLeaderList, selcetIdsAndWorktype, selectList } from '@/api/build/labour'
	import { selectAreaList } from '@/api/common'

	import Reminder from '@/components/Reminder'
	import XrHeader from '@/components/XrHeader'
	import request from '@/utils/request'
	import vPinyin from '@/utils/vuepy.js';
	export default {
		name: 'team',
		components: {
			Reminder,
			SelectTree,
			XrHeader
		},
		data() {
			return {
				colist:[
					{
						value: '1',
						label: '诺杯项目'
					},
					{
						value: '2',
						label: '德诺'
					},
				],
				upResName: '',
				// 遮罩层
				loading: true,
				// 显示搜索条件
				showSearch: true,
				// 弹出层显示搜索条件
				showSearch2: true,
				// 分页
				currentPage: 1,
				pageSize: 10,
				pageSizes: [10, 20, 30, 40],
				total: 0,
				// 班组长信息弹出框分页
				currentPage2: 1,
				pageSize2: 5,
				pageSizes2: [5, 10, 20, 30, 40],
				total2: 0,
				defaultProps: {
					children: 'children',
					label: 'label',
					value:'id'
				},
				//
				workzl:[
					{
						value: '1',
						label: '一般单'
					},
					{
						value: '2',
						label: '加急单'
					},
				],
				positiondata: [{
					label: '诺贝项目',
					children: [{
						label: '办公',
						id:1,
						children: [{
							label: '221',
							id:1-1,
						}]
					}]
				}, {
					label: '二期',
					id:2,
					children: [{
						label: '三大队',
						id:2-1,
						children: [{
							id:2-1-1,
							label: ' 2-1-1'
						}]
					}, {
						label: '四大队',
						id:2-2,
						children: [{
							id:2-2-1,
							label: '2-2-1'
						}]
					}]
				}],
				// 表格数据
				tableHeight: document.documentElement.clientHeight - 330, // 表的高度
				dataList: [{
					code: '10012451011',
					status: 1,
					name: '小王',
					phone: 17783588011,
					bxtime: '2021-05-06',
					fpr: '小刘',
					wxr: '张工',
					gdsx: '8小时内处理（适用于一般单）',
					gddl: '保修业务',
					gdxl: '一般单',
					bxly: '上门',
					yqclsx: '漏水',
					hdqksm: '已维修',
					glq: '诺怀项目',
					ld: 'A2栋',
					hfry: '李伟',
					hfsj: '2021-06-22',
					pj: '好评'
				}],

				// 弹出层表格数据
				dataList2: [],
				// 弹出层标题
				title: '',
				// 是否显示弹出层
				open: false,
				// 弹出层标题
				title2: '转交',
				// 是否显示个人信息弹出层
				open2: false,
				form2:{
					
				},
				// 提交平台状态字典数据
				sendStatusDict: [],
				// 查询参数
				queryParams: {
					page: '',
					limit: '',
					subcontractorId: '',
					Administration: ''

				},
				// 弹出层查询参数
				queryParams2: {
					page: '',
					limit: '',
					ifTeamLeader: 'Y'
				},
				open3:false,
				// 表单参数
				form: {
					position1:''
				},
				form3:{
					
				},
				// 判断对话框是新建还是修改 true为新建 false为修改
				dialogType: undefined,
				//工单时限
				timeWork:[
				{
						value: '1',
						label: '8小时内处理'
					},
					{
						value: '2',
						label: '4小时内处理'
					},
					{
						value: '3',
						label: '24小时内处理'
					},
					{
						value: '4',
						label: '疑难问题维修'
					},
				],
				//维修人
				repair: [{
						value: '1',
						label: '张三'
					},
					{
						value: '2',
						label: '张强'
					},
					{
						value: '3',
						label: '李斯'
					},
					{
						value: '4',
						label: '张泽'
					},
					{
						value: '5',
						label: '王望'
					},
				],
				//分配人
				distribution: [{
						value: '1',
						label: '企业版'
					},
					{
						value: '2',
						label: '张强'
					},
					{
						value: '3',
						label: '李斯'
					},
					{
						value: '4',
						label: '张泽'
					},
					{
						value: '5',
						label: '王望'
					},
				],
				//报修来源
				source: [{
						value: '1',
						label: '上门'
					},
					{
						value: '2',
						label: '呼叫中心'
					},
					{
						value: '3',
						label: '网上申报'
					},
					{
						value: '4',
						label: '微信端'
					},
					{
						value: '5',
						label: 'APP'
					},
					{
						value: '6',
						label: '现场'
					},
					{
						value: '7',
						label: '其他'
					},
				],
				//管理区
				Administration: [{
					value: '1',
					label: '诺贝项目'
				}],
				//楼栋
				Building: [{
						value: '1',
						label: 'A01栋'
					}, {
						value: '2',
						label: 'A02栋'
					}, {
						value: '3',
						label: 'A03栋'
					},
					{
						value: '4',
						label: 'B01栋'
					}, {
						value: '5',
						label: 'B02栋'
					}, {
						value: '6',
						label: 'B03栋'
					},
				],
				//报修人
				workTypeOption: [{
					value: '1',
					label: '报修业务'
				}, {
					value: '2',
					label: '充值业务'
				}, {
					value: '3',
					label: '住户维修'
				}],
				// 分包商下拉框
				subcontractorOptions: [{
					value: '选项1',
					label: '全部'
				}, {
					value: '选项2',
					label: '待分配'
				}, {
					value: '已分配',
					label: '处理中'
				}, {
					value: '选项4',
					label: '已处理'
				}, {
					value: '选项5',
					label: '已确认完成'
				}, {
					value: '选项6',
					label: '已确未完成'
				}, {
					value: '选项7',
					label: '已关闭'
				}, {
					value: '选项8',
					label: '已撤销'
				}, {
					value: '选项9',
					label: '已作废'
				}],
				// 表单校验
				rules3:{
					tjsm:[{
						required: true,
						message: '维修说明不得为空',
						trigger: 'change'
					}]
				},
				rules2:{
					zj:[{
						required: true,
						message: '转交不得为空',
						trigger: 'change'
					}]
				},
				rules: {
					position1: [{
						required: true,
						message: '报修位置不能为空',
						trigger: 'change'
					}],
					yqclsx:[{
						required: true,
						message: '报修详情不能为空',
						trigger: 'change'
					}],
					name: [{
						required: true,
						message: '报修人不能为空',
						trigger: 'change'
					}],
					source: [{
						required: true,
						message: '报修来源不能为空',
						trigger: 'change'
					}],
					gdsx: [{
						required: true,
						message: '工单时限不能为空',
						trigger: 'change'
					}],
					gddl: [{
						required: true,
						message: '工单大类不能为空',
						trigger: 'change'
					}],
					gdzl: [{
						required: true,
						message: '工单子类不能为空',
						trigger: 'change'
					}],
					ldr: [{
						required: true,
						message: '录单人不能为空',
						trigger: 'change'
					}],
						ldsj: [{
						required: true,
						message: '录单时间不能为空',
						trigger: 'change'
					}],
				}
			}
		},
		created() {

			this.initDicts()
		},
		watch: {
			'form.teamName': function(val, oldVal) {
				this.inputPY()
			},
		},
		methods: {
			description(){
				this.open3=true
			},
			co(){
				this.open2=true
			},
			selectPreNode() {

			},

			// 新建或修改工种Change事件
			workTypeChange(value) {
				// 获取对应的选中对象
				let obj = {};

				let name = '';
				if(this.form.teamLeaderName != undefined && this.form.teamLeaderName != null) {
					name = this.form.teamLeaderName
				}
				// 拼接班组名称
				var tenamName = obj.workType + "【" + name + "】"
				this.$set(this.form, "teamName", tenamName)
				//this.form.teamName = obj.workType + "【" + name + "】"
			},
			// 查询劳务人员信息列表

			// 初始化字典数据
			initDicts() {
				this.getDicts('b_is_send_status').then(response => {
					this.sendStatusDict = response.data
				})
			},
			// 平台状态字典翻译
			sendStatusFormat(row, column) {
				return this.selectDictLabel(this.sendStatusDict, row.isSendStatus)
			},
			// 拼音首拼
			inputPY() {
				let pinyin = vPinyin.chineseToPinYin(this.form.teamName);
				let SX = '';
				for(var i = 0; i < pinyin.length; i++) {
					var c = pinyin.charAt(i);
					if(/^[A-Z]+$/.test(c)) {
						SX += c;
					}
				}
				this.form.teamSpell = SX
			},
			// 根据选择的人员信息绑定数据
			bindingData(row) {
				this.form.teamLeaderId = row.id
				this.form.teamLeaderName = row.name
				this.$set(this.form, "idCard", row.idCard)
				this.$set(this.form, "phone", row.phone)
				//this.form.idCard = row.idCard
				//this.form.phone = row.phone
				this.form.subcontractorId = row.subcontractor
				this.form.subcontractorName = row.subcontractorName
				this.open2 = false
			},
			// 弹出层双击事件
			dobuleClick(row) {
				this.bindingData(row)
			},
			// 班组长input点击事件
			inputClick() {
				// 弹出层表单重置
				this.resetForm('queryForm2')
				this.title2 = '选择劳务人员信息'
				this.open2 = true
				this.currentPage2 = 1
				this.pageSize2 = 5
				this.getTeamLeaderInfoList()
			},
			cancel3(){
				this.open3=false
			},
			cancel2(){
				this.open2=false
			},
			// 取消按钮
			cancel() {
				this.open = false
				
				this.reset()
			},
			// 表单重置
			reset() {
				this.form = {}
				this.resetForm('form')
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.currentPage = 1
				this.queryParams.limit = this.pageSize

			},
			/** 弹出层搜索按钮操作 */
			handleQuery2() {
				this.currentPage2 = 1
				this.queryParams2.limit = this.pageSize2
				this.getTeamLeaderInfoList()
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.resetForm('queryForm')
				this.handleQuery()
			},
			/** 弹出层重置按钮操作 */
			resetQuery2() {
				this.resetForm('queryForm2')
				this.handleQuery2()
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.dialogType = true
				this.reset()
				this.open = true
				this.title = '新建报修'
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.dialogType = true
				this.reset()
				this.form=row
				this.open = true
				this.title = '编辑报修'
			},
			submitForm3:function() {
				this.$refs['form3'].validate(valid => {
					if(valid) {
						this.msgSuccess('添加成功')
									this.open3 = false

					}
				})
			},
			submitForm2:function() {
				this.$refs['form2'].validate(valid => {
					if(valid) {
						this.msgSuccess('转交成功')
									this.open2 = false

					}
				})
			},
			/** 提交按钮 */
			submitForm: function() {
				console.log(this.form)
				this.$refs['form'].validate(valid => {
					if(valid) {
						if(this.form.id != undefined) {
							editTeam(this.form).then(response => {
								if(response.code === 0) {
									this.msgSuccess('修改成功')
									this.open = false

								}
							})
						} else {
							addTeam(this.form).then(response => {
								if(response.code === 0) {
									this.msgSuccess('新增成功')
									this.open = false

								}
							})
						}
					}
				})
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const id = row.id
				this.$confirm('是否确认删除', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return deleteTeam(id)
				}).then(() => {

					this.msgSuccess('删除成功')
				}).catch(function() {})
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('system/dict/type/export', {
					...this.queryParams
				}, `type_${new Date().getTime()}.xlsx`)
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				this.pageSize = val

			},
			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val

			},
			// 弹出层更改每页展示数量
			handleSizeChange2(val) {
				this.pageSize2 = val
				this.getTeamLeaderInfoList()
			},
			// 弹出层更改当前页数
			handleCurrentChange2(val) {
				this.currentPage2 = val
				this.getTeamLeaderInfoList()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;
	}
	
	/deep/ .xr-header {
		padding: 15px 30px;
	}
	
	.main-body {
		background-color: white;
		/*border-top: 1px solid $xr-border-line-color;*/
		border-bottom: 1px solid $xr-border-line-color;
	}
	
	.main-table-header-button {
		float: right;
		margin-right: 20px;
		margin-top: 10px;
	}
	
	.add-form-input {
		float: left;
	}
	
	.add-form::after {
		content: "";
		display: block;
		clear: both;
	}
	
	.add-form .el-form-item {
		width: 50%;
	}
	
	.link-type,
	.link-type:focus {
		color: #337ab7;
		cursor: pointer
	}
	
	.link-type:focus:hover,
	.link-type:hover {
		color: #20a0ff
	}
	
	.el-table {
		border-top: 1px solid $xr-border-line-color;
	}
	/deep/.el-dialog__body{
		overflow: auto;
		height: 500px;
	}
</style>