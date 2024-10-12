<template>
    <div class="main">

        <div class="main-body">
            <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
                <el-form-item label="报警地点" prop="subcontractorId">
                    <el-select
                            v-model="queryParams.subcontractorId"
                            placeholder=""
                            clearable
                            size="small"
                            style="width: 200px"
                    >
                        <el-option
                                v-for="option in subcontractorOptions"
                                :key="option.id"
                                :label="option.realName"
                                :value="option.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="报警日期" prop="teamLeaderName">
                    <el-date-picker
                            type="datetime"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="处理人" prop="teamLeaderName">
                    <el-input
                            v-model="queryParams.teamLeaderName"
                            placeholder=""
                            clearable
                            size="small"
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
                    >新建</el-button>
                </el-form-item>
            </el-form>
            <el-table v-loading="loading" :data="dataList">
                <el-table-column label="序号" type="index" align="center" width="70"/>
                <el-table-column label="报警地点" align="center" prop="t1" width="250px"/>
                <el-table-column label="报警类型" align="center" prop="t2" width="120px"/>
                <el-table-column label="报警时间" align="center" prop="t3"/>
                <el-table-column label="报警内容" align="center" prop="t4" width="250px"/>
                <el-table-column label="处理时间" align="center" prop="t5"/>
                <el-table-column label="处理人" align="center" prop="t6"/>
                <el-table-column label="处理结果" align="center" prop="t7"/>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                        >处理</el-button>
