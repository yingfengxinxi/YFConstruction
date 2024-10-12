import request from '@/utils/request'

//列表查询
export function getList(query) {
  return request({
    url: 'build/projectInfo/queryPageList',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getListAll(query) {
  return request({
    url: 'build/projectInfo/queryPageListAll',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 新增项目信息
export function addProjectInfo(data) {
  return request({
    url: 'build/projectInfo/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 修改采集点信息
export function updateProjectInfo(data) {
  return request({
    url: 'build/projectInfo/updateProjectInfo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 查询采集点信息详情
export function getProjectInfoById(id) {
  return request({
    url: 'build/projectInfo/getProjectInfoById/' + id,
    method: 'post'
  })
}

// 删除采集点
export function delSupplyPoint(pointId) {
  return request({
    url: 'adminSupplyPoint/delete/' + pointId,
    method: 'post'
  })
}

// 查询水厂实时详情数据 1:自来水、2::污水
export function selectRealTimeData(type) {
  return request({
    url: 'adminSupplyPoint/waterRealTimeData/' + type,
    method: 'post'
  })
}

// 水厂单个采集点历史数据接口
export function getHistoryCurveData(data) {
  return request({
    url: 'adminSupplyPoint/getHistoryCurveData',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 查询历史报表数据
export function getHistoryReportData(data) {
  return request({
    url: 'adminSupplyPoint/getHistoryReportData',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


// 保存授权用户
export function saveUsers(data) {
  return request({
    url: 'build/projectUser/saveUsers',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//删除授权用户
export function delUsers(data) {
  return request({
    url: 'build/projectUser/delUser/' + data,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
// 授权用户列表
export function getUserListAll(data) {
  return request({
    url: 'build/projectUser/queryPageListAll',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 授权用户列表
export function getUserList(data) {
  return request({
    url: 'build/projectUser/queryPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//楼座接口
export function addBuilding(data) {
  return request({
    url: 'build/singleBuildingInfo/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectById(id) {
  return request({
    url: 'build/singleBuildingInfo/selectById/'+id,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectByProjectId(id) {
  return request({
    url: 'build/singleBuildingInfo/selectByProjectId/' + id,
    method: 'post'
  })
}

export function removeById(id) {
  return request({
    url: 'build/singleBuildingInfo/removeById/' + id,
    method: 'post'
  })
}


// 首页  右侧上方三个块
export function getThree(id) {
  return request({
    url: 'build/projectInfo/getThree/' + id,
    method: 'post'
  })
}

//工程进度--首页
export function selectProgress(){
  return request({
    url: 'build/progressPlanManageDetails/selectPlanLastTimeSHOUYE' ,
    method: 'post'
  })
}

//列表查询
export function getListByEval(query) {
  return request({
    url: 'build/projectInfo/queryPageListByEval',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//首页
export function selectNumber(query) {
  return request({
    url: 'build/monitorPointAlert/selectNumber',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//首页
export function selectEnvDataAt() {
  return request({
    url: 'build/eqMonitorDataAt/selectEnvDataAt',
    method: 'post',
  })
}
//首页
export function selectStatePM() {
  return request({
    url: 'build/monitorPointAlert/selectState',
    method: 'post',
  })
}
//首页
export function getSelectUntreatedDataList(query) {
  return request({
    url: 'build/monitorPointAlert/selectUntreatedDataList',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//首页
export function getSelectLaowu(query) {
  return request({
    url: 'adminConfig/queryConfigByName',
    method: 'post',
    data: {name:'kaoqin'},
    
  })
}
/**
 * 查询列表用于下拉框
 * @param {*} data
 *
 */
export function selectLevelList(data) {
  return request({
    url: 'build/evalLevel/selectList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//同步账号
export function syncAccountApi(){
  return request({
    url: 'build/projectInfo/sync',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
  })
}

/**
 *劳务人员
 * @param {*} data
 */
export function selectAttendanceData() {
  return request({
    url: 'build/labourInfocollection/selectAttendanceData',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
