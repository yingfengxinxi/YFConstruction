<template>
  <div>
    <c-r-m-list-head
      :search.sync="search"
      :crm-type="crmType"
      :create-fun="createClick"
      title="费用管理"
      placeholder="费用编号/客户名称/合同编号/商机名称"
      main-title="新建"
      @on-handle="listHeadHandle"
      @on-search="crmSearch"
      @on-export="exportInfos"
      @on-examine="createExamine"/>
    <div
      v-empty="!crm.expense.index"
      xs-empty-icon="nopermission"
      xs-empty-text="暂无权限"
      class="crm-container">
      <c-r-m-table-head
        ref="crmTableHead"
        :crm-type="crmType"
        @filter="handleFilter"
        @handle="handleHandle"
        @scene="handleScene"/>
      <el-table
        v-loading="loading"
        id="crm-table"
        :row-height="40"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        :header-cell-class-name="headerCellClassName"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
        @sort-change="sortChange"
        @header-dragend="handleHeaderDragend"
        @selection-change="handleSelectionChange">
        <el-table-column
          show-overflow-tooltip
          type="selection"
          align="center"
          width="55"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          sortable="custom"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <template v-if="item.prop == 'checkStatus'">
              <span :style="getStatusStyle(scope.row.checkStatus)" class="status-mark"/>
              <span>{{ getStatusName(scope.row.checkStatus) }}</span>
            </template>
            <template v-else>
              {{ fieldFormatter(scope.row, scope.column) }}
            </template>
          </template>
        </el-table-column>
        <el-table-column/>
        <el-table-column
          :resizable="false"
          fixed="right"
          width="40">
          <template
            slot="header"
            slot-scope="slot">
            <field-set
              :crm-type="crmType"
              @change="setSave"/>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
    <!-- 相关详情页面 -->
    <c-r-m-all-detail
      :visible.sync="showDview"
      :crm-type="rowType"
      :id="rowID"
      class="d-view"
      @handle="handleHandle"/>

    <!-- 新建 -->
    <expense-create
      v-if="createShow"
      @close="createShow = false"
      @save-success="handleHandle"
    />

    <!-- 报销 -->
    <expense-examine
      v-if="createExamineShow"
      @close="createExamineShow = false"
      @save-success="handleHandle"
    />

  </div>
</template>

<script>
import ExpenseCreate from './Create'
import ExpenseExamine from './Examine'

import CRMAllDetail from '@/views/crm/components/CRMAllDetail'

import TableMixin from '../mixins/Table'
import { floatAdd } from '@/utils'

export default {
  /** 客户管理 的 费用列表 */
  name: 'ExpenseIndex',
  components: {
    ExpenseCreate,
    ExpenseExamine,
    CRMAllDetail
  },
  mixins: [TableMixin],
  data() {
    return {
      crmType: 'expense',
      createShow: false,//新建窗口
      createExamineShow: false,//报销窗口
      moneyData: null // 列表金额
    }
  },
  computed: {
    moneyPageData() {
      if (!this.moneyData || JSON.stringify(this.moneyData) == '{}') {
        return null
      }

      if (this.selectionList.length == 0) {
        return this.moneyData || {}
      } else {
        let money = 0.0
        for (let index = 0; index < this.selectionList.length; index++) {
          const element = this.selectionList[index]
          // 1 审核通过的
          if (element.checkStatus == 1) {
            money = floatAdd(money, parseFloat(element.money))
          }
        }
        return {
          receivablesMoney: money.toFixed(2)
        }
      }
    }
  },
  mounted() {},
  methods: {
    /**
     * 通过回调控制class
     */
    cellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'expenseNum') {
        return 'can-visit--underline can-visit--bold'
      } else if (
        column.property === 'customerName' ||
        column.property === 'contractNum' ||
        column.property === 'businessName'
      ) {
        return 'can-visit--underline'
      } else {
        return ''
      }
    },

    headerCellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'expenseNum') {
        return 'header-can-visit-backgroud'
      }
      return ''
    },

    /**
     * 新建点击
     */
    createClick() {
      this.createShow = true
    },
    createExamine() {
      this.createExamineShow = true;
    }

  }
}
</script>

<style lang="scss" scoped>
@import '../styles/table.scss';
</style>