<!--                        <el-button-->
<!--                                size="mini"-->
<!--                                type="text"-->
<!--                                icon="el-icon-delete"-->
<!--                        >删除</el-button>-->
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
            <el-dialog :title="title" :visible.sync="open" width="700px"  append-to-body>
                <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
                    <!-- 新建时显示-->
                    <el-form-item v-if="dialogType" label="班组长" prop="teamLeaderName" class="add-form-input">
                        <el-input v-model="form.teamLeaderName" readonly v-on:click.native="inputClick"/>
                    </el-form-item>
                    <!-- 更新时显示 -->
                    <el-form-item v-else label="班组长" prop="teamLeaderId" class="add-form-input">
                        <el-select
                                v-model="form.teamLeaderId"
                                placeholder="请选择"
                                @change="teamLeaderChange($event, dataList2)"
                                clearable
                                size="small"
                                style="width: 200px"
                        >
                            <el-option
                                    v-for="option in dataList2"
                                    :key="option.id"
                                    :label="option.name"
                                    :value="option.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="劳务工种" prop="workTypeId" class="add-form-input">
                        <el-select
                                v-model="form.workTypeId"
                                placeholder="请选择"
                                @change="workTypeChange"
                                clearable
                                size="small"
                                style="width: 200px"
                        >
                            <el-option
                                    v-for="option in workTypeOption"
                                    :key="option.id"
                                    :label="option.workType"
                                    :value="option.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="班组名称" prop="teamName" class="add-form-input">
                        <el-input v-model="form.teamName"/>
                    </el-form-item>
                    <el-form-item label="分包单位" prop="subcontractorName" class="add-form-input">
                        <el-input v-model="form.subcontractorName" readonly/>
                    </el-form-item>
                    <el-form-item label="身份证号" prop="idCard" class="add-form-input">
                        <el-input v-model="form.idCard"/>
                    </el-form-item>
                    <el-form-item label="联系方式" prop="phone" class="add-form-input">
                        <el-input v-model="form.phone"/>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </el-dialog>
            <!-- 选择劳务人员信息弹出层 -->
            <el-dialog :title="title2" :visible.sync="open2" width="700px" append-to-body>
                <el-form v-show="showSearch2" ref="queryForm2" :model="queryParams2"
                         label-width="80px" style="padding-top: 20px;padding-left:10px;">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="姓名" prop="name"  label-position="right">
                                <el-input
                                        v-model="queryParams2.name"
                                        clearable
                                        size="small"
                                        @keyup.enter.native="handleQuery2"
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item label="身份证号" prop="idCard">
                                <el-input
                                        v-model="queryParams2.idCard"
                                        clearable
                                        size="small"
                                        @keyup.enter.native="handleQuery2"
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery2">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery2">重置</el-button>
                        </el-col>
                    </el-row>
                </el-form>
                <el-table v-loading="loading" :data="dataList2" height="250px" @row-dblclick="dobuleClick">
                    <el-table-column label="姓名" align="center" prop="name" width="120px"/>
                    <el-table-column label="身份证号" align="center" prop="idCard" width="150px"/>
                    <el-table-column label="家庭住址" align="center" prop="address"/>
                </el-table>
                <div class="p-contianer">
                    <el-pagination
                            :current-page="currentPage2"
                            :page-sizes="pageSizes2"
                            :page-size.sync="pageSize2"
                            :total="total2"
                            class="p-bar"
                            background
                            layout="prev, pager, next, sizes, total, jumper"
                            @size-change="handleSizeChange2"
                            @current-change="handleCurrentChange2"/>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {selectTeamList, addTeam, editTeam, getTeamById, deleteTeam, selectTeamLeaderList, selcetIdsAndWorktype, selectList} from '@/api/build/labour'
    import {selectAreaList} from '@/api/common'

    import Reminder from '@/components/Reminder'
    import XrHeader from '@/components/XrHeader'
    import request from '@/utils/request'
    import vPinyin from '@/utils/vuepy.js';
    export default {
        name: 'team',
        components: {
            Reminder,
            XrHeader
        },
        data() {
            return {
                // 遮罩层
                loading: false,
                // 显示搜索条件
                showSearch: true,
                // 弹出层显示搜索条件
                showSearch2: true,
                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,
                // 班组长信息弹出框分页
                currentPage2: 1,
                pageSize2: 5,
                pageSizes2: [5, 10, 20, 30, 40],
                total2: 0,
                // 表格数据
                dataList: [{t1:"C座A栋3F南走廊",t2:"非授权人员",t3:"2021-06-20 15:33",t4:"视频检测到未授权人员进入",t5:"2021-06-20 15:34",
                    t6:"李强",t7:"已阻止"},
                    {t1:"A座B栋3F",t2:"非授权人员",t3:"2021-06-21 15:33",t4:"视频检测到未授权人员进入",t5:"2021-06-21 15:35",
                        t6:"李强",t7:"派人前往"}],
                // 弹出层表格数据
                dataList2: [],
                // 弹出层标题
                title: '',
                // 是否显示弹出层
                open: false,
                // 弹出层标题
                title2: '',
                // 是否显示个人信息弹出层
                open2: false,
                // 提交平台状态字典数据
                sendStatusDict: [],
                // 查询参数
                queryParams: {
                    page: '',
                    limit: ''
                },
                // 弹出层查询参数
                queryParams2: {
                    page: '',
                    limit: '',
                    ifTeamLeader: 'Y'
                },
                // 表单参数
                form: {},
                // 判断对话框是新建还是修改 true为新建 false为修改
                dialogType: undefined,
                // 劳务工种
                workTypeOption: [],
                // 分包商下拉框
                subcontractorOptions: [],
                // 表单校验
                rules: {
                    teamLeaderName: [
                        { required: true, message: '班组长不能为空', trigger: 'change' }
                    ],
                    workTypeId: [
                        { required: true, message: '劳务工种不能为空', trigger: 'change' }
                    ],
                    teamName: [
                        { required: true, message: '班组名称不能为空', trigger: 'change' }
                    ],
                    subcontractorName: [
                        { required: true, message: '分包单位不能为空', trigger: 'change' }
                    ],
                    idCard: [
                        { required: true, message: '身份证号不能为空', trigger: 'change' }
                    ],
                    phone: [
                        { required: true, message: '联系方式不能为空', trigger: 'change' }
                    ]
                }
            }
        },
        created() {
            // this.getList()
            // this.initDicts()
            // this.selectWorkTypeList()
            // this.selectSubcontractorList()
        },
        watch: {
            'form.teamName': function (val, oldVal) {
                this.inputPY()
            },
        },
        methods: {
            /** 查询班组数据 **/
            getList() {
                this.loading = true
                this.queryParams.page = this.currentPage
                this.queryParams.limit = this.pageSize
                selectTeamList(this.queryParams).then(response => {
                    this.dataList = response.data.list
                    if (response.data.totalRow && Math.ceil(response.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
                        this.currentPage = this.currentPage - 1
                        this.getList()
                    } else {
                        this.total = response.data.totalRow
                        this.loading = false
                    }
                    this.loading = false
                }).catch(() => {
                    this.loading = false
                })
            },
            /** 查询分包商数据 **/
            selectSubcontractorList() {
                const params = {
                    pageType: 0
                }
                selectList(params).then(response => {
                    this.subcontractorOptions = response.data.list
                }).catch(() => {
                })
            },
            // 获取所有的劳务工种
            selectWorkTypeList(){
                selcetIdsAndWorktype().then(res => {
                    this.workTypeOption = res.data
                })
            },
            // 修改操作，班组长信息下拉框Change事件
            teamLeaderChange(value, datalist2){
                // 获取对应的选中对象
                let obj = {};
                obj = datalist2.find((item) => {
                    return item.id === value;
                })
                // 绑定数据到form
                this.form.teamLeaderName = obj.name
                this.form.teamLeaderId = obj.id
                this.form.idCard = obj.idCard
                this.form.phone = obj.phone
                this.form.subcontractorId = obj.subcontractor
                this.form.subcontractorName = obj.subcontractorName
                //this.form.workTypeId = parseInt(obj.workTypeId)

                // 调用工种的Change事件
                this.workTypeChange(this.form.workTypeId)
                this.$forceUpdate()
            },
            // 新建或修改工种Change事件
            workTypeChange(value){
                // 获取对应的选中对象
                let obj = {};
                obj = this.workTypeOption.find((item) => {
                    return item.id === value;
                })

                let name = '';
                if (this.form.teamLeaderName != undefined && this.form.teamLeaderName != null) {
                    name = this.form.teamLeaderName
                }
                // 拼接班组名称
                var tenamName = obj.workType + "【" + name + "】"
                this.$set(this.form, "teamName", tenamName)
                //this.form.teamName = obj.workType + "【" + name + "】"
            },
            // 查询劳务人员信息列表
            getTeamLeaderInfoList(isPage){
                var params = {}
                // 判断是否分页查询
                if (isPage === 0) {
                    // pageType = 0 取消分页
                    params = {
                        ifTeamLeader: "Y",
                        pageType: 0
                    }
                } else {
                    this.queryParams2.page = this.currentPage2
                    this.queryParams2.limit = this.pageSize2
                    params = this.queryParams2
                }

                this.loading = true
                selectTeamLeaderList(params).then(response => {
                    this.dataList2 = response.data.list
                    this.total2 = response.data.totalRow
                    this.loading = false
                }).catch(() => {
                    this.loading = false
                })
            },
            // 初始化字典数据
            initDicts() {
                this.getDicts('b_is_send_status').then(response => {
                    this.sendStatusDict = response.data
                })
            },
            // 平台状态字典翻译
            sendStatusFormat(row, column) {
                return this.selectDictLabel(this.sendStatusDict, row.isSendStatus)
            },
            // 拼音首拼
            inputPY() {
                let pinyin= vPinyin.chineseToPinYin(this.form.teamName);
                let SX = '';
                for (var i = 0; i < pinyin.length; i++) {
                    var c = pinyin.charAt(i);
                    if (/^[A-Z]+$/.test(c)) {
                        SX += c;
                    }
                }
                this.form.teamSpell  = SX
            },
            // 根据选择的人员信息绑定数据
            bindingData(row){
                this.form.teamLeaderId = row.id
                this.form.teamLeaderName = row.name
                this.$set(this.form, "idCard", row.idCard)
                this.$set(this.form, "phone", row.phone)
                //this.form.idCard = row.idCard
                //this.form.phone = row.phone
                this.form.subcontractorId = row.subcontractor
                this.form.subcontractorName = row.subcontractorName
                this.open2 = false
            },
            // 弹出层双击事件
            dobuleClick(row){
                this.bindingData(row)
            },
            // 班组长input点击事件
            inputClick() {
                // 弹出层表单重置
                this.resetForm('queryForm2')
                this.title2 = '选择劳务人员信息'
                this.open2 = true
                this.currentPage2 = 1
                this.pageSize2 = 5
                this.getTeamLeaderInfoList()
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
                this.currentPage = 1
                this.queryParams.limit = this.pageSize
                this.getList()
            },
            /** 弹出层搜索按钮操作 */
            handleQuery2() {
                this.currentPage2 = 1
                this.queryParams2.limit = this.pageSize2
                this.getTeamLeaderInfoList()
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.resetForm('queryForm')
                this.handleQuery()
            },
            /** 弹出层重置按钮操作 */
            resetQuery2() {
                this.resetForm('queryForm2')
                this.handleQuery2()
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.dialogType = true
                this.reset()
                this.open = true
                this.title = '添加班组信息'
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.dialogType = false
                this.reset()
                const id = row.id
                getTeamById(id).then(response => {
                    this.form = response.data
                    this.open = true
                    this.title = '修改班组信息'
                    // 查询班组长信息  pageType = 1 分页  pageType = 0 不分页
                    var isPage = 0
                    this.getTeamLeaderInfoList(isPage)
                })
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs['form'].validate(valid => {
                    if (valid) {
                        if (this.form.id != undefined) {
                            editTeam(this.form).then(response => {
                                if (response.code === 0) {
                                    this.msgSuccess('修改成功')
                                    this.open = false
                                    this.getList()
                                }
                            })
                        } else {
                            addTeam(this.form).then(response => {
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
                const id = row.id
                const teamName = row.teamName
                this.$confirm('是否确认删除班组"' + teamName + '"的数据项?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function() {
                    return deleteTeam(id)
                }).then(() => {
                    this.getList()
                    this.msgSuccess('删除成功')
                }).catch(function() {})
            },
            /** 导出按钮操作 */
            handleExport() {
                this.download('system/dict/type/export', {
                    ...this.queryParams
                }, `type_${new Date().getTime()}.xlsx`)
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
            // 弹出层更改每页展示数量
            handleSizeChange2(val) {
                this.pageSize2 = val
                this.getTeamLeaderInfoList()
            },
            // 弹出层更改当前页数
            handleCurrentChange2(val) {
                this.currentPage2 = val
                this.getTeamLeaderInfoList()
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

    .add-form-input{
        float: left;
    }

    .add-form::after{
        content: "";
        display: block;
        clear: both;
    }

    .add-form .el-form-item{
        width: 50%;
    }

    .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

    .link-type:focus:hover,.link-type:hover{color:#20a0ff}

    .el-table {
        border-top: 1px solid $xr-border-line-color;
    }

</style>