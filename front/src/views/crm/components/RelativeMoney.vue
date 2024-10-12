<template>
  <div
    v-empty="nopermission"
    class="rc-cont"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限">
    <flexbox
      v-if="!isSeas"
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
      :cell-class-name="cellClassName"
      stripe
      style="width: 100%;border: 1px solid #E6E6E6;"
      @row-click="handleRowClick"
      @selection-change="selectionList = $event">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :formatter="fieldFormatter"
        show-overflow-tooltip/>
    </el-table>

    <!-- 创建 -->
    <examine-create-view
      v-if="isCreate"
      :category-id="createInfo.categoryId"
      :type="createInfo.type"
      :category-title="createInfo.title"
      :action="createAction"
      @save-success="refreshList"
      @hiden-view="createHideView" />

   <!-- <c-r-m-full-screen-detail
      :visible.sync="showFullDetail"
      :id="showFullId"
      :crm-type="showFullCrmType"
      @handle="detailHandle"/>-->
  </div>
</template>

<script type="text/javascript">
import LoadingMixin from '../mixins/Loading'
import CheckStatusMixin from '@/mixins/CheckStatusMixin'
import CRMAllCreate from './CRMAllCreate'
import { crmCustomerQueryMoneyAPI } from '@/api/crm/customer'
import { crmContractQueryMoneyAPI } from '@/api/crm/contract'
import { crmBusinessQueryMoneyAPI } from '@/api/crm/business'
import CrmRelative from '@/components/CreateCom/CrmRelative'
import { oaSelectExamineCategoryByType } from '@/api/oa/examine'
import ExamineCreateView from '@/views/oa/examine//components/ExamineCreateView'

export default {
  name: 'RelativeMoney', // 相关联系人商机  可能再很多地方展示 放到客户管理目录下（新建时仅和客户进行关联）
  components: {
    CRMFullScreenDetail: () => import('@/components/CRMFullScreenDetail'),
    CRMAllCreate,
    ExamineCreateView,
    CrmRelative
  },
  mixins: [LoadingMixin, CheckStatusMixin],
  props: {
    /** 模块ID */
    id: [String, Number],
    /** 没有值就是全部类型 有值就是当个类型 */
    crmType: {
      type: String,
      default: ''
    },
    /** 是公海 默认是客户 */
    isSeas: {
      type: Boolean,
      default: false
    },
    /** 联系人人下 新建商机 需要联系人里的客户信息  合同下需要客户和商机信息 */
    detail: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      nopermission: false,
      list: [],
      fieldList: [],
      tableHeight: '250px',
      showFullDetail: false,
      showFullCrmType: 'examine',
      // 新建
      showCategorySelect: false,
      isCreate: false, // 是创建
      createAction: { type: 'save' },
      createInfo: {}, // 创建所需要的id 标题名信息
      rowDetail: {},
      showFullId: '', // 查看全屏详情的 ID
      // 创建的相关信息
      createActionInfo: { type: 'relative', crmType: this.crmType, data: {}},
      /**
       * 关联的逻辑
       */
      showRelativeView: false, // 控制关联信息视图
      selectionList: [] // 取消关联勾选的数据
    }
  },
  inject: ['rootTabs'],
  watch: {
    id(val) {
      this.list = []
      this.getDetail()
    }
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    /**
     * 关联的数据

    checkRelativeInfos(data) {
      if (data.data.length > 0) {
        const params = { contactsId: this.id }
        params.workIds = data.data
          .map(item => {
            return item.workId
          })
        crmContactsRelateBusinessAPI(params)
          .then(res => {
            this.getDetail()
            this.$message.success('操作成功')
          })
          .catch(() => {})
      }
    },*/

    /**
     * 取消关联

    unRelevanceHandleClick() {
      if (this.selectionList.length == 0) {
        this.$message.error('请先勾选数据')
      } else {
        this.$confirm('确认取消关联?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const params = { contactsId: this.id }
            params.workIds = this.selectionList
              .map(item => {
                return item.workId
              })
            crmContactsUnrelateBusinessAPI(params)
              .then(res => {
                this.getDetail()
                this.$message.success('操作成功')
              })
              .catch(() => {})
          })
          .catch(() => {})
      }
    },*/

    /**
     * 获取字段信息
     */
    getFieldList() {
      this.fieldList.push({
        prop: 'content',
        width: '200',
        label: '报销事由'
      })
      this.fieldList.push({
        prop: 'money',
        width: '200',
        label: '报销总金额'
      })
      this.fieldList.push({
        prop: 'createTime',
        width: '200',
        label: '创建时间'
      })
      // this.fieldList.push({
      //   prop: 'endTime',
      //   width: '200',
      //   label: '结束时间'
      // })
      this.fieldList.push({
        prop: 'realname', width: '200', label: '创建人'
      })
      this.fieldList.push({
        prop: 'examineStatus',
        width: '200',
        label: '审批状态'
      })
    },

    fieldFormatter(row, column) {
      if (column.property === 'examineStatus') {
        return this.getStatusName(row.examineStatus)
      }
      return row[column.property]
    },

    getDetail(loading = true) {
      this.loading = loading
      const request = {
        customer: crmCustomerQueryMoneyAPI,
        contract: crmContractQueryMoneyAPI,
        business: crmBusinessQueryMoneyAPI
      }[this.crmType]
      const params = { pageType: 0 }
      params[this.crmType + 'Id'] = this.id
      request(params)
        .then(res => {
          if (this.fieldList.length == 0) {
            this.getFieldList()
          }
          this.nopermission = false
          this.loading = false
          this.list = res.data.list
        })
        .catch(data => {
          if (data.code == 102) {
            this.nopermission = true
          }
          this.loading = false
        })
    },

    /**
     * 新建
     */
    createClick() {
      const examineCategoryType = 5
      oaSelectExamineCategoryByType(examineCategoryType)
        .then(res => {
          this.createInfo = res.data
          this.createAction = { type: 'save' }
          this.isCreate = true
        })
    },

    /**
     * 创建关闭
     */
    createHideView() {
      this.isCreate = false
    },

    /**
     * 刷新
     */
    refreshList() {
      this.handleCurrentChange(1)
    },

    /**
     * 当某一行被点击时会触发该事件
     */
    handleRowClick(row, column, event) {
      if (column.property == 'content') {
        this.showFullId = row.examineId
        this.showFullCrmType = 'examine'
        this.showFullDetail = true
      }
    },

    /**
     * 通过回调控制class
     */
    cellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'content') {
        return 'can-visit--underline'
      } else {
        return ''
      }
    },

    /**
     * 详情操作
     */
    detailHandle(data) {
      if (data.type === 'delete') {
        this.getDetail()
        this.$nextTick(() => {
          this.$bus.emit('crm-tab-num-update')
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../styles/relativecrm.scss';
</style>
