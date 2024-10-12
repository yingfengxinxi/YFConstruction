import request from '@/utils/request'

/**
 * 班组列表
 * @param {*} data
 *
 */
export function getTeamList() {
  return request({
    url: 'build/labourTeam/selcetIdsAndName',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 班组下人员数据
 * @param {*} data
 *
 */
export function selectPersonByTeam(data) {
  return request({
    url: 'build/labourInfocollection/selectListByTeam/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

