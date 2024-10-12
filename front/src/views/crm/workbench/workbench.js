import request from '@/utils/request'

/**
 * 安全隐患
 * @param {*} data 
 */
export function selectSafetyHiddenDanger(data){
  return request({
      url: 'build/safetyRiskDetection/selectSafetyHiddenDanger',
      method: 'post',
      data: data,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}

/**
 * 质量隐患
 * @param {*} data 
 */
export function selectQualHiddenDanger(data){
  return request({
      url: 'build/qualQuestions/selectQualHiddenDanger',
      method: 'post',
      data: data,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}

/**
 * 人员现场管理 劳务信息
 * @param {*} data 
 */
export function selectAttendanceData(){
  return request({
      url: 'build/labourInfocollection/selectAttendanceData',
      method: 'post',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}
//公告信息
export function selectNotice(data){
    return request({
        url: 'oaAnnouncement/queryList',
        method: 'post',
        data:data
    })
}
//PM2.5/PM10
export function getEnvironment(data){
    return request({
        url: 'build/homePage/monitSituationPM',
        method: 'post',
        data:data
    })
}