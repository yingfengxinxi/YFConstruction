import request from '@/utils/request'

// 获取消息列表
export function getMessageList(data) {
  return request({
    url: 'msg/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 列表接口
 * @param {*} data
 *
 */
export function getTypeList() {
  return request({
    url: 'msgQueues/listAll',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
// 未读消息数量
export function getMessageUnreadCount() {
  return request({
    url: 'msg/countUnread',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 读取消息
export function readMessage(data) {
  return request({
    url: 'msg/read',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除消息(单条)
export function delOne(data) {
  return request({
    url: 'msg/delOne',
    method: 'post',
    data: {id: data},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除已读消息
export function delReadMessage(data) {
  return request({
    url: 'msg/delRead',
    method: 'post',
    data: {type: data},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


