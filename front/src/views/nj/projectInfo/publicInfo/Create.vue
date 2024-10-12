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
            <el-form-item label="公示标题" prop="publicTitle">
              <el-input v-model="form.publicTitle" placeholder="请输入公示标题"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公示时间" prop="publicTime">
				<el-date-picker
					v-model="form.publicTime"
					type="datetime"
					placeholder="选择公示时间"
					value-format="yyyy-MM-dd HH:mm:ss">
				</el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
				
        <el-row>
          <el-col :span="12">
            <el-form-item label="公示人" prop="publicPeople">
              <el-input v-model="form.publicPeople" placeholder="请输入公示人"/>
            </el-form-item>
          </el-col>
        </el-row>
				
        <el-row>
          <el-col :span="24">
            <el-form-item label="公示内容" prop="publicConent">
              <el-input v-model="form.publicConent" :rows="10" type="textarea" placeholder="请输入公示内容"/>
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

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/publicInfo"

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
          publicTitle: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          publicTime: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          publicPeople: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          publicConent: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑公示信息' : '新建公示信息'
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
