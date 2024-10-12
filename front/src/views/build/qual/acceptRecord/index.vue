<template>
    <div class="main">
        <div class="main-body">
            <el-tabs
                    v-model="tabCurrentName"
                    type="border-card"
                    class="d-container-bd--left">
                <el-tab-pane label="单位工程跟踪" name="all">
                    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 5px;padding-left:10px;">
                        <el-form-item label="工程名称" prop="itemName">
                            <el-input
                                    v-model="queryParams.itemName"
                                    placeholder="请输入工程名称"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                        </el-form-item>
                        <el-form-item style="float: right">
                            <el-button
                                    class="main-table-header-button xr-btn--orange"
                                    type="primary"
                                    icon="el-icon-plus"
                                    size="mini"
                                    @click="handleAdd" >验收</el-button>
                        </el-form-item>
                    </el-form>
                    <el-table v-loading="loading" :data="dataList" :span-method="objectSpanMethod" :border="true"
                              :height="tableHeight" @selection-change="handleSelectionChange">
                         <el-table-column type="selection" align="center" width="55"/>
                        <el-table-column label="单位工程" align="center" prop="unitP" width="120"/>
                        <el-table-column label="子单位工程" align="center" prop="subUnitP" width="120"/>
                        <el-table-column label="分部工程" align="center" prop="parcelP" width="120"/>
                        <el-table-column label="子分部工程" align="center" prop="subParcelP" width="120"/>
                        <el-table-column label="分项工程" align="center" prop="itemP" width="120"/>
                        <el-table-column label="检验批" align="center" prop="inspectionLot" width="120"/>
                        <el-table-column label="工程名称" align="center" prop="itemName" :show-overflow-tooltip="true" />
                        <el-table-column label="验收编号" align="center" prop="acceptCode" />
                        <el-table-column label="验收说明" align="center" prop="content" />
                        <el-table-column label="验收日期" align="center" prop="reportDate" />
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
                </el-tab-pane>
                <el-tab-pane label="分项验收记录" name="list">
                    <el-table v-loading="loading" :data="checkList" :border="true" :height="tableHeight2">
                        <el-table-column type="index" label="序号" align="center" width="80"/>
                        <el-table-column label="验收编号" align="center" prop="acceptCode"/>
                        <el-table-column label="验收日期" align="center" prop="reportDate"/>
                        <el-table-column label="验收单位" align="center" prop="acceptUnit"/>
                        <el-table-column label="验收人" align="center" prop="reportUser"/>
                        <el-table-column label="整改问题项" align="center" prop="correctItem"/>
                        <el-table-column label="验收结论" align="center" prop="acceptResult"/>
                        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                            <template slot-scope="scope">
                                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" >修改</el-button>
                                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="p-contianer">
                        <el-pagination
                                :current-page="currentPage2"
                                :page-size.sync="pageSize2"
                                :total="total2"
                                class="p-bar"
                                background
                                layout="prev, pager, next, sizes, total, jumper"
                                @size-change="handleSizeChange2"
                                @current-change="handleCurrentChange2"/>
                    </div>
                </el-tab-pane>

            </el-tabs>

            <!-- 添加或修改参数配置对话框 -->
            <el-dialog :title="title" :visible.sync="open" width="1050px" append-to-body :close-on-click-modal="false">
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="验收编号" prop="acceptCode">
                                <el-input v-model="form.acceptCode" placeholder="请输入验收编号" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="验收日期" prop="reportDate">
                                <el-date-picker
                                        v-model="form.reportDate"
                                        type="date"
                                        placeholder="请选择验收日期"
                                        value-format="yyyy-MM-dd"/>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="验收单位" prop="acceptUnit">
                                <el-input v-model="form.acceptUnit" placeholder="请输入验收单位" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="验收人" prop="reportUser">
                                <el-input v-model="form.reportUser" placeholder="请输入验收人" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="整改问题项" prop="correctItem">
                                <el-input v-model="form.correctItem" type="textarea" :row="2" placeholder="请输入整改问题项" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="验收结论" prop="acceptResult">
                                <el-input v-model="form.acceptResult" type="textarea" :row="2" placeholder="请输入验收结论" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-table :data="form.recordList" :border="true" :height="280">
                        <el-table-column label="单位工程" align="center" prop="unitP" width="120"/>
                        <el-table-column label="子单位工程" align="center" prop="subUnitP" width="120"/>
                        <el-table-column label="分部工程" align="center" prop="parcelP" width="120"/>
                        <el-table-column label="子分部工程" align="center" prop="subParcelP" width="120"/>
                        <el-table-column label="分项工程" align="center" prop="itemP" width="120"/>
                        <el-table-column label="检验批" align="center" prop="inspectionLot" width="120"/>
                        <el-table-column label="工程名称" align="center" prop="itemName" :show-overflow-tooltip="true" />
                        <el-table-column label="验收说明" align="center" prop="content" width="120">
                            <template slot-scope="scope">
                                <el-input v-model="scope.row.content" placeholder="请输入验收说明" />
                            </template>
                        </el-table-column>
                    </el-table>
