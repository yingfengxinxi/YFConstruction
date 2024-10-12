<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;overflow: hidden;">
        <el-form-item label="分包单位" class="special" prop="attendanceSubcontractor" >
          <el-select
            v-model="queryParams.attendanceSubcontractor"
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
        <el-form-item label="劳务工种" class="special" prop="attendanceWorkId" >
          <el-select
            v-model="queryParams.attendanceWorkId"
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

        <el-form-item label="班组名称" class="special" prop="attendanceTeamName" >
          <el-select
            v-model="queryParams.attendanceTeamName"
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
          <el-button type="primary" @click="bulkImportClick">考勤导入</el-button>
          <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

          <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-row >
        <!--主列表信息 -->
        <el-col :span="24">
          <el-card class="box-card">
            <el-table v-loading="loading" :data="dataList"  height="270px" @row-click="infocollectionSelect" @sort-change="sortChange">
              <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
              <el-table-column :formatter="attendanceSubcontractorFc" label="分包单位" sortable="custom" align="center" prop="attendanceSubcontractor" width="200" show-overflow-tooltip/>
              <el-table-column :formatter="attendanceWorkIdFc" label="劳务工种" sortable="custom" align="center" prop="attendanceWorkId" width="200" />
              <el-table-column :formatter="attendanceTeamNameFc" label="班组名称" sortable="custom" align="center" prop="attendanceTeamName" width="200" show-overflow-tooltip/>
              <el-table-column label="年月" align="center" prop="attendanceMonth" sortable="custom" />
              <el-table-column label="人数" align="center" prop="infocollectionNumber" sortable="custom" />
              <el-table-column label="总出勤" align="center" prop="attendanceTotal" sortable="custom" />
              <el-table-column label="总工日" align="center" prop="manDayTotal" sortable="custom" />

              <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
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
        <el-col :span="16">
          <el-card class="box-card">
            <el-table v-loading="loading" :data="infocollectionList" height="290px" @row-click="infocollectionSelectRecord" >
              <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
              <el-table-column label="人员姓名" align="center" prop="infocollectionName" />
              <el-table-column label="出勤（天）" align="center" prop="zs" width="120px"/>
              <el-table-column label="工日（天）" align="center" prop="zs" width="120px" />
              <el-table-column label="日期">
                <el-table-column v-for="(item,indez) in riList"
                                 :key="indez"
                                 :prop="item.prop"
                                 :label="item.label" align="center">
                  <template slot-scope="scope">
                    <div v-if="scope.row[item.prop] < 0" style="background-color:#cccccc;text-align: center;width:100%">
                       {{scope.row[item.prop]+2}}
                    </div>
                    <span v-else>
                      {{scope.row[item.prop]}}
                    </span>
                  </template>
                </el-table-column>
              </el-table-column>

            </el-table>
            <!-- <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>-->
          </el-card>
        </el-col>
        <!--详细列表信息 -->
        <!--记录列表信息 -->
        <el-col :span="8">
          <el-card class="box-card">
            <el-table v-loading="loading" :data="infocollectionRecordList" height="290px" >
              <el-table-column  label="序号"  align='center' type="index"  width="60"></el-table-column>
              <el-table-column label="打卡时间" align="center" prop="clockTime" />
              <el-table-column :formatter="dkfx" label="打卡方向" align="center" prop="clockDirection" />

              <el-table-column label="是否异常" align="center" prop="dataShowJudge">
                <template slot-scope="scope">
                  <span
                    v-if="scope.row.dataShowJudge == 1"
                    style="color:red;">{{ sfyc(scope.row.dataShowJudge) }}</span>
                  <span v-if="scope.row.dataShowJudge != 1">{{ sfyc(scope.row.dataShowJudge) }}</span>
                </template>
              </el-table-column>
            </el-table>
            <!--<div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>-->
          </el-card>
        </el-col>
        <!--记录列表信息 -->
      </el-row>
      <!-- 新增对话框 -->
      <el-dialog v-loading.fullscreen.lock="fullscreenLoading" :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="8">
              <el-form-item label="班组名称" prop="attendanceTeamName" class="add-form-input">
                <el-select
                  v-model="form.attendanceTeamName"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px"
                  @change="dataload">
                  <el-option
                    v-for="dict in BZList"
                    :key="dict.id"
                    :label="dict.teamName"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="分包单位" prop="attendanceSubcontractor" class="add-form-input">
                <el-select
                  v-model="form.attendanceSubcontractor"
                  :disabled="!isAble"
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
            </el-col>
            <el-col :span="8">
              <el-form-item label="劳务工种" prop="attendanceWorkId" class="add-form-input">
                <el-select
                  v-model="form.attendanceWorkId"
                  :disabled="!isAble"
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
            </el-col>
            <el-col :span="8">
              <el-form-item label="打卡次数" prop="attendanceClock" class="add-form-input">
                <el-select
                  v-model="form.attendanceClock"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in attendanceClock"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="编制日期" prop="attendanceMonth" class="add-form-input">
                <el-date-picker
                  v-model="form.attendanceMonth"
                  type="month"
                  value-format="yyyy-MM"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 新增对话框 -->
      <!-- 修改对话框 -->
      <el-dialog :title="titleUpdate" :visible.sync="openUpdate" width="1200px" append-to-body>
        <el-row >
          <!--修改详细列表信息 -->
          <el-col :span="16">
            <el-card class="box-card">
              <el-table v-loading="loading" :data="infocollectionUpdateList" height="500px" @row-click="infocollectionSelectUpdateRecord" >
                <el-table-column label="人员姓名" align="center" prop="infocollectionName" />
                <el-table-column label="出勤（天）" align="center" prop="zs" width="120px"/>
                <el-table-column label="工日（天）" align="center" prop="zs" width="120px" />
                <el-table-column label="日期">
                  <el-table-column v-for="(item,indez) in riList"
                                   :key="indez"
                                   :prop="item.prop"
                                   :label="item.label" align="center">
                    <template slot-scope="scope">
                      <div v-if="scope.row[item.prop] < 0" >
                        {{scope.row[item.prop]=scope.row[item.prop]+2}}
                        <el-input size="small" v-model="scope.row[item.prop]"
                                  @input="changeInput1(scope.row[item.prop],scope.row.infocollectionId,item.label)" />
                      </div>
                      <span v-else>
                        <el-input v-model="scope.row[item.prop]" size="small"
                                  @input="changeInput1(scope.row[item.prop],scope.row.infocollectionId,item.label)" />
                    </span>
                    </template>
                  </el-table-column>
                </el-table-column>
              </el-table>
              <!-- <div class="p-contianer">
                 <el-pagination
                   :current-page="currentPage"
                   :total="total"
                   class="p-bar"
                   background
                   layout="prev, pager, next, sizes, total, jumper"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"/>
               </div>-->
            </el-card>
          </el-col>
          <!--修改详细列表信息 -->
          <!--修改记录列表信息 -->
          <el-col :span="8">
            <el-card class="box-card">
              <el-table v-loading="loading" :data="infocollectionRecordUpdateList" height="500px" >
                <el-table-column label="打卡时间" align="center" prop="clockTime" />
                <el-table-column :formatter="dkfx" label="打卡方向" align="center" prop="clockDirection" />
                <el-table-column :formatter="sfyc" label="是否异常" align="center" prop="dataShowJudge" >
                  <template slot-scope="scope">
                  <span v-if="scope.row.dataShowJudge == 1"
                          style="color:red;">{{ sfyc(scope.row.dataShowJudge) }}</span>
                    <span v-if="scope.row.dataShowJudge != 1">{{ sfyc(scope.row.dataShowJudge) }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <!--<div class="p-contianer">
                <el-pagination
                  :current-page="currentPage"
                  :total="total"
                  class="p-bar"
                  background
                  layout="prev, pager, next, sizes, total, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"/>
              </div>-->
            </el-card>
          </el-col>
          <!--修改记录列表信息 -->
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFormUpdate">确 定</el-button>
          <el-button @click="cancelUpdate">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 修改对话框 -->
    </div>

    <!-- 批量导入 -->
    <bulk-import-user
      :show="bulkImportShow"
      :param="param"
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

import BulkImportUser from '@/components/importModel'
//	import vPinyin from './vuepy.js';

export default {
  name: 'attendance',
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
      // 遮罩层
      loading: true,
      // table高度控制
      tableHeight: document.documentElement.clientHeight - 260,
      // 选中数组
      ids: [],
      // 分包商数据信息
      FBSList: [],
      // 劳务工种数据信息
      worktypeList: [],
	  workTypeOptions: [],
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
      riList:[],
      // 人员详情表格数据
      infocollectionRecordList: [],
      // 人员修改详情表格数据
      infocollectionUpdateList: [],
      // 人员修改详情表格数据
      infocollectionRecordUpdateList: [],
      // 月份数据
      attendanceMonth: '',
      // 主数据id暂存
      attendanceids: '',
      // 转圈提示
      fullscreenLoading: false,
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示修改的弹出层
      openUpdate: false,
      // 修改弹出层标题
      titleUpdate: '',
      // 人员修改详情记录数据
      infocollectionUpdateData: [],
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
      // 修改表单参数
      formUpdate: {},
      // 表单校验
      rules: {
        attendanceTeamName: [
          { required: true, message: '请选择打卡班组', trigger: 'blur' }
        ],
        attendanceClock: [
          { required: true, message: '请选择打卡次数', trigger: 'blur' }
        ],
        attendanceMonth: [
          { required: true, message: '请选择月份', trigger: 'blur' }
        ]

      },
      attendanceClock: [{
        value: '2',
        label: '2'
      }, {
        value: '4',
        label: '4'
      }],
      isAble: false,
      param:{}//导入的模板使用参数
    }
  },

  created() {
    for(var i=0;i<31;i++){
      var j = i+1
      var label = (j<10?'0'+j:j)
      this.riList.push({prop:"ri"+label,label:""+label})
    }
    this.getList()
    //this.getWorktypeList()
	// 工种字典
	this.getDicts('b_labour_work_type').then(response => {
	  this.workTypeOptions = response.data
	})
    this.getListByFBS()
    this.getListByBZ()
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
        url: 'build/bLabourAttendance/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.dataList = response.data.list
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
      if (this.sjtbDate != null&&this.sjtbDate != '') {
        console.log(this.sjtbDate)
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
      this.currentPage = 1
      this.pageSize = 10
      this.infocollectionList = []
      this.infocollectionRecordList = []
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
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.attendanceMonth = row.attendanceMonth
      this.attendanceids = row.id
      request({
        url: 'build/bLabourAttendanceDetail/selectByPId/' + row.id,
        method: 'get'
      }).then(response => {
        this.infocollectionUpdateList = response.data
        this.openUpdate = true
        this.titleUpdate = '修改'
        this.infocollectionUpdateData = []
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.fullscreenLoading = true
      this.$refs['form'].validate(valid => {
        if (valid) {
          request({
            url: 'build/bLabourAttendance/validation',
            method: 'post',
            data: this.form,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(response2 => {
            if (response2.data == 1) {
              request({
                url: 'build/bLabourAttendance/add',
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
                  this.fullscreenLoading = false
                }
              })
            } else {
              this.fullscreenLoading = false
              if ( response2.data == 4) {
                this.$message({
                  message: '该班组所选月份已存在数据，不可重复添加。',
                  type: 'warning'
                })
              }
              if ( response2.data == 5) {
                this.$message({
                  message: '该班组下不存任何人员数据。',
                  type: 'warning'
                })
              }
              if ( response2.data == 6) {
                this.$message({
                  message: '班组成员无相应打卡记录，请先导入考勤打卡记录。',
                  type: 'warning'
                })
              }

            }
          })
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
          url: 'build/bLabourAttendance/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.infocollectionList = []
        this.infocollectionRecordList = []
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
      var value = row.attendanceSubcontractor
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
      var value = row.attendanceWorkId
      var actions = []

      Object.keys(datas).some((key) => {
        /* if (datas[key].id == ('' + value)) {
          actions.push(datas[key].workType)
          return true
        } */
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
      var value = row.attendanceTeamName
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
        than.form.attendanceWorkId = response.data.workTypeId
        than.form.attendanceSubcontractor = response.data.subcontractorId
        this.$forceUpdate()
      })
    },
    infocollectionSelect(row) {
      this.attendanceMonth = row.attendanceMonth
      request({
        url: 'build/bLabourAttendanceDetail/selectByPId/' + row.id,
        method: 'get'
      }).then(response => {
        this.infocollectionList = response.data
        this.infocollectionRecordList = []
      })
    },
    infocollectionSelectRecord(row) {
      request({
        url: 'build/bLabourAttendanceRecord/selectByMonth/' + row.infocollectionId + '/' + this.attendanceMonth + '/' + row.infocollectionName,
        method: 'get'
      }).then(response => {
        this.infocollectionRecordList = response.data
      })
    },
    infocollectionSelectUpdateRecord(row) {
      request({
        url: 'build/bLabourAttendanceRecord/selectByMonth/' + row.infocollectionId + '/' + this.attendanceMonth + '/' + row.infocollectionName,
        method: 'get'
      }).then(response => {
        this.infocollectionRecordUpdateList = response.data
      })
    },
    changeInput1(value, infocollectionId, date) {
      if (value == 0 || value == 1) {

      } else {
        alert('请填写0或1')
        return
      }
      var ids = infocollectionId + '-' + date
      const idarr = this.infocollectionUpdateData.map(item => {
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
        this.infocollectionUpdateData.push({
          id: ids,
          value: value
        })
      } else {
        const num = idarr.indexOf(ids)
        const arr = this.infocollectionUpdateData
        arr[num]['value'] = value
        this.infocollectionUpdateData = arr
      }
    },
    submitFormUpdate() {
      this.formUpdate.createBy = JSON.stringify(this.infocollectionUpdateData)
      this.formUpdate.attendanceMonth = this.attendanceMonth // 主数据月份
      this.formUpdate.id = this.attendanceids // 主数据id
      request({
        url: 'build/bLabourAttendance/edit',
        method: 'post',
        data: this.formUpdate,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        if (response.code === 0) {
          this.msgSuccess('修改成功')
          this.openUpdate = false
          this.getList()
          this.infocollectionList = []
          this.infocollectionRecordList = []
        }
      })
    },
    // 取消按钮
    cancelUpdate() {
      this.openUpdate = false
      this.infocollectionUpdateData = []
    },
    /**
     * 批量导入
     */
    bulkImportClick() {
      let lists = [
        "姓名(*)",
        "身份证号(*)",
        "考勤卡号(*)",
        "打卡方向(*)",
        "打卡时间(*)",
        "体温",
        "酒精含量",
        "血压",
        "血氧",
        "刷卡近照",
        "通道的名称",
        "通行方式",
        "WGS84经度",
        "WGS84纬度"
      ]
      //示例数据
      let dataLists = [
        "王积国",
        "372926197906070516",
        "111",
        "出场",
        "2021-01-10 15:47:14",
        "37°",
        "133mg/100mL",
        "1mmHg",
        "97%",
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAU4AAAE6CAYAAAB54Lu+AAAgAElEQVR4Xu2dd3xVxbbHVwokhB6KFEUIHRUhEPrFd22gwqVjQhp6EaSDVFGQKxelSREsD1RKCgmBICA1gCC96b1e27uKEiAhSJEOAZK8z5qTISeYnJnZ+5x99j5nzefDhz8ys2f2mrW/Z83es37jk52dnQdOKNevX3fCVegSZAGyAFnA9RYoXbq0rk58nAXOkiVL6hoINSYLkAXIAkZZ4Pbt27q6cho4dY2CGpMFyAJkAQtZgMBpocmioZIFyALmsACB0xzzQKMgC5AFLGQBAqeFJouGShYgC5jDAgROc8wDjYIsQBawkAUInBaaLBoqWYAsYA4LEDjNMQ80CrIAWcBCFiBwWmiyaKhkAbKAOSxA4DTHPNAoyAJkAQtZgMBpocmioZIFyALmsIDTwEkpl+aYUBoFWYAsILaAaVIuCZziyaIaZAGygDksQOA0xzzQKMgCZAELWYDAaaHJoqGSBcgC5rAAgdMc80CjIAuQBSxkAQKnhSaLhkoWIAuYwwIETnPMA42CLEAWsJAFLAXOO3fuQE5ODvj4+FjIxNYeal5eHpQoUQL8/PxcfiO5ublw9+5dwP89ZY7Rfr6+vqC6awTbZWdne4wdXO48TugAbY5+jv4uKpYBJwLTiIdXZDD6u2ssgE7rKbAsykIq/uvptnCNBznvqjJzZQlwYqSJvwIfffQRbNy4CYKDg1nkScW1FsBI6ebNmzB69Cho3769yzrDCBP7On78OCxcuAgyMk5DYGAg5OY65RxAl41bdGFfXx+4evUaNGvWDKZOfYtF0/7+/qJm7O+XLl2Cl156GcqWLUu+LmUxfZUwKLt48SK88MLzMHjwYODMKe6qlgDnrVu32IPUpUtX2LjxC30WotbKFliyZAkMGDBAuZ1sA/4Lv3fvPnjyySfhzh19B2HJ9mtUvZCQEPajgEvvgIAAh93yaDMjIwMefPBBo4",
        "东北部通道1",
        "1",
        "116.397128",
        "39.916527"
      ]

      this.param = {
        title:"考勤导入模板(*)为必填项 身份证号或考勤号必填一个",
        excelTitle:"考勤导入模板",
        colNames:lists,
        sampleData:dataLists,
        url:'build/bLabourAttendanceRecord/excelImport'
      }
      this.bulkImportShow = true
    },
    /**
     * 打卡方向
     */
    dkfx(row) {
      if (row.clockDirection == '0') {
        return '进场'
      } else {
        return '出场'
      }
    },
    /**
     * 是否异常
     */
    sfyc(row) {
      if (row == '1') {
        return '异常'
      } else {
        return '正常'
      }
    },
    /**
     * 异常颜色展示
     */
    tableRowClassName({ row, rowIndex }) {
      if (row.dataShowJudge == '0') {
      } else {
        return 'warning-row'
      }
      return ''
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
  /deep/.el-table .warning-row {
    background:red!important;
  }
</style>
