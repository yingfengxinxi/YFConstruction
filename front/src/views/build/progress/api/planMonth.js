/**
 * zy 2021-06-28
 */
import request from '@/utils/request'

//查询计划
export function selectPlan(data){
    return request({
      url: 'build/progressPlanMonthDetails/selectByMapNoPage',
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
    url: 'build/progressPlanMonthDetails/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//保存单条信息
export function updateOne(data){
  return request({
    url: 'build/progressPlanMonthDetails/updateOne',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据主键查询单条信息
export function selectById(data){
  return request({
    url: 'build/progressPlanMonthDetails/selectById/'+data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}

//查询此模板最近生成的计划
export function selectByLast(data){
  return request({
    url: 'build/progressPlanMonthDetails/selectByLast/'+data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}

//查询月计划树结构数据 id 和 name
export function selectTreeMonth(data){
  return request({
    url: 'build/progressPlanMonthDetails/selectTreeMonth',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}

//查询月计划list数据 id 和 name
export function selectIdAndName(data){
  return request({
    url: 'build/progressPlanMonthDetails/selectIdAndName/'+data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}


//
export function planMonthIdAndNameFormmater(value,datas){

  if(datas == undefined || datas == null){
    return null
  }

  var actions = []
  if(value == null || value == undefined){
    return null
  }else{
    Object.keys(datas).some((key) => {
      if (datas[key].id == ('' + value)) {
      actions.push(datas[key].name)
      return true
      }
    })
    return actions.join('')
  }
  

}



//生成 周计划使用

//查询月计划最新的一条数据的mid
export function selectLastMidFormMonth(){
  return request({
    url: 'build/progressPlanMonthDetails/selectLastMidFormMonth',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据日期和mid，查询月计划数据生成周计划
export function selectByMapForWeekPlan(data){
  return request({
    url: 'build/progressPlanMonthDetails/selectByMapForWeekPlan',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}


//导出
export function exportExcel(data){
  return request({
      url: 'build/progressPlanMonthDetails/exportExcel',
      method: 'post',
      data: data,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      },
      responseType: 'blob'
    })
}


//根据mid 删除月计划
export function deleteByMid(data){
  return request({
    url: 'build/progressPlanMonthDetails/deleteByMid/'+data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}
