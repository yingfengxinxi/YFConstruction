<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
			<el-form-item label="项目名称" prop="projectId">
			  <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
				<el-option
				  v-for="item in dictData.projectOptions"
				  :key="item.id"
				  :label="item.name"
				  :value="item.id">
				</el-option>
			  </el-select>
			</el-form-item>
      <!-- <el-form-item label="隐患编号" prop="hiddenNum">
        <el-input
          v-model="queryParams.hiddenNum"
          placeholder="请输入"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item> -->
	  <el-form-item label="隐患类别" prop="category">
	    <el-select v-model="queryParams.category" clearable placeholder="请选择">
	      <el-option
	    	v-for="item in dictData.hazardCategoryDict"
	    	:key="item.dictValue"
	    	:label="item.dictLabel"
	    	:value="item.dictValue">
	      </el-option>
	    </el-select>
	  </el-form-item>
	  <el-form-item label="隐患等级" prop="level">
	    <el-select v-model="queryParams.level" clearable placeholder="请选择">
	      <el-option
	    	v-for="item in dictData.hazardLevelDict"
	    	:key="item.dictValue"
	    	:label="item.dictLabel"
	    	:value="item.dictValue">
	      </el-option>
	    </el-select>
	  </el-form-item>
      <el-form-item label="风险点名称" prop="riskName">
        <el-input
          v-model="queryParams.riskName"
          placeholder="请输入"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
	  <el-form-item label="隐患状态" prop="infoState">
		<el-select v-model="queryParams.infoState" clearable placeholder="请选择">
		  <el-option
			v-for="item in dictData.infoStateDict"
			:key="item.dictValue"
			:label="item.dictLabel"
			:value="item.dictValue">
		  </el-option>
		</el-select>
	  </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <!-- <el-button
            v-if="canHandle('add')"
            class="xr-btn--orange"
            icon="el-icon-plus"
            type="primary"
            @click="createClick">新建
          </el-button> -->
        </div>
      </el-form-item>
    </el-form>
    <div
      v-empty="!canHandle('list')"
      xs-empty-icon="nopermission"
      xs-empty-text="暂无权限"
      >
      <el-table
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        :header-cell-class-name="headerCellClassName"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
        @selection-change="handleSelectionChange"
        @sort-change="sortChange">
           <el-table-column fixed label="序号"  align='center' type="index"  width="60"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :align="item.align"
          :formatter="fieldFormatter"
          sortable="custom"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
			  v-if="scope.row.infoState == '0'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >查看
            </el-button>
            <el-button
			  v-if="scope.row.infoState == '1'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="rectifyClick(scope.row.id)"
            >查看
            </el-button>
            <el-button
			  v-if="scope.row.infoState == '2'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="recheckClick(scope.row.id)"
            >查看
            </el-button>
            <el-button
			  v-if="scope.row.infoState == '3'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="detailsClick(scope.row.id)"
            >查看
            </el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteClick(scope.row.id)"
            >删除
            </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>

    <!-- 新建 -->
    <create
      v-if="createShow"
      :businessType="businessType"
      :action="action"
	  :dictData="dictData"
      @close="createShow = false"
      @save-success="getList"
    />
  </div>
</template>

