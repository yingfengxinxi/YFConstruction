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
