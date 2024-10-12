<template>
  <div class="main">
<!--    <xr-header-->
<!--      icon-class="wk wk-project"-->
<!--      icon-color="#2362FB"-->
<!--      label="安全应急管理" />-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="100px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="应急预案名称" prop="safetyEmergencyPlanName">
          <el-input
            v-model="queryParams.safetyEmergencyPlanName"
            placeholder="请输入名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="应急预案等级" prop="safetyEmergencyPlanLevel" label-width="100px" >
          <el-select
            v-model="queryParams.safetyEmergencyPlanLevel"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in safetyEmergencyPlanLevel"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!--<el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

          <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="dataList" :height="tableHeight" @sort-change="sortChange">
        <el-table-column  label="序号"  align='center' type="index"  width="60"/>
        <el-table-column label="应急预案名称" align="center" prop="safetyEmergencyPlanName"  sortable="custom"/>
        <el-table-column :formatter="safetyEmergencyPlanLevelFc" label="应急预案等级" align="center" prop="safetyEmergencyPlanLevel" sortable="custom"/>
        <el-table-column label="应急预案描述" align="center" prop="safetyEmergencyPlanDescribe" sortable="custom"/>
        <el-table-column label="创建日期" align="center" prop="createTime"  sortable="custom"/>
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
          <el-form-item label="应急预案名称" prop="safetyEmergencyPlanName" class="add-form-input">
            <el-input v-model="form.safetyEmergencyPlanName" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="应急预案等级" prop="safetyEmergencyPlanLevel" class="add-form-input">
            <el-select
              v-model="form.safetyEmergencyPlanLevel"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px">
              <el-option
                v-for="dict in safetyEmergencyPlanLevel"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="应急预案描述" prop="safetyEmergencyPlanDescribe">
            <el-input type="textarea" :rows="2" v-model="form.safetyEmergencyPlanDescribe" placeholder="请输入应急预案描述"></el-input>
          </el-form-item>
          <el-form-item label="应急预案附件">
            <wk-upload-file
              ref="wkUploadFile"
              :auto-upload="false"
              :batch-id="batchId"
              name="file"
              multiple
              accept="*"
            />
          </el-form-item>

          <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAddMembers" >新建</el-button>

          <el-table v-loading="loading" :data="membersList" >
            <el-table-column label="人员名称" align="center" prop="membersName" />
            <el-table-column label="职能" align="center" prop="membersFunction" />
            <el-table-column label="联系电话" align="center" prop="membersTel" />
            <el-table-column label="备注" align="center" prop="membersRemarks" />

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdateMembers(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDeleteMembers(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>


      <el-dialog :title="titleMembers" :visible.sync="membersOpen" width="900px" append-to-body>
        <el-form ref="formMembers" :model="formMembers" :rules="rulesMembers" label-width="100px" class="add-form">
          <el-form-item label="人员名称" prop="membersName" class="add-form-input">
            <el-input v-model="formMembers.membersName" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="职能" prop="membersFunction" class="add-form-input">
            <el-input v-model="formMembers.membersFunction" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="联系电话" prop="membersTel" class="add-form-input">
            <el-input v-model="formMembers.membersTel" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="备注" prop="membersRemarks" class="add-form-input">
            <el-input v-model="formMembers.membersRemarks" placeholder="请输入" />
          </el-form-item>
        </el-form>
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
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
import { guid } from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表
import { exportFile } from "@/api/build/exportFile";
import { sortColumn } from '@/utils/index';
//	import vPinyin from './vuepy.js';

export default {
  name: 'safetyEmergency',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList
  },
  data() {
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('手机号不能为空'))
      } else {
        const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
        console.log(reg.test(value));
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error('请输入正确的手机号'));
        }
      }
    };
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
      tableHeight: document.documentElement.clientHeight - 250, // 表的高度
      // 表格数据
      dataList: [],
      // 应急班组成员数据
      membersList: [],
      // 题目分类  字典表safety_emergency_level
      safetyEmergencyPlanLevel: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 应急班组成员弹出层标题
      titleMembers: '',
      // 是否显示弹出层
      open: false,
      // 是否显示应急班组成员弹出层
      membersOpen: false,
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 表单参数
      form: {},
      formMembers: {},
      // 表单校验
      rules: {
        safetyEmergencyPlanName: [
          { required: true, message: '应急预案名称不能为空', trigger: 'blur' }
        ],
        safetyEmergencyPlanLevel: [
          { required: true, message: '应急预案等级不能为空', trigger: 'blur' }
        ],
        safetyEmergencyPlanDescribe: [
          { required: true, message: '应急预案描述不能为空', trigger: 'blur' }
        ]

      },
      // 应急管理人员表单校验
      rulesMembers: {
        membersName: [
          { required: true, message: '人员名称不能为空', trigger: 'blur' }
        ],
        membersFunction: [
          { required: true, message: '职能不能为空', trigger: 'blur' }
        ],
        membersTel: [
          { validator: checkPhone, trigger: 'blur'}
        ]

      }
    }

  },

  created() {
    this.getList()
    // 题目分类
    this.getDicts('safety_emergency_level').then(response => {
      this.safetyEmergencyPlanLevel = response.data
    })
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bSafetySafetyEmergency/list',
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
    safetyEmergencyPlanLevelFc(row, column){
      return this.selectDictLabel(this.safetyEmergencyPlanLevel, row.safetyEmergencyPlanLevel)
    },
    /** 查询数据列表 */
    getMembersList() {
      var pid = this.batchId
      request({
        url: 'build/bSafetySafetyEmergencyMembers/selectByPId/' + pid,
        method: 'get'
      }).then(response => {
        this.membersList = response.data
        this.membersOpen = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    cancelMembers() {
      this.membersOpen = false
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
      this.batchId = guid()
      this.membersList = []
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bSafetySafetyEmergency/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        request({
          url: 'build/bSafetySafetyEmergencyMembers/selectByPId/' + response.data.batchId,
          method: 'get'
        }).then(response2 => {
          console.log(response2.data)
          this.membersList = response2.data
          this.open = true
          this.title = '修改'
        })
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
              url: 'build/bSafetySafetyEmergency/edit',
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
              url: 'build/bSafetySafetyEmergency/add',
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
          url: 'build/bSafetySafetyEmergency/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 应急安全组使用的方法 */
    /** 新增按钮操作 */
    handleAddMembers() {
      this.resetMembers()
      this.membersOpen = true
      this.titleMembers = '新增'
    },
    /** 修改按钮操作 */
    handleUpdateMembers(row) {
      this.resetMembers()
      const pipeId = row.id || this.ids0
      request({
        url: 'build/bSafetySafetyEmergencyMembers/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.formMembers = response.data
        this.membersOpen = true
        this.titleMembers = '修改'
      })
    },
    /** 删除按钮操作 */
    handleDeleteMembers(row) {
      const id = row.id || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return request({
          url: 'build/bSafetySafetyEmergencyMembers/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getMembersList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 提交按钮 */
    submitFormMembers: function() {
      this.$refs['formMembers'].validate(valid => {
        if (valid) {
          this.formMembers.pid = this.batchId
          if (this.formMembers.id != undefined) {
            request({
              url: 'build/bSafetySafetyEmergencyMembers/edit',
              method: 'post',
              data: this.formMembers,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.getMembersList()
              }
            })
          } else {
            request({
              url: 'build/bSafetySafetyEmergencyMembers/add',
              method: 'post',
              data: this.formMembers,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.getMembersList()
              }
            })
          }
        }
      })
    },
    // 表单重置
    resetMembers() {
      this.formMembers = {}
      this.resetForm('formMembers')
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
</style>
