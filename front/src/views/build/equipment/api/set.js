/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function setList(data) {
  return request({
    url: 'build/equipmentSet/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表
 * @param {*} data
 */
export function setListByAsset(data) {
  return request({
    url: 'build/equipmentSet/listByAsset',
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
export function setGet(data) {
  return request({
    url: 'build/equipmentSet/get/'+data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function setSave(data) {
  return request({
    url: 'build/equipmentSet/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

