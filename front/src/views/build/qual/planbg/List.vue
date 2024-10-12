<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="变更原因">
        <el-input
          v-model="queryParams.reason"
          placeholder="请输入变更原因"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="变更人">
        <el-input
          v-model="queryParams.bgBy"
          placeholder="请输入变更人"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
        :cell-class-name="cellClassName"
        :header-cell-class-name="headerCellClassName"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
        @sort-change="sortChange">
        <el-table-column
          show-overflow-tooltip
          type="selection"
          align="center"
          width="55"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          :sortable="false"
          :align="item.align"
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
    <!-- 相关详情页面 -->
    <detail
      v-if="showDview"
      :id="rowID"
      :businessType="businessType"
      @close="showDview = false"/>
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import CheckStatusMixin from '@/mixins/CheckStatusMixin'//审核状态

  import Detail from './Detail'

  import {getList} from "../api/planbg";

  export default {
    /** DEMO列表 */
    name: 'PlanBgList',
    components: {
      Detail,
    },
    mixins: [CheckStatusMixin],
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画

        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 440, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "reason",
            label: "变更原因",
          },
          {
            prop: "bgBy",
            label: "变更人",
            width: "180",
            align:'center'
          },
          {
            prop: "bgDate",
            label: "变更日期",
            width: "180",
            align:'center'
          },

          /*{
            prop: "checkStatus",
            label: "审核状态",
            width: "180"
          },*/
        ],
        sortData: [], // 字段排序
        currentPage: 1,
        pageSize: Lockr.get('crmPageSizes') || 15,
        pageSizes: [15, 30, 60, 100],
        total: 0,
        queryParams:{},
        /***/
        action: {type: 'save', data: null},
        /** 控制详情展示 */
        rowID: '', // 行信息
        showDview: false,
        /** 高级筛选 2021-03-15 暂未使用*/
        filterObj: [], // 筛选确定数据
        sceneId: '', // 场景筛选ID
        sceneName: '', // 场景名字
      }
    },
    props: {
      id: {
        type: String,
        require:true,
        default: '-1'
      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
      //权限 end
    },
    mounted() {
      /** 控制table的高度 */
      window.onresize = () => {
        this.updateTableHeight()
      }
      //获取列表数据
      this.getList();
    },
    methods: {
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //方案ID
        this.queryParams.planId = this.id;
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
        if (column.property == 'reason') {
          this.rowID = row.id;
          this.showDview = true;
        }
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
      //按钮操作权限
      canHandle(type) {
        return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      /**
       * 字段排序
       */
      sortChange(column, prop, order) {
        this.currentPage = 1
        this.sortData = column
        this.getList()
      },

      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },
      // 0待审核、1审核中、2审核通过、3已拒绝 4已撤回 5未提交
      getStatusStyle(status) {
        return {
          backgroundColor: this.getStatusColor(status)
        }
      },
      /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'reason') {
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
        this.queryParams = {};
        this.getList()
      },
      /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 320
        this.tableHeight = offsetHei - removeHeight
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
