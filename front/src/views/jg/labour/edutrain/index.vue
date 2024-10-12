<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="项目名称" prop="projectId">
          <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="培训类型" prop="trainType">
          <el-select v-model="queryParams.trainType" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in trainTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>

        <el-form-item label="培训人" prop="trainPeople">
          <el-input v-model="queryParams.trainPeople" placeholder="请输入授课人" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="培训日期" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" @change="dateChange" value-format="yyyy-MM-dd" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="培训日期起" end-placeholder="培训日期止" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

          <!--<el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column label="序号" align='center' type="index" width="60"  />
        <el-table-column label="项目名称" align="center" prop="projectId" width="200px" sortable="custom" :formatter="projectFormatter" show-overflow-tooltip/>
        <el-table-column label="项目编号" align="center" prop="projectCode" sortable="custom" show-overflow-tooltip/>
        <el-table-column label="培训日期" align="center" prop="trainDate" sortable="custom" show-overflow-tooltip/>
        <el-table-column label="培训时长(h)" align="center" prop="trainDuration" sortable="custom" show-overflow-tooltip/>
        <el-table-column label="培训名称" align="center" prop="trainName" sortable="custom" show-overflow-tooltip/>
        <el-table-column label="培训类型" align="center" prop="trainType" sortable="custom" :formatter="trainTypeFormatter" show-overflow-tooltip/>
        <el-table-column label="培训人" align="center" prop="trainPeople" sortable="custom" show-overflow-tooltip/>
        <el-table-column label="培训机构" align="center" prop="trainInstitutions" sortable="custom" show-overflow-tooltip/>
        <!-- <el-table-column label="培训地址" align="center" prop="trainAddress" />
        <el-table-column label="培训简述" align="center" prop="trainDescribe" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">查看</el-button>
            <!--<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>-->
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
              <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                <el-select v-model="form.projectId" placeholder="请选择" clearable size="small" style="width: 330px" :disabled="!isAble">
                  <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目编号" prop="projectCode" class="add-form-input">
                <el-input v-model="form.projectCode" placeholder="请输入项目编号" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训日期" prop="trainDate" class="add-form-input">
                <el-date-picker v-model="form.trainDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 330px" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训时长" prop="trainDuration" class="add-form-input">
                <el-input v-model.number="form.trainDuration" type="number" placeholder="请输入培训时长" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训名称" prop="trainName" class="add-form-input">
                <el-input v-model="form.trainName" placeholder="请输入培训名称" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训类型" prop="trainType" class="add-form-input">
                <el-select v-model="form.trainType" placeholder="请选择" clearable size="small" style="width: 330px" :disabled="!isAble">
                  <el-option v-for="dict in trainTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训人" prop="trainPeople" class="add-form-input">
                <el-input v-model="form.trainPeople" placeholder="请输入" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训机构" prop="trainInstitutions" class="add-form-input">
                <el-input v-model="form.trainInstitutions" placeholder="请输入" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="培训地点" prop="trainAddress" class="add-form-input">
                <el-input v-model="form.trainAddress" placeholder="请输入" :disabled="!isAble" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="培训简述" prop="trainDescribe">
                <el-input :rows="2" v-model="form.trainDescribe" type="textarea" placeholder="请输入培训简述" :disabled="!isAble" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="附件" prop="enactDate">
                <wk-upload-file ref="wkUploadFile" :disabled="!isAble" name="file" multiple accept="*" :autoUpload="false" :batchId="batchId"></wk-upload-file>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <!--<el-button type="primary" @click="submitForm">确 定</el-button>-->
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid,sortColumn } from '@/utils'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";

import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'

