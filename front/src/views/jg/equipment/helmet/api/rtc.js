import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function sendTRtc(data) {
  return request({
    url: 'message/msg/trtc',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
