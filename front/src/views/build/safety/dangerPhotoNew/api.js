import request from '@/utils/request'

export function getList(data) {
  return request({
    url: 'build/safetyRiskDetection/selectPhotoList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectById(id) {
  return request({
    url: 'build/safetyRiskDetection/getById/'+id,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


