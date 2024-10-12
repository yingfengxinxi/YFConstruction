<template>
    <div class="handle-box">
        <div class="title" style="float: left;line-height: 3;width: calc(100% - 250px);">拖动以下模块可设置模块位置，点击开启/关闭按钮可设置模块是否在仪表盘显示，点击保存按钮即可生效。</div>
        <div class="title" style="float: left; margin-bottom: 5px;">
            <xr-radio-menu
                    :options="rangeOptions"
                    v-model="filterDataType"
                    width="170"
                    @select="radioMenuSelect">
                <flexbox slot="reference" class="user-box">
                    <span class="username">{{ avatarData.realname }}</span>
                    <span class="el-icon-caret-bottom icon" style="margin-right: 0px;margin-left: auto;"/>
                </flexbox>
            </xr-radio-menu>
        </div>
        <div class="title" style="float: right;margin: 2px auto;"><el-button
                type="primary"
                @click.native="handleConfirm">保存</el-button></div>
        <flexbox
                align="stretch"
                class="section">
            <div class="left">
                <draggable
                        v-model="sortLeft"
                        :group="{ name: 'sort'}"
                        :options="{ forceFallback: false }"
                        class="draggable-box">
                    <set-sort-item
                            v-for="(item, index) in sortLeft"
                            :key="index"
                            :title="item.modelName"
                            :icon="item.icon"
                            :icon-color="item.iconColor"
                            :img="getImgBase64(item.img)"
                            :editable="true"
                            :modelId="item.modelId"
                            @updateModelName="updateModelName"
                            class="content"
                    >
                        <el-switch
                                slot="header"
                                v-model="item.show"/>
                    </set-sort-item>
                </draggable>
            </div>
            <div class="right">
                <draggable
                        v-model="sortRight"
                        :group="{ name: 'sort'}"
                        :options="{ forceFallback: false }"
                        class="draggable-box">
                    <set-sort-item
                            v-for="(item, index) in sortRight"
                            :key="index"
                            :title="item.modelName"
                            :icon="item.icon"
                            :icon-color="item.iconColor"
                            :img="getImgBase64(item.img)"
                            :editable="true"
                            :modelId="item.modelId"
                            @updateModelName="updateModelName"
                            class="content"
                    >
                        <el-switch
                                slot="header"
                                v-model="item.show"/>
                    </set-sort-item>
                </draggable>
            </div>
        </flexbox>
    </div>
</template>

