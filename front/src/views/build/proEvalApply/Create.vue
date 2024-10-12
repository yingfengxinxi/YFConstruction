<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    
	  
	<create-sections title="申请信息">
      <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="8">
            <el-form-item label="申请评级" prop="levelId">
              <el-select style="width: 100%;" @change="evalLevelChange" v-model="form.levelId" placeholder="请选择申请评级">
                <el-option
                  v-for="item in evalLevelList"
                  :key="item.id"
                  :label="item.levelName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请说明" prop="applyDes">
              <el-input v-model="form.applyDes" placeholder="请输入申请说明"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人" prop="applyBy">
              <el-input v-model="form.applyBy" placeholder="请输入申请人"></el-input>
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
		></wk-upload-file>
    </create-sections>
	<create-sections title="选项列表">
	  
	  <!-- <el-form ref="queryParams" :model="queryParams" label-position="top" label-width="100px"
	           class="form">
        <el-row>
		  <el-col :span="8">
			<el-form-item label="应用类别" prop="classType">
			  <el-select style="width: 100%;" v-model="queryParams.classType" placeholder="请选择应用类别">
			  	<el-option
			  	  v-for="item in classTypeDict"
			  	  :key="item.dictValue"
			  	  :label="item.dictLabel"
			  	  :value="item.dictValue">
			  	</el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item label="分项类型" prop="itemType">
			  <el-select style="width: 100%;" v-model="queryParams.itemType" placeholder="请选择分项类型">
				<el-option
				  v-for="item in itemTypeDict"
				  :key="item.dictValue"
				  :label="item.dictLabel"
				  :value="item.dictValue">
				</el-option>
			  </el-select>
			</el-form-item>
		  </el-col>
		  <el-col :span="8">
			<el-form-item style="margin-top: 40px;">
			  <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
			  <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		  </el-col>
		</el-row>
			   
	  </el-form> -->
		
        <el-table
				ref="itemTable"
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
		:row-key="getRowKey" 
		@selection-change="handleSelectionChange"
		@select="selectSingle"
        style="width: 100%"
				>
		  <el-table-column
		    show-overflow-tooltip
			:reserve-selection="true"
		    type="selection"
		    align="center"
		    width="55"/>
		  <el-table-column label="序号" type="index" align='center' width="70"></el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="应用类别" align="center" prop="classTypeName" />
          <!-- <el-table-column :show-overflow-tooltip="true" label="分项类型" align="center" prop="itmeTypeName" /> -->
		<el-table-column label="打分项名称 " align="center" prop="itemName" width="180">
		  <template  slot-scope="scope">
			<div >
				{{scope.row.itemName}}
				<!-- <el-tooltip class="item" effect="light" :content="scope.row.itemDes" placement="top">
					<img style="width: 16px; height: 16px; vertical-align: text-bottom;" src="@/assets/img/wenhao.png" >
				</el-tooltip> -->
			</div>
		  </template>
		</el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="最大分值" align="center" prop="maxTotal" />
          <el-table-column :show-overflow-tooltip="true" label="最小分值" align="center" prop="minTotal" />

		</el-table>
    </create-sections>
   

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import WkUploadFile from "@/components/CreateCom/WkUploadFile";

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import { getById, selectLevelList, selectItemList, saveApplyInfo} from "./api/eval";

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
            monitType:'',
            data: {}
          }
        }
      },
      titleName:''
    },
    data() {
      return {
        loading: false,
		batchId: '',//附件批次id
		title:'评级申请',
        form: {
          dataDate:this.initDataSel(),
        },
        list: [],
        tableHeight: 500, // 表的高度
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
		/* 下拉选择相关数据 */
		// 工地等级数据
		evalLevelList: [],
		// 应用类别
		classTypeDict: [],
		// 分项类型
		itemTypeDict: [],
		
		// 当前等级默认打分项数据
		cacheItemList: [],
		
		// 被选中的数据
		selectionList: [],
		
		// 查询条件
		queryParams: {},
		
      }
    },
    computed: {
      title() {
        if(this.action.type === 'show'){
            return "评级申请"
        }else{
            return "评级申请"
        }
        
      },
      disabledT() {
        return this.action.type === 'show' ? true : false
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
	  // 查询字典和下拉框数据
	  this.initDicts()
	  
      if (this.action.type == "save") {
	    this.batchId = guid()
	    this.form.batchId = this.batchId
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
        getById(this.action.id,this.action.monitType)
          .then(res => {
            this.form = res.data;
          
            selectItemList().then(res =>{
              this.list = res.data
            })

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
			  
			var fileFlag = this.$refs.wkUploadFile.submitAsync();
			if(!fileFlag){
			  this.$message({
			    type: 'error',
			    message: '附件提交异常'
			  })
			  return;
			}
			
			let params = {
				applyInfo: this.form,
				applyItemList: this.selectionList
			}

            this.loading = true;

            saveApplyInfo(params).then(response => {
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
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      //获取监测项
      getList(){
		  

        selectItemList(this.queryParams)
          .then(res => {
            this.list = res.data;
			
			
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      //选择时间
      initDataSel(){
          //获取当前时间
          var now   = new Date()
          var monthn = now.getMonth()+1>9?now.getMonth()+1:"0"+(now.getMonth()+1)
          var yearn  = now.getFullYear()
          var dayn = now.getDate()>9?now.getDate():"0"+(now.getDate())
          var selectMonth = yearn+"-"+monthn +"-"+dayn
          
          return selectMonth
      },
	  
	  // 初始化字典数据
	  initDicts() {
	  	this.getDicts('b_eval_class_type').then(response => {
	  	  this.classTypeDict = response.data
	  	})
	  	this.getDicts('b_eval_item_type').then(response => {
	  	  this.itemTypeDict = response.data
	  	})
	  
		// 查询工地等级数据
		selectLevelList().then(res =>{
			this.evalLevelList = res.data
		}) 
	  },
	  
	  evalLevelChange(val) {
		  /* let params = {
			  evalLevelId: val
		  }
		  
		  this.$message.error(
			'打分项已重置，请重新选择！'
		  )
		  
		  // 清空被选中数据、缓存的被选中数据、查询条件
		  this.selectionList = []
		  this.queryParams = {}
		  
		  // 重新查询列表数据
		  selectItemList(this.queryParams)
		    .then(res => {
		      this.list = res.data;
			  
			  // 查询当前申请评级 默认选中的打分项数据
			  selectItemList(params).then(response => {
				  // 储存当前等级默认打分项数据 
				  this.cacheItemList = response.data
				  // 清空选中数据
				  this.$refs.itemTable.clearSelection();
				  // 添加选中数据
				  let selectList = this.createSelectionData(this.cacheItemList)
				  
				  this.toggleSelection(selectList)
			  })
			  
		      this.loading = false
		    })
		    .catch(() => {
		      this.loading = false
		  }) */
		  
	  },
	  
	  // 创建添加的数据
	  createSelectionData(selectionList) {
		  let selectList = []
		  selectionList.forEach(item => {
			  this.list.forEach(tableItem => {
				  if (item.id == tableItem.id ) {
					selectList.push(tableItem)				  
				  }
			  })
		  })
		  return selectList
	  },
	  
	  // 添加选中的数据
	  toggleSelection(rows) {
		if (rows) {
			rows.forEach(row => {
				this.$refs.itemTable.toggleRowSelection(row);	
			});
		} 
	  },
	  
	  /** 勾选操作 */
	  getRowKey(row){
	  	return row.id;
	  },
	  
	  // 当选择项发生变化时会触发该事件
	  handleSelectionChange(val) {
		  this.selectionList = val
	  },
	  selectSingle(selection,row) {
		  
		  this.cacheItemList.forEach(item => {
			  if (item.id === row.id){
				this.$refs.itemTable.toggleRowSelection(row)
			  }
		  })
	  },
	  
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.getList()
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
  .el-date-editor{
    width: 100%;
  }
  /* #crm-table{
    height: 600px!important;
  } */
  /deep/.crm-create-card-container{
	  width: 1200px!important;
  }
</style>
