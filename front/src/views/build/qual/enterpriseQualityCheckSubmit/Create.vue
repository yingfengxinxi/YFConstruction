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
              <el-select
                v-model="checkForm.checkProject"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="dict in ProjectInfos"
                  :key="dict.id"
                  :label="dict.projectName"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item
              label="检查项目"
              prop="checkProject"
            >
              <el-input
                v-model="checkForm.checkProject"
                placeholder="请输入检查项目"
              ></el-input>
            </el-form-item>
          </el-col> -->

        </el-row>
        <el-row>
          <el-col :span="6">
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
          <el-col :span="8">
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
                :disabled="true"
                v-model="checkForm.checkScore"
                placeholder="自动计算"
              ></el-input>
            </el-form-item>
            
          </el-col>
           <el-col :span="5">
            <el-form-item
              label="评定结果"
              prop="evaluateResult"
            >
              <el-select
                v-model="checkForm.evaluateResult"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="dict in EvaluationResults"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="5">
            <el-form-item
              label="评定结果"
              prop="evaluateResult"
            >
              <el-input
                v-model="checkForm.evaluateResult"
                placeholder="请输入评定结果"
              ></el-input>
            </el-form-item>
          </el-col> -->
        </el-row>
      </create-sections>
      <create-sections title="检查明细项">
        <el-table
          v-loading="loading"
          id="crm-table"
          :data="templateList"
          :height="tableHeight"
          class="n-table--border"
          use-virtual
          stripe
          border
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column
            :show-overflow-tooltip="true"
            label="检查项"
            align="center"
            prop="checkItems"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            label="检查内容"
            align="center"
            prop="checkContent"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            label="评分标准"
            align="center"
            prop="scoringCriteria"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            label="满分分值"
            align="center"
            prop="fullScore"
          />
          <el-table-column
            label="真实得分"
            align="center"
            prop="actualScore"
          >
            <template slot-scope="scope">

              <el-input
                size="small"
                type="number"
                min="0"
                v-model="scope.row.actualScore"
                @change="actualScoreChange"
                placeholder="请输入真实得分"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column
            label="说明"
            align="center"
            prop="situationExplain"
          >
            <template slot-scope="scope">
              <el-input
                size="small"
                v-model="scope.row.situationExplain"
                placeholder="请输入说明"
              ></el-input>
            </template>
          </el-table-column>

        </el-table>
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

import { getList, getById } from "../api/enterpriseQualityCheckSetting";
import {
  add,
  edit,
  getCheckSubById,
  getProjectInfo
} from "../api/enterpriseQualityCheckSubmit";

export default {
  // 新建编辑
  name: "PlanCreate",
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
    MembersDep,
    XhStrucUserCell
  },
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
      tableHeight: document.documentElement.clientHeight - 580,
      //评定结果
      EvaluationResults:[],
      //项目信息
      ProjectInfos: [],
      loading: false,
      //控制表单是否可提交
      IsSubmit: false,
      IsBig:false,
      // tableHeight: document.documentElement.clientHeight, // 表的高度
      /**模板内容明细列表 */
      templateList: [],
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
        )
      },
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
        ],
        checkTemplateId: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        checkProject: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        checkPersonName: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        checkDate: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        evaluateResult: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
      }
    };
  },
  computed: {
    title() {
      var title = "新建检查";
      if (this.action.type === "update") {
        title = "编辑检查";
      }
      return title;
    }
  },
  watch: {},
  created() {
    // console.log(document.documentElement.clientHeight);
    this.debouncedSaveField = debounce(300, this.save);
    // console.log(this.action.type);
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
    //获取所有项目信息
    getProjectInfo()
      .then(res => {
        // console.log(res.data.list);
        this.ProjectInfos = res.data.list;
      })
      .catch(() => {
        this.loading = false;
      });
      //获取评定结果字典数据
       this.getDicts('b_qual_EvaluationResults').then(response => {
        // console.log(response.data)
      this.EvaluationResults = response.data
    })
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**父组件监听子组件返回的值 */
    monitor(value) {
      // console.log(value);
      this.$set(this.checkForm, "checkScore", value);
      // console.log(this.checkForm);
    },
    /**
     * 获取详情
     * */
    getDetail() {
      // console.log(this.action.id);
      getCheckSubById(this.action.id)
        .then(res => {
          // console.log(res);
          // 绑定基本信息
          this.checkForm = res.data.checkForm;
          //绑定模板内容明细
          this.templateList = res.data.hiddenqualList;
          // // 绑定隐患
          // this.hiddenTroubleList = res.data.hiddenqualList;
          // // 将数据传到子控件
          // this.$refs.hiddenQualityItems.fieldFormAll = this.hiddenTroubleList;
          // // 生成页对象
          // var pageAllItems = [];
          // this.hiddenTroubleList.forEach(item => {
          //   pageAllItems.push({});
          // });
          // // 将数据传到子控件
          // this.$refs.hiddenQualityItems.pageAllItems = pageAllItems;
        })
        .catch(() => {});
    },
    /**
     * 保存
     */
    save(isSubmit) {
      this.$refs["form"].validate(valid => {if(valid){return;}});
       let data = this.templateList;
      for (let i = 0; i < data.length; i++) {
        let TempActualScore = data[i].actualScore; //真实得分
        // console.log(TempActualScore);console.log(TempActualScore != "" && TempActualScore != undefined);
        if (TempActualScore != "" && TempActualScore != undefined) {
          this.IsSubmit = true;
          if(parseInt(TempActualScore)<=parseInt(data[i].fullScore)){
             this.IsBig = true;
          }else{
            this.IsBig = false;
            break;
              // this.$message.error( "真实得分不能超过满分分值" );
          }
        } else {
          this.IsSubmit = false;
          break;
        }
      }
      // console.log(this.IsSubmit,this.IsBig);
      if(!this.IsSubmit){
        this.$message.error( "真实得分不能为空" );
          return ;
          } 
          if(!this.IsBig){
        this.$message.error( "真实得分不能超过满分分值" );
          return ;
          }
        this.$refs["form"].validate(valid => {
          // console.log(this.checkForm);
          // console.log(this.templateList);
          if (valid) {
            this.loading = true;
            var params = {
              checkForm: this.checkForm,
              hiddenqualList: this.templateList
            };
          
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
            this.templateList = res.data.hiddenqualList;
          })
          .catch(() => {});
      } else {
        this.templateList = [];
      }
    },
    /**
     * 真实得分change事件
     * @ value 获取input输入的参数
     */
    actualScoreChange(value) {
      let data = this.templateList;
      let TotalScore = 0; //总分
      let flag = true;
      //判断真实得分是否大于满分分值
      for (let i = 0; i < data.length; i++) {
        let TempActualScore = data[i].actualScore; //真实得分
        let TempFullScore = data[i].fullScore; //满分

        if (TempActualScore != "" && TempActualScore != undefined) {
          if (parseInt(TempActualScore) > parseInt(TempFullScore)) {
            flag = false;
          } else {
            TotalScore = parseInt(TotalScore) + parseInt(TempActualScore);
          }
        } else {
          this.IsSubmit = false;
        }
      }
      if (flag) {
        this.IsSubmit = true;
        this.$set(this.checkForm, "checkScore", TotalScore);
      } else {
        this.$message.error("真实得分不能超过满分分值");
        this.IsSubmit = false;
      }
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
