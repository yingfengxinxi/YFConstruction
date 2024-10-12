<template>
  <div class="main">
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm"   :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="施工日期">
          <el-date-picker
            v-model="queryParams.constructionDate"
            type="daterange"
            align="right"
            unlink-panels
            value-format="yyyy-MM-dd"
            format="yyyy年MM月dd"
            size="	large"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <!-- <el-button type="primary" icon="el-icon-printer" size="mini" >导出</el-button>-->

          <el-button
            class="xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList" >
         <el-table-column fixed label="序号"  align='center' type="index"  width="60"/>
        <el-table-column label="施工日期" align="center" prop="constructionDate" width="120px">
          <template slot-scope="scope">
            {{scope.row.constructionDate.substring(0,10)}}
          </template>
        </el-table-column>
        <el-table-column label="天气情况" align="center" prop="weatherCondition"/>
        <el-table-column label="风向" align="center" prop="windDirection" width="100px"/>
        <el-table-column label="风力" align="center" prop="windPower" width="100px"/>
        <el-table-column label="最高温度" align="center" prop="maxTemperature" width="100px"/>
        <el-table-column label="最低温度" align="center" prop="minTemperature" width="100px"/>
        <el-table-column label="累计施工天数" align="center" prop="constructionDays" width="100px"/>
        <el-table-column label="施工部位" align="center" prop="constructionSite" :show-overflow-tooltip="true"/>
        <el-table-column label="施工情况" align="center" prop="management" :show-overflow-tooltip="true"/>
        <el-table-column label="防范情况" align="center" prop="prevention" :show-overflow-tooltip="true"/>
        <el-table-column label="监理记录" align="center" prop="supervisionRecord" :show-overflow-tooltip="true"/>
<!--        <el-table-column label="创建时间" align="center">-->
<!--          <template slot-scope="scope">-->
<!--            {{scope.row.createTime.substring(0,10)}}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="填报人" align="center" prop="userName" width="120px"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdateSelect(scope.row)"
            >查看</el-button>
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
        <el-form ref="form" :model="form" :rules="rules" label-width="160px" class="add-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="施工日期" prop="constructionDate">
                <el-date-picker
                  v-model="form.constructionDate"
                  type="date"
                  :disabled="!isAble"
                  placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="施工部位" prop="constructionSite" class="add-form-input">
                <el-input v-model="form.constructionSite" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="累计施工天数" prop="constructionDays" class="add-form-input">
                <el-input v-model.number="form.constructionDays" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item  label="天气情况" prop="weatherCondition" class="add-form-input">
                <el-input v-model="form.weatherCondition" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item  label="最高温度" prop="maxTemperature" class="add-form-input">
                <el-input v-model="form.maxTemperature" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item  label="最低温度" prop="minTemperature" class="add-form-input">
                <el-input v-model="form.minTemperature" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风向" prop="windDirection" class="add-form-input">
                <el-input v-model="form.windDirection" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="风力" prop="windPower" class="add-form-input">
                <el-input v-model="form.windPower" :disabled="!isAble" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="施工现场管理情况" prop="management" class="add-form-input">
                <el-input v-model="form.management" :disabled="!isAble" type="textarea" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="防范情况" prop="prevention" class="add-form-input">
                <el-input v-model="form.prevention" :disabled="!isAble" type="textarea" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="监理记录" prop="supervisionRecord" class="add-form-input">
                <el-input v-model="form.supervisionRecord" :disabled="!isAble" type="textarea" placeholder="请输入" />
              </el-form-item>
            </el-col>
          </el-row>
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
  import SelectCol from '@/views/admin/components/selectCol'
  //	import vPinyin from './vuepy.js';

  export default {
    name: 'safetyLog',
    components: {
      // Reminder,
      XrHeader,
      SelectCol
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        tableHeight: document.documentElement.clientHeight - 280,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        isAble: false,
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
        // 选项类型  字典表safety_education_options
        safetyEducationOptions: [],
        // 题目分类  字典表safety_education_type
        safetyEducationType: [],
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
          constructionDate: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          constructionDays: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          weatherCondition: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          maxTemperature: [
            { required: true, message: '必填项', trigger: 'change' },
            {pattern:/^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: '必须为小数或正整数', trigger: 'blur'}
          ],
          minTemperature: [
            { required: true, message: '必填项', trigger: 'change'},
            {pattern:/^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: '必须为小数或正整数', trigger: 'blur'}
          ],
          windDirection: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          windPower: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          constructionSite: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          management: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          prevention: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          supervisionRecord: [
            { required: true, message: '必填项', trigger: 'change' }
          ]
        }
      }
    },

    created() {
      this.getList()
      // 选项类型
      this.getDicts('safety_education_options').then(response => {
        this.safetyEducationOptions = response.data
      })
      // 题目分类
      this.getDicts('safety_education_type').then(response => {
        this.safetyEducationType = response.data
      })
    },
    methods: {

      /** 查询数据列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize
        if(this.queryParams.constructionDate != undefined && this.queryParams.constructionDate!= null){
          this.queryParams.constructionDateSta = this.queryParams.constructionDate[0];
          this.queryParams.constructionDateEnd = this.queryParams.constructionDate[1];
        }else{
        	this.queryParams.constructionDateSta = '';
          this.queryParams.constructionDateEnd = '';
        }
        request({
          url: 'build/safetyLog/list',
          method: 'post',
          data: this.queryParams,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          // console.log(response.data)
          this.dataList = response.data.list
          if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1
          ) {
            this.currentPage = this.currentPage - 1;
            this.getList();
          } else {
            this.total = response.data.totalRow;
            this.loading = false;
          }
          // this.total = response.data.totalRow
          // this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      //选项类型
      safetyEducationOptionsFc(row, column){
        return this.selectDictLabel(this.safetyEducationOptions, row.safetyEducationOptions)
      },
      //题目分类
      safetyEducationTypeFc(row, column){
        return this.selectDictLabel(this.safetyEducationType, row.safetyEducationType)
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
        this.queryParams = {};
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.isAble = true
        this.title = '新增'
      },
      /** 查看按钮操作 */
      handleUpdateSelect(row) {
        this.reset()
        const pipeId = row.id || this.ids
        request({
          url: 'build/safetyLog/selectById/' + pipeId,
          method: 'get'
        }).then(response => {
          this.form = response.data
          this.open = true
          this.isAble = false
          this.title = '查看'
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const pipeId = row.id || this.ids
        request({
          url: 'build/safetyLog/selectById/' + pipeId,
          method: 'get'
        }).then(response => {
          this.form = response.data
          this.open = true
          this.isAble = true
          this.title = '修改'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              request({
                url: 'build/safetyLog/save',
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
                url: 'build/safetyLog/save',
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

        this.$confirm('是否确认删除该数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return request({
            url: 'build/safetyLog/delete/' + id,
            method: 'get'
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
