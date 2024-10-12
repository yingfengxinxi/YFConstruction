import {
  mapGetters
} from 'vuex'

import { crmLeadsNumAPI, crmLeadsStarAPI } from '@/api/crm/leads'
import { crmCustomerNumAPI, crmCustomerStarAPI } from '@/api/crm/customer'
import { crmContactsNumAPI, crmContactsStarAPI } from '@/api/crm/contacts'
import { crmBusinessNumAPI, crmBusinessStarAPI } from '@/api/crm/business'
import { crmContractNumAPI } from '@/api/crm/contract'
import { crmProductNumAPI } from '@/api/crm/product'
import { crmReceivablesNumAPI } from '@/api/crm/receivables'
import { crmProjectNumAPI } from '@/api/pm/project'
import { crmExpenseNumAPI } from '@/api/crm/expense'
import {crmExpenseExaminesNumAPI} from '@/api/crm/expense'
import { debounce } from 'throttle-debounce'

export default {
  data() {
    return {
      showFirstDetail: true,
      detailData: null,
      // tabs Number
      tabsNumber: {}
    }
  },
  props: {
    /** 是公海 默认是客户 */
    isSeas: {
      type: Boolean,
      default: false
    }
  },

  computed: {
    ...mapGetters(['crm']),
    ...mapGetters(['project']),
    // 是否能新建跟进记录
    canCreateFollowRecord() {
      return this.crm && this.crm.followRecord && this.crm.followRecord.save
    },
    showTabsNumber() {
      return this.crmType !== 'visit'
    }
  },


  mounted() {
    this.$refs.crmDetailMain.style.background = 'white'
    this.debouncedGetTabsNum = debounce(300, this.getTabsNum)
  },

  beforeDestroy() {
    this.$bus.off('crm-tab-num-update', this.debouncedGetTabsNum)
  },

  methods: {
    viewAfterEnter() {
      if (this.canShowExaminesDetail) {
        this.getDetail()
        this.getTabsNum()
        this.$bus.on('crm-tab-num-update', this.debouncedGetTabsNum)
      }
    },

    /**
     * 顶部头 操作
     * @param {*} data
     */
    detailHeadHandle(data) {
      console.log(data)
      if (data.type === 'edit') {
        this.isCreate = true
      } else if (data.type === 'cancel') {
        this.getDetail()
      } else if (data.type === 'delete' || data.type === 'exit-team') {
        //执行删除
        this.hideView()
      } else if (data.type === 'state_start' || data.type === 'state_disable') {
        this.getDetail()
      }

      if (data.type === 'edit') {
        this.getDetail()
      }

      this.$emit('handle', data)
    },
    /**
     * 编辑成功
     */
    editSaveSuccess() {
      this.$bus.$emit('crm-detail-update', this.crmType)
      this.$emit('handle', { type: 'save-success' })
      this.getDetail()
    },

    /**
     * 获取tabs的数据数量
     */
    getTabsNum() {
      if (!this.showTabsNumber) {
        return
      }
      //TODO 详情页面扩展类型
      const request = {
        leads: crmLeadsNumAPI,
        customer: crmCustomerNumAPI,
        contacts: crmContactsNumAPI,
        business: crmBusinessNumAPI,
        contract: crmContractNumAPI,
        product: crmProductNumAPI,
        receivables: crmReceivablesNumAPI,
        expense: crmExpenseNumAPI,
        'expense-examines':crmExpenseExaminesNumAPI
      }[this.crmType]

      const params = {}
      params[`id`] = this.id

      request(params)
        .then(res => {
          this.tabsNumber = res.data || {}
        })
        .catch(() => {
        })
    },

    /**
     * 获取tabs名字
     * @param {*} name
     * @param {*} num
     */
    getTabName(name, num) {
      return `${name}${num && num > 0 ? '（' + num + '）' : ''}`
    },

    /**
     * 切换关注状态
     * @param index
     * @param status
     */
    toggleStar() {
      this.loading = true

      const request = {
        leads: crmLeadsStarAPI,
        customer: crmCustomerStarAPI,
        contacts: crmContactsStarAPI,
        business: crmBusinessStarAPI
      }[this.crmType]

      const params = {}
      params[`id`] = this.detailData[`${this.crmType}Id`]
      request(params).then(() => {
        this.loading = false
        this.$message.success(this.detailData.star > 0 ? '取消关注成功' : '关注成功')
        this.detailData.star = this.detailData.star > 0 ? 0 : 1
        this.$emit('handle', { type: 'star' })
      }).catch(() => {
        this.loading = false
      })
    }
  },

  deactivated: function() { }

}
