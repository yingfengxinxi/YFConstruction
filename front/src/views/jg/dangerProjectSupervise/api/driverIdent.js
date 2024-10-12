/**
 * zy 2021-07-26
 * 机械设备司机识别
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/equipmentDriverIdent/selectListNew',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}



//导入
export function excelImport(data){
    var param = new FormData()
    Object.keys(data).forEach(key => {
        param.append(key, data[key])
    })
   
    return request({
        url: 'build/equipmentDriverIdent/excelImport',
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
      })
}

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

