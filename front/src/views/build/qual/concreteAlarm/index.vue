<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="测温孔名称" class="special" prop="nickId" >
          <el-select
            v-model="queryParams.nickId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in nickIdList"
              :key="dict.id"
              :label="dict.nickName"
              :value="dict.id" />
          </el-select>
        </el-form-item>


        <el-form-item label="告警类型" class="special" prop="warningType" >
          <el-select
            v-model="queryParams.warningType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in qualConcreteAlarmType"
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
      <el-table v-loading="loading" :data="dataList" @sort-change="sortChange">
        <el-table-column label="测温孔名称" align="center" width="160px" prop="nickId" :formatter="nickIdDescribe" sortable="custom"/>
        <el-table-column label="电池电量" align="center" prop="batteryPower" sortable="custom"/>
        <el-table-column label="测点温度" align="center" prop="pointTemp" sortable="custom"/>
        <el-table-column label="告警项" align="center" prop="alarmRuleType" sortable="custom"/>
        <el-table-column label="告警描述" align="center" prop="alarmRuleTypeDesc" sortable="custom"/>
        <el-table-column label="告警类型" align="center" prop="warningType" :formatter="alarmRuleTypeDescDescribe" sortable="custom"/>
        <el-table-column label="告警阈值" align="center" prop="alertValue" sortable="custom"/>
        <el-table-column label="开始时间" align="center" prop="eventTime" :formatter="formatterTime" sortable="custom"/>
        <el-table-column label="结束时间" align="center" prop="recoverTime" :formatter="formatterTime" sortable="custom"/>
        <el-table-column label="开始告警温度" align="center" width="160px" prop="tempStartValue" sortable="custom"/>
        <el-table-column label="结束告警温度" align="center" width="160px" prop="tempEndValue" sortable="custom"/>
        <el-table-column label="表层温度" align="center" prop="fromPositionTempValue" sortable="custom"/>
        <el-table-column label="底层温度" align="center" prop="toPositionTempValue"sortable="custom" />

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
        <el-form ref="form" :model="form" :rules="selectVale == '表里温差告警' ? rules2 : rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="测温孔名称" prop="nickId" class="add-form-input">
                <el-select
                  v-model="form.nickId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in nickIdList"
                    :key="dict.id"
                    :label="dict.nickName"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电池电量" prop="batteryPower" class="add-form-input">
                <el-input-number v-model="form.batteryPower" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="测点温度" prop="pointTemp" class="add-form-input">
                <el-input-number v-model="form.pointTemp" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="告警项" prop="alarmRuleType" class="add-form-input">
                <el-select
                  v-model="form.alarmRuleType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px"
                  @change="dataload">
                  <el-option label="" value="" />
                  <el-option label="表里温差告警" value="表里温差告警" />
                  <el-option label="降温速率告警" value="降温速率告警" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="告警描述" prop="alarmRuleTypeDesc" class="add-form-input">
                <el-input v-model="form.alarmRuleTypeDesc" placeholder="请输入告警描述" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="告警类型" prop="warningType" class="add-form-input">
                <el-select
                  v-model="form.warningType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in qualConcreteAlarmType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="告警阈值" prop="alertValue" class="add-form-input">
                <el-input-number v-model="form.alertValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="开始时间" prop="eventTime" class="add-form-input">
                <el-date-picker
                  v-model="form.eventTime"
                  type="date"
                  style="width: 329px"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="recoverTime" class="add-form-input">
                <el-date-picker
                  v-model="form.recoverTime"
                  type="date"
                  style="width: 329px"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="开始告警温度" prop="tempStartValue" class="add-form-input">
                <el-input-number v-model="form.tempStartValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束告警温度" prop="tempEndValue" class="add-form-input">
                <el-input-number v-model="form.tempEndValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="表层温度" prop="fromPositionTempValue" class="add-form-input">
                <el-input-number v-model="form.fromPositionTempValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="底层温度" prop="toPositionTempValue" class="add-form-input">
                <el-input-number v-model="form.toPositionTempValue" :precision="2" :step="0.1" ></el-input-number>
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
import { guid } from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表
import { sortColumn } from '@/utils/index';
//	import vPinyin from './vuepy.js';

