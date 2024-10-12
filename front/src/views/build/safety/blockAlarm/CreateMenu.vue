<template>
 
<xr-create
        :loading="loading"
        :title="title"
        :confirm-button-text="'保存'"
        @close="hidenView"
        @save="debouncedSaveField(true)"
        :showSave="!disabledT"
    >
      
      <el-form
        v-loading="loading"
        class="form"
        ref="menuForm"
        :rules="memuRules"
        :model="menuForm"
        :disabled="disabledT"
        label-width="115px">
        <create-sections title="报警信息" v-show="disabledT">
            <el-row>
              <el-col :span="12">
                  <el-form-item label="防护网编号" prop="protectiveNetCode">
                    <el-input v-model="menuForm.protectiveNetCode" />
                  </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="类型" prop="type">
                  <el-select
                        v-model="menuForm.type"
                        placeholder="请选择"
                        clearable
                        size="small"
                        style="width: 100%">
                  <el-option
                          v-for="dict in typeOptions"
                          :key="dict.dictValue"
                          :label="dict.dictLabel"
                          :value="dict.dictValue" />
                </el-select>
                </el-form-item>
              </el-col>
              
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="边界名称" prop="name">
                  <el-input  v-model="menuForm.name"/>
                </el-form-item>  
              </el-col>
              <el-col :span="12">
                <el-form-item label="单体工程" prop="monolithicEngineering">
                  <el-input  v-model="menuForm.monolithicEngineering"/>
                </el-form-item>  
              </el-col>
              
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="报警时间" prop="alarmTime">
                  <el-date-picker
                    v-model="menuForm.alarmTime"
                    type="datetime"
                    placeholder="选择日期时间"
                    clearable
                    value-format="yyyy-MM-dd HH:mm:ss"
                    >
                  </el-date-picker>
                </el-form-item>  
              </el-col>
              <el-col :span="12">
                  <el-form-item label="报警内容" prop="alarmText">
                    <el-input v-model="menuForm.alarmText" />
                  </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="报警类型" prop="alarmType">
                  
                  <el-select
                        v-model="menuForm.alarmType"
                        placeholder="请选择"
                        clearable
                        size="small"
                        style="width: 100%">
                    <el-option
                            v-for="dict in alarmTypeOptions"
                            :key="dict.dictValue"
                            :label="dict.dictLabel"
                            :value="dict.dictValue" />
                  </el-select>
                </el-form-item>  
              </el-col>
              
            </el-row>
        </create-sections>

        <create-sections title="处理信息">
          <el-row >
            <el-col :span="12">
                <el-form-item label="处理人员" prop="handlePeopleName">
                  <el-input  v-model="menuForm.handlePeopleName"/>
                </el-form-item>  
              </el-col>
              <el-col :span="12">
                <el-form-item label="处理时间" prop="handleTime">
                  <el-date-picker
                    v-model="menuForm.handleTime"
                    type="datetime"
                    placeholder="选择日期时间"
                    clearable
                    value-format="yyyy-MM-dd HH:mm:ss"
                    >
                  </el-date-picker>
                </el-form-item>  
              </el-col>
          </el-row>
        <el-row >
          
          <el-col :span="24">
            <el-form-item label="处理措施" prop="handleMeasures">
              <el-input  type="textarea" :rows="4" v-model="menuForm.handleMeasures" />
            </el-form-item>
          </el-col>
          </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="处理后结论" prop="handleConclusion">
              <el-input  type="textarea" :rows="4" v-model="menuForm.handleConclusion" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="过程照片">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="image/*"
                :autoUpload="false"
                :batchId="batchId"
              ></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>

      </el-form>

  
