<template>
  <div class="main">

    <div class="main-body">
      <el-table  v-loading="loading" :data="dataList" >
        <el-table-column label="序号" align="center" type="index" width="60"/>
        <el-table-column label="项目名称" align="center" prop="projectName" sortable/>
        <el-table-column label="设备名称" align="center" prop="assetName" sortable/>
        <el-table-column label="设备状态" align="center" prop="online" width="120px" sortable>
          <template  slot-scope="scope">
               <span
                 v-if="scope.row.online == 0"
                 style="color:red;">离线</span>
              <span
                v-else-if="scope.row.online == 1"
                style="color:black;">在线</span>
            <span
              v-else style="color:black;">--</span>
          </template>
        </el-table-column>
        <el-table-column label="时间" align="center" prop="monitorTime" width="160px" sortable />
        <el-table-column label="β射线法PM10（ug/M3）" align="center" >
          <el-table-column label="浓度" align="center" prop="pm10" sortable >
            <template  slot-scope="scope">
               <span
                 v-if="Number(scope.row.pm10)>Number(PM10standard)"
                 style="color:red;">{{scope.row.pm10}}</span>
              <span
                v-else style="color:black;">{{scope.row.pm10}}</span>
            </template>
          </el-table-column>
          <el-table-column label="标准" align="center" sortable >
            <template slot-scope="scope">
              <span>{{PM10standard}}</span>
            </template>
          </el-table-column>
          <el-table-column label="超标倍数" align="center" sortable >
            <template  slot-scope="scope">
              <span style="color:red;">{{cbbs(scope.row)}}</span>
            </template>
          </el-table-column>
        </el-table-column>

      </el-table>
    </div>
  </div>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import { selectOverViewList } from '../api/projectEnvList'
import { mapGetters } from 'vuex'
import {sortColumnNoToLowerCase } from '@/utils'
//	import vPinyin from './vuepy.js';
export default {
  name: 'ProjectEnvTableList',
  components: {
    XrHeader
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      // table高度控制
      tableHeight: document.documentElement.clientHeight,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 分页
      currentPage: 1,
      rostercurrentPage: 1,
      pageSize: 10,
      rosterpageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      rostertotal: 0,
      // 表格数据
      dataList: [],
      timer:null, //定时器名称
      // 项目信息列表
      projectInfoList: [],
      queryParams:{}
    }
  },
  computed: {
    ...mapGetters(['PM10standard','pageDataReflushTime']),
  },
  created () {
    this.initData()
  },
  beforeDestroy(){
    clearInterval(this.timer);　　// 清除定时器
    this.timer = null;
  },
  methods: {
  	sortChange(column) {
      var list = []
      list.push("projectName");
      list.push("monitorTime");
      sortColumnNoToLowerCase(this,column,list);
    },
    initData() {
      this.getList()
      let _this=this
      this.timer = setInterval(
        function()  {
          _this.getList()
        }, this.pageDataReflushTime
      )
    },
    /** 查询数据列表 */
    getList() {
      this.loading = true
      selectOverViewList(this.queryParams).then(res => {
        this.dataList = res.data
        this.loading = false
        //console.log("555555"+JSON.stringify(this.dataList))
      })
    },
    // 超标倍数
    cbbs(row, column) {
      var cbbsData = ''
      var pm10Text =  Number(row.pm10)
      var PM10standard = Number(this.PM10standard)
      if (pm10Text > PM10standard && pm10Text != PM10standard) {
        var k = pm10Text - PM10standard
        cbbsData = k / PM10standard
        cbbsData = cbbsData.toFixed(2);
      }
      return cbbsData
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
</style>
