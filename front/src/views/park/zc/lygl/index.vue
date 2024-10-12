<template>
    <div class="main">
        <div class="main-body">
            <el-row  style="background-color: #fff">
                <el-col :span="4"  style="">
                    <el-card class="box-card" style="margin: 10px;height:calc(100vh - 170px);">
                        <div slot="header" class="clearfix">
<!--                            <span style="font-weight: bold;font-size: 16px">安全房间目录</span>-->
                            <el-button style="float: right; padding: 3px 0" type="text" @click="openCatalog" >新增楼栋</el-button>
                        </div>
                        <el-tree
                                :data="catalogData"
                                node-key="id"
                                default-expand-all
                                :expand-on-click-node="false"
                                :highlight-current="true"
                                :render-content="renderContent">
                        </el-tree>
                    </el-card>
                </el-col>
                <el-col :span="20" >
                    <el-card class="box-card" style="margin: 10px;height:calc(100vh - 170px);">
                        <el-button
                                class="xr-btn--orange"
                                type="primary"
                                icon="el-icon-plus"
                                size="mini"
                                style="position: absolute;top: 35px;right: 35px;z-index: 999"
                                @click="handleAdd" >新建房间</el-button>
                                <el-table v-loading="loading" :data="dataList" >
                                    <el-table-column label="楼栋" align="center" prop="t1" />
                                    <el-table-column label="房间编号" align="center" prop="materialCode" />
                                    <el-table-column label="房间名称" align="center" prop="materialName" />
                                    <el-table-column label="楼层" align="center" prop="t2" />
                                    <el-table-column label="房间类型" align="center" prop="materialType"/>
                                    <el-table-column label="房间状态" align="center" prop="preparedUnit" />
                                    <el-table-column label="是否禁用" align="center" prop="preparedBy"/>
                                    <el-table-column label="业主" align="center" prop="preparedNumber" width="120px"/>
                                    <el-table-column label="联系电话" align="center" prop="personCharge"/>
                                    <el-table-column label="证件号码" align="center" prop="personId" />
                                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160px">
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
                                            :page-sizes="pageSizes"
                                            :page-size.sync="pageSize"
                                            :total="total"
                                            class="p-bar"
                                            background
                                            layout="prev, pager, next, sizes, total, jumper"
                                            @size-change="handleSizeChange"
                                            @current-change="handleCurrentChange"/>
                                </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
        <!-- 新增或修改参数配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="catalogForm" :model="catalogForm" :rules="catalogRules" label-width="80px" class="add-form">
                <el-form-item label="上级名称" prop="name" class="add-form-input">
                    <el-input v-model.number="catalogForm.name" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="名称" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="序号" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="层数" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="类型" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="用途" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="朝向" prop="sort" class="add-form-input">
                    <el-input v-model.number="catalogForm.sort" placeholder="请输入" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="close">取 消</el-button>
            </div>
        </el-dialog>
        <Create
                v-if="createShow"
                :catalogId="catalogId"
                :id="id"
                width="500px" height="700px"
                @close="createShow = false"
                @save-success="getList"
        />
        <Update
                v-if="updateShow"
                :catalogId="catalogId"
                :id="id"
                @close="updateShow = false"
                @save-success="getList"
        />
        <Details
                v-if="detailsShow"
                :catalogId="catalogId"
                :id="id"
                @close="detailsShow = false"
                @save-success="getList"
        />
    </div>
</template>

