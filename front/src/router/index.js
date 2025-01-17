import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

import crmRouter from './modules/crm'
import pmRouter from './modules/pm'
import adminRouter from './modules/admin'
import userRouter from './modules/user'
import biRouter from './modules/bi'

import taskExamineRouter from './modules/taskExamine'
import workLogRouter from './modules/workLog'
import addressBookRouter from './modules/addressBook'
import calendarRouter from './modules/calendar'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  /*{
    path: '/welcome',
    component: () => import('@/views/login/Welcome'),
    hidden: true
  },*/
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  //userRouter,
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

export const asyncRouterMap = [
  { type: 'crm', router: crmRouter },
  { type: 'taskExamine', router: taskExamineRouter },
  { type: 'workLog', router: workLogRouter },
  { type: 'addressBook', router: addressBookRouter },
  { type: 'project', router: pmRouter },
  { type: 'bi', router: biRouter },
  { type: 'manage', router: adminRouter },
  { type: 'calendar', router: calendarRouter }
]
