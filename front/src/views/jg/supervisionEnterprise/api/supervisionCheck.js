import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/bLabourInspection/selectApplyList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}



/**
 * 查询接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/bLabourInspectionCheck/selectByInspectionId/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 用户列表接口
 * @param {*} data
 *
 */
export function queryUserList(data) {
  return request({
    url: 'adminUser/queryUserList',
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
export function saveCheckData(data) {
  return request({
    url: 'build/bLabourInspectionCheck/saveCheckData',
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
export function getProjectsList() {
  return request({
    url: 'build/projectInfo/queryProjectList',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 结果列表接口，返回所有的监督报验数据
 * @param {*} data
 *
 */
export function selectAllDataList(data) {
  return request({
    url: 'build/bLabourInspection/selectAllDataList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询检查结果接口
 * @param {*} data
 *
 */
export function selectResultDataByPid(data) {
  return request({
    url: 'build/bLabourInspectionResults/getPid/'+data,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}