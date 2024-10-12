<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="分包商名称" prop="realName">
          <el-input
            v-model="queryParams.realName"
            placeholder="请输入分包商名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="分包商类型" prop="subcontractorType">
          <el-select
            v-model="queryParams.subcontractorType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 200px"
          >
            <el-option
              v-for="dict in subcontractorTypeDict"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button
            class="main-table-header-button xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange">
        <el-table-column label="序号" type="index"  align='center' width="70"></el-table-column>
        <el-table-column label="分包商类型" align="center" prop="subcontractorType" width="180px" :formatter="subcontractorTypeFormat" sortable="custom" />
        <el-table-column label="分包商名称" align="center" prop="realName" width="180px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="统一社会信用代码" align="center" prop="corpCode" width="180px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="法人" align="center" prop="legalRepresentative" width="100px" sortable="custom"/>
        <el-table-column label="注册资金(万)" align="center" prop="registeredCapital" width="180px" sortable="custom"/>
        <el-table-column label="地址" align="center" prop="unitAddress" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="联系人" align="center" prop="principal" width="120px" sortable="custom"/>
        <el-table-column label="联系电话" align="center" prop="principaltel" width="120px" sortable="custom"/>
        <el-table-column label="公司简称首拼" align="center" prop="nameSpell" width="180px" sortable="custom"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150px">
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
          :page-size.sync="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px"  append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="add-form">
          <el-form-item label="分包商名称" prop="realName" class="add-form-input">
            <el-input v-model="form.realName" placeholder="请输入分包商名称" />
          </el-form-item>
          <el-form-item label="分包商简称" prop="shortName" class="add-form-input">
            <el-input v-model="form.shortName" placeholder="请输入分包商简称" @blur="addPY(form.shortName)" />
          </el-form-item>
          <el-form-item label="分包商类型" prop="subcontractorType" class="add-form-input">
            <el-select
              v-model="form.subcontractorType"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="dict in subcontractorTypeDict"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属省" prop="province" class="add-form-input">
            <el-select
              v-model="form.province"
		      @change="areaChangeProvince"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="option in provinceOptions"
                :key="option.id"
                :label="option.name"
                :value="option.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属市" prop="city" class="add-form-input">
            <el-select
              v-model="form.city"
		      @change="areaChangeCity"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="option in cityOptions"
                :key="option.id"
                :label="option.name"
                :value="option.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属区" prop="district" class="add-form-input">
            <el-select
              v-model="form.district"
		      @change="areaChangeDistrict"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="option in districtOptions"
                :key="option.id"
                :label="option.name"
                :value="option.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="参建单位类型" prop="corpType" class="add-form-input">
            <el-select
              v-model="form.corpType"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="dict in corpTypeDict"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="等级" prop="overallMerit" class="add-form-input">
            <el-select
              v-model="form.overallMerit"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="dict in overallMeritDict"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="是否生效" prop="isConformity" class="add-form-input">
            <el-select
              v-model="form.isConformity"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 200px"
            >
              <el-option
                v-for="option in isConformityOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="单位地址" prop="unitAddress" class="add-form-input">
            <el-input v-model="form.unitAddress" placeholder="请输入单位地址" />
          </el-form-item>
          <el-form-item label="法人" prop="legalRepresentative" class="add-form-input">
            <el-input v-model="form.legalRepresentative" placeholder="请输入法人" />
          </el-form-item>
          <el-form-item label="法人联系电话" prop="legalRepresentativePhone" class="add-form-input">
            <el-input v-model="form.legalRepresentativePhone" placeholder="请输入法人联系电话" />
          </el-form-item>
          <el-form-item label="注册资金(万)" prop="registeredCapital" class="add-form-input">
            <el-input v-model.number="form.registeredCapital" type="number" placeholder="请输入注册资金(万)" />
          </el-form-item>
          <el-form-item label="注册时间" prop="regdate" class="add-form-input">
            <el-date-picker
              v-model="form.regdate"
              type="date"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="联系人" prop="principal" class="add-form-input">
            <el-input v-model="form.principal" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系电话" prop="principaltel" class="add-form-input">
            <el-input v-model="form.principaltel" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系人身份证号" prop="idCard" class="add-form-input">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="资质" prop="subject" class="add-form-input">
            <el-input v-model="form.subject" placeholder="请输入资质" />
          </el-form-item>
          <el-form-item label="公司简称首拼" prop="nameSpell" class="add-form-input">
            <el-input v-model="form.nameSpell" disabled placeholder="请输入公司简称首拼" />
          </el-form-item>
          <el-form-item label="社会信用代码" prop="corpCode" class="add-form-input">
            <el-input v-model="form.corpCode" placeholder="请输入社会信用代码" />
          </el-form-item>
          <el-divider content-position="left" class="add-form-input">注册地</el-divider>
          <el-form-item label="住建部投诉电话" prop="buildComplaintCall" class="add-form-input">
            <el-input v-model="form.buildComplaintCall" placeholder="请输入注册地址住建部投诉电话" />
          </el-form-item>
          <el-form-item label="人社部投诉电话" prop="societyComplaintCall" class="add-form-input">
            <el-input v-model="form.societyComplaintCall" placeholder="请输入注册地人社部投诉电话" />
          </el-form-item>
          <el-form-item label="公司项目经验" prop="companyScore" class="add-form-input">
            <el-input v-model="form.companyScore" type="textarea" placeholder="请输入公司项目经验" />
          </el-form-item>
          <el-form-item label="公司简介" prop="companySummary" class="add-form-input">
            <el-input v-model="form.companySummary" type="textarea" placeholder="请输入公司简介" />
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
import {selectList, addSubcontractor, editSubContractor, getSubcontractorById, deleteSubcontractor} from '@/api/build/labour'
import {selectAreaList} from '@/api/common'
import { validatePhone,validateIdNo,isNum,guid,sortColumn } from '@/utils'
import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import vPinyin from '@/utils/vuepy.js'

