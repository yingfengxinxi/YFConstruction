/**
 * wnj
 */
import request from '@/utils/request'

/**
 * 列表
 * @param {*} data 
 * @returns 
 */
export function getList(data) {
    return request({
        url: 'build/safetyRoomInfoMaintain/list' ,
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
        url: 'build/safetyRoomInfoMaintain/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function getById(data) {
    return request({
        url: 'build/safetyRoomInfoMaintain/getById/'+data,
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


export function delTrueByIds(data) {
    return request({
        url: 'build/safetyRoomInfoMaintain/delTrueByIds',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}