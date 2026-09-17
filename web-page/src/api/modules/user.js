import request from '@/utils/request'
import { API } from '../endpoints'

export const getUserInfo = (id) => request.get(API.users.detail(id))
export const updateUserInfo = (id, data) => request.put(API.users.update(id), data)
export const getAllUsers = () => request.get(API.users.all)
export const deleteUser = (id) => request.delete(API.users.delete(id))
