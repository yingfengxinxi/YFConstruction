import request from '@/utils/request'

export function businessGroupListAPI(data) {
  return request({
    url: 'crmBusinessType/queryBusinessTypeList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function businessGroupAddAPI(data) {
  return request({
    url: 'crmBusinessType/setBusinessType',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}

/**
 * 商机状态组状态修改
 * @param {*} data
 */
export function businessGroupUpdateStatusAPI(data) {
  return request({
    url: 'crmBusinessType/updateStatus',
    method: 'post',
    data: data
  })
}

/**
 * 商机状态组详情
 * @param {*} data
 */
export function businessGroupReadAPI(data) {
  return request({
    url: 'crmBusinessType/getBusinessType',
    method: 'post',
    data: data
  })
}

/**
 * 商机状态组编辑
 * @param {*} data
 */
export function businessGroupUpdateAPI(data) {
  return request({
    url: 'crmBusinessType/setBusinessType',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}

/**
 * 商机状态组删除
 * @param {*} data
 */
export function businessGroupDeleteAPI(data) {
  return request({
    url: 'crmBusinessType/deleteById',
    method: 'post',
    data: data
  })
}

/**
 * 自定义字段（字段数据）的添加编辑操作
 * @param {*} data
 */
export function customFieldHandleAPI(data) {
  return request({
    url: 'crmField/saveField',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 自定义字段（字段数据）的详情
 * @param {*} data
 */
export function customFieldListAPI(data) {
  return request({
    url: `crmField/list/${data.label}`,
    method: 'post'
  })
}

/**
 * 办公审批的自定义字段
 * @param {*} data
 */
export function oaExamineFieldListAPI(data) {
  return request({
    url: `oaExamineField/queryField/${data.categoryId}`,
    method: 'post'
  })
}

/**
 * 办公审批自定义字段保存
 * @param {*} data
 */
export function oaFieldHandleAPI(data) {
  return request({
    url: 'oaExamineField/saveField',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 自定义字段（字段数据）的列表更新时间
 * @param {*} data
 */
export function customFieldIndexAPI(data) {
  return request({
    url: 'crmField/queryFields',
    method: 'post',
    data: data
  })
}

/**
 * 产品类别 数据获取
 * @param {*} data
 */
export function productCategoryIndexAPI(data) {
  return request({
    url: 'crmProductCategory/queryList',
    method: 'post',
    data: data
  })
}

/**
 * 产品分类添加
 * @param {*} data
 */
export function productCategorySaveAPI(data) {
  const url = data.categoryId ? 'update' : 'save'
  return request({
    url: 'crmProductCategory/' + url,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 产品分类删除
 * @param {*} data
 */
export function productCategoryDeleteAPI(data) {
  return request({
    url: `crmProductCategory/deleteById/${data.id}`,
    method: 'post'
  })
}

/**
 * 客户保护规则
 * @param {*} data
 */
export function crmSettingConfigAPI(data) {
  return request({
    url: 'crmCustomer/updateRulesSetting',
    method: 'post',
    data: data
  })
}

/**
 * 客户保护规则
 * @param {*} data
 */
export function crmSettingConfigDataAPI(data) {
  return request({
    url: 'crmCustomer/getRulesSetting',
    method: 'post',
    data: data
  })
}

/**
 * 部门业绩目标列表
 * @param {*} data
 */
export function crmAchievementIndex(data) {
  return request({
    url: 'biAchievement/queryAchievementList',
    method: 'post',
    data: data
  })
}

/**
 * 业绩目标编辑接口
 * @param {*} data
 */
export function crmAchievementUpdate(data) {
  return request({
    url: 'biAchievement/setAchievement',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}


/**
 * 新增业绩目标
 * @param {*} data
 */
export function crmAchievementAdd(data) {
  return request({
    url: 'biAchievement/addAchievement',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}


/**
 * 删除业绩目标
 * @param {*} data
 */
export function crmAchievementDelete(data) {
  return request({
    url: 'biAchievement/deleteAchievement',
    method: 'post',
    data: data
  })
}

/**
 * 记录类型 数据
 * @param {*} data
 */
export function crmSettingRecordListAPI(data) {
  return request({
    url: 'crmActionRecord/queryRecordOptions',
    method: 'post',
    data: data
  })
}

/**
 * 记录类型 编辑
 * @param {*} data
 */
export function crmSettingRecordEditAPI(data) {
  return request({
    url: 'adminConfig/setRecordOptions',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 报销费用类型 数据
 * @param {*} data
 */
export function crmSettingExamineExpenseListAPI(data) {
  return request({
    url: 'adminConfig/queryExamineExpenseType',
    method: 'post',
    data: data
  })
}

/**
 * 报销费用类型 编辑
 * @param {*} data
 */
export function crmSettingExamineExpenseEditAPI(data) {
  return request({
    url: 'adminConfig/setExamineExpenseType',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 合同到期提醒 设置
 * @param {*} data
 */
export function crmSettingContractDayAPI(data) {
  return request({
    url: 'crmContract/setContractConfig',
    method: 'post',
    data: data
  })
}

/**
 * 拥有、锁定客户数限制
 * @param {*} data
 */
export function crmSettingCustomerConfigListAPI(data) {
  return request({
    url: 'crmCustomer/queryCustomerSetting',
    method: 'post',
    data: data
  })
}

/**
 * 拥有、锁定客户数限制 - 创建 编辑
 * @param {*} data
 */
export function crmSettingCustomerConfigSetAPI(data) {
  return request({
    url: 'crmCustomer/customerSetting',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}

/**
 * 拥有、锁定客户数限制 - 删除
 * @param {*} data
 */
export function crmSettingCustomerConfigDelAPI(data) {
  return request({
    url: 'crmCustomer/deleteCustomerSetting',
    method: 'post',
    data: data
  })
}

/**
 * 客户回访查询
 * @param {*} data
 */
export function returnVisitConfigQueryAPI(data) {
  return request({
    url: 'crmReturnVisit/queryReturnVisitRemindConfig',
    method: 'post',
    data: data
  })
}

/**
 * 客户回访设置
 * @param {*} data
 */
export function returnVisitConfigSetAPI(data) {
  return request({
    url: 'crmReturnVisit/updateReturnVisitRemindConfig',
    method: 'post',
    data: data
  })
}

/**
 * 编号查询
 * @param {*} data
 */
export function sysConfigNumberQueryAPI(data) {
  return request({
    url: 'crmNumberSetting/queryNumberSetting',
    method: 'post',
    data: data
  })
}

/**
 * 编号设置
 * @param {*} data
 */
export function sysConfigNumberSetAPI(data) {
  return request({
    url: 'crmNumberSetting/setNumberSetting',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}

/** ****************自定义模板***********************/

/**
 * 查询 打印模板列表
 * @param {*} data
 */
export function printTemplateListAPI(data) {
  return request({
    url: 'crmPrint/queryPrintTemplateList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询指定打印模板
 * templateId
 * @param {*} data
 */
export function printTemplateByIdAPI(data) {
  return request({
    url: 'crmPrint/queryPrintTemplateById',
    method: 'post',
    data: data
  })
}

/**
 * 添加模板 打印模板
 * templateName 模板名称
 * type 关联对象
 * content模板
 * @param {*} data
 */
export function printAddTemplateAPI(data) {
  return request({
    url: 'crmPrint/addPrintTemplate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 编辑模板 打印模板
 * templateName 模板名称
 * type 关联对象
 * content模板
 * @param {*} data
 */
export function printUpdateTemplateAPI(data) {
  return request({
    url: 'crmPrint/updatePrintTemplate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除指定打印模板
 * templateId
 * @param {*} data
 */
export function printDeleteTemplateAPI(data) {
  return request({
    url: 'crmPrint/deletePrintTemplate',
    method: 'post',
    data: data
  })
}

/**
 * 模块对应字段查询
 * type
 * @param {*} data
 */
export function printQueryFieldsAPI(data) {
  return request({
    url: 'crmPrint/queryFields',
    method: 'post',
    data: data
  })
}

/**
 * 模板校准为内容
 * type
 * @param {*} data
 */
export function printPrintAPI(data) {
  return request({
    url: 'crmPrint/print',
    method: 'post',
    data: data
  })
}

/**
 * 模板复制
 * type
 * @param {*} data
 */
export function printCopyTemplateAPI(data) {
  return request({
    url: 'crmPrint/copyTemplate',
    method: 'post',
    data: data
  })
}

/**
 * 保存打印记录
 * type
 * @param {*} data
 */
export function printSaveRecordAPI(data) {
  return request({
    url: 'crmPrint/savePrintRecord',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询打印记录
 * type
 * @param {*} data
 */
export function printQueryPrintRecordAPI(data) {
  return request({
    url: 'crmPrint/queryPrintRecord',
    method: 'post',
    data: data
  })
}

/**
 * 打印记录详情
 * type
 * @param {*} data
 */
export function printRecordDetailAPI(data) {
  return request({
    url: 'crmPrint/queryPrintRecordById',
    method: 'post',
    data: data
  })
}

/**
 * 打印预览
 * type
 * @param {*} data
 */
export function printPreviewAPI(data) {
  return request({
    url: 'crmPrint/preview',
    method: 'post',
    data: data
  })
}
