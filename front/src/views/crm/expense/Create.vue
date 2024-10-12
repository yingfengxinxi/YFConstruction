<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="sureBtnTitle"
    @close="close"
    @save="saveClick">
    <create-sections title="基本信息">
      <wk-form
        ref="crmForm"
        :model="fieldForm"
        :rules="fieldRules"
        :field-from="fieldForm"
        :field-list="fieldList"
        label-position="top"
        @change="formChange"
      >
        <template slot-scope="{ data }">
          <crm-relative-cell
            v-if="data && data.formType == 'customer'"
            :value="fieldForm[data.field]"
            :disabled="data.disabled"
            relative-type="customer"
            @value-change="otherChange($event, data)"
          />
          <crm-relative-cell
            v-if="data && data.formType == 'contract'"
            :value="fieldForm[data.field]"
            :disabled="data.disabled"
            :relation="data.relation"
            :relative-type="data.formType"
            @value-change="otherChange($event, data)"
          />
          <crm-relative-cell
            v-if="data && data.formType == 'invoice'"
            :value="fieldForm[data.field]"
            :disabled="data.disabled"
            :relation="data.relation"
            :relative-type="data.formType"
            @value-change="otherChange($event, data)"
          />
          <crm-relative-cell
            v-if="data && (data.formType == 'business')"
            :value="fieldForm[data.field]"
            :disabled="data.disabled"
            :relation="data.relation"
            :relative-type="data.formType"
            @value-change="otherChange($event, data)"
          />
        </template>
      </wk-form>
    </create-sections>

    <!-- 图片附件 -->
    <wk-upload-img
      ref="wkUploadImg"
      name="file"
      multiple
      accept="image/*"
      list-type="picture-card"
      :autoUpload="false"
      :title="'发票上传'"
      :batchId="batchId"
    ></wk-upload-img>
  </xr-create>
</template>

<script>
import { filedGetFieldAPI } from '@/api/crm/common'
import { crmExpenseSaveAPI } from '@/api/crm/expense'

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import WkForm from '@/components/NewCom/WkForm'
import {
  CrmRelativeCell
} from '@/components/CreateCom'
import CreateExamineInfo from '@/components/Examine/CreateExamineInfo'

import crmTypeModel from '@/views/crm/model/crmTypeModel'
import CustomFieldsMixin from '@/mixins/CustomFields'
import { debounce } from 'throttle-debounce'

import WkUploadImg from '@/components/CreateCom/WkUploadImg'

import {guid, objDeepCopy} from '@/utils'

