<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="sureBtnTitle"
    @close="close"
    @save="saveClick(false)">
    <el-form
      ref="crmForm"
      :model="crmForm"
      :rules="rules"
      label-position="top"
      class="crm-create-box"
    >
      <create-sections title="基本信息">
        <el-row>
          <el-col :span="12">
            <el-form-item prop="content"
                          style="padding: 0 20px;margin-bottom: 20px"
            >
              <div
                slot="label"
                style="display: inline-block;">
                <div class="form-label">报销说明
                  <span style="color:#999;">
                        （此次报销费用情况说明）
                      </span>
                </div>
              </div>
              <el-input
                v-model="crmForm.content"
                :autosize="{ minRows: 3}"
                :maxlength="200"
                type="textarea"
                show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item style="padding: 0 20px 0 0;">
              <div
                slot="label"
                style="display: inline-block;">
                <div class="form-label">
                  费用总额
                </div>
              </div>
              <el-input
                type="number"
                v-model="crmForm.money"
                :disabled="true"
                :maxlength="100"/>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="费用信息">
        <expense-items
          :action="action"
          ref="expenseItems" @change="handleItemsChange"
        ></expense-items>
      </create-sections>
    </el-form>
    <create-sections
      v-if="isOpenExamine"
      title="审核信息">
      <template slot="header">
        <div
          v-if="examineInfo.examineType===1 || examineInfo.examineType===2"
          class="examine-type">({{ examineInfo.examineType===1 ? '固定审批流' : '授权审批人' }})
        </div>
        <el-tooltip
          v-if="examineInfo && examineInfo.remarks"
          :content="examineInfo.remarks"
          effect="dark"
          placement="top">
          <i class="wk wk-help wk-help-tips" style="height: 13px;"/>
        </el-tooltip>
      </template>
      <create-examine-info
        ref="examineInfo"
        :types-id="action.id"
        types="crm_expense"
        @value-change="examineValueChange"/>
    </create-sections>

    <el-button
      slot="footer"
      type="primary"
      plain
      @click="saveClick(true)">保存草稿
    </el-button>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import CreateExamineInfo from '@/components/Examine/CreateExamineInfo'

  import CustomFieldsMixin from '@/mixins/CustomFields'

  import ExpenseItems from './ExpenseItems'

  import {crmExpenseExamineSaveAPI} from '@/api/crm/expense'

  export default {
    // 新建编辑
    name: 'ExpenseExamine',

    components: {
      XrCreate,
      CreateSections,
      CreateExamineInfo,
      ExpenseItems
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
        crmForm: {money: 0},
        rules: {
          content: [{'message': '报销事由不能为空', 'required': true}]
        },
        // 审批信息
        examineInfo: {},
      }
    },

    computed: {
      title() {
        return this.action.type === 'update' ? '编辑报销' : '新建报销'
      },
      // 费用 下展示审批人信息
      isOpenExamine() {
        if (this.examineInfo) {
          // 初始状态是空对象默认展示，请求之后，根据status判断
          return Object.keys(this.examineInfo).length > 0 ? this.examineInfo.status == 1 : true
        }
        return false
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

      this.setData()
    },

    mounted() {

    },

    beforeDestroy() {
    },

    methods: {
      setData() {
        this.crmForm = this.action.data;
      },
      /**
       * 保存
       */
      saveClick(isDraft) {//isDraft 是否提交
        const crmForm = this.$refs.crmForm;
        crmForm.validate(valid => {
          if (valid) {
            //校验费用项
            var checkResult = this.$refs.expenseItems.startCheck();
            if (!checkResult) {//数据项不合法
              return;
            }
            this.loading = true;
            if (this.isOpenExamine) {
              /** 验证审批数据 */
              if (isDraft) {
                if (
                  this.examineInfo.examineType === 2 &&
                  this.examineInfo.hasOwnProperty('value') &&
                  this.examineInfo.value.length
                ) {
                  this.crmForm.checkUserId = this.examineInfo.value[0].userId
                }
                this.crmForm.checkStatus = 5
                this.submiteParams()
              } else {
                this.$refs.examineInfo.validateField((result) => {
                  if (result) {
                    if (this.examineInfo.examineType === 2) {
                      this.crmForm.checkUserId = this.examineInfo.value[0].userId
                    }
                    this.crmForm.checkStatus = null;
                    this.submiteParams()
                  } else {
                    this.loading = false
                  }
                })
              }
            } else {
              if (isDraft) {
                this.crmForm.checkStatus = 5
              }
              this.submiteParams()
            }
          }
        })
      },

      /**
       * 提交上传
       */
      submiteParams() {
        //现场组装数据
        var baseFields = this.$refs.expenseItems.baseFields;
        var fieldFormAll = this.$refs.expenseItems.fieldFormAll;
        var batchIdAll = this.$refs.expenseItems.batchIdAll;
        var delItemIds = this.$refs.expenseItems.delItemIds;
        var params = {examinesData: this.crmForm, expenseList: [], delItemIds: delItemIds};
        //审批人
        params.checkUserId = this.crmForm.checkUserId;
        var expenseList = [];
        var time = 0;
        fieldFormAll.forEach(item => {
          var one = this.getSubmiteParams(baseFields, item);
          if (this.action.type == 'update') {
            //单独处理明细主键
            one.entity.expenseId = item.expenseId;
          }
          //附件
          one.entity.batchId = batchIdAll[time];
          expenseList.push(one);
          time++;
        })
        params.expenseList = expenseList;
        //异步提交
        var fileFlag = this.$refs.expenseItems.submitImgs();
        if (!fileFlag) {
          this.$message({
            type: 'error',
            message: '发票操作异常'
          })
          this.loading = false
          return;
        }
        //是否新增
        if (this.action.type == 'update') {
          params.isCreate = '0';
        } else {
          params.isCreate = '1';
        }
        console.log(params)
        crmExpenseExamineSaveAPI(params)
          .then(res => {
            this.loading = false

            this.$message.success(
              '操作成功'
            )

            this.close()

            // 保存成功
            this.$emit('save-success', {
              type: 'expense-examines'
            })
          })
          .catch(() => {
            this.loading = false
          })
      },
      handleItemsChange(items) {
        //console.log("结果：", items)
        //计算金额
        var total = 0;
        items.forEach(item => {
          if (item.money) {
            total += item.money;
          }
        })
        this.crmForm.money = total;
      },
      /**
       * 验证唯一
       */
      UniquePromise({field, value}) {
        return this.getUniquePromise(field, value, this.action)
      },

      /**
       * change
       */
      formChange(field, index, value, valueList) {
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

  // 占用一整行
  .crm-create-block-item {
    flex: 0 0 100%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  .el-form-item /deep/ .el-form-item__label {
    line-height: normal;
    font-size: 13px;
    color: #333333;
    position: relative;
    padding-left: 8px;
    padding-bottom: 0;
  }

  .el-form /deep/ .el-form-item {
    margin-bottom: 0px;
  }

  .el-form /deep/ .el-form-item.is-required .el-form-item__label:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
    position: absolute;
    left: 0;
    top: 5px;
  }

  .form-label {
    margin: 5px 0;
    font-size: 13px;
    word-wrap: break-word;
    word-break: break-all;
  }

</style>
