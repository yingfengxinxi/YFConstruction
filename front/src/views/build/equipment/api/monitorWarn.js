import request from '@/utils/request'

/**
 * 根据typeID获取配置接口
 * @param {*} data
 *
 */
export function getByType(data) {
  return request({
    url: 'build/equipmentMonitorWarn/getByType/'+data,
    method: 'get',
    data: "",
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 保存接口
 * @param {*} data
 *
 */
export function save(data) {
  return request({
    url: 'build/equipmentMonitorWarn/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
