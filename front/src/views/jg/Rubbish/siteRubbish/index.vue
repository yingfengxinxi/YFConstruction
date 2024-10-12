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
        <el-form-item label="垃圾场名称" prop="dumpName">
          <el-input
            v-model="queryParams.dumpName"
            placeholder="请输入垃圾场名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="垃圾类型" class="special" prop="garbageType" >
          <el-select
            v-model="queryParams.garbageType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in garbageTypeList"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理日期">
         	<el-date-picker
         		class="timecom"
         	  v-model="sjtbDate"
         	  type="daterange"
         	  align="right"
         	  unlink-panels
         	  range-separator="至"
         	  start-placeholder="处理开始日期"
         	  end-placeholder="处理结束日期"
         	  :picker-options="pickerOptions">
         	</el-date-picker>
        </el-form-item>

        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
         <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

<!--          <el-button
            class="xr-btn&#45;&#45;orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column label="项目名称" align="center" prop="projectName" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="垃圾场名称" align="center" prop="dumpName" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="垃圾类型" align="center" prop="garbageType" :formatter="garbageTypeZh" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="内容说明" align="center" prop="content" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="数量" align="center" prop="garbageNumber" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="单位" align="center" prop="garbageUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="处理人" align="center" prop="dealwithPeople" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="处理时间" align="center" prop="dealwithTime" :formatter="formatterTime" show-overflow-tooltip sortable="custom"/>
<!--        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
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
        </el-table-column>-->
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
          <el-form-item  label="项目名称" class="add-form-input" prop="projectId" >
            <el-select
              v-model="form.projectId"
              placeholder="请选择"
              :disabled="!isAdd"
              clearable
              size="small">
              <el-option
                v-for="item in projectInfoList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="垃圾场名称" prop="dumpName" class="add-form-input">
            <el-input v-model="form.dumpName" :disabled="!isAdd" placeholder="请输入垃圾场名称" />
          </el-form-item>
          <el-form-item label="垃圾类型" prop="garbageType" class="add-form-input">
            <el-select
              v-model="form.garbageType"
              :disabled="!isAdd"
              placeholder="请选择"
              clearable
              size="small">
              <el-option
                v-for="dict in garbageTypeList"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="内容说明" prop="content" class="add-form-input">
            <el-input v-model="form.content" :disabled="!isAdd" placeholder="请输入内容说明" />
          </el-form-item>
          <el-form-item label="数量" prop="garbageNumber" class="add-form-input">
            <el-input v-model="form.garbageNumber" :disabled="!isAdd" placeholder="请输入数量" />
          </el-form-item>
          <el-form-item label="处理人" prop="dealwithPeople" class="add-form-input">
            <el-input v-model="form.dealwithPeople" :disabled="!isAdd" placeholder="请输入处理人" />
          </el-form-item>
          <el-form-item label="处理时间" prop="dealwithTime" class="add-form-input">
            <el-date-picker
              v-model="form.dealwithTime"
              :disabled="!isAdd"
              type="date"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"/>
          </el-form-item>
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
import { guid } from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表

import { sortColumn } from '@/utils/index';
//	import vPinyin from './vuepy.js';

export default {
  name: 'safetyData',
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
      garbageTypeList: [],
      // 项目信息列表
      projectInfoList: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      isAdd: true,
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
        dumpName: [
          { required: true, message: '垃圾场名称不能为空', trigger: 'blur' }
        ],
        garbageType: [
          { required: true, message: '垃圾类型不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '内容说明不能为空', trigger: 'blur' }
        ],
        garbageNumber: [
          { required: true, message: '数量不能为空', trigger: 'blur' }
        ],
        dealwithPeople: [
          { required: true, message: '处理人不能为空', trigger: 'blur' }
        ],
        dealwithTime: [
          { required: true, message: '处理时间不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    this.getProjectInfoList()
    this.getDicts('b_site_rubbish').then(response => {
      this.garbageTypeList = response.data
    })
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bJgSiteRubbish/list',
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
    garbageTypeZh(row,column){
      return this.selectDictLabel(this.garbageTypeList, row.garbageType)
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
      this.isAdd = true

    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.isAdd = true
      const pipeId = row.id || this.ids
      request({
        url: 'build/bJgSiteRubbish/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            request({
              url: 'build/bJgSiteRubbish/edit',
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
              url: 'build/bJgSiteRubbish/add',
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
          url: 'build/bJgSiteRubbish/deleteDetails/' + id,
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
    sortChange(column) {
      if( column.prop == 'garbageNumber'){
        column.prop =  'garbage_number+0'
      }
      sortColumn(this,column);
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
