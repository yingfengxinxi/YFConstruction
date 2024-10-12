<template>
  <div class="employee-dep-management">
    <!-- <xr-header
      :show-search="false"
      icon-class="wk wk-s-seas"
      icon-color="#26D4DA"
      label="设备位置管理">

    </xr-header> -->
    <div class="system-content">
      <!-- 左边导航栏 -->
      <div
        v-loading="treeLoading"
        class="system-nav">
        <div class="system-nav__title">
          部门信息
        </div>
        <div class="system-nav__content">
          <div class="section">
            <div class="section__content">
              <el-tree
                ref="tree"
                :data="showTreeData"
                node-key="id"
                highlight-current
                default-expand-all
                :expand-on-click-node="false"
                @node-click="changeTreeClick">
                <flexbox
                  slot-scope="{ node }"
                  :class="{ 'is-current': node.isCurrent}"
                  class="node-data">
                  <i
                    v-if="node.level == 1"
                    class="wk wk-department" />
                  <span
                    v-else
                    class="node-data__mark" />

                  <div class="node-data__label text-one-line ">{{ node.label }}</div>
                  <i
                    v-if="node.childNodes && node.childNodes.length"
                    :class="{ 'is-close': !node.expanded }"
                    class="wk wk-up-unfold" @click="changeExpanded(node)"/>
                </flexbox>
              </el-tree>
            </div>
          </div>

        </div>
      </div>
      <!-- 右边内容 -->
      <div class="system-view-table flex-index" v-loading="formLoading">
        <!-- <flexbox
          justify="space-between"
          class="table-top">
          <div style="margin-right: 0px;margin-left: auto">
            <el-button
              class="main-table-header-button xr-btn--orange"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="add()" >新建</el-button>
          </div>
        </flexbox> -->

		<!-- 头部：搜索、操作 start -->
		<el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
		         style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
		  <el-form-item v-if="dateType === '3'" label="时间范围">
		    <el-date-picker
		    v-model="dateValue"
		    type="daterange"
		    value-format="yyyy-MM-dd"
		    range-separator="至"
		    start-placeholder="开始日期"
		    end-placeholder="结束日期">
		    </el-date-picker>
		  </el-form-item>
		  <el-form-item v-if="dateType === '1'" label="年份">
		    <el-date-picker
		    v-model="date"
		    type="year"
				value-format="yyyy-MM-dd"
				placeholder="选择年">
		    </el-date-picker>
		  </el-form-item>
			<el-form-item v-if="dateType === '2'" label="月份">
				<el-date-picker
				v-model="date"
				type="month"
				value-format="yyyy-MM-dd"
				placeholder="选择月">
				</el-date-picker>
			</el-form-item>
		  <el-form-item>
		    <el-button type="primary" icon="el-icon-search" size="mini" @click="getTableList">搜索</el-button>
		    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
		  </el-form-item>
		  <el-form-item style="float: right">
		    <div class="right-container">
		      <el-button type="primary" @click="bulkImportClick">导入</el-button>
		    </div>
		  </el-form-item>
		</el-form>

        <div class="flex-box">
          <el-table
            :data="list"
            :height="tableHeight"
            style="width: 100%" highlight-current-row>
            <el-table-column label="序号" fixed type="index" show-overflow-tooltip width="70" align="center">
            </el-table-column>
            <el-table-column
              fixed
              prop="assetName"
              label="设备名称"
              width="140"
              align="center">
            </el-table-column>

			<el-table-column
			  v-for="(item, index) in fieldList"
			  :key="index"
			  :label="item.label"
			  align="center">

				  <el-table-column
				  :key="index"
				  :prop="item.prop"
				  label="用量(m³)"
				  :width="item.width"
				  :formatter="fieldFormatter"
				  :sortable="false"
				  align="center"
				  show-overflow-tooltip>

				  </el-table-column>

			</el-table-column>
          </el-table>
        </div>
      </div>


    </div>

	<!-- 批量导入 -->
	<bulk-import-user
	  :show="bulkImportShow"
    :param="param"
	  @close="bulkImportClose"
	   />

  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'

  import {depListAPI} from '@/api/common' // 直属上级接口
  import {getList} from '../api/waterStatements'
  import BulkImportUser from '@/components/importModel'
  import {timeToFormatTime} from '@/utils'