export default {
  name: 'ConcreteAlarm',
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
      nickIdList: [],
      selectVale: '0',
      qualConcreteAlarmType: [],
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
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        nickId: [
          { required: true, message: '测温孔名称不能为空', trigger: 'blur' }
        ],
        pointTemp: [
          { required: true, message: '测点温度不能为空', trigger: 'blur' }
        ],
        alarmRuleType: [
          { required: true, message: '告警项不能为空', trigger: 'blur' }
        ],
        alarmRuleTypeDesc: [
          { required: true, message: '告警描述不能为空', trigger: 'blur' }
        ],
        warningType: [
          { required: true, message: '告警类型不能为空', trigger: 'blur' }
        ],
        alertValue: [
          { required: true, message: '告警阈值不能为空', trigger: 'blur' }
        ],
        eventTime: [
          { required: true, message: '开始时间不能为空', trigger: 'blur' }
        ],
        recoverTime: [
          { required: true, message: '结束时间不能为空', trigger: 'blur' }
        ],
        tempStartValue: [
          { required: true, message: '开始告警温度不能为空', trigger: 'blur' }
        ],
        tempEndValue: [
          { required: true, message: '结束告警温度不能为空', trigger: 'blur' }
        ]
      },
      rules2: {
        nickId: [
          { required: true, message: '测温孔名称不能为空', trigger: 'blur' }
        ],
        pointTemp: [
          { required: true, message: '测点温度不能为空', trigger: 'blur' }
        ],
        alarmRuleType: [
          { required: true, message: '告警项不能为空', trigger: 'blur' }
        ],
        alarmRuleTypeDesc: [
          { required: true, message: '告警描述不能为空', trigger: 'blur' }
        ],
        warningType: [
          { required: true, message: '告警类型不能为空', trigger: 'blur' }
        ],
        alertValue: [
          { required: true, message: '告警阈值不能为空', trigger: 'blur' }
        ],
        eventTime: [
          { required: true, message: '开始时间不能为空', trigger: 'blur' }
        ],
        recoverTime: [
          { required: true, message: '结束时间不能为空', trigger: 'blur' }
        ],
        tempStartValue: [
          { required: true, message: '开始告警温度不能为空', trigger: 'blur' }
        ],
        tempEndValue: [
          { required: true, message: '结束告警温度不能为空', trigger: 'blur' }
        ],
        fromPositionTempValue: [
          { required: true, message: '表层温度不能为空', trigger: 'blur' }
        ],
        toPositionTempValue: [
          { required: true, message: '底层温度不能为空', trigger: 'blur' }
        ]
      }
    }
  },

  created() {
    this.getDicts('b_qual_concrete_alarm_type').then(response => {
      this.qualConcreteAlarmType = response.data
    })
    this.getListByNickIdList()
    this.getList()
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bQualConcreteAlarm/list',
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
      this.currentPage = 1
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
        url: 'build/bQualConcreteAlarm/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        this.selectVale = this.form.alarmRuleType;
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
              url: 'build/bQualConcreteAlarm/edit',
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
              url: 'build/bQualConcreteAlarm/add',
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
          url: 'build/bQualConcreteAlarm/deleteDetails/' + id,
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
    alarmRuleTypeDescDescribe(row, column){
      return this.selectDictLabel(this.qualConcreteAlarmType, row.warningType)
    },
    nickIdDescribe(row, column){
      var datas = this.nickIdList
      var value = row.nickId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].nickName)
          return true
        }
      })
      return actions.join('')
    },
    getListByNickIdList() {
      request({
        url: 'build/bQualConcretePoint/listAll',
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.nickIdList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    dataload(value) {
      var than = this
      than.selectVale = value
      this.$refs.form.clearValidate()
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
