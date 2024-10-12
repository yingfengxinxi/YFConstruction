import request from '@/utils/request'
/**
 * 户型管理
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
        url: 'build/qualHouseTypeMange/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function saveOrUpdate(data) {
    return request({
        url: 'build/qualHouseTypeMange/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function getById(data) {
    return request({
        url: 'build/qualHouseTypeMange/getById/' + data,
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function getDetailsByMainId(data) {
    return request({
        url: 'build/qualHouseName/getById/' + data,
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function remove(data) {
    return request({
        url: 'build/qualHouseTypeMange/del/' + data,
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function CopyHouseType (data) {
    return request({
        url: 'build/qualHouseTypeMange/CopyHouseType',
        method: 'post',
        data:data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
