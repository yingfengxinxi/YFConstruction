<template>
  <div class="main">
    <div class="main-body">
      <el-form v-show="true" ref="queryForm" :model="queryParams" :scroll-wheel-zoom="true"
               :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="授权用户">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入授权用户名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item style="float: right;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <el-button
            class=" xr-btn--orange"
            type="primary"
            icon="el-icon-circle-plus-outline"
            size="mini"
            @click="handleAdd" >添加</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="typeList"
                highlight-current-row>
        <el-table-column label="所属部门" align="center" prop="deptName" />
        <el-table-column label="姓名" align="center" prop="realname" />
        <el-table-column label="登录账号" align="center" prop="username" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-remove-outline"
              @click="delUser(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
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
    <!-- 添加用户 -->
    <el-dialog title="添加授权" :visible.sync="addOpen" width="500px" height="300px" append-to-body>
      <xh-user
        :show="addOpen"
        :allowCheckAll="false"
        :disabled-data="defaultUsers"
        style="width: 100%;"
        @changeCheckout="strcUserChange"/>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitUsers">提 交</el-button>
        <el-button @click="addOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { getUserList, delUsers, saveUsers,getUserListAll} from '@/api/build/projectInfo'
  import XhUser from '@/components/CreateCom/XhUser'
  export default {
    name: 'GrantProject',
    components: {
      XhUser
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        // 字典表格数据
        typeList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        open2: false,
        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          userName: undefined,
        },
        // 表单参数
        form: {
          sort: undefined
        },
        //addUser
        addOpen:false,
        changeUsers: [],
        defaultUsers:[],
      }
    },
    props: {
      projectId: {
        type: Number,
      }
    },
    watch: {
      projectId(){
        this.getList();
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询字典类型列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize
        this.queryParams.projectId = this.projectId;
        getUserList(this.queryParams).then(response => {
          this.typeList = response.data.list
          this.total = response.data.totalRow
          this.loading = false
          this.getListAll();
        }).catch(() => {
          this.loading = false
        })
      },
      /** 获取所有已授权用户 */
      getListAll() {
        this.queryParams.projectId = this.projectId;
        getUserListAll(this.queryParams).then(response => {
          this.defaultUsers = response.data || [];
        }).catch(() => {
        })
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.addOpen = true;
      },
      strcUserChange(data) {
        this.changeUsers = data;
      },
      submitUsers(){
        if(this.changeUsers.length < 1){
          this.$message.warning("未选择任何用户");
          return;
        }
        var data = {};
        data.projectId = this.projectId;
        data.users = this.changeUsers.map(item => item.userId);
        console.log(data)
        saveUsers(data).then(response => {
          this.getList();
          this.$message.success("提交成功");
          this.addOpen = false;
        }).catch(() => {
        })
      },
      delUser(row){
        this.$confirm("确定删除选中的数据吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.loading = true;
            delUsers(row.id).then(res => {
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
      // 取消按钮
      cancel() {
        this.open = false;
        this.open2 = false;
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.currentPage = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
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
      },
    }
  }
</script>


<style lang="scss" scoped>

  .main {
    height:100%;
  }
  /deep/ .xr-header {
    padding: 15px 30px;
  }
  .handle-item-content {
    flex: 1;
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

  .add-form-input{
    float: left;
    margin-bottom: 5px;
  }
  add-form-divider{
    float: left;
    margin-bottom: 10px;
  }
  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  add-form-a .el-form-item{
    width: 50% !important;
  }
  .add-form .el-form-item{
    width: 33%;
  }
  .add-form-w100 .el-form-item{
    width: 100%;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
  .bm-view {
    width: 100%;
    height: 700px;
  }
  /deep/ .el-dialog{
    height: 500px;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    margin-top: auto!important;
  }

  /deep/ .el-dialog .el-dialog__body{
    height: 400px;
    overflow: auto;
  }
  .uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 200px;
    height: 200px;
    line-height: 200px;
    text-align: center;
  }
  .upload /deep/ .el-upload-dragger {
    height: 200px;
  }
  .upload-show {
    position: relative;
    display: block;
    width: 200px;
    height: 200px;
    img {
      width: 100%;
      height: 100%;
    }

    .icon-delete {
      position: absolute;
      top: -10px;
      right: -8px;
      color: red;
      font-size: 20px;
      display: none;
    }
  }
  .upload-show:hover {
    .icon-delete {
      display: block;
    }
  }

</style>
