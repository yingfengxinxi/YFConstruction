<template>
    <xr-create
        :loading="loading"
        :title="title"
        :confirm-button-text="'保存'"
        @close="close"
        @save="debouncedSaveField(true)"
    >
        <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-position="top"
            label-width="100px"
            class="form"
        >
            <create-sections title="基本信息">

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="库房编号"
                            prop="roomNum"
                        >
                            <el-input
                                v-model="form.roomNum"
                                placeholder="请输入库房编号"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="库房名称"
                            prop="roomName"
                        >
                            <el-input
                                v-model="form.roomName"
                                placeholder="请输入库房名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="库房存储材料类型"
                            prop="roomMateriType"
                        >
                            <el-select
                                v-model="form.roomMateriType"
                                placeholder="库房存储材料类型"
                                clearable
                                size="small"
                                style="width: 400px"
                            >
                                <el-option
                                    v-for="temp in roomMateriTypeList"
                                    :key="temp.dictValue"
                                    :label="temp.dictLabel"
                                    :value="temp.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="库房管理员姓名"
                            prop="roomAdminName"
                        >
                            <el-input
                                v-model="form.roomAdminName"
                                placeholder="请输入库房管理员姓名"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item
                            label="库房管理员身份证号"
                            prop="roomAdminCard"
                        >
                            <el-input
                                v-model="form.roomAdminCard"
                                placeholder="请输入库房管理员身份证号"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item
                            label="库房管理员性别"
                            prop="roomAdminSex"
                        >
                            <el-select
                                v-model="form.roomAdminSex"
                                placeholder="请选择性别"
                                clearable
                                size="small"
                                style="width: 260px"
                               
                            >
                                <el-option
                                    v-for="dict in sexList"
                                    :key="dict.dictValue"
                                    :label="dict.dictLabel"
                                    :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item
                            label="库房管理员联系方式"
                            prop="roomAdminPhone"
                        >
                            <el-input
                                v-model="form.roomAdminPhone"
                                placeholder="请输入库房管理员联系方式"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </create-sections>
        </el-form>

    </xr-create>
</template>

<script>
import XrCreate from "@/components/XrCreate";
import CreateSections from "@/components/CreateSections";

import WkUploadFile from "@/components/CreateCom/WkUploadFile";

import { guid } from "@/utils";

import { debounce } from "throttle-debounce";

import { saveOrUpdate, getById } from "../api/roomInfoMaintain";
// //业务审批Copy:审批流程
import ExamineMixin from "@/components/common/examine/mixins/examine";
import CreateExamine from "@/components/common/examine/create";

export default {
    // 新建编辑
    name: "roomInfoMaintainCreate",
    components: {
        XrCreate,
        CreateSections,
        WkUploadFile,
        CreateExamine
    },
    mixins: [ExamineMixin], //业务审批Copy:审批
    props: {
        action: {
            type: Object,
            default: () => {
                return {
                    type: "save",
                    id: "",
                    data: {}
                };
            }
        },
        businessType: ""
    },
    data() {
        return {
            loading: false,
            form: {},
            batchId: "",

            rules: {
                roomNum: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                roomName: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                roomMateriType: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                roomAdminName: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                roomAdminCard: [
                    { required: true, message: "必填项不得为空", trigger: "blur" },
                    {

                        validator: function (rule, value, callback) {

                            let _IDRe18 = /^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
                            let _IDre15 = /^([1-6][1-9]|50)\d{4}\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}$/;
                            let _IDRE = '';
                            if (value.length == 18) {
                                _IDRE = _IDRe18;
                            } else if (value.length == 15) {
                                _IDRE = _IDre15;
                            } else {
                                _IDRE = _IDRe18;
                            }
                            if (value != "" && value != undefined) {
                                if (_IDRE.test(value) == true) {
                                    callback();
                                } else {
                                    callback(new Error("请输入正确的身份证号"));
                                }
                            } else {
                                callback(new Error("请输入正确的身份证号"));
                            }
                        },
                        trigger: "blur"
                    }
                ],
                roomAdminSex: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                roomAdminPhone: [
                    { required: true, message: '必填项不得为空', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (value != "" && value != undefined) {
                                if (/^1[34578]\d{9}$/.test(value) == true) {
                                    callback();
                                } else {
                                    callback(new Error("请输入正确的手机号"));
                                }
                            } else {
                                callback(new Error("请输入正确的手机号"));
                            }
                        },
                        trigger: "blur"
                    }
                ],
            },
            //字典项数据
            roomMateriTypeList: [],
            sexList: [],
        };
    },
    computed: {
        title() {
            return this.action.type === "update" ? "编辑库房信息" : "新建库房信息";
        }
    },
    watch: {},
    created() {
        this.debouncedSaveField = debounce(300, this.save);
        console.log(this.action.type);
        console.log(this.action)
        //获取字典项数据
        this.roomMateriTypeList = this.action.roomMateriTypeList;
        this.sexList = this.action.sexList
        if (this.action.type == "update") {
            this.getDetail();
             this.loading = true;
        }
    },

    methods: {
        /**
         * 获取详情
         * */
        getDetail() {
            getById(this.action.id)
                .then(res => {
                    this.form = res.data;
                    this.batchId = res.data.batchId;
                    console.log(res.data);
                     this.loading = false;
                })
                .catch(() => { this.loading = false; });
        },
        /**
         * 保存
         */
        save(isSubmit) {
            
            var params = null;
            this.$refs["form"].validate(valid => {
                if(valid){
                     this.loading = true;
                params = this.form;
                 params = this.form;
                console.log(params);
                console.log(this.action.type);
                saveOrUpdate(params)
                    .then(response => {
                        console.log(response.code);
                        if (response.code === 0) {
                            this.$message.success(
                                this.action.type == "update" ? "编辑成功" : "添加成功"
                            );
                            this.close();
                            // 保存成功
                            this.$emit("save-success", {
                                type: "receivables"
                            });
                        } else {
                            this.$message.error(response.msg);
                        }
                        this.loading = false;
                    })
                    .catch(err => {
                        this.loading = false;
                    });
                }
               
               
            });
        },
        /**
         * 关闭
         */
        close() {
            console.log("close");
            this.$emit("close");
        }
    }
};
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
</style>
