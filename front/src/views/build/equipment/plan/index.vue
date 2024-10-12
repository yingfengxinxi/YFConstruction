<template>
  <div class="main">
    <!-- <xr-header
      icon-class="wk wk-project"
      icon-color="#33D08F"
      label="维护计划管理"/> -->

    <div class="main-body">
      <el-form class="searchFrom" v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="92px" style="padding: 20px 20px 0px; background-color: white; border-top: 0px solid rgb(230, 230, 230);">
        <el-form-item label="执行计划名称">
          <el-input
            v-model="queryParams.planName"
            placeholder="请输入计划名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
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
            @click="handleAdd">新建
          </el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="typeList" :height="tableHeight" highlight-current-row="" @sort-change="sortChange" >
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column label="维护计划名称" prop="planName" width="250px" show-overflow-tooltip sortable="custom" />
        <el-table-column label="计划类型" prop="planType" :formatter="planTypeFormat" show-overflow-tooltip sortable="custom" />
        <el-table-column label="计划等级" prop="level" :formatter="levelFormat" show-overflow-tooltip sortable="custom" />
        <el-table-column label="执行周期" prop="cycle" align="center" show-overflow-tooltip sortable="custom" />
        <el-table-column label="执行周期单位" prop="unit" :formatter="unitFormat" align="center" show-overflow-tooltip sortable="custom" />
        <el-table-column label="执行部门" prop="deptName" width="150px" show-overflow-tooltip sortable="custom" />
        <el-table-column label="是否启用" prop="enable" :formatter="enableFormat" align="center" show-overflow-tooltip sortable="custom" />
        <el-table-column label="下次执行日期" prop="nextDate" align="center" width="150px" show-overflow-tooltip sortable="custom" >
          <template slot-scope="scope">
            <span>{{ dateFormat(scope.row.nextDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除
            </el-button>
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
      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="new-dialog-form">
          <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
            <span>基本信息</span>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="计划名称" prop="planName">
                <el-input v-model="form.planName" placeholder="请输入计划名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="计划类型" prop="planType">
                <el-select v-model="form.planType" placeholder="请选择计划类型">
                  <el-option
                    v-for="item in planTypes"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否启用" prop="enable">
                <el-select v-model="form.enable" placeholder="请选择是否启用">
                  <el-option
                    v-for="item in enables"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="执行周期" prop="cycle">
                <el-input type="number" v-model="form.cycle" placeholder="请输入执行周期"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行周期单位" prop="unit">
                <el-select v-model="form.unit" placeholder="请选择执行周期单位"
                >
                  <el-option
                    v-for="item in units"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="计划等级" prop="level">
                <el-select v-model="form.level" placeholder="请选择计划等级">
                  <el-option
                    v-for="item in levels"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行部门" prop="deptId">
                <select-tree :options="showDeptTreeData" v-model="form.deptId" :props="defaultProps" :expandAll="true"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="作业名称" prop="workName">
                <el-input v-model="form.workName" placeholder="请输入作业名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="作业指导" prop="workDirect">
                <el-input type="textarea" :rows="2" v-model="form.workDirect" placeholder="请输入作业指导"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="启动日期" prop="startDate">
                <el-date-picker
                  v-model="form.startDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="请选择启动日期"
                  :picker-options="pickerOptions"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col v-if="!isAdd" :span="12">
              <el-form-item label="下次执行日期" prop="nextDate">
                <el-date-picker
                                v-model="form.nextDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="下次执行日期"
                                :picker-options="pickerOptions"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="padding-bottom: 10px;">
            <span>计划维护设备信息</span>
          </el-row>
          <el-row>
            <AssetList :plan-id="form.planId" @change="assetChange" ref="assetList"></AssetList>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

  import {depListAPI} from '@/api/common' // 直属上级接口
	import {sortColumn } from '@/utils'
  import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import SelectTree from '@/components/SelectTree.vue';

  import AssetList from './components/assetList'

  import {
    planList,
    planSave,
    planDelete,
    planGet
  } from '../api/plan'

  const that = this;

  export default {
    name: 'equplan',
    components: {
      Reminder,
      XrHeader,
      SelectTree,
      AssetList
    },
    data() {
      return {
        pickerOptions: { // 限制收货时间不让选择今天以前的
          disabledDate(time) {
            return time.getTime() < Date.now()
          }
        },

        tableHeight: document.documentElement.clientHeight - 260, // 表的高度
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
        // 字典表格数据
        typeList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        // 日期范围
        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          dictName: undefined,
          dictType: undefined,
          status: undefined
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
        planTypes: [],
        units: [],
        enables: [],
        levels: [],
        assetDatas: [],//选中的设备列表
        // 表单校验
        rules: {
          planName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          startDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          nextDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          cycle: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          unit: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          level: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          deptId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          enable: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          workName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          workDirect: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          /*serviceResult: [
            {required: true, message: '必填项不得为空', trigger: 'blur'},
          ],*/
        },
        isAdd: false,
        //
        oldNextDate: '',
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
      this.getDicts('b_equipment_enable').then(response => {
        this.enables = response.data
      })
      this.getDicts('b_equipment_plan_level').then(response => {
        this.levels = response.data
      })
      this.getDeptTreeList();
    },
    watch: {
      /* 'form.cycle': function () {
        this.planChange()
      },
      'form.unit': function () {
        this.planChange()
      },
      'form.startDate': function () {
        this.planChange()
      }, */
      'form.nextDate': function () {
        console.log(this.form.nextDate)
        if(this.isAdd){
          var startDate = this.form.startDate;
          var newNextDate = this.form.nextDate;
          if (newNextDate <= startDate) {
            this.$message({
              message: '下次执行日期必须在启动日期【' + startDate + '】之后',
              type: 'warning',
              duration:3000
            });
            this.form.nextDate = null;
          }
        }else{
          var oldNextDate = this.oldNextDate;
          if (oldNextDate) {
            var newNextDate = this.form.nextDate;
            if (newNextDate < oldNextDate) {
              this.$message({
                message: '下次执行日期必须在原下次执行日期【' + this.oldNextDate + '】之后',
                type: 'warning',
                duration:3000
              });
              this.form.nextDate = oldNextDate;
            }
          }
        }
      }
    },
    methods: {
    	sortChange(column) {
      sortColumn(this,column);
    },
      //计划改变
      planChange() {
        if (!this.isAdd) {
          return;
        }
        if (this.form.cycle && this.form.unit && this.form.startDate) {
          //计算下次执行时间
          var cycle = parseInt(this.form.cycle);
          var startDate = new Date(this.form.startDate + ":00:00");
          var nextDate = new Date();
          var unit = this.form.unit;
          if (unit == "1") {//小时
            nextDate = startDate.setHours(startDate.getHours() + cycle);
          }
          if (unit == "2") {//天
            nextDate = startDate.setDate(startDate.getDate() + cycle);
          }
          if (unit == "4") {//月
            nextDate = startDate.setMonth(startDate.getMonth() + cycle);
          }
          if (unit == "5") {//年
            nextDate = startDate.setFullYear(startDate.getFullYear() + cycle);
          }
          if (unit == "3") {//周
            nextDate = startDate.setDate(startDate.getDate() + (7 * cycle));
          }
          nextDate = new Date(nextDate);
          this.form.nextDate = nextDate.getFullYear() + "-" + (nextDate.getMonth() + 1) + "-" + nextDate.getDate() + " " + nextDate.getHours() + ":00:00";
        } else {
          this.form.nextDate = null;
        }
      },
      //是否本日
      isToday() {
        var today = new Date(new Date().toDateString() + " 00:00:00");
        var nextDate = undefined;
        if (this.form.nextDate.indexOf(":00:00") > 0) {
          nextDate = new Date(new Date(this.form.nextDate).toDateString() + " 00:00:00");
        } else {
          nextDate = new Date(new Date(this.form.nextDate + ":00:00").toDateString() + " 00:00:00");
        }
        if (today.valueOf() == nextDate.valueOf()) {
          return true;
        }
        return false;
      },
      // 获取树形列表
      getDeptTreeList() {
        depListAPI({type: 'tree'})
          .then(response => {
            this.showDeptTreeData = response.data || []
            if (this.showDeptTreeData.length > 0) {
              if (!this.showDeptTreeData[0].children) {
                this.showDeptTreeData[0].children = []
              }
            }
          })
          .catch(() => {
          })
      },
      /** 查询字典类型列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize
        planList(this.queryParams).then(res => {
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
      },
      //字典翻译
      planTypeFormat(row, column) {
        return this.selectDictLabel(this.planTypes, row.planType)
      },
      unitFormat(row, column) {
        return this.selectDictLabel(this.units, row.unit)
      },
      enableFormat(row, column) {
        return this.selectDictLabel(this.enables, row.enable)
      },
      levelFormat(row, column) {
        return this.selectDictLabel(this.levels, row.level)
      },
      assetChange(data) {
        this.assetDatas = data;
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
		// 清空子组件数据
		this.$refs.assetList.tableData = []
		//this.$refs.assetList.clearTableData()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.assetDatas = [];
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
		this.reset()
        this.open = true
        this.title = '添加维护信息'
        this.isAdd = true;
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.dictId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
		this.reset()
        planGet(row.planId).then(response => {
          this.form = response.data;
          this.open = true
          this.title = '修改维护信息'
          this.isAdd = false;
          this.oldNextDate = this.form.nextDate;
        }).catch(() => {
        })

      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.assetDatas.length < 1) {
              this.$message({
                message: '请添加至少一项设备信息',
                type: 'warning'
              });
              return;
            }
            //设备信息
            this.form.assetList = this.assetDatas;
            if (false && this.isAdd && this.form.enable == '1' && this.isToday()) {
              var that = this;
              this.$confirm('将会生成当日待执行计划，是否继续提交?', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(function () {
                planSave(that.form).then(response => {
                  if (response.code === 0) {
                    that.$message({
                      message: '提交成功',
                      type: 'success'
                    });
                    that.open = false
                    that.getList()
                  }
                })
              }).catch(function () {
              })
            } else {
              planSave(this.form).then(response => {
                if (response.code === 0) {
                  this.$message({
                    message: '提交成功',
                    type: 'success'
                  });
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
        const that = this;
        this.$confirm('是否确认删除数据项吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          planDelete(row).then(response => {
            if (response.code === 0) {
              that.$message({
                message: '删除成功',
                type: 'success'
              });
              that.getList()
            }
          })
        }).catch(function () {
        })
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
      dateFormat: function (date, column) {
        if (!date) {
          return "";
        }
        var t = new Date(date);//row 表示一行数据, updateTime 表示要格式化的字段名称
        var year = t.getFullYear(),
          month = t.getMonth() + 1,
          day = t.getDate();
        var newTime = year + '-' +
          (month < 10 ? '0' + month : month) + '-' +
          (day < 10 ? '0' + day : day);
        return newTime;
      },
    }
  }
</script>


<style lang="scss" scoped>
/deep/.el-form--inline .el-form-item{
  display: inline-block;
    margin-right: 10px;
    vertical-align: top;
}
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
    // padding: 20px 20px 0px 20px;
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
  .searchFrom .el-form-item{
    margin-bottom:22px;
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
  	/deep/ .el-dialog__body{
		height: 600px;
		overflow: auto;
	}
  @import '@/views/styles/table.scss';

</style>
