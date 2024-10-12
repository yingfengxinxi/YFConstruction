import request from '@/utils/request'


/**
 * 列表接口
 * @param {*} data
 *
 */
export function getProjectsList() {
  return request({
    url: 'build/projectInfo/queryProjectList',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
