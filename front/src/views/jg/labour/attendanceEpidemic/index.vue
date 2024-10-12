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
            style="width: 200px"
          >
            <el-option
              v-for="item in projectInfoList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>


        <el-form-item label="人员姓名" prop="infocollectionName" >
          <el-input
            v-model="queryParams.infocollectionName"
            placeholder="请输入人员姓名"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="健康码" class="special" prop="healthCode" >
          <el-select
            v-model="queryParams.healthCode"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in bHealthCodeOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
         	<el-date-picker
         		class="timecom"
         	  v-model="sjtbDate"
         	  type="daterange"
         	  align="right"
         	  unlink-panels
         	  range-separator="至"
         	  start-placeholder="进出场开始日期"
         	  end-placeholder="进出场结束日期"
         	  :picker-options="pickerOptions">
         	</el-date-picker>


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
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange"  >
        <el-table-column label="项目名称" align="center" prop="projectId" :formatter="projectNameZc" sortable="custom"/>
        <el-table-column label="人员姓名" align="center" prop="infocollectionName" sortable="custom" />
        <el-table-column label="进出场方向" :formatter="dkfx" align="center" prop="clockDirection" sortable="custom" />
        <el-table-column label="进出场时间" :formatter="formatterTime" align="center" prop="clockTime" sortable="custom" />
        <!--<el-table-column label="健康码状态" :formatter="healthCodeFormat" align="center" prop="healthCode" />-->
        <el-table-column label="健康码状态" align="center" prop="healthCode">
          <template slot-scope="scope">
            <span v-if="scope.row.healthCode == 0" style="color:green;">{{ healthCodeFormat(scope.row.healthCode) }}</span>
            <span v-if="scope.row.healthCode == 1" style="color:#FAAD14;">{{ healthCodeFormat(scope.row.healthCode) }}</span>
            <span v-if="scope.row.healthCode == 2" style="color:red;">{{ healthCodeFormat(scope.row.healthCode) }}</span>
          </template>
        </el-table-column>
        <!--<el-table-column label="核酸检测时间" :formatter="formatterTime" align="center" prop="nucleicAcidTime" />-->
        <el-table-column label="核酸检测时间" align="center" prop="healthCode" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.nucleicAcidInFO == 0" style="color:green;">{{ scope.row.nucleicAcidTimeText }}</span>
            <span v-if="scope.row.nucleicAcidInFO == 1" style="color:red;">{{ scope.row.nucleicAcidTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="进出场体温" align="center" prop="bodyTemperature" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.bodyTemperature <=37" >{{ scope.row.bodyTemperature }}</span>
            <span v-if="scope.row.bodyTemperature > 37" style="color:red;">{{ scope.row.bodyTemperature }}</span>
          </template>
        </el-table-column>

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
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                <el-select
                  v-model="form.projectId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px"
                  @change="getLabourInfocollectionList">
                  <el-option
                    v-for="dict in projectInfoList"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="人员名称" prop="infocollectionId" class="add-form-input">
                <el-select
                  v-model="form.infocollectionId"
                  :disabled="!isAble"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px"
                  @change="getLabourInfocollectionName">
                  <el-option
                    v-for="dict in labourInfocollectionList"
                    :key="dict.id"
                    :label="dict.name"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="进出场方向" prop="clockDirection" class="add-form-input">
                <el-select
                  v-model="form.clockDirection"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    label="进场"
                    value="0" />
                  <el-option
                    label="出场"
                    value="1" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="进出场时间" prop="clockTime" class="add-form-input">
                <el-date-picker
                  v-model="form.clockTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="健康码状态" prop="healthCode" class="add-form-input">
                <el-select
                  v-model="form.healthCode"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in bHealthCodeOption"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="核酸检测时间" prop="nucleicAcidTime" class="add-form-input">
                <el-date-picker
                  v-model="form.nucleicAcidTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="进出场体温" prop="bodyTemperature" class="add-form-input">
                <el-input v-model="form.bodyTemperature" placeholder="请输入进出场体温" />
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
  name: 'attendanceEpidemic',
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
      // 项目信息列表
      projectInfoList: [],
      bHealthCodeOption: [],
      labourInfocollectionList: [],
      isAble: false,
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
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectId: [
          { required: true, message: '项目名称不能为空', trigger: 'blur' }
        ],
        infocollectionId: [
          { required: true, message: '人员姓名不能为空', trigger: 'blur' }
        ],
        clockDirection: [
          { required: true, message: '进出场方向不能为空', trigger: 'blur' }
        ],
        clockTime: [
          { required: true, message: '进出场时间不能为空', trigger: 'blur' }
        ],
        healthCode: [
          { required: true, message: '健康码状态不能为空', trigger: 'blur' }
        ],
        nucleicAcidTime: [
          { required: true, message: '核酸检测时间不能为空', trigger: 'blur' }
        ],
        bodyTemperature: [
          { required: true, message: '进出场体温不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    this.getProjectInfoList()
    this.getDicts('b_health_code').then(response => {
      this.bHealthCodeOption = response.data
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
        url: 'build/bLabourAttendanceEpidemic/list',
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
      this.isAble = false
      this.labourInfocollectionList = []
      this.open = true
      this.title = '新增'
      this.batchId = guid()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bLabourAttendanceEpidemic/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.getLabourInfocollectionList(response.data.projectId)
        this.form = response.data
        this.open = true
        this.title = '修改'

      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          //this.form.batchId = this.batchId
/*          var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '附件提交异常'
            })
            return
          }*/
          if (this.form.id != undefined) {
            request({
              url: 'build/bLabourAttendanceEpidemic/edit',
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
              url: 'build/bLabourAttendanceEpidemic/add',
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
          url: 'build/bLabourAttendanceEpidemic/deleteDetails/' + id,
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
    // 设备状态
    healthCodeFormat(healthCode){
      return this.selectDictLabel(this.bHealthCodeOption, healthCode)
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
    getLabourInfocollectionList(value) {
      var LabourInfocollectionQueryParams = {}
      LabourInfocollectionQueryParams.page = 1
      LabourInfocollectionQueryParams.limit = 10000
      LabourInfocollectionQueryParams.projectId = value
      request({
        url: 'build/labourInfocollection/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.labourInfocollectionList = response.data.list
        this.isAble = true
        this.$forceUpdate()
      }).catch(() => {
      })
    },
    getLabourInfocollectionName(value){
      var datas = this.labourInfocollectionList
      var value = value
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].name)
          return true
        }
      })

      //console.log("123132:"+actions.join(''))
      this.form.infocollectionName = actions.join('')
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
