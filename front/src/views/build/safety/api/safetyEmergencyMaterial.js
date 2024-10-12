import request from '@/utils/request'


/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
    return request({
        url: 'build/safetyEmergencyMaterial/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 根据id查询详情
 * @param {*} data 
 */
export function getById(data) {
    return request({
        url: 'build/safetyEmergencyMaterial/selectById/' + data,
        method: 'get',
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
export function add(data) {
    return request({
        url: 'build/safetyEmergencyMaterial/add',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 编辑接口
 * @param {*} data
 *
 */
export function edit(data) {
    return request({
        url: 'build/safetyEmergencyMaterial/edit',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 单数据删除接口
 * @param {*} data
 *
 */
export function remove(data) {
    return request({
        url: 'build/safetyEmergencyMaterial/delete/' + data,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