export default {
  // 新建编辑
  name: 'ExpenseCreate',

  components: {
    XrCreate,
    CreateSections,
    CrmRelativeCell,
    CreateExamineInfo,
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
      loading: false,
      baseFields: [],
      fieldList: [],
      fieldForm: {},
      fieldRules: {},
      //附件
      batchId: "",
      // 审批信息
      examineInfo: {}
    }
  },

  computed: {
    title() {
      return this.action.type === 'update' ? '编辑费用' : '新建费用'
    },

    // 确认名称
    sureBtnTitle() {
      if (this.isOpenExamine) {
        return '提交审核'
      }
      return '保存'
    },

  },

  watch: {},

  created() {
    this.debouncedSaveField = debounce(300, this.saveClick)

    if(this.action.type == 'update'){
      this.batchId = this.action.batchId;
    }else{
      this.batchId = guid();
    }
    this.getField();
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 获取数据
     */
    getField() {
      this.loading = true
      const params = {
        label: crmTypeModel.expense
      }

      if (this.action.type == 'update') {
        params.id = this.action.id
      }
      filedGetFieldAPI(params)
        .then(res => {
          const list = res.data || []
          const fieldList = []
          const fieldRules = {}
          const fieldForm = {}
          list.forEach(item => {
            const temp = {}
            temp.field = item.fieldName
            temp.formType = item.formType
            temp.fieldId = item.fieldId
            temp.inputTips = item.inputTips
            temp.name = item.name
            temp.setting = item.setting

            // 是否能编辑权限
            if (this.action.type != 'update') {
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

            // 禁止某些业务组件选择
            if (temp.formType == 'business' || temp.formType == 'contract' ||
                temp.formType == 'customer') {
              if (this.action.type == 'relative') {
                const relativeDisInfos = {
                  contacts: { customer: true },
                  customer: { customer: true },
                  business: { customer: true },
                  contract: { customer: true, contract: true },
                  expense: { customer: true, contract: true , business: true }
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
                customerItem['params'] = { checkStatus: 1 }
                temp['relation'] = customerItem
              }
            }

            // 特殊字段允许多选
            this.getItemRadio(item, temp)

            // 获取默认值
            fieldForm[temp.field] = this.getItemValue(item, this.action.data, this.action.type)
            fieldList.push(temp)
          })
          this.baseFields = list
          this.fieldList = fieldList
          this.fieldForm = fieldForm
          this.fieldRules = fieldRules

          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 保存
     */
    saveClick(isDraft = false) {
      this.loading = true
      const crmForm = this.$refs.crmForm.instance
      crmForm.validate(valid => {
        if (valid) {
          const params = this.getSubmiteParams(this.baseFields, this.fieldForm)
          if (isDraft) {
            params.entity.checkStatus = 5
          }
          this.submiteParams(params)
        } else {
          this.loading = false
          // 提示第一个error
          this.getFormErrorMessage(crmForm)
          return false
        }
      })
    },

    /**
     * 提交上传
     */
    submiteParams(params) {
      params.entity.batchId = this.batchId
      params.entity.isCreate = "1";
      if (this.action.type == 'update') {//修改
        params.entity.expenseId = this.action.id
      }
      if (this.action.type == 'examine') {//审批新增
        params.entity.examineId = this.action.id
        params.entity.hasExamine ='1';
      }
      //TODO 如果为异步上传需要主动调用提交
      var fileFlag = this.$refs.wkUploadImg.submitAsync();
      if(!fileFlag){
        this.$message({
          type: 'error',
          message: '发票操作异常'
        })
        return;
      }
      // 相关添加时候的多余提交信息
      if (
        this.action.relativeData &&
        Object.keys(this.action.relativeData).length
      ) {
        params = { ...params, ...this.action.relativeData }
      }
      crmExpenseSaveAPI(params)
        .then(res => {
          this.loading = false

          this.$message.success(
            this.action.type == 'update' ? '编辑成功' : '添加成功'
          )

          this.close()

          // 保存成功
          this.$emit('save-success', {
            type: 'receivables'
          })
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 验证唯一
     */
    UniquePromise({ field, value }) {
      return this.getUniquePromise(field, value, this.action)
    },

    /**
     * change
     */
    formChange(field, index, value, valueList) {
    },

    /**
     * 地址change
     */
    otherChange(data, field) {
      if (field.formType === 'customer') {
        this.fieldList.forEach(fieldItem => {
          if (fieldItem.formType === 'contract' || fieldItem.formType === 'business' ) {
            // 如果是合同 改变合同样式和传入客户ID
            if (data.value.length > 0) {
              fieldItem.disabled = false
              const customerItem = data.value[0]
              customerItem['moduleType'] = 'customer'
              customerItem['params'] = { checkStatus: 1 }
              fieldItem['relation'] = customerItem
            } else {
              fieldItem.disabled = true
              fieldItem['relation'] = {}
            }
            this.fieldForm[fieldItem.field] = []
          }
        })
      }
      this.$set(this.fieldForm, field.field, data.value)
      this.$refs.crmForm.instance.validateField(field.field)
    },

    /**
     * 审批信息值更新
     */
    examineValueChange(data) {
      this.examineInfo = data
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

  // 图片 附件
  .img-accessory {
    padding: 0 20px;
    font-size: 12px;
    img {
      width: 16px;
      vertical-align: middle;
    }
    .img-box /deep/ .el-upload {
      width: 80px;
      height: 80px;
      line-height: 90px;
    }
    .img-box /deep/ .el-upload-list {
      .el-upload-list__item {
        width: 80px;
        height: 80px;
      }
    }
    .img-box {
      position: relative;
      margin-top: 40px;
      .add-img {
        position: absolute;
        left: 0;
        top: -30px;
        height: 20px;
        line-height: 20px;
        margin-bottom: 10px;
        color: #2362FB;
      }
    }
    .add-accessory {
      margin-top: 25px;
      margin-bottom: 20px;
      color: #2362FB;

      .wukong-file {
        font-size: 13px;
      }
    }
  }
.wk-form {
  /deep/ .el-form-item.is-product {
    flex: 0 0 100%;
  }
}
</style>
