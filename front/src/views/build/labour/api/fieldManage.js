import request from '@/utils/request'

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

/**
 * 人员现场管理 现场工种
 * @param {*} data 
 */
export function selectWorkTypeList(){
  return request({
      url: 'build/labourInfocollection/selectSceneWorkTypeList',
      method: 'post',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}

/**
 * 人员现场管理 班组人数
 * @param {*} data 
 */
export function selectBarData(){
  return  request({
    url: 'build/labourInfocollection/selectTeamList',
    method: 'post',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}

/**
 * 人员现场管理 民族和性别
 * @param {*} data 
 */
export function selectNationAndGenderList(){
  return  request({
    url: 'build/labourInfocollection/selectNationAndGender',
    method: 'post',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}