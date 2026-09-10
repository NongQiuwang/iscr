import request from '@/utils/request'

/**
 * 获取所有预约列表
 */
export function getAllReservations() {
    return request({
        url: '/api/reservations/all',
        method: 'get'
    })
}

/**
 * 根据条件获取预约列表
 */
export function getReservationList(params) {
    return request({
        url: '/api/reservations/list',
        method: 'get',
        params
    })
}

/**
 * 根据用户ID获取预约列表
 */
export function getReservationsByUserId(userId) {
    return request({
        url: `/api/reservations/user/${userId}`,
        method: 'get'
    })
}

/**
 * 根据ID获取预约详情
 */
export function getReservationById(id) {
    return request({
        url: `/api/reservations/${id}`,
        method: 'get'
    })
}

/**
 * 创建预约
 */
export function createReservation(data) {
    return request({
        url: '/api/reservations',
        method: 'post',
        data
    })
}

/**
 * 更新预约信息
 */
export function updateReservation(id, data) {
    return request({
        url: `/api/reservations/${id}`,
        method: 'put',
        data
    })
}

/**
 * 更新预约状态
 */
export function updateReservationStatus(id, status) {
    return request({
        url: `/api/reservations/${id}/status`,
        method: 'patch',
        params: { status }
    })
}

/**
 * 取消预约
 */
export function cancelReservation(id) {
    return request({
        url: `/api/reservations/${id}`,
        method: 'delete'
    })
}
