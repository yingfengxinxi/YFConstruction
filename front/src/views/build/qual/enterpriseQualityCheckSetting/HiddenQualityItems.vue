<template>
  <div>
    <create-sections
      v-for="(item, formIndex) in pageAllItems"
      :key="formIndex"
    >
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">检查内容<span v-if="pageAllItems.length > 1">（{{ formIndex+1 }}）</span></div>
        <i
          v-if="action.type != 'receipt'"
          class="el-icon-delete expense-item-head-delete"
          @click="deleteItems(formIndex)"
        />
      </flexbox>
      <el-form
        :ref="'itemForm'+formIndex"
        :model="fieldFormAll[formIndex]"
        :rules="rules"
        label-position="top"
        label-width="100px"
        class="form"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="检查分类"
              prop="checkClassification"
            >
              <el-select
                v-model="fieldFormAll[formIndex].checkClassification"
                placeholder="请选择"
                clearable
                :disabled="action.isReadonly"
              >
                <el-option
                  v-for="dict in checkClassifications"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="检查项"
              prop="checkItems"
            >
              <el-input
                :readonly="action.isReadonly"
                v-model="fieldFormAll[formIndex].checkItems"
                placeholder="请输入检查项"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="满分分值"
              prop="fullScore"
            >
              <el-input
                :readonly="action.isReadonly"
                v-model="fieldFormAll[formIndex].fullScore"
                placeholder="请输入满分分值"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col>
            <el-form-item
              label="检查内容"
              prop="checkContent"
            >
              <el-input
                :readonly="action.isReadonly"
                type="textarea"
                :rows="1"
                v-model="fieldFormAll[formIndex].checkContent"
                placeholder="请输入检查内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item
              label="评分标准"
              prop="scoringCriteria"
            >
              <el-input
                :readonly="action.isReadonly"
                type="textarea"
                :rows="1"
                v-model="fieldFormAll[formIndex].scoringCriteria"
                placeholder="请输入评分标准"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

    </create-sections>
    <div class="handle-bar">
      <el-button
        v-if="action.type != 'receipt'"
        class="handle-bar-button"
        type="text"
        icon="el-icon-plus"
        @click="addItem()"
      >添加
      </el-button>
    </div>
  </div>
</template>

<script>
import { filedGetFieldAPI } from "@/api/crm/common";
import CreateSections from "@/components/CreateSections";
import WkForm from "@/components/NewCom/WkForm";
import { CrmRelativeCell } from "@/components/CreateCom";

import crmTypeModel from "@/views/crm/model/crmTypeModel";
import CustomFieldsMixin from "@/mixins/CustomFields";

import { guid, objDeepCopy } from "@/utils";

import { filedGetFieldExmaines } from "@/api/crm/expense";
import { queryQuestionsList } from "@/views/build/qual/api/question";

import WkUploadImg from "@/components/CreateCom/WkUploadImg";
import { queryQuestionsTreeList } from "@/views/build/qual/api/question";

export default {
  // 新建编辑
  name: "ExpenseCreate",

  components: {
    CrmRelativeCell,
    CreateSections,
    WkForm,
    WkUploadImg
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
    ruls: {}
  },

  data() {
    return {
      //检查分类
      checkClassifications: [],
      /**表单校验规则 */
      rules: {
        checkClassification: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        checkItems: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        checkContent: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        scoringCriteria: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        fullScore: [
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
      },
      //所有费用item
      pageAllItems: [],
      // fieldListAll: [],
      fieldFormAll: [],
      // fieldRulesAll: [],
      //基础字段
      baseFields: [],
      // //附件
      // batchIdAll: [],
      // // 回执图片
      // receiptBatchIdAll: [],
      // 被删除的隐患信息
      delItemIds: []
      // // 隐患等级字典项
      // hazardLevleDict: [],
      // // 问题类型相关
      // questionOptions: [],
      //   defaultProps: {
      // 	  children: 'children',
      // 	  label: 'questionName'
      //   },
    };
  },

  computed: {},

  watch: {},

  created() {
    //this.getField()
    //获取字典表数据
    this.initDicts();
  },

  mounted() {
    // this.getQuestionList()
  },
  beforeDestroy() {},

  methods: {
    //父组件调用  校验数据项
    startCheck() {
      var flag = true;
      // console.log(this.$refs['itemForm0'][0].validate(valid=>{}));
      for (var i = 0; i < this.pageAllItems.length; i++) {
        this.$refs["itemForm" + i][0].validate(valid => {
          if (!valid) {
            flag = false;
          }
        });
      }
      return flag;
    },
    //新增模板内容明细
    addItem() {
      this.handle(this.baseFields);
    },
    deleteItems(index) {
      //记录被删除的、已存在的隐患信息
      var item = this.fieldFormAll[index];
      // console.log(item);
      if (item.expenseId) {
        this.delItemIds.push(item.expenseId);
      }
      // this.fieldListAll.splice(index, 1);
      this.fieldFormAll.splice(index, 1);
      // this.batchIdAll.splice(index, 1);
      // this.receiptBatchIdAll.splice(index, 1);
      // this.fieldRulesAll.splice(index, 1);
      this.pageAllItems.splice(index, 1);
    },

    //格式化 字段数据
    handle(list) {
      // console.log("打印")
      // console.log(list)
      // console.log(this.fieldFormAll)
      const fieldForm = {}; //初始化
      var batchId = "";
      var receiptBatchId = "";

      this.fieldFormAll.push(fieldForm);
      if (!batchId) {
        batchId = guid();
      }

      //加入一次空遍历
      this.pageAllItems.push({});
      // console.log(this.pageAllItems)
    },
    /**
     * 获取字段是否可编辑
     */
    /* getItemIsCanEdit(item, type) {
        // authLevel 1 不能查看不能编辑 2可查看  3 可编辑可查看
        return (type === 'update' && item.authLevel == 3) || type !== 'update'
      }, */
    /**
     * 关闭
     */
    close() {
      this.$emit("close");
    },
    // 初始化字典数据
    initDicts() {
      this.getDicts("b_qual_enterprisecheck_classification").then(response => {
        // console.log(response.data);
        this.checkClassifications = response.data;
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
