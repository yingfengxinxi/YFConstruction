<template>
  <div class="main">

    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="项目名称" prop="projectId">
          <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="隐患类别" prop="dangerType">
          <el-select v-model="queryParams.dangerType" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in hazardCategoryOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="隐患等级" prop="dangerLevel">
          <el-select v-model="queryParams.dangerLevel" placeholder="请选择" clearable size="small" style="width: 160px">
            <el-option v-for="dict in hazardLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <!--        <el-form-item label="检查负责人" prop="inspectPeople">-->
        <!--          <el-input v-model="queryParams.inspectPeople" placeholder="请输入检查负责人" clearable size="small" @keyup.enter.native="handleQuery" />-->
        <!--        </el-form-item>-->
        <el-form-item label="检查日期" prop="sjtbDate">
          <el-date-picker class="timecom" v-model="sjtbDate" @change="dateChange" value-format="yyyy-MM-dd" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="检查日期起" end-placeholder="检查日期止" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

          <!--          <el-button class="xr-btn&#45;&#45;orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>-->
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" v-loading="loading" :data="dataList">
        <el-table-column label="序号" align='center' type="index" width="60" />
        <el-table-column label="项目名称" align="center" prop="projectId" :formatter="projectFormatter" show-overflow-tooltip/>
        <el-table-column label="隐患类别" align="center" prop="dangerType" :formatter="hazardCategoryFormatter" show-overflow-tooltip/>
        <el-table-column label="隐患等级" align="center" prop="dangerLevel" :formatter="hazardLevelFormatter" show-overflow-tooltip/>
        <el-table-column label="检查负责人" align="center" prop="inspectPeople" show-overflow-tooltip/>
        <el-table-column label="检查日期" align="center" prop="inspectDate" show-overflow-tooltip/>
        <el-table-column label="检查类型" align="center" prop="inspectType" :formatter="chekcTypeFormatter" show-overflow-tooltip/>
        <el-table-column label="责任单位" align="center" prop="responUnit" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">查看</el-button>
            <!--            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination :current-page="currentPage" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
          <create-sections style="overflow:hidden" title="隐患基本信息">
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                <el-select v-model="form.projectId" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目编号" prop="projectCode" class="add-form-input">
                <el-input v-model="form.projectCode" placeholder="请输入项目编号" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="隐患编号" prop="dangerCode" class="add-form-input">
                <el-input v-model="form.dangerCode" placeholder="请输入隐患编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐患类别" prop="dangerType" class="add-form-input">
                <el-select v-model="form.dangerType" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in hazardCategoryOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐患等级" prop="dangerLevel" class="add-form-input">
                <el-select v-model="form.dangerLevel" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in hazardLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐患内容" prop="dangerContent" class="add-form-input">
                <el-input v-model="form.dangerContent" :rows="2" type="textarea" placeholder="请输入隐患内容" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="检查负责人" prop="inspectPeople" class="add-form-input">
                <el-input v-model="form.inspectPeople" placeholder="请输入检查负责人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="inspectPeoplePhone" class="add-form-input">
                <el-input v-model.number="form.inspectPeoplePhone" placeholder="请输入联系电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查日期" prop="inspectDate" class="add-form-input">
                <el-date-picker v-model="form.inspectDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 330px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查类型" prop="inspectType" class="add-form-input">
                <el-select v-model="form.inspectType" placeholder="请选择" clearable size="small" style="width: 330px">
                  <el-option v-for="dict in chekcTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="责任单位" prop="responUnit" class="add-form-input">
                <el-input v-model="form.responUnit" placeholder="请输入责任单位" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="社会信用代码" prop="responUnitCreditCode" class="add-form-input">
                <el-input v-model="form.responUnitCreditCode" placeholder="请输入统一社会信用代码" />
              </el-form-item>
            </el-col>
          </create-sections>
          <create-sections style="overflow:hidden" title="整改信息">
            <el-col :span="12">
              <el-form-item label="整改人" prop="rectPerson" class="add-form-input">
                <el-input v-model="form.rectPerson" placeholder="请输入整改人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="rectPersonPhone" class="add-form-input">
                <el-input v-model.number="form.rectPersonPhone" placeholder="请输入联系电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改期限" prop="rectTerm" class="add-form-input">
                <el-input v-model="form.rectTerm" placeholder="请输入整改期限" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上报时间" prop="reportTime" class="add-form-input">
                <el-date-picker v-model="form.reportTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择时间" style="width: 330px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查结果" prop="inspectResult" class="add-form-input">
                <el-input v-model="form.inspectResult" placeholder="请输入检查结果" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查区域" prop="inspectRegion" class="add-form-input">
                <el-input v-model="form.inspectRegion" :rows="2" type="textarea" placeholder="请输入检查区域" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改完成时间" prop="rectOverTime" class="add-form-input">
                <el-date-picker v-model="form.rectOverTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择时间" style="width: 330px" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="复查人" prop="reviewPeopel" class="add-form-input">
                <el-input v-model="form.reviewPeopel" placeholder="请输入复查人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="复查结果" prop="reviewResult" class="add-form-input">
                <el-input v-model="form.reviewResult" placeholder="请输入复查结果" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="复查完成时间" prop="reviewOverTime" class="add-form-input">
                <el-date-picker v-model="form.reviewOverTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择时间" style="width: 330px" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="说明" prop="remarks" class="add-form-input">
                <el-input v-model="form.remarks" :rows="2" type="textarea" placeholder="请输入说明" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="罚款信息" prop="fineInfo" class="add-form-input">
                <el-input v-model="form.fineInfo" :rows="2" type="textarea" placeholder="请输入罚款信息" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="补充信息" prop="supplementInfo">
                <el-input v-model="form.supplementInfo" :rows="2" type="textarea" placeholder="请输入补充信息" />
              </el-form-item>
            </el-col>
          </create-sections>
          <create-sections style="overflow:hidden" title="附件信息">
            <el-col :span="24">
              <wk-upload-file ref="wkUploadFile" name="file" multiple accept="*" :autoUpload="false" :batchId="batchId"></wk-upload-file>
            </el-col>
          </create-sections>
        </el-form>
        <!--        <div slot="footer" class="dialog-footer">-->
        <!--          <el-button type="primary" @click="submitForm">确 定</el-button>-->
        <!--          <el-button @click="cancel">取 消</el-button>-->
        <!--        </div>-->
      </el-dialog>
    </div>
  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid } from '@/utils'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";
