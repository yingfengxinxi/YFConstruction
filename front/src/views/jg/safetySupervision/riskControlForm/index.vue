<template>
    <div class="main">
        <div class="main-body">
            <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
                <el-form-item label="分部分项" prop="subItemWorksParts">
                    <el-select v-model="queryParams.subItemWorksParts" placeholder="请选择" clearable size="small" style="width: 160px">
                        <el-option v-for="dict in subItemWorksPartsOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="项目" prop="projectId">
                       <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small" style="width: 160px">
                        <el-option v-for="dict in projectNameList" :key="dict.id" :label="dict.name" :value="dict.id" />
                    </el-select>
                </el-form-item>
                <el-form-item style="float: right">
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                    <!-- <el-button type="primary" icon="el-icon-printer" size="mini" @click="exportFile(1)">导出</el-button> -->
                </el-form-item>
            </el-form>
            <el-table v-loading="loading" id="crm-table" :span-method="objectSpanMethod" :data="dataList" :height="tableHeight" :cell-class-name="cellClassName" :header-cell-class-name="headerCellClassName" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @row-click="handleRowClick" @sort-change="sortChange">
                <el-table-column label="序号" align='center' type="index" width="60" />
                <el-table-column label="分部分项" :formatter="SubItemWorksPartsFc" align='center' prop="subItemWorksParts" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="风险类型" align='center' prop="riskType" show-overflow-tooltip sortable="custom"/>
                <el-table-column align='center' label="风险分级/标识" >
                    <el-table-column align='center' prop="red" label="红" width="120" sortable="custom"/>
                    <el-table-column align='center' prop="orange" label="橙" width="120" sortable="custom"/>
                    <el-table-column align='center' prop="yellow" label="黄" width="120" sortable="custom"/>
                    <el-table-column align='center' prop="blue" label="蓝" width="120" sortable="custom"/>
                </el-table-column>
                 <el-table-column align='center' label="管控等级">
                    <el-table-column align='center' prop="one" label="一级" width="120" sortable="custom"/>
                    <el-table-column align='center' prop="two" label="二级" width="120" sortable="custom"/>
                    <el-table-column align='center' prop="three" label="三级" width="120" sortable="custom"/>
                </el-table-column>
            </el-table>
            <div class="p-contianer">
                <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
        </div>
    </div>
</template>

<script>

