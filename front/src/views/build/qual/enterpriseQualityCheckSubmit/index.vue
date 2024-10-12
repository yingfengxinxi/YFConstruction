<template>
  <div>
    <!-- <div class="c-container">
      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'
        :src="titleIcon"
        class="title-icon">{{titleName}}
      </flexbox>
    </div> -->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">

      <el-form-item label="模板名称" prop="checkTemplateId">
        <el-select v-model="queryParams.checkTemplateId" placeholder="请选择" clearable size="small" style="width: 200px">
          <el-option v-for="dict in checkTemplates" :key="dict.id" :label="dict.checkTemplateName" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="检查项目" prop="checkProject">
        <el-select v-model="queryParams.checkProject" placeholder="请选择" clearable size="small" style="width: 200px">
          <el-option v-for="dict in ProjectInfos" :key="dict.id" :label="dict.projectName" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="评定结果" prop="evaluateResult">
        <el-select v-model="queryParams.evaluateResult" placeholder="请选择" clearable size="small" style="width: 200px">
          <el-option v-for="dict in EvaluationResults" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="检查得分" prop="checkScore">
        <el-input v-model="queryParams.checkScoreStart" placeholder="请输入" style="width: 140px" clearable size="small" @keyup.enter.native="getList" /> ~
        <el-input v-model="queryParams.checkScoreEnd" style="width: 140px" placeholder="请输入" clearable size="small" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item>
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
    <div v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限">
      <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange" @selection-change="handleSelectionChange">
        <el-table-column show-overflow-tooltip type="selection" align="center" width="55" />
        <el-table-column fixed type="index" label="序号" align="center" width="80" />
        <el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.id)">修改
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteClick(scope.row.id)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
    </div>
    <!-- 相关详情页面 -->
    <detail v-if="showDview" :id="rowID" :titleName="titleName" :businessType="businessType" @close="showDview = false" />
    <!-- 新建 -->
    <create v-if="createShow" :businessType="businessType" :action="action" :titleName="titleName" @close="createShow = false" @save-success="getList" />
  </div>
</template>

<script>
import { mapGetters } from "vuex"; //权限
import Lockr from "lockr"; //分页
import CheckStatusMixin from "@/mixins/CheckStatusMixin"; //审核状态

import Create from "./Create";
// import Detail from "./Detail";

