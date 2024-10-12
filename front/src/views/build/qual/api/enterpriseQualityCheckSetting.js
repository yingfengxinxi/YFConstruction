import request from '@/utils/request'
/**
 * 企业质量检查设置
 * @param {*} data
 *
 */

/**
 * 查询列表接口
 * @param {*} data
 *
 */
export function getList(data) {
    return request({
        url: 'build/qualEnterprisecheckMainGrp/list',
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
        url: 'build/qualEnterprisecheckMainGrp/add',
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
        url: 'build/qualEnterprisecheckMainGrp/edit',
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
        url: 'build/qualEnterprisecheckMainGrp/delete/'+data,
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
export function getById(data) {
    return request({
        url: 'build/qualEnterprisecheckMainGrp/selectById/'+data,
        method: 'get',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}