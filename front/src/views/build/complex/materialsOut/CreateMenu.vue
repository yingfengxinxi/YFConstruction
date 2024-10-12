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
        <el-row >
          <el-col :span="12">
            <el-form-item label="物料编号" prop="inId">
              <el-select
                          v-model="menuForm.inId"
                          placeholder="请选择"
                          clearable
                          size="small"
                          @change="changeInCode"
                          style="width: 100%">
                <el-option
                    v-for="dict in inIdAndCodeList"
                    :key="dict.id"
                    :label="dict.code"
                    :value="dict.id" />
              </el-select>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料名称" prop="name">
              <el-input :disabled="true" v-model="menuFormIn.name"/>
            </el-form-item>  
          </el-col>
          
        </el-row>

        <el-row v-show="disabledT">
          <el-col :span="12">
            <el-form-item label="规格" prop="specification">
              <el-input :disabled="true" v-model="menuFormIn.specification"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input :disabled="true" v-model="menuFormIn.model"/>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row v-show="!disabledT">
          
          <el-col :span="12">
            <el-form-item label="退场数量" prop="outNumber">
              <el-input type="number" v-model="menuForm.outNumber"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="unit">
              <el-input :disabled="true" v-model="menuFormIn.unit"/>
            </el-form-item>  
          </el-col>
          
        </el-row>

        <el-row>
          <el-col :span="12">
             <el-form-item label="退场时间" prop="outTime">
              <el-date-picker
                v-model="menuForm.outTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
             <el-form-item label="退料人员" prop="materialClerk">
              <el-input  v-model="menuForm.materialClerk"/>
            </el-form-item>  
          </el-col>
         
        </el-row>
        <el-row>
           <el-col :span="12">
             <el-form-item label="见证人员" prop="witness">
              <el-input  v-model="menuForm.witness"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
             <el-form-item label="生产厂家" prop="manufacturer">
              <el-input :disabled="true" v-model="menuFormIn.manufacturer"/>
            </el-form-item>  
          </el-col>
          
        </el-row>
        <el-row>
          <el-col :span="12">
             <el-form-item label="供应单位" prop="supplyUnit">
              <el-input :disabled="true" v-model="menuFormIn.supplyUnit"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
             <el-form-item label="购买单位" prop="purchaseUnit">
              <el-input :disabled="true" v-model="menuFormIn.purchaseUnit"/>
            </el-form-item>  
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
             <el-form-item label="退场原因" prop="outReason">
              <el-input type="textarea" :rows="2" v-model="menuForm.outReason"/>
            </el-form-item>  
          </el-col>
        </el-row>
        


      </el-form>

  <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary" v-show="!disabledT"
          @click.native="saveMaterialsOut">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">

  import {saveMaterialsOut, updateById} from '@/views/build/complex/api/materialsOut'
  import {selectIdAndCodeLists ,selectById} from '@/views/build/complex/api/materialsIn'

  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件

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
          
          inId:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],

          outNumber:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
         
          outTime:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
        },

        menuFormIn:{},
        inIdAndCodeList:[],//物料进场的id和code
        
      }
    },
    created(){

      this.selectIdAndCodeLists()

      this.menuFormIn = this.menuForm

      this.loading= false

    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑物料退场' : (this.actionType === 'show' ? '查看物料退场' : '新建物料退场')   
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
      
      saveMaterialsOut() {

        this.$refs.menuForm.validate(valid => {
      
          if (valid) {

            this.loading = true;

            if(this.actionType=='create'){
              saveMaterialsOut(this.menuForm)
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

      selectIdAndCodeLists(){
        selectIdAndCodeLists().then(res=>{
          this.inIdAndCodeList = res.data
        })
      },

      changeInCode(inId){
        selectById(inId).then(res=>{
            this.menuFormIn = res.data
            this.$forceUpdate()//强制刷新
        })
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
