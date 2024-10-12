<template>
	<xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close" @save="debouncedSaveField(true)"
		>
		<el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
			<el-row>
				<el-col :span="12">
					<el-form-item label="检查评定项目" class="add-form-input label_1" prop="securityId">
						<el-select v-model="form.securityId" placeholder="请选择" clearable size="small"
							@change=changeSecurityId>
							<el-option v-for="dict in securityEvaluationOptions" :key="dict.dictValue"
								:label="dict.dictLabel" :value="dict.dictValue" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="自评月份" prop="evaluateMonth">
						<el-date-picker v-model="form.evaluateMonth" value-format="yyyy-MM" type="month" placeholder="选择月">
						</el-date-picker>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-table :data="tableData" :span-method="objectSpanMethod" border
						style="width: 100%; margin-top: 20px">
						<el-table-column type="index" width="50">
						</el-table-column>
						<el-table-column  label="检查项目">
							<el-table-column prop="securityEvaluationName" ></el-table-column>
							<el-table-column prop="securityEvaluationSubitem"></el-table-column>
						</el-table-column>
						<el-table-column  width="320px" prop="securityEvaluationStandard" label="扣分标准">
						</el-table-column>
						<el-table-column  prop="shouldScore" label="应得分数">
						</el-table-column>
						<el-table-column prop="" label="扣减分数">
							<template slot-scope="scope">
								<el-input type="number" v-model="form.mark[scope.$index]"  placeholder="请输入分数" />
							</template>
						</el-table-column>
					</el-table>
				</el-col>
			</el-row>
		</el-form>
	</xr-create>
</template>
<script>
	import XrCreate from '@/components/XrCreate'
	import CreateSections from '@/components/CreateSections'
	import WkUploadFile from '@/components/CreateCom/WkUploadFile'
	import FilesList from '@/components/common/FilesList' // 附件列表
	import {
		guid
	} from '@/utils'
	import {
		debounce
	} from 'throttle-debounce'
	import {
		add,
		getById,
		selectByEvaluationId
	} from "../api/selfEvaluate";

	export default {
		// 新建编辑
		name: 'ExchangesCreate',
		components: {
			XrCreate,
			CreateSections,
			WkUploadFile,
			FilesList
		},
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
			}
		},
		data() {
			return {
				loading: false,
				disabled: false,
				tableData: [],
				form: {
					mark:[],
					securityEvaluationId:[],
				},
				defaultValue: {
					mark:[],
					securityEvaluationId:[],
				},
				securityEvaluationOptions: [],
				rules: {
					securityId: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					evaluateMonth: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
				},
			}
		},
		computed: {
			title() {
				return this.action.type === 'update' ? '编辑' : '新建'
			},

		},
		watch: {},
		created() {
			this.debouncedSaveField = debounce(300, this.save)
			if (this.action.type == "update") {
				this.disabled = true;
				this.getDetail();
			} else {
				this.form = this.defaultValue;
			}
			this.getDicts('b_security_evaluation').then(response => {
				this.securityEvaluationOptions = response.data
			})
		},

		mounted() {},

		beforeDestroy() {},

		methods: {
			/**
			 * 获取详情
			 * */
			getDetail() {
				getById(this.action.id)
					.then(res => {
						this.form = res.data;
						this.batchId = this.form.batchId
						if (!this.batchId) {
							this.batchId = guid()

						}
					})
					.catch(() => {})
			},
			/**
			 * 保存
			 */
			save() {
				this.$refs['form'].validate(valid => {
					if (valid) {
						var than = this;
						var params = [];
						this.form.securityEvaluationId.forEach(function(val,i){
							var obj = {
								securityEvaluationId:val,
								securityId:than.form.securityId,
								evaluateMonth:than.form.evaluateMonth,
								mark:than.form.mark[i]
								}
							params.push(obj);
						})
						this.loading = true;
						add(params).then(response => {
							if (response.code === 0) {
								this.$message.success(
									this.action.type == 'update' ? '编辑成功' : '添加成功'
								)
								this.close()
								// 保存成功
								this.$emit('save-success')
							} else {
								this.$message.error(
									response.msg
								)
							}
							this.loading = false;
						}).catch(err => {
							this.loading = false;
						})

					}
				})
			},
			/**
			 * 关闭
			 */
			close() {
				console.log("close")
				this.$emit('close')
			},
			changeSecurityId(value) {

				selectByEvaluationId(value)
					.then(res => {
						var than = this;
						this.tableData = res.data
						this.tableData.forEach(function(val,i){
							than.form.securityEvaluationId[i] = val.id
						})
						
					})
					.catch(() => {})
			},
			objectSpanMethod({row,column,rowIndex,columnIndex}) {
				if(columnIndex == 1){
					// console.log("列",column)
					// console.log("行",row)
				}
				
				// return {
				// 	rowspan: 2,
				// 	colspan: 1
				// };
				// return {
				// 	rowspan: 0,
				// 	colspan: 0
				// };
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

	.form /deep/ .el-date-editor {
		width: 100%;
	}
</style>
