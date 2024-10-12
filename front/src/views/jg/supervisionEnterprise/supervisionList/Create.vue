<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
	:showSave="action.type != 'detail'"
    @close="close"
    @save="debouncedSaveField(true)">


	<create-sections title="基本信息">
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

		</el-form>

    </create-sections>
	<create-sections title="监督人员目前监督情况">
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
		   <!-- <el-table-column :formatter="fieldFormatter" label="分项类型" align="center" prop="itemType" width="100" /> -->
		   <el-table-column label="监督员" align="center" prop="realname" />
		   <el-table-column label="监督工程数量" align="center" prop="projectNum" />
		   <el-table-column label="监督工程总面积(㎡)" align="center" prop="totalArea" />
		   <el-table-column label="已检查次数" align="center" prop="checkNum" />
		   <el-table-column label="总检查次数" align="center" prop="totalNum" />
		   <el-table-column label="最近检查日期" align="center" prop="checkTime" />
		</el-table>
    </create-sections>
	<create-sections title="任务分配">
	  <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
	           class="form">
	
		<el-row>
			<el-col :span="24">
			  <el-form-item label="选择监督人员" prop="contractCompletionDate">
				  <el-checkbox-group v-model="userIds">
				    <el-checkbox :disabled="action.type === 'detail'"
					v-for="item in userList" :label="item.userId" :key="item.userId">{{item.realname}}</el-checkbox>
				  </el-checkbox-group>
			  </el-form-item>
			</el-col>
		</el-row>
	
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="备注" prop="remark">
		      <el-input type="textarea" :disabled="action.type === 'detail'" v-model="form.remark" placeholder="请输入备注"></el-input>
		    </el-form-item>
		  </el-col>
		  
		</el-row>
	
		</el-form>
	
	</create-sections>


  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {getById, queryUserList, insertOrupdateData, getSupeList} from '../api/supervisionList'

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
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
	  open: false,

    },
    data() {
      return {
        loading: false,
		batchId: '',//附件批次id
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
		tableData:[],
		rules2: {
		  itemScore: [
		    {required: true, message: '必填项不得为空', trigger: 'blur'}
		  ],
		},
		
		userIds: [],
		userList: [],
		

      }
    },
    computed: {

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
	  
	  
	  // 查询监督人员监督情况
	  this.getSupeList()
	  
	  if(this.action.type === 'detail'){
	      this.title = "监督详情"
	  }else{
	      this.title =  this.action.type === 'allot' ? '分配任务' : '调整任务'
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
			  
			this.form = res.data
			
		    let data = {
			  pageType: 0,
			  roleType: 11,
		    }
			// 查询监督用户
			queryUserList(data).then(res => {
			  this.userList = res.data.list
			  let list = this.form.supe.split(",")
				list.forEach(item => {
					this.userIds.push(parseInt(item))
				})
			  this.$forceUpdate();
			})
			
          })
          .catch(() => {
          })
      },
	  getSupeList() {
        getSupeList()
          .then(res => {
			this.tableData = res.data
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

            this.loading = true;
			
			this.form.supe = this.userIds.join(",")
			let params = {}
			
			if (this.form.supervisionId) {
				params.id = this.form.supervisionId
			}
			
			params.supe = this.form.supe
			params.state = this.form.state
			params.remark = this.form.remark
			params.projectId = this.form.id

            insertOrupdateData(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'allot' ? '分配成功' : '调整成功'
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
		  } 

		  return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
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