<script>


  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import CheckStatusMixin from '@/mixins/CheckStatusMixin'//审核状态

  import Create from './Create'

  import {getList, delByIds, getSubcontractorList, getProjectsList} from "../api/inspectionListNew"

  import { sortColumn } from '@/utils/index';

  export default {
    /** DEMO列表 */
    name: 'inspectionPlan',
    components: {
      Create,
    },
    mixins: [CheckStatusMixin],
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 250, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "projectName",
            label: "项目名称",
            align:'center'
          },
          {
            prop: "hiddenNum",
            label: "隐患编号",
            align:'center'
          },
          {
            prop: "category",
            label: "隐患类别",
            align:'center'
          },
          {
            prop: "level",
            label: "隐患等级",
            align:'center'
          },
          {
            prop: "riskName",
            label: "风险点名称",
            align:'center'
          },
          {
            prop: "riskDesc",
            label: "隐患描述",
            align:'center'
          },
          {
            prop: "checkPersonName",
            label: "检查负责人",
            align:'center'
          },
          {
            prop: "rectifyTime",
            label: "整改期限",
            align:'center'
          },
          {
            prop: "infoState",
            label: "隐患状态",
            align:'center'
          },
        ],
        sortData: [], // 字段排序
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams: {},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
		// 字典数据
		dictData: {
			// 隐患类别
			hazardCategoryDict: [],
			// 隐患等级
			hazardLevelDict: [],
			// 事故类型
			accidentTypeDict:[],
			// 检查类型
			routineCheckTypeDict: [],
			// 分包商
			subcontractorList: [],
			// 项目下拉框列表
			projectOptions:[],
			// 隐患状态
			infoStateDict: [
				{
				dictLabel:'已检查',
				dictValue:'0'
				},
				{
				dictLabel:'已下发',
				dictValue:'1'
				},
				{
				dictLabel:'已整改',
				dictValue:'2'
				},
				{
				dictLabel:'已复检',
				dictValue:'3'
				},
			],

		},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
        /** 控制详情展示 */
        rowID: '', // 行信息
        showDview: false,
      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
      //权限 end
      titleIcon() {
        return require(`@/assets/img/crm/customer.png`)
      },
    },
    mounted() {
      /** 控制table的高度 */
      window.onresize = () => {
        this.updateTableHeight()
      }

		// 查询项目数据
		getProjectsList().then(res => {
			this.dictData.projectOptions = res.data
		})
	  // 获取字典数据
	  this.initDicts()
	  // 分包商数据
	  this.getSubcontractorList()
      //获取列表数据
      this.getList();
    },
    methods: {
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;


        getList(this.queryParams)
          .then(res => {
            this.list = res.data.list;
            if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
              this.currentPage = this.currentPage - 1
              this.getList()
            } else {
              this.total = res.data.totalRow
              this.loading = false
            }
            this.$nextTick(() => {
              document.querySelector('.el-table__body-wrapper').scrollTop = 1
            })
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
        if (column.property == 'code') {
          this.rowID = row.id;
          this.showDview = true;
        }
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		  if (column.property === 'category') {
		  	return this.selectDictLabel(this.dictData.hazardCategoryDict, row.category)
		  } else if (column.property === 'level') {
		  	return this.selectDictLabel(this.dictData.hazardLevelDict, row.level)
		  } else if (column.property === 'infoState') {
		  	return this.selectDictLabel(this.dictData.infoStateDict, row.infoState)
		  }
		  return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
      //按钮操作权限
      canHandle(type) {
        return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      /** 勾选操作 */
      // 当选择项发生变化时会触发该事件
      handleSelectionChange(val) {
        this.selectionList = val // 勾选的行
        this.updateTableHeight();
      },

      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },
      /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'code') {
          return 'can-visit--underline can-visit--bold'
        }
      },

      headerCellClassName({row, column, rowIndex, columnIndex}) {
        /*if (column.property === 'code') {
          return 'header-can-visit-backgroud'
        }*/
        return ''
      },
      // 更改每页展示数量
      handleSizeChange(val) {
        Lockr.set('crmPageSizes', val)
        this.pageSize = val
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
		this.currentPage = 1;
        this.queryParams = {};
        this.getList()
      },
      /**
       * 新建点击
       */
      createClick() {
        this.action.type = 'save';
        this.createShow = true
      },
      /**
       * 修改点击
       */
      updateClick(id) {
        this.action.type = 'update';
        this.action.id = id;
        this.createShow = true
      },
      /**
       * 整改点击
       */
      rectifyClick(id) {
        this.action.type = 'rectify';
        this.action.id = id;
        this.createShow = true
      },
      /**
       * 复检点击
       */
      recheckClick(id) {
        this.action.type = 'recheck';
        this.action.id = id;
        this.createShow = true
      },
      /**
       * 详情点击
       */
      detailsClick(id) {
        this.action.type = 'details';
        this.action.id = id;
        this.createShow = true
      },
      deleteClick(id) {
        this.$confirm("确定删除选中的数据吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.loading = true;
            delByIds(id).then(res => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.getList();
              } else {
                this.$message.error(res.msg);
              }
              this.loading = false;
            }).catch(err => {
              this.loading = false;
            });
          })
          .catch(() => {
          })
      },
      /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 300 : 250
        this.tableHeight = offsetHei - removeHeight
      },
	  // 初始化字典数据
	  initDicts() {
	  	this.getDicts('b_hazard_category').then(response => {
	  	  this.dictData.hazardCategoryDict = response.data
	  	})
	  	this.getDicts('b_hazard_level').then(response => {
	  	  this.dictData.hazardLevelDict = response.data
	  	})
	  	this.getDicts('b_hidden_accident_type').then(response => {
	  	  this.dictData.accidentTypeDict = response.data
	  	})
	  	this.getDicts('b_routine_check_type').then(response => {
	  	  this.dictData.routineCheckTypeDict = response.data
	  	})
	  },
	  // 查询分包商
	  getSubcontractorList() {
	    getSubcontractorList()
	      .then(response => {
	        this.dictData.subcontractorList = response.data
	      })
	      .catch(() => {
	      })
	  },
      sortChange(column) {
        sortColumn(this,column);
      },
    }
  }
