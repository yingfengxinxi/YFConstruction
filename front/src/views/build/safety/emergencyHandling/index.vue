<template>
  <div>
<!--    <div class="c-container">-->
<!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
<!--        :src="titleIcon"-->
<!--        class="title-icon">应急事件处置-->
<!--      </flexbox>-->
<!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="事故编号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入事故编号"
          clearable
          size="small"
          style="width:160px;"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item  label="事故类型" class='special' prop="accidentType" >
        <el-select
                v-model="queryParams.accidentType"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 160px">
          <el-option
                  v-for="dict in accidentTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue" />
        </el-select>
      </el-form-item>

        <el-date-picker
        class="timecom"
        v-model="happenedTime"
        type="datetimerange"
        :picker-options="pickerOptions"
        range-separator="至"
        start-placeholder="事故发生时间启"
        end-placeholder="事故发生时间止"
        align="right"
        value-format="yyyy-MM-dd HH:mm:ss">
      </el-date-picker>

      <el-form-item style="float: right">
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
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%"

        @sort-change="sortChange"
        @selection-change="handleSelectionChange">
        <el-table-column
          show-overflow-tooltip
          type="selection"
          align="center"
          width="55"/>
           <el-table-column fixed label="序号"  align='center' type="index"  width="60"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          align="center"
          :formatter="fieldFormatter"
          sortable="custom"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="250px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-document"
              @click="showDetails(scope.row.id)"
            >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="deleteClick(scope.row.id)"
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
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
    <!-- 相关详情页面 -->
    <!-- <detail
      v-if="showDview"
      :id="rowID"
      @close="showDview = false"/> -->
    <!-- 新建 -->
    <create
      v-if="createShow"
      :action="action"
      @close="createShow = false"
      @save-success="getList"
    />
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页

  import Create from './Create'
  // import Detail from './Details'

  import {getList, delTrueByIds, selectEids} from "../api/emergencyHandling";
  import { sortColumn } from '@/utils/index';

  export default {
    /** DEMO列表 */
    name: 'emergencyHandling',
    components: {
      Create,
      // Detail
    },
    data() {
      return {
        loading: false, // 加载动画
        /** 选中时操作 */
        selectionHandleItems: [
          {
            name: '删除',
            type: 'delete',
            icon: 'delete'
          }
          // ,
          // {
          //   name: '导出选中',
          //   type: 'export',
          //   icon: 'export'
          // }
        ],
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        accidentTypeOptions : [],//事故类型
        handleTypeOptions : [],//处置类型
        eIdsOptions:[],//关联应急预案
        fieldList: [//表头
          {
            prop: "code",
            label: "事故编号",
            width: "140"
          },
          {
            prop: "happenedTime",
            label: "事故发生时间",
            width: "200"
          },
          {
            prop: "accidentType",
            label: "事故类型",
            width: "160"
          },
          {
            prop: "happenedAddress",
            label: "事故发生地点",
            width: "220"
          },
          {
            prop: "deathNumber",
            label: "死亡人数",
            width: "120"
          },
          {
            prop: "seriousInjuryNumber",
            label: "重伤人数",
            width: "120"
          },
          {
            prop: "eid",
            label: "关联应急预案",
            width: "140"
          },
          {
            prop: "accidentNote",
            label: "事故描述",
            // width: "220"
          },
        ],
        sortData: [], // 字段排序
        currentPage: 1,
        pageSize: Lockr.get('crmPageSizes') || 15,
        pageSizes: [15, 30, 60, 100],
        total: 0,
        queryParams:{},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于删除、全局导出
        /** 控制详情展示 */
        rowID: '', // 行信息
        // showDview: false,

         pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        happenedTime:''//事故发生时间

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
      //获取列表数据
      this.getList();
    },
    watch:{},
    created(){
      //事故类型
      this.getDicts('b_safety_accident_Type').then(response => {
        this.accidentTypeOptions = response.data
      })

      //处置类型
      this.getDicts('b_safety_handle_type').then(response => {
        this.handleTypeOptions = response.data
      })

      //关联应急预案
      this.selectEids()

    },
    methods: {
      //关联应急预案
      selectEids(){
        selectEids().then(res =>{
          this.eIdsOptions = res.data
        })
      },
      /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;

        if (this.happenedTime != null&&this.happenedTime != '') {
            this.queryParams.happenedTimeStart = this.happenedTime[0]
            this.queryParams.happenedTimeEnd = this.happenedTime[1]
        }else{
        	this.queryParams.happenedTimeStart = ''
            this.queryParams.happenedTimeEnd = ''
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
      /**
       *更多操作点击
       */
      handleTypeDrop(command) {
        // console.log(command)
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

      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        //事故类型
        if(column.property == 'accidentType'){
          return this.selectDictLabel(this.accidentTypeOptions, row.accidentType)
        }
        //处置类型
        if(column.property == 'handleType'){
          return this.selectDictLabel(this.handleTypeOptions, row.handleType)
        }

        //关联应急预案
        if(column.property == 'eid'){
          return this.selectEidDictLabel(this.eIdsOptions, row.eid)
        }

        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      // 关联应急预案  回显数据字典
      selectEidDictLabel(datas, value) {
        var actions = []
        Object.keys(datas).some((key) => {
          if (datas[key].id == ('' + value)) {
            actions.push(datas[key].name)
            return true
          }
        })
        return actions.join('')
      },

      //按钮操作权限
      canHandle(type) {
        return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      sortChange(column) {
        if(column.prop == 'eid') {
          column.prop = 'e_id'
        }
        sortColumn(this,column);
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
        this.happenedTime = ''
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
       * 修改点击
       */
      updateClick(id) {
        this.action.type = 'update';
        this.action.id = id;
        this.createShow = true
      },
       /**
       * 查看点击
       */
      showDetails(id){
          this.action.type = 'show';
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

  .timecom{
		margin-top: 4px;
		height: 32px;
	}
	.timecom span{
		width: 20px;
	}

  @import '../../../admin/styles/table.scss';
</style>
