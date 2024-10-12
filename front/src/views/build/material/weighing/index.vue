<template>
  <div class="main">
<!--    <xr-header
      icon-class="wk wk-project"
      icon-color="#2362FB"
      label="安全资料管理" />-->

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="过磅日期" >
          <el-date-picker
            class="timecom"
            v-model="sjtbDate"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="过磅日期起"
            end-placeholder="过磅日期止"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号" prop="licenseNumber" >
          <el-input
            v-model="queryParams.licenseNumber"
            placeholder="请输入车牌号"
            clearable
            size="small" style="width: 120px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="物料类型" prop="materialType" >
          <el-select
            v-model="queryParams.materialType"
            placeholder="请选择"
            clearable
            size="small">
            <el-option
              v-for="dict in qualSpecimenType"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="发货单位" prop="deliveryUnit" >
          <el-input
            v-model="queryParams.deliveryUnit"
            placeholder="请输入发货单位"
            clearable
            size="small" style="width: 120px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="收货单位" prop="thegoodsUnit" >
          <el-input
            v-model="queryParams.thegoodsUnit"
            placeholder="请输入收货单位"
            clearable
            size="small" style="width: 120px"
            @keyup.enter.native="handleQuery"
          />
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
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed ></el-table-column>
        <el-table-column label="过磅单号" align="center" prop="weighingOrderOn" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="车牌号" align="center" prop="licenseNumber" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="物料名称" align="center" prop="materialName" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="物料类型" :formatter="qualSpecimenTypeFc" align="center" prop="materialType" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="规格型号" align="center" prop="specificationsType" sortable="custom"/>
        <el-table-column label="发货单位" align="center" prop="deliveryUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="收货单位" align="center" prop="thegoodsUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="运输单位" align="center" prop="transportUnit" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="司机" align="center" prop="theDriver" sortable="custom"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-document"
              @click="handleSelect(scope.row)"
            >查看</el-button>
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
            <el-col :span="8">
              <el-form-item label="过磅单号" prop="weighingOrderOn" class="add-form-input">
                <el-input v-model="form.weighingOrderOn" :disabled="true" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="车牌号" prop="licenseNumber" class="add-form-input">
                <el-input v-model="form.licenseNumber" placeholder="请输入车牌号" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="物料类型" prop="materialType" class="add-form-input">
                <el-select
                  v-model="form.materialType"
                  placeholder="请选择"
                  clearable
                  size="small"
                  :disabled="!showInfo"
                  style="width: 160px">
                  <el-option
                    v-for="dict in qualSpecimenType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="物料名称" prop="materialName" class="add-form-input">
                <el-input v-model="form.materialName" placeholder="请输入物料名称" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="规格型号" prop="specificationsType" class="add-form-input">
                <el-input v-model="form.specificationsType" placeholder="请输入规格型号" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发货单位" prop="deliveryUnit" class="add-form-input">
                <el-input v-model="form.deliveryUnit" placeholder="请输入发货单位" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="收货单位" prop="thegoodsUnit" class="add-form-input">
                <el-input v-model="form.thegoodsUnit" placeholder="请输入收货单位" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="运输单位" prop="transportUnit" class="add-form-input">
                <el-input v-model="form.transportUnit" placeholder="请输入运输单位" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="司机" prop="theDriver" class="add-form-input">
                <el-input v-model="form.theDriver" placeholder="请输入司机" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="毛重" prop="grossWeight" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.grossWeight" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="皮重" prop="tareWeight" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.tareWeight" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="净重" prop="thenetWeight" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.thenetWeight" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="扣杂(%)" prop="buckleMiscellaneous" class="add-form-input">
                <el-input-number :precision="2" :min="0" :max="100" v-model="form.buckleMiscellaneous" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实重" prop="actualWeight" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.actualWeight" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单价" prop="unitPrice" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.unitPrice" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="金额" prop="amountOf" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.amountOf" :disabled="!showInfo" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="数量" prop="numberOf" class="add-form-input">
                <el-input-number :precision="2" :min="0" v-model="form.numberOf" :disabled="!showInfo" />

              </el-form-item>
            </el-col>
