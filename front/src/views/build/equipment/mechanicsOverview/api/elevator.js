import request from '@/utils/request'

/*
 查询升降机列表
 */
export function getList() {
  return request({
    url: 'build/equipmentAssets/selectElevatorOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询升降机报警数据
 */
export function getAlertList() {
  return request({
    url: 'build/equipmentAssets/selectElevatorAlertNew',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询升降机详情
 */
export function selectElevatorById(id) {
  return request({
    url: 'build/equipmentAssets/selectElevatorById/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询吊重力矩统计
 */
export function selectDZTJ(id) {
  return request({
    url: 'build/equipmentElevatorEffect/selectDZTJ/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/*
升降机今日运转数据
 */
export function selectJRGX(id) {
  return request({
    url: 'build/equipmentElevatorEffect/selectJRGX/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/*
升降机司机信息
 */
export function selectSJXX(code) {
  return request({
    url: 'build/equipmentElevatorEffect/selectSJXX/'+code,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
吊钩列表
 */
export function getHookList() {
  return request({
    url: 'build/equipmentAssets/selectHookOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/*
设备总览列表
 */
export function getAllList() {
  return request({
    url: 'build/equipmentAssets/selectAllOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询总览设备信息
 */
export function getAllAlertList() {
  return request({
    url: 'build/equipmentAssets/selectAllAlertNew',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}