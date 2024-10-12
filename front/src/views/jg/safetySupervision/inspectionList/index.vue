<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="项目名称" prop="projectId">
          <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small">
            <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="隐患内容" prop="problemDescription">
          <el-input v-model="queryParams.problemDescription" placeholder="请输入隐患内容" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="隐患状态" prop="hiddenState">
          <el-select v-model="queryParams.hiddenState" placeholder="请选择" clearable size="small">
            <el-option v-for="dict in hiddenStateOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查日期" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" @change="dateChange" value-format="yyyy-MM-dd" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="检查日期起" end-placeholder="检查日期止" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

          <!--          <el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList">
        <!-- <el-table-column label="序号" align='center' type="index" width="60" /> -->
        <el-table-column label="项目名称" align="center" prop="projectName" show-overflow-tooltip/>
        <el-table-column label="隐患类别" align="center" prop="category" :formatter="hazardCategoryFormatter" show-overflow-tooltip/>
        <el-table-column label="隐患级别" align="center" prop="level" :formatter="hazardLevelFormatter" show-overflow-tooltip/>
        <el-table-column label="隐患内容" align="center" prop="problemDescription" show-overflow-tooltip/>
        <el-table-column label="隐患状态" align="center" prop="hiddenState" :formatter="hiddenStateFormatter" show-overflow-tooltip/>
        <el-table-column label="检查日期" align="center" prop="checkDate" show-overflow-tooltip/>
        <el-table-column label="检查人" align="center" prop="inspectorId" :formatter="userFormatter" show-overflow-tooltip/>
        <el-table-column label="责任单位" align="center" prop="dutyUnit" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDetails(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" disabled :rules="rules" label-width="100px" class="add-form">
          <create-sections style="overflow:hidden" title="隐患基本信息">
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                <el-select v-model="form.projectId" placeholder="请选择" clearable style="width: 320px">
                  <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
			<el-col :span="12">
			  <el-form-item label="检查人" prop="inspectorId" class="add-form-input">
			    <el-select v-model="form.inspectorId" placeholder="请选择" clearable style="width: 320px">
			      <el-option v-for="dict in userListData" :key="dict.userId" :label="dict.realname" :value="dict.userId" />
			    </el-select>
			  </el-form-item>
			</el-col>
            <el-col :span="12">
              <el-form-item label="检查日期" prop="checkDate" class="add-form-input">
                <el-date-picker v-model="form.checkDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 320px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改期限" prop="repairTimeLimit" class="add-form-input">
                <el-date-picker v-model="form.repairTimeLimit" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 320px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡检部位" prop="place" class="add-form-input" >
                <el-input v-model="form.place" placeholder="请输入巡检部位" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="问题描述" prop="problemDescription" class="add-form-input">
                <el-input v-model="form.problemDescription" placeholder="请输入问题描述" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="巡检结论" prop="inspectionConclusion" class="add-form-input">
                <el-input v-model="form.inspectionConclusion" placeholder="请输入巡检结论" />
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12">
              <el-form-item label="项目编号" prop="projectCode" class="add-form-input">
                <el-input v-model="form.projectCode" placeholder="请输入项目编号" />
              </el-form-item>
            </el-col> -->

            <!-- <el-col :span="12">
              <el-form-item label="隐患编号" prop="dangerCode" class="add-form-input">
                <el-input v-model="form.dangerCode" placeholder="请输入隐患编号" />
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="检查类型" prop="routineCheck" class="add-form-input">
                <el-select v-model="form.routineCheck" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option v-for="dict in chekcTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐患类别" prop="category" class="add-form-input">
                <el-select v-model="form.category" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option v-for="dict in hazardCategoryOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐患等级" prop="level" class="add-form-input">
                <el-select v-model="form.level" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option v-for="dict in hazardLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
			<el-col :span="12">
				<el-form-item label="危险源" prop="dangerSourceId">
				  <el-select :ref="'selectTree'" v-model="form.dangerSourceId" filterable style="width: 320px"
										  placeholder="请选择">
					  <el-option :value="form.dangerSourceId" :label="form.dangerName"
								 style="width: 260px;height:120px;overflow:auto;background-color: #fff">
						  <el-tree
								  node-key="dangerSourceId"
								  :data="dangerOptions"
								  :props="defaultProps"
								  @node-click="handleNodeClick"
						  ></el-tree>
					  </el-option>
				  </el-select>
				</el-form-item>
			</el-col>
            <el-col :span="12">
              <el-form-item label="检查结果" prop="checkResult" class="add-form-input">
                <el-select v-model="form.checkResult" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option label="无需整改" value="无需整改" />
                  <el-option label="待整改" value="待整改" />
                  <el-option label="合格" value="合格" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="实际整改人" prop="rectificationPeople" class="add-form-input">
                <el-input v-model="form.rectificationPeople" placeholder="请输入实际整改人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改人电话" prop="rectifyPersonPhone" class="add-form-input">
                <el-input v-model.number="form.rectifyPersonPhone" placeholder="请输入整改人电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="责任单位" prop="dutyUnit" class="add-form-input">
                <el-input v-model="form.dutyUnit" placeholder="请输入责任单位" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="社会信用代码" prop="dutyUnitCode" class="add-form-input">
                <el-input v-model="form.dutyUnitCode" placeholder="请输入统一社会信用代码" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="劳务班组" prop="serviceClass" class="add-form-input">
                <el-select v-model="form.serviceClass" @change="teamChange" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option v-for="dict in teamOptionsList" :key="dict.id" :label="dict.teamName" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="通知人" prop="informPerson" class="add-form-input">
                <el-select v-model="form.informPerson" @change="personChange" placeholder="请选择" clearable size="small" style="width: 320px">
                  <el-option v-for="dict in teamPersonAll" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改时间" prop="rectifyDate" class="add-form-input">
                <el-date-picker v-model="form.rectifyDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 320px" />
              </el-form-item>
            </el-col>
			<el-col :span="12">
			  <el-form-item label="整改效果说明" prop="rectifyEffect" class="add-form-input">
			    <el-input v-model="form.rectifyEffect" placeholder="请输入整改效果说明" />
			  </el-form-item>
			</el-col>
			<el-col :span="12">
			  <el-form-item label="处罚信息" prop="fineInfo" class="add-form-input">
			    <el-input v-model="form.fineInfo" placeholder="请输入处罚信息" />
			  </el-form-item>
			</el-col>
			<el-col :span="12">
			  <el-form-item label="补充信息" prop="supplementInfo" class="add-form-input">
			    <el-input v-model="form.supplementInfo" placeholder="请输入补充信息" />
			  </el-form-item>
			</el-col>
            <el-col :span="12">
              <el-form-item label="复检时间" prop="recheckDate" class="add-form-input">
                <el-date-picker v-model="form.recheckDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 320px" />
              </el-form-item>
            </el-col>
			<el-col :span="12">
			  <el-form-item label="复检人" prop="recheckPeopleId" class="add-form-input">
			    <el-select v-model="form.recheckPeopleId" placeholder="请选择" clearable style="width: 320px">
			      <el-option v-for="dict in userListData" :key="dict.userId" :label="dict.realname" :value="dict.userId" />
			    </el-select>
			  </el-form-item>
			</el-col>
			<el-col :span="12">
			  <el-form-item label="复检结论" prop="recheckConclusion" class="add-form-input">
			    <el-input v-model="form.recheckConclusion" placeholder="请输入复检结论" />
			  </el-form-item>
			</el-col>
          </create-sections>
        </el-form>
		<!--图片上传-->
		<wk-upload-img
		  :ref="'wkUploadImg'"
		  name="file"
		  multiple
		  accept="image/*"
		  list-type="picture-card"
		  :autoUpload="false"
		  :title="'图片上传'"
		  :batchId="batchId"
		></wk-upload-img>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid } from '@/utils'
