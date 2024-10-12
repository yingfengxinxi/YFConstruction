<template>
  <div class="navbar">
    <img
      v-src="logo"
      :onerror="`this.src=&quot;${require('@/assets/noimgsrc.png')}&quot;`"
      :key="logo"
      class="logo"
      title="首页"
    >
    <div class="nav-items-container">
      <!--      2021-04-27：系统菜单移至左侧-->
      <!--      <el-menu-->
      <!--        :default-active="navActiveIndex"-->
      <!--        mode="horizontal"-->
      <!--        active-text-color="#2362FB"-->
      <!--        @select="navItemsClick">-->
      <!--        <el-menu-item-->
      <!--          v-for="(item, index) in headerModule"-->
      <!--          :key="index"-->
      <!--          :index="getIndex(item.menuId)">-->
      <!--          <i :style="{ fontSize: '17px' }" :class="item.icon"/>-->
      <!--          <span>{{ item.menuName }}</span>-->
      <!--        </el-menu-item>-->
      <!--        &lt;!&ndash; 置顶管理 &ndash;&gt;-->
      <!--        <el-menu-item-->
      <!--          ref="navManagerMenu"-->
      <!--          index="other">-->
      <!--          <i class="wk wk-grid"/>-->
      <!--        </el-menu-item>-->
      <!--      </el-menu>-->
    </div>
    <el-dropdown
      v-if="showProList"
      size="medium"
      split-button
      @command="chengePeoject"
    >
      <span class="el-dropdown-link">
        {{ userProName }}
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          v-for="(item, index) in userProListData"
          :key="index"
          :command="item.projectId"
          :disabled="item.projectId == userInfo.projectId"
        >
          {{ item.projectName }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <!-- 未读公告提示 -->
    <!--<el-badge
      :value="unreadNoticeNums"
      :hidden="!unreadNoticeNums || unreadNoticeNums == 0"
      :max="99"
      class="bell-message-hook">
      <i
        class="wk wk-announcement"
        @click="sysNoticeShow = true"/>
    </el-badge>-->
    <!-- 未读消息提示 -->
    <el-badge
      :value="unreadMessageNums"
      :hidden="!unreadMessageNums || unreadMessageNums == 0"
      :max="99"
      class="bell-message-hook"
    >
      <i class="wk wk-bell" @click="sysMessageShow = true" />
    </el-badge>
    <!-- 公告列表 -->
    <!--<system-message
      ref="noticeMessage"
      :visible.sync="sysNoticeShow"
      :is-notice="true"
      :type="messageTypeNotice"
      :unread-nums="unreadNoticeNums"
      @update-count="getNoticeUnreadNum"/>-->
    <!-- 消息列表 -->
    <system-message
      ref="msgMessage"
      :visible.sync="sysMessageShow"
      :unread-nums="unreadMessageNums"
      @update-count="getMessageUnreadNum"
    />
    <!-- 个人相关操作 -->
    <el-dropdown
      :hide-on-click="false"
      trigger="click"
      @command="moreMenuClick"
    >
      <div class="user-container">
        <template v-if="userInfo && Object.keys(userInfo).length > 0">
          <xr-avatar
            :name="userInfo.realname"
            :size="32"
            :src="userInfo.img"
            class="user-img"
          />
        </template>
        <i class="el-icon-caret-bottom mark" />
      </div>
      <el-dropdown-menu slot="dropdown" class="el-dropdown-unarrow">
        <el-dropdown-item
          v-for="(item, index) in moreMenu"
          v-if="!(item.command == 'change' && tenantList.length <= 1)"
          :key="index"
          :command="item.command"
          :divided="item.divided"
          :icon="item.icon"
          :disabled="item.disabled"
        >
          <el-dropdown
            v-if="item.command == 'change'"
            trigger="click"
            @command="changeClick"
          >
            <div>
              {{ item.label }}<i class="el-icon-arrow-down el-icon--right" />
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="(itemT, indexT) in tenantList"
                :key="indexT"
                :command="itemT.tenantId"
              >{{ itemT.deptName }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span v-else>{{ item.label }}</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <nav-manager
      v-if="navManagerShow"
      ref="navManager"
      :collapse="collapse"
      :top-module="headerModule"
      @change="getHeaderModule"
      @select="navManagerSelect"
    />

    <!-- 查看详情 -->
    <component
      v-if="showComDetail"
      :is="componentName"
      :action="action"
      :id="rowID"
      :business-type="businessType"
      :class="componentClass"
      @close="showComDetail = false"
    />
  </div>
</template>

<script>
import {
  findTenantListByUsernameAPI,
  changeTenantAPI,
  userProListAPI,
  changeProjectAPI,
  reLoginAPI
} from '@/api/common'
import { getMessageUnreadCount } from '@/components/common/message/api'

import SystemMessage from '@/components/common/message/component/Message'
import Constent from '@/components/common/constent'
import NavManager from './NavManager'

import { mapGetters } from 'vuex'
import { Loading } from 'element-ui'
import { on, off } from '@/utils/dom'

export default {
  components: {
    SystemMessage,
    NavManager
  },
  mixins: [Constent],
  props: {
    navIndex: {
      type: [Number, String],
      default: 0
      // authRedirect: ''
    }
  },
  data() {
    return {
      sss: 555,
      unreadNoticeNums: 0,
      unreadMessageNums: 0,
      sysMessageShow: false,
      sysNoticeShow: false,
      type: 0,
      // items: [],
      showItems: [],
      hiddenItems: [],
      navManagerShow: false,
      isNewest: false,
      lastNavId: '',
      tenantList: [{}],
      //
      messageTypeNotice: 'SYS_XTGG',
      // 项目
      showProList: false,
      userProName: '',
      userProListData: [],

      // CRM详情
      showComDetail: false, // 查看相关客户管理详情
      rowID: '',
      businessType: null,
      componentName: '',
      componentClass: [],
      action: { type: 'detail' }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo',
      'lang',
      'logo',
      'crm',
      'bi',
      'manage',
      'oa',
      'project',
      'collapse',
      'headerModule',
      'allModules',
      'navActiveIndex',
      'applicationId'
    ]),
    moreMenu() {
      return [
        {
          command: 'baseInfo',
          divided: false,
          label: '基本信息',
          icon: 'wk wk-user'
        },
        {
          command: 'change',
          divided: true,
          label: '切换组织',
          icon: 'wk wk-go-out'
        },
        {
          command: 'logOut',
          divided: false,
          label: '退出登录',
          icon: 'wk wk-logout'
        }
      ]
    }
  },
  watch: {},
  created() {
    this.getHeaderModule()
    // 后缓存所有应用信息
    this.getAllModule()
    // 通用消息刷新
    window.app.$on('MSG_REFRESH', (data) => {
      this.getMessageUnreadNum()
      if (this.sysMessageShow) {
        this.$refs.msgMessage.refreshList()
      }
    })
    // 需弹窗
    window.app.$on('ALARM_EQU_ENVRMT', (data) => {
      this.popupNotification(data, 'ALARM_EQU_ENVRMT')
    })
    window.app.$on('EQUI_OFFLINE_TIMEOUT', (data) => {
      this.popupNotification(data, 'EQUI_OFFLINE_TIMEOUT')
    })
    // 设备上线提醒
    // window.app.$on('MONITOR_EQUI_ONLINE', (data) => {
    //   this.popupNotification2(data, 'MONITOR_EQUI_ONLINE')
    // })
    // 设备离线提醒
    // window.app.$on('MONITOR_EQUI_OFFLINE', (data) => {
    //   this.popupNotification2(data, 'MONITOR_EQUI_OFFLINE')
    // })
  },
  inject: ['reload'], // 使用 inject 注入 reload 变量
  mounted() {
    on(document, 'click', this.handleDocumentClick)
    /* window.onresize = () => {
        this.changeMenu(document.documentElement.clientWidth)
      }*/
    // 公告
    // this.getNoticeUnreadNum();

    // 消息
    // this.getMessageUnreadNum();

    this.userProList()
  },
  beforeDestroy() {
    this.$bus.off('document-visibility')
    off(document, 'click', this.handleDocumentClick)
  },
  methods: {
    // 项目列表
    userProList() {
      var that = this
      userProListAPI().then((res) => {
        that.userProListData = res.data
        that.userProListData.forEach((item) => {
          if (item.projectId == that.userInfo.projectId) {
            that.userProName = item.projectName
          }
        })
        //
        if (this.userProListData.length > 0) {
          this.showProList = true
        }
      })
    },
    // 切换项目
    chengePeoject(pid) {
      var that = this
      changeProjectAPI(pid).then((res) => {
        if (res.code == 0) {
          that.$message.success('锁定项目成功')
          reLoginAPI().then((res) => {
            this.$store.commit('SET_AUTH', res.data)
            that.$router.go(0)
          })
        } else {
          that.$message.error(res.msg)
        }
      })
    },
    getIndex(menuId) {
      return menuId + ''
    },
    /**
     * 获取置顶头
     */
    getHeaderModule() {
      if (!this.headerModule.length) {
        this.$store
          .dispatch('WKHeaderModule')
          .then((res) => {
            // console.log("headers:", res)
          })
          .catch(() => {})
      } else {
      }
    },
    /**
     * 获取全部应用
     */
    getAllModule() {
      if (!this.allModules.length) {
        this.$store
          .dispatch('WKAllModule')
          .then((res) => {
            // console.log("all:", res)
          })
          .catch(() => {})
      }
    },

    navItemsClick(menuId) {
      var item = this.getAuthItem(menuId)
      if (menuId != 'other') {
        // 点击的不不是右侧扩展
        this.navManagerShow = false
        if (this.applicationId != menuId) {
          this.$router.push(item.path)
          this.$store.commit('SET_APPLCATIONID', menuId)
          this.$store.commit('SET_BASEPATH', item.path)
          // 强制刷新
          this.reload()
        }
        this.$store.commit('SET_NAVACTIVEINDEX', menuId)
      } else {
        this.navManagerShow = !this.navManagerShow
        if (this.navManagerShow) {
          this.lastNavId = this.navActiveIndex
          this.$store.commit('SET_NAVACTIVEINDEX', menuId)
        } else {
          if (!this.lastNavId) {
            this.lastNavId = '-1'
          }
          this.$store.commit('SET_NAVACTIVEINDEX', this.lastNavId)
        }
      }
      this.$emit('nav-items-click', menuId)
    },
    getAuthItem(menuId) {
      return this.allModules.find((item) => {
        return item.menuId == menuId
      })
    },

    // 消息角标  =======================
    /**
     * 获取系统未读消息数
     */
    getNoticeUnreadNum() {
      getMessageUnreadCount(this.messageTypeNotice)
        .then((res) => {
          this.unreadNoticeNums = res.data || 0
        })
        .catch(() => {})
    },
    getMessageUnreadNum() {
      getMessageUnreadCount()
        .then((res) => {
          this.unreadMessageNums = res.data || 0
        })
        .catch(() => {})
    },
    // 消息角标  =======================

    /**
     * 进入首页
     */
    enterCustoemBoard() {
      this.$store.commit('SET_NAVACTIVEINDEX', '-1')
      this.$router.push('/logo')

      this.$store.commit('SET_APPLCATIONID', '-1')
      this.$store.commit('SET_BASEPATH', '')
    },

    /**
     * 控制导航管理隐藏
     */
    handleDocumentClick(e) {
      if (
        !this.$refs.navManagerMenu ||
        !this.$refs.navManager ||
        this.$refs.navManagerMenu.$el.contains(e.target) ||
        this.$refs.navManager.$el.contains(e.target)
      ) {
        return
      }
      this.navManagerShow = false
      if (!this.lastNavId) {
        this.lastNavId = '-1'
      }
      this.$store.commit('SET_NAVACTIVEINDEX', this.lastNavId)
    },
    changeClick(command) {
      // console.log("tenantId=="+JSON.stringify(this.userInfo))
      // if(this.userInfo.tenantId == command){
      //   this.$message.success("当前登录已是该部门")
      //   return;
      // }
      changeTenantAPI(command).then((res) => {
        if (res.code == 0) {
          // this.$message.success("切换成功")
          this.$store.commit('SET_AUTH', res.data)
          this.$store.dispatch('GetUserInfo')
          this.$store.dispatch('SystemLogoAndName')
          this.$router.go(0)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    moreMenuClick(command) {
      if (command == 'baseInfo') {
        this.$router.push({
          name: 'person'
        })
      } else if (command == 'change') {
        if (this.tenantList.length == 0) {
          findTenantListByUsernameAPI(this.userInfo.username).then((res) => {
            this.tenantList = res.data
          })
        }
      } else if (command == 'logOut') {
        this.$confirm('退出登录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            var loading = Loading.service({
              target: document.getElementById('#app')
            })
            this.$store
              .dispatch('LogOut')
              .then(() => {
                location.reload()
                loading.close()
              })
              .catch(() => {
                location.reload()
                loading.close()
              })
          })
          .catch(() => {})
      }
      // 重置状态
      this.$store.commit('SET_NAVACTIVEINDEX', '-1')
      this.$store.commit('SET_APPLCATIONID', '-1')
      this.$store.commit('SET_BASEPATH', '')
    },
    navManagerSelect({ menuId, path }) {
      this.navManagerShow = false

      if (this.applicationId != menuId) {
        this.$router.push(path)
        this.$store.commit('SET_APPLCATIONID', menuId + '')
        this.$store.commit('SET_BASEPATH', path)
      }
      this.reload()
      let navActiveIndex = menuId + ''
      var moduleArr = this.topModules()
      // console.log(menuId, moduleArr)
      if (!moduleArr.includes(menuId)) {
        navActiveIndex = 'other'
      }
      this.$store.commit('SET_NAVACTIVEINDEX', navActiveIndex)
    },
    topModules() {
      return this.headerModule.map((item) => item.menuId)
    },
    checkIsNewest(a, b) {
      var a = toNum(a)
      var b = toNum(b)

      if (a > b) {
        return false
      } else {
        return true
      }

      function toNum(a) {
        var a = a.toString()
        var c = a.split('.')
        var num_place = ['', '0', '00', '000', '0000']
        var r = num_place.reverse()
        for (var i = 0; i < c.length; i++) {
          var len = c[i].length
          c[i] = r[len] + c[i]
        }
        var res = c.join('')
        return res
      }
    },
    // 消息通知
    popupNotification(data, messageType) {
      var jsonObj = JSON.parse(data)
      const msgContent = jsonObj.msgContent
      console.log('通知')
      const _this = this
      this.$notify({
        title: '警告',
        dangerouslyUseHTMLString: true,
        message: '<span>' + msgContent + '</span>',
        duration: 0,
        type: 'warning',
        onClick() {
          _this.notificationClick(jsonObj, messageType) // 自定义回调,jsonObj为传的参数
        }
      })
    },
    popupNotification2(data, messageType) {
      var jsonObj = JSON.parse(data)
      let msgContent = ''
      let title = ''
      let type = ''
      if (jsonObj.type == 'MONITOR_EQUI_ONLINE') {
        title = '提醒'
        msgContent = jsonObj.title
        type = 'success'
      } else if (jsonObj.type == 'MONITOR_EQUI_OFFLINE') {
        title = '警告'
        msgContent = jsonObj.title
        type = 'warning'
      } else {
        title = '警告'
        msgContent = jsonObj.msgContent
        type = 'warning'
      }

      const _this = this
      this.$notify({
        title: title,
        dangerouslyUseHTMLString: true,
        message: '<span>' + msgContent + '</span>',
        duration: 0,
        type: type
        // onClick() {
        //   _this.notificationClick(jsonObj, messageType); //自定义回调,jsonObj为传的参数
        // }
      })
    },
    // 通知点击事件
    notificationClick(jsonObj, messageType) {
      var com = this.messageConfig[messageType].component
      this.action.type = 'save'
      this.componentName = this.loadView(com.url)
      this.rowID = jsonObj.data
      this.componentClass.push('detail-view')
      this.showComDetail = true
    },
    loadView(view) {
      // 路由懒加载
      return (resolve) => require([`@/views/${view}`], resolve)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 60px;
  min-height: 60px;
  // background-color: white;
  background-image: linear-gradient(90deg, #1d42ab, #2173dc, #1e93ff);
  display: flex;
  align-items: center;
  position: relative;
  padding: 0 30px;
  .logo {
    /*width: 150px;//取消logo宽度设置*/
    /* width: 250px;*/
    /*height: 38px;*/
    display: block;
    flex-shrink: 0;
    margin-right: 15px;
    // background-color: white;
    cursor: pointer;
    max-width: 250px;
    max-height: 50px;
  }
  .nav-items-container {
    flex: 1;
    display: flex;
    min-width: 500px;
    height: 100%;
    overflow-x: auto;
    line-height: 60px;
    font-size: 15px;
  }

  .user-container {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    cursor: pointer;
    .user-img {
      margin-right: 8px;
    }
    .mark {
      font-size: 15px;
      color: #ffffffb3;
    }
  }

  .user-container:hover {
    .mark {
      color: rgba($color: #fff, $alpha: 1);
    }
  }
}

// 菜单
.el-menu {
  overflow: hidden;

  /deep/ .el-submenu__icon-arrow {
    display: none;
  }

  /deep/ .el-submenu {
    .el-submenu__title {
      i {
        color: #5c6075;
      }
    }
  }

  /deep/ .el-submenu.is-active {
    .el-submenu__title {
      i {
        color: $xr-color-primary;
      }
    }
  }
}

.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.el-menu-item {
  padding: 0;
  margin: 0 20px;
  font-size: 16px;
  font-weight: 500;
  color: #2a304d;
  i {
    color: #5c6075;
  }
}

.el-menu-item:hover {
  i {
    color: $xr-color-primary;
  }
}

.el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
  color: $xr-color-primary;
}

.el-menu-item.is-active {
  border-width: 3px;
  i {
    color: $xr-color-primary;
  }
}

// 右侧操作
.handel-box {
  padding: 0 15px;
  border-top: 1px solid #ebeef5;
  padding-top: 10px;
  .handel-button {
    width: 100%;
    border-radius: $xr-border-radius-base;
    border-color: #2362fb;
    background-color: #2362fb;
  }
}

.hr-top {
  margin-top: 8px;
  border-top: 1px solid #f4f4f4;
  padding-top: 3px;
}

.nav-lang {
  cursor: pointer;
  color: #888;
  padding: 20px;
  &:hover {
    color: #2362fb;
  }
  &.active {
    font-weight: bold;
    color: #2362fb;
  }
}

// 系统消息
.wk-announcement,
.wk-bell {
  color: #ffffffb3;
  cursor: pointer;
  font-size: 20px;
}

.el-badge {
  margin-right: 30px;
}

.wk-announcement:hover,
.wk-bell:hover {
  color: rgba($color: #fff, $alpha: 1);
}

.el-dropdown-menu {
  /deep/ .popper__arrow {
    display: none;
  }
}

.top-btn {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  color: $xr-color-primary;
  .gift {
    color: $xr-color-primary;
    margin-right: 5px;
  }
}

.wk-update-tips {
  font-size: 12px;
  text-align: center;
  color: #bbb;
  margin-bottom: 10px;

  .el-badge {
    margin-left: 5px;
    margin-right: 0;
  }

  .el-button {
    padding: 2px 5px;
    font-size: 12px;
  }
}

//下面切换
.el-dropdown {
  margin-right: 10px;
  /deep/ .el-button {
    height: 36px;
  }
}

.click-content {
  color: $xr-color-primary;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

.detail-view {
  position: fixed;
  min-width: 926px;
  width: 75%;
  top: 0px;
  bottom: 0px;
  right: 0px;
}
</style>

