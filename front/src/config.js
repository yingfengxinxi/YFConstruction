const getLocationOrigin = () => {
  return window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '')
}
/* 这个是title配置 */
const companyName = '智慧工地'
const version = 'V11.0.0'
const baiduKey = '8SPafncsFsyVXFGGfUEmmDNwcQo6UrI9'
/** 工具包 start*/
//这里可以理解为代理的基础路径，因为socket使vue代理失效了
const startSocket = false;//是否开启
let socketBaseUrl = 'http://zhgd.sdyingfeng.cn?target=PCM';//开发基础路径，懒得动代理了
if(process.env.NODE_ENV == 'production'){
  socketBaseUrl = process.env.BASE_API;
}
const socketOpts = {//相关配置
  path:'',//生产环境时，需要配置path
  transports: ['websocket', 'polling'],//连接方式 顺序代表依次选择
  timeout: 5000,// 连接超时时间(ms)
  reconnectionAttempts: 5,//尝试重连次数 为空则会一直重连
  reconnectionDelay: 10000,// 失败重连的时间间隔
};
//已实现的消息类型
const messageTypes = ['SYS_XTGG', 'BUS_EXA_SUBMIT', 'BUS_EXA_REF', 'BUS_EXA_TG ', 'SAFETY_EMERGENCY', 'SECURITY_RISKS', 'SECURITY_RISKS_DETECTION', 'ALARM_EQU_ENVRMT', 'EQUI_OFFLINE_TIMEOUT', 'MONITOR_EQUI_ONLINE','MONITOR_EQUI_OFFLINE','QUAL_RISKS_DETECTION','ENTERPRISE_SUBMISSION'];
/** 工具包 end*/


export default {
  version,
  companyName,
  getLocationOrigin,
  baiduKey,
  startSocket,
  socketBaseUrl,
  socketOpts,
  messageTypes,
}
