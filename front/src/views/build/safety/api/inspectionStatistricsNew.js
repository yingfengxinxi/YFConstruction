import request from '@/utils/request'

/**
 * 饼状图 数据展示
 * @param {*} data 
 */
export function selectPieData(){
  return request({
      url: 'build/safetyRiskDetection/selectPieData',
      method: 'post',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}

/**
 * 柱状图数据展示
 * @param {*} data 
 */
export function selectBarData(){
  return  request({
    url: 'build/safetyRiskDetection/selectBarData',
    method: 'post',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 曲线图数据展示
 * @param {*} data 
 */
export function selectCurveData(){
  return  request({
    url: 'build/safetyRiskDetection/selectCurveData',
    method: 'post',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 整改超时预警 饼状图 数据展示
 * @param {*} data 
 */
export function selectWarningData(){
  return request({
      url: 'build/safetyRiskDetection/selectRectifyDateWarning',
      method: 'post',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}