<script>
    import {
        crmIndexSortAPI,
        crmIndexSetSortAPI,
        updateModelNameAPI
    } from '@/api/admin/instrument'

    import XrRadioMenu from '@/components/Menu/XrRadioMenu'
    import SetSortItem from '../../../crm/workbench/components/SetSortItem'
    import draggable from 'vuedraggable'
    import Lockr from "lockr";

    export default {
        name: "Instrument",
        components: {
            SetSortItem,
            XrRadioMenu,
            draggable
        },
        mixins: [],
        props: {
            // visible: {
            //     type: Boolean,
            //     required: true,
            //     default: false
            // }
            roleId:{
                    type: Number,
                    required: true,
                    default: false
                }
        },
        data() {
            return {
                sortLeft: [],
                sortRight: [],
                rangeOptions: [
                    { label: '本人', command: 1 },
                    { label: '本人及下属', command: 2 },
                    { label: '本部门', command: 3 },
                    { label: '本部门及下属部门', command: 4 },
                    { label: '全部（可自定义）', command: 5 }
                ],
                membersDepVisible: false,
                filterDataType: 2,
                filterValue: {
                    dataType: 2,
                    users: [],
                    strucs: [],
                    timeLine: {
                        type: 'default',
                        value: 'month'
                    }
                }
            }
        },
        computed: {
            avatarData() {
                if (this.filterValue.dataType === 'custom') {
                    const users = this.filterValue.users || []
                    if (users.length) {
                        return users[0]
                    }
                    const strucs = this.filterValue.strucs || []
                    if (strucs.length) {
                        return {
                            realname: strucs[0].name,
                            img: ''
                        }
                    }
                }

                if (this.filterValue.dataType == 1) {
                    return this.userInfo
                }
                return {
                    showIcon: true,
                    realname: {
                        1: '本人',
                        2: '本人及下属',
                        3: '本部门',
                        4: '本部门及下属部门',
                        5: '全部（可自定义）'

                    }[this.filterValue.dataType]
                }
            }
        },
        watch: {},
        created() {
            this.getModelSort()
        },
        methods: {
            getImgBase64(imgUrl) {
                return require('@/assets/'+imgUrl);
                // return require('@/assets/img/skeleton/sort-chart.png')
            },
            /**
             * 排序
             */
            getModelSort(param) {
                let roleIdParam = this.roleId
                if(param){
                    roleIdParam = param
                }
                /**
                 * 1 合同金额目标及完成情况
                 * 2 数据汇总
                 * 3 回款金额目标及完成情况
                 * 4 业绩指标完成率 (回款金额)
                 * 5 销售漏斗
                 * 6 遗忘提醒
                 * 7 排行榜
                 */
                crmIndexSortAPI(roleIdParam).then(res => {
                    const left = res.data.left || []
                    const right = res.data.right || []

                    this.sortLeft = left.map(item => {
                        item.show = item.isHidden == 0 // 0显示 1隐藏
                        return item
                    })

                    this.sortRight = right.map(item => {
                        item.show = item.isHidden == 0
                        return item
                    })
                    this.filterDataType = (left.length!=0?left[0].dataType:right[0].dataType)
                    this.filterValue.dataType = this.filterDataType
                    // console.log("==filterDataType"+this.filterDataType)
                })
            },
            /**
             * 仪表盘名称修改
             */
            updateModelName(params){
                updateModelNameAPI(params)
                    .then(res => {
                        this.getModelSort()
                    })
            },
            timeTypeChange(data) {
                this.filterValue.timeLine = data
            },
            radioMenuSelect() {
                if (this.filterDataType != 'custom') {
                    Lockr.set('crmWorkbenchRangeFilter', this.filterDataType)
                    this.filterValue.dataType = this.filterDataType
                } else {
                    this.membersDepVisible = true
                }
            },
            handleConfirm() {
                const params = {roleId:this.roleId, dataType:this.filterDataType}
                params.left = this.sortLeft.map(item => {
                    const data = {}
                    data.isHidden = item.show ? 0 : 1
                    data.modelId = item.modelId
                    return data
                })
                params.right = this.sortRight.map(item => {
                    const data = {}
                    data.isHidden = item.show ? 0 : 1
                    data.modelId = item.modelId
                    return data
                })
                crmIndexSetSortAPI(params)
                    .then(res => {
                        this.$message({
                            type: 'success',
                            message: '操作成功'
                        })
                        // this.$emit('save')
                        // this.handleCancel()
                    })
            }
        }
    }
</script>

<style scoped>
    .user-box {
        width: 170px;
        height: 36px;
        padding: 4px 7px;
        border: 1px solid #E1E1E1;
        border-radius: $xr-border-radius-base;
        background-color: white;
        margin-right: 20px;
        display: flex;
        cursor: pointer;
        float: right;
    }
    .username {
        font-size: 12px;
        margin: 0 8px;
    }
    .handle-box {
        /*background-color: #efefef;*/
        color: #333;
        font-size: 12px;
        padding: 0px 15px ;
    }
    .title {
        color: #999;
    }

    .section {
        margin-top: 12px;
        overflow: auto;
        user-select: none;
    }

    .left {
        width: calc(60.5% - 12px);
        margin-right: 12px;
    }
    .right {
        width: 39.5%;
    }

    .content {
        height: 150px;
    }
    /deep/ .el-switch {
        position: absolute;
        right: 0;
        top: 0;
    }
    .content + .content {
        margin-top: 12px;

    }

    .draggable-box {
        height: 100%;
    }
</style>