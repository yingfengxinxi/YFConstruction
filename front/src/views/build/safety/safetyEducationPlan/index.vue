<template>
  <div class="main">

    <div class="main-body">
      <!-- 列表页查询 -->
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="培训计划名称" prop="trainPlanName">
          <el-input v-model="queryParams.trainPlanName" placeholder="请输入培训计划名称" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="培训类型" prop="trainType">
          <el-select v-model="queryParams.trainType" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in trainType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="培训周期" prop="trainCycle">
          <el-select v-model="queryParams.trainCycle" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in safetyTrainCycle" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
<!--        <el-form-item label="总课时" prop="totalClassHours">-->
<!--          <el-input v-model="queryParams.totalClassHours" placeholder="请输入总课时" clearable size="small" @keyup.enter.native="handleQuery" />-->
<!--        </el-form-item>-->
        <el-form-item label="负责人" prop="personCharge">
          <el-input v-model="queryParams.personCharge" placeholder="请输入负责人" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->
          <el-button class="xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
        </el-form-item>
      </el-form>
      <!-- 数据列表 -->
      <el-table :height="tableHeight" v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column  label="序号"  align='center' type="index"  width="60"/>
        <el-table-column label="培训计划名称" align="center" prop="trainPlanName" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训类型" align="center" :formatter="trainingTypeFc" prop="trainType" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训周期" align="center" :formatter="safetyTrainCycleFc" prop="trainCycle" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="培训内容" align="center" prop="trainContent" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="总课时" align="center" prop="totalClassHours" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="负责人" align="center" prop="personCharge" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            <el-button size="mini" type="text" icon="el-icon-document" @click="ExecutionRecord(scope.row)">执行记录</el-button>
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
            <el-col :span="8">
              <el-form-item label="培训计划名称" prop="trainPlanName" class="add-form-input">
                <el-input v-model="form.trainPlanName" placeholder="请输入" />
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="培训类型" prop="trainType" class="add-form-input">
                <el-select v-model="form.trainType" placeholder="请选择" clearable size="small" style="width: 160px">
                  <el-option v-for="dict in trainType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="培训周期" prop="trainCycle" class="add-form-input">
                <el-select v-model="form.trainCycle" placeholder="请选择" clearable size="small" style="width: 160px">
                  <el-option v-for="dict in safetyTrainCycle" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="培训内容" prop="trainContent" class="add-form-input">
                <el-input :rows="2" type="textarea" v-model="form.trainContent" placeholder="请输入" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="总课时" prop="totalClassHours">
                <el-input-number v-model="form.totalClassHours" :min="0" placeholder="请输入" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="负责人" prop="personCharge" class="add-form-input">
                <el-input v-model.number="form.personCharge" placeholder="请输入" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <ExecutionRecord v-if="showRecord" :id="rowID" @close="showRecord = false" />
    </div>

  </div>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid, sortColumn } from '@/utils'

import ExecutionRecord from './ExecutionRecord'
export default {
  name: 'safetyEducationPlan',
  components: {
    // Reminder,
    XrHeader,
    SelectCol,
    ExecutionRecord
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 280,
      // 选中数组
      ids: [],
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
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 培训分类
      trainType: [],
      //培训周期
      safetyTrainCycle: [],
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        trainPlanName: [
          { required: true, message: '请输入培训计划名称', trigger: 'blur' }
        ],
        trainType: [
          { required: true, message: '请选择培训类型', trigger: 'blur' }
        ],
        trainCycle: [
          { required: true, message: '请输入培训周期', trigger: 'blur' }
        ],
        trainContent: [
          { required: true, message: '请输入培训内容', trigger: 'blur' }
        ],
        totalClassHours: [
          { required: true, message: '请输入总课时', trigger: 'blur' }
        ],
        personCharge: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ]
      },
      //控制执行记录开关
      showRecord: false,
      rowID: '',
    }
  },

  created () {
    this.getList()
    // 培训分类
    this.getDicts('safety_training_type').then(response => {
      this.trainType = response.data
    })
    // 培训周期
    this.getDicts('safety_train_cycle').then(response => {
      this.safetyTrainCycle = response.data
    })
  },
  methods: {
    ExecutionRecord (row) {
      this.showRecord = true;
      this.rowID = row.id;
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/safetyEducationPlan/list',
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
    // 培训分类
    trainingTypeFc (row, column) {
      return this.selectDictLabel(this.trainType, row.trainType)
    },
    // 培训周期
    safetyTrainCycleFc (row, column) {
      return this.selectDictLabel(this.safetyTrainCycle, row.trainCycle)
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
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.open = true
      this.title = '新增'
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const pipeId = row.id
      request({
        url: 'build/safetyEducationPlan/getById/' + pipeId,
        method: 'post'
      }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          request({
            url: 'build/safetyEducationPlan/saveOrUpdate',
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
          url: 'build/safetyEducationPlan/delByIds',
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
	sortChange(column) {
		sortColumn(this,column);
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
