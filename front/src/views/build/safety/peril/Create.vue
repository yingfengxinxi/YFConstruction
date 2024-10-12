<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <el-form
      ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
      class="form">
      <create-sections title="基本信息">

        <el-row>
          <el-col :span="12">
            <el-form-item label="项工程类型" prop="projectType">
              <el-input v-model="form.projectType" placeholder="项工程类型"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="辨识标准" prop="criteria">
              <el-input v-model="form.criteria" placeholder="辨识标准"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="管理方案" prop="managePlan">
              <el-input :rows="2" v-model="form.managePlan" type="textarea" placeholder="管理方案"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="方案附件">
              <wk-upload-file
                ref="wkUploadFile"
                :auto-upload="false"
                :batch-id="batchId"
                name="file"
                multiple
                accept="*"
              />
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

import WkUploadFile from '@/components/CreateCom/WkUploadFile'

import { guid } from '@/utils'

import { debounce } from 'throttle-debounce'

import { saveOrUpdate, getById } from '../api/peril'

// 业务审批Copy:审批流程
// import ExamineMixin from '@/components/common/examine/mixins/examine'
// import CreateExamine from '@/components/common/examine/create'

export default {
  // 新建编辑
  name: 'PlanCreate',
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile
  },
  // mixins: [ExamineMixin],//业务审批Copy:审批
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
      form: {},
      batchId: '',

      rules: {
        projectType: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        criteria: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        managePlan: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑方案' : '新建方案'
    }

  },
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save)
    // 考虑附件ID
    if (this.action.type == 'update') {
      this.getDetail()
    } else {
      this.batchId = guid()
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
          this.form = res.data
          this.batchId = res.data.batchId
          console.log(res.data)
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
          // 如果为异步上传需要主动调用提交
          this.form.batchId = this.batchId
          var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '方案附件提交异常'
            })
            return
          }

          var params = this.form
          this.loading = true
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
            this.loading = false
          }).catch(err => {
            this.loading = false
          })
        }
      })
    },
    /**
       * 关闭
       */
    close() {
      console.log('close')
      this.$emit('close')
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
  .el-date-editor {
    width: 100%;
  }
</style>
