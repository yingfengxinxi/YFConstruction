<template>
  <div>
    <create-sections v-for="(item, formIndex) in pageAllItems"
                     :key="formIndex">
      <flexbox class="expense-item-head">
        <div class="expense-item-head-title">报销费用明细<span v-if="formIndex != 0">（{{ formIndex }}）</span></div>
        <i
          v-if="formIndex != 0"
          class="el-icon-delete expense-item-head-delete"
          @click="deleteItems(formIndex)"/>
      </flexbox>
      <wk-form
        :ref="'crmForm'+formIndex"
        :model="fieldFormAll[formIndex]"
        :rules="fieldRulesAll[formIndex]"
        :field-from="fieldFormAll[formIndex]"
        :field-list="fieldListAll[formIndex]"
        label-position="left"
        @change="componentValue"
      >
        <template slot-scope="{ data }">
          <crm-relative-cell
            v-if="data && data.formType == 'customer'"
            :value="fieldFormAll[formIndex][data.field]"
            :disabled="data.disabled"
            relative-type="customer"
            :index="formIndex"
            @value-change="otherChange($event, data)"
          />
          <crm-relative-cell
            :index="formIndex"
            v-if="data && data.formType == 'contract'"
            :value="fieldFormAll[formIndex][data.field]"
            :disabled="data.disabled"
            :relation="data.relation"
            :relative-type="data.formType"
            @value-change="otherChange($event, data)"
          />
          <crm-relative-cell
            :index="formIndex"
            v-if="data && (data.formType == 'business')"
            :value="fieldFormAll[formIndex][data.field]"
            :disabled="data.disabled"
            :relation="data.relation"
            :relative-type="data.formType"
            @value-change="otherChange($event, data)"
          />
        </template>
      </wk-form>
      <!--发票上传-->
      <wk-upload-img
        :ref="'wkUploadImg'+formIndex"
        name="file"
        multiple
        accept="image/*"
        list-type="picture-card"
        :autoUpload="false"
        :title="'发票上传'"
        :batchId="batchIdAll[formIndex]"
      ></wk-upload-img>
    </create-sections>
    <div class="handle-bar">
      <el-button
        class="handle-bar-button"
        type="text"
        icon="el-icon-plus"
        @click="addItem()">添加明细
      </el-button>
    </div>
  </div>
</template>

