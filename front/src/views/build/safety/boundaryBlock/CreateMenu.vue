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
              <el-form-item label="防护网编号" prop="protectiveNetCode">
                <el-input v-model="menuForm.protectiveNetCode" />
              </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="防护网形式" prop="protectiveNetType">
              <el-input  v-model="menuForm.protectiveNetType"/>
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
            <el-form-item label="单体工程" prop="monolithicEngineering">
              <el-input  v-model="menuForm.monolithicEngineering"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼层" prop="loftNumber">
              <el-input type="number" v-model="menuForm.loftNumber"/>
            </el-form-item>  
          </el-col>
          
        </el-row>
        <el-row>
          <el-col :span="12">
             <el-form-item label="位置" prop="position">
              <el-input  v-model="menuForm.position"/>
            </el-form-item>  
          </el-col>
          
          </el-row>


      </el-form>

  <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary" v-show="!disabledT"
          @click.native="saveBoundaryBlock">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">

  import {saveBoundaryBlock, updateById} from '@/views/build/safety/api/boundaryBlock'


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
          protectiveNetCode: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          protectiveNetType:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          name:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          type:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          
        },

        batchId:'',

			
        typeOptions:[],//类型
        
      }
    },
    created(){

      //类型（基坑边界、施工边界）
			this.getDicts('b_safety_boundary_block_type').then(response=>{
				this.typeOptions = response.data
      })
      
      this.loading= false

    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑边界区块管理' : (this.actionType === 'show' ? '查看边界区块管理' : '新建边界区块管理')   
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
      
      saveBoundaryBlock() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {

            this.loading = true;

            if(this.actionType=='create'){
              saveBoundaryBlock(this.menuForm)
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
