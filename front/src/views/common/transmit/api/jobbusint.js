import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'bTransmitJobbusint/list',
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
export function saveOrUpdate(data) {
  return request({
    url: 'bTransmitJobbusint/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function add(data) {
  return request({
    url: 'bTransmitJobbusint/add',
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
    url: 'bTransmitJobbusint/getById/'+data,
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
    url: 'bTransmitJobbusint/del/'+data,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


export function getJob() {
  return request({
    url: 'bTransmitJobbusint/listJobInfoAll',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getProject() {
  return request({
    url: 'bTransmitJobbusint/listProjectAll',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getBusint() {
  return request({
    url: 'bTransmitBusint/listAll',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getPlatform() {
  return request({
    url: 'bTransmitPlatform/listAll',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function  getPlatint(id) {
  return request({
    url: 'bTransmitPlatint/listByPlatform/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
