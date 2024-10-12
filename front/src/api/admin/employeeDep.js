import request from '@/utils/request'

export function depDeleteAPI(data) {
  return request({
    url: 'adminDept/deleteDept/' + data.id,
    method: 'post'
  })
}

export function depEditAPI(data) {
  return request({
    url: 'adminDept/setDept',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function depSaveAPI(data) {
  return request({
    url: 'adminDept/addDept',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function userEditAPI(params) {
  return request({
    url: 'adminUser/setUser',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function userAddAPI(params) {
  return request({
    url: 'adminUser/addUser',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function userDelAPI(userId) {
  return request({
    url: `adminUser/delUser/${userId}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 角色列表
export function roleListAPI(data) {
  return request({
    url: 'adminRole/getAllRoleList',
    method: 'post',
    data: data
  })
}

/**
 * 批量修改密码接口
 * @param {*} data
 * password
 * id 用户数组
 */
export function adminUsersUpdatePwdAPI(data) {
  return request({
    url: 'adminUser/resetPassword',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 编辑登录名
 * @param {*} data
 * username
 * password
 * id
 */
export function adminUsersUsernameEditAPI(data) {
  return request({
    url: 'adminUser/usernameEdit',
    method: 'post',
    data: data
  })
}

/**
 * 编辑登录名
 * @param {*} data
 * username
 * password
 * id
 * 可修改管理员
 */
export function adminUsersManagerUsernameEditAPI(data) {
  return request({
    url: 'adminUser/usernameEditByManager',
    method: 'post',
    data: data
  })
}

/**
 * 用户状态修改
 * @param {*} data
 */
export function usersEditStatusAPI(data) {
  return request({
    url: 'adminUser/setUserStatus',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 用户导入模板
 * @param {*} data
 */
export const userImportTemplateURL = 'https://www.72crm.com/npm/static/user_import.xlsx'
export function userImportTemplateAPI(data) {
  return request({
    url: 'user_import.xlsx',
    method: 'post',
    data: data
  })
}

/**
 * 用户导入
 * @param {*} data
 */
export function userExcelImportAPI(data) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: 'adminUser/excelImport',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}

/**
 * 下载用户导入错误数据
 * @param {*} data
 *
 */
export function userErrorExcelDownAPI(data) {
  return request({
    url: 'adminUser/downExcel',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

/**
 * 用户导入
 * @param {*} data
 */
export function attendanceExcelImportAPI(data) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: 'build/bLabourAttendanceRecord/excelImport',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}

//授权项目列表
export function getBindProjectList (data) {
  return request({
    url: 'build/projectUser/queryProPageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//获取所有已授权项目的ids
export function getAllBindProIds (data) {
  return request({
    url: 'build/projectUser/queryProListAll',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
//查询所有项目列表
export function getAllProList (data) {
  return request({
    url: 'build/projectInfo/queryPageListAll',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 保存授权用户
export function saveBindProjects (data) {
  return request({
    url: 'build/projectUser/saveBindProjects',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


//删除授权用户
export function delUsers (data) {
  return request({
    url: 'build/projectUser/delUser/' + data,
    method: 'post',
    data: {},
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}