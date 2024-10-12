<template>
  <div>
    <create-sections v-for="(item, formIndex) in pageAllItems" :key="formIndex">
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">模板内容<span v-if="pageAllItems.length > 1">（{{ formIndex+1 }}）</span></div>
        <i v-if="action.type != 'receipt'" class="el-icon-delete expense-item-head-delete" @click="deleteItems(formIndex)" />
      </flexbox>
      <el-form :ref="'itemForm'+formIndex" :model="fieldFormAll[formIndex]" :rules="rules" label-position="top" label-width="100px" class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收类型" prop="acceptType">
              <el-select v-model="fieldFormAll[formIndex].acceptType" placeholder="请选择" clearable :disabled="action.isReadonly">
                <el-option v-for="dict in acceptTypes" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收项目" prop="acceptPro">
              <el-input :readonly="action.isReadonly" v-model="fieldFormAll[formIndex].acceptPro" placeholder="请输入验收项目"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收内容" prop="acceptContent" style="width:98%">
              <el-input :readonly="action.isReadonly" type="textarea" :rows="2" v-model="fieldFormAll[formIndex].acceptContent" placeholder="请输入验收内容"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input :readonly="action.isReadonly" type="textarea" :rows="2" v-model="fieldFormAll[formIndex].remarks" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort" style="width:98%">
              <el-input :readonly="action.isReadonly" type="number" v-model="fieldFormAll[formIndex].sort" placeholder="请输入排序"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="fieldFormAll[formIndex].status" placeholder="请选择" clearable :disabled="action.isReadonly">
                <el-option v-for="dict in statusList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

    </create-sections>
    <div class="handle-bar">
      <el-button class="handle-bar-button" type="text" icon="el-icon-plus" @click="addItem()">添加
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

  data () {
    return {
      //检查分类
      checkClassifications: [],
      /**表单校验规则 */
      rules: {
        acceptType: [
          { required: true, message: "必填项不得为空", trigger: "change" }
        ],
        acceptPro: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        acceptContent: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "必填项不得为空", trigger: "blur" }
        ],
      },
      //所有费用item
      pageAllItems: [],
      fieldFormAll: [],
      //基础字段
      baseFields: [],
      // 被删除的隐患信息
      delItemIds: [],
      //字典项
      acceptTypes: [],//验收类型
      statusList: [{ 'dictLabel': '启用', 'dictValue': '0' }, { 'dictLabel': '停用', 'dictValue': '1' }]
    };
  },
  computed: {},
  watch: {},
  created () {
    //this.getField()
    //获取字典表数据
    this.initDicts();
  },

  mounted () {
    // this.getQuestionList()
  },
  beforeDestroy () { },

  methods: {
    //父组件调用  校验数据项
    startCheck () {
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
    addItem () {
      this.handle(this.baseFields);
    },
    deleteItems (index) {
      //记录被删除的、已存在的信息
      var item = this.fieldFormAll[index];
      if (item.expenseId) {
        this.delItemIds.push(item.expenseId);
      }
      this.fieldFormAll.splice(index, 1);
      this.pageAllItems.splice(index, 1);
    },

    //格式化 字段数据
    handle (list) {
      const fieldForm = {'status':'0'}; //初始化
      this.fieldFormAll.push(fieldForm);
      //加入一次空遍历
      this.pageAllItems.push({});
    },
    /**
     * 关闭
     */
    close () {
      this.$emit("close");
    },
    // 初始化字典数据
    initDicts () {
      this.getDicts("b_qual_accept_type").then(response => {
        this.acceptTypes = response.data;
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
  width: 98%;
}
/deep/ .el-form--label-top .el-form-item__label {
  float: none;
  display: inline;
  text-align: left;
  padding: 0 0 10px;
}
</style>
