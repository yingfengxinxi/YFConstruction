<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 1px solid #e6e6e6;">
      <el-form-item label="创优评优批次" prop="appraisingBatch" label-width="100px">
        <el-input v-model="queryParams.appraisingBatch" placeholder="请输入批次" clearable size="small" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item label="评审时间">
        <!--<el-date-picker v-model="queryParams.startDate" type="date" style="width: 140px" size="small" value-format="yyyy-MM-dd HH:mm:ss" placeholder="开始时间">
        </el-date-picker>&nbsp;~
        <el-date-picker v-model="queryParams.endDate" type="date" style="width: 140px" size="small" value-format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间">
        </el-date-picker>-->
        <el-date-picker
        v-model="executeDates"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="评审组长" prop="reviewLeader">
        <el-select v-model="queryParams.reviewLeader" placeholder="请选择" clearable>
          <el-option v-for="dict in dictData.userListData" :key="dict.userId" :label="dict.realname" :value="dict.userId" />
        </el-select>

        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary" @click="createClick">新建
          </el-button>
          <!-- <el-dropdown
            v-if="moreTypes.length > 0"
            trigger="click"
            @command="handleTypeDrop"
          >
            <el-button icon="el-icon-more" />
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="(item, index) in moreTypes"
                :key="index"
                v-if="canHandle(item.type)"
                :icon="item.icon | wkIconPre"
                :command="item.type"
              >{{ item.name }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown> -->
        </div>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">
      <flexbox v-if="selectionList.length > 0" class="selection-bar">
        <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
        <flexbox class="selection-items-box">
          <el-button v-for="(item, index) in selectionHandleItems" v-if="canHandle(item.type)" :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
          </el-button>
        </flexbox>
      </flexbox>
    </div>
    <!--    <div-->
    <!--      v-empty="!canHandle('list')"-->
    <!--      xs-empty-icon="nopermission"-->
    <!--      xs-empty-text="暂无权限"-->
    <!--      class="crm-container"-->
    <!--    >-->
    <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange" @selection-change="handleSelectionChange">
      <el-table-column show-overflow-tooltip type="selection" align="center" width="55" />
      <el-table-column type="index" label="序号" align="center" width="80" />
      <!-- <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          :sortable="false"
          show-overflow-tooltip
        > -->

      <!-- </el-table-column> -->
      <el-table-column label="创优评优批次" align="center" prop="appraisingBatch" sortable="custom"/>
      <el-table-column label="评审开始时间" align="center" prop="reviewTime" width="150px" sortable="custom"/>
      <el-table-column label="评审结束时间" align="center" prop="reviewEndTime" width="150px" sortable="custom"/>
      <el-table-column label="评审标准" align="center" prop="reviewStandard" sortable="custom"/>
      <el-table-column label="评审组长" align="center" prop="reviewLeader" width="150px" sortable="custom"/>
      <el-table-column label="审核状态" align="center" prop="checkStatus" width="150px" sortable="custom">
        <template slot-scope="scope">
          <el-popover width="300" v-model="scope.row.showPopover">
            <check-flow :show="scope.row.showPopover" :id="scope.row.examineRecordId" @close="scope.row.showPopover = false" />
            <el-button slot="reference" type="text">
              <span :style="getStatusStyle(scope.row.checkStatus)" class="status-mark" />
              <span>{{ getStatusName(scope.row.checkStatus) }}</span>
            </el-button>
          </el-popover>
        </template>

      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.id)">修改
          </el-button>
          <el-button v-if="scope.row.checkStatus == '5'" size="mini" type="text" icon="el-icon-delete" @click="deleteClick(scope.row.id)">删除
          </el-button>
          <el-button v-else size="mini" type="text" icon="el-icon-delete" disabled>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination :current-page="currentPage" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>
    <!--    </div>-->
    <!-- 相关详情页面 -->
    <!-- <detail
      v-if="showDview"
      :id="rowID"
      :businessType="businessType"
      @close="showDview = false"/> -->
    <!-- 新建 -->
    <create v-if="createShow" :businessType="businessType" :dictData="dictData" :action="action" @close="createShow = false" @save-success="getList" />
  </div>
</template>

<script>
import { mapGetters } from "vuex"; //权限
import Lockr from "lockr"; //分页
import CheckStatusMixin from "@/mixins/CheckStatusMixin"; //审核状态

import Create from "./Create";
import Detail from "./Detail";
import { userListAPI } from "@/api/common"; // 直属上级接口  用户接口
import CheckFlow from './../../../../components/common/examine/components/CheckFlow' // 审批流程

