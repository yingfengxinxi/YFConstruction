<template>
  <div class="main">


    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="人员姓名" prop="infocollectionName">
          <el-input
            v-model="queryParams.infocollectionName"
            placeholder="请输入人员姓名"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="人员身份证号" prop="infocollectionCard" >
          <el-input
            v-model="queryParams.infocollectionCard"
            placeholder="请输入人员身份证号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="第三方工资单" prop="thirdPayRollCode" >
          <el-input
            v-model="queryParams.thirdPayRollCode"
            placeholder="请输入第三方工资单编号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="发放日期">
          <el-date-picker
            class="timecom"
            v-model="sjtbDate"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="发放开始日期"
            end-placeholder="发放结束日期"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>



        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
         <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange" >
        <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
        <el-table-column label="人员姓名" align="center" prop="infocollectionName" sortable="custom" width="120px" />
        <el-table-column label="点工(日工)" align="center" sortable="custom">
          <el-table-column label="日单价" align="center" prop="dayPrice" width="120px" sortable="custom" />
          <el-table-column label="工日（天）" align="center" prop="manDays" width="120px" sortable="custom" />
        </el-table-column>
        <el-table-column label="包工" align="center">
          <el-table-column label="工程量单价" align="center" prop="quantitiesPrice" width="120px" sortable="custom" />
          <el-table-column label="工程量" align="center" prop="quantities" width="120px" sortable="custom" />
        </el-table-column>
        <el-table-column :formatter="formatterTime" label="发放日期" align="center" width="120px" prop="issueDate" sortable="custom" />
        <el-table-column label="应发工资(元)" align="center" prop="wageDetailShould" width="160px" sortable="custom" />
        <el-table-column label="预支工资(元)" align="center" prop="wageDetailAdvance" width="160px" sortable="custom" />
        <el-table-column label="本月余额(元)" align="center" prop="monthBalance" width="160px" sortable="custom" />
        <el-table-column label="累计剩余(元)" align="center" prop="cumulativeBalance" width="160px" sortable="custom" />
        <el-table-column label="总工时" align="center" prop="workHours" sortable="custom" width="120px" />
        <el-table-column label="实发金额(元)" align="center" prop="actualAmount" sortable="custom" width="140px" />
        <el-table-column :formatter="formatterYesNo" label="是否补发" align="center" prop="isBackPay" width="120px" sortable="custom" />
        <el-table-column label="补发月份" align="center" prop="backPayMonth" sortable="custom" width="120px" />
        <el-table-column label="第三方工资单编号" align="center" prop="thirdPayRollCode" sortable="custom" width="160px" />

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >查看</el-button>
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
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="8">
              <el-form-item label="人员姓名" prop="infocollectionName" class="add-form-input">
                <el-input v-model="form.infocollectionName" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="身份证号" prop="infocollectionCard" class="add-form-input">
                <el-input v-model="form.infocollectionCard" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工资月份" prop="wageDetailMonth" class="add-form-input">
                <el-input v-model="form.wageDetailMonth" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发放日期" prop="issueDate" class="add-form-input">
                <el-input v-model="form.issueDate" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="日单价" prop="dayPrice" class="add-form-input">
                <el-input v-model="form.dayPrice" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工日（天）" prop="manDays" class="add-form-input">
                <el-input v-model="form.manDays" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工程量单价" prop="quantitiesPrice" class="add-form-input">
                <el-input v-model="form.quantitiesPrice" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工程量" prop="quantities" class="add-form-input">
                <el-input v-model="form.quantities" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="应发工资(元)" prop="wageDetailShould" class="add-form-input">
                <el-input v-model="form.wageDetailShould" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预支工资(元)" prop="wageDetailAdvance" class="add-form-input">
                <el-input v-model="form.wageDetailAdvance" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="本月余额(元)" prop="monthBalance" class="add-form-input">
                <el-input v-model="form.monthBalance" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="累计剩余(元)" prop="cumulativeBalance" class="add-form-input">
                <el-input v-model="form.cumulativeBalance" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="总工时" prop="workHours" class="add-form-input">
                <el-input v-model="form.workHours" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实发金额(元)" prop="actualAmount" class="add-form-input">
                <el-input v-model="form.actualAmount" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="是否补发" prop="isBackPay" class="add-form-input">
                <el-select
                  v-model="form.isBackPay"
                  :disabled="!isAble"
                  placeholder="请选择"
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="dict in qgYesNo"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="补发月份" prop="backPayMonth" class="add-form-input">
                <el-input v-model="form.backPayMonth" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="第三方工资单" prop="thirdPayRollCode" class="add-form-input">
                <el-input v-model="form.thirdPayRollCode" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工资卡号" prop="wagesCardNumber" class="add-form-input">
                <el-input v-model="form.wagesCardNumber" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="银行代码" prop="wagesBankCode" class="add-form-input">
                <el-input v-model="form.wagesBankCode" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="开户行名称" prop="wagesBankName" class="add-form-input">
                <el-input v-model="form.wagesBankName" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="代发银行卡号" prop="undertakesCardNumber" class="add-form-input">
                <el-input v-model="form.undertakesCardNumber" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="代发银行代码" prop="undertakesBankCode" class="add-form-input">
                <el-input v-model="form.undertakesBankCode" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="代发银行名称" prop="undertakesBankName" class="add-form-input">
                <el-input v-model="form.undertakesBankName" :disabled="!isAble" placeholder="" />
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
  name: 'wageList',
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
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
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
      // 是否字典项
      qgYesNo: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      isAble: false,
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
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },

  created() {
    this.getList()
    // 题目分类
    this.getDicts('qg_yes_no').then(response => {
      this.qgYesNo = response.data
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
      this.queryParams.dataGeneration = 1
      request({
        url: 'build/bLabourWageDetail/list',
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
    formatterYesNo(row, column) {
      return this.selectDictLabel(this.qgYesNo, row.isBackPay)
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
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bLabourWageDetail/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '查看'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.form.batchId = this.batchId
          var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '附件提交异常'
            })
            return
          }
          if (this.form.id != undefined) {
            request({
              url: 'build/bSafetySafetyData/edit',
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
              url: 'build/bSafetySafetyData/add',
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
          url: 'build/bSafetySafetyData/deleteDetails/' + id,
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