import { getCheckList, remove, getProjectInfo } from "../api/enterpriseQualityCheckSubmit";
import { sortColumn } from '@/utils/index';
import { getList } from "../api/enterpriseQualityCheckSetting";
export default {
  /** DEMO列表 */
  name: "enterpriseQualityCheckSubmit",
  components: {
    Create
    // Detail
  },
  mixins: [CheckStatusMixin],
  data () {
    return {
      //评定结果
      EvaluationResults: [],
      //项目信息
      ProjectInfos: [],
      //模板信息
      checkTemplates: [],
      businessType: 10000,
      loading: false, // 加载动画
      /** 页面头部 */
      inputContent: "", //查询值
      moreTypes: [
        { type: "import", name: "导入", icon: "import" },
        { type: "export", name: "导出", icon: "export" }
      ], //更多操作
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
      tableHeight: document.documentElement.clientHeight - 310, // 表的高度
      list: [],
      fieldList: [
        //表头
        {
          prop: "checkTemplateId",
          label: "模板名称",
          // width: "400"
        },
        {
          prop: "checkProject",
          label: "检查项目"
          // width: "350"
        },
        {
          prop: "checkPersonName",
          label: "检查人"
          // width: "250"
        },
        {
          prop: "checkDate",
          label: "检查日期",
          // width: "250"
        },
        {
          prop: "checkScore",
          label: "检查得分",
          // width: "150"
        },
        {
          prop: "evaluateResult",
          label: "评定结果",
          // width: "150"
        }
      ],
      sortData: [], // 字段排序
      currentPage: 1,
      pageSize: Lockr.get("crmPageSizes") || 10,
      pageSizes: [10, 20, 30, 40, 50, 100],
      total: 0,
      queryParams: {},
      /** 新增*/
      createShow: false,
      /***/
      action: { type: "save", data: null },
      /** 勾选行 */
      selectionList: [], // 勾选数据 用于全局导出
      /** 控制详情展示 */
      rowID: "", // 行信息
      showDview: false,
      /** 高级筛选 2021-03-15 暂未使用*/
      filterObj: [], // 筛选确定数据
      sceneId: "", // 场景筛选ID
      sceneName: "", // 场景名字
      titleName: "",

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
    this.initData(this.$route);
    /** 控制table的高度 */
    window.onresize = () => {
      this.updateTableHeight();
    };
    //获取列表数据
    this.getList();
    //获取模板数据
    this.getCheckTemplates();
    //获取所有项目信息
    getProjectInfo()
      .then(res => {
        // console.log('项目信息',res.data.list)
        this.ProjectInfos = res.data.list;
      })
      .catch(() => { });
  },
  watch: {
    //监听页面切换
    $route (to, from) {
      //TODO 重新加载数据
      this.initData(to);
    }
  },
  methods: {
    //初始化数据
    initData (to) {
      this.titleName = to.meta.title;
      //获取评定结果字典数据
      this.getDicts('b_qual_EvaluationResults').then(response => {
        // console.log('评定结果',response.data)
        this.EvaluationResults = response.data
      })
      // if(to.path.indexOf('/standardroomMonit')>0){
      //  this.monitType = '2'
      // }else if(to.path.indexOf('/actualMonit')>0){
      //   this.monitType = '1'
      // }else if(to.path.indexOf('/inspectMonit')>0){
      //   this.monitType = '3'
      // }
      this.getList();
    },
    /** 获取列表数据 */
    getList () {
      this.loading = true;
      //分页
      this.queryParams.page = this.currentPage;
      this.queryParams.limit = this.pageSize;

      //监测项 分类
      this.queryParams.type = this.monitType;

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
      getCheckList(this.queryParams)
        .then(res => {
          // console.log(res);
          this.list = res.data.list;
          if (
            res.data.totalRow &&
            Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage &&
            this.currentPage > 1
          ) {
            this.currentPage = this.currentPage - 1;
            this.getList();
          } else {
            this.total = res.data.totalRow;
            this.loading = false;
          }
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
      // console.log(command)
      if (command == "delete") {
        //删除
        this.$confirm("确定删除选中的数据吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            const ids = this.selectionList.map(item => item["id"]);
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
      } else if (command == "export") {
        //导出
      } else if (command == "import") {
        //导入
      }
    },
    // 当某一行被点击时会触发该事件
    handleRowClick (row, column, event) {
      // console.log(column)
      // if (column.property == "code") {
      //   this.rowID = row.id;
      //   this.showDview = true;
      // }
    },

    //列格式化  如一些字典等
    fieldFormatter (row, column, cellValue) {
      if (column.property == "checkTemplateId") {
        return this.CustomFormat(this.checkTemplates, row.checkTemplateId);
      } else if (column.property == "checkProject") {
        return this.CustomFormat_ProjectInfos(this.ProjectInfos, row.checkProject);
      } else if (column.property == "evaluateResult") {
        return this.selectDictLabel(this.EvaluationResults, row.evaluateResult);
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
      Lockr.set("crmPageSizes", val);
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
      this.action.monitType = this.monitType;
      this.createShow = true;
    },
    /**
     * 修改点击
     */
    updateClick (id) {
      this.action.type = "update";
      this.action.id = id;
      this.createShow = true;
    },
    deleteClick (id) {
      this.$confirm("确定删除当前数据吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const ids = [id];
          // console.log(ids)
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
    updateTableHeight () {
      var offsetHei = document.documentElement.clientHeight;
      var removeHeight = this.selectionList.length > 0 ? 360:310;
      this.tableHeight = offsetHei - removeHeight;
    },
    /**查询所有模板数据 */
    getCheckTemplates () {
      getList()
        .then(res => {
          this.checkTemplates = res.data.list;
        })
        .catch(() => { });
    },

    /**
     * 模板自定义格式化列表
     */
    CustomFormat (datas, value) {
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].id == "" + value) {
          actions.push(datas[key].checkTemplateName);
          return true;
        }
      });
      return actions.join("");
    },
    /**
    * 模板自定义格式化列表
    */
    CustomFormat_ProjectInfos (datas, value) {
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].id == "" + value) {
          actions.push(datas[key].projectName);
          return true;
        }
      });
      return actions.join("");
    },
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
