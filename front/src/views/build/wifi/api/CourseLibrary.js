/**
 * wnj 2021-08-19
 * 课程库配置
 */
import request from '@/utils/request'

//查询列表
export function getLibraryList (data) {
    return request({
        url: 'build/wifiCourseLibrary/getLibraryList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function saveCourseVideo (data) {
    return request({
        url: 'build/wifiCourseLibrary/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//修改
export function updateCourseVideoById (data) {
    return request({
        url: 'build/wifiCourseLibrary/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function selectCourseVideoById (data) {
    return request({
        url: 'build/wifiCourseLibrary/selectById/' + data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//删除
export function delCourseVideoById (data) {
    return request({
        url: 'build/wifiCourseLibrary/delById/' + data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}