export default {
  name: 'edutrain',
  components: {
    XrHeader,
    SelectCol,
    WkUploadFile,
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 310,
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
      // 附件id
      batchId: '',
      isAble: false,
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
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectId: [
          { required: true, message: '请选择项目', trigger: 'blur' }
        ],
        projectCode: [
          { required: true, message: '项目编号不能为空', trigger: 'blur' }
        ],
        trainDate: [
          { required: true, message: '培训日期不能为空', trigger: 'blur' }
        ],
        trainDuration: [
          { required: true, message: '培训时长不能为空', trigger: 'blur' }
        ],
        trainName: [
          { required: true, message: '培训名称不能为空', trigger: 'blur' }
        ],
        trainType: [
          { required: true, message: '培训类型不能为空', trigger: 'blur' }
        ],
        trainPeople: [
          { required: true, message: '培训人不能为空', trigger: 'blur' }
        ],
        trainInstitutions: [
          { required: true, message: '培训机构不能为空', trigger: 'blur' }
        ],
        trainAddress: [
          { required: true, message: '培训地址不能为空', trigger: 'blur' }
        ],
        trainDescribe: [
          { required: true, message: '培训简述不能为空', trigger: 'blur' }
        ]
      },
      //项目列表
      projectOptions: [],
      //教育培训类型
      trainTypeOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,

    }
  },
  created () {
    //查询项目列表
    request({
      url: 'build/projectInfo/queryProjectList',
      method: 'post',
      data: {},
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    }).then(response => {
      this.projectOptions = response.data
    })
    //查询教育培训类型
    this.getDicts('b_jg_train_type').then(response => {
      this.trainTypeOptions = response.data
    })
    //查询教育培训记录列表
    this.getList()
  },
  methods: {
  	sortChange(column) {
      sortColumn(this,column);
    },
    dateChange () {
      if (this.sjtbDate == null) {
        this.queryParams.trainStartDate = ''
        this.queryParams.trainEndDate = ''
      }
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      if (this.sjtbDate != undefined && this.sjtbDate != '' && this.sjtbDate.length != 0) {
        this.queryParams.trainStartDate = this.sjtbDate[0]
        this.queryParams.trainEndDate = this.sjtbDate[1]
      }
      request({
        url: 'build/jgEducationTrainRecord/list',
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
    //项目格式化
    projectFormatter (row, column) {
      return this.selectDIYLabel(this.projectOptions, row.projectId, 'id', 'name')
    },
    //培训类型格式化
    trainTypeFormatter (row, column) {
      return this.selectDictLabel(this.trainTypeOptions, row.trainType)
    },
    //列格式化
    selectDIYLabel (datas, value, dataValue, dataLablel) {
      var actions = []
      Object.keys(datas).some((key) => {
        if (datas[key][dataValue] == (typeof datas[key][dataValue] == 'number' ? (0 + value) : ('' + value))) {
          actions.push(datas[key][dataLablel])
          return true
        }
      })
      return actions.join('')
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          var fileFlag = this.$refs.wkUploadFile.submitAsync();
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '方案附件提交异常'
            })
            return;
          } else {
            this.form.batchId = this.batchId;
          }
          request({
            url: 'build/jgEducationTrainRecord/save',
            method: 'post',
            data: this.form,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(response => {
            if (response.code === 0) {
              if (this.form.id != undefined) {
                this.msgSuccess('修改成功')
              } else {
                this.msgSuccess('新增成功')
              }
              this.open = false
              this.getList()
            }
          })
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.sjtbDate = ''
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
    // 表单重置
    reset () {
      this.form = {}
      this.resetForm('form')
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const pipeId = row.id
      request({
        url: 'build/jgEducationTrainRecord/getById/' + pipeId,
        method: 'post'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        this.open = true
        this.title = '修改'
      })
    },

    /** 删除按钮操作 */
    handleDelete (row) {
      const id = [row.id] || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return request({
          url: 'build/jgEducationTrainRecord/delete',
          method: 'post',
          data: id,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function () { })
    },

    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
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
    /** 日期去除时分秒 */
    formatterTime (row, column) {
      let data = row[column.property]
      return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
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
