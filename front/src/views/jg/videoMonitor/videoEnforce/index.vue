<template>
    <div class="main">

        <div class="main-body">
            <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
                <el-form-item label="项目名称" prop="projectId">
                    <el-select v-model="queryParams.projectId" placeholder="请选择" clearable size="small" style="width: 160px">
                        <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                    </el-select>
                </el-form-item>

                <el-form-item label="发现人" prop="findPeople">
                    <el-input v-model="queryParams.findPeople" placeholder="请输入发现人" clearable size="small" @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="发现时间" prop="sjtbDate">
                    <el-date-picker class="timecom" v-model="sjtbDate" @change="dateChange" value-format="yyyy-MM-dd" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="发现时间起" end-placeholder="发现时间止" :picker-options="pickerOptions">
                    </el-date-picker>
                </el-form-item>
                <el-form-item style="float: right">
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

                    <el-button class="xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建</el-button>
                </el-form-item>
            </el-form>

            <div class="table-head-container">
                <flexbox v-if="selectionList.length > 0" class="selection-bar">
                    <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
                    <flexbox class="selection-items-box">
                        <el-button v-for="(item, index) in selectionHandleItems" v-if="canHandle(item.type)" :icon="item.icon | wkIconPre" :key="index" type="primary" @click.native="handleTypeDrop(item.type)">{{ item.name }}
                        </el-button>
                    </flexbox>
                </flexbox>
            </div>

            <el-table :height="tableHeight" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" @sort-change="sortChange">
                <el-table-column  type="selection" align="center" width="55" />
                <el-table-column label="序号" align='center' type="index" width="60" />
                <el-table-column label="项目名称" align="center" prop="projectId" :formatter="projectFormatter" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="发现时间" align="center" prop="findDate" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="发现人" align="center" prop="findPeople" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="发现问题" align="center" prop="findProblem" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="处罚措施" align="center" prop="punishMeasures" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="处罚条例" align="center" prop="punishReg" show-overflow-tooltip sortable="custom"/>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" icon="el-icon-edit" v-if="!controlDisplay(scope.row)" @click="handleUpdate(scope.row)">整改</el-button>
                        <el-button size="mini" type="text" icon="el-icon-tickets" v-if="controlDisplay(scope.row)" @click="handleView(scope.row)">查看</el-button>
                        <el-button size="mini" type="text" icon="el-icon-delete" v-if="!controlDisplay(scope.row)" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="p-contianer">
                <el-pagination :current-page="currentPage" :total="total" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
            <!-- 监控执法新增 -->
            <el-dialog :title="title" :visible.sync="addOpen" width="900px" append-to-body>
                <el-form ref="form" :model="form" :rules="rules_add" label-width="100px" class="add-form">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                                <el-select v-model="form.projectId" placeholder="请选择" clearable size="small" style="width: 330px">
                                    <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现时间" prop="findDate" class="add-form-input">
                                <el-date-picker v-model="form.findDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 330px" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现人" prop="findPeople" class="add-form-input">
                                <el-input v-model="form.findPeople" placeholder="请输入发现人" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现问题" prop="findProblem" class="add-form-input">
                                <el-input v-model="form.findProblem" placeholder="请输入问题" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="处罚措施" prop="punishMeasures" class="add-form-input">
                                <el-input v-model="form.punishMeasures" placeholder="请输入处罚措施" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="处罚条例" prop="punishReg" class="add-form-input">
                                <el-input v-model="form.punishReg" placeholder="请输入处罚条例" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改期限" prop="rectTerm" class="add-form-input">
                                <el-input v-model="form.rectTerm" placeholder="请输入整改期限" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改要求" prop="rectReq" class="add-form-input">
                                <el-input v-model="form.rectReq" placeholder="请输入整改要求" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="执法依据附件">
                                <wk-upload-file ref="wkUploadFile" name="file" multiple accept="*" :autoUpload="false" :batchId="batchId"></wk-upload-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="addCancel">取 消</el-button>
                </div>
            </el-dialog>
            <!-- 整改参数配置对话框 -->
            <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
                <el-form ref="form" :model="form" :rules="rules" :disabled="viewDislable" label-width="100px" class="add-form">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="项目名称" prop="projectId" class="add-form-input">
                                <el-select v-model="form.projectId" placeholder="请选择" :disabled="rectDislable" clearable size="small" style="width: 330px">
                                    <el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现时间" prop="findDate" class="add-form-input">
                                <el-date-picker v-model="form.findDate" type="date" :disabled="rectDislable" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 330px" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现人" prop="findPeople" class="add-form-input">
                                <el-input v-model="form.findPeople" :disabled="rectDislable" placeholder="请输入发现人" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="发现问题" prop="findProblem" class="add-form-input">
                                <el-input v-model="form.findProblem" :disabled="rectDislable" placeholder="请输入问题" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="处罚措施" prop="punishMeasures" class="add-form-input">
                                <el-input v-model="form.punishMeasures" :disabled="rectDislable" placeholder="请输入处罚措施" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="处罚条例" prop="punishReg" class="add-form-input">
                                <el-input v-model="form.punishReg" :disabled="rectDislable" placeholder="请输入处罚条例" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改期限" prop="rectTerm" class="add-form-input">
                                <el-input v-model="form.rectTerm" :disabled="rectDislable" placeholder="请输入整改期限" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改要求" prop="rectReq" class="add-form-input">
                                <el-input v-model="form.rectReq" :disabled="rectDislable" placeholder="请输入整改要求" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="执法依据附件">
                                <wk-upload-file ref="wkUploadFile" name="file" multiple accept="*" :disabled="rectDislable" :autoUpload="false" :batchId="batchId"></wk-upload-file>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="整改单位" prop="rectUnit" class="add-form-input">
                                <el-input v-model="form.rectUnit" placeholder="请输入整改单位" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改日期" prop="rectTime" class="add-form-input">
                                <el-date-picker v-model="form.rectTime" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 330px" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="整改结果" prop="rectResult" class="add-form-input">
                                <el-input v-model="form.rectResult" placeholder="请输入整改结果" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" v-if="!viewDislable" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
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

