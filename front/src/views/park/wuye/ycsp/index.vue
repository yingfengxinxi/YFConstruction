<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="状态" prop="status" >
          <el-select v-model="queryParams.status"  placeholder="请选择">
		    <el-option
		      v-for="item in options1"
		      :key="item.value"
		      :label="item.label"
		      :value="item.value">
		    </el-option>
  			</el-select>
        </el-form-item>
        <el-form-item label="审批人" prop="trainingType" >
          <el-select v-model="queryParams.trainingType" multiple collapse-tags placeholder="请选择">
		    <el-option
		      v-for="item in options"
		      :key="item.value"
		      :label="item.label"
		      :value="item.value">
		    </el-option>
  			</el-select>
        </el-form-item>


        <el-form-item label="申请人" prop="trainingTeaching" >
          <el-select v-model="queryParams.trainingTeaching" multiple collapse-tags placeholder="请选择">
		    <el-option
		      v-for="item in options"
		      :key="item.value"
		      :label="item.label"
		      :value="item.value">
		    </el-option>
  			</el-select>
        </el-form-item>
        <el-form-item label="申请日期" prop="sjtbDate" >
        <el-date-picker
          class="timecom"
          v-model="sjtbDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="培训时间起"
          end-placeholder="培训时间止"
          :picker-options="pickerOptions">
        </el-date-picker>
        </el-form-item>

          <el-form-item style="float: right">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
      </el-form>
      <el-table :height="tableHeight" :data="dataList" >
        <el-table-column label="申请编号" align="center" prop="sqbh" />
        <el-table-column label="派工单号" align="center"  prop="pgdh" />
        <el-table-column label="申请人" align="center" prop="sqr" />
        <el-table-column label="原工单时限（小时）" align="center" prop="ygdsx" />
        <el-table-column label="申请延长时间(小时)" align="center" prop="sqycsj" />
        <el-table-column label="申请原因" align="center" prop="sqyy" />
        <el-table-column label="申请时间" align="center" prop="sqsj" />
        <el-table-column label="审批时间" align="center" prop="spsj" />
        <el-table-column label="状态" align="center" prop="zt">
	         <template slot-scope="scope">
	        <el-tag
	          :type="scope.row.zt === '1' ? 'primary' : 'success'"
	          disable-transitions>{{scope.row.zt === '1'?'同意':'不同意'}}</el-tag>
	      </template>
	     </el-table-column> 
        <el-table-column label="操作" align="center"  fixed="right" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
        
              @click="handleUpdate(scope.row)"
            >同意</el-button>
            <el-button
              size="mini"
              type="text"
    
              @click="handleDelete(scope.row)"
            >不同意</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>

    </div>
  </div>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid } from '@/utils'
