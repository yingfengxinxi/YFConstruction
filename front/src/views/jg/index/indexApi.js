import request from '@/utils/request'

//环境监测点数据
export function envMonitPont (data) {
    return request({
        url: 'build/homePage/envMonitPont',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//设备分类数据
export function equipmentNum (data) {
    return request({
        url: 'build/homePage/equipmentNum',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//总报警数（环境预警项目）
export function alarmsNum1 (data) {
    return request({
        url: 'build/homePage/alarmsNum/1',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//报警项目统计（严重超标项目）
export function alarmsNum2 (data) {
    return request({
        url: 'build/homePage/alarmsNum/2',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//报警项目统计（接入项目）
export function projectNum (data) {
    return request({
        url: 'build/homePage/projectNum',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//预警分类
export function earlyWarning (data) {
    return request({
        url: 'build/homePage/earlyWarning',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//按项目分类统计报警数
export function projectAlarmNum (data) {
    return request({
        url: 'build/homePage/projectAlarmNum',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//噪音监控情况(报警趋势)
export function monitSituationNoise (data) {
    return request({
        url: 'build/homePage/monitSituationNoise',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
//PM2.5、PM10监控情况（环境检测）
export function monitSituationPM (data) {
    return request({
        url: 'build/homePage/monitSituationPM',
        method: 'post',
        data: {},
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function selectProjectStateCount (data) {
  return request({
    url: '/build/homePage/selectProjectStateCount',
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//地图标注点
export function getMAP (data) {
    return request({
        url: '/build/homePage/projectInfoListByProjectState',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}


export function getEquiStateList (data) {
    return request({
        url: '/build/equipmentAssets/equiStateList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
