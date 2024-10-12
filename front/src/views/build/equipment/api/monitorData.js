import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getMDataByEqList(data) {
  return request({
    url: 'build/equipmentMonitorData/getMDataByEq',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

