import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/monitorPointAlert/selectPage',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 详情接口
 * @param {*} data
 *
 */
export function selectById(data) {
  return request({
    url: 'build/monitorPointAlert/selectById/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
/**
 * 处理详情数据提交
 * @param {*} data
 *
 */
export function handleSave(data) {
  return request({
    url: 'build/monitorPointAlert/handleSave',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 环境报警数据列表接口
 * @param {*} data
 *
 */
export function selectPageByEnv(data) {
  return request({
    url: 'build/monitorPointAlert/selectPageByEnv',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}