export default {
  name: '',
  components: {
    XrHeader,
	BulkImportUser
  },
  data() {
    return {
	  tableHeight: document.documentElement.clientHeight - 240, // 表的高度
      treeLoading:false,
      showTreeData:[],
      formLoading:false,
      currTree:{},
	  list: [],
	  fieldList: [
		  /* {
		  	prop: "2021-06-17",
		  	label: "2021-06-17",
		  	//width: '100',
		  } */
	  ],
	  // 报表类型
	  dateType: '',
	  // 查询
	  queryParams: {},
		// 日期
		date: undefined,
		// 时间范围
		dateValue: '',
	  // 批量导入
    bulkImportShow: false,
    param:{}
    }
  },
  computed: {
  },
  mounted() {
    var self = this
    /** 控制table的高度 */
    window.onresize = function() {
      self.tableHeight = document.documentElement.clientHeight - 240
    }
	this.initData(this.$route)
  },
  watch:{
	 //监听页面切换
	 '$route'(to, from) {
	   //TODO 重新加载数据
	   this.initData(to)
	 }
  },
  methods: {
	// 初始化
	initData(to) {
		this.showTreeData = []
		this.fieldList = []
		this.list = []
		this.queryParams = {}
		if(to.path.indexOf('/waterStatementsYear')>0){
			this.date = timeToFormatTime(new Date())
			this.dateValue = ''
			this.dateType = '1'
		 this.$options.name='waterStatementsYear'
		} else if (to.path.indexOf('/waterStatementsMonth')>0) {
			this.date = timeToFormatTime(new Date())
			this.dateValue = ''
			this.dateType = '2'
			this.$options.name='waterStatementsMonth'
		} else if(to.path.indexOf('/waterStatementsTimeFrame')>0){
			this.date = undefined

			let startDate = timeToFormatTime(new Date(), 'yyyy-MM') + '-01'
			let endDate = timeToFormatTime(new Date())
			this.dateValue = [startDate, endDate]

			this.dateType = '3'
		 this.$options.name='waterStatementsTimeFrame'
		}
		this.getTreeList()
		this.getTableList()
	},
    //改变展开状态
    changeExpanded(node) {
      node.expanded ? node.expanded = false : node.expanded = true;
    },
    // 获取树形列表
    getTreeList() {
      this.treeLoading = true
      depListAPI({type: 'tree'})
        .then(response => {
          this.showTreeData = response.data || []
          this.treeLoading = false
        })
        .catch(() => {
          this.treeLoading = false
        })
    },
    /**
     * 选择部门
     */
    changeTreeClick(data, checked, indeterminate) {
      this.currTree = data;
	  this.getTableList()
    },

	getTableList() {
	  this.queryParams.dateType = this.dateType
	  this.queryParams.date = this.date
	  if(this.currTree.id != 0){
	    this.queryParams.deptId = this.currTree.id;
	  }
	  if (this.dateValue != null&&this.dateValue != '') {
	  	this.queryParams.startDate = this.dateValue[0]
	  	this.queryParams.endDate = this.dateValue[1]
	  }else{
	  	this.queryParams.startDate = ''
	  	this.queryParams.endDate = ''
	  }
	  this.tableLoading = true;
	  getList(this.queryParams)
	    .then(res => {
	      if (res.code == 0) {
			this.fieldList = []
			var cols = res.data.cols;
			cols.forEach(item => {
				var map = {
					prop: item,
					label: item,
					width: '100',
				}
				this.fieldList.push(map)
			})
	        this.list = res.data.tableList;
	      } else {
	        this.$message.error(data.msg);
	      }
	      this.tableLoading = false;
	    })
	    .catch(() => {
	      this.tableLoading = false;
	    })
	},
	//列格式化  如一些字典等
	fieldFormatter(row, column, cellValue) {
		/* if (column.property === 'monitorType') {
			return this.selectDictLabel(this.dictData.monitorTypeDict, row.monitorType)
		} */
		//console.log(row, column)
		return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
	},
	/** 重置按钮操作 */
	resetQuery() {
		this.queryParams = {}
		if (this.dateType == "1") {
			this.date = timeToFormatTime(new Date())
		} else if (this.dateType == "2") {
			this.date = timeToFormatTime(new Date())
		} else if (this.dateType == "3") {
			let startDate = timeToFormatTime(new Date(), 'yyyy-MM') + '-01'
			let endDate = timeToFormatTime(new Date())
			this.dateValue = [startDate, endDate]
		}
		this.getTableList()
	},
	/**
	* 批量导入
	*/
	bulkImportClick() {
    let lists = [
        "设备名称(*)",
        "记录日期(*)yyyy-MM-dd HH:mm:ss",
        "用水量(*) kWh"
      ]
      let colNameCodes = [
        "equipmentName",
        "recordDate",
        "waterConsumption"
      ]

      //示例数据
      let dataLists = [
        "智能压浆监测1",
        "2021-06-22 10:22:22",
        "100"
      ]



      this.param = {
        title:"设备用水量导入模板(*)为必填项",
        excelTitle:"设备用水量导入模板",
        colNameCodes:colNameCodes,
        colNames:lists,
        sampleData:dataLists,
        url:'build/equipmentWaterRecord/excelImport'
      }
		this.bulkImportShow = true
	},
	// 关闭导入组件
	bulkImportClose() {
	  this.bulkImportShow=false
	  this.getTableList()
	},
  }
}
</script>

<style lang="scss" scoped>
@import '@/views/login/index.scss';
.verify-picture {
  margin-top: 20px;
}

