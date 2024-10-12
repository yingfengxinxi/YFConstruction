import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/equipmentMonitorData/selectMonitorDataList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * @param {Object} data
 * 2022-04-15更换基坑，高支模数据接口
 * shz
 */
export function getDataList(data) {
  return request({
    url: 'build/equipmentPitDataAtBak/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 监测数据-echarts数据
 * @param {*} data
 *
 */
export function selectMonitorDataListEcharts(data) {
  return request({
    url: 'build/equipmentMonitorData/selectMonitorDataListEcharts',
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
export function selectById(data) {
  return request({
    url: 'build/equipmentMonitorData/selectById/'+data,
    method: 'post',
  })
}
/**
 * 保存
 * @param {*} data
 */
export function saveOrUpdate(data) {
  return request({
    url: 'build/equipmentMonitorData/save',
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
export function deleteById(data) {
  return request({
    url: 'build/equipmentMonitorData/delete/'+data,
    method: 'post',
  })
}


/**
 * 日均数据列表接口
 * @param {*} data
 *
 */
export function getListByMonth(data) {
  return request({
    url: 'build/equipmentMonitorDataMonth/selectMonitorDataListByMonth',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 日均数据详情
 * @param {*} data
 */
export function selectMonthById(data) {
  return request({
    url: 'build/equipmentMonitorDataMonth/selectById/'+data,
    method: 'post',
  })
}
/**
 * 日均数据保存
 * @param {*} data
 */
export function saveOrUpdateMonth(data) {
  return request({
    url: 'build/equipmentMonitorDataMonth/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 监测数据-echarts日均数据 
 * @param {*} data
 *
 */
export function selectMonitorDataMonthEcharts(data) {
  return request({
    url: 'build/equipmentMonitorDataMonth/selectMonitorDataMonthEcharts',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}