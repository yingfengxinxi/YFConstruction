<template>
  <div>
    <create-sections v-for="(item, formIndex) in pageAllItems"
                     :key="formIndex">
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">巡检明细<span v-if="pageAllItems.length > 1">（{{ formIndex+1 }}）</span></div>
        <i
		  v-if="action.type != 'receipt'"
          class="el-icon-delete expense-item-head-delete"
          @click="deleteItems(formIndex)"/>
      </flexbox>
	  <el-form :ref="'itemForm'+formIndex" :model="fieldFormAll[formIndex]" :rules="rules" label-position="top" label-width="100px"
	  	class="form">
		<el-row>
		  <el-col :span="8">
				<el-form-item label="巡检部位" prop="place">
					<el-input :readonly="action.isReadonly" v-model="fieldFormAll[formIndex].place" placeholder="请输入巡检部位"></el-input>
				</el-form-item>
		  </el-col>
		  <el-col :span="8">
				<el-form-item label="问题描述" prop="problemDescription">
					<el-input :readonly="action.isReadonly" v-model="fieldFormAll[formIndex].problemDescription" placeholder="请输入问题描述"></el-input>
				</el-form-item>
		  </el-col>
		  <el-col :span="8">
				<el-form-item label="巡检结论" prop="inspectionConclusion">
					<el-input :readonly="action.isReadonly" v-model="fieldFormAll[formIndex].inspectionConclusion" placeholder="请输入巡检结论"></el-input>
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
		  <!-- <el-col :span="8">
			<el-form-item label="危险源" prop="dangerSourceId">
			  <el-select :ref="'selectTree'+formIndex" v-model="fieldFormAll[formIndex].dangerSourceId" filterable
			  						  placeholder="请选择">
				  <el-option :value="fieldFormAll[formIndex].dangerSourceId" :label="fieldFormAll[formIndex].dangerName"
							 style="width: 260px;height:120px;overflow:auto;background-color: #fff">
					  <el-tree
							  node-key="dangerSourceId"
							  v-bind:formIndex="formIndex"
							  :data="dangerOptions"
							  :props="defaultProps"
							  @node-click="handleNodeClick"
					  ></el-tree>
				  </el-option>
			  </el-select>
			</el-form-item>
		  </el-col> -->
		  <el-col :span="8">
			<el-form-item label="危险源" prop="dangerSourceId">
				<el-select
				  v-model="fieldFormAll[formIndex].dangerSourceId"
				  placeholder="请选择"
				  clearable
				  :disabled="action.isReadonly"
				>
				  <el-option
					v-for="dict in SafetyProductionList"
					:key="dict.id"
					:label="dict.name"
					:value="dict.id"
				  />
				</el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
		  			<el-form-item label="实际整改人" prop="rectificationPeople">
		  			  <el-input v-model="fieldFormAll[formIndex].rectificationPeople" placeholder="请输入实际整改人"></el-input>
		  			</el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="8">
		    <el-form-item label="整改时间" prop="rectifyDate">
			  <el-date-picker
			    v-model="fieldFormAll[formIndex].rectifyDate"
			    type="date"
			    value-format="yyyy-MM-dd"
			    placeholder="请选择检查时间"
          @change="zgsjbd($event, formIndex)"
			  >
			  </el-date-picker>
		    </el-form-item>
		  </el-col>
		  <el-col :span="8">
			  <el-form-item label="整改效果说明" prop="rectifyEffect">
				  <el-input v-model="fieldFormAll[formIndex].rectifyEffect" placeholder="请输入整改效果说明"></el-input>
			  </el-form-item>
		  </el-col>
		</el-row>
		<el-row v-if="action.type === 'recheck'">
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
				  	v-for="item in dictData.userListData"
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
		<!--图片上传-->
		<wk-upload-img
		  :ref="'wkUploadImg'+formIndex"
		  name="file"
		  multiple
		  accept="image/*"
		  list-type="picture-card"
		  :autoUpload="false"
		  :title="'图片上传'"
		  :batchId="batchIdAll[formIndex]"
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
  import CreateSections from '@/components/CreateSections'
  import WkForm from '@/components/NewCom/WkForm'
  import {guid} from '@/utils'
  import {
    CrmRelativeCell
  } from '@/components/CreateCom'

  import CustomFieldsMixin from '@/mixins/CustomFields'

  import WkUploadImg from '@/components/CreateCom/WkUploadImg'
  import {
  	  queryDangerTreeList
  } from '@/views/build/safety/api/dangerSource'

  import {getTeamList, selectPersonByTeam, selectSafetyProduction} from "../api/inspectionInfo"

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
	  dictData:{}
    },

    data() {
      return {
        //所有费用item
        pageAllItems: [],
        fieldListAll: [],
        fieldFormAll: [],
        fieldRulesAll: [],
        //基础字段
        baseFields: [],
        //附件
        batchIdAll: [],
        checkDate: '',
		// 被删除的明细
        delItemIds:[],
		// 隐患等级字典项
		hazardLevleDict: [],
		// 问题类型相关
		dangerOptions: [],
		// 安全检查标准
		SafetyProductionList:[],
		  defaultProps: {
			  children: 'children',
			  label: 'dangerName'
		  },
		rules: {
			place: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			problemDescription: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			inspectionConclusion: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			level: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			dangerSourceId: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			rectificationPeople: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			rectifyDate: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			],
			rectifyEffect: [
			  {required: true, message: '必填项不得为空', trigger: 'blur'}
			]
		},

      }
    },

    computed: {},

    watch: {},

    created() {
		this.initDicts()
    },

    mounted() {
		//this.getQuestionList()
		this.getSafetyProduction()
    },
    beforeDestroy() {
    },

    methods: {
		handleNodeClick:function(data,node,obj){
			if(node.children){

			}else{
				var formIndex = obj.$data.tree.$attrs.formIndex
				this.fieldFormAll[formIndex].dangerSourceId = node.key
				this.fieldFormAll[formIndex].dangerName = node.label
				var ref = 'selectTree' + formIndex;

				this.$refs[ref][0].blur();

			}
		},
      componentValue(params) {
        this.$emit("change", this.fieldFormAll);
      },
	  // 父组件调用上传图片
	  submitImgs() {
	    var flag = true;
	    for(var i = 0;i < this.pageAllItems.length; i++){
	      var ref = 'wkUploadImg' + i;
	      flag = this.$refs[ref][0].submitAsync();
	    }
	    return flag;
	  },
      //新增费用项
      addItem() {
        this.handle();
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
        this.fieldRulesAll.splice(index, 1)
        this.pageAllItems.splice(index, 1)
      },

      //格式化 字段数据
      handle(list) {
        const fieldForm = {dangerSourceId:"",dangerName:""}//初始化时下拉框绑定属性必须声明，否则下拉树无法进行控件绑定赋值
		var batchId = '';
		if (!batchId) {
		  batchId = guid();
		}
		this.batchIdAll.push(batchId);

		this.fieldFormAll.push(fieldForm)

        this.pageAllItems.push({})
      },
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
	  },
	  // 查询质量问题库所有数据
	  getQuestionList() {
		  queryDangerTreeList().then(res => {
			  // 问题类型列表
			  this.dangerOptions = res.data
		  })
	  },
	  // 查询安全检查标准
	  getSafetyProduction() {
		  selectSafetyProduction().then(res => {
			  // 问题类型列表
			  this.SafetyProductionList = res.data
		  })
	  },
      zgsjbd(e,formIndex){
        var kssj = this.checkDate
        var jssj = e
        if(kssj==""||kssj==null||kssj==undefined||jssj==""||jssj==null||jssj==undefined){
          return
        }else{
          var formatDate1 = new Date(kssj)
          var formatDate2 = new Date(jssj)
          if(formatDate1 > formatDate2){
            this.fieldFormAll[formIndex].rectifyDate = ""
            this.$message({
              message: '整改时间不能小于检查时间',
              type: 'warning'
            })
          }
        }
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
  /deep/.el-input{
	  width: 220px;
  }
  /deep/.el-select{
	  width: 220px;
  }
</style>
