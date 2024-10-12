<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="项目名称" v-if="isJG == 1?true:false">
        <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
          <el-option v-for="item in projects" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="监测类型">
        <el-select v-model="queryParams.monitorType" clearable placeholder="请选择监测项类型">
          <el-option
            v-for="item in dictData.monitorTypeDict"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态">
        <el-select v-model="queryParams.online" clearable placeholder="请选择设备状态">
          <el-option v-for="item in dictData.equiState" :key="item.dictValue" :label="item.dictLabel"
                     :value="item.dictValue">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="监测点名称">
          <el-input v-model="queryParams.pointName" placeholder="请输入监测点名称" clearable size="small" @keyup.enter.native="getList" />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限">
      <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" class="n-table--border"
                use-virtual stripe border highlight-current-row style="width: 100%"
                :row-key="getRowKeys"
                :expand-row-keys="expandRows"
                @expand-change="handleExpand"
                @sort-change="sortChange"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-button icon="el-icon-refresh" circle style="position: absolute;right:9px;" title="刷新"
                       @click="postionDataRe(props.row.assetId)"></el-button>
            <div class="expand_div">
              <el-table class="n-table--border" :data="positionJSON[props.row.assetId]"
                        style="width: 100%"
              >
                <el-table-column label="监测项名称" align="center" prop="pointName">
                </el-table-column>
                <el-table-column label="监测点编号" width="250" align="center" prop="pointCode"/>
                <el-table-column label="监测值" width="150" align="right" prop="value"/>
                <el-table-column label="数值单位" width="200" align="left" prop="unit"/>
                <el-table-column label="监测时间" width="250" align="center" prop="monitorTime"/>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-for="(item, index) in fieldList" :key="index" :prop="item.prop"
                         :label="item.label" :width="item.width" sortable="custom"
                         :align="item.align" show-overflow-tooltip
                         :v-if="item.prop == 'projectName'?isJG == 1?true:false:true">
          <template slot-scope="scope">
            <div class="status-name" v-if="item.prop == 'online'">
              <div :style="{'background-color' : getStatusColor(scope.row.online)}"
                   style="display: inline-block;width: 6px;height: 6px;border-radius: 3px;margin-right: 5px;"/>
              {{ scope.row.online =='1'?'在线':'离线' }}
            </div>
            <div v-if="item.prop == 'monitorType'">
              {{getMonitorTypeName(scope.row.monitorType)}}
            </div>
            <div v-if="item.prop != 'online' && item.prop != 'monitorType'">
              {{scope.row[item.prop]}}
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <div class="status-des">
          <div
            :key="1"
            class="status-des-item">
            <div :style="{'background-color' :'#2362fb'}"/>
            在线
          </div>
          <div
            :key="0"
            class="status-des-item">
            <div :style="{'background-color' :'#cccac9'}"/>
            离线
          </div>
        </div>
        <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total"
                       :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper"
                       @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
      </div>
    </div>
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页

  import {getEquiStateList, getProjectsList} from "../api/equiState"

  import {getPositionListByAssetId} from "../api/monitorPoint.js"
  import { mapState} from 'vuex'
  import { sortColumn,sortColumnNoToLowerCase } from '@/utils/index';


  export default {
    /** 设备列表
     *  参数 proId
     *  PRO 项目页面
     *
     * */
    name: 'equiState',
    components: {},
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
        tableHeight: document.documentElement.clientHeight - 250, // 表的高度

        fieldList: [//表头
          /* {
            prop: "projectName",
            label: "项目名称",
            align: 'center',
          }, */
          {
            prop: "assetName",
            label: "设备名称",
            align: 'center',
          },
          {
            prop: "assetCode",
            label: "设备编号",
            align: 'center',
          },
          {
            prop: "monitorType",
            label: "监测类型",
            align: 'center',
          },
          {
            prop: "online",
            label: "状态",
            align: 'center',
          },
          {
            prop: "lastUpdateTime",
            label: "最后更新时间",
            align: 'center',
          },
        ],
        projects: [],
        list: [],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams: {},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
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
          // 设备类型
          equipmentOptions: [],
          // 监测项类型子项-环境监测
          monitorTypeDict_9: [],
          equiState: [{'dictValue': '0', 'dictLabel': '离线'}, {'dictValue': '1', 'dictLabel': '在线'}],
          equiTypeList: [{'dictValue': '30', 'dictLabel': '视频监控'}, {
            'dictValue': '40',
            'dictLabel': '环境监测'
          }, {'dictValue': '50', 'dictLabel': '喷淋设备'}]
        },
        //jg or 项目
        isJG: 0,
        projectId:'',
        //
        positionJSON: {},
        //展开行
        expandRows: [],
        //
        monitorTypeDict: []
      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
	  ...mapState(['user']),
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
      //尝试获取proId
      this.projectId = this.$route.params.proId;
      // // 获取字典数据
      this.initDicts()

      //获取列表数据
      this.getList()
    },
    methods: {
      /** 展开相关 */
      postionDataRe(assetId) {//刷新点位
        getPositionListByAssetId(assetId)
          .then(res => {
            this.positionJSON[assetId] = res.data == null ? [] : res.data;
          });
      },
      // 控制只展开一个 如果不需要控制 可以删除相关代码
      getRowKeys(row) {
        return row.assetId
      },
      //异步加载
      handleExpand(row, expandRows) {
        var assetId = row.assetId;
        if (expandRows.length) {
          this.expandRows = []
          this.expandRows.push(assetId)
          this.postionDataRe(assetId);
        }
      },
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //
        this.expandRows = [];
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;
        //
        //this.queryParams.projectId = this.projectId;
        this.queryParams.projectId = this.user.userInfo.projectId;
        getEquiStateList(this.queryParams)
          .then(res => {
            this.list = res.data.list;
            //初始化折叠表默认数据
            this.list.forEach(item => {
              this.positionJSON[item.assetId] = [];
            })
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
      getMonitorTypeName(v) {
        var name = "--";
        this.dictData.monitorTypeDict.forEach(item => {
          if (item.dictValue == v) {
            name = item.dictLabel;
          }
        })
        return name;
      },
      //按钮操作权限
      canHandle(type) {
        return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      // 获取状态颜色 0 报废 1 正常
      getStatusColor(status) {
        if (status == '1') {
          return '#2362fb'
        } else {
          return '#cccac9'
        }
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
      sortChange(column) {
        //sortColumn(this,column);
        var list = []
        list.push("lastUpdateTime");
        sortColumnNoToLowerCase(this,column,list);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.getList()
      },
      // 初始化字典数据
      initDicts() {
        // this.getDicts('b_equipment_statistical_type').then(res => {
        //     this.dictData.statisticalTypeDict = res.data
        // })
        this.getDicts('b_equipment_monitor_type').then(res => {
          this.dictData.monitorTypeDict = res.data
        })
        // this.getDicts('b_equipment_monitor_type_9').then(res => {
        //     this.dictData.monitorTypeDict_9 = res.data
        // })
        getProjectsList().then(res => {
          console.log(res)
          this.projects = res.data;
        })
      },
      /** 勾选操作 */
      // 当选择项发生变化时会触发该事件
      handleSelectionChange(val) {
        this.selectionList = val // 勾选的行
        this.updateTableHeight();
      },
      /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 260
        this.tableHeight = offsetHei - removeHeight
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

  .status-des {
    font-size: 12px;
    color: #777777;
    margin: 0 5px;
    position: absolute;
    left: 0;
    top: 7px;
    .status-des-item {
      margin: 8px;
      display: inline-block;
      div {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 3px;
        margin-right: 5px;
      }
    }
  }

  .expand_div {
    border: solid 1px #e6e6e6;
    /deep/ .el-table__empty-block {
      width: 100% !important;
    }
  }

  /deep/ .has-gutter {
    .cell {
      text-align: center;
    }
  }

  @import "@/views/styles/table.scss";
</style>
