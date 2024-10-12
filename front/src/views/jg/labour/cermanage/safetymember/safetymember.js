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
    url: 'build/labourSafetyOfficer/getListNew',
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
    url: 'build/labourSafetyOfficer/saveOrUpdate',
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
    url: 'build/labourSafetyOfficer/selectById/'+data,
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
export function delByIds(data) {
  return request({
    url: 'build/labourSafetyOfficer/delByIds',
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
 * 查询工种信息
 * @returns 
 */
export function selectWorkTypeInfo () {
  return request({
    url: 'build/labourWorktype/list',
    method: 'post',
    data: {'pageType':'0'},
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