/**
 * zy 2021-05-18
 */
import request from '@/utils/request'

//项目类型
export function queryProjTypeMapListAll(){
  return request({
    url: 'build/projectInfo/queryMapListAll',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//项目明细
export function queryProjNoPageList(){
  return request({
    url: 'build/projectInfo/queryMapNoPageList',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 今日施工在场人数 劳务信息
 * @param {*} data 
 */
export function selectAttendanceDataByProject(data){
  return request({
      url: 'build/labourInfocollection/selectAttendanceDataByProject',
      method: 'post',
      data: data,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}
/**
 * 项目进度echarts + 右上角四个块
 */
export function selectProgress(){
  return request({
    url: 'build/projectInfo/selectProgress',
    method: 'post',
    data: '',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//隐患  四个块
export function selectYinHuanFour(data) {  
  return request({
    url: 'build/safetyInspectionInfo/getCount/'+data,
    method: 'post',
    data: '',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}