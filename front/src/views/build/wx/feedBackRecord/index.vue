<template>
  <div class="main">
<!--    <xr-header
      icon-class="wk wk-project"
      icon-color="#2362FB"
      label="问题反馈" />-->

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="用户名称" prop="feedbackName">
          <el-input
            v-model="queryParams.feedbackName"
            placeholder="请输入用户名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>


        <el-form-item label="用户电话" prop="feedbackTel" >
          <el-input
            v-model="queryParams.feedbackTel"
            placeholder="请输入用户电话"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
         	<el-date-picker
         		class="timecom"
         	  v-model="sjtbDate"
         	  type="daterange"
         	  align="right"
         	  unlink-panels
         	  range-separator="至"
         	  start-placeholder="编制开始日期"
         	  end-placeholder="编制结束日期"
         	  :picker-options="pickerOptions">
         	</el-date-picker>


        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
         <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->
<!--
          <el-button
            class="xr-btn&#45;&#45;orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed />
        <el-table-column label="用户名称" align="center" prop="feedbackName" sortable="custom"/>
        <el-table-column label="联系电话" align="center" prop="feedbackTel" sortable="custom"/>
        <el-table-column label="反馈类型" align="center" prop="feedbackType" sortable="custom"/>
        <el-table-column label="反馈内容" align="center" prop="feedbackContent" sortable="custom"/>
        <el-table-column label="反馈时间" :formatter="formatterTime" align="center" prop="createTime" sortable="custom"/>
        <el-table-column label="当前状态" align="center" :formatter="feedbackStateZh" prop="feedbackState" sortable="custom"/>
        <el-table-column label="答复人" align="center" prop="reply" sortable="custom"/>
        <el-table-column label="答复时间" :formatter="formatterTime" align="center" prop="replyTime" sortable="custom"/>
        <el-table-column label="答复内容" align="center" prop="replyContent" sortable="custom"/>
        <el-table-column label="是否解决" :formatter="replyStateZh" align="center" prop="replyState" sortable="custom"/>
        <el-table-column label="满意情况" :formatter="evaluationLevelZh" align="center" prop="evaluationLevel" sortable="custom"/>
        <el-table-column label="评价内容" align="center" prop="evaluation" sortable="custom"/>
        <!--  <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >答复</el-button>
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

          <el-form-item label="反馈内容" prop="feedbackContent" class="add-form-input">
            <el-input v-model="form.feedbackContent" :rows="2" type="textarea" disabled="" />
          </el-form-item>
          <el-form-item label="答复内容" prop="replyContent" class="add-form-input">
            <el-input v-model="form.replyContent" :rows="2" type="textarea" placeholder="请输入答复内容" />
          </el-form-item>

          <el-form-item label="用户上传附件">
            <wk-upload-file
              ref="wkUploadFile"
              :auto-upload="false"
              :batch-id="batchId"
              name="file"
              multiple
              accept="*"
            />
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
  name: 'FeedBackRecord',
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
        replyContent: [
          { required: true, message: '答复内容不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      this.queryParams.feedbackState = '1'

      request({
        url: 'build/bWxFeedback/list',
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
    feedbackStateZh(row,column){
      let data = row[column.property]
      var zh = ""
      if(data=="1"){
        zh="已回复";
      }else{
        zh="未回复";
      }
      return  zh
    },
    replyStateZh(row,column){
      let data = row[column.property]
      var zh = ""
      if(data=="1"){
        zh="已解决";
      }else{
        zh="未解决";
      }
      return  zh
    },
    evaluationLevelZh(row,column){
      let data = row[column.property]
      var zh = ""
      if(data=="1"){
        zh="非常不满意";
      }else if(data=="2"){
        zh="不满意";
      }else if(data=="3"){
        zh="一般";
      }else if(data=="4"){
        zh="比较满意";
      }else if(data=="5"){
        zh="非常满意";
      }else{
        zh="未评价";
      }
      return  zh
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
      if (this.sjtbDate != ''&&this.sjtbDate != null) {
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
        url: 'build/bWxFeedback/selectById/' + pipeId,
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
              url: 'build/bWxFeedback/edit',
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
              url: 'build/bWxFeedback/add',
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
          url: 'build/bWxFeedback/deleteDetails/' + id,
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
    sortChange(column) {
      sortColumn(this,column);
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
</style>
