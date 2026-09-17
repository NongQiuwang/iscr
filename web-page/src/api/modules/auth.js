import request from '@/utils/request'
import { API } from '../endpoints'

export const login = (data) => request.post(API.auth.login, data)
export const register = (data) => request.post(API.auth.register, data)
export const logout = () => request.post(API.auth.logout)