export default {
  name: 'subcontractor',
  components: {
    Reminder,
    XrHeader
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
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
      // 分包商类型字典数据
      subcontractorTypeDict: [],
	  // 参建单位类型字典数据
	  corpTypeDict: [],
	  // 分包资质等级
	  overallMeritDict: [],
	  // 是否生效
	  isConformityOptions:[
		  {
			value: '1',
			label: '是'
		  },
		  {
		  	value: '0',
		  	label: '否'
		  }
	  ],
	  // 区划数据-省
	  provinceOptions: [],
	  // 区划数据-市
	  cityOptions: [],
	  // 区划数据-区
	  districtOptions: [],
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
      },
      // 表单参数
      form: {
		  city: undefined
	  },
      // 表单校验
      rules: {
        realName: [
          { required: true, message: '分包商名称不能为空', trigger: 'blur' }
        ],
        shortName: [
          { required: true, message: '分包商简称不能为空', trigger: 'blur' }
        ],
        subcontractorType: [
          { required: true, message: '分包商类型不能为空', trigger: 'blur' }
        ],
        province: [
          { required: true, message: '所属省不能为空', trigger: 'blur' }
        ],
        city: [
          { required: true, message: '所属市不能为空', trigger: 'blur' }
        ],
        district: [
          { required: true, message: '所属区不能为空', trigger: 'blur' }
        ],
		corpType: [
		  { required: true, message: '参建单位类型不能为空', trigger: 'blur' }
		],
		unitAddress: [
		  { required: true, message: '地址不能为空', trigger: 'blur' }
		],
		registeredCapital: [
		  { required: true, message: '注册资金(万)不能为空', trigger: 'blur' }
		],
		legalRepresentative: [
		  { required: true, message: '法定代表人不能为空', trigger: 'blur' }
		],
		principal: [
		  { required: true, message: '联系人不能为空', trigger: 'blur' }
		],
		isConformity: [
		  { required: true, message: '是否生效不能为空', trigger: 'blur' }
		],
		nameSpell: [
		  { required: true, message: '公司简称首拼不能为空', trigger: 'blur' }
		],
		corpCode: [
		  { required: true, message: '社会信用代码不能为空', trigger: 'blur' }
		],
        idCard: [
          {validator: validateIdNo,trigger: 'blur'}
        ],
        principaltel: [
          {validator: validatePhone,trigger: 'blur'}
        ],
        legalRepresentativePhone: [
          {validator: validatePhone,trigger: 'blur'}
        ],
      }
    }
  },
  created() {
	// 获取列表数据
    this.getList()
	// 初始化字典数据
	this.initDicts()
  },
  methods: {
  	sortChange(column) {
      sortColumn(this,column);
    },
    /** 查询分包商数据 **/
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      selectList(this.queryParams).then(response => {
		if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
		  this.currentPage = this.currentPage - 1
		  this.getList()
		} else {
		  this.total = response.data.totalRow
		  this.loading = false
		}
        this.dataList = response.data.list
        this.total = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
	// 初始化字典数据
	initDicts() {
		this.getDicts('b_subcontractor_type').then(response => {
		  this.subcontractorTypeDict = response.data
		})
		this.getDicts('b_corp_type').then(response => {
		  this.corpTypeDict = response.data
		})

		this.getDicts('b_overall_merit').then(res => {
		  this.overallMeritDict = res.data
		})
	},
    // 分包商类型字典翻译
    subcontractorTypeFormat(row, column) {
      return this.selectDictLabel(this.subcontractorTypeDict, row.subcontractorType)
    },
	// 获取省级区划数据
	getProvinceList() {
		const param = {
			level:1
		}
		selectAreaList(param).then(res => {
			this.provinceOptions = res.data
		})
	},
	// 获取市级区划数据
	getCityList(id) {
		const param = {
			parentId: id
		}
		selectAreaList(param).then(res => {
			this.cityOptions = res.data
		})
	},
	// 获取县级区划数据
	getDistrictList(id) {
		const param = {
			parentId: id
		}
		selectAreaList(param).then(res => {
			this.districtOptions = res.data
			console.log(this.districtOptions)

		})
	},
	// 强制刷新下拉框
	areaChangeProvince(d){
		this.form.city = ''
		this.form.district = ''
		this.getCityList(d)
	},
	areaChangeCity(d){
		this.form.district = ''
		this.getDistrictList(d)
		this.$forceUpdate()
	},
	areaChangeDistrict(d){
		this.$forceUpdate()
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
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
	  // 清空市、区区划数据
	  this.cityOptions = []
	  this.districtOptions = []
	  // 获取省级区划
	  this.getProvinceList()
      this.reset()
      this.open = true
      this.title = '添加分包商信息'
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
	  // 获取省级区划
	  this.getProvinceList()
      this.reset()
      const id = row.id
      getSubcontractorById(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改分包商信息'
        this.getCityList(response.data.province)
        this.getDistrictList(response.data.city)
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            editSubContractor(this.form).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addSubcontractor(this.form).then(response => {
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
      const id = row.id
	  const realName = row.realName
      this.$confirm('是否确认删除分包商"' + realName + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteSubcontractor(id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dict/type/export', {
        ...this.queryParams
      }, `type_${new Date().getTime()}.xlsx`)
    },
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

    //公司简称首拼
    addPY () {
       this.$forceUpdate()//强制刷新

			 let pinyin = vPinyin.chineseToPinYin(this.form.shortName);
		   let SX = '';
		   for (var i = 0; i < pinyin.length; i++) {
		      var c = pinyin.charAt(i);
		      if (/^[A-Z]+$/.test(c)) {
		           SX += c;
		      }
		   }
		   this.form.nameSpell  = SX;
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

.add-form-input{
	float: left;
}

.add-form::after{
	content: "";
	display: block;
	clear: both;
}

.add-form .el-form-item{
  width: 50%;
}

.link-type,.link-type:focus{color:#337ab7;cursor:pointer}

.link-type:focus:hover,.link-type:hover{color:#20a0ff}

.el-table {
  border-top: 1px solid $xr-border-line-color;
}
/deep/ .el-dialog{
		height: 700px;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    margin-top: auto!important;
}
/deep/ .el-dialog .el-dialog__body{
	height: 600px;
    overflow: auto;
}
</style>
