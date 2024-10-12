/**
 * zy 2021-05-26
 */
import request from '@/utils/request'

//根据试题id查询试题选项列表
export function getQuestOptionList(data){
    return request({
        url: 'build/wifiQuestOption/getQuestOptionList/' + data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增试题
export function saveQuestOption(data){
    return request({
        url: 'build/wifiQuestOption/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改试题
export function updateQuestOptionById(data){
    return request({
        url: 'build/wifiQuestOption/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//删除试题
export function delQuestOptionById(data){
    return request({
        url: 'build/wifiQuestOption/delById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//根据主键查询试题
export function selectQuestOptionById(data){
    return request({
        url: 'build/wifiQuestOption/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}