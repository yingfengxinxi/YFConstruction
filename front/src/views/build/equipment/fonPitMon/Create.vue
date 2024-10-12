<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
	
	<el-form ref="form" :model="checkForm" :rules="rules" label-position="top" label-width="100px"
		class="form">
	  <create-sections title="基本信息" v-if="action.type != 'receipt'" >
        <el-row>
          <el-col :span="12">
            <el-form-item label="选择监测点" prop="pointId">
							<el-select
								v-model="checkForm.pointId"
								placeholder="请选择"
								clearable
							>
								<el-option
									v-for="dict in dictData.pointList"
									:key="dict.pointId"
									:label="dict.pointCode"
									:value="dict.pointId"
								/>
							</el-select>
            </el-form-item>
          </el-col>
			<el-col :span="12">
			  <el-form-item label="记录值(mm)" prop="value">
				<el-input v-model="checkForm.value" placeholder="请输入记录值"></el-input>
			  </el-form-item>
			</el-col>
        </el-row>
		<!-- <el-row>
			<el-col :span="12">
			<el-form-item label="累计变化量(mm)" prop="qualDocumentNumber">
			  <el-input v-model="checkForm.qualDocumentNumber" placeholder="请输入累计变化量"></el-input>
			</el-form-item>
		  </el-col>
		  <el-col :span="12">
			<el-form-item label="单次变化量(mm)" prop="qualPhone">
			  <el-input v-model="checkForm.qualPhone" placeholder="请输入单次变化量"></el-input>
			</el-form-item>
		  </el-col>
		</el-row> -->
		<el-row>
		  
			<!-- <el-col :span="12">
			  <el-form-item label="变化速率(mm/d)" prop="qualCheckCom">
				<el-input v-model="checkForm.qualCheckCom" placeholder="变化速率"></el-input>
			  </el-form-item>
			</el-col> -->
			<el-col :span="12">
			<el-form-item label="采集时间" prop="monitorTime">
			  <el-date-picker
			    v-model="checkForm.monitorTime"
			    type="datetime"
			    placeholder="请选择时间"
			    value-format="yyyy-MM-dd HH:mm:ss">
			  </el-date-picker>
			</el-form-item>
		  </el-col>
		</el-row>
     </create-sections>
	</el-form>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import { XhStrucUserCell} from '@/components/CreateCom'

  import {debounce} from 'throttle-debounce'
    import {saveOrUpdate, selectById} from './../api/fonPitMon'



  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
	  XhStrucUserCell,
	  CreateSections
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
      action: {
        type: Object,	
		// 是否只读
		isReadonly: false,
        default: () => {
          return {
            type: 'save',
            id: '',
          }
        }
      },
	  // 字典数据
	  dictData:{
	  	pointList: []
	  }
    },
    data() {
      return {
		  radio:false,
        loading: false,
		// 质量巡检基本信息
        checkForm: {
        },

        rules: {
          qualCheckType: [
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ],
          value: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          monitorTime: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
		var title = '添加数据'
		if (this.action.type === 'update') {
			title = '编辑数据'
		} 
        return title
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
     //考虑附件ID
      if (this.action.type == "update" || this.action.type == "receipt") {
        this.getDetail();
      }
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
        selectById(this.action.id)
          .then(res => {
			this.checkForm = res.data;
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
			  var params = this.checkForm;
			  this.loading = true;
			  saveOrUpdate(params).then(response => {
			    if (response.code === 0) {
			      this.$message.success(
			        this.action.type == 'update' ? '编辑成功' : '添加成功'
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
        this.$emit('close')
      }, 
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
  .el-select{
	 width: 100%; 
  }
  /deep/
  .el-input.is-disabled .el-input__inner{
	  background: none!important;
	  color: #666;
  }
</style>