<!--            <el-col :span="8" v-if="showInfo">-->
<!--              <el-upload-->
<!--							  class="avatar-uploader"-->
<!--							  action="#"-->
<!--							  :show-file-list="false"-->
<!--							  :http-request="handleAvatarSuccess"-->
<!--								>-->
<!--							  <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     v-if="imageUrl" :src="imageUrl" class="avatar">-->
<!--							  <el-button v-else size="small" type="primary">自动识别</el-button>-->
<!--							  &lt;!&ndash;<i v-else class="el-icon-plus avatar-uploader-icon"></i>&ndash;&gt;-->
<!--							</el-upload>-->
<!--            </el-col>-->
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button v-if="showInfo" type="primary" @click="submitForm">确 定</el-button>
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
//	import vPinyin from './vuepy.js';
import { sortColumn } from '@/utils/index';

export default {
  name: 'Weighing',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    FilesList,
  },
  data() {
    return {
    	imageUrl:'',
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
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
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否可修改对应事项
      showInfo: true,
      // 试件类型  字典表qual_specimen_type
      qualSpecimenType: [],
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
      batchIdAll:'',
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
        licenseNumber: [
          { required: true, message: '车牌号不能为空', trigger: 'blur' }
        ],
        materialType: [
          { required: true, message: '请选择物料类型', trigger: 'blur' }
        ],
        materialName: [
          { required: true, message: '物料名称不能为空', trigger: 'blur' }
        ],
        specificationsType: [
          { required: true, message: '规格型号不能为空', trigger: 'blur' }
        ],
        deliveryUnit: [
          { required: true, message: '发货单位不能为空', trigger: 'blur' }
        ],
        thegoodsUnit: [
          { required: true, message: '收货单位不能为空', trigger: 'blur' }
        ],
        transportUnit: [
          { required: true, message: '运输单位不能为空', trigger: 'blur' }
        ],
        theDriver: [
          { required: true, message: '司机不能为空', trigger: 'blur' }
        ],
        grossWeight: [
          { required: true, message: '毛重不能为空', trigger: 'blur' },
          { type: 'number', message: '毛重必须为数字值' }
        ],
        tareWeight: [
          { required: true, message: '皮重不能为空', trigger: 'blur' },
          { type: 'number', message: '皮重必须为数字值' }
        ],
        thenetWeight: [
          { required: true, message: '净重不能为空', trigger: 'blur' },
          { type: 'number', message: '净重必须为数字值' }
        ],
        buckleMiscellaneous: [
          { required: true, message: '扣杂不能为空', trigger: 'blur' },
          { type: 'number', message: '扣杂必须为数字值' }
        ],
        actualWeight: [
          { required: true, message: '实重不能为空', trigger: 'blur' },
          { type: 'number', message: '实重必须为数字值' }
        ],
        unitPrice: [
          { required: true, message: '单价不能为空', trigger: 'blur' },
          { type: 'number', message: '单价必须为数字值' }
        ],
        amountOf: [
          { required: true, message: '金额不能为空', trigger: 'blur' },
          { type: 'number', message: '金额必须为数字值' }
        ],
        numberOf: [
          { required: true, message: '数量不能为空', trigger: 'blur' },
          { type: 'number', message: '数量必须为数字值' }
        ]

      }
    }
  },

  created() {
    this.getList()
    // 试件类型
    this.getDicts('qual_specimen_type').then(response => {
      this.qualSpecimenType = response.data
    })
  },
  methods: {
		handleAvatarSuccess(content){
			this.imageUrl=URL.createObjectURL(content.file);
			this.form.numberOf=80
		},
    /** 查询数据列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize

      request({
        url: 'build/bMaterialWeighing/list',
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
    	this.imageUrl=''
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
      request({
        url: 'build/bMaterialWeighing/generateDocumentNumber',
        method: 'post'
      }).then(response => {
        console.log(response.data)
        this.reset()
        this.form.weighingOrderOn = response.data
        this.open = true
        this.title = '新增'
        this.showInfo = true
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bMaterialWeighing/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改'
        this.showInfo = true
      })
    },
    handleSelect(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bMaterialWeighing/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.open = true
        this.title = '查看'
        this.showInfo = false
      })
    },
    /** 提交按钮 */
    submitForm: function() {
    	this.imageUrl=''
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            request({
              url: 'build/bMaterialWeighing/edit',
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
              url: 'build/bMaterialWeighing/add',
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
          url: 'build/bMaterialWeighing/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    // 试件类型
    qualSpecimenTypeFc(row, column) {
      return this.selectDictLabel(this.qualSpecimenType, row.materialType)
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
	.avatar{
		width: 100px;
		height: 100px;
	}
</style>
