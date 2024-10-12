import request from '@/utils/request'

//24小时
export function realTimeData(data) {
  return request({
    url: 'build/equipmentMonitorData/realTimeDataZs',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function realTimeDataTemperature(data) {
  return request({
    url: 'build/equipmentMonitorData/realTimeDataTemperature',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function realTimeDataAll(data) {
  return request({
    url: 'build/equipmentMonitorData/realTimeDataAll',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectEquList(data) {
  return request({
    url: 'build/equipmentSpraySystem/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectPmTsp() {
  return request({
    url: 'build/projectAnalysis/todayAirQualityAnalysisNew',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectSeven() {
  return request({
    url: 'build/projectAnalysis/latelyAirQualityAnalysisNew',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selecAnalysis() {
  return request({
    url: 'build/projectAnalysis/hisAirQualityAnalysisNew',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectEnvDataAt(data) {
  return request({
    url: 'build/eqMonitorDataAt/selectEnvDataAt',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
