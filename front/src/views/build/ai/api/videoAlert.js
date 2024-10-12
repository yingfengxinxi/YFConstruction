import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/aiVideoAlert/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getById(id) {
  return request({
    url: 'build/equipmentMonitorData/getById/' + id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * ai设备数量统计
 * @returns 
 */
export function getaiEqumentStatistic () {
  return request({
    url: 'build/aiVideoAlert/aiEqumentStatistic' ,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * ai事件类型数量统计
 * @returns 
 */
export function getEventTypeStatistic () {
  return request({
    url: 'build/aiVideoAlert/aiEventTypeStatistic',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}