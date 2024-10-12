import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentHelmet/list',
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
    url: 'build/equipmentHelmet/saveOrUpdate',
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
    url: 'build/equipmentHelmet/selectById/'+data,
    method: 'get',
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
export function deleteDetails(data) {
  return request({
    url: 'build/equipmentHelmet/deleteDetails/'+data,
    method: 'get',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 项目人员数据
 */
export function getPeopleList() {
  return request({
    url: 'build/labourInfocollection/selectInfoByMapNoPage',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 轨迹数据列表接口
 * @param {*} data
 *
 */
export function getGpsList(data) {
  return request({
    url: 'build/equipmentHelmetGps/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

