<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <!-- <el-col :span="24">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称"></el-input>
            </el-form-item>
          </el-col> -->
			<el-col :span="24">
				<el-form-item label="计划名称" prop="planId">
				<el-select v-model="form.planId" @change="planNameChange" placeholder="请选择计划名称">
					<el-option
					v-for="item in dictData.planList"
					:key="item.id"
					:label="item.planName"
					:value="item.id">
					</el-option>
				</el-select>
				</el-form-item>
			</el-col>
        </el-row>
		<el-row>
		  <el-col :span="12">
			<el-form-item label="检查编号" prop="checkCode">
			  <el-input v-model="form.checkCode" placeholder="请输入检查编号"></el-input>
			</el-form-item>
		  </el-col>
			<el-col :span="12">
			  <el-form-item label="计划等级" prop="planLevel">
				<el-select v-model="form.planLevel" disabled="true" placeholder="请选择计划等级">
				  <el-option
					v-for="item in dictData.planLevelDict"
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
			<el-form-item label="执行部门" prop="deptId">
				<select-tree :options="dictData.showDeptTreeData"
				v-model="form.deptId" :props="defaultProps" :expandAll="true"/>
			</el-form-item>
          </el-col>
			<el-col :span="12">
			  <el-form-item label="检查人" prop="inspectorId">
				<el-select v-model="form.inspectorId" placeholder="请选择检查人">
				  <el-option
					v-for="item in dictData.userListData"
					:key="item.userId"
					:label="item.realname"
					:value="item.userId">
				  </el-option>
				</el-select>
			  </el-form-item>
			</el-col>
        </el-row>
		<el-row>
			<el-col :span="12">
			  <el-form-item label="检查时间" prop="checkDate">
				<el-date-picker
				  v-model="form.checkDate"
				  type="date"
				  value-format="yyyy-MM-dd"
				  placeholder="请选择检查时间"
          @change="yxqq"
				>
				</el-date-picker>
			  </el-form-item>
			</el-col>
			<el-col :span="12">
			  <el-form-item label="整改期限" prop="repairTimeLimit">
				<el-date-picker
				  v-model="form.repairTimeLimit"
				  type="date"
				  placeholder="请选择整改期限"
          @change="yxqq2"
				  value-format="yyyy-MM-dd">
				</el-date-picker>
			  </el-form-item>
			</el-col>
		</el-row>

      </el-form>
    </create-sections>
	<create-sections title="巡检明细">
	  <inspection-detail-items
	    :action="action"
		:dictData="secondDictData"
	    ref="inspectionDetailItems"
	  ></inspection-detail-items>
	</create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import InspectionDetailItems from './InspectionDetailItems' // 明细组件

  import {updateInspectionInfo, getById} from "../api/inspectionInfo"

  import {getPlanList} from "../api/inspecitonPlan"

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
	  SelectTree,
	  InspectionDetailItems
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
	  // 字典数据
	  dictData: {
		  planList: [],
	  },
    },
    data() {
      return {
        loading: false,
        form: {
        },
		// 明细信息
		inspectionDetailList: [],
		// 数据默认字段
		defaultProps: {
		  parent: 'parentId',   // 父级唯一标识
		  value: 'id',          // 唯一标识
		  label: 'label',       // 标签显示
		  children: 'children', // 子级
		},
		// 二级表单字典数据
		secondDictData:{
			userListData: []
		},

        rules: {
          /* planName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ], */
          planId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          checkCode: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planLevel: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          deptId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          inspectorId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          checkDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          repairTimeLimit: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'execute' ? '执行检查' : '复检信息'
      },

    },
    created() {
	  this.getPlanList()
      this.debouncedSaveField = debounce(300, this.save)
	  this.getDetail();
      // 传递数据
	  this.secondDictData.userListData = this.dictData.userListData
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
			// 绑定基本信息
			var inspectionInfo = res.data.inspectionInfo
            this.form = inspectionInfo
			// 绑定明细数据
			this.inspectionDetailList = res.data.inspectionDetailList

			// 生成图片batchId列表
			var batchIdAll = []
			this.inspectionDetailList.forEach(item => {
				if(item.batchId == null) {
					batchIdAll.push(guid())
				} else {
					batchIdAll.push(item.batchId)
				}
			})

			// 生成页对象
			var pageAllItems = []
			this.inspectionDetailList.forEach(item => {
				pageAllItems.push({})
			})
			// 将数据传到子控件
			this.$refs.inspectionDetailItems.pageAllItems = pageAllItems
			this.$refs.inspectionDetailItems.batchIdAll = batchIdAll
			this.$refs.inspectionDetailItems.fieldFormAll = this.inspectionDetailList
      this.$refs.inspectionDetailItems.checkDate = this.form.checkDate
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

			var fieldFormAll = this.$refs.inspectionDetailItems.fieldFormAll
			var batchIdAll = this.$refs.inspectionDetailItems.batchIdAll

			if (fieldFormAll.length <= 0) {
				this.$message({
			    type: 'error',
			    message: '请添加隐患信息'
			  })
			  return
			}

			var time = 0;
			for (var i=0; i<fieldFormAll.length; i++) {
				fieldFormAll[time].batchId = batchIdAll[time]
				time++
			}

			//异步提交
			var fileFlag = this.$refs.inspectionDetailItems.submitImgs();
			if (!fileFlag) {
			  this.$message({
			    type: 'error',
			    message: '图片上传异常'
			  })
			  this.loading = false
			  return;
			}

			var itemFlag = true
			for (var i=0; i<fieldFormAll.length; i++) {
			  this.$refs.inspectionDetailItems.$refs['itemForm'+i][0].validate(validFlag => {
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

			var params = {
				inspectionInfo: this.form,
				inspectionDetailList: fieldFormAll
			}


            this.loading = true;
            updateInspectionInfo(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'execute' ? '执行成功' : '复检成功'
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

          }
        })
      },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },
	  // 查询安全检查标准
	  getPlanList() {
		getPlanList().then(res => {
			// 问题类型列表
			this.dictData.planList = res.data
		})
	  },
	  planNameChange(value){
		  // 获取对应的选中对象
		  let obj = {};
		  obj = this.dictData.planList.find((item) => {
		  	return item.id === value;
		  })
		  // 绑定当前计划对应数据
		  this.form.planName = obj.planName 
		  this.form.planLevel = obj.planLevel 
	  },
      yxqq(e){
        var kssj = this.form.checkDate
        var jssj = this.form.repairTimeLimit
        if(kssj==""||kssj==null||kssj==undefined){
          return
        }else{
          if(jssj!=null&&jssj!=""&&jssj!=undefined){
            var formatDate1 = new Date(kssj)
            var formatDate2 = new Date(jssj)
            if(formatDate1 > formatDate2){
              this.form.repairTimeLimit = ""
              this.$message({
                message: '整改期限不能小于检查时间',
                type: 'warning'
              })
            }
          }

          var fieldFormAll = this.$refs.inspectionDetailItems.fieldFormAll

          if (fieldFormAll.length > 0) {
            for (var i=0; i<fieldFormAll.length; i++) {
              jssj = this.$refs.inspectionDetailItems.fieldFormAll[i].rectifyDate
              if(jssj!=null&&jssj!=""&&jssj!=undefined){
                formatDate2 = new Date(jssj)
                if(formatDate1 > formatDate2){
                  this.$refs.inspectionDetailItems.fieldFormAll[i].rectifyDate = ""
                  this.$message({
                    message: '整改时间不能小于检查时间',
                    type: 'warning'
                  })
                }
              }
            }
          }
        }
      },
      yxqq2(e){
        var kssj = this.form.checkDate
        var jssj = this.form.repairTimeLimit
        if(kssj==""||kssj==null||kssj==undefined||jssj==""||jssj==null||jssj==undefined){
          return
        }else{
          var formatDate1 = new Date(kssj)
          var formatDate2 = new Date(jssj)
          if(formatDate1 > formatDate2){
            this.form.repairTimeLimit = ""
            this.$message({
              message: '整改期限不能小于检查时间',
              type: 'warning'
            })
          }
        }
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
