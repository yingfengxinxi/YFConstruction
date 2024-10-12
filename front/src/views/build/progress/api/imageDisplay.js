import request from '@/utils/request'


export function getList(data) {
  return request({
    url: 'build/progressImageDisplay/list',
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
    url: 'build/progressImageDisplay/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取详情
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/progressImageDisplay/getById/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//删除
export function delById(id) {
  return request({
    url: 'build/progressImageDisplay/delById/'+id,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


