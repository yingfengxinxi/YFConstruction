import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentPitReport/selectPage',
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
    url: 'build/equipmentPitReport/save',
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
export function getById(id) {
  return request({
    url: 'build/equipmentPitReport/getById/' + id,
    method: 'post',
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
export function delTrueByIds(id) {
  return request({
    url: 'build/equipmentPitReport/delete/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


