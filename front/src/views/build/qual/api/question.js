/**
 * wudw 2020-11-12
 */
import request from '@/utils/request'

export function queryById(data) {
  return request({
    url: 'build/qualQuestions/getById/' + data,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function queryByParentId(data) {
  return request({
    url: 'build/qualQuestions/queryByParentId/' + data,
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
    url: 'build/qualQuestions/saveOrUpdate',
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
    url: 'build/qualQuestions/saveOrUpdate',
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
    url: 'build/qualQuestions/del/'+menuId,
    method: 'get',
    data: '',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//查询质量问题库所有数据
export function queryQuestionsList() {
  return request({
    url: 'build/qualQuestions/queryTextList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//查询质量问题库所有数据
export function queryQuestionsTreeList() {
  return request({
    url: 'build/qualQuestions/queryTreeList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 用户导入
 * @param {*} data
 */
export function excelImportAPI(data) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: 'build/qualQuestions/excelImport',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}