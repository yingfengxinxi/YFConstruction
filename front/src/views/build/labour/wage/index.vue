<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;overflow: hidden;">
        <el-form-item label="分包单位" class="special" prop="wageSubcontractor" >
          <el-select
            v-model="queryParams.wageSubcontractor"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in FBSList"
              :key="dict.id"
              :label="dict.realName"
              :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="劳务工种" class="special" prop="wageWorkId" >
          <el-select
            v-model="queryParams.wageWorkId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in workTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>

        <el-form-item label="班组名称" class="special" prop="wageTeamName" >
          <el-select
            v-model="queryParams.wageTeamName"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in BZList"
              :key="dict.id"
              :label="dict.teamName"
              :value="dict.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="年月查询" prop="safetyDataName" >
          <el-date-picker
            v-model="sjtbDate"
            :picker-options="pickerOptions"
            class="timecom"
            type="monthrange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"/>
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
        <div class="table-head-container">
      <flexbox
        v-if="selectionList.length > 0"
        class="selection-bar">
        <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
        <flexbox class="selection-items-box">
          <el-button
            v-for="(item, index) in selectionHandleItems"
            :icon="item.icon | wkIconPre"
            :key="index"
            type="primary"
            @click.native="handleTypeDrop(item.type)">{{ item.name }}
          </el-button>
        </flexbox>
      </flexbox>
    </div>
      <el-row >
        <!--主列表信息 -->
        <el-col :span="24">
        	<el-card class="box-card">
            <el-table v-loading="loading" :data="dataList" height="280px" @row-click="infocollectionSelect" @selection-change="handleSelectionChange" @sort-change="sortChange">
              <el-table-column show-overflow-tooltip type="selection" align="center" width="55" />
              <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
              <el-table-column :formatter="attendanceSubcontractorFc" label="分包单位" align="center" prop="wageSubcontractor" width="200px" show-overflow-tooltip sortable="custom"/>
              <el-table-column :formatter="attendanceWorkIdFc" label="劳务工种" align="center" prop="wageWorkId" show-overflow-tooltip sortable="custom"  width="120px"/>
              <el-table-column label="班组长名称" align="center" prop="updateBy" sortable="custom"  width="140px"/>
              <el-table-column label="年月" align="center" prop="wageMonth" width="100px" sortable="custom"/>
              <el-table-column label="人数" align="center" prop="infocollectionNumber"  sortable="custom" width="100px"/>
              <el-table-column label="总工日" align="center" prop="wageManDays"  sortable="custom" width="100px"/>
              <el-table-column label="总出勤" align="center" prop="wageQuantities"  sortable="custom" width="100px"/>
              <el-table-column label="应发工资(元)" align="center" prop="wageShould" sortable="custom" width="150px" />
              <el-table-column label="预支工资(元)" align="center" prop="wageDvance" sortable="custom" width="150px" />
              <el-table-column label="累计剩余(元)" align="center" prop="wageaCumulativeBalance" sortable="custom" />
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150px">
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
                :total="total"
                class="p-bar"
                background
                layout="prev, pager, next, sizes, total, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"/>
            </div>
            </el-card>
        </el-col>
        <!--主列表信息 -->
        <!--详细列表信息 -->
        <el-col :span="24" style="height: 375px">
        	<el-card class="box-card">
          <el-table v-loading="loading" :data="infocollectionList" ref="table-01" height="375px" :summary-method="getSummaries" show-summary >
            <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
            <el-table-column label="人员姓名" align="center" prop="infocollectionName" />
            <el-table-column label="点工(日工)" align="center">
              <el-table-column label="日单价" align="center" prop="dayPrice" width="120px" />
              <el-table-column label="工日（天）" align="center" prop="manDays" width="120px" />
            </el-table-column>
            <el-table-column label="包工" align="center">
              <el-table-column label="工程量单价" align="center" prop="quantitiesPrice" width="120px" />
              <el-table-column label="工程量" align="center" prop="quantities" width="120px" />
            </el-table-column>
            <el-table-column :formatter="formatterTime" label="发放日期" align="center" prop="issueDate" />
            <el-table-column label="应发工资(元)" align="center" prop="wageDetailShould" />
            <el-table-column label="预支工资(元)" align="center" prop="wageDetailAdvance" />
            <el-table-column label="本月余额(元)" align="center" prop="monthBalance" />
            <el-table-column label="累计剩余(元)" align="center" prop="cumulativeBalance" />
            <el-table-column label="总工时" align="center" prop="workHours" />
            <el-table-column label="实发金额(元)" align="center" prop="actualAmount" />
            <el-table-column :formatter="formatterYesNo" label="是否补发" align="center" prop="isBackPay" />
            <el-table-column label="补发月份" align="center" prop="backPayMonth" />
            <el-table-column label="第三方工资单编号" align="center" prop="thirdPayRollCode" />
          </el-table>
          </el-card>
        </el-col>
        <!--详细列表信息 -->
      </el-row>
      <!-- 新增对话框 -->
      <el-dialog v-loading.fullscreen.lock="fullscreenLoading" :title="title" :visible.sync="open" width="1850px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="8">
              <el-form-item label="班组名称" prop="wageTeamName" class="add-form-input">
                <el-select
                  v-model="form.wageTeamName"
                  :disabled="!isAdd"
                  placeholder="请选择"
                  clearable
                  size="small"
                  @change="dataload">
                  <el-option
                    v-for="dict in BZList"
                    :key="dict.id"
                    :label="dict.teamName"
                    :value="dict.id+''" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="分包单位" prop="wageSubcontractor" class="add-form-input">
                <el-select
                  v-model="form.wageSubcontractor"
                  :disabled="!isAble"
                  placeholder="请选择"
                  clearable
                  size="small">
                  <el-option
                    v-for="dict in FBSList"
                    :key="dict.id"
                    :label="dict.realName"
                    :value="dict.id +''" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="劳务工种" prop="wageWorkId" class="add-form-input">
                <el-select
                  v-model="form.wageWorkId"
                  :disabled="!isAble"
                  placeholder="请选择"
                  clearable
                  size="small">
                  <el-option
                    v-for="dict in workTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue+''" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="人数" prop="infocollectionNumber" class="add-form-input">
                <el-input v-model="form.infocollectionNumber" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="总工日" prop="wageManDays" class="add-form-input">
                <el-input v-model="form.wageManDays" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="总工程量" prop="wageQuantities" class="add-form-input">
                <el-input v-model="form.wageQuantities" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="应发工资(元)" prop="wageShould" class="add-form-input">
                <el-input v-model="form.wageShould" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预支工资(元)" prop="wageDvance" class="add-form-input">
                <el-input v-model="form.wageDvance" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="累计剩余(元)" prop="wageaCumulativeBalance" class="add-form-input">
                <el-input v-model="form.wageaCumulativeBalance" :disabled="!isAble" placeholder="" />
              </el-form-item>
            </el-col>


            <el-col :span="8">
              <el-form-item label="年月" prop="wageMonth" class="add-form-input">
                <el-date-picker
                  v-model="form.wageMonth"
                  :disabled="!isAdd"
                  type="month"
                  value-format="yyyy-MM"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发放日期" prop="wageDate" class="add-form-input">
                <el-date-picker
                  v-model="form.wageDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="8" style="text-align: center;">
              <el-button
                class="xr-btn--orange"
                :disabled="!isAdd"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="AddWage" >生成</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="infocollectionListAdd" height="240px" >
            <el-table-column label="人员姓名" align="center" prop="infocollectionName" />
            <el-table-column label="点工(日工)" align="center">
              <el-table-column label="日单价" align="center" prop="dayPrice">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.dayPrice" size="small" @input="changeInput1(scope.row.dayPrice,scope.row.id,scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="工日（天）" align="center" prop="manDays">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.manDays" size="small" @input="changeInput1(scope.row.manDays,scope.row.id,scope.row)" />
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="包工" align="center">
              <el-table-column label="工程量单价" align="center" prop="quantitiesPrice">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.quantitiesPrice" size="small" @input="changeInput1(scope.row.quantitiesPrice,scope.row.id,scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="工程量" align="center" prop="quantities">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.quantities" size="small" @input="changeInput1(scope.row.quantities,scope.row.id,scope.row)" />
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column :formatter="formatterTime" label="发放日期" align="center" prop="issueDate" />
            <el-table-column label="应发工资(元)" align="center" prop="wageDetailShould" />
            <el-table-column label="预支工资(元)" align="center" prop="wageDetailAdvance">
              <template slot-scope="scope">
                <el-input v-model="scope.row.wageDetailAdvance" size="small" @input="changeInput1(scope.row.wageDetailAdvance,scope.row.id,scope.row)" />
              </template>
            </el-table-column>
            <el-table-column label="本月余额(元)" align="center" prop="monthBalance" />
            <el-table-column label="累计剩余(元)" align="center" prop="cumulativeBalance" />
            <el-table-column label="总工时" align="center" prop="workHours" />
            <el-table-column label="实发金额(元)" align="center" prop="actualAmount">
              <template slot-scope="scope">
                <el-input v-model="scope.row.actualAmount" size="small" @input="changeInput1(scope.row.actualAmount,scope.row.id,scope.row)"/>
              </template>
            </el-table-column>
            <el-table-column label="是否补发" align="center" prop="isBackPay">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.isBackPay"
                  placeholder="请选择"
                  clearable
                  size="small"
                  @change="changeInput1(scope.row.wageDetailAdvance,scope.row.id,scope.row)"
                >
                  <el-option
                    v-for="dict in qgYesNo"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="补发月份" align="center" prop="backPayMonth">
              <template slot-scope="scope">
                <el-date-picker
                  v-model="scope.row.backPayMonth"
                  type="month"
                  value-format="yyyy-MM"
                  placeholder="选择日期"
                  size="small"
                  @change="changeInput1(scope.row.wageDetailAdvance,scope.row.id,scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="第三方工资单编号" align="center" prop="thirdPayRollCode">
              <template slot-scope="scope">
                <el-input v-model="scope.row.thirdPayRollCode" size="small" @input="changeInput1(scope.row.wageDetailAdvance,scope.row.id,scope.row)"/>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 新增对话框 -->

    </div>

    <!-- 批量导入 -->
    <bulk-import-user
      :show="bulkImportShow"
      @close="bulkImportShow=false"
    />
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
import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import BulkImportUser from './components/BulkImportUser'
import { downloadExcelWithResData } from '@/utils/index'//下载导出文件
//	import vPinyin from './vuepy.js';

export default {
  name: 'wage',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    BulkImportUser,
    FilesList
  },
  data() {
    return {
       /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
      /** 选中时操作 */
        selectionHandleItems: [
          {
            name: "导出选中",
            type: "export",
            icon: "export"
          }
        ],
      // 遮罩层
      loading: true,
      // table高度控制
      // tableHeight: document.documentElement.clientHeight - 260,
      // 选中数组
      ids: [],
      // 分包商数据信息
      FBSList: [],
      // 劳务工种数据信息
      worktypeList: [],
	  workTypeOptions: [],
      // 是否字典项
      qgYesNo: [],
      // 班组数据信息
      BZList: [],
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
      // 人员信息表格数据
      infocollectionList: [],
      // 人员信息新增表格数据
      infocollectionListAdd: [],
      // 月份数据
      wageMonth: '',
      // 主数据id暂存
      bLabourWageids: '',
      // 转圈提示
      fullscreenLoading: false,
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 批量导入
      bulkImportShow: false,
      pickerOptions: {
        shortcuts: [{
          text: '本月',
          onClick(picker) {
            picker.$emit('pick', [new Date(), new Date()])
          }
        }, {
          text: '今年至今',
          onClick(picker) {
            const end = new Date()
            const start = new Date(new Date().getFullYear(), 0)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近六个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setMonth(start.getMonth() - 6)
            picker.$emit('pick', [start, end])
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
        wageTeamName: [
          { required: true, message: '请选择打卡班组', trigger: 'blur' }
        ],
        wageDate: [
          { required: true, message: '请选择发放日期', trigger: 'blur' }
        ],
        wageMonth: [
          { required: true, message: '请选择月份', trigger: 'blur' }
        ]

      },
      isAble: false,
      isAdd: true,
      // 修改详情记录数据
      UpdateData: []
    }
  },

  created() {
    this.getList()
    this.getListByFBS()
    this.getListByBZ()
    // 题目分类
    this.getDicts('qg_yes_no').then(response => {
      this.qgYesNo = response.data
    })
	// 工种
    //this.getWorktypeList()
	// 工种字典
	this.getDicts('b_labour_work_type').then(response => {
		this.workTypeOptions = response.data
	})
  },
  updated () {
　　this.$nextTick(() => {
　　　　this.$refs['table-01'].doLayout();
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

      request({
        url: 'build/bLabourWage/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        console.log(response.data)
        this.dataList = response.data.list
        if (response.data.list.length > 0) {
          request({
            url: 'build/bLabourWageDetail/selectByPId/' + response.data.list[0].id,
            method: 'get'
          }).then(response2 => {
            this.infocollectionList = response2.data
          })
        }else{
          this.infocollectionList =[]
        }
        if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
          this.currentPage = this.currentPage - 1
          this.getList()
        } else {
          this.total = response.data.totalRow
          this.loading = false
        }
      }).catch(() => {
        this.loading = false
      })
    },
    formatterTime(row, column) {
      const data = row[column.property]
      return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },
    formatterYesNo(row, column) {
      return this.selectDictLabel(this.qgYesNo, row.isBackPay)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
      this.getList()
    },
    // 表单重置
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.sjtbDate != null &&this.sjtbDate != '') {
        var date = new Date(this.sjtbDate[0])
        var y = date.getFullYear()
        var m = date.getMonth() + 1
        m = m < 10 ? ('0' + m) : m
        const time = y + '-' + m
        this.queryParams.createBy = time
        var date = new Date(this.sjtbDate[1])
        var y = date.getFullYear()
        var m = date.getMonth() + 1
        m = m < 10 ? ('0' + m) : m
        const time2 = y + '-' + m
        this.queryParams.updateBy = time2
      }else{
      	 this.queryParams.createBy=''
      	this.queryParams.updateBy=''
      }
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.sjtbDate = ''
      // this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增'
      this.batchId = guid()
      this.infocollectionListAdd = []
      this.UpdateData = []
      this.isAdd = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.wageMonth = row.wageMonth
      this.bLabourWageids = row.id
      this.batchId = row.id
      this.isAdd = false

      request({
        url: 'build/bLabourWage/selectById/' + row.id,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.$forceUpdate()
        request({
          url: 'build/bLabourWageDetail/selectByPId/' + row.id,
          method: 'get'
        }).then(response2 => {
          this.infocollectionListAdd = response2.data
          this.open = true
          this.title = '修改'
          this.UpdateData = []
        })
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.fullscreenLoading = true
      this.form.updateBy = JSON.stringify(this.UpdateData)
      request({
        url: 'build/bLabourWage/edit',
        method: 'post',
        data: this.form,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        if (response.code === 0) {
          this.msgSuccess('保存成功')
          this.open = false
          this.fullscreenLoading = false
          this.getList()
          this.infocollectionList = []
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
          url: 'build/bLabourWage/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.infocollectionList = []
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
    /** 查询所工种数据列表 */
    getWorktypeList() {
      getWTList().then(response => {
        this.worktypeList = response.data
        console.log(response.data)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询所有分包商id和name */
    getListByFBS() {
      getListByFBS().then(response => {
        this.FBSList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询所有班组名称 */
    getListByBZ() {
      getListByBZ().then(response => {
        this.BZList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 分包商数据
    attendanceSubcontractorFc(row, column) {
      var datas = this.FBSList
      var value = row.wageSubcontractor
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].realName)
          return true
        }
      })
      return actions.join('')
    },
    // 劳务工种数据
    attendanceWorkIdFc(row, column) {
      //var datas = this.worktypeList
	  var datas = this.workTypeOptions
      var value = row.wageWorkId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].dictValue == ('' + value)) {
          actions.push(datas[key].dictLabel)
          return true
        }
      })
      return actions.join('')
    },
    // 班组数据信息
    attendanceTeamNameFc(row, column) {
      var datas = this.BZList
      var value = row.wageTeamName
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].teamName)
          return true
        }
      })
      return actions.join('')
    },
    dataload(value) {
      var than = this
      request({
        url: 'build/labourTeam/queryById/' + value,
        method: 'post'
      }).then(response => {
        console.log(response.data)
        than.form.wageWorkId = response.data.workTypeId + ''
        than.form.wageSubcontractor = response.data.subcontractorId + ''
        this.$forceUpdate()
        console.log(this.form)
      })
    },
    infocollectionSelect(row) {
      this.wageMonth = row.wageMonth
      request({
        url: 'build/bLabourWageDetail/selectByPId/' + row.id,
        method: 'get'
      }).then(response => {
        console.log(response.data)
        this.infocollectionList = response.data
      })
    },
    changeInput1(value, ids, row) {
      var regPos = /^\d+(\.\d+)?$/ // 非负浮点数
      var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/ // 负浮点数
      var Editdata = {}
      if (regPos.test(value) || regNeg.test(value)) {
      } else {
        alert('请填写数字')
        return
      }

      var manDaysPrice = row.dayPrice * row.manDays
      var quantitiesPrice = row.quantitiesPrice * row.quantities
      row.wageDetailShould = manDaysPrice + quantitiesPrice
      row.monthBalance = manDaysPrice + quantitiesPrice - row.wageDetailAdvance
      Editdata = {
        dayPrice: row.dayPrice,
        manDays: row.manDays,
        quantitiesPrice: row.quantitiesPrice,
        quantities: row.quantities,
        wageDetailShould: row.wageDetailShould,
        wageDetailAdvance: row.wageDetailAdvance,
        monthBalance: row.monthBalance,
        actualAmount: row.actualAmount,
        isBackPay: row.isBackPay,
        backPayMonth: row.backPayMonth,
        thirdPayRollCode: row.thirdPayRollCode
      }
      const idarr = this.UpdateData.map(item => {
        return item.id
      })
      let flag = true
      // 验证该数据是否已存在，已存在后应将对应的数据替换而不是新增一个
      if (idarr.indexOf(ids) == -1) {
        flag = false
      } else {
        flag = true
      }
      if (idarr.length == 0 || !flag) {
        this.UpdateData.push({
          id: ids,
          value: Editdata
        })
      } else {
        const num = idarr.indexOf(ids)
        const arr = this.UpdateData
        arr[num]['value'] = Editdata
        this.UpdateData = arr
      }
      console.log(this.UpdateData)
    },
    /**
     * 批量导入
     */
    bulkImportClick() {
      this.bulkImportShow = true
    },
    // 新增数据
    AddWage() {

      this.$refs['form'].validate(valid => {
        if (valid) {
          this.fullscreenLoading = true
          this.form.id = this.batchId
          request({
            url: 'build/bLabourWage/validation',
            method: 'post',
            data: this.form,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(response2 => {
            console.log('ceshi:' + response2.data)
            if (response2.data == 1) {
              request({
                url: 'build/bLabourWage/add',
                method: 'post',
                data: this.form,
                headers: {
                  'Content-Type': 'application/json;charset=UTF-8'
                }
              }).then(response => {
                if (response.code === 0) {
                  // 查询子数据表的生成对应列表数据
                  request({
                    url: 'build/bLabourWageDetail/selectByPId/' + this.batchId,
                    method: 'get'
                  }).then(response => {
                    this.msgSuccess('生成成功')
                    this.fullscreenLoading = false
                    this.infocollectionListAdd = response.data
                    this.isAdd = false
                  })
                }
              })
            } else {
              this.fullscreenLoading = false
              if (response2.data == 4) {
                this.$message({
                  message: '该班组所选月份已存在数据，不可重复添加！',
                  type: 'warning'
                })
              }
              if (response2.data == 5) {
                this.$message({
                  message: '该班组下不存任何人员数据！',
                  type: 'warning'
                })
              }
              if (response2.data == 6) {
                this.$message({
                  message: '该班组下不存在考勤数据！',
                  type: 'warning'
                })
              }
            }
          })
        }
      })
    },
    /** 数据合计行 */
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (index === 1 || index === 3 || index === 13 || index === 14 || index === 15) {
          sums[index] = ''
        } else {
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
          } else {
            sums[index] = ''
          }
        }
      })
      return sums
    },
      //勾选行记录
     handleSelectionChange(val) {
        this.selectionList = val // 勾选的行
        // this.updateTableHeight();
      },
        /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 320
        this.tableHeight = offsetHei - removeHeight
      },
       /**
       *更多操作点击
       */
      handleTypeDrop(command) {
        // console.log(command)
        if (command == 'export') {//导出
          this.$confirm("确定导出选中的数据吗？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              this.loading = true;
              const ids = this.selectionList.map(item => item['id'])
              let id_str=ids.join(',');
              console.log("获取id",id_str);
              this.queryParams={};
               console.log("queryParams清除",this.queryParams);
              this.queryParams.ids=id_str
               console.log("queryParams参数",this.queryParams);
                  request({
                      url: 'build/bLabourWage/export',
                      method: 'post',
                      data: this.queryParams,
                      headers: {
                      'Content-Type': 'application/json;charset=UTF-8'
                      },
                      responseType: 'blob'
                  }).then(res => {
                    console.log("已成功返回",res);
                      downloadExcelWithResData(res);
                      this.loading = false;
                  }).catch(() => {
                     this.loading = false;
                  })
            })
            .catch(() => {
            })
        }
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
  /deep/.el-table .warning-row {
    background:red!important;
  }
  /deep/.el-form .el-input{
    width: 180px;
  }
  /deep/.el-form .el-select{
    width: 180px;
  }
  /deep/.el-form .el-textarea{
    width: 180px;
  }
  .cell .el-input.el-input--small{
    width:100px
  }

  /**选中数据行时样式 */
  .table-head-container {
    //border-bottom: 1px solid #e6e6e6;
    border-top: 1px solid #e6e6e6;
    background-color: white;
  }
    /** 勾选操作 */
  .selection-bar {
    font-size: 12px;
    height: 50px;
    padding: 0 20px;
    color: #777;

    .selected—title {
      flex-shrink: 0;
      padding-right: 20px;
      color: #333;
      .selected—count {
        color: $xr-color-primary;
      }
    }
  }
  .selection-items-box {
    overflow-x: auto;
    overflow-y: hidden;
    padding: 0 15px;

    .el-button {
      color: #666;
      background-color: #f9f9f9;
      border-color: #e5e5e5;
      font-size: 12px;
      height: 28px;
      border-radius: 4px;
      /deep/ i {
        font-size: 12px;
        margin-right: 5px;
      }
    }

    .el-button--primary:hover {
      background: $xr-color-primary;
      border-color: $xr-color-primary;
      color: #ffffff;
    }

    .el-button + .el-button {
      margin-left: 15px;
    }
  }
</style>
