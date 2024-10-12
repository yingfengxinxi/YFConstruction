import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import {
  Message
} from 'element-ui'
import {
  getAuth
} from '@/utils/auth' // 验权

let loadAsyncRouter = false
const whiteList = ['/login'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  if (to.meta.disabled) {
    next(false)
    return
  }
  //console.log(to)
  NProgress.start()
  /** 请求头包含授权信息 并且 页面必须授权 直接进入 */
  if (getAuth()) {
    if (whiteList.includes(to.path)) {
      next({
        path: '/'
      })
      NProgress.done()
    } else {
      if (!loadAsyncRouter) { // 判断当前用户是否获取权限

        loadAsyncRouter = true
        if (store.getters.allAuth) {

          //console.log("通道一：一般不会走，权限获取之后，则直接生成路由……")
          store.dispatch('GenerateRoutes', store.getters.allAuth).then(() => { // 根据auths权限生成可访问的路由表
            router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表

            if (to.path === '/404') {
              next({
                path: to.redirectedFrom || '/',
                replace: true
              })
            } else {

              next({
                ...to,
                replace: true
              })
            }
          })
        } else {

          //console.log("通道二：获取权限……")
          store.dispatch('getAuth').then(auths => { // 获取用户权限
            store.dispatch('GenerateRoutes', auths).then((addRouters) => { // 根据auths权限生成可访问的路由表
              //console.log("待添加路由：",addRouters);
              router.addRoutes(addRouters);
              next({
                path: '/',
                replace: true
              })
              //router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
              /*if (to.path === '/404') {
                next({
                  path: to.redirectedFrom || '/',
                  replace: true
                })
              } else {
                next({
                  ...to,
                  replace: true
                })
              }*/
            })
          }).catch((err) => {
            loadAsyncRouter = false
            store.dispatch('LogOut').then(() => {
              Message.error(err.msg || '获取用户信息失败')
              next({
                path: '/'
              })
            })
          })
        }
      } else {
        
        //console.log("one next to……");
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      //console.log("two next to……");
      next()
    } else {
      //console.log('three next to login……')
      next(`/login`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  
  NProgress.done() // 结束Progress
})

router.onError((error) => {

  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  const targetPath = router.history.pending.fullPath
  if (isChunkLoadFailed) {
    router.replace(targetPath)
  }
})
