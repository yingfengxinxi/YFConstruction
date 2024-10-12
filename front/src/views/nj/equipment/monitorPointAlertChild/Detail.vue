<template>
    <xr-create
        :loading="loading"
        :title="title"
        @close="close"
        :showSave="false"
    >
        <create-sections title="基本信息">
            <el-form
                ref="form"
                :model="form"
                :rules="rules"
                label-position="top"
                label-width="100px"
                class="form"
                :disabled="true"
            >
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="处理时间"
                            prop="handleTime"
                        >
                            <el-date-picker
                                v-model="form.handleTime"
                                type="datetime"
                                placeholder="请选择处理时间"
                                value-format="yyyy-MM-dd HH:mm:ss"
                            >
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="处理人"
                            prop="handleBy"
                        >
                            <el-input
                                v-model="form.handleBy"
                                placeholder="请输入处理人"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="24">
                        <el-form-item
                            label="处理措施"
                            prop="handleMeasures"
                        >
                            <el-input
                                type="textarea"
                                :rows="4"
                                v-model="form.handleMeasures"
                                placeholder="请输入处理措施"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item
                            label="处理后结论"
                            prop="handleConclu"
                        >
                            <el-input
                                type="textarea"
                                :rows="4"
                                v-model="form.handleConclu"
                                placeholder="请输入处理后结论"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <wk-upload-file
            :disabled="true"
                ref="wkUploadFile"
                name="file"
                multiple
                accept="image/*"
                :autoUpload="false"
                :batchId="batchId"
            ></wk-upload-file>
        </create-sections>
    </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";
import { guid } from '@/utils'

import { debounce } from 'throttle-debounce'

import { selectById, handleSave } from "../api/monitorPointAlert";

export default {
    // 新建编辑
    name: 'monitorPointAlertHandle',
    components: {
        XrCreate,
        CreateSections,
        WkUploadFile
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
        action: {
            type: Object,
            default: () => {
                return {
                    type: 'save',
                    id: '',
                    data: {}
                }
            }
        },
        dictData: {},
    },
    data() {
        return {
            loading: false,
            batchId: '',//附件批次id
            isDisable: true,
            form: {
                handleTime: new Date(),
            },
            rules: {
                handleTime: [
                    { required: true, message: '必填项不得为空', trigger: 'blur' }
                ],
                handleBy: [
                    { required: true, message: '必填项不得为空', trigger: 'blur' }
                ],
                handleMeasures: [
                    { required: true, message: '必填项不得为空', trigger: 'blur' }
                ],
                handleConclu: [
                    { required: true, message: '必填项不得为空', trigger: 'blur' }
                ],
            },
        }
    },
    computed: {
        title() {
            return this.action.type === 'handle' ? '处理详情' : '处理详情查看'
        },
    },
    watch: {},
    created() {
        this.debouncedSaveField = debounce(300, this.save)
        //考虑附件ID
            this.getDetail();
        
    },
    methods: {
        /**
         * 获取详情
         * */
        getDetail() {
            selectById(this.action.id)
                .then(res => {
                    console.log(res);
                    this.form = res.data;
                    this.batchId = res.data.batchId;
                    if (res.data.handleBy != null||res.data.handleConclu != null||res.data.handleMeasures != null||res.data.handleTime != null||res.data.handleTime != null) {
                        this.isDisable = false;
                    }
                })
                .catch(() => {
                })
        },
       
        /**
         * 关闭
         */
        close() {
            console.log("close")
            this.$emit('close')
        },
    },
    mounted() {
    },
    beforeDestroy() {
    },
}
</script>

<style lang="scss" scoped>
.form /deep/ .el-form-item .el-form-item__content {
  width: calc(100% - 10px);
}

.form /deep/ .el-form-item__label {
  padding: 0px;
}
.form /deep/ .el-date-editor {
  width: 100%;
}
.c-view{
	padding: 65px 0 !important;
}
</style>
