import request from '@/utils/request'
// 监管端-项目隐患数量统计
export function selectProDangerNumber() {
  return request({
    url: 'build/safetyRiskDetection/selectProDangerNumber',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端-安全隐患级别占比
export function selectProDangerLevelProportion() {
  return request({
    url: 'build/safetyRiskDetection/selectProDangerLevelProportion',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 安全隐患类型占比
export function selectProDangerTypeProportion() {
  return request({
    url: 'build/safetyRiskDetection/selectProDangerTypeProportion',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 安全隐患类型占比
export function selectProDangerRectifyEfficiency() {
  return request({
    url: 'build/safetyRiskDetection/selectProDangerRectifyEfficiency',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 近30天隐患整改数据
export function selectTrendAnalyses(type) {
  return request({
    url: 'build/safetyRiskDetection/selectTrendAnalyses/' + type,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 安全隐患排名
export function selectProDangerRank() {
  return request({
    url: 'build/safetyRiskDetection/selectProDangerRank',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 安全隐患数量占比分析
export function selectDangerNumberProportion() {
  return request({
    url: 'build/safetyRiskDetection/selectDangerNumberProportion',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 监管端使用 整改时效分析
export function selectProAgingAnalysis() {
  return request({
    url: 'build/safetyRiskDetection/selectProAgingAnalysis',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}