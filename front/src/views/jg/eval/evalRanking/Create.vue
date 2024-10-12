<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    :confirm-button-text2="'暂存'"
	:showSave="action.type != 'detail'"
	:showHold="action.type != 'detail'"
    @close="close"
    @hold="debouncedSaveField2(true)"
    @save="debouncedSaveField(true)">


	<create-sections title="申请信息">
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
            <el-form-item label="申请评级" prop="levelId">
              <el-select style="width: 100%;" :disabled="true" v-model="form.levelId" placeholder="请选择申请评级">
                <el-option
                  v-for="item in dictData.evalLevelList"
                  :key="item.id"
                  :label="item.levelName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请说明" prop="applyDes">
              <el-input :disabled="true" v-model="form.applyDes" placeholder="请输入申请说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

		<el-row>
          <el-col :span="8">
            <el-form-item label="申请人" prop="applyBy">
              <el-input :disabled="true" v-model="form.applyBy" placeholder="请输入申请人"></el-input>
            </el-form-item>
          </el-col>
		  <el-col :span="8">
		    <el-form-item label="最终得分" prop="score">
		      <el-input :disabled="true" v-model="form.score" placeholder="请输入最终得分"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="8">
		    <el-form-item label="实际等级" prop="appLevel">
		      <el-input :disabled="true" v-model="form.appLevel" placeholder="请输入实际等级"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>

		<el-row>
		  <el-col :span="8">
		    <el-form-item label="评估说明" prop="appDes">
		      <el-input :disabled="action.type === 'detail'" v-model="form.appDes" placeholder="请输入评估说明"></el-input>
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
			:batchId="batchId"
			:disabled="true"
		></wk-upload-file>

    </create-sections>
	<create-sections title="选项列表">
	  <el-form :rules="formData.rules" :model="formData"  ref="formData" >
			
		<el-tabs
		  v-model="tabCurrentName"
		  type="border-card"
		  @tab-click="tabClick"
		  class="d-container-bd--left">
		  
		   <el-tab-pane v-for="(item,index) in classTypeItemList" :key="index" :label="item.itemName" :name="item.classType">
				<el-table
				ref="itemTable"
				v-loading="loading"
				id="crm-table"
				:data="tableData"
				:height="tableHeight"
				class="n-table--border"
				use-virtual
				stripe
				border
				highlight-current-row>
				   <el-table-column label="序号" type="index" align='center' width="60"></el-table-column>
				   <!-- <el-table-column :formatter="fieldFormatter" label="应用类别" align="center" prop="classType" width="160" /> -->
				   <el-table-column :formatter="fieldFormatter" label="分项类型" align="center" prop="itemType" width="100" />
				 <el-table-column label="打分项名称 " align="center" prop="itemName" width="180">
				   <template  slot-scope="scope">
					<div >
						{{scope.row.itemName}}
						<el-tooltip class="item" effect="light" :content="scope.row.itemDes" placement="top">
							<img style="width: 16px; height: 16px; vertical-align: text-bottom;" src="@/assets/img/wenhao.png" >
						</el-tooltip>
					</div>
				   </template>
				 </el-table-column>
				 <el-table-column label="分项描述" align="center" prop="itemDes" width="200" />
				 
				 <el-table-column v-if="action.type === 'detail'" label="得分" align="center" prop="itemScore" width="100" />
				 
				   <el-table-column v-if="action.type != 'detail'" label="得分" align="center" prop="itemScore" width="140">
					  <template  slot-scope="scope">
						<el-form-item :prop="'list.' + scope.$index + '.itemScore'" :rules='rules2.itemScore'>
							<el-input-number size="small" :max="scope.row.maxData" :min="scope.row.minData" type="number" v-model="scope.row.itemScore"
							placeholder="请输入得分" @change="itemScoreChange" />
						</el-form-item>
					  </template>
				   </el-table-column>
				   
				 <el-table-column v-if="action.type === 'detail'" label="打分说明" align="center" prop="scoreDes" width="160" />
				 <el-table-column v-if="action.type != 'detail'" label="打分说明" align="center" prop="scoreDes" width="160">
					 <template  slot-scope="scope">
						<el-form-item :prop="'list.' + scope.$index + '.scoreDes'">
							<el-input size="small" v-model="scope.row.scoreDes"
							placeholder="请输入打分说明" ></el-input>
						</el-form-item>
					 </template>
				 </el-table-column>
				 
				 <el-table-column label="打分标准 " align="center" prop="minData">
				   <template  slot-scope="scope">
					<div >
						{{scope.row.minData}}~{{scope.row.maxData}}
					</div>
				   </template>
				 </el-table-column>
				 
				 <el-table-column label="附件" align="center">
				   <template  slot-scope="scope">
					<div>已上传数量{{scope.row.fileNumber?scope.row.fileNumber:0}}</div>
					<el-button
					  size="mini"
					  type="success"
					  @click="filePageOpen(scope.row.index)">点击上传</el-button>
				   </template>
				 </el-table-column>
				 </el-table>
		   </el-tab-pane>
		</el-tabs>
	  
	  </el-form>
	  <el-dialog title="附件" :visible.sync="open" width="500px" append-to-body>
		  <wk-upload-file
		  	:ref="'wkUploadFile'+itemIndex"
		  	name="file"
		  	multiple
		  	accept="image/*"
		  	:autoUpload="true"
		  	:batchId="batchIdAll[itemIndex]"
			:refInfex="itemIndex"
			:callbackFile="true"
			:disabled="disabledT"
			@callbackFileList="callbackFileList"
		  ></wk-upload-file>
	  </el-dialog>
    </create-sections>


  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import WkUploadFile from "@/components/CreateCom/WkUploadFile";

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import { getById, selectLevelList, gradeOrHold} from "../api/apply";

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
	  WkUploadFile,
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
      titleName:'',
	  dictData: {},
	  
	  itemIndex: 0,
	  open: false,
	  

    },
    data() {
      return {
        loading: false,
		batchId: '',//附件批次id
	    batchIdAll: [], // 列表附件
		title:'打分评级',
        form: {
        },
        tableHeight: 300, // 表的高度
        rules: {
          levelId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          applyDes: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          applyBy: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ]
        },
		formData: {
			list: [
				{
					scoreDes: '',
					itemScore: ''
				}
			],
		},
		tableData:[],
		rules2: {
		  itemScore: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		},
	    disabledT:false,
		
		tabCurrentName:'1',
		classTypeItemList: [],
		classType:'1',

      }
    },
    computed: {

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      this.debouncedSaveField2 = debounce(300, this.hold)
	  
	  this.disabledT = this.action.type === 'detail' ? true : false
	
	  if(this.action.type === 'detail'){
	      this.title = "评分详情"
	  }else{
	      this.title =  this.action.type === 'update' ? '打分评级' : '评分申请'
	  }

      if (this.action.type == "save") {
        this.getList()
      }else{
         this.getDetail()
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
			let data = res.data
			if (data.evalApply) {
				this.form = data.evalApply
				this.batchId = this.form.batchId
			}
			if (data.evalApplyItemsList) {
				this.formData.list = data.evalApplyItemsList
				
				if (this.action.type == "update") {
					this.formData.list.forEach(item => {
						if (item.batchId) {
							this.batchIdAll.push(item.batchId)
						} else {
							let batchId = guid()
							item.batchId = batchId
							this.batchIdAll.push(batchId)
						}
					})
				} else {
					this.formData.list.forEach(item => {
						this.batchIdAll.push(item.batchId)
					})
				}
				
			}
			// 过滤列表数据
			this.classTypeItemList = data.classTypeItemList
			if (this.classTypeItemList.length >= 0) {
				this.tabCurrentName = this.classTypeItemList[0].classType
				this.classType = this.classTypeItemList[0].classType
				this.filterItemList()
			}
			

          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {

        this.$refs['form', 'formData'].validate(valid => {
          if (valid) {

			// 计算最终得分
			this.itemScoreChange()

			// 设置状态 status 1：评估中 2：申请通过 3：申请拒绝
			this.form.status = '2'
			let params = {
				applyInfo: this.form,
				applyItemList: this.formData.list
			}

            this.loading = true;

            gradeOrHold(params).then(response => {
              if (response.code === 0) {
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
              this.loading = false;
            }).catch(err => {
              this.loading = false;
            })

          } else {
			  this.$message.error(
			    "请完成所有打分项！"
			  )
		  }
        })
      },
	  /**
	   * 暂存
	   */
	  hold(isSubmit) {
		  
		  // 计算得分
		  this.itemScoreChange()

		  // 设置状态 status 1：评估中 2：申请通过 3：申请拒绝
		  this.form.status = '1'
		  let params = {
		  	applyInfo: this.form,
		  	applyItemList: this.formData.list
		  }

		  this.loading = true;

		  gradeOrHold(params).then(response => {
		    if (response.code === 0) {
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
		  if (column.property === 'classType') {
		  	return this.selectDictLabel(this.dictData.classTypeDict, row.classType)
		  } else if (column.property === 'itemType') {
		  	return this.selectDictLabel(this.dictData.itemTypeDict, row.itemType)
		  }

		  return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.getList()
      },
	  // 计算得分和实际等级
	  itemScoreChange() {
		  
		  // 通过下标 覆盖源数据
		  this.tableData.forEach(item => {
			  this.formData.list[item.index] = item
		  })
		  
		  // 计算
		  let list = this.formData.list
		  let score = 0
		  list.forEach(item => {
			  if (item.itemScore) {
				score += this.DetermineItemType(item)
			  }
		  })
			
		  if (score == 0) {
			this.form.score = 0
		  } else {
			this.form.score = parseFloat(score).toFixed(2)
		  }
		  // 根据分数判断实际等级
		  if (this.form.score < 70) {
			  this.form.appLevel = '星级未达标'
		  } else if (this.form.score >= 70 && this.form.score < 80) {
			  this.form.appLevel = '一星级'
		  } else if (this.form.score >= 80 && this.form.score < 90) {
			  this.form.appLevel = '二星级'
		  } else if (this.form.score >= 90) {
			  this.form.appLevel = '三星级'
		  }
	  },

	  // 判断分项类型
	  DetermineItemType(item) {
		  // 实际单项得分
		  let actualScore = 0
		  if (item.itemType == '10') {
			  actualScore = this.basicScoreCalculation(item.classType, item.itemScore)
		  } else if (item.itemType == '20') {
			  actualScore = item.itemScore
		  } else if (item.itemType == '30') {
			  actualScore = item.itemScore
		  }
		  return actualScore
	  },
	  // 计算单项分值
	  basicScoreCalculation(classType, itemScore) {
		  if (itemScore === 0) {
			  return 0;
		  }
		  let weight
		  if (classType == '1') {
			  weight = 0.15
		  } else if (classType == '2') {
			  weight = 0.15
		  } else if (classType == '3') {
			  weight = 0.4
		  } else if (classType == '4') {
			  weight = 0.1
		  } else if (classType == '5') {
			  weight = 0.1
		  } else if (classType == '6') {
			  weight = 0.1
		  }
		  return itemScore * weight * 0.85
	  },
	  // 显示文件上传页面
	  filePageOpen(index){
		  this.itemIndex = index;
		  this.open = true;
	  },
	  callbackFileList(index,data){
		  this.formData.list[index].fileNumber = data.length
		  this.filterItemList()
	  },
	  // 重新筛选table数据
	  filterItemList() {
		  this.tableData = this.formData.list.filter(item => item.classType == this.classType)
	  },
	  tabClick(tab){
		  
		  // 通过下标 覆盖源数据
		  this.tableData.forEach(item => {
			  this.formData.list[item.index] = item
		  })
		  // 更新table数据
		  this.classType = tab.name
		  this.filterItemList()
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
	  width: 1200px!important;
  }
</style>
