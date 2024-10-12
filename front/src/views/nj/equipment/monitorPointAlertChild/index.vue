<template>
  <div>
    <el-form
      v-show="true"
      ref="queryForm"
      :model="queryParams"
      :inline="true"
      label-width="80px"
      style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;"
    >
      <el-form-item label="所属项目">
        <el-select
          v-model="queryParams.projectId"
          clearable
          placeholder="请选择所属项目"
          style="width:160px"
        >
          <el-option
            v-for="item in projectInfoList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
	  <el-form-item label="报警级别">
	    <el-select
	      v-model="queryParams.alertType"
	      clearable
	      placeholder="请选择报警级别"
	      style="width:140px"
	    >
	      <el-option
	        v-for="item in alertTypeList"
	        :key="item.value"
	        :label="item.label"
	        :value="item.value"
	      >
	      </el-option>
	    </el-select>
	  </el-form-item>
	  <el-form-item label="报警类型">
	    <el-select
	      v-model="queryParams.monitorSonType"
	      clearable
	      placeholder="请选择报警类型"
	      style="width:140px"
	    >
	      <el-option
	        v-for="item in dictData.monitorTypeDict_9"
	        :key="item.dictValue"
	        :label="item.dictLabel"
	        :value="item.dictValue"
	      >
	      </el-option>
	    </el-select>
	  </el-form-item>
      <el-form-item label="报警时间">
        <el-date-picker
          v-model="queryParams.startDate"
          type="datetime"
          style="width: 200px"
          size="small"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="开始时间"
        >
        </el-date-picker>&nbsp;~
        <el-date-picker
          v-model="queryParams.endDate"
          type="datetime"
          style="width: 200px"
          size="small"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="结束时间"
        >
        </el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="监测点名称">
        <el-input
          v-model="queryParams.pointName"
          placeholder="请输入监测点名称"
          clearable
          size="small"
          style="width:150px"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="设备名称">
        <el-input
          v-model="queryParams.assetName"
          placeholder="请输入设备名称"
          clearable
          size="small"
          style="width:140px"
          @keyup.enter.native="getList"
        />
      </el-form-item> -->
 <el-form-item label="处理状态">
        <el-select
          v-model="queryParams.handleState"
          clearable
          placeholder="请选择处理状态"
          style="width:140px"
        >
          <el-option
            v-for="item in dictData.handleStateList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="getList"
        >搜索</el-button>
        <el-button
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
        >重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
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
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
		  fixed
          label="序号"
          type="index"
          width="70"
        ></el-table-column>
        <el-table-column
          v-for="(item, index) in fieldList"
		  :fixed="index==0"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          :align="item.align"
          show-overflow-tooltip
        >

        </el-table-column>
        <el-table-column
          label="操作"
		  fixed="right"
          align="center"
          class-name="small-padding fixed-width"
          width="140px"
        >
          <template slot-scope="scope">
           
            <el-button
            v-if="scope.row.handleState == '0'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handle(scope.row.id)"
            >处理
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-document"
              @click="showDetails(scope.row.id)"
            >查看
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
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <!-- 新建 -->
    <handle
      v-if="createShow"
      :businessType="businessType"
      :action="action"
      @close="createShow = false"
      @save-success="getList"
    />
    <!-- 详情查看 -->
    <detail
      v-if="showDview"
      :businessType="businessType"
      :action="action"
      @close="showDview = false"
      @save-success="getList"
    />
  </div>
