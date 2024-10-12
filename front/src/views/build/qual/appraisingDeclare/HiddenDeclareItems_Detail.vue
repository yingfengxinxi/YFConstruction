<template>
  <div>
    <create-sections
      v-for="(item, formIndex) in pageAllItems"
      :key="formIndex"
    >
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">申报信息<span v-if="pageAllItems.length > 1">（{{ formIndex+1 }}）</span></div>
      </flexbox>
      <el-form
        :ref="'itemForm'+formIndex"
        :model="fieldFormAll[formIndex]"
        :rules="rules"
        label-position="top"
        label-width="100px"
        class="form"
        :disabled="true"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="申报项目"
              prop="declareProject"
            >
              <el-select
                v-model="fieldFormAll[formIndex].declareProject"
                placeholder="请选择"
                clearable
                @change="declareProjectChange($event,formIndex)"
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
          <el-col :span="8">
            <el-form-item
              label="项目经理"
              prop="projectManager"
            >
              <el-input
                :readonly="isReadonly"
                v-model="fieldFormAll[formIndex].projectManager"
                placeholder="请输入项目经理"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="建筑规模"
              prop="architectureScale"
            >
              <el-select
                v-model="fieldFormAll[formIndex].architectureScale"
                placeholder="请选择"
                clearable
                :disabled="isReadonly"
              >
                <el-option
                  v-for="dict in constructionDict"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item
              label="总计评分"
              prop="totalScore"
            >
              <el-input
                v-model="fieldFormAll[formIndex].totalScore"
                placeholder="请输入总计评分"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="检查结论"
              prop="inspectionConclusion"
            >
              <el-input
                v-model="fieldFormAll[formIndex].inspectionConclusion"
                placeholder="请输入检查结论"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="是否通过"
              prop="whetherAdopt"
            >
              <el-select
                v-model="fieldFormAll[formIndex].whetherAdopt"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="dict in IsAdopt"
                  :key="dict.dataValue"
                  :label="dict.dataLable"
                  :value="dict.dataValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 申报材料上传 -->
      <wk-upload-file
        :ref="'wkUploadFile'+formIndex"
        name="file"
        multiple
        accept="*"
        :autoUpload="false"
        :batchId="batchIdAll[formIndex]"
      ></wk-upload-file>
    </create-sections>
    <!-- <div class="handle-bar">
      <el-button
        class="handle-bar-button"
        type="text"
        icon="el-icon-plus"
        @click="addItem()"
      >添加
      </el-button>
    </div> -->
  </div>
</template>

<script>
import { userListAPI } from "@/api/common"; // 直属上级接口  用户接口
import CreateSections from "@/components/CreateSections";
import WkForm from "@/components/NewCom/WkForm";
import { CrmRelativeCell } from "@/components/CreateCom";

import CustomFieldsMixin from "@/mixins/CustomFields";

import { guid } from "@/utils";

import WkUploadFile from "@/components/CreateCom/WkUploadFile";
import { queryQuestionsTreeList } from "@/views/build/qual/api/question";

import { getProjectInfo, getProjectInfoById } from "../api/appraisingDeclare";

