
import request from '@/utils/request'

// 查询自定义表中指定列作为字典项
export function custom(query) {
  return request({
    url: 'adminCommon/getDict',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