import {getWTList,getListByFBS, getListByBZ} from '@/api/build/infocollection/infocoll'
//	import vPinyin from './vuepy.js';
export default {
  name: 'safetyTraining',
  components: {
    // Reminder,
    XrHeader,
    SelectCol
  },
  data() {
    return {
    	options1:[{
          value: '选项1',
          label: '全部'
        }, {
          value: '选项2',
          label: '审批中'
        }, {
          value: '选项3',
          label: '同意'
        }, {
          value: '选项4',
          label: '拒绝'
        }],
        
    	 options: [{
          value: '选项1',
          label: 'admin'
        }, {
          value: '选项2',
          label: '张霖'
        }, {
          value: '选项3',
          label: '李晓晓'
        }, {
          value: '选项4',
          label: '刘国栋'
        }, {
          value: '选项5',
          label: '王金'
        }],
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 300,
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
      dataList: [
      	{
			sqbh: '1',
			pgdh : '10021515',
			sqr: '小王',
			ygdsx: 4,
			sqycsj: '24',
			sqyy: '缺少物料',
			sqsj: '2021-06-22',
			spsj: '2021-06-22',
			zt:1
		}
      ],
      // 培训成员数据
      membersList: [],
      // 花名册成员数据
      rosterList: [],
      // 分包商数据信息
      FBSList: [],
      // 劳务工种数据信息
      worktypeList: [],
      // 班组数据信息
      BZList: [],
      // 附件id
      batchId: '',
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示应急班组成员弹出层
      membersOpen: false,
      // 应急班组成员弹出层标题
      titleMembers: '',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      sjtbDate: '',
      // 题目分类  字典表safety_training_type
      trainingType: [
      	{
			value: '1',
			label: '全部'
		},
		{
			value: '2',
			label: '审批中'
		},
		{
			value: '3',
			label: '同意'
		},
		{
			value: '4',
			label: '拒绝'
		},
      ],
      // 查询参数
      queryParams: {
        page: this.currentPage,
        limit: this.pageSize,
        hydrantCode: undefined,
        pipeId: undefined
      },
      // 花名册查询参数
      rosterQueryParams: {
        page: this.currentPage,
        limit: this.pageSize
      },
      // 表单参数
      form: {},
      //花名册人员
      formMembers: {},
      // 表单校验
      rules: {
        trainingType: [
          { required: true, message: '请选择培训类型', trigger: 'blur' }
        ],
        trainingTime: [
          { required: true, message: '培训时间不能为空', trigger: 'blur' }
        ],
        trainingAddress: [
          { required: true, message: '培训地点不能为空', trigger: 'blur' }
        ],
        trainingContent: [
          { required: true, message: '培训内容不能为空', trigger: 'blur' }
        ],
        trainingTeaching: [
          { required: true, message: '授课人不能为空', trigger: 'blur' }
        ],
        trainingTeachingTime: [
          { required: true, message: '课时不能为空', trigger: 'blur' }
        ],
        trainingTeachingAddress: [
          { required: true, message: '授课地点不能为空', trigger: 'blur' }
        ],
        trainingPeople: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ]

      }
    }
  },

  created() {
    this.getWorktypeList()
    this.getListByFBS()
    this.getListByBZ()
  },
  methods: {

    /** 查询数据列表 */

    /** 查询花名册数据列表 */
    getRosterList() {
      this.loading = true
      this.rosterQueryParams.page = this.rostercurrentPage
      this.rosterQueryParams.limit = this.rosterpageSize
      this.rosterQueryParams.note = this.batchId

      request({
        url: 'build/labourInfocollection/listSafetyTraining',
        method: 'post',
        data: this.rosterQueryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.rosterList = response.data.list
        this.rostertotal = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 题目分类
    trainingTypeFc(row, column) {
      return this.selectDictLabel(this.trainingType, row.trainingType)
    },
    /** 查询数据列表 */
    getMembersList() {
      var pid = this.batchId
      request({
        url: 'build/bSafetySafetyTrainingMembers/selectByPId/' + pid,
        method: 'get'
      }).then(response => {
        this.membersList = response.data
      })
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
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
    },
    /** 搜索按钮操作 */
    rosterhandleQuery() {
      this.rosterQueryParams.page = 1
      this.rosterQueryParams.note = this.batchId
      this.rosterQueryParams.limit = this.rosterpageSize
      this.getRosterList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.sjtbDate = ''
      // this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.batchId = guid()
      this.open = true
      this.title = '新增'
      this.membersList = []
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$confirm('确定同意该申请?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            request({
              url: 'build/bSafetySafetyTraining/edit',
              method: 'post',
              data: this.form,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                this.open = false
          
              }
            })
          } else {
            this.form.id = this.batchId
            request({
              url: 'build/bSafetySafetyTraining/add',
              method: 'post',
              data: this.form,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess('新增成功')
                this.open = false
            
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id || this.ids

      this.$confirm('确定不同意该申请?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {

      }).then(() => {
   
        
      }).catch(function() {})
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
          url: 'build/bSafetySafetyTrainingMembers/deleteDetails/' + id,
          method: 'get'
        })
      }).then(() => {
        this.getMembersList()
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

    },
    // 更改当前页数
    handleCurrentChange(val) {
      this.currentPage = val

    },
    // 打开花名册列表
    handleAddMembers() {
      this.getRosterList()
      this.membersOpen = true
      this.titleMembers = '人员列表'
      this.resetForm('rosterQueryParams')
    },

    //性别
    sexFormat(row, column){
      return this.selectDictLabel(this.sexOptions, row.sex)
    },
    //劳务工种
    WTFormat(row, column){
      var datas  = this.worktypeList
      var value = row.workTypeId
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].workType)
          return true
        }
      })
      return actions.join('')
    },
    //班组
    teamNameFormat(row, column){
      var datas  = this.BZList
      var value = row.teamName
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].teamName)
          return true
        }
      })
      return actions.join('')

    },
    //班组
    teamNameFormat2(row, column){
      var datas  = this.BZList
      var value = row.infocollectionTeamName
      var actions = []

      Object.keys(datas).some((key) => {
        if (datas[key].id == ('' + value)) {
          actions.push(datas[key].teamName)
          return true
        }
      })
      return actions.join('')

    },
    /** 提交按钮 */
    submitFormMembers: function() {
      var k = this.$refs.accountTable.selection
      var s= []
      for (var i = 0; i < k.length; i++) {
        s.push(k[i].id)
      }

      console.log(s)
      if(s == "" || s == null ){
        this.membersOpen = false
        this.getMembersList()
        return
      }
      request({
        url: 'build/bSafetySafetyTrainingMembers/addByIds',
        method: 'post',
        data: {ids:s,pid:this.batchId},
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        if (response.code === 0) {
          this.membersOpen = false
          this.getMembersList()
        }
      })
    },
    cancelMembers() {
      this.membersOpen = false
    },
    /** 查询所工种数据列表 */
    getWorktypeList() {
      getWTList().then(response => {
        this.worktypeList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 日期去除时分秒 */
    formatterTime(row,column){
      let data = row[column.property]
      return  /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },
    /** 查询所有分包商id和name */
    getListByFBS() {
      getListByFBS().then(response => {
        this.FBSList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 查询所有班组名称 */
    getListByBZ() {
      getListByBZ().then(response => {
        this.BZList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 更改每页展示数量
    rosterHandleSizeChange(val) {
      this.rosterpageSize = val
      this.getRosterList()
    },
    // 更改当前页数
    rosterHandleCurrentChange(val) {
      this.rostercurrentPage = val
      this.getRosterList()
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
</style>
