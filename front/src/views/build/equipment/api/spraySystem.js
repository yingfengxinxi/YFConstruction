import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentSpraySystem/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}