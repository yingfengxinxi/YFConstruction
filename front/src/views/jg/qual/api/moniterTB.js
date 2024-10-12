import request from '@/utils/request'
/**
 * 监测项 数据填报 设置
 * @param {*} data 
 */

/**
 * 三表公用  列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  let s = 'buildQualMonitTbZhb/list'
  if(data.type == '2'){
    s = 'buildQualMonitBtbZhb/list'
  }else if(data.type == '3'){
    s = 'buildQualMonitJtbZhb/list'
  }
  
  return request({
    url: s,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 三表公用
 * 保存接口
 * @param {*} data
 *
 */
export function saveOrUpdate(data,type) {
  let s = 'buildQualMonitTbZhb/saveOrUpdate'
  if(type == '2'){
    s = 'buildQualMonitBtbZhb/saveOrUpdate'
  }else if(type == '3'){
    s = 'buildQualMonitJtbZhb/saveOrUpdate'
  }

  return request({
    url: s,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 三表公用
 * 根据主键查询接口
 * @param {*} data
 *
 */
export function getById(data,type) {
  let s = 'buildQualMonitTbZhb/queryById/'
  if(type == '2'){
    s = 'buildQualMonitBtbZhb/queryById/'
  }else if(type == '3'){
    s = 'buildQualMonitJtbZhb/queryById/'
  }

  return request({
    url: s+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 三表公用
 * 根据主表主键查询子表数据  接口
 * @param {*} data
 *
 */
export function getDetailsByZHId(data,type) {
  let s = 'buildQualMonitTbZb/queryByZHUId/'
  if(type == '2'){
    s = 'buildQualMonitBtbZb/queryByZHUId/'
  }else if(type == '3'){
    s = 'buildQualMonitJtbZb/queryByZHUId/'
  }

  return request({
    url: s+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 三表公用  删除接口
 * @param {*} data
 *
 */
export function  delTrueByIds(data,type) {
  let s = 'buildQualMonitTbZhb/delTrueByIds'
  if(type == '2'){
    s = 'buildQualMonitBtbZhb/delTrueByIds'
  }else if(type == '3'){
    s = 'buildQualMonitJtbZhb/delTrueByIds'
  }

  return request({
    url: s,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
