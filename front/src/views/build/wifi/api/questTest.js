/**
 * zy 2021-07-30
 */
import request from '@/utils/request'

//echarts 图
export function getMapByTestTypeToEcharts() {
    return request({
      url: 'build/wifiTest/getMapByTestTypeToEcharts',
      method: 'post',
      data: '',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }
