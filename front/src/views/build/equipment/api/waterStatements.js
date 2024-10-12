import request from '@/utils/request'

/**
 * 列表
 * @param {*} data
 */
export function getList(data) {
  return request({
    url: 'build/equipmentWaterRecord/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 用户导入
 * @param {*} data
 */
export function excelImportAPI(data) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: 'build/equipmentWaterRecord/excelImport',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}