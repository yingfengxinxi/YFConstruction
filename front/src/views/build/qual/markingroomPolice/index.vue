<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="标养室名称" class="special" prop="curingRoomId" >
          <el-select
            v-model="queryParams.curingRoomId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in curingRoomList"
              :key="dict.id"
              :label="dict.curingRoomName"
              :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备名称" class="special" prop="deviceId" >
          <el-select
            v-model="queryParams.deviceId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in deviceList"
              :key="dict.id"
              :label="dict.deviceName"
              :value="dict.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="报警类型" class="special" prop="alarmType" >
          <el-select
            v-model="queryParams.alarmType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in alarmTypeOptions"
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
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange" >
        <el-table-column label="标养室名称" :formatter="curingRoomIdDescribe" align="center" prop="curingRoomId" sortable="custom" />
        <el-table-column label="设备名称" :formatter="deviceIdDescribe" align="center" prop="deviceId" sortable="custom" />
        <el-table-column label="报警类型" :formatter="alarmTypeDescribe" align="center" prop="alarmType" sortable="custom" />
        <el-table-column label="报警类型描述" align="center" prop="alarmTypeDes" sortable="custom" />
        <el-table-column label="报警值" align="center" prop="alarmValue" sortable="custom" />
        <el-table-column label="恢复值" align="center" prop="recoverValue" sortable="custom" />
        <el-table-column label="数据产生时间" :formatter="formatterTime" align="center" prop="eventTime" sortable="custom" />
        <el-table-column label="报警结束时间" :formatter="formatterTime" align="center" prop="endAlarmTime" sortable="custom" />
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
              <el-form-item label="标养室名称" prop="curingRoomId" class="add-form-input">
                <el-select
                  v-model="form.curingRoomId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in curingRoomList"
                    :key="dict.id"
                    :label="dict.curingRoomName"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备名称" prop="deviceId" class="add-form-input">
                <el-select
                  v-model="form.deviceId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in deviceList"
                    :key="dict.id"
                    :label="dict.deviceName"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="报警类型" prop="alarmType" class="add-form-input">
                <el-select
                  v-model="form.alarmType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in alarmTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警类型描述" prop="alarmTypeDes" class="add-form-input">
                <el-input v-model="form.alarmTypeDes" placeholder="请输入报警类型描述" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="报警值" prop="alarmValue" class="add-form-input">
                <el-input-number v-model="form.alarmValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="恢复值" prop="recoverValue" class="add-form-input">
                <el-input-number v-model="form.recoverValue" :precision="2" :step="0.1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="数据产生时间" prop="eventTime" class="add-form-input">
                <el-date-picker
                  v-model="form.eventTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警结束时间" prop="endAlarmTime" class="add-form-input">
                <el-date-picker
                  v-model="form.endAlarmTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="提示信息" prop="notification" class="add-form-input">
                <el-input type="textarea" :rows="2" v-model="form.notification" placeholder="请输入提示信息"></el-input>
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
  name: 'markingroomPolice',
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
      alarmTypeOptions: [],
      curingRoomList: [],
      deviceList: [],
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
        curingRoomId: [
          { required: true, message: '标养室不能为空', trigger: 'blur' }
        ],
        deviceId: [
          { required: true, message: '设备不能为空', trigger: 'blur' }
        ],
        alarmType: [
          { required: true, message: '报警类型不能为空', trigger: 'blur' }
        ],
        alarmTypeDes: [
          { required: true, message: '报警类型描述不能为空', trigger: 'blur' }
        ],
        alarmValue: [
          { required: true, message: '报警值不能为空', trigger: 'blur' }
        ],
        recoverValue: [
          { required: true, message: '恢复值不能为空', trigger: 'blur' }
        ],
        eventTime: [
          { required: true, message: '数据产生时间不能为空', trigger: 'blur' }
        ],
        endAlarmTime: [
          { required: true, message: '报警结束时间不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getDicts('b_qual_markingroom_police_type').then(response => {
      this.alarmTypeOptions = response.data
    })
    this.getListBycuringRoomList()
    this.getListByDeviceList()
    this.getList()
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bQualMarkingroomPolice/list',
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
        url: 'build/bQualMarkingroomPolice/selectById/' + pipeId,
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
              url: 'build/bQualMarkingroomPolice/edit',
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
              url: 'build/bQualMarkingroomPolice/add',
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
          url: 'build/bQualMarkingroomPolice/deleteDetails/' + id,
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
    alarmTypeDescribe(row, column){
      return this.selectDictLabel(this.alarmTypeOptions, row.alarmType)
    },
    curingRoomIdDescribe(row, column){
      var datas = this.curingRoomList
      var value = row.curingRoomId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].curingRoomName)
          return true
        }
      })
      return actions.join('')
    },
    deviceIdDescribe(row, column){
      var datas = this.deviceList
      var value = row.deviceId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].deviceName)
          return true
        }
      })
      return actions.join('')
    },
    getListBycuringRoomList() {
      request({
        url: 'build/bQualMarkingroom/listAll',
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.curingRoomList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getListByDeviceList() {
      request({
        url: 'build/bQualMarkingroomEquipment/listAll',
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.deviceList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
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
