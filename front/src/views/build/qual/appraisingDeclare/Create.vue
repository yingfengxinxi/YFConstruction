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
              label="创优评优批次"
              prop="appraisingBatch"
            >
              <el-input
                v-model="checkForm.appraisingBatch"
                placeholder="请输入创优评优批次"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              label="评审开始时间"
              prop="reviewTime"
            >
              <el-date-picker
                v-model="checkForm.reviewTime"
                type="date"
                placeholder="请选择开始时间"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              label="评审结束时间"
              prop="reviewEndTime"
            >
              <el-date-picker
                v-model="checkForm.reviewEndTime"
                type="date"
                placeholder="请选择结束时间"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="评审组长"
              prop="reviewLeader"
            >
              <el-select
                v-model="checkForm.reviewLeader"
                placeholder="请选择"
                clearable
                :disabled="action.isReadonly"
              >
                <el-option
                  v-for="dict in dictData.userListData"
                  :key="dict.userId"
                  :label="dict.realname"
                  :value="dict.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="评审成员"
              prop="reviewMember"
              :error="error.reviewMemberError"
            >
              <xh-struc-user-cell
                :users="checkForm.dep.ownerUserList"
                style="width: 100%;"
                @value-change="reviewMemberChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="评审标准" prop="reviewStandard">
              <el-input type="textarea" :rows="2" v-model="checkForm.reviewStandard" placeholder="请输入评审标准"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="申报材料" prop="enactDate">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="*"
                :autoUpload="false"
                :batchId="batchId"
              ></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="申报明细">
        <hidden-declare-items
          :action="action"
          ref="hiddenDeclareItems"
        ></hidden-declare-items>
      </create-sections>
    </el-form>
    <!-- 业务审批Copy:流程 start -->
    <create-examine
      :businessType="businessType"
      @value-change="examineValueChange"
    ></create-examine>
    <el-button
      v-if="isOpenExamine"
      slot="footer"
      type="primary"
      plain
      @click.native="debouncedSaveField(false)">保存草稿
    </el-button>
    <!-- 业务审批Copy:流程 end -->
  </xr-create>
</template>

<script>
import XrCreate from "@/components/XrCreate";
import CreateSections from "@/components/CreateSections";
// import MembersDep from "@/components/SelectEmployee/MembersDep";

import WkUploadFile from "@/components/CreateCom/WkUploadFile";
import { XhStrucUserCell } from "@/components/CreateCom";

  import {guid} from '@/utils'

import { debounce } from "throttle-debounce";

import HiddenDeclareItems from "./HiddenDeclareItems";

import { add, edit,saveOrUpdate,getAppraisingDeclareById } from "../api/appraisingDeclare";
//业务审批Copy:审批流程
import ExamineMixin from '@/components/common/examine/mixins/examine'
import CreateExamine from '@/components/common/examine/create'

