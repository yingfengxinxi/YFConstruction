import request from '@/utils/request'
import crmTypeModel from '@/views/crm/model/crmTypeModel'

/**
 *crm 自定义字段的添加
 * @param {*} data
 */
export function filedGetFieldAPI(data) {
  const label = data.label
  let url
  if (label === 10) {
    url = '/oaExamineField/queryField/' + data.id
  } else {
    url = crmTypeModel.labelToType[label] + '/field/' + (data.id ? data.id : '')
  }
  return request({
    url: url,
    method: 'post'
  })
}


/**
 * 详情页基本信息
 * data 会包含poolId 需保留
 * @param {*} data
 */
export function filedGetInformationAPI(data) {
  return request({
    url: crmTypeModel.labelToType[data.types] + '/information/' + data.id,
    method: 'post',
    data
  })
}


/**
 * 表头
 * @param {*} data
 */
export function filedGetTableFieldAPI(data) {
  return request({
    url: 'crmField/queryListHead/' + data.label,
    method: 'post',
    data: data
  })
}


/**
 * 基本信息编辑
 * @param {*} data
 */
//TODO 详情保存扩展
export function filedUpdateTableFieldAPI(data) {
  const url = {
    1: 'crmLeads',
    2: 'crmCustomer',
    3: 'crmContacts',
    4: 'crmProduct',
    5: 'crmBusiness',
    6: 'crmContract',
    7: 'crmReceivables',
    17: 'crmReturnVisit',
    50: 'crmExpense'
  }[data.label]
  return request({
    url: `${url}/updateInformation`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 公海表头
 * @param {*} data
 */
export function filedGetPoolTableFieldAPI(data) {
  return request({
    url: 'crmCustomerPool/queryPoolListHead',
    method: 'post',
    data: data
  })
}

/**
 * 自定义字段验重
 * @param {*} data
 */
export function filedValidatesAPI(data) {
  return request({
    url: 'crmField/verify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 自定义字段(高级筛选)
 * @param {*} data
 */
export function filterIndexfieldsAPI(data) {
  return request({
    url: 'crmScene/queryField',
    method: 'post',
    data: data
  })
}

/**
 * 公海自定义字段(高级筛选)
 * @param {*} data
 */
export function filterIndexPoolfieldsAPI(data) {
  return request({
    url: 'crmCustomerPool/queryPoolField',
    method: 'post',
    data: data
  })
}

/**
 * 场景列表
 * @param {*} data
 */
export function crmSceneIndexAPI(data) {
  //console.log(data)
  return request({
    url: 'crmScene/queryScene',
    method: 'post',
    data: data
  })
}

/**
 * 场景设置列表
 * @param {*} data
 */
export function crmSceneSetIndexAPI(data) {
  return request({
    url: 'crmScene/querySceneConfig',
    method: 'post',
    data: data
  })
}

/**
 * 场景创建
 * @param {*} data
 */
export function crmSceneSaveAPI(data) {
  return request({
    url: 'crmScene/addScene',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 场景编辑
 * @param {*} data
 */
export function crmSceneUpdateAPI(data) {
  return request({
    url: 'crmScene/updateScene',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 场景默认
 * @param {*} data
 */
export function crmSceneDefaultsAPI(data) {
  return request({
    url: 'crmScene/setDefaultScene',
    method: 'post',
    data: data
  })
}

/**
 * 场景删除
 * @param {*} data
 */
export function crmSceneDeleteAPI(data) {
  return request({
    url: 'crmScene/deleteScene',
    method: 'post',
    data: data
  })
}

/**
 * 场景排序
 * @param {*} data
 */
export function crmSceneSortAPI(data) {
  return request({
    url: 'crmScene/sceneConfig',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表字段排序数据
 * @param {*} data
 */
export function crmFieldConfigAPIIndexAPI(data) {
  return request({
    url: 'crmField/queryFieldConfig',
    method: 'post',
    data: data
  })
}

/**
 * 公海列表字段排序数据
 * @param {*} data
 */
export function crmPoolFieldConfigIndexAPI(data) {
  return request({
    url: 'crmCustomerPool/queryPoolFieldConfig',
    method: 'post',
    data: data
  })
}

/**
 * 列表排序编辑
 * @param {*} data
 */
export function crmFieldConfigAPI(data) {
  return request({
    url: 'crmField/setFieldConfig',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 公海列表排序编辑
 * @param {*} data
 */
export function crmPoolFieldConfigAPI(data) {
  return request({
    url: 'crmCustomerPool/poolFieldConfig',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表宽度设置
 * @param {*} data
 */
export function crmFieldColumnWidthAPI(data) {
  return request({
    url: 'crmField/setFieldStyle',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 公海 列表宽度设置
 * @param {*} data
 */
export function crmPoolFieldColumnWidthAPI(data) {
  return request({
    url: 'crmField/setPoolFieldStyle',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 操作记录
 * @param {*} data
 */
export function crmIndexFieldRecordAPI(data) {
  return request({
    url: 'crmActionRecord/queryRecordList',
    method: 'post',
    data: data
  })
}

/**
 * 客户管理下 合同回款的待审批
 * @param {*} data
 */
export function crmExamineMyExamineAPI(data) {
  return request({
    url: 'crmExamine/myExamine',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询活动分页列表
 * @param {*} data
 */
export function crmActivityListAPI(data) {
  return request({
    url: 'crmActivity/getCrmActivityPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除跟进记录
 * @param {*} data
 */
export function crmActivityDeleteAPI(data) {
  return request({
    url: `crmActivity/deleteCrmActivityRecord/${data.activityId}`,
    method: 'post'
  })
}

/**
 * 删除外勤签到
 * @param {*} data
 */
export function crmActivityOutworkSignDeleteAPI(data) {
  return request({
    url: 'crmActivity/deleteOutworkSign',
    method: 'post',
    data: data
  })
}

/**
 * 添加跟进记录
 * @param {*} data
 */
export function crmActivityAddAPI(data) {
  return request({
    url: 'crmActivity/addCrmActivityRecord',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 编辑跟进记录
 * @param {*} data
 */
export function crmActivityUpdateAPI(data) {
  return request({
    url: 'crmActivity/updateActivityRecord',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查看当前导入数量
 * @param {*} data
 */
export function crmQueryImportNumAPI(data) {
  return request({
    url: 'adminMessage/queryImportNum',
    method: 'post',
    data: data
  })
}

/**
 * 查询导入结果
 * @param {*} data
 */
export function crmQueryImportInfoAPI(data) {
  return request({
    url: 'adminMessage/queryImportInfo',
    method: 'post',
    data: data
  })
}

/**
 * 查看错误数据接口
 * @param {*} data
 */
export function crmDownImportErrorAPI(data) {
  return request({
    url: 'adminMessage/downImportError',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

/**
 * 查询跟进记录常用语
 * @param {*} data
 */
export function sysConfigQueryPhraseAPI(data) {
  return request({
    url: 'adminConfig/queryActivityPhrase',
    method: 'post',
    data: data
  })
}

/**
 * 设置跟进记录常用语
 * @param {*} data
 */
export function sysConfigSetPhraseAPI(data) {
  return request({
    url: 'adminConfig/setActivityPhrase',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
