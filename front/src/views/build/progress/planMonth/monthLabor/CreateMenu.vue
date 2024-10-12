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
            <el-form-item label="年月" prop="planMonth">
              <el-date-picker
                v-model="menuForm.planMonth"
                type="month"
                placeholder="选择日期时间"
                clearable
                value-format="yyyy-MM"
                >
              </el-date-picker>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
              <el-form-item label="劳务工种" prop="workTypeId">
                 <el-select
                    v-model="menuForm.workTypeId"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 100%">
                    <el-option
                        v-for="option in worktypeList"
                        :key="option.id"
                        :label="option.workType"
                        :value="option.id"
                      />
                  </el-select>
              </el-form-item>
          </el-col>
          
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="本月计划人数" prop="curMonPlanPeopleNum">
              <el-input type="number" v-model="menuForm.curMonPlanPeopleNum"/>
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="本月实际人数" prop="curMonActualPeopleNum">
              <el-input type="number" v-model="menuForm.curMonActualPeopleNum"/>
            </el-form-item>  
          </el-col>
        </el-row>
        
        <el-row>
         
          <el-col :span="12">
            <el-form-item label="本月偏差" prop="curMonDeviation">
              <el-input v-model="menuForm.curMonDeviation"/>
            </el-form-item>  
          </el-col>
           <el-col :span="12">
            <el-form-item label="工程量" prop="engineeringMount">
              <el-input v-model="menuForm.engineeringMount" />
            </el-form-item>  
          </el-col>
        </el-row>
        

      </el-form>

  <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary" v-show="!disabledT"
          @click.native="saveInfo">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">

  import {saveInfo, updateById} from '@/views/build/progress/api/monthLabor'

  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件

    },
    filters: {//过滤器
    },
    props: {//属性
     menuForm:{},
     worktypeList:{},
     actionType:''
    },
    data() {
      return {
        loading: true,
        tableHeight: document.documentElement.clientHeight - 450, // 表的高度

        // 自定义字段验证规则
        memuRules: {
          planMonth: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          workTypeId:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
         
         
        },
        
      }
    },
    created(){
      this.loading = false
    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑设备租赁明细' : (this.actionType === 'show' ? '查看设备租赁明细' : '新建设备租赁明细')   
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

      
      saveInfo() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {

            this.loading = true;

            if(this.actionType=='create'){
              saveInfo(this.menuForm)
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
