import request from '@/utils/request'

/** 查询所工种数据列表 */
export function getWTList() {
  return request({
    url: 'build/labourWorktype/selcetIdsAndWorktype',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

 /** 查询所有分包商id和name */
export function getListByFBS() {
  return request({
    url: 'build/labourSubcontractor/selcetIdsAndName',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/** 查询所有班组名称 */
export function getListByBZ() {
	return request({
		url: 'build/labourTeam/selcetIdsAndName',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
	})
}





// 根据id查询班组信息
export function getTeamById(id) {
  return request({
    url: 'build/labourTeam/queryById/' + id,
    method: 'post'
  })
}

// 查询班组长劳务人员信息列表
export function selectTeamLeaderList(data) {
  return request({
    url: 'build/labourInfocollection/getTeamLeaderList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 查询所有的劳务工种
export function selcetIdsAndWorktype() {
  return request({
    url: 'build/labourWorktype/selcetIdsAndWorktype',
    method: 'post',
  })
}

//查询 零工的id和name
export function selectOddIdAndName() {
  return request({
    url: 'build/labourInfocollection/selectOddIdAndName',
    method: 'post',
  })
}