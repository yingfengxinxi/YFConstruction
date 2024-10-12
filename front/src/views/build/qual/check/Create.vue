<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">

	<el-form ref="form" :model="checkForm" :rules="rules" label-position="top" label-width="100px"
		class="form">
	  <create-sections title="基本信息" v-if="action.type != 'receipt'" >
        <el-row>
          <el-col :span="8">
            <el-form-item label="巡检类型" prop="qualCheckType">
			  <el-select
			    v-model="checkForm.qualCheckType"
			    placeholder="请选择"
			    clearable
				@change="checkTypeChange"
			  >
			    <el-option
			      v-for="dict in dictData.checkTypeDict"
			      :key="dict.dictValue"
			      :label="dict.dictLabel"
			      :value="dict.dictValue"
			    />
			  </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="巡检人员" prop="qualCheckPersonId">
			  <xh-struc-user-cell
                :users="checkForm.dep.ownerUserList"
			    style="width: 100%;"
			    @value-change="strcUserChange"/>
            </el-form-item>
          </el-col>
			<el-col :span="8">
			  <el-form-item label="项目负责人" prop="qualPrincipalName">
				<el-input v-model="checkForm.qualPrincipalName" placeholder="请输入项目负责人"></el-input>
			  </el-form-item>
			</el-col>
        </el-row>
		<el-row>
		  <el-col :span="8">
			<el-form-item label="巡检日期" prop="qualCheckDate">
			  <el-date-picker
			    v-model="checkForm.qualCheckDate"
			    type="date"
			    placeholder="请选择巡检日期"
			    value-format="yyyy-MM-dd">
			  </el-date-picker>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item label="联系电话" prop="qualPhone">
			  <el-input v-model="checkForm.qualPhone" placeholder="请输入联系电话"></el-input>
			</el-form-item>
		  </el-col>
			<el-col :span="8">
			  <el-form-item label="整改期限" prop="qualRepairTimeLimit">
				<el-date-picker
				  v-model="checkForm.qualRepairTimeLimit"
				  type="date"
				  placeholder="请选择整改期限"
				  value-format="yyyy-MM-dd">
				</el-date-picker>
			  </el-form-item>
			</el-col>
		</el-row>
		<el-row>
		  <el-col :span="8">
			<el-form-item label="单据编号" prop="qualDocumentNumber">
			  <el-input disabled v-model="checkForm.qualDocumentNumber" placeholder="请输入单据编号"></el-input>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item label="处理意见" prop="checkboxList">
			   <el-checkbox-group v-model="checkForm.checkboxList">
			      <el-checkbox label="1">停工</el-checkbox>
			      <el-checkbox label="2">罚款</el-checkbox>
			      <el-checkbox label="3">整改</el-checkbox>
			   </el-checkbox-group>
			</el-form-item>
		  </el-col>
			<el-col :span="8">
			  <el-form-item label="巡检单位" prop="qualCheckCom">
				<el-input v-model="checkForm.qualCheckCom" placeholder="请输入巡检单位"></el-input>
			  </el-form-item>
			</el-col>
		</el-row>
		<el-row>
		  <el-col :span="8">
			<el-form-item label="形象进度" prop="qualProgress">
			  <el-select
			    v-model="checkForm.qualProgress"
			    placeholder="请选择"
			    clearable
			  >
			    <el-option
			      v-for="dict in dictData.imageProgressDict"
			      :key="dict.dictValue"
			      :label="dict.dictLabel"
			      :value="dict.dictValue"
			    />
			  </el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item label="巡检计分" prop="qualCheckScore">
			  <el-input v-model="checkForm.qualCheckScore" placeholder="请输入巡检计分"></el-input>
			</el-form-item>
		  </el-col>
			<el-col :span="8">
			  <el-form-item label="单据日期" prop="qualDocumentDate">
				<el-date-picker
				  :readonly="true"
				  v-model="checkForm.qualDocumentDate"
				  type="date"
				  placeholder="请选择单据日期"
				  value-format="yyyy-MM-dd">
				</el-date-picker>
			  </el-form-item>
			</el-col>
		</el-row>
      </create-sections>
	  <create-sections title="巡检明细">
	    <hidden-trouble-items
	      :action="action"
	      ref="hiddenTroubleItems"
	    ></hidden-trouble-items>
	  </create-sections>
	</el-form>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import MembersDep from '@/components/SelectEmployee/MembersDep'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import { XhStrucUserCell} from '@/components/CreateCom'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import HiddenTroubleItems from './HiddenTroubleItems'

  import {generateDocumentNumber, saveOrUpdate, getById, receiptInfo} from "../api/check";

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
	  MembersDep,
	  XhStrucUserCell,
	  HiddenTroubleItems
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
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
	  // 字典数据
	  dictData:{}
    },
    data() {
      return {
		  radio:false,
        loading: false,
		// 质量巡检基本信息
        checkForm: {
		    checkboxList: ['3'],
			qualDocumentNumber: '',
			qualCheckDate: new Date(new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() +
						" 00:00:00"),
			qualDocumentDate: new Date(new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() +
						" 00:00:00"),
			qualCheckPersonName: '',
			dep: {
				ownerUserList: []
			}
        },
		// 隐患信息
		hiddenTroubleListL: [],

        rules: {
          qualCheckType: [
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ],
          qualCheckPersonId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          qualPrincipalName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          qualPhone: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          qualDocumentNumber: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          checkboxList: [
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ],
          qualCheckCom: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          qualDocumentDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ],
        },
      }
    },
    computed: {
      title() {
		var title = '新建巡检'
		if (this.action.type === 'update') {
			title = '编辑巡检'
		} else if (this.action.type === 'receipt') {
			title = '复检巡检'
		}
        return title
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
     //考虑附件ID
      if (this.action.type == "update" || this.action.type == "receipt") {
        this.getDetail();
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
			// 设置多选框数据
			var qualCheck = res.data.qualCheck;
			qualCheck.checkboxList = qualCheck.qualOpinionOption.split(",")
			// console.log(qualCheck.checkboxList)
			// console.log(qualCheck)

			// 转换用户对象
			var ownerUserList = qualCheck.ownerUserList
			var dep = {
				ownerUserList: ownerUserList
			}
			qualCheck.dep = dep

			// 绑定基本信息
            this.checkForm = qualCheck

			// 绑定隐患
            this.hiddenTroubleList = res.data.hiddenTroubleList;

			// 生成图片batchId列表
			var batchIdAll = []
			var receiptBatchIdAll = []
			this.hiddenTroubleList.forEach(item => {
				batchIdAll.push(item.hiddenTroubleBatchId)
				if(item.receiptImgBatchId == null){
					receiptBatchIdAll.push(guid())
				}else{
					receiptBatchIdAll.push(item.receiptImgBatchId)
				}
			})

			// 将数据传到子控件
			this.$refs.hiddenTroubleItems.fieldFormAll = this.hiddenTroubleList
			this.$refs.hiddenTroubleItems.batchIdAll = batchIdAll
			this.$refs.hiddenTroubleItems.receiptBatchIdAll = receiptBatchIdAll

			// 生成页对象
			var pageAllItems = []
			this.hiddenTroubleList.forEach(item => {
				pageAllItems.push({})
			})
			// 将数据传到子控件
			this.$refs.hiddenTroubleItems.pageAllItems = pageAllItems
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



			var fieldFormAll = this.$refs.hiddenTroubleItems.fieldFormAll
			var batchIdAll = this.$refs.hiddenTroubleItems.batchIdAll
			var receiptBatchIdAll = this.$refs.hiddenTroubleItems.receiptBatchIdAll

			if (fieldFormAll.length <= 0) {
				this.$message({
			    type: 'error',
			    message: '请添加隐患信息'
			  })
			  return
			}
			var itemFlag = true
			for (var i=0; i<fieldFormAll.length; i++) {
			  this.$refs.hiddenTroubleItems.$refs['itemForm'+i][0].validate(validFlag => {
				  // console.log("itemForm=validate")
				  itemFlag = validFlag
			  })
			}
			if(!itemFlag){
				  this.$message({
					  type: 'error',
					  message: '请完善隐患信息'
				  })
				  return false;
			}
			var time = 0;
			for (var i=0; i<fieldFormAll.length; i++) {
				fieldFormAll[time].hiddenTroubleBatchId = batchIdAll[time]
				time++
			}
			var time2 = 0;
			for (var i=0; i<receiptBatchIdAll.length; i++) {
				fieldFormAll[time2].receiptImgBatchId = receiptBatchIdAll[time2]
				time2++
			}

			this.hiddenTroubleList = fieldFormAll
			this.loading = true;

			//异步提交
			var fileFlag = this.$refs.hiddenTroubleItems.submitImgs();
			if (!fileFlag) {
			  this.$message({
			    type: 'error',
			    message: '隐患图片上传异常'
			  })
			  this.loading = false
			  return;
			}

			  if(this.action.type === 'receipt'){
				var fileFlag = this.$refs.hiddenTroubleItems.submitImgs2();
				if (!fileFlag) {
				  this.$message({
					type: 'error',
					message: '复检图片上传异常'
				  })
				  this.loading = false
				  return;
				}
			  }

			// 将处理意见转换为字符串
			var qualOpinionOption = ''
			var checkboxList = this.checkForm.checkboxList
			for (var i = 0; i<checkboxList.length; i++) {
				if (i === checkboxList.length-1) {
					qualOpinionOption += checkboxList[i]
				} else {
					qualOpinionOption += checkboxList[i] + ","
				}
			}
			this.checkForm.qualOpinionOption = qualOpinionOption


			var params = {
				checkForm: this.checkForm,
				hiddenTroubleList: this.hiddenTroubleList
			}

			if (this.action.type != 'receipt') {
				saveOrUpdate(params).then(res => {
					if (res.code === 0) {
					  this.$message.success(
						this.action.type == 'update' ? '编辑成功' : '添加成功'
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
					this.loading = false
				}).catch(err => {
					this.loading = false
				})
			} else {
				// 复检
				receiptInfo(params).then(res => {
					if (res.code === 0) {
					  this.$message.success('复检成功')
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
					this.loading = false
				}).catch(err => {
					this.loading = false
				})
			}
          }
        })
      },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },
	  // TODO 如果两个人同时点击 单据号相同需要处理
	  // 巡检类型Change事件
	  checkTypeChange(value) {
		  // 巡检类型等于项目类型 向表单添加单据标号的值
		  if (value === "1") {
			  generateDocumentNumber().then(res => {
				  this.checkForm.qualDocumentNumber = res.data
			  })
		  } else {
			  this.checkForm.qualDocumentNumber = ''
		  }
	  },
	  // 添加巡检人员
	  strcUserChange(data) {
		  this.$set(this.checkForm, 'dep', { ownerUserList: data.value.users})
      console.log("人员列表")
      console.log(this.checkForm.dep.ownerUserList)
      console.log("人员列表")
		  var qualCheckPersonId = this.checkForm.dep.ownerUserList
									.map(item => item.userId)
									.join(',')
		  this.checkForm.qualCheckPersonId = qualCheckPersonId
		  var qualCheckPersonName = this.checkForm.dep.ownerUserList
		  									.map(item => item.realname)
		  									.join(',')
		  this.checkForm.qualCheckPersonName = qualCheckPersonName
      var userTel = ''
      if (this.checkForm.dep.ownerUserList.length > 0) {
        for (var i = 0 ; i < this.checkForm.dep.ownerUserList.length ; i++) {
          if (userTel == '') {
            userTel = this.checkForm.dep.ownerUserList[i].mobile
          } else {
            userTel = userTel + ',' + this.checkForm.dep.ownerUserList[i].mobile
          }
        }
      }
      this.checkForm.qualPhone = userTel
    }
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
  .el-date-editor{
    width: 100%;
  }
  .el-select{
	 width: 100%;
  }
  /deep/
  .el-input.is-disabled .el-input__inner{
	  background: none!important;
	  color: #666;
  }
</style>
