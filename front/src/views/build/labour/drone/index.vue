<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="无人机编号" prop="uavNumber">
          <el-input
            v-model="queryParams.uavNumber"
            placeholder="请输入无人机编号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>


        <el-form-item label="无人机型号" prop="uavModel" >
          <el-input
            v-model="queryParams.uavModel"
            placeholder="请输入无人机型号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="巡检时间" >
          <el-date-picker
            class="timecom"
            v-model="sjtbDate"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="巡检开始日期"
            end-placeholder="巡检结束日期"
            :picker-options="pickerOptions">
          </el-date-picker>
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
        <el-table-column label="巡检时间" align="center" prop="inspectionTime" :formatter="formatterTime" sortable="custom"/>
        <el-table-column label="无人机编号" align="center" prop="uavNumber" sortable="custom" />
        <el-table-column label="无人机型号" align="center" prop="uavModel" sortable="custom" />
        <el-table-column label="无人机操作人员" align="center" prop="uavPperators" sortable="custom" />
        <el-table-column label="无人机操作证号" align="center" prop="uavLicenseNumber" sortable="custom" />
        <el-table-column label="无人机影像" align="center" width="120px"  >
          <template slot-scope="scope">
           <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="scope.row.url" style="width: 100px; height: 100px" @click="handlePreview(scope.row)"/>
          </template>
        </el-table-column>
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
              <el-form-item label="巡检时间" prop="inspectionTime" class="add-form-input" >
                <el-date-picker
                  v-model="form.inspectionTime"
                  type="date"
                  style="width: 329px"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="无人机编号" prop="uavNumber" class="add-form-input">
                <el-input v-model="form.uavNumber" placeholder="请输入无人机编号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="无人机型号" prop="uavModel" class="add-form-input">
                <el-input v-model="form.uavModel" placeholder="请输入无人机型号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="操作人员" prop="uavPperators" class="add-form-input">
                <el-input v-model="form.uavPperators" placeholder="请输入无人机操作人员" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="操作证号" prop="uavLicenseNumber" class="add-form-input">
                <el-input v-model="form.uavLicenseNumber" placeholder="请输入无人机操作证号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <!--              <el-form-item label="巡检影像" prop="inspectionImage" class="add-form-input">
                              <el-input v-model="form.inspectionImage" placeholder="请输入巡检影像" />
                            </el-form-item>-->
              <el-form-item label="巡检影像">
                <!--图片上传-->
                <wk-upload-img
                  name="file"
                  ref="wkUploadImg"
                  multiple
                  accept="image/*"
                  list-type="picture-card"
                  :autoUpload="false"
                  :title="'图片上传'"
                  :batchId="batchId"
                ></wk-upload-img>
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
    <!-- 图片预览 -->
    <el-image-viewer
      v-if="fileimage"
      :on-close="()=>{fileimage=false,srcList=[]}"
      :url-list="srcList" />
  </div>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
import { guid ,sortColumn} from '@/utils'
import FilesList from '@/components/common/FilesList' // 附件列表
import WkUploadImg from '@/components/CreateCom/WkUploadImg'
//	import vPinyin from './vuepy.js';

export default {
  name: 'Drone',
  components: {
    // Reminder,
    XrHeader,
    WkUploadFile,
    WkUploadImg,
    SelectCol,
    FilesList,
    'el-image-viewer':()=>import('element-ui/packages/image/src/image-viewer')
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      // 选中数组
      ids: [],
      srcList: [
      ],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 图片预览
      fileimage: false, // 预览图片是否显示
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
        inspectionTime: [
          { required: true, message: '巡检时间不能为空', trigger: 'blur' }
        ],
        uavNumber: [
          { required: true, message: '无人机编号不能为空', trigger: 'blur' }
        ],
        uavModel: [
          { required: true, message: '无人机型号不能为空', trigger: 'blur' }
        ],
        uavPperators: [
          { required: true, message: '无人机操作人员不能为空', trigger: 'blur' }
        ],
        uavLicenseNumber: [
          { required: true, message: '无人机操作证号不能为空', trigger: 'blur' }
        ]/*,
        inspectionImage: [
          { required: true, message: '巡检影像不能为空', trigger: 'blur' }
        ]*/

      }
    }
  },

  created() {
    this.getList()
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
        url: 'build/bLabourDrone/list',
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
    // 表单重置
    reset() {
      this.form = {}
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
      this.currentPage = 1
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
    handleUpdate(row) {
      this.reset()
      const pipeId = row.id || this.ids
      request({
        url: 'build/bLabourDrone/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.inspectionImage
        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.form.inspectionImage = this.batchId
          var fileFlag = this.$refs['wkUploadImg'].submitAsync();
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '图片上传异常'
            })
            return;
          }
          if (this.form.id != undefined) {
            request({
              url: 'build/bLabourDrone/edit',
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
              url: 'build/bLabourDrone/add',
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
          url: 'build/bLabourDrone/deleteDetails/' + id,
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
    handlePreview(row) {
      this.srcList = []
      var batchid = row.inspectionImage
      request({
        url: 'build/bLabourDrone/selectFileList/' + batchid,
        method: 'get',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.selectImgFileList = response.data
        for (let index = 0; index < this.selectImgFileList.length; index++) {
          var file = this.selectImgFileList[index]
          if (file != null) {
            this.srcList.push(file.Base64)
          }
        }
        this.fileimage = true
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
