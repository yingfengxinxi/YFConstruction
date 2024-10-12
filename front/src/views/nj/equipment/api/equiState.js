/*
 * @Author: wnj 
 * @Date: 2021-11-17 10:04:47 
 * @Last Modified by: wnj
 * @Last Modified time: 2021-11-17 10:25:37
 */
import request from '@/utils/request'

export function getEquiStateList (data) {
    return request({
        url: '/build/equipmentAssets/equiStateList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function getProjectsList () {
    return request({
        url: 'build/projectInfo/querySalesOfficeProjectList',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
