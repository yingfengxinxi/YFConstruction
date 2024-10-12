<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="设备名称" class="special" prop="deviceId" >
          <el-select
            v-model="queryParams.deviceId"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in deviceIdList"
              :key="dict.assetId"
              :label="dict.assetName"
              :value="dict.assetId" />
          </el-select>
        </el-form-item>

        <el-form-item label="监测状态" class="special" prop="isSupervise" >
          <el-select
            v-model="queryParams.isSupervise"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in markingroomMonitoringOptions"
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
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
        <el-table-column label="设备名称" align="center" width="120px" prop="deviceId" :formatter="deviceIdDescribe" sortable="custom"/>
        <el-table-column label="沉桩深度" align="center" prop="pileDepth" sortable="custom"/>
        <el-table-column label="电流" align="center" prop="current1" sortable="custom"/>
        <el-table-column label="混凝土灌入量" align="center" width="140px" prop="infusion" sortable="custom"/>
        <el-table-column label="工作速度" align="center" prop="workSpeed" sortable="custom"/>
        <el-table-column label="拔管速度" align="center" prop="extubateSpeed" sortable="custom"/>
        <el-table-column label="左右倾斜" align="center" prop="leftrightTilt" sortable="custom"/>
        <el-table-column label="上下倾斜" align="center" prop="updownTilt" sortable="custom"/>
        <el-table-column label="桩编号" align="center" prop="pileMachineId" sortable="custom"/>
        <el-table-column label="桩混凝土灌入量" align="center" width="140px" prop="recordInfusion" sortable="custom"/>
        <el-table-column label="打桩开始时间" align="center" width="140px" prop="startTime" :formatter="formatterTime" sortable="custom"/>
        <el-table-column label="打桩结束时间" align="center" width="140px" prop="endTime" :formatter="formatterTime" sortable="custom"/>
        <el-table-column label="监测状态" align="center" prop="isSupervise" :formatter="isSuperviseDescribe" sortable="custom"/>
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
              <el-form-item label="设备名称" prop="deviceId" class="add-form-input">
                <el-select
                  v-model="form.deviceId"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in deviceIdList"
                    :key="dict.assetId"
                    :label="dict.assetName"
                    :value="dict.assetId" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="沉桩深度" prop="pileDepth" class="add-form-input">
                <el-input-number v-model="form.pileDepth" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="电流" prop="current1" class="add-form-input">
                <el-input-number v-model="form.current1" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="混凝土灌入量" prop="infusion" class="add-form-input">
                <el-input-number v-model="form.infusion" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="工作速度" prop="workSpeed" class="add-form-input">
                <el-input-number v-model="form.workSpeed" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="拔管速度" prop="extubateSpeed" class="add-form-input">
                <el-input-number v-model="form.extubateSpeed" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="左右倾斜" prop="leftrightTilt" class="add-form-input">
                <el-input-number v-model="form.leftrightTilt" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上下倾斜" prop="updownTilt" class="add-form-input">
                <el-input-number v-model="form.updownTilt" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="桩编号" prop="pileMachineId" class="add-form-input">
                <el-input v-model="form.pileMachineId" placeholder="请输入桩编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="桩区间id" prop="areaId" class="add-form-input">
                <el-input v-model="form.areaId" placeholder="请输入桩区间id" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="桩混凝土灌入量" prop="recordInfusion" class="add-form-input">
                <el-input-number v-model="form.recordInfusion" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="打桩开始时间" prop="startTime" class="add-form-input">
                <el-date-picker
                  v-model="form.startTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="打桩结束时间" prop="endTime" class="add-form-input">
                <el-date-picker
                  v-model="form.endTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="总时长" prop="totalTime" class="add-form-input">
                <el-input-number v-model="form.totalTime" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="成孔电流" prop="holeCurrent" class="add-form-input">
                <el-input-number v-model="form.holeCurrent" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="成孔时间" prop="holeTime" class="add-form-input">
                <el-date-picker
                  v-model="form.holeTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="成桩时间" prop="pileTime" class="add-form-input">
                <el-date-picker
                  v-model="form.pileTime"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参考灌入量" prop="referInfusion" class="add-form-input">
                <el-input-number v-model="form.referInfusion" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="成桩深度" prop="recordPileDepth" class="add-form-input">
                <el-input-number v-model="form.recordPileDepth" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="充盈系数" prop="fillFactor" class="add-form-input">
                <el-input-number v-model="form.fillFactor" :precision="2" :step="1" ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="经度" prop="lng" class="add-form-input">
                <el-input v-model="form.lng" placeholder="请输入经度" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="纬度" prop="lat" class="add-form-input">
                <el-input v-model="form.lat" placeholder="请输入纬度" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="X坐标" prop="coordinateX" class="add-form-input">
                <el-input v-model="form.coordinateX" placeholder="请输入X坐标" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Y坐标" prop="coordinateY" class="add-form-input">
                <el-input v-model="form.coordinateY" placeholder="请输入Y坐标" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="桩基监测状态" prop="isSupervise" class="add-form-input">
                <el-select
                  v-model="form.isSupervise"
                  placeholder="请选择"
                  clearable>
                  <el-option
                    v-for="dict in markingroomMonitoringOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
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
  name: 'PileFoundation',
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
      betonIdList: [],
      deviceIdList: [],
      positionOptions: [],
      markingroomMonitoringOptions: [],
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
        deviceId: [
          { required: true, message: '设备不能为空', trigger: 'blur' }
        ],
        pileDepth: [
          { required: true, message: '沉桩深度不能为空', trigger: 'blur' }
        ],
        current: [
          { required: true, message: '电流不能为空', trigger: 'blur' }
        ],
        infusion: [
          { required: true, message: '混凝土灌入量不能为空', trigger: 'blur' }
        ],
        workSpeed: [
          { required: true, message: '工作速度不能为空', trigger: 'blur' }
        ],
        extubateSpeed: [
          { required: true, message: '拔管速度不能为空', trigger: 'blur' }
        ],
        leftrightTilt: [
          { required: true, message: '左右倾斜不能为空', trigger: 'blur' }
        ],
        updownTilt: [
          { required: true, message: '上下倾斜不能为空', trigger: 'blur' }
        ],
        pileMachineId: [
          { required: true, message: '桩编号不能为空', trigger: 'blur' }
        ],
        areaId: [
          { required: true, message: '桩区间id不能为空', trigger: 'blur' }
        ],
        recordInfusion: [
          { required: true, message: '桩混凝土灌入量不能为空', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '打桩开始时间不能为空', trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: '打桩结束时间不能为空', trigger: 'blur' }
        ],
        totalTime: [
          { required: true, message: '总时长不能为空', trigger: 'blur' }
        ],
        holeCurrent: [
          { required: true, message: '成孔电流不能为空', trigger: 'blur' }
        ],
        holeTime: [
          { required: true, message: '成孔时间不能为空', trigger: 'blur' }
        ],
        pileTime: [
          { required: true, message: '成桩时间不能为空', trigger: 'blur' }
        ],
        referInfusion: [
          { required: true, message: '参考灌入量不能为空', trigger: 'blur' }
        ],
        recordPileDepth: [
          { required: true, message: '成桩深度不能为空', trigger: 'blur' }
        ],
        fillFactor: [
          { required: true, message: '充盈系数不能为空', trigger: 'blur' }
        ],
        lng: [
          { required: true, message: '经度不能为空', trigger: 'blur' }
        ],
        lat: [
          { required: true, message: '纬度不能为空', trigger: 'blur' }
        ],
        coordinateX: [
          { required: true, message: 'X坐标不能为空', trigger: 'blur' }
        ],
        coordinateY: [
          { required: true, message: 'Y坐标不能为空', trigger: 'blur' }
        ],
        isSupervise: [
          { required: true, message: '桩基监测状态不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getDicts('b_qual_markingroom_monitoring').then(response => {
      this.markingroomMonitoringOptions = response.data
    })
    this.getListByDeviceIdList()
    this.getList()
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bQualPileFoundation/list',
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
        url: 'build/bQualPileFoundation/selectById/' + pipeId,
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
              url: 'build/bQualPileFoundation/edit',
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
              url: 'build/bQualPileFoundation/add',
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
          url: 'build/bQualPileFoundation/deleteDetails/' + id,
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
    deviceIdDescribe(row, column){
      var datas = this.deviceIdList
      var value = row.deviceId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].assetId == ('' + value)) {
          actions.push(datas[key].assetName)
          return true
        }
      })
      return actions.join('')
    },
    getListByDeviceIdList() {
      request({
        url: 'build/bQualPileFoundation/assetConcreteList',
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.deviceIdList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    isSuperviseDescribe(row, column){
      return this.selectDictLabel(this.markingroomMonitoringOptions, row.isSupervise)
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
