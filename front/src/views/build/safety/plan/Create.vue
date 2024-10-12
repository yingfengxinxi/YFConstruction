<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
             class="form">
      <create-sections title="基本信息">

        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入质量方案编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入质量方案名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="content">
              <el-input type="textarea" :rows="2" v-model="form.content" placeholder="请输入质量方案描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="编制日期" prop="enactDate">
              <el-date-picker
                v-model="form.enactDate"
                type="date"
                placeholder="请选择编制日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编制人" prop="enactBy">
              <el-input v-model="form.enactBy" placeholder="请输入编制人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="方案附件">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="*"
                :autoUpload="false"
                :batchId="batchId"
              ></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>


      </create-sections>
      <create-sections title="安全技术交底">

        <el-row>
          <el-col :span="8">
            <el-form-item label="交底日期" prop="jdDate">
              <el-date-picker
                v-model="form.jdDate"
                type="date"
                placeholder="请选择交底日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交底人" prop="jdBy">
              <el-input v-model="form.jdBy" placeholder="请输入交底人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="被交底人" prop="jdByB">
              <el-input v-model="form.jdByB" placeholder="请输入被交底人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="交底说明" prop="jdContent">
              <el-input type="textarea" :rows="2" v-model="form.jdContent" placeholder="请输入交底说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </create-sections>
    </el-form>
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

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/plan";

  //业务审批Copy:审批流程
  import ExamineMixin from '@/components/common/examine/mixins/examine'
  import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
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
      },
      businessType:''
    },
    data() {
      return {
        loading: false,
        form: {},
        batchId: '',

        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          enactDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          enactBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          jdContent: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          jdDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          jdBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          jdByB: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑方案' : '新建方案'
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      } else {
        this.batchId = guid();
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
            this.batchId = res.data.batchId;
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
            //如果为异步上传需要主动调用提交
            this.form.batchId = this.batchId;
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '方案附件提交异常'
              })
              return;
            }

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

  .form
  /deep/
  .el-date-editor {
    width: 100%;
  }
</style>
