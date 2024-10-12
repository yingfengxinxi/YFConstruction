import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'datarecept/receptPlatform/list',
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
    url: 'datarecept/receptPlatform/saveOrUpdate',
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
export function changeStatus(data) {
  return request({
    url: 'datarecept/receptPlatform/changeStatus',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 获取详情
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'datarecept/receptPlatform/getById/'+data,
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
    url: 'datarecept/receptPlatform/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

