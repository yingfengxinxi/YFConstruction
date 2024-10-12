<template>
    <div class="main">
        <div class="main-body">
            <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
                     style="padding-top: 20px;padding-left:10px;">
                <el-form-item label="分部工程" prop="partialName">
                    <el-input
                            v-model="queryParams.partialName"
                            placeholder="请输入分部工程"

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
                            @click="handleAdd" >新建</el-button>
                  <el-button class="main-table-header-button xr-btn--orange"
                             type="primary"
                             icon="el-icon-plus"
                             size="mini" @click="bulkImportClick">项目导入</el-button>
                </el-form-item>

            </el-form>
            <div class="table-head-container">
                <flexbox
                        v-if="selectionList.length > 0"
                        class="selection-bar"
                >
                    <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
                    <flexbox class="selection-items-box">
                        <el-button
                                v-for="(item, index) in selectionHandleItems"
                                :icon="item.icon | wkIconPre"
                                :key="index"
                                type="primary"
                                @click.native="handleTypeDrop(item.type)"
                        >{{ item.name }}
                        </el-button>
                    </flexbox>
                </flexbox>
            </div>
            <el-table v-loading="loading" :data="dataList" :span-method="objectSpanMethod" :border="true"
                      :height="tableHeight" @selection-change="handleSelectionChange">
                <el-table-column type="selection" align="center" width="55"/>
                <el-table-column type="index" label="序号" align="center" width="80"/>
                <el-table-column label="分部工程" align="center" prop="partialName" width="120"/>
                <el-table-column label="子分部工程" align="center" prop="partialNameSon"/>
                <el-table-column label="分项工程" align="center" prop="partialEngineering"   />
<!--                <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
<!--                    <template slot-scope="scope">-->
<!--                        <span>{{ parseTime(scope.row.createTime) }}</span>-->
<!--                    </template>-->
<!--                </el-table-column>-->
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" >修改</el-button>
                        <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
            <!-- 添加或修改参数配置对话框 -->
            <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body :close-on-click-modal="false">
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="分部工程" prop="partialName">
                                <el-input v-model="form.partialName" placeholder="请输入分部工程" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="子分部工程" prop="partialNameSon">
                                <el-input v-model="form.partialNameSon" placeholder="请输入子分部工程" />
                            </el-form-item>
                        </el-col>
                    </el-row>


                    <el-form-item label="分项工程" prop="partialEngineering">
                        <el-input type="textarea" v-model="form.partialEngineering" placeholder="请输入分项工程"/>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </el-dialog>
        </div>
      <!-- 批量导入 -->
      <bulk-import-user
        :show="bulkImportShow"
        :param="param"
        @close="bulkImportShow=false"
      />
    </div>
</template>

