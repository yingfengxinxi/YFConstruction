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
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="观测时间" prop="observeTime">
              <el-date-picker
                v-model="form.observeTime"
                type="datetime"
                placeholder="请选择观测时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="观测人" prop="observeBy">
              <el-input v-model="form.observeBy" placeholder="请输入观测人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计算人" prop="countBy">
              <el-input v-model="form.countBy" placeholder="请输入计算人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="校对人" prop="calibrationBy">
              <el-input v-model="form.calibrationBy" placeholder="请输入校对人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测单位" prop="observeUnit">
              <el-input v-model="form.observeUnit" placeholder="请输入监测单位"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注说明">
              <el-input type="textarea" :rows="2" v-model="form.remarks" placeholder="请输入备注说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="监测报告" >
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

  import {saveOrUpdate, getById} from "../api/pitReport"

  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile

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
        form: {},
        batchId:'',
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
        pointType: [
          {
            label: '警报提醒',
            value: '1'
          },
          {
            label: '预警提醒',
            value: '0'
          },
        ],
        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          observeTime: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          observeBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          countBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          calibrationBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          observeUnit: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          /* monitorSonType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ], */
        },
        equipmentOptions: [],
        // 监测项子项数据
        monitorSonTypeDict: [],
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑' : '新建'
      },

    },
    watch: {

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }else {
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
            if (this.form.monitorType) {
              this.getMonitorSonTypeDict(this.form.monitorType)
            }
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
            this.form.batchId = this.batchId;
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if(!fileFlag){
              this.$message({
                type: 'error',
                message: '方案附件提交异常'
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
      },
      monitorTypeChange(value) {
        this.monitorSonType = ''
        // 查询监测项子类型数据
        this.getMonitorSonTypeDict(value)
      },
      getMonitorSonTypeDict(value) {
        this.getDicts('b_equipment_monitor_type_' + value).then(res => {
          this.monitorSonTypeDict = res.data
        })
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
  .el-date-editor {
    width: 100%;
  }
</style>
