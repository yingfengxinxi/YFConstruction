import request from '@/utils/request'


/**
 * 保存接口
 * @param {*} data
 *
 */
export function getById(data) {
  return request({
    url: 'build/evalApply/getById/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询工地等级列表
 * @param {*} data
 *
 */
export function selectLevelList(data) {
  return request({
    url: 'build/evalLevel/selectList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询打分项列表
 * @param {*} data
 *
 */
export function selectItemList(data) {
  return request({
    url: 'build/evalItems/selectList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 保持申请数据
 * @param {*} data
 *
 */
export function saveApplyInfo(data) {

  return request({
    url: 'build/evalApply/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 评分或暂存数据
 * @param {*} data
 *
 */
export function gradeOrHold(data) {

  return request({
    url: 'build/evalApply/gradeOrHold',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

