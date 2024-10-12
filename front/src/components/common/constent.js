/** 系统模块配置
 * */

const examineConfig = {
  10000: {
    name: '测试DEMO',//名称描述
    icon: {
      name: 'wk-expense',//
      color: 'blue',//背景色
    },
    component: {//详情组件
      type: 'slide',
      url: 'demo/wf/Detail'//路径
    }
  },
  10010: {
    name: '专项安全方案',//名称描述
    icon: {
      name: 'wk-expense',//
      color: 'blue',//背景色
    },
    component: {//详情组件
      type: 'slide',
      url: 'build/safety/plan/Detail_LC'//路径  build/safety/plan/Detail_LC
    }
  },
  10020: {
    name: '创优评优申报',//名称描述
    icon: {
      name: 'wk-expense',//
      color: 'blue',//背景色
    },
    component: {//详情组件
      type: 'slide',
      url: 'build/qual/appraisingDeclare/Detail'//路径  build/qual/appraisingDeclare/Detail
    }
  },
  10030: {
    name: '月计划管理',//名称描述
    icon: {
      name: 'wk-expense',//
      color: 'blue',//背景色
    },
    component: {//详情组件
      type: 'slide',
      url: 'build/progress/planMonth/planMonthDetail/Detail'
    }
  },


};

export default {
  data() {
    return {
      /**
       * ===============审批===========
       * name    业务类型描述
       * icon
       *  name  图标的class
       *  color 背景色
       * component 审批点击详情组件
       *  type 组件类型  slide：滑出  dialog 弹框
       *  url  组件路径
       * */
      examineConfig: examineConfig,
      /**
       * ===============消息===========
       * 注意 拼接内容支持  ${this.data.realname} 表达式
       */
      messageConfig: {
        //=======公告================
        'SYS_XTGG': {
          name: '公告',//名称描述
          leftContent: '您有一个新公告',//左侧拼接内容
          rightContent: '，请及时查看',//右侧拼接内容
          icon: {
            name: 'wk wk-announcement',//
            color: '#6995FF',//背景色
          },
          component: {//详情组件
            type: 'slide',
            url: 'oa/notice/NoticeDetail'//路径
          }
        },
        //=======通知==================
        'BUS_EXA_SUBMIT': {
          name: '审批提交',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}提交了`,//左侧拼接内容
          rightContent: `审批，请及时查看`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#6995FF',//背景色
          },
          component: examineConfig[`${this.data ? this.data.businessType : '-1'}`] ?
            examineConfig[`${this.data ? this.data.businessType : '-1'}`].component : {},
        },
        'BUS_EXA_TG': {
          name: '审批通过',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}通过了您的`,//左侧拼接内容
          rightContent: `审批，请及时查看`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#67c23a',//背景色
          },
          component: examineConfig[`${this.data ? this.data.businessType : '-1'}`] ?
            examineConfig[`${this.data ? this.data.businessType : '-1'}`].component : {},
        },
        'BUS_EXA_REF': {
          name: '审批拒绝',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}拒绝了您的`,//左侧拼接内容
          rightContent: `审批，请及时处理`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#fb6767',//背景色
          },
          component: examineConfig[`${this.data ? this.data.businessType : '-1'}`] ?
            examineConfig[`${this.data ? this.data.businessType : '-1'}`].component : {},
        },
        //其他==
        'SAFETY_EMERGENCY': {
          name: '应急事件',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}提交了应急事件`,//左侧拼接内容
          rightContent: `，请及时处理`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#fb6767',//背景色
          },
          component:  {//详情组件
            type: 'slide',
            url: 'build/safety/emergencyHandling/Details_XX'//路径
          }
        },
        //环境预警==
        'ALARM_EQU_ENVRMT': {
          name: '环境预警',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}提交了环境预警`,//左侧拼接内容
          rightContent: `，请及时处理`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#fb6767',//背景色
          },
          component:  {//详情组件
            type: 'slide',
            url: 'build/equipment/monitorPointAlert/Details_XX'//路径
          }
        },
				//设备报警==
				'EQUI_OFFLINE_TIMEOUT': {
				  name: '设备超时预警',//名称描述
				  leftContent: `${this.data ? this.data.byUserName : ''}设备超时预警`,//左侧拼接内容
				  rightContent: `，请及时处理`,//右侧拼接内容
				  icon: {
				    name: 'wk wk-o-task',//
				    color: '#fb6767',//背景色
				  },
				  component:  {//详情组件
				    type: 'slide',
				    url: 'build/equipment/monitorPointAlert/Details_XX'//路径
				  }
        },
        'ENTERPRISE_SUBMISSION': {
          name: '企业提报消息提醒',//名称描述
          leftContent: `${this.data ? this.data.byUserName : ''}发起了企业提报`,//左侧拼接内容
          rightContent: `，请及时处理`,//右侧拼接内容
          icon: {
            name: 'wk wk-o-task',//
            color: '#fb6767',//背景色
          },
          component:  {//详情组件
            type: 'slide',
            url: 'jg/supervisionEnterprise/supervisionCheck'//路径
          }
        },
        //监控设备上线
        // 'MONITOR_EQUI_ONLINE': {
        //   name: '监控设备上线提醒',//名称描述
        //   leftContent: `${this.data ? this.data.byUserName : ''}监控设备上线提醒`,//左侧拼接内容
        //   rightContent: ``,//右侧拼接内容
        //   icon: {
        //     name: 'wk wk-o-task',//
        //     color: '#6995FF',//背景色
        //   },
        //   component: {//详情组件
        //     type: 'slide',
        //     url: 'build/equipment/monitorPointAlert/Details_XX'//路径
        //   }
        // },
        //监控设备离线
        // 'MONITOR_EQUI_OFFLINE': {
        //   name: '设备超时预警',//名称描述
        //   leftContent: `${this.data ? this.data.byUserName : ''}设备超时预警`,//左侧拼接内容
        //   rightContent: `，请及时处理`,//右侧拼接内容
        //   icon: {
        //     name: 'wk wk-o-task',//
        //     color: '#fb6767',//背景色
        //   },
        //   component: {//详情组件
        //     type: 'slide',
        //     url: 'build/equipment/monitorPointAlert/Details_XX'//路径
        //   }
        // },
      }
    }
  },


}
