import request from '@/utils/request'

/**
 * 仪表盘排序
 * @param {*} data
 */
export function crmIndexSortAPI(roleId) {
  // console.log("==crmIndexSortAPI:roleId"+roleId);
  return request({
    url: `adminInstrumentRole/query/${roleId}`,
    method: 'post',
  })
}

/**
 * 仪表盘排序设置
 * @param {*} data
 */
export function crmIndexSetSortAPI(data) {
  return request({
    url: 'adminInstrumentRole/setModelSort',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 仪表盘名称修改
 * @param {*} data
 */
export function updateModelNameAPI(data) {
  return request({
    url: 'adminInstrument/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}