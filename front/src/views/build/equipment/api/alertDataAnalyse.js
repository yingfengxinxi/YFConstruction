/**
 * Create by yxk at 2021/09/26
 */
import request from '@/utils/request'

/**
 * 查询报警数量
 * @param {*} data
 */
export function selectAlertNumber() {
  return request({
    url: 'build/monitorPointAlert/selectAlertNumber',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询报警列表
 * @param {*} data
 */
export function selectAlertListNew() {
  return request({
    url: 'build/monitorPointAlert/selectAlertListNew',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询报警类型占比
 * @param {*} data
 */
export function selectTypeProportionByParent() {
  return request({
    url: 'build/monitorPointAlert/selectTypeProportionByParent',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
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

/**
 * 查询扬尘报警多发报警时段
 * @param {*} data
 */
export function selectRaiseDustAlert(data) {
  return request({
    url: 'build/monitorPointAlert/selectRaiseDustAlert',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询颗粒物排名
 * @param {*} data
 */
export function selectPMRank(data) {
  return request({
    url: 'build/monitorPointAlert/selectPMRank',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询近一个月各项目报警数对比
 * @param {*} data
 */
export function selectProjectAlertNumber() {
  return request({
    url: 'build/monitorPointAlert/selectProjectAlertNumber',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询噪音报警多发报警时段
 * @param {*} data
 */
export function selectNoiseAlert(data) {
  return request({
    url: 'build/monitorPointAlert/selectNoiseAlert',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询AI识别报警数据
 * @param {*} data
 */
export function selectDregCarAlert(data) {
  return request({
    url: 'build/aiDregCarAlert/selectDregAlertList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
