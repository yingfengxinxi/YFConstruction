<template>
	<div>
		<div class="jisuan">工单处理时限 = 处理结束时间 - 开始处理时间</div>
		<div class="jscontent">
			<el-form ref="form" :model="form"  label-width="100px">
				<el-row>
					<el-col :span="12">
						<el-form-item label="开始处理时间"  prop="startTime">
							<el-select v-model="form.startTime" placeholder="请选择">
						      <el-option label="报修单创建时间" value="1"></el-option>
						      <el-option label="派工时间" value="2"></el-option>
						      <el-option label="开始处理时间" value="3"></el-option>
						    </el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="" prop="type">
						    <el-checkbox-group v-model="form.type">
						      <el-checkbox label="当有填写预约时间时，开始处理时间按预约时间计算" name="type"></el-checkbox>
						    </el-checkbox-group>
						  </el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="处理结束时间"  prop="endTime">
							<el-select v-model="form.endTime" placeholder="请选择">
						      <el-option label="回单时间" value="1"></el-option>
						      <el-option label="确认时间" value="2"></el-option>
						    </el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-form-item>
				    <el-button type="primary" @click="submitForm('form')">保存</el-button>
				    <el-button @click="resetForm('form')">重置</el-button>
				 </el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				form:{
					type:[],
					startTime:'',
					endTime:''
				}
			}
		},
		methods: {
			submitForm(formName) {
		        this.$refs[formName].validate((valid) => {
		          if (valid) {
		            alert('submit!');
		          } else {
		            console.log('error submit!!');
		            return false;
		          }
		        });
		      },
		      resetForm(formName) {
		      	this.$refs[formName].resetFields();	      
		      }
		}
	}
</script>

<style scoped>
	.jisuan {
		margin-left: 40px;
	}
	.jscontent{
		margin-left: 40px;
		margin-top: 40px;
		width: 600px;
	}
</style>