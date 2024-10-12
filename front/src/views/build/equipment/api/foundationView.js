import request from '@/utils/request'

/*
 查询深基坑
 */
export function getList(id) {
  return request({
    url: '/build/bEquipmentPro/dplist/'+id,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getlistByPro(id) {//实时数据+点位分布-工程
  return request({
    url: 'build/eqMonitorDataAt/listByPro/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getDayDataForPit(id) {//基坑沉降
  return request({
    url: 'build/eqMonDataDay/getDayDataForPit/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getCountBarByPro(id) {//一周预警报警数据统计
  return request({
    url: 'build/monitorPointAlert/getCountBarByPro/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getCountPieByPro(id) {//累计报警监测类型占比统计
  return request({
    url: 'build/monitorPointAlert/getCountPieByPro/'+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}