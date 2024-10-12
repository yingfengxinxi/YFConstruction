/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function locationList() {
  return request({
    url: 'build/equipmentLocation/queryTreeList',
    method: 'post',
  })
}
/**
 * 详情
 * @param {*} data
 */
export function locationGet(data) {
  return request({
    url: 'build/equipmentLocation/get/' + data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function locationSave(data) {
  return request({
    url: 'build/equipmentLocation/save',
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
export function locationDelete(data) {
  return request({
    url: 'build/equipmentLocation/delete',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 集团数据列表
 * @param {*} data
 */
export function locationGroupList(data) {
  return request({
    url: 'build/equipmentLocation/groupQueryTreeList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
