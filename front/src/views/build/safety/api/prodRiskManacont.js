/**
 * wudw 2020-11-12
 */
import request from '@/utils/request'

export function queryById(data) {
  return request({
    url: 'build/safetyProdRiskManacont/getById/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function queryByParentId(data) {
  return request({
    url: 'build/safetyProdRiskManacont/queryByParentId/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//保存菜单
export function saveMenu(menu) {
  return request({
    url: 'build/safetyProdRiskManacont/saveOrUpdate',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//保存菜单
export function updateMenu(menu) {
  return request({
    url: 'build/safetyProdRiskManacont/saveOrUpdate',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//删除
export function deleteMenu(menuId) {
  return request({
    url: 'build/safetyProdRiskManacont/del/'+menuId,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

