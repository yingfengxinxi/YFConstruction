<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
	  <el-form-item label="设备名称">
	    <el-input
	      v-model="queryParams.assetName"
	      placeholder="请输入设备名称"
	      clearable
	      size="small"
	      @keyup.enter.native="getList"
	    />
	  </el-form-item>
      <el-form-item label="报警类型">
        <el-select v-model="queryParams.warnType" clearable placeholder="请选择">
          <el-option
        	v-for="item in dictData.warnTypeDict"
        	:key="item.dictValue"
        	:label="item.dictLabel"
        	:value="item.dictValue">
          </el-option>
        </el-select>
      </el-form-item>
		<el-form-item label="时间范围">
		  <el-date-picker
		  v-model="executeDates"
		  type="datetimerange"
		  range-separator="至"
		  start-placeholder="开始日期"
		  end-placeholder="结束日期"
		  value-format="yyyy-MM-dd HH:mm:ss"
		  :default-time="['00:00:00', '23:59:59']">
		  </el-date-picker>
		</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">

      <el-table
        v-loading="loading"
        id="crm-table"
        @sort-change="sortChange"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%">

		<el-table-column label="序号" type="index" align="center" show-overflow-tooltip width="70">
		</el-table-column>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          align="center"
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
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import {sortColumn } from '@/utils'
  import {getList} from "../api/standrdRearingRoom";

  export default {
    /** DEMO列表 */
    name: 'standrdRearingRoom',
    components: {
    },
    data() {
      return {
      	conName:'',
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "assetCode",
            label: "设备编号",
            //width: "150"
          },
          {
            prop: "assetName",
            label: "设备名称",
            //width: "150"
          },
          {
            prop: "temperature",
            label: "温度(℃)",
            //width: "180"
          },
          {
            prop: "humidity",
            label: "湿度(%RH)",
            //width: "180"
          },
          {
            prop: "warnType",
            label: "报警类型",
            //width: "180"
          },
          {
            prop: "monitorDataTime",
            label: "数据发送时间",
            //width: "250"
          },
        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams: {},
		// 查询时间
		executeDates: '',
		// 设备下拉选择
		equipmentOptions: [],
		dictData:{
			// 报警类型
			warnTypeDict: []
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
		created(){

		},
		mounted() {
			this.initDicts()
			//获取列表数据
			this.getList()
		},
		watch:{

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
		if (this.executeDates != '' && this.executeDates != null) {
			this.queryParams.startTime = this.executeDates[0]
			this.queryParams.endTime = this.executeDates[1]
		}else{
			this.queryParams.startTime = ''
			this.queryParams.endTime = ''
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
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        if (column.property == "warnType") {
			return this.selectDictLabel(this.dictData.warnTypeDict, row.warnType);
        }
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
			this.getDicts('b_qual_standard_rearing_room').then(res => {
				this.dictData.warnTypeDict = res.data
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
  /deep/.column-font-color-red .el-tooltip{
  	color: red !important;
  }

  @import '@/views/styles/table.scss';
</style>
