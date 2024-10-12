/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function planList(data) {
  return request({
    url: 'build/equipmentPlan/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 详情
 * @param {*} data
 */
export function planGet(data) {
  return request({
    url: 'build/equipmentPlan/get/'+data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function planSave(data) {
  return request({
    url: 'build/equipmentPlan/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除
 * @param {*} data
 */
export function planDelete(data) {
  return request({
    url: 'build/equipmentPlan/delete',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