<!--                    TODO 附件&&表格-->
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
    import { queryList,queryBatchList,queryById, saveOrUpdate, del } from '@/views/build/qual/api/acceptBatch'
    import mergeList from '@/views/build/qual/acceptance/mergeList';
    export default {
        name: "acceptReocrd",
        components: {
        },
        mixins: [mergeList],
        data() {
            return {
                //tab
                tabCurrentName: 'all',
                // 遮罩层
                loading: true,
                // 选中数组
                ids: [],
                selectionList: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                // 分页
                currentPage: 1,
                pageSize: 10,
                total: 0,
                /** 列表 */
                tableHeight: document.documentElement.clientHeight - 300, // 表的高度
                dataList: [],
                spanArr: [],//用于存放每一行记录的合并数
                // 弹出层标题
                title: '',
                // 是否显示弹出层
                open: false,
                // 日期范围
                dateRange: [],
                // 查询参数
                queryParams: {
                    itemName:"",
                    page: this.currentPage,
                    limit: this.pageSize,
                    orders: []
                },
                checkList: [],
                currentPage2: 1,
                pageSize2: 10,
                total2: 0,
                sortData: {}, // 字段排序
                tableHeight2: document.documentElement.clientHeight - 280, // 表的高度
                // 查询参数
                queryParams2: {
                    page: this.currentPage2,
                    limit: this.pageSize2,
                    orders: []
                },
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                    // dictName: [
                    //     { required: true, message: '工程名称不能为空', trigger: 'blur' }
                    // ],
                    // dictType: [
                    //     { required: true, message: '单位工程划分不能为空', trigger: 'blur' }
                    // ]
                }
            }
        },
        created() {
            this.getList()
            this.getList2()
        },
        methods: {
            /** 查询单位工程划分列表 */
            getList() {
                this.loading = true
                this.queryParams.page = this.currentPage
                this.queryParams.limit = this.pageSize
                /* 排序逻辑 */
                for (let index = 0; index < this.queryParams.orders.length; index++) {
                    if(this.queryParams.orders[index].column == this.sortData.prop){
                        this.queryParams.orders.splice(index, 1);
                    }
                }
                if (this.sortData.order) {
                    this.queryParams.orders[this.queryParams.orders.length] = {"column":this.sortData.prop,"asc":this.sortData.order == 'ascending' ? true : false}
                }
                queryList(this.queryParams).then(response => {
                    this.dataList = response.data.list
                    this.total = response.data.totalRow
                    this.loading = false
                    this.getListDataForRowAndColumn(this.dataList)
                }).catch(() => {
                    this.loading = false
                })
            },
            // 取消按钮
            cancel() {
                this.open = false
                this.reset()
            },
            // 表单重置
            reset() {
                this.form = {
                    id: undefined,
                    acceptCode: undefined,
                    reportDate: undefined,
                    acceptUnit: undefined,
                    reportUser: undefined,
                    correctItem: undefined,
                    acceptResult: undefined,
                    recordList: []
                }
                this.resetForm('form')
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1
                this.queryParams.limit = this.pageSize
                this.getList()
            },
            /** 重置按钮操作 */
            resetQuery() {
                // this.dateRange = []
                this.resetForm('queryForm')
                this.handleQuery()
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.selectionList = selection; // 勾选的行
                this.ids = selection.map(item => item.id)
                this.single = selection.length != 1
                this.multiple = !selection.length
            },
            /** 新增按钮操作 */
            handleAdd() {
                if(this.selectionList.length == 0){
                    this.msgError('请选择进行验收的单位工程')
                    return false;
                }
                for(var indez = 0 ;  indez< this.selectionList.length; indez++){
                    var data = this.selectionList[indez]
                    // alert(JSON.stringify(data))
                    // alert(data.acceptCode)
                    if(data.acceptCode!=undefined && data.acceptCode!= ''){
                        this.msgError('工程名称【'+data.itemName+'】已进行过验收')
                        return false;
                    }
                }
                this.reset()
                this.form.recordList = this.selectionList
                this.open = true
                this.title = '新建工程质量验收'
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs['form'].validate(valid => {
                    if (valid) {
                        if (this.form.id != undefined) {
                            saveOrUpdate(this.form).then(response => {
                                if (response.code === 0) {
                                    this.msgSuccess('修改成功')
                                    this.open = false
                                    this.getList()
                                    this.getList2()
                                }
                            })
                        } else {
                            saveOrUpdate(this.form).then(response => {
                                if (response.code === 0) {
                                    this.msgSuccess('新增成功')
                                    this.open = false
                                    this.getList()
                                    this.getList2()
                                }
                            })
                        }
                    }
                })
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
            /*--------验收记录---------*/
            getList2() {
                this.loading = true
                this.queryParams2.page = this.currentPage2
                this.queryParams2.limit = this.pageSize2
                /* 排序逻辑 */
                for (let index = 0; index < this.queryParams2.orders.length; index++) {
                    if(this.queryParams2.orders[index].column == this.sortData.prop){
                        this.queryParams2.orders.splice(index, 1);
                    }
                }
                if (this.sortData.order) {
                    this.queryParams2.orders[this.queryParams2.orders.length] = {"column":this.sortData.prop,"asc":this.sortData.order == 'ascending' ? true : false}
                }
                queryBatchList(this.queryParams2).then(response => {
                    this.checkList = response.data.list
                    this.total2 = response.data.totalRow
                    this.loading = false
                }).catch(() => {
                    this.loading = false
                })
            },
            // 更改每页展示数量
            handleSizeChange2(val) {
                this.pageSize2 = val
                this.getList2()
            },
            // 更改当前页数
            handleCurrentChange2(val) {
                this.currentPage2 = val
                this.getList2()
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset()
                const id = row.id || this.ids
                queryById(id).then(response => {
                    this.form = response.data
                    this.open = true
                    this.title = '修改工程质量验收'
                })
            },
            /** 删除按钮操作 */
            handleDelete(row) {
                const ids = row.id || this.ids
                this.$confirm('是否确认删除选择的数据项?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function() {
                    return del(ids)
                }).then(() => {
                    this.getList()
                    this.getList2()
                    this.msgSuccess('删除成功')
                }).catch(function() {})
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
    /deep/ .el-table__body td{
        border-right-color: #e6e6e6;
    }
    /*.link-type,.link-type:focus{color:#337ab7;cursor:pointer}*/
    /*.link-type:focus:hover,.link-type:hover{color:#20a0ff}*/
    /deep/.el-date-editor .el-input{
        width: 100%;
    }
</style>