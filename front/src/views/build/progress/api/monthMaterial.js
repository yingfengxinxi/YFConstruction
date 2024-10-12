/**
 * zy 2021-06-30
 * 月计划--材料明细
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/progressPlanMonthMaterial/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增
export function saveInfo(data){
    return request({
        url: 'build/progressPlanMonthMaterial/save',
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
        url: 'build/progressPlanMonthMaterial/updateById',
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
        url: 'build/progressPlanMonthMaterial/delById/'+data,
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
        url: 'build/progressPlanMonthMaterial/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//导出
export function exportExcel(data){
    // return request({
    //     url: 'build/labourOddVisa/exportExcel',
    //     method: 'post',
    //     data: data,
    //     headers: {
    //         'Content-Type': 'application/json;charset=UTF-8'
    //     },
    //     responseType: 'blob'
    //   })
}