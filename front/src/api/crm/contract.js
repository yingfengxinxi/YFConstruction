import request from '@/utils/request'

/**
 * 新建合同
 * @param {*} data
 */
export function crmContractSaveAPI(data) {
  const url = data.entity && data.entity.contractId ? 'update' : 'add'
  return request({
    url: 'crmContract/' + url,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表
 * @param {*} data
 */
export function crmContractIndexAPI(data) {
  return request({
    url: 'crmContract/queryPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除
 * @param {*} data
 */
export function crmContractDeleteAPI(data) {
  return request({
    url: 'crmContract/deleteByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 详情
 * @param {*} data
 */
export function crmContractReadAPI(data) {
  return request({
    url: `crmContract/queryById/${data.contractId}`,
    method: 'post'
  })
}

/**
 * 合同相关产品
 * @param {*} data
 * contract_id
 */
export function crmContractProductAPI(data) {
  return request({
    url: 'crmContract/queryProductListByContractId',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 转移
 * @param {*} data
 */
export function crmContractTransferAPI(data) {
  return request({
    url: 'crmContract/changeOwnerUser',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 合同下回款
 * @param {*} data
 */
export function crmContractQueryReceivablesAPI(data) {
  return request({
    url: 'crmContract/qureyReceivablesListByContractId',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 合同下回款计划
 * @param {*} data
 */
export function crmContractQueryReceivablesPlanAPI(data) {
  return request({
    url: 'crmContract/queryReceivablesPlanListByContractId',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 团队操作
/**
 * 团队成员创建
 * @param {*} data
 */
export function crmContractSettingTeamSaveAPI(data) {
  return request({
    url: 'crmContract/addMembers',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function crmContractSettingTeamDeleteAPI(data) {
  return request({
    url: 'crmContract/deleteMembers',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function crmContractTeamMembersAPI(data) {
  return request({
    url: `crmContract/getMembers/${data.id}`,
    method: 'post'
  })
}

export function crmContractUpdateMembersAPI(data) {
  return request({
    url: 'crmContract/updateMembers',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 退出团队
 * @param {*} data
 */
export function crmContractExitTeamAPI(data) {
  return request({
    url: `crmContract/exitTeam/${data.id}`,
    method: 'post'
  })
}

/**
 * 新建回款查询回款计划
 * @param {*} data
 */
export function crmQueryReceivablesPlansByContractIdAPI(data) {
  return request({
    url: 'crmContract/queryReceivablesPlansByContractId',
    method: 'post',
    data: data
  })
}

/**
 * 作废
 * @param {*} data
 */
export function crmContractCancelAPI(data) {
  return request({
    url: 'crmContract/contractDiscard',
    method: 'post',
    data: data
  })
}

/**
 * 附件列表
 * @param {*} data
 *
 */
export function crmContractFileListAPI(data) {
  return request({
    url: 'crmContract/queryFileList',
    method: 'post',
    data: data
  })
}

/**
 * tab数量
 * @param {*} data
 *
 */
export function crmContractNumAPI(data) {
  return request({
    url: 'crmContract/num',
    method: 'post',
    data: data
  })
}

/**
 * 合同导出
 * @param {*} data
 */
export function crmContractExcelExportAPI(data) {
  return request({
    url: 'crmContract/batchExportExcel',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    responseType: 'blob',
    timeout: 60000
  })
}

export function crmContractExcelAllExportAPI(data) {
  return request({
    url: 'crmContract/allExportExcel',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 回访
 * @param {*} data
 */
export function crmContractQueryVisitAPI(data) {
  return request({
    url: 'crmContract/queryReturnVisit',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 合同下费用
 * @param {*} data
 */
export function crmContractQueryMoneyAPI(data) {
  return request({
    url: 'crmContract/queryMoney',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
