<template>
  <div class="main">
<!--    <xr-header-->
<!--      icon-class="wk wk-project"-->
<!--      icon-color="#2362FB"-->
<!--      label="安全教育培训管理" />-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="分值起" prop="safetyEducationScore">
          <el-input
            v-model="queryParams.safetyEducationScore"
            placeholder="请输入分值"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="分值止" prop="safetyEducationAnswer">
          <el-input
            v-model="queryParams.safetyEducationAnswer"
            placeholder="请输入分值"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="题目分类" prop="workTypeIfspecial" >
          <el-select
            v-model="queryParams.safetyEducationType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in safetyEducationType"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="选项类型" prop="safetyEducationOptions" >
          <el-select
            v-model="queryParams.safetyEducationOptions"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in safetyEducationOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
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
      <el-table v-loading="loading" :data="dataList" >
        <el-table-column :formatter="safetyEducationTypeFc" label="题目分类" align="center" prop="safetyEducationType" />
        <el-table-column label="题目描述" align="center" prop="safetyEducationDescribe" />
        <el-table-column :formatter="safetyEducationOptionsFc" label="选项类型" align="center" prop="safetyEducationOptions" />
        <el-table-column label="选项集合" align="center" prop="safetyEducationProblem" />
        <el-table-column label="正确答案" align="center" prop="safetyEducationAnswer" />
        <el-table-column label="分值" align="center" prop="safetyEducationScore" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
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
          <el-form-item label="题目分类" prop="safetyEducationType" class="add-form-input">
            <el-select
              v-model="form.safetyEducationType"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px">
              <el-option
                v-for="dict in safetyEducationType"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="题目描述" prop="safetyEducationDescribe" class="add-form-input">
            <el-input v-model="form.safetyEducationDescribe" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="选项类型" prop="safetyEducationOptions" class="add-form-input">
            <el-select
              v-model="form.safetyEducationOptions"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px">
              <el-option
                v-for="dict in safetyEducationOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="选项集合" prop="safetyEducationProblem">
            <el-input type="textarea" :rows="2" v-model="form.safetyEducationProblem" placeholder="请输入选项集合"></el-input>
          </el-form-item>
          <el-form-item label="正确答案" prop="safetyEducationAnswer" class="add-form-input">
            <el-input v-model="form.safetyEducationAnswer" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="分值" prop="safetyEducationScore" class="add-form-input">
            <el-input v-model.number="form.safetyEducationScore" type="number" placeholder="请输入" />
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
//	import vPinyin from './vuepy.js';

export default {
  name: 'SafetyEducation',
  components: {
    // Reminder,
    XrHeader,
    SelectCol
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
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 选项类型  字典表safety_education_options
      safetyEducationOptions: [],
      // 题目分类  字典表safety_education_type
      safetyEducationType: [],
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
        safetyEducationType: [
          { required: true, message: '请选择题目分类', trigger: 'blur' }
        ],
        safetyEducationDescribe: [
          { required: true, message: '题目描述不能为空', trigger: 'blur' }
        ],
        safetyEducationOptions: [
          { required: true, message: '请选择选项类型', trigger: 'blur' }
        ],
        safetyEducationProblem: [
          { required: true, message: '选项集合不能为空', trigger: 'blur' }
        ],
        safetyEducationAnswer: [
          { required: true, message: '正确答案不能为空', trigger: 'blur' }
        ],
        safetyEducationScore: [
          { required: true, message: '分值不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    // 选项类型
    this.getDicts('safety_education_options').then(response => {
      this.safetyEducationOptions = response.data
    })
    // 题目分类
    this.getDicts('safety_education_type').then(response => {
      this.safetyEducationType = response.data
    })
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bSafetySafetyEducation/list',
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
    //选项类型
    safetyEducationOptionsFc(row, column){
      return this.selectDictLabel(this.safetyEducationOptions, row.safetyEducationOptions)
    },
    //题目分类
    safetyEducationTypeFc(row, column){
      return this.selectDictLabel(this.safetyEducationType, row.safetyEducationType)
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
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
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
        url: 'build/bSafetySafetyEducation/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
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
              url: 'build/bSafetySafetyEducation/edit',
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
              url: 'build/bSafetySafetyEducation/add',
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
          url: 'build/bSafetySafetyEducation/deleteDetails/' + id,
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
</style>
