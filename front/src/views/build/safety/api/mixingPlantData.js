import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
    return request({
        url: 'build/safetyMixPlantData/list',
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