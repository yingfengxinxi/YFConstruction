import request from '@/utils/request'

/**
 * 创优评优申报列表接口
 * @param {*} data
 *
 */
export function getAppraisingDeclareList(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 添加创优评优申报数据
 * @param {*} data
 *
 */
export function add(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/add',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 修改创优评优申报数据
 * @param {*} data
 *
 */
export function edit(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/edit',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
export function saveOrUpdate(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/saveOrUpdate',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 删除创优评优申报数据
 * @param {*} data
 *
 */
export function remove(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/remove/'+data,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 创优评优申报数据详情
 * @param {*} data
 *
 */
export function getAppraisingDeclareById(data) {
    return request({
        url: 'build/qualAppraisingdeclareMainGrp/selectById/' + data,
        method: 'get',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
    * 模板自定义格式化列表
    */
export function CustomFormat(datas, value) {
    var actions = [];
    Object.keys(datas).some(key => {
        if (datas[key].id == "" + value) {
            actions.push(datas[key].realname);
            return true;
        }
    });
    return actions.join("");
}

/**
 * 项目信息列表查询
 * @param {*} query 
 * @returns 
 */
export function getProjectInfo(query) {
    return request({
        url: 'build/projectInfo/queryPageList',
        method: 'post',
        data: query,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


/**
 * 项目详细信息查询
 * @param {*} query 
 * @returns 
 */
export function getProjectInfoById(query) {
    return request({
        url: 'build/projectInfo/getProjectInfoById/' + query,
        method: 'post',
        data: query,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}