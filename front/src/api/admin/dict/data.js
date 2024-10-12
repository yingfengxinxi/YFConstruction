import request from '@/utils/request'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: 'adminDict/data/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(dictCode) {
  return request({
    url: 'adminDict/data/' + dictCode,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: 'adminDict/data/type/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: 'adminDict/data',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: 'adminDict/data',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除字典数据
export function delData(dictCode) {
  return request({
    url: 'adminDict/data/' + dictCode,
    method: 'delete'
  })
}
