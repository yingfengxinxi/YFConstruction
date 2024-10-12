/**
 * wnj
 */

import request from '@/utils/request'

export function queryList (data) {
    return request({
        url: 'build/qualCheckPersonnel/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


export function queryById (data) {
    return request({
        url: 'build/qualCheckPersonnel/getById/' + data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


export function saveOrUpdate (data) {
    return request({
        url: 'build/qualCheckPersonnel/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


export function del (data) {
    return request({
        url: 'build/qualCheckPersonnel/delTrueByIds',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//查询id和name ---验收人员下拉框使用
export function getIdAndNameList () {
    return request({
        url: 'build/qualCheckPersonnel/getIdAndNameList',
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}