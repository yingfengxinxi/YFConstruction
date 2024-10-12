<template>
  <div class="main">
<!--    <xr-header
      icon-class="wk wk-project"
      icon-color="#2362FB"
      label="安全资料管理" />-->

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;overflow: hidden;">
        <el-form-item label="工人姓名" prop="personnelName">
          <el-input
            v-model="queryParams.personnelName"
            placeholder="请输入工人姓名"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>


        <el-form-item label="证件号码" prop="personnelCard" >
          <el-input
            v-model="queryParams.personnelCard"
            placeholder="请输入证件号码"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="所在企业名称" prop="enterpriseName" >
          <el-input
            v-model="queryParams.enterpriseName"
            placeholder="请输入所在企业名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="行为发生时间" prop="badDate" >
         	<el-date-picker
         		class="timecom"
         	  v-model="sjtbDate"
         	  type="daterange"
         	  align="right"
         	  unlink-panels
         	  range-separator="至"
         	  start-placeholder="行为发生日期起"
         	  end-placeholder="行为发生日期止"
         	  :picker-options="pickerOptions">
         	</el-date-picker>
        </el-form-item>

        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
         <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

          <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="dataList" :height="tableHeight" @sort-change="sortChange">
        <el-table-column label="工人姓名" align="center" prop="personnelName" width="120px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="证件类型" :formatter="personnelCardTypeDataFc" align="center" width="120px" prop="personnelCardType" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="证件号码" align="center" prop="personnelCard" width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="所在企业名称" align="center" prop="enterpriseName" width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="统一社会信用代码" align="center" prop="enterpriseUniscid" width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="登记部门" align="center" prop="goodDept" width="150px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="登记人姓名" align="center" prop="goodDeptNpersonnel" width="120px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="登记日期" :formatter="formatterTime" align="center" prop="goodDeptDate" width="120px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="良好行为描述" align="center" prop="goodDescribe" width="200px" show-overflow-tooltip="true" sortable="custom" />
        <el-table-column label="良好行为发生日期" :formatter="formatterTime" align="center" prop="goodDate" width="160px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="良好行为发生地行政区划" align="center" prop="goodDivision" width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="项目编码" align="center" prop="projectNumber" width="120px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="奖励部门" align="center" prop="rewardDept" width="120px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="奖励部门级别" :formatter="rewardDeptLevelDataFc" align="center"  width="140px" prop="rewardDeptLevel" show-overflow-tooltip sortable="custom" />
        <el-table-column label="奖励决定内容" align="center" prop="rewardContent" width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="奖励决定文号" align="center" prop="rewardDocument" width="140px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="奖励日期" :formatter="formatterTime" align="center" prop="rewardDate" width="120px" show-overflow-tooltip sortable="custom"/>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
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
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row >
            <el-col :span="8">
              <el-form-item label="工人姓名" prop="personnelName" class="add-form-input">
                <el-input v-model="form.personnelName" placeholder="请输入工人姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="证件类型" prop="personnelCardType" class="add-form-input">
                <el-select
                  v-model="form.personnelCardType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in personnelCardTypeData"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="证件号码" prop="personnelCard" class="add-form-input">
                <el-input v-model="form.personnelCard" placeholder="请输入证件号码" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所在企业名称" prop="enterpriseName" class="add-form-input">
                <el-input v-model="form.enterpriseName" placeholder="请输入所在企业名称" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="社会信用代码" prop="enterpriseUniscid" class="add-form-input">
                <el-input v-model="form.enterpriseUniscid" placeholder="统一社会信用代码" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="登记部门" prop="goodDept" class="add-form-input">
                <el-input v-model="form.goodDept" placeholder="请输入登记部门" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="登记人姓名" prop="goodDeptNpersonnel" class="add-form-input">
                <el-input v-model="form.goodDeptNpersonnel" placeholder="请输入登记人姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="登记日期" prop="goodDeptDate" class="add-form-input">
                <el-date-picker
                  v-model="form.goodDeptDate"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发生日期" prop="goodDate" class="add-form-input">
                <el-date-picker
                  v-model="form.goodDate"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="行为描述" prop="goodDescribe">
                <el-input type="textarea" :rows="2" v-model="form.goodDescribe" placeholder="请输入良好行为描述"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="行政区划" prop="goodDivision" class="add-form-input">
                <el-input v-model="form.goodDivision" placeholder="请输入良好行为发生地行政区划" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编码" prop="projectNumber" class="add-form-input">
                <el-input v-model="form.projectNumber" placeholder="请输入项目编码" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="奖励部门" prop="rewardDept" class="add-form-input">
                <el-input v-model="form.rewardDept" placeholder="请输入奖励部门" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="部门级别" prop="rewardDeptLevel" class="add-form-input">
                <el-select
                  v-model="form.rewardDeptLevel"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in rewardDeptLevelData"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="决定文号" prop="rewardDocument" class="add-form-input">
                <el-input v-model="form.rewardDocument" placeholder="请输入奖励决定文号" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="奖励日期" prop="rewardDate" class="add-form-input">
                <el-date-picker
                  v-model="form.rewardDate"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="奖励决定内容" prop="rewardContent">
                <el-input type="textarea" :rows="2" v-model="form.rewardContent" placeholder="请输入奖励决定内容"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
