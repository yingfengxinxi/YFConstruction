import request from '@/utils/request'

/**
 * 列表接口
 * @param {*} data
 *
 */
export function getList(data) {
  return request({
    url: 'build/envLevel/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}


// 保存质量检验数据
export function saveOrUpdate(data) {
  return request({
    url: 'build/envLevel/saveOrUpdate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// // 根据id查询质量检查详情
// export function getById(checkId) {
//   return request({
//     url: 'envLevel/getById/'+checkId,
//     method: 'post'
//   })
// }

// 删除接口
export function  delTrueByIds(data) {
  return request({
    url: 'build/envLevel/delTrueByIds',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

