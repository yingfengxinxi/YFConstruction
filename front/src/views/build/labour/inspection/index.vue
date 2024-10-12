<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="验收阶段" class="special" prop="inspectionPhase" >
          <el-select
            v-model="queryParams.inspectionPhase"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in bInspectionPhaseOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>

        <el-form-item label="申请状态" class="special" prop="inspectionType" >
          <el-select
            v-model="queryParams.inspectionType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in bInspectionTypeOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
<!--         	<el-date-picker
         		class="timecom"
         	  v-model="sjtbDate"
         	  type="daterange"
         	  align="right"
         	  unlink-panels
         	  range-separator="至"
         	  start-placeholder="编制开始日期"
         	  end-placeholder="编制结束日期"
         	  :picker-options="pickerOptions">
         	</el-date-picker>-->


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
      <el-table v-loading="loading" :height="tableHeight" :data="dataList"  @sort-change="sortChange">
        <el-table-column label="序号" type="index"  align='center' width="70" ></el-table-column>
        <el-table-column label="申请时间" :formatter="formatterTime" align="center" prop="createTime" sortable="custom" />
        <el-table-column label="验收阶段" :formatter="inspectionPhaseFormat" align="center" prop="inspectionPhase" sortable="custom" />
        <el-table-column label="申请说明" align="center" prop="inspectionContent" sortable="custom" />
        <el-table-column label="申请状态" :formatter="inspectionTypeFormat" align="center" prop="inspectionType" sortable="custom" />

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.inspectionType == '1'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >验收/整改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-collection"
              @click="handleSelect(scope.row)"
            >查看</el-button>
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
            <el-col :span="24">
              <el-form-item label="验收阶段" prop="inspectionPhase" class="add-form-input">
                <el-select
                  v-model="form.inspectionPhase"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px"
                  @change="getLabourInfocollectionName">
                  <el-option
                    v-for="dict in bInspectionPhaseOption"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="申请说明" prop="inspectionContent" class="add-form-input">
                <el-input v-model="form.inspectionContent" placeholder="请输入申请说明" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="报验申请附件">
                <wk-upload-file
                  ref="wkUploadFile"
                  :auto-upload="false"
                  :batch-id="batchId"
                  name="file"
                  multiple
                  accept="*"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>

      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title2" :visible.sync="open2" width="900px" append-to-body>
        <el-form ref="form2" :model="formResults" :rules="rules2" label-width="100px" class="add-form">
          <create-sections title="申请信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="验收阶段" prop="inspectionPhase" class="add-form-input">
                  <el-select
                    v-model="form2.inspectionPhase"
                    placeholder="请选择"
                    clearable
                    size="small"
                    disabled="disabled"
                    style="width: 160px">
                    <el-option
                      v-for="dict in bInspectionPhaseOption"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="申请状态" prop="inspectionType" class="add-form-input">
                  <el-select
                    v-model="form2.inspectionType"
                    placeholder="请选择"
                    clearable
                    size="small"
                    disabled="disabled"
                    style="width: 160px">
                    <el-option
                      v-for="dict in bInspectionTypeOption"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="申请说明" prop="inspectionContent" class="add-form-input">
                  <el-input disabled="disabled" v-model="form2.inspectionContent" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="报验申请附件">
                  <wk-upload-file
                    ref="wkUploadFile"
                    :auto-upload="false"
                    :batch-id="batchId"
                    :disabled="true"
                    name="file"
                    multiple
                    accept="*"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </create-sections>
          <create-sections title="检查信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="检查人" prop="checkPeople" class="add-form-input">
                  <el-input v-model="formCheck.checkPeople" disabled="disabled"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检查时间" prop="checkTime" class="add-form-input">
                  <el-date-picker
                    v-model="formCheck.checkTime"
                    type="date"
                    disabled="disabled"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位工程名称" prop="projectName" class="add-form-input">
                  <el-input v-model="formCheck.projectName" disabled="disabled"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="在施部位" prop="constructionParts" class="add-form-input">
                  <el-input v-model="formCheck.constructionParts" disabled="disabled"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="气象状况" prop="meteorological" class="add-form-input">
                  <el-input v-model="formCheck.meteorological" disabled="disabled"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工程评价" prop="projectEvaluation" class="add-form-input">
                  <el-radio-group v-model="formCheck.projectEvaluation">
                    <el-radio-button
                      disabled="disabled"
                      :label="item.dictValue"
                      :key="item.dictValue"
                      v-for="item in bInspectionProjectAppraiseOption">{{ item.dictLabel }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="主管到岗情况" prop="securityWorkInfo" class="add-form-input">
                  <el-radio-group v-model="formCheck.securityWorkInfo">
                    <el-radio-button
                      disabled="disabled"
                      :label="item.dictValue"
                      :key="item.dictValue"
                      v-for="item in bInspectionDutyStatusOption">{{ item.dictLabel }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="经理到岗情况" prop="managerWorkInfo" class="add-form-input">
                  <el-radio-group v-model="formCheck.managerWorkInfo">
                    <el-radio-button
                      disabled="disabled"
                      :label="item.dictValue"
                      :key="item.dictValue"
                      v-for="item in bInspectionDutyStatusOption">{{ item.dictLabel }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="总监到岗情况" prop="directorWorkInfo" class="add-form-input">
                  <el-radio-group v-model="formCheck.directorWorkInfo">
                    <el-radio-button
                      disabled="disabled"
                      :label="item.dictValue"
                      :key="item.dictValue"
                      v-for="item in bInspectionDutyStatusOption">{{ item.dictLabel }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查附件">
                  <wk-upload-file
                    ref="wkUploadFile"
                    :auto-upload="false"
                    :batch-id="formCheck.batchId"
                    :disabled="true"
                    name="file"
                    multiple
                    accept="*"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </create-sections>
          <create-sections title="检查详情">
            <el-table :data="dataCheckList" >
              <el-table-column label="检查项目" align="center" prop="checkProject" />
              <el-table-column label="检查部位" align="center" prop="checkParts" />
              <el-table-column label="检查情况" align="center" prop="checkSituation" />
              <el-table-column label="检查结果" align="center" prop="checkResults" />
              <el-table-column label="处置情况"  :formatter="checkDisposalFormat" align="center" prop="checkDisposal" />
              <el-table-column label="责任方" align="center" prop="responsibility" />
              <el-table-column label="附件" align="center">
                <template  slot-scope="scope">
                  <div>附件数量{{scope.row.fileNumber?scope.row.fileNumber:0}}</div>
                  <el-button
                    size="mini"
                    type="success"
                    @click="filePageOpen(scope.row.batchId)">查看列表</el-button>
                </template>
              </el-table-column>
            </el-table>
          </create-sections>
          <create-sections :title="bttile">
            <el-row>
              <el-col :span="24">
                <el-form-item :label="resultsTimeText" prop="resultsTime" class="add-form-input">
                  <el-date-picker
                    v-model="formResults.resultsTime"
                    type="date"
                    :disabled="!isAble"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="resultsContentText" prop="resultsContent" class="add-form-input">
                  <el-input v-model="formResults.resultsContent" :disabled="!isAble"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="fileText">
                  <wk-upload-file
                    ref="wkUploadFile"
                    :auto-upload="false"
                    :batch-id="batchId2"
                    :disabled="!isAble"
                    name="file"
                    multiple
                    accept="*"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </create-sections>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm2" :disabled="!isAble">确 定</el-button>
          <el-button @click="cancel2">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog title="附件查看" :visible.sync="open3" width="500px" append-to-body>
        <wk-upload-file
          :ref="'wkUploadFile'+itemIndex"
          name="file"
          multiple
          accept="image/*"
          :autoUpload="true"
          :batchId="batchId3"
          :callbackFile="true"
          :disabled="fjdisabled"></wk-upload-file>
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
import CreateSections from '@/components/CreateSections'
//	import vPinyin from './vuepy.js';

export default {
  name: 'inspection',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList,
    CreateSections
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
      resultsContentText: '验收/整改说明',
      fileText: '验收/整改附件',
      resultsTimeText: '验收/整改日期',
      bttile: '验收/整改信息',
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      // 表格数据
      dataList: [],
      dataCheckList: [],
      bInspectionPhaseOption: [],
      bInspectionTypeOption: [],
      bInspectionProjectAppraiseOption: [],
      bInspectionPlaceOption: [],
      bInspectionDutyStatusOption: [],
      bInspectionDisposalStateOption: [],
      batchIdAll: [],
      // 附件id
      batchId: '',
      batchId2: '',
      batchId3: '',
      isAble: false,
      fjdisabled: true,
      // 弹出层标题
      title: '',
      title2: '',
      // 是否显示弹出层
      open: false,
      open2: false,
      open3: false,
      itemIndex: 0,
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
      // 表单参数
      form2: {},
      formCheck: {},
      formResults: {},
      // 表单校验
      rules: {
        inspectionPhase: [
          { required: true, message: '验收阶段不能为空', trigger: 'blur' }
        ],
        inspectionContent: [
          { required: true, message: '申请说明不能为空', trigger: 'blur' }
        ]

      },
      // 表单校验
      rules2: {
        resultsTime: [
          { required: true, message: '验收结束日期/整改日期不能为空', trigger: 'blur' }
        ],
        resultsContent: [
          { required: true, message: '验收说明/整改说明不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    this.getDicts('b_inspection_phase').then(response => {
      this.bInspectionPhaseOption = response.data
    })
    this.getDicts('b_inspection_type').then(response => {
      this.bInspectionTypeOption = response.data
    })

    this.getDicts('b_inspection_project_appraise').then(response => { // 监督检查工程评价
      this.bInspectionProjectAppraiseOption = response.data
    })
    this.getDicts('b_inspection_place').then(response => { // 监督检查在施部位
      this.bInspectionPlaceOption = response.data
    })
    this.getDicts('b_inspection_duty_status').then(response => { // 监督检查到岗情况
      this.bInspectionDutyStatusOption = response.data
    })
    this.getDicts('b_inspection_disposal_state').then(response => { // 监督检查处置情况
      this.bInspectionDisposalStateOption = response.data
    })
  },
  methods: {
		sortChange(column) {
      sortColumn(this,column);
    },
    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bLabourInspection/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
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
    // 取消按钮
    cancel2() {
      this.open2 = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {}
      this.form2 = {}
      this.formCheck = {}
      this.formResults = {}
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
    },
    /** 修改按钮操作 */
/*    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bLabourInspection/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        this.open = true
        this.title = '修改'
      })
    },*/
    handleSelect(row) {  // 查看用
      this.reset()
      const pipeId = row.id || this.ids
      this.dataCheckList = []
      this.isAble = false
      request({
        url: 'build/bLabourInspection/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form2 = response.data
        this.batchId = response.data.batchId
        if (this.form2.rectificationInfo == '0') {
          this.resultsContentText = '验收说明'
          this.fileText = '验收附件'
          this.resultsTimeText = '验收日期'
          this.bttile = '验收信息'
        } else if (this.form2.rectificationInfo == '1') {
          this.resultsContentText = '整改说明'
          this.fileText = '整改附件'
          this.resultsTimeText = '整改日期'
          this.bttile = '整改信息'
        }
        request({
          url: 'build/bLabourInspectionCheck/selectByInspectionId/' + response.data.id,
          method: 'post'
        }).then(response2 => {
          if (response2.data != null && response2.data != "" ) {
            this.formCheck = response2.data.inspectionCheck
            this.dataCheckList = response2.data.inspectionCheckDetails
            this.dataCheckList.forEach(item => {
              if (item.batchId) {
                this.batchIdAll.push(item.batchId)
              } else {
                let batchId = guid()
                item.batchId = batchId
                this.batchIdAll.push(batchId)
              }
            })
          }
        })
        request({
          url: 'build/bLabourInspectionResults/getPid/' + response.data.id,
          method: 'get'
        }).then(response2 => {
          if (response2.data != null && response2.data != "" ) {
            this.formResults = response2.data
            this.batchId2 = this.formResults.batchId
          }
        })
        this.open2 = true
        this.title2 = '查看'
      })
    },
    handleUpdate(row) {  // 修改用
      this.reset()
      const pipeId = row.id || this.ids
      this.dataCheckList = []
      this.isAble = true
      this.batchId2 = guid()
      request({
        url: 'build/bLabourInspection/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form2 = response.data
        this.batchId = response.data.batchId
        if (this.form2.rectificationInfo == '0') {
          this.resultsContentText = '验收说明'
          this.fileText = '验收附件'
          this.resultsTimeText = '验收日期'
          this.title2 = '验收'
          this.bttile = '验收信息'
        } else if (this.form2.rectificationInfo == '1') {
          this.resultsContentText = '整改说明'
          this.fileText = '整改附件'
          this.resultsTimeText = '整改日期'
          this.title2 = '整改'
          this.bttile = '整改信息'
        }
        request({
          url: 'build/bLabourInspectionCheck/selectByInspectionId/' + response.data.id,
          method: 'post'
        }).then(response2 => {
          if (response2.data != null && response2.data != "" ) {
            this.formCheck = response2.data.inspectionCheck
            this.dataCheckList = response2.data.inspectionCheckDetails
            this.dataCheckList.forEach(item => {
              if (item.batchId) {
                this.batchIdAll.push(item.batchId)
              } else {
                let batchId = guid()
                item.batchId = batchId
                this.batchIdAll.push(batchId)
              }
            })
          }
        })
        request({
          url: 'build/bLabourInspectionResults/getPid/' + response.data.id,
          method: 'get'
        }).then(response2 => {
          if (response2.data != null && response2.data != "" ) {
            this.formResults = response2.data
          }
        })
        this.open2 = true
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
              url: 'build/bLabourInspection/edit',
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
              url: 'build/bLabourInspection/add',
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
    /** 提交按钮 */
    submitForm2: function() {
      this.$refs['form2'].validate(valid => {
        if (valid) {
          this.formResults.batchId = this.batchId2
          this.formResults.inspectionId = this.form2.id
          var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '附件提交异常'
            })
            return
          }
          request({
            url: 'build/bLabourInspectionResults/add',
            method: 'post',
            data: this.formResults,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(response => {
            if (response.code === 0) {
              this.msgSuccess('提交成功')
              this.open2 = false
              this.getList()
            }
          })
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
          url: 'build/bLabourInspection/deleteDetails/' + id,
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
    inspectionPhaseFormat(row, column){
      return this.selectDictLabel(this.bInspectionPhaseOption, row.inspectionPhase)
    },
    inspectionTypeFormat(row, column){
      return this.selectDictLabel(this.bInspectionTypeOption, row.inspectionType)
    },
    checkDisposalFormat(row, column){
      return this.selectDictLabel(this.bInspectionDisposalStateOption, row.checkDisposal)
    },
    // 显示文件上传页面
    filePageOpen(index){
      console.log("1213123131:"+index)
      this.batchId3 = index;
      this.open3 = true;
    },
    getLabourInfocollectionName(value){
    var datas = this.bInspectionPhaseOption
    var value = value
    var actions = []

    Object.keys(datas).some((key) => {
      if (datas[key].dictValue == ('' + value)) {
        actions.push(datas[key].dictLabel)
        return true
      }
    })

    //console.log("123132:"+actions.join(''))
    this.form.remarks = actions.join('')
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
