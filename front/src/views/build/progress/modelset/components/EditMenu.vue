<template>
  <el-dialog
    title="编辑任务"
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
      label-width="115px">
      <el-row>
        <el-col :span="12">
            <el-form-item label="模板名称" prop="mname">
              <el-input v-model="menuForm.mname" :disabled="true"/>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="父任务名称" prop="pname">
              <el-input v-model="menuForm.pname" :disabled="true"/>
            </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="等级" prop="level">
            <el-input type="number" v-model="menuForm.level"/>
          </el-form-item>  
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input v-model="menuForm.name" />
          </el-form-item>
        </el-col>
       
      </el-row>
      <el-row>
         <el-col :span="12">
            <el-form-item label="计划工期天数" prop="planGqiNumber">
              <el-input type="number" v-model="menuForm.planGqiNumber" />
            </el-form-item>
          </el-col>
         <el-col :span="12">
          <el-form-item label="是否关键路线" prop="ifCriticalRoute">
          <el-select
                v-model="menuForm.ifCriticalRoute"
                placeholder="请选择"
                clearable
                size="small"
                >
            <el-option
                    v-for="dict in ifCriticalRouteOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
         </el-select>
        </el-form-item>
        </el-col>
       
      </el-row>
        
      <el-row>
        <el-col :span="12">
          <el-form-item label="是否里程碑" prop="ifMilestone">
            <el-select
                  v-model="menuForm.ifMilestone"
                  placeholder="请选择"
                  clearable
                  size="small"
                  >
              <el-option
                      v-for="dict in ifMilestoneOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
          </el-select>
        
          </el-form-item>
        </el-col>
        <el-col :span="12">
           <el-form-item label="前置节点" prop="preNode">
              <select-tree :options="TreeData"
                            @selected="selectPreNode"
                            v-model="menuForm.preNode" 
                            :props="defaultProps" 
                            :expandAll="true"/>
              <!-- <el-tree :data="TreeData" 
              :props="defaultProps"
              v-model="menuForm.preNode" 
               default-expand-all
              @node-click="selectPreNode"> -->
              </el-tree>
            </el-form-item>
        </el-col>
         
      </el-row>

      <el-row>
       <el-col :span="12">
           <el-form-item label="排序" prop="serialNumber">
              <el-input type="number" v-model="menuForm.serialNumber"/>
            </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="备  注"
            prop="note">
            <el-input v-model="menuForm.note" :row="2"
                      :maxlength="300"
                      type="textarea"/>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click.native="updateMenu">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script type="text/javascript">
  import SelectTree from '@/components/SelectTree'
  import {
    updateMenu,selectTree
  } from '@/views/build/progress/api/modelset'

export default {
  name: 'EditMenu', // 所有新建效果的view
  components: {//声明组件
    SelectTree
  },
  filters: {//过滤器
  },
  props: {//属性
    menu: {
      type: Object,
      default: {}
    },
  },
  data() {
    return {
      loading: true,
      // 自定义字段验证规则
      memuRules: {
        name: [
          {required: true, message: '必填项不能为空', trigger: 'blur'}
        ],
        planGqiNumber:[
          {required: true, message: '必填项不能为空', trigger: 'blur'}
        ]
      },
      // 自定义字段信息表单
      menuForm: {
      },
      // menuTypeDisabled:false,

      ifMilestoneOptions:[],//是否里程碑
      ifCriticalRouteOptions:[],//是否关键路线

      TreeData:[],

      defaultProps: {
        parent: 'pid',   // 父级唯一标识
        value: 'id',          // 唯一标识
        label: 'name',       // 标签显示
        children: 'children', // 子级
      },

    }
  },
  created(){
      //  是否里程碑字典项
      this.getDicts('sys_yes_no').then(response => {
        this.ifMilestoneOptions = response.data
      })

      //  是否关键路线字典项
      this.getDicts('sys_yes_no').then(response => {
        this.ifCriticalRouteOptions = response.data
      })

      this.getMenuList()

  },
  computed: {//监听属性

  },
  mounted() {//渲染完成后
    this.loading = false;
  },
  destroyed() {//销毁后
  },
  methods: {//声明方法

  //选择前置节点  判断是否是自己,不能选择自己
    selectPreNode(data){
      
      if(data == this.menu.id){
        this.$message({
          type: 'error',
          message: '前置节点不能选择节点本身，请重新选择'
        })
        this.menuForm.preNode = null
        return;
      }
      
      
    },
    updateMenu(){
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          updateMenu(this.menuForm)
            .then(res => {

              this.$emit('save');
              this.hidenView();
              this.loading = false;
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          this.$message.error('请完善必填信息')
          return false
        }
      })
    },
    hidenView() {
      this.$emit('hiden-view')
    },

    //获取前置节点树
      getMenuList() {
				selectTree(this.menu.mid)
					.then(res => {

            if(res.data != null){
              this.TreeData = res.data;
            }
            
            this.menuForm = this.menu;

					})
					.catch(() => {
						this.loading = false
					})
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
  //   content: '*';
  //   color: #f56c6c;
  //   margin-right: 4px;
  //   position: absolute;
  //   left: 0;
  //   top: 5px;
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
