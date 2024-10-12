<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入客户名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="leadIndustryId">
              <el-select v-model="form.leadIndustryId" placeholder="请选择所属行业">
                <el-option
				  v-for="item in dictData.leadIndustryDict"
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
            <el-form-item label="客户状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择客户状态">
			    <el-option
				  v-for="item in dictData.customerStatusDict"
				  :key="item.dictValue"
				  :label="item.dictLabel"
				  :value="item.dictValue">
			    </el-option>
			  </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
		
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="联系人电话" prop="contactPhone">
		      <el-input v-model="form.contactPhone" placeholder="请输入联系人电话"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="来访时间" prop="visitedTime">
		      <el-date-picker
		        v-model="form.visitedTime"
		        type="date"
		        value-format="yyyy-MM-dd"
		        placeholder="请选择日期"
		      >
		      </el-date-picker>
		    </el-form-item>
		  </el-col>
		</el-row>
		
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="来源渠道" prop="source">
			  <el-select v-model="form.source" placeholder="请选择客户来源">
				<el-option
				  v-for="item in dictData.customerSourceDict"
				  :key="item.dictValue"
				  :label="item.dictLabel"
				  :value="item.dictValue">
				</el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="销售阶段" prop="saleStage">
		      <el-select v-model="form.saleStage" placeholder="请选择销售阶段">
				<el-option
				  v-for="item in dictData.saleStageDict"
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
		    <el-form-item label="客户需求" prop="requirements">
		      <el-input v-model="form.requirements" placeholder="请输入客户需求"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="24">
		    <el-form-item label="备注" prop="remarks">
		      <el-input v-model="form.remarks" type="textarea" :rows="4"
			  placeholder="请输入备注"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="意向房源" prop="typeId">
		      <select-tree :options="showTreeData" v-model="form.typeId" :props="defaultProps"/>
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
  import SelectTree from '@/components/SelectTree.vue'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  //import {saveOrUpdate, getById} from "../api/monitorPoint"

  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections,
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
		// 房源数据
		showTreeData: [],

        rules: {
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          leadIndustryId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          status: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑客户' : '新增客户'
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
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
		  this.form = this.action.data
        /* getById(this.action.id)
          .then(res => {
            this.form = res.data;
          })
          .catch(() => {
          }) */
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
