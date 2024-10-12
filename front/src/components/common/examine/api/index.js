/**
 * Create by yxk at 2020/6/22
 */
import request from '@/utils/request'

/**
 * 查询所有未删除审批流程
 * @param {*} data
 */
export function examineQueryAllAPI(data) {
  return request({
    url: 'wfExamine/queryAllExamine',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 查询当前启用审核流程步骤
 */
export function examineStepAPI(data) {
  return request({
    url: 'wfExamine/queryExamineStep',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 添加审批流程
 */
export function examineSaveAPI(data) {
  return request({
    url: 'wfExamine/saveExamine',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 停用或删除审批流程
 */
export function examineUpdateStatusAPI(data) {
  return request({
    url: 'wfExamine/updateStatus',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 公用审批列表
 * @param {*} data
 */
export function examineMyExamineAPI(data) {
  return request({
    url: 'wfExamine/myExamine',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 进行审批 AuditExamine
 */
export function examineRecordAuditAPI(data) {
  return request({
    url: 'wfExamineRecord/auditExamine',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询审批记录
 */
export function examineRecordLogListAPI(data) {
  return request({
    url: 'wfExamineRecord/queryExamineLogList',
    method: 'post',
    data: data
  })
}

/**
 * 查询审批流程列表
 */
export function examineRecordRecordListAPI(data) {
  return request({
    url: 'wfExamineRecord/queryExamineRecordList',
    method: 'post',
    data: data
  })
}
