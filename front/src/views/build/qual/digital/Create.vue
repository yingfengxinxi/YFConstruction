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
            <el-form-item label="质量资料编号" prop="infoCode">
              <el-input v-model="form.infoCode" placeholder="请输入质量资料编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质量资料名称" prop="infoName">
              <el-input v-model="form.infoName" placeholder="请输入质量资料名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input type="textarea" :rows="2" v-model="form.remarks" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="提交日期" prop="submitDate">
              <el-date-picker
                v-model="form.submitDate"
                type="date"
                placeholder="请选择提交日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提交人" prop="submitUserId">
              <el-input v-model="form.submitUserId" placeholder="请提交编制人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="资料附件" prop="batchId">
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

  import {saveOrUpdate, getById} from "../api/digital";

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'digitalCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
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
          infoCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          infoName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          remarks: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          submitDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          submitUserId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑资料' : '新建资料'
      },

    },
    watch: {},
    created() {
      console.log("--created--"+this.batchId);
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
        console.log("--getDetail--this.batchId--"+this.batchId);
      }else{
        this.batchId = guid();
        console.log("--this.batchId--"+this.batchId);
      }
    },

    mounted() {
      console.log("--mounted--"+this.batchId);
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
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if(!fileFlag){
              this.$message({
                type: 'error',
                message: '资料附件提交异常'
              })
              return;
            }

            var params = this.form;
            params.batchId = this.batchId;
            console.log("--params.batchId--"+params.batchId);
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
