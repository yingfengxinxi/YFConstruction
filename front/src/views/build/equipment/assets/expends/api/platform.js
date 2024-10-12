/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 塔机扩展获取详情
 * @param {*} data
 */
export function platformDetail(data) {
  return request({
    url: 'build/equipmentPlatform/get/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 塔机扩展提交
 * @param {*} data
 */
export function platformSave(data) {
  return request({
    url: 'build/equipmentPlatform/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 塔机扩展删除
 * @param {*} data
 */
export function platformDelete(data) {
  return request({
    url: 'build/equipmentPlatform/delByEqID/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

