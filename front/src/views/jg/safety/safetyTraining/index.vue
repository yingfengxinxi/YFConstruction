<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="项目名称" prop="projectId">
          <el-select
            v-model="queryParams.projectId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="item in projectInfoList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="培训计划名称" prop="trainingPlanId">
          <el-select v-model="queryParams.trainingPlanId" placeholder="请选择" clearable size="small" style="width: 160px">
             <el-option v-for="dict in trainPlanNames" :key="dict.id" :label="dict.trainPlanName" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="培训类型" prop="trainingType">
          <el-select v-model="queryParams.trainingType" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in trainingType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>

        <el-form-item label="授课人" prop="trainingTeaching">
          <el-input v-model="queryParams.trainingTeaching" placeholder="请输入授课人" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="培训时间" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="培训时间起" end-placeholder="培训时间止" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->
          <!--el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column  label="序号"  align='center' type="index"  width="60"/>
        <el-table-column label="项目名称" :formatter="projectNameZc" align="center" prop="projectId"  width="200px" show-overflow-tooltip sortable="custom"/>
        <el-table-column :formatter="trainingPlanIdFc" label="培训计划" align="center" prop="trainingPlanId" show-overflow-tooltip sortable="custom"/>
        <el-table-column :formatter="trainingTypeFc" label="培训类型" align="center" prop="trainingType" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训时间" align="center" :formatter="formatterTime" prop="trainingTime" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训地点" align="center" prop="trainingAddress" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训内容" align="center" prop="trainingContent" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="授课人" align="center" prop="trainingTeaching" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="课时" align="center" prop="trainingTeachingTime" show-overflow-tooltip sortable="custom"/>
        <!--<el-table-column label="授课地点" align="center" prop="trainingTeachingAddress" />-->
        <el-table-column label="负责人" align="center" prop="trainingPeople" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="创建时间" align="center" prop="createTime" :formatter="formatterTime" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">查看</el-button>
            <!--<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="培训计划" prop="trainingPlanId" class="add-form-input">
                <el-select v-model="form.trainingPlanId" @change="selectChanged" placeholder="请选择" :disabled="true" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in trainPlanNames" :key="dict.id" :label="dict.trainPlanName" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训类型" prop="trainingType" class="add-form-input">
                <el-select v-model="form.trainingType" placeholder="请选择" :disabled="true" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in trainingType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="培训时间" prop="trainingTime" class="add-form-input">
                <el-date-picker v-model="form.trainingTime" :disabled="true" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" style="width: 330px" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="授课人" prop="trainingTeaching" class="add-form-input">
                <el-input v-model="form.trainingTeaching" :disabled="true" placeholder="请输入" />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="培训地点" prop="trainingAddress" class="add-form-input">
                <el-input v-model="form.trainingAddress" :disabled="true" placeholder="请输入" />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="培训内容" prop="trainingContent">
                <el-input :rows="2" v-model="form.trainingContent" :disabled="true" type="textarea" placeholder="请输入选项集合" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="课时" prop="trainingTeachingTime" class="add-form-input">
                <el-input v-model.number="form.trainingTeachingTime" :disabled="true" type="number" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <!-- <el-form-item label="授课地点" prop="trainingTeachingAddress" class="add-form-input">
            <el-input v-model="form.trainingTeachingAddress" placeholder="请输入" />
          </el-form-item>-->
            <el-col :span="12">
              <el-form-item label="负责人" prop="trainingPeople" class="add-form-input">
                <el-input v-model="form.trainingPeople" :disabled="true" placeholder="请输入" />
              </el-form-item>
            </el-col>

          </el-row>
          <!--<el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAddMembers">添加</el-button>-->

          <el-table v-loading="loading" :data="membersList" height="240px">
            <el-table-column label="参加人员" align="center" prop="infocollectionName" />
            <el-table-column label="班组名" align="center" prop="infocollectionTeamName" :formatter="teamNameFormat2" />
