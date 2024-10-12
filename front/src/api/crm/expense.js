import request from '@/utils/request'

/**
 * 新建费用
 * @param {*} data
 */
export function crmExpenseSaveAPI(data) {
  const url = data.entity && data.entity.expenseId ? 'update' : 'add'
  return request({
    url: `crmExpense/${url}`,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 新建报销
 */
export function crmExpenseExamineSaveAPI(data) {
  return request({
    url: `crmExpenseExamines/save`,
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
export function crmExpenseIndexAPI(data) {
  return request({
    url: 'crmExpense/queryPageList',
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
export function crmExpenseDeleteAPI(data) {
  return request({
    url: 'crmExpense/deleteByIds',
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
export function crmExpenseReadAPI(data) {
  return request({
    url: `crmExpense/queryById/${data.id}`,
    method: 'post'
  })
}

/**
 * tabs的数据数量
 * @param {*} data
 *
 */
export function crmExpenseNumAPI(data) {
  return request({
    url: 'crmExpense/num',
    method: 'post',
    data: data
  })
}

/**
 * 费用附件列表
 * @param {*} data
 *
 */
export function crmExpenseFileListAPI(data) {
  return request({
    url: 'crmExpense/queryFileList',
    method: 'post',
    data: data
  })
}

/**
 * 费用导出
 * @param {*} data
 */
export function crmExpenseExcelExportAPI(data) {
  return request({
    url: 'crmExpense/batchExportExcel',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    responseType: 'blob',
    timeout: 60000
  })
}

export function crmExpenseExcelAllExportAPI(data) {
  return request({
    url: 'crmExpense/allExportExcel',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}



/**
 * 报销下费用
 * @param {*} data
 */
export function crmExaminesQueryExpenseAPI(data) {
  return request({
    url: 'crmExpense/queryExpense',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//费用审批

export function crmExpenseExaminesReadAPI(data) {
  return request({
    url: `crmExpenseExamines/queryById/${data.id}`,
    method: 'post'
  })
}

/**
 * tabs的数据数量
 * @param {*} data
 *
 */
export function crmExpenseExaminesNumAPI(data) {
  return request({
    url: 'crmExpenseExamines/num',
    method: 'post',
    data: data
  })
}

/**
 * 删除
 * @param {*} data
 */
export function crmExpenseExamineDeleteAPI(data) {
  return request({
    url: 'crmExpenseExamines/deleteById',
    method: 'post',
    data: data[0],
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 *crm
 * @param {*} data
 */
export function filedGetFieldExmaines(data) {
  return request({
    url: 'crmExpense/fieldExamine/' + data.id,
    method: 'post'
  })
}