import WkUploadImg from '@/components/CreateCom/WkUploadImg'
import {userListAPI} from '@/api/common' // 直属上级接口  用户接口
import CreateSections from '@/components/CreateSections'
import {queryDangerTreeList} from '@/views/build/safety/api/dangerSource'
import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import {getTeamList, selectPersonByTeam} from "../api/inspectionList"

export default {
  name: 'supervision',
  components: {
    XrHeader,
    SelectCol,
    CreateSections,
	WkUploadImg
  },
  data () {
    var bargainMoney = (rule, value, callback) => {
      // rule 对应使用bargainMoney自定义验证的 对象
      // value 对应使用bargainMoney自定义验证的 数值
      // callback 回调函数
      const r = /^1[34578]\d{9}$/; // 手机号
      if (value != "" && value != undefined) {
        if (r.test(value) == false) {
          callback(new Error("请输入正确的手机号"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 310,
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
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      sjtbDate: '',
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      	projectId: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
		inspectorId: [
		  {required: true, message: '必填项不得为空', trigger: 'blur'}
		],
		checkDate: [
		  {required: true, message: '必填项不得为空', trigger: 'blur'}
		],
		repairTimeLimit: [
		  {required: true, message: '必填项不得为空', trigger: 'blur'}
		],
		place: [
		  {required: true, message: '必填项不得为空', trigger: 'blur'}
		],
      	problemDescription: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	inspectionConclusion: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	level: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	dangerSourceId: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	rectificationPeople: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	rectifyDate: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	rectifyEffect: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	recheckDate: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	recheckPeopleId: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	recheckConclusion: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	rectifyPersonPhone: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	dutyUnit: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	dutyUnitCode: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	checkResult: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	serviceClass: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	informPerson: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	fineInfo: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	supplementInfo: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	supplementInfo: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	routineCheck: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	],
      	category: [
      	  {required: true, message: '必填项不得为空', trigger: 'blur'}
      	]
      },
      //项目列表
      projectOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      //隐患级别
      hazardLevelOptions: [],
      //隐患类别
      hazardCategoryOptions: [],
	  // 人员数据
	  userListData: [],
      //检查类型
      chekcTypeOptions: [],
	  // 问题类型相关
	  dangerOptions: [],
	  defaultProps: {
		  children: 'children',
		  label: 'dangerName'
	  },
	  // 班组数据
	  teamOptionsList:[],
	  // 班组人员数据
	  teamPersonAll: [],
	  // 隐患状态
	  hiddenStateOptions: [
		  {
			  dictValue: '1',
			  dictLabel: '整改中'
		  },
		  {
			  dictValue: '2',
			  dictLabel: '复检中'
		  },
		  {
			  dictValue: '3',
			  dictLabel: '已完成'
		  },
	  ],
    }
  },
  watch: {

  	form(val) {
		if (this.form.serviceClass) {
			this.getTeamPersonList(this.form.serviceClass)
		}
  	},
  },
  created () {
    //查询项目列表
    request({
      url: 'build/projectInfo/queryProjectList',
      method: 'post',
      data: {},
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    }).then(response => {
      this.projectOptions = response.data
    })
    //查询隐患类别
    this.getDicts('b_hazard_level').then(response => {
      this.hazardLevelOptions = response.data
    })
    //查询隐患等级
    this.getDicts('b_hazard_category').then(response => {
      this.hazardCategoryOptions = response.data
    })
    //查询检查类型
    this.getDicts('b_inspect_type').then(response => {
      this.chekcTypeOptions = response.data
    })
	// 查询班组数据
	// 查询班组数据
	getTeamList().then(res => {
		this.teamOptionsList = res.data
	})
	this.getUserList()
	this.getQuestionList()
    //查询数据列表
    this.getList()
  },
  mounted () {

  },
  methods: {
    dateChange () {
      if (this.sjtbDate == null) {
        this.queryParams.inspectStartDate = ''
        this.queryParams.inspectEndDate = ''
      }
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      if (this.sjtbDate != undefined && this.sjtbDate != '' && this.sjtbDate.length != 0) {
        this.queryParams.inspectStartDate = this.sjtbDate[0]
        this.queryParams.inspectEndDate = this.sjtbDate[1]
      }
      request({
        url: 'build/safetyInspectionDetail/selectListByPro',
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
    //隐患类型格式化
    hazardCategoryFormatter(row, column) {
      return this.selectDIYLabel(this.hazardCategoryOptions, row.category, 'dictValue', 'dictLabel')
    },
    //隐患级别格式化
    hazardLevelFormatter(row, column) {
      return this.selectDIYLabel(this.hazardLevelOptions, row.level, 'dictValue', 'dictLabel')
    },
    //隐患级别格式化
    hiddenStateFormatter(row, column) {
      return this.selectDIYLabel(this.hiddenStateOptions, row.hiddenState, 'dictValue', 'dictLabel')
    },
    //隐患级别格式化
    userFormatter(row, column) {
      return this.selectDIYLabel(this.userListData, row.inspectorId, 'userId', 'realname')
    },
    //列格式化
    selectDIYLabel (datas, value, dataValue, dataLablel) {
      var actions = []
      Object.keys(datas).some((key) => {
        if (datas[key][dataValue] == (typeof datas[key][dataValue] == 'number' ? (0 + value) : ('' + value))) {
          actions.push(datas[key][dataLablel])
          return true
        }
      })
      return actions.join('')
    },
	// 查询用户
	getUserList() {
	  var params = {
		  pageType: 0
	  }
	  userListAPI(params).then(res => {
		  this.userListData = res.data.list
	  })
	  .catch(() => {

	  })
	},
	// 查询质量问题库所有数据
	getQuestionList() {
	  queryDangerTreeList().then(res => {
		  // 问题类型列表
		  this.dangerOptions = res.data
	  })
	},
	// 劳务班组相关
	teamChange(value) {
	  this.form.informPerson = ''
	  this.getTeamPersonList(value)
	},
	getTeamPersonList(teamId) {
	  selectPersonByTeam(teamId).then(res => {
		  this.teamPersonAll = res.data
		  this.$forceUpdate()
	  })
	},
	personChange(value) {
		this.$forceUpdate()
	},
	handleNodeClick:function(data,node,obj){
		if(node.children){

		}else{
			this.form.dangerSourceId = node.key
			this.form.dangerName = node.label
			var ref = 'selectTree';

			this.$refs[ref].blur();

		}
	},
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.sjtbDate = ''
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.batchId = guid()
      this.open = true
      this.title = '新增'
      this.membersList = []
    },
    // 表单重置
    reset () {
      this.form = {}
      this.resetForm('form')
    },
    /** 修改按钮操作 */
    handleDetails (row) {
      this.reset()
      var id = row.id
      request({
        url: 'build/safetyInspectionDetail/selectDetailByIdPro/' + id,
        method: 'post'
      }).then(res => {
        this.form = res.data
        this.batchId = res.data.batchId
        this.open = true
        this.title = '查看详情'
      })
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
