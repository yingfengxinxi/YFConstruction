<template>
  <div>
    <create-sections v-for="(item, formIndex) in pageAllItems"
                     :key="formIndex">
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">隐患信息<span v-if="pageAllItems.length > 1">（{{ formIndex+1 }}）</span></div>
        <i
		  v-if="action.type != 'receipt'"
          class="el-icon-delete expense-item-head-delete"
          @click="deleteItems(formIndex)"/>
      </flexbox>
	  <el-form :ref="'itemForm'+formIndex" :model="fieldFormAll[formIndex]" :rules="rules" label-position="top" label-width="100px"
	  	class="form">
		<el-row>
		  <el-col :span="8">
				<el-form-item label="隐患位置" prop="place">
					<el-input :readonly="action.isReadonly" v-model="fieldFormAll[formIndex].place" placeholder="请输入隐患位置"></el-input>
				</el-form-item>
		  </el-col>
		  <el-col :span="16">
				<el-form-item label="隐患描述" prop="qualTrobuleDescription">
					<el-input :readonly="action.isReadonly" type="textarea" :rows="1"
							  v-model="fieldFormAll[formIndex].qualTrobuleDescription" placeholder="请输入隐患描述"></el-input>
				</el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="8">
		    <el-form-item label="隐患等级" prop="level">
		  	<el-select
		  	  v-model="fieldFormAll[formIndex].level"
		  	  placeholder="请选择"
		  	  clearable
			  :disabled="action.isReadonly"
		  	>
		  	  <el-option
		  		v-for="dict in hazardLevleDict"
		  		:key="dict.dictValue"
		  		:label="dict.dictLabel"
		  		:value="dict.dictValue"
		  	  />
		  	</el-select>
		    </el-form-item>
		  </el-col>
		  <el-col :span="8">
		    <el-form-item label="紧急程度" prop="priority">
		  	<el-select
		  	  v-model="fieldFormAll[formIndex].priority"
		  	  placeholder="请选择"
		  	  clearable
		  			  :disabled="action.isReadonly"
		  	>
		  	  <el-option
		  		v-for="dict in priorityDict"
		  		:key="dict.dictValue"
		  		:label="dict.dictLabel"
		  		:value="dict.dictValue"
		  	  />
		  	</el-select>
		    </el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item label="问题类型" prop="questionId">
			  <el-select :ref="'selectTree'+formIndex" v-model="fieldFormAll[formIndex].questionId" filterable
						 :disabled="action.isReadonly" placeholder="请选择">
				  <el-option :value="fieldFormAll[formIndex].questionId" :label="fieldFormAll[formIndex].questionName"
							 style="width: 260px;height:120px;overflow:auto;background-color: #fff">
					  <el-tree
							  node-key="questionId"
							  v-bind:formIndex="formIndex"
							  :data="questionOptions"
							  :props="defaultProps"
							  @node-click="handleNodeClick"
					  ></el-tree>
				  </el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		</el-row>
		<el-row>
			<el-col :span="8">
				<el-form-item label="实际整改人" prop="actualCorrectorName">
				  <el-input v-model="fieldFormAll[formIndex].actualCorrectorName" placeholder="请输入实际整改人"></el-input>
				</el-form-item>
			</el-col>
			<el-col :span="8">
				<el-form-item label="整改时间" prop="rectifyDate">
				  <el-date-picker
					v-model="fieldFormAll[formIndex].rectifyDate"
					type="date"
					value-format="yyyy-MM-dd"
					placeholder="请选择检查时间"
				  >
				  </el-date-picker>
				</el-form-item>
			</el-col>
			<el-col :span="8">
				<el-form-item label="整改效果说明" prop="receiptInfo">
					<el-input v-model="fieldFormAll[formIndex].receiptInfo" placeholder="请输入整改效果说明"></el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row v-if="action.type === 'receipt'">
			<el-col :span="8">
			  <el-form-item label="复检时间" prop="recheckDate">
			  <el-date-picker
			    v-model="fieldFormAll[formIndex].recheckDate"
			    type="date"
			    value-format="yyyy-MM-dd"
			    placeholder="请选择检查时间"
			  >
			  </el-date-picker>
			  </el-form-item>
			</el-col>
			<el-col :span="8">
			  <el-form-item label="复检人" prop="recheckPeopleId">
				  <el-select v-model="fieldFormAll[formIndex].recheckPeopleId" placeholder="选择复检人">
				    <el-option
				  	v-for="item in userListData"
				  	:key="item.userId"
				  	:label="item.realname"
				  	:value="item.userId">
				    </el-option>
				  </el-select>
			  </el-form-item>
			</el-col>
			<el-col :span="8">
			  <el-form-item label="复检结论" prop="recheckConclusion">
				  <el-input v-model="fieldFormAll[formIndex].recheckConclusion" placeholder="请输入复检结论"></el-input>
			  </el-form-item>
			</el-col>
		</el-row>
	  </el-form>
      <!--隐患图片上传-->
      <wk-upload-img
        :ref="'wkUploadImg'+formIndex"
        name="file"
        multiple
        accept="image/*"
        list-type="picture-card"
        :autoUpload="false"
        :title="'隐患图片上传'"
        :batchId="batchIdAll[formIndex]"
      ></wk-upload-img>
      <!--复检图片上传-->
      <wk-upload-img
	    v-if="action.type === 'receipt'"
        :ref="'wkUploadImg2'+formIndex"
        name="file"
        multiple
        accept="image/*"
        list-type="picture-card"
        :autoUpload="false"
        :title="'复检图片上传'"
        :batchId="receiptBatchIdAll[formIndex]"
      ></wk-upload-img>
    </create-sections>
    <div class="handle-bar">
      <el-button
		v-if="action.type != 'receipt'"
        class="handle-bar-button"
        type="text"
        icon="el-icon-plus"
        @click="addItem()">添加
      </el-button>
    </div>
  </div>