<script>
    import { getList, addCatalog, getCatalogTree ,deleteCatalog,getCollectList,get,removeById,collect,deleteCollect,selectById } from '@/api/build/safetyMaterial'
    import XrHeader from '@/components/XrHeader'
    import Create from './Create'
    import Update from '@/views/build/safety/safetyMaterial/Update'
    import Details from '@/views/build/safety/safetyMaterial/Details'


    export default {
        name: 'safetyMaterial',
        components: {
            XrHeader,
            Create,
            Update,
            Details
        },
        data() {
            return {
                // 遮罩层
                loading: true,
                createShow: false,
                updateShow:false,
                detailsShow:false,
                tableHeight:document.documentElement.clientHeight - 340,
                // 是否显示弹出层
                open: false,
                title: '',
                // 分页
                currentPage: 1,
                pageSize: 10,
                pageSizes: [10, 20, 30, 40],
                total: 0,
                catalogData: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                activeName: 'all',
                catalogId:0,
                isAdd:true,
                id: 0,
                catalogRules: {
                    name: [
                        { required: true, message: '必填项', trigger: 'change' }
                    ],
                    sort: [
                        { required: true, message: '必填项', trigger: 'change' }
                    ],
                },
                catalogForm: {},
                dataList:[],
                dataCollectList:[]
            }
        },

        created() {
            this.init();
            this.getList();
        },
        methods: {
            init(){
                // getCatalogTree(0).then(response => {
                //    this.catalogData = this.catalogData.concat(response.data)
                //     this.loading = false
                // }).catch(() => {
                //     this.loading = false
                // })
                let dd = [{"id":7,"label":"东区A栋"},{"id":8,"label":"东区B栋"},{"id":8,"label":"南区C栋"},
                    // {"children":[{"id":10,"label":"安全技术标准"},
                    //         {"id":11,"label":"主管部门下发"},
                    //         {"id":12,"label":"法律法规"}],"id":9,"label":"安全操作规程"}
                            ]
                this.catalogData = this.catalogData.concat(dd)
            },
            getList(){
                // var queryParams = {};
                // queryParams.page = this.currentPage;
                // queryParams.limit = this.pageSize;
                // queryParams.catalogId = this.catalogId;
                // getList(queryParams).then(response => {
                //     this.dataList = response.data.list
                //     this.total = response.data.totalRow
                //     this.loading = false
                // }).catch(() => {
                //     this.loading = false
                // })
                // getCollectList(this.catalogId).then(response => {
                //     this.dataCollectList = response.data
                //     this.loading = false
                // }).catch(() => {
                //     this.loading = false
                // })
                this.dataList = [{"t1":"东区A栋","t2":"12层","materialCode":"A-B101","materialName":"B101","materialType":"商务","preparedUnit":"入驻",
                    "preparedBy":"否","preparedNumber":"逄震","personCharge":"13061460757","personId":"370203190010100205"},
                    {"t1":"东区A栋","t2":"12层","materialCode":"A-B201","materialName":"B201","materialType":"商务","preparedUnit":"入驻",
                        "preparedBy":"否","preparedNumber":"逄震","personCharge":"13061460757","personId":"370203190010100205"},
                    {"t1":"东区B栋","t2":"12层","materialCode":"B-B203","materialName":"B203","materialType":"商务","preparedUnit":"入驻",
                        "preparedBy":"否","preparedNumber":"","personCharge":"","personId":""},]
                this.loading = false
            },
            handleNodeClick(data) {
                console.log(data);
            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            close(){
                this.createShow = false;
                this.open = false;
                this.updateShow = false;
                this.detailsShow = false;
            },
            submitForm(){
                if(this.isAdd){
                    this.catalogForm.parentId = this.catalogId;
                }
                addCatalog(this.catalogForm).then(response => {
                    if (response.code === 0) {
                        if(this.isAdd){
                            this.msgSuccess('添加成功')
                        }else {
                            this.msgSuccess('修改成功')
                        }
                        this.open = false
                        this.init();
                        this.catalogForm = {};
                        this.isAdd = true;
                    }
                }).catch(() => {

                })

            },
            append(data) {
                this.title = "新建房间";
                this.catalogId = data.id;
                this.open = true;
            },

            remove(data,node) {
                console.log(node);
                this.$confirm('是否删除此数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log(2222)
                    deleteCatalog(data.id).then(response => {
                        console.log(response)
                        if (response.code === 0) {
                            this.msgSuccess('删除成功')
                            this.init();
                        }
                    }).catch(() => {})
                }).catch(() => {});

            },
            updata(data){
                this.title = "修改房间";
                this.catalogId = data.id;
                this.open = true;
                this.isAdd = false;
                selectById(data.id).then(response => {
                    console.log(response)
                    if (response.code === 0) {
                        this.catalogForm = response.data;
                    }
                }).catch(() => {})

            },
            renderContent(h, { node, data, store }) {
                if(data.id == 0){
                    return (
                        <span on-click={ () => this.checkCatalog(data) } style="display:block;width:100%;position: relative">
                        <span   >{node.label}</span>
                        </span>);
                }
                if(data.children == undefined || data.children == null || data.children == []){
                    return (
                        <span on-click={ () => this.checkCatalog(data) } style="display:block;width:100%;position: relative">
                        <span   >{node.label}</span>
                        <span style="position: absolute;right: 10px;top: 0px;">
                        <el-dropdown >
                        <el-button style="padding: 3px 10px;" type="primary" size="mini" type="info" plain >
                    <i class="el-icon-more"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item ><span on-click={ () => this.append(data) } style="display:block;width:100%;">添加下级</span></el-dropdown-item>
                    <el-dropdown-item ><span on-click={ () => this.remove(data,node) } style="display:block;width:100%;">删除</span></el-dropdown-item>
                    <el-dropdown-item><span on-click={ () => this.updata(data) } style="display:block;width:100%;">修改</span></el-dropdown-item>
                    </el-dropdown-menu>
                    </el-dropdown>
                    </span>
                    </span>);
                }else {
                    return (
                        <span on-click={ () => this.checkCatalog(data) } style="display:block;width:100%;position: relative">
                        <span   >{node.label}</span>
                        <span style="position: absolute;right: 10px;top: 0px;">
                        <el-dropdown >
                        <el-button style="padding: 3px 10px;" type="primary" size="mini" type="info" plain >
                    <i class="el-icon-more"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item ><span on-click={ () => this.append(data) } style="display:block;width:100%;">添加下级</span></el-dropdown-item>
                    <el-dropdown-item><span on-click={ () => this.updata(data) } style="display:block;width:100%;">修改</span></el-dropdown-item>
                    </el-dropdown-menu>
                    </el-dropdown>
                    </span>
                    </span>);
                }
            },
            openCatalog(){
                this.open = true;
                this.catalogId = 0;
                this.title = "新建楼栋";
            },
            checkCatalog(data){
                this.catalogId = data.id;
                this.getList();
                console.log(data)
            },
            handleAdd(){
                if(this.catalogId == 0){
                    this.$message({
                        message: '请选择要添加的楼栋',
                        type: 'warning'
                    });
                }else {
                    this.createShow = true;
                }

            },
            details(data){
                console.log(data)
                this.id = data.id;
                this.detailsShow = true;
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
            shouc(data){
                console.log(data)
                if(data.collect == "1"){
                    collect(data.id).then(response => {
                        if (response.code === 0) {
                            this.getList()
                        }
                    }).catch(() => {})
                }else {
                    deleteCollect(data.id).then(response => {
                        if (response.code === 0) {
                            this.getList()
                        }
                    }).catch(() => {})
                }
            },
            handleUpdate(data){
                this.id = data.id;
                this.updateShow = true;
            },
            handleDelete(data){
                this.$confirm('是否删除此数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    removeById(data.id).then(response => {
                        if (response.code === 0) {
                            this.msgSuccess('删除成功')
                            this.getList();
                        }
                    }).catch(() => {})
                }).catch(() => {});
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
    .el-header {
        background-color: #B3C0D1;
        color: #333;
        line-height: 60px;
    }
    .el-aside {
        color: #333;
    }
    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
    .custom-tree-but{
        display: inline-block;
        position: relative;
        right: 10px;
        top: 0px;
    }
    .custom-tree-button{
        padding: 3px 10px;
    }
    .xl-but{
        background-color: #CCCCCC;
        color: #000000;
    }
</style>
