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
            <el-form-item label="变更方案" prop="planId">
              <diy-dict v-model="form.planId" sTb="b_qual_plan"
                          sColKey="id" sColVal="id" sColName="name">
              </diy-dict>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更原因" prop="reason">
              <el-input v-model="form.reason" placeholder="请输入变更原因"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容简述" prop="content">
              <el-input type="textarea" :rows="2" v-model="form.content" placeholder="请输入内容简述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="变更人" prop="bgBy">
              <el-input v-model="form.bgBy" placeholder="请输入变更人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更日期" prop="bgDate">
              <el-date-picker
                v-model="form.bgDate"
                type="date"
                placeholder="请选择变更日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="变更附件" prop="enactDate">
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

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/planbg";

  import DiyDict from "@/components/common/Dict/diy";

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
      DiyDict
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
      }
    },
    data() {
      return {
        loading: false,
        form: {
        },
        batchId:'',

        rules: {
          planId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          reason: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          bgDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          bgBy: [
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
      }else{
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
            if(!fileFlag){
              this.$message({
                type: 'error',
                message: '变更附件提交异常'
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
  .el-date-editor{
    width: 100%;
  }
</style>
