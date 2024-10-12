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
      :model="checkForm"
      :rules="rules"
      label-position="top"
      label-width="100px"
      class="form"
    >
      <create-sections
        title="基本信息"
        v-if="action.type != 'receipt'"
      >
        <el-row>
          <el-col>
            <el-form-item
              label="模板名称"
              prop="checkTemplateName"
            >
              <el-input
                v-model="checkForm.checkTemplateName"
                placeholder="请输入模板名称"
              ></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="检查类型"
              prop="checkType"
            >
              <el-select
                v-model="checkForm.checkType"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="dict in checkTypes"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="满分"
              prop="fullMarks"
              :error="myError.fullMarksError"
            >
              <el-input
                v-model="checkForm.fullMarks"
                placeholder="请输入"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="模板内容明细">
        <hidden-quality-items
          :action="action"
          ref="hiddenQualityItems"
        ></hidden-quality-items>
      </create-sections>
    </el-form>
  </xr-create>
</template>

<script>
import XrCreate from "@/components/XrCreate";
import CreateSections from "@/components/CreateSections";
import MembersDep from "@/components/SelectEmployee/MembersDep";

import WkUploadFile from "@/components/CreateCom/WkUploadFile";
import { XhStrucUserCell } from "@/components/CreateCom";

import { guid } from "@/utils";

import { debounce } from "throttle-debounce";

import HiddenQualityItems from "./HiddenQualityItems";

import { add, getById, edit } from "../api/enterpriseQualityCheckSetting";

//业务审批Copy:审批流程
//import ExamineMixin from '@/components/common/examine/mixins/examine'
//import CreateExamine from '@/components/common/examine/create'

export default {
  // 新建编辑
  name: "PlanCreate",
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
    MembersDep,
    XhStrucUserCell,
    HiddenQualityItems
  },
  //mixins: [ExamineMixin],//业务审批Copy:审批
  props: {
    action: {
      type: Object,
      // 是否只读
      isReadonly: false,
      default: () => {
        return {
          type: "save",
          id: "",
          data: {}
        };
      }
    },
    // 字典数据
    dictData: {}
  },
  data() {
    return {
      loading: false,
      //检查类型（1月检查，2季检查）
      checkTypes: [],
      // 表单
      checkForm: {},
      // 明细信息
      hiddenTroubleListL: [],
      /**错误信息提示 */
      myError: { fullMarksError: "" },
      /**表单校验规则 */
      rules: {
        checkTemplateName: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        checkType: [
          {
            required: true,
            message: "必填项不得为空",
            trigger: "change"
          }
        ],
        fullMarks: [
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
          },
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    title() {
      var title = "新建检查";
      if (this.action.type === "update") {
        title = "编辑检查";
      } else if (this.action.type === "receipt") {
        title = "回执检查";
      }
      return title;
    }
  },
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save);
    //考虑附件ID
    if (this.action.type == "update" || this.action.type == "receipt") {
      this.getDetail();
    }
    //获取字典表数据
    this.getDicts("b_qual_enterprisecheck_type").then(response => {
      // console.log(response.data);
      this.checkTypes = response.data;
    });
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 获取详情
     * */
    getDetail() {
      // console.log(this.action.id);
      getById(this.action.id)
        .then(res => {
          // console.log(res);
          // 绑定基本信息
          this.checkForm = res.data.checkForm;
          // 绑定隐患
          this.hiddenTroubleList = res.data.hiddenqualList;
          // 将数据传到子控件
          this.$refs.hiddenQualityItems.fieldFormAll = this.hiddenTroubleList;
          // 生成页对象
          var pageAllItems = [];
          this.hiddenTroubleList.forEach(item => {
            pageAllItems.push({});
          });
          // 将数据传到子控件
          this.$refs.hiddenQualityItems.pageAllItems = pageAllItems;
        })
        .catch(() => {});
    },
    /**
     * 保存
     */
    save(isSubmit) {
      this.$refs["form"].validate(valid => {
        // console.log(this.$refs.hiddenQualityItems.startCheck());
        /**判断组件表单是否通过必填校验 */
        if (!this.$refs.hiddenQualityItems.startCheck()) {
          return;
        }
        /**判断是否添加模板内容 */
        var fieldFormAll = this.$refs.hiddenQualityItems.fieldFormAll;
        if (valid) {
          var fieldFormAll = this.$refs.hiddenQualityItems.fieldFormAll;
          if (fieldFormAll.length <= 0) {
            this.$message({
              type: "error",
              message: "请添加模板内容"
            });
            return;
          }
          /**校验各检查项分数总和是否与满分相等 */
          let TempTotelData = 0; //检查项分数总和
          let fullTempData = parseInt(this.checkForm.fullMarks); //满分
          for (let i = 0; i < fieldFormAll.length; i++) {
            if (fieldFormAll[i].hasOwnProperty("fullScore")) {
              if (
                fieldFormAll[i].fullScore != "" &&
                fieldFormAll[i].fullScore.length != 0
              ) {
                TempTotelData =
                  parseInt(TempTotelData) + parseInt(fieldFormAll[i].fullScore);
              }
            }
          }
          // console.log(TempTotelData);
          // console.log(fullTempData);
          // console.log(fullTempData != TempTotelData);
          if (fullTempData != TempTotelData) {
            this.myError.fullMarksError = "各检查项分数总和需等于满分";
            return;
          }
         

          this.hiddenTroubleList = fieldFormAll;
          this.loading = true;
          var params = {
            checkForm: this.checkForm,
            hiddenqualList: this.hiddenTroubleList
          };
          // console.log(params);
          // console.log(this.action.type);
          if (this.action.type == "update") {
            edit(params)
              .then(res => {
                if (res.code === 0) {
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
              .then(res => {
                if (res.code === 0) {
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
      });
    },
    /**
     * 关闭
     */
    close() {
      this.$emit("close");
    },
    // TODO 如果两个人同时点击 单据号相同需要处理
    // 检查类型Change事件
    checkTypeChange(value) {
      // 检查类型等于项目类型 向表单添加单据标号的值
      if (value === "1") {
        generateDocumentNumber().then(res => {
          this.checkForm.qualDocumentNumber = res.data;
        });
      } else {
        this.checkForm.qualDocumentNumber = "";
      }
    },
    // 添加检查人员
    strcUserChange(data) {
      this.$set(this.checkForm, "dep", { ownerUserList: data.value.users });

      var qualCheckPersonId = this.checkForm.dep.ownerUserList
        .map(item => item.userId)
        .join(",");
      this.checkForm.qualCheckPersonId = qualCheckPersonId;
      var qualCheckPersonName = this.checkForm.dep.ownerUserList
        .map(item => item.realname)
        .join(",");
      this.checkForm.qualCheckPersonName = qualCheckPersonName;
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
.el-select {
  width: 100%;
}
/deep/ .el-input.is-disabled .el-input__inner {
  background: none !important;
  color: #666;
}
</style>
