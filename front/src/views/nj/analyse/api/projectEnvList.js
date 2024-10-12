import request from '@/utils/request'
//获取环境总览数据
export function selectOverViewList(data) {
  return request({
    url: 'build/projectAnalysis/overView',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
