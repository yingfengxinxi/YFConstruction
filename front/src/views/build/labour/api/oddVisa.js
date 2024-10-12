/**
 * zy 2021-06-08
 * 教育视频配置
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/labourOddVisa/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增配置
export function saveOddVisa(data){
    return request({
        url: 'build/labourOddVisa/save',
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
        url: 'build/labourOddVisa/updateById',
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
        url: 'build/labourOddVisa/delById/'+data,
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
        url: 'build/labourOddVisa/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//导出
export function exportExcel(data){
    return request({
        url: 'build/labourOddVisa/exportExcel',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        responseType: 'blob'
      })
}

