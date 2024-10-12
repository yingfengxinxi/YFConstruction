import request from '@/utils/request'

/**
 * 数据获取
 * @param {*} data
 */
export function selectChartData(){
  return request({
    url: 'build/bSafetyRiskControls/getChartData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

