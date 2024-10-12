<template>
    <div>
        <!-- 头部：搜索、操作 start -->
        <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
            <el-form-item label="项目名称">
                <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目">
                    <el-option v-for="item in projects" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备状态">
                <el-select v-model="queryParams.online" clearable placeholder="请选择设备状态">
                    <el-option v-for="item in dictData.equiState" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备类型">
                <el-select v-model="queryParams.typeId" clearable placeholder="请选择设备类型">
                    <el-option v-for="item in dictData.equiTypeList" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
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
            <el-table :cell-style="cellStyle" v-loading="loading" id="crm-table" :data="list" :height="tableHeight" class="n-table--border" @sort-change="sortChange" use-virtual stripe border highlight-current-row style="width: 100%">
                <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed />
                <el-table-column v-for="(item, index) in fieldList" :key="index"  :fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" :align="item.align" show-overflow-tooltip>
									 <!--<template slot-scope="scope">
									 		<div v-if="item.prop == 'online'">11111</div>
									 </template>-->
                </el-table-column>
            </el-table>
            <div class="p-contianer">
                <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
        </div>
    </div>
</template>

<script>

import { mapGetters } from 'vuex'//权限
import Lockr from 'lockr'//分页
import {sortColumn } from '@/utils'
import { getEquiStateList, getProjectsList } from "../api/equiState"

export default {
    /** DEMO列表 */
    name: 'equiState',
    components: {
    },
    data () {
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

            fieldList: [//表头
				{
				    prop: "projectName",
				    label: "项目名称",
				},
                {
                    prop: "assetCode",
                    label: "设备编号",
                    // width: "180"
                },
                {
                    prop: "assetName",
                    label: "设备名称",
                },
                /* {
                    prop: "locationName",
                    label: "设备位置",
                }, */
                {
                    prop: "typeName",
                    label: "设备类型",
                    // width: "120",
                },
                {
                    prop: "online",
                    label: "设备状态",
                    // width: "100",
                },
                {
                    prop: "offlineDate",
                    label: "更新时间",
                    // width: "100",
                }
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
                // 设备类型
                equipmentOptions: [],
                // 监测项类型子项-环境监测
                monitorTypeDict_9: [],
                equiState: [{ 'dictValue': '0', 'dictLabel': '离线' }, { 'dictValue': '1', 'dictLabel': '在线' }],
                equiTypeList: [{ 'dictValue': '30', 'dictLabel': '视频监控' }, { 'dictValue': '40', 'dictLabel': '环境监测' }, { 'dictValue': '50', 'dictLabel': '喷淋设备' }]
            }
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
        /** 控制table的高度 */
        window.onresize = () => {
            this.updateTableHeight()
        }
        // // 获取字典数据
        this.initDicts()

        this.$route.query.typeId!=''&&this.$route.query.typeId!=undefined?
        this.$set(this.queryParams,"typeId",this.$route.query.typeId)
        :this.$set(this.queryParams,"typeId",'')

        this.$route.query.online!=''&&this.$route.query.online!=undefined?
        this.$set(this.queryParams,"online",this.$route.query.online)
        :this.$set(this.queryParams,"online",'')
  
        //获取列表数据
        this.getList()
    },
    methods: {
    	sortChange(column) {
      sortColumn(this,column);
    },
    		cellStyle({ row, column, rowIndex, columnIndex }){
    			if (row.online != 1 && columnIndex == 5) {
    				return {color: 'red'}
				  }
    		},
        /** 获取列表数据 */
        getList () {
            this.loading = true
            //分页
            this.queryParams.page = this.currentPage;
            this.queryParams.limit = this.pageSize;
           
            // if(this.queryParams.online==''||this.queryParams.online==undefined){
            //     this.queryParams.online='1'
            // }
            // if(this.queryParams.typeId==''||this.queryParams.typeId==undefined){
            //     this.queryParams.typeId='50'
            // }
             console.log(this.queryParams.online);
            getEquiStateList(this.queryParams)
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

        //列格式化  如一些字典等
        fieldFormatter (row, column, cellValue) {
            if (column.property === 'online') {
                if (row.online == "0") {
                    return "离线"
                } else if (row.online == "1") {
                    return "在线"
                } else {
                    return "--"
                }
            }
            return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
        },
        //按钮操作权限
        canHandle (type) {
            return true;
            //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
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
            this.queryParams = {};
            this.getList()
        },
        /**
         * 新建点击
         */
        createClick () {
            this.action.type = 'save';
            this.createShow = true
        },
        /**
         * 修改点击
         */
        updateClick (id) {
            this.action.type = 'update';
            this.action.id = id;
            this.createShow = true
        },
        deleteClick (id) {
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

        // 初始化字典数据
        initDicts () {
            // this.getDicts('b_equipment_statistical_type').then(res => {
            //     this.dictData.statisticalTypeDict = res.data
            // })
            // this.getDicts('b_equipment_monitor_type').then(res => {
            //     this.dictData.monitorTypeDict = res.data
            // })
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
        handleSelectionChange (val) {
            this.selectionList = val // 勾选的行
            this.updateTableHeight();
        },
        /**
         * 更新表高
         */
        updateTableHeight () {
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

@import "@/views/styles/table.scss";
</style>
