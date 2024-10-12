import request from '@/utils/request'

/*
 查询工程
 */
export function getList(id) {
  return request({
    url: 'build/bEquipmentPro/dplist/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//基坑报警
export function getJkAlertList() {
  return request({
    url: 'build/equipmentAssets/selectPitAlertNew',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//高支模报警
export function getGzmAlertList() {
  return request({
    url: 'build/equipmentAssets/selectFormworkAlertNew',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//深基坑设备列表
export function getJkList() {
  return request({
    url: 'build/equipmentAssets/selectPitOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//高支模设备列表
export function getGzmList() {
  return request({
    url: 'build/equipmentAssets/selectFormworkOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}