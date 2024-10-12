/**
 * zy 2021-05-27
 * 试卷配置规则详细信息
 */
import request from '@/utils/request'

//查询试卷配置列表
export function getPaperRulesDelList(data){
    return request({
        url: 'build/wifiTestPaperRulesDel/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增试卷配置
export function savePaperRulesDel(data){
    return request({
        url: 'build/wifiTestPaperRulesDel/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改试题
export function updatePaperRulesDelById(data){
    return request({
        url: 'build/wifiTestPaperRulesDel/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//删除试题
export function delPaperRulesDelById(data){
    return request({
        url: 'build/wifiTestPaperRulesDel/delById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//根据主键查询试题
export function selectPaperRulesDelById(data){
    return request({
        url: 'build/wifiTestPaperRulesDel/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}