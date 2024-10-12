import request from '@/utils/request'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: 'adminDict/type/list',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: 'adminDict/type/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: 'adminDict/type',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: 'adminDict/type',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除字典类型
export function delType(dictId) {
  return request({
    url: 'adminDict/type/' + dictId,
    method: 'delete'
  })
}

// 清理参数缓存
export function clearCache() {
  return request({
    url: 'adminDict/type/clearCache',
    method: 'delete'
  })
}

// 查询自定义表中指定列作为字典项
export function custom(query) {
  return request({
    url: 'adminDict/type/custom',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}