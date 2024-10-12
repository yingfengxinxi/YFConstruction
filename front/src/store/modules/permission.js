import {
  asyncRouterMap
} from '@/router'
import Vue from 'vue'
import {adminGroupsTypeListAPI} from '@/api/admin/role'
import {adminRouterListAPI,getDeptPid} from '@/api/common'
/**
 *
 * @param {*} router
 * @param {*} authInfo
 */
function checkAuth(router, authInfo) {
  // 判断当前路由在权限数组中是否存在
  if (router.meta) {
    const metaInfo = router.meta
    if (!metaInfo.requiresAuth) {
      return true
    } else {
      if (metaInfo.permissions) {
        authInfo = {...authInfo}
        return forCheckPermission(metaInfo.permissions, authInfo)
      } else if (metaInfo.permissionList) { // 一个路由受多个权限判断
        for (let index = 0; index < metaInfo.permissionList.length; index++) {
          authInfo = {...authInfo}
          const permission = forCheckPermission(metaInfo.permissionList[index], authInfo)
          if (permission) {
            return true
          }
        }
        return false
      }
    }
  }
  return true
}

/**
 * 循环关键字检查权限
 * @param {*} permissions 权限关键数组
 * @param {*} authInfo
 */
const forCheckPermission = function (permissions, authInfo) {
  for (let index = 0; index < permissions.length; index++) {
    const key = permissions[index]
    authInfo = authInfo[key]
    if (!authInfo) {
      return false
    } else if (permissions.length - 1 === index) {
      return true
    }
  }
}

/**
 *
 * @param {*} routers
 * @param {*} authInfo
 */
const filterAsyncRouter = function (routers, authInfo) {
  const res = []
  routers.forEach(router => {
    const tmp = {
      ...router
    }
    if (checkAuth(tmp, authInfo)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, authInfo)
      }
      res.push(tmp)
    }
  })
  return res
}


/**
 * 忽略用于菜单展示的传参路由
 * @param {*} routers
 * @param {*} authInfo
 */
const filterIgnoreRouter = function (routers) {
  const res = []
  routers.forEach(router => {
    const tmp = {
      ...router
    }
    if (!tmp.ignore) {
      if (tmp.children) {
        tmp.children = filterIgnoreRouter(tmp.children)
      }
      res.push(tmp)
    }
  })
  return res
}

/**
 * 路由重定向和角色路由完善
 */
const perfectRouter = function (authInfo, result) {
  //获取了系统管理中四个角色管理的分组信息 groupData
  getGroupData(authInfo, (groupData) => {
    //console.log("state:",this.state)
    const routerObj = {}
    let addRouter = []
    let redirect = ''
    let topRedirect = '' // 置顶的第一个路由
    //根据 应用 遍历所有路由
    for (let index = 0; index < asyncRouterMap.length; index++) {
      //应用路由
      const mainRouter = asyncRouterMap[index]
      //根据权限对路由进行清洗
      //允许通过的路由
      const accessedRouters = filterAsyncRouter(mainRouter.router, authInfo)
      for (let childIndex = 0; childIndex < accessedRouters.length; childIndex++) {
        const element = accessedRouters[childIndex]

        // 处理系统管理逻辑
        if (mainRouter.type == 'manage' && groupData.requiresAuth) {

          //单独处理 角色权限管理
          const authItem = getAuthItem(accessedRouters)
          //追加四个虚拟路由
          const roleMenus = groupData.list.map(item => {
            return {
              name: 'role-auth',
              path: `role-auth/${item.roleType}/${encodeURI(item.name)}`,
              ignore: true, // 不加入路由 仅菜单展示
              meta: {
                title: item.name
              }
            }
          })

          //追加至真实角色管理下面，形成children模式
          if (roleMenus && roleMenus.length > 0) {
            const roleFirstChild = authItem.children[0]
            roleFirstChild.meta.redirect = roleMenus[0].path
            authItem.children = authItem.children.concat(roleMenus)
          }
        }

        //通用处理追加至上级菜单下面，形成children模式
        if (element.children && element.children.length > 0) {
          const firstChild = element.children[0]
          const childPath = firstChild.meta ? firstChild.meta.redirect || firstChild.path : firstChild.path
          element.redirect = element.path + '/' + childPath
        }

        // 获取跳转
        if (element.redirect) {
          if (!redirect) {
            redirect = element.redirect
          }

          if (authInfo.wkFirstModel && !topRedirect) {
            const modelName = {
              crm: 'crm',
              taskExamine: 'taskExamine',
              log: 'workLog',
              book: 'addressBook',
              project: 'project',
              bi: 'bi',
              calendar: 'calendar'
            }[authInfo.wkFirstModel]
            if (modelName == mainRouter.type) {
              topRedirect = element.redirect
            }
          }

          // 为导航头 获取每个模块的 重定向 url
          accessedRouters.push({
            path: `/${mainRouter.type}`,
            name: mainRouter.type,
            redirect: element.redirect,
            hidden: true
          })

          break
        }
      }
      routerObj[mainRouter.type] = accessedRouters
      addRouter = addRouter.concat(filterIgnoreRouter(accessedRouters))
    }

    if (redirect || topRedirect) {
      addRouter.push({//这里就是赋值第一个加载的页面，/ 跳转
        path: '/',
        redirect: topRedirect || redirect,
        hidden: true
      })
    }
    if (result) {
      result({router: routerObj, addRouter})
    }
  })
}

/**
 *
 */
function getAuthItem(array) {
  return array.find(item => {
    return item.name == 'manage-role-auth'
  })
}

/**
 * 获取角色列表
 * @param {*} authInfo 授权信息
 * @param {*} result 回调
 */
