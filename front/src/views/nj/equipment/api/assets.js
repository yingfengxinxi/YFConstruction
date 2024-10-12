/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function assetsList(data) {
  return request({
    url: 'build/equipmentAssets/list',
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
    url: 'build/equipmentAssets/listByLocationId',
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
export function assetGet(data) {
  return request({
    url: 'build/equipmentAssets/get/'+data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function assetSave(data) {
  return request({
    url: 'build/equipmentAssets/save',
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
export function assetDelete(data) {
  return request({
    url: 'build/equipmentAssets/delete',
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
export function selectEquOptions(data) {
  return request({
    url: 'build/equipmentAssets/selectEquList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 塔吊设备列表
 * @param {*} data
 */
export function selectTowerCrane(data) {
  return request({
    url: 'build/equipmentAssets/selectTowerCrane',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目信息列表
 * @param {*} data
 */
export function queryProjectList(data) {
  return request({
    url: 'build/projectInfo/queryProjectList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询资产台账列表 集团数据
 * @param {*} data
 */
export function assetsGroupList(data) {
  return request({
    url: 'build/equipmentAssets/groupList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
