const getters = {
  userInfo: state => state.user.userInfo,
  lang: state => state.app.lang,
  app: state => state.app,
  logo: state => {
    if (state.app.logo) {
      return state.app.logo
    }
    //return require('@/assets/img/logo_oner.png')
    return require('@/assets/img/logo_gd.png')
  },
  name: state => {
    if (state.app.name) {
      return state.app.name
    }
    return '欧纳信息'
  },
  collapse: state => state.app.sidebar.collapse,
  activeIndex: state => state.app.sidebar.activeIndex,
  navActiveIndex: state => state.app.navbar.activeIndex,
  pageheader:state => state.app.pageheader,
  applicationId: state => state.app.applicationId,
  basePath: state => state.app.basePath,
  headerModule: state => state.app.headerModule,
  //2020-11-20
  allModules: state => state.app.allModules,
  // 权限
  allAuth: state => state.user.allAuth,
  crm: state => state.user.crm,
  bi: state => state.user.bi,
  manage: state => state.user.manage,
  oa: state => state.user.oa,
  project: state => state.user.project,
 
  
  // 路由
  routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters,
  crmRouters: state => state.permission.crmRouters,
  taskExamineRouters: state => state.permission.taskExamineRouters,
  workLogRouters: state => state.permission.workLogRouters,
  addressBookRouters: state => state.permission.addressBookRouters,
  projectRouters: state => state.permission.projectRouters,
  biRouters: state => state.permission.biRouters,
  manageRouters: state => state.permission.manageRouters,

  // 客户管理信息
  messageNum: state => state.crm.messageNum,
  // 配置信息
  CRMConfig: state => state.app.CRMConfig,
  imageCache: state => state.app.imageCache,
  pageDataReflushTime :state => state.app.pageDataReflushTime,
  PM10standard :state => state.app.PM10standard,

}
/**
 * 使用说明
 * import { mapGetters } from 'vuex'
 * computed: {
    ...mapGetters([
      'userInfo'
    ])
  }
 */

export default getters
