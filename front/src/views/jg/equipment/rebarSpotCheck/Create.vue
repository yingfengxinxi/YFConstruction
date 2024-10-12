<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form :disabled="true" ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
		<create-sections title="点检图片">
		  <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    :src="form.img" width="100%"/>
		</create-sections>
        <el-row>
			<el-col :span="12">
				<el-form-item label="项目名称" prop="projectId">
				<el-select v-model="form.projectId" placeholder="请选择项目">
					<el-option
					v-for="item in dictData.projectOptions"
					:key="item.id"
					:label="item.name"
					:value="item.id">
					</el-option>
				</el-select>
				</el-form-item>
			</el-col>
          <el-col :span="12">
            <el-form-item label="钢筋类型" prop="rebarType">
          	<el-select v-model="form.rebarType" placeholder="请选择钢筋类型">
          	  <el-option
          		v-for="item in dictData.rebarTypeDict"
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
				<el-form-item label="规格" prop="specification">
				<el-input v-model="form.specification" placeholder="请输入规格"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
			  <el-form-item label="强度等级" prop="strengthGrade">
				<el-select v-model="form.strengthGrade" placeholder="请选择钢筋类型">
				  <el-option
					v-for="item in dictData.rebarStrengthDict"
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
				<el-form-item label="数量" prop="number">
					<el-input type="number" v-model="form.number" placeholder="请输入数量"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="点检日期" prop="spotCheckDate">
				  <el-date-picker
				    v-model="form.spotCheckDate"
				    type="date"
				    value-format="yyyy-MM-dd"
				    placeholder="请选择点检日期"
				  >
				  </el-date-picker>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="12">
				<el-form-item label="点检人员" prop="spotCheckPeople">
					<el-input v-model="form.spotCheckPeople" placeholder="请输入点检人员"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="点检结果" prop="spotCheckResult">
					<el-input v-model="form.spotCheckResult" placeholder="请输入点检结果"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
        
			<!--图片上传-->
			<wk-upload-img
			  name="file"
				ref="wkUploadImg"
			  multiple
			  accept="image/*"
			  list-type="picture-card"
			  :autoUpload="false"
			  :title="'图片上传'"
			  :batchId="batchId"
			></wk-upload-img>
      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  
  import {guid} from '@/utils'
  
  import WkUploadImg from '@/components/CreateCom/WkUploadImg'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/rebarSpotCheck"


  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadImg
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
        },
		// 数据默认字段
		defaultProps: {
		  parent: 'parentId',   // 父级唯一标识
		  value: 'id',          // 唯一标识
		  label: 'label',       // 标签显示
		  children: 'children', // 子级
		},
		pickerOptions: { // 限制收货时间不让选择今天以前的
		  disabledDate(time) {
		    return time.getTime() < Date.now()
		  }
		},
		// 是否启用
		enableOption: [
			{
				label: '启用',
				value: '1'
			},
			{
				label: '禁用',
				value: '0'
			}
		],

        rules: {
          rebarType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          specification: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          number: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          spotCheckDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
		
		batchId:'',
      }
    },
    computed: {
      title() {
		if (this.action.type === 'update') {
			return '编辑数据'
		} else if (this.action.type === 'details') {
			return '查看数据'
		} else {
			return '新建数据'
		}
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update" || this.action.type == "details") {
        this.getDetail();
      } else {
		this.batchId = guid()
		this.form.batchId = this.batchId
	  }
    },
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
			this.batchId = res.data.batchId
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
            
			var fileFlag = this.$refs['wkUploadImg'].submitAsync();
			if (!fileFlag) {
			    this.$message({
			        type: 'error',
			        message: '图片上传异常'
			    })
			    return;
			}

            var params = this.form;
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
