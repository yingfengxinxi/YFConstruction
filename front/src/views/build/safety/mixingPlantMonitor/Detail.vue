<template>
    <xr-create
        :loading="loading"
        :title="title"
        @close="close"
        :showSave="false"
    >
        <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-position="top"
            label-width="100px"
            class="form"
             :disabled="true"
        >
            <create-sections title="基本信息">

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="拌合站名称"
                            prop="mixingPlantName"
                        >
                            <el-input
                                v-model="form.mixingPlantName"
                                placeholder="请输入拌合站名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="砼强度等级"
                            prop="concreteGrade"
                        >
                            <el-select
                                v-model="form.concreteGrade"
                                placeholder="请选择砼强度等级"
                                clearable
                                size="small"
                                style="width: 400px"
                            >
                                <el-option
                                    v-for="temp in strengGradeList"
                                    :key="temp.dictValue"
                                    :label="temp.dictLabel"
                                    :value="temp.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="单位工程"
                            prop="unitWorks"
                        >
                            <el-input
                                v-model="form.unitWorks"
                                placeholder="请输入单位工程"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="工程部位"
                            prop="proLocation"
                        >
                            <el-input
                                v-model="form.proLocation"
                                placeholder="请输入工程部位"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="试验室配合比"
                            prop="laboratoryMix"
                        >
                            <el-input
                                v-model="form.laboratoryMix"
                                placeholder="请输入试验室配合比"
                            ></el-input>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item
                            label="施工配合比"
                            prop="constructionMix"
                        >
                            <el-input
                                v-model="form.constructionMix"
                                placeholder="请输入施工配合比"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="材料名称"
                            prop="materialName"
                        >
                            <el-input
                                v-model="form.materialName"
                                placeholder="请输入材料名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="材料标准重量(T)"
                            prop="materialWeight"
                        >
                            <el-input
                                v-model="form.materialWeight"
                                placeholder="请输入材料标准重量"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="实际重量(T)"
                            prop="actualWeight"
                        >
                            <el-input
                                v-model="form.actualWeight"
                                placeholder="请输入实际重量"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="允许误差"
                            prop="allowableError"
                        >
                            <el-input
                                v-model="form.allowableError"
                                placeholder="请输入允许误差"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="实际误差"
                            prop="actualError"
                        >
                            <el-input
                                v-model="form.actualError"
                                placeholder="请输入实际误差"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="报警级别"
                            prop="alarmLevel"
                        >
                            <el-select
                                v-model="form.alarmLevel"
                                placeholder="请选择报警级别"
                                clearable
                                size="small"
                                style="width: 400px"
                            >
                                <el-option
                                    v-for="dict in AlarmLevelList"
                                    :key="dict.dictValue"
                                    :label="dict.dictLabel"
                                    :value="dict.dictValue"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>

                    <el-col :span="12">
                        <el-form-item
                            label="报警时间"
                            prop="alarmTime"
                        >
                            <el-date-picker
                                v-model="form.alarmTime"
                                type="datetime"
                                placeholder="请选择报警时间"
                                value-format="yyyy-MM-dd HH:mm:ss"
                            >
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
				<!--图片上传-->
				<wk-upload-img
				  name="file"
					ref="wkUploadImg"
				  multiple
				  accept="image/*"
				  list-type="picture-card"
				  :autoUpload="false"
				  :title="'图片上传'"
				  :batchId="batchId"
				></wk-upload-img>
            </create-sections>
        </el-form>

    </xr-create>
</template>

<script>
import XrCreate from "@/components/XrCreate";
import CreateSections from "@/components/CreateSections";

import WkUploadImg from '@/components/CreateCom/WkUploadImg'

import { guid } from "@/utils";

import { debounce } from "throttle-debounce";

import { saveOrUpdate, getById } from "../api/mixingPlantMonitor";
// //业务审批Copy:审批流程
import ExamineMixin from "@/components/common/examine/mixins/examine";
import CreateExamine from "@/components/common/examine/create";

export default {
    // 新建编辑
    name: "roomInfoMaintainCreate",
    components: {
        XrCreate,
        CreateSections,
        WkUploadImg,
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
                mixingPlantName: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                concreteGrade: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                unitWorks: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                proLocation: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                laboratoryMix: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 constructionMix: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 materialName: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 materialWeight: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 actualWeight: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 allowableError: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 actualError: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 alarmLevel: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 alarmTime: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 handleMeasures: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
                 handleConclusion: [
                    { required: true, message: "必填项不得为空", trigger: "blur" }
                ],
            },
            //字典项数据
            strengGradeList: [],
            AlarmLevelList: [],
        };
    },
    computed: {
        title() {
            return this.action.type === "update" ? "编辑报警信息" : (this.action.type==='show'?"查看报警信息":"新建报警信息");
        }
    },
    watch: {},
    created() {
        this.debouncedSaveField = debounce(300, this.save);
        // console.log(this.action.type);
        // console.log(this.action)
        // console.log(this.action.type == "show")
        //获取字典项数据
        this.strengGradeList = this.action.strengGrade;
        this.AlarmLevelList = this.action.AlarmLevel
        if (this.action.type == "show") {
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
                    // console.log(res.data);
                    this.loading = false;
                })
                .catch(() => { this.loading = false; });
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
