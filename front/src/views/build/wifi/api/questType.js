/**
 * zy 2021-05-25
 */
import request from '@/utils/request'

//根据模板ID查询详细表的根节点 
export function queryChildrenByMap(data) {
    return request({
      url: 'build/wifiQuestType/queryChildrenByMap',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }

  
//删除,根据父节点删除all子节点
export function deleteMenu(id) {
    return request({
      url: 'build/wifiQuestType/deleteMenu/'+id,
      method: 'post',
      data: '',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }

export function saveMenu(data){
    return request({
        url: 'build/wifiQuestType/save',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//根据id查询详情
export function queryById(id){
    return request({
        url: 'build/wifiQuestType/queryById/'+id,
        method: 'post',
        data: '',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//修改
export function updateMenu(data){
    return request({
        url: 'build/wifiQuestType/updateById',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

//查询树--基础问题管理使用
export function selectTree(){
  return request({
    url: 'build/wifiQuestType/selectTree',
    method: 'post',
    data: '',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//基础问题管理使用
export function selectQuestionIdAndName(){
  return request({
    url: 'build/wifiQuestType/selectQuestionIdAndName',
    method: 'post',
    data: '',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//试题类型
export function typeIdFormatter(value, datas) {

  var actions = []

  Object.keys(datas).some((key) => {
      if (datas[key].id == ('' + value)) {
      actions.push(datas[key].name)
      return true
      }
  })
  return actions.join('')
}