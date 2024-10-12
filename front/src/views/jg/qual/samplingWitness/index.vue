<template>
  <div class="main">
<!--    <xr-header-->
<!--      icon-class="wk wk-project"-->
<!--      icon-color="#2362FB"-->
<!--      label="安全应急管理" />-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="100px" style="padding-top: 20px;padding-left:10px;">

		<el-form-item label="项目名称" prop="projectId">
		  <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
		    <el-option
		      v-for="item in projectOptions"
		      :key="item.id"
		      :label="item.name"
		      :value="item.id">
		    </el-option>
		  </el-select>
		</el-form-item>
        <el-form-item label="取样人" prop="samplers">
          <el-input
            v-model="queryParams.samplers"
            placeholder="请输入取样人"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
<!--        <el-form-item label="取样单位" prop="samplingUnit">-->
<!--          <el-input-->
<!--            v-model="queryParams.samplingUnit"-->
<!--            placeholder="请输入取样单位"-->
<!--            clearable-->
<!--            size="small"-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="见证人" prop="witness">
          <el-input
            v-model="queryParams.witness"
            placeholder="请输入见证人"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
<!--        <el-form-item label="见证单位" prop="witnessUnit">-->
<!--          <el-input-->
<!--            v-model="queryParams.witnessUnit"-->
<!--            placeholder="请输入见证单位"-->
<!--            clearable-->
<!--            size="small"-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="试件类型" prop="specimenType" label-width="100px" >
          <el-select
            v-model="queryParams.specimenType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px">
            <el-option
              v-for="dict in qualSpecimenType"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="取样日期">
        <el-date-picker
          class="timecom"
          v-model="sjtbDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="取样日期起"
          end-placeholder="取样日期止"
          :picker-options="pickerOptions">
        </el-date-picker>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!--<el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

          <!-- <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button> -->
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="dataList" :height="tableHeight" @sort-change="sortChange">
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed ></el-table-column>
        <el-table-column label="项目名称" align="center" prop="projectName" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="取样人" align="center" prop="samplers" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="取样单位" align="center" prop="samplingUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="见证人" align="center" prop="witness" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="见证单位" align="center" prop="witnessUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column :formatter="qualSpecimenTypeFc" label="试件类型" align="center" prop="specimenType" show-overflow-tooltip sortable="custom"/>
        <el-table-column :formatter="formatterTime" label="取样日期" align="center" prop="samplingDate" show-overflow-tooltip sortable="custom"/>
       <!-- <el-table-column label="试件详细信息" align="center" prop="specimenDetails" />-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button> -->
			<el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleDetails(scope.row)"
            >查看</el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-picture-outline"
              @click="qrcode(scope.row.id)"
            >二维码</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button> -->
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
        <el-form ref="form" :disabled="isDetails" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-row>
			<el-col :span="12">
			  <el-form-item label="项目名称" prop="projectId" class="add-form-input">
				<el-select disabled v-model="form.projectId" placeholder="请选择" clearable style="width: 320px">
				  <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
				</el-select>
			  </el-form-item>
			</el-col>
            <el-col :span="12">
              <el-form-item label="取样人" prop="samplers" class="add-form-input">
                <el-input v-model="form.samplers" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="取样单位" prop="samplingUnit" class="add-form-input">
                <el-input v-model="form.samplingUnit" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="见证人" prop="witness" class="add-form-input">
                <el-input v-model="form.witness" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="见证单位" prop="witnessUnit" class="add-form-input">
                <el-input v-model="form.witnessUnit" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="取样日期" prop="samplingDate" class="add-form-input">
                <el-date-picker
                  v-model="form.samplingDate"
                  type="date"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="试件类型" prop="specimenType" class="add-form-input">
                <el-select
                  v-model="form.specimenType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  style="width: 160px">
                  <el-option
                    v-for="dict in qualSpecimenType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="过磅单号" prop="weighingOrderOn" >
                <el-input v-model="weighingForm.weighingOrderOn" :disabled="true" style="width:75%" />
                <el-button type="primary" round icon="el-icon-search" size="mini" @click="selectWeighing(weighingForm.projectId)" >查询</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="物料名称" prop="materialName">
                <el-input v-model="weighingForm.materialName" :disabled="true" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规格型号" prop="specificationsType">
                <el-input v-model="weighingForm.specificationsType" :disabled="true" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发货单位" prop="deliveryUnit">
                <el-input v-model="weighingForm.deliveryUnit" :disabled="true" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="见证取样附件">
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
            <el-col :span="24">
              <!-- <el-button
                class="xr-btn--orange"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                style="float: right;margin-bottom: 5px"
                @click="handleAddMembers" >新建</el-button> -->

              <el-table v-loading="loading" :data="membersList" >
                <el-table-column :formatter="qualProcessTypeFc" label="过程类型" align="center" prop="processType" />
                <el-table-column label="情况说明" align="center" prop="description" />
                <el-table-column label="操作人" align="center" prop="operationUser" />
                <el-table-column :formatter="formatterTime" label="操作时间" align="center" prop="operationTime" />
                <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
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
                </el-table-column> -->
              </el-table>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>


      <el-dialog :title="titleMembers" :visible.sync="membersOpen" width="900px" append-to-body>
        <el-form ref="formMembers" :model="formMembers" :rules="rulesMembers" label-width="100px" class="add-form">
          <el-form-item label="过程类型" prop="processType" class="add-form-input">
            <el-select
              v-model="formMembers.processType"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px">
              <el-option
                v-for="dict in qualProcessType"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="情况说明" prop="description">
            <el-input type="textarea" :rows="2" v-model="formMembers.description" placeholder="请输入情况说明"></el-input>
          </el-form-item>
          <el-form-item label="见证取样过程附件">
            <wk-upload-file
              ref="wkUploadFile"
              :auto-upload="false"
              :batch-id="batchIdprocess"
              name="file"
              multiple
              accept="*"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFormMembers">确 定</el-button>
          <el-button @click="cancelMembers">取 消</el-button>
        </div>
      </el-dialog>


      <el-dialog :title="titleWeighing" :visible.sync="WeighingOpen" width="1200px" append-to-body>
        <el-form v-show="showSearch" ref="WeighingqueryForm" :model="WeighingqueryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
          <el-form-item label="过磅单号" prop="weighingOrderOn">
            <el-input
              v-model="WeighingqueryParams.weighingOrderOn"
              placeholder="请输入过磅单号"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>


          <el-form-item label="车牌号" prop="licenseNumber" >
            <el-input
              v-model="WeighingqueryParams.licenseNumber"
              placeholder="请输入车牌号"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item label="物料名称" prop="materialName" >
            <el-input
              v-model="WeighingqueryParams.materialName"
              placeholder="请输入物料名称"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item label="规格型号" prop="specificationsType" >
            <el-input
              v-model="WeighingqueryParams.specificationsType"
              placeholder="请输入规格型号"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item label="发货单位" prop="deliveryUnit" >
            <el-input
              v-model="WeighingqueryParams.deliveryUnit"
              placeholder="请输入发货单位"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item label="收货单位" prop="thegoodsUnit" >
            <el-input
              v-model="WeighingqueryParams.thegoodsUnit"
              placeholder="请输入收货单位"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item label="运输单位" prop="transportUnit" >
            <el-input
              v-model="WeighingqueryParams.transportUnit"
              placeholder="请输入运输单位"
              clearable
              size="small"
              @keyup.enter.native="handleQueryWeighing"
            />
          </el-form-item>
          <el-form-item style="float: right">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryWeighing">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQueryWeighing">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="WeighingList" highlight-current-row @current-change="selectListData">
          <el-table-column label="过磅单号" align="center" prop="weighingOrderOn" />
          <el-table-column label="车牌号" align="center" prop="licenseNumber" />
          <el-table-column label="物料名称" align="center" prop="materialName" />
          <el-table-column label="物料类型" :formatter="qualSpecimenTypeFc2" align="center" prop="materialType" />
          <el-table-column label="规格型号" align="center" prop="specificationsType" />
          <el-table-column label="发货单位" align="center" prop="deliveryUnit" />
          <el-table-column label="收货单位" align="center" prop="thegoodsUnit" />
          <el-table-column label="运输单位" align="center" prop="transportUnit" />
          <el-table-column label="司机" align="center" prop="theDriver" />
        </el-table>
        <div class="p-contianer">
          <el-pagination
            :current-page="WeighingcurrentPage"
            :page-sizes="WeighingpageSizes"
            :page-size.sync="WeighingpageSize"
            :total="Weighingtotal"
            class="p-bar"
            background
            layout="prev, pager, next, sizes, total, jumper"
            @size-change="WeighinghandleSizeChange"
            @current-change="WeighinghandleCurrentChange"/>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitWeighing">确 定</el-button>
          <el-button @click="cancelWeighing">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog :visible.sync="qrcodeOpen" title="二维码" width="450px" append-to-body>
        <div ref="qrCodeUrl" id="qrcode" class="qrcode"></div>
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
import QRCode from 'qrcodejs2'
import { sortColumn } from '@/utils/index';