<script>
  import {filedGetFieldAPI} from '@/api/crm/common'
  import CreateSections from '@/components/CreateSections'
  import WkForm from '@/components/NewCom/WkForm'
  import {
    CrmRelativeCell
  } from '@/components/CreateCom'

  import crmTypeModel from '@/views/crm/model/crmTypeModel'
  import CustomFieldsMixin from '@/mixins/CustomFields'

  import {guid, objDeepCopy} from '@/utils'

  import {filedGetFieldExmaines} from '@/api/crm/expense'

  import WkUploadImg from '@/components/CreateCom/WkUploadImg'

  export default {
    // 新建编辑
    name: 'ExpenseCreate',

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
        default: () => {
          return {
            type: 'save',
            id: '',
            data: {}
          }
        }
      }
    },

    data() {
      return {
        //所有费用item
        pageAllItems: [],
        fieldListAll: [],
        fieldFormAll: [],
        fieldRulesAll: [],
        //基础字段
        baseFields: [],
        //附件
        batchIdAll: [],
        delItemIds:[]
      }
    },

    computed: {},

    watch: {},

    created() {
      this.getField();
    },

    mounted() {
    },
    beforeDestroy() {
    },

    methods: {
      componentValue(params) {//handleItemsChange
        //console.log(params,this.fieldFormAll)
        this.$emit("change", this.fieldFormAll);
      },
      //父组件调用  校验数据项
      startCheck() {
        var flag = true;
        for (var i = 0; i < this.pageAllItems.length; i++) {
          this.$refs['crmForm' + i][0].instance.validate(valid => {
            if (!valid) {
              flag = false;
            }
          });
        }
        return flag;
      },
      submitImgs() {
        var flag = true;
        for(var i = 0;i < this.pageAllItems.length; i++){
          var ref = 'wkUploadImg' + i;
          flag = this.$refs[ref][0].submitAsync();
        }
        return flag;
      },
      //新增费用项
      addItem() {
        this.handle(this.baseFields);
      },
      deleteItems(index) {
        //记录被删除的、已存在的费用明细
        var item = this.fieldFormAll[index];
        if(item.expenseId){
          this.delItemIds.push(item.expenseId);
        }
        this.fieldListAll.splice(index, 1)
        this.fieldFormAll.splice(index, 1)
        this.fieldRulesAll.splice(index, 1)
        this.pageAllItems.splice(index, 1)
      },
      /**
       * 获取数据
       */
      getField() {
        const params = {
          label: crmTypeModel.expense
        }
        filedGetFieldAPI(params)
          .then(res => {
            var data = res.data || [];
            this.baseFields = data;
            //组装带值的fields
            if (this.action.type == 'update') {
              params.id = this.action.id
              filedGetFieldExmaines(params)
                .then(res => {
                  var dataArr = res.data || [];
                  dataArr.forEach(data => {
                    //处理
                    this.handle(data);
                  })
                })
                .catch(() => {
                })
            } else {
              //处理
              this.handle(data);
            }

          })
          .catch(() => {
          })
      },
      //格式化 字段数据
      handle(list) {
        console.log(list)
        const fieldList = []
        const fieldRules = {}
        const fieldForm = {}
        var batchId = '';
        list.forEach(item => {
          const temp = {}
          temp.field = item.fieldName
          temp.formType = item.formType
          temp.fieldId = item.fieldId
          temp.inputTips = item.inputTips
          temp.name = item.name
          temp.setting = item.setting
          const canEdit = this.getItemIsCanEdit(item, this.action.type)
          // 是否能编辑权限
          if (canEdit) {
            // 自动生成编号
            if (item.autoGeneNumber == 1) {
              temp.placeholder = '根据编号规则自动生成，支持手动输入'
              const copyItem = objDeepCopy(item)
              copyItem.isNull = 0
              fieldRules[temp.field] = this.getRules(copyItem)
            } else {
              fieldRules[temp.field] = this.getRules(item)
            }
          }

          // 是否可编辑
          temp.disabled = !canEdit
          // 禁止某些业务组件选择
          if (temp.formType == 'business' || temp.formType == 'contract' ||
            temp.formType == 'customer') {
            if (this.action.type == 'relative') {
              const relativeDisInfos = {
                contacts: {customer: true},
                customer: {customer: true},
                business: {customer: true},
                contract: {customer: true, contract: true},
                expense: {customer: true, contract: true, business: true}
              }

              // 在哪个类型下添加
              const relativeTypeDisInfos = relativeDisInfos[this.action.crmType]
              if (relativeTypeDisInfos) {
                // 包含的字段值
                temp.disabled = relativeTypeDisInfos[item.formType] || false
              }
            } else if (this.action.type != 'update') {
              temp.disabled = item.formType === 'contract' || item.formType === 'business'
            }
          }
          // 处理关联
          if (this.action.type == 'relative' || this.action.type == 'update' && (item.formType == 'business' || item.formType == 'contract'
          )) {
            const customerItem = this.getItemRelatveInfo(list, 'customer')
            if (customerItem) {
              customerItem['moduleType'] = 'customer'
              customerItem['params'] = {checkStatus: 1}
              temp['relation'] = customerItem
            }
          }

          // 特殊字段允许多选
          this.getItemRadio(item, temp)

          if (item.fieldName == 'expenseId') {
            fieldForm[temp.field] = item.value;
          } else if (item.fieldName == 'batchId') {
            batchId = item.value;
          } else {
            // 获取默认值
            fieldForm[temp.field] = this.getItemValue(item, this.action.data, this.action.type)
            fieldList.push(temp)
          }
        })
        this.fieldListAll.push(fieldList)
        this.fieldFormAll.push(fieldForm)
        this.fieldRulesAll.push(fieldRules)
        if (!batchId) {
          batchId = guid();
        }
        this.batchIdAll.push(batchId);
        //加入一次空遍历
        this.pageAllItems.push({});
        //console.log(this.fieldListAll, this.fieldFormAll, this.fieldRulesAll, this.pageAllItems)
      },
      /**
       * 获取字段是否可编辑
       */
      getItemIsCanEdit(item, type) {
        // authLevel 1 不能查看不能编辑 2可查看  3 可编辑可查看
        return (type === 'update' && item.authLevel == 3) || type !== 'update'
      },
      /**
       * 地址change
       */
      otherChange(data, field) {
        var index = data.index;
        if (field.formType === 'customer') {
          this.fieldListAll[index].forEach(fieldItem => {
            if (fieldItem.formType === 'contract' || fieldItem.formType === 'business') {
              // 如果是合同 改变合同样式和传入客户ID
              if (data.value.length > 0) {
                fieldItem.disabled = false
                const customerItem = data.value[0]
                customerItem['moduleType'] = 'customer'
                customerItem['params'] = {checkStatus: 1}
                fieldItem['relation'] = customerItem
              } else {
                fieldItem.disabled = true
                fieldItem['relation'] = {}
              }
              this.fieldFormAll[index][fieldItem.field] = []
            }
          })
        }
        this.$set(this.fieldFormAll[index], field.field, data.value)
      },

      /**
       * 关闭
       */
      close() {
        this.$emit('close')
      },

    }
  }
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
        color: #2362FB;
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
      color: #2362FB;
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
    /deep/
    .el-form-item.is-product {
      flex: 0 0 100%;

    }

  }

  .wk-form
  /deep/
  .el-form-item {
    margin-bottom: 20px !important;
  }
</style>
