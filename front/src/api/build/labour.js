import request from '@/utils/request'

// 查询分包商数据
export function selectList(data) {
  return request({
    url: 'build/labourSubcontractor/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//建设单位查询
export function selectConstructionUnitList(data) {
  return request({
    url: 'build/labourConstructionUnit/selectPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 根据id查询分包商信息
export function getSubcontractorById(id) {
  return request({
    url: 'build/labourSubcontractor/queryById/' + id,
    method: 'post'
  })
}

// 根据id查询企业商信息
export function getConstructionUnitById(id) {
  return request({
    url: 'build/labourConstructionUnit/getById/' + id,
    method: 'post'
  })
}

// 新增分包商信息
export function saveConstructionUnit(data) {
	return request({
		url: 'build/labourConstructionUnit/save',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}
// 新增建设单位
export function addSubcontractor(data) {
	return request({
		url: 'build/labourSubcontractor/add',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}
// 修改分包商信息
export function editSubContractor(data) {
	return request({
		url: 'build/labourSubcontractor/edit',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}

// 删除分包商信息
export function deleteSubcontractor(id) {
  return request({
    url: 'build/labourSubcontractor/delete/' + id,
    method: 'post'
  })
}
//删除企业数据
export function deleteConstructionUnit(id) {
  return request({
    url: 'build/labourConstructionUnit/delete/' + id,
    method: 'post'
  })
}

// 查询班组数据列表
export function selectTeamList(data) {
  return request({
    url: 'build/labourTeam/list',
    method: 'post',
    data: data,
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

// 新增班组信息
export function addTeam(data) {
	return request({
		url: 'build/labourTeam/add',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}

// 修改班组信息
export function editTeam(data) {
	return request({
		url: 'build/labourTeam/edit',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}

// 删除班组信息
export function deleteTeam(id) {
  return request({
    url: 'build/labourTeam/delete/' + id,
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
//工人进出场查询
export function selectAdvanceRetreatList(data) {
  return request({
    url: 'build/labourAdvanceRetreat/selectPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
// 根据id工人进出场信息
export function getAdvanceRetreatById(id) {
  return request({
    url: 'build/labourAdvanceRetreat/getById/' + id,
    method: 'post'
  })
}
// 更新进出场信息
export function saveAdvanceRetreat(data) {
	return request({
		url: 'build/labourAdvanceRetreat/save',
		method: 'post',
		data: data,
		headers:{
			'Content-Type': 'application/json;charset=UTF-8'
		}
	})
}
//删除进出场信息
export function deleteAdvanceRetreat(id) {
  return request({
    url: 'build/labourAdvanceRetreat/delete/' + id,
    method: 'post'
  })
}