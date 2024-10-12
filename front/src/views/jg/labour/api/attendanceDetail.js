import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/bLabourAttendanceRecord/getAttendanceDetailList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询班组数据
 * @param {Object} data
 */
export function selectTeamOptions(data) {
  return request({
    url: 'build/labourTeam/selcetIdsAndName',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

