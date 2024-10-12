/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 塔机扩展获取详情
 * @param {*} data
 */
export function pitDetail(data) {
  return request({
    url: 'build/bEquipmentPitFormwork/get/' + data,
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
export function pitSave(data) {
  return request({
    url: 'build/bEquipmentPitFormwork/saveOrUpdate',
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
export function pitDelete(data) {
  return request({
    url: 'build/bEquipmentPitFormwork/delByEqID/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

