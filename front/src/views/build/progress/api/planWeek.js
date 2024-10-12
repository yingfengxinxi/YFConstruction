/**
 * zy 2021-07-06
 * 周计划
 */
import request from '@/utils/request'

//查询计划
export function selectPlan(data){
    return request({
      url: 'build/progresssPlanWeek/selectByMapNoPage',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }


//保存
export function savePlan(data) {
  return request({
    url: 'build/progresssPlanWeek/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}



//查询此模板最近生成的计划
export function selectByLast(data){
  return request({
    url: 'build/progresssPlanWeek/selectByLast/'+data,
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
      url: 'build/progresssPlanWeek/exportExcel',
      method: 'post',
      data: data,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      },
      responseType: 'blob'
    })
}





