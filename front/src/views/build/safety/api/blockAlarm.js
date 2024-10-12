/**
 * zy 2021-06-10
 * 周界防护报警
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/safetyBlockAlarm/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增
export function saveBlockAlarm(data){
    return request({
        url: 'build/safetyBlockAlarm/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改
export function updateById(data){
    return request({
        url: 'build/safetyBlockAlarm/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//删除
export function delById(data){
    return request({
        url: 'build/safetyBlockAlarm/delById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//根据主键查询
export function selectById(data){
    return request({
        url: 'build/safetyBlockAlarm/selectById/'+data,
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
        url: 'build/safetyBlockAlarm/excelImport',
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
      })
}

