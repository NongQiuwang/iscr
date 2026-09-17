import request from '@/utils/request'
import { API } from '../endpoints'

export const getAllReservations = () => request.get(API.reservations.all)
export const getReservationList = (params) => request.get(API.reservations.list, { params })
export const getReservationsByUserId = (userId) => request.get(API.reservations.byUser(userId))
export const getReservationById = (id) => request.get(API.reservations.detail(id))
export const createReservation = (data) => request.post(API.reservations.create, data)
export const updateReservation = (id, data) => request.put(API.reservations.update(id), data)
export const updateReservationStatus = (id, status) => request.patch(API.reservations.updateStatus(id), null, { params: { status } })
export const cancelReservation = (id) => request.delete(API.reservations.cancel(id))