export default {
  // 新建编辑
  name: "PlanCreate",
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
    // MembersDep,
    XhStrucUserCell,
    HiddenDeclareItems,
    // XhUserCell,
    CreateExamine
  },
  mixins: [ExamineMixin],//业务审批Copy:审批
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
    const validateChoices = (rule, value, callback) => {
      // console.log("校验规则value", value);
    };
    return {
      error: { reviewMemberError: "" },
      //控制加载框显示
      loading: false,
      // 创优评优申报基本信息
      checkForm: {
        reviewTime: new Date(
          new Date().getFullYear() +
            "-" +
            (new Date().getMonth() + 1) +
            "-" +
            new Date().getDate() +
            " 00:00:00"
        ),
        reviewEndTime:new Date(
          new Date().getFullYear() +
            "-" +
            (new Date().getMonth() + 1) +
            "-" +
            new Date().getDate() +
            " 00:00:00"
        ),
        dep: {
          ownerUserList: []
        }
      },
      // 申报信息
      hiddenDeclareList: [],
      /**表单校验规则 */
      rules: {
        appraisingBatch: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        reviewTime: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        reviewEndTime: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        reviewStandard: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        reviewLeader: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ]
      },
      batchId:'',
    };
  },
  computed: {
    title() {
      var title = "新建申报";
      if (this.action.type === "update") {
        title = "编辑申报";
      }
     
      return title;
    }
  },
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save);
    //考虑附件ID
    if (this.action.type == "update") {
      this.getDetail();
    }else{
      this.batchId = guid();
    }
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 获取详情
     * */
    getDetail() {
      getAppraisingDeclareById(this.action.id)
        .then(res => {
          // 设置表单数据
          var appraisingDeclare = res.data.appraisingDeclare;
           this.batchId =appraisingDeclare.mainBatchId;
          // 转换用户对象
          //评审成员信息
          var ownerUserList = appraisingDeclare.ownerUserList;
          var dep = {
            ownerUserList: ownerUserList
          };
          appraisingDeclare.dep = dep;

          // 绑定基本信息
          this.checkForm = appraisingDeclare;

          // 绑定隐患
          this.hiddenDeclareList = res.data.hiddenDeclareList;

          // 生成附件batchId列表
          var batchIdAll = [];
          //   var receiptBatchIdAll = [];
          this.hiddenDeclareList.forEach(item => {
            batchIdAll.push(item.batchId);
          });

          // 将数据传到子控件
          this.$refs.hiddenDeclareItems.fieldFormAll = this.hiddenDeclareList;
          this.$refs.hiddenDeclareItems.batchIdAll = batchIdAll;
          //   this.$refs.hiddenDeclareItems.receiptBatchIdAll = receiptBatchIdAll;

          // 生成页对象
          var pageAllItems = [];
          this.hiddenDeclareList.forEach(item => {
            pageAllItems.push({});
          });
          // 将数据传到子控件
          this.$refs.hiddenDeclareItems.pageAllItems = pageAllItems;
        })
        .catch(() => {});
    },
    /**
     * 保存
     */
    save(isSubmit) {
       if (this.checkForm.dep.ownerUserList.length == 0) {
        this.error.reviewMemberError = "成员不能为空";
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
           
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if(!fileFlag){
              this.$message({
                type: 'error',
                message: '方案附件提交异常'
              })
              return;
            }else{
                  this.checkForm.mainBatchId = this.batchId;
            }
          var fieldFormAll = this.$refs.hiddenDeclareItems.fieldFormAll;
          var batchIdAll = this.$refs.hiddenDeclareItems.batchIdAll;
        

          if (fieldFormAll.length <= 0) {
            this.$message({
              type: "error",
              message: "请添加申报信息"
            });
            return;
          }
          var itemFlag = true;
          for (var i = 0; i < fieldFormAll.length; i++) {
            this.$refs.hiddenDeclareItems.$refs["itemForm" + i][0].validate(
              validFlag => {
                itemFlag = validFlag;
              }
            );
          }
          if (!itemFlag) {
            this.$message({
              type: "error",
              message: "请完善申报信息"
            });
            return false;
          }
          var time = 0;
          for (var i = 0; i < fieldFormAll.length; i++) {
            fieldFormAll[time].batchId = batchIdAll[time];
            time++;
          }
          this.hiddenDeclareList = fieldFormAll;
          this.loading = true;

          //异步提交
          var fileFlag = this.$refs.hiddenDeclareItems.submitFiles();
          if (!fileFlag) {
            this.$message({
              type: "error",
              message: "申报材料上传异常"
            });
            this.loading = false;
            return;
          }

          var params = {
            id:this.checkForm.id,
            checkForm: this.checkForm,
            hiddenDeclareList: this.hiddenDeclareList
          };
           //业务审批Copy: 提交追加审批数据 start
           console.log(isSubmit);
           console.log(this.isOpenExamine);
           console.log(this.handleExamineInfo(isSubmit,params));
          //  return;
            if (this.isOpenExamine) {
              if(!this.handleExamineInfo(isSubmit,params)){
                return;
              };
            }
            //业务审批Copy:end    
            saveOrUpdate(params)
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
          // if (this.action.type == "update") {
          //   edit(params)
          //     .then(res => {
          //       if (res.code === 0) {
          //         this.$message.success(
          //           this.action.type == "update" ? "编辑成功" : "添加成功"
          //         );
          //         this.close();
          //         // 保存成功
          //         this.$emit("save-success", {
          //           type: "receivables"
          //         });
          //       } else {
          //         this.$message.error(response.msg);
          //       }
          //       this.loading = false;
          //     })
          //     .catch(err => {
          //       this.loading = false;
          //     });
          // } else {
          //   add(params)
          //     .then(res => {
          //       if (res.code === 0) {
          //         this.$message.success(
          //           this.action.type == "update" ? "编辑成功" : "添加成功"
          //         );
          //         this.close();
          //         // 保存成功
          //         this.$emit("save-success", {
          //           type: "receivables"
          //         });
          //       } else {
          //         this.$message.error(response.msg);
          //       }
          //       this.loading = false;
          //     })
          //     .catch(err => {
          //       this.loading = false;
          //     });
          // }
        }
      });
    },
    /**
     * 关闭
     */
    close() {
      this.$emit("close");
    },
    /**添加评审成员 */
    reviewMemberChange(data) {
      if (data.value.users.length == 0) {
        this.error.reviewMemberError = "成员不能为空";
        return;
      }

      this.$set(this.checkForm, "dep", { ownerUserList: data.value.users });
      var reviewMember = this.checkForm.dep.ownerUserList
        .map(item => item.userId)
        .join(",");
      this.checkForm.reviewMember = reviewMember;
      this.error.reviewMemberError = "";
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
