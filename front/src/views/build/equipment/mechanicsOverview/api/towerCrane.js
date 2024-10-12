import request from '@/utils/request'

/*
 查询塔机列表
 */
export function getList() {
  return request({
    url: 'build/equipmentAssets/selectTowerCraneOverview',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询塔机报警数据
 */
export function getAlertList() {
  return request({
    url: 'build/equipmentAssets/selectTowerCraneAlertNew',
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/*
查询塔机详情
 */
export function selectTowerCraneById(id) {
  return request({
    url: 'build/equipmentAssets/selectTowerCraneById/'+id,
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
    url: 'build/equipmentTowerEffect/selectDZTJ/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/*
塔吊今日运转数据
 */
export function selectJRGX(id) {
  return request({
    url: 'build/equipmentTowerEffect/selectJRGX/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/*
塔吊司机信息
 */
export function selectSJXX(code) {
  return request({
    url: 'build/equipmentTowerEffect/selectSJXX/'+code,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
