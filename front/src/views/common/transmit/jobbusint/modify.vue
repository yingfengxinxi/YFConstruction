<template>
	<xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close"
		@save="debouncedSaveField(true)">
		<create-sections title="基本信息">
			<el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
				<el-row>
					<el-col :span="12">
						<el-form-item label="任务" prop="jobCode">
							<el-select v-model="form.jobCode" clearable placeholder="请选择">
								<el-option v-for="item in jobNames" :key="item.id+''" :label="item.jobDesc+':'+item.executorHandler"
									:value="item.id+''">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="状态" prop="status">
							<el-switch style="display: block" v-model="form.status" active-color="#13ce66"
								active-text="启用" inactive-text="停用" active-value="1" inactive-value="0">
							</el-switch>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="项目" prop="projectId">
							<el-select v-model="form.projectId" clearable placeholder="请选择" filterable >
								<el-option v-for="item in projectNames" :key="item.id" :label="item.projectName"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="业务接口" prop="busintId">
							<el-select v-model="form.busintId" clearable placeholder="请选择" filterable >
								<el-option v-for="item in busints" :key="item.busintId" :label="item.name"
									:value="item.busintId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="对接平台" prop="platformId">
							<el-select v-model="form.platformId" clearable placeholder="请选择"  @change=getPlatint >
								<el-option v-for="item in platformNames" :key="item.platformId" :label="item.name"
									:value="item.platformId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="平台接口" prop="platintId">
							<el-select v-model="form.platintId" clearable placeholder="请选择" filterable >
								<el-option v-for="item in platintNames" :key="item.platintId" :label="item.name"
									:value="item.platintId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</create-sections>
	</xr-create>
</template>
<script>
	import XrCreate from '@/components/XrCreate'
	import CreateSections from '@/components/CreateSections'
	import {
		debounce
	} from 'throttle-debounce'
	import {
		saveOrUpdate,
		getById,
		getJob,
		getProject,
		getBusint,
		getPlatform,
		getPlatint
	} from "../api/jobbusint";
	export default {
		// 新建编辑
		name: 'ExchangesCreate',
		components: {
			XrCreate,
			CreateSections,
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
				jobNames: [],
				projectNames: [],
				busints: [],
				platformNames: [],
				platintNames: [],
				form: {},
				defaultValue: {

				},
				batchId: '',

				rules: {
					jobCode: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					status: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					projectId: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					busintId: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					platformId: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					platintId: [{
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
			//考虑附件ID
			if (this.action.type == "update") {
				this.disabled = true;
				this.getDetail();
			} else {
				this.form = this.defaultValue;
			}
		},

		mounted() {
			this.init();
		},

		beforeDestroy() {},

		methods: {
			/**
			 * 获取详情
			 * */
			getDetail() {
				getById(this.action.id)
					.then(res => {
						this.form = res.data;
						console.log(this.form.platformId)
						this.getPlatint(this.form.platformId);
					})
					.catch(() => {})
			},
			/**
			 * 保存
			 */
			save() {
				this.$refs['form'].validate(valid => {
					if (valid) {
						var params = this.form;
						this.loading = true;
						saveOrUpdate(params).then(response => {
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
			/**
			 *
			 */
			init(){
				getJob()
					.then(res => {
						console.log(res)
						if (res.code == 0) {
							this.jobNames = res.data
						}
					})
					.catch((e) => {
						console.log(e)
					})
				getProject()
					.then(res => {
						console.log(res)
						if (res.code == 0) {
							this.projectNames = res.data
						}
					})
					.catch((e) => {
						console.log(e)
					})
				getBusint()
					.then(res => {
						console.log(res)
						if (res.code == 0) {
							this.busints = res.data
						}
					})
					.catch((e) => {
						console.log(e)
					})
				getPlatform()
					.then(res => {
						console.log(res)
						if (res.code == 0) {
							this.platformNames = res.data
						}
					})
					.catch((e) => {
						console.log(e)
					})
			},
			getPlatint(value){
				getPlatint(value)
					.then(res => {
						console.log(res)
						if (res.code == 0) {
							this.platintNames = res.data
						}
					})
					.catch((e) => {
						console.log(e)
					})
			}
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
	.form /deep/ .el-input__inner{
		width: 300px;
	}
</style>
