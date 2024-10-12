<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
	:showSave="!(action.type == 'details')"
    @close="close"
    @save="debouncedSaveField(true)">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
	  <create-sections title="基本信息">
        <el-row>
          <el-col :span="12">
            <el-form-item label="隐患编号" prop="hiddenNum">
              <el-input v-model="form.hiddenNum" :disabled="true" placeholder="请输入隐患编号"></el-input>
            </el-form-item>
          </el-col>
		  <el-col v-if="dataType == '2'" :span="12">
		    <el-form-item label="检查类型" prop="inspectionType">
		      <el-select v-model="form.inspectionType" :disabled="!(action.type == 'save') && !(action.type == 'update')" 
			  placeholder="请选择检查类型">
		        <el-option
		          v-for="item in dictData.inspectionTypeDict"
		          :key="item.dictValue"
		          :label="item.dictLabel"
		          :value="item.dictValue">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
		  <el-col v-if="dataType == '1'" :span="12">
		    <el-form-item label="问题类型" prop="questionId">
			  <el-select :ref="'selectTree'" v-model="form.questionId" filterable
			  		 :disabled="action.isReadonly" placeholder="请选择问题类型">
			    <el-option :value="form.questionId" :label="form.questionName"
			  			 style="width: 260px;height:120px;overflow:auto;background-color: #fff">
			  	  <el-tree
					  node-key="questionId"
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
          <el-col :span="12">
            <el-form-item label="巡检部位" prop="inspectionPosition">
              <el-input v-model="form.inspectionPosition" :disabled="!(action.type == 'save') && !(action.type == 'update')"  
			  placeholder="请输入巡检部位"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题描述" prop="problemDescription">
              <el-input v-model="form.problemDescription" :disabled="!(action.type == 'save') && !(action.type == 'update')"  
			  placeholder="请输入问题描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
		  <el-col :span="12">
		    <el-form-item label="问题级别" prop="problemLevel">
		      <el-select v-model="form.problemLevel" :disabled="!(action.type == 'save') && !(action.type == 'update')" 
			  placeholder="请选择问题级别">
		        <el-option
		          v-for="item in dictData.problemLevelDict"
		          :key="item.dictValue"
		          :label="item.dictLabel"
		          :value="item.dictValue">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="紧急程度" prop="urgency">
		      <el-select v-model="form.urgency" :disabled="!(action.type == 'save') && !(action.type == 'update')" 
			  placeholder="请选择紧急程度">
		        <el-option
		          v-for="item in dictData.urgencyDict"
		          :key="item.dictValue"
		          :label="item.dictLabel"
		          :value="item.dictValue">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
        </el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="检查负责人" prop="inspectorsName">
		      <el-input v-model="form.inspectorsName" :disabled="true" placeholder="请输入检查负责人"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="联系电话" prop="inspectorsPhone">
		      <el-input v-model="form.inspectorsPhone" :disabled="true" placeholder="请输入联系电话"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="检查日期" prop="inspectionTime">
		      <el-date-picker
						  :disabled="!(action.type == 'save') && !(action.type == 'update')" 
		        v-model="form.inspectionTime"
		        type="datetime"
		        value-format="yyyy-MM-dd HH:mm:ss"
		        placeholder="请选择检查日期"
		      >
		      </el-date-picker>
		    </el-form-item>
		  </el-col>
		</el-row>
		<!--图片上传-->
		<wk-upload-img
		  name="file"
			ref="wkUploadImg"
		  multiple
		  accept="image/*"
		  list-type="picture-card"
		  :autoUpload="false"
		  :title="'图片上传'"
		  :batchId="batchId"
		></wk-upload-img>
	  </create-sections>
	  
	  <create-sections title="整改信息">
		  <el-row>
		  <el-col :span="12">
		    <el-form-item label="整改责任单位" prop="dutyUnitId">
		      <el-select v-model="form.dutyUnitId" :disabled="!(action.type == 'save') && !(action.type == 'update')"  
			  @change="dutyUnitChange" placeholder="请选择整改责任单位">
		        <el-option
		          v-for="item in dictData.subcontractorList"
		          :key="item.id"
		          :label="item.realName"
		          :value="item.id">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
		    <el-col :span="12">
		      <el-form-item label="整改期限" prop="rectifyTime">
		        <el-date-picker
				  :disabled="!(action.type == 'save') && !(action.type == 'update')" 
		  		  v-model="form.rectifyTime"
		  		  type="datetime"
		  		  value-format="yyyy-MM-dd HH:mm:ss"
		  		  placeholder="请选择整改期限"
		        >
		        </el-date-picker>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <el-row>
		    <el-col :span="12">
		      <el-form-item label="整改负责人" prop="rectifyPersonId">
		        <el-select v-model="form.rectifyPersonId" :disabled="!(action.type == 'save') && !(action.type == 'update')"  
				@change="rectifyPersonChange($event, jobForemanList)" placeholder="请选择整改负责人">
		          <el-option
		            v-for="item in jobForemanList"
		            :key="item.id"
		            :label="item.name"
		            :value="item.id">
		          </el-option>
		        </el-select>
		      </el-form-item>
		    </el-col>
		    <el-col :span="12">
		      <el-form-item label="联系电话" prop="rectifyPersonPhone">
		        <el-input v-model="form.rectifyPersonPhone" :disabled="true" placeholder="请输入联系电话"></el-input>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <el-row v-if="!(action.type == 'save') && !(action.type == 'update')">
		    <el-col :span="12">
		      <el-form-item label="实际整改人" prop="actualRectifyPersonName">
		        <el-input v-model="form.actualRectifyPersonName" :disabled="true" placeholder="请输入实际整改人"></el-input>
		      </el-form-item>
		    </el-col>
		    <el-col :span="12">
		      <el-form-item label="联系电话" prop="actualRectifyPersonPhone">
		        <el-input v-model="form.actualRectifyPersonPhone" :disabled="true" placeholder="请输入联系电话"></el-input>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <el-row v-if="!(action.type == 'save') && !(action.type == 'update')">
		    <el-col :span="12">
		      <el-form-item label="整改结果" prop="rectificationResult">
		        <el-input v-model="form.rectificationResult" :disabled="!(action.type == 'rectify')"  placeholder="请输入整改结果"></el-input>
		      </el-form-item>
		    </el-col>
		    <el-col :span="12">
		      <el-form-item label="整改完成时间" prop="rectifyCompleteTime">
		        <el-date-picker
				  :disabled="!(action.type == 'rectify')" 
		  		  v-model="form.rectifyCompleteTime"
		  		  type="datetime"
		  		  value-format="yyyy-MM-dd HH:mm:ss"
		  		  placeholder="请选择整改完成时间"
		        >
		        </el-date-picker>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <!--图片上传-->
		  <wk-upload-img
		    v-if="!(action.type == 'save') && !(action.type == 'update')"
		    name="file"
		  	ref="wkUploadImg2"
		    multiple
		    accept="image/*"
		    list-type="picture-card"
		    :autoUpload="false"
		    :title="'图片上传'"
		    :batchId="rectifyBatchId"
		  ></wk-upload-img>
		  
	  </create-sections>
	  
	  <create-sections v-if="action.type == 'recheck' || action.type == 'details'" title="复检信息">
		  <el-row>
		    <el-col :span="12">
		      <el-form-item label="复查人" prop="reviewPersonName">
		        <el-input v-model="form.reviewPersonName" :disabled="true" placeholder="请输入复查人"></el-input>
		      </el-form-item>
		    </el-col>
		    <el-col :span="12">
		      <el-form-item label="联系电话" prop="reviewPersonPhone">
		        <el-input v-model="form.reviewPersonPhone" :disabled="true" placeholder="请输入联系电话"></el-input>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <el-row>
		    <el-col :span="12">
		      <el-form-item label="复查结论" prop="reviewResult">
		        <el-input v-model="form.reviewResult" :disabled="action.type == 'details'"  placeholder="请输入复查结论"></el-input>
		      </el-form-item>
		    </el-col>
		    <el-col :span="12">
		      <el-form-item label="复查完成时间" prop="reviewTime">
		        <el-date-picker
				  :disabled="action.type == 'details'" 
		  		  v-model="form.reviewTime"
		  		  type="datetime"
		  		  value-format="yyyy-MM-dd HH:mm:ss"
		  		  placeholder="请选择复查完成时间"
		        >
		        </el-date-picker>
		      </el-form-item>
		    </el-col>
		  </el-row>
		  <el-row v-if="dataType == '2'">
		    <el-col :span="12">
		      <el-form-item label="补充说明" prop="rectificationReply">
		        <el-input v-model="form.rectificationReply" :disabled="action.type == 'details'"  placeholder="请输入补充说明"></el-input>
		      </el-form-item>
		    </el-col>
		  </el-row>
	  </create-sections>

      </el-form>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'
  import {mapGetters} from 'vuex'

  import WkUploadImg from '@/components/CreateCom/WkUploadImg'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {save, updateInfo, getById, getNumber, getTeamLeaderList} from "../api/qualRiskDetection"
  import {queryQuestionsTreeList} from '@/views/build/qual/api/question'

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadImg,
	  SelectTree
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            id: '',
            data: {}
          }
        }
      },
	  // 表单类型
      dataType: {
        type: String,
        default: () => {
          return ''
        }
      },
	  // 字典数据
	  dictData: {},
    },
    data() {
      return {
        loading: false,
        form: {
			dataType: '',
			questionId: '',
			questionName: '',
			hiddenNum: '',
			inspectionTime: '',
			rectifyTime: '',
        },
		
		// 班组长列表
		jobForemanList: [],
		batchId: '',
		rectifyBatchId:'',
		
		// 问题类型相关
		questionOptions: [],
		defaultProps: {
		  children: 'children',
		  label: 'questionName'
		},

        rules: {
          hiddenNum: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectionType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          questionId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectionPosition: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          problemDescription: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          problemLevel: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          urgency: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectorsName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectorsPhone: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectionTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          dutyUnitId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectifyTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectifyPersonId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectifyPersonPhone: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          actualRectifyPersonName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          actualRectifyPersonPhone: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectificationResult: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectifyCompleteTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          reviewPersonName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          reviewPersonPhone: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          reviewResult: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          reviewTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          rectificationReply: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      ...mapGetters([
        'userInfo',
      ]),
      title() {
				let title = ''
				if (this.dataType == '1') {
					title = '日常质量巡检'
				} else {
					title = '企业质量巡检'
				}
				switch(this.action.type) {
				case 'save':
					return '新建' + title
				case 'update':
					return '编辑' + title
				case 'rectify':
					return '整改' + title
				case 'recheck':
					return '复检' + title
				case 'details':
					return '查看' + title
				}
		  return ''
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
	  // 问题类型
	  this.getQuestionList()
      //考虑附件ID
	  
      if (this.action.type == "save") {
		  
		this.batchId = guid()
		this.form.dataType = this.dataType
		this.form.batchId = this.batchId
		// 创建时生成编号
		this.selectNumber()
		// 隐患信息初始化
		this.form.category = 'basic'
		this.form.level = 'general'
		this.form.routineCheckType = 'corp'
		this.form.chekcResult = '需整改'
		// 检查人信息
		this.form.inspectorsId = this.userInfo.userId
		this.form.inspectorsName = this.userInfo.realname
		this.form.inspectorsPhone = this.userInfo.mobile
		
		this.form.inspectionTime = this.formatterDate(new Date().getTime())
		this.form.rectifyTime = this.formatterDate(new Date(this.form.inspectionTime).getTime() + 24 * 60 * 60 * 1000)
      } else {
        this.getDetail();
	  }
    },
	watch: {
	},
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
			this.dataType = this.form.dataType
			this.batchId = res.data.batchId
			
			if (this.action.type == "rectify") {
				// 整改生成batchId
				this.rectifyBatchId = guid()
				this.form.rectifyBatchId = this.rectifyBatchId
				// 实际整改人
				this.form.actualRectifyPersonId = this.userInfo.userId
				this.form.actualRectifyPersonName = this.userInfo.realname
				this.form.actualRectifyPersonPhone = this.userInfo.mobile
			} else if (this.action.type == "recheck") {
				this.rectifyBatchId = this.form.rectifyBatchId
				// 检查人
				this.form.reviewPersonId = this.userInfo.userId
				this.form.reviewPersonName = this.userInfo.realname
				this.form.reviewPersonPhone = this.userInfo.mobile
			} else {
				this.rectifyBatchId = this.form.rectifyBatchId
			}
			
			// 查询对应分分包公司的班组长信息
			this.getTeamLeaderList()
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
			if (this.action.type == 'save') {
				this.saveInfo();
			} else {
				this.updateInfo()
			}
          }
        })
      },
	  saveInfo() {
		  var fileFlag = this.$refs['wkUploadImg'].submitAsync();
		  if (!fileFlag) {
		      this.$message({
		          type: 'error',
		          message: '隐患图片上传异常'
		      })
		      return;
		  }
		  
		  var params = this.form;
		  this.loading = true;
		  save(params).then(response => {
		    if (response.code === 0) {
		      this.$message.success(
		        response.data
		      )
		      this.close()
		      // 保存成功
		      this.$emit('save-success', {
		        type: 'receivables'
		      })
		    } else {
		      this.$message.error(
		        response.msg
		      )
		    }
		    this.loading = false;
		  }).catch(err => {
		    this.loading = false;
		  })
	  },
	  updateInfo(){
		  if (this.action.type != 'update') {
			  var fileFlag = this.$refs['wkUploadImg2'].submitAsync();
			  if (!fileFlag) {
				  this.$message({
					  type: 'error',
					  message: '整改图片上传异常'
				  })
				  return;
			  }
		  }
		  
		  var params = this.form;
		  this.loading = true;
		  updateInfo(params).then(response => {
		    if (response.code === 0) {
			  let message = ''
			  switch(this.action.type) {
				case 'update':
					message = '修改成功'
					break;
				case 'rectify':
					message = '整改成功'
					break;
				case 'recheck':
					message = '复检成功'
					break;
			  }
		      this.$message.success(
		        message
		      )
		      this.close()
		      // 保存成功
		      this.$emit('save-success', {
		        type: 'receivables'
		      })
		    } else {
		      this.$message.error(
		        response.msg
		      )
		    }
		    this.loading = false;
		  }).catch(err => {
		    this.loading = false;
		  })
	  },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },
	  // 获取隐患编号
	  selectNumber() {
		  getNumber().then(res => {
		  	this.form.hiddenNum = res.data
		  })
	  },
	  dutyUnitChange() {
		  this.form.rectifyPersonId = undefined
		  this.form.rectifyPersonPhone = undefined
		  this.getTeamLeaderList()
	  },
	  getTeamLeaderList() {
		  let params = {
			  subcontractor: this.form.dutyUnitId,
			  ifTeamLeader: 'Y',
			  pageType: 0
		  }
		  getTeamLeaderList(params).then(res => {
			this.jobForemanList = res.data.list
		  })
	  },
	  rectifyPersonChange(value, datalist) {
		  // 获取对应的选中对象
		  let obj = {};
		  obj = datalist.find((item) => {
		  	return item.id === value;
		  })
		  this.$set(this.form, "rectifyPersonPhone", obj.phone)
		  this.$forceUpdate()
	  },
	  // 格式化时间字符串
	  formatterDate (date) {
	  	let time=new Date(date)
	    if (date == null || date == "") return "";
	    let Y = time.getFullYear() + "-";
	    let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
	    let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
	    let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
	    let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
	    let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
	    let all = (Y + M + D + h + m +s)
	    return all;
	  },
	  
	  // 查询质量问题库所有数据
	  getQuestionList() {
	    queryQuestionsTreeList().then(res => {
	  	  // 问题类型列表
	  	  this.questionOptions = res.data
	    })
	  },
	  handleNodeClick(data,node,obj){
	  	if(node.children){
	  			
	  	}else{
	  		this.form.questionId = node.key
	  		this.form.questionName = node.label
	  		var ref = 'selectTree';
	  		this.$refs[ref].blur();
	  	}
	  },
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
