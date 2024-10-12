<template>
  <slide-view
    v-empty="!canShowDetail"
    :listener-ids="listenerIDs"
    :no-listener-ids="noListenerIDs"
    :no-listener-class="noListenerClass"
    :body-style="{padding: 0, height: '100%'}"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限"
    @afterEnter="viewAfterEnter"
    @close="hideView">
    <div
      v-loading="loading"
      ref="crmDetailMain"
      class="detail-main">
      <flexbox
        v-if="canShowDetail && detailData"
        direction="column"
        align="stretch"
        class="d-container">
        <c-r-m-detail-head
          :detail="detailData"
          :head-details="headDetails"
          :id="id"
          :crm-type="crmType"
          @handle="detailHeadHandle"
          @close="hideView" />
        <examine-info
          v-if="detailData.examineRecordId"
          :id="id"
          :record-id="detailData.examineRecordId"
          :owner-user-id="detailData.ownerUserId"
          class="examine-info"
          examine-type="crm_receivables"
          @on-handle="examineHandle" />
        <div class="d-container-bd">
          <el-tabs
            v-model="tabCurrentName"
            type="border-card"
            class="d-container-bd--left">
            <el-tab-pane
              v-for="(item, index) in tabNames"
              :key="index"
              :label="item.label"
              :name="item.name"
              lazy>
              <component
                :is="item.name"
                :detail="detailData"
                :id="id"
                :crm-type="crmType" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </flexbox>
    </div>

    <c-r-m-all-create
      v-if="isCreate"
      :action="{type: 'update', id: id, batchId: detailData.batchId}"
      :crm-type="crmType"
      @save-success="editExpenseSaveSuccess"
      @close="isCreate=false" />
  </slide-view>
</template>

<script>
import { crmExpenseReadAPI } from '@/api/crm/expense'

import SlideView from '@/components/SlideView'
import CRMDetailHead from '../components/CRMDetailHead'
import CRMEditBaseInfo from '../components/CRMEditBaseInfo' // 基本信息
import RelativeFiles from '../components/RelativeFiles' // 相关附件
import RelativeHandle from '../components/RelativeHandle' // 相关操作
import CRMAllCreate from '../components/CRMAllCreate' // 新建页面
import ExamineInfo from '@/components/Examine/ExamineInfo'

import DetailMixin from '../mixins/Detail'
import { separator } from '@/filters/vueNumeralFilter/filters'

export default {
  // 客户管理 的 费用详情
  name: 'ExpenseDetail',
  components: {
    SlideView,
    CRMDetailHead,
    CRMEditBaseInfo,
    RelativeFiles,
    RelativeHandle,
    ExamineInfo,
    CRMAllCreate
  },
  mixins: [DetailMixin],
  props: {
    // 详情信息id
    id: [String, Number],
    // 监听的dom 进行隐藏详情
    listenerIDs: {
      type: Array,
      default: () => {
        return ['crm-main-container']
      }
    },
    // 不监听
    noListenerIDs: {
      type: Array,
      default: () => {
        return []
      }
    },
    noListenerClass: {
      type: Array,
      default: () => {
        return ['el-table__body']
      }
    }
  },
  data() {
    return {
      // 展示加载loading
      loading: false,
      crmType: 'expense',
      // 名称
      name: '',
      headDetails: [
        { title: '费用类型', value: '' },
        { title: '费用金额', value: '' },
        { title: '费用日期', value: '' },
      ],
      tabCurrentName: 'CRMEditBaseInfo',
      // 编辑操作
      isCreate: false
    }
  },
  computed: {
    tabNames() {
      var tempsTabs = [
        { label: '详细资料', name: 'CRMEditBaseInfo' },
        {
          label: this.getTabName('附件', this.tabsNumber.fileCount),
          name: 'RelativeFiles'
        },
        { label: '操作记录', name: 'RelativeHandle' }
      ]
      //这是打印
      if (this.crm.expense && this.crm.expense.print) {
        tempsTabs.push({ label: '打印记录', name: 'RelativePrint' })
      }

      return tempsTabs
    }
  },
  mounted() {},
  methods: {
    /**
     * 编辑成功
     */
    editExpenseSaveSuccess() {
      this.$bus.emit('crm-tab-num-update')
      this.$emit('handle', { type: 'save-success' })
      this.getDetial();
      this.tabCurrentName = 'CRMEditBaseInfo';
    },
    /**
     * 详情
     */
    getDetial() {
      this.loading = true
      crmExpenseReadAPI({
        id: this.id
      })
        .then(res => {
          this.loading = false
          this.name = res.data.expenseNum
          this.detailData = res.data
          //
          this.headDetails[0].value = res.data.expenseType
          this.headDetails[1].value = separator(res.data.money || 0)
          this.headDetails[2].value = res.data.expenseTime
        })
        .catch(() => {
          this.loading = false
          this.hideView()
        })
    },

    /**
     * 关闭
     */
    hideView() {
      this.$emit('hide-view')
    },

    // /**
    //  * 编辑成功
    //  */
    // editSaveSuccess() {
    //   this.$emit('handle', { type: 'save-success' })
    //   this.getDetial()
    // },

    /**
     * 审核操作
     */
    examineHandle() {
      this.$emit('handle', { type: 'examine' })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/crmdetail.scss';
</style>
