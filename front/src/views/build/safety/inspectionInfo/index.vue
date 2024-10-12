<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="检查编号" prop="checkCode">
        <el-input
          v-model="queryParams.checkCode" style="width: 120px"
          placeholder="请输入检查编号"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
	  <el-form-item label="计划名称" prop="planName">
	    <el-input
	      v-model="queryParams.planName"  style="width: 120px"
	      placeholder="请输入计划名"
	      clearable
	      size="small"
	      @keyup.enter.native="getList"
	    />
	  </el-form-item>
	  <el-form-item label="执行时间" prop="executeDates">
	    <el-date-picker
		  v-model="executeDates"
		  type="daterange"
		  range-separator="至"
		  start-placeholder="开始日期"
		  end-placeholder="结束日期"
		  value-format="yyyy-MM-dd HH:mm:ss"
		  :default-time="['00:00:00', '23:59:59']">
			</el-date-picker>
	  </el-form-item>
	  <el-form-item label="检查人" prop="inspectorId">
	    <el-select v-model="queryParams.inspectorId" clearable placeholder="请选择" style="width: 100px">
	      <el-option
	    	v-for="item in dictData.userListData"
	    	:key="item.userId"
	    	:label="item.realname"
	    	:value="item.userId">
	      </el-option>
	    </el-select>
	  </el-form-item>
	  <el-form-item label="执行状态" prop="isExecute">
	    <el-select v-model="queryParams.isExecute" clearable placeholder="请选择" style="width: 100px">
	      <el-option
	    	v-for="item in isExecuteOptions"
	    	:key="item.value"
	    	:label="item.label"
	    	:value="item.value">
	      </el-option>
	    </el-select>
	  </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
		<el-form-item style="float: right">
		  <div class="right-container">
			<el-button
			  v-if="canHandle('add')"
			  class="xr-btn--orange"
			  icon="el-icon-plus"
			  type="primary"
			  @click="createClick">新建
			</el-button>
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
        @sort-change="sortChange"
        @selection-change="handleSelectionChange">
         <el-table-column fixed label="序号"  align='center' type="index"  width="60"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          align="center"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
			  v-if="scope.row.isExecute === '0'"
              size="mini"
              type="text"
              icon="el-icon-video-play"
              @click="executeClick(scope.row.id)"
            >执行
            </el-button>
            <el-button
			  v-if="scope.row.isExecute === '1' && scope.row.rectifyStatus === '1'"
              size="mini"
              type="text"
              icon="el-icon-arrow-right"
              @click="rectifyClick(scope.row.id)"
            >整改
            </el-button>
            <el-button
			  v-if="scope.row.isExecute === '1' && scope.row.rectifyStatus === '2'"
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="recheckClick(scope.row.id)"
            >复检
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-message"
              @click="informClick(scope.row.id)"
            >整改通知
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
  import {sortColumnNoToLowerCase} from '@/utils'

  import Create from './Create'

  import {depListAPI, userListAPI} from '@/api/common' // 直属上级接口  用户接口
  import {getList, delTrueByIds, getPrintData, queryPrintTemplateByType} from "../api/inspectionInfo"

  export default {
    /** DEMO列表 */
    name: 'inspectionInfo',
    components: {
      Create,
    },
    mixins: [CheckStatusMixin],
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画
        /** 页面头部 */
        inputContent: '',//查询值
        moreTypes: [
          {type: 'import', name: '导入', icon: 'import'},
          {type: 'export', name: '导出', icon: 'export'},
        ],//更多操作
        /** 选中时操作 */
        selectionHandleItems: [
          {
            name: '删除',
            type: 'delete',
            icon: 'delete'
          },
          {
            name: '导出选中',
            type: 'export',
            icon: 'export'
          },
        ],
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 310, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "checkCode",
            label: "检查编号"
          },
          {
            prop: "planName",
            label: "计划名称"
          },
          {
            prop: "deptName",
            label: "检查部门"
          },
          {
            prop: "realname",
            label: "检查人"
          },
          {
            prop: "repairTimeLimit",
            label: "整改期限"
          },
          {
            prop: "checkDate",
            label: "检查时间"
          },
          {
            prop: "isExecute",
            label: "执行状态"
          },
        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams: {},
		// 执行时间范围
		executeDates: '',
		// 执行状态下拉数据
		isExecuteOptions:[
			{
				label: "未执行",
				value: "0"
			},
			{
				label: "已执行",
				value: "1"
			}
		],

        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'execute', data: null},
		// 字典数据
		dictData: {
			// 计划等级
			planLevelDict: [],
			// 执行部门
			showDeptTreeData: [],
			// 用户列表
			userListData: []
		},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
        /** 控制详情展示 */
        rowID: '', // 行信息
        showDview: false,
        /** 高级筛选 2021-03-15 暂未使用*/
        filterObj: [], // 筛选确定数据
        sceneId: '', // 场景筛选ID
        sceneName: '', // 场景名字
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
      //获取列表数据
      this.getList();
	  // 获取字典数据
	  this.initDicts()
	  // 获取执行部门数据
	  this.getDeptTreeList()
	  // 查询用户数据
	  this.getUserList()
    },
    methods: {
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;
        //场景ID
        if (this.sceneId) {
          this.queryParams.sceneId = this.sceneId
        }
        if (this.filterObj && this.filterObj.length > 0) {
          this.queryParams.searchList = this.filterObj
        }

		// 执行时间范围
		if (this.executeDates != null && this.executeDates != '') {
			this.queryParams.beginDate = this.executeDates[0]
			this.queryParams.endDate = this.executeDates[1]
		} else {
			this.queryParams.beginDate = ''
			this.queryParams.endDate = ''
		}

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
      /**
       *更多操作点击
       */
      handleTypeDrop(command) {
        if (command == 'delete') {//删除
          this.$confirm("确定删除选中的数据吗？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              const ids = this.selectionList.map(item => item['id'])
              this.loading = true;
              delTrueByIds(ids).then(res => {
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
        } else if (command == 'export') {//导出

        } else if (command == 'import') {//导入

        }
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
		  if (column.property === 'planLevel') {
		  	return this.selectDictLabel(this.dictData.planLevelDict, row.planLevel)
		  } else if (column.property === 'isExecute') {
			  return row.isExecute === '1' ? '已执行' : '未执行'
		  } else if (column.property === 'checkDate'
					|| column.property === 'repairTimeLimit') {
			// 获取单元格数据
			let data = row[column.property]
			if(data == null) {
				return null
			}
			let dt = new Date(data)
			let month = dt.getMonth() + 1
			let day = dt.getDate()
			return dt.getFullYear() + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day)

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
      sortChange(column) {
		var list = []
		list.push("deptName");
		sortColumnNoToLowerCase(this,column,list);
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
		this.executeDates = ''
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
       * 执行点击
       */
      executeClick(id) {
        this.action.type = 'execute';
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
	   * 整改通知点击
	   */
	  informClick(id) {
	    //组装数据
	    getPrintData(id).then(res => {
			let type = 10011
			let data = res.data
			queryPrintTemplateByType(type).then(res => {
				let templateId = res.data.templateId
				 //Copy 进行打印
				 var params = {
				   templateId: templateId,
				   dataId: id,
				   printData: data,
				 }
				this.$router.push({
				   path: this.$route.path + '/print',
				   query: {
				     printParams: params,
				   }
				 })
			})
	    })
	      .catch(() => {
	        this.loading = false
	      })
	  },
      /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 320
        this.tableHeight = offsetHei - removeHeight
      },
	  // 初始化字典数据
	  initDicts() {
	  	this.getDicts('b_safety_plan_level').then(response => {
	  	  this.dictData.planLevelDict = response.data
	  	})
	  },

	  // 执行部门 获取树形列表
	  getDeptTreeList() {
	    depListAPI({type: 'tree'})
	      .then(response => {
	        this.dictData.showDeptTreeData = response.data || []
			if (!this.dictData.showDeptTreeData[0].children) {
				this.dictData.showDeptTreeData[0].children = []
			}
	      })
	      .catch(() => {
	      })
	  },

	  // 查询当前项目的用户
	  getUserList() {
		  var params = {
			  pageType: 0
		  }
		  userListAPI(params).then(res => {
			  this.dictData.userListData = res.data.list
		  })
		  .catch(() => {

		  })
	  }

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
