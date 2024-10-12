<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
	<!-- <el-form-item label="项目名称" prop="projectId">
	  <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
		<el-option
		  v-for="item in dictData.projectOptions"
		  :key="item.id"
		  :label="item.name"
		  :value="item.id">
		</el-option>
	  </el-select>
	</el-form-item> -->
      <el-form-item label="申请人" prop="applyBy">
        <el-input
          v-model="queryParams.applyBy"
          placeholder="请输入申请人"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
	<el-form-item label="申请评级" prop="levelId">
	  <el-select v-model="queryParams.levelId" placeholder="请选择申请评级">
		<el-option
		  v-for="item in dictData.evalLevelList"
		  :key="item.id"
		  :label="item.levelName"
		  :value="item.id">
		</el-option>
	  </el-select>
	</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
			  v-if="scope.row.status === '0' || scope.row.status === '1'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >评分
            </el-button>
            <el-button
			  v-if="scope.row.status === '0' || scope.row.status === '1'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="refuseClick(scope.row.id)"
            >拒绝
            </el-button>
			<el-button
			  v-if="scope.row.status === '2' || scope.row.status === '3'"
			  size="mini"
			  type="text"
			  icon="el-icon-edit"
			  @click="detailClick(scope.row.id)"
			>详情
			</el-button>
			<el-button
			  v-if="scope.row.status === '2'"
			  size="mini"
			  type="text"
			  icon="el-icon-edit"
			  @click="wordClick(scope.row.id)"
			>预览
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

  import Create from './Create'

  import {depListAPI} from '@/api/common' // 直属上级接口
  import {getList, applyRefuse, getProjectsList,downEvalWord} from "../api/apply"
  import {selectList} from "../api/level"
  import {downloadFileWithBuffer,sortColumn} from '@/utils'

  export default {
    /** DEMO列表 */
    name: 'evalApply',
    components: {
      Create,
    },
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
            label: "项目名称"
          },
          {
            prop: "levelName",
            label: "申请评级"
          },
          {
            prop: "applyDes",
            label: "申请描述"
          },
          {
            prop: "applyBy",
            label: "申请人"
          },
          {
            prop: "applyTime",
            label: "申请时间"
          },
          {
            prop: "appBy",
            label: "审批人"
          },
          {
            prop: "score",
            label: "总分值"
          },
          {
            prop: "appLevel",
            label: "评估等级"
          },
          {
            prop: "status",
            label: "状态"
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
			// 应用类型
			classTypeDict: [],
			// 分项类别
			itemTypeDict: [],
			// 工地等级数据
			evalLevelList: [],
			// 状态
			statusDict: [
				{
					dictValue: '0',
					dictLabel: '待评估',
				},
				{
					dictValue: '1',
					dictLabel: '评估中',
				},
				{
					dictValue: '2',
					dictLabel: '申请通过',
				},
				{
					dictValue: '3',
					dictLabel: '申请拒绝',
				}
			],
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
	  // 获取字典数据
	  this.initDicts()
      //获取列表数据
      this.getList();
    },
    methods: {
    	sortChange(column) {
	      sortColumn(this,column);
	    },
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;
        //排序
        if (this.sortData.length > 0) {
          var orders = [];
          this.sortData.forEach(function (item, index) {
            var one = {};
            one.prop = item.prop;
            one.asc = item.order == 'ascending';
            orders.push(orders);
          })
          this.queryParams.orders = orders;
        }
        //场景ID
        if (this.sceneId) {
          this.queryParams.sceneId = this.sceneId
        }
        if (this.filterObj && this.filterObj.length > 0) {
          this.queryParams.searchList = this.filterObj
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
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
        if (column.property == 'code') {
          this.rowID = row.id;
          this.showDview = true;
        }
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		  if (column.property === 'status') {
		  	return this.selectDictLabel(this.dictData.statusDict, row.status)
		  } else if (column.property === 'classType') {
		  	return this.selectDictLabel(this.dictData.classTypeDict, row.classType)
		  } else if (column.property === 'itemType') {
		  	return this.selectDictLabel(this.dictData.itemTypeDict, row.itemType)
		  }
		  return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
      //按钮操作权限
      canHandle(type) {
        return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      /**
       * 字段排序
       */
//    sortChange(column, prop, order) {
//      this.currentPage = 1
//      this.sortData = column
//      this.getList()
//    },
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
       * 详情点击
       */
      detailClick(id) {
        this.action.type = 'detail';
        this.action.id = id;
        this.createShow = true
      },
      /**
       * 下载点击
       */
      wordClick(id) {
		  
		  
		  var params = {
			  id:id,
				url: 'utilsPrint/downEvalWord'
		  }
		  
		  this.$router.push({
		     path: this.$route.path + '/preview',
		     query: {
		       printParams: params,
		     }
		   })
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
       * 拒绝点击
       */
		refuseClick(id) {
		  this.$confirm("确定拒绝申请吗？", '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		  })
			.then(() => {
			  this.loading = true;
				applyRefuse(id).then(res => {
					if (res.code == 0) {
					  this.$message.success("申请已拒绝");
					  this.getList();
					} else {
					  this.$message.error(res.msg);
					}
					this.loading = false;
				}).catch(() => {
				  this.loading = false;
				})
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
	  	this.getDicts('b_eval_class_type').then(response => {
	  	  this.dictData.classTypeDict = response.data
	  	})
	  	this.getDicts('b_eval_item_type').then(response => {
	  	  this.dictData.itemTypeDict = response.data
	  	})
		// 查询项目数据
		getProjectsList().then(res => {
		  this.dictData.projectOptions = res.data;
		})

		// 查询工地等级数据
		selectList().then(res =>{
			this.dictData.evalLevelList = res.data
		})
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
