<template>
  <div class="main">
    <xr-header
      icon-class="wk wk-edit"
      icon-color="#33D08F"
      label="字典数据" />

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="字典名称" prop="dictType">
          <select-col v-model="queryParams.dictType" sTb="wk_admin_dict_type"
            sColKey="dict_id" sColVal="dict_type" sColName="dict_name">
          </select-col>
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input
            v-model="queryParams.dictLabel"
            placeholder="请输入字典标签"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="数据状态" clearable size="small">
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button
            class="main-table-header-button"
            type="normal"
            icon="el-icon-back"
            @click="back" >返回</el-button>
          <el-button
            class="main-table-header-button xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column label="字典编码" align="center" prop="dictCode" />
        <el-table-column label="字典标签" align="center" prop="dictLabel" />
        <el-table-column label="字典键值" align="center" prop="dictValue" />
        <el-table-column label="字典排序" align="center" prop="dictSort" />
        <el-table-column :formatter="statusFormat" label="状态" align="center" prop="status" />
        <el-table-column :show-overflow-tooltip="true" label="备注" align="center" prop="remark" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)">修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div> -->

      <!-- 添加或修改参数配置对话框 -->
      <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="字典类型">
            <el-input v-model="form.dictType" :disabled="true" />
          </el-form-item>
          <el-form-item label="数据标签" prop="dictLabel">
            <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
          </el-form-item>
          <el-form-item label="数据键值" prop="dictValue">
            <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
          </el-form-item>
          <el-form-item label="显示排序" prop="dictSort">
            <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"> {{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog> -->
			
		<!-- 新增或修改参数配置对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
		  <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
			<el-row>
			  <el-col :span="12">
				<el-form-item label="字典类型" prop="dictType" class="add-form-input">
				  <el-input v-model="form.dictType" :disabled="true" placeholder="请输入" />
				</el-form-item>
			  </el-col>
			  <el-col :span="12">
				<el-form-item label="数据标签" prop="dictLabel" class="add-form-input">
				  <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
				</el-form-item>
			  </el-col>
			  <el-col :span="12">
				<el-form-item label="数据键值" prop="dictValue" class="add-form-input">
				  <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
				</el-form-item>
			  </el-col>
			  <el-col :span="12">
				<el-form-item label="显示排序" prop="dictSort" class="add-form-input">
				  <el-input v-model.number="form.dictSort" type="number" placeholder="请输入" controls-position="right" :min="0" />
				</el-form-item>
			  </el-col>
			  <el-col :span="12">
				<el-form-item label="状态" prop="status" class="add-form-input">
				  <el-radio-group v-model="form.status">
				    <el-radio
				      v-for="dict in statusOptions"
				      :key="dict.dictValue"
				      :label="dict.dictValue"> {{ dict.dictLabel }}</el-radio>
				  </el-radio-group>
				</el-form-item>
			  </el-col>
			  <el-col :span="12">
				<el-form-item label="备注" prop="remark" class="add-form-input">
				  <el-input v-model="form.remark" placeholder="请输入内容" />
				</el-form-item>
			  </el-col>
			  <el-col v-if="form.dictCode" :span="24">
				<el-button
				  class="xr-btn--orange"
				  type="primary"
				  icon="el-icon-plus"
				  size="mini"
				  style="float: right;margin-bottom: 5px"
				  @click="handleAddMatch" >新建</el-button>

				<el-table v-loading="loading" :data="matchList" >
				  <el-table-column label="本系统字典类型" align="center" prop="selfDict" />
				  <el-table-column label="本系统字典值" align="center" prop="selfDictValue" />
				  <el-table-column label="对应系统标签" align="center" prop="matchSystem" />
				  <el-table-column label="对应系统字典类型" align="center" prop="matchDict" />
				  <el-table-column label="对应系统值" align="center" prop="matchValue" />
				  <el-table-column label="对应系统名称" align="center" prop="matchSysName" />
				  <el-table-column label="备注" align="center" prop="remarks" />
				  <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
					<template slot-scope="scope">
					  <el-button
						size="mini"
						type="text"
						icon="el-icon-edit"
						@click="handleUpdateMatch(scope.row)"
					  >修改</el-button>
					  <el-button
						size="mini"
						type="text"
						icon="el-icon-delete"
						@click="handleDeleteMatch(scope.row)"
					  >删除</el-button>
					</template>
				  </el-table-column>
				</el-table>
			  </el-col>
			</el-row>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="submitForm">确 定</el-button>
			<el-button @click="cancel">取 消</el-button>
		  </div>
		</el-dialog>
		
		<el-dialog :title="titleMatch" :visible.sync="matchOpen" width="900px" append-to-body>
		  <el-form ref="matchForm" :model="matchForm" :rules="matchRules" label-width="140px" class="add-form">
		    <el-form-item label="本系统字典类型" prop="selfDict">
		      <el-input v-model="matchForm.selfDict" :disabled="true" />
		    </el-form-item>
		    <el-form-item label="本系统字典值" prop="selfDictValue">
		      <el-input v-model="matchForm.selfDictValue" :disabled="true" />
		    </el-form-item>
		    <el-form-item label="对应系统标签" prop="matchSystem">
		      <el-autocomplete style="width: 100%;" v-model="matchForm.matchSystem" :fetch-suggestions="querySearchAsync1" placeholder="请输入对应系统编码" @select="handleSelect" ></el-autocomplete>
		    </el-form-item>
		    <el-form-item label="对应系统字典类型" prop="matchDict">
		      <el-autocomplete style="width: 100%;" v-model="matchForm.matchDict" :fetch-suggestions="querySearchAsync2" placeholder="请输入对应系统字典类型" @select="handleSelect" ></el-autocomplete>
		    </el-form-item>
		    <el-form-item label="对应系统值" prop="matchValue">
		      <el-input v-model="matchForm.matchValue" placeholder="请输入对应系统值" />
		    </el-form-item>
		    <el-form-item label="对应系统名称" prop="matchSysName">
		      <el-input v-model="matchForm.matchSysName" placeholder="请输入对应系统名称" />
		    </el-form-item>
				<el-form-item label="备注" prop="remarks">
				  <el-input v-model="matchForm.remarks" placeholder="请输入对应系统名称" />
				</el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button type="primary" @click="submitFormMatch">确 定</el-button>
		    <el-button @click="cancelMatch">取 消</el-button>
		  </div>
		</el-dialog>
    </div>
  </div>
