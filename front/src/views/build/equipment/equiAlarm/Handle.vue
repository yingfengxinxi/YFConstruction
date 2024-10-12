<template>
    <xr-create
        :loading="loading"
        :title="title"
        :confirm-button-text="'保存'"
        @close="close"
        @save="debouncedSaveField(true)"
    >
        <create-sections title="基本信息">
            <el-form
                ref="form"
                :model="form"
                :rules="rules"
                label-position="top"
                label-width="100px"
                class="form"
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

                <el-row>

                </el-row>

            </el-form>
            <wk-upload-file
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
            return this.action.type === 'handle' ? '处理详情' : '新建监测点'
        },
    },
    watch: {},
    created() {
        this.debouncedSaveField = debounce(300, this.save)
        //考虑附件ID
        if (this.action.type == "handle") {
            this.batchId = guid()
            this.form.batchId = this.batchId
           
        } 
    },
    methods: {
        /**
         * 保存
         */
        save(isSubmit) {
            this.$refs['form'].validate(valid => {
                if (valid) {
                    var fileFlag = this.$refs['wkUploadFile'].submitAsync();
                    if (!fileFlag) {
                        this.$message({
                            type: 'error',
                            message: '图片上传异常'
                        })
                        return;
                    }
                    var params = this.form;
                    params.id = this.action.id;
                    params.handleState='1'
                    params.handleTime = this.getNowFormatDate(params.handleTime);
                    console.log(params);
                    // return ;
                    this.loading = true;
                    handleSave(params).then(response => {
                        if (response.code === 0) {
                            this.$message.success(
                                this.action.type == 'handle' ? '处理成功' : '添加成功'
                            )
                            this.close()
                            // 保存成功
                            this.$emit('save-success', {
                                type: 'receivables'
                            })
                        } else {
                            this.$message.error(
                                response.msg
                            )
                        }
                        this.loading = false;
                    }).catch(err => {
                        this.loading = false;
                    })

                }
            })
        },
        /**
         * 关闭
         */
        close() {
            console.log("close")
            this.$emit('close')
        },

        getNowFormatDate(time) {
            var date = new Date(time);
            var seperator1 = "-";
            var seperator2 = ":";
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + date.getHours() + seperator2 + date.getMinutes()
                + seperator2 + date.getSeconds();
            return currentdate;
        }
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
