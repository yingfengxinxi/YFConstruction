import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/progressSelfEvaluateYear/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getEvaluateList(id) {
  return request({
    url: 'build/progressSelfEvaluateYear/getEvaluateList/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getPrintData(id) {
  return request({
    url: 'build/progressSelfEvaluateYear/getPrintData/'+id,
    method: 'get',
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
export function add(data) {
  return request({
    url: 'build/progressSelfEvaluateYear/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function update(data) {
  return request({
    url: 'build/progressSelfEvaluateYear/update',
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
    url: 'bTransmitPlatint/getById/'+data,
    method: 'get',
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
    url: 'build/progressSelfEvaluateYear/delBySecurityId/'+data,
    method: 'get',
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
export function selectByEvaluationId(id) {
  return request({
    url: 'build/progressSafetyProduction/selectByEvaluationId/'+id,
    method: 'get',
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
