/**
 * zy 2021-04-28
 */
import request from '@/utils/request'

//查询模板all，用于下拉框
export function selectModels(){
  return request({
    url: 'build/progressModelset/selectModels',
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据模板ID查询详细表的根节点 
export function queryByMap(data) {
  return request({
    url: 'build/progressModelsetDetails/selectByMap',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//保存模板
export function saveModel(model) {
  return request({
    url: 'build/progressModelset/save',
    method: 'post',
    data: model,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//保存 任务===》》详情信息
export function saveMenu(menu) {
  return request({
    url: 'build/progressModelsetDetails/save',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据id获取详细任务
export function queryById(data) {
  return request({
    url: 'build/progressModelsetDetails/selectById/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//根据id修改详细任务
export function updateMenu(menu) {
  return request({
    url: 'build/progressModelsetDetails/updateById',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据pid查询子节点
export function queryByParentId(data) {
  return request({
    url: 'build/progressModelsetDetails/selectByPid/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//查询树节点
export function selectTree(data) {
  return request({
    url: 'build/progressModelsetDetails/selectTree/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据mid查询全部id和name
export function selectByMidAll(data){
  return request({
    url: 'build/progressModelsetDetails/selectByMidAll/' + data,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//删除,根据父节点删除all子节点
export function deleteMenu(id) {
  return request({
    url: 'build/progressModelsetDetails/delById/'+id,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//删除模板
export function deleteModel(id) {
  return request({
    url: 'build/progressModelset/delById/'+id,
    method: 'post',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//根据模板ID查询详细表 ---计划生成使用
export function queryMapByMap(data) {
  return request({
    url: 'build/progressModelsetDetails/selectMapByMap',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}