import request from '@/utils/request'

// 查询字典数据列表
export function matchList(query) {
  return request({
    url: 'adminDictMatch/list',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 查询字典数据详细
export function getMatchById(id) {
  return request({
    url: 'adminDictMatch/get/' + id,
    method: 'post'
  })
}

// 修改字典数据
export function saveOrUpdateMatch(data) {
  return request({
    url: 'adminDictMatch/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 删除字典数据
export function delMatchById(id) {
  return request({
    url: 'adminDictMatch/delete/' + id,
    method: 'post'
  })
}

// 删除字典转换数据
export function delMatchByDict(data) {
  return request({
    url: 'adminDictMatch/delete/' + data.selfDict + '/' + data.selfDictValue, 
    method: 'post'
  })
}

// 删除字典数据
export function selectSuggestList(data) {
  return request({
    url: 'adminDictMatch/selectSuggestList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