.sms-box {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: nowrap;
  .el-input {
    width: 210px;
  }
  .el-button {
    flex: 1;
    font-size: 12px;
    color: white;
    background-color: #3e6bea;
    border-radius: $xr-border-radius-base;
    border: 0 none;
    padding: 0;
    margin-left: 20px;

    .btn-content {
      width: 100%;
      height: 42px;
      @include center;
      .icon {
        font-size: 16px;
        margin-right: 5px;
      }
    }
    &:hover,
    &.is-disabled,
    &.is-disabled:hover {
      color: white;
      border-color: #517aec;
      background-color: #517aec;
    }
  }
}

.employee-dep-management {
  padding: 10px 15px;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.system-content {
  position: relative;
  height: 100%;
  flex: 1;
  display: flex;
  overflow: hidden;
}
.system-nav {
  width: 280px;
  height: 100%;
  overflow: auto;
  margin-right: 10px;
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;

  &__title {
    padding: 15px;
    font-size: 16px;
    font-weight: 600;
    border-bottom: 1px solid $xr-border-line-color;
  }

  &__content {
    overflow: auto;
    height: calc(100% - 50px);
    overflow-y: overlay;
    overflow-x: overlay;
  }
}
// 菜单
.section {
  position: relative;
  &__title {
    position: relative;
    font-size: 14px;
    font-weight: 600;
    color: #333;
    padding: 15px;
    .add-btn {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
}

.menu-item {
  position: relative;
  cursor: pointer;
  padding: 12px 15px;
  color: #333;

  &__icon {
    font-size: 14px;
    margin-right: 8px;
    color: #8a94a6;
  }

  &__content {
    font-size: 14px;
  }
}

.menu-item:hover,
.menu-item.is-select {
  background-color: $xr--background-color-base;
}

.menu-item:hover::before,
.menu-item.is-select::before {
  content: ' ';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}

.system-view-table {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  position: absolute;
  top: 0;
  left: 295px;
  bottom: 0;
  right: 0;
}

.table-top {
  padding: 0 30px;
  height: 50px;
  border-bottom: 1px solid #e6e6e6;
  &__title {
    font-size: 16px;
    color: #333;
  }

  .el-dropdown {
    margin-left: 7px;
  }
}

.all-user-reminder {
  width: auto;
  margin-left: 5px;
  display: inline-block;
}

.el-table /deep/ .el-table-column--selection .cell {
  padding-left: 14px;
}

// .status {
//   display: inline-block;
//   margin-left: 50px;
// }
// .status > span {
//   margin-right: 10px;
// }

.status-name {
  div {
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius: 3px;
  }
  color: $xr-color-primary;
  cursor: pointer;
}


/** 树形结构 */
.el-tree /deep/ .el-tree-node__expand-icon {
  display: none;
}
.el-tree /deep/ .el-tree-node__content {
  height: 40px;

  .node-data {
    // .node-img {
    //   width: 15px;
    //   height: 15px;
    //   display: block;
    //   margin-right: 8px;
    //   margin-left: 24px;
    // }
    height: 40px;
    padding: 0 15px;
    position: relative;
    border-radius: $xr-border-radius-base;

    .wk {
      font-size: 14px;
      color: #8a94a6;
      flex-shrink: 0;
    }

    .wk-department {
      margin-right: 8px;
    }

    &__mark {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #e6e6e6;
      margin-right: 8px;
      flex-shrink: 0;
    }

    &__label {
      flex: 1;
      color: #333;
      font-size: 14px;
      margin-right: 8px;
    }

    .wk-up-unfold {
      margin-left: 8px;
      transition: transform 0.3s;
    }

    .wk-up-unfold.is-close {
      transform: rotateZ(180deg);
    }
    // .node-label-set {
    //   display: none;
    // }
  }

  .node-data.is-current,
  .node-data:hover {
    background-color: $xr--background-color-base;
  }

  // .node-data:hover .node-label-set {
  //   display: block;
  // }
}
// .el-tree /deep/ .el-tree-node.is-current > .el-tree-node__content {
//   background-color: #ebf3ff;
//   border-right: 2px solid #46cdcf;
//   .node-label-set {
//     display: block;
//   }
// }
.system-nav /deep/ .el-tree-node > .el-tree-node__children {
  overflow: visible;
}
.system-nav /deep/ .el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block !important;
}

.system-nav
  /deep/
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background-color: white;
}

.system-nav /deep/ .el-tree-node__content:hover {
  background-color: white;
}

/* 搜索框图标按钮 */
.icon-search .el-icon-search {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  width: 40px;
  line-height: 40px;
  text-align: center;
  cursor: pointer;
  font-size: 20px;
  color: #ccc;
}
/* 设置flex布局 */
.flex-index {
  display: flex;
  flex-direction: column;
}
/* 设置占位 */
.flex-box {
  flex: 1;
  // border-bottom: 1px solid $xr-border-line-color;
}


</style>

