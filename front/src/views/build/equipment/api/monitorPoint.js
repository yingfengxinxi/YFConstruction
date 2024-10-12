import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentMonitorPoint/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 所有项目接口列表接口
 * @param {*} data
 *
 */
export function getListAllProject(data) {
  return request({
    url: 'build/equipmentMonitorPoint/listAllProject',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取设备列表  关联监测类型
 * @param {*} data
 *
 */
export function getEqListByMoType(data) {
  return request({
    url: 'build/equipmentAssets/getEqListByMoType/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据设备获取有效点位  关联实时数据
 * @param {*} data
 *
 */
export function getPositionListByAssetId(data) {
  return request({
    url: 'build/equipmentMonitorPoint/getPositionListByAssetId/' + data,
    method: 'get',
    data: '',
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
    url: 'build/equipmentMonitorPoint/saveOrUpdate',
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
    url: 'build/equipmentMonitorPoint/getById/' + id,
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
export function delTrueByIds(data) {
  return request({
    url: 'build/equipmentMonitorPoint/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//基坑监测历史数据曲线图---监测点
export function selectPointsInfoForEcharts(data) {
  return request({
    url: 'build/equipmentMonitorPoint/selectPointsInfo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
  })
}

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getProjectsList() {
  return request({
    url: 'build/projectInfo/WxslelctAll',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表接口
 * @param {*} data
 *
 */
export function updateStatusByProject(projectId) {
  return request({
    url: 'datarecept/equ/' + projectId + '/status',
    method: 'get',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
