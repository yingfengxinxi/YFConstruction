import request from '@/utils/request'
//报警趋势
export function getTrand(data) {
  return request({
    url: '/build/homePage/monitSituationNoise',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getLine() {
  return request({
    url: '/build/homePage/getLine',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
