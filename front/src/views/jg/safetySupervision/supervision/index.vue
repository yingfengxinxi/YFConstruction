<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="项目名称" prop="projectId">
          <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡查人" prop="patrolPeople">
          <el-input v-model="queryParams.patrolPeople" placeholder="请输入巡查人" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="巡查时间" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" @change="dateChange" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" align="right" unlink-panels range-separator="至" start-placeholder="巡查时间起" end-placeholder="巡查时间止" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

          <el-button class="xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column label="序号" align='center' type="index" width="60" />
        <el-table-column label="项目名称" align="center" prop="projectId" :formatter="projectFormatter" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="项目地址" align="center" prop="projectAddress" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="巡查时间" align="center" prop="patrolTime" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="巡查内容" align="center" prop="patrolContent" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="巡查人" align="center" prop="patrolPeople" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="陪同巡查人" align="center" prop="patrolPeopleAccom" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
                <el-select v-model="form.projectId" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目地址" prop="projectAddress" class="add-form-input">
                <el-input v-model="form.projectAddress" placeholder="请输入项目地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡查时间" prop="patrolTime" class="add-form-input">
                <el-date-picker v-model="form.patrolTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择巡查时间" style="width: 330px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡查内容" prop="patrolContent" class="add-form-input">
                <el-input v-model="form.patrolContent"  placeholder="请输入巡查内容" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡查综述" prop="patrolOverview" class="add-form-input">
                <el-input v-model="form.patrolOverview" placeholder="请输入巡查综述" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="处理意见" prop="handleOpinion" class="add-form-input">
                <el-input v-model="form.handleOpinion" placeholder="请输入处理意见" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡查人" prop="patrolPeople" class="add-form-input">
                <el-input v-model="form.patrolPeople" placeholder="请输入巡查人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="陪同巡查人" prop="patrolPeopleAccom" class="add-form-input">
                <el-input v-model="form.patrolPeopleAccom" placeholder="请输入陪同巡查人" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input :rows="2" v-model="form.remarks" type="textarea" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="附件" prop="enactDate">
                <wk-upload-file ref="wkUploadFile" name="file" multiple accept="*" :autoUpload="false" :batchId="batchId"></wk-upload-file>
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
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid } from '@/utils'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";

import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import { sortColumn } from '@/utils/index';

export default {
  name: 'supervision',
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
        projectAddress: [
          { required: true, message: '项目地址不能为空', trigger: 'blur' }
        ],
        patrolTime: [
          { required: true, message: '巡查时间不能为空', trigger: 'blur' }
        ],
        patrolContent: [
          { required: true, message: '巡查内容不能为空', trigger: 'blur' }
        ],
        patrolOverview: [
          { required: true, message: '巡查综述不能为空', trigger: 'blur' }
        ],
        handleOpinion: [
          { required: true, message: '处理意见不能为空', trigger: 'blur' }
        ],
        patrolPeople: [
          { required: true, message: '巡查人不能为空', trigger: 'blur' }
        ],
        patrolPeopleAccom: [
          { required: true, message: '陪同巡查人不能为空', trigger: 'blur' }
        ],
      },
      //项目列表
      projectOptions: [],
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
    //查询教育培训记录列表
    this.getList()
  },
  methods: {
    dateChange () {
      if (this.sjtbDate == null) {
        this.queryParams.patrolStartTime = ''
        this.queryParams.patrolEndTime = ''
      }
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      if (this.sjtbDate != undefined && this.sjtbDate != '' && this.sjtbDate.length != 0) {
        this.queryParams.patrolStartTime = this.sjtbDate[0]
        this.queryParams.patrolEndTime = this.sjtbDate[1]
      }
      request({
        url: 'build/jgSupervision/list',
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
            url: 'build/jgSupervision/save',
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
        url: 'build/jgSupervision/getById/' + pipeId,
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
          url: 'build/jgSupervision/delete',
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
