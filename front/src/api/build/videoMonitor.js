import request from '@/utils/request'
//视频token
export function getVideoToken() {
  return request({
    url: 'build/ysy/getAccessToken',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//列表查询
export function queryByParentId(query) {
  return request({
    url: 'build/videoMonitor/queryByParentId/'+query,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//设备台账位置树表查询
export function queryTreeListByParentId (query,data) {
  return request({
    url: 'build/equipmentLocation/queryTreeListByParentId/' + query,
    method: 'post',
    data:data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//设备台账类型中设备列表查询，不分页
export function queryEquiBytypeId (data) {
  return request({
    url: 'build/equipmentAssets/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data:data
  })
}
//查询视频流地址（hls）
export function queryVideoMonitor (data) {
  return request({
    url: 'build/equipmentAssets/selectVideoMonitor',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}
// 新增项目信息
export function addVideoMonitor(data) {
  return request({
    url: 'build/videoMonitor/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function removeById(id) {
  return request({
    url: 'build/videoMonitor/removeById/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据设备资产id查询录像机id和通道id，准备获取视频流
 * @param {*} id 
 * @returns 
 */
export function getDeviceId (id) {
  return request({
    url: 'build/equipmentAssets/getDeviceId/' + id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

