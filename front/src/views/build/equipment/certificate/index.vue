<template>
  <div class="main">
<!--    <xr-header
      icon-class="wk wk-project"
      icon-color="#2362FB"
      label="安全资料管理" />-->

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="设备类型" prop="equipmentType">
          <select-tree :options="showTreeData" v-model="queryParams.equipmentType" :props="defaultProps"/>
        </el-form-item>
        <el-form-item label="证书种类" prop="certificateSpecies">
          <el-select v-model="queryParams.certificateSpecies" placeholder="请选择" clearable size="small" style="width: 100%" @change="cerOptionsChangeCx">
            <el-option
              v-for="dict in cerCategoryOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="证书类型" prop="certificateType">
          <el-select
            v-model="queryParams.certificateType"
            placeholder="请选择"
            clearable
            size="small">
            <el-option
              v-for="dict in cerCategoryTypeSelectOptionsCx"
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
        <el-table-column type="index" align="center" label="序号" width="100"/>
        <el-table-column label="设备类型" align="center" prop="remarks" width="240px" sortable="custom"/>
        <el-table-column label="证书种类" :formatter="certificateSpeciesZh" align="center" prop="certificateSpecies" sortable="custom" />
        <el-table-column label="证书类型" :formatter="certificateTypeZh" align="center" prop="certificateType" sortable="custom" />

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
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-form-item label="设备类型" prop="equipmentType">
            <select-tree :options="showTreeData" v-model="form.equipmentType" :props="defaultProps"/>
          </el-form-item>
          <el-form-item label="证书种类" class="add-form-input" prop="certificateSpecies" >
            <el-select
              v-model="form.certificateSpecies"
              placeholder="请选择"
              @change="cerOptionsChange"
              clearable style="width: 100%"
              size="small">
              <el-option
                v-for="dict in cerCategoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
          </el-form-item>
          <el-form-item label="证书类型" class="add-form-input" prop="certificateType" >
            <el-select
              v-model="form.certificateType"
              placeholder="请选择"
              clearable
              multiple style="width: 100%"
              size="small">
              <el-option
                v-for="dict in cerCategoryTypeSelectOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue" />
            </el-select>
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

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
import { guid,sortColumn} from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表
import SelectTree from '@/components/SelectTree.vue';
//	import vPinyin from './vuepy.js';

export default {
  name: 'Certificate',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    SelectCol,
    SelectTree,
    FilesList
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
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      // 表格数据
      dataList: [],
      // 证书种类
      cerCategoryOptions: [],
      // 证书类型 下拉框使用
      cerCategoryTypeSelectOptions: [],
      // 证书类型 下拉框使用
      cerCategoryTypeSelectOptionsCx: [],
      // 证书类型
      cerCategoryTypeOptions: [],
      showTreeData: [],
      showTreeTableData: [
        {
          id: 0,
          label: "全部类型",
          parentId: -1,
          children: []
        }
      ],
      // 数据默认字段
      defaultProps: {
        parent: 'parentId',   // 父级唯一标识
        value: 'id',          // 唯一标识
        label: 'label',       // 标签显示
        children: 'children', // 子级
      },
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
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
        equipmentType: [
          { required: true, message: '请选择设备类型', trigger: 'blur' }
        ],
        certificateSpecies: [
          { required: true, message: '请选择证书种类', trigger: 'blur' }
        ],
        certificateType: [
          { required: true, message: '请选择证书类型', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getList()
    //  证书种类 字典项
    this.getDicts('b_cerCategory').then(response => {
      this.cerCategoryOptions = response.data
    })
    this.getDicts('b_cerCategoryType').then(response => {
      this.cerCategoryTypeOptions = response.data
    })
    this.getTreeList()
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
        url: 'build/bEquipmentCertificate/list',
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
    certificateSpeciesZh(row,column) {
      return this.selectDictLabel(this.cerCategoryOptions, row.certificateSpecies)
    },
    certificateTypeZh(row,column) {
      return this.acceptByFormat(this.cerCategoryTypeOptions, row.certificateType)
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
      if (this.sjtbDate != '') {
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
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增'
      this.batchId = guid()
      this.cerCategoryTypeSelectOptions = []
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bEquipmentCertificate/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.cerOptions(this.form.certificateSpecies)

        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.certificateType != null && this.form.certificateType.length > 0) {
            this.form.certificateType = this.form.certificateType.join(',')
          }
          if (this.form.id != undefined) {
            request({
              url: 'build/bEquipmentCertificate/edit',
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
              url: 'build/bEquipmentCertificate/add',
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
          url: 'build/bEquipmentCertificate/deleteDetails/' + id,
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
    // 根据证书种类显示证书类型
    cerOptions(value) {
      var actions = []
      var datas = this.cerCategoryTypeOptions

      Object.keys(datas).some((key) => {
        var a = datas[key].dictValue.split('-')[0]
        if (a == ('' + value)) {
          actions.push(datas[key])
        }
      })

      this.cerCategoryTypeSelectOptions = actions
    },
    // 根据证书种类显示证书类型
    cerOptionsCx(value) {
      var actions = []
      var datas = this.cerCategoryTypeOptions

      Object.keys(datas).some((key) => {
        var a = datas[key].dictValue.split('-')[0]
        if (a == ('' + value)) {
          actions.push(datas[key])
        }
      })

      this.cerCategoryTypeSelectOptionsCx = actions

      this.loading = false
    },
    // 当证书种类的下拉框改变时
    cerOptionsChange(value) {
      this.$forceUpdate()// 强制刷新
      this.cerOptions(value)
    },
    cerOptionsChangeCx(value) {
      this.$forceUpdate()// 强制刷新
      this.cerOptionsCx(value)
    },
    // 获取树形列表
    getTreeList() {
      request({
        url: 'build/equipmentType/queryTreeList',
        method: 'post'
      }).then(response => {
        this.showTreeTableData[0]['children'] = response.data || []
        this.showTreeData = response.data || []
      }).catch(() => {
      })
    },

    // 验收人员
    acceptByFormat(datas, value){

      if(datas == undefined || datas == null){
        return null
      }

      var actions = []
      if(value == null || value == undefined){
        return null
      }else{

        let values = value.split(',')

        Object.keys(values).some((index) => {
          Object.keys(datas).some((key) => {
            if (datas[key].dictValue == values[index] ) {
              actions.push(datas[key].dictLabel)
              actions.push(',')
              return true
            }
          })
        })
        actions.splice(-1,1)

        return actions.join('')
      }


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
