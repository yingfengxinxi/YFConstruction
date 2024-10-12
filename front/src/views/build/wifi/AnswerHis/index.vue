<template>
    <div>
        <!-- 头部：搜索、操作 start -->
        <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
            <el-form-item label="工种" prop="workTypeId">
                <el-select v-model="queryParams.workTypeId" placeholder="请选择" clearable size="small" style="width: 200px">
                    <el-option v-for="dict in workTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item label="得分" prop="questScore">
                <el-input v-model="queryParams.questScoreStart" placeholder="请输入" style="width: 140px" clearable size="small" @keyup.enter.native="getList" /> ~
                <el-input v-model="queryParams.questScoreEnd" style="width: 140px" placeholder="请输入" clearable size="small" @keyup.enter.native="getList" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
                <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed align="center" ></el-table-column>
                <el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop" :label="item.label" align="center" :width="item.width" :formatter="fieldFormatter" sortable="custom" show-overflow-tooltip >

                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200px" >
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" icon="el-icon-document" @click="showDetails(scope.row.testId+'')">查看
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="p-contianer">
                <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
        </div>
        <!-- 相关详情页面 -->
        <detail v-if="showDview" :action="action" :titleName="titleName" @close="showDview = false" />
    </div>
</template>
<style>
.el-table--enable-row-transition .el-table__body td.el-table__cell{
    height: 70px !important;
}
</style>
<script>
import { mapGetters } from "vuex"; //权限
import Lockr from "lockr"; //分页
import { getAllTestPaper, getWorkTypeList } from "../api/AnswerHis";
import { getPaperRulesList } from "../api/paperRules";
import Detail from './Detail'
import {sortColumn } from '@/utils'
export default {
    /** DEMO列表 */
    name: "wifiAnswerHis",
    components: {
        Detail
    },
    data () {
        return {
            PaperRules: [],
            dictList: [],
			workTypeOptions: [],
            loading: false, // 加载动画
            /** 页面头部 */
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
                //     name: "导出选中",
                //     type: "export",
                //     icon: "export"
                // }
            ],
            /** 列表 */
            tableHeight: document.documentElement.clientHeight - 280, // 表的高度
            list: [],
            fieldList: [
                //表头
                {
                    prop: "paperRulesId",
                    label: "试卷规则",
                    // width: "400"
                },
                {
                    prop: "workTypeId",
                    label: "适用工种"
                    // width: "350"
                },
                {
                    prop: "paperScore",
                    label: "试卷总分值"
                    // width: "250"
                },
                {
                    prop: "paperCount",
                    label: "试题数量",
                    // width: "250"
                },
                {
                    prop: "answerName",
                    label: "答题人员",
                    // width: "150"
                },
                {
                    prop: "questScore",
                    label: "总得分",
                    // width: "150"
                },
	               {
	                   prop: "createTime",
	                   label: "答题时间",
	                   // width: "150"
	               },
	               {
	                   prop: "updateTime",
	                   label: "审阅时间",
	                   // width: "150"
	               }
            ],
            sortData: [], // 字段排序
            currentPage: 1,
            pageSize: Lockr.get("crmPageSizes") || 10,
            pageSizes: [10, 20, 30, 40, 50, 100],
            total: 0,
            queryParams: {},
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
        // this.getList();
    },
    watch: {
        //监听页面切换
        $route (to, from) {
            //TODO 重新加载数据
            this.initData(to);
        }
    },
    methods: {
    	sortChange(column) {
	      sortColumn(this,column);
	    },
        //初始化数据
        initData (to) {
            this.titleName = to.meta.title;
            //获取工种字典数据
            //this.getWorkTypeList();
			// 工种字典
			this.getDicts('b_labour_work_type').then(response => {
			  this.workTypeOptions = response.data
			  this.action.data = response.data;
			})
            this.getPaperRulesLists();
            this.getList();
        },
        getPaperRulesLists () {
            getPaperRulesList()
                .then(res => {
                    //  console.log("获取数据====", res);
                    this.PaperRules = res.data.list;
                })
                .catch(() => {
                    this.loading = false;
                });
        },
        getWorkTypeList () {
            getWorkTypeList()
                .then(res => {
                    this.dictList = res.data;
                    this.action.data = res.data;
                })
                .catch(() => {
                    this.loading = false;
                });
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
            getAllTestPaper(this.queryParams)
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
            if (command == "export") {
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
            if (column.property == "workTypeId") {
                //return this.CustomFormat(this.dictList, row.workTypeId);
				return this.selectDictLabel(this.workTypeOptions, row.workTypeId)
            } else if (column.property == "paperRulesId") {
                return this.CustomFormat2(this.PaperRules, row.paperRulesId);
            } else {
                return row[column.property] === "" || row[column.property] === null
                    ? "--"
                    : row[column.property];
            }
        },
        //按钮操作权限
        canHandle (type) {
			return true;
            /* return this.allAuth.demo
                ? this.allAuth.demo.wf
                    ? this.allAuth.demo.wf[type]
                    : false
                : false; */
        },
        /**
         * 字段排序
         */
//      sortChange (column, prop, order) {
//          this.currentPage = 1;
//          this.sortData = column;
//          this.getList();
//      },
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
        updateTableHeight () {
            var offsetHei = document.documentElement.clientHeight;
            var removeHeight = this.selectionList.length > 0 ? 350 : 280;
            this.tableHeight = offsetHei - removeHeight;
        },
        /**
      * 查看点击
      */
        showDetails (id) {
            this.action.type = 'show';
            this.action.id = id;
            this.showDview = true
            // console.log(this.action);
        },


        /**
             * 模板自定义格式化列表
             */
        CustomFormat (datas, value) {
            // console.log("模板自定义格式化====", datas, value);
            var actions = [];
            Object.keys(datas).some(key => {
                if (datas[key].id == "" + value) {
                    actions.push(datas[key].workType);
                    return true;
                }
            });
            return actions.join("");
        },
        /**
           * 模板自定义格式化列表
           */
        CustomFormat2 (datas, value) {
            // console.log("模板自定义格式化====", datas, value);
            var actions = [];
            Object.keys(datas).some(key => {
                if (datas[key].id == "" + value) {
                    actions.push(datas[key].paperRulesName);
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
