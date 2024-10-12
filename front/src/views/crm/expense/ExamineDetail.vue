<template>
  <slide-view
    v-empty="!canShowExaminesDetail"
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
        v-if="canShowExaminesDetail && detailData"
        direction="column"
        align="stretch"
        class="d-container">
        <c-r-m-detail-head
          :detail="detailData"
          :head-details="headDetails"
          :id="id"
          :crm-type="crmType"
          @handle="detailHeadHandle"
          @save-success="detailHeadHandle"
          @close="hideView"/>
        <examine-info
          v-if="detailData.examineRecordId"
          :id="id"
          :record-id="detailData.examineRecordId"
          :owner-user-id="detailData.createUserId"
          class="examine-info"
          examine-type="crm_expense"
          @on-handle="examineHandle"/>
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
                :ref="item.name"
                :detail="detailData"
                :id="id"
                :crm-type="crmType"
                @handle="handleHandle"/>
            </el-tab-pane>
          </el-tabs>
        </div>
      </flexbox>
    </div>

    <!-- 报销编辑 -->
    <expense-examine
      v-if="isCreate"
      :action="{type: 'update', id: id,data:detailData}"
      @close="isCreate = false"
      @save-success="handleHandle"
    />

  </slide-view>
</template>

<script>
  import {crmExpenseExaminesReadAPI,crmExpenseExamineDeleteAPI} from '@/api/crm/expense'

  import SlideView from '@/components/SlideView'
  import CRMDetailHead from '../components/CRMDetailHead'
  import RelativeHandle from '../components/RelativeHandle' // 相关操作

  import DetailMixin from '../mixins/ExpenseExamiesDetail'
  import {separator} from '@/filters/vueNumeralFilter/filters'
  import ExamineInfo from '@/components/Examine/ExamineInfo'
  import ExpenseItems from '../components/RelativeExpense';

  import ExpenseExamine from './Examine'

  export default {
    // 客户管理 的 费用审批详情
    name: 'ExpenseDetail',
    components: {
      SlideView,
      CRMDetailHead,
      RelativeHandle,
      ExpenseExamine,
      ExpenseItems,
      ExamineInfo
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
        crmType: 'expense-examines',
        // 名称
        name: '',
        headDetails: [
          {title: '费用总额', value: ''},
        ],
        tabCurrentName: 'ExpenseItems',
        // 编辑操作
        isCreate: false,
      }
    },
    watch: {
      id: function () {
        if (this.canShowExaminesDetail) {
          this.detailData = null
          this.tabsNumber = {}
          this.getDetail()
          this.getTabsNum()
        }
      },

      detailData() {
        if (this.$refs.crmDetailMain && this.$refs.crmDetailMain.style.background == 'white') {
          this.$refs.crmDetailMain.style.background = 'inherit'
        }
      }
    },
    computed: {
      tabNames() {
        var tempsTabs = [
          {
            label: this.getTabName('费用明细', this.tabsNumber.moneyCount),
            name: 'ExpenseItems'
          },
          {label: '操作记录', name: 'RelativeHandle'}
        ]

        return tempsTabs
      },
      // 能否查看详情
      canShowExaminesDetail() {
        if (this.detailData && this.detailData.dataAuth === 0) {
          return false
        }
        var flag = this.crm && this.crm.expense && this.crm.expense.read;
        return flag;
      },
    },
    mounted() {
    },
    methods: {
      /**
       * 详情
       */
      getDetail() {
        this.loading = true
        crmExpenseExaminesReadAPI({
          id: this.id
        })
          .then(res => {
            this.loading = false
            this.detailData = res.data
            //
            this.headDetails[0].value = separator(res.data.money || 0)
          })
          .catch(() => {
            this.loading = false
            this.hideView()
          })
      },
      //
      deleteExpenseExamine(){
        this.loading = true
        crmExpenseExamineDeleteAPI({
          id: this.id
        }).then(res => {
          this.loading = false
          this.hideView();
        })
          .catch(() => {
            this.loading = false
          });
      },
      /** 勾选操作 */
      handleHandle(data) {
        this.$nextTick(() => {
          this.$bus.emit('crm-tab-num-update')
        })
        //更新列表
        this.$refs.ExpenseItems[0].getDetail();
        this.getDetail()
        this.$emit('handle',data)
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
      //   this.getDetail()
      // },

      /**
       * 审核操作
       */
      examineHandle() {
        this.$emit('handle', {type: 'examine'})
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import '../styles/crmdetail.scss';
</style>