export default {
  // 新建编辑
  name: "ExpenseCreate",

  components: {
    CrmRelativeCell,
    CreateSections,
    WkForm,
    WkUploadFile
  },

  mixins: [CustomFieldsMixin],

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
    detaildata:{
      type: Object,
    }
  },

  data() {
    const validates=(rule,value,callback)=>{
    let regExp= "^[0-9]*$";
      if(regExp.test(value)){
           callback(new Error('不通过正则'));
      }
    }
    return {
      //是否只读
      isReadonly: true,
      //项目列表
      ProjectInfos: [],
      //建设规模
      constructionDict: [],
      //是否通过
      IsAdopt: [
        { dataLable: "是", dataValue: "1" },
        { dataLable: "否", dataValue: "2" }
      ],
      //所有费用item
      pageAllItems:[],
      fieldListAll: [],
      fieldFormAll:[],
      //基础字段
      baseFields: [],
      //附件
      batchIdAll: [],
      // 被删除的申报信息
      delItemIds: [],
      //   // 用户列表
      //   userListData: [],
      //   // 问题类型相关
      //   questionOptions: [],
      defaultProps: {
        children: "children",
        label: "questionName"
      },
      rules: {
        declareProject:[{ required: true, message: "必填项不得为空", trigger: "blur"}],
        totalScore: [
          { required: true, message: "必填项不得为空", trigger: "blur"},
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
        ],
        inspectionConclusion: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        whetherAdopt: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ]
      }
    };
  },
  computed: {},

  created() {
    this.initDicts();
    setTimeout(() => {
      console.log(this.detaildata)
      this.pageAllItems=this.detaildata.pageAllItems;
      this.fieldFormAll=this.detaildata.fieldFormAll;
      this.batchIdAll=this.detaildata.batchIdAll
    },1000);
      
   
  },
  mounted() {
    // this.getQuestionList();
    // this.getUserList();
  },
  beforeDestroy() {},

  methods: {
    handleNodeClick: function(data, node, obj) {
      if (node.children) {
      } else {
        var formIndex = obj.$data.tree.$attrs.formIndex;
        this.fieldFormAll[formIndex].questionId = node.key;
        this.fieldFormAll[formIndex].questionName = node.label;
        var ref = "selectTree" + formIndex;
        this.$refs[ref][0].blur();
      }
    },
    componentValue(params) {
      this.$emit("change", this.fieldFormAll);
    },
    //父组件调用  校验数据项
    startCheck() {
      var flag = true;
      for (var i = 0; i < this.pageAllItems.length; i++) {
        this.$refs["crmForm" + i][0].instance.validate(valid => {
          if (!valid) {
            flag = false;
          }
        });
      }
      return flag;
    },
    submitFiles() {
      var flag = true;
      for (var i = 0; i < this.pageAllItems.length; i++) {
        var ref = "wkUploadFile" + i;
        flag = this.$refs[ref][0].submitAsync();
      }
      return flag;
    },

    //新增费用项
    addItem() {
      this.handle(this.baseFields);
    },
    deleteItems(index) {
      //记录被删除的、已存在的隐患信息
      var item = this.fieldFormAll[index];
      if (item.expenseId) {
        this.delItemIds.push(item.expenseId);
      }
      this.fieldListAll.splice(index, 1);
      this.fieldFormAll.splice(index, 1);
      this.batchIdAll.splice(index, 1);
      //   this.receiptBatchIdAll.splice(index, 1);
      this.pageAllItems.splice(index, 1);
    },

    //格式化 字段数据
    handle(list) {
      const fieldForm = { questionId: "", questionName: "" }; //初始化时下拉框绑定属性必须声明，否则下拉树无法进行控件绑定赋值
      var batchId = "";
      //   var receiptBatchId = "";

      this.fieldFormAll.push(fieldForm);
      if (!batchId) {
        batchId = guid();
      }
      this.batchIdAll.push(batchId);
   
      //加入一次空遍历
      this.pageAllItems.push({});
    },

    /**
     * 关闭
     */
    close() {
      this.$emit("close");
    },
    // 初始化字典数据
    initDicts() {
      this.getDicts("b_construction_scale").then(response => {
        this.constructionDict = response.data;
      });
      //获取所有项目信息
      getProjectInfo()
        .then(res => {
          console.log("项目信息", res.data.list);
          this.ProjectInfos = res.data.list;
        })
        .catch(() => {});
    },

    //数据绑定
    declareProjectChange(val, index) {
      //   console.log(val);
      //   console.log(index);
      getProjectInfoById(val).then(res => {
        // console.log(res);
        let data = res.data;
        // console.log("项目经理", data.projectManager);
        // console.log("建设规模", data.constructionScale);
        if (data.projectManager != null) {
          this.$set(
            this.fieldFormAll[index],
            "projectManager",
            data.projectManager
          );
        } else {
          this.$set(this.fieldFormAll[index], "projectManager", "");
        }
        if (data.constructionScale != null) {
          console.log("建设规模");
          this.$set(
            this.fieldFormAll[index],
            "architectureScale",
            data.constructionScale
          );
        } else {
          this.$set(this.fieldFormAll[index], "architectureScale", "");
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.expense-item {
  border: 1px solid #e6e6e6;
  border-radius: 2px;
  padding-bottom: 15px;
  margin-bottom: 10px;
  &-head {
    padding: 10px 20px;
    background-color: #f5f5f5;
    &-title {
      height: auto;
      font-size: 12px;
      color: #333;
      flex: 1;
      line-height: normal;
    }

    &-delete {
      padding: 0 10px;
      color: #2362fb;
      font-size: 14px;
    }
  }
}

.handle-bar {
  padding: 0 20px;
  height: 29px;
  &-button {
    float: right;
    border: none;
    color: #2362fb;
  }
  &-total {
    margin-top: 10px;
    text-align: left;
    font-size: 13px;
    color: #333;
    span {
      color: #666;
    }
  }
}

.wk-form {
  /deep/ .el-form-item.is-product {
    flex: 0 0 100%;
  }
}

.wk-form /deep/ .el-form-item {
  margin-bottom: 20px !important;
}
.el-select {
  width: 100%;
}
</style>
