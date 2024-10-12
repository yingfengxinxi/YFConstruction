<template>
    <xr-create :loading="loading" :title="title" @close="close" :showSave="false">
        <div class="main">
            <div class="main-body">
                <!-- 培训记录 -->
                <el-table v-loading="loading" :height="tableHeight-250" :data="dataList" @row-click="rowClick">
                    <el-table-column :formatter="trainingPlanIdFc" label="培训计划" align="center" prop="trainingPlanId" show-overflow-tooltip />
                    <el-table-column :formatter="trainingTypeFc" label="培训类型" align="center" prop="trainingType" show-overflow-tooltip />
                    <el-table-column label="培训时间" align="center" :formatter="formatterTime" prop="trainingTime" show-overflow-tooltip />
                    <el-table-column label="培训地点" align="center" prop="trainingAddress" show-overflow-tooltip />
                    <el-table-column label="培训内容" align="center" prop="trainingContent" show-overflow-tooltip />
                    <el-table-column label="授课人" align="center" prop="trainingTeaching" show-overflow-tooltip />
                    <el-table-column label="课时" align="center" prop="trainingTeachingTime" show-overflow-tooltip />
                    <el-table-column label="负责人" align="center" prop="trainingPeople" show-overflow-tooltip />
<!--                    <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip />-->
                </el-table>
                <!-- <div class="p-contianer">
                    <el-pagination :current-page="rostercurrentPage" :total="rostertotal" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="rosterHandleSizeChange" @current-change="rosterHandleCurrentChange" />
                </div> -->
                <!--  -->
                <el-table v-loading="loadUsering" :data="membersList" height="240px">
                    <el-table-column label="参加人员" align="center" prop="infocollectionName" show-overflow-tooltip  />
                    <el-table-column label="班组名" align="center" prop="infocollectionTeamName" :formatter="teamNameFormat2" show-overflow-tooltip />
                </el-table>
            </div>
        </div>
    </xr-create>
</template>

<script>

// import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import request from '@/utils/request'
import SelectCol from '@/views/admin/components/selectCol'
import { guid } from '@/utils'
import { getWTList, getListByFBS, getListByBZ } from '@/api/build/infocollection/infocoll'
import XrCreate from '@/components/XrCreate'

export default {
    name: 'safetyTraining',
    components: {
        // Reminder,
        XrHeader,
        SelectCol,
        XrCreate
    },
    props: {
        id: '',
    },
    data () {
        return {
            // 遮罩层
            loading: true,
            loadUsering: false,
            //table高度控制
            tableHeight: document.documentElement.clientHeight - 280,


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
            // 培训成员数据
            membersList: [],
            // 弹出层标题
            title: '执行记录',
            // 题目分类  字典表safety_training_type
            trainingType: [],
            //培训计划
            trainPlanNames: [],
            // 班组数据信息
            BZList: [],
            // 查询参数
            queryParams: {
                // page: this.currentPage,
                // limit: this.pageSize,
                // hydrantCode: undefined,
                // pipeId: undefined
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                trainingPlanId: [
                    { required: true, message: '请选择培训计划', trigger: 'blur' }
                ],
                trainingType: [
                    { required: true, message: '请选择培训类型', trigger: 'blur' }
                ],
                trainingTime: [
                    { required: true, message: '培训时间不能为空', trigger: 'blur' }
                ],
                trainingAddress: [
                    { required: true, message: '培训地点不能为空', trigger: 'blur' }
                ],
                trainingContent: [
                    { required: true, message: '培训内容不能为空', trigger: 'blur' }
                ],
                trainingTeaching: [
                    { required: true, message: '授课人不能为空', trigger: 'blur' }
                ],
                trainingTeachingTime: [
                    { required: true, message: '课时不能为空', trigger: 'blur' }
                ],
                trainingTeachingAddress: [
                    { required: true, message: '授课地点不能为空', trigger: 'blur' }
                ],
                trainingPeople: [
                    { required: true, message: '负责人不能为空', trigger: 'blur' }
                ]
            }
        }
    },
    watch: {
        $route (to, from) {
            request({
                url: 'build/safetyEducationPlan/list',
                method: 'post',
                data: {},
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                this.trainPlanNames = response.data.list
            })
        }
    },
    created () {
        this.getList()
        this.getListByBZ();
        // 题目分类
        this.getDicts('safety_training_type').then(response => {
            this.trainingType = response.data
        })
        request({
            url: 'build/safetyEducationPlan/list',
            method: 'post',
            data: {},
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        }).then(response => {
            this.trainPlanNames = response.data.list

        })
    },
    methods: {
        /**
        * 关闭
        */
        close () {
            this.$emit('close')
        },
        rowClick (row, column, event) {
            this.loadUsering = true
            var pid = row.id
            request({
                url: 'build/bSafetySafetyTrainingMembers/selectByPId/' + pid,
                method: 'get'
            }).then(response => {
                this.membersList = response.data
                this.loadUsering = false
            }).catch(() => {
                this.loadUsering = false
            })
        },
        selectChanged (val) {
            let that = this;
            this.trainPlanNames.forEach(function (obj, index) {
                if (val === obj.id) {
                    that.$set(that.form, "trainingType", obj.trainType);
                }
            })

        },
        /** 查询数据列表 */
        getList () {
            this.loading = true
            this.queryParams.trainingPlanId = this.id
            request({
                url: 'build/bSafetySafetyTraining/list',
                method: 'post',
                data: this.queryParams,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                this.dataList = response.data.list
                this.loading = false
            }).catch(() => {
                this.loading = false
            })
        },
        // 题目分类 
        trainingTypeFc (row, column) {
            return this.selectDictLabel(this.trainingType, row.trainingType)
        },
        // 培训计划
        trainingPlanIdFc (row, column) {
            return this.CustomFormat(this.trainPlanNames, row.trainingPlanId)
        },
        CustomFormat (datas, value) {
            var actions = [];
            Object.keys(datas).some(key => {
                if (datas[key].id == "" + value) {
                    actions.push(datas[key].trainPlanName);
                    return true;
                }
            });
            return actions.join("");
        },
        /** 日期去除时分秒 */
        formatterTime (row, column) {
            let data = row[column.property]
            return /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
        },
        //班组格式化
        teamNameFormat2 (row, column) {
            var datas = this.BZList
            var value = row.infocollectionTeamName
            var actions = []

            Object.keys(datas).some((key) => {
                if (datas[key].id == ('' + value)) {
                    actions.push(datas[key].teamName)
                    return true
                }
            })
            return actions.join('')
        },
        /** 查询所有班组名称 */
        getListByBZ () {
            getListByBZ().then(response => {
                this.BZList = response.data
                this.loading = false
            }).catch(() => {
                this.loading = false
            })
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
