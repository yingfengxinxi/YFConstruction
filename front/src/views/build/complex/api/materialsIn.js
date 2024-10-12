/**
 * zy 2021-06-16
 * 施工综合--材料进场
 */
import request from '@/utils/request'

//查询列表
export function getList(data){
    return request({
        url: 'build/complexMaterialsIn/list',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//新增
export function saveMaterialsIn(data){
    return request({
        url: 'build/complexMaterialsIn/save',
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
        url: 'build/complexMaterialsIn/updateById',
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
        url: 'build/complexMaterialsIn/delById/'+data,
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
        url: 'build/complexMaterialsIn/selectById/'+data,
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}

//查询进场物料的id和code
export function selectIdAndCodeLists(){
    return request({
        url: 'build/complexMaterialsIn/selectIdAndCodeLists',
        method: 'post',
        data: '',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
      })
}



