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
          <el-col :span="12">
            <el-form-item
              label="检查模板"
              prop="checkTemplateId"
            >
              <el-select
                v-model="checkForm.checkTemplateId"
                placeholder="请选择"
                clearable
                @change="checkTemplateChange"
              >
                <el-option
                  v-for="dict in checkTemplateIds"
                  :key="dict.id"
                  :label="dict.checkTemplateName"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="检查项目"
              prop="checkProject"
            >
              <el-input
                v-model="checkForm.checkProject"
                placeholder="请输入检查项目"
              ></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
           <!-- <el-col :span="8">
            <el-form-item label="检查人员" prop="qualCheckPersonId">		  
			       <xh-struc-user-cell
                :users="checkForm.dep.ownerUserList"
			    style="width: 100%;"
			    @value-change="strcUserChange"/>		  
            </el-form-item>
          </el-col> -->
          <el-col :span="5">
            <el-form-item
              label="检查人"
              prop="checkPersonName"
            >
              <el-input
                v-model="checkForm.checkPersonName"
                placeholder="请输入检查人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item
              label="检查日期"
              prop="checkDate"
            >
              <el-date-picker
                v-model="checkForm.checkDate"
                type="date"
                placeholder="请选择检查日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item
              label="检查得分"
              prop="checkScore"
            >
              <el-input
              :readonly="true"
                v-model="checkForm.checkScore"
                placeholder=""
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item
              label="评定结果"
              prop="evaluateResult"
            >
              <el-input
                v-model="checkForm.evaluateResult"
                placeholder="请输入评定结果"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="模板内容明细">
        <hidden-quality-items
          :action="action"
          ref="hiddenQualityItems"
          v-on:monitor="monitor"
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

import { getList, getById } from "../api/enterpriseQualityCheckSetting";
import { add,edit,getCheckSubById } from "../api/enterpriseQualityCheckSubmit";
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
      //检查模板
      checkTemplateIds: [],
      // 表单
      checkForm: {
        checkDate: new Date(
          new Date().getFullYear() +
            "-" +
            (new Date().getMonth() + 1) +
            "-" +
            new Date().getDate() +
            " 00:00:00"
        ),
      //   dep: {
			// 	ownerUserList: []
			// }
      },
      // 明细信息
      hiddenTroubleListL: [],
      /**表单校验规则 */
      rules: {
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
          }
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
    console.log(this.action.type);
    if (this.action.type == "update") {
      this.getDetail();
    }
    //获取模板数据
    this.loading = true;
    getList(this.queryParams)
      .then(res => {
        this.checkTemplateIds = res.data.list;
        // console.log(this.checkTemplateIds);
        this.loading = false;
      })
      .catch(() => {
        this.loading = false;
      });
  },

  mounted() {

  },

  beforeDestroy() {},

  methods: {
    /**父组件监听子组件返回的值 */
    monitor(value){
      // console.log(value);
       this.$set(this.checkForm,"checkScore",value);
        // console.log(this.checkForm);
    },
    /**
     * 获取详情
     * */
    getDetail() {
      console.log(this.action.id);
      getCheckSubById(this.action.id)
        .then(res => {
          console.log(res);
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
        // console.log(valid);
        var fieldFormAll = this.$refs.hiddenQualityItems.fieldFormAll;

        // console.log(fieldFormAll);
        // console.log(this.checkForm);

        if (valid) {
          var fieldFormAll = this.$refs.hiddenQualityItems.fieldFormAll;
          if (fieldFormAll.length <= 0) {
            this.$message({
              type: "error",
              message: "请添加模板内容"
            });
            return;
          }
          // var itemFlag = true;
          // for (var i = 0; i < fieldFormAll.length; i++) {
          //   this.$refs.hiddenQualityItems.$refs["itemForm" + i][0].validate(
          //     validFlag => {
          //       // console.log("itemForm=validate")
          //       itemFlag = validFlag;
          //     }
          //   );
          // }
          // if (!itemFlag) {
          //   this.$message({
          //     type: "error",
          //     message: "请完善模板内容"
          //   });
          //   return false;
          // }

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
    /**检查模板被选中时获取当前模板内容 */
    checkTemplateChange(value) {
      // console.log(value);
      if (value != "" && value != undefined) {
        getById(value)
          .then(res => {
           
            // 绑定明细
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
      }else{
         this.hiddenTroubleList = [];
      // 将数据传到子控件
      this.$refs.hiddenQualityItems.fieldFormAll = this.hiddenTroubleList;
      // 生成页对象
      var pageAllItems = [];
      this.hiddenTroubleList.forEach(item => {
        pageAllItems.push({});
      });
      // 将数据传到子控件
      this.$refs.hiddenQualityItems.pageAllItems = pageAllItems;
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
