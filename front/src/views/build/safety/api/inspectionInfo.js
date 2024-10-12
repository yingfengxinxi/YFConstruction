import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/safetyInspectionInfo/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 执行和复检接口
 * @param {*} data
 *
 */
export function updateInspectionInfo(data) {
  return request({
    url: 'build/safetyInspectionInfo/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 新增数据
 * @param {*} data
 *
 */
export function addInspectionInfo(data) {
  return request({
    url: 'build/safetyInspectionInfo/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据Id查询接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/safetyInspectionInfo/getById/'+data,
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
    url: 'build/safetyInspectionPlan/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 整改通知单
 * @param {*} data
 *
 */
export function getPrintData(data) {
  return request({
    url: 'build/safetyInspectionInfo/getPrintData/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全隐患超期列表
 * @param {*} data
 *
 */
export function selectTimeoutList(data) {
  return request({
    url: 'build/safetyInspectionInfo/selectTimeoutList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全隐患超期列表
 * @param {*} data
 *
 */
export function dangeTimeoutPush(id, detailId) {
  return request({
    url: 'build/safetyInspectionInfo/dangeTimeoutPush/' + id + '/' + detailId,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据类型查询整改配置
 * @param {*} data
 *
 */
export function queryPrintTemplateByType(data) {
  return request({
    url: 'utilsPrint/queryPrintTemplateByType/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 班组列表
 * @param {*} data
 *
 */
export function getTeamList() {
  return request({
    url: 'build/labourTeam/selcetIdsAndName',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 班组下人员数据
 * @param {*} data
 *
 */
export function selectPersonByTeam(data) {
  return request({
    url: 'build/labourInfocollection/selectListByTeam/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全检查标准数据
 * @param {*} data
 *
 */
export function selectSafetyProduction(data) {
  return request({
    url: 'build/bSafetyProduction/selectSafetyProduction',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

