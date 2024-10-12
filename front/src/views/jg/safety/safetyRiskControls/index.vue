<template>
  <div class="main">
    <!--    <xr-header
      icon-class="wk wk-project"
      icon-color="#2362FB"
      label="安全资料管理" />-->

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
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
        <el-form-item label="分部分项" prop="subItemWorksParts">
          <el-select v-model="queryParams.subItemWorksParts" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in subItemWorksPartsOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="风险类型" prop="riskCategory">
          <el-select v-model="queryParams.riskCategory" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option label="动态风险" value="0" />
            <el-option label="静态风险" value="1" />
          </el-select>
        </el-form-item> -->

        <!--        <el-form-item label="事故类型" prop="accidentType" >-->
        <!--          <el-select-->
        <!--            v-model="queryParams.accidentType"-->
        <!--            placeholder="请选择"-->
        <!--            clearable-->
        <!--            size="small"-->
        <!--            style="width: 160px">-->
        <!--            <el-option-->
        <!--              v-for="dict in accidentTypeOptions"-->
        <!--              :key="dict.dictValue"-->
        <!--              :label="dict.dictLabel"-->
        <!--              :value="dict.dictValue" />-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="管控等级" prop="riskLevel">
          <el-select v-model="queryParams.riskLevel" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in riskLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布时间" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="发布时间起" end-placeholder="发布时间至" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="解除状态" prop="relieveStatus">
          <el-select v-model="queryParams.relieveStatus" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option label="未解除" value="0" />
            <el-option label="已解除" value="1" />
          </el-select>
        </el-form-item>

        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

  <!--        <el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
        <el-table-column label="序号" align='center' type="index" width="60" />

        <!-- <el-table-column label="风险名称" align="center" prop="riskName" />
        <el-table-column label="风险类型" :formatter="riskCategoryFc" align="center" prop="riskCategory" />
        <el-table-column label="工作内容" align="center" prop="content" />
        <el-table-column label="标准要求" align="center" prop="standardRequire" />
        <el-table-column label="事故类型" :formatter="accidentTypeOptionsFc" align="center" prop="accidentType" /> -->
        <el-table-column label="项目名称" :formatter="projectNameZc" align="center" prop="projectId"  width="200px" sortable="custom"/>
        <el-table-column label="分部分项" :formatter="SubItemWorksPartsFc" align="subItemWorksParts" prop="riskName" :show-overflow-tooltip="true"  sortable="custom"/>
        <el-table-column label="风险类型" align="center" prop="riskType" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="风险辨识点" align="center" prop="riskIdentPoint" :show-overflow-tooltip="true" sortable="custom" width="140px"/>
        <el-table-column label="事故类型" align="center" prop="accidentType" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="风险分级/标识" :formatter="riskLevelIdentFc" align="center" prop="riskLevelIdent" :show-overflow-tooltip="true" sortable="custom" width="140px"/>

        <el-table-column label="管控等级" :formatter="riskLevelOptionsFc" align="center" prop="riskLevel" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="责任人" align="center" prop="dutyPerson" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="发布时间" :formatter="formatterTime" align="center" prop="publishDate" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="风险解除状态" :formatter="relieveStatusFc" align="center" prop="relieveStatus" :show-overflow-tooltip="true" sortable="custom" width="140px"/>
        <el-table-column label="工程措施" align="center" prop="engineeringMeasure" :show-overflow-tooltip="true" sortable="custom"/>
        <el-table-column label="个体防护措施" align="center" prop="individualProtection" :show-overflow-tooltip="true" sortable="custom" width="140px"/>
        <el-table-column label="设备设施名称" align="center" prop="deviceType" :show-overflow-tooltip="true" sortable="custom" width="140px"/>

