import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/supervisionEnterprise/list',
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
    url: 'build/supervisionEnterprise/getById/'+data,
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
export function insertOrupdateData(data) {
  return request({
    url: 'build/supervisionEnterprise/insertOrupdateData',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询监督人员监督情况
 * @param {*} data
 *
 */
export function getSupeList(data) {
  return request({
    url: 'build/supervisionEnterprise/selectSupeList',
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