import request from '@/utils/request'
import { API } from '../endpoints'

export const getAllCars = () => request.get(API.cars.all)
export const getCarList = (params) => request.get(API.cars.list, { params })
export const getCarById = (id) => request.get(API.cars.detail(id))
export const addCar = (data) => request.post(API.cars.create, data)
export const updateCar = (id, data) => request.put(API.cars.update(id), data)
export const deleteCar = (id) => request.delete(API.cars.delete(id))
export const deleteCarBatch = (ids) => request.delete(API.cars.batchDelete, { data: ids })
export const uploadCarImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(API.upload.carImage, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
