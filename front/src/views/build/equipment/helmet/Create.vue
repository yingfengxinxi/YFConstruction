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
            <el-form-item label="设备编号" prop="imei">
              <el-input v-model="form.imei" placeholder="请输入设备编号"></el-input>
            </el-form-item>
          </el-col>
		  <el-col :span="12">
		    <el-form-item label="项目人员" prop="peopleId">
		      <el-select v-model="form.peopleId" placeholder="请选择项目人员">
		        <el-option
		          v-for="item in dictData.peopleLict"
		          :key="item.id"
		          :label="item.name"
		          :value="item.id">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="呼叫状态" prop="sos">
              <el-select v-model="form.sos" placeholder="请选择呼叫状态">
                <el-option
                  v-for="item in dictData.sosStateDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电压" prop="vol">
              <el-input v-model="form.vol" placeholder="请输入电压"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
		<el-row>
          <el-col :span="12">
            <el-form-item label="信号强度" prop="gsm">
              <el-input v-model="form.gsm" placeholder="请输入信号强度"></el-input>
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

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/helmet"

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
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
			imei: [
				{required: true, message: '必填项不得为空', trigger: 'change'},
			],
			sos: [
				{required: true, message: '必填项不得为空', trigger: 'change'},
			],
		},
		
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑数据' : '新建数据'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
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
