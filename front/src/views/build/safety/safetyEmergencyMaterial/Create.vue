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
              label="物资编号"
              prop="emergencyMaterialNumId"
            >
              <el-input
                v-model="form.emergencyMaterialNumId"
                placeholder="请输入物资编号"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="物资名称"
              prop="emergencyMaterialName"
            >
              <el-input
                v-model="form.emergencyMaterialName"
                placeholder="请输入物资名称"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="物资类型"
              prop="emergencyMaterialType"
            >
              <el-select
                v-model="form.emergencyMaterialType"
                placeholder="请选择物资类型"
                clearable
                size="small"
                style="width: 400px"
              >
                <el-option
                  v-for="dict in safetyEmergencyMaterialOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="物资型号"
              prop="emergencyMaterialModel"
            >
              <el-input
                v-model="form.emergencyMaterialModel"
                placeholder="请输入物资型号"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item
              label="物资数量"
              prop="emergencyMaterialNumber"
            >
              <el-input
                v-model="form.emergencyMaterialNumber"
                placeholder="请输入物资数量"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="负责人"
              prop="emergencyMaterialPerson"
            >
              <el-input
                v-model="form.emergencyMaterialPerson"
                placeholder="请输入负责人姓名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="联系电话"
              prop="emergencyMaterialPhone"
            >
              <el-input
                v-model="form.emergencyMaterialPhone"
                placeholder="请输入联系电话"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item
              label="物资所在位置"
              prop="emergencyMaterialPosition"
            >
              <el-input
                v-model="form.emergencyMaterialPosition"
                placeholder="请输入物资所在位置"
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

import { add, getById, edit, remove } from "../api/safetyEmergencyMaterial";

// //业务审批Copy:审批流程
import ExamineMixin from "@/components/common/examine/mixins/examine";
import CreateExamine from "@/components/common/examine/create";

export default {
  // 新建编辑
  name: "safetyEmergencyMaterialCreate",
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
        emergencyMaterialNumId: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        emergencyMaterialName: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        emergencyMaterialPhone: [
          //  { required: false, message: '请输入手机号码', trigger: 'blur' },
          {
            validator: function(rule, value, callback) {
              if (value != "" && value != undefined) {
                if (/^1[34578]\d{9}$/.test(value) == false) {
                  callback(new Error("请输入正确的手机号"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        emergencyMaterialNumber: [
          {
            validator: function(rule, value, callback) {
              if (value != "" && value != undefined) {
                if (/^[0-9]*$/.test(value) == false) {
                  callback(new Error("请输入数字"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      },
      safetyEmergencyMaterialOptions: []
    };
  },
  computed: {
    title() {
      return this.action.type === "update" ? "编辑应急物资" : "新建应急物资";
    }
  },
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save);
    console.log(this.action.type);
    if (this.action.type == "update") {
      this.getDetail();
    }
  },

  mounted() {
    //获取应急物资类型字典数据
    this.getDicts("b_safety_emergency_material_type").then(response => {
      console.log(response.data);
      this.safetyEmergencyMaterialOptions = response.data;
    });
  },

  beforeDestroy() {},

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
        })
        .catch(() => {});
    },
    /**
     * 保存
     */
    save(isSubmit) {
      var params =null;
      var flag=false;
      this.$refs["form"].validate(valid => {
        // console.log(this.form);
       params = this.form;
        if (
          params.emergencyMaterialPhone != "" &&
          params.emergencyMaterialPhone != undefined
        ) {
          if (/^1[34578]\d{9}$/.test(params.emergencyMaterialPhone) == false) {
            return new Error("请输入正确的手机号");
          } else {
            // console.log('手机号正确');
            flag = true;
          }
        } else {
          flag = true;
        }
      });

      if (flag) {
        // console.log(params);
        // console.log(this.action.type);
        if (this.action.type === "update") {
          edit(params)
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
        } else {
          add(params)
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
      }
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
