/**
 * zy 2021-05-26
 */
import request from '@/utils/request'

//查询试题列表
export function getQuestionList(data){
    return request({
        url: 'build/wifiQuestions/getQuestionList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增试题
export function saveQuestion(data){
    return request({
        url: 'build/wifiQuestions/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改试题
export function updateQuestionById(data){
    return request({
        url: 'build/wifiQuestions/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//删除试题
export function delQuestionById(data){
    return request({
        url: 'build/wifiQuestions/delById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//根据主键查询试题
export function selectQuestionById(data){
    return request({
        url: 'build/wifiQuestions/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//导入
export function excelImport(data){
    var param = new FormData()
    Object.keys(data).forEach(key => {
        param.append(key, data[key])
    })
   
    return request({
        url: 'build/wifiQuestions/excelImport',
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
      })
}