</xr-create>
</template>
<script type="text/javascript">
  import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import { debounce } from 'throttle-debounce'

  import {guid} from '@/utils'

	import {saveBlockAlarm, updateById } from '@/views/build/safety/api/blockAlarm'

  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件
      WkUploadFile,
      XrCreate,
      CreateSections

    },
    filters: {//过滤器
    },
    props: {//属性
     menuForm:{},
     actionType:''
    },
    data() {
      return {
        loading: true,
        tableHeight: document.documentElement.clientHeight - 450, // 表的高度

        // 自定义字段验证规则
        memuRules: {
          handlePeopleName: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          handleTime: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          handleMeasures: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          
        },

        typeOptions:[],//类型
        alarmTypeOptions:[],//报警类型

        batchId:'',
        
      }
    },
    created(){

      //类型（基坑边界、施工边界）
			this.getDicts('b_safety_boundary_block_type').then(response=>{
				this.typeOptions = response.data
      })
      //报警类型 
			this.getDicts('b_safety_blockAlarm_alarmType').then(response=>{
				this.alarmTypeOptions = response.data
      })
      
      if(this.actionType == 'create'){
        this.batchId = guid()
        this.menuForm.batchId = this.batchId
      }else{
        this.batchId = this.menuForm.batchId
      }
      
       this.debouncedSaveField = debounce(300, this.saveBlockAlarm)

      this.loading= false

    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑周界防护报警' : (this.actionType === 'show' ? '查看周界防护报警' : '新建周界防护报警')   
      },
      disabledT() {
        return this.actionType === 'show' ? true : false
      },

      

    },
    mounted() {//渲染完成后

    },
    destroyed() {//销毁后
    },
    methods: {//声明方法

      
      saveBlockAlarm() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {

            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '图片上传异常'
              })
              return;
            }

  
            this.loading = true;

            if(this.actionType=='create'){
              saveBlockAlarm(this.menuForm)
              .then(res => {
                this.msgSuccess('添加成功')
                this.$emit('save');
                this.hidenView();
                this.loading = false;
              })
              .catch(() => {
                this.loading = false
              })
            }else{
              updateById(this.menuForm)
              .then(res => {
                this.msgSuccess('修改成功')
                this.$emit('save');
                this.hidenView();
                this.loading = false;
              })
              .catch(() => {
                this.loading = false
              })
            }
            
          } else {
            this.$message.error('请完善必填信息')
            return false
          }
        })
      },


      hidenView() {
        this.$emit('hiden-view')
      },
      
    }
  }
</script>
<style lang="scss" scoped>
  .crm-create-container {
    position: relative;
    height: 100%;
  }

  .crm-create-flex {
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;
    flex: 1;
  }

  .crm-create-header {
    height: 40px;
    margin-bottom: 20px;
    padding: 0 10px;
    flex-shrink: 0;
    .close {
      display: block;
      font-size: 24px;
      color: #909399;
      margin-right: -10px;
      padding: 10px;
      cursor: pointer;
    }
    .close:hover {
      color: $xr-color-primary;
    }
  }

  .create-name {
    font-size: 14px;
    color: #333;
    font-weight: 600;
    padding-left: 12px;
    position: relative;
    margin-left: 15px;
    margin-bottom: 10px;
  }

  .create-name::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    height: 14px;
    border-radius: 2px;
    width: 4px;
    background-color: $xr-color-primary;
  }

  .crm-create-body {
    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
  }

  /** 将其改变为flex布局 */
  .crm-create-box {
    display: flex;
    flex-wrap: wrap;
    padding: 0 20px;
  }

  .crm-create-item {
    flex: 0 0 50%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  // 占用一整行
  .crm-create-block-item {
    flex: 0 0 100%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  .el-form-item /deep/ .el-form-item__label {
    font-size: 13px;
    color: #333333;
    position: relative;
    padding-left: 8px;
    padding-bottom: 0;
  }


  .form-label {
    margin: 5px 0;
    font-size: 13px;
    word-wrap: break-word;
    word-break: break-all;
  }

  .examine-items {
    padding: 10px 0;
  }

  .examine-item {
    padding-bottom: 8px;
    .examine-item-name {
      width: 60px;
      padding-left: 20px;
      font-size: 13px;
      margin-right: 20px;
    }
    .examine-item-select {
      margin-right: 20px;
    }
    .examine-item-user {
      flex: 1;
      margin-right: 42px;
    }
    .examine-item-delete {
      color: #ff6767;
      font-size: 22px;
      margin: 0 10px;
      display: none;
    }
  }

  .examine-item:hover {
    .examine-item-delete {
      display: block;
    }
    .examine-item-user {
      margin-right: 0px;
    }
  }

  .examine-items-add {
    padding: 5px 0 20px 0;
    font-size: 13px;
    color: $xr-color-primary;
  }

  .examine-add-des {
    font-size: 12px;
    background-color: #fffcf0;
    padding: 10px;
    line-height: 23px;
    color: #999;
    margin-bottom: 10px;
    .examine-add-required {
      color: #ff6767;
    }
  }

  .handle-bar {
    position: relative;
    .handle-button {
      float: right;
      margin-top: 5px;
      margin-right: 20px;
    }
  }

  .form
  /deep/
  .el-date-editor, .el-select{
    width: 100%;
  }

  // .el-row{
  //   margin-bottom: 20px;
  // }
</style>