</template>
<script>
import { mapGetters } from 'vuex'//权限
import Lockr from 'lockr'//分页
import { selectPageByEnv } from "../api/monitorPointAlert";
import {queryProjectList} from '../api/assets'
import Handle from './Handle'
import Detail from './Detail'
import {sortColumn } from '@/utils'
export default {
  /** DEMO列表 */
  name: 'envAlert',
  components: {
    Handle,
    Detail
  },
  data() {
    return {
      businessType: 10000,
      loading: false, // 加载动画
      /** 选中时操作 */
      selectionHandleItems: [
        {
          name: '删除',
          type: 'delete',
          icon: 'delete'
        },
      ],
      /** 列表 */
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      list: [],
      fieldList: [//表头
        {
          prop: "projectName",
          label: "项目名称",
          width: "200"
        },
		{
		  prop: "alertType",
		  label: "报警级别",
		  width: "120"
		},
		{
		  prop: "monitorSonType",
		  label: "报警类型",
		  width: "140"
		},
        {
          prop: "pointName",
          label: "监测点名称",
          width: "160"
        },
        // {
        //   prop: "assetName",
        //   label: "设备名称",
        //   width: "200"
        //   //align:'center'
        // },
        // {
        //   prop: "locationName",
        //   label: "报警位置",
        //   width: "160",
        //   //align:'center'
        // },
        {
          prop: "content",
          label: "报警内容",
          //align:'center'
        },
        {
          prop: "valueUnit",
          label: "报警值",
          width: "160",
          //align:'center'
        }, 
		// {
        //   prop: "upperValue",
        //   label: "上限值",
        //   width: "100",
        //   //align:'center'
        // }, {
        //   prop: "lowerValue",
        //   label: "下限值",
        //   width: "100",
        //   //align:'center'
        // },
        {
          prop: "createTime",
          label: "报警时间",
          width: "160",
          //align:'center'
        },
        {
          prop: "handleState",
          label: "处理状态",
          width: "120",
          //align:'center'
        }
      ],
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 40, 80, 100],
      total: 0,
      queryParams: {},
      /** 新增*/
      createShow: false,
      /**详情 */
       showDview: false,
      /***/
      action: { type: 'save', data: null },
      /** 勾选行 */
      selectionList: [], // 勾选数据 用于全局导出
      /** 控制详情展示 */
      rowID: '', // 行信息,

      // 字典数据或关联下拉框数据
      dictData: {
        // 统计类型字典项
        statisticalTypeDict: [],
        // 监测项类型字典项
        monitorTypeDict: [],
		// 监测项类型子项-环境监测
		monitorTypeDict_9: [],
        //处理状态
        handleStateList:[
          {'dictLabel':'已处理','dictValue':'1'}, {'dictLabel':'未处理','dictValue':'0'}
        ]
      },
	  // 监测类型 9为环境监测
	  monitorType: '9',
	  // 项目信息列表
	  projectInfoList: [],
	  // 报警级别
	  alertTypeList: [
		  {
			  value: '0',
			  label: '预警提醒'
		  },
		  {
			  value: '1',
			  label: '报警提醒'
		  }
	  ],
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
	this.getProjectInfoList()
    // 获取字典数据
    this.initDicts()
    if( this.$route.query.alertType==0){
    	this.queryParams.alertType='0'
    }else if(this.$route.query.alertType==1){
    	this.queryParams.alertType='1'
    }

    this.$route.query.projectId!=undefined&&this.$route.query.projectId!=''	?this.queryParams.projectId=this.$route.query.projectId:'';
     this.$route.query.startDate!=undefined&&this.$route.query.startDate!=''	?this.queryParams.startDate=this.$route.query.startDate:'';
      this.$route.query.endDate!=undefined&&this.$route.query.endDate!=''	?this.queryParams.endDate=this.$route.query.endDate:'';
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
	  
	  // 监测类型
	  this.queryParams.monitorType = this.monitorType

      selectPageByEnv(this.queryParams)
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
    //列格式化  如一些字典等
    fieldFormatter(row, column, cellValue) {
      if (column.property ==='alertType'){
        if(row.alertType=='0'){
          return "预警提醒"
        }else if(row.alertType=='1'){
          return "报警提醒"
        }else{
          return "--"
        }
      } else if(column.property === 'monitorSonType') {
        return this.selectDictLabel(this.dictData.monitorTypeDict_9, row.monitorSonType)
      } else if (column.property === 'statisticalType') {
        return this.selectDictLabel(this.dictData.statisticalTypeDict, row.statisticalType)
      }else if(column.property ==='handleState'){
        if(row.handleState=='0'){
          return "未处理"
        }else if(row.handleState=='1'){
          return "已处理"
        }else{
          return "--"
        }
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
     * 新建点击
     */
    createClick() {
      this.action.type = 'save';
      this.createShow = true
    },
    /**
    * 查看点击
    */
    showDetails(id) {
      this.action.type = 'show';
      this.action.id = id;
      this.showDview = true
    },
    /**
   * 处理点击
   */
    handle(id) {
      this.action.type = 'handle';
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
    deleteClick(id) {
      this.$confirm("确定删除选中的数据吗？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const ids = [id];
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
      this.getDicts('b_equipment_statistical_type').then(res => {
        this.dictData.statisticalTypeDict = res.data
      })
      this.getDicts('b_equipment_monitor_type').then(res => {
        this.dictData.monitorTypeDict = res.data
      })
      this.getDicts('b_equipment_monitor_type_9').then(res => {
        this.dictData.monitorTypeDict_9 = res.data
      })
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