import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import { sortColumn } from '@/utils/index';

export default {
    name: 'safetyTraining',
    components: {
        XrHeader,
        SelectCol,
        WkUploadFile,
    },
    data () {
        return {
            // 遮罩层
            loading: true,
            //table高度控制
            tableHeight: document.documentElement.clientHeight - 280,
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
                rectUnit: [
                    { required: true, message: '整改单位不能为空', trigger: 'blur' }
                ],
                rectTime: [
                    { required: true, message: '整改日期不能为空', trigger: 'blur' }
                ],
                rectResult: [
                    { required: true, message: '整改结果不能为空', trigger: 'blur' }
                ],
            },
            rules_add: {
                projectId: [
                    { required: true, message: '请选择项目', trigger: 'blur' }
                ],
                findDate: [
                    { required: true, message: '发现时间不能为空', trigger: 'blur' }
                ],
                findPeople: [
                    { required: true, message: '发现人不能为空', trigger: 'blur' }
                ],
                findProblem: [
                    { required: true, message: '发现问题不能为空', trigger: 'blur' }
                ],
                punishMeasures: [
                    { required: true, message: '处罚措施不能为空', trigger: 'blur' }
                ],
                punishReg: [
                    { required: true, message: '处罚条例不能为空', trigger: 'blur' }
                ],
                rectTerm: [
                    { required: true, message: '整改期限不能为空', trigger: 'blur' }
                ],
                rectReq: [
                    { required: true, message: '整改要求不能为空', trigger: 'blur' }
                ],
            },
            //项目列表
            projectOptions: [],
            // 弹出层标题
            title: '',
            // 是否显示弹出层
            open: false,
            rectDislable: false,
            viewDislable: false,
            addOpen: false,
            //列表多选操作
            selectionList: [],
            /** 选中时操作 */
            selectionHandleItems: [
                {
                    name: '删除',
                    type: 'delete',
                    icon: 'delete'
                },
            ],
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
        //查询教育培训记录列表
        this.getList()
    },
    methods: {

        handleAdd () {
            this.addOpen = true;
            this.title = '视频监控执法新建';
            this.reset()
            this.batchId = guid();
            this.form.findDate = new Date()
        },
        controlDisplay (row) {
            if (row.rectUnit != null && row.rectTime != null && row.rectResult != null) {
                return true;
            }
            return false;
        },
        dateChange () {
            if (this.sjtbDate == null) {
                this.queryParams.findStartDate = ''
                this.queryParams.findEndDate = ''
            }
        },
        /** 查询数据列表 */
        getList () {
            this.loading = true
            this.queryParams.page = this.currentPage
            this.queryParams.limit = this.pageSize
            if (this.sjtbDate != undefined && this.sjtbDate != '' && this.sjtbDate.length != 0) {
                this.queryParams.findStartDate = this.sjtbDate[0]
                this.queryParams.findEndDate = this.sjtbDate[1]
            }
            request({
                url: 'build/jgEnforceRecord/list',
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
                        url: 'build/jgEnforceRecord/save',
                        method: 'post',
                        data: this.form,
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        }
                    }).then(response => {
                        if (response.code === 0) {
                            if (this.form.id != undefined) {
                                this.msgSuccess('整改成功')
                                this.open = false
                                this.rectDislable = false;
                            } else {
                                this.msgSuccess('新增成功')
                                this.addOpen = false
                            }

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
                url: 'build/jgEnforceRecord/getById/' + pipeId,
                method: 'post'
            }).then(response => {
                this.form = response.data
                this.batchId = response.data.batchId
                this.open = true
                this.title = '视频监控执法整改'
                this.rectDislable = true;
                this.viewDislable = false
            })
        },
        handleView (row) {
            this.reset()
            const pipeId = row.id
            request({
                url: 'build/jgEnforceRecord/getById/' + pipeId,
                method: 'post'
            }).then(response => {
                this.form = response.data
                this.batchId = response.data.batchId
                this.open = true
                this.title = '视频监控执法查看'
                this.rectDislable = false;
                this.viewDislable = true;
            })
        },
        handleDelete (row) {
            this.reset()
            const deleteIds = [row.id]
            this.$confirm('是否确认删除该数据项?', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                return request({
                    url: 'build/jgEnforceRecord/delete/',
                    method: 'post',
                    data: deleteIds,
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    }
                })
            }).then(() => {
                this.getList()
                this.msgSuccess('删除成功')
            }).catch(function () { })
        },
        //按钮操作权限
        canHandle (type) {
            return true;
            //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
        },
        /**
   *更多操作点击
   */
        handleTypeDrop (command) {
            console.log(command)
            if (command == 'delete') {//删除
                this.$confirm("确定删除选中的数据吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                })
                    .then(() => {
                        const deleteIds = this.selectionList.map(item => item['id'])
                        this.loading = true;
                        request({
                            url: 'build/jgEnforceRecord/delete/',
                            method: 'post',
                            data: deleteIds,
                            headers: {
                                'Content-Type': 'application/json;charset=UTF-8'
                            }
                        }).then(res => {
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
                        this.loading = false;
                    })
            }
        },
        /** 勾选操作 */
        // 当选择项发生变化时会触发该事件
        handleSelectionChange (val) {
            this.selectionList = val // 勾选的行
            this.updateTableHeight();
        },
        /**
   * 更新表高
   */
        updateTableHeight () {
            var offsetHei = document.documentElement.clientHeight
            var removeHeight = this.selectionList.length > 0 ? 370 : 260
            this.tableHeight = offsetHei - removeHeight
        },
        addCancel () {
            this.addOpen = false
            this.reset()
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
        sortChange(column) {
          sortColumn(this,column);
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
//列表数据多选
//表头
.table-head-container {
  //border-bottom: 1px solid #e6e6e6;
  border-top: 1px solid #e6e6e6;
  background-color: white;
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
</style>
