<template>
  <div>
    <!--    <div class="c-container">-->
    <!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
    <!--        :src="titleIcon"-->
    <!--        class="title-icon">质量方案管理-->
    <!--      </flexbox>-->
    <!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="人员姓名" prop="infocollectionName">
        <el-input v-model="queryParams.infocollectionName" placeholder="请输入人员姓名" clearable size="small" style="width: 160px" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item label="班组长名" prop="teamLeaderName">
        <el-input v-model="queryParams.teamLeaderName" placeholder="请输入班组长名" clearable size="small" style="width: 160px" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item label="班组名称" prop="attendanceRecordTeamName">
        <el-select v-model="queryParams.attendanceRecordTeamName" clearable placeholder="请选择" style="width: 160px">
          <el-option v-for="item in teamOptions" :key="item.id" :label="item.teamName" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <!--<el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="queryParams.startTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="queryParams.endTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item label="时间范围" prop="attendanceRecordTeamName">
        <el-date-picker
      v-model="timeFrame"
      type="daterange"
      range-separator="至"
      value-format="yyyy-MM-dd HH:mm:ss"
      start-placeholder="开始时间"
      end-placeholder="结束时间">
    </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <el-button type="primary" @click="bulkImportClick">考勤导入</el-button>
          <el-button type="primary" @click="exportFile(1)">考勤导出</el-button>
        </div>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">

      <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @sort-change="sortChange">
        <el-table-column label="序号" type="index" show-overflow-tooltip width="70"></el-table-column>
        <el-table-column v-for="(item, index) in fieldList" :key="index" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" show-overflow-tooltip>

        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
    </div>

    <!-- 批量导入 -->
    <bulk-import-user :show="bulkImportShow" :param="param" @close="importClose" />
  </div>
</template>

<script>

import { mapGetters } from 'vuex'//权限
import Lockr from 'lockr'//分页

