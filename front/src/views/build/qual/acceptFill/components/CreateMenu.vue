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
            <el-form-item label="验收类型" prop="acceptType">
              <el-select v-model="menuForm.acceptType" placeholder="请选择" clearable :disabled="ifDisabled">
                <el-option v-for="dict in acceptTypes" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收项目" prop="acceptPro">
              <el-input :disabled="ifDisabled" v-model="menuForm.acceptPro" placeholder="请输入验收项目"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="验收内容" prop="acceptContent">
              <el-input :disabled="ifDisabled" type="textarea" :rows="2" v-model="menuForm.acceptContent" placeholder="请输入验收内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收结果" prop="acceptResult">
              <el-input  v-model="menuForm.acceptResult" placeholder="请输入验收结果"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否通过" prop="isAdopt">
              <el-select v-model="menuForm.isAdopt" placeholder="请选择通过状态"  clearable >
                <el-option v-for="dict in isAdopts" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!--申报材料上传-->
          <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="*"
                :autoUpload="false"
                :batchId="menuForm.batchId"
              ></wk-upload-file>

      </el-form>

    <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary" v-show="!disabledT"
          @click.native="saveInfo">确 定</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import {saveInfo, updateById} from '@/views/build/progress/api/monthEquipmentLease'
  import { guid } from '@/utils'
  
  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件
        WkUploadFile
    },
    filters: {//过滤器
    },
    props: {//属性
     form:{},
     actionType:'',
    },
    
    data() {
      return {
        loading: true,
        tableHeight: document.documentElement.clientHeight - 450, // 表的高度

        // 自定义字段验证规则
        memuRules: {
          acceptResult: [
            { required: true, message: "必填项不得为空", trigger: "blur" }
          ],
          isAdopt: [
            { required: true, message: "必填项不得为空", trigger: "blur" }
          ]
        },
        menuForm:{},

         //字典项
        acceptTypes: [],//验收类型
        isAdopts: [{ 'dictValue': '0', 'dictLabel': '否' }, { 'dictValue': '1', 'dictLabel': '是' }],//是否通过
        
      }
    },
    created(){

      this.getDicts("b_qual_accept_type").then(response => {
        this.acceptTypes = response.data;
      });

      if(this.actionType == 'create'){
        this.menuForm = {}
        this.menuForm.batchId = guid()
      }else{
        this.menuForm = this.form
      }
      
      this.loading = false
    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '验收项目明细填报' : (this.actionType === 'show' ? '查看验收项目明细' : '新建验收项目明细')   
      },
      disabledT() {
        return this.actionType === 'show' ? true : false
      },
      ifDisabled(){
        return this.actionType === 'create' ? false : true
      }

    },
    mounted() {//渲染完成后
      
    },
    destroyed() {//销毁后
    },
    watch:{

    },
    methods: {//声明方法

     
      
      saveInfo() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {

            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '文件上传异常'
              })
              return;
            }
            
            this.$emit('save',this.menuForm)
            
            
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

  .el-form /deep/ .el-form-item {
    margin-bottom: 15px;
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


</style>
