/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function eqpitList(data) {
  return request({
    url: 'build/bEquipmentPit/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 对接配置列表接口
 * @param {*} data
 *
 */
export function getPitConfigList(data) {
  return request({
    url: 'build/receptPlatform/list',
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
export function assetsListLocation(data) {
  return request({
    url: 'build/bEquipmentPit/listByLocationId',
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
export function eqpitGet(data) {
  return request({
    url: 'build/bEquipmentPit/get/'+data,
    method: 'post',
  })
}


/**
 * 保存
 * @param {*} data
 */
export function eqpitSave(data) {
  return request({
    url: 'build/bEquipmentPit/save',
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
export function eqpitDelete(data) {
  return request({
    url: 'build/bEquipmentPit/delete',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询监测点列表
 * @param {*} data
 *
 */
export function eqpitEquOptions(data) {
  return request({
    url: 'build/bEquipmentPit/selectEquList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

