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
          <el-col :span="24">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="form.taskName" placeholder="请输入任务名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
		  <el-col :span="12">
		    <el-form-item label="是否启用" prop="isEnable">
		      <el-select v-model="form.isEnable" placeholder="请选择是否启用">
		        <el-option
		          v-for="item in enableOption"
		          :key="item.value"
		          :label="item.label"
		          :value="item.value">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="喷淋时长(分钟)" prop="duration">
		      <el-input type="number" v-model="form.duration" placeholder="请输入喷淋时长"
		      ></el-input>
		    </el-form-item>
		  </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="执行周期" prop="cycle">
              <el-input type="number" v-model="form.cycle" placeholder="请输入执行周期"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行周期单位" prop="cycleUnit">
              <el-select v-model="form.cycleUnit" placeholder="请选择执行周期单位"
              >
                <el-option
                  v-for="item in dictData.cycleUnitDict"
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
            <el-form-item label="启动时间" prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
				value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择启动时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col v-if="action.type === 'update'" :span="12">
            <el-form-item label="下次执行时间" prop="nextTime">
              <el-date-picker
                v-model="form.nextTime"
                type="datetime"
				value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择下次执行时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/sprayTask"

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
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
          taskName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          startTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          duration: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          cycle: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          cycleUnit: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          isEnable: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑任务' : '新建任务'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }
    },
	watch: {},
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
