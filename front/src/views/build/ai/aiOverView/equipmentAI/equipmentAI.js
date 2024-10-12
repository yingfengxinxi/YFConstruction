import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList (data) {
    return request({
        url: 'build/equipmentFluoriteCloud/getAIEquipmentList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 
 * @param {获取AI设备信息} data 
 * @returns 
 */
export function getAssetList (data) {
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
 * 删除AI设备接口
 * @param {*} data
 *
 */
export function delTrueByIds (data) {
    return request({
        url: 'build/equipmentFluoriteCloud/deleteAIEquipment',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 项目列表接口
 * @param {*} data
 *
 */
export function getProjectsList () {
    return request({
        url: 'build/projectInfo/queryProjectList',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 
 * @returns 重新定义获取字典ajax，AI监测类型
 */
export function getDictList () {
    return request({
        url: '/adminDict/data/type/b_ai_event_type',
        method: 'get',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 
 * @param {保存AI设备信息} data
 * @returns 
 */
export function  saveOrUpdate(data){
    return request({
        url: '/build/equipmentFluoriteCloud/addAIEquipment',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 
 * @param {修改AI设备信息} data
 * @returns 
 */
export function getById (data) {
    return request({
        url: '/build/equipmentFluoriteCloud/getDetails/' + data,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
