import {
  loginAPI,
  logoutAPI
} from '@/api/login'
import {
  adminUserAuthListAPI
} from '@/api/common'
import {
  resetRouter
} from '@/router'

import {
  adminUsersReadAPI
} from '@/api/user/personCenter'

import {
  addAuth,
  removeAuth
} from '@/utils/auth'
import Lockr from 'lockr'
import CryptoJS from 'crypto-js'

const user = {
  state: {
    userInfo: {}, // 用户信息
    // 权限信息
    allAuth: null, // 总权限信息 默认空 调整动态路由
    crm: {}, // 客户管理
    bi: {}, // 商业智能
    manage: {}, // 管理后台
    oa: {}, // 办公
    project: {} // 项目管理
  },

  mutations: {
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
      //console.log("set_userInfo:",userInfo);
      //todo 判断账号所属端
      var parentDeptId = userInfo.deptParentId;
      var token = Lockr.get('Admin-Token');
      if(parentDeptId == "1" || parentDeptId == "0"){
        localStorage.setItem('Supervise-Token', JSON.stringify({'data':token}))
        localStorage.removeItem("Project-Token");
      }else{
        localStorage.setItem('Project-Token', JSON.stringify({'data':token}))
        localStorage.removeItem("Supervise-Token");
      }
      localStorage.setItem('loginUserInfo', JSON.stringify(userInfo))
    },
    SET_ALLAUTH: (state, allAuth) => {
      state.allAuth = allAuth
    },
    SET_ALLMODEL: (state, allAuth) => {
      const allItemsObj = [];
      for (var i = 0; i < allAuth.length; i++) {
        var one = allAuth[i];
        console.log(one)
        if (one.parentId == 0) {
          allItemsObj.push(one);
        }
      }
      state.allItemsObj = allItemsObj
    },
    SET_CRM: (state, crm) => {
      state.crm = crm
    },
    SET_BI: (state, bi) => {
      state.bi = bi
    },
    SET_MANAGE: (state, manage) => {
      state.manage = manage
    },
    SET_OA: (state, oa) => {
      state.oa = oa
    },
    SET_PROJECT: (state, project) => {
      state.project = project
    },
    SET_AUTH: (state, data) => {
      const token = data.adminToken
      Lockr.set('Admin-Token', token)
      addAuth(token)
    }
  },

  actions: {
    // 登录
    Login({
            commit,
            dispatch
          }, userInfo) {
      return new Promise((resolve, reject) => {
        //用户名+密码传输加密
        var key = CryptoJS.enc.Utf8.parse("qdoner1234567890");
        var username = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(userInfo.username), key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        var password = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(userInfo.password), key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        var userInfoCopy= JSON.parse(JSON.stringify(userInfo))
        userInfoCopy.username = username.toString()
        userInfoCopy.password = password.toString()

        loginAPI(userInfoCopy).then(res => {//TODO 这里返回只是 Admin-Token
          const data = res.data || data
          if (!data.hasOwnProperty('companyList')) {
            commit('SET_AUTH', data)//这里header里放入了admin-token
            dispatch('pageDataReflushTime')
            dispatch('PM10standard')
          }

          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取权限
    getAuth({
              commit,
              dispatch
            }) {
      return new Promise((resolve, reject) => {
        //2020-11-23 wudw 此处没办法从用户信息获取，因为用户信息的初始化为异步，慢
        /*console.log("state:",state);
        console.log("state:",state.bi);
        console.log("用户信息:",state.userInfo);
        const data = state.userInfo.oauth;
        Lockr.set('authList', data)
        data.wkFirstModel = response.firstModel
        commit('SET_ALLAUTH', data)
        commit('SET_CRM', data.crm)
        commit('SET_BI', data.bi)
        commit('SET_MANAGE', data.manage)
        commit('SET_OA', data.oa)
        commit('SET_PROJECT', data.project)

        resolve(data)*/
        //刷新当前用户数据
        dispatch('GetUserInfo')
        dispatch('SystemLogoAndName')
        //2020-11-24 wudw 重写权限获取方法
        adminUserAuthListAPI().then((response) => {//TODO 这里获取了拥有的全部权限
          const data = response.data;
          //console.log("权限：",data);
          Lockr.set('authList', data)
          data.wkFirstModel = response.firstModel
          commit('SET_ALLAUTH', data)
          //TODO 2020-11-25 wudw 权限应由应用ID区分 这里需要对每个模块的获取进行调整   2020-12-07 取消ID,兼容多租户
          commit('SET_CRM', data.crm)
          commit('SET_BI', data.bi)
          commit('SET_MANAGE', data.manage)
          commit('SET_OA', data.oa)
          commit('SET_PROJECT', data.project)

          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({
                  commit,
                  state
                }) {
      return new Promise((resolve, reject) => {
        adminUsersReadAPI().then(response => {//TODO 这里只是用户的基本信息{realname: "admin", username: "13969782210", userId: 14773, sex: 1, mobile: "13969782210", …}createTime: "2020-11-02 14:09:46"deptId: 1deptName: "顶级租户"email: nullemailId: nullimg: "/adminFile/down/1323145718449442816"isAdmin: trueisReadNotice: 1mobile: "13969782210"parentId: 0parentName: ""password: nullpost: "标准岗位"realname: "admin"roleId: nullroleIds: nullroleName: nullsex: 1status: 1userId: 14773username: "13969782210"__ob__: Observer {value: {…}, dep: Dep, vmCount: 0}get createTime: ƒ reactiveGetter()set createTime: ƒ reactiveSetter(newVal)get deptId: ƒ reactiveGetter()set deptId: ƒ reactiveSetter(newVal)get deptName: ƒ reactiveGetter()set deptName: ƒ reactiveSetter(newVal)get email: ƒ reactiveGetter()set email: ƒ reactiveSetter(newVal)get emailId: ƒ reactiveGetter()set emailId: ƒ reactiveSetter(newVal)get img: ƒ reactiveGetter()set img: ƒ reactiveSetter(newVal)get isAdmin: ƒ reactiveGetter()set isAdmin: ƒ reactiveSetter(newVal)get isReadNotice: ƒ reactiveGetter()set isReadNotice: ƒ reactiveSetter(newVal)get mobile: ƒ reactiveGetter()set mobile: ƒ reactiveSetter(newVal)get parentId: ƒ reactiveGetter()set parentId: ƒ reactiveSetter(newVal)get parentName: ƒ reactiveGetter()set parentName: ƒ reactiveSetter(newVal)get password: ƒ reactiveGetter()set password: ƒ reactiveSetter(newVal)get post: ƒ reactiveGetter()set post: ƒ reactiveSetter(newVal)get realname: ƒ reactiveGetter()set realname: ƒ reactiveSetter(newVal)get roleId: ƒ reactiveGetter()set roleId: ƒ reactiveSetter(newVal)get roleIds: ƒ reactiveGetter()set roleIds: ƒ reactiveSetter(newVal)get roleName: ƒ reactiveGetter()set roleName: ƒ reactiveSetter(newVal)get sex: ƒ reactiveGetter()set sex: ƒ reactiveSetter(newVal)get status: ƒ reactiveGetter()set status: ƒ reactiveSetter(newVal)get userId: ƒ reactiveGetter()set userId: ƒ reactiveSetter(newVal)get username: ƒ reactiveGetter()set username: ƒ reactiveSetter(newVal)__proto__: Object

          // // 开启了小程序
          // if (response && response.hasOwnProperty('cardAuth')) {
          //   response.data.cardAuth = response.cardAuth
          // }
          console.log("userinf:",response.data)
          commit('SET_USERINFO', response.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({
             commit
           }) {
      return new Promise((resolve, reject) => {
        logoutAPI().then(() => {
          /** flush 清空localStorage .rm('authKey') 按照key清除 */
          removeAuth()
          resetRouter()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
