/**
 * zy 2021-07-16
 * 周计划每日填报
 */
import request from '@/utils/request'

//查询计划
export function selectByMap(data){
    return request({
      url: 'build/progressPlanDayFillin/selectByMap',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }


//保存或者修改
export function saveDayFillin(data) {
  return request({
    url: 'build/progressPlanDayFillin/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

