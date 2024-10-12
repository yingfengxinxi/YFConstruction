<template>
  <el-dialog
    :title="title"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    @close="hidenView">

      <el-form
        v-loading="loading"
        class="form"
        ref="menuForm"
        :rules="memuRules"
        :model="menuForm"
        :disabled="disabledT"
        label-width="115px">
        <el-row>
          <el-col :span="12">
              <el-form-item label="记录人姓名" prop="recordPeopleName">
                <el-input v-model="menuForm.recordPeopleName" />
              </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="派工时间" prop="dispatchTime">
              <el-date-picker
                v-model="menuForm.dispatchTime"
                type="datetime"
                placeholder="选择日期时间"
                clearable
                value-format="yyyy-MM-dd HH:mm:ss"
                >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="派工单位" prop="dispatchUnitName">
              <el-input  v-model="menuForm.dispatchUnitName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="派工班组" prop="dispatchTeamId">
              <el-select
                    v-model="menuForm.dispatchTeamId"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 100%">
              <el-option
                      v-for="dict in teamList"
                      :key="dict.id"
                      :label="dict.teamName"
                      :value="dict.id" />
            </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="派工人员" prop="dispatchPeopleId">
              <el-select
                          v-model="menuForm.dispatchPeopleId"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="width: 100%">
                    <el-option
                            v-for="dict in oddList"
                            :key="dict.id"
                            :label="dict.name"
                            :value="dict.id" />
                  </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签证部门" prop="visaDepartmentName">
              <el-input  v-model="menuForm.visaDepartmentName"/>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="作业开始时间" prop="workTimeStart">
              <el-date-picker
                v-model="menuForm.workTimeStart"
                type="datetime"
                placeholder="选择日期时间"
                clearable
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="yxqq"
                @blur="yxqq"
                >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作业结束时间" prop="workTimeEnd">
              <el-date-picker
                v-model="menuForm.workTimeEnd"
                type="datetime"
                placeholder="选择日期时间"
                clearable
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="yxqq"
                @blur="yxqq"
                >
              </el-date-picker>
            </el-form-item>
          </el-col>

          </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="责任人" prop="responsibleName">
              <el-input  v-model="menuForm.responsibleName" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row style="margin-bottom: 10px;">

          <el-col :span="24">
            <el-form-item label="派工原因" prop="dispatchReason">
              <el-input  type="textarea" :rows="2" v-model="menuForm.dispatchReason" />
            </el-form-item>
          </el-col>
          </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="派工内容" prop="dispatchText">
              <el-input  type="textarea" :rows="2" v-model="menuForm.dispatchText" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="影像见证资料">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="video/*"
                :autoUpload="false"
                :batchId="batchId"
              ></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>

  <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary" v-show="!disabledT"
          @click.native="debouncedSaveField(true)">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">
  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {guid} from '@/utils'
  import {debounce} from 'throttle-debounce'

  import {saveOddVisa, updateById} from '@/views/build/labour/api/oddVisa'
	import { getWTList, getListByFBS, getListByBZ ,selectOddIdAndName} from '@/api/build/infocollection/infocoll'


  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件
      XrCreate,
      CreateSections,
      WkUploadFile

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
          recordPeopleName: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          dispatchTime:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          dispatchUnitName:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          dispatchText:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          dispatchTeamId:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          dispatchPeopleId:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
        },

        batchId:'',

				teamList:[],//班组
				oddList:[],//零工

      }
    },
    created(){
      this.debouncedSaveField = debounce(300, this.saveOddVisa)

			this.getListByBZ()//班组
      this.selectOddIdAndName()//零工

      if(this.actionType == 'create'){
        this.batchId = guid()
        this.menuForm.batchId = this.batchId
      }else{
        this.batchId = this.menuForm.batchId
      }

      this.loading= false

    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑零工签证' : (this.actionType === 'show' ? '查看零工签证' : '新建零工签证')
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

			//班组
			getListByBZ(){
				getListByBZ().then(res=>{
					this.teamList = res.data
				})
      },
      //零工
			selectOddIdAndName(){
				selectOddIdAndName().then(res=>{
					this.oddList = res.data
				})
      },

      saveOddVisa() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {

            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '视频上传异常'
              })
              return;
            }

            //派工班组
            this.menuForm.dispatchTeamName = this.teamNameFormat(this.menuForm.dispatchTeamId,this.teamList)
            //派工人员
            this.menuForm.dispatchPeopleName = this.oddFormat(this.menuForm.dispatchPeopleId,this.oddList)

            this.loading = true;

            if(this.actionType=='create'){
              saveOddVisa(this.menuForm)
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

      //派工班组
			teamNameFormat(value,datas){

				var actions = []

				Object.keys(datas).some((key) => {
				if (datas[key].id == ('' + value)) {
					actions.push(datas[key].teamName)
					return true
				}
				})
				return actions.join('')
      },

      //派工人员
			oddFormat(value,datas){

				var actions = []

				Object.keys(datas).some((key) => {
				if (datas[key].id == ('' + value)) {
					actions.push(datas[key].name)
					return true
				}
				})
				return actions.join('')
      },

      hidenView() {
        this.$emit('hiden-view')
      },
      yxqq(e){
        var kssj = this.menuForm.workTimeStart
        var jssj = this.menuForm.workTimeEnd
        if(kssj==""||kssj==null||kssj==undefined||jssj==""||jssj==null||jssj==undefined){
          return
        }else{
          var formatDate1 = new Date(kssj)
          var formatDate2 = new Date(jssj)
          if(formatDate1 > formatDate2){
            this.menuForm.workTimeEnd = ""
            this.$message({
              message: '作业结束时间不能小于作业开始时间',
              type: 'warning'
            })
          }
        }
      }

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

  .el-form /deep/ .el-form-item {
    margin-bottom: 0px;
  }

  // .el-form /deep/ .el-form-item.is-required .el-form-item__label:before {
    // content: '*';
    // color: #f56c6c;
    // margin-right: 4px;
    // position: absolute;
    // left: 0;
    // top: 5px;
  // }

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