import {
  getAppraisingDeclareList, remove
} from "../api/appraisingDeclare";
import { sortColumn } from '@/utils/index';

export default {
  /** DEMO列表 */
  name: "appraisingDeclare",
  components: {
    Create,
    Detail,
    CheckFlow
  },
  mixins: [CheckStatusMixin],
  data () {
    return {
      //form-流程发起组件使用参数
      businessType: 10020,
      dictData: {
        // 巡检类型字典项
        checkTypeDict: [],
        // 形象进度字典项
        imageProgressDict: [],
        userListData: []
      },
      loading: false, // 加载动画
      /** 页面头部 */
      inputContent: "", //查询值
      //更多操作
      // moreTypes: [
      //   { type: "import", name: "导入", icon: "import" },
      //   { type: "export", name: "导出", icon: "export" }
      // ],
      /** 选中时操作 */
      selectionHandleItems: [
        {
          name: "删除",
          type: "delete",
          icon: "delete"
        },
        // {
        //   name: "导出选中",
        //   type: "export",
        //   icon: "export"
        // }
      ],
      /** 列表 */
      tableHeight: document.documentElement.clientHeight - 250, // 表的高度
      list: [],
      fieldList: [
        //表头
        {
          prop: "appraisingBatch",
          label: "创优评优批次"
          //width: "150"
        },
        {
          prop: "reviewTime",
          label: "评审开始时间"
          //width: "250"
        },
        {
          prop: "reviewEndTime",
          label: "评审结束时间"
          //width: "250"
        },
        {
          prop: "reviewStandard",
          label: "评审标准"
          //width: "250"
        },
        {
          prop: "reviewLeader",
          label: "评审组长"
          //width: "180"
        },
        {
          prop: "checkStatus",
          label: "审核状态",
          width: "180"
        }
      ],
      executeDates:'',
      sortData: [], // 字段排序
      currentPage: 1,
      pageSize: 10,
      // pageSizes: [10,20,30,40,50,100],
      total: 5,
      queryParams: {},
      /** 新增*/
      createShow: false,
      /***/
      action: { type: "save", isReadonly: false, data: null },
      /** 勾选行 */
      selectionList: [], // 勾选数据 用于全局导出
      /** 控制详情展示 */
      rowID: "", // 行信息
      showDview: false,
      /** 高级筛选 2021-03-15 暂未使用*/
      filterObj: [], // 筛选确定数据
      sceneId: "", // 场景筛选ID
      sceneName: "" // 场景名字
    };
  },
  computed: {
    //权限 start
    ...mapGetters(["allAuth"]),
    //权限 end
    titleIcon () {
      return require(`@/assets/img/crm/customer.png`);
    }
  },
  mounted () {
    /** 控制table的高度 */
    window.onresize = () => {
      this.updateTableHeight();
    };
    //获取列表数据
    this.getList();
  },
  created () {
    // 初始化字典数据
    this.initDicts();
  },
  methods: {
    /** 获取列表数据 */
    getList () {

      this.loading = true;
      if (this.executeDates != null && this.executeDates != '') {
		      this.queryParams.startDate = this.executeDates[0]
		      this.queryParams.endDate = this.executeDates[1]
		    } else {
		      this.queryParams.startDate = ''
		      this.queryParams.endDate = ''
		    }
      //分页
      this.queryParams.page = this.currentPage;
      this.queryParams.limit = this.pageSize;
      //排序
      if (this.sortData.length > 0) {
        var orders = [];
        this.sortData.forEach(function (item, index) {
          var one = {};
          one.prop = item.prop;
          one.asc = item.order == "ascending";
          orders.push(orders);
        });
        this.queryParams.orders = orders;
      }
      //场景ID
      if (this.sceneId) {
        this.queryParams.sceneId = this.sceneId;
      }
      if (this.filterObj && this.filterObj.length > 0) {
        this.queryParams.searchList = this.filterObj;
      }
      // console.log(this.queryParams)
      getAppraisingDeclareList(this.queryParams)
        .then(res => {
          // console.log(res);
          this.list = res.data.list;
          // for(var temp in list){
          //     temp.showPopover = false;
          //   }
          if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
            this.currentPage = this.currentPage - 1;
            this.getList();
          } else {
            this.total = res.data.totalRow;
            this.loading = false;
          }
          // console.log(this.total)
          this.$nextTick(() => {
            document.querySelector(".el-table__body-wrapper").scrollTop = 1;
          });
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    /**
     *更多操作点击
     */
    handleTypeDrop (command) {
      console.log(command);
      if (command == "delete") {
        //删除
        this.$confirm("确定删除选中的数据吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            const ids = this.selectionList.map(item => item["id"]);
            var flag = false
            for (var i = 0; i < this.selectionList.length; i++) {
              var item = this.selectionList[i];
              if (item.checkStatus != '5') {
                flag = true
              }
            }
            this.loading = true;
            if (flag) {
              this.$message({
                message: "存在未结束流程的信息，不能删除，请重新选择",
                type: 'warning'
              })
              this.loading = false;
            } else {
              remove(ids)
                .then(res => {
                  if (res.code == 0) {
                    this.$message.success("删除成功");
                    this.getList();
                  } else {
                    this.$message.error(res.msg);
                  }
                  this.loading = false;
                })
                .catch(err => {
                  this.loading = false;
                });
            }
          })
          .catch(() => { });
      } else if (command == "export") {
        //导出
      } else if (command == "import") {
        //导入
      }
    },
    // 当某一行被点击时会触发该事件
    handleRowClick (row, column, event) {
      if (column.property == "code") {
        this.rowID = row.id;
        this.showDview = true;
      }
    },
    //列格式化  如一些字典等
    fieldFormatter (row, column, cellValue) {
      if (column.property === "reviewLeader") {
        return this.CustomFormat(
          this.dictData.userListData,
          row.reviewLeader

        );
      } else {
        return row[column.property] === "" || row[column.property] === null
          ? "--"
          : row[column.property];
      }
    },
    //按钮操作权限
    canHandle (type) {
      return this.allAuth.demo
        ? this.allAuth.demo.wf
          ? this.allAuth.demo.wf[type]
          : false
        : false;
    },
    sortChange(column) {
      sortColumn(this,column);
    },
    /** 勾选操作 */
    // 当选择项发生变化时会触发该事件
    handleSelectionChange (val) {
      this.selectionList = val; // 勾选的行
      this.updateTableHeight();
    },

    // 更改当前页数
    handleCurrentChange (val) {
      this.currentPage = val;
      this.getList();
    },
    // 0待审核、1审核中、2审核通过、3已拒绝 4已撤回 5未提交
    getStatusStyle (status) {
      return {
        backgroundColor: this.getStatusColor(status)
      };
    },
    /**
     * 通过回调控制class
     */
    cellClassName ({ row, column, rowIndex, columnIndex }) {
      if (column.property === "code") {
        return "can-visit--underline can-visit--bold";
      }
    },

    headerCellClassName ({ row, column, rowIndex, columnIndex }) {
      /*if (column.property === 'code') {
          return 'header-can-visit-backgroud'
        }*/
      return "";
    },
    // 更改每页展示数量
    handleSizeChange (val) {
      // Lockr.set("crmPageSizes", val);
      this.pageSize = val;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams = {};
      this.getList();
    },
    /**
     * 新建点击
     */
    createClick () {
      this.action.type = "save";
      this.createShow = true;
    },
    /**
     * 修改点击
     */
    updateClick (id) {
      console.log("修改id==", id);
      this.action.type = "update";
      this.action.id = id;
      this.createShow = true;
    },
    deleteClick (id) {
      this.$confirm("确定删除选中的数据吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const ids = [id];
          this.loading = true;
          remove(ids)
            .then(res => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.getList();
              } else {
                this.$message.error(res.msg);
              }
              this.loading = false;
            })
            .catch(err => {
              this.loading = false;
            });
        })
        .catch(() => { });
    },
    /**
     * 更新表高
     */
    updateTableHeight () {
      var offsetHei = document.documentElement.clientHeight;
      var removeHeight = this.selectionList.length > 0 ? 300 : 250;
      this.tableHeight = offsetHei - removeHeight;
    },
    // 初始化字典数据
    initDicts () {
      this.getDicts("b_chekc_type").then(response => {
        this.dictData.checkTypeDict = response.data;
      });
      this.getDicts("b_image_progress").then(response => {
        this.dictData.imageProgressDict = response.data;
      });
      // 查询当前项目的用户

      var params = {
        pageType: 0
      };
      userListAPI(params)
        .then(res => {
          this.dictData.userListData = res.data.list;
        })
        .catch(() => { });
    },
    CustomFormat (datas, value) {
      // console.log(datas,value);
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].userId == "" + value) {
          actions.push(datas[key].realname);
          return true;
        }
      });
      return actions.join("");
    }
  }
};
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
