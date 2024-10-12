import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/aiBareSoilData/selectByPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function selectById(id) {
  return request({
    url: 'build/aiBareSoilData/selectById/' + id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取项目信息列表
 * @param {*} data
 */
export function queryProjectList(data) {
  return request({
    url: 'build/projectInfo/queryProjectList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


