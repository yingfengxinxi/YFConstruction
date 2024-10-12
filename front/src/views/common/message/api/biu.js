import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function sendMsg(data) {
  return request({
    url: 'msgTest/send',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

