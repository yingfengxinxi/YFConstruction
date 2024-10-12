<template>
	<el-form ref="expform" :model="expform" :rules="exprules" label-position="top" label-width="100px" class="form">
		<el-row>
			<el-col :span="12">
				<el-form-item label="单体工程名称" prop="monomerProjectName">
					<el-input v-model="expform.monomerProjectName"  placeholder="请输入单体工程名称"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="卸料平台额定载重" prop="deviceRatedLoad">
					<el-input v-model="expform.deviceRatedLoad" min="0" type="number" placeholder="请输入卸料平台额定载重"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-form-item label="主绳额定载重" prop="mainRatedLoad">
					<el-input v-model="expform.mainRatedLoad" min="0" type="number" placeholder="请输入主绳额定载重"></el-input>
				</el-form-item>
			</el-col>

			<el-col :span="12">
				<el-form-item label="副绳额定载重" prop="subRatedLoad">
					<el-input v-model="expform.subRatedLoad"  min="0" type="number"  placeholder="请输入副绳额定载重"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-form-item label="X倾角预警阈值" prop="angleWarmX">
					<el-input v-model="expform.angleWarmX"  type="number"  placeholder="请输入X倾角预警阈值"></el-input>
				</el-form-item>
			</el-col>

			<el-col :span="12">
				<el-form-item label="Y倾角预警阈值" prop="angleWarnY">
					<el-input v-model="expform.angleWarnY" type="number" placeholder="请输入Y倾角预警阈值"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-form-item label="X倾角报警阈值" prop="angleAlarmX">
					<el-input v-model="expform.angleAlarmX" type="number" placeholder="请输入X倾角报警阈值"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="Y倾角报警阈值" prop="angleAlarmY">
					<el-input v-model="expform.angleAlarmY" type="number" placeholder="请输入Y倾角报警阈值"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-form-item label="楼层" prop="floor">
					<el-input v-model="expform.floor" type="number" placeholder="请输入楼层">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="本平台安装次数" prop="installNumber">
					<el-input v-model="expform.installNumber" min="1" type="number" placeholder="请输入本平台安装次数">
					</el-input>
				</el-form-item>
			</el-col>
		</el-row>
	</el-form>
</template>

<script>
	import {
		platformSave,
		platformDetail,
		platformDelete
	} from "./api/platform"

	export default {
		/**
		 * 必须包含
		 * */
		name: 'ToweExpend',
		components: {},
		props: {},
		data() {
			return {
				expform: {},
				exprules: {
					deviceRatedLoad: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					mainRatedLoad: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					subRatedLoad: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
					weightAlarmValue: [{
						required: true,
						message: '必填项不得为空',
						trigger: 'blur'
					}],
				},
				towerCraneConfig: [],
			}
		},
		computed: {},
		watch: {
			eqID: function(val) {
				if (val == -1) {
					this.expform = {};
				} else {
					this.getDetail();
				}
			}
		},
		created() {
			this.getDicts('b_tower_crane_config').then(response => {
				this.towerCraneConfig = response.data
			})
		},

		mounted() {},

		beforeDestroy() {},

		methods: {
			/**
			 * 获取详情:必要
			 * */
			expendInit(eqID) {
				if (eqID) {
					platformDetail(eqID)
						.then(res => {
							this.expform = res.data == null ? {} : res.data;
						})
						.catch(() => {})
				} else {
					this.expform = {};
				}

			},
			/**
			 * 校验:必要
			 */
			expendCheck() {
				var flag = false;
				this.$refs['expform'].validate(valid => {
					if (valid) {
						flag = true;
					}
				})
				return flag;
			},
			/**
			 * 保存:必要
			 */
			expendSave(eqID) {
				var flag = false;
				var params = this.expform;
				if (eqID) {
					params.equipmentId = eqID;
				}
				this.$refs['expform'].validate(valid => {
					if (valid) {
						platformSave(params).then(response => {
							if (response.code === 0) {
								// 保存成功
								flag = true;
							}
						}).catch(err => {})
					}
					return flag;
				})
			},
			/**
			 * 删除:必要
			 */
			expendDelete(eqID) {
				if (eqID) {
					platformDelete(eqID)
						.then(res => {})
						.catch(() => {})
				}

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
