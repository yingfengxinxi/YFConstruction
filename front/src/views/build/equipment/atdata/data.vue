<template>
  <div class="main">
    <xr-header
      icon-color="#33D08F"
      :label="'监测设备：'+ assetName"/>
    <el-button
      class="main-table-header-button"
      type="normal"
      icon="el-icon-back"
      @click="back">返回
    </el-button>
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true"
               style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="开始时间" prop="status">
          <el-date-picker
            v-model="queryParams.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择开始时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="status">
          <el-date-picker
            v-model="queryParams.endTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择结束时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">

        <el-table-column v-for="(item, index) in colsMap" :label="item.name" align="center" :prop="item.key"
                         :width="150" :key="index"/>
      </el-table>

      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
  </div>
</template>

<script>

  import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'

  import {getMDataByEqList, getType} from '../api/monitorData'

  export default {
    name: 'Data',
    components: {
      Reminder,
      XrHeader
    },
    data() {
      return {
        // 遮罩层
        loading: true,
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
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        // 字典表格数据
        dataList: [],
        // 默认字典类型
        defaultDictType: '',
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          startTime:null,
          endTime:null
        },
        colsMap: [],
        assetId: '',
        assetName: '',
      }
    },
    created() {
      this.assetId = this.$route.query.assetId;
      this.assetName = this.$route.query.assetName;

      this.getList();
    },
    methods: {
      /** 查询字典数据列表 */
      getList() {
        this.loading = true
        var params = {};
        params.page = this.currentPage;
        params.limit = this.pageSize;
        //设备ID
        params.equipmentId = this.assetId;
        //时间
        if (this.queryParams.startTime) {
          params.startTime = this.queryParams.startTime;
        }
        if (this.queryParams.endTime) {
          params.endTime = this.queryParams.endTime;
        }
        getMDataByEqList(params).then(response => {

          if(response.code == 0){
            var data = response.data;
            this.colsMap = data.colsMap;
            this.dataList = data.basePage.list;
            this.total = data.basePage.totalRow
          }else{
            this.$message.error(response.msg);
          }
          this.loading = false
        })
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.queryParams = {
          startTime:null,
            endTime:null
        };
        this.handleQuery()
      },
      back() {
        this.$router.go(-1)
      },
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getList()
      },
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      }
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

  .link-type, .link-type:focus {
    color: #337ab7;
    cursor: pointer
  }

  .link-type:focus:hover, .link-type:hover {
    color: #20a0ff
  }

  @import '../styles/table.scss';

</style>
