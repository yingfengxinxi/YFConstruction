/**
 * wnj 2021年5月28日
 */
import request from '@/utils/request'

/**
 * 查询试卷列表
 * @param {*} data 
 * @returns 
 */
export function getAllTestPaper(data) {
    return request({
        url: 'build/wifiTest/getAllTestPaper',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

/**
 * 查询试卷详情
 * @param {*} data 
 */
export function getTestPaperDetail(data) {
    return request({
        url: 'build/wifiTest/getPaperDetail/' + data,
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
/**
 * 获取工种列表
 * @param {*} data 
 * @returns 
 */
export function getWorkTypeList() {
    return request({
        url: 'build/labourWorktype/selcetIdsAndWorktype',
        method: 'post',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
