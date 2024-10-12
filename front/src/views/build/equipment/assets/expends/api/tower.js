/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 塔机扩展获取详情
 * @param {*} data
 */
export function towerDetail(data) {
  return request({
    url: 'build/bEquipmentTower/get/' + data,
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
export function towerSave(data) {
  return request({
    url: 'build/bEquipmentTower/saveOrUpdate',
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
export function towerDelete(data) {
  return request({
    url: 'build/bEquipmentTower/delByEqID/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

