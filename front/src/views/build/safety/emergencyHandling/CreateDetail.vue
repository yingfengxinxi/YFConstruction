<template>
<!-- 详细信息 -->
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)"
    :showSave="!disabledT">
    
    <create-sections title="详细信息">

      <el-form ref="formDetail" :model="formDetail" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px"
               class="form">
         <el-row>
          <el-col :span="12">
            <el-form-item label="处置类型" prop="handleType">
              <el-select
                  v-model="formDetail.handleType"
                  placeholder="请选择"
                  clearable
                  size="small">
              <el-option
                  v-for="dict in handleTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue" />
            </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处置人" prop="handleUserName">
              <el-input v-model="formDetail.handleUserName" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="处置时间" prop="handleTime">          
              <el-date-picker
                      v-model="formDetail.handleTime"
                      type="datetime" 
                      placeholder="选择时间" 
                      value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="情况说明" prop="informationNote">
              <el-input type="textarea" :rows="2" v-model="formDetail.informationNote" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="处置措施" prop="handleMeasures">
              <el-input type="textarea" :rows="2" v-model="formDetail.handleMeasures" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="附件">
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

  import {debounce} from 'throttle-debounce'

  import {updateDetail, save, getByIdDetail} from "../api/emergencyHandlingDetail"

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile
    },

    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            pid:'',
            id: '',
            data: {}
          }
        }
      },
    },
    data() {
      return {
        loading: false,
        formDetail: {
          // handleTime:this.initDataSel(),
        },
        list: [],
        handleTypeOptions : [],//处置类型
        tableHeight: document.documentElement.clientHeight - 320, // 表的高度
        rules: {
          handleType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ]
        },
        batchId: '',
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑应急事件处置详细' : (this.action.type === 'show' ? '查看应急事件处置详细' : '新建应急事件处置详细')   
      },
      disabledT() {
        return this.action.type === 'show' ? true : false
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      //处置类型
      this.getDicts('b_safety_handle_type').then(response => {
        this.handleTypeOptions = response.data
      })

      if (this.action.type == "save") {
        this.batchId = guid();
      }else{
         this.getDetail()
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
        getByIdDetail(this.action.id)
          .then(res => {
            this.formDetail = res.data;
            if(res.data.batchId == null){
              this.batchId = guid();
            }else{
              this.batchId = res.data.batchId;
            }
            
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        
        this.$refs['formDetail'].validate(valid => {
          if (valid) {
            //如果为异步上传需要主动调用提交
            this.formDetail.batchId = this.batchId;
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '方案附件提交异常'
              })
              return;
            }

            this.loading = true;

            this.formDetail.pid = this.action.pid
            if(this.action.type == 'update'){
                updateDetail(this.formDetail).then(response => {
                if (response.code === 0) {
                  this.$message.success(
                    '编辑成功'
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
            }else{
                save(this.formDetail).then(response => {
                if (response.code === 0) {
                  this.$message.success(
                    '添加成功'
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

          }
        })
      },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },

      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },


      //选择时间
      initDataSel(){
          //获取当前时间
          var now   = new Date()
          // var monthn = now.getMonth()+1>9?now.getMonth()+1:"0"+(now.getMonth()+1)
          // var yearn  = now.getFullYear()
          // var dayn = now.getDate()>9?now.getDate():"0"+(now.getDate())
          // var selectMonth = yearn+"-"+monthn +"-"+dayn
          
          return now
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
  #crm-table{
    height: 420px!important;
  }
  /deep/
  .el-input{
    width: 395px;
  }
</style>