import {getProjectsList} from "../api/samplingWitness";

//	import vPinyin from './vuepy.js';

export default {
  name: 'SamplingWitness',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList,
    QRCode
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
      // 分页
      WeighingcurrentPage: 1,
      WeighingpageSize: 10,
      WeighingpageSizes: [10, 20, 30, 40],
      Weighingtotal: 0,
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      WeighingId: 0,
      // 表格数据
      dataList: [],
      // 试件详情数据
      membersList: [],
      // 物料称重数据
      WeighingList: [],
      // 试件类型  字典表qual_specimen_type
      qualSpecimenType: [],
      // 过程类型  字典表qual_process_type
      qualProcessType: [],
      // 附件id
      batchId: '',
      // 过程附件id
      batchIdprocess: '',
      // 弹出层标题
      title: '',
      // 试件详细信息
      titleMembers: '',
      // 物料称重
      titleWeighing: '',
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
      // 是否显示弹出层
      open: false,
      // 是否禁止修改
      isDetails: false,
      // 是否显示试件详情弹出层
      membersOpen: false,
      // 是否显示物料称重弹出层
      WeighingOpen: false,
      // 是否显示物料称重弹出层
      qrcodeOpen: false,
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 物料称重查询参数
      WeighingqueryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 表单参数
      form: {},
      formMembers: {},
      weighingForm: {},
      // 表单校验
      rules: {
        samplers: [
          { required: true, message: '取样人不能为空', trigger: 'blur' }
        ],
        samplingUnit: [
          { required: true, message: '取样单位不能为空', trigger: 'blur' }
        ],
        witness: [
          { required: true, message: '见证人不能为空', trigger: 'blur' }
        ],
        witnessUnit: [
          { required: true, message: '见证单位不能为空', trigger: 'blur' }
        ],
        samplingDate: [
          { required: true, message: '取样日期不能为空', trigger: 'blur' }
        ],
        specimenType: [
          { required: true, message: '请选择试件类型', trigger: 'blur' }
        ],
        specimenDetails: [
          { required: true, message: '请选择过磅单号', trigger: 'blur' }
        ]

      },
      // 见证取样过程表单校验
      rulesMembers: {
        processType: [
          { required: true, message: '请选择过程类型', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '情况说明不能为空', trigger: 'blur' }
        ]

      },
	  // 项目下拉框列表
	  projectOptions:[],
    }

  },

  created() {

	// 查询项目数据
	getProjectsList().then(res => {
	  this.projectOptions = res.data;
	})
    this.getList()
    // 试件类型
    this.getDicts('qual_specimen_type').then(response => {
      this.qualSpecimenType = response.data
    })

    // 过程类型
    this.getDicts('qual_process_type').then(response => {
      this.qualProcessType = response.data
    })
  },
  methods: {

    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bQualSamplingWitness/selectListNew',
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
    // 选项类型
    qualSpecimenTypeFc(row, column){
      return this.selectDictLabel(this.qualSpecimenType, row.specimenType)
    },
    // 选项类型
    qualSpecimenTypeFc2(row, column){
      return this.selectDictLabel(this.qualSpecimenType, row.materialType)
    },
    // 选项类型
    qualProcessTypeFc(row, column){
      return this.selectDictLabel(this.qualProcessType, row.processType)
    },
    formatterTime(row,column){
      let data = row[column.property]
      return  /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },
    /** 查询数据列表 */
    getMembersList() {
      var pid = this.batchId
      request({
        url: 'build/bQualSamplingProcess/selectByPId/' + pid,
        method: 'get'
      }).then(response => {
        this.membersList = response.data
        this.membersOpen = false
      })
    },
    // 取消按钮
    cancel() {
      console.log("222222222222")
      this.open = false
      this.reset()
    },
    cancelMembers() {
      this.membersOpen = false
    },
    cancelWeighing() {
      this.WeighingOpen = false
      this.WeighingId = ''
    },
    // 表单重置
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.sjtbDate != null&&this.sjtbDate != '') {
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
      	this.queryParams.createBy =''
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
			this.isDetails = false;
      this.reset()
      this.open = true
      this.title = '新增'
      this.batchId = guid()
      this.membersList = []
      this.weighingForm = {}
    },
    /** 查看 */
    handleDetails(row) {
      console.log("8888888")
	  this.isDetails = true;
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bQualSamplingWitness/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        console.log("555555")
        console.log(response)
        if(response.data.specimenDetails) {
          request({
            url: 'build/bMaterialWeighing/selectById/' + response.data.specimenDetails,
            method: 'get'
          }).then(response3 => {
            this.weighingForm = response3.data
          })
        }
        if(response.data.batchId) {
          request({
            url: 'build/bQualSamplingProcess/selectByPId/' + response.data.batchId,
            method: 'get'
          }).then(response2 => {
            console.log(response2.data)
            this.membersList = response2.data
            this.open = true
            this.title = '详情'
          })
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
			this.isDetails = false;
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bQualSamplingWitness/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        request({
          url: 'build/bMaterialWeighing/selectById/' + response.data.specimenDetails,
          method: 'get'
        }).then(response3 => {
          this.weighingForm = response3.data
        })
        request({
          url: 'build/bQualSamplingProcess/selectByPId/' + response.data.batchId,
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
          if (this.form.specimenDetails == '' || this.form.specimenDetails == undefined) {
            this.$message({
              showClose: true,
              message: '请先选择需要过磅单号，确认要见证取样监测的物料！',
              type: 'error'
            })
            return
          }
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
              url: 'build/bQualSamplingWitness/edit',
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
              url: 'build/bQualSamplingWitness/add',
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
          url: 'build/bQualSamplingWitness/deleteDetails/' + id,
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
      this.batchIdprocess = guid()
    },
    /** 修改按钮操作 */
    handleUpdateMembers(row) {
      this.resetMembers()
      const pipeId = row.id || this.ids0
      request({
        url: 'build/bQualSamplingProcess/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.formMembers = response.data
        this.batchIdprocess = response.data.batchId
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
          url: 'build/bQualSamplingProcess/deleteDetails/' + id,
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
          this.formMembers.samplingId = this.batchId
          this.formMembers.batchId = this.batchIdprocess
          var fileFlag = this.$refs.wkUploadFile.submitAsync()
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '附件提交异常'
            })
            return
          }
          if (this.formMembers.id != undefined) {
            request({
              url: 'build/bQualSamplingProcess/edit',
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
              url: 'build/bQualSamplingProcess/add',
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
    // 查询物料称重列表
    selectWeighing(projectId) {
      if (this.form.specimenType == '' || this.form.specimenType == undefined) {
        this.$message({
          message: '请先选择试件类型后在点击！',
          type: 'warning'
        })
        return
      }
	  this.projectId = projectId
      this.WeighingId = ''
      this.WeighingqueryParams = {}
      this.loading = true
      this.WeighingqueryParams.page = this.WeighingcurrentPage
      this.WeighingqueryParams.limit = this.WeighingpageSize
      this.WeighingqueryParams.materialType = this.form.specimenType
	  this.WeighingqueryParams.projectId = this.projectId
      request({
        url: 'build/bMaterialWeighing/list',
        method: 'post',
        data: this.WeighingqueryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.WeighingList = response.data.list
        this.Weighingtotal = response.data.totalRow
        this.loading = false
        this.WeighingOpen = true
        this.titleWeighing = '查询'
      }).catch(() => {
        this.loading = false
      })
    },
    /** 提交按钮 */
    submitWeighing: function() {
      if (this.WeighingId == '' || this.WeighingId == undefined) {
        this.$message({
          message: '请先选择一条数据后在点击确定！',
          type: 'warning'
        })
        return
      }
      request({
        url: 'build/bMaterialWeighing/selectById/' + this.WeighingId,
        method: 'get'
      }).then(response => {
        this.weighingForm = response.data
        this.form.specimenDetails = this.WeighingId
        this.WeighingOpen = false
      })
    },
    getListWeighing() {
      this.WeighingqueryParams.page = this.WeighingcurrentPage
      this.WeighingqueryParams.limit = this.WeighingpageSize
      this.WeighingqueryParams.materialType = this.form.specimenType
      request({
        url: 'build/bMaterialWeighing/list',
        method: 'post',
        data: this.WeighingqueryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.WeighingList = response.data.list
        this.Weighingtotal = response.data.totalRow
      }).catch(() => {
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQueryWeighing() {
      this.WeighingcurrentPage = 1
      this.getListWeighing()
    },
    /** 重置按钮操作 */
    resetQueryWeighing() {
      this.WeighingqueryParams = {}
      this.handleQueryWeighing()
    },
    WeighinghandleSizeChange(val) {
      this.WeighingpageSize = val
      this.getListWeighing()
    },
    // 更改当前页数
    WeighinghandleCurrentChange(val) {
      this.WeighingcurrentPage = val
      this.getListWeighing()
    },
    selectListData(val) {
      this.WeighingId = val.id
      console.log("ddd"+val.id)
    },
    qrcode(text) {
      let that=this
      console.log(text)
      setTimeout(function () {
        document.getElementById("qrcode").innerHTML = ""
        let qrcode = new QRCode(that.$refs.qrCodeUrl, {
          width: 400,
          height: 400, // 高度
          text: text // 二维码内容
          // render: 'canvas' // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
          // background: '#f0f'
          // foreground: '#ff0'
        })
        console.log(qrcode)
      },30)

      this.qrcodeOpen = true
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
  .qrcode{
    display: inline-block;
    img {
      width: 432px;
      height: 432px;
      background-color: #fff; //设置白色背景色
      padding: 6px; // 利用padding的特性，挤出白边
      box-sizing: border-box;
    }
  }
</style>
