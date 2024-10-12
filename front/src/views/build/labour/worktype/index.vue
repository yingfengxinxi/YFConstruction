<template>
  <div class="main">
<!--    <xr-header-->
<!--            icon-class="wk wk-project"-->
<!--            icon-color="#2362FB"-->
<!--            label="工种管理" />-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true"  style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="编码" prop="code">
          <el-input style="width=50px"
                  v-model="queryParams.code"
                  placeholder="请输入编码"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="工种" prop="workType">
          <el-input
                  v-model="queryParams.workType"
                  placeholder="请输入工种"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="首字母简拼" prop="workTypeJp">
          <el-input
                  v-model="queryParams.workTypeJp"
                  placeholder="请输入首字母简拼"
                  clearable
                  size="small"
                  @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item  label="是否特殊工种" class='special' prop="workTypeIfspecial" >
            <el-select
                    v-model="queryParams.workTypeIfspecial"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in workTypeIfspecialOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
          </el-form-item>
<!--          <el-form-item label="是否父工种" prop="workTypeIfspecial"  >
            <el-select
                    v-model="queryParams.workTypeIfspecial"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in workTypeIfspecialOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
          </el-form-item>-->
        <el-form-item style="float: right">
        	          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <el-button type="primary" icon="el-icon-printer" @click="exportFile(1)" size="mini" >导出</el-button>

          <el-button
                  class="xr-btn--orange"
                  type="primary"
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange" >
        <el-table-column label="序号" type="index"  align='center' width="70"></el-table-column>
        <el-table-column label="编码" align="center" prop="code" width="150px" sortable="custom"/>
        <el-table-column label="工种" align="center" prop="workType" sortable="custom" />
        <el-table-column label="首字母简拼" align="center" prop="workTypeJp" sortable="custom" />
        <el-table-column label="是否特殊工种" align="center" prop="workTypeIfspecial" :formatter="workTypeIfspecialFormat" sortable="custom" />
        <el-table-column label="排序" align="center" prop="sort" sortable="custom" />

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
                :current-page="currentPage"
                :page-size.sync="pageSize"
                :total="total"
                class="p-bar"
                background
                layout="prev, pager, next, sizes, total, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"/>
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <el-form-item label="编码" prop="code" class="add-form-input">
            <el-input v-model="form.code" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="工种" prop="workType" class="add-form-input">
            <el-input v-model="form.workType" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="首字母简拼" prop="workTypeJp" class="add-form-input">
            <el-input v-model="form.workTypeJp" placeholder="请输入" />
          </el-form-item>
           <el-form-item label="排序" prop="sort" class="add-form-input">
            <el-input v-model.number="form.sort" type="number" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="是否特殊工种" prop="workTypeIfspecial" class="add-form-input">
            <el-select
                    v-model="form.workTypeIfspecial"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in workTypeIfspecialOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

  // import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import request from '@/utils/request'
  import SelectCol from "@/views/admin/components/selectCol";
