/**
 * zy 2021-06-01
 * 教育视频配置
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/wifiVideo/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增配置
export function saveVideoSet(data){
    return request({
        url: 'build/wifiVideo/save',
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
        url: 'build/wifiVideo/updateById',
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
        url: 'build/wifiVideo/delById/'+data,
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
        url: 'build/wifiVideo/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//查询全部名称
export function selectNameList(){
    return request({
        url: 'build/wifiVideo/selectNameList',
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//子表---开始

//保存子表
export function saveDetail(data){
    return request({
        url: 'build/wifiVideoDetail/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//修改子表
export function editDetail(data){
    return request({
        url: 'build/wifiVideoDetail/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}


//根据主表id查询子表数据
export function wifiVideoDetailList(data) {
    return request({
        url: 'build/wifiVideoDetail/list/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
  }

//子表 根据id查询信息
  export function selectDetailById(data){
    return request({
        url: 'build/wifiVideoDetail/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}
//子表 根据id查询信息
export function selectByBithIdToFileId(data){
  return request({
    url: 'build/wifiVideoDetail/selectByBithIdToFileId/'+data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
  //查询课程库列表（可根据工种筛选）
export function getLibraryListByWorkType (data) {
    return request({
        url: 'build/wifiCourseLibrary/getLibraryList',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}
