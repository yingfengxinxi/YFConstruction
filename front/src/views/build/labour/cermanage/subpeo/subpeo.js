import request from '@/utils/request'
/**
 * 特殊工种 证件管理信息
 * @param {*} data 
 */

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/labourSubconPeopleCer/selectTabsByInfo',
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
export function saveOrUpdate(data) {
  return request({
    url: 'build/labourSubconPeopleCer/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据主键查询接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/labourSubconPeopleCer/queryById/'+data,
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
export function  delTrueByIds(data) {
  return request({
    url: 'build/labourSubconPeopleCer/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询分包商信息
 */
export function selectFBSInfo() {
  return request({
    url: 'build/labourSubcontractor/selcetIdsAndName',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * selectInfoByMapNoPage
 * 根据分包商查询人员信息
 */
export function selectInfoByMapNoPage(data) {
  return request({
    url: 'build/labourInfocollection/selectInfoByMapNoPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 添加用户信息
 * @param {Object} data
 */
export function addUser(data,roleId) {
  return request({
    url: 'build/labourSubconPeopleCer/addUser/'+data+'/'+roleId,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getRole() {
  return request({
    url: 'adminRole/getRoleByIdForRoleAuth',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}