<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
	:showSave="false"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
				<create-sections title="抓拍图片">
				  <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    :src="form.alertImg" width="100%"/>
				</create-sections>
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" disabled placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="licensePlateNumber">
              <el-input v-model="form.licensePlateNumber" disabled placeholder="请输入车牌号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
				
		<el-row>
		  <el-col :span="12">
			<el-form-item label="车牌颜色" prop="licensePlateColor">
			  <el-select v-model="form.licensePlateColor" disabled placeholder="请选择车牌颜色">
				<el-option
				v-for="item in dictData.licensePlateColorDict"
				:key="item.dictValue"
				:label="item.dictLabel"
				:value="item.dictValue">
				</el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="12">
			<el-form-item label="车型" prop="carType">
			  <el-select v-model="form.carType" disabled placeholder="请选择车型">
				<el-option
				v-for="item in dictData.carTypeDict"
				:key="item.dictValue"
				:label="item.dictLabel"
				:value="item.dictValue">
				</el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		</el-row>
		
        <el-row>
          <el-col :span="12">
            <el-form-item label="报警时间" prop="alertTime">
              <el-input v-model="form.alertTime" disabled placeholder="请输入报警时间"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警类型" prop="alertType">
              <el-select v-model="form.alertType" disabled placeholder="请选择报警类型">
                <el-option
              	v-for="item in dictData.dregAlertTypeDict"
              	:key="item.dictValue"
              	:label="item.dictLabel"
              	:value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
		
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="报警内容" prop="alertContent">
		      <el-input v-model="form.alertContent" disabled type="textarea" placeholder="请输入" />
		    </el-form-item>
		  </el-col>
		</el-row>
		
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="报警数据" prop="alertData">
		      <el-input v-model="form.alertData" disabled type="textarea" placeholder="请输入" />
		    </el-form-item>
		  </el-col>
		</el-row>
		
		<el-row>
		  <el-col v-if="action.type === 'detals'" :span="12">
		    <el-form-item label="处理人" prop="handleBy">
		      <el-input v-model="form.handleBy" disabled placeholder="请输入处理人" />
		    </el-form-item>
		  </el-col>
		  <el-col v-if="action.type === 'detals'" :span="12">
		    <el-form-item label="处理时间" prop="handleTime">
		      <el-input v-model="form.handleTime" disabled placeholder="请输入处理时间" />
		    </el-form-item>
		  </el-col>
		</el-row>
		
		<el-row>
		  <el-form-item label="处理内容" prop="handleContent">
		    <el-input v-model="form.handleContent" :disabled="action.type === 'detals'" type="textarea" placeholder="请输入处理内容" />
		  </el-form-item>
		</el-row>

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/dregCarAlert"

  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections
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
	  dictData: {},
    },
    data() {
      return {
        loading: false,
        form: {
        },
		// 是否关联喷淋系统
		spraySystemOptions: [
			{
				label: '是',
				value: '1'
			},
			{
				label: '否',
				value: '0'
			},
		],

        rules: {
          pointCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          pointName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          monitorType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          equipmentId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          unit: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          sort: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '处理' : '详情'
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
	  this.getDetail();
      /* if (this.action.type == "update") {
        this.getDetail();
      } */
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data;
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {

            var params = this.form;
            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  '处理成功'
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
	  //
	  spraySystemChange(value) {
		  if (value === '1') {
			 this.rules.startSprayValue = [
			   {required: true, message: '必填项不得为空', trigger: 'blur'}
			 ]
			 this.rules.closeSprayValue = [
			   {required: true, message: '必填项不得为空', trigger: 'blur'}
			 ]
		  } else {
			 delete this.rules.startSprayValue 
			 delete this.rules.closeSprayValue
		  }
	  }
    }
  }
</script>

<style lang="scss" scoped>
  .form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 10px);
  }

  .form
  /deep/
  .el-form-item__label {
    padding: 0px;
  }
  .form
  /deep/
  .el-date-editor{
    width: 100%;
  }
</style>
