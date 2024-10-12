/**
 * wnj 2021年6月22日
 */
import request from '@/utils/request'

export function queryByParentId(data) {
    return request({
        url: 'build/qualBuildMange/queryByParentId/' + data,
        method: 'get',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function queryById(data) {
    return request({
        url: 'build/qualBuildMange/getById/' + data,
        method: 'get',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function SaveRoootMenu(menu) {
    return request({
        url: '/build/qualBuildMange/Save',
        method: 'post',
        data: menu,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function CopyMenu(menu) {
    return request({
        url: '/build/qualBuildMange/CopyMenu',
        method: 'post',
        data: menu,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//保存菜单
export function saveMenu(menu) {
    return request({
        url: '/build/qualBuildMange/saveOrUpdate',
        method: 'post',
        data: menu,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//保存菜单
export function updateMenu(menu) {
    return request({
        url: 'build/qualBuildMange/saveOrUpdate',
        method: 'post',
        data: menu,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//删除
export function deleteMenu(menuId) {
    return request({
        url: 'build/qualBuildMange/del/' + menuId,
        method: 'get',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function queryQuestionsTreeList(data) {
    return request({
        url: 'build/qualBuildMange/queryTreeList/' + data,
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function queryQuestionsTreeList2 (data) {
    return request({
        url: 'build/qualBuildMange/queryTreeAllList/' + data,
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function SaveDistrib (data) {
    return request({
        url: 'build/qualBuildMange/SaveDistrib',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


/**
 * 用户导入
 * @param {*} data
 */
export function excelImportAPI(data) {
    var param = new FormData()
    Object.keys(data).forEach(key => {
        param.append(key, data[key])
    })
    return request({
        url: 'build/qualQuestions/excelImport',
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        timeout: 60000
    })
}