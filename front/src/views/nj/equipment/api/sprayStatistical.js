import request from '@/utils/request'

/**
 * 饼状图 数据展示
 * @param {*} data
 */
export function selectPieData(){
  return request({
    url: 'build/equipmentSpraySystem/selectPieData',
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
    url: 'build/equipmentSpraySystem/selectBarData',
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
    url: 'build/equipmentSpraySystem/selectPlscfxData',
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
export function selectKqsdfxData(){
  return request({
    url: 'build/equipmentSpraySystem/selectKqsdfxData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
