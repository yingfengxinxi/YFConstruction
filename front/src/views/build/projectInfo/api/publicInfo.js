import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/projectPublicInfo/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存接口
 * @param {*} data
 *
 */
export function saveOrUpdate(data) {
  return request({
    url: 'build/projectPublicInfo/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保存接口
 * @param {*} data
 *
 */
export function getById(id) {
  return request({
    url: 'build/projectPublicInfo/getById/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除接口
 * @param {*} data
 *
 */
export function  delTrueByIds(data) {
  return request({
    url: 'build/projectPublicInfo/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//基坑监测历史数据曲线图---监测点
export function selectPointsInfoForEcharts(data){
  return request({
    url: 'build/equipmentMonitorPoint/selectPointsInfo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
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