//	import vPinyin from './vuepy.js';
	import vPinyin from '@/utils/vuepy.js';
  import { exportFile } from "@/api/build/exportFile";
  import { downloadExcelWithResData,sortColumn } from '@/utils/index';
  export default {
    name: 'worktype',
    components: {
      // Reminder,
      XrHeader,
      SelectCol
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
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
        // 表格数据
        dataList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        //是否特殊工种
        workTypeIfspecialOptions: [],
        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          hydrantCode: undefined,
          pipeId: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          code: [
            { required: true, message: '编码不能为空', trigger: 'blur' }
          ],

        },
      }
    },
    created() {
      this.getList()
      //是否特殊工种
      this.getDicts('sys_yes_no').then(response => {
        this.workTypeIfspecialOptions = response.data
      })

    },
    watch: {
		  'queryParams.workType': function (val, oldVal) {
		  	this.inputPY()
		  },
		  'form.workType': function (val, oldVal) {
		  	// console.log(val,oldVal)
		  	this.addPY()
		  },
		},
    methods: {
    	sortChange(column) {
	      sortColumn(this,column);
	    },
//  	拼音首字母
		inputPY () {
			let pinyin= vPinyin.chineseToPinYin(this.queryParams.workType);
		   let SX = '';
		   for (var i = 0; i < pinyin.length; i++) {
		      var c = pinyin.charAt(i);
		      if (/^[A-Z]+$/.test(c)) {
		           SX += c;
		      }
		   }
		   this.queryParams.workTypeJp  = SX;
		},
		addPY () {
			 let pinyin = vPinyin.chineseToPinYin(this.form.workType);
		   let SX = '';
		   for (var i = 0; i < pinyin.length; i++) {
		      var c = pinyin.charAt(i);
		      if (/^[A-Z]+$/.test(c)) {
		           SX += c;
		      }
		   }
		   this.form.workTypeJp  = SX;
		},
      /** 查询数据列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize

        request({
          url: 'build/labourWorktype/list',
          method: 'post',
          data: this.queryParams,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          // console.log(res.data)
          this.dataList = res.data.list

          if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
              this.currentPage = this.currentPage - 1
              this.getList()
          } else {
            this.total = res.data.totalRow
            this.loading = false
          }

          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
       //显示是否特殊工种
      workTypeIfspecialFormat(row, column){
        return this.selectDictLabel(this.workTypeIfspecialOptions, row.workTypeIfspecial)
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        //this.resetForm('queryForm')
        this.queryParams = {}
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '新增数据'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const pipeId = row.id || this.ids
        request({
          url: 'build/labourWorktype/queryById/' + pipeId,
          method: 'post'
        }).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改数据'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.hydrantId != undefined) {
              request({
                url: 'build/labourWorktype/update',
                method: 'post',
                data: this.form,
                headers: {
                  'Content-Type': 'application/json;charset=UTF-8'
                }
              }).then(response => {
                if (response.code === 0) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                }
              })
            } else {
              request({
                url: 'build/labourWorktype/add',
                method: 'post',
                data: this.form,
                headers: {
                  'Content-Type': 'application/json;charset=UTF-8'
                }
              }).then(response => {
                if (response.code === 0) {
                  this.msgSuccess('新增成功')
                  this.open = false
                  this.getList()
                }
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const id = row.id || this.ids
        const code = row.code
        this.$confirm('是否确认删除编码为"' + code + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return request({
            url: 'build/labourWorktype/delete/' + id,
            method: 'post'
          })
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {})
      },

      // /** 导出按钮操作 */
      // handleExport() {
      //   this.download('system/dict/type/export', {
      //     ...this.queryParams
      //   }, `type_${new Date().getTime()}.xlsx`)
      // },
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
      /**
       *
       * 使用导出需引入js文件如下：
       * import { exportFile } from "@/api/build/exportFile";
       * import { downloadExcelWithResData } from '@/utils/index';
       * 方法入参说明： type=1 全部导出  type=2 部分导出
       * 请求列表数据说明：需替换为每页列表的数据请求url
       *
       */
      async  exportFile (type) {
        this.loading = true;
        let data = '';
        if (type == 1) {
          //请求不分页的列表数据
          var objString = JSON.stringify(this.queryParams);
          var obj2 = JSON.parse(objString);
          let reqParam = obj2;
          reqParam.pageType = 0
          await request({
            url: 'build/labourWorktype/list',
            method: 'post',
            data: this.queryParams,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(res => {
              data = res.data.list;
            })
            .catch(err => {
              // console.log(err);
              this.$message.error("导出数据加载失败");
              this.loading = false;
            })
          // console.log(data);
        } else if (type == 2) {
          //获取当前选中行的数据
          data = this.selectionList;
        }
        /**
         * 整理数据
         * param中result是需要导出的数据(必填)
         * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)，
         * 如果有字典需转换请添加dictType类型，如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)
         * param中title是导出Excel的一级表头(必填)
         * 注意：fieldList中字段值必须和列表中字段值相对应！！！
         */
        let fieldList = [//表头
          { prop: "code", label: "编码", },
          { prop: "workType", label: "工种", },
          { prop: "workTypeJp", label: "首字母简拼", },
          {  prop: "workTypeIfspecial", label: "是否特殊工种",dictList:[{"dictLabel":"否","dictValue":"N"},{"dictLabel":"是","dictValue":"Y"}]  },
        ]
        let param = {};
        param.result = data;
        param.fieldList = fieldList;
        param.title = "工种列表";
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
      }
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


  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
	/deep/ .special .el-form-item__label{
		width: 100px!important;
	}
</style>
