import request from '@/utils/request'

export function queryList(data) {
  return request({
    url: 'build/bQualPartial/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function queryById(data) {
  return request({
    url: 'build/bQualPartial/getById/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function saveOrUpdate(menu) {
  return request({
    url: 'build/bQualPartial/saveOrUpdate',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//删除
export function del(id) {
  return request({
    url: 'build/bQualPartial/del/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
