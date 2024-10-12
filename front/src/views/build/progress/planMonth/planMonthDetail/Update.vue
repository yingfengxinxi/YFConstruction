<template>
  <xr-create
    :loading="loading"
    title="进度填报"
    :confirm-button-text="'项目经理审批'"
    @close="close"
    @save="debouncedSaveField(true)">
    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
             class="form">
      <create-sections title="基本信息">

        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联楼座" prop="relatedBuildingId">
              <el-select
               
                v-model="form.relatedBuildingId"
                placeholder="请选择"
                clearable
                size="small">
                <el-option
                    v-for="dict in queryLoftMap.loftList"
                    :key="dict.id"
                    :label="dict.building"
                    :value="dict.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否里程碑" prop="ifMilestone">
              <el-select
                v-model="form.ifMilestone"
                placeholder="请选择"
                clearable
                size="small">
                <el-option
                    v-for="dict in ifMilestoneOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划起日"  prop="planStartDate">
              <el-date-picker
                class="date"
                v-model="form.planStartDate"
                type="date"
                placeholder="请选择"
                align="right"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划至日"  prop="planEndDate">
              <el-date-picker
                class="date"
                v-model="form.planEndDate"
                type="date"
                placeholder="请选择"
                align="right"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划天数"  prop="planGqiNumber">
              <el-input type="number" v-model="form.planGqiNumber" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划完成比例%"  prop="planFinishPersent">
              <el-input type="number" v-model="form.planFinishPersent" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际起日" prop="actualStartDate">
              <el-date-picker
                v-model="form.actualStartDate"
                type="date"
                placeholder="请选择"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际至日" prop="actualEndDate">
              <el-date-picker
                class="date"
                v-model="form.actualEndDate"
                type="date"
                placeholder="请选择"
                align="right"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际天数" prop="actualGqiNumber">
              <el-input type="number" v-model="form.actualGqiNumber" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际完成比例%" prop="actualFinishPersent">
              <el-input type="number" v-model="form.actualFinishPersent" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="延误天数" prop="delayDays">
              <el-input type="number" v-model="form.delayDays" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="延期责任" prop="delayDuty">
             
               <el-select
                v-model="form.delayDuty"
                placeholder="请选择"
                clearable
                size="small">
                <el-option
                    v-for="dict in  delayDutyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="责任单位" prop="responsibleParty">
              <el-input v-model="form.responsibleParty" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="延期原因" prop="delayReason">
              <el-select
                v-model="form.delayReason"
                placeholder="请选择"
                clearable
                size="small">
                <el-option
                    v-for="dict in  delayReasonOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="填报说明" prop="fillInDescription">
              <el-input type="textarea" :rows="2" v-model="form.fillInDescription" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="延期原因描述" prop="delayReason">
              <el-input type="textarea" :rows="2" v-model="form.delayReason" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="采取措施" prop="measures">
              <el-input type="textarea" :rows="2" v-model="form.measures" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
          
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="图片上传">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="*"
                :autoUpload="false"
                :batchId="form.batchId"
              ></wk-upload-file>
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

  import {debounce} from 'throttle-debounce'

  import {updateOne,selectById} from '@/views/build/progress/api/planMonth'
  import {selectLoft } from '@/views/build/progress/api/planCreate'
	

  //业务审批Copy:审批流程
  import ExamineMixin from '@/components/common/examine/mixins/examine'
  import CreateExamine from '@/components/common/examine/create'

  export default {
    // 进度填报
    name: 'planProgress',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
      CreateExamine
    },
    mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
      id:{},
      businessType:''
    },
    data() {
      return {
        loading: false,

        form:{},

        rules: {
          
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          
        },

        queryLoftMap:{},//关联楼座
        ifMilestoneOptions:[],//是否里程碑字典项
        delayDutyOptions:[],//延期责任
        delayReasonOptions:[],//延期原因


      }
    },
    computed: {
     

    },
    watch: {},
    created() {

      //  是否里程碑字典项
			this.getDicts('sys_yes_no').then(response => {
				this.ifMilestoneOptions = response.data
      })

      // 延期责任
      this.getDicts('b_progress_delay_duty').then(response => {
				this.delayDutyOptions = response.data
      })
      //延期原因
      this.getDicts('b_progress_delay_reason').then(response => {
				this.delayReasonOptions = response.data
      })

      // 关联楼层下拉 
      this.selectLoft()
      
      this.debouncedSaveField = debounce(300, this.updateOne)
      
      this.getList()

    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {

      getList(){
        selectById(this.id)
					.then(res => {
            this.form = res.data;
            this.loading=false;
					})
      },

      //查询 关联楼层下拉 
			selectLoft(){
				selectLoft().then(res=>{
					
					this.queryLoftMap = res.data

					
				})
			},
      
      /**
       * 保存
       */
      updateOne(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            //如果为异步上传需要主动调用提交

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
            updateOne(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  '编辑成功'
                )
                this.close()
                this.$emit('save');
               

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
        this.$emit('hiden-view')
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
