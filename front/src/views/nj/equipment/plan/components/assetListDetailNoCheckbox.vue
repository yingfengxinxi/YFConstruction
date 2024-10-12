<template>
  <div class="main">
    <div class="main-body">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" label="序号" type="index" show-overflow-tooltip width="70">
        </el-table-column>
        <el-table-column
          prop="assetName"
          label="资产名称"
          width="200">
        </el-table-column>
        <el-table-column
          prop="assetCode"
          label="资产编号"
        >
        </el-table-column>
        <el-table-column
          prop="locationName"
          label="资产位置"
        >
        </el-table-column>
        <el-table-column
          prop="level"
          label="重要性"
          :formatter="levelFormat"
        >
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

  import {
    planAssetList,
  } from '../../api/planAsset'

  export default {
    name: 'asset',

    props: {
      planId: {
        require: true,
        type: Number,
        default: -1
      },
      showCheckbox:{
        require: false,
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        tableData: [],
        multipleSelection:[],
        // 分页
        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          status: '1'//正常
        },
        currentPage: 1,
        pageSize: 9999,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        levels: [],
        // 是否显示弹出层
        open: false,
        //
        currAsset: {}
      }
    },
    created() {
      this.getList()
      this.getDicts('equ_assets_level').then(response => {
        this.levels = response.data
      })
    },
    watch: {
      planId() {
        this.getList();
      },
      tableData(){
        this.$emit('change',this.tableData)
      }
    },
    methods: {
      /** 多选 */
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      /** 选择设备 */
      change(row) {
        this.currAsset = row;
      },
      changeAsset() {
        if(!this.currAsset.assetId){
          this.$message({
            type: 'info',
            message: '未选择任何设备'
          });
          return;
        }
        this.tableData.push(this.currAsset)
        this.open = false;
      },
      add() {
        this.open = true;
      },
      del(){
        if(this.multipleSelection.length < 1){
          this.$message({
            type: 'info',
            message: '未选择任何数据项'
          });
          return;
        }
        this.$confirm('是否移除选中的设备信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var that = this;
          var empArr = [];
          this.tableData.forEach(function(row) {
            console.log(row)
            console.log(that.multipleSelection)
            that.multipleSelection.forEach(function(data) {
              console.log(data)
              if(row.assetId != data.assetId){
                empArr.push(row);
              }
            });
          });

          this.tableData = empArr;

        }).catch(() => {
        });
      },
      cancel() {
        this.currAsset = {};
        this.open = false;
      },
      /** 查询字典类型列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize;
        this.queryParams.planId = this.planId;
        planAssetList(this.queryParams).then(response => {
          this.tableData = response.data.list
          this.total = response.data.totalRow
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      //字典翻译
      levelFormat(row, column) {
        return this.selectDictLabel(this.levels, row.level)
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
    border-top: 1px solid $xr-border-line-color;
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

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
    display: none;
  }

  .new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
    padding-left: 10px;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
    border-bottom: 1px solid #e4e7ed;
    padding: 0 0 7px;
    margin: 0 20px 5px;
  }

  .new-dialog-form
  /deep/
  .el-form-item {
    width: 100%;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__label {
    text-align: right;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 120px);
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-date-editor--date, .el-select, .el-date-editor--datetime {
    width: 100%;
  }

  @import '@/views/styles/table.scss';

</style>
