<template>
	<xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close"
		@save="debouncedSaveField(true)">
		<create-sections title="基本信息">
			<el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
				<el-row>
					<el-col :span="12">
						<el-form-item label="名称" prop="name">
							<el-input v-model="form.name" placeholder="请输入名称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="对接平台" prop="platformId">
							<el-select v-model="form.platformId" placeholder="请选择" clearable size="small"
								style="width: 160px">
								<el-option v-for="dict in platforms" :key="dict.platformId" :label="dict.name"
									:value="dict.platformId" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="接口地址" prop="url">
							<el-input v-model="form.url" placeholder="请输入完整接口地址"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="测试地址" prop="testUrl">
              <el-input v-model="form.testUrl" placeholder="请输入完整测试接口地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="前置执行器" prop="beforeExecutor">
							<el-input v-model="form.beforeExecutor" placeholder="请输入前置启动器"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="后置执行器" prop="afterExecutor">
							<el-input v-model="form.afterExecutor" placeholder="请输入后置执行器"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="描述" prop="describes">
							<el-input type="textarea" :rows="2" v-model="form.describes" placeholder="请输入描述"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="参数" prop="innerparam">
							<el-input type="textarea" :rows="2" v-model="form.innerparam" placeholder="请输入JSON格式参数">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="是否循环发送" prop="pattern">
							<el-switch style="display: block" v-model="form.pattern" active-color="#13ce66"
								active-text="是" inactive-text="否" active-value="1" inactive-value="0">
							</el-switch>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="是否开启首次校验" prop="openFirst">
							<el-switch style="display: block" v-model="form.openFirst" active-color="#13ce66"
								active-text="是" inactive-text="否" active-value="1" inactive-value="0">
							</el-switch>
						</el-form-item>
					</el-col>
				</el-row>
				<el-col :span="24">
					<el-form-item label="请求类型" prop="reqType">
					  <el-radio-group v-model="form.reqType">
					    <el-radio label="0">POST</el-radio>
					    <el-radio label="1">GET</el-radio>
					    <el-radio label="2">PUT</el-radio>
					    <el-radio label="3">DELETE</el-radio>
					  </el-radio-group>
					</el-form-item>
				</el-col>
			</el-form>
		</create-sections>
	</xr-create>
</template>
<script>
	import XrCreate from '@/components/XrCreate'
	import CreateSections from '@/components/CreateSections'
	import {
		guid
	} from '@/utils'
	import {
		debounce
	} from 'throttle-debounce'
	import {
		saveOrUpdate,
		getById,
		selectPlatform
	} from "../api/platint";
	export default {
		// 新建编辑
		name: 'ExchangesCreate',
		components: {
			XrCreate,
			CreateSections
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
				form: {},
				defaultValue: {
					reqType:'0'
				},
				batchId: '',
				platforms: [],
				rules: {
					name: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					describe: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					platformId: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					url: [{
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
			this.getPlatforms();
			this.debouncedSaveField = debounce(300, this.save)
			//考虑附件ID
			if (this.action.type == "update") {
				this.disabled = true;
				this.getDetail();
			} else {
				this.form = this.defaultValue;
			}
		},

		mounted() {},

		beforeDestroy() {},

		methods: {
			getPlatforms(){
				selectPlatform()
					.then(res => {
						this.platforms = res.data;
					}).catch(() => {})
			},
			/**
			 * 获取详情
			 * */
			getDetail() {
				getById(this.action.id)
					.then(res => {
						this.form = res.data;
					}).catch(() => {})
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
</style>
