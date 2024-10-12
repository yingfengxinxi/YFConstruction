import request from '@/utils/request'

export function queryList(data) {
  return request({
    url: 'build/qualAcceptance/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function queryById(data) {
  return request({
    url: 'build/qualAcceptance/getById/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function saveOrUpdate(menu) {
  return request({
    url: 'build/qualAcceptance/saveOrUpdate',
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
    url: 'build/qualAcceptance/del/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}