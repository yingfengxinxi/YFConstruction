/**
 * zy 2021-05-06
 */
import request from '@/utils/request'

//查询计划
export function selectPlan(data){
    return request({
      url: 'build/progressPlanManageDetails/selectByMapNoPage',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }

  //查询楼座信息，最高层，及日期 
  export function selectLoft(){
    return request({
      url: 'build/singleBuildingInfo/selectLoft',
      method: 'post',
      data: '',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }

//保存
export function savePlan(data) {
  return request({
    url: 'build/progressPlanManageDetails/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//查询最近生成的计划信息，显示列表及模板
export function selectPlanLastTime(){
  return request({
    url: 'build/progressPlanManageDetails/selectPlanLastTime',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//生成  月计划
export function selectByMapForMonthPlan(data){
  return request({
    url: 'build/progressPlanManageDetails/selectByMapForMonthPlan',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//查询 最近的生成计划的 计划时间的月份范围及mid
export function getMaxMinFormLastPlan(){
  return request({
    url: 'build/progressPlanManageDetails/getMaxMinFormLastPlan',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}