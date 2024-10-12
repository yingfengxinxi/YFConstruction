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
      <el-form-item label="项目名称" prop="projectId">
        <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
          <el-option
            v-for="item in projectOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
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
      <el-form-item style="float: right" v-if="showDownload">
        <div class="right-container">
          <el-button type="warning" plain @click="downBG" icon="el-icon-download">环境监测报告</el-button>
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
        highlight-current-row
        style="width: 100%"
        @sort-change="sortChange">
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column
          v-for="(item, index) in (queryParams.dataType == '1'?fieldList:fieldList2)"
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

  import {getList, getProjectsList,listByProject} from "../api/monitorHistoryData";
  import BulkImportUser from '@/components/importModel'

  import {downloadExcelWithResData} from '@/utils'
  import {excelImpYS} from '@/api/common'

  import { sortColumn } from '@/utils/index';

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
            label: "监测点名称",
            //width: "150"
          },
          {
            prop: "monitorSonType",
            label: "监测项",
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
            label: "时间",
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
        executeDates: '',
        queryParams: {},
        // 字典数据或关联下拉框数据
        dictData: {
          // 监测项类型字典项
          monitorTypeDict: [],
          // 监测项子类型字典项
          monitorSonTypeDict: [],
        },
        // 项目下拉框列表
        projectOptions: [],
        // 菜单类型
        monitorType: '',
        // 批量导入
        bulkImportShow: false,
        param: {},
        showDownload: false,
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
      /* if(this.$route.path.indexOf('/foundationHistory')>0){
           this.$options.name='foundationHistory'
         }else if(this.$route.path.indexOf('/towerCraneHistory')>0){
           this.$options.name='towerCraneHistory'
         }else if(this.$route.path.indexOf('/lifterHistory')>0){
           this.$options.name='lifterHistory'
         }else if(this.$route.path.indexOf('/environHistory')>0){
           this.$options.name='environHistory'
         }else if(this.$route.path.indexOf('/boltLooseness')>0){
           this.$options.name='boltLooseness'
         } */
    },
    mounted() {
      //获取列表数据
      this.initDicts()
      this.initData(this.$route)
      console.log(this.fieldList)
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
      // 2021-11-28 模拟监测报告
      downBG() {
        excelImpYS(this.param).then(res => {

          downloadExcelWithResData(res)
          this.loading = false

        }).catch(() => {
        })
      },
      // 初始化数据
      initData(to) {
        this.queryParams = {dataType:'1'}
        this.currentPage = 1;
        this.pageSize = 10;
        this.executeDates = ''
        this.showDownload = false;
        // 根据路由确定监测项类型
        // 监测项类型 字典项 1：基坑监测、2：塔机监测、3；升降机监测、9：环境监测
        if (to.path.indexOf('/foundationHistory') > 0) {
          this.monitorType = '1'
          this.$options.name = 'foundationHistory'
        } else if (to.path.indexOf('/towerCraneHistory') > 0) {
          this.monitorType = '2'
          this.$options.name = 'towerCraneHistory'
        } else if (to.path.indexOf('/lifterHistory') > 0) {
          this.monitorType = '3'
          this.$options.name = 'lifterHistory'
        } else if (to.path.indexOf('/environHistory') > 0) {
          this.monitorType = '9'
          this.$options.name = 'environHistory'
          this.showDownload = true;
        } else if (to.path.indexOf('/boltLooseness') > 0) {
          this.monitorType = '10'
          this.$options.name = 'boltLooseness'
        } else if (to.path.indexOf('/highFormwork') > 0) {
          this.monitorType = '11'
          this.$options.name = 'highFormwork'
        } else if (to.path.indexOf('/dischargingPlatform') > 0) {
          this.monitorType = '12'
          this.$options.name = 'dischargingPlatform'
        } else if (to.path.indexOf('/smartSmoke') > 0) {
          this.monitorType = '13'
          this.$options.name = 'smartSmoke'
        } else if (to.path.indexOf('/nacelle') > 0) {
          this.monitorType = '14'
          this.$options.name = 'nacelle'
        } else if (to.path.indexOf('/exteriorScaffolding') > 0) {
          this.monitorType = '15'
          this.$options.name = 'exteriorScaffolding'
        } else if (to.path.indexOf('/safetySteelStructure') > 0) {
          this.monitorType = '16'
          this.$options.name = 'safetySteelStructure'
        } else if (to.path.indexOf('/electricityBox') > 0) {
          this.monitorType = '17'
          this.$options.name = 'electricityBox'
        } else if (to.path.indexOf('/massConcrete') > 0) {
          this.monitorType = '18'
          this.$options.name = 'massConcrete'
        } else if (to.path.indexOf('/pileFoundation') > 0) {
          this.monitorType = '19'
          this.$options.name = 'pileFoundation'
        } else if (to.path.indexOf('/dynamicCompaction') > 0) {
          this.monitorType = '20'
          this.$options.name = 'dynamicCompaction'
        } else if (to.path.indexOf('/smartGrouting') > 0) {
          this.monitorType = '21'
          this.$options.name = 'smartGrouting'
        } else if (to.path.indexOf('/smartTension') > 0) {
          this.monitorType = '22'
          this.$options.name = 'smartTension'
        } else if (to.path.indexOf('/testRoom') > 0) {
          this.monitorType = '23'
          this.$options.name = 'testRoom'
        } else if (to.path.indexOf('/sewage') > 0) {
          this.monitorType = '24'
          this.$options.name = 'sewage'
        } else if (to.path.indexOf('/concreteQuality') > 0) {
          this.monitorType = '25'
          this.$options.name = 'concreteQuality'
        } else if (to.path.indexOf('/steelWire') > 0) {
          this.monitorType = '26'
          this.$options.name = 'steelWire'
        }

        this.getDicts('b_equipment_monitor_type_' + this.monitorType).then(res => {
          this.dictData.monitorSonTypeDict = res.data
        })
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
        } else if (column.property === 'monitorSonType') {
          return this.selectDictLabel(this.dictData.monitorSonTypeDict, row.monitorSonType)
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
        this.queryParams = {dataType:'1'}
        this.executeDates = ''
        this.getList()
      },
      // 初始化字典数据
      initDicts() {
        this.getDicts('b_equipment_monitor_type').then(res => {
          this.dictData.monitorTypeDict = res.data
        })
        // 查询项目数据
        getProjectsList().then(res => {
          this.projectOptions = res.data;
        })
      },
      // 关闭导入组件
      bulkImportClose() {
        this.bulkImportShow = false
        this.getList()
      },
      sortChange(column) {
        if(column.prop == 'value'){
          column.prop = 'value+0'
        }
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

  /deep/ .column-font-color-red .el-tooltip {
    color: red !important;
  }

  @import '@/views/styles/table.scss';
</style>