</template>

<script>
import { listData, getData, delData, addData, updateData } from '@/api/admin/dict/data'
import { matchList, getMatchById, saveOrUpdateMatch, delMatchById, delMatchByDict, selectSuggestList} from '@/api/admin/dict/match'
import { listType, getType } from '@/api/admin/dict/type'

import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import SelectCol from "../components/selectCol";

export default {
  name: 'Data',
  components: {
    SelectCol,
    Reminder,
    XrHeader
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      // 字典表格数据
      dataList: [],
      // 默认字典类型
      defaultDictType: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dictLabel: [
          { required: true, message: '数据标签不能为空', trigger: 'blur' }
        ],
        dictValue: [
          { required: true, message: '数据键值不能为空', trigger: 'blur' }
        ],
        dictSort: [
          { required: true, message: '数据顺序不能为空', trigger: 'blur' }
        ]
      },
	  
	  // 字典转换数据
	  matchList: [],
	  // 字典转换标题
	  titleMatch: '',
	  // 是否弹出
	  matchOpen: false,
	  // 表单
	  matchForm: {},
	  // 表单校验
	  matchRules: {
	    selfDict: [
	      { required: true, message: '本系统字典类型不能为空', trigger: 'blur' }
	    ],
	    selfDictValue: [
	      { required: true, message: '本系统字典值不能为空', trigger: 'blur' }
	    ],
	    matchDict: [
	      { required: true, message: '对应系统字典类型不能为空', trigger: 'blur' }
	    ],
	    matchValue: [
	      { required: true, message: '对应系统值不能为空', trigger: 'blur' }
	    ],
	    matchSystem: [
	      { required: true, message: '对应系统编码不能为空', trigger: 'blur' }
	    ]
	  },
	  
    }
  },
  created() {
    const dictId = this.$route.params && this.$route.query.dictId
    this.getType(dictId)
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询字典类型详细 */
    getType(dictId) {
      getType(dictId).then(response => {
        this.queryParams.dictType = response.data.dictType
        this.defaultDictType = response.data.dictType
        this.getList()
      })
    },
    /** 查询字典数据列表 */
    getList() {
      this.loading = true
      // this.queryParams.page = this.currentPage
      // this.queryParams.limit = this.pageSize
      listData(this.queryParams).then(response => {
        this.dataList = response.data
        // this.total = response.data.totalRow
        this.loading = false
      })
    },
	querySearchAsync1(queryString, cb) {
		let data = {
			type: '1',
			queryString: queryString
		}
		selectSuggestList(data).then(res => {
			var restaurants = res.data;
			cb(restaurants);
		})
		
	},
	querySearchAsync2(queryString, cb) {
		let data = {
			type: '2',
			queryString: queryString
		}
		selectSuggestList(data).then(res => {
			var restaurants = res.data;
			cb(restaurants);
		})
		
	},
	handleSelect(item) {
		console.log(item);
	},
    // 数据状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        dictCode: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        dictSort: 0,
        status: '0',
        remark: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.dictType = this.defaultDictType
      this.handleQuery()
    },
    back() {
      this.$router.go(-1)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加字典数据'
      this.form.dictType = this.queryParams.dictType
	  // 重置字典转换数据列表
	  this.matchList = []
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictCode)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const dictCode = row.dictCode || this.ids
      getData(dictCode).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改字典数据'
		
		// 查询对应字典转换数据
		this.getMatchList()
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.dictCode != undefined) {
            updateData(this.form).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addData(this.form).then(response => {
              if (response.code === 0) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dictCodes = row.dictCode || this.ids
      this.$confirm('是否确认删除字典编码为\'' + dictCodes + '\'的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delData(dictCodes)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
		
		// 删除对应字典转换数据
		let params = {
			selfDict: row.dictType,
			selfDictValue: row.dictValue
		}
		delMatchByDict(params)
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dict/data/export', {
        ...this.queryParams
      }, `data_${new Date().getTime()}.xlsx`)
    },
    // 更改每页展示数量
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    // 更改当前页数
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
	
	
	  /** 查询字典转换数据列表 */
	  getMatchList() {
	    this.loading = true
		let queryParams = {
			page: 1,
			limit: 100,
		}
		queryParams.selfDict = this.form.dictType
		queryParams.selfDictValue = this.form.dictValue
		matchList(queryParams).then(response => {
		  this.matchList = response.data.list
		  // this.total = response.data.totalRow
		  this.loading = false
		})
	  },
	/** 字典转换新增按钮操作 */
	handleAddMatch() {
	  this.resetMatch()
	  this.matchOpen = true
	  this.titleMatch = '添加字典转换数据'
	  this.matchForm.selfDict = this.form.dictType
	  this.matchForm.selfDictValue = this.form.dictValue
	},
	/** 字典转换修改按钮操作 */
	handleUpdateMatch(row) {
	  getMatchById(row.id).then(response => {
	    this.matchForm = response.data
	    this.matchOpen = true
	    this.titleMatch = '修改字典转换数据'
	  })
	},
	/** 字典转换提交按钮 */
	submitFormMatch: function() {
	  this.$refs['matchForm'].validate(valid => {
	    if (valid) {
	      if (this.matchForm.id != undefined) {
	        saveOrUpdateMatch(this.matchForm).then(response => {
	          if (response.code === 0) {
	            this.msgSuccess('修改成功')
	            this.matchOpen = false
	            this.getMatchList()
	          }
	        })
	      } else {
	        saveOrUpdateMatch(this.matchForm).then(response => {
	          if (response.code === 0) {
	            this.msgSuccess('新增成功')
	            this.matchOpen = false
	            this.getMatchList()
	          }
	        })
	      }
	    }
	  })
	},
	/** 字典转换删除按钮操作 */
	handleDeleteMatch(row) {
	  this.$confirm('是否确认删除当前字典转换数据?', '警告', {
	    confirmButtonText: '确定',
	    cancelButtonText: '取消',
	    type: 'warning'
	  }).then(function() {
	    return delMatchById(row.id)
	  }).then(() => {
	    this.getMatchList()
	    this.msgSuccess('删除成功')
	  }).catch(function() {})
	},
    // 取消按钮
    cancelMatch() {
      this.matchOpen = false
      this.resetMatch()
    },
	// 表单重置
	resetMatch() {
	  this.matchForm = {
	    matchSystem: undefined,
	    matchDict: undefined,
	    matchValue: undefined,
	    matchSysName: undefined
	  }
	  this.resetForm('matchForm')
	},
  }
}
</script>

<style lang="scss" scoped>
  .main {
    height:100%;
  }
  /deep/ .xr-header {
    padding: 15px 30px;
  }

  .main-body {
    background-color: white;
    /*border-top: 1px solid $xr-border-line-color;*/
    border-bottom: 1px solid $xr-border-line-color;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }
  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  @import '../styles/table.scss';

</style>
