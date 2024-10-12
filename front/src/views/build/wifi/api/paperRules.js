/**
 * zy 2021-05-27
 * 试卷配置规则
 */
import request from '@/utils/request'

//查询试卷配置列表
export function getPaperRulesList(data){
    return request({
        url: 'build/wifiTestPaperRules/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增试卷配置
export function savePaperRules(data){
    return request({
        url: 'build/wifiTestPaperRules/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改试题
export function updatePaperRulesById(data){
    return request({
        url: 'build/wifiTestPaperRules/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//删除试题
export function delPaperRulesById(data){
    return request({
        url: 'build/wifiTestPaperRules/delById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//根据主键查询试题
export function selectPaperRulesById(data){
    return request({
        url: 'build/wifiTestPaperRules/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}