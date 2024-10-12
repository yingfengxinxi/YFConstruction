<template>
  <div
    v-show="visible"
    class="full-container">
    <slide-view
      v-if="showDetail"
      :show-close="false"
      :no-listener-class="['bell-message-hook']"
      class="d-view"
      @close="hiddenView">
      <div class="sm-main">
        <div class="sm-main__hd">
          <span class="title">{{ title }}</span>
        </div>
        <div
          class="sm-main__bd">
          <div
            v-infinite-scroll="getList"
            :key="scrollKey"
            infinite-scroll-distance="20"
            infinite-scroll-disabled="scrollDisabled">
            <message-cell
              v-for="(item, index) in list"
              :key="index"
              :data="item"
              :data-index="index"
              @detail="checkDetail"
              @read="readMessageClick"
              @delete="deleteMessageClick"/>
          </div>
          <p
            v-if="loading"
            class="scroll-bottom-tips" @click="clickMore()">点击加载更多</p>
          <p
            v-if="noMore"
            class="scroll-bottom-tips">没有更多了</p>
        </div>

        <flexbox class="sm-main__ft">
          <div class="switch-read">
            <el-switch
              v-model="isUnRead"
              @change="refreshList"/>
            <span class="switch-read__title">仅显示未读消息</span>
          </div>
          <el-dropdown
            trigger="click"
            @command="handleCommand">
            <i
              class="el-icon-more more"/>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                icon="el-icon-check"
                command="read">{{ `全部标记为已读` }}
              </el-dropdown-item>
              <el-dropdown-item
                icon="wk wk-s-delete"
                command="delete">{{ `删除已读消息` }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </flexbox>
      </div>
    </slide-view>

    <!-- 查看详情 -->
    <!--<component
      :is="componentName"
      v-if="showComDetail"
      :action="action"
      :id="rowID"
      :businessType="businessType"
      class="detail-view"
      @close="showComDetail=false"
    />-->
    <!-- 自定义路由 -->
    <el-dialog title="详情" :visible.sync="showComDetail" width="800px" append-to-body
               :close-on-click-modal="true">
      <component
        :is="componentName"
        :data="componentData"
        :id="rowID"
        :businessType="businessType"
      />
    </el-dialog>
  </div>
</template>

<script>
  import {
    getMessageList,
    readMessage,
    delOne,
    delReadMessage
  } from '../api'
  import {
    crmDownImportErrorAPI
  } from '@/api/crm/common'
  import SlideView from '@/components/SlideView'
  import MessageCell from './MessageCell'

  import {getMaxIndex} from '@/utils/index'

  export default {
    // 导航头部系统消息
    name: 'Message',
    components: {
      SlideView,
      MessageCell,
    },
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      unreadNums: Number,
    },
    data() {
      return {
        showDetail: false,

        // 公告
        announcementAddShow: false,

        // 列表
        list: [],
        loading: false,
        noMore: false,
        scrollKey: Date.now(),
        isUnRead: false, // 仅展示未读

        // CRM详情
        showComDetail: false, // 查看相关客户管理详情
        rowID: '',
        businessType: null,
        componentName: '',
        componentClass: [],
        page: 1,
        limit: 20,
        action: {type: 'detail'},
        componentData:{},
      }
    },
    computed: {
      title() {
        return this.unreadNums > 0 ? `消息（${this.unreadNums}）` : '消息'
      },

      scrollDisabled() {
        return this.loading || this.noMore
      },

      contentHeight() {
        return 'calc(100% - 155px)'
      },
    },
    watch: {
      visible: {
        handler(val) {
          this.showDetail = val
          if (val) {
            this.$el.style.zIndex = getMaxIndex()
            this.refreshList()
          }
        },
        immediate: true
      },
      showDetail(val) {
        if (!val) {
          setTimeout(() => {
            this.$emit('update:visible', false)
          }, 350)
        }
      }
    },
    mounted() {
      // console.log("=this.$el"+JSON.stringify(this.$el))
      document.body.appendChild(this.$el)
      this.$el.addEventListener('click', this.handleDocumentClick, false)
    },

    beforeDestroy() {
      // remove DOM node after destroy
      if (this.$el && this.$el.parentNode) {
        this.$el.parentNode.removeChild(this.$el)
        this.$el.removeEventListener('click', this.handleDocumentClick, false)
      }
    },
    methods: {
      //点击加载更多
      clickMore() {
        this.page += 1
        this.getList()
      },

      /**
       * 刷新列表
       */
      refreshList() {
        this.page = 1
        this.list = []
        this.noMore = true
        this.scrollKey = Date.now()
        this.getList();
      },

      /**
       * 获取列表
       */
      getList() {
        let params = {
          page: this.page,
          limit: this.limit
        }
        this.loading = true
        if (this.isUnRead) {
          params.ifRead = '0';
        }
        getMessageList(params)
          .then(res => {
            this.list = this.list.concat(res.data.list)
            if (res.data.list.length < this.limit) {
              this.loading = false
              this.noMore = true;
            } else {
              this.page += 1
              this.noMore = false;
              this.loading = false
            }
          })
          .catch(() => {
            this.loading = false
          })
      },

      /**
       * 查看详情
       */
      checkDetail(data) {
        if(data.clickOpen == '1'){
          this.componentData = data;
          this.rowID = data.data;//存放的需要传递的data
          this.businessType = data.businessType;
          this.componentName = this.loadView(data.url);
          this.showComDetail = true;
        }
      },
      loadView(view) { // 路由懒加载
        return (resolve) => require([`@/views/${view}`], resolve)
      },
      /**
       * 读取消息
       */
      readMessageClick(id, index) {
        var param = {};
        if (id) {
          param.id = id;
        }
        readMessage(param)
          .then(res => {
            this.list[index].ifRead = 1
            this.$emit('update-count')
          })
          .catch(() => {
          })
      },

      deleteMessageClick(id, index) {
        this.$confirm('确定删除这条消息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            delOne(id)
              .then(res => {
                this.list.splice(index, 1)
                this.$emit('update-count')
                this.$message.success('操作成功')
              })
              .catch(() => {
              })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      },

      handleCommand(action) {
        if (action === 'delete') {
          this.allDeleteClick()
        } else {
          this.allMarkDoneClick()
        }
      },

      /**
       * 全部标记完成
       */
      allMarkDoneClick() {
        readMessage({
        })
          .then(res => {
            this.list.forEach(item => {
              item.ifRead = 1
            })
            this.$emit('update-count')
          })
          .catch(() => {
          })
      },

      /**
       * 全部删除
       */
      allDeleteClick() {
        this.$confirm(`确定删除全部已读消息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            delReadMessage(this.type)
              .then(res => {
                this.refreshList()
                this.$emit('update-count')
                this.$message.success('操作成功')
              })
              .catch(() => {
              })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      },

      hiddenView() {
        this.showDetail = false
      },

      handleDocumentClick(e) {
        e.stopPropagation()
        if (this.$el == e.target) {
          this.showDetail = false
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .full-container {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    margin: 0;
    background-color: rgba(0, 0, 0, 0.3);
  }

  .d-view {
    position: fixed;
    width: 530px;
    top: 0px;
    bottom: 0px;
    right: 0px;
    background-color: white;
    /deep/ .el-card__body {
      height: 100%;
    }
  }

  .sm-main {
    height: 100%;
    position: relative;

    &__hd {
      padding: 0 20px;
      height: 50px;
      line-height: 50px;
      font-size: 14px;
      color: #333;
      background-color: #f7f8fa;

      .title {
        font-weight: 600;
      }

      .notice-btn {
        float: right;
        margin-top: 10px;
      }
    }

    &__bd {
      overflow-y: auto;
      height: calc(100% - 50px);
    }

    &__ft {
      position: absolute;
      left: 0;
      right: 0;
      bottom: -1px;
      height: 50px;
      background-color: #f7f8fa;
      padding: 0 15px;

      .switch-read {
        flex: 1;
        &__title {
          font-size: 13px;
          margin-left: 10px;
          color: #333;
        }
      }

      .more {
        flex-shrink: 0;
        cursor: pointer;
      }

      .more:hover {
        color: $xr-color-primary;
      }
    }
  }

  // 菜单
  .menu {
    overflow-x: auto;
    overflow-y: hidden;
    padding-right: 15px;
    margin-bottom: 15px;
    // line-height: 60px;
    border-top: 1px solid $xr-border-color-base;
    // border-bottom: 1px solid $xr-border-color-base;

    .el-button {
      color: #666;
      margin-left: 15px;
      background-color: $xr--background-color-base;
      border-color: $xr--background-color-base;
      font-size: 12px;
      height: 28px;
      border-radius: 14px;
      /deep/ i {
        font-size: 12px;
        margin-right: 5px;
      }
    }

    .el-button--primary.is-current,
    .el-button--primary:hover {
      background: $xr-color-primary;
      border-color: $xr-color-primary;
      color: #ffffff;
    }

    .el-badge {
      margin-top: 15px;
    }
  }

  .scroll-bottom-tips {
    margin: 15px 0 65px;
  }

  .el-badge {
    /deep/ .el-badge__content.is-fixed {
      z-index: 2;
    }
  }

  .detail-view {
    position: fixed;
    width: 55%;
    top: 0px;
    bottom: 0px;
    right: 530px;
  }
</style>