<!--        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>-->
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body @close="handleClose">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="分部分项" prop="subItemWorksParts" class="add-form-input">
                <el-select v-model="form.subItemWorksParts" filterable placeholder="请选择" :disabled="updateDisableT" clearable size="small" style="width: 330px" @change="loadRiskType">
                  <el-option v-for="dict in subItemWorksPartsOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" style="max-width: 330px !important" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风险类型" prop="riskType" class="add-form-input">
                <el-select v-model="form.riskType" filterable placeholder="请选择" :disabled="updateDisableT" clearable size="small" style="width: 330px" @blur="blurRiskType" @change="loadRiskIdentPoint">
                  <el-option v-for="dict in riskTypeOptions" :key="dict.riskType" :label="dict.riskType" :value="dict.riskType" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风险辨识点" prop="riskIdentPoint" class="add-form-input">
                <el-select v-model="form.riskIdentPoint" popper-class="sjnrxl" filterable placeholder="请选择" :disabled="updateDisableT" clearable size="small" style="width: 330px" @blur="blurRiskLevelIdentDetail" @change="loadRiskDetail">
                  <el-option v-for="dict in RiskIdentPointOption" :key="dict.id" :label="dict.riskIdentPoint" :value="dict.riskIdentPoint" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事故类型" prop="accidentType" class="add-form-input">
                <el-input v-model="form.accidentType" placeholder="请输入事故类型" :disabled="disabledT" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风险分级/标识" prop="riskLevelIdent" class="add-form-input" label-width="105px">
                <el-select v-model="form.riskLevelIdent" placeholder="请选择" :disabled="disabledT" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in riskLevelIdentOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="防范措施" prop="preventiveMeasures">
                <el-input type="textarea" :rows="2" v-model="form.preventiveMeasures" :disabled="disabledT" placeholder="请输入防范措施"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="工作依据" prop="workingBasis">
                <el-input type="textarea" :rows="2" v-model="form.workingBasis" :disabled="disabledT" placeholder="请输入工作依据"></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12">
              <el-form-item label="风险名称" prop="riskName" class="add-form-input">
                <el-input v-model="form.riskName" placeholder="请输入风险名称" />
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="12">
              <el-form-item label="风险类型" prop="riskCategory" class="add-form-input">
                <el-select v-model="form.riskCategory" placeholder="请选择" clearable size="small" style="width: 160px" @change="dataload">
                  <el-option label="动态风险" value="0" />
                  <el-option label="静态风险" value="1" />
                </el-select>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="24">
              <el-form-item :label="contentTitle" prop="content">
                <el-input type="textarea" :rows="2" v-model="form.content" placeholder=""></el-input>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="24">
              <el-form-item :label="standardRequireTitle" prop="standardRequire">
                <el-input type="textarea" :rows="2" v-model="form.standardRequire" placeholder=""></el-input>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="12">
              <el-form-item label="事故类型" prop="accidentType" class="add-form-input">
                <el-select v-model="form.accidentType" placeholder="请选择" clearable size="small" style="width: 160px">
                  <el-option v-for="dict in accidentTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="管控等级" prop="riskLevel" class="add-form-input">
                <el-select v-model="form.riskLevel" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in riskLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="责任人" prop="dutyPerson" class="add-form-input">
                <el-input v-model="form.dutyPerson" placeholder="请输入责任人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布时间" prop="publishDate" class="add-form-input">
                <el-date-picker v-model="form.publishDate" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" style="width: 330px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风险解除状态" prop="relieveStatus" class="add-form-input">
                <el-select v-model="form.relieveStatus" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option label="未解除" value="0" />
                  <el-option label="已解除" value="1" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="工程措施" prop="engineeringMeasure">
                <el-input type="textarea" :rows="2" v-model="form.engineeringMeasure" placeholder="请输入工程措施"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="个体防护措施" prop="safetyDataDescribe">
                <el-input type="textarea" :rows="2" v-model="form.individualProtection" placeholder="请输入个体防护措施"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="设备设施名称" prop="deviceType" class="add-form-input">
                <el-input v-model="form.deviceType" placeholder="请输入设备设施名称" />
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
  name: 'SafetyRiskControls',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList
  },
  data () {
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
      // 隐藏设备设施名称
      deviceTypeShow: true,
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      // 表格数据
      dataList: [],
      // 项目信息列表
      projectInfoList: [],
      // 事故类型  字典表b_safety_accidentType
      accidentTypeOptions: [],
      // 管控等级  字典表b_safety_riskRating
      riskLevelOptions: [],
      // 附件id
      batchId: '',
      // 工作内容（静态风险：检查内容）（动态风险：工作内容—活动作业内容）
      contentTitle: '工作内容',
      // 标准要求（静态独有：标准要求）（动态独有：风险点描述）
      standardRequireTitle: '标准要求',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
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
        subItemWorksParts: [
          { required: true, message: '分部分项不能为空', trigger: 'blur' }
        ],
        riskType: [
          { required: true, message: '风险类型不能为空', trigger: 'blur' }
        ],
        riskIdentPoint: [
          { required: true, message: '风险辨识点不能为空', trigger: 'blur' }
        ],
        accidentType: [
          { required: true, message: '事故类型不能为空', trigger: 'blur' }
        ],
        riskLevelIdent: [
          { required: true, message: '风险分级/辨识不能为空', trigger: 'blur' }
        ],
        preventiveMeasures: [
          { required: true, message: '防范措施不能为空', trigger: 'blur' }
        ],
        workingBasis: [
          { required: true, message: '工作依据不能为空', trigger: 'blur' }
        ],
        riskLevel: [
          { required: true, message: '管控等级不能为空', trigger: 'blur' }
        ],
        dutyPerson: [
          { required: true, message: '责任人不能为空', trigger: 'blur' }
        ],
        publishDate: [
          { required: true, message: '发布时间不能为空', trigger: 'blur' }
        ],
        relieveStatus: [
          { required: true, message: '风险解除状态不能为空', trigger: 'blur' }
        ],
        engineeringMeasure: [
          { required: true, message: '工程措施不能为空', trigger: 'blur' }
        ],
        individualProtection: [
          { required: true, message: '个体防护措施不能为空', trigger: 'blur' }
        ],
        // deviceType: [
        //   { required: true, message: '设备设施名称不能为空', trigger: 'blur' }
        // ]

      },
      riskTypeOptions: [],
      subItemWorksPartsOption: [],
      RiskIdentPointOption: [],
      riskLevelIdentOption: [],
      disabledT: true,
      updateDisableT: false,
    }
  },

  created () {
    this.getList()
    this.getProjectInfoList()
    // 事故类型
    this.getDicts('b_safety_accidentType').then(response => {
      this.accidentTypeOptions = response.data
    })
    // 管控等级
    this.getDicts('b_safety_riskRating').then(response => {
      this.riskLevelOptions = response.data
    })
    //分部分项
    this.getDicts('b_sub_item_works_parts').then(response => {
      this.subItemWorksPartsOption = response.data
    })
    // 风险等级
    this.getDicts('b_risk_level_ident').then(response => {
      this.riskLevelIdentOption = response.data
    })
  },
  methods: {

    //获取风险类型
    loadRiskType () {
      let jsonArray = ['riskType', 'riskIdentPoint', 'accidentType', 'riskLevelIdent', 'preventiveMeasures', 'workingBasis', 'templateId']
      let arrayArray = ['riskTypeOptions', 'RiskIdentPointOption']
      this.formInit(jsonArray, arrayArray)

      this.loading = true
      let obj = {};
      obj.subItemWorksParts = this.form.subItemWorksParts;
      obj.isGroupName = 'risk_type'
      request({
        url: 'build/safetyRiskControlsTemplate/templateList',
        method: 'post',
        data: obj,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.riskTypeOptions = response.data.list
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    //获取风险辨识点
    loadRiskIdentPoint (e) {
      let jsonArray = ['riskIdentPoint', 'accidentType', 'riskLevelIdent', 'preventiveMeasures', 'workingBasis', 'templateId']
      let arrayArray = ['RiskIdentPointOption']
      this.formInit(jsonArray, arrayArray)

      this.loading = true
      let obj = {};
      obj.riskType = e;
      obj.subItemWorksParts = this.form.subItemWorksParts
      request({
        url: 'build/safetyRiskControlsTemplate/templateList',
        method: 'post',
        data: obj,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.RiskIdentPointOption = response.data.list;
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    blurRiskType (e) {
      if (this.riskTypeOptions.length == 0) this.$message.error("请选择分部分项");
    },
    blurRiskLevelIdentDetail (e) {
      if (this.RiskIdentPointOption.length == 0) this.$message.error("请选择风险类型");
    },
    //获取风险模板详情
    loadRiskDetail (e) {
      let value = '';
      this.RiskIdentPointOption.forEach(item => {
        if (item.riskIdentPoint == e) {
          value = item.id;
        }
      })
      this.form.templateId = value;
      this.loading = true
      request({
        url: 'build/safetyRiskControlsTemplate/getById/' + value,
        method: 'get',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.form.accidentType = response.data.accidentType;
        this.form.riskLevelIdent = response.data.riskLevelIdent;
        this.form.preventiveMeasures = response.data.preventiveMeasures;
        this.form.workingBasis = response.data.workingBasis;

        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bSafetyRiskControls/list',
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
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
      this.riskTypeOptions = [];
      this.RiskIdentPointOption = [];

    },
    handleClose () {
      this.open = false
      this.reset()
      this.riskTypeOptions = [];
      this.RiskIdentPointOption = [];

    },
    // 分部分项字典格式化
    SubItemWorksPartsFc (row, column) {
      return this.selectDictLabel(this.subItemWorksPartsOption, row.subItemWorksParts);
    },
    //风险标识字典格式化
    riskLevelIdentFc (row, column) {
      return this.selectDictLabel(this.riskLevelIdentOption, row.riskLevelIdent);
    },
    /** 提交按钮 */
    submitForm: function () {

      this.$refs['form'].validate(valid => {
        if (valid) {
          this.form.accidentType = '';
          if (this.form.id != undefined) {
            request({
              url: 'build/bSafetyRiskControls/edit',
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
              url: 'build/bSafetyRiskControls/add',
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
    formatterTime (row, column) {
      let data = row[column.property]
      return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },

    // 表单重置
    reset () {
      this.form = {}
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery () {
      if (this.sjtbDate != null&&this.sjtbDate != '') {
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
      }else{
      	this.queryParams.createBy=''
      	this.queryParams.updateBy=''
      }
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.sjtbDate = ''
      //this.resetForm('queryForm')
      this.queryParams = {};
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.open = true
      this.title = '新增'
      this.batchId = guid()
      this.updateDisableT = false
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bSafetyRiskControls/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        // if (this.form.riskCategory == '0') {
        //   this.contentTitle = "工作内容—活动作业内容"
        //   this.standardRequireTitle = "风险点描述"
        //   this.deviceTypeShow = true
        // } else if (this.form.riskCategory == '1') {
        //   this.contentTitle = "检查内容"
        //   this.standardRequireTitle = "标准要求"
        //   this.deviceTypeShow = false

        // } else {
        //   this.contentTitle = "工作内容"
        //   this.standardRequireTitle = "标准要求"
        //   this.deviceTypeShow = true
        // }
        this.open = true
        this.title = '修改'
        this.updateDisableT = true
        this.form.templateId = response.data.templateId;
      })
    },

    /** 删除按钮操作 */
    handleDelete (row) {
      const id = row.id || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return request({
          url: 'build/bSafetyRiskControls/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function () { })
    },

    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('system/dict/type/export', {
    //     ...this.queryParams
    //   }, `type_${new Date().getTime()}.xlsx`)
    // },
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
    //表单字段以及数组初始化
    formInit (jsonArray, arrayArray) {
      jsonArray.forEach(item => {
        this.form[item] = ''
      })
      arrayArray.forEach(item => {
        this[item] = [];
      })
    },
    // 选项类型
    accidentTypeOptionsFc (row, column) {
      return this.selectDictLabel(this.accidentTypeOptions, row.accidentType)
    },
    // 选项类型
    riskLevelOptionsFc (row, column) {
      return this.selectDictLabel(this.riskLevelOptions, row.riskLevel)
    },
    // 选项类型
    riskCategoryFc (row, column) {
      let data = row[column.property]
      if (data == '1') {
        return "静态风险"
      } else {
        return "动态风险"
      }
    },
    // 选项类型
    relieveStatusFc (row, column) {
      let data = row[column.property]
      if (data == '1') {
        return "已解除"
      } else {
        return "未解除"
      }
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
    // 项目名称
    projectNameZc(row, column) {
      var datas = this.projectInfoList
      var value = row.projectId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].name)
          return true
        }
      })
      return actions.join('')
    },
    sortChange(column) {
      sortColumn(this,column);
    },
    // dataload (value) {
    //   if (value == '0') {
    //     this.contentTitle = "工作内容—活动作业内容"
    //     this.standardRequireTitle = "风险点描述"
    //     this.deviceTypeShow = true
    //   } else if (value == '1') {
    //     this.contentTitle = "检查内容"
    //     this.standardRequireTitle = "标准要求"
    //     this.deviceTypeShow = false

    //   } else {
    //     this.contentTitle = "工作内容"
    //     this.standardRequireTitle = "标准要求"
    //     this.deviceTypeShow = true
    //   }
    // },

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
.timecom {
  margin-top: 4px;
  height: 32px;
}
.timecom span {
  width: 20px;
}
</style>

<style lang="less">
.sjnrxl {
  max-width: 330px;
  .el-select-dropdown__item {
    overflow: visible;
    display: block;
    span {
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>