import { guid,sortColumn } from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表
//	import vPinyin from './vuepy.js';

export default {
  name: 'perGoodBehavior',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // table高度控制
      tableHeight: document.documentElement.clientHeight - 260,
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
      // 表格数据
      dataList: [],
      // 证件类型
      personnelCardTypeData: [],
      // 奖励部门级别
      rewardDeptLevelData: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      sjtbDate: '',
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        crediType: '1'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        personnelName: [
          { required: true, message: '员工姓名不能为空', trigger: 'blur' }
        ],
        personnelCardType: [
          { required: true, message: '证件类型不能为空', trigger: 'blur' }
        ],
        personnelCard: [
          { required: true, message: '证件号码不能为空', trigger: 'blur' }
        ],
        enterpriseName: [
          { required: true, message: '所在企业名称不能为空', trigger: 'blur' }
        ],
        enterpriseUniscid: [
          { required: true, message: '所在企业统一社会信用代码不能为空', trigger: 'blur' }
        ],
        goodDept: [
          { required: true, message: '登记部门不能为空', trigger: 'blur' }
        ],
        goodDeptNpersonnel: [
          { required: true, message: '登记人姓名不能为空', trigger: 'blur' }
        ],
        goodDeptDate: [
          { required: true, message: '登记日期不能为空', trigger: 'blur' }
        ],
        goodDescribe: [
          { required: true, message: '良好行为描述不能为空', trigger: 'blur' }
        ],
        goodDate: [
          { required: true, message: '良好行为发生日期不能为空', trigger: 'blur' }
        ],
        rewardContent: [
          { required: true, message: '奖励决定内容不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    // 证件类型
    this.getDicts('b_id_card_type').then(response => {
      this.personnelCardTypeData = response.data
    })
    // 奖励部门级别
    this.getDicts('b_reward_dept_level').then(response => {
      this.rewardDeptLevelData = response.data
    })
    this.$nextTick(function(){
    	this.tableHeight=document.documentElement.clientHeight-this.$refs.queryForm.$el.offsetHeight-170;
    })
  },
  methods: {
		sortChange(column) {
      sortColumn(this,column);
    },
    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      this.queryParams.crediType = '1'
      request({
        url: 'build/bCreditGood/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        console.log(response.data)
        this.dataList = response.data.list
        this.total = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    formatterTime(row,column){
      let data = row[column.property]
      return  /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.sjtbDate != '') {
        console.log(this.sjtbDate)
        var date = new Date(this.sjtbDate[0])
        var y = date.getFullYear()
        var m = date.getMonth() + 1
        m = m < 10 ? ('0' + m) : m
        var d = date.getDate()
        d = d < 10 ? ('0' + d) : d
        const time = y + '-' + m + '-' + d
        this.queryParams.createBy = time
        var date = new Date(this.sjtbDate[1])
        var y = date.getFullYear()
        var m = date.getMonth() + 1
        m = m < 10 ? ('0' + m) : m
        var d = date.getDate()
        d = d < 10 ? ('0' + d) : d
        const time2 = y + '-' + m + '-' + d
        this.queryParams.updateBy = time2
      }
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.sjtbDate = ''
      //this.resetForm('queryForm')
      this.queryParams = {};
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增'
      this.batchId = guid()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bCreditGood/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        this.form.crediType = '1'
        if (valid) {
          //this.form.batchId = this.batchId
     /*     var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '附件提交异常'
            })
            return
          }*/
          if (this.form.id != undefined) {
            request({
              url: 'build/bCreditGood/edit',
              method: 'post',
              data: this.form,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            request({
              url: 'build/bCreditGood/add',
              method: 'post',
              data: this.form,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
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
      const id = row.id || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return request({
          url: 'build/bCreditGood/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },

    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('system/dict/type/export', {
    //     ...this.queryParams
    //   }, `type_${new Date().getTime()}.xlsx`)
    // },
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
    // 证件类型
    personnelCardTypeDataFc(row, column){
      return this.selectDictLabel(this.personnelCardTypeData, row.personnelCardType)
    },
    //  奖励部门级别
    rewardDeptLevelDataFc(row, column){
      return this.selectDictLabel(this.rewardDeptLevelData, row.rewardDeptLevel)
    }
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


  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
	/deep/ .special .el-form-item__label{
		width: 100px!important;
	}
	.timecom{
		margin-top: 4px;
		height: 32px;
	}
	.timecom span{
		width: 20px;
	}
</style>