function getGroupData(authInfo, result) {
  if (authInfo && authInfo.manage && authInfo.manage.permission) {
    adminGroupsTypeListAPI().then((response) => {
      if (result) {
        result({
          requiresAuth: true,
          list: response.data || []
        })
      }
    }).catch(() => {
    })
  } else {
    if (result) {
      result({
        requiresAuth: false
      })
    }
  }
}

//socket
import {socketInit} from '@/components/common/message/socketio'

const permission = {
  state: {
    routers:{},
    addRouters: [],
    crmRouters: [],
    taskExamineRouters: [],
    workLogRouters: [],
    addressBookRouters: [],
    projectRouters: [],
    biRouters: [],
    manageRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, data) => {
      //2020-11-23  2020-11-24废除
      /*state.addRouters = data
      state.crmRouters = data.crm || []
      state.workLogRouters = data.workLog || []
      state.taskExamineRouters = data.taskExamine || []
      state.addressBookRouters = data.addressBook || []
      state.projectRouters = data.project || []
      state.biRouters = data.bi || []
      state.manageRouters = data.manage || []*/
      state.routers = data.routers;
      state.addRouters = data.addRouter
      /*state.crmRouters = data.router.crm || []
      state.workLogRouters = data.router.workLog || []
      state.taskExamineRouters = data.router.taskExamine || []
      state.addressBookRouters = data.router.addressBook || []
      state.projectRouters = data.router.project || []
      state.biRouters = data.router.bi || []
      state.manageRouters = data.router.manage || []*/
    },

    /**
     * 客户管理待办消息数
     */
    SET_CRMROUTERSNUM: (state, num) => {
      var routers = state.routers;
      var messageItem = {};
      for (var key in routers) {
        var route = routers[key];
        if(route.path.indexOf("/crm") != -1){
          messageItem = route;
        }
      }
      messageItem.children[0].meta.num = num
      Vue.set(state.crmRouters, 1, messageItem)
    },

    SET_GROUPSLIST: (state, data) => {
      state.groupsList = data
    }
  },
  actions: {
    GenerateRoutes({
                     commit,
                     state
                   }, data) {
      return new Promise(resolve => {
        //2020-11-24 wudw 重写获取路由方法
        getDeptPid().then((res)=>{//获取父部门Id

           adminRouterListAPI().then((response) => {//TODO 这里获取了全部路由
              var result = response.data;
              //console.log("服务端返回(处理前)：", result);
              //全部菜单路由
              var routers = result.routers;
              //无规则路由需要重写处理
              var addRoutersHandle = handleRouters(result.addRouters);
              //
             var startPage = "crm/workbench"
            

             if(res.data == 1 || res.data == 0){
               //父部门ID为1||0时展示监管平台首页
               startPage = process.env.INDEX_URL//"jg/index"
             }
              var addRouters = handleRootPath(addRoutersHandle,result.firstRoute,startPage);

              const SET_ROUTERS = {'routers': routers, 'addRouters': addRouters};
        
              
              
              //console.log("服务端返回路由(处理后)：", result);
              commit('SET_ROUTERS', SET_ROUTERS)
              //在这里连接Socket
              // socketInit();
              resolve(addRouters)
            });
         })

        // 路由完善
        /*perfectRouter(data, (routers) => {
          console.log('set routers', routers)
          commit('SET_ROUTERS', routers)
          resolve()
        })*/
      })
    }
  }
}
//开始处理首页

function handleRootPath(childRouters,firstRoute,page){
  //加载个人中心及首页
  childRouters.push({
    path: 'person',
    component: () => import('@/views/layout/UserLayout'),
    redirect: '/person/index',
    name: 'person',
    hidden: true,
    meta: {
      title: '个人中心'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/user/index')
    }]
  },
    {
      path: 'logo',
      // component: () => import('@/views/crm/workbench'),
      component: () => import('@/views/layout/CommonLayou'),
      name: 'logo',
      hidden: true,
      meta: {
        title: '首页'
      },
      redirect: '/logo/index',
      children: [{
        name: 'home',
        path: 'index',
        component:(resolve) => require(['@/views/'+page+'/index.vue'], resolve),
        //component: () => import('@/views/crm/workbench'),//智慧工地
        // component: () => import('@/views/build/index/index.vue'),//宁津
        // component: () => import('@/views/park/parkindex'),//园区
      }]
    });
  var redirect = false;
  /*if (firstRoute) {
    //这里就是赋值第一个加载的页面，/ 跳转
    redirect = firstRoute.path;
    //赋值活跃菜单（头部）
    app.state.basePath = firstRoute.basePath;
    app.state.navbar.activeIndex = firstRoute.applicationId;
    app.state.applicationId = firstRoute.applicationId;
  }else{
    redirect = '/logo';//跳转个人中心
  }*/
  var addRouters = [{
    path: '/',
    //redirect: firstRoute.path,
    component: () => import('@/views/layout/MainLayout'),
    hidden: false,
    redirect: '/logo/index',
    children:childRouters
  }];
  return addRouters;
}

// 遍历后台传来的路由字符串，转换为组件对象
function handleRouters(routers) {
  var resultArray = [];
  for (var key in routers) {
    var routeArray = routers[key];
    //console.log(routeArray)
    routeArray = filterChildren(routeArray);
    resultArray = resultArray.concat(routeArray);
  }
  return resultArray;
}

//处理子路由
function filterChildren(routeArray) {
  return routeArray.filter(route => {
    // Layout组件特殊处理
    if (route.component && route.path) {
      route.component = loadView(route.component)
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterChildren(route.children)
    }
    return true;
  })
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default permission
