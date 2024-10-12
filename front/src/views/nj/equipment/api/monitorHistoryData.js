import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentMonitorData/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 用户导入
 * @param {*} data
 */
export function excelImportAPI(data) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: 'build/equipmentMonitorData/excelImport',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}






//基坑监测历史数据曲线图---数据查询
export function selectInfoForEcharts(param){
  return request({
    url: 'build/equipmentMonitorData/selectInfo',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
  })

}

/**
 * 环境监测历史数据列表接口
 * @param {*} data
 *
 */
export function listByProject(data) {
  return request({
    url: 'build/equipmentMonitorData/listByProject',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 下载智能工地监测数据报告
 * @param {*} data
 */
export function downMonitorDataReport(data) {
  return request({
    url: 'utilsPrint/downMonitorDataReport',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}
/**塔机实时数据
 * @param {Object} data
 */
export function selectTowerPage(data) {
  return request({
    url: 'build/equipmentTowerDataAt/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**塔机功效数据
 * @param {Object} data
 */
export function selectTowerEffectPage(data) {
  return request({
    url: 'build/equipmentTowerEffect/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**升降机实时数据
 * @param {Object} data
 */
export function selectElevatorPage(data) {
  return request({
    url: 'build/equipmentElevatorDataAt/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**升降机功效数据
 * @param {Object} data
 */
export function selectElevatorEffectPage(data) {
  return request({
    url: 'build/equipmentElevatorEffect/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectElevatorAlertPage(data) {
  return request({
    url: 'build/monitorPointAlert/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectOfflinePage(data) {
  return request({
    url: 'build/equipmentOffline/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
