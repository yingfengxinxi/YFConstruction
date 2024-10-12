import request from '@/utils/request'

//列表查询
export function getList(query) {
  return request({
    url: 'build/qualMaterial/selectPageAllByPid',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//收藏列表查询
export function getCollectList(id) {
  return request({
    url: 'build/qualMaterial/selectByCollect/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function add(data) {
  return request({
    url: 'build/qualMaterial/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function get(id) {
  return request({
    url: 'build/qualMaterial/getById/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function removeById(id) {
  return request({
    url: 'build/qualMaterial/removeById/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function collect(id) {
  return request({
    url: 'build/qualMaterial/collect/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function deleteCollect(id) {
  return request({
    url: 'build/qualMaterial/deleteCollect/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 新增目录
export function addCatalog(data) {
  return request({
    url: 'build/qualMaterialCatalog/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//查询目录树
export function getCatalogTree(id) {
  return request({
    url: 'build/qualMaterialCatalog/selectTree/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//删除目录
export function deleteCatalog(id) {
  return request({
    url: 'build/qualMaterialCatalog/delete/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function addChangelog(data) {
  return request({
    url: 'build/qualMaterialChangelog/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function selectByPid(id) {
  return request({
    url: 'build/qualMaterialChangelog/selectByPid/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function selectById(id) {
  return request({
    url: 'build/qualMaterialCatalog/selectById/'+id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


