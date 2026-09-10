import request from '@/utils/request'

export function getUserInfo(id) {
    return request({
        url: `/api/users/${id}`,
        method: 'get'
    })
}

export function updateUserInfo(id, data) {
    return request({
        url: `/api/users/${id}`,
        method: 'put',
        data
    })
}

export function getAllUsers() {
    return request({
        url: '/api/users/all',
        method: 'get'
    })
}

export function deleteUser(id) {
    return request({
        url: `/api/users/${id}`,
        method: 'delete'
    })
}
