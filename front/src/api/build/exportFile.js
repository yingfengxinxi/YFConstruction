import request from '@/utils/request'

export function exportFile (data) {
    return request({
        url: 'build/ApiFile/export',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        responseType: 'blob'
    })
}