import request from '@/utils/request'
import mergeList from '@/views/jg/safetySupervision/riskControl/mergeList2';
//导出
import { exportFile } from "@/api/build/exportFile";
import { downloadExcelWithResData ,sortColumn } from '@/utils/index';
export default {
    name: 'riskControlForm',
    components: {
    },
    mixins: [mergeList],
    data () {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 分页
            currentPage: 1,
            pageSize: 10,
            pageSizes: [10, 20, 30, 40],
            total: 0,
            // 表格数据
            dataList: [],
            // 查询参数
            queryParams: {
                page: this.currentPage,
                limit: this.pageSize,
            },
            //新--------------
            subItemWorksPartsOption: [],
            tableHeight: document.documentElement.clientHeight - 250,
            sortData: [], // 字段排序
            projectNameList:[]
        }
    },

    created () {
        this.getList()
        //分部分项
        this.getDicts('b_sub_item_works_parts').then(response => {
            this.subItemWorksPartsOption = response.data
        })
        request({
                url: 'build/projectInfo/queryProjectList',
                method: 'post',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                console.log(response)
                this.projectNameList=response.data;
            }).catch(() => {

            })
    },
    methods: {

        /** 查询数据列表 */
        async  getList () {
            this.loading = true
            this.queryParams.page = this.currentPage
            this.queryParams.limit = this.pageSize
            console.log(this.queryParams)
            await request({
                url: 'build/bSafetyRiskControls/getRiskControlForm',
                method: 'post',
                data: this.queryParams,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                console.log(response)
                this.dataList = response.data.list
                this.total = response.data.totalRow
                this.dataList.forEach((data, index) => {
                    data.id = String(data.id)
                })

                this.getListDataForRowAndColumn(this.dataList)
                this.loading = false
            }).catch(() => {
                this.loading = false
            })
        },
        // 分部分项字典格式化
        SubItemWorksPartsFc (row, column) {
            return this.selectDictLabel(this.subItemWorksPartsOption, row.subItemWorksParts);
        },
        formatterTime (row, column) {
            let data = row[column.property]
            if (data == null || data == '') return "--";
            return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
        },
        /** 搜索按钮操作 */
        handleQuery () {
            this.queryParams.page = 1
            this.queryParams.limit = this.pageSize
            this.getList()
        },

        /** 重置按钮操作 */
        resetQuery () {
            //this.resetForm('queryForm')
            this.queryParams = {};
            this.handleQuery()
        },
        // 更改每页展示数量
        handleSizeChange (val) {
            this.pageSize = val
            this.getList()
        },
        // 更改当前页数
        handleCurrentChange (val) {
            this.currentPage = val
            this.getList()
        },

        /**
         *更多操作点击
        */
        handleTypeDrop (command) {
            if (command == 'delete') {//删除

            } else if (command == 'export') {//导出

            } else if (command == 'import') {//导入

            }
        },

        canHandle (type) {
            return true;
        },
        /**
               * 通过回调控制class
               */
        cellClassName ({ row, column, rowIndex, columnIndex }) {
            if (column.property === 'code') {
                return 'can-visit--underline can-visit--bold'
            }
        },
        headerCellClassName ({ row, column, rowIndex, columnIndex }) {
            return ''
        },
        // 当某一行被点击时会触发该事件
        handleRowClick (row, column, event) {
            if (column.property == 'code') {
                this.rowID = row.id;
                this.showDview = true;
            }
        },
        /**
               * 字段排序
               */
/*        sortChange (column, prop, order) {
            this.currentPage = 1
            this.sortData = column
            this.getList()
        },*/
        sortChange(column) {
          sortColumn(this,column);
        },
        /**
         *
         * 使用导出需引入js文件如下：
         * import { exportFile } from "@/api/build/exportFile";
         * import { downloadExcelWithResData } from '@/utils/index';
         * 方法入参说明： type=1 全部导出  type=2 部分导出
         *
         */
        async exportFile (type) {
            this.loading = true;
            let data = '';
            if (type == 1) {
                //请求不分页的列表数据 需替换为当前页面列表数据请求url
                var objString = JSON.stringify(this.queryParams);
                var obj2 = JSON.parse(objString);
                let reqParam = obj2;
                reqParam.pageType = 0
                await request({
                    url: 'build/safetyRiskControlsTemplate/templateList',
                    method: 'post',
                    data: reqParam,
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    }
                }).then(res => {
                    data = res.data.list
                }).catch(() => {
                    this.loading = false
                })

            } else if (type == 2) {
                //获取当前选中行的数据
                // data = this.selectionList;
            }
            /**
             * 整理数据
             * param中result是需要导出的数据(必填)
             * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)
             * 如果有字典需转换请添加dictType类型，
             * 如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)，
             * dictList中字段名必须严格设置为dictLabel，dictValue，如果不是，请处理
             * param中title是导出Excel的一级表头(必填)
             * 注意：fieldList中字段值必须和列表中字段值相对应！！！
             */
            let fieldList = [ //表头
                { prop: "subItemWorksParts", label: "分部分项", dictType: 'b_sub_item_works_parts' },
                { prop: "riskType", label: "风险类型" },
                { prop: "riskIdentPoint", label: "风险辨识点" },
                { prop: "accidentType", label: "事故类型" },
                { prop: "riskLevelIdent", label: "风险分级/标识", dictType: "b_risk_level_ident" },
                { prop: "preventiveMeasures", label: "防范措施" },
            ]
            let param = {};
            param.result = data;
            param.fieldList = fieldList;
            param.title = "安全风险管控模板";
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
        },
    }
}
</script>


<style lang="scss" scoped>
.main {
  height: 100%;
}
/deep/ .xr-header {
  padding: 15px 30px;
}

.main-body {
  background-color: white;
  /*border-top: 1px solid $xr-border-line-color;*/
  border-bottom: 1px solid $xr-border-line-color;
}

.main-table-header-button {
  float: right;
  margin-right: 20px;
  margin-top: 10px;
}

.add-form::after {
  content: "";
  display: block;
  clear: both;
}

.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;
}

.link-type:focus:hover,
.link-type:hover {
  color: #20a0ff;
}

.el-table {
  border-top: 1px solid $xr-border-line-color;
}
/deep/ .special .el-form-item__label {
  width: 100px !important;
}
.timecom {
  margin-top: 4px;
  height: 32px;
}
.timecom span {
  width: 20px;
}

.table-head-container {
  //border-bottom: 1px solid #e6e6e6;
  border-top: 1px solid #e6e6e6;
  background-color: white;
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
</style>
