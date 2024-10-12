import request from '@/utils/request'

//查询树列表
export function getTree() {
  return request({
    url: 'build/bSafetyDangerSource/queryTreeList',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getList(data) {
  return request({
    url: 'build/safetyInspectionDetail/selectInspectionDetail',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectById(id) {
  return request({
    url: 'build/safetyInspectionDetail/selectById/'+id,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


