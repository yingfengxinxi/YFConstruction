<template>
  <div>
<!--    <div class="c-container">-->
<!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
<!--        :src="titleIcon"-->
<!--        class="title-icon">方案变更管理-->
<!--      </flexbox>-->
<!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="编号">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编号"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="报警类型">
        <el-select v-model="queryParams.alertType" clearable placeholder="请选择报警类型">
          <el-option
            v-for="item in dictData.dregAlertTypeDict"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue">
          </el-option>
        </el-select>
      </el-form-item>
	  <el-form-item label="报警时间">
	    <el-date-picker
	      v-model="queryParams.alertTimeStr"
	      value-format="yyyy-MM-dd HH:mm:ss"
	      type="datetimerange"
	      range-separator="至"
	      start-placeholder="开始日期"
	      end-placeholder="结束日期">
	    </el-date-picker>
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
        class="n-table--border"
        use-virtual
        stripe
        border
        @sort-change="sortChange" 
        highlight-current-row
        style="width: 100%">
		<el-table-column label="序号" type="index" align='center' show-overflow-tooltip width="70"></el-table-column>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          :align="item.align"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="detailsClick(scope.row.id)"
            >详情
            </el-button>
            <el-button
			  v-if="scope.row.handleBy == null"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >处理
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
	  :dictData="dictData"
      :action="action"
      @close="createShow = false"
      @save-success="getList"
    />
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页

  import Create from './Create'
	import {sortColumn } from '@/utils'
  import {getList, delTrueByIds} from "../api/dregCarAlert"

  export default {
    /** DEMO列表 */
    name: 'aiDregCarAlert',
    components: {
      Create
    },
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "code",
            label: "编号",
            width: "120",
            align:'center'
            
          },
          {
            prop: "licensePlateNumber",
            label: "车牌号",
            width: "120",
            align:'center'
          },
          {
            prop: "licensePlateColor",
            label: "车牌颜色",
            width: "120",
            align:'center'
          },
          {
            prop: "carType",
            label: "车型",
            width: "120",
            align:'center'
          },
          {
            prop: "carColor",
            label: "车辆颜色",
            width: "120",
            align:'center'
          },
          {
            prop: "alertTime",
            label: "报警时间",
            width: "120",
            align:'center'
            //align:'center'
          },
          {
            prop: "alertType",
            label: "报警类型",
            width: "120",
            align:'center'
            //align:'center'
          },
          {
            prop: "alertContent",
            label: "报警内容",
            width: "120",
            align:'center'
            //align:'center'
          },
          {
            prop: "handleBy",
            label: "处理人",
            width: "100",
              align:'center'
          },
          {
            prop: "handleContent",
            label: "处理内容",
            width: "120",
            align:'center'
            //align:'center'
          },
          {
            prop: "handleTime",
            label: "处置时间",
            width: "180",
              align:'center'
          },
        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams:{},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
		
		// 字典数据或关联下拉框数据
		dictData: {
			// 渣土车报警类型
			dregAlertTypeDict: [],
			// 车辆类型
			carTypeDict: [],
			// 车牌颜色
			licensePlateColorDict: [],
		}
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
		this.getList()
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
		
        if(this.queryParams.alertTimeStr != null && this.queryParams.alertTimeStr!= ''){
          this.queryParams.startTime = this.queryParams.alertTimeStr[0];
          this.queryParams.endTime = this.queryParams.alertTimeStr[1];
        }else{
        	  this.queryParams.startTime = '';
          this.queryParams.endTime = '';
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
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		if (column.property === 'alertType') {
			return this.selectDictLabel(this.dictData.dregAlertTypeDict, row.alertType)
		} else if (column.property === 'carType') {
			return this.selectDictLabel(this.dictData.carTypeDict, row.carType)
		} else if (column.property === 'licensePlateColor') {
			return this.selectDictLabel(this.dictData.licensePlateColorDict, row.licensePlateColor)
		}
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
      //按钮操作权限
      canHandle(type) {
        return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },

      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },
	  
      // 更改每页展示数量
      handleSizeChange(val) {
        Lockr.set('crmPageSizes', val)
        this.pageSize = val
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.getList()
      },
      /**
       * 详情点击
       */
      detailsClick(id) {
        this.action.type = 'detals';
        this.action.id = id;
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
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 260
        this.tableHeight = offsetHei - removeHeight
      },
	  // 初始化字典数据
	  initDicts() {
		  this.getDicts('b_ai_dreg_alert_type').then(res => {
		    this.dictData.dregAlertTypeDict = res.data
		  })
		  // 车辆类型
		  this.getDicts('b_ai_car_type').then(res => {
		  	this.dictData.carTypeDict = res.data
		  })
		  // 车牌颜色
		  this.getDicts('b_ai_license_plate_color').then(res => {
		  	this.dictData.licensePlateColorDict = res.data
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
