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
      <el-form-item label="数据类型" prop="dataType">
        <el-select
          v-model="queryParams.dataType"
          placeholder="请选择"
          size="small"
          style="width: 200px"
          @change="getList"
        >
          <el-option label="实时" value="1" />
          <el-option label="小时" value="2" />
          <el-option label="天" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称" prop="subcontractorType">
        <el-select
          v-model="queryParams.projectId"
          placeholder="请选择"
          clearable
          size="small"
          style="width: 200px"
        >
          <el-option
            v-for="item in projectInfoList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="监测点名称">
        <el-input
          v-model="queryParams.pointName"
          placeholder="请输入监测点名称"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="监测时间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="searchList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-document" size="mini" @click="monitorReport">监测报告</el-button>
      </el-form-item>
      <!-- <el-form-item style="float: right">
        <div class="right-container">
          <el-button type="primary" @click="bulkImportClick">导入</el-button>
        </div>
      </el-form-item> -->
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
          v-for="(item, index) in (queryParams.dataType == '1'?fieldList:fieldList2)"
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
	
	<el-dialog :title="reportTitle" :visible.sync="reportOpen" width="600px"   append-to-body>
		<el-form ref="form" :model="reportForm" label-width="140px" label-position="right" >
			<el-row>
				<el-col :span="24">
				  <el-form-item label="监测时间">
				    <el-date-picker
				      v-model="executeDates2"
					  :clearable = "false"
				      type="daterange"
				      range-separator="至"
				      start-placeholder="开始日期"
				      end-placeholder="结束日期"
				      value-format="yyyy-MM-dd">
				    </el-date-picker>
				  </el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<div slot="footer" class="dialog-footer">
		  <el-button type="primary" @click="submitForm">确 定</el-button>
		  <el-button @click="cancel">取 消</el-button>
		</div>
	</el-dialog>

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

  import {listByProject, downMonitorDataReport} from "../api/monitorHistoryData";
  import {queryProjectList} from '../api/assets'
  import BulkImportUser from '@/components/importModel'
  import {downloadFileWithBuffer,sortColumn} from '@/utils'
  export default {
    /** DEMO列表 */
    name: '',
    components: {
      BulkImportUser,
    },
    data() {
      return {
        conName: '',
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "projectName",
            label: "项目名称",
            //width: "250"
          },
          {
            prop: "assetName",
            label: "设备名称",
            //width: "250"
          },
          {
            prop: "pointCode",
            label: "监测点编号",
            //width: "250"
          },
          {
            prop: "pointName",
            label: "监测项名称",
            //width: "150"
          },
          {
            prop: "value",
            label: "监测值",
            //width: "180"
          },
          {
            prop: "unit",
            label: "单位",
            //width: "250"
          },
          {
            prop: "alarmUpper",
            label: "报警上限",
            //width: "180"
          },
          {
            prop: "alarmLower",
            label: "报警下限",
            //width: "180"
          },
          {
            prop: "monitorTime",
            label: "监测时间",
            //width: "180"
          },
        ],
        fieldList2: [//表头
          {
            prop: "projectName",
            label: "项目名称",
            //width: "250"
          },
          {
            prop: "assetName",
            label: "设备名称",
            //width: "250"
          },
          {
            prop: "pointCode",
            label: "监测点编号",
            //width: "250"
          },
          {
            prop: "pointName",
            label: "监测项名称",
            //width: "150"
          },
          {
            prop: "value",
            label: "监测值",
            //width: "180"
          },
          {
            prop: "maxValue",
            label: "最大值",
            //width: "180"
          },
          {
            prop: "minValue",
            label: "最小值",
            //width: "180"
          },
          {
            prop: "unit",
            label: "单位",
            //width: "250"
          },
          {
            prop: "alarmUpper",
            label: "报警上限",
            //width: "180"
          },
          {
            prop: "alarmLower",
            label: "报警下限",
            //width: "180"
          },
          {
            prop: "startTime",
            label: "开始时间",
            //width: "180"
          },
          {
            prop: "endTime",
            label: "结束时间",
            //width: "180"
          },
        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        // 执行时间范围
		executeDates:[this.formatterDate(new Date(new Date().getTime() - 2 * 60 * 60 * 1000), '2'),this.formatterDate(new Date(), '2')],
        queryParams: {},
        // 字典数据或关联下拉框数据
        dictData: {
          // 监测项类型字典项
          monitorTypeDict: [],
        },
        // 项目信息列表
        projectInfoList: [],
        // 菜单类型
        monitorType: '',
        // 监测项子类型
        envMonitorSonType: '',
        // 批量导入
        bulkImportShow: false,
        param: {},
		
		// 监测报告
		reportTitle: '监测报告',
		reportOpen: false,
		reportForm: {},
        //executeDates2: ['2021-11-11', '2022-11-11'],
		executeDates2:[this.formatterDate(new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * 6), '1'),this.formatterDate(new Date(), '1')],
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
    created() {
    },
    mounted() {
      this.getProjectInfoList()
      //获取列表数据
      this.initDicts()
      this.initData(this.$route)
      // 初始化时间，默认为今天
    },
    watch: {
      //监听页面切换
      '$route'(to, from) {
        //TODO 重新加载数据
        this.initData(to)
        console.log(this.$route)
      }
    },
    methods: {
    	sortChange(column) {
      sortColumn(this,column);
    },
      // 初始化数据
      initData(to) {
        this.queryParams = {dataType:'1'}
        this.executeDates = [this.formatterDate(new Date(new Date().getTime() - 2 * 60 * 60 * 1000), '2'),this.formatterDate(new Date(), '2')]
        // 根据路由确定监测项类型
        // 监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
        if (to.path.indexOf('/raiseDustHistory') > 0) {
          this.monitorType = '9'
          this.envMonitorSonType = '1'
          this.$options.name = 'raiseDustHistory'
        } else if (to.path.indexOf('/noiseHistory') > 0) {
          this.monitorType = '9'
          this.envMonitorSonType = '2'
          this.$options.name = 'noiseHistory'
        } else if (to.path.indexOf('/weatherHistory') > 0) {
          this.monitorType = '9'
          this.envMonitorSonType = '3'
          this.$options.name = 'weatherHistory'
        }
        this.getList()
      },
      searchList(){
        this.currentPage = 1;
        this.total = 0;
        this.getList()
      },
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;

        // 监测项类型
        this.queryParams.monitorType = this.monitorType
        this.queryParams.envMonitorSonType = this.envMonitorSonType

        // 执行时间范围
        if (this.executeDates != null && this.executeDates != '') {
          this.queryParams.startTime = this.executeDates[0]
          this.queryParams.endTime = this.executeDates[1]
        } else {
          this.queryParams.startTime = ''
          this.queryParams.endTime = ''
        }

        listByProject(this.queryParams)
          .then(res => {
            console.log(res)
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
       * 批量导入
       */
      bulkImportClick() {
        let lists = [
          "监测点编号(*)",
          "监测项名称(*)",
          "监测值(*)",
          "监测时间(*) 时间格式如下yyyy-MM-dd HH:mm:ss"
        ]
        let colNameCodes = [
          "pointCode",
          "pointName",
          "value",
          "monitorTime"
        ]

        //示例数据
        let dataLists = [
          "XLPT-001",
          "混凝土支撑轴力",
          "33",
          "2020-05-11 10:22:22"
        ]


        this.param = {
          title: "监测项历史数据导入模板(*)为必填项 监测项名称需与对应监测点相同",
          excelTitle: "监测项历史数据导入模板",
          colNameCodes: colNameCodes,
          colNames: lists,
          sampleData: dataLists,
          url: 'build/equipmentMonitorData/excelImport'
        }

        this.bulkImportShow = true
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        if (column.property === 'monitorType') {
          return this.selectDictLabel(this.dictData.monitorTypeDict, row.monitorType)
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
          if ((row.alarmUpper != null &&parseInt(row.value)  >= row.alarmUpper )
            || (row.alarmLower != null && parseInt(row.value)<= row.alarmLower  )) {
            return 'column-font-color-red'
          }
        }
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.currentPage = 1;
        this.queryParams = {dataType:'1'}
        this.executeDates = [this.formatterDate(new Date(new Date().getTime() - 2 * 60 * 60 * 1000), '2'),this.formatterDate(new Date(), '2')]
        this.getList()
      },
	  monitorReport() {
		  this.reset()
		  this.reportOpen = true
	  },
	  submitForm() {
		  if (this.executeDates2 != null && this.executeDates2 != '') {
		    this.reportForm.startDate = this.executeDates2[0]
		    this.reportForm.endDate = this.executeDates2[1]
		  } else {
		    this.reportForm.startDate = ''
		    this.reportForm.endDate = ''
		  }
		  downMonitorDataReport(this.reportForm).then(res => {
			this.reportOpen = false
			const blob = new Blob([res.data], {
			  type: ''
			})
			downloadFileWithBuffer(blob, '智能工地监测数据报告.doc')
		    
		  }).catch(() => {
		  })
	  },
      // 取消按钮
      cancel() {
		  this.reportOpen = false
		  this.reset()
      },
      // 表单重置
      reset() {
		  this.reportform = {}
		  this.executeDates2 = [this.formatterDate(new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * 6), '1'),this.formatterDate(new Date(), '1')]
      },
      // 初始化字典数据
      initDicts() {
        this.getDicts('b_equipment_monitor_type').then(res => {
          this.dictData.monitorTypeDict = res.data
        })
      },
      // 关闭导入组件
      bulkImportClose() {
        this.bulkImportShow = false
        this.getList()
      },
      // 获取项目信息列表
      getProjectInfoList() {
        queryProjectList()
          .then(response => {
            this.projectInfoList = response.data || []
          })
          .catch(() => {
          })
      },
	  
	  // 格式化时间字符串
	  formatterDate(date, type) {
		  console.log(date)
	    if(date==null || date=="") {
			return "";
		} 
	    let Y = date.getFullYear() + "-";
	    let M = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) + "-" : date.getMonth() + 1 + "-";
	    let D = date.getDate() < 10 ? "0" + date.getDate() + " " : date.getDate() + " ";
	    let h = date.getHours() < 10 ? "0" + date.getHours() + ":" : date.getHours() + ":";
	    let m = date.getMinutes()  < 10 ? "0" + date.getMinutes() + ":" : date.getMinutes() + ":";
	    let s = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
		let all;
		if (type === '1') {
			all = (Y + M + D)
		} else if (type === '2') {
			all = (Y + M + D+h+m+s)
		}
	    return all;
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

  /deep/ .column-font-color-red .el-tooltip {
    color: red !important;
  }

  @import '@/views/styles/table.scss';
</style>
