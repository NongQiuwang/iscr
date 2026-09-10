import request from '@/utils/request'

/**
 * 获取所有汽车列表
 */
export function getAllCars() {
    return request({
        url: '/api/cars/all',
        method: 'get'
    })
}

/**
 * 根据条件获取汽车列表
 */
export function getCarList(params) {
    return request({
        url: '/api/cars/list',
        method: 'get',
        params
    })
}

/**
 * 根据ID获取汽车详情
 */
export function getCarById(id) {
    return request({
        url: `/api/cars/${id}`,
        method: 'get'
    })
}

/**
 * 新增汽车
 */
export function addCar(data) {
    return request({
        url: '/api/cars',
        method: 'post',
        data
    })
}

/**
 * 更新汽车信息
 */
export function updateCar(id, data) {
    return request({
        url: `/api/cars/${id}`,
        method: 'put',
        data
    })
}

/**
 * 删除汽车
 */
export function deleteCar(id) {
    return request({
        url: `/api/cars/${id}`,
        method: 'delete'
    })
}

/**
 * 批量删除汽车
 */
export function deleteCarBatch(ids) {
    return request({
        url: '/api/cars/batch',
        method: 'delete',
        data: ids
    })
}

/**
 * 上传汽车图片
 */
export function uploadCarImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/api/upload/car-image',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}