</script>

<style lang="scss" scoped>
  //表头
  .table-head-container {
    //border-bottom: 1px solid #e6e6e6;
    border-top: 1px solid #e6e6e6;
    background-color: white;
  }

  .th-container {
    font-size: 13px;
    height: 50px;
    padding: 0 20px;

    .scene-select {
      width: 180px;
      /deep/ .el-input__inner {
        cursor: pointer;
      }
    }

    .filter-button {
      margin-left: 20px;
      /deep/ i {
        font-size: 14px;
        margin-right: 5px;
      }
    }
  }

  /** 场景和筛选 */
  .condition_title {
    cursor: pointer;
  }

  .condition_title:hover {
    color: $xr-color-primary;
  }

  .m-arrow {
    margin: 0 8px;
  }

  .c-filtrate {
    margin: 0 10px 0 30px;
    width: 12px;
  }

  /** 勾选操作 */
  .selection-bar {
    font-size: 12px;
    height: 50px;
    padding: 0 20px;
    color: #777;

    .selected—title {
      flex-shrink: 0;
      padding-right: 20px;
      color: #333;
      .selected—count {
        color: $xr-color-primary;
      }
    }
  }

  .selection-items-box {
    overflow-x: auto;
    overflow-y: hidden;
    padding: 0 15px;

    .el-button {
      color: #666;
      background-color: #f9f9f9;
      border-color: #e5e5e5;
      font-size: 12px;
      height: 28px;
      border-radius: 4px;
      /deep/ i {
        font-size: 12px;
        margin-right: 5px;
      }
    }

    .el-button--primary:hover {
      background: $xr-color-primary;
      border-color: $xr-color-primary;
      color: #ffffff;
    }

    .el-button + .el-button {
      margin-left: 15px;
    }
  }

  //页面头部
  .c-container {
    height: 60px;
    position: relative;
    //z-index: 100;
    .title-icon {
      width: 30px;
      height: 30px;
      margin-right: 10px;
    }

    .title {
      float: left;
      width: auto;
      padding-left: 28px;
      font-size: 16px;
      font-weight: 600;
      margin-top: 15px;
    }
    .sc-container {
      width: 300px;
      margin: -16px 0 0 -150px;
      position: absolute;
      left: 50%;
      top: 50%;
    }

    .el-input {
      /deep/ .el-input-group__append {
        background-color: $xr-color-primary;
        border-color: $xr-color-primary;
        color: white;
      }
    }

    .right-container {
      margin-right: -10px;
      float: right;
      margin: 15px 20px 0 0;
      position: relative;

      .dup-check-btn,
      .xr-btn--orange {
        margin-left: 0;
        margin-right: 10px;
      }

      .right-item {
        float: left;
        margin-right: 10px;
        padding: 8px 10px;
        font-size: 13px;
        border-radius: 2px;
      }
    }
  }

  @import '@/views/styles/table.scss';
</style>
