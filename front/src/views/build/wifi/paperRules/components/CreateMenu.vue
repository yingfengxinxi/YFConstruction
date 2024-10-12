<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="hidenView"
    @save="debouncedSaveField(true)"
    :showSave="!disabledT">

    <create-sections title="基本信息" style="margin-bottom: 20px;">
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
              <el-form-item label="试卷名称" prop="paperRulesName">
                <el-input v-model="menuForm.paperRulesName" />
              </el-form-item>
          </el-col>
          <el-col :span="12">
              <el-form-item  label="适用工种" class='special' prop="workTypeId" >
                  <el-select
                          v-model="menuForm.workTypeId"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="width: 100%">
                    <el-option
                            v-for="dict in workTypeOptions"
                            :key="dict.dictValue"
                            :label="dict.dictLabel"
                            :value="dict.dictValue" />
                  </el-select>
              </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="试卷总分值" prop="paperScore">
              <el-input type="number" v-model="menuForm.paperScore"/>
            </el-form-item>  
          </el-col>
          <el-col :span="12">
            <el-form-item label="试题总数量" prop="paperCount">
              <el-input type="number" v-model="menuForm.paperCount" />
            </el-form-item>
          </el-col>
          
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="通过分值" prop="pastScope">
              <el-input type="number" v-model="menuForm.pastScope" />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
    </create-sections>

    <create-sections title="组卷规则" style="margin-bottom: 20px;">
       <el-button
            class="xr-btn--orange"
            icon="el-icon-plus"
            type="primary"
            style="float: right"
            v-show="!disabledT"
            @click="createrulesDetailsClick">新建
        </el-button>
      <el-table
        v-loading="loading"
        :data="ruleDetailsList"
        :height="tableHeight"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%">
        
        <el-table-column
          v-for="(item, index) in ruleDetailsFieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          align="center"
          :formatter="ruleDetailsFieldFormatter"
          :sortable="false"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220px">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-edit" size="small" :disabled="disabledT" @click="handleRuleDelClick('edit', scope)">编辑</el-button>
              <el-button type="text" icon="el-icon-delete" size="small" :disabled="disabledT" @click="handleRuleDelClick('delete', scope)">删除</el-button>
              <el-button type="text" icon="el-icon-document" size="small" @click="handleRuleDelClick('show', scope)">查看</el-button>
            </template>
        </el-table-column>

      </el-table>
    </create-sections>

    
    <!-- 试卷规则配置详细表 组卷规则-->
    <create
      v-if="rulesDetailsShow"
      :showType="rulesDetailsType"
      :paperRulesDelData="paperRulesDelData"
      @close="rulesDetailsShow = false"
      @save-success="getPaperRulesDelList"
    />

  </xr-create>
