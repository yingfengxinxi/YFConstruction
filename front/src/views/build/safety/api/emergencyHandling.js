import request from '@/utils/request'
/**
 * 应急事件处置
 * @param {*} data 
 */

/**
 * 列表接口--主表
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'buildSafetyEmergencyHandling/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 主表
 * 新增保存接口
 * @param {*} data
 *
 */
export function save(data) {
  return request({
    url: 'buildSafetyEmergencyHandling/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 主表
 * 修改接口
 * @param {*} data
 *
 */
export function updateById(data) {
  return request({
    url: 'buildSafetyEmergencyHandling/updateById',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 *主表
 * 根据主键查询接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'buildSafetyEmergencyHandling/queryById/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 主表 删除接口
 * @param {*} data
 *
 */
export function  delTrueByIds(data) {
  return request({
    url: 'buildSafetyEmergencyHandling/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 关联应急预案
 */
export function selectEids(){
  return request({
    url: 'build/bSafetySafetyEmergency/selectIdAndName',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}