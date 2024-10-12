import request from '@/utils/request'

/**
 * 部门
 * @param {*} data
 */
export function depListAPI() {
  return request({
    url: 'adminTenant/queryDeptList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function userListAPI(data) {
  return request({
    url: 'adminTenant/queryUserList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 获取全部菜单
 * @param {*} roleType
 */
export function systemRuleByTypeAPI(tenantId) {
  return request({
    url: `adminTenant/getMenuList/${tenantId}`,
    method: 'post'
  })
}

/**
 * 获取租户权限
 * @param {*} deptId
 */
export function systemRoleByTypeAPI() {
  return request({
    url: `adminTenant/getRoleByType`,
    method: 'post'
  })
}

export function updateTenantMenuAPI(data) {
  return request({
    url: 'adminTenant/updateTenantMenu',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}



// export function roleListFun(data) {
//   return request({
//     url: 'adminRole/getAllRoleList',
//     method: 'post',
//     data: data
//   })
// }

// export function rulesList(data) {
//   return request({
//     url: 'adminMenu/getAllMenuList',
//     method: 'post',
//     data: data
//   })
// }

export function roleAddAPI(data) {
  return request({
    url: 'adminRole/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function roleDeleteAPI(data) {
  return request({
    url: 'adminRole/delete',
    method: 'post',
    data: data
  })
}

export function roleUpdateAPI(data) {
  return request({
    url: 'adminRole/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


/**
 * 角色复制
 * @param {*} data
 */
export function roleCopyAPI(data) {
  return request({
    url: 'adminRole/copy',
    method: 'post',
    data: data
  })
}

/**
 * 添加编辑员工
 * @param {*} data
 */
export function relatedUserAPI(data) {
  return request({
    url: 'adminRole/relatedUser',
    method: 'post',
    data: data
  })
}

/**
 * 删除员工
 * @param {*} data
 */
export function unbindingUserAPI(data) {
  return request({
    url: 'adminRole/unbindingUser',
    method: 'post',
    data: data
  })
}

/**
 * 角色分类列表
 * @param {*} data
 */
export function adminGroupsTypeListAPI(data) {
  return request({
    url: 'adminRole/getRoleTypeList',
    method: 'post',
    data: data
  })
}

/**
 * 查询字段授权设置
 * @param {*} data
 */
export function systemRoleGetFieldAPI(data) {
  return request({
    url: 'crmField/queryRoleField',
    method: 'post',
    data: data
  })
}

/**
 * 模块设置字段授权
 * @param {*} data
 */
export function systemRoleSetFieldAPI(data) {
  return request({
    url: 'crmField/saveRoleField',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: data
  })
}


/**
 *  对接配置管理
 */
export function queryConfigList(data) {
  return request({
    url: 'adminDeptConfig/queryConfigList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getInfo(id) {
  return request({
    url: 'adminDeptConfig/getInfo/' + id,
    method: 'get'
  })
}
export function addOrUpdateConfig(data) {
  return request({
    url: 'adminDeptConfig/addOrUpdateConfig',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function delConfig(data) {
  return request({
    url: 'adminDeptConfig/delConfig',
      method: 'post',
      data: data,
      headers: {
    'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function tenantAdminAddAPI(params) {
  return request({
    url: 'adminTenant/addTenantAdmin',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function tenantAddAPI(data) {
  return request({
    url: 'adminTenant/addTenant',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