<!--            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteMembers(scope.row)">删除</el-button>
              </template>
            </el-table-column>-->
          </el-table>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <!--<el-button type="primary" @click="submitForm">确 定</el-button>-->
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>

      <el-dialog :title="titleMembers" :visible.sync="membersOpen" width="700px" append-to-body>

        <el-form v-show="showSearch" ref="rosterQueryParams" class="topFormClass" :model="rosterQueryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">

          <el-form-item label="分包单位" class='special' prop="subcontractor">
            <el-select v-model="rosterQueryParams.subcontractor" placeholder="请选择" clearable size="small" style="width: 160px">
              <el-option v-for="dict in FBSList" :key="dict.realName" :label="dict.realName" :value="dict.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="劳务工种" class='special' prop="workTypeId">
            <el-select v-model="rosterQueryParams.workTypeId" placeholder="请选择" clearable size="small" style="width: 160px">
              <el-option v-for="dict in worktypeList" :key="dict.workType" :label="dict.workType" :value="dict.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="班组名称" class="special" prop="teamName">
            <el-select v-model="rosterQueryParams.teamName" placeholder="请选择" clearable size="small" style="width: 160px">
              <el-option v-for="dict in BZList" :key="dict.teamName" :label="dict.teamName" :value="dict.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="人员姓名" class="special" prop="name">
            <el-input v-model="rosterQueryParams.name" placeholder="请输入人员姓名" clearable size="small" style="width: 160px" @keyup.enter.native="rosterhandleQuery" />
          </el-form-item>
          <el-form-item label="身份证号" class="special" prop="idCard">
            <el-input v-model="rosterQueryParams.idCard" placeholder="请输入身份证号" clearable size="small" style="width: 160px" @keyup.enter.native="rosterhandleQuery" />
          </el-form-item>

          <el-form-item style="float: right">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="rosterhandleQuery">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="rosterList" ref="accountTable" height="250px">

          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column label="人员姓名" align="center" prop="name" />
          <!--          <el-table-column label="性别" align="center" prop="sex" :formatter="sexFormat"/>-->
          <el-table-column label="电话号码" align="center" prop="phone" />
          <el-table-column label="劳务工种" align="center" prop="workTypeId" :formatter="WTFormat" />
          <el-table-column label="班组名" align="center" prop="teamName" :formatter="teamNameFormat" />
        </el-table>
        <div class="p-contianer">
          <el-pagination :current-page="rostercurrentPage" :total="rostertotal" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="rosterHandleSizeChange" @current-change="rosterHandleCurrentChange" />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFormMembers">确 定</el-button>
          <el-button @click="cancelMembers">取 消</el-button>
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
import { guid } from '@/utils'
import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import { sortColumn } from '@/utils/index';
//	import vPinyin from './vuepy.js';
export default {
  name: 'safetyTraining',
  components: {
    // Reminder,
    XrHeader,
    SelectCol
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 280,
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
      rostercurrentPage: 1,
      pageSize: 10,
      rosterpageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      rostertotal: 0,
      // 表格数据
      dataList: [],
      // 项目信息列表
      projectInfoList: [],
      // 培训成员数据
      membersList: [],
      // 花名册成员数据
      rosterList: [],
      // 分包商数据信息
      FBSList: [],
      // 劳务工种数据信息
      worktypeList: [],
      // 班组数据信息
      BZList: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示应急班组成员弹出层
      membersOpen: false,
      // 应急班组成员弹出层标题
      titleMembers: '',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      sjtbDate: '',
      // 题目分类  字典表safety_training_type
      trainingType: [],
      //培训计划
      trainPlanNames: [],
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 花名册查询参数
      rosterQueryParams: {
        page: this.currentPage,
        limit: this.pageSize
      },
      // 表单参数
      form: {},
      //花名册人员
      formMembers: {},
      // 表单校验
      rules: {
        trainingPlanId: [
          { required: true, message: '请选择培训计划', trigger: 'blur' }
        ],
        trainingType: [
          { required: true, message: '请选择培训类型', trigger: 'blur' }
        ],
        trainingTime: [
          { required: true, message: '培训时间不能为空', trigger: 'blur' }
        ],
        trainingAddress: [
          { required: true, message: '培训地点不能为空', trigger: 'blur' }
        ],
        trainingContent: [
          { required: true, message: '培训内容不能为空', trigger: 'blur' }
        ],
        trainingTeaching: [
          { required: true, message: '授课人不能为空', trigger: 'blur' }
        ],
        trainingTeachingTime: [
          { required: true, message: '课时不能为空', trigger: 'blur' }
        ],
        trainingTeachingAddress: [
          { required: true, message: '授课地点不能为空', trigger: 'blur' }
        ],
        trainingPeople: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ]

      }
    }
  },
  watch: {
    $route (to, from) {
      request({
        url: 'build/safetyEducationPlan/list',
        method: 'post',
        data: {},
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.trainPlanNames = response.data.list
      })
    }
  },
  created () {
    this.getList()
    this.getWorktypeList()
    this.getListByFBS()
    this.getListByBZ()
    this.getProjectInfoList()
    // 题目分类
    this.getDicts('safety_training_type').then(response => {
      this.trainingType = response.data
    })
    request({
      url: 'build/safetyEducationPlan/list',
      method: 'post',
      data: {},
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    }).then(response => {
      this.trainPlanNames = response.data.list

    })
  },
  methods: {
    selectChanged (val) {
      let that = this;
      this.trainPlanNames.forEach(function (obj, index) {
        if (val === obj.id) {
          that.$set(that.form, "trainingType", obj.trainType);
        }
      })

    },

    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bSafetySafetyTraining/list',
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
    /** 查询花名册数据列表 */
    getRosterList () {
      this.loading = true
      this.rosterQueryParams.page = this.rostercurrentPage
      this.rosterQueryParams.limit = this.rosterpageSize
      this.rosterQueryParams.note = this.batchId

      request({
        url: 'build/labourInfocollection/listSafetyTraining',
        method: 'post',
        data: this.rosterQueryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.rosterList = response.data.list
        this.rostertotal = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 题目分类
    trainingTypeFc (row, column) {
      return this.selectDictLabel(this.trainingType, row.trainingType)
    },
    // 培训计划
    trainingPlanIdFc (row, column) {
      return this.CustomFormat(this.trainPlanNames, row.trainingPlanId)
    },
    CustomFormat (datas, value) {
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].id == "" + value) {
          actions.push(datas[key].trainPlanName);
          return true;
        }
      });
      return actions.join("");
    },
    /** 查询数据列表 */
    getMembersList () {
      var pid = this.batchId
      request({
        url: 'build/bSafetySafetyTrainingMembers/selectByPId/' + pid,
        method: 'get'
      }).then(response => {
        this.membersList = response.data
      })
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.form = {}
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery () {
      if (this.sjtbDate != null&&this.sjtbDate != '') {
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
      }else{
      	this.queryParams.createBy = ''
      	this.queryParams.updateBy = ''
      }
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },
    /** 搜索按钮操作 */
    rosterhandleQuery () {
      this.rosterQueryParams.page = 1
      this.rosterQueryParams.note = this.batchId
      this.rosterQueryParams.limit = this.rosterpageSize
      this.getRosterList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.sjtbDate = ''
      // this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.batchId = guid()
      this.open = true
      this.title = '新增'
      this.membersList = []
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bSafetySafetyTraining/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.batchId = response.data.id
        this.form = response.data
        request({
          url: 'build/bSafetySafetyTrainingMembers/selectByPId/' + response.data.id,
          method: 'get'
        }).then(response2 => {
          this.membersList = response2.data
          this.open = true
          this.title = '详情'
        })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            request({
              url: 'build/bSafetySafetyTraining/edit',
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
            this.form.id = this.batchId
            request({
              url: 'build/bSafetySafetyTraining/add',
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
    handleDelete (row) {
      const id = row.id || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return request({
          url: 'build/bSafetySafetyTraining/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function () { })
    },
    /** 删除按钮操作 */
    handleDeleteMembers (row) {
      const id = row.id || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return request({
          url: 'build/bSafetySafetyTrainingMembers/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getMembersList()
        this.msgSuccess('删除成功')
      }).catch(function () { })
    },
    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('system/dict/type/export', {
    //     ...this.queryParams
    //   }, `type_${new Date().getTime()}.xlsx`)
    // },
    // 更改每页展示数量
    handleSizeChange (val) {
      this.pageSize = val
      this.getList()
    },
    // 更改当前页数
    handleCurrentChange (val) {
      this.currentPage = val
      this.getList()
    },
    // 打开花名册列表
    handleAddMembers () {
      this.getRosterList()
      this.membersOpen = true
      this.titleMembers = '人员列表'
      this.resetForm('rosterQueryParams')
    },

    //性别
    sexFormat (row, column) {
      return this.selectDictLabel(this.sexOptions, row.sex)
    },
    //劳务工种
    WTFormat (row, column) {
      var datas = this.worktypeList
      var value = row.workTypeId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].workType)
          return true
        }
      })
      return actions.join('')
    },
    //班组
    teamNameFormat (row, column) {
      var datas = this.BZList
      var value = row.teamName
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].teamName)
          return true
        }
      })
      return actions.join('')

    },
    //班组
    teamNameFormat2 (row, column) {
      var datas = this.BZList
      var value = row.infocollectionTeamName
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].teamName)
          return true
        }
      })
      return actions.join('')

    },
    /** 提交按钮 */
    submitFormMembers: function () {
      var k = this.$refs.accountTable.selection
      var s = []
      for (var i = 0; i < k.length; i++) {
        s.push(k[i].id)
      }

      if (s == "" || s == null) {
        this.membersOpen = false
        this.getMembersList()
        return
      }
      request({
        url: 'build/bSafetySafetyTrainingMembers/addByIds',
        method: 'post',
        data: { ids: s, pid: this.batchId },
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        if (response.code === 0) {
          this.membersOpen = false
          this.getMembersList()
        }
      })
    },
    cancelMembers () {
      this.membersOpen = false
    },
    /** 查询所工种数据列表 */
    getWorktypeList () {
      getWTList().then(response => {
        this.worktypeList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 日期去除时分秒 */
    formatterTime (row, column) {
      let data = row[column.property]
      return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },
    /** 查询所有分包商id和name */
    getListByFBS () {
      getListByFBS().then(response => {
        this.FBSList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询所有班组名称 */
    getListByBZ () {
      getListByBZ().then(response => {
        this.BZList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 更改每页展示数量
    rosterHandleSizeChange (val) {
      this.rosterpageSize = val
      this.getRosterList()
    },
    // 更改当前页数
    rosterHandleCurrentChange (val) {
      this.rostercurrentPage = val
      this.getRosterList()
    },
    // 获取项目信息列表
    getProjectInfoList() {
      request({
        url: 'build/projectInfo/queryProjectList',
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.projectInfoList = response.data || []
      })
        .catch(() => {
        })
    },
    // 项目名称
    projectNameZc(row, column) {
      var datas = this.projectInfoList
      var value = row.projectId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].name)
          return true
        }
      })
      return actions.join('')
    },
    sortChange(column) {
      sortColumn(this,column);
    },
  }
}
</script>


<style lang="scss" scoped>
.main {
  height: 100%;
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

.add-form::after {
  content: "";
  display: block;
  clear: both;
}

.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;
}

.link-type:focus:hover,
.link-type:hover {
  color: #20a0ff;
}

.el-table {
  border-top: 1px solid $xr-border-line-color;
}
/deep/ .special .el-form-item__label {
  width: 100px !important;
}
</style>