</template>
<script type="text/javascript">
  
  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {guid} from '@/utils'

  import Create from './CreateDel'

  import {debounce} from 'throttle-debounce'

  import {savePaperRules, updatePaperRulesById} from '@/views/build/wifi/api/paperRules'
  import {getPaperRulesDelList , delPaperRulesDelById , selectPaperRulesDelById} from '@/views/build/wifi/api/paperRulesDetails'
  import {getWTList } from '@/api/build/infocollection/infocoll'
  import {selectQuestionIdAndName ,typeIdFormatter }from '@/views/build/wifi/api/questType'

  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件
      XrCreate,
      CreateSections,
      Create

    },
    filters: {//过滤器
    },
    props: {//属性
     menuForm:{},
     actionType:''
    },
    data() {
      return {
        loading: false,
        tableHeight: document.documentElement.clientHeight - 450, // 表的高度

        // 自定义字段验证规则
        memuRules: {
          paperRulesName: [
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          paperScore:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          workTypeId:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          paperCount:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
          pastScope:[
            {required: true, message: '必填项不能为空', trigger: 'blur'}
          ],
        },
        
        // 工种数据
        worktypeList: [],
		workTypeOptions: [],//工种字典

// 组卷规则详细信息--开始
        ruleDetailsFieldList: [//试卷规则配置表头
          {
            prop: "questScore",
            label: "分值",
            width: "120"
          },
          {
            prop: "questCount",
            label: "数量",
            width: "120"
          },
          {
            prop: "questType",
            label: "题型",
            width: "180"
          },
          {
            prop: "typeId",
            label: "试题类别",
          },
        ],
        ruleDetailsList:[],//组卷规则详细信息列表
        rulesDetailsShow:false,//新建组卷规则的弹框
        rulesDetailsType:'',//试卷配置规则详情弹框，新增、修改（updatePaperRulesDelShow）、查看（detailsPaperRulesDelShow）的类型

        questTypeList:[],//题型(1:单选2:多选3：填空4：问答)
        typeList:[],//试题类型
        paperRulesDelData:{},//试卷配置规则详情
// 组卷规则详细信息--结束

      }
    },
    created(){
      this.debouncedSaveField = debounce(300, this.savePaperRules)

      //工种数据
      //this.getWorktypeList()
	  // 工种字典
      this.getDicts('b_labour_work_type').then(response => {
        this.workTypeOptions = response.data
      })

      //  题型 字典项
      this.getDicts('b_wifi_quest_type').then(response => {
        this.questTypeList = response.data
      })

      //试题类型
      this.typeListFunction()


      //当前试卷规则配置的id，用于规则详情新增的时候使用
      if(this.actionType == 'create'){
        this.paperRulesDelData.paperRulesId = guid()
        this.menuForm.id = this.paperRulesDelData.paperRulesId
      }else{
        this.paperRulesDelData.paperRulesId = this.menuForm.id
        this.getPaperRulesDelList()
      }
        

    },
    computed: {//监听属性
      title() {
        return this.actionType === 'update' ? '编辑试卷规则配置' : (this.actionType === 'show' ? '查看试卷规则配置' : '新建试卷规则配置')   
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

      //组卷规则详情列表
      getPaperRulesDelList(){
        getPaperRulesDelList({paperRulesId:this.menuForm.id}).then(res=>{
          this.ruleDetailsList = res.data
        })
      },

      //新建组卷规则详情
      createrulesDetailsClick(){
        this.rulesDetailsType = 'createPaperRulesDelShow'
        this.rulesDetailsShow = true
      },

      //查询试题类型id和name
      typeListFunction(){
          selectQuestionIdAndName().then(res=>{
              this.typeList = res.data
          })
      },


      //新建组卷规则详情列表  列格式化  如一些字典等
      ruleDetailsFieldFormatter(row, column, cellValue) {
        //试题类别
        if(column.property == 'typeId'){
          return typeIdFormatter(row.typeId, this.typeList)
        }
        //题型
        if(column.property == 'questType'){
          return this.selectDictLabel(this.questTypeList, row.questType)
        }

        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      //试卷配置规则详情列表  修改、删除、查看
      async handleRuleDelClick(event, scope){
        //此处需同步调用
        await selectPaperRulesDelById(scope.row.id)
            .then(res => {
                this.paperRulesDelData = res.data;
 
            })

        if (event == 'edit') {//修改
            this.rulesDetailsType = 'updatePaperRulesDelShow'
            this.rulesDetailsShow = true;
        } else if (event == 'delete') { //删除
            this.$confirm('您确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                })
                .then(() => {
                    delPaperRulesDelById(this.paperRulesDelData.id)
                        .then(res => {
                            this.loading = false
                            if (res.code == 0) {
                                this.msgSuccess('删除成功')
                                this.getPaperRulesDelList(this.questId);

                            } else {
                                this.$message({
                                    type: 'warn',
                                    message: res.msg
                                })
                            }
                        })
                        .catch(() => {
                            this.loading = false
                        })
                })
                .catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    })
                })
        } else if(event == 'show'){//查看
            this.rulesDetailsType = 'detailsPaperRulesDelShow'
            this.rulesDetailsShow = true;
        } 
      },

    
      savePaperRules() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {
            this.loading = true;
            
            //查询组卷规则详情的试题总数量，与基本信息的总数量对比
            let questCount = this.getAllNum()
            //查询组卷规则详情的试题总分值，与基本信息的总数量分值
            let questScore = this.getAllScope()

            if(questCount != this.menuForm.paperCount){
              this.$message({
                type: 'error',
                message: '试题总数量（'+this.menuForm.paperCount+'）与组卷规则的总数量（'+questCount+'）不匹配，请检查'
              })
               this.loading = false;
              return;
            }
            if(questScore != this.menuForm.paperScore){
              this.$message({
                type: 'error',
                message: '试卷总分值（'+this.menuForm.paperScore+'）与组卷规则的总分值（'+questScore+'）不匹配，请检查'
              })
               this.loading = false;
              return;
            }

            if(this.actionType=='create'){
              savePaperRules(this.menuForm)
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
              updatePaperRulesById(this.menuForm)
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

      //查询组卷规则详情的试题总数量
      getAllNum(){
        let questCount = 0
        for(let i = 0; i < this.ruleDetailsList.length; i++){
          let data = this.ruleDetailsList[i]
          if(data.questCount > 0){//数量
            questCount += data.questCount
          }
        }
        return questCount
      },
      getAllScope(){
        let questScore= 0
        for(let i = 0; i < this.ruleDetailsList.length; i++){
          let data = this.ruleDetailsList[i]
          if(data.questScore > 0 && data.questCount > 0){//分值

            questScore += data.questScore * data.questCount
          }
        }
        return questScore
      },


      hidenView() {
        this.$emit('hiden-view')
      },

      /** 查询所工种数据列表 */
      getWorktypeList() {
        getWTList().then(response => {
          this.worktypeList = response.data
          this.loading = false
        }).catch(() => {
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
