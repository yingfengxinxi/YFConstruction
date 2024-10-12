<template>
  <div class="main">
    <base-tabs-head
      :tabs="tabs"
      :select-value.sync="tabsSelectValue"
      @change="tabsChange" style="border-top: 0px"/>

    <div
      v-infinite-scroll="getList"
      :key="`${scrollKey}${tabsSelectValue}`"
      infinite-scroll-distance="100"
      infinite-scroll-disabled="scrollDisabled"
      class="cell-section">
      <examine-cell
        v-for="(item, index) in list"
        :key="index"
        :data="item"
        :index="index"
        @handle="cellHandle"/>
      <p
        v-if="loading"
        class="scroll-bottom-tips">加载中...</p>
      <p
        v-if="noMore"
        class="scroll-bottom-tips">没有更多了</p>
    </div>

    <!-- 查看详情 -->
    <component
      :is="componentName"
      v-if="showDetail"
      :action="{type:'detail'}"
      :id="rowID"
      :businessType="businessType"
      :class="componentClass"
      @close="showDetail=false"
    />

    <!-- 审批操作 -->
    <examine-handle
      :show="showExamineHandle"
      :id="rowID"
      :record-id="rowData.examineRecordId"
      :detail="rowData"
      :status="examineStatus"
      @close="showExamineHandle = false"
      @save="examineHandleCallBack"/>
  </div>
</template>

<script>
  import {
    examineMyExamineAPI
  } from '@/components/common/examine/api'
  import BaseTabsHead from '@/components/BaseTabsHead'
  import ExamineCell from '@/components/common/examine/components/ExamineCell'
  import ExamineHandle from '@/components/common/examine/components/ExamineHandle'

  import Constent from '@/components/common/constent'

  export default {
    // 通用审批
    name: 'ExamineList',
    components: {
      BaseTabsHead,
      ExamineCell,
      ExamineHandle
    },
    mixins: [Constent],
    props: {},
    data() {
      return {
        // 待我审批的
        tabsSelectValue: '1',
        tabs: [
          {
            label: '全部',
            name: 'all'
          },
          {
            label: '待我审批的',
            name: '1'
          },
          {
            label: '我已审批的',
            name: '2'
          }
        ],
        list: [],
        loading: false,
        noMore: false,
        scrollKey: Date.now(),
        page: 1,

        // 控制详情展示
        detailIndex: 0,

        // 相关详情的查看
        rowID: '',
        rowData: {}, // 行全部信息
        showDetail: false,
        componentName: null,
        businessType: -1,
        componentClass:[],
        // 撤回操作
        showExamineHandle: false,
        recordId: '',
        // 审核操作 1 审核通过 2 审核拒绝 4 已撤回
        examineStatus: 1
      }
    },
    computed: {
      // 无线滚动控制
      scrollDisabled() {
        return this.loading || this.noMore
      }
    },
    watch: {},
    mounted() {
    },

    beforeRouteUpdate(to, from, next) {
      this.tabsSelectValue = '1'

      this.refreshList()
      next()
    },

    beforeDestroy() {
    },
    methods: {
      /**
       * 刷新列表
       */
      refreshList() {
        this.page = 1
        this.list = []
        this.noMore = false
        this.scrollKey = Date.now()
      },

      /**
       * 获取数据列表
       */
      getList() {
        this.loading = true
        const params = {
          page: this.page,
          limit: 15,
          status: this.tabsSelectValue == 'all' ? '' : this.tabsSelectValue
        }

        examineMyExamineAPI(params)
          .then(res => {
            this.loading = false
            const status = this.tabsSelectValue == 'all' ? '' : this.tabsSelectValue
            if (params.status == status) {
              if (!this.noMore) {
                this.list = this.list.concat(res.data.list)
                this.page++
              }
              this.noMore = res.data.lastPage
            } else {
              this.refreshList()
            }
          })
          .catch(() => {
            this.noMore = true
            this.loading = false
          })
      },

      /**
       * 中间tabs改变
       */
      tabsChange(type) {
        this.refreshList()
      },

      /**
       * cell 操作
       */
      cellHandle(type, data, index) {
        this.detailIndex = index
        this.rowID = data.catagoryId
        if (type == 'detail') {
          this.businessType = data.categoryType;
          //动态渲染组件
          var component = this.examineConfig[data.categoryType].component;
          this.componentName = this.loadView(component.url);
          //组件样式
          this.componentClass = [];
          if(component.type == 'slide'){
            this.componentClass.push('d-view');
          }
          this.showDetail = true
        } else if (type == 'reject' || type == 'pass' || type == 'withdraw') {
          // 审核操作 1 审核通过 2 审核拒绝 4 已撤回
          this.examineStatus = {
            pass: 1,
            reject: 2,
            withdraw: 4
          }[type]
          this.rowData = data
          this.showExamineHandle = true
        }
      },
      loadView(view) { // 路由懒加载
        return (resolve) => require([`@/views/${view}`], resolve)
      },
      /**
       * 审批操作回调
       */
      examineHandleCallBack(data) {
        // 1 通过 2 拒绝 4 撤回
        this.refreshDataByHandle()
      },

      /**
       * 根据请求刷新数据
       */
      refreshDataByHandle() {
        this.refreshList()
      },

      detailHandleCallBack(data, index) {
        this.detailIndex = index
        this.refreshDataByHandle()
      },
    }
  }
</script>

<style lang="scss" scoped>
  .main {
    position: relative;
    height: 100%;
    /*width: 920px;*/
  }

  .base-tabs-head {
    margin-bottom: 15px;
  }

  .cell-section {
    height: calc(100% - 70px);
    overflow: auto;
    padding: 0 15px;
  }

  .d-view {
    position: fixed;
    min-width: 926px;
    width: 75%;
    top: 60px;
    bottom: 0px;
    right: 0px;
  }
</style>
