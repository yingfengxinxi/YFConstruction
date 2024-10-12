<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="sureBtnTitle?sureBtnTitle:'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" placeholder="请输入计划备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              <el-input v-model="form.content" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </create-sections>
    <!-- 业务审批Copy:流程 start -->
    <create-examine
      :businessType="businessType"
      @value-change="examineValueChange"
    ></create-examine>
    <el-button
      v-if="isOpenExamine"
      slot="footer"
      type="primary"
      plain
      @click.native="debouncedSaveField(false)">保存草稿
    </el-button>
    <!-- 业务审批Copy:流程 end -->
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate} from "./api";

  //业务审批Copy:审批流程
  import ExamineMixin from '@/components/common/examine/mixins/examine'
  import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'DemoCreate',
    components: {
      XrCreate,
      CreateSections,
      CreateExamine
    },
    mixins: [ExamineMixin],//业务审批Copy:审批
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
        rules: {
          number: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ]
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑回款' : '新建回款'
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      if (this.action.type == "update") {
        this.form = this.action.data;
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            var params = this.form;
            //业务审批Copy: 提交追加审批数据 start
            if (this.isOpenExamine) {
              if(!this.handleExamineInfo(isSubmit,params)){
                return;
              };
            }
            //业务审批Copy:end
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
</style>