import { getList, selectTeamOptions } from "../api/attendanceDetail";
// import BulkImportUser from '../attendance/components/BulkImportUser.vue'
import BulkImportUser from '@/components/importModel'
import { exportFile } from "@/api/build/exportFile";
import { downloadExcelWithResData,sortColumn } from '@/utils/index';
export default {
  /** DEMO列表 */
  name: 'attendanceDetail',
  components: {
    BulkImportUser,
  },
  data () {
    return {
      loading: false, // 加载动画
      /** 列表 */
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      list: [],
      fieldList: [//表头
        {
          prop: "infocollectionName",
          label: "姓名",
          //width: "150"
        },
        {
          prop: "infocollectionCard",
          label: "证件号码",
          //width: "250"
        },
        {
          prop: "attendanceNumber",
          label: "考勤卡号",
          //width: "250"
        },
        {
          prop: "clockTime",
          label: "打卡时间",
          //width: "180"
        },
        {
          prop: "clockDirection",
          label: "打卡方向",
          //width: "180"
        },
        {
          prop: "bodyTemperature",
          label: "体温",
          //width: "180"
        },
        {
          prop: "alcoholicity",
          label: "酒精含量",
          //width: "180"
        },
        {
          prop: "bloodPressure",
          label: "血压",
          //width: "180"
        },
        {
          prop: "bloodOxygen",
          label: "血氧",
          //width: "180"
        },
      ],
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 40, 80, 100],
      total: 0,
      queryParams: {
        startTime: "2021-05-17 00:00:00"
      },
      teamOptions: [], // 班组名称
      // 批量导入
      bulkImportShow: false,
      param: {},
      timeFrame:[this.formatterDate(new Date().getTime()),this.formatterDate(this.formatterDate(new Date().getTime()+24*60*60*1000))]
    }
  },
  computed: {
    //权限 start
    ...mapGetters(['allAuth']),
    //权限 end
    titleIcon () {
      return require(`@/assets/img/crm/customer.png`)
    },
  },
  mounted () {
    //获取列表数据
    this.getTeamOptions()
  
    this.getList()
    // 初始化时间，默认为今天
  },
  methods: {
  	sortChange(column) {
      sortColumn(this,column);
    },
    /** 获取列表数据 */
    getList () {
      this.loading = true
      //分页
      this.queryParams.page = this.currentPage;
      this.queryParams.limit = this.pageSize;
			if (this.timeFrame != null && this.timeFrame != '') {
	      this.queryParams.startTime = this.timeFrame[0]
	      this.queryParams.endTime = this.timeFrame[1]
	    } else {
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
    // 查询班组数据
    getTeamOptions () {
      selectTeamOptions().then(res => {
        this.teamOptions = res.data
      }).catch(() => {
      })
    },
	  /**
	   * 批量导入
	   */
    bulkImportClick () {
      let lists = [
        "姓名(*)",
        "身份证号(*)",
        "考勤卡号",
        "打卡方向(*)",
        "打卡时间(*)",
        "体温",
        "酒精含量",
        "血压",
        "血氧"
      ]
      //示例数据
      let dataLists = [
        "王积国",
        "372926197906070516",
        "",
        "出场",
        "2021-01-10 15:47:14",
        "37°",
        "133mg/100mL",
        "1mmHg",
        "97%"
      ]

      this.param = {
        title: "考勤导入模板(*)为必填项 身份证号或考勤号必填一个",
        excelTitle: "考勤导入模板",
        colNames: lists,
        sampleData: dataLists,
        url: 'build/bLabourAttendanceRecord/excelImport'
      }
      this.bulkImportShow = true
    },
    createClick () {
    },
    //列格式化  如一些字典等
    fieldFormatter (row, column, cellValue) {
      if (column.property === 'clockDirection') {
        return row.clockDirection === '0' ? '进场' : '出场'
      }
      //console.log(row, column)
      return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
    },

    // 更改当前页数
    handleCurrentChange (val) {
      this.currentPage = val
      this.getList()
    },
    // 更改每页展示数量
    handleSizeChange (val) {
      Lockr.set('crmPageSizes', val)
      this.pageSize = val
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams.teamLeaderName =''
      this.queryParams.infocollectionName =''
      this.queryParams.attendanceRecordTeamName =''
       this.queryParams.startTime = ''
	      this.queryParams.endTime = ''

      // this.initStartTime()
      this.getList()
    },
    // 默认查询时间为今天
    initStartTime () {
      var date = this.formatterDate(new Date())
      this.queryParams.startTime = date
    },
    // 格式化时间字符串
    formatterDate (date) {
    	let time=new Date(date)
      if (date == null || date == "") return "";
      let Y = time.getFullYear() + "-";
      let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
      let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
      let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
      let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
      let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
      let all = (Y + M + D + "00:00:00")
      return all;
    },
    // 导入成功后
    importClose () {
      this.bulkImportShow = false
      this.getList()
    },
    /**
		 *
		 * 使用导出需引入js文件如下：
		 * import { exportFile } from "@/api/build/exportFile";
		 * import { downloadExcelWithResData } from '@/utils/index';
		 * 方法入参说明： type=1 全部导出  type=2 部分导出
     * 请求列表数据说明：需替换为每页列表的数据请求url
		 * 
		 */
    async  exportFile (type) {
      this.loading = true;
      let data = '';
      if (type == 1) {
        //请求不分页的列表数据
		var objString = JSON.stringify(this.queryParams);
		var obj2 = JSON.parse(objString);
		let reqParam = obj2;
        reqParam.pageType = 0
        await getList(reqParam)
          .then(res => {
            data = res.data.list;
          })
          .catch(err => {
            // console.log(err);
            this.$message.error("导出数据加载失败");
            this.loading = false;
          })
        // console.log(data);
      } else if (type == 2) {
        //获取当前选中行的数据
        data = this.selectionList;
      }
			/**
			 * 整理数据
			 * param中result是需要导出的数据(必填)
			 * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)，
			 * 如果有字典需转换请添加dictType类型，如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)
			 * param中title是导出Excel的一级表头(必填)
			 * 注意：fieldList中字段值必须和列表中字段值相对应！！！
			 */
      let fieldList = [//表头
        { prop: "infocollectionName", label: "姓名", },
        { prop: "infocollectionCard", label: "证件号码", },
        { prop: "attendanceNumber", label: "考勤卡号", },
        {  prop: "clockTime", label: "打卡时间", },
        {  prop: "clockDirection", label: "打卡方向",dictList:[{"dictLabel":"出场","dictValue":"1"},{"dictLabel":"进场","dictValue":"0"}]  },
        { prop: "bodyTemperature", label: "体温", },
        { prop: "alcoholicity", label: "酒精含量", },
        { prop: "bloodPressure", label: "血压",  },
        { prop: "bloodOxygen", label: "血氧", },
      ]
      let param = {};
      param.result = data;
      param.fieldList = fieldList;
      param.title = "考勤明细";
      await exportFile(param)
        .then(res => {
          // console.log(res);
          downloadExcelWithResData(res);
          this.loading = false;
        })
        .catch(err => {
          // console.log(err)
          this.$message.error("导出文件生成异常");
          this.loading = false;
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

@import "@/views/styles/table.scss";
</style>
