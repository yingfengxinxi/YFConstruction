import request from '@/utils/request'

export function queryList (data) {
    return request({
        url: 'build/qualHouseAcceptFillMain/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function queryById (data) {
    return request({
        url: 'build/qualHouseAcceptFillMain/getById/' + data,
        method: 'get',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function saveOrUpdate (menu) {
    return request({
        url: 'build/qualHouseAcceptFillMain/saveOrUpdate',
        method: 'post',
        data: menu,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//删除
export function del (ids) {
    return request({
        url: 'build/qualHouseAcceptFillMain/del',
        method: 'post',
        data: ids,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 查询楼座列表
 * @param {*} data
 * @returns
 */
export function buildList (data) {
    return request({
        url: 'build/qualBuildMange/queryByParentId/' + data,
        method: 'get',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 查询模板设置列表
 * @param {*} data
 * @returns
 */
export function TempLateSetList (data) {
    return request({
        url: 'build/qualAcceptTemplateMainSet/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 查询楼座树列表（不包括本级）
 * @param {*} data
 * @returns
 */
export function queryTreeList (data) {
    return request({
        url: 'build/qualBuildMange/queryTreeList/' + data,
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 获取户型设置的验收模板
 * @param {*} data
 * @returns
 */
export function getAcceptTemplateById (data) {
    return request({
        url: 'build/qualHouseTypeMange/getById/' + data,
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 获取户型设置的验收模板
 * @param {*} data
 * @returns
 */
export function getAcceptTemplateMainSetById (data) {
    return request({
        url: 'build/qualAcceptTemplateMainSet/getById/' + data,
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 获取所有房号
 * @param {*} data
 * @returns
 */
export function getAllHouseList (data) {
    return request({
        url: 'build/qualBuildMange/getAllHouseList',
        method: 'get',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


/**
 * 据户型id和验收工序查询模板信息--分户验收填报使用
 * @param {*} data
 * @returns
 */
export function getInfoByMap (data) {
    return request({
        url: 'build/qualHouseName/getInfoByMap',
        method: 'post',
        data:data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 获取户型列表
 * @param {*} data
 * @returns
 */
export function gethouseTemplateList(data) {
  return request({
    url: 'build/qualHouseTypeMange/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