import CreateSections from '@/components/CreateSections'
import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'

export default {
  name: 'supervision',
  components: {
    XrHeader,
    SelectCol,
    CreateSections,
    WkUploadFile,
  },
  data () {
    var bargainMoney = (rule, value, callback) => {
      // rule 对应使用bargainMoney自定义验证的 对象
      // value 对应使用bargainMoney自定义验证的 数值
      // callback 回调函数
      const r = /^1[34578]\d{9}$/; // 手机号
      if (value != "" && value != undefined) {
        if (r.test(value) == false) {
          callback(new Error("请输入正确的手机号"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      //table高度控制
      tableHeight: document.documentElement.clientHeight - 310,
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
      // 附件id
      batchId: '',
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      sjtbDate: '',
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectId: [
          { required: true, message: '请选择项目', trigger: 'blur' }
        ],
        projectCode: [
          { required: true, message: '项目编号不能为空', trigger: 'blur' }
        ],
        dangerCode: [
          { required: true, message: '隐患编号不能为空', trigger: 'blur' }
        ],
        dangerType: [
          { required: true, message: '隐患类别不能为空', trigger: 'blur' }
        ],
        dangerLevel: [
          { required: true, message: '隐患等级不能为空', trigger: 'blur' }
        ],
        dangerContent: [
          { required: true, message: '隐患内容不能为空', trigger: 'blur' }
        ],
        inspectPeople: [
          { required: true, message: '检查负责人不能为空', trigger: 'blur' }
        ],
        inspectPeoplePhone: [
          { required: true, message: '检查负责人联系电话不能为空', trigger: 'blur' },
          { required: true, validator: bargainMoney, trigger: 'blur' },
        ],
        inspectDate: [
          { required: true, message: '检查日期不能为空', trigger: 'blur' }
        ],
        inspectType: [
          { required: true, message: '检查类型不能为空', trigger: 'blur' }
        ],
        responUnit: [
          { required: true, message: '责任单位不能为空', trigger: 'blur' }
        ],
        responUnitCreditCode: [
          { required: true, message: '责任单位统一社会信用代码不能为空', trigger: 'blur' }
        ],
        rectPerson: [
          { required: true, message: '整改人不能为空', trigger: 'blur' }
        ],
        rectPersonPhone: [
          { required: true, message: '整改人联系电话不能为空', trigger: 'blur' },
          { required: true, validator: bargainMoney, trigger: 'blur' },
        ],
        rectTerm: [
          { required: true, message: '整改期限不能为空', trigger: 'blur' }
        ],
        reportTime: [
          { required: true, message: '上报时间不能为空', trigger: 'blur' }
        ],
        inspectResult: [
          { required: true, message: '检查结果不能为空', trigger: 'blur' }
        ],
        inspectRegion: [
          { required: true, message: '检查区域不能为空', trigger: 'blur' }
        ],
        rectOverTime: [
          { required: true, message: '整改完成时间不能为空', trigger: 'blur' }
        ],
        reviewPeopel: [
          { required: true, message: '复查人不能为空', trigger: 'blur' }
        ],
        reviewResult: [
          { required: true, message: '复查结果不能为空', trigger: 'blur' }
        ],
        reviewOverTime: [
          { required: true, message: '复查完成时间不能为空', trigger: 'blur' }
        ],
      },
      //项目列表
      projectOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      //隐患级别
      hazardLevelOptions: [],
      //隐患类别
      hazardCategoryOptions: [],
      //检查类型
      chekcTypeOptions: [],
    }
  },
  created () {
    //查询项目列表
    request({
      url: 'build/projectInfo/queryProjectList',
      method: 'post',
      data: {},
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    }).then(response => {
      this.projectOptions = response.data
    })
    //查询隐患级别
    this.getDicts('b_hazard_level').then(response => {
      this.hazardLevelOptions = response.data
    })
    //查询隐患类别
    this.getDicts('b_hazard_category').then(response => {
      this.hazardCategoryOptions = response.data
    })
    //查询检查类型
    this.getDicts('b_chekc_type').then(response => {
      this.chekcTypeOptions = response.data
    })
    //查询教育培训记录列表
    this.getList()
  },
  mounted () {
    console.log(this.$route.query)

    this.$route.query.projectId != undefined && this.$route.query.projectId != '' ?
      this.$set(this.queryParams, 'projectId', this.$route.query.projectId) : ''

    this.$route.query.timeSlot != undefined && this.$route.query.timeSlot != '' ?
      this.$set(this.queryParams, 'timeSlot', this.$route.query.timeSlot) : ''

  },
  methods: {
    dateChange () {
      if (this.sjtbDate == null) {
        this.queryParams.inspectStartDate = ''
        this.queryParams.inspectEndDate = ''
      }
    },
    /** 查询数据列表 */
    getList () {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      if (this.sjtbDate != undefined && this.sjtbDate != '' && this.sjtbDate.length != 0) {
        this.queryParams.inspectStartDate = this.sjtbDate[0]
        this.queryParams.inspectEndDate = this.sjtbDate[1]
      }
      request({
        url: 'build/jgSafetyDangerRecord/list',
        method: 'post',
        data: this.queryParams,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        this.dataList = response.data.list
        if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
          this.currentPage = this.currentPage - 1
          this.getList()
        } else {
          this.total = response.data.totalRow
          this.loading = false
        }
      }).catch(() => {
        this.loading = false
      })
    },
    //项目格式化
    projectFormatter (row, column) {
      return this.selectDIYLabel(this.projectOptions, row.projectId, 'id', 'name')
    },
    //隐患类型格式化
    hazardCategoryFormatter (row, column) {
      return this.selectDIYLabel(this.hazardCategoryOptions, row.dangerType, 'dictValue', 'dictLabel')
    },
    //隐患级别格式化
    hazardLevelFormatter (row, column) {
      return this.selectDIYLabel(this.hazardLevelOptions, row.dangerLevel, 'dictValue', 'dictLabel')
    },
    //检查类型格式化
    chekcTypeFormatter (row, column) {
      return this.selectDIYLabel(this.chekcTypeOptions, row.inspectType, 'dictValue', 'dictLabel')
    },
    //列格式化
    selectDIYLabel (datas, value, dataValue, dataLablel) {
      var actions = []
      Object.keys(datas).some((key) => {
        if (datas[key][dataValue] == (typeof datas[key][dataValue] == 'number' ? (0 + value) : ('' + value))) {
          actions.push(datas[key][dataLablel])
          return true
        }
      })
      return actions.join('')
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          var fileFlag = this.$refs.wkUploadFile.submitAsync();
          if (!fileFlag) {
            this.$message({
              type: 'error',
              message: '方案附件提交异常'
            })
            return;
          } else {
            this.form.batchId = this.batchId;
          }
          request({
            url: 'build/jgSafetyDangerRecord/save',
            method: 'post',
            data: this.form,
            headers: {
              'Content-Type': 'application/json;charset=UTF-8'
            }
          }).then(response => {
            if (response.code === 0) {
              if (this.form.id != undefined) {
                this.msgSuccess('修改成功')
              } else {
                this.msgSuccess('新增成功')
              }
              this.open = false
              this.getList()
            }
          })
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1
      this.queryParams.limit = this.pageSize
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.sjtbDate = ''
      this.queryParams = {}
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd () {
      console.log(guid());
      this.reset()
      this.batchId = guid()
      this.open = true
      this.title = '新增'
      this.membersList = []
    },
    // 表单重置
    reset () {
      this.form = {}
      this.resetForm('form')
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      const pipeId = row.id
      request({
        url: 'build/jgSafetyDangerRecord/getById/' + pipeId,
        method: 'post'
      }).then(response => {
        this.form = response.data
        this.batchId = response.data.batchId
        this.open = true
        this.title = '查看详情'
      })
    },

    /** 删除按钮操作 */
    handleDelete (row) {
      const id = [row.id] || this.ids

      this.$confirm('是否确认删除该数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return request({
          url: 'build/jgSafetyDangerRecord/delete',
          method: 'post',
          data: id,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function () { })
    },

    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 更改每页展示数量
    handleSizeChange (val) {
      this.pageSize = val
      this.getList()
    },
    // 更改当前页数
    handleCurrentChange (val) {
      this.currentPage = val
      this.getList()
    },
    /** 日期去除时分秒 */
    formatterTime (row, column) {
      let data = row[column.property]
      return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
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
