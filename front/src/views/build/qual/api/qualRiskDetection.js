import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/qualRiskDetection/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存接口
 * @param {*} data
 *
 */
export function save(data) {
  return request({
    url: 'build/qualRiskDetection/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新接口
 * @param {*} data
 *
 */
export function updateInfo(data) {
  return request({
    url: 'build/qualRiskDetection/updateInfo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/qualRiskDetection/getById/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除接口
 * @param {*} data
 *
 */
export function  delByIds(data) {
  return request({
    url: 'build/qualRiskDetection/delete/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取单据编号
 * @param {*} data
 *
 */
export function getNumber(data) {
  return request({
    url: 'build/qualRiskDetection/generateDocumentNumber',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分包商下拉框
 * @param {*} data
 *
 */
export function getSubcontractorList(data) {
  return request({
    url: 'build/labourSubcontractor/selcetIdsAndName',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 班组长下拉框
 * @param {*} data
 *
 */
export function getTeamLeaderList(data) {
  return request({
    url: 'build/labourInfocollection/getTeamLeaderList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

