/**
 * zy 2021-06-01
 * 教育视频配置
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/wifiVideoWatchRecord/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增配置
export function saveVideoWatch(data){
    return request({
        url: 'build/wifiVideoWatchRecord/save',
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
        url: 'build/wifiVideoWatchRecord/updateById',
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
        url: 'build/wifiVideoWatchRecord/delById/'+data,
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
        url: 'build/wifiVideoWatchRecord/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}
export function queryById(data) {
    return request({
        url: 'adminFile/queryById/'+data,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
  }

