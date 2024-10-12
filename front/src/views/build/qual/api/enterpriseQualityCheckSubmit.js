import request from '@/utils/request'
/**
 * 企业质量检查填报
 * @param {*} data
 *
 */

/**
 * 查询列表接口
 * @param {*} data
 *
 */
export function getCheckList(data) {
    return request({
        url: 'build/qualEnterprisecheckfillMainGrp/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 数据新增
 * @param {*} data 
 * @returns 
 */
export function add(data) {
    return request({
        url: 'build/qualEnterprisecheckfillMainGrp/add',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 数据修改
 * @param {*} data 
 * @returns 
 */
export function edit(data) {
    return request({
        url: 'build/qualEnterprisecheckfillMainGrp/edit',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 单条数据删除
 * @param {*} data 
 * @returns 
 */
export function remove(data) {
    return request({
        url: 'build/qualEnterprisecheckfillMainGrp/remove/' + data,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 查询单条数据详情
 * @param {*} data 
 * @returns 
 */
export function getCheckSubById(data) {
    return request({
        url: 'build/qualEnterprisecheckfillMainGrp/selectById/' + data,
        method: 'get',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 项目信息列表查询
 * @param {*} query 
 * @returns 
 */
export function getProjectInfo(query) {
    return request({
        url: 'build/projectInfo/queryPageList',
        method: 'post',
        data: query,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}