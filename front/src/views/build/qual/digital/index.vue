<template>
  <div>
<!--    <xr-header-->
<!--            icon-class="wk wk-associate"-->
<!--            icon-color="#FFB940"-->
<!--            label="数字质量资料管理" />-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="120px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="质量资料编号" prop="infoCode">
        <el-input
          v-model="queryParams.infoCode"
          placeholder="请输入质量资料编号"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="质量资料名称" prop="infoName">
        <el-input
          v-model="queryParams.infoName"
          placeholder="请输入质量资料名称"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <el-button
            v-if="canHandle('add')"
            class="xr-btn--orange"
            icon="el-icon-plus"
            type="primary"
            @click="createClick">新建
          </el-button>
          <el-dropdown
            v-if="moreTypes.length > 0"
            trigger="click"
            @command="handleTypeDrop">
            <el-button icon="el-icon-more"/>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="(item, index) in moreTypes"
                :key="index"
                v-if="canHandle(item.type)"
                :icon="item.icon | wkIconPre"
                :command="item.type">{{ item.name }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">
      <flexbox
        v-if="selectionList.length > 0"
        class="selection-bar">
        <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
        <flexbox class="selection-items-box">
          <el-button
            v-for="(item, index) in selectionHandleItems"
            v-if="canHandle(item.type)"
            :icon="item.icon | wkIconPre"
            :key="index"
            type="primary"
            @click.native="handleTypeDrop(item.type)">{{ item.name }}
          </el-button>
        </flexbox>
      </flexbox>
    </div>
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
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.digitalInfoId)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteClick(scope.row.digitalInfoId)"
            >删除
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
    <!-- 新建 -->
    <create
      v-if="createShow"
      :businessType="businessType"
      :action="action"
      @close="createShow = false"
      @save-success="getList"
    />
  </div>
</template>

<script>

  import XrHeader from '@/components/XrHeader'
  import {mapGetters} from 'vuex'//权限
  // import CheckStatusMixin from '@/mixins/CheckStatusMixin'//审核状态

  import Create from './Create'
  import Detail from './Detail'

  import {getList, delTrueByIds} from "../api/digital";

  export default {
    name: 'digital',
    components: {
      XrHeader,
      Create,
      Detail
    },
    // mixins: [CheckStatusMixin],
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画
        /** 页面头部 */
        inputContent: '',//查询值
        moreTypes: [
          // {type: 'import', name: '导入', icon: 'import'},
          // {type: 'export', name: '导出', icon: 'export'},
        ],//更多操作
        /** 选中时操作 */
        selectionHandleItems: [
          {
            name: '删除',
            type: 'delete',
            icon: 'delete'
          },
          // {
          //   name: '导出选中',
          //   type: 'export',
          //   icon: 'export'
          // }
        ],
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "infoCode",
            label: "质量资料编号",
            width: "220"
          },
          {
            prop: "infoName",
            label: "质量资料名称",
            width: "260"
          },
          {
            prop: "submitDate",
            label: "提交日期",
            width: "200"
          },
          {
            prop: "submitUserId",
            label: "提交人",
            width: "200"
          },
          {
            prop: "remarks",
            label: "备注"
          }
          /*{
            prop: "checkStatus",
            label: "审核状态",
            width: "180"
          },*/
        ],
        sortData: [], // 字段排序
        currentPage: 1,
        pageSize: 15,
        pageSizes: [15, 30, 60, 100],
        total: 0,
        queryParams:{},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
        /** 控制详情展示 */
        rowID: '', // 行信息
        showDview: false,
        /** 高级筛选 2021-03-15 暂未使用*/
        filterObj: [], // 筛选确定数据
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
        if (this.filterObj && this.filterObj.length > 0) {
          this.queryParams.searchList = this.filterObj
        }
        getList(this.queryParams)
          .then(res => {
            this.list = res.data.list;
            this.total = res.data.totalRow
            // if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
            //   this.currentPage = this.currentPage - 1
            //   this.getList()
            // } else {
            //   this.total = res.data.totalRow
            //   this.loading = false
            // }
            // this.$nextTick(() => {
            //   document.querySelector('.el-table__body-wrapper').scrollTop = 1
            // })
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      /**
       *更多操作点击
       */
      handleTypeDrop(command) {
        console.log(command)
        if (command == 'delete') {//删除
          this.$confirm("确定删除选中的数据吗？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              const ids = this.selectionList.map(item => item['id'])
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
        } else if (command == 'export') {//导出

        } else if (command == 'import') {//导入

        }
      },
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
        if (column.property == 'code') {
          this.rowID = row.digitalInfoId;
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
        this.queryParams = {};
        this.getList()
      },
      /**
       * 新建点击
       */
      createClick() {
        this.action.type = 'save';

        console.log("--createClick--");
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
