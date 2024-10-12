import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/safetyRiskDetection/selectListByPro',
    method: 'post',
    data: data,
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
    url: 'build/safetyRiskDetection/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新接口
 * @param {*} data
 *
 */
export function updateInfo(data) {
  return request({
    url: 'build/safetyRiskDetection/updateInfo',
    method: 'post',
    data: data,
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
export function getById(data) {
  return request({
    url: 'build/safetyRiskDetection/getById/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除接口
 * @param {*} data
 *
 */
export function  delByIds(data) {
  return request({
    url: 'build/safetyRiskDetection/delete/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 隐患超期列表接口
 * @param {*} data
 *
 */
export function selectTimeoutList(data) {
  return request({
    url: 'build/safetyRiskDetection/selectTimeoutList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 安全隐患超期推送
 * @param {*} data
 *
 */
export function dangeTimeoutPush(id) {
  return request({
    url: 'build/safetyRiskDetection/dangeTimeoutPush/' + id,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取单据编号
 * @param {*} data
 *
 */
export function getNumber(data) {
  return request({
    url: 'build/safetyRiskDetection/generateDocumentNumber',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询静态风险点
 * @param {*} data
 *
 */
export function selectStaticRiskList(data) {
  return request({
    url: 'build/safetyStaticRisk/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询动态风险点
 * @param {*} data
 *
 */
export function selectDynamicRiskList(data) {
  return request({
    url: 'build/safetyDynamicRisk/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分包商下拉框
 * @param {*} data
 *
 */
export function getSubcontractorList(data) {
  return request({
    url: 'build/labourSubcontractor/selcetIdsAndName',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 分组长下拉框
 * @param {*} data
 *
 */
export function getTeamLeaderList(data) {
  return request({
    url: 'build/labourInfocollection/getTeamLeaderList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 项目列表接口
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