<script>
    import { queryList,queryById, saveOrUpdate, del } from '@/views/build/qual/api/partial'
    import mergeList from '@/views/build/qual/partial/mergeList';
    import {remove} from "../api/enterpriseQualityCheckSubmit";
    import BulkImportUser from '@/components/importModel'
    export default {
        name:"partial",
        components: {
          BulkImportUser
        },
        mixins: [mergeList],
        data() {
            return {
                // 遮罩层
                loading: true,
              // 批量导入
              bulkImportShow: false,
                // 选中数组
                ids: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                /** 勾选行 */
                selectionList: [], // 勾选数据 用于全局导出
                /** 选中时操作 */
                selectionHandleItems: [
                    {
                        name: "删除",
                        type: "delete",
                        icon: "delete"
                    },
                    // {
                    //     name: "导出选中",
                    //     type: "export",
                    //     icon: "export"
                    // }
                ],
                // 分页
                currentPage: 1,
                pageSize: 10,
                total: 0,
                sortData: {}, // 字段排序
                /** 列表 */
                tableHeight: document.documentElement.clientHeight - 250, // 表的高度
                // 字典表格数据
                dataList: [],
                spanArr: [],//用于存放每一行记录的合并数
                // 弹出层标题
                title: '',
                // 是否显示弹出层
                open: false,
                // 状态数据字典
                statusOptions: [],
                // 日期范围
                dateRange: [],
                // 查询参数
                queryParams: {
                    page: this.currentPage,
                    limit: this.pageSize,
                    dictName: undefined,
                    dictType: undefined,
                    status: undefined,
                    orders: []
                },
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                  partialName: [
                        { required: true, message: '分部工程不能为空', trigger: 'blur' }
                    ],
                  partialNameSon: [
                        { required: true, message: '子分部工程不能为空', trigger: 'blur' }
                    ],
                  partialEngineering: [
                    { required: true, message: '分项工程不能为空', trigger: 'blur' }
                    ]
                }
            }
        },
        created() {
            this.getList()
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
            // 字典状态字典翻译
            statusFormat(row, column) {
                return this.selectDictLabel(this.statusOptions, row.status)
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
                    dictName: undefined,
                    dictType: undefined,
                    status: '0',
                    remark: undefined
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
                this.dateRange = []
                this.resetForm('queryForm')
                this.handleQuery()
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset()
                this.open = true
                this.title = '添加分部分项'
            },
            /**
             * 字段排序
             */
            sortChange(column, prop, order) {
                this.currentPage = 1
                this.sortData = column
                this.getList()
            },
            // 多选框选中数据
            handleSelectionChange(val) {
                // this.ids = selection.map(item => item.id)
                // this.single = selection.length != 1
                // this.multiple = !selection.length
                this.selectionList = val; // 勾选的行
                this.updateTableHeight();
            },

            /**
             *更多操作点击
             */
            handleTypeDrop(command) {
                // console.log(command)
                if (command == "delete") {
                    //删除
                    this.$confirm("确定删除选中的数据吗？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                        .then(() => {
                            const ids = this.selectionList.map(item => item["id"]);
                            this.loading = true;
                            del(ids)
                                .then(res => {
                                    if (res.code == 0) {
                                        this.$message.success("删除成功");
                                        this.getList();
                                    } else {
                                        this.$message.error(res.msg);
                                    }
                                    this.loading = false;
                                })
                                .catch(err => {
                                    this.loading = false;
                                });
                        })
                        .catch(() => {});
                } else if (command == "export") {
                    //导出
                } else if (command == "import") {
                    //导入
                }
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset()
                const id = row.id || this.ids
                queryById(id).then(response => {
                    this.form = response.data
                    this.open = true
                    this.title = '修改分部分项'
                })
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
                                }
                            })
                        } else {
                            saveOrUpdate(this.form).then(response => {
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
                const ids = row.id || this.ids
                this.$confirm('是否确认删除选择的数据项?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function() {
                    return del(ids)
                }).then(() => {
                    this.getList()
                    this.msgSuccess('删除成功')
                }).catch(function() {})
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
            /**
             * 更新表高
             */
            updateTableHeight() {
                var offsetHei = document.documentElement.clientHeight;
                var removeHeight = this.selectionList.length > 0 ? 300 : 250;
                this.tableHeight = offsetHei - removeHeight;
            },
          /**
           * 批量导入
           */
          bulkImportClick() {
            let lists = [
              "分部工程(*)",
              "子分部工程(*)",
              "分项工程(*)"
            ]
            //示例数据
            let dataLists = [
              "地基与基础工程",
              "地基",
              "素土、灰土地基，砂和砂石地基"
            ]

            this.param = {
              title:"分部分项模板(*)为必填项 ",
              excelTitle:"分部分项模板",
              colNames:lists,
              sampleData:dataLists,
              url:'build/bQualPartial/excelImport'
            }
            this.bulkImportShow = true
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
        /*border-bottom: 1px solid $xr-border-line-color;*/
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

    /** //表头 */
    .table-head-container {
        /*//border-bottom: 1px solid #e6e6e6;*/
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
