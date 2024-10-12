import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getQualCheckList(data) {
  return request({
    url: 'buildQualCheck/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 生成单据编号
export function generateDocumentNumber() {
  return request({
    url: 'buildQualCheck/generateDocumentNumber',
    method: 'post'
  })
}

// 保存质量检验数据
export function saveOrUpdate(data) {
  return request({
    url: 'buildQualCheck/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 回执质量检验数据
export function receiptInfo(data) {
  return request({
    url: 'buildQualCheck/receiptInfo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 根据id查询质量检查详情
export function getById(checkId) {
  return request({
    url: 'buildQualCheck/getById/'+checkId,
    method: 'post'
  })
}

// 删除接口
export function  delTrueByIds(data) {
  return request({
    url: 'buildQualCheck/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 整改通知单数据
 * @param {*} data
 *
 */
export function getPrintData(data) {
  return request({
    url: 'build/buildQualCheck/getPrintData/'+data,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 根据类型查询整改配置
 * @param {*} data
 *
 */
export function queryPrintTemplateByType(data) {
  return request({
    url: 'utilsPrint/queryPrintTemplateByType/'+data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
