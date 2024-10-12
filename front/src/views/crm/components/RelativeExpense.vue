<template>
  <div v-loading="loading" class="rc-cont">
    <flexbox
      class="rc-head"
      direction="row-reverse">
      <el-button
        class="xr-btn--orange rc-head-item"
        icon="el-icon-plus"
        type="primary"
        @click="createClick">新建费用</el-button>

    </flexbox>
    <el-table
      :data="list"
      :height="tableHeight"
      class="file-table"
      stripe
      :cell-class-name="cellClassName"
      style="width: 100%;border: 1px solid #E6E6E6;"
      @row-click="handleRowClick">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :align="item.align"
        show-overflow-tooltip />

    </el-table>

    <!-- 相关详情页面 -->
    <c-r-m-full-screen-detail
      :visible.sync="showDview"
      :id="rowID"
      crm-type="expense"
      @handle="detailHandle" />

    <!-- 新建 -->
    <expense-create
      v-if="createShow"
      :action="{type:'examine',id:id}"
      @close="createShow = false"
      @save-success="detailHandle"
    />
  </div>
</template>

<script type="text/javascript">

import { crmExaminesQueryExpenseAPI } from '@/api/crm/expense'
import { debounce } from 'throttle-debounce'

import ExpenseCreate from '../expense/Create'

export default {
  name: 'RelativeExpense', // 费用明细列表
  components: {
    CRMFullScreenDetail: () => import('@/components/CRMFullScreenDetail'),
    ExpenseCreate
  },
  props: {
    /** 模块ID */
    id: [String, Number],
    /** 没有值就是全部类型 有值就是当个类型 */
    crmType: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      loading: false,
      list: [],
      fieldList: [],
      tableHeight: '400px',
      searchType:{
        'expense-examines':'examineId',
        'customer':'customerId',
        'business':'businessId',
        'contract':'contractId',
      },
      rowID:-1,
      showDview:false,
      //新建
      createShow:false
    }
  },
  inject: ['rootTabs'],
  computed: {},
  watch: {
    id(val) {
      this.list = []
      this.getDetail()
    },

    'rootTabs.currentName'(val) {
      if (val === 'RelativeExpense') {
        this.getDetail(false)
      }
    }
  },
  mounted() {
    this.fieldList.push({ prop: 'expenseNum', width: '200', label: '费用编号',align:'center' })
    this.fieldList.push({ prop: 'expenseType', width: '100', label: '费用类型',align:'center'  })
    this.fieldList.push({
      prop: 'money',
      width: '100',
      label: '费用金额',align:'center'
    })
    this.fieldList.push({
      prop: 'expenseTime',
      width: '200',
      label: '费用日期',align:'center'
    })
    this.fieldList.push({ prop: 'hasInvoice', width: '150', label: '是否有发票',align:'center'  })
    this.getDetail()

    this.debouncedGetDetail = debounce(300, () => {
      this.$bus.emit('crm-tab-num-update')
      this.getDetail()
    })
  },
  methods: {
    createClick(){
      this.createShow = true;
    },
    getDetail(loading = true) {
      this.loading = loading
      const params = {}
      params[this.searchType[this.crmType]] = this.id
      crmExaminesQueryExpenseAPI(params)
        .then(res => {
          this.loading = false
          this.list = res.data.list || [];
        })
        .catch(() => {
          this.loading = false
        })
    },

    // 当某一行被点击时会触发该事件
    handleRowClick(row, column, event) {
      if(column.property == "expenseNum"){//"expenseNum"
        this.rowID = row.expenseId;
        this.showDview = true;
      }
    },
    /**
     * 详情操作
     */
    detailHandle(data) {
      this.getDetail()
      this.$emit('handle',data);
    },
    /**
     * 通过回调控制class
     */
    cellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'expenseNum') {
        return 'can-visit--underline'
      } else {
        return ''
      }
    },
  }
}
</script>
<style lang="scss" scoped>
@import '../styles/relativecrm.scss';

.h-item {
  font-size: 13px;
  color: #409eff;
  margin: 0 5px;
  cursor: pointer;
}

.rc-head-file {
  position: absolute;
  top: 0;
  right: 0;
  height: 98px;
  width: 98px;
  opacity: 0;
  z-index: -1;
  cursor: pointer;
}
</style>

