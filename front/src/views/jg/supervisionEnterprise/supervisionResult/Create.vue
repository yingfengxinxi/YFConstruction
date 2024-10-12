<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
	:showSave="action.type != 'detail'"
    @close="close"
    @save="debouncedSaveField(true)">


	<create-sections title="工程概况">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="projectId">
              <el-select style="width: 100%;" :disabled="true" v-model="form.projectId" placeholder="请选择项目名称">
                <el-option
                  v-for="item in dictData.projectOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="建设单位" prop="constructionUnitName">
              <el-input :disabled="true" v-model="form.constructionUnitName" placeholder="请输入建设单位"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="施工许可证编号" prop="constructionPermitCode">
              <el-input :disabled="true" v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

		<el-row>
          <el-col :span="8">
            <el-form-item label="建设性质" prop="constructionNature">
              <el-select style="width: 100%;" :disabled="true" v-model="form.constructionNature" placeholder="请选择建设性质">
                <el-option
                  v-for="item in dictData.constructionNatureOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总造价(万元)" prop="totalCost">
              <el-input :disabled="true" v-model="form.totalCost" placeholder="请输入总造价"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总面积(㎡)" prop="totalArea">
              <el-input :disabled="true" v-model="form.totalArea" placeholder="请输入总面积"></el-input>
            </el-form-item>
          </el-col>
		</el-row>
		<el-row>
			<el-col :span="8">
			  <el-form-item label="项目地址" prop="projectAddress">
				<el-input :disabled="true" v-model="form.projectAddress" placeholder="请输入项目地址"></el-input>
			  </el-form-item>
			</el-col>
			<el-col :span="8">
			  <el-form-item label="开工日期" prop="contractCommencementDate">
				<el-date-picker
				  :disabled="true"
				  v-model="form.contractCommencementDate"
				  type="date"
				  value-format="yyyy-MM-dd"
				  placeholder="请选择开工日期"
				>
				</el-date-picker>
			  </el-form-item>
			</el-col>
			<el-col :span="8">
			  <el-form-item label="竣工日期" prop="contractCompletionDate">
				<el-date-picker
				  :disabled="true"
				  v-model="form.contractCompletionDate"
				  type="date"
				  value-format="yyyy-MM-dd"
				  placeholder="请选择竣工日期"
				>
				</el-date-picker>
			  </el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="8">
			  <el-form-item label="验收阶段" prop="inspectionPhase">
			    <el-select style="width: 100%;" :disabled="true" v-model="form.inspectionPhase" 
				placeholder="请选择验收阶段">
			      <el-option
			        v-for="item in dictData.inspectionPhaseDict"
			        :key="item.dictValue"
			        :label="item.dictLabel"
			        :value="item.dictValue">
			      </el-option>
			    </el-select>
			  </el-form-item>
			</el-col>
			<el-col :span="16">
			  <el-form-item label="申请说明" prop="inspectionContent">
			    <el-input :disabled="true" v-model="form.inspectionContent" placeholder="请输入申请说明"></el-input>
			  </el-form-item>
			</el-col>
		</el-row>
			
		</el-form>
		<wk-upload-file
		ref="wkUploadFile"
		name="file"
		multiple
		accept="image/*"
		:autoUpload="false"
		:batchId="form.batchId"
		:disabled="true"
		></wk-upload-file>

    </create-sections>
	<create-sections title="监督检查">
	  <el-form ref="checkForm" :model="checkForm" :rules="rules" label-position="top" label-width="100px"
	           class="form">
	    <el-row>
	      <el-col :span="8">
	        <el-form-item label="单位工程名称" prop="projectName">
	          <el-input :disabled="action.type === 'detail'" v-model="checkForm.projectName" placeholder="请输入单位工程名称"></el-input>
	        </el-form-item>
	      </el-col>
	      <el-col :span="8">
	        <el-form-item label="在施部位" prop="constructionParts">
			  <el-input :disabled="action.type === 'detail'" v-model="checkForm.constructionParts" placeholder="请输入在施部位"></el-input>
	          <!-- <el-select style="width: 100%;" :disabled="action.type === 'detail'" v-model="checkForm.constructionParts" 
						placeholder="请选择在施部位">
	            <el-option
	              v-for="item in dictData.inspectionPhaseDict"
	              :key="item.dictValue"
	              :label="item.dictLabel"
	              :value="item.dictValue">
	            </el-option>
	          </el-select> -->
	        </el-form-item>
	      </el-col>
			<el-col :span="8">
			  <el-form-item label="检查时间" prop="checkTime">
				<el-date-picker
				  :disabled="action.type === 'detail'"
				  v-model="checkForm.checkTime"
				  type="datetime"
				  value-format="yyyy-MM-dd HH:mm:ss"
				  placeholder="请选择检查时间"
				>
				</el-date-picker>
			  </el-form-item>
			</el-col>
	    </el-row>
	
		<el-row>
	      <el-col :span="8">
	        <el-form-item label="气象状况" prop="meteorological">
	          <el-input :disabled="action.type === 'detail'" v-model="checkForm.meteorological" placeholder="请输入气象状况"></el-input>
	        </el-form-item>
	      </el-col>
	      <el-col :span="8">
	        <el-form-item label="工程评价" prop="projectEvaluation">
				<el-radio-group v-model="projectEvaluation">
				  <el-radio :disabled="action.type === 'detail'" v-for="(item,index) in dictData.projectAppraiseDict" :key="index" 
				  :label="item.dictValue">{{item.dictLabel}}</el-radio>
				</el-radio-group>
	        </el-form-item>
	      </el-col>
	      <el-col :span="8">
	        <el-form-item label="安全主管到岗情况" prop="securityWorkInfo">
				<el-radio-group v-model="securityWorkInfo">
				  <el-radio :disabled="action.type === 'detail'" v-for="(item,index) in dictData.dutyStatusDict" :key="index" 
				  :label="item.dictValue">{{item.dictLabel}}</el-radio>
				</el-radio-group>
	        </el-form-item>
	      </el-col>
		</el-row>
		<el-row>
		  <el-col :span="8">
		    <el-form-item label="项目经理到岗情况" prop="managerWorkInfo">
				<el-radio-group v-model="managerWorkInfo">
				  <el-radio :disabled="action.type === 'detail'" v-for="(item,index) in dictData.dutyStatusDict" :key="index" 
				  :label="item.dictValue">{{item.dictLabel}}</el-radio>
				</el-radio-group>
		    </el-form-item>
		  </el-col>
		  <el-col :span="8">
		    <el-form-item label="总监到岗情况" prop="directorWorkInfo">
				<el-radio-group v-model="directorWorkInfo">
				  <el-radio :disabled="action.type === 'detail'" v-for="(item,index) in dictData.dutyStatusDict" :key="index" 
				  :label="item.dictValue">{{item.dictLabel}}</el-radio>
				</el-radio-group>
		    </el-form-item>
		  </el-col>
		</el-row>
	
		</el-form>
		<wk-upload-file
		ref="wkUploadFile"
		name="file"
		multiple
		accept="image/*"
		:autoUpload="false"
		:disabled="true"
		:batchId="checkForm.batchId"
		></wk-upload-file>
	
	</create-sections>
	<create-sections title="检查内容">
	  <el-form :model="detailForm"  ref="detailForm" class="table-form">
		<!-- <el-button
			class="xr-btn--orange"
			type="primary"
			icon="el-icon-plus"
			size="mini"
			style="float: right;margin-bottom: 5px"
			@click="handleAddClick" >新增</el-button> -->
		<el-table
		ref="itemTable"
		v-loading="loading"
		id="crm-table"
		:data="detailForm.list"
		:height="tableHeight"
		class="n-table--border"
		use-virtual
		stripe
		border
		highlight-current-row>
		   <el-table-column label="序号" type="index" align='center' width="60"></el-table-column>
		   <!-- <el-table-column :formatter="fieldFormatter" label="分项类型" align="center" prop="itemType" width="100" /> -->
		   
		  <el-table-column v-if="action.type === 'detail'" label="检查项目" align="center" prop="checkProject" />
		  <el-table-column v-if="action.type != 'detail'" label="检查项目" align="center" prop="checkProject" >
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.checkProject'" :rules='detailRules.checkProject'>
					  <el-input size="small" v-model="scope.row.checkProject"
					  placeholder="请输入检查项目" ></el-input>
				  </el-form-item>
			  </template>
		  </el-table-column>
		  <el-table-column v-if="action.type === 'detail'" label="检查部位" align="center" prop="checkParts" />
		  <el-table-column v-if="action.type != 'detail'" label="检查部位" align="center" prop="checkParts" >
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.checkParts'" :rules='detailRules.checkParts'>
					  <el-input size="small" v-model="scope.row.checkParts"
					  placeholder="请输入检查部位" ></el-input>
				  </el-form-item>
			  </template>
		  </el-table-column>
		  <el-table-column v-if="action.type === 'detail'" label="检查情况" align="center" prop="checkSituation" />
		  <el-table-column v-if="action.type != 'detail'" label="检查情况" align="center" prop="checkSituation" >
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.checkSituation'" :rules='detailRules.checkSituation'>
					  <el-input size="small" v-model="scope.row.checkSituation"
					  placeholder="请输入检查情况" ></el-input>
				  </el-form-item>
			  </template>
		  </el-table-column>
		  <el-table-column v-if="action.type === 'detail'" label="检查结果" align="center" prop="checkResults" />
		  <el-table-column v-if="action.type != 'detail'" label="检查结果" align="center" prop="checkResults" >
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.checkResults'" :rules='detailRules.checkResults'>
					  <el-input size="small" v-model="scope.row.checkResults"
					  placeholder="请输入检查结果" ></el-input>
				  </el-form-item>
			  </template>
		  </el-table-column>
		  <el-table-column v-if="action.type === 'detail'" :formatter="checkDisposalFormat" label="处置情况" align="center" prop="checkDisposal" width="300" />
		  <el-table-column v-if="action.type != 'detail'" label="处置情况" align="center" prop="checkDisposal" width="300">
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.checkDisposal'" :rules='detailRules.checkDisposal'>
					<el-radio-group v-model="detailForm.list[scope.$index].checkDisposal">  
					  <el-radio v-for="(item,index) in dictData.disposalStateDict" :key="index" 
					  :label="item.dictValue">{{item.dictLabel}}</el-radio>
					</el-radio-group>
				  </el-form-item>
			  </template>
		  </el-table-column>
		  <el-table-column v-if="action.type === 'detail'" label="责任方" align="center" prop="responsibility" />
		  <el-table-column v-if="action.type != 'detail'" label="责任方" align="center" prop="responsibility" >
			  <template  slot-scope="scope">
				  <el-form-item :prop="'list.' + scope.$index + '.responsibility'" :rules='detailRules.responsibility'>
					  <el-input size="small" v-model="scope.row.responsibility"
					  placeholder="请输入责任方" ></el-input>
				  </el-form-item>
			  </template>
		  </el-table-column>
			 <el-table-column label="附件" align="center" width="160">
			   <template  slot-scope="scope">
				<div>已上传数量{{scope.row.fileNumber?scope.row.fileNumber:0}}</div>
				<el-button
				  size="mini"
				  type="success"
				  @click="filePageOpen(scope.row.batchId, scope.$index)">查看文件</el-button>
			   </template>
			 </el-table-column>
			<!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
			  <template slot-scope="scope">
				<el-button
				  size="mini"
				  type="text"
				  icon="el-icon-delete"
				  @click="handleDeleteClick(scope.$index)"
				>删除</el-button>
			  </template>
			</el-table-column> -->
		</el-table>
	  </el-form>
	  
	  <el-dialog :title="fileTitle" :visible.sync="open" width="500px" append-to-body>
	    <wk-upload-file
		:ref="'wkUploadFile'+refInfex"
		name="file"
		multiple
		accept="image/*"
		:autoUpload="true"
		:batchId="refInfex"
	  	:refInfex="refInfex"
	  	:callbackFile="true"
	  	:disabled="disabledT"
	  	@callbackFileList="callbackFileList"
	    ></wk-upload-file>
	  </el-dialog>
    </create-sections>
	
	<create-sections v-if="form.inspectionType == '2' || form.inspectionType == '3'" title="检查结果">
	  <el-form ref="formResults" :model="formResults" :rules="rules" label-position="top" label-width="100px"
	           class="form">
	    <el-row>
	      <el-col :span="16">
	        <el-form-item :label="resultsContentText" prop="resultsContent">
	          <el-input :disabled="action.type === 'detail'" v-model="formResults.resultsContent"></el-input>
	        </el-form-item>
	      </el-col>
			<el-col :span="8">
			  <el-form-item :label="resultsTimeText" prop="resultsTime">
				<el-date-picker
				  :disabled="action.type === 'detail'"
				  v-model="formResults.resultsTime"
				  type="datetime"
				  value-format="yyyy-MM-dd HH:mm:ss"
				  placeholder="请选择"
				>
				</el-date-picker>
			  </el-form-item>
			</el-col>
	    </el-row>
	
		</el-form>
		<wk-upload-file
		ref="wkUploadFile"
		name="file"
		multiple
		accept="image/*"
		:autoUpload="false"
		:disabled="true"
		:batchId="formResults.batchId"
		></wk-upload-file>
	
	</create-sections>


  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import WkUploadFile from "@/components/CreateCom/WkUploadFile";

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {getById, saveCheckData, selectResultDataByPid} from '../api/supervisionCheck'

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
	  WkUploadFile
    }, 

    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            id: '',
          }
        }
      },
	  dictData: {},

    },
    data() {
      return {
        loading: false,
		batchId: '',//附件批次id
		title:'',
		fileTitle: '附件',
        form: {},
		checkForm: {},
        tableHeight: 300, // 表的高度
        rules: {
          projectName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          constructionParts: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          checkTime: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
		detailRules: {
		  checkProject: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		  checkParts: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		  checkSituation: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		  checkResults: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		  responsibility: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		},
		
		projectAppraiseDict: [], // 工程评价字典值
		dutyStatusDict: [], // 到岗情况字典值
		disposalStateDict: [], // 监督检查处置情况字典值
		
		projectEvaluation: '', // 工程评价
		securityWorkInfo: '', // 安全主管到岗情况
		managerWorkInfo: '', // 项目经理到岗情况
		directorWorkInfo: '', // 总监到岗情况
		checkDisposal: '', // 总监到岗情况
		
		detailForm: {
			list: []
		},
		formResults:{}, // 检查结果
		refInfex: "",
		itemIndex: 0,
	    disabledT:false,
	    batchIdAll: [], // 列表附件
		open: false,
		resultsContentText: '验收/整改说明',
		fileText: '验收/整改附件',
		resultsTimeText: '验收/整改日期',
		

      }
    },
    computed: {

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
	  
	  // 查询字典值
	  this.initDicts()
	  this.checkForm.batchId =guid()
	  this.disabledT = this.action.type === 'detail' ? true : false
	  
	  if(this.action.type === 'detail'){
	      this.title = "监督详情"
	  }else{
	      this.title =  this.action.type === 'check' ? '监督检查' : '检查详情'
	  }
	  
	  this.getDetail()

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
			
			this.form = res.data.inspection
			if (this.form.rectificationInfo == '0') {
			  this.resultsContentText = '验收说明'
			  this.fileText = '验收附件'
			  this.resultsTimeText = '验收日期'
			} else if (this.form.rectificationInfo == '1') {
			  this.resultsContentText = '整改说明'
			  this.fileText = '整改附件'
			  this.resultsTimeText = '整改日期'
			}
			this.checkForm = res.data.inspectionCheck
			this.projectEvaluation = this.checkForm.projectEvaluation
			this.securityWorkInfo = this.checkForm.securityWorkInfo
			this.managerWorkInfo = this.checkForm.managerWorkInfo
			this.directorWorkInfo = this.checkForm.directorWorkInfo
			
			this.detailForm.list = res.data.inspectionCheckDetails
			
			// 查询结果数据
			selectResultDataByPid(this.form.id).then(res => {
				this.formResults = res.data
			})
			
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
		  
		this.$refs['checkForm'].validate(valid => {
			if (valid) {
				if (this.detailForm.list == null || this.detailForm.list.length < 1) {
				  this.$message.error(
					"检查内容的数量不能小于1！"
				  )
				} else {
					this.$refs['detailForm'].validate(valid => {
					  if (valid) {
					
					    this.loading = true;
						
						let params = {}
						
						this.checkForm.inspectionId = this.form.id
						this.checkForm.projectId = this.form.projectId
						params.inspectionCheck = this.checkForm
						params.inspectionCheckDetails = this.detailForm.list
					
					    saveCheckData(params).then(response => {
					      if (response.code === 0) {
					        this.$message.success(
					          this.action.type == 'check' ? '保存成功' : '修改成功'
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
					
					  } else {
						  this.$message.error(
						    "请完成检查内容！"
						  )
					  }
					})
				}
			} else {
			  this.$message.error(
			    "请填写完监督检查的内容！"
			  )
			}
		})
        
      },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },

       /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'code') {
          return 'can-visit--underline can-visit--bold'
        }
      },

      headerCellClassName({row, column, rowIndex, columnIndex}) {
        /*if (column.property === 'code') {
          return 'header-can-visit-backgroud'
        }*/
        return ''
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		  /* if (column.property === 'classType') {
		  	return this.selectDictLabel(this.dictData.classTypeDict, row.classType)
		  } */

		  return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
	  
	  // 初始化字典数据
	  initDicts() {
		this.getDicts('b_inspection_project_appraise').then(response => {
		  this.dictData.projectAppraiseDict = response.data
		})
		this.getDicts('b_inspection_duty_status').then(response => {
		  this.dictData.dutyStatusDict = response.data
		})
		this.getDicts('b_inspection_disposal_state').then(response => {
		  this.dictData.disposalStateDict = response.data
		})
	  },
	  handleAddClick() {
		let batchId = guid()
		this.detailForm.list.push({
			batchId: batchId,
			checkDisposal: '1',
		})
	  },
	  handleDeleteClick(index) {
			this.$confirm("确定删除选中的数据吗？", '提示', {
			  confirmButtonText: '确定',
			  cancelButtonText: '取消',
			  type: 'warning'
			}).then(() => {
				this.detailForm.list.splice(index, 1)
			})
	  },
	  // 显示文件上传页面
	  filePageOpen(batchId, index){
		  console.log('打印')
		  console.log(batchId)
		  console.log(index)
		  console.log(this.$refs)
	    this.refInfex = batchId;
		this.itemIndex = index
	    this.open = true;
	  },
	  callbackFileList(refInfex,data){
		  this.detailForm.list[this.itemIndex].fileNumber = data.length
	  },
	  checkDisposalFormat(row, column){
		  return this.selectDictLabel(this.dictData.disposalStateDict, row.checkDisposal)
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
	margin-bottom: 12;
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
  /deep/.crm-create-card-container{
	  width: 1600px!important;
  }
  /deep/ .table-form .el-form-item{
	  margin-bottom: 0px;
  }
</style>
