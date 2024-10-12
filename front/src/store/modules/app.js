import {
  adminSystemIndexAPI
} from '@/api/admin/config'
import {
  crmSettingConfigDataAPI
} from '@/api/admin/crm'
import request from '@/utils/request'
import {configHeaderModelSortAPI, configAllModelSortAPI} from '@/api/config'
import Lockr from 'lockr'

import permission from './permission'
/** 记录 侧边索引 */
const app = {
  state: {
    logo: '',
    name: '',
    lang: localStorage.lang || 'cn',
    sidebar: {
      activeIndex: '', // 目前激活的 行
      collapse: Lockr.get('sideBarCollapse') || false
    },
    navbar: {
      activeIndex: '' // 导航目前是第几个
    },
    applicationId: '',//当前应用ID
    basePath: '',//应用基础路由
    // CRM配置信息
    CRMConfig: {},
    // 图片缓存
    imageCache: {},
    headerModule: [],// 置顶模块
    allModules: [],//所有模块
		pageheader:[],//page头部
		activeclass:'',//page头部动态添加class判断
    pageDataReflushTime: 5000,//页面数据刷新频率/毫秒
    PM10standard:0//PM数值   定义VUEX 在user.js页面登录时调用 getter.js抛出  页面刷新缓存：@/utils/cache.js
  },

  mutations: {
    SET_APPLCATIONID: (state, applicationId) => {
      console.log(applicationId,">>>>>>>>>>>>>>000");
      
      //console.log("SET_APPLCATIONID:" + applicationId)
      state.applicationId = applicationId
    },
    SET_BASEPATH: (state, basePath) => {
      //console.log("SET_BASEPATH:" + basePath)
      state.basePath = basePath
    },
    SET_ACTIVEINDEX: (state, path) => {
      state.sidebar.activeIndex = path
    },
    SET_COLLAPSE: (state, collapse) => {
      state.sidebar.collapse = collapse
      Lockr.set('sideBarCollapse', collapse)
    },
    SET_NAVACTIVEINDEX: (state, path) => {
      //console.log("SET_NAVACTIVEINDEX:" + path)
      state.navbar.activeIndex = path + '';
    },
    SET_APPLOGO: (state, logo) => {
      state.logo = logo
    },
    SET_APPNAME: (state, name) => {
      state.name = name
    },
    SET_LANG: (state, lang) => {
      state.lang = lang
      window.app.$i18n.locale = lang
      localStorage.setItem('lang', lang)
      window.location.reload()
    },
    SET_CRMCONFIG: (state, config) => {
      state.CRMConfig = config
    },
    SET_IMAGECACHE: (state, value) => {
      state.imageCache = value
    },
    SET_ALLMODULES: (state, value) => {
      state.allModules = value
    },
    SET_PAGE_DATA_REFLUSH_TIME: (state, config) => {
      state.pageDataReflushTime = config
    },
    SET_PM10_STANDARD: (state, config) => {
      state.PM10standard = config
    },
  },

  actions: {
    // 登录
    SystemLogoAndName({
                        commit
                      }) {
      return new Promise((resolve, reject) => {
        adminSystemIndexAPI().then(response => {
          commit('SET_APPNAME', response.data.companyName)
          commit('SET_APPLOGO', response.data.companyLogo)
          Lockr.set('systemLogo', response.data.companyLogo)
          Lockr.set('systemName', response.data.companyName)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    /**
     * 获取客户管理配置
     */
    CRMSettingConfig({
                       commit
                     }) {
      return new Promise((resolve, reject) => {
        crmSettingConfigDataAPI().then(response => {
          commit('SET_CRMCONFIG', response.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    /**
     * 获取配置：页面数据刷新频率/毫秒
     */
    pageDataReflushTime({
                          commit
                        }) {
      return new Promise((resolve, reject) => {
        request({
          url: `adminConfig/queryFirstConfigByName`,
          method: 'post',
          data: {name: 'pageDataReflushTime'}
        }).then(response => {
          // console.log("response.data.value=="+response.data.value);
          commit('SET_PAGE_DATA_REFLUSH_TIME', response.data.value)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    /**
     * 获取配置：PM10标准值
     */
    PM10standard({
                          commit
                        }) {
      return new Promise((resolve, reject) => {
        request({
          url: `adminConfig/queryFirstConfigByName`,
          method: 'post',
          data: {name: 'PM10standard'}
        }).then(response => {
          // console.log("response.data.value=="+response.data.value);
          console.log(response)
          commit('SET_PM10_STANDARD', response.data.value)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 置顶应用
    WKHeaderModule({
                     commit,
                     state
                   }) {
      return new Promise((resolve, reject) => {
        configHeaderModelSortAPI().then(response => {
          state.headerModule = response.data || []
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 全部应用
    WKAllModule({
                  commit,
                  state
                }) {
      return new Promise((resolve, reject) => {
        //2020-12-01 wudw 从菜单进行解析
        var routers = permission.state.routers;
        var allModules = [];
        for(var key in routers){
          var one = {};
          one.icon =  routers[key].meta.icon;
          one.menuId =  parseInt(key);
          one.menuName =  routers[key].meta.title;
          one.path =  routers[key].path;
          one.sort =  routers[key].sort;
          allModules.push(one);
        }
        //console.log("==allModules=="+JSON.stringify(allModules))
        allModules.sort((a,b)=>{ return a.sort-b.sort})//升序
        //console.log("==allModules=="+JSON.stringify(allModules))
        state.allModules = allModules || []
        resolve(allModules)
        /*configAllModelSortAPI().then(response => {
          state.allModules = response.data || []
          resolve(response)
        }).catch(error => {
          reject(error)
        })*/
      })
    },
  }
}

export default app
