<template>
	<xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" :showSave="!(action.type == 'details')"
		@close="close" @save="debouncedSaveField(true)">
		<el-form ref="form" :model="form" :rules="rules" :disabled="true" label-position="top" label-width="100px"
			class="form">
			<create-sections title="基本信息">
				<el-row>
					<el-col :span="12">
						<el-form-item label="项目名称" class="add-form-input subcontractorClass" prop="projectId">
							<el-select v-model="form.projectId" placeholder="请选择" clearable
								size="small">
								<el-option v-for="item in dictData.projectOptions" :key="item.id" :label="item.name"
									:value="item.id" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="隐患编号" prop="hiddenNum">
							<el-input v-model="form.hiddenNum" :disabled="true" placeholder="请输入">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="隐患类别" prop="category">
							<el-select v-model="form.category"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								@change="categoryChange" placeholder="请选择隐患类别">
								<el-option v-for="item in dictData.hazardCategoryDict" :key="item.dictValue"
									:label="item.dictLabel" :value="item.dictValue">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="隐患等级" prop="level">
							<el-select v-model="form.level"
								:disabled="!(action.type == 'save') && !(action.type == 'update')" @change="levelChange"
								placeholder="请选择计划类型">
								<el-option v-for="item in dictData.hazardLevelDict" :key="item.dictValue"
									:label="item.dictLabel" :value="item.dictValue">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="风险点类型" prop="riskType">
							<el-select v-model="form.riskType"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								@change="riskTypeChange" placeholder="请选择风险点类型">
								<el-option v-for="item in riskPointOption" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="风险点名称" prop="riskName">
							<el-input v-model="form.riskName"
								:disabled="!(action.type == 'save') && !(action.type == 'update')" readonly
								v-on:click.native="inputClick" suffix-icon="el-icon-search" placeholder="请选择风险点">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="事故类型" prop="accidentType">
							<el-select v-model="form.accidentType"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								placeholder="请选择事故类型">
								<el-option v-for="item in dictData.accidentTypeDict" :key="item.dictValue"
									:label="item.dictLabel" :value="item.dictValue">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="隐患描述" prop="riskDesc">
							<el-input v-model="form.riskDesc"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								placeholder="请输入隐患描述"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="主要治理措施" prop="administerStep">
							<el-input v-model="form.administerStep"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								placeholder="请输入主要治理措施"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<!--图片上传-->
				<wk-upload-img name="file" ref="wkUploadImg" multiple accept="image/*" list-type="picture-card"
					:autoUpload="false" :title="'图片上传'" :batchId="batchId"></wk-upload-img>
			</create-sections>

			<create-sections title="检查信息">
				<el-row>
					<el-col :span="12">
						<el-form-item label="检查负责人" prop="checkPersonName">
							<el-input v-model="form.checkPersonName" :disabled="true" placeholder="请输入检查负责人"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="checkPersonPhone">
							<el-input v-model="form.checkPersonPhone" :disabled="true" placeholder="请输入联系电话"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="检查日期" prop="checkTime">
							<el-date-picker :disabled="!(action.type == 'save') && !(action.type == 'update')"
								v-model="form.checkTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
								placeholder="请选择检查日期">
							</el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="检查类型" prop="routineCheckType">
							<el-select v-model="form.routineCheckType"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								@change="routineCheckTypeChange" placeholder="请选择检查类型">
								<el-option v-for="item in dictData.routineCheckTypeDict" :key="item.dictValue"
									:label="item.dictLabel" :value="item.dictValue">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="检查区域" prop="checkArea">
							<el-input v-model="form.checkArea"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								placeholder="请输入检查区域"></el-input>
						</el-form-item>
					</el-col>
					<!-- <el-col :span="12">
			  <el-form-item label="检查结果" prop="chekcResult">
			    <el-select v-model="form.chekcResult" :disabled="!(action.type == 'save') && !(action.type == 'update')"  
				@change="chekcResultChange" placeholder="请选择检查结果">
			      <el-option label="无需整改" value="无需整改" />
			      <el-option label="待整改" value="待整改" />
			      <el-option label="合格" value="合格" />
			    </el-select>
			  </el-form-item>
			</el-col> -->
				</el-row>
			</create-sections>

			<create-sections title="整改信息">
				<el-row>
					<el-col :span="12">
						<el-form-item label="整改责任单位" prop="dutyUnitId">
							<el-select v-model="form.dutyUnitId"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								@change="dutyUnitChange" placeholder="请选择整改责任单位">
								<el-option v-for="item in dictData.subcontractorList" :key="item.id"
									:label="item.realName" :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="整改期限" prop="rectifyTime">
							<el-date-picker :disabled="!(action.type == 'save') && !(action.type == 'update')"
								v-model="form.rectifyTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
								placeholder="请选择整改期限" :picker-options="pickerOptions">
							</el-date-picker>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="整改负责人" prop="rectifyPersonId">
							<el-select v-model="form.rectifyPersonId"
								:disabled="!(action.type == 'save') && !(action.type == 'update')"
								@change="rectifyPersonChange($event, jobForemanList)" placeholder="请选择整改负责人">
								<el-option v-for="item in jobForemanList" :key="item.id" :label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="rectifyPersonPhone">
							<el-input v-model="form.rectifyPersonPhone" :disabled="true" placeholder="请输入联系电话">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row v-if="!(action.type == 'save') && !(action.type == 'update')">
					<el-col :span="12">
						<el-form-item label="实际整改人" prop="actualRectifyPersonName">
							<el-input v-model="form.actualRectifyPersonName" :disabled="true" placeholder="请输入实际整改人">
							</el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="actualRectifyPersonPhone">
							<el-input v-model="form.actualRectifyPersonPhone" :disabled="true" placeholder="请输入联系电话">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row v-if="!(action.type == 'save') && !(action.type == 'update')">
					<el-col :span="12">
						<el-form-item label="整改情况" prop="dutySituation">
							<el-input v-model="form.dutySituation" :disabled="!(action.type == 'rectify')"
								placeholder="请输入整改情况"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="整改完成时间" prop="rectifyCompleteTime">
							<el-date-picker :disabled="!(action.type == 'rectify')" v-model="form.rectifyCompleteTime"
								type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择整改完成时间"
								:picker-options="pickerOptions2">
							</el-date-picker>
						</el-form-item>
					</el-col>
				</el-row>
				<!--图片上传-->
				<wk-upload-img v-if="!(action.type == 'save') && !(action.type == 'update')" name="file"
					ref="wkUploadImg2" multiple accept="image/*" list-type="picture-card" :autoUpload="false"
					:title="'图片上传'" :batchId="rectifyBatchId"></wk-upload-img>

			</create-sections>

			<create-sections v-if="action.type == 'recheck' || action.type == 'details'" title="复检信息">
				<el-row>
					<el-col :span="12">
						<el-form-item label="复查人" prop="examinePersonName">
							<el-input v-model="form.examinePersonName" :disabled="true" placeholder="请输入复查人"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="examinePersonPhone">
							<el-input v-model="form.examinePersonPhone" :disabled="true" placeholder="请输入联系电话">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="复查情况" prop="reExamineSituttion">
							<el-input v-model="form.reExamineSituttion" :disabled="action.type == 'details'"
								placeholder="请输入复查情况"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="复查完成时间" prop="reviewTime">
							<el-date-picker :disabled="action.type == 'details'" v-model="form.reviewTime"
								type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择复查完成时间"
								:picker-options="pickerOptions2">
							</el-date-picker>
						</el-form-item>
					</el-col>
				</el-row>
			</create-sections>

		</el-form>

		<!-- 选择风险点 -->
		<el-dialog :title="title2" :visible.sync="open2" width="1000px" append-to-body>
			<el-form v-show="showSearch2" ref="queryForm2" :model="queryParams2" :inline="true" label-width="80px"
				style="padding-top: 20px;padding-left:10px;">
				<el-form-item label="风险名称" prop="riskName" label-position="right">
					<el-input v-model="queryParams2.riskName" size="small" @keyup.enter.native="handleQuery2" />
				</el-form-item>
				<el-form-item label="风险点描述" prop="riskDesc" label-position="right">
					<el-input v-model="queryParams2.riskDesc" size="small" @keyup.enter.native="handleQuery2" />
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery2">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery2">重置</el-button>
				</el-form-item>
			</el-form>
			<el-table v-loading="loading" :data="dataList2" height="450px" @row-dblclick="dobuleClick">
				<el-table-column label="风险编号" align="center" prop="riskNum" width="130px" />
				<el-table-column label="风险名称" align="center" prop="riskName" />
				<el-table-column label="风险点描述" align="center" prop="riskDesc" :show-overflow-tooltip="true" />
				<el-table-column label="风险等级" :formatter="riskLevelIdentFc" align="center" prop="riskType" width="80px"
					:show-overflow-tooltip="true" />
				<el-table-column label="潜在事故类型" :formatter="accidentTypeFc" align="center" prop="accidentType"
					width="100px" :show-overflow-tooltip="true" />
				<el-table-column label="管控层级" :formatter="riskLevelOptionsFc" align="center" prop="controlLevel"
					width="80px" :show-overflow-tooltip="true" />
				<el-table-column label="管理措施" align="center" prop="controlMeasures" :show-overflow-tooltip="true" />
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage2" :page-sizes="pageSizes2" :page-size.sync="pageSize2"
					:total="total2" class="p-bar" background layout="prev, pager, next, sizes, total, jumper"
					@size-change="handleSizeChange2" @current-change="handleCurrentChange2" />
			</div>
		</el-dialog>
	</xr-create>