</template>

<script>
  import {userListAPI} from '@/api/common' // 直属上级接口  用户接口
  import CreateSections from '@/components/CreateSections'
  import WkForm from '@/components/NewCom/WkForm'
  import {
    CrmRelativeCell
  } from '@/components/CreateCom'

  import CustomFieldsMixin from '@/mixins/CustomFields'

  import {guid} from '@/utils'

  import WkUploadImg from '@/components/CreateCom/WkUploadImg'
  import {
	  queryQuestionsTreeList
  } from '@/views/build/qual/api/question'

  export default {
    // 新建编辑
    name: 'ExpenseCreate',

    components: {
      CrmRelativeCell,
      CreateSections,
      WkForm,
      WkUploadImg
    },

    mixins: [CustomFieldsMixin],

    props: {
      action: {
        type: Object,	
		// 是否只读
		isReadonly: false,
        default: () => {
          return {
            type: 'save',
            id: '',
            data: {}
          }
        }
      },
    },

    data() {
      return {
        //所有费用item
        pageAllItems: [],
        fieldListAll: [],
        fieldFormAll: [],
        //基础字段
        baseFields: [],
        //附件
        batchIdAll: [],
		// 复检图片
		receiptBatchIdAll: [],
		// 被删除的隐患信息
        delItemIds:[],	
		// 隐患等级字典项
		hazardLevleDict: [],
		// 紧急状况字典项
		priorityDict:[],
		// 用户列表
		userListData: [],
		// 问题类型相关
		questionOptions: [],
		  defaultProps: {
			  children: 'children',
			  label: 'questionName'
		  },
		rules: {
			place: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			qualTrobuleDescription: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			level: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			questionId: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			recheckDate: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			recheckPeopleId: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			recheckConclusion: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
		},
		      
      }
    },

    computed: {},

    watch: {},

    created() {
      //this.getField()
	  this.initDicts()
    },

    mounted() {
		this.getQuestionList()
		this.getUserList()
    },
    beforeDestroy() {
    },

    methods: {
		handleNodeClick:function(data,node,obj){
			if(node.children){

			}else{
				var formIndex = obj.$data.tree.$attrs.formIndex
				this.fieldFormAll[formIndex].questionId = node.key
				this.fieldFormAll[formIndex].questionName = node.label
				var ref = 'selectTree' + formIndex;
				this.$refs[ref][0].blur();
			}
		},
      componentValue(params) {
        this.$emit("change", this.fieldFormAll);
      },
      //父组件调用  校验数据项
      startCheck() {
        var flag = true;
        for (var i = 0; i < this.pageAllItems.length; i++) {
          this.$refs['crmForm' + i][0].instance.validate(valid => {
            if (!valid) {
              flag = false;
            }
          });
        }
        return flag;
      },
      submitImgs() {
        var flag = true;
        for(var i = 0;i < this.pageAllItems.length; i++){
          var ref = 'wkUploadImg' + i;
          flag = this.$refs[ref][0].submitAsync();
        }
        return flag;
      },

	  submitImgs2() {
	    var flag = true;
	    for(var i = 0;i < this.pageAllItems.length; i++){
		  var ref = 'wkUploadImg2' + i;
		  flag = this.$refs[ref][0].submitAsync();
	    }
	    return flag;
	  },
      //新增费用项
      addItem() {
        this.handle(this.baseFields);
      },
      deleteItems(index) {
        //记录被删除的、已存在的隐患信息
        var item = this.fieldFormAll[index];
        if(item.expenseId){
          this.delItemIds.push(item.expenseId);
        }
        this.fieldListAll.splice(index, 1)
        this.fieldFormAll.splice(index, 1)
        this.batchIdAll.splice(index, 1)
		this.receiptBatchIdAll.splice(index, 1)
        this.pageAllItems.splice(index, 1)
      },
      
      //格式化 字段数据
      handle(list) {
        const fieldForm = {questionId:"",questionName:""}//初始化时下拉框绑定属性必须声明，否则下拉树无法进行控件绑定赋值
        var batchId = '';
		var receiptBatchId = '';
		
        this.fieldFormAll.push(fieldForm)
        if (!batchId) {
          batchId = guid();
        }
        this.batchIdAll.push(batchId);
		if (!receiptBatchId) {
		  receiptBatchId = guid();
		}
		this.receiptBatchIdAll.push(receiptBatchId)
        //加入一次空遍历
        this.pageAllItems.push({})
      },
      /**
       * 获取字段是否可编辑
       */
      /* getItemIsCanEdit(item, type) {
        // authLevel 1 不能查看不能编辑 2可查看  3 可编辑可查看
        return (type === 'update' && item.authLevel == 3) || type !== 'update'
      }, */
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },  
	  // 初始化字典数据
	  initDicts() {
	  	this.getDicts('b_hazard_level').then(response => {
	  	  this.hazardLevleDict = response.data
	  	})
		this.getDicts('b_qual_priority').then(response => {
		  this.priorityDict = response.data
		})
	  },
	  // 查询质量问题库所有数据
	  getQuestionList() {
		  queryQuestionsTreeList().then(res => {
			  // 问题类型列表
			  this.questionOptions = res.data
		  })
	  },
	  // 查询当前项目的用户
	  getUserList() {
	    var params = {
	  	  pageType: 0
	    }
	    userListAPI(params).then(res => {
	  	  this.userListData = res.data.list
	    })
	    .catch(() => {
	  	  
	    })
	  }
    }
  }
</script>

<style lang="scss" scoped>

  .expense-item {
    border: 1px solid #e6e6e6;
    border-radius: 2px;
    padding-bottom: 15px;
    margin-bottom: 10px;
    &-head {
      padding: 10px 20px;
      background-color: #f5f5f5;
      &-title {
        height: auto;
        font-size: 12px;
        color: #333;
        flex: 1;
        line-height: normal;
      }

      &-delete {
        padding: 0 10px;
        color: #2362FB;
        font-size: 14px;
      }
    }
  }

  .handle-bar {
    padding: 0 20px;
    height: 29px;
    &-button {
      float: right;
      border: none;
      color: #2362FB;
    }
    &-total {
      margin-top: 10px;
      text-align: left;
      font-size: 13px;
      color: #333;
      span {
        color: #666;
      }
    }
  }

  .wk-form {
    /deep/
    .el-form-item.is-product {
      flex: 0 0 100%;

    }

  }

  .wk-form
  /deep/
  .el-form-item {
    margin-bottom: 20px !important;
  }
  .el-select{
  	 width: 100%; 
  }
</style>
