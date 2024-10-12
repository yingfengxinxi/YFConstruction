import request from '@/utils/request'

/**
 * 饼状图 数据展示
 * @param {*} data 
 */
export function selectPieData(){
  return request({
      url: 'build/buildQualHiddenTrouble/selectPieData',
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
export function selectBarData(data){
  return  request({
    url: 'build/buildQualHiddenTrouble/selectBarData',
    method: 'post',
	data: data,
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
      url: 'build/buildQualHiddenTrouble/selectRectifyDateWarning',
      method: 'post',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}