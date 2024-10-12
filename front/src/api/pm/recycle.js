import request from '@/utils/request'

/**
 * 回收站列表
 * @param {*} data
 */
export function workTrashIndexAPI(data) {
  return request({
    url: 'workTask/queryTrashList',
    method: 'post',
    data: data
  })
}

/**
 * 回收站彻底删除
 * @param {*} data
 */
export function workTrashDeleteAPI(taskId) {
  return request({
    url: `workTask/deleteTask/${taskId}`,
    method: 'post'
  })
}

/**
 * 回收站恢复
 * @param {*} data
 */
export function workTrashRecoverAPI(taskId) {
  return request({
    url: `workTask/restore/${taskId}`,
    method: 'post'
  })
}