</template>

<script>
	import XrCreate from '@/components/XrCreate'
	import SelectTree from '@/components/SelectTree'
	import CreateSections from '@/components/CreateSections'
	import {
		mapGetters
	} from 'vuex'

	import WkUploadImg from '@/components/CreateCom/WkUploadImg'

	import {
		guid
	} from '@/utils'

	import {
		debounce
	} from 'throttle-debounce'

	import {
		save,
		updateInfo,
		getById,
		getNumber,
		selectStaticRiskList,
		selectDynamicRiskList,
		getTeamLeaderList
	} from "../api/inspectionListNew"

	//业务审批Copy:审批流程
	//import ExamineMixin from '@/components/common/examine/mixins/examine'
	//import CreateExamine from '@/components/common/examine/create'

	export default {
		// 新建编辑
		name: 'PlanCreate',
		components: {
			XrCreate,
			CreateSections,
			WkUploadImg,
			SelectTree
		},
		//mixins: [ExamineMixin],//业务审批Copy:审批
		props: {
			action: {
				type: Object,
				default: () => {
					return {
						type: 'save',
						id: '',
						data: {}
					}
				}
			},
			// 字典数据
			dictData: {},
		},
		data() {
			return {
				loading: false,
				form: {
					hiddenNum: '',
					checkTime: '',
					rectifyTime: '',
					riskDesc: '',
					administerStep: '',
				},
				// 数据默认字段
				defaultProps: {
					parent: 'parentId', // 父级唯一标识
					value: 'id', // 唯一标识
					label: 'label', // 标签显示
					children: 'children', // 子级
				},
				pickerOptions: { // 限制收货时间不让选择今天以前的
					disabledDate(time) {
						return time.getTime() < Date.now()
					}
				},
				pickerOptions2: { // 限制收货时间不让选择今天以前的
					disabledDate(time) {
						return time.getTime() < (Date.now() - 24 * 60 * 60 * 1000)
					}
				},
				// 风险点类型
				riskPointOption: [{
						label: '设备设施类风险',
						value: '1'
					},
					{
						label: '作业活动类风险',
						value: '2'
					}
				],
				// 弹出层标题
				title2: "",
				open2: false,
				// 弹出层显示搜索条件
				showSearch2: true,
				// 弹出层查询参数
				queryParams2: {
					page: '',
					limit: '',
					riskName: '',
					riskDesc: '',
				},
				dataList2: [],
				currentPage2: 1,
				pageSize2: 10,
				pageSizes2: [10, 20, 30, 40],
				total2: 0,

				// 班组长列表
				jobForemanList: [],
				// 风险等级
				riskLevelIdentOption: [],
				// 管控层级
				riskLevelOptions: [],
				batchId: '',
				rectifyBatchId: '',

				rules: {
					/* hiddenNum: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					category: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					level: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					riskType: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					riskName: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					riskDesc: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					accidentType: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					administerStep: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					checkPersonName: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					checkPersonPhone: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					checkTime: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					routineCheckType: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					chekcResult: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					checkArea: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					dutyUnitId: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					rectifyPersonId: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					rectifyPersonPhone: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					rectifyTime: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					actualRectifyPersonName: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					actualRectifyPersonPhone: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					dutySituation: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					rectifyCompleteTime: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					examinePersonName: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					examinePersonPhone: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					reExamineSituttion: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					],
					reviewTime: [
					  {required: true, message: '必填项不得为空', trigger: 'change'},
					], */
				},
			}
		},
		computed: {
			...mapGetters([
				'userInfo',
			]),
			title() {
				switch (this.action.type) {
					case 'save':
						return '新建安全隐患排查'
					case 'update':
						return '编辑安全隐患排查'
					case 'rectify':
						return '整改安全隐患排查'
					case 'recheck':
						return '复检安全隐患排查'
					case 'details':
						return '查看安全隐患排查'
				}
				return ''
			},

		},
		created() {
			this.debouncedSaveField = debounce(300, this.save)
			// 风险等级
			this.getDicts('b_risk_level_ident').then(response => {
				this.riskLevelIdentOption = response.data
			})
			// 管控等级
			this.getDicts('b_control_level').then(response => {
				this.riskLevelOptions = response.data
			})
			//考虑附件ID

			if (this.action.type == "save") {

				this.batchId = guid()
				this.form.batchId = this.batchId
				// 创建时生成编号
				this.selectNumber()
				// 隐患信息初始化
				this.form.category = 'basic'
				this.form.level = 'general'
				this.form.routineCheckType = 'corp'
				this.form.chekcResult = '需整改'
				// 检查人信息
				this.form.checkPersonId = this.userInfo.userId
				this.form.checkPersonName = this.userInfo.realname
				this.form.checkPersonPhone = this.userInfo.mobile

				this.form.checkTime = this.formatterDate(new Date().getTime())
				this.form.rectifyTime = this.formatterDate(new Date(this.form.checkTime).getTime() + 24 * 60 * 60 * 1000)
			} else {
				this.getDetail();
			}
		},
		watch: {},
		methods: {
			/**
			 * 获取详情
			 * */
			getDetail() {
				getById(this.action.id)
					.then(res => {
						this.form = res.data
						this.batchId = res.data.batchId

						if (this.action.type == "rectify") {
							// 整改生成batchId
							this.rectifyBatchId = guid()
							this.form.rectifyBatchId = this.rectifyBatchId
							// 实际整改人
							this.form.actualRectifyPersonId = this.userInfo.userId
							this.form.actualRectifyPersonName = this.userInfo.realname
							this.form.actualRectifyPersonPhone = this.userInfo.mobile
						} else if (this.action.type == "recheck") {
							this.rectifyBatchId = this.form.rectifyBatchId
							// 检查人
							this.form.examinePersonId = this.userInfo.userId
							this.form.examinePersonName = this.userInfo.realname
							this.form.examinePersonPhone = this.userInfo.mobile
						} else {
							this.rectifyBatchId = this.form.rectifyBatchId
						}

						// 查询对应分分包公司的班组长信息
						this.getTeamLeaderList()
					})
					.catch(() => {})
			},
			/**
			 * 保存
			 */
			save(isSubmit) {
				this.$refs['form'].validate(valid => {
					if (valid) {
						if (this.action.type == 'save') {
							this.saveInfo();
						} else {
							this.updateInfo()
						}
					}
				})
			},
			saveInfo() {
				var fileFlag = this.$refs['wkUploadImg'].submitAsync();
				if (!fileFlag) {
					this.$message({
						type: 'error',
						message: '隐患图片上传异常'
					})
					return;
				}

				var params = this.form;
				this.loading = true;
				save(params).then(response => {
					if (response.code === 0) {
						this.$message.success(
							response.data
						)
						this.close()
						// 保存成功
						this.$emit('save-success', {
							type: 'receivables'
						})
					} else {
						this.$message.error(
							response.msg
						)
					}
					this.loading = false;
				}).catch(err => {
					this.loading = false;
				})
			},
			updateInfo() {
				if (this.action.type != 'update') {
					var fileFlag = this.$refs['wkUploadImg2'].submitAsync();
					if (!fileFlag) {
						this.$message({
							type: 'error',
							message: '整改图片上传异常'
						})
						return;
					}
				}

				var params = this.form;
				this.loading = true;
				updateInfo(params).then(response => {
					if (response.code === 0) {
						let message = ''
						switch (this.action.type) {
							case 'update':
								message = '修改成功'
								break;
							case 'rectify':
								message = '整改成功'
								break;
							case 'recheck':
								message = '复检成功'
								break;
						}
						this.$message.success(
							message
						)
						this.close()
						// 保存成功
						this.$emit('save-success', {
							type: 'receivables'
						})
					} else {
						this.$message.error(
							response.msg
						)
					}
					this.loading = false;
				}).catch(err => {
					this.loading = false;
				})
			},
			/**
			 * 关闭
			 */
			close() {
				this.$emit('close')
			},
			categoryChange() {
				this.$forceUpdate()
			},
			levelChange() {
				this.$forceUpdate()
			},
			routineCheckTypeChange() {
				this.$forceUpdate()
			},
			chekcResultChange() {
				this.$forceUpdate()
			},
			// 获取隐患编号
			selectNumber() {
				getNumber().then(res => {
					this.form.hiddenNum = res.data
				})
			},
			dutyUnitChange() {
				this.form.rectifyPersonId = undefined
				this.form.rectifyPersonPhone = undefined
				this.getTeamLeaderList()
			},
			getTeamLeaderList() {
				let params = {
					subcontractor: this.form.dutyUnitId,
					ifTeamLeader: 'Y',
					pageType: 0
				}
				getTeamLeaderList(params).then(res => {
					this.jobForemanList = res.data.list
				})
			},
			rectifyPersonChange(value, datalist) {
				// 获取对应的选中对象
				let obj = {};
				obj = datalist.find((item) => {
					return item.id === value;
				})
				this.$set(this.form, "rectifyPersonPhone", obj.phone)
				this.$forceUpdate()
			},
			riskTypeChange() {
				this.form.riskId = ''
				this.form.riskNum = ''
				this.form.riskName = ''
				this.form.riskDesc = ''
				this.form.administerStep = ''
			},
			// 风险点名称点击事件
			inputClick() {
				// 弹出层表单重置
				this.resetForm('queryForm2')
				if (this.form.riskType == '1') {
					this.title2 = '选择设备设施类风险'
				} else if (this.form.riskType == '2') {
					this.title2 = '选择作业活动类风险'
				} else {
					this.title2 = '请先选择风险点类型'
				}
				this.open2 = true
				this.currentPage2 = 1
				this.pageSize2 = 10
				this.selectRiskPointList(this.form.riskType)
			},
			selectRiskPointList(type) {
				this.queryParams2.page = this.currentPage2
				this.queryParams2.limit = this.pageSize2
				if (type == '1') {
					this.loading = true
					selectStaticRiskList(this.queryParams2).then(res => {
						this.dataList2 = res.data.list
						this.total2 = res.data.totalRow
						this.loading = false
					}).catch(() => {
						this.loading = false
					})
				} else if (type == '2') {
					this.loading = true
					selectDynamicRiskList(this.queryParams2).then(res => {
						this.dataList2 = res.data.list
						this.total2 = res.data.totalRow
						this.loading = false
					}).catch(() => {
						this.loading = false
					})
				}
			},
			// 弹出层双击事件
			dobuleClick(row) {
				this.bindingData(row)
			},
			// 根据选择的人员信息绑定数据
			bindingData(row) {
				this.form.riskId = row.id
				this.form.riskNum = row.riskNum
				this.$set(this.form, "riskName", row.riskName)
				this.$set(this.form, "riskDesc", row.riskDesc)
				this.$set(this.form, "administerStep", row.controlMeasures)
				this.$set(this.form, "accidentType", row.accidentType)
				this.open2 = false
			},
			/** 弹出层搜索按钮操作 */
			handleQuery2() {
				this.currentPage2 = 1
				this.queryParams2.limit = this.pageSize2
				this.selectRiskPointList(this.form.riskType)
			},
			/** 弹出层重置按钮操作 */
			resetQuery2() {
				this.resetForm('queryForm2')
				this.handleQuery2()
			},
			// 弹出层更改每页展示数量
			handleSizeChange2(val) {
				this.pageSize2 = val
				this.selectRiskPointList(this.form.riskType)
			},
			// 弹出层更改当前页数
			handleCurrentChange2(val) {
				this.currentPage2 = val
				this.selectRiskPointList(this.form.riskType)
			},
			// 格式化时间字符串
			formatterDate(date) {
				let time = new Date(date)
				if (date == null || date == "") return "";
				let Y = time.getFullYear() + "-";
				let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
				let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
				let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
				let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
				let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
				let all = (Y + M + D + h + m + s)
				return all;
			},

			//风险A等级
			riskLevelIdentFc(row, column) {
				return this.selectDictLabel(this.riskLevelIdentOption, row.riskLevel);
			},
			// 潜在事故类型
			accidentTypeFc(row, column) {
				return this.selectDictLabel(this.dictData.accidentTypeDict, row.accidentType)
			},
			// 管控层级
			riskLevelOptionsFc(row, column) {
				return this.selectDictLabel(this.riskLevelOptions, row.controlLevel)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.form /deep/ .el-form-item .el-form-item__content {
		width: calc(100% - 10px);
	}

	.form /deep/ .el-form-item__label {
		padding: 0px;
	}

	.form /deep/ .el-date-editor,
	.el-select {
		width: 100%;
	}
</style>
