import request from '@/utils/request'
/**
 * 统计 饼状图、柱状图
 * @param {*} data 
 */

/**
 * 饼状图 数据展示
 * @param {*} data 
 */
export function initPieList(data){
  let map = {tableName:'b_qual_monit_stb_zb'}
  if(data == '3'){
    map.tableName = 'b_qual_monit_jtb_zb'
  }

  return request({
      url: 'buildQualMonitTbZb/selectByOutRangeStaticsAll',
      method: 'post',
      data: map,
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      }
  })
}

/**
 * 柱状图统计，下拉框code选择，展示数值信息
 */
export function selectCodeAndIdList(data) {
  let map = {tableName:'b_qual_monit_stb_zhb'}
  if(data == '3'){
    map.tableName = 'b_qual_monit_jtb_zhb'
  } 
  
  return request({
    url: 'buildQualMonitTbZhb/selectCodeAndId',
    method: 'post',
    data: map,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 获取柱数值信息及X轴
 * @param {*} data 
 */
export function initBarList(data){
  let map = {tableName:'b_qual_monit_stb_zb',tableNameZH:'b_qual_monit_stb_zhb'}
  if(data == '3'){
    map.tableName = 'b_qual_monit_jtb_zb'
    map.tableNameZH = 'b_qual_monit_jtb_zhb'
  }

  return  request({
    url: 'buildQualMonitTbZb/selectByOutRangeStaticsSelect',
    method: 'post',
    data: map,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
  })

}