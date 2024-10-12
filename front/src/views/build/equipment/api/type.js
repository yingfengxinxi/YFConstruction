/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function typeList() {
  return request({
    url: 'build/equipmentType/queryTreeList',
    method: 'post',
  })
}
/**
 * 详情
 * @param {*} data
 */
export function typeGet(data) {
  return request({
    url: 'build/equipmentType/get/'+data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function typeSave(data) {
  return request({
    url: 'build/equipmentType/save',
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
export function typeDelete(data) {
  return request({
    url: 'build/equipmentType/delete',
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
export function typeGroupList() {
  return request({
    url: 'build/equipmentType/queryTreeGroupList',
    method: 'post',
  })
}
