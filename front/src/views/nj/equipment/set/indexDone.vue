<template>
  <div class="main">
    <!-- <xr-header
      icon-class="wk wk-project"
      icon-color="#33D08F"
      label="维护执行记录"/> -->

    <div class="main-body">
      <el-form class="my-form" v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
        <el-row>
          <el-col :span="4">
            <el-form-item label="计划名称">
              <el-input
                v-model="queryParams.planName"
                placeholder="计划名称"
                clearable
                size="small"
                style="width:180px"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="执行人">
              <el-input
                v-model="queryParams.setBy"
                placeholder="执行人"
                clearable
                size="small"
                style="width:160px"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="执行部门">
              <el-form-item>
                <select-tree :options="showDeptTreeData" v-model="queryParams.deptId" :props="defaultProps"
                :expandAll="true" style="width:160px"/>
              </el-form-item>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="执行日期">
              <el-date-picker
                v-model="queryParams.searchSetDate"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="~"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
				value-format="yyyy-MM-dd"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item style="padding-left: 10%">
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <el-table v-loading="loading" :data="typeList"
                :height="tableHeight" highlight-current-row>
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column label="执行编号" prop="setCode">
          <template slot-scope="scope">
            <div class="status-name" @click="detail(scope.row)">
              {{ scope.row.setCode }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="执行部门" prop="deptName"/>
        <el-table-column label="计划名称" prop="planName"/>
        <el-table-column label="计划类型" prop="planType" :formatter="planTypeFormat"/>
        <el-table-column label="执行时间" prop="setDate">
          <template slot-scope="scope">
            <span>{{ dateFormat(scope.row.setDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行周期" prop="cycle" width="150px">
        </el-table-column>
        <el-table-column label="周期单位" prop="unit" :formatter="unitFormat"/>
        <el-table-column label="计划等级" prop="level" :formatter="levelFormat"/>
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
    </div>
    <!-- 详情 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body :lock-scroll="false">
      <el-tabs v-model="activeName">
        <el-tab-pane label="计划信息" name="first">
          <PlanDetail :plan-id="form.planId"></PlanDetail>
        </el-tab-pane>
        <el-tab-pane label="执行记录" name="second">
          <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="new-dialog-form">
            <el-row>
              <el-col :span="24">
                <el-form-item label="执行编号">
                  <el-input v-model="form.setCode" placeholder="执行编号" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="计划名称">
                  <el-input v-model="form.planName" placeholder="计划名称" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="执行日期">
                  <el-date-picker disabled
                                  v-model="form.setDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择执行日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="执行开始时间" prop="startDate">
                  <el-date-picker disabled
                                  v-model="form.startDate"
                                  type="datetime"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="请选择执行开始时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="执行结束时间" prop="endDate">
                  <el-date-picker disabled
                                  v-model="form.endDate"
                                  type="datetime"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="请选择执行结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="执行人" prop="setBy">
                  <el-input v-model="form.setBy" placeholder="请输入执行人" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="执行情况说明" prop="setContent">
                  <el-input type="textarea" :rows="2" v-model="form.setContent" placeholder="请输入执行情况说明"
                            disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import SelectTree from '@/components/SelectTree.vue';

  import {depListAPI} from '@/api/common' // 直属上级接口

  import PlanDetail from "../plan/indexDetail"

  import {
    setList,
    setSave,
    setGet
  } from '../api/set'

  export default {
    name: 'equsetDone',
    components: {
      Reminder,
      XrHeader,
      SelectTree,
      PlanDetail
    },
    data() {
      return {
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
        tableHeight: document.documentElement.clientHeight - 260, // 表的高度
        // 遮罩层
        loading: true,
        typeList: [],
        // 显示搜索条件
        showSearch: true,
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        // 字典表格数据
        planTypes: [],
        units: [],
        levels: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        activeName: 'first',
        // 状态数据字典
        statusOptions: [],
        // 查询参数
        isSet: '1',//未执行
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          searchSetDate:[]
        },
        // 表单参数
        form: {},
        // 数据默认字段
        defaultProps: {
          parent: 'parentId',   // 父级唯一标识
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        showDeptTreeData: [],
        // 表单校验
        rules: {},
      }
    },
    created() {
      this.getList()
      this.getDicts('b_equipment_plan_type').then(response => {
        this.planTypes = response.data
      })
      this.getDicts('b_equipment_plan_unit').then(response => {
        this.units = response.data
      })
      this.getDicts('b_equipment_plan_level').then(response => {
        this.levels = response.data
      })

      this.getDeptTreeList();
    }
    ,
    watch: {}
    ,
    methods: {
      /** 详情按钮操作 */
      detail(row) {
        setGet(row.setId).then(response => {
          this.form = response.data;
          //console.log(this.form.planId)
          this.open = true;
        })

      }
      ,
      // 获取树形列表
      getDeptTreeList() {
        depListAPI({type: 'tree'})
          .then(response => {
            this.showDeptTreeData = response.data || []
          })
          .catch(() => {
          })
      }
      ,
      /** 查询字典类型列表 */
      getList() {
        this.loading = true
        this.queryParams.isSet = this.isSet;
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize
        if(this.queryParams.searchSetDate.length > 0){//日期查询
          this.queryParams.searchStartDate = this.queryParams.searchSetDate[0];
          this.queryParams.searchEndDate = this.queryParams.searchSetDate[1];
        }
        setList(this.queryParams).then(res => {
			this.typeList = res.data.list
			if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
			  this.currentPage = this.currentPage - 1
			  this.getList()
			} else {
			  this.total = res.data.totalRow
			  this.loading = false
			}
        }).catch(() => {
          this.loading = false
        })
      }
      ,
      //字典翻译
      planTypeFormat(row, column) {
        return this.selectDictLabel(this.planTypes, row.planType)
      }
      ,
      unitFormat(row, column) {
        return this.selectDictLabel(this.units, row.unit)
      }
      ,
      levelFormat(row, column) {
        return this.selectDictLabel(this.levels, row.level)
      }
      ,
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      }
      ,
      // 表单重置
      reset() {
        this.form = {}
        this.activeName = 'first';
        this.resetForm('form')
      }
      ,
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      }
      ,
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {		
			searchSetDate:[]
		};
        this.resetForm('queryForm')
        this.handleQuery()
      }
      ,
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getList()
      }
      ,
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      }
      ,
      dateFormat: function (date, column) {
        var t = new Date(date);//row 表示一行数据, updateTime 表示要格式化的字段名称
        var year = t.getFullYear(),
          month = t.getMonth() + 1,
          day = t.getDate();
        var newTime = year + '-' +
          (month < 10 ? '0' + month : month) + '-' +
          (day < 10 ? '0' + day : day);
        return newTime;
      }
      ,
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
    border-top: 1px solid $xr-border-line-color;
    border-bottom: 1px solid $xr-border-line-color;
    padding: 20px 20px 0px 20px;
  }

  .el-table {
    border-left: 1px solid $xr-border-line-color;
    border-right: 1px solid $xr-border-line-color;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }

  .link-type, .link-type:focus {
    color: #337ab7;
    cursor: pointer
  }

  .link-type:focus:hover, .link-type:hover {
    color: #20a0ff
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
    display: none;
  }

  .new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
    padding-left: 10px;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
    border-bottom: 1px solid #e4e7ed;
    padding: 0 0 7px;
    margin: 0 20px 5px;
  }

  .new-dialog-form
  /deep/
  .el-form-item {
    width: 100%;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__label {
    text-align: right;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 120px);
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-date-editor--date, .el-select, .el-date-editor--datetime {
    width: 100%;
  }

  .status-name {
    div {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 3px;
    }
    color: $xr-color-primary;
    cursor: pointer;
  }

  .my-form
  /deep/
  .el-form-item,.el-range-editor{
    width: 100%;
    margin-bottom:0px;
  }
  .my-form
  /deep/
  .el-form-item__content{
    width: calc(100% - 68px);
  }
  .my-form
  /deep/
  .el-range-separator{
    width:7%;
  }

  @import '@/views/styles/table.scss';

</style>
