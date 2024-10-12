<template>
  <div>
<!--    <div class="c-container">-->
<!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
<!--        :src="titleIcon"-->
<!--        class="title-icon">质量方案管理-->
<!--      </flexbox>-->
<!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;"> 
      <el-form-item label="能源名称" prop="energyName">
        <el-select v-model="queryParams.energyName" clearable placeholder="请选择检查人">
          <el-option
        	v-for="item in energyOptions"
        	:key="item.value"
        	:label="item.label"
        	:value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
	  <el-form-item label="超限类型" prop="transfiniteType">
	    <el-select v-model="queryParams.transfiniteType" clearable placeholder="请选择检查人">
	      <el-option
	    	v-for="item in transfiniteTypeOptions"
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
          <el-button type="primary" @click="bulkImportClick">导入</el-button>
        </div>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">
     
      <el-table
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        class="n-table--border"
        use-virtual
        stripe
        border
        @sort-change="sortChange"
        highlight-current-row
        style="width: 100%">
		<el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
           sortable="custom"
          show-overflow-tooltip>

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
	
	<!-- 批量导入 -->
	<bulk-import-user
	  :show="bulkImportShow"
    :param="param"
	  @close="bulkImportClose"
	   />
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
	import {sortColumn } from '@/utils'
  import {getList} from "../api/hydropowerAlarm";
  import BulkImportUser from '@/components/importModel'

  export default {
    /** DEMO列表 */
    name: 'hydropowerAlarm',
    components: {
		BulkImportUser,
    },
    data() {
      return {
      	conName:'hydropowerAlarm',
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "locationName",
            label: "监测区域",
            //width: "250"
          },
          {
            prop: "energyName",
            label: "能源名称",
            //width: "250"
          },
          {
            prop: "alarmTime",
            label: "报警时间",
            width: "180"
          },
          {
            prop: "startTime",
            label: "监测开始时间",
            width: "180"
          },
          {
            prop: "endTime",
            label: "监测结束时间",
            width: "180"
          },
          {
            prop: "accumulativePrevious",
            label: "累计上期使用量",
            //width: "180"
          },
          {
            prop: "currentUsage",
            label: "本期使用量",
            //width: "180"
          },
          {
            prop: "cumulativeUsage",
            label: "累计使用量",
            //width: "180"
          },
          {
            prop: "currentIndex",
            label: "本期使用指标",
            //width: "180"
          },
          {
            prop: "cumulativeIndex",
            label: "累计使用指标",
            //width: "180"
          },
          {
            prop: "transfiniteType",
            label: "超限类型",
            //width: "180"
          },
          {
            prop: "transfiniteNumber",
            label: "超限数量",
            //width: "180"
          },
        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
		// 执行时间范围
		executeDates: '',
        queryParams: {},
		// 字典数据或关联下拉框数据
		dictData: {
			// 监测项类型字典项
			monitorTypeDict: [],
		},
		// 批量导入
		bulkImportShow: false,
		// 能源名称下拉数据
		energyOptions:[
			{
				value: '1',
				label: '电量',
			},
			{
				value: '2',
				label: '水量',
			},
		],
		// 超限类型下拉框数据
		transfiniteTypeOptions:[
			{
				value: '1',
				label: '当期',
			},
			{
				value: '2',
				label: '累计',
			},
    ],
    param:{}
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
    created(){
    },
    mounted() {
      //获取列表数据
	  //this.initDicts()
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
		
		
		// 执行时间范围
		if (this.executeDates != '') {
			this.queryParams.startTime = this.executeDates[0]
			this.queryParams.endTime = this.executeDates[1]
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
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
	  /**
	   * 批量导入
	   */
	  bulkImportClick() {
      let lists = [
	    "监测区域(*)",
	    "能源名称(*)电量/水量",
	    "报警时间(*)yyyy-MM-dd HH:mm:ss",
	    "监测开始时间(*)yyyy-MM-dd HH:mm:ss",
	    "监测结束时间(*)yyyy-MM-dd HH:mm:ss",
	    "累计上期使用量(*)",
	    "本期使用量(*)",
	    "累计使用量(*)",
	    "本期使用指标(*)",
	    "累计使用指标(*)",
	    "超限类型(*)当期/累计(*)",
	    "超限数量(*)(*)",
	  ]
	  let colNameCodes = [
	    "locationName",
	    "energyName",
	    "alarmTime",
	    "startTime",
	    "endTime",
	    "accumulativePrevious",
	    "currentUsage",
	    "cumulativeUsage",
	    "currentIndex",
	    "cumulativeIndex",
	    "transfiniteType",
	    "transfiniteNumber",
	  ]
	  
	  //示例数据
	  let dataLists = [
	    "绿化中心",
	    "电量",
	    "2021-06-15 10:22:22",
	    "2021-06-14 10:22:22",
	    "2021-06-15 10:22:22",
	    "33",
	    "33",
	    "77",
	    "66",
	    "44",
	    "当期",
	    "22",
	  ]
	  
	  
	  
	  this.param = {
	    title:"设备水电报警导入模板(*)为必填项",
	    excelTitle:"设备水电报警导入模板",
	    colNameCodes:colNameCodes,
	    colNames:lists,
      sampleData:dataLists,
      url:'build/equipmentHydropowerAlarm/excelImport'
    }
    
		this.bulkImportShow = true
	  },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		if (column.property === 'energyName') {
			return row.energyName === '1' ? '电量' : '水量'
		} else if (column.property === 'transfiniteType') {
			return row.transfiniteType === '1' ? '当期' : '累计'
		}
        //console.log(row, column)
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
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
	  /**
	   * 通过回调控制class
	   */
	  cellClassName({row, column, rowIndex, columnIndex}) {
	    if (column.property === 'value') {
			if ((row.alarmUpper != null && row.value >= row.alarmUpper) 
			|| (row.alarmLower != null && row.value >= row.alarmLower)) {
				return 'column-font-color-red'
			}
	    }
	  },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {}
		this.executeDates = ''
        this.getList()
      },
	  // 初始化字典数据
	  initDicts() {
		  this.getDicts('b_equipment_monitor_type').then(res => {
			this.dictData.monitorTypeDict = res.data
		  })
	  },
	  // 关闭导入组件
	  bulkImportClose() {
		  this.bulkImportShow=false
		  this.getList()
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
  /deep/.column-font-color-red .el-tooltip{
  	color: red !important;
  }

  @import '@/views/styles/table.scss';
</style>
