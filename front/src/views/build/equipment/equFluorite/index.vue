<template>
  <div>
    <!--    <div class="c-container">-->
    <!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
    <!--        :src="titleIcon"-->
    <!--        class="title-icon">方案变更管理-->
    <!--      </flexbox>-->
    <!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="项目名称">
        <el-select v-model="queryParams.projectId" clearable placeholder="请选择项目名称" filterable>
          <el-option v-for="item in projectList" :key="item.id" :label="item.name" :value="item.id"  >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称">
        <el-input v-model="queryParams.assetName" placeholder="请输入设备名称" clearable size="small" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item label="设备编号">
        <el-input v-model="queryParams.deviceChannelId" placeholder="请输入萤石设备编号" clearable size="small" @keyup.enter.native="getList" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button icon="el-icon-s-operation" size="mini" @click="synchronous">同步</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <el-button type="primary" @click="bulkImportClick">导入</el-button>
          <!-- <el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary" @click="createClick">新建 -->
          <!-- </el-button> -->
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
      <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change="sortChange">
        <el-table-column show-overflow-tooltip type="selection" align="center" width="55" />
        <el-table-column label="序号" type="index" show-overflow-tooltip width="50" fixed />
        <el-table-column v-for="(item, index) in fieldList" :key="index" :fixed="index==0" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" :align="item.align" show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="updateClick(scope.row.assetId)">修改
            </el-button>
            <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteClick(scope.row.id)">删除 -->
            <!-- </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
    </div>
    <!-- 新建 -->
    <create v-if="createShow" :businessType="businessType" :dictData="dictData" :action="action" @close="createShow = false" @save-success="getList" />
    <!-- 批量导入 -->
    <bulk-import-user :show="bulkImportShow" :param="param" @close="bulkImportShow=false" />
  </div>
</template>

<script>
import BulkImportUser from '@/components/importModel'
import { getList, getProjectList } from "../api/equFluorite"



import { mapGetters } from 'vuex'//权限
import Lockr from 'lockr'//分页
import Create from './Create'
import { delTrueByIds,updateStatusByProject } from "../api/monitorPoint"
import { selectEquOptions } from '../api/assets'
import { sortColumn } from '@/utils/index';
export default {
  /** DEMO列表 */
  name: 'monitorPoint',
  components: {
    Create,
    BulkImportUser
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
      list: [],
      fieldList: [//表头
        {
          prop: "projectName",
          label: "项目名称",
          width: "250",
          align: 'center'
        },
        {
          prop: "assetCode",
          label: "设备编号",
          width: "200",
          align: 'center'
        },
        {
          prop: "assetName",
          label: "设备名称",
          width: "250",
          align: 'center'
        },
        {
          prop: "deviceChannelId",
          label: "萤石设备编号",
          width: "200",
          align: 'center'
        },
        {
          prop: "deviceSerial",
          label: "萤石设备序列号",
          width: "360",
          align: 'center'
        },
        {
          prop: "status",
          label: "设备状态",
          width: "140",
          align: 'center'
        }
        ,
        {
          prop: "hookCheckBox",
          label: "是否吊钩",
          width: "140",
          align: 'center'
        }
        ,
        {
          prop: "ptzCheckBox",
          label: "云台控制",
          width: "140",
          align: 'center'
        }
      ],
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 40, 80, 100],
      total: 0,
      queryParams: {},
      projectList: [],
      // 批量导入
      bulkImportShow: false,
      param: {},




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
        //塔机监测子项
        equipmentStatisticalTypeTj: [],

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
    // 获取字典数据
    this.initDicts()
    // 获取设备数据
    this.getEquOptions()
    //获取列表数据
    this.getList()
  },
  methods: {
    synchronous(){
      if(!this.queryParams.projectId){
        this.$message.error("请选择项目");
        return;
      }
      this.$confirm("确定同步选中项目的状态吗？", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.loading = true;
          updateStatusByProject(this.queryParams.projectId).then(res => {
            if (res.code == 0) {
              this.$message.success("同步成功");
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
    /** 获取列表数据 */
    getList () {
      this.loading = true
      //分页
      this.queryParams.page = this.currentPage;
      this.queryParams.limit = this.pageSize;

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

    /**
     * 批量导入
     */
    bulkImportClick () {
      let lists = [
        "设备编号(*)",
        "设备名称(*)",
        "设备品牌(*)",
        "设备型号(*)",
        "萤石IPC序列号(*)",
        "萤石IPC license(*)",
        "萤石NVR序列号(*)",
        "萤石NVR license(*)",
        "项目id(*)"
      ]
      //示例数据
      let dataLists = [
        // "371422-001-000001-301",
        "371422-001-000001-301",
        "测试设备",
        "海康",
        "DS-8616N-K8",
        "33011051991327162332",
        "0EEF7HXTVLUK",
        "33010958991117306641",
        "8V7PQWOUKF7H",
        '161'
      ]

      this.param = {
        title: "萤石国标设备导入模板(*)为必填项 ",
        excelTitle: "萤石国标设备导入模板",
        colNames: lists,
        sampleData: dataLists,
        url: 'build/equipmentFluoriteCloud/excelImport'
      }
      this.bulkImportShow = true
    },

    /**
     *更多操作点击
     */
    handleTypeDrop (command) {
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
      }
    },
    //列格式化  如一些字典等
    fieldFormatter (row, column, cellValue) {
      if (column.property === 'status') {

        if (row.status == '1') {
          return "在线"
        } else {
          return "离线"
        }
        // return this.selectDictLabel(this.dictData.monitorTypeDict, row.monitorType)
      } else if (column.property === 'hookCheckBox') {
        if (row.hookCheckBox == '1') {
          return "是"
        } else {
          return "否"
        }
        // return this.selectDictLabel(this.dictData.statisticalTypeDict, row.statisticalType)
      } else if (column.property === 'ptzCheckBox') {
        if (row.ptzCheckBox == '1') {
          return "是"
        } else {
          return "否"
        }
      }
      return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
    },
    //按钮操作权限
    canHandle (type) {
      return true;
      //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
    },
    /** 勾选操作 */
    // 当选择项发生变化时会触发该事件
    handleSelectionChange (val) {
      this.selectionList = val // 勾选的行
      this.updateTableHeight();
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
    /**
     * 更新表高
     */
    updateTableHeight () {
      var offsetHei = document.documentElement.clientHeight
      var removeHeight = this.selectionList.length > 0 ? 370 : 260
      this.tableHeight = offsetHei - removeHeight
    },
    // 初始化字典数据
    initDicts () {

      getProjectList().then(res => {
        this.projectList = res.data;
      })
        .catch(() => {

        })



      // this.getDicts('b_equipment_statistical_type').then(res => {
      //   this.dictData.statisticalTypeDict = res.data
      // })
      // this.getDicts('b_equipment_monitor_type').then(res => {
      //   this.dictData.monitorTypeDict = res.data
      // })
      // this.getDicts('b_equipment_monitor_type_2').then(res => {
      //   this.dictData.equipmentStatisticalTypeTj = res.data
      // })
    },
    // 查询设备下拉框数据
    getEquOptions () {
      selectEquOptions().then(res => {
        this.dictData.equipmentOptions = res.data
      })
    },
    sortChange(column) {
      sortColumn(this,column);
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
