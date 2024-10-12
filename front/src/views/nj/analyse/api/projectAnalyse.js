import request from '@/utils/request'
//获取项目
export function projectList(data) {
  return request({
    url: 'build/projectInfo/queryProjectListByState',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//报警数量
export function alarmsStatistics(data) {
  return request({
    url: 'build/projectAnalysis/alarmsStatistics',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//喷淋启停记录
export function getStartStop(data) {
  return request({
    url: 'build/projectAnalysis/sprayRecord',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//近七日空气质量
export function latelyAirQualityAnalysis(data) {
  return request({
    url: 'build/projectAnalysis/latelyAirQualityAnalysis',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//24小时
export function realTimeData(data) {
  return request({
    url: 'build/projectAnalysis/realTimeData',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//历史空气分析
export function hisAirQualityAnalysis(data) {
  return request({
    url: 'build/projectAnalysis/hisAirQualityAnalysis',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//PM2.5/10
export function todayAirQualityAnalysis(data) {
  return request({
    url: 'build/projectAnalysis/todayAirQualityAnalysis',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//报警趋势
export function alarmsTrend(data) {
  return request({
    url: 'build/projectAnalysis/alarmsTrend',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//时间 面积 造价
export function getTimeArea(data) {
  return request({
    url: 'build/projectAnalysis/projectInfoById/'+data,
    method: 'post',
  })
}

/**
 * 查询报警趋势
 * @param {*} data
 */
export function selectAlertTrend(data) {
  return request({
    url: 'build/monitorPointAlert/selectAlertTrend',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}