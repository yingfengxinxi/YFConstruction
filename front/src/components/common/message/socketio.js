import socketio from 'socket.io-client'
// 配置信息
import config from '@/config'

import {
  getTypeList,
} from './api'

/**
 * socketIO 初始化
 */
export function socketInit() {
  if (!config.startSocket) {
    return null;
  }
  // socketIo连接的服务器信息，就是我们后端配置的信息
  let socketBaseUrl = window.location.protocol + '//' + window.location.hostname + ':8443?target=PCM'
  // let socketBaseUrl = 'http://101.200.143.182:8443?target=PCM';//TODO 发布时修改网关
//   console.log("socketBaseUrl=== "+socketBaseUrl);
  var socketIOClient = socketio.connect(socketBaseUrl, config.socketOpts);
  socketIOClient.on('connect_error', function (data) {
    console.log("connect_error===" + data);
  });
  socketIOClient.on('connect', function (data) {
    console.log("socket：connected at ", new Date());
  });
  //连接超时监听
  socketIOClient.on('connect_timeout', function (data) {
    console.log("connect_timeout===" + data);
  });
  //错误监听
  socketIOClient.on('error', function (data) {
    console.log("error===" + data);
  });
  //配置监听
  getTypeList()
    .then(res => {
      var typeList = res.data;
      for(var i = 0; i < typeList.length; i++){
        var one  = typeList[i];
        socketIOClient.on(one.code, function (data) {
          //触发通用消息
          window.app.$emit("MSG_REFRESH", true);
          //触发自定义消息处理
          window.app.$emit(one.code, data);
        });
      }
    });
}
