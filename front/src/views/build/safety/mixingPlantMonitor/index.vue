<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="拌合站名称" prop="mixingPlantName">
        <el-input
          v-model="queryParams.mixingPlantName"
          placeholder="请输入拌合站名称"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
         <el-form-item label="砼强度等级" prop="concreteGrade"  >
              <el-select
                    v-model="queryParams.concreteGrade"
                    placeholder="请选择砼强度等级"
                    clearable
                    size="small"
                    style="width: 150px">
              <el-option
                      v-for="dict in strengGrade"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
            </el-form-item>
      <el-form-item label="报警级别" prop="alarmLevel"  >
              <el-select
                    v-model="queryParams.alarmLevel"
                    placeholder="请选择报警级别"
                    clearable
                    size="small"
                    style="width: 150px">
              <el-option
                      v-for="dict in AlarmLevel"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
            </el-form-item>
       <el-form-item label="报警时间" prop="alarmTime" >
     <el-date-picker
        class="timecom"
        v-model="alarmTime"
        type="datetimerange"
        :picker-options="pickerOptions"
        range-separator="至"
        start-placeholder="报警时间启"
        end-placeholder="报警时间止"
        align="right"
        value-format="yyyy-MM-dd HH:mm:ss">
      </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <div class="right-container">
          <!-- <el-button
            v-if="canHandle('add')"
            class="xr-btn--orange"
            icon="el-icon-plus"
            type="primary"
            @click="createClick">新建
          </el-button> -->
		  <el-button
		    type="primary"
		    @click="excelImport">导入
		  </el-button>
          <!-- <el-dropdown
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
          </el-dropdown> -->
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
          <el-table-column label="序号" type="index" show-overflow-tooltip width="70" fixed />
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          align="center"
          show-overflow-tooltip>

          <!--审批start-->
          <template slot-scope="scope">
            <template v-if="item.prop == 'checkStatus'">
              <span :style="getStatusStyle(scope.row.checkStatus)" class="status-mark"/>
              <span>{{ getStatusName(scope.row.checkStatus) }}</span>
            </template>
            <template v-else>
              {{ fieldFormatter(scope.row, scope.column) }}
            </template>
          </template>
          <!--审批end-->

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
			  v-if="scope.row.handleUserId == null"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="updateClick(scope.row.id)"
            >处理
            </el-button>
            <el-button
			  v-if="scope.row.handleUserId != null"
              size="mini"
              type="text"
              icon="el-icon-document"
              @click="showDetails(scope.row.id)"
            >查看
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
    <detail
      v-if="showDview"
      :id="rowID"
       :action="action"
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
     <!-- 批量导入 -->
		<bulk-import-user
			:show="bulkImportShow"
			:param="param"
			@close="bulkImportClose"
			/>
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import CheckStatusMixin from '@/mixins/CheckStatusMixin'//审核状态

  import Create from './Create'
  import Detail from './Detail'

  import {getList,delTrueByIds} from "../api/mixingPlantMonitor";
  import BulkImportUser from '@/components/importModel'
  import { sortColumn } from '@/utils/index';

  export default {
    /** DEMO列表 */
    name: 'mixingPlantMonitor',
    components: {
      Create,
      Detail,
      BulkImportUser
    },
    mixins: [CheckStatusMixin],
    data() {
      return {
        businessType: 10001,
        loading: false, // 加载动画
        /** 页面头部 */
        inputContent: '',//查询值
        moreTypes: [
          {type: 'import', name: '导入', icon: 'import'},
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
            prop: "mixingPlantName",
            label: "拌合站名称",
            // width: "180"
          },
          {
            prop: "concreteGrade",
            label: "砼强度等级",
            // width: "280"
          },
          {
            prop: "unitWorks",
            label: "单位工程",
            // width: "295"
          },
          {
            prop: "proLocation",
            label: "工程部位",
            width: "170"
          },
          // {
          //   prop: "laboratoryMix",
          //   label: "试验室配合比",
          //   width: "230"
          // },
          // {
          //   prop: "constructionMix",
          //   label: "施工配合比",
          //   width: "150"
          // },
          {
            prop: "materialName",
            label: "材料名称",
            width: "160"
          },
          {
            prop: "materialWeight",
            label: "材料标准重量",
            width: "160"
          },
          {
            prop: "actualWeight",
            label: "实际重量",
            width: "160"
          },
          // {
          //   prop: "allowableError",
          //   label: "允许误差",
          //   width: "160"
          // },{
          //   prop: "actualError",
          //   label: "实际误差",
          //   width: "160"
          // },
          {
            prop: "alarmLevel",
            label: "报警级别",
            // width: "160"
          },{
            prop: "alarmTime",
            label: "报警时间",
            // width: "160"
          }
        ],
        sortData: [], // 字段排序
        currentPage: 1,
        pageSize: Lockr.get('crmPageSizes') || 10,
        pageSizes: [10,20,30,40,50, 100],
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
        sceneId: '', // 场景筛选ID
        sceneName: '', // 场景名字

        strengGrade:[],
        AlarmLevel:[
			{
				dictLabel:'预警',
				dictValue:'1'
			},
			{
				dictLabel:'报警',
				dictValue:'2'
			}
		],
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
         alarmTime:'',//事故发生时间
         //导入
        bulkImportShow:false,
        param:{}
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
      //获取砼强度等级
      this.getDicts('b_safety_streng_Grade').then(response => {
      this.strengGrade = response.data
    })


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
        //场景ID
        if (this.sceneId) {
          this.queryParams.sceneId = this.sceneId
        }
        if (this.filterObj && this.filterObj.length > 0) {
          this.queryParams.searchList = this.filterObj
        }

          if (this.alarmTime != null&&this.alarmTime != '') {
          this.queryParams.alarmTimeStart=this.alarmTime[0];
         this.queryParams.alarmTimeEnd=this.alarmTime[1];
          }else{
          	this.queryParams.alarmTimeStart='';
         this.queryParams.alarmTimeEnd='';
          }
        // console.log("查询条件",this.queryParams)
        getList(this.queryParams)
          .then(res => {
            // console.log("数据返回",res)
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
		this.excelImport();
        }
      },
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
        // if (column.property == 'code') {
        //   this.rowID = row.id;
        //   this.showDview = true;
        // }
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        // console.log(row, column)

        // return  row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
        if(column.property=="concreteGrade"){
          return this.selectDictLabel(this.strengGrade, row.concreteGrade)
        } else if(column.property=="alarmLevel"){
          return this.selectDictLabel(this.AlarmLevel, row.alarmLevel)
        }else{
          return  row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
        }

      },
      //按钮操作权限
      canHandle(type) {
				return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      sortChange(column) {
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
        this.alarmTime='';
        this.queryParams = {};
        this.getList()
      },
        //导入
			excelImport(){
        let lists = [
          "拌合站名称(*)",
          "砼强度等级(*)",
          "单位工程(*)",
          "工程部位(*)",
          "试验室配合比(*)",
          "施工配合比(*)",
          "材料名称(*)",
          "材料标准重量(*)",
          "实际重量(*)",
          "允许误差(*)",
          "实际误差(*)",
          "报警级别(*)（预警：1；报警：2；）",
          "报警时间(*)"
        ]

        //示例数据
        let dataLists = [
          "测试2",
          "C15",
          "一号地",
          "MK-14",
          "1",
          "1",
          "水泥",
          "0.9",
          "1",
          "±1",
          "±0.5",
          "1",
          "2020-05-11 10:22:22"
        ]

        this.param = {
          title:"拌合站报警导入模板(*)为必填项",
          excelTitle:"拌合站报警导入模板",
          colNames:lists,
          sampleData:dataLists,
          url:'build/safetyMixPlant/excelImport'
        }
				this.bulkImportShow = true
			},
			// 关闭导入组件
			bulkImportClose() {
				this.bulkImportShow=false
				this.getList()
			},
      /**
       * 新建点击
       */
      createClick() {
        this.action.type = 'save';
        this.createShow = true;
         this.action.AlarmLevel=this.AlarmLevel;
      this.action.strengGrade=this.strengGrade
      },
      /**
       * 修改点击
       */
      updateClick(id) {
        this.action.type = 'update';
        this.action.id = id;
        this.createShow = true
         this.action.AlarmLevel=this.AlarmLevel;
      this.action.strengGrade=this.strengGrade;
      },
      deleteClick(id) {
        // console.log(id)
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
      showDetails(id){
        this.action.type = 'show';
        this.action.id = id;
        this.showDview = true
        this.action.AlarmLevel=this.AlarmLevel;
		this.action.strengGrade=this.strengGrade;
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
