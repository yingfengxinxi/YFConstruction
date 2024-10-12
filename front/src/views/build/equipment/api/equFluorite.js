/**
 * Create by yxk at 2021/09/26
 */
import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList (data) {
    return request({
        url: 'build/equipmentFluoriteCloud/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 
 * @param {获取项目列表} data 
 * @returns 
 */
export function getProjectList (data) {
    return request({
        url: 'build/projectInfo/queryProjectList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 
 * @param {获取详情} id 
 * @returns 
 */
export function getById (id) {
    return request({
        url: 'build/equipmentFluoriteCloud/getDetails/' + id,
        method: 'post',
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
export function saveOrUpdate (data) {
    return request({
        url: 'build/equipmentFluoriteCloud/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}