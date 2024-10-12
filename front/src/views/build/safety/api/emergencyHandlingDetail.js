import request from '@/utils/request'
/**
 * 应急事件处置--详细表
 * @param {*} data 
 */

/**
 * 列表接口--子表
 * @param {*} data
 *
 */
export function getListDetail(data) {
  let formD = {}
  formD.pid = data
  return request({
    url: 'buildSafetyEmergencyHandlingDetail/list',
    method: 'post',
    data: formD,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// /**
//  * 根据pid查询信息
//  */
// export function getDetailByPid(){
//   return request({
//     url: 'buildSafetyEmergencyHandlingDetail/queryById/'+data,
//     method: 'post',
//     headers: {
//       'Content-Type': 'application/json;charset=UTF-8'
//     }
//   })
// }
/**
 * 子表
 * 新增保存接口
 * @param {*} data
 *
 */
export function save(data) {
  return request({
    url: 'buildSafetyEmergencyHandlingDetail/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 子表
 * 修改接口
 * @param {*} data
 *
 */
export function updateDetail(data) {
  return request({
    url: 'buildSafetyEmergencyHandlingDetail/updateById',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 *子表
 * 根据主键查询接口
 * @param {*} data
 *
 */
export function getByIdDetail(data) {
  return request({
    url: 'buildSafetyEmergencyHandlingDetail/queryById/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 子表 删除接口
 * @param {*} data
 *
 */
export function  delTrueByIdsDetail(data) {
  return request({
    url: 'buildSafetyEmergencyHandlingDetail/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
