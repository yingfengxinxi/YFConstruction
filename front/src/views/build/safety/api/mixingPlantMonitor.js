import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
    return request({
        url: 'build/safetyMixPlant/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 保存或更新
 * @param {*} saveOrUpdate
 * @returns 
 */
export function saveOrUpdate(data) {
    return request({
        url: 'build/safetyMixPlant/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 根据id获取数据详情
 * @param {*} data 
 * @returns 
 */
export function getById(data) {
    return request({
        url: 'build/safetyMixPlant/getById/' + data,
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 数据删除（单条或多条）
 * @param {*} data 
 * @returns 
 */
export function delTrueByIds(data) {
    return request({
        url: 'build/safetyMixPlant/delTrueByIds',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 导入
 * @param {*} data 
 * @returns 
 */
export function excelImport(data) {
    var param = new FormData()
    Object.keys(data).forEach(key => {
        param.append(key, data[key])
    })

    return request({
        url: 'build/safetyMixPlant/excelImport',
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